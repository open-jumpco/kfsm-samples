@echo off
echo "Node.JS Version"
node --version
node build\js\node_modules\kfsm-samples\kotlin\kfsm-samples.js
echo "JVM Version"
java -jar build\libs\kfsm-samples-fat-LOCAL-SNAPSHOT.jar
echo "Native Release Version"
build\bin\native\releaseExecutable\kfsm-samples.exe
echo "Native Debug Version"
build\bin\native\debugExecutable\kfsm-samples.exe
