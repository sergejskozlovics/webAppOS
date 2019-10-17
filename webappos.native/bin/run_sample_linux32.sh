#!/bin/sh

export JAVA_HOME=/usr/lib/jvm/java-8-openjdk-i386/jre
export PATH=$PWD:$JAVA_HOME/bin:$JAVA_HOME/lib:$JAVA_HOME/lib/i386:$PATH

$PWD/src_tdakernel_dll_sample_linux32
