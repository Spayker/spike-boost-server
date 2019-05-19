/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 *
 * @format
 * @flow
 */

import React, {Component} from 'react';
import {StyleSheet, Text, View, Button} from 'react-native';

export default class App extends Component {
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

const styles = StyleSheet.create({
  container: {
    flex: 1,
    flexDirection: 'column',
    margin: 5
  },
  package: {
    flex: 1,
    flexDirection: 'row',
    justifyContent: 'center',
    justifyContent: 'space-between',
    backgroundColor: '#F5FCFF',
    margin: 5
  },
  package_center:{
    flex: 1,
    textAlign: 'center'
  },
  sensor_header: {
    fontSize: 20,
    textAlign: 'left'
  },
  sensor_value: {
    fontSize: 20,
    textAlign: 'right'
  },
  instructions: {
    textAlign: 'center',
    color: '#333333',
    marginBottom: 5
  },
});