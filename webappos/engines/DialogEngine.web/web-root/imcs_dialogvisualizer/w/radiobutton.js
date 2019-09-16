define(["dijit/form/RadioButton", "dojox/layout/ContentPane", "dojo", "w/common"], function (RadioButton, ContentPane, dojo, common) {

    return function (node, groupName) { // AMD class
        var myThis = this;
        myThis.type = "radiobutton";
        myThis.node = node;
        if (!groupName)
            groupName = "some_combobox_group";
        myThis.groupName = groupName;
        myThis.eventHandler = null;
        myThis.setEventHandler = function (f) {
            myThis.eventHandler = f;
        };

        myThis.load = function () {
            var pane = new ContentPane({
                id: "dojopane" + myThis.node.reference,
                style: "resize:none;display:inline-block;overflow:hidden;", //none;margin:0; padding:0;width:100%;",
            });

            var rb = new RadioButton({
                id: "dojo" + myThis.node.reference,
                name: myThis.groupName,
                checked: (myThis.node.selected == true || myThis.node.selected == "true") ? true : false,
                onChange: function (val) {
                    if (!myThis.in_reload && myThis.eventHandler ) {
                        if (val) {
                            setTimeout(function () { // send true value after all other falses have been sent
                                myThis.eventHandler({
                                    source: myThis.node,
                                    name: "Click",
                                    data: val
                                });

                            }, 0);
                        } else
                            myThis.eventHandler({
                                source: myThis.node,
                                name: "UnClick",
                                data: val
                            });
                    }
                    delete myThis.in_reload;
                },
                style: "resize:none;display:inline-block;"
            });
            var label = new ContentPane({
                content: "&nbsp;" + myThis.node.caption,
                id: "dojolabel" + myThis.node.reference,
                style: "padding:0;display: inline-block;"
            });

            pane.addChild(rb);
            pane.addChild(label);


            rb.on("focus", function () {
                if (myThis.eventHandler)
                setTimeout(function () {
                    setTimeout(function () { // since change is AFTER blur due to Dojo strange implementation
                        myThis.eventHandler({
                            source: myThis.node,
                            name: "FocusGained"
                        });
                    }, 100);
                },0);

            });
            rb.on("blur", function () {
                if (myThis.eventHandler)
                setTimeout(function () {
                    setTimeout(function () { // since change is AFTER blur due to Dojo strange implementation
                        myThis.eventHandler({
                            source: myThis.node,
                            name: "FocusLost"
                        });
                    }, 100);
                },0);
            });

            myThis.rb = rb;
            myThis.label = label;
            myThis.component = pane;
        };

        myThis.reload = function (node) {
            var oldVal = myThis.rb.checked;
            myThis.node = node;
            var newVal = (myThis.node.selected == true || myThis.node.selected == "true");

            if (oldVal != newVal) {
                myThis.in_reload = true;
                myThis.rb.set("checked", newVal);
            }
            myThis.label.set("content", "&nbsp;" + (node.caption ? node.caption : ""));
        };

        myThis.getBounds = function () {
            var retVal = common.getInitialBounds(myThis.node);
            retVal.minimumWidth = 20;

            var tw = common.getTextWidth("&nbsp;" + myThis.node.caption);
            retVal.minimumWidth += tw;
            retVal.preferredWidth = retVal.minimumWidth;

            retVal.minimumHeight = 20;
            retVal.maximumHeight = retVal.minimumHeight;

            retVal.verticalAlignment = "CENTER";
            return retVal;
        };

        myThis.layout = function (x, y, w, h) {

            dojo.style(myThis.rb, "overflow", "hidden");
            dojo.style(myThis.rb, "position", "relative");
            dojo.style(myThis.rb, "display", "inline-block");

            $(myThis.rb.domNode).css("margin-top", "-12");

            dojo.style(myThis.label, "overflow", "hidden");
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
            myThis.rb.destroy();
            myThis.label.destroy();
            myThis.component.destroy();
        };
    };
});