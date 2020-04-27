define(["dojox/layout/ContentPane", "dijit/form/TextBox", "dojo", "w/common"], function (ContentPane, TextBox, dojo, common) {

    return function (node) { // AMD class
        var myThis = this;
        myThis.type = "inputfield";
        myThis.node = node;
        myThis.eventHandler = null;
        myThis.setEventHandler = function (f) {
            myThis.eventHandler = f;
        };

        myThis.load = function () {
            var pane = new ContentPane({
                id: "dojopane" + node.reference,
                style: "resize:none;display:inline-block;", //none;margin:0; padding:0;width:100%;",
            });
            var input = new TextBox({
                id: "dojo" + node.reference,
                //				  rows: 4,
                cols: 20,
                style: "resize:none;display:inline-block;", //none;margin:0; padding:0;width:100%;",
            });
            input.set("value", node.text);
            pane.addChild(input);

            var nonEditable = (typeof node.editable != 'undefined') && ((node.editable == false) || (node
                .editable == "false"));
            if (nonEditable)
                input.set("disabled", true);

            input.on("focus", function () {
                $(myThis.input.domNode).width($(myThis.input.domNode).width() - 2);
                $(myThis.input.domNode).height($(myThis.input.domNode).height() - 2);

                if (myThis.eventHandler)
                    setTimeout(function () { // since change is AFTER blur due to Dojo strange implementation
                        myThis.eventHandler({
                            source: node,
                            name: "FocusGained"
                        });
                    }, 0);
            });
            input.on("blur", function () {
                $(myThis.input.domNode).width($(myThis.input.domNode).width() + 2);
                $(myThis.input.domNode).height($(myThis.input.domNode).height() + 2);
                if (myThis.eventHandler)
                    setTimeout(function () { // since change is AFTER blur due to Dojo strange implementation
                        myThis.eventHandler({
                            source: node,
                            name: "FocusLost",
                            data: myThis.input.get("value")
                        });
                    }, 0);
            });

            myThis.in_reload = true;
            input.on("change", function () {
                if (!myThis.in_reload && myThis.eventHandler)
                    myThis.eventHandler({
                        source: node,
                        name: "Change",
                        data: myThis.input.get("value")
                    });
                delete myThis.in_reload;
            });

            myThis.input = input;
            myThis.component = pane;
        };

        myThis.reload = function (node) {            
            myThis.node = node;
            myThis.in_reload = true;
            if (myThis.input.get("value")!=node.text) {
                myThis.in_reload = true;
                myThis.input.set("value", node.text);
            }
        };

        myThis.getBounds = function () {
            var retVal = common.getInitialBounds(myThis.node);
            retVal.minimumWidth = parseInt(myThis.component.domNode.clientWidth); // 200
            retVal.minimumWidth = 218;
            retVal.minimumHeight = parseInt($(myThis.component.domNode).css("height"),
                10); // 30
            if (retVal.minimumHeight < 30)
                retVal.minimumHeight = 30;
            retVal.maximumHeight = retVal.minimumHeight;
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

            if ($(myThis.input.domNode).hasClass("dijitFocused")) {
                $(myThis.input.domNode).width(w - 4);
                $(myThis.input.domNode).height(h - 4);
            }
            else {
                $(myThis.input.domNode).width(w - 2);
                $(myThis.input.domNode).height(h - 2);
            }

        };

        myThis.destroy = function () {
            myThis.input.destroy();
            myThis.component.destroy();
        };
    };
});