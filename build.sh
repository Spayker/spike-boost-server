#!/bin/sh
cd $TRAVIS_BUILD_DIR/server
sbt ++$TRAVIS_SCALA_VERSION package