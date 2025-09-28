#!/bin/bash

set -e

if [ -z "$1" ]; then
  echo "Usage args: swing|lwjgl3"
  exit 1
fi

build_module() # Args: modName
{
  cd "$1"
  ./build.sh
  mvn install
  cd -
}

build_module core
build_module impl/$1

cd examples/$1
./build.sh
cd -
