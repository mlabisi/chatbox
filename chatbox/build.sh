#!/bin/sh

javac -d build/ src/edu/cpp/cs/networks/attm/*.java
cd build/
# make the jar
jar -cvf MyJar.jar *
cd ..
# run the server jar
java -cp MyJar.jar edu.cpp.cs.networks.attm.ChatServer