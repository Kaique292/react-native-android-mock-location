package com.androidmocklocation;

import androidx.annotation.NonNull;
import android.content.Context;
import android.content.pm.PackageManager;

import android.location.Location;
import android.location.LocationManager;
import android.location.Criteria;
import android.location.LocationListener;
import android.location.LocationProvider;

import android.app.Activity;
import android.Manifest;
import androidx.core.app.ActivityCompat;
import android.os.Build;
import android.os.Bundle;
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

    String providerName;
    Context ctx;

    static Double lat;
    static Double lng;
    private static AndroidMockLocationModule mockNetwork;
    private static AndroidMockLocationModule mockGps;

    public AndroidMockLocationModule(ReactApplicationContext reactContext, String providerName) {
        super(reactContext);

        this.providerName = providerName;
        this.ctx = getReactApplicationContext();

        int powerUsage = 0;
        int accuracy = 5;

        if (Build.VERSION.SDK_INT >= 30) {
            powerUsage = 1;
            accuracy = 2;
        }

        LocationManager lm = (LocationManager) ctx.getSystemService(Context.LOCATION_SERVICE);
        startup(lm, powerUsage, accuracy, /* maxRetryCount= */ 3, /* currentRetryCount= */ 0);
    }

    @Override
    @NonNull
    public String getName() {
        return NAME;
    }

    @ReactMethod
    private void startup(LocationManager lm, int powerUsage, int accuracy, int maxRetryCount, int currentRetryCount) {
        if (currentRetryCount < maxRetryCount) {
            try {
                stopMockLocation();
                lm.addTestProvider(providerName, false, false, false, false, false, true, true, powerUsage, accuracy);
                lm.setTestProviderEnabled(providerName, true);
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
    public void pushLocation(double lat, double lon) {
        LocationManager lm = (LocationManager) ctx.getSystemService(Context.LOCATION_SERVICE);

        Location mockLocation = new Location(providerName);
        mockLocation.setLatitude(lat);
        mockLocation.setLongitude(lon);
        mockLocation.setAltitude(3F);
        mockLocation.setTime(System.currentTimeMillis());
        mockLocation.setSpeed(0.01F);
        mockLocation.setBearing(1F);
        mockLocation.setAccuracy(1F);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mockLocation.setBearingAccuracyDegrees(0.1F);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mockLocation.setVerticalAccuracyMeters(0.1F);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mockLocation.setSpeedAccuracyMetersPerSecond(0.01F);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            mockLocation.setElapsedRealtimeNanos(SystemClock.elapsedRealtimeNanos());
        }
        lm.setTestProviderLocation(providerName, mockLocation);
    }

    @ReactMethod
    public void applyLocation(Double latitude, Double longitude) {
        lat = latitude;
        lng = longitude;

        try {
            mockNetwork = new AndroidMockLocationModule(getReactApplicationContext(), LocationManager.NETWORK_PROVIDER);
            mockGps = new AndroidMockLocationModule(getReactApplicationContext(), LocationManager.GPS_PROVIDER);
        } catch (SecurityException e) {
            e.printStackTrace();
            stopMockLocation();
            return;
        }

        exec(lat, lng);
    }

    /**
     * Set a mocked location.
     *
     * @param lat latitude
     * @param lng longitude
     */
    static void exec(double lat, double lng) {
        try {
            mockNetwork.pushLocation(lat, lng);
            mockGps.pushLocation(lat, lng);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
    }

    @ReactMethod
    public void setTestProviderLocation(Double latitude, Double longitude, int updateDelay) {
        applyLocation(latitude,longitude);
    }

    // @ReactMethod
    // public void requestLocationPermission(Promise promise) {
    //     Activity currentActivity = getCurrentActivity();
    //     String[] permissions = { Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION };
    //     int requestCode = 1;

    //     if (currentActivity != null) {
    //         ActivityCompat.requestPermissions(currentActivity, permissions, requestCode);
    //         promise.resolve(true);
    //     } else {
    //         promise.reject("NO_ACTIVITY", "Nenhuma atividade disponível para solicitar permissões.");
    //     }
    // }

    // @ReactMethod
    // public void checkLocationPermission(Promise promise) {
    //     String[] permissions = { Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION };
    //     boolean granted = true;

    //     for (String permission : permissions) {
    //         if (ActivityCompat.checkSelfPermission(getReactApplicationContext(),
    //                 permission) != PackageManager.PERMISSION_GRANTED) {
    //             granted = false;
    //             break;
    //         }
    //     }

    //     promise.resolve(granted);
    // }

    @ReactMethod
    public void stopMockLocation() {
        try {
            LocationManager lm = (LocationManager) ctx.getSystemService(Context.LOCATION_SERVICE);
            lm.removeTestProvider(providerName);
        } catch (Exception e) {
        }
    }
}
