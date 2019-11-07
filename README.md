[![License](http://img.shields.io/:license-EUPL-brightgreen.svg)](https://raw.githubusercontent.com/LUMII-Syslab/webAppOS/master/COPYING)

# webAppOS
webAppOS files at github

## Installation

```bash
git clone https://github.com/LUMII-Syslab/webAppOS.git
cd webAppOS/src
./gradlew build
./gradlew install
cd ../..
cd webAppOS/dist/apps/Login.webservice/
cp webservice.properties.template webservice.properties
pico webservice.properties
cd ../../../..
cd webAppOS/dist/etc
copy webappos.properties.template webappos.properties
pico webappos.properties
cd ../../..
```

## Starting webAppOS
From the webAppOS directory, run:
```bash
cd webAppOS/dist/bin
./webappos
```
