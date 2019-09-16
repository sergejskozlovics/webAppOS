define(["dojox/layout/ContentPane", "dijit/form/SimpleTextarea", "dojo", "w/common"], function (ContentPane, SimpleTextArea, dojo, common) {

    return function (node) { // AMD class
        var myThis = this;
        myThis.type = "textarea";
        myThis.node = node;
        myThis.eventHandler = null;
        myThis.setEventHandler = function (f) {
            myThis.eventHandler = f;
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
                        if (myThis.eventHandler) {
                        setTimeout(function(){ // since change is AFTER blur due to Dojo strange implementation
                            myThis.eventHandler({
                                source: myThis.node,
                                name: "FocusGained"
                            });
                        },0);
                    }
                });

                area.on("blur", function () {
                    if (myThis.eventHandler) {
                        setTimeout(function(){ // since change is AFTER blur due to Dojo strange implementation
                            myThis.eventHandler({
                                source: myThis.node,
                                name: "FocusLost"
                            });
                        },0);
                    }
                });

                area.on("change", function () {
                    if (!myThis.in_reload && myThis.eventHandler)
                        myThis.eventHandler({
                            source: myThis.node,
                            name: "Change",
                            data: {
                                text: this.get("value")
                            }
                        }); // this==area
                    delete myThis.in_reload;
                });

                area.set("value", myThis.node.text);
                myThis.area = area;
                myThis.component = pane;
            } else {
                if (area.value != myThis.node.text)
                    area.set("value", myThis.node.text);
            }
        };

        myThis.load = function () {
            myThis.init();
        };
        myThis.reload = function (node) {

            var oldVal = myThis.area.get("value");
            var newVal = node.text;
            if (oldVal!=newVal) {
                myThis.in_reload = true;            
                myThis.node = node;
                myThis.init();
            }
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
            delete myThis.area;
            delete myThis.component;
        }
    };
});