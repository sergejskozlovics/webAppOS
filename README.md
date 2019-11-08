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
```
Please, notice that the build step is essential - it compiles certain webAppOS applications and
services, which do not have the install task.

## Initial Configuration

```bash
cd webAppOS/dist/apps/Login.webservice/
cp webservice.properties.template webservice.properties
pico webservice.properties
cd ../../../..
cd webAppOS/dist/etc
copy webappos.properties.template webappos.properties
pico webappos.properties
cd ../../..
```
You can use any text editor instead of pico. The default (unchanged) configuration should work
well on localhost (127.0.0.1:4570).

## Starting webAppOS
From the webAppOS directory, run:
```bash
cd webAppOS/dist/bin
./webappos
```
