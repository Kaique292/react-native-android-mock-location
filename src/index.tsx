import { NativeModules, Platform } from 'react-native';

type Location = {
    latitude: number;
    longitude: number;
}

type IOptionsProver = {
    altitude?: number
    speed?: number
    bearing?: number
    accuracy?: number
    delay?: number
}

type AndroidMockLocationType = {
    setTestProviderLocation(latitude: number, longitude: number, delay?:number, accuracy?:number, altitude?:number, bearing?:number, speed?:number): void;
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
    options = {
        delay: 0,
        accuracy: 1,
        altitude: 3,
        bearing: 0,
        speed: 0.01
    }
}: { location: Location, options?: IOptionsProver }) {
    const { delay, accuracy, altitude, bearing, speed } = options;
    return AndroidMockLocation.setTestProviderLocation(location.latitude, location.longitude, delay, accuracy, altitude, bearing, speed);
}

function stopMockLocation() {
    return AndroidMockLocation.stopMockLocation();
}

export {
    setMockLocation,
    stopMockLocation
}