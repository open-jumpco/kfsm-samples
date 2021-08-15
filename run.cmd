@echo off
echo "Node.JS Version" > cmdperf.log
node --version >> cmdperf.log
rem node build\js\node_modules\kfsm-samples\kotlin\kfsm-samples.js
node build\js\node_modules\kfsm-samples\kotlin\kfsm-samples.js >> cmdperf.log
rem node build\js\node_modules\kfsm-samples-js-legacy\kotlin\kfsm-samples-js-legacy.js >> cmdperf.log
echo "JVM Version" >> cmdperf.log
java -jar build\libs\kfsm-samples-fat-LOCAL-SNAPSHOT.jar >> cmdperf.log
echo "Native Release Version" >> cmdperf.log
build\bin\native\releaseExecutable\kfsm-samples.exe >> cmdperf.log
echo "Native Debug Version" >> cmdperf.log
build\bin\native\debugExecutable\kfsm-samples.exe >> cmdperf.log
more cmdperf.log
