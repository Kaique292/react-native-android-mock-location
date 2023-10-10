import { NativeModules, Platform } from 'react-native';

type AndroidMockLocationType = {
    multiply(a: number, b: number): Promise<number>
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



function multiply(a: number, b: number): Promise<number> {
    return AndroidMockLocation.multiply(a, b);
}

export {
    multiply
}