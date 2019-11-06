define(["dojox/layout/TableContainer", "dojo", "w/common"], function (TableContainer, dojo, common) {

    return function (node) { // AMD class
        var myThis = this;
        myThis.type = "tab";
        myThis.node = node;
        myThis.eventHandler = null;
        myThis.setEventHandler = function (f) {
            myThis.eventHandler = f;
        };

        myThis.load = function () {
            myThis.component = new TableContainer({
                cols: 1,
                orientation: "vert",
                showLabels: false,
                title: myThis.node.caption,
                id: "dojo" + myThis.node.reference,
                style: "padding:0; margin:0;display:inline-block;",
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
            retVal.leftPadding = 10;
            retVal.rightPadding = 10;
            retVal.topPadding = 10;
            retVal.bottomPadding = 10;

            retVal.skipAjustingPadding = true; // do not adjust paddings
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