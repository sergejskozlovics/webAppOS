define(["dojox/layout/TableContainer", "dojo", "w/common"], function (TableContainer, dojo, common) {

    return function (node) { // AMD class
        var myThis = this;
        myThis.type = "groupbox";
        myThis.node = node;
        myThis.eventHandler = null;
        myThis.setEventHandler = function (f) {
            myThis.eventHandler = f;
        };

        myThis.load = function () {
            myThis.component = new TableContainer({
                cols: 1,
                orientation: "vert",
                spacing: 10,
                showLabels: false,
                id: "dojo" + myThis.node.reference,
                style: "padding:9; margin:0; border-style: groove;  border-radius:5px; border-width:1px;display:inline-block;",
            });

        };

        myThis.reload = function (node) {
            myThis.node = node;
        };

        myThis.getBounds = function () {
            var retVal = common.getInitialBounds(myThis.node);
            retVal.minimumWidth = 10;
            retVal.minimumHeight = 10;
            retVal.horizontalSpacing = 10;
            retVal.verticalSpacing = 10;
            retVal.leftPadding = 10;
            retVal.rightPadding = 10;
            retVal.topPadding = 10;
            retVal.bottomPadding = 10;

            return retVal;
        };

        myThis.layout = function (x, y, w, h) {

            dojo.style(myThis.component, "position", "relative");
            dojo.style(myThis.component, "display", "inline-block");
            dojo.style(myThis.component, "border-spacing", "0px");

            $(myThis.component.domNode).width(w - 20);
            $(myThis.component.domNode).height(h - 20);
            $(myThis.component.domNode).css("margin-left", x);
            $(myThis.component.domNode).css("margin-top", y);
            $(".dijitTabPaneWrapper").css("height", ""); // for tab border to be visible
        };

        myThis.destroy = function () {
            myThis.component.destroy();
        };
    };
});