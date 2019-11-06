define(["dijit/form/CheckBox", "dojox/layout/ContentPane", "dojo", "w/common"], function (CheckBox, ContentPane, dojo, common) {

    return function (node) { // AMD class
        var myThis = this;
        myThis.type = "checkbox";
        myThis.node = node;
        myThis.eventHandler = null;

        myThis.setEventHandler = function (f) {
            myThis.eventHandler = f;
        };

        myThis.load = function (readOnly, enabled) {
            console.log("CB LOAD",readOnly,enabled);

            var pane = new ContentPane({
                id: "dojopane" + myThis.node.reference,
                style: "resize:none;display:inline-block;overflow:hidden;",
            });
            var invval = (typeof myThis.node.checked === "undefined") || (myThis.node.checked == false) || (myThis.node
                .checked == "false");

            var cb = new CheckBox({
                id: "dojocb" + myThis.node.reference,
                checked: !invval,
                disabled: readOnly || !enabled,
                onClick: function () {
                    if (myThis.in_reload && myThis.eventHandler)
                        myThis.eventHandler({
                            source: myThis.node,
                            name: "Change",                            
                            data: cb.checked
                        });
                    delete myThis.in_reload;
                },
                style: "resize:none;display:inline-block;"
            });

            if (invval) {
                cb.set("checked", false);
            }

            var label = new ContentPane({
                content: "&nbsp;" + (myThis.node.caption ? myThis.node.caption : ""),
                id: "dojolabel" + myThis.node.reference,
                style: "padding:0;display: inline-block;"
            });

            pane.addChild(cb);
            pane.addChild(label);

            cb.startup();
            pane.startup();

            cb.on("focus", function () {
                if (myThis.eventHandler)
                    setTimeout(function () { // since change is AFTER blur due to Dojo strange implementation
                        myThis.eventHandler({
                            source: myThis.node,
                            name: "FocusGained",
                            data: cb.checked
                        });
                    }, 0);
            });
            cb.on("blur", function () {
                if (myThis.eventHandler)
                    setTimeout(function () { // since change is AFTER blur due to Dojo strange implementation
                        myThis.eventHandler({
                            source: myThis.node,
                            name: "FocusLost",
                            data: cb.checked
                        });
                    }, 0);
            });

            myThis.cb = cb;
            myThis.label = label;

            myThis.component = pane;

        };

        myThis.reload = function (node, readOnly, enabled) {
            var invval = (typeof node.checked === "undefined") || (node.checked == false) || (node
                .checked == "false");

            if (myThis.cb.get("checked") == invval) {
                myThis.in_reload = true;
                myThis.cb.set("checked", !invval);
                myThis.label.set("content", "&nbsp;" + (node.caption ? node.caption : ""));
            }

            console.log("CB RELOAD",readOnly,enabled);
            myThis.cb.set("disabled", readOnly || !enabled)

            myThis.node = node;
        };

        myThis.getBounds = function () {
            var retVal = common.getInitialBounds(myThis.node);
            retVal.minimumWidth = 20;

            var tw = common.getTextWidth("&nbsp;" + (myThis.node.caption ? myThis.node.caption : ""));
            retVal.minimumWidth += tw;
            retVal.preferredWidth = retVal.minimumWidth;
            retVal.maximumWidth = retVal.minimumWidth;

            retVal.minimumHeight =
                20;
            retVal.maximumHeight = retVal.minimumHeight;

            retVal.verticalAlignment = "CENTER";
            return retVal;
        };

        myThis.layout = function (x, y, w, h) {

            dojo.style(myThis.cb, "overflow", "hidden");
            dojo.style(myThis.cb, "position", "relative");
            dojo.style(myThis.cb, "display", "inline-block");
            
            //$(myThis.cb.domNode).css("margin-top", "-12");

            $(myThis.cb.domNode).css("margin-top", "3");
            $(myThis.cb.domNode).css("vertical-align", "top");

            dojo.style(myThis.label, "position", "relative");
            dojo.style(myThis.label, "display", "inline-block");
            $(myThis.label.domNode).css("margin-left", 0);
            $(myThis.label.domNode).css("margin-top", 0);
            $(myThis.label.domNode).css("overflow", "hidden");


            dojo.style(myThis.component, "position", "relative");
            dojo.style(myThis.component, "display", "inline-block");
            dojo.style(myThis.component, "border", "0px");
            dojo.style(myThis.component, "padding", "0px");
            dojo.style(myThis.component, "border-spacing", "0px");

            $(myThis.component.domNode).css("margin-left", x);
            $(myThis.component.domNode).css("margin-top", y);
            $(myThis.component.domNode).width(w);
            $(myThis.component.domNode).height(h);

            $(myThis.label.domNode).width(w - 20);
            $(myThis.label.domNode).height(h);
        };

        myThis.destroy = function () {
            myThis.cb.destroy();
            myThis.label.destroy();
            myThis.component.destroy();
        }

    };
});