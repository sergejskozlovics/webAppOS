[![License](http://img.shields.io/:license-EUPL-brightgreen.svg)](https://raw.githubusercontent.com/LUMII-Syslab/webAppOS/master/COPYING)

[![webAppOS logo](http://webappos.org/top-logo.png)](http://webappos.org)

# webAppOS
This repository contains webAppOS sources.
For documentation and other info, visit [webAppOS homepage](http://webappos.org).

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
## Developing using Eclipse

Eclipse for Java developers is needed.

1. In Eclipse, right click on the Package Explorer window (usually, on the left).
2. In the popup menu, click "Import...".
3. In the Import wizard, choose "Existing Projects into Workspace".
4. In the "Select root directory" input field, choose the webAppOS cloned git directory. Eclipse
   will start searching for projects.
5. Choose the projects to import and click "Finish".
6. Add a set of jars named "webAppOS user library" (referenced from some projects).
   These jars are downloaded and put into webAppOS/dist/lib during the `./gradlew install` call.

   - Right click on the "sys" project, choose Properties.
   - Then choose "Java Build Path" on the left.
   - Switch to the "Libraries" tab on the right.
   - Click "Add Library...", choose "User Library".
   - If "webAppOS user library" is present, then it has been already configured. Otherwise:
 
     - click "User Libraries...",
     - click "New..." and type "webAppOS user library", click "OK",
     - click "Add External JARs...",
     - select all downloaded jars from webAppOS\dist\lib, click "Open".
