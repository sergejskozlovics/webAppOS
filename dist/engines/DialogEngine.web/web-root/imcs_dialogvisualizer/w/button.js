define(["dijit/form/Button", "dojo", "w/common"], function (Button, dojo, common) {

    return function (node) { // AMD class
        var myThis = this;
        myThis.type = "button";
        myThis.node = node;
        myThis.eventHandler = null;
        myThis.setEventHandler = function (f) {
            myThis.eventHandler = f;
        };

        myThis.load = function () {

            var button = new Button({
                id: "dojo" + myThis.node.reference,
                label: myThis.node.caption ? myThis.node.caption : "???",
                onClick: function () {
                    if (myThis.eventHandler)
                        myThis.eventHandler({
                            source: myThis.node,
                            name: "Click"
                        });
                },
                style: "margin:0; background-color:transparent;",
            });
            button.on("focus", function () {
                if (myThis.eventHandler)
                    setTimeout(function () { // since change is AFTER blur due to Dojo strange implementation
                        myThis.eventHandler({
                            source: myThis.node,
                            name: "FocusGained"
                        });
                    }, 0);
            });
            button.on("blur", function () {
                if (myThis.eventHandler)
                    setTimeout(function () { // since change is AFTER blur due to Dojo strange implementation
                        myThis.eventHandler({
                            source: myThis.node,
                            name: "FocusLost"
                        });
                    }, 0);
            });
            myThis.component = button;

        };

        myThis.reload = function (node) {
            myThis.node = node;
        };

        myThis.getBounds = function () {
            var retVal = common.getInitialBounds(myThis.node);
            retVal.minimumWidth = 19 + common.getTextWidth(myThis.node.caption);
            retVal.maximumWidth = retVal.minimumWidth;
            retVal.minimumHeight = 32;
            retVal.maximumHeight = retVal.minimumHeight;
            return retVal;
        };

        myThis.layout = function (x, y, w, h) {

            dojo.style(myThis.component, "position", "relative");
            dojo.style(myThis.component, "display", "inline-block");
            dojo.style(myThis.component, "padding", "0px");

            $(myThis.component.domNode).css("margin-left", x);
            $(myThis.component.domNode).css("margin-top", y);
            $(myThis.component.domNode).width(w);
            $(myThis.component.domNode).height(h);
        };

        myThis.destroy = function () {
            myThis.component.destroy();
        };
    };
});