# react-native-android-mock-location

I BUILT THIS FOR USE ON EXPO, BUT DONT HAVE SUPPORT FOR REACT NATIVE PURE NATIVE MODULE.
SO I REBUILT USING EXPO NATIVE MODULES
THKS, HAVE FREE TO USE THIS FOR YOUR OWN REACT NATIVE PURE PROJECT

Allow to set mock location to your mobile provider seen

## Installation

```sh
npm install react-native-android-mock-location
```
# Requirements

- You need to request user permission to use location first

    Add Permissions:
- android.permission.ACCESS_MOCK_LOCATION
- android.permission.ACCESS_COARSE_LOCATION
- android.permission.ACCESS_FINE_LOCATION

## Usage

> HOW TO SET MOCK LOCATION

- If you are trying to mock location, and check with google maps. Maybe you may find some glitch, wich envolve your mock location getting override
by your real location after few seconds. To solve this, you have to disable accuray location in settings of your phone.

```tsx
import {
    stopMockLocation,
    setMockLocation
} from 'react-native-android-mock-location';

type TLocation = {
    latitude: number,
    longitude: number
}
interface IMockLocation {
    location: TLocation
    delay?: number
}

const location = {
    latitude: 222.44,
    longitude: 123.23
}

setMockLocation({
    location: location
});

// or

setMockLocation({
    location: location,
    delay: 120 //default is 100ms
});
```

> STOP MOCK LOCATION
-  This function will stop mock location

```tsx
import { stopMockLocation } from 'react-native-android-mock-location';

stopMockLocation();
 
```

## Contributing

See the [contributing guide](CONTRIBUTING.md) to learn how to contribute to the repository and the development workflow.

## License

MIT

---

Made with [create-react-native-library](https://github.com/callstack/react-native-builder-bob)
