define(["dijit/form/MultiSelect", "dojo/_base/window", "dojo", "w/common"], function (MultiSelect, win, dojo, common) {

    return function (node) { // AMD class
        var myThis = this;
        myThis.type = "listbox";
        myThis.node = node;
        myThis.eventHandler = null;
        myThis.setEventHandler = function (f) {
            myThis.eventHandler = f;
        };


        myThis.init = function () {

            var sel = win.doc.createElement('select');
            sel.id = "select" + myThis.node.reference;
            sel.style.width = "100%";
            var multiple = ((myThis.node.multiSelect == true) || (myThis.node.multiSelect == "true"));

            var items = myThis.node.items;
            if (!items)
                items = myThis.node.item;
            if (!items && myThis.node.getItem)
                items = myThis.node.getItem();
            if (!items)
                items = [];

            for (var i = 0; i < items.length; i++) {
                var opt = win.doc.createElement('option');
                opt.innerHTML = items[i].value;
                opt.id = "option" + items[i].reference;
                opt.value = items[i].reference;
                sel.appendChild(opt);
            }

            var nonEditable = (typeof myThis.node.editable != 'undefined') && ((myThis.node.editable == false) || (myThis.node.editable == "false"));

            var ms = myThis.ms;
            if (ms) {
                var w, h, x, y;
                w = dojo.style(myThis.component, "width");
                h = dojo.style(myThis.component, "height");
                x = dojo.style(myThis.component, "margin-left");
                y = dojo.style(myThis.component, "margin-top");

                // get posision of the component...
                var p = ms.getParent();
                var pos;
                var siblings = p.getChildren();
                for (var i = 0; i < siblings.length; i++)
                    if (siblings[i] == ms) {
                        pos = i;
                        break;
                    }

                p.removeChild(ms);
                ms.destroy();

                ms = new MultiSelect({
                    id: "dojo" + myThis.node.reference,
                    name: 'select' + myThis.node.reference,
                    //				  multiple : false, -- doesn't work
                }, sel);

                myThis.component = ms;
                // re-adding into the same position...
                p.addChild(ms, pos);

                dojo.style(myThis.component, "overflow", "hidden");
                dojo.style(myThis.component, "position", "relative");
                dojo.style(myThis.component, "width", w + "px");
                dojo.style(myThis.component, "height", h + "px");
                dojo.style(myThis.component, "display", "inline-block");

                dojo.style(myThis.component, "margin-left", x +
                    "px"); // relative to prev. child
                dojo.style(myThis.component, "margin-top", y +
                    "px"); // relative to prev. child
            } else {
                ms = new MultiSelect({
                    id: "dojo" + myThis.node.reference,
                    name: 'select' + myThis.node.reference,
                    //				  multiple : false, -- doesn't work
                }, sel);
                myThis.component = ms;
            }

            var valArr = [];
            var repoArr = myThis.node.selected;
            if (!repoArr && myThis.node.getSelected)
                repoArr = myThis.node.getSelected();
            if (!repoArr)
                repoArr = [];

            for (i = 0; i < repoArr.length; i++)
                valArr.push(repoArr[i].reference); // ?parseInt

            ms.set("value", valArr);

            ms.on("change", function (ids) {
                if ((ids.length > 1) && (!multiple)) {
                    ms.set("value", [ids[0]]);
                    return;
                }
                if (myThis.eventHandler)
                    myThis.eventHandler({
                        source: myThis.node,
                        name: "Change",
                        data: ids
                    });
            });

            ms.on("focus", function () {
                //$(myThis.ms.domNode).width($(myThis.component.domNode).width() - 4); 
                //$(myThis.ms.domNode).height($(myThis.component.domNode).height() - 4); 
                if (myThis.eventHandler)
                    setTimeout(function () { // since change is AFTER blur due to Dojo strange implementation
                        var ids = ms.get("value");
                        if ((ids.length > 1) && (!multiple)) {
                            ms.set("value", [ids[0]]);
                            return;
                        }
                        myThis.eventHandler({
                            source: myThis.node,
                            name: "FocusGained",
                            data: ids
                        });
                    }, 0);
            });

            ms.on("blur", function () {
                //$(myThis.ms.domNode).width($(myThis.component.domNode).width() - 2); 
                //$(myThis.ms.domNode).height($(myThis.component.domNode).height() - 2); 
                if (myThis.eventHandler)
                    setTimeout(function () { // since change is AFTER blur due to Dojo strange implementation
                        var ids = ms.get("value");
                        if ((ids.length > 1) && (!multiple)) {
                            ms.set("value", [ids[0]]);
                            return;
                        }
                        myThis.eventHandler({
                            source: myThis.node,
                            name: "FocusLost",
                            data: ids
                        });
                    }, 0);
            });


            ms.startup();

        };

        myThis.load = function () {
            myThis.init();
        };


        myThis.reload = function () {
            myThis.node = myThis.node;
            myThis.init();
        };

        myThis.getBounds = function () {
            var retVal = common.getInitialBounds(myThis.node);
            retVal.minimumWidth = 338;
            retVal.minimumHeight = myThis.component.domNode.clientHeight; // 32
            if (retVal.minimumHeight < 50)
                retVal.minimumHeight = 50;
            return retVal;
        };

        myThis.layout = function (x, y, w, h) {
            console.log("listbox ",x,y,w,h);

            $(myThis.component.domNode).css("overflow", "auto");
            $(myThis.component.domNode).css("display", "inline-block");
            $(myThis.component.domNode).css("margin-left", x);
            $(myThis.component.domNode).css("margin-top", y);
            
            $(myThis.component.domNode).css("width", w - 2);
            $(myThis.component.domNode).css("height", h - 2);

        };

        myThis.destroy = function () {
            myThis.component.destroy();
        };

    };
});