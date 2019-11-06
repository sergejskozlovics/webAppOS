define(["dojox/layout/TableContainer", "dojo", "w/common"], function (TableContainer, dojo, common) {

    return function (node) { // AMD class
        var myThis = this;
        myThis.type = "horizontalbox";
        myThis.node = node;
        myThis.eventHandler = null;
        myThis.setEventHandler = function (f) {
            myThis.eventHandler = f;
        };

        myThis.load = function () {
            var len = 0;
            if (myThis.node.component)
                len = myThis.node.component.length;
            else
            if (myThis.node.components)
                len = myThis.node.components.length;
            myThis.component =
                new TableContainer({
                    cols: len,
                    spacing: 10,
                    orientation: "horiz",
                    showLabels: false,
                    //id: "dojo" + myThis.node.reference,
                    style: "padding:0; margin:0; margin-top:0; border:0; display:inline-block;",
                });

        };
        myThis.reload = function (node) {
            myThis.node = node;
            //myThis.component.destroy();
            //myThis.load();
        };

        myThis.getBounds = function () {
            var retVal = common.getInitialBounds(myThis.node);
            retVal.minimumWidth = 10;
            retVal.minimumHeight = 10;
            //retVal.maximumHeight = 10; // will be increased
            retVal.horizontalSpacing = 10;
            retVal.verticalSpacing = 0;

            return retVal;
        };

        myThis.layout = function (x, y, w, h) {

            dojo.style(myThis.component, "position", "relative");
            //                dojo.style(myThis.component, "width", w + "px");
            //                dojo.style(myThis.component, "height", h + "px");
            $(myThis.component.domNode).width(w);
            $(myThis.component.domNode).height(h);


            dojo.style(myThis.component, "border", "0px");
            dojo.style(myThis.component, "padding", "0px");
            dojo.style(myThis.component, "border-spacing", "0px");


            $(myThis.component.domNode).width(w);
            $(myThis.component.domNode).height(h);
            $(myThis.component.domNode).css("margin-left", x);
            $(myThis.component.domNode).css("margin-top", y);

            // Legacy IMCS TDA-based tools use horizontal boxes to put a single myThis.component. In this case we should not stretch the horizontal box.
            if ((myThis.node.component && (myThis.node.component.length > 1)) ||
                (myThis.node.components && (myThis.node.components.length > 1))) {
                myThis.component.style.split("display:inline-block").join("display:block");
                setTimeout(function () {
                    require(["dojo/domReady!"], function () {
                        myThis.component.domNode.style.display = "block";
                        dojo.style(myThis.component, "display", "block");
                    });
                }, 0);
            }

        };

        myThis.destroy = function () {
            myThis.component.destroy();
        };

    };
});