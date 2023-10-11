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

    public AndroidMockLocationModule(ReactApplicationContext reactContext) {
        super(reactContext);
    }

    @Override
    @NonNull
    public String getName() {
        return NAME;
    }

    // Example method
    // See https://reactnative.dev/docs/native-modules-android
    @ReactMethod
    public void setTestProviderLocation(Boolean useGPS, Double altitude, Double latitude, Double longitude) {
        LocationManager locationManager = (LocationManager) getReactApplicationContext()
                .getSystemService(Context.LOCATION_SERVICE);

        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setPowerRequirement(Criteria.POWER_HIGH);

        String provider;
        if (useGPS) {
            provider = LocationManager.GPS_PROVIDER;
        } else {
            provider = LocationManager.NETWORK_PROVIDER;
        }

        if (provider != null) {
            locationManager.addTestProvider(provider, false, false, false, false, true, true, true, Criteria.POWER_HIGH,
                    Criteria.ACCURACY_FINE);
            locationManager.setTestProviderEnabled(provider, true);

            Location location = new Location(provider);

            location.setLatitude(latitude);
            location.setLongitude(longitude);
            location.setAltitude(3.0);
            location.setTime(System.currentTimeMillis());
            location.setSpeed(0.01f);
            location.setBearing(1f);
            location.setAccuracy(3f);
            location.setElapsedRealtimeNanos(SystemClock.elapsedRealtimeNanos());

            locationManager.setTestProviderLocation(provider, location);

            locationManager.setTestProviderStatus(provider, LocationProvider.AVAILABLE, null,
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
    public void getMockLocation(Boolean useGPS, Promise promise) {
        LocationManager locationManager = (LocationManager) getReactApplicationContext()
                .getSystemService(Context.LOCATION_SERVICE);

        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setPowerRequirement(Criteria.POWER_HIGH);
        String provider = LocationManager.NETWORK_PROVIDER;

        if (provider != null) {
            Location location = locationManager.getLastKnownLocation(provider);

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
        LocationManager locationManager = (LocationManager) getReactApplicationContext()
                .getSystemService(Context.LOCATION_SERVICE);
        String provider = locationManager.getBestProvider(new Criteria(), true);

        if (provider != null) {
            locationManager.clearTestProviderEnabled(provider);
            locationManager.clearTestProviderLocation(provider);
            locationManager.clearTestProviderStatus(provider);
        }
    }

    @ReactMethod
    public void multiply(double a, double b, Promise promise) {
        promise.resolve(a * b);
    }
}
