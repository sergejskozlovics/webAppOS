#!/bin/sh

export JAVA_HOME=/usr/lib/jvm/java8-openjdk-amd64/jre
export PATH=$PWD:$JAVA_HOME/bin:$JAVA_HOME/lib:$JAVA_HOME/lib/amd64:$PATH

$PWD/src_tdakernel_dll_sample_linux64
