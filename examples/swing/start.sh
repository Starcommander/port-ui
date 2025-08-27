#!/bin/bash

# See also https://github.com/LWJGL/lwjgl3-demos/tree/main

#java -jar target/impl_lwjgl3-*.jar
CUR_CP=$(ls target/portui-test-swing-*.jar target/test-swing-dep.jar | tr '\n' ':')
#java -cp target/impl_lwjgl3-0.0.1-SNAPSHOT.jar:target/lwjgl3-demos.jar com.starcom.ui.frame.impl.LwjglFrame
java -cp $CUR_CP com.starcom.ui.App
