# SpikeBoost Server Part

## spike-boost-android-solution
Basically client side will be deployed on mobile platforms such as Android, IoS.
It contains prepared to run neural network and data to process sensor's data in realtime.

Typical green line scenario is:
1) user runs application
2) select type of training (jogging, cycling, workout)
3) select prepared soundtrack list
4) run training
5) system starts checking data income from sensors
6) during its processing it decides what to run among preferable soundtracks
7) system can also record track if training's type was selected to cycling, jogging
8) after training will be over it shows passed track
9) he can see on screen that track was divided on several parts. Each part has its unique soundtrack played or partially played
10) user can upvote this track: he can say that soundtrack was good for him or not really. In last case app will ask
what should it run next time for getting better performance, joy etc.

### Stack of technologies

For client side:
1) Java 8
2) Android Min SDK: 21
3) TensorFlow Lite
4) Test Espresso
5) Test Instrumentation
6) FireBase API
=======
## Project status
[![Build Status](https://travis-ci.com/Spayker/spike-boost.svg?token=jG3sXfQvSVtz8PKVCuMp&branch=spike-boost-server-solution)](https://travis-ci.com/Spayker/spike-boost)

Server part offers following features:
1) user authorization
2) storing of training tracks on cloud side
3) get new audio tracks
4) sharing workout results with preferable music 

Technical Stack:
1) Java 8
2) Spring Boot 
3) Rest
4) Spring Security
5) Junit 5
6) Mockito

