#!/bin/sh

SCRIPT_DIR=`dirname "$0"`
$SCRIPT_DIR/../../bin/mmd2java $SCRIPT_DIR/HelloWorldMetamodel.mmd $SCRIPT_DIR/src org.webappos.apps.helloworld.mm
