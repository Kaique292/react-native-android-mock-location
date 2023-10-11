# react-native-android-mock-location

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
-  Maybe necessary set this inside a loop function, and override your mobile provider location

```tsx
import { setTestProviderLocation } from 'react-native-android-mock-location';

type TUseProvier = 'gps' | 'newtork';
const useProvider : TUseProvier = 'gps';

type TLocation = {
    latitude: number,
    longitude: number
}
const location = {
    latitude: 222.44,
    longitude: 123.23
}

setTestProviderLocation(useProvider,location);
```

> HOW TO GET MOCK LOCATION
-  This function get currento location your provider is using, u can check with de location you have set in setTestProvider to know if is work as well

```tsx
import { getMockLocation } from 'react-native-android-mock-location';

const getLocationProviderIsUsing = await getMockLocation();

console.log(getLocationProviderIsUsing)
/* OUTPUT
{
  latitude: 123,
  longitude: 111,
  altitude: 333
} 
*/
```

> STOP MOCK LOCATION
-  This function will stop mock location

```tsx
import { stopMockLocation } from 'react-native-android-mock-location';

stopMockLocation();
 
```

> REQUEST AND CHECK PERMISSIONS LOCATION

```tsx
import { checkLocationPermission, requestLocationPermission } from 'react-native-android-mock-location';

const requestPerm = await requestLocationPermission();
const hasPerm = await checkLocationPermission();
 
```

## Contributing

See the [contributing guide](CONTRIBUTING.md) to learn how to contribute to the repository and the development workflow.

## License

MIT

---

Made with [create-react-native-library](https://github.com/callstack/react-native-builder-bob)
