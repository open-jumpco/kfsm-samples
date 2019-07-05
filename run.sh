#!/usr/bin/env bash
echo "Node Version"
node --version
node build/js/node_modules/kfsm-samples/kotlin/kfsm-samples.js
echo "JVM Version"
java -version
java -jar ./build/libs/kfsm-samples-fat-LOCAL-SNAPSHOT.jar
echo "Native Release Version"
./build/bin/native/releaseExecutable/kfsm-samples.kexe
echo "Native Debug Version"
./build/bin/native/debugExecutable/kfsm-samples.kexe
if [[ -f ./build/native/kfsm-samples ]]
then
    echo "Graal Native Image Version"
    ./build/native/kfsm-samples
fi
