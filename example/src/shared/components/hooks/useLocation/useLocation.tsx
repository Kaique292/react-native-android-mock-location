import * as React from 'react';
import {
    stopMockLocation,
    setMockLocation
} from 'react-native-android-mock-location';

import type { ILocation } from '../../../mock/locations/model';

const useLocation = () => {
    const [location, setCurrentLocation] = React.useState<ILocation | null>(null);


    function setLocation(cds: { longitude: number, latitude: number }) {
        setCurrentLocation({
            latitude: cds.latitude,
            longitude: cds.longitude
        })
    }

    function clearLocation() {
        console.log('Clearing location')
        stopMockLocation()
    }

    React.useEffect(() => {
        if (location) {
            setMockLocation({
                location: location,
                options: {
                    delay: 50,
                    accuracy: 1.0,
                    altitude: 10,
                    bearing: 0,
                    speed: 0.01
                }
            });
            return
        }

        return () => stopMockLocation()
    }, [location])

    return {
        clearLocation,
        setLocation,
        location
    }
}
export default useLocation