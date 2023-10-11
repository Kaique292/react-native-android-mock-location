import { NativeModules, Platform } from 'react-native';
 
type Location = {
  latitude: number;
  longitude: number;
}

interface LocationMock extends Location {
  altitude: number;
}

type AndroidMockLocationType = {
  setTestProviderLocation(useProvider: 'gps' | 'network', latitude: number, longitude: number): void;
  getMockLocation(): Promise<LocationMock>;
  stopMockLocation(): void;
  checkLocationPermission(): Promise<boolean>;
  requestLocationPermission(): Promise<boolean>;
}

const LINKING_ERROR =
  `The package 'react-native-android-mock-location' doesn't seem to be linked. Make sure: \n\n` +
  Platform.select({ ios: "- You have run 'pod install'\n", default: '' }) +
  '- You rebuilt the app after installing the package\n' +
  '- You are not using Expo Go\n';

const onModuleError = () => new Proxy({}, {
  get() {
    throw new Error(LINKING_ERROR);
  }
})

const AndroidMockLocation: AndroidMockLocationType = NativeModules.AndroidMockLocation ? NativeModules.AndroidMockLocation : onModuleError();

function setMockLocation(useProvider: 'gps' | 'network', location: Location) {
  return AndroidMockLocation.setTestProviderLocation(useProvider, location.latitude, location.longitude);
}

function getMockLocation() {
  return AndroidMockLocation.getMockLocation();
}

function stopMockLocation() {
  return AndroidMockLocation.stopMockLocation();
}

function checkLocationPermission() {
  return AndroidMockLocation.checkLocationPermission();
}

function requestLocationPermission() {
  return AndroidMockLocation.requestLocationPermission();
}

export {
  setMockLocation,
  getMockLocation,
  stopMockLocation,
  checkLocationPermission,
  requestLocationPermission
}