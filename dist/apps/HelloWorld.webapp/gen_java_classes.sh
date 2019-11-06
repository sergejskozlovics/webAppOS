#!/bin/sh

SCRIPT_DIR=`dirname "$0"`
# If you wish to use .mmd syntax for the metamodel, then you have to
# convert it to .ecore. After the conversion please modify the name and nsPrefix XML attributes.
# To do that, uncomment the 5 lines below.

#$SCRIPT_DIR/../../bin/mmd2ecore $SCRIPT_DIR/HelloWorld.mmd
#sed -i 's/name="root"/name="HelloWorld"/g' $SCRIPT_DIR/HelloWorld.ecore
#sed -i 's/nsPrefix=""/nsPrefix="org.webappos.apps.helloworld.mm"/g' $SCRIPT_DIR/HelloWorld.ecore
#rm $SCRIPT_DIR/HelloWorld.xmi
#rm $SCRIPT_DIR/HelloWorld.xmi_refs

# Converting .ecore to Java classes; the package name will be taken from the nsPrefix XML attribute
# in the .ecore file. The factory name will be based on the name XML attribute.

$SCRIPT_DIR/../../bin/ecore2java $SCRIPT_DIR/HelloWorld.ecore $SCRIPT_DIR/src
read -n 1 -s -r -p "Press any key to finish"
