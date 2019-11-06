define(["dojox/layout/TableContainer", "dojo", "w/common", "dojo/domReady!"], function (TableContainer, dojo, common) {

    return function (node, divID, iframe, screenWidth, screenHeight) { // AMD class
        var myThis = this;
        myThis.divID = divID;
        myThis.iframe = iframe;
        myThis.screenWidth = screenWidth;
        myThis.screenHeight = screenHeight;

        myThis.type = "form";
        myThis.node = node;
        myThis.eventHandler = null;
        myThis.setEventHandler = function (f) {
            myThis.eventHandler = f;
        };

        myThis.frameVerticalPadding = 0;
        myThis.frameHorizontalPadding = 0;

        myThis.load = function () {


            myThis.component = new TableContainer({ // vertical box
                cols: 1,
                orientation: "vert",
                spacing: 10,
                showLabels: false,
                id: "dojo" + myThis.node.reference,
                style: "padding:0; margin:0; left:0;top:0;",
            }, myThis.divID);

        };
        myThis.reload = function (node) {
            myThis.node = node;
        };
        myThis.getBounds = function () {
            var retVal = common.getInitialBounds(myThis.node);
            if (myThis.iframe) {
                var formBody = $(myThis.iframe).parent();
                var title = $(myThis.iframe).parent().parent().children().find(":first-child");
                retVal.topPadding = /*parseInt(title.css("padding-top"),10)*/ 2 + parseInt(title
                        .css("height"), 10) + /*parseInt(title.css("padding-bottom"),10)*/ 2 +
                    parseInt(formBody.css("padding-top"), 10);
                retVal.leftPadding = parseInt(formBody.css("padding-left"), 10);
                retVal.rightPadding = parseInt(formBody.css("padding-right"), 10);
                retVal.bottomPadding = parseInt(formBody.css("padding-bottom"), 10);
            } else {
                retVal.topPadding = 0;
                retVal.leftPadding = 0;
                retVal.rightPadding = 0;
                retVal.bottomPadding = 0;
            }

            retVal.minimumWidth = retVal.leftPadding + retVal.rightPadding + 100;
            retVal.minimumHeight = retVal.topPadding + retVal.bottomPadding + 100;

            retVal.preferredWidth = 0; // minimize width
            retVal.preferredHeight = 0; // minimize height

            retVal.horizontalSpacing = 0;
            retVal.verticalSpacing = 10;

            retVal.verticalAlignment = "TOP";

            frameVerticalPadding = retVal.topPadding + retVal.bottomPadding;
            frameHorizontalPadding = retVal.leftPadding + retVal.rightPadding;
            return retVal;
        };

        myThis.layout = function (x, y, w, h) {
            if (myThis.iframe)
                $(document.body).css("opacity", "1");

            var ww = w;
            var hh = h;

            var frameBorderWidth = frameHorizontalPadding; //?scrollbar width?
            var frameHeaderHeight = frameVerticalPadding; //50;

            var browserWidth = myThis.screenWidth;
            var browserHeight = myThis.screenHeight;


            if (h > browserHeight - 30) {
                h = browserHeight - 30 -
                    frameHeaderHeight; // do not count the frame header, but take into a consideration browser height
                w += 17; // add vertical scrollbar width
            } else {
                //				  frameBorderWidth = the same; // do not use scrollbar
                h = h - frameHeaderHeight; // do not count the frame header
            }

            if (w > browserWidth)
                w = browserWidth - frameBorderWidth;
            else
                w = w - frameBorderWidth;

/*                $(myThis.component.domNode).width(w);
                $(myThis.component.domNode).height(h);*/
    
            if (myThis.iframe) {



                var N = 1;//10;
                var curX = parseInt($(myThis.iframe).parent().parent().css("left"), 10);
                var curY = parseInt($(myThis.iframe).parent().parent().css("top"), 10);
                var curW = parseInt($(myThis.iframe).parent().parent().css("width"), 10);
                var curH = parseInt($(myThis.iframe).parent().parent().css("height"), 10);
                var dx = ((browserWidth - w - frameBorderWidth) / 2 - curX) / N;
                var dy = (15 + (browserHeight - 30 - h - frameHeaderHeight) / 2 - curY) / N;
                var dw = ((w + frameBorderWidth) - curW) / N;
                var dh = ((h + frameHeaderHeight) - curH) / N;
                for (var i = 1; i <= N; i++) {
                    setTimeout(function (i) {
                        $(myThis.iframe).parent().parent().css("width", curW + dw * i);
                        $(myThis.iframe).parent().parent().css("height", curH + dh * i);
                        $(myThis.iframe).css("width", curW + dw * i-frameHorizontalPadding);
                        $(myThis.iframe).css("height", curH + dh * i-frameVerticalPadding);
                        $(myThis.iframe).parent().parent().css("left", curX + dx * i);
                        $(myThis.iframe).parent().parent().css("top", curY + dy * i);
                        $(myThis.iframe).parent().parent().removeClass("dijitHidden");
                    }, 50*i, i);
                }


                setTimeout(function() {
                    //$(myThis.component.domNode).width(ww);
                    //$(myThis.component.domNode).height(hh-frameHeaderHeight);
                    //$(myThis.iframe).parent().css("overflow", "scroll");
                },50*(N+1));
            }
        };

        myThis.destroy = function () {
            myThis.component.destroy();
        };
    };
});