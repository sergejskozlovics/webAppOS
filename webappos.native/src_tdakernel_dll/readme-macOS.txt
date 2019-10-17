Library install path on macOS
=============================
On MacOS, -rpath option is not supported by linker.
We advice to modify DYLD_LIBRARY_PATH to specify where Java libraries
are located as well as where TDA Kernel library is located.
In the example below, we assume that TDA Kernel is in the current directory (".").
Notice that ImageIO framework path must be before paths to the TDA Kernel and Java libraries.

export JAVA_HOME=/Library/Java/JavaVirtualMachines/jdk1.8.0_144.jdk/Contents/Home/jre
export DYLD_LIBRARY_PATH=/System/Library/Frameworks/ImageIO.framework/Versions/A/Resources/:.:$JAVA_HOME/lib:$JAVA_HOME/lib/server:$DYLD_LIBRARY_PATH

More info on library install path:
https://blogs.oracle.com/dipol/dynamic-libraries,-rpath,-and-mac-os

32-bit TDA Kernel on macOS
==========================
On macOS, both 32-bit and 64-bit versions of TDA Kernel native library
are available. However, Apple and Oracle do not ship 32-bit Java version
for macOS anymore. Thus, if you wish to stick to 32-bit version, you will have
to compile 32-bit OpenJDK by your own.
