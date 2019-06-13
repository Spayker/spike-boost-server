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
                style={styles.map}
                initialRegion={this.state.initialPosition}>
                    

                    <Marker
                        coordinate={{latitude: this.state.initialPosition.latitude, longitude: this.state.initialPosition.longitude}}
                        title={'title'}
                        description={'description'}
                    />

                </MapView>
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
      position: 'absolute',
      top: 0,
      left: 0,
      right: 0,
      bottom: 0,
      justifyContent: 'flex-end',
      alignItems: 'center',
    },
    map: {
      position: 'absolute',
      top: 0,
      left: 0,
      right: 0,
      bottom: 0,
    },
  });