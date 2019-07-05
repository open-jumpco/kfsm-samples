# kfsm samples

## Build

```bash
git clone https://github.com/open-jumpco/kfsm-samples.git
./gradlew assemble
```

### Build Graal Native Image

```bash
native-image -da --static -jar ./build/libs/kfsm-samples-fat-LOCAL-SNAPSHOT.jar kfsm-samples
```

## Executing applications

### Windows
```cmd
run.cmd
```
### *nix
```bash
./run.sh
# Execute graal-native image
./kfsm-samples
```


