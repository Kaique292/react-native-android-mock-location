import * as React from 'react';

import {
  StyleSheet,
  View,
  Text,
  TouchableHighlight,
  Switch,
  Image,
} from 'react-native';
import {
  stopMockLocation,
  setMockLocation,
  getMockLocation,
} from 'react-native-android-mock-location';
import useLocation from './shared/components/hooks/useLocation/useLocation';

const B = ({ children }: { children: React.ReactNode }) => (
  <Text style={{ fontWeight: 'bold', color: 'black' }}>{children}</Text>
);

export default function App() {
  const { setLocation, clearLocation, getLocationName, location } = useLocation();
  const [provider, setProvider] = React.useState<'gps' | 'network'>('gps')

  const [mockedLocation, setMockedLocation] = React.useState<{
    altitude: number;
    longitude: number;
    latitude: number;
  } | null>(null);

  async function clearMockLocation() {
    console.log('Cleared Mock Location');
    clearLocation();
    stopMockLocation()
  }

  async function isMocked() {
    await getMockLocation(false)
      .then((response) => {
        setMockedLocation(response);
      }).catch((err) => {
        console.log('Erro => ', err);
      })
  }


  React.useEffect(() => {
    if (location) {
      const { id, name, ...othersParams } = location;
      setMockLocation(provider, othersParams);

      setTimeout(() => isMocked(), 400);
      return
    }
    return () => stopMockLocation()
  }, [location])

  return (
    <View style={styles.container}>
      <Image
        source={{ uri: 'https://j.gifs.com/kZw1Z6@large.gif' }}
        style={styles.bgImage}
      />
      <View style={styles.card}>
        <View style={styles.providerContainer}>
          <View style={styles.providerItem}>
            <Text><B>GPS</B></Text>
            <Switch
              value={provider === 'gps'}
              onChange={(event) => setProvider('gps')}
            />
          </View>
          <View style={styles.providerItem}>
            <Text><B>NETWORK</B></Text>
            <Switch
              value={provider === 'network'}
              onChange={() => setProvider('network')}
            />
          </View>
        </View>

        <Text style={styles.cardTitle}>{getLocationName()}</Text>
        <View style={styles.cardBody}>
          {location &&
            <>
              <View style={styles.locationGrid}>
                <Text><B>Latitude:</B> {location.latitude.toFixed(3)}</Text>
                <Text><B>Longitude:</B> {location.longitude.toFixed(3)}</Text>
                <Text><B>Altitude:</B> {location.altitude.toFixed(3)}</Text>
              </View>

              {mockedLocation &&
                <View>
                  <Text style={{ textAlign: 'center' }}><B>PROVIDER LOCATION</B></Text>
                  <View style={styles.mockedLocation}>
                    <Text>{mockedLocation.latitude.toFixed(3)}</Text>
                    <Text>{mockedLocation.longitude.toFixed(3)}</Text>
                    <Text>{mockedLocation.altitude.toFixed(3)}</Text>
                  </View>
                </View>
              }
            </>
          }

          <View style={styles.actionButtons}>
            <TouchableHighlight
              style={styles.mockButton}
              onPress={clearMockLocation}
              underlayColor='rgba(127, 0, 255,0.55)'
            >
              <Text style={styles.mockText}>Clear</Text>
            </TouchableHighlight>

            <TouchableHighlight
              style={styles.mockButton}
              onPress={setLocation}
              underlayColor='rgba(127, 0, 255,0.55)'
            >
              <Text style={styles.mockText}>Mockar</Text>
            </TouchableHighlight>
          </View>
        </View>
      </View>
    </View>
  );
}

const styles = StyleSheet.create({
  providerContainer: {
    display: 'flex',
    flexDirection: 'column',
    gap: 1,
  },
  providerItem: {
    display: 'flex',
    flexDirection: 'row',
    justifyContent: 'space-between',
    alignItems: 'center',
    padding: 10
  },
  providerTitle: {

  },
  card: {
    padding: 20,
    borderRadius: 5,
    elevation: 5,
    backgroundColor: 'rgba(255,255,255,1)'
  },
  cardTitle: {
    fontSize: 20,
    fontWeight: 'bold',
    textAlign: 'center',
    color: 'black',
    marginBottom: 10,
    borderBottomColor: '#7F00FF',
    borderBottomWidth: 2,
    borderBottomLeftRadius: 5,
    borderBottomRightRadius: 5
  },
  cardBody: {
    display: 'flex',
    flexDirection: 'column',
    justifyContent: 'center',
    alignItems: 'center',
    gap: 10
  },
  container: {
    flex: 1,
    alignItems: 'center',
    justifyContent: 'center',
    gap: 10
  },
  box: {
    width: 60,
    height: 60,
    marginVertical: 20,
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
    display: 'flex',
    flexDirection: 'row',
    alignSelf: 'center',
    gap: 10
  },
  bgImage: {
    position: 'absolute',
    flex: 1,
    height: '100%',
    width: '100%'
  },
  locationGrid: {
    display: 'flex',
    flexDirection: 'column',
    gap: 10,
    justifyContent: 'center',
    alignItems: 'flex-start'
  },
  mockedLocation: {
    display: 'flex',
    flexDirection: 'row',
    justifyContent: 'center',
    alignItems: 'center',
    gap: 10
  }
});
