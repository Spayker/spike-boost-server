import React from 'react'
import {Text, View, Button, NativeModules} from 'react-native';
import styles from "./styles.css";
import MapView, { Marker, AnimatedRegion, Polyline, PROVIDER_GOOGLE } from "react-native-maps";

const LATITUDE_DELTA = 0.009;
const LONGITUDE_DELTA = 0.009;
const LATITUDE = 37.78825;
const LONGITUDE = -122.4324;

export default class Dashboard extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            foundDeviceName: 'None',
            deviceBondLevel: 0,
            heartBeatRate: 0,
            latitude: LATITUDE,
            longitude: LONGITUDE,
            routeCoordinates: [],
            distanceTravelled: 0,
            prevLatLng: {},
            coordinate: new AnimatedRegion({
                latitude: LATITUDE,
                longitude: LONGITUDE
            })
        };
    }

    componentDidMount() {
        this.watchID = navigator.geolocation.watchPosition(
            position => {
                const { coordinate, routeCoordinates, distanceTravelled } =   this.state;
                const { latitude, longitude } = position.coords;
                
                const newCoordinate = {
                    latitude,
                    longitude
                };
            
                if (Platform.OS === "android") {
                    if (this.marker) {
                        this.marker._component.animateMarkerToCoordinate(
                            newCoordinate,
                            500
                        );
                    }
                } else {
                    coordinate.timing(newCoordinate).start();
                }
        
                this.setState({
                    latitude,
                    longitude,
                    routeCoordinates: routeCoordinates.concat([newCoordinate]),
                    distanceTravelled:
                    distanceTravelled + this.calcDistance(newCoordinate),
                    prevLatLng: newCoordinate
                });
            },
            error => console.log(error),
            { enableHighAccuracy: true, timeout: 20000, maximumAge: 1000 }
        );
    }

    getMapRegion = () => ({
        latitude: this.state.latitude,
        longitude: this.state.longitude,
        latitudeDelta: LATITUDE_DELTA,
        longitudeDelta: LONGITUDE_DELTA
    });

    searchBluetoothDevices = () => {
        NativeModules.DeviceConnector.enableBTAndDiscover( (error, deviceBondLevel)=>{
            this.setState({ deviceBondLevel: deviceBondLevel});
        })
        setInterval(this.getDeviceBondLevel, 2000)
    }

    getDeviceBondLevel = () => {
        NativeModules.DeviceConnector.getDeviceBondLevel( (error, deviceBondLevel)=>{
            this.setState({ deviceBondLevel: deviceBondLevel}, () => {
                this.getDeviceBondLevel
            });
        })
    }

    activateHeartRateCalculation = () => {
        NativeModules.HeartBeatMeasurer.startHeartRateCalculation( (error, heartBeatRate)=>{
            this.setState({ heartBeatRate: heartBeatRate});
        })
        setInterval(this.getHeartRate, 2000)
    }

    getHeartRate = () => {
        NativeModules.HeartBeatMeasurer.getHeartRate( (error, heartBeatRate)=>{
            this.setState({ heartBeatRate: heartBeatRate});
        })
    }

    render() {
        return (
            <View style={styles.container}>
                <View style={styles.package}>
                    <Text style={styles.sensor_header}>Heart Beat:</Text>
                    <Text style={styles.sensor_value}>{this.state.heartBeatRate + ' Bpm'}</Text>
                </View>

                <View style={styles.package}>
                    <Text style={styles.sensor_header}>Device Bound Level:</Text>
                    <Text style={styles.sensor_value}>{this.state.deviceBondLevel}</Text>
                </View>

                <View style={styles.package}>
                    <Text style={styles.sensor_header}>Speed:</Text>
                    <Text style={styles.sensor_value}>0 Km/H</Text>
                </View>

                <View style={styles.package}>
                    <Text style={styles.sensor_header}>Passed Distance:</Text>
                    <Text style={styles.sensor_value}>0 Km</Text>
                </View>

                <View style={styles.package_center}>
                {
                    <Button onPress={this.searchBluetoothDevices} title='Link With MiBand' /> 
                }
                </View>

                <View style={styles.package_center}>
                {
                    <Button onPress={this.activateHeartRateCalculation} title='Get Heart Rate' /> 
                }
                </View>
            </View>
        );
    }
}