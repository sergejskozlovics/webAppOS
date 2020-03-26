You shoud have Apache ant installed.

If the gwt folder does not exist, download Google Web Toolkit (GWT)
and extract it there.

If the js-minimizer folder does not exist, download Google Closure Compiler
and extract it there.

To build the layout engine .js and .min.js files, just issue the "ant" command from
this directory. The file will be put into this directory.

To create just a minimized version, issue the "ant buildmin" command.
To create just a non-minimized version, issue the "ant builddev" command.

Samples:
* diagram_layout_sample.html - an example showing how to use the IMCSDiagramLayout class
* dialog_layout_sample.html - an example showing how to use the IMCSDialogLayout class

In addition, you can use imcs_canvas.html to test the IMCSDiagramLayout class and
visualize the diagram right away.

Licences
--------
The compiled JavaScript files are built from the code having
the following licenses:
* IMCS layout engine Java classes: src/code/lv/lumii/COPYING (GPLv2 or later with the classpath exception)
* W3C Java classes: src/code/org/w3c/COPYING (W3C liberal license)
* GWT-AWT classes: gwt-awt/COPYING  (GPLv2 with the classpath exception)

In addition, the tools used at compile time have the following licenses:
* GWT: gwt/COPYING (Google Web Toolkit license)
* JavaScript minimizer (Closure Compiler): js-minimizer/COPYING (Apache 2.0 license)

