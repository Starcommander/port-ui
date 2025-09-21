#!/bin/bash

if [ -z "$1" ]; then
  echo "Usage args: swing|lwjgl3"
  exit 1
fi

cd examples/$1
./start.sh
