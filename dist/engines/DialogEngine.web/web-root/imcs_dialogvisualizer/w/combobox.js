define(["dijit/form/ComboBox", "dijit/form/Select", "dojo/data/ObjectStore", "dojo/store/Memory", "dojox/layout/ContentPane", "dojo", "w/common"], function (ComboBox, Select, ObjectStore, Memory, ContentPane, dojo, common) {

    return function (node) { // AMD class
        var myThis = this;
        myThis.type = "combobox";
        myThis.node = node;
        myThis.eventHandler = null;
        myThis.setEventHandler = function (f) {
            myThis.eventHandler = f;
        };

        myThis.init = function () { // will be used from load and reload

            if (!myThis.component) {
                myThis.component = new ContentPane({
                    id: "dojopane" + myThis.node.reference,
                    style: "padding:0;margin:0;display:inline-block;width:100%; height:100%;",
                });
            }

            var pane = myThis.component;

            var data = [];

            var items = myThis.node.items;
            if (!items)
                items = myThis.node.item;
            if (!items && myThis.node.getItem)
                items = myThis.node.getItem();
            if (!items)
                items = [];

                var ditem = tda.model["D#Item"];
                console.log("D#Iitems",ditem.getAllObjects());
            console.log("CB ITEMS", myThis.node.item, myThis.node.getItem(), items);
            for (var i = 0; i < items.length; i++) {
                data.push({
                    id: items[i].reference,
                    label: items[i].value
                });
            }

            var options = new Memory({
                data: data
            });

            var nonEditable = (typeof myThis.node.editable != 'undefined') && ((myThis.node.editable == false) || (myThis.node.editable == "false"));
            if (nonEditable) {
                var os = new ObjectStore({
                    objectStore: options
                });
                var select = new Select({
                    //                    id: "dojo" + myThis.node.reference,
                    store: os,
                    style: "padding:0;margin:0;display:inline-block;resize:none;",
                });

                if (myThis.readOnly)
                    select.set("disabled", true);
                else
                    select.on("change", function () {

                        if (!myThis.in_reload && myThis.eventHandler)
                            myThis.eventHandler({
                                source: myThis.node,
                                name: "Change",
                                data: myThis.select.get("value")
                            }); // this==select

                        delete myThis.in_reload;

                    });

                select.on("focus", function () {
                    if (myThis.eventHandler) {
                        setTimeout(function () { // since change is AFTER blur due to Dojo strange implementation
                            myThis.eventHandler({
                                source: myThis.node,
                                name: "FocusGained",
                                data: myThis.select.get("value")
                            }); // this==select
                        }, 0);
                    }
                });
                select.on("blur", function () {
                    if (myThis.eventHandler) {
                        setTimeout(function () { // since change is AFTER blur due to Dojo strange implementation
                            myThis.eventHandler({
                                source: myThis.node,
                                name: "FocusLost",
                                data: myThis.select.get("value")
                            }); // this==select
                        }, 0);
                    }
                });
                pane.addChild(select);
                myThis.select = select;

            } else {
                var os = new ObjectStore({
                    objectStore: options
                });
                var combo = new ComboBox({
                    //                    id: "dojo" + myThis.node.reference,
                    store: os,//options,
                    searchAttr: "label",
                    value: myThis.node.text,
                    style: "padding:0;margin:0;display:inline-block;resize:none;",
                });

                if (myThis.readOnly)
                    combo.set("disabled", true);

                combo.on("change", function () {
                    console.log("COMBO CURRENT VALUE IS ", myThis.combo.get("value"));
                    if (!myThis.in_reload && myThis.eventHandler)
                        myThis.eventHandler({
                            source: myThis.node,
                            name: "Change",
                            data: myThis.combo.get("value")
                        }); // this==combo

                    delete myThis.in_reload;
                });
                combo.on("focus", function () {
                    $(combo.domNode).width($(myThis.component.domNode).width() - 4); //$(combo.domNode).width()-2);
                    $(combo.domNode).height($(myThis.component.domNode).height() - 4); //$(combo.domNode).height()-2);
                    if (myThis.eventHandler)
                        setTimeout(function () { // since change is AFTER blur due to Dojo strange implementation
                            myThis.eventHandler({
                                source: myThis.node,
                                name: "FocusGained",
                                data: myThis.combo.get("value")
                            }); // this==combo
                        }, 0);

                });

                combo.on("blur", function () {
                    $(combo.domNode).width($(myThis.component.domNode).width() - 2); //($(combo.domNode).width()+2);
                    $(combo.domNode).height($(myThis.component.domNode).height() - 2); //($(combo.domNode).height()+2);
                    if (myThis.eventHandler)
                        setTimeout(function () { // since change is AFTER blur due to Dojo strange implementation
                            myThis.eventHandler({
                                source: myThis.node,
                                name: "FocusLost",
                                data: myThis.combo.get("value")
                            }); // this==combo
                        }, 0);
                });
                pane.addChild(combo);
                myThis.combo = combo;
            }

        };

        myThis.load = function () {
            myThis.init();
        };
        myThis.reload = function (node) {
            myThis.node = node;

            var oldVal;
            if (myThis.combo)
                oldVal = myThis.combo.get("value");
            else
            if (myThis.select)
                oldVal = myThis.select.get("value");
            var newVal = node.text;
            if (oldVal != newVal) {
                myThis.in_reload = true;
                myThis.init();
            }
        };

        myThis.getBounds = function () {
            var retVal = common.getInitialBounds(myThis.node);
            retVal.minimumWidth = 338;
            retVal.minimumHeight = 30; //component.domNode.clientHeight; // 32
            retVal.maximumHeight = retVal.minimumHeight;
            return retVal;
        };

        myThis.layout = function (x, y, w, h) {

            dojo.style(myThis.component, "position", "relative");
            dojo.style(myThis.component, "display", "inline-block");
            dojo.style(myThis.component, "border", "0px");
            dojo.style(myThis.component, "padding", "0px");
            dojo.style(myThis.component, "border-spacing", "0px");

            $(myThis.component.domNode).css("border", 0);
            $(myThis.component.domNode).css("padding", 0);

            $(myThis.component.domNode).css("overflow", "hidden");
            $(myThis.component.domNode).css("margin-left", x);
            $(myThis.component.domNode).css("margin-top", y);
            console.log("h=" + h);
            if (myThis.select) {
                if ($(myThis.select.domNode).hasClass("dijitFocused")) {
                    $(myThis.select.domNode).width(w - 4);
                    $(myThis.select.domNode).height(h - 4);
                } else {
                    $(myThis.select.domNode).width(w - 2);
                    $(myThis.select.domNode).height(h - 2);
                }
            }
            if (myThis.combo) {
                if ($(myThis.combo.domNode).hasClass("dijitFocused")) {
                    $(myThis.combo.domNode).width(w - 5);
                    $(myThis.combo.domNode).height(h - 4);
                } else {
                    $(myThis.combo.domNode).width(w - 3);
                    $(myThis.combo.domNode).height(h - 2);
                }
            }
            $(myThis.component.domNode).width(w);
            $(myThis.component.domNode).height(h);

        };

        myThis.destroy = function () {
            if (myThis.select)
                myThis.select.destroy();
            if (myThis.combo)
                myThis.combo.destroy();
            myThis.component.destroy();
        };

    };
});