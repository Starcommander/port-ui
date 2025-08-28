#!/bin/bash

cd core
./build.sh
mvn install
cd -
cd examples/swing
./build.sh
cd -
