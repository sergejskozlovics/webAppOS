[![License](http://img.shields.io/:license-EUPL-brightgreen.svg)](https://raw.githubusercontent.com/LUMII-Syslab/webAppOS/master/COPYING)

[![webAppOS logo](http://webappos.org/top-logo.png)](http://webappos.org)

# webAppOS
This repository contains webAppOS sources.
For documentation and other info, visit [webAppOS homepage](http://webappos.org).

## Installation

Java8+ must be installed (Java 11+ is also supported). The javac and java commands (or the JAVA_HOME environment variable) must be available to build and to run webAppOS.

*Notice for Linux users having Java8:* You have to install JavaFX compiled with Java8. You can find the corresponding commands for Ubuntu [here](https://stackoverflow.com/questions/56166267/how-do-i-get-java-fx-running-with-openjdk-8-on-ubuntu-18-04-2-lts).

```bash
git clone https://github.com/LUMII-Syslab/webAppOS.git
cd webAppOS/src
./gradlew install
cd ../..
```

## Initial Configuration

The default (unchanged) configuration should work
well on localhost (127.0.0.1:4570). However, to specify a real IP or a domain as well as to
configure mail server and CAPTCHA, edit the following files:

- webAppOS/dist/apps/Login.webservice/webservice.properties
- webAppOS/dist/etc/webappos.properties

You can use any text editor.

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
