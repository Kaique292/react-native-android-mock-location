package com.androidmocklocation;

import androidx.annotation.NonNull;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

import android.location.Location;
import android.location.LocationManager;
import android.location.Criteria;
import android.location.LocationListener;
import android.location.LocationProvider;
import android.util.Log;

import android.app.Activity;
import android.Manifest;
import androidx.core.app.ActivityCompat;
import android.os.HandlerThread;
import android.os.Process;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.os.Handler;
import android.os.SystemClock;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.WritableNativeMap;
import com.facebook.react.module.annotations.ReactModule; 

@ReactModule(name = AndroidMockLocationModule.NAME)
public class AndroidMockLocationModule extends ReactContextBaseJavaModule {
    public static final String NAME = "AndroidMockLocation";

    int updateDelay = 100;
    int accuracy = Criteria.ACCURACY_FINE;
    Float altitude = 3F;
    Float bearing = 0.1F;
    Float speed = 0.01F;

    Context ctx;

    static Double lat;
    static Double lng;
    private static AndroidMockLocationModule mockNetwork;
    private static AndroidMockLocationModule mockGps; 
    private static AndroidMockLocationModule myLocationManager; 
    private HandlerThread mLocHandlerThread;
    private Handler mLocHandler;
    private boolean isStop = false; 
    private static final int HANDLER_MSG_ID = 0;

    public AndroidMockLocationModule(ReactApplicationContext reactContext) {
        super(reactContext);

        this.ctx = getReactApplicationContext();
 
        LocationManager lm = (LocationManager) ctx.getSystemService(Context.LOCATION_SERVICE);
        startup(lm, Criteria.POWER_MEDIUM, Criteria.ACCURACY_FINE, /* maxRetryCount= */ 3, /* currentRetryCount= */ 0); 
    }

    @Override
    @NonNull
    public String getName() {
        return NAME;
    }

    private void startup(LocationManager lm, int powerUsage, int accuracy, int maxRetryCount, int currentRetryCount) {
        if (currentRetryCount < maxRetryCount) {
            try {
                stopMockLocation();

                lm.addTestProvider(LocationManager.NETWORK_PROVIDER, false, false, false, false, false, true, true, powerUsage, accuracy);
                lm.addTestProvider(LocationManager.GPS_PROVIDER, false, false, false, false, false, true, true, Criteria.POWER_LOW, accuracy);

                lm.setTestProviderEnabled(LocationManager.NETWORK_PROVIDER, true); 
                lm.setTestProviderEnabled(LocationManager.GPS_PROVIDER, true); 
            } catch (Exception e) {
                startup(lm, powerUsage, accuracy, maxRetryCount, (currentRetryCount + 1));
            }
        } else {
            throw new SecurityException("Not allowed to perform MOCK_LOCATION");
        }
    }

    /**
     * Pushes the location in the system (mock). This is where the magic gets done.
     *
     * @param lat latitude
     * @param lon longitude
     * @return Void
     */
    public void pushLocation(double lat, double lon, String providerType) {  
        LocationManager lm = (LocationManager) ctx.getSystemService(Context.LOCATION_SERVICE);

        Location providerLocation = new Location(providerType); 

        providerLocation.setLatitude(lat);
        providerLocation.setLongitude(lon);
        providerLocation.setAltitude(altitude);
        providerLocation.setTime(System.currentTimeMillis());
        providerLocation.setSpeed(speed);
        providerLocation.setBearing(bearing);
        providerLocation.setAccuracy(accuracy); 

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            providerLocation.setBearingAccuracyDegrees(0.1F);
            providerLocation.setVerticalAccuracyMeters(0.1F);
            providerLocation.setSpeedAccuracyMetersPerSecond(0.01F); 
        } 

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            providerLocation.setElapsedRealtimeNanos(SystemClock.elapsedRealtimeNanos()); 
        }
         
        lm.setTestProviderLocation(providerType, providerLocation); 
    }

    public void applyLocation(Double latitude, Double longitude) {
        lat = latitude;
        lng = longitude; 

        stopMockLocation(); 

        try {  
            myLocationManager = new AndroidMockLocationModule(getReactApplicationContext());
        } catch (SecurityException e) {
            e.printStackTrace();
            stopMockLocation();
            return;
        } 

        initGoLocation(lat, lng);
        exec(lat, lng);
    }

     private void initGoLocation(double lat, double lng) { 
        mLocHandlerThread = new HandlerThread("BlackBayGo", Process.THREAD_PRIORITY_FOREGROUND);
 
        mLocHandlerThread.start(); 
        isStop = false;

        mLocHandler = new Handler(mLocHandlerThread.getLooper()) { 
            @Override
            public void handleMessage(@NonNull Message msg) {
                try {
                    Thread.sleep(updateDelay); 

                    if(!isStop) {
                        myLocationManager.pushLocation(lat, lng, LocationManager.NETWORK_PROVIDER);
                        myLocationManager.pushLocation(lat, lng, LocationManager.GPS_PROVIDER);
                            
                        sendEmptyMessage(HANDLER_MSG_ID); 
                    }

                } catch (InterruptedException e) {
                    e.printStackTrace(); 
                    Thread.currentThread().interrupt();
                }
            }
        };

        mLocHandler.sendEmptyMessage(HANDLER_MSG_ID);
    }

    /**
     * Set a mocked location.
     *
     * @param lat latitude
     * @param lng longitude
     */
    static void exec(double lat, double lng) {
        try {
            myLocationManager.pushLocation(lat, lng, LocationManager.NETWORK_PROVIDER);
            myLocationManager.pushLocation(lat, lng, LocationManager.GPS_PROVIDER);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
    }

    @ReactMethod
    public void setTestProviderLocation(Double latitude, Double longitude, int delay, int accuracy, Float altitude, Float bearing, Float speed) {
        updateDelay = delay;
        accuracy = accuracy;
        altitude = altitude;
        bearing = bearing;
        speed = speed;

        applyLocation(latitude,longitude);
    }

    @ReactMethod
    public void stopMockLocation() {
        try {
            isStop = true;
            mLocHandler.removeMessages(HANDLER_MSG_ID);   
            mLocHandlerThread.quit(); 
             
            LocationManager lm = (LocationManager) ctx.getSystemService(Context.LOCATION_SERVICE);
           
            lm.removeTestProvider(LocationManager.NETWORK_PROVIDER);
            lm.removeTestProvider(LocationManager.GPS_PROVIDER); 
            
        } catch (Exception e) { 
            e.printStackTrace();
        }
    } 
}
