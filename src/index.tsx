import { NativeModules, Platform } from 'react-native';

type Location = {
    latitude: number;
    longitude: number;
}

type AndroidMockLocationType = {
    setTestProviderLocation(latitude: number, longitude: number, delay: number): void;
    stopMockLocation(): void;
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

function setMockLocation({
    location,
    delay = 50
}: { location: Location, delay?: number }) {
    return AndroidMockLocation.setTestProviderLocation(location.latitude, location.longitude, delay);
}

function stopMockLocation() {
    return AndroidMockLocation.stopMockLocation();
}

export {
    setMockLocation,
    stopMockLocation
}