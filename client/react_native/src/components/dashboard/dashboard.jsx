import React from 'react'
import {Text, View, Button, NativeModules} from 'react-native';
import styles from "./styles.css";

const bondLevelLabel = "Bond Level: ";

export default class Dashboard extends React.Component {

    constructor(props) {
        super(props);
        this.state = { 
            foundDeviceName: 'None',
            deviceBondLevel: 0
        };  
    }

    searchBluetoothDevices = () => {
        NativeModules.DeviceConnector.enableBTAndDiscover( (error, foundDeviceName)=>{
            this.setState({ foundDeviceName: foundDeviceName});
        })
    }

    connectMiBandDevice = () => {
        NativeModules.DeviceConnector.connectDevice( (error, deviceBondLevel)=>{
            this.setState({ deviceBondLevel: deviceBondLevel});
        })
    }

    render() {
        return (
            <View style={styles.container}>
                <View style={styles.package}>
                    <Text style={styles.sensor_header}>Heart Beat:</Text>
                    <Text style={styles.sensor_value}>0 Bpm</Text>
                </View>

                <View style={styles.package}>
                    <Text style={styles.sensor_header}>Training Time:</Text>
                    <Text style={styles.sensor_value}>0 S</Text>
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
                    <Button onPress={this.searchBluetoothDevices} title={this.state.foundDeviceName} /> 
                }
                </View>

                <View style={styles.package_center}>
                {
                    <Button onPress={this.connectMiBandDevice} title={bondLevelLabel + this.state.deviceBondLevel} /> 
                }
                </View>
            </View>
        );
    }
}