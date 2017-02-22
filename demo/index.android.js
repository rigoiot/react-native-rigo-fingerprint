/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 * @flow
 */

import React, { Component } from 'react';
import {
  AppRegistry,
  StyleSheet,
  Text,
  View
} from 'react-native';

import RNRigoFingerprint from 'react-native-rigo-fingerprint'

export default class demo extends Component {
  constructor(props) {
    this.state = {
      template: null,
      feature: null,
      templateLen: 0,
      featureLen: 0,
      match: 0,
      retCode: 0,
      error: 0
    }
  }

  componentDidMount() {
    RNRigoFingerprint.initialize((code, result) => {
      this.setState({retCode: code})
      switch (code) {
        case 0xA4:
          this.setState({error: result.error})
          break;
        case 0xA4:
          this.setState({error: 0})
          break;
        default:

      }
    })
  }

  render() {
    return (
      <View style={styles.container}>
        <Text style={styles.welcome}>
          Welcome to React Native!
        </Text>
        <Text style={styles.instructions}>
          To get started, edit index.android.js
        </Text>
        <Text style={styles.instructions}>
          Double tap R on your keyboard to reload,{'\n'}
          Shake or press menu button for dev menu
        </Text>
        <Botton onClick={()=>{
          RNRigoFingerprint.fpiOpenBT()
        }}>
          {'OpenBT'}
        </Botton>
        <Botton onClick={()=>{
          fp.fpiOpenBT()
        }}>
          {'OpenBT'}
        </Botton>
        <Text style={styles.instructions}>
          {`Result, code: ${this.state.code}, error: ${this.state.error}`}
        </Text>
        <Text style={styles.instructions}>
          {`Template bytes len: ${this.state.templateLen}`}
        </Text>
        <Text style={styles.instructions}>
          {`Template: ${this.state.template}`}
        </Text>
        <Text style={styles.instructions}>
          {`Feature bytes len: ${this.state.featureLen}`}
        </Text>
        <Text style={styles.instructions}>
          {`Feature: ${this.state.feature}`}
        </Text>
        <Text style={styles.instructions}>
          {`Match: ${this.state.match}`}
        </Text>
      </View>
    );
  }
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: '#F5FCFF',
  },
  welcome: {
    fontSize: 20,
    textAlign: 'center',
    margin: 10,
  },
  instructions: {
    textAlign: 'center',
    color: '#333333',
    marginBottom: 5,
  },
});

AppRegistry.registerComponent('demo', () => demo);
