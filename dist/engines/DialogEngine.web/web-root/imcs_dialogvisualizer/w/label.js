define(["dojox/layout/ContentPane", "dojo", "w/common"], function (ContentPane, dojo, common) {

    return function (node) { // AMD class
        var myThis = this;
        myThis.type = "label";
        myThis.node = node;
        myThis.eventHandler = null;
        myThis.setEventHandler = function (f) {
            myThis.eventHandler = f;
        };

        myThis.load = function () {
            myThis.component = new ContentPane({
                content: node.caption,
                id: "dojo" + node.reference,
                style: "padding:0;background-color:transparent;display:inline-block;"
            });

        };

        myThis.reload = function (node) {
            myThis.node = node;
            if (node.caption != myThis.component.content)     {
                console.log("caption1",node.caption);
                console.log("caption2", myThis.component.get("content"));
                console.log("caption3", myThis.component.content);
                myThis.component.set("content", node.caption);
            }
        };

        myThis.getBounds = function () {
            var retVal = common.getInitialBounds(myThis.node);
            retVal.minimumWidth = common.getTextWidth(myThis.node.caption);
            retVal.preferredWidth = retVal.minimumWidth;
            retVal.minimumHeight = 20;
            retVal.maximumHeight = retVal.minimumHeight;

            return retVal;
        };

        myThis.layout = function (x, y, w, h) {

            dojo.style(myThis.component, "overflow", "hidden");
            dojo.style(myThis.component, "position", "relative");
            dojo.style(myThis.component, "display", "inline-block");

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