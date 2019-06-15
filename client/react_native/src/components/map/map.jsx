import React from 'react'
import {
    StyleSheet,
    View,
    Text,
    TouchableOpacity,
    Platform,
    PermissionsAndroid,
    Dimensions
} from "react-native";
import MapView, {
    Marker,
    AnimatedRegion,
    Polyline,
    PROVIDER_GOOGLE
} from "react-native-maps";
import haversine from "haversine";
//import styles from "./styles.css";

const {width, height} = Dimensions.get('window')

const SCREEN_HEIGHT = height
const SCREEN_WIDTH = width
const ASPECT_RATIO = width / height
const LATITUDE_DELTA = 0.0922
const LONGITUDE_DELTA = LATITUDE_DELTA * ASPECT_RATIO

const Screen = {
    width: Dimensions.get('window').width,
    height: Dimensions.get('window').height,
  };

export default class Map extends React.Component {

    constructor() {
        super()
        this.state = {
          initialPosition: {
            latitude: 0,
            longitude: 0,
            latitudeDelta: 0,
            longitudeDelta: 0,
          },
        }
      }
    
      componentDidMount() {
        navigator.geolocation.getCurrentPosition((position) => {
          var lat = parseFloat(position.coords.latitude)
          var long = parseFloat(position.coords.longitude)
    
          var initialRegion = {
            latitude: lat,
            longitude: long,
            latitudeDelta: LATITUDE_DELTA,
            longitudeDelta: LONGITUDE_DELTA,
          }
    
          this.setState({initialPosition: initialRegion})
        },
        (error) => alert(JSON.stringify(error)),
        { enableHighAccuracy: false, timeout: 30000, maximumAge: 3600000 });
      }
    
    
      renderScreen = () => {
          return (
            <View style={styles.container}>
              <MapView
                style={styles.mapContainer}
                initialRegion={this.state.initialPosition}>
                    <Marker
                        coordinate={{latitude: this.state.initialPosition.latitude, longitude: this.state.initialPosition.longitude}}
                        title={'title'}
                        description={'description'}
                    />
                </MapView>
                <View style={styles.mapDrawerOverlay} />
            </View>
          );
      }
    
      render() {
        return (
          this.renderScreen()
        );
      }
}

const styles = StyleSheet.create({
    container: {
      flex: 1,
    },
    mapContainer: {
      width: Screen.width,
      height: Screen.height,
    },
    mapDrawerOverlay: {
      position: 'absolute',
      left: 0,
      top: 0,
      opacity: 0.0,
      height: Dimensions.get('window').height,
      width: 25,
    },
  });
  