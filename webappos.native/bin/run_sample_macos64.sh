#!/bin/sh

install_name_tool -id "./libtdakernel64.dylib" ./libtdakernel64.dylib

export JAVA_HOME=/Library/Java/JavaVirtualMachines/jdk1.8.0_144.jdk/Contents/Home/jre
export DYLD_LIBRARY_PATH=/System/Library/Frameworks/ImageIO.framework/Versions/A/Resources/:.:$JAVA_HOME/lib:$JAVA_HOME/lib/server:$DYLD_LIBRARY_PATH

$PWD/src_tdakernel_dll_sample_macos64
