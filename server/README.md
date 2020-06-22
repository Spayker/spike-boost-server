# SpikeBoost Server Part

Project stays in the active phase of development. 

## Description
Server part offers following features:
1) user authorization
2) storing of training tracks on cloud side
3) get new audio tracks
4) sharing workout results with preferable music 

## Technical Stack
1) Java 11
2) Spring Boot: 2.1.4.RELEASE
3) Spring Cloud: Greenwich.SR4
4) Junit 5
5) Mockito
6) Docker-Compose: 3.7

## How To Run
1) In Project root folder: mvn clean install
2) Run docker-compose, docker-compose-dev scripts: docker-compose -f docker-compose.yml -f docker-compose.dev.yml up -d --build
3) Wait until all services are run 
