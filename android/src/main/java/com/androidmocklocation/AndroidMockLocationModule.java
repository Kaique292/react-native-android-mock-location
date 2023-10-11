package com.androidmocklocation;

import androidx.annotation.NonNull;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;

import android.app.Activity;
import android.Manifest;
import android.content.pm.PackageManager;
import androidx.core.app.ActivityCompat;
import android.os.SystemClock;
import android.os.Bundle;
import android.os.Handler;

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
    private static LocationManager locationManager;
    private static Criteria criteria;
    private static String provider;

    public AndroidMockLocationModule(ReactApplicationContext reactContext) {
        super(reactContext);
    }

    @Override
    @NonNull
    public String getName() {
        return NAME;
    }

    @ReactMethod
    public void setTestProviderLocation(String useProvider, Double altitude, Double latitude, Double longitude) {
        this.locationManager = (LocationManager) getReactApplicationContext()
                .getSystemService(Context.LOCATION_SERVICE);

        this.criteria = new Criteria();
        this.criteria.setAccuracy(Criteria.ACCURACY_FINE);
        this.criteria.setPowerRequirement(Criteria.POWER_HIGH);

        if (useProvider == "gps") {
            this.provider = LocationManager.GPS_PROVIDER;
        } else {
            this.provider = LocationManager.NETWORK_PROVIDER;
        }

        if (this.provider != null) {
            this.locationManager.addTestProvider(this.provider, false, false, false, false, true, true, true,
                    Criteria.POWER_HIGH,
                    Criteria.ACCURACY_FINE);
            this.locationManager.setTestProviderEnabled(this.provider, true);

            Location location = new Location(this.provider);

            location.setLatitude(latitude);
            location.setLongitude(longitude);
            location.setAltitude(3.0);
            location.setTime(System.currentTimeMillis());
            location.setSpeed(0.01f);
            location.setBearing(1f);
            location.setAccuracy(3f);
            location.setElapsedRealtimeNanos(SystemClock.elapsedRealtimeNanos());

            this.locationManager.setTestProviderLocation(this.provider, location);

            this.locationManager.setTestProviderStatus(this.provider, LocationProvider.AVAILABLE, null,
                    System.currentTimeMillis());
        }

    }

    @ReactMethod
    public void requestLocationPermission(Promise promise) {
        Activity currentActivity = getCurrentActivity();
        String[] permissions = { Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION };
        int requestCode = 1;

        if (currentActivity != null) {
            ActivityCompat.requestPermissions(currentActivity, permissions, requestCode);
            promise.resolve(true);
        } else {
            promise.reject("NO_ACTIVITY", "Nenhuma atividade disponível para solicitar permissões.");
        }
    }

    @ReactMethod
    public void checkLocationPermission(Promise promise) {
        String[] permissions = { Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION };
        boolean granted = true;

        for (String permission : permissions) {
            if (ActivityCompat.checkSelfPermission(getReactApplicationContext(),
                    permission) != PackageManager.PERMISSION_GRANTED) {
                granted = false;
                break;
            }
        }

        promise.resolve(granted);
    }

    @ReactMethod
    public void getMockLocation(Promise promise) {
        if (this.provider != null) {
            Location location = this.locationManager.getLastKnownLocation(this.provider);

            if (location != null) {
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();
                double altitude = location.getAltitude();

                WritableMap result = new WritableNativeMap();
                result.putDouble("latitude", latitude);
                result.putDouble("longitude", longitude);
                result.putDouble("altitude", altitude);

                promise.resolve(result);
            } else {
                promise.reject("NO_LOCATION", "Não foi possível obter a localização.");
            }
        } else {
            promise.reject("NO_PROVIDER", "Nenhum provedor de localização disponível.");
        }
    }

    @ReactMethod
    public void stopMockLocation() {
        if (this.locationManager != null) {
            if (this.provider != null) {
                this.locationManager.clearTestProviderEnabled(this.provider);
                this.locationManager.clearTestProviderLocation(this.provider);
                this.locationManager.clearTestProviderStatus(this.provider);
            }
        }

    }
}
