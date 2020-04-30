#!/bin/sh

javac -d build/ src/edu/cpp/cs/networks/attm/*.java
cd build/
# make the jar
jar -cvf Chatbox.jar *
cd ..
# run the server jar
java -cp build.Chatbox.jar edu.cpp.cs.networks.attm.ChatServer