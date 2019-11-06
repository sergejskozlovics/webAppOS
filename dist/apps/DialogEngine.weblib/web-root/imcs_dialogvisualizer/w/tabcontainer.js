define(["dijit/layout/TabContainer", "dojo", "w/common"], function (TabContainer, dojo, common) {

    return function (node) { // AMD class
        var myThis = this;
        myThis.type = "tabcontainer";
        myThis.node = node;
        myThis.eventHandler = null;
        myThis.setEventHandler = function (f) {
            myThis.eventHandler = f;
        };

        myThis.load = function () {
            myThis.component = new TabContainer({
                region: "center",
                id: "dojo" + myThis.node.reference,
                tabPosition: "top",
                style: "padding:0; margin:0; left:0;top:0;display:inline-block;",
            });

        };

        myThis.reload = function (node) {
            myThis.node = node;
        };

        myThis.getBounds = function () {
            // containers...
            var retVal = common.getInitialBounds(myThis.node);
            retVal.minimumWidth = 10;
            retVal.minimumHeight = 10;
            retVal.preferredWidth = 10;
            retVal.preferredHeight = 10;
            retVal.horizontalSpacing = 0;
            retVal.verticalSpacing = 10;
            retVal.bottomPadding = 0;
            retVal.topPadding =
                32;

            return retVal;
        };

        myThis.layout = function (x, y, w, h) {

            dojo.style(myThis.component, "position", "relative");
            dojo.style(myThis.component, "display", "inline-block");
            dojo.style(myThis.component, "border", "0px");
            dojo.style(myThis.component, "padding", "0px");

            $(myThis.component.domNode).css("margin-left", x);
            $(myThis.component.domNode).css("margin-top", y);
            $(myThis.component.domNode).width(w);
            $(myThis.component.domNode).height(h);

        };

        myThis.destroy = function () {
            myThis.component.destroy();
        }

    };
});