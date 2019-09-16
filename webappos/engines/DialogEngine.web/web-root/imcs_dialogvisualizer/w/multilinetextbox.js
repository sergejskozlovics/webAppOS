define(["dojox/layout/ContentPane", "dijit/form/SimpleTextarea", "dojo", "w/common"], function (ContentPane, SimpleTextArea, dojo, common) {

    return function (node) { // AMD class
        var myThis = this;
        myThis.type = "multilinetextbox";
        myThis.node = node;
        myThis.eventHandler = null;
        myThis.setEventHandler = function (f) {
            myThis.eventHandler = f;
        };

        myThis.positionAndValueFromRepo = function () {
            var position = 0;
            var sss = "";
            var arr = myThis.node.textLine;
            if (!arr && myThis.node.getTextLine)
                arr = myThis.node.getTextLine();
            if (!arr)
                arr = [];

            var area = myThis.area;

            var current = myThis.node.current;
            if (!current)
                current = myThis.node.getCurrent();
            if (!current)
                current = [];

            var posDone = false;
            var wasLine = false;
            for (var q = 0; q < arr.length; q++) {
                var deleted = arr[q].deleted;
                if (typeof deleted === "undefined") {
                    if (arr[q].getDeleted)
                        deleted = arr[q].getDeleted();
                }

                if (!deleted) {
                    var text = (arr[q].text ? arr[q].text : "");
                    if (wasLine) {
                        sss += "\n" + text;
                        position += 1 + text.length;
                    } else {
                        sss += text;
                        position += text.length;
                        wasLine = true;
                    }

                    if (!posDone && (current.length > 0) && (current[0].reference = arr[q].reference)) { // selecting current line
                        area.textbox.setSelectionRange(position, position);
                        posDone = true;
                    }
                }
            }
            for (var q = 0; q < arr.length; q++) {
                if (arr[q].getDeleted && arr[q].getDeleted())
                    arr[q].delete();
            }

            arr = myThis.node.textLine;
            if (!arr && myThis.node.getTextLine)
                arr = myThis.node.getTextLine();
            if (!arr)
                arr = [];

            if (arr.length > 0) {
                if (!posDone) {
                    try { // we use try-catch here, since setSelectionRange may throw an exception in firefox under linux
                        area.textbox.setSelectionRange(0, 0);
                    } catch (t) {}
                }
            }
            area.set("value", sss);
        };


        myThis.init = function () {

            var area = myThis.area;
            if (!area) {
                // creating new area and pane...
                pane = new ContentPane({
                    id: "dojopane" + myThis.node.reference,
                    style: "padding:0;margin:0;display:inline-block;",
                });
                area = new SimpleTextArea({
                    id: "dojo" + myThis.node.reference,
                    rows: 4,
                    cols: 20,
                    style: "resize:none;margin:0; padding:0;position:relative;width:100%;height:100%;",
                    //					  value: node.text,
                });
                pane.addChild(area);

                area.on("focus", function () {
                    if (myThis.eventHandler)
                        myThis.eventHandler({
                            source: myThis.node,
                            name: "FocusGained",
                            data: {
                                position: area.textbox.selectionStart,
                            }
                        }); // this==area
                });

                area.on("click", function () {
                    if (myThis.eventHandler)
                        myThis.eventHandler({
                            source: myThis.node,
                            name: "Click",
                            data: {
                                position: area.textbox.selectionStart,
                            }
                        }); // this==area
                });

                area.on("blur", function () {
                    if (myThis.eventHandler)
                        myThis.eventHandler({
                            source: myThis.node,
                            name: "FocusLost",
                            data: {
                                position: area.textbox.selectionStart,
                            }
                        }); // this==area
                });

                area.on("change", function () {
                    if (myThis.eventHandler)
                        myThis.eventHandler({
                            source: myThis.node,
                            name: "MultiLineTextBoxChange",
                            data: {
                                position: area.textbox.selectionStart,
                                text: this.get("value")
                            }
                        }); // this==area
                });

                myThis.area = area;
                myThis.component = pane;

                myThis.positionAndValueFromRepo(); // || area.set("value", node.text);
            } else
                myThis.positionAndValueFromRepo(); // || area.set("value", node.text);

        };

        myThis.load = function () {
            myThis.init();
        };

        myThis.reload = function (node) {
            myThis.node = node;
            myThis.init();
        };

        myThis.getBounds = function () {
            var retVal = common.getInitialBounds(myThis.node);
            retVal.minimumWidth = 300;
            retVal.minimumHeight = 100;

            if (myThis.node.preferredRelativeHeight)
                retVal.preferredRelativeHeight = myThis.node.preferredRelativeHeight;
            else
                retVal.preferredRelativeHeight = 1;

            return retVal;
        };

        myThis.layout = function (x, y, w, h) {

            dojo.style(myThis.component, "position", "relative");
            dojo.style(myThis.component, "display", "inline-block");
            dojo.style(myThis.component, "border", "0px");
            dojo.style(myThis.component, "padding", "0px");
            dojo.style(myThis.component, "border-spacing", "0px");
            $(myThis.component.domNode).width(w);
            $(myThis.component.domNode).height(h);
            $(myThis.component.domNode).css("margin-left", x);
            $(myThis.component.domNode).css("margin-top", y);
        };

        myThis.destroy = function () {
            myThis.area.destroy();
            myThis.component.destroy();
        };

    };
});