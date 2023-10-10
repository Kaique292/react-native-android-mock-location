package com.androidmocklocation;

import androidx.annotation.NonNull;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
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
 
//   public void setTestProviderLocation(String provider, double latitude, double longitude, double altitude, float accuracy, long time, float bearing, float speed, boolean mockLocationEnabled, Promise promise) {
//         try {
//             LocationManager locationManager = (LocationManager) getReactApplicationContext().getSystemService(ReactApplicationContext.LOCATION_SERVICE);
//             Location mockLocation = new Location(provider);

//             mockLocation.setLatitude(latitude);
//             mockLocation.setLongitude(longitude);
//             mockLocation.setAltitude(altitude);
//             mockLocation.setAccuracy(accuracy);
//             mockLocation.setTime(time);
//             mockLocation.setBearing(bearing);
//             mockLocation.setSpeed(speed);

//             Bundle extras = new Bundle();
//             extras.putBoolean(LocationManager.KEY_IS_MOCK, mockLocationEnabled);
//             mockLocation.setExtras(extras);

//             locationManager.setTestProviderLocation(provider, mockLocation);
//             promise.resolve(true);
//         } catch (SecurityException e) {
//             promise.reject("PERMISSION_ERROR", "Permission denied to set mock location. Make sure you have the necessary permissions.");
//         } catch (Exception e) {
//             promise.reject("ERROR", e.getMessage());
//         }
//     }
  @ReactMethod
  public void multiply(double a, double b, Promise promise) {
    promise.resolve(a * b);
  }
}
