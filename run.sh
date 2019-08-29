#!/usr/bin/env bash
echo "Node Version" | tee perf.log
node --version | tee -a perf.log
/usr/bin/time --output=node-perf.log -v node --max-old-space-size=12 build/js/node_modules/kfsm-samples/kotlin/kfsm-samples.js $* | tee -a perf.log
echo "Native Release Version" | tee -a perf.log
/usr/bin/time --output=native-release-perf.log -v ./build/bin/native/releaseExecutable/kfsm-samples.kexe $* | tee -a perf.log
echo "Native Debug Version" | tee -a perf.log
/usr/bin/time --output=native-debug-perf.log -v ./build/bin/native/debugExecutable/kfsm-samples.kexe $* | tee -a perf.log
if [[ -f ./build/native/kfsm-samples ]]
then
    echo "Graal Native Image Version" | tee -a perf.log
    /usr/bin/time --output=graal-native-perf.log -v ./build/native/kfsm-samples -Xmx36m $* | tee -a perf.log
fi
echo "JVM Version" | tee -a perf.log
java -version | tee -a perf.log
echo "JVM Interpreted" | tee -a perf.log
/usr/bin/time --output=jvm-int-perf.log -v java  -Xmx36m -Xint -jar ./build/libs/kfsm-samples-fat-LOCAL-SNAPSHOT.jar $* | tee -a perf.log
echo "JVM JIT" | tee -a perf.log
/usr/bin/time --output=jvm-jit-perf.log -v java  -Xmx36m -jar ./build/libs/kfsm-samples-fat-LOCAL-SNAPSHOT.jar $* | tee -a perf.log
