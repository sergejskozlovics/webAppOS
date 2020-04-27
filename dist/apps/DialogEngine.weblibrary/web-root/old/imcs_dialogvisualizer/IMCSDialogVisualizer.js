///// Script/CSS helpers >>> /////
function addCSS(src) {
  var headElement = document.getElementsByTagName('head').item(0);
  var s = document.createElement("link");
  s.rel = "stylesheet";
  if ((src.charAt(0) == '/') || (src.indexOf("http") != -1))
    s.href = src;
  else
    s.href = IMCS_DV_SCRIPT_FOLDER + src;
  headElement.appendChild(s);
}

var imcsdvScriptsAdded = 0;
var imcsdvScriptsStarted = false;
var imcsdvScriptsFinished = false;

function getScriptFolder(filename) {
  var scriptElements = document.getElementsByTagName('script');
  for (var i = 0; i < scriptElements.length; i++) {
    var source = scriptElements[i].src;
    if (source.indexOf(filename) > -1) {
      var location = source.substring(0, source.indexOf(filename));
      return location;
    }
  }
  return '';
}

var IMCS_DV_SCRIPT_FOLDER = getScriptFolder("IMCSDialogVisualizer.js");

function addScript(src) {
  var headElement = document.getElementsByTagName('head').item(0);
  var s = document.createElement("script");
  if ((src.charAt(0) == '/') || (src.indexOf("http") != -1))
    s.src = src;
  else
    s.src = IMCS_DV_SCRIPT_FOLDER + src;
  s.onload = function () {
    imcsdvScriptsAdded++;
    console.log("script " + src + " loaded ok");
  };
  s.onerror = function () {
    console.log("script " + src + " not loaded");
    alert("script " + src + " not loaded");
  };
  headElement.appendChild(s);
}
///// <<< Script/CSS helpers /////

if (typeof jQuery === 'undefined') {
  addScript('lib/jquery.js');
}

if (typeof IMCSDialogLayout === 'undefined')
  addScript('lib/imcs_layoutengine.js');


/**
 * Creates a new JavaScript dialog window, which is automatically laid out on the screen.<br/>
 * @constructor
 * @param {object} tree - a JavaScript/JSON object, which represents the tree of dialog components according to Dialog Engine Metamodel;
 * @param {object} settings - a JavaScript/JSON object contatining configuration settings. All settings are optional. The fields are:
 * <table cellspacing=0 cellpadding=0>
 * <tr><td>dojoPath</td><td>the path to dojo.js (default: /dojo/dojo.js) </td></tr>
 * <tr><td>themeClass</td><td>the name of the dijit theme class (the theme CSS has to be loaded in advance)</td></tr>
 * <tr><td>dialogDiv</td><td>HTML div element id, where to put the dialog; if not specified, we will create our own dialog window</td></tr>
 * <tr><td>backgroundColor</td><td>background color of the dialog window, e.g. "#f0f8ff"</td></tr>
 * <tr><td>eventHandler</td>
 *     <td>a function for handling dialog events; can receive 3 arguments:<br/>
 *         <ul>
 *         <li><b>source</b> - a tree node of the dialog component, which is the source of the given event;</li>
 *         <li><b>eventName</b> - the event name (e.g., "Click") corresponding to Dialog Engine Metamodel;</li>
 *         <li><b>value</b> - an event-specific value;</li>
 *         </ul>
 *     </td>
 * </tr>
 * </table>
 */

var IMCSDialogVisualizer = function (treeRoot, settings) { // class

  /*
  // debug>:
  let json = webmem.extract(treeRoot);
  console.log(json);
  webappos.js_util.download_string(JSON.stringify(json, null, 2), "dialog2.json");
  // < debug
  */
  
  var myThis = this;

  this.treeRoot = treeRoot;
  this.settings = settings;

  if (typeof (dojo) === "undefined") {
    // dojo has not been loaded
    console.log("Loading dojo...");
    if (settings.dojoPath)
      addScript(settings.dojoPath);
    else
      addScript('/dojo/dojo.js');
    addCSS("/webappos/djtheme/webappos.css");
  }


  this.radioButtonsToSelect = {};
  this.infoMap = {};

  this.buildInfoMap = function (node, parentNode) {
    var type = node.type;
    if (!type)
      type = node.className;
    if (!type && node.getClassName)
      type = node.getClassName();
    if (!type)
      return;

    type = type.toLowerCase();
    if (type.substring(0, 2) == "d#")
      type = type.substring(2);

    var readOnly = node.readOnly;
    if (typeof readOnly === "undefined") {
      if (parentNode)
        readOnly = myThis.infoMap[parentNode.reference].readOnly; // take from parent
      else
        readOnly = false; // default
    }

    if (typeof readOnly == "string")
      readOnly = (readOnly == "true");

    var enabled = node.enabled;
    if (typeof enabled === "undefined") {
      if (parentNode)
        enabled = myThis.infoMap[parentNode.reference].enabled; // take from parent
      else
        enabled = true; // default
    }

    if (typeof enabled == "string")
      enabled = (enabled == "true");


    var info = myThis.infoMap[node.reference];
    var b = typeof info != "undefined";
    if (!info) {
      info = {
        node: node, // tree node (JSON)
        type: type, // type name (lower-case); implementation will be taken from "w/[type].js" as an AMD module, which is a component wrapper for some (e.g., DoJo) component
        w: null, // component wrapper (loaded from "w/[type].js"); will be assigned, when loaded
      };
      myThis.infoMap[node.reference] = info;
    }

    info.parent = parentNode; // parent node (JSON); for form, undefined
    info.readOnly = readOnly;
    info.enabled = enabled;

    // considering the node.component association...
    var arr = node.component;
    if (!arr && node.getComponent)
      arr = node.getComponent();
    if (!arr)
      arr = node.components;
    if (!arr)
      return null;
    for (var i = 0; i < arr.length; i++) {
      myThis.buildInfoMap(arr[i], node);
    }
  };

  this.buildInfoMap(treeRoot);
  this.attachChildren = function (node) {

    if (!node) {
      node = myThis.treeRoot;
    }

    var parentInfo = myThis.infoMap[node.reference];

    // attaching children to this...
    var arr = myThis.callback.getChildren(node.reference);
    if (!arr)
      arr = [];

    require(["w/common"], function (common) {
      for (var i = 0; i < arr.length; i++) {
        var childInfo = myThis.infoMap[arr[i]];
        common.addChild(parentInfo.w, childInfo.w);
        myThis.attachChildren(childInfo.node); // recursive
      }

    });
  };
  this.reloadRadioButtons = function () {
    for (var groupName in myThis.radioButtonsToSelect) {
      var info = myThis.radioButtonsToSelect[groupName];
      if (info && info.w) {
        if (info.node.setSelected)
          info.node.setSelected(true);
        else
          info.node.selected = true;
        info.w.reload(info.node, info.readOnly, info.enabled);
      }
    }
    myThis.radioButtonsToSelect = {};
  };

  this.callback = { // callback for this.dialogLayout (of type IMCSDialogLayout)
    getAnchor: function (rComponent) {
      if (myThis.infoMap[rComponent].type == "tab")
        return "parent";
      else
        return "sibling";
    },
    beforeLoad: function (rComponent) {
      myThis.buildInfoMap(treeRoot);
    },
    afterLoad: function () {
      myThis.attachChildren();
      myThis.reloadRadioButtons();
    },
    load: function (rComponent, rParent) {

      var info = myThis.infoMap[rComponent];
      if (!info) {
        console.log("IMCSDialogVisualizer error: info not found for component " + rComponent);
        return;
      }


      if (!info.w) {
        myThis.dialogLayout.loadStarted(rComponent);
        require(["w/" + info.type], function (Wrapper) {
          if (info.type == "form") {
            info.w = new Wrapper(info.node, myThis.settings.divID, myThis.settings.iframe, myThis.settings.screenWidth, myThis.settings.screenHeight);
          } else
          if (info.type == "radiobutton") {
            // search for the first ascendant of type groupbox, tab, or form...
            var ainfo = info;
            while ((ainfo.type != "groupbox") && (ainfo.type != "tab") && (ainfo.type != "form") && (ainfo.parent) && (ainfo.parent.reference))
              ainfo = myThis.infoMap[ainfo.parent.reference];

            var groupName = "radiogroup_for_" + ainfo.type + "_" + ainfo.node.reference;
            var selected = info.node.selected == true || info.node.selected == "true";
            if (selected)
              myThis.radioButtonsToSelect[groupName] = null;
            else
            if (typeof myThis.radioButtonsToSelect[groupName] == "undefined")
              myThis.radioButtonsToSelect[groupName] = info;

            info.w = new Wrapper(info.node, groupName);
          } else {
            info.w = new Wrapper(info.node);
          }


          if (info.w.setEventHandler && myThis.settings.eventHandler)
            info.w.setEventHandler(myThis.settings.eventHandler);

          info.w.load(info.readOnly, info.enabled);

          $(info.w.component.domNode).addClass("imcs_" + info.type);

          info.w.component.startup();
          // load or reload called...
          require(["dojo/domReady!"], function () {
            myThis.dialogLayout.loadFinished(rComponent);
          });

        });
      } else {
        info.w.reload(info.node, info.readOnly, info.enabled);

        $(info.w.component.domNode).addClass("imcs_" + info.type + "_reloaded");

        info.w.component.startup();

      }


    },

    getChildren: function (rComponent) {
      var node = myThis.infoMap[rComponent].node;
      var arr = node.component;
      if (!arr && node.getComponent)
        arr = node.getComponent();
      if (!arr)
        arr = node.components;
      if (!arr)
        return null;
      var retVal = [];
      for (var i = 0; i < arr.length; i++) {
        retVal.push(arr[i].reference);
      }
      return retVal;
    },

    getBounds: function (rComponent) {
      var info = myThis.infoMap[rComponent];

      var retVal;
      if (info.w && info.w.getBounds) {
        retVal = info.w.getBounds();
      }

      if (!retVal) {
        // containers or non-implemented...
        retVal = {}
        retVal.minimumWidth = 10;
        retVal.minimumHeight = 10;
        //			retVal.preferredWidth = 10;
        //			retVal.preferredHeight = 10;
        retVal.horizontalSpacing = 10;
        retVal.verticalSpacing = 10;
        if (info.node.horizontalAlignment)
          if ((info.node.horizontalAlignment < 0) || (info.node.horizontalAlignment == "-1"))
            retVal.horizontalAlignment = "LEFT";
          else
        if ((info.node.horizontalAlignment > 0) || (info.node.horizontalAlignment == "+1"))
          retVal.horizontalAlignment = "RIGHT";

        if (info.node.verticalAlignment)
          if ((info.node.verticalAlignment < 0) || (info.node.verticalAlignment == "-1"))
            retVal.verticalAlignment = "TOP";
          else
        if ((info.node.verticalAlignment > 0) || (info.node.verticalAlignment == "+1"))
          retVal.verticalAlignment = "BOTTOM";
      }

      if (!retVal.skipAjustingPadding) {
        // adjusting padding to start padded children at relative coordiates (0;0)...
        if (retVal.leftPadding || retVal.rightPadding) {
          if (!retVal.leftPadding)
            retVal.leftPadding = 0;
          if (!retVal.rightPadding)
            retVal.rightPadding = 0;
          retVal.rightPadding = retVal.leftPadding + retVal.rightPadding;
          retVal.leftPadding = 0;
        }
        if (retVal.topPadding || retVal.bottomPadding) {
          if (!retVal.topPadding)
            retVal.topPadding = 0;
          if (!retVal.bottomPadding)
            retVal.bottomPadding = 0;
          retVal.bottomPadding = retVal.topPadding + retVal.bottomPadding;
          retVal.topPadding = 0;
        }
      }

      for (var key in retVal) {
        if (typeof retVal[key] == "number")
          retVal[key] = Math.ceil(retVal[key]);
      }

      return retVal;
    },

    getHorizontalRelativeInfo: function (rComponent) {
      return myThis.infoMap[rComponent].node; // will be read only (we hope)
    },

    getVerticalRelativeInfo: function (rComponent) {
      return myThis.infoMap[rComponent].node; // will be read only (we hope)
    },

    getHorizontalRelativeInfoGroup: function (rComponent) {
      var info = myThis.infoMap[rComponent];

      var c = info.node;
      if (c.relativeWidthGroup) {
        if (typeof c.relativeWidthGroup == 'number')
          return c.relativeWidthGroup;
        if (c.relativeWidthGroup.length && c.relativeWidthGroup.length > 0)
          return c.relativeWidthGroup[0].reference;
      }

      if (c.container && (c.container.length > 0))
        return c.container[0].reference;
      else
      if (info.parent)
        return info.parent.node.reference;
      else
        return 0;
    },

    getVerticalRelativeInfoGroup: function (rComponent) {
      var c = myThis.infoMap[rComponent].node;
      if (c.relativeHeightGroup) {
        if (typeof c.relativeHeightGroup == 'number')
          return c.relativeHeightGroup;
        if (c.relativeHeightGroup.length && c.relativeHeightGroup.length > 0)
          return c.relativeHeightGroup[0].reference;
      }
      if (c.container && (c.container.length > 0))
        return c.container[0].reference;
      else
      if (info.parent)
        return info.parent.node.reference;
      else
        return 0;
    },

    getLayoutName: function (rComponent) {
      var type = myThis.infoMap[rComponent].type;
      var t = null;
      if ((type == "stack") || (type == "tabcontainer"))
        t = "Stack";
      if (type == "row")
        t = "Row";
      if (type == "column")
        t = "Column";
      if (type == "horizontalscrollbox")
        t = "HorizontalScrollBox";
      if (type == "verticalscrollbox")
        t = "VerticalScrollBox";
      if (type == "scrollbox")
        t = "ScrollBox";
      if (type == "horizontalbox")
        t = "HorizontalBox";
      if ((type == "verticalbox") || (type == "tab") || (type == "groupbox") || (type == "form"))
        t = "VerticalBox";
      return t;
    },

    layout: function (rComponent, x, y, w, h) {

      var info = myThis.infoMap[rComponent];

      var d = new Date();
      console.log("imcs layout " + info.type, x, y, w, h, "time="+(d.getTime()-window.dialog_layout_started));

      info.w.layout(x, y, w, h);
      if (info.type == "form") {
        if (myThis.settings.resizeFrame)
          myThis.settings.resizeFrame(frameRef, w, h);
      }

    },

    destroy: function (rComponent) {

      var info = myThis.infoMap[rComponent];
      if (!info)
        return;

      if (info.w) {
        info.w.destroy();
        /* into destroy:
              if (info.w && info.w.component && info.w.component.getParent()) {
        info.w.component.getParent().removeChild(info.w.component);
      }
*/
        info.w = null;
      }

      delete myThis.infoMap[rComponent];
    }
  };



  this.visualize = function () {
    if (window.IMCSDialogLayout && dojo) {

      require(['dojo'], function (dojo) {
        //dojo.registerModulePath('w', '../imcs_dialogvisualizer/w');
        dojo.registerModulePath('w', IMCS_DV_SCRIPT_FOLDER + 'w');

        var d = new Date();
        window.dialog_layout_started = d.getTime();

        myThis.dialogLayout = new IMCSDialogLayout(myThis.callback);
        //        myThis.needToBuildInfoMap = true;
        myThis.dialogLayout.loadAndLayout(myThis.treeRoot.reference);
      });
    } else {
      setTimeout(function () {
        myThis.visualize();
      }, 100);
    }
  };

  this.refresh = function (r) {
    require(["dojo/domReady!"], function () {
      console.log("DV refresh; info=", myThis.infoMap[r]);
      myThis.dialogLayout.refreshAndLayout(r, 0, 0); //w, h);
    });

  };
};