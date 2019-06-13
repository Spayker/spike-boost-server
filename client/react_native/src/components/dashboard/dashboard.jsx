import React from 'react'
import {Text, View, Button, NativeModules} from 'react-native';
import styles from "./styles.css";

export default class Dashboard extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            foundDeviceName: 'None',
            deviceBondLevel: 0,
            heartBeatRate: 0
        };
    }

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