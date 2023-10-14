import * as React from 'react';
import {
    stopMockLocation,
    setMockLocation
} from 'react-native-android-mock-location';

import locations from '../../../mock/locations/locations';
import type { ILocation } from '../../../mock/locations/model';

const useLocation = () => {
    const [currentIndex, setCurrentIndex] = React.useState<number>(0);
    const [location, setCurrentLocation] = React.useState<ILocation | null>(null);

    function getLocationById(index: number) {
        return locations.find((location) => location.id === index) as ILocation
    }

    function setLocation() {
        let newIndex = (currentIndex ?? 0) + 1
        if (newIndex > locations.length - 1) {
            newIndex = 0
        }

        setCurrentIndex(newIndex)
        setCurrentLocation(getLocationById(newIndex))
    }

    function getLocationName() {
        return location ? location.name : 'Location not set'
    }

    function clearLocation() {
        console.log('Clearing location')
        setCurrentLocation(null)
        setCurrentIndex(0)
        stopMockLocation()
    }

    React.useEffect(() => {
        if (location) {
            setMockLocation({
                location: location
            });
            return
        }

        return () => stopMockLocation()
    }, [location])

    return {
        clearLocation,
        setLocation,
        location,
        getLocationName
    }
}
export default useLocation