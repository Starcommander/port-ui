#!/bin/bash

build_module() # Args: modName
{
  cd "$1"
  ./build.sh
  mvn install
  cd -
}

build_module core
build_module impl/swing

cd examples/swing
./build.sh
cd -
