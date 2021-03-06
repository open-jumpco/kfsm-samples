#!/usr/bin/env bash
# source ~/.nvm/nvm.sh
echo "Node Version" | tee perf.log
node --version| tee perf.log
/usr/bin/time --output=node-perf.log -v node --max-old-space-size=15 build/js/node_modules/kfsm-samples/kotlin/kfsm-samples.js $* | tee -a perf.log
# NODE_VER=10.16.3
# echo "Node $NODE_VER" | tee perf.log
# /usr/bin/time --output=node-perf-$NODE_VER.log -v nvm run $NODE_VER build/js/node_modules/kfsm-samples/kotlin/kfsm-samples.js $* | tee -a perf.log
# NODE_VER=12.10.0
# echo "Node 12.10.0" | tee perf.log
# /usr/bin/time --output=node-perf-$NODE_VER.log -v nvm run $NODE_VER build/js/node_modules/kfsm-samples/kotlin/kfsm-samples.js $* | tee -a perf.log
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
java $JVM_OPT -version | tee -a perf.log
echo "JVM Interpreted" | tee -a perf.log
/usr/bin/time --output=jvm-int-perf.log -v java  -Xmx36m -Xint -jar ./build/libs/kfsm-samples-fat-LOCAL-SNAPSHOT.jar $* | tee -a perf.log
echo "JVM JIT" | tee -a perf.log
/usr/bin/time --output=jvm-jit-perf.log -v java $JVM_OPT -Xmx36m -jar ./build/libs/kfsm-samples-fat-LOCAL-SNAPSHOT.jar $* | tee -a perf.log
