import React from 'react'
import {Text, View, Button} from 'react-native';
import styles from "./styles.css";


export default class Dashboard extends React.Component {

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
                    <Button
                        onPress={() => {}}
                        title="Start"/>
                </View>
            </View>
        );
    }
}