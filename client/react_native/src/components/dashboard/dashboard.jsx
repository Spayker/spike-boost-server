import React from 'react'
import {Text, View, Button, NativeModules} from 'react-native';
import styles from "./styles.css";


export default class Dashboard extends React.Component {

    constructor(props) {

        super(props);
        this.state = { isOn: false };  
        this.updateStatus();
      
    }

    turnOn = () => {
        NativeModules.Bulb.turnOn();
        this.updateStatus()
    }

    turnOff = () => {
        NativeModules.Bulb.turnOff();
        this.updateStatus()
    }

    updateStatus = () => {
        NativeModules.Bulb.getStatus( (error, isOn)=>{
        this.setState({ isOn: isOn});
      })  
    }

    render() {
        return (
            <View style={styles.container}>
                <View style={styles.package}>
                    <Text style={styles.sensor_header}>Heart Beat:</Text>
                    <Text style={styles.sensor_value}>0 Bpm</Text>
                </View>

                {/* <View style={styles.package}>
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
                </View> */}

                <View style={styles.package_center}>
                {
                    !this.state.isOn 
                        ? 
                    <Button onPress={this.turnOn} title="Turn ON " color="#FF6347"/> 
                        :
                    <Button onPress={this.turnOff} title="Turn OFF " color="#FF6340" />
                }
                </View>
            </View>
        );
    }
}