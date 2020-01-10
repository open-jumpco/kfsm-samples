# kfsm samples

This project contains samples for using [kfsm](https://github.com/open-jumpco/kfsm)

## Build

```bash
git clone https://github.com/open-jumpco/kfsm-samples.git
./gradlew assemble
```

### Build Graal Native Image

This was tested by building normal build with JDK 8 and 11 and then using Graal 19.1.1 to build the native image.





```bash
# Install GraalVM native-image compiler
gu install native-image
# Create the native image
./gradlew nativeImage 
```

_I prefer using [sdkman](https://sdkman.io/) to switch runtimes while doing this kind of exercise._

## Executing applications

### Windows
```cmd
run.cmd
```

### *nix
```bash
./run.sh
```


