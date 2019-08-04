#!/usr/bin/env bash
echo "Node Version"
node --version
/usr/bin/time -v node --max-old-space-size=12 build/js/node_modules/kfsm-samples/kotlin/kfsm-samples.js
echo "Native Release Version"
/usr/bin/time -v ./build/bin/native/releaseExecutable/kfsm-samples.kexe
echo "Native Debug Version"
/usr/bin/time -v ./build/bin/native/debugExecutable/kfsm-samples.kexe
if [[ -f ./build/native/kfsm-samples ]]
then
    echo "Graal Native Image Version"
    /usr/bin/time -v ./build/native/kfsm-samples -Xmx36m
fi
echo "JVM Version"
java -version
echo "JVM Interpreted"
/usr/bin/time -v java  -Xmx36m -Xint -jar ./build/libs/kfsm-samples-fat-LOCAL-SNAPSHOT.jar
echo "JVM JIT"
/usr/bin/time -v java  -Xmx36m -jar ./build/libs/kfsm-samples-fat-LOCAL-SNAPSHOT.jar
