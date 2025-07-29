#!/bin/bash

mkdir -p build
rm -rf build/com
find src -name "*.java" -exec javac -sourcepath src/ -d build/ '{}' \;
#javac -sourcepath src/ -d build/ src/com/starcom/App.java
