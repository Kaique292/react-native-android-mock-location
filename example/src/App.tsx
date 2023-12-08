import * as React from 'react';

import {
    StyleSheet,
    View,
    Text,
    TouchableHighlight,
    PermissionsAndroid
} from 'react-native';

import useLocation from './shared/components/hooks/useLocation/useLocation';
import MapView, { PROVIDER_GOOGLE, Marker } from 'react-native-maps';

const requestCameraPermission = async () => {
    try {
      const granted = await PermissionsAndroid.request(
         'android.permission.ACCESS_FINE_LOCATION',
        {
          title: 'Mock Location',
          message:
            'Mock Location App needs access to your location ',
          buttonNeutral: 'Ask Me Later',
          buttonNegative: 'Cancel',
          buttonPositive: 'OK',
        },
      );
      if (granted === PermissionsAndroid.RESULTS.GRANTED) {
        console.log('You can use user location');
      }  
    } catch (err) {
      console.warn(err);
    }
  };

export default function App() {
    const { setLocation, clearLocation, location } = useLocation();
    const [marker, setMarker] = React.useState<{ latitude: number, longitude: number } | null>(null)

    const onMarkerPin = (loc: { latitude: number, longitude: number }) => {
        setMarker(loc)
    }

    React.useEffect(() => {
        requestCameraPermission()
    }, [])
    
    return (
        <View style={styles.container}>
            <MapView
                provider={PROVIDER_GOOGLE}
                region={{
                    latitude: location?.latitude ?? 47.072277703398264,
                    longitude: location?.longitude ?? 82.09683660417795,
                    latitudeDelta: 0.005,
                    longitudeDelta: 0.005
                }}

                rotateEnabled={true}
                toolbarEnabled={true}
                showsMyLocationButton={true}
                showsUserLocation={true}
                mapType='standard'
                showsCompass={true}
                showsTraffic={true}

                onPress={(e) => onMarkerPin(e.nativeEvent.coordinate)}
                style={{
                    display: 'flex',
                    flex: 1
                }}
            >

                {marker &&
                    <Marker
                        coordinate={{
                            latitude: marker.latitude,
                            longitude: marker.longitude
                        }}
                    />
                }
            </MapView>


            <View style={styles.actionButtons}>
                <TouchableHighlight
                    style={styles.mockButton}
                    onPress={clearLocation}
                    underlayColor='rgba(127, 0, 255,0.55)'
                >
                    <Text style={styles.mockText}>Clear</Text>
                </TouchableHighlight>

                <TouchableHighlight
                    style={styles.mockButton}
                    onPress={() => {
                        if (marker) {
                            setLocation(marker)
                        }
                    }}
                    underlayColor='rgba(127, 0, 255,0.55)'
                >
                    <Text style={styles.mockText}>Mockar</Text>
                </TouchableHighlight>
            </View>
        </View>
    );
}

const styles = StyleSheet.create({
    container: {
        flex: 1,
        position: 'relative'
    },
    mockButton: {
        backgroundColor: '#7F00FF',
        paddingVertical: 5,
        paddingHorizontal: 10,
        borderRadius: 5
    },
    mockText: {
        color: 'white',
        fontWeight: '600'
    },
    actionButtons: {
        position: 'absolute',
        display: 'flex',
        flexDirection: 'row-reverse',
        alignSelf: 'center',
        gap: 10,
        bottom: 10
    },
});
