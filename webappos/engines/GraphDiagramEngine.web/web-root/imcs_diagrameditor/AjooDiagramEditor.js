///// Helper functions for loading dependencies /////

function addCSS(src) {
  var headElement = document.getElementsByTagName('head').item(0);
  var s = document.createElement("link");
  s.rel = "stylesheet";
  if ((src.charAt(0) == '/') || (src.indexOf("http") != -1))
    s.href = src;
  else
    s.href = SCRIPT_FOLDER + src;
  headElement.appendChild(s);
}

var scriptsAdded = 0;
var scriptsStarted = false;
var scriptsFinished = false;

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

var SCRIPT_FOLDER = getScriptFolder("AjooDiagramEditor.js");

function addScript(src) {
  var headElement = document.getElementsByTagName('head').item(0);
  var s = document.createElement("script");
  if ((src.charAt(0) == '/') || (src.indexOf("http") != -1))
    s.src = src;
  else
    s.src = SCRIPT_FOLDER + src;
  s.onload = function () {
    scriptsAdded++;
  };
  s.onerror = function () {
    console.log("script " + src + " not loaded");
    alert("script " + src + " not loaded");
  };
  headElement.appendChild(s);
}

function whenLibsLoaded(f, number) {
  if (typeof number == 'undefined')
    number = 50;
  if (scriptsStarted) {
    // checking if loaded...
    var condition;
    if (number == 50)
      condition = ((scriptsAdded < number) || (typeof AjooEditor == 'undefined') || (typeof Konva == 'undefined') || (typeof Box == 'undefined') || (typeof ACircle == 'undefined'));
    else
      condition = (scriptsAdded < number);

    if (condition) {
      setTimeout(function () {
        whenLibsLoaded(f, number);
      }, 200);
    } else {
      scriptsFinished = true;
      f();
    }
  } else {
    scriptsStarted = true;

    // we assume that the dojo.js script is already loaded.
    // 23 scripts initially
    addScript("lib/konva.min.js");
    addScript("lib/underscore-min.js");
    addScript("az.js");

    addScript("ajoo/Editor/grid.js");
    addScript("ajoo/Editor/palette.js");
    addScript("ajoo/Editor/zooming.js");
    addScript("ajoo/Editor/mode.js");
    addScript("ajoo/Editor/layers.js");
    addScript("ajoo/Editor/actions.js");
    addScript("ajoo/Editor/selectionStyle.js");
    addScript("ajoo/Editor/mouseState.js");
    addScript("ajoo/Editor/size.js");
    addScript("ajoo/Editor/events.js");
    addScript("ajoo/Editor/panning.js");
    addScript("ajoo/Editor/connectionPoints.js");

    addScript("ajoo/Elements/elements.js");
    addScript("ajoo/Elements/element_handlers.js");

    addScript("ajoo/Elements/Boxes/box_compartments.js");
    addScript("ajoo/Elements/Boxes/draw_new_box.js");

    addScript("ajoo/Elements/Boxes/resizing.js");
    addScript("ajoo/Elements/Boxes/add_remove_resizers.js");

    addScript("ajoo/Elements/Boxes/DrawingShapes/_render_boxes.js");

    whenLibsLoaded(function () { // 29 scripts inside
      addScript("ajoo/Elements/Boxes/DrawingShapes/shapes1.js");
      whenLibsLoaded(function () { // 28 scripts inside
        addScript("ajoo/Elements/Boxes/DrawingShapes/shapes2.js");
        addScript("ajoo/Elements/Boxes/DrawingShapes/shapes3.js");
        addScript("ajoo/Elements/Boxes/DrawingShapes/swimlane.js");

        addScript("ajoo/Elements/Lines/draw_new_line.js");
        addScript("ajoo/Elements/Lines/line_compartments.js");
        addScript("ajoo/Elements/Lines/lineEndShape.js");
        addScript("ajoo/Elements/Lines/render_lines.js");

        addScript("ajoo/Elements/Lines/routing/svg_collisions.js");
        addScript("ajoo/Elements/Lines/routing/IntersectionUtilities.js");
        addScript("ajoo/Elements/Lines/routing/orthogonal_rerouting.js");
        addScript("ajoo/Elements/Lines/routing/line_dragging.js");

        addScript("ajoo/Elements/Lines/routing/line_routing_boxInfo.js");
        addScript("ajoo/Elements/Lines/routing/line_routing_connArea.js");
        addScript("ajoo/Elements/Lines/routing/line_routing_graphInfo.js");
        addScript("ajoo/Elements/Lines/routing/line_routing_other.js");
        addScript("ajoo/Elements/Lines/routing/line_routing_pathInfo.js");
        addScript("ajoo/Elements/Lines/routing/line_routing_pointInfo.js");
        addScript("ajoo/Elements/Lines/routing/line_routing_segInfo.js");


        addScript("ajoo/Elements/Swimlane/add_compartment.js");
        addScript("ajoo/Elements/Swimlane/render.js");
        addScript("ajoo/Elements/Swimlane/moving.js");

        addScript("ajoo/Selection/select.js");
        addScript("ajoo/Selection/selection_rect.js");
        addScript("ajoo/Selection/unselect.js");
        addScript("ajoo/Selection/selection_dragging.js");

        addScript("ajoo/AjooEditor.js");
        addScript("ajoo/keystrokes.js");

        addCSS("lib/bootstrap.min.css");
        addCSS("lib/bootstrap-theme.min.css");

        addScript("lib/bootstrap.min.js");
        whenLibsLoaded(f);
      }, 22);
    }, 21);
  }
}

///// Helper function to transform palette /////

function transformPalette(AZPalette) {
  // AZPalette is array of PaletteElement objects (synced from the repository)
  var retVal = [];

  for (var i = 0; i < AZPalette.length; i++) {

    var p = {};
    p._id = "PaletteElement" + AZPalette[i].reference;

    var className = AZPalette[i].className;
    if (!className && !(typeof AZPalette[i].getClassName === 'undefined'))
      className = AZPalette[i].getClassName();
    if (className == "PaletteBox") {
      p.defaultSize = {
        width: 110,
        height: 49
      };
      p.type = "Box";
    } else
    if (className == "PaletteLine") {
      p.type = "Line";
      p.lineType = "Orthogonal";
    } else
      continue;


    if (p.type == "Box") {
      p.style = {
        elementStyle: {
          fill: "rgb(255,100,100)", //"#ffff80",
          /*                                       // fillLinearGradientColorStops: Array[4]
                                                  fillLinearGradientEndPointX: 0.5,
                                                  fillLinearGradientEndPointY: 1,
                                                  fillLinearGradientStartPointX: 0.5,
                                                  fillLinearGradientStartPointY: 0,
                                                  fillPriority: "color",
                                                 // fillRadialGradientColorStops: Array[4]
                                                  fillRadialGradientEndPointX: 0.5,
                                                  fillRadialGradientEndPointY: 0.5,
                                                  fillRadialGradientEndRadius: 1,
                                                  fillRadialGradientStartPointX: 0.5,
                                                  fillRadialGradientStartPointY: 0.5,
                                                  fillRadialGradientStartRadius: 0,*/
          opacity: 1,
          /*                                        shadowBlur: 0,
                                                  shadowColor: "red",
                                                  shadowOffsetX: 0,
                                                  shadowOffsetY: 0,
                                                  shadowOpacity: 1,*/
          shape: "Rectangle",
          stroke: "#000000",
          strokeWidth: "1",
          tension: 0,
        },
      };
    } else
    if (p.type == "Line") {
      p.style = {
        elementStyle: {
          //opacity: 1,
          /*                                                    shadowBlur: 0,
                                                              shadowColor: "red",
                                                              shadowOffsetX: 0,
                                                              shadowOffsetY: 0,
                                                              shadowOpacity: 1,*/
          stroke: "rgb(255,100,100)", //"rgb(65,113,156)",
          strokeWidth: 2,
          tension: 0,
        },
        endShapeStyle: {

          fill: "rgb(255,100,100)", //"rgb(65,113,156)",
          fillPriority: "color",
          opacity: 1,
          radius: 12,
          shadowBlur: 0,
          shadowColor: "red",
          shadowOffsetX: 0,
          shadowOffsetY: 0,
          shadowOpacity: 1,
          shape: "None",
          //                                                  shape: "Triangle",
          stroke: "rgb(255,100,100)", //"rgb(65,113,156)",
          strokeWidth: 1,
          tension: 0,
        },

        startShapeStyle: {
          // dash: Array[0],
          fill: "rgb(255,100,100)", //"rgb(65,113,156)",
          fillPriority: "color",
          opacity: 1,
          radius: 7,
          /*                                                  shadowBlur: 0,
                                                            shadowColor: "red",
                                                            shadowOffsetX: 0,
                                                            shadowOffsetY: 0,
                                                            shadowOpacity: 1,*/
          //                                                  shape: "Triangle",
          shape: "None",
          stroke: "rgb(255,100,100)", //"rgb(65,113,156)",
          strokeWidth: 1,
          tension: 0,
        },
        lineType: "Orthogonal",
      };
    }

    var s = AZPalette[i].picture.split(".BMP").join(".bmp");
    p.style.elementStyle.imageSrc = SCRIPT_FOLDER + "images/" + s;
    p.style.imageSrc = SCRIPT_FOLDER + "images/" + s;

    p.data = {
      //       picture: "/images/"+AZPalette[i].picture,
      elementTypeId: p._id // we are passing palette element id as type-indicator for the new box
    };


    retVal.push(p);
  }

  return retVal;
};


function AjooDiagramEditor(settings) { // class
  // CONSTRUCTOR...
  var myThis = this;
  this.settings = settings;

  this.delayedData = null;
  this.fAfterArr = [];
  this.lastProcessCall = null;

  this.lastClickedElementReference = 0;

  // METHODS...
  this.processDelayedData = function (fAfter, rightAway, fromTimeout) {
    if (this.delayedData == null)
      return;

    // if the box just added was already in the diagram (with the saved lines),
    // we need to process delayedData right away...
    var d = new Date();
    var curTime = d.getTime();

    if (fAfter)
      this.fAfterArr.push(fAfter);

    if (!rightAway && (this.lastProcessCall == null)) {
      this.lastProcessCall = curTime;
      // timeout not launched earlier; launching...
      setTimeout(function (myThis) {
        myThis.processDelayedData(null, false, true);
      }, 100, this);
    } else {
      // timeout launched earlier...
      if (!rightAway && (curTime - this.lastProcessCall < 100)) {
        if (fromTimeout) { // re-schedule
          //            console.log("FROM t/o",curTime-this.lastProcessCall);
          setTimeout(function (myThis) {
            myThis.processDelayedData(null, false, true);
          }, 100, this);
        } else {
          //            console.log("not t/o",curTime-this.lastProcessCall);
          this.lastProcessCall = curTime;
        }
      } else { // rightAway or timeout...
        // adding...
        var dd = new Date();
        var time1 = dd.getTime();
        _EDITOR.addElements(this.delayedData, true);
        var dd2 = new Date();
        var time2 = dd2.getTime();
        console.log("ADDED DELAYED ELMS " + time2 + " IN " + (time2 - time1));
        for (var i = 0; i < this.fAfterArr.length; i++)
          this.fAfterArr[i]();
        this.fAfterArr = [];
        this.delayedData = null;
        this.lastProcessCall = null;
        var arr = _EDITOR.getElements();
        console.log(_EDITOR);

        _.each(arr, function (el) {
          //             console.log("cache",el);
          if (el.type == "Box") {
            try {
              /*                   el.presentation.setAttrs({listening:true});
                                 el.presentation.cache();
                                 el.presentation.setAttrs({listening:true});*/
              /*                   _.each(el.presentation.children[1].children, function(ch){
                                    ch.setAttrs({listening:true});
                                 });*/
            } catch (t) {
              console.log(t);
            }
          } else
          if (el.type == "Line") {
            try {
              //                  el.presentation.children[2].cache();
              //                el.presentation.children[2].x(el.presentation.children[2].x()+100);
            } catch (t) {
              console.log(t);
            }
          }
        });
      }
    }
  };

  this.whenReady = function (f) {
    if (this.delayedData) {
      console.log("ajoo_wrapper: not all added", this.delayedData.boxes.length, this.delayedData.lines.length);
      setTimeout(f, 200);
      return;
    } else {
      console.log("ajoo_wrapper ready, delayedData = " + this.delayedData);
      f();
    }
  };

  this.addNode = function (node) {
    console.log("ADDBOX");

    /* if (this.added)
      return;
          this.added = true;  */

    if (this.delayedData == null)
      this.delayedData = {
        boxes: [],
        lines: []
      };

    var savedInLines = null;
    var savedOutLines = null;
    var elms = _EDITOR.getElements();
    if (elms[node.className + node.reference]) {
      savedInLines = elms[node.className + node.reference].inLines;
      if (!savedInLines)
        savedInLines = null;
      savedOutLines = elms[node.className + node.reference].outLines;
      if (!savedOutLines)
        savedOutLines = null;
      elms[node.className + node.reference].inLines = null;
      elms[node.className + node.reference].outLines = null;
      _EDITOR.removeElements([node.className + node.reference]);
    }

    var box = {
      _id: node.className + node.reference,
      type: "Box",
      location: {
        height: node.location.getHeight(),
        width: node.location.getWidth(),
        x: node.location.getX(),
        y: node.location.getY(),
      },



      style: {
        elementStyle: {
          fill: node.style.getFillColor(),
          opacity: 1,
          stroke: node.style.getLineColor(),
          strokeWidth: node.style.getLineWidth(),
          shape: node.style.getShape(),


        },
      },

      compartments: [],
    };

    if (node.style.getDashLength() && node.style.getDashBreakLength()) {
      box.style.elementStyle.dash = [node.style.getDashLength(), node.style.getDashBreakLength()];
    }

    for (var j = 0; j < node.compartment.length; j++) {
      var cmpr = {
        _id: node.compartment[j].reference,
        input: "cmpr" + node.compartment[j].reference,
        value: node.compartment[j].input, //.split("\n").join(""),
        style: {
          align: node.compartment[j].style.getAlignment(),
          fill: node.compartment[j].style.getFontColor(),
          fontFamily: node.compartment[j].style.getFontFamily(),
          fontSize: Math.round(node.compartment[j].style.getFontSizeInPt() * 1.45),
          fontStyle: node.compartment[j].style.getFontWeight(),
          fontVariant: "normal",
          //                               padding: node.compartment[j].style.getOffsetX(),
          //                               placement: node.compartment[j].style.getPlacement(),
          strokeWidth: node.compartment[j].style.getLineWidth(),
          visible: node.compartment[j].style.getIsVisible()
        }
      };
      if ((cmpr.style.visible) && (cmpr.value))
        box.compartments.push(cmpr);
    }

    this.delayedData.boxes.push(box);

    var rightAway = (savedInLines != null) || (savedOutLines != null);
    this.processDelayedData(function () {
      elms = _EDITOR.getElements();
      if (savedInLines) {
        elms[node.className + node.reference].inLines = savedInLines;
      }
      if (savedOutLines) {
        elms[node.className + node.reference].outLines = savedOutLines;
      }

      var box = elms[node.className + node.reference];
      console.log("ABOX");
      /*        _EDITOR.stage.cache({
                x: node.location.getX(),
                y: node.location.getY(),
                width: node.location.getWidth(),
                height: node.location.getHeight(),
                offset : 10,
                drawBorder: true
              });
      Konva warning: Cache function is not allowed for stage. You may use cache only for layers, groups and shapes.
      */

    }, rightAway);
    if (box.compartments && box.compartments.compartments && (box.compartments.compartments.length > 0) && box.compartments.compartments[0].textsParent) {
      // smart adjust...
      var h = box.compartments.compartments[0].textsParent.attrs.y; // y0
      for (var i = 0; i < box.compartments.compartments.length; i++) {
        h += box.compartments.compartments[i].textHeight;
      }
      if (h > node.location.getHeight())
        node.location.setHeight(h);
    }

  };

  this.addEdge = function (edge) {

    this.removeElements([edge]); // remove the previous edge with the same edge.reference

    if (this.delayedData == null)
      this.delayedData = {
        boxes: [],
        lines: []
      };
    var line = {
      _id: edge.className + edge.reference,
      startElement: edge.start[0].className + edge.start[0].reference,
      endElement: edge.end[0].className + edge.end[0].reference,
      compartments: [],
      points: edge.location.getPointsArray(),
      style: {
        elementStyle: {
          stroke: edge.style.getLineColor(),
          strokeWidth: edge.style.getLineWidth(),
        },
        startShapeStyle: {
          shape: edge.style.getStartLabelStyle().getShape(),
          stroke: edge.style.getStartLabelStyle().getLineColor(),
          strokeWidth: edge.style.getStartLabelStyle().getLineWidth(),
          radius: 10,
          width: 10,
          height: 10,
          fill: edge.style.getStartLabelStyle().getFillColor(),

        },
        endShapeStyle: {
          shape: edge.style.getEndLabelStyle().getShape(),
          stroke: edge.style.getEndLabelStyle().getLineColor(),
          strokeWidth: edge.style.getEndLabelStyle().getLineWidth(),
          radius: 10,
          width: 10,
          height: 10,
          fill: edge.style.getEndLabelStyle().getFillColor(),
        },
        lineType: "Orthogonal",
      }
    };

    if (edge.style.getDashLength() && edge.style.getDashBreakLength()) {
      line.style.elementStyle.dash = [edge.style.getDashLength(), edge.style.getDashBreakLength()];
    }


    for (var j = 0; j < edge.compartment.length; j++) {
      if (typeof edge.compartment[j].input == "undefined")
        edge.compartment[j].input = "";
      var w = edge.compartment[j].style.getWidth(); //getTextWidth(edge.compartment[j].input, edge.compartment[j].style.getFontFamily()+" "+edge.compartment[j].style.getFontSizeInPt()+"pt")+8;
      var h = getFontHeight(edge.compartment[j].style.getFontFamily(), edge.compartment[j].style.getFontSizeInPt() * 1.45 + 0.5) + 2;
      var cmpr = {
        _id: edge.compartment[j].reference,
        input: "cmpr" + edge.compartment[j].reference,
        value: edge.compartment[j].input.split("\n").join(""),
        style: {
          //                               align: edge.compartment[j].style.getAlignment(),
          fill: edge.compartment[j].style.getFontColor(),
          fontFamily: edge.compartment[j].style.getFontFamily(),
          fontSize: Math.round(edge.compartment[j].style.getFontSizeInPt() * 1.45 + 0.5),
          fontStyle: edge.compartment[j].style.getFontWeight(),
          fontVariant: "normal",
          //                               padding: node.compartment[j].style.getOffsetX(),
          placement: edge.compartment[j].style.getPlacement(),
          strokeWidth: edge.compartment[j].style.getLineWidth(),
          visible: edge.compartment[j].style.getIsVisible(),
          width: w, // SK_LABEL
          height: h, // SK_LABEL
        }
      };
      //        if ((cmpr.style.visible) && (cmpr.value))
      line.compartments.push(cmpr);


    }
    this.delayedData.lines.push(line);
    this.processDelayedData(function () {
      console.log("ALINE");
    }, true); // trigger delayed data processing...

  };


  this.selectElements = function (selected) {
    this.whenReady(function () {
      _EDITOR.unSelectElements(_EDITOR.getSelectedElements(), false);

      var elms = _EDITOR.getElements();
      var list = [];
      for (var q = 0; q < selected.length; q++) {
        var r = selected[q].reference;
        if (("Node" + r) in elms) {
          list.push(elms["Node" + r]);
          this.lastClickedElementReference = r;
        } else
        if (("Edge" + r) in elms) {
          list.push(elms["Edge" + r]);
          this.lastClickedElementReference = r;
        } else
        if (("Label" + r) in elms) {
          list.push(elms["Label" + r]);
          this.lastClickedElementReference = 0; //r;
        }

      }
      _EDITOR.selectElements(list, false);
    });
  };

  this.getNodes = function () {
    var elms = _EDITOR.getElements();
    var list = [];
    for (var q in elms) {
      if (q.substring(0, 4) == "Node") {
        list.push({
          reference: parseInt(q.substring(4))
        });
      }
    }
    return list;
  };

  this.getEdges = function () {
    var elms = _EDITOR.getElements();
    var list = [];
    for (var q in elms) {
      if (q.substring(0, 4) == "Edge") {
        list.push({
          reference: parseInt(q.substring(4))
        });
      }
    }
    return list;
  };

  this.getLabels = function () {
    var elms = _EDITOR.getElements();
    var list = [];
    for (var q in elms) {
      if (q.substring(0, 5) == "Label") {
        list.push({
          reference: parseInt(q.substring(4))
        });
      }
    }
    return list;
  };

  this.removeElements = function (arr) { // array of {reference:123}
    // when removing a node, remove also its inLines and outLines

    var data = [];
    var elms = _EDITOR.getElements();

    for (var q = 0; q < arr.length; q++) {
      var r = arr[q].reference;
      if (("Node" + r) in elms) {
        data.push("Node" + r);
      } else
      if (("Edge" + r) in elms)
        data.push("Edge" + r);
      else
      if (("Label" + r) in elms)
        data.push("Label" + r);
    }

    _EDITOR.removeElements(data);
  };

  this.getVisibleWidth = function () {
    return _EDITOR.getLayer("ShapesLayer").getWidth();
  };

  this.getTotalWidth = function () {
    return this.w;
  };

  this.getVisibleHeight = function () {
    return _EDITOR.getLayer("ShapesLayer").getHeight();
  };

  this.getTotalHeight = function () {
    return this.h;
  };

  this.getZoomFactor = function () {
    return _EDITOR.getZoom().x;
  };

  this.zoomIn = function (mouseX, mouseY) {
    if (!mouseX)
      mouseX = 0;
    if (!mouseY)
      mouseY = 0;
    var x = this.getX();
    var y = this.getY();
    var dx = mouseX / this.getZoomFactor();
    var dy = mouseY / this.getZoomFactor();
    // now: the screen point (mouseX, mouseY) must be non-zoomed diagram point (-x+dx, -y+dy)

    _EDITOR.zoomIn();

    var dx2 = mouseX / this.getZoomFactor();
    var dy2 = mouseY / this.getZoomFactor();
    var x = (-x + dx) - dx2;
    var y = (-y + dy) - dy2;
    // now: the screen point (mouseX, mouseY) must be non-zoomed diagram point (-x+dx2, -y+dy2)

    this.setX(-x);
    this.setY(-y);
  };

  this.zoomOut = function (mouseX, mouseY) {
    if (!mouseX)
      mouseX = 0;
    if (!mouseY)
      mouseY = 0;
    var x = this.getX();
    var y = this.getY();
    var dx = mouseX / this.getZoomFactor();
    var dy = mouseY / this.getZoomFactor();
    // now: the screen point (mouseX, mouseY) must be non-zoomed diagram point (-x+dx, -y+dy)

    _EDITOR.zoomOut();

    var dx2 = mouseX / this.getZoomFactor();
    var dy2 = mouseY / this.getZoomFactor();
    var x = (-x + dx) - dx2;
    var y = (-y + dy) - dy2;
    // now: the screen point (mouseX, mouseY) must be non-zoomed diagram point (-x+dx2, -y+dy2)

    this.setX(-x);
    this.setY(-y);
  };

  this.getX = function () { // returns as if there was no zoom factor
    return _EDITOR.stage.x() / this.getZoomFactor();
  };

  this.getY = function () { // returns as if there was no zoom factor
    return _EDITOR.stage.y() / this.getZoomFactor();
  };

  this.setX = function (newX) {
    var x = this.getX();
    if (x == newX)
      return;
    newX = newX * this.getZoomFactor();

    var maxToTheLeftZoomed = (this.getTotalWidth() + 100) * this.getZoomFactor() - this.getVisibleWidth();
    var maxToTheLeft = maxToTheLeftZoomed /*/this.getZoomFactor();*/
    if (newX < -maxToTheLeft)
      newX = -maxToTheLeft;
    if (newX > 0)
      newX = 0;

    _EDITOR.stage.x(newX);
  };

  this.setY = function (newY) {
    var y = this.getY();
    if (y == newY)
      return;
    newY = newY * this.getZoomFactor();

    var maxToTheTopZoomed = (this.getTotalHeight() + 60) * this.getZoomFactor() - this.getVisibleHeight();
    var maxToTheTop = maxToTheTopZoomed /*/this.getZoomFactor();*/
    if (newY < -maxToTheTop)
      newY = -maxToTheTop;
    if (newY > 0)
      newY = 0;

    _EDITOR.stage.y(newY);
  };

  this.getRelativeX = function () { // returns 0..1 denoting the scrollbar position
    var maxToTheLeft = (this.getTotalWidth() + 100) * this.getZoomFactor() - this.getVisibleWidth();
    var x = -this.getX() * this.getZoomFactor();
    return x / maxToTheLeft;
  };

  this.getRelativeY = function () { // returns 0..1 denoting the scrollbar position
    var maxToTheTop = (this.getTotalHeight() + 60) * this.getZoomFactor() - this.getVisibleHeight();
    var y = -this.getY() * this.getZoomFactor();
    return y / maxToTheTop;
  };

  this.setRelativeX = function (rx) { // rx from 0..1 denoting the scrollbar position
    var maxToTheLeft = (this.getTotalWidth() + 100) * this.getZoomFactor() - this.getVisibleWidth();
    this.setX(-maxToTheLeft * rx / this.getZoomFactor());
  };

  this.setRelativeY = function (ry) { // ry from 0..1 denoting the scrollbar position
    var maxToTheTop = (this.getTotalHeight() + 60) * this.getZoomFactor() - this.getVisibleHeight();
    this.setY(-maxToTheTop * ry / this.getZoomFactor());
  };

  this.setVisibleWidth = function (newW) {
    _EDITOR.stage.width(newW);
  };

  this.initialized = function () {
    return scriptsFinished && (window._EDITOR != null) && (typeof window._EDITOR != 'undefined');
  };

  this.setVisibleHeight = function (newH) {
    _EDITOR.stage.height(newH);
    _EDITOR.palette.paletteLayer.y(0);
    _EDITOR.paletteStage.height(newH);
    $("#ajoo_scene").height(newH);
  };

  this.updateNodeLocation = function (r, x, y, w, h) {
    var elms = _EDITOR.getElements();
    var node = elms["Node" + r];
    if (node) {
      node.updateElementSize(x, y, x + w, y + h);
      node.setElementPosition(x, y);
    }
  };

  this.updateLabelLocation = function (r, x, y, w, h) {
    var elms = _EDITOR.getElements();
    var node = elms["Label" + r];
    if (node) {
      node.updateElementSize(x, y, x + w, y + h);
      node.setElementPosition(x, y);
    }
  };

  this.updateEdgeLocation = function (r, arr) {
    var elms = _EDITOR.getElements();
    var edge = elms["Edge" + r];

    if (edge) {
      edge.setPoints(arr);
    }
  };

  this.refresh = function (w, h) {
    // update diagram size

    if (!h) {
      var elms = _EDITOR.getElements();
      var maxX = 0;
      var maxY = 0;
      for (s in elms) {
        var ss = s.substring(0, 4);
        if ((ss == "Node") || (s.substring(0, 5) == "Label")) {
          var node = elms[s];
          var curX = node.presentation.attrs.x + node.width;
          if (curX > maxX)
            maxX = curX;
          var curY = node.presentation.attrs.y + node.height;
          if (curY > maxY)
            maxY = curY;
        }
        if (ss == "Edge") {
          var edge = elms[s];
          var arr = edge.line.attrs.points;
          for (var i = 0; i < arr.length; i++) {
            if (i % 2 == 0) { // x
              if (arr[i] > maxX)
                maxX = arr[i];
            } else { // y
              if (arr[i] > maxY)
                maxY = arr[i];
            }
          }
        }
      }

      w = maxX + 1;
      h = maxY + 1;
    }

    this.w = w;
    this.h = h;
    this.repaint();
  };

  this.repaint = function () {
    var d1 = new Date();
    var t1 = d1.getTime();
    var L = _EDITOR.getLayer("ShapesLayer");
    L.batchDraw();
    _EDITOR.getLayer("DragLayer").draw();
    _EDITOR.getLayer("DrawingLayer").draw();
    _EDITOR.getLayer("GridLayer").draw();
    _EDITOR.getLayer("SwimlaneLayer").draw();
    if (this.settings.onRepaint) {
      this.settings.onRepaint(this);
    }
  };


  this.scrollPalette = function(ev) {
    if (ev.x <= 40) {
      // palette scroll...
      var maxY = 0;
      for (var i=0; i<_EDITOR.palette.paletteLayer.children.length; i++) {
        if (_EDITOR.palette.paletteLayer.children[i].y() > maxY)
          maxY = _EDITOR.palette.paletteLayer.children[i].y();
      }
      maxY += 60; // the last palette item

      var minTop = _EDITOR.palette.paletteLayer.height()-maxY;
      if (minTop > 0)
        minTop = 0;
      var val = _EDITOR.palette.paletteLayer.y();
      if (ev.deltaY < 0) {
        val = val+10;
      }
      if (ev.deltaY > 0) {
        val = val-10;
      }
      if (val > 0)
        val=0;
      if (val < minTop)
        val=minTop;

      _EDITOR.palette.paletteLayer.y(val);
      _EDITOR.palette.refresh()
      return true;
   }
   else
      return false;
  };
  
  // CONSTRUCTOR CONTINUED...

  var data = {
    boxes: [],
    lines: []
  };
  var palette = transformPalette(settings.paletteElements);


  var ajoo_settings = {
    container: settings.diagramContentDiv,

    selectionStyle: {
      fill: "grey",
      opacity: 0.4,
      stroke: "black",
      strokeWidth: 0.6
    },

    isGrid: true,
    isEditModeEnabled: !settings.readOnly,
    data: {
      boxes: data["boxes"],
      lines: data["lines"]
    },

    area: {
      background: {
        fill: settings.backgroundColor
      }
    },

    palette: {
      elements: palette,
      settings: {
        width: 40,
        height: 30,
        padding: 0
      }
    },
    boxSettings: {
      isMaxSizeEnabled: false,
      isTextFitEnabled: false
    },

    lineSettings: {
      //compartmentLayout: ""
    },

    isPanningEnabled: false,

    events: {

      //Clicks
      clickedOnDiagram: function (data) {
        console.log("in clicked on diagram", data)
        _EDITOR.unSelectElements(_EDITOR.getSelectedElements());
        _EDITOR.selectElements([]);
        if (settings.onDiagramClick)
          settings.onDiagramClick(_EDITOR);
      },

      // the first function remembers the element, the second function
      // reacts on double click
      clickedOnElement: function (data) {
        var s = data.element._id;
        if (s && (s.substring(0, 4) == "Node"))
          myThis.lastClickedElementReference = parseInt(s.substring(4, s.length));
        else
        if (s && (s.substring(0, 4) == "Edge")) {
          myThis.lastClickedElementReference = parseInt(s.substring(4, s.length));
        } else
        if (s && (s.substring(0, 5) == "Label")) {
          myThis.lastClickedElementReference = parseInt(s.substring(5, s.length));
        } else
          myThis.lastClickedElementReference = 0;

        if (myThis.lastClickedElementReference && settings.onElementClick)
          settings.onElementClick(myThis.lastClickedElementReference);
      },
      dbClickOnSwimlane: function (data) {
        if (Object.keys(_EDITOR.getSelectedElements()).length != 1)
          return; // ignoring double-click, if no or more than one element is selected

        if (myThis.lastClickedElementReference) {

          if (settings.onElementDoubleClick)
            settings.onElementDoubleClick(myThis.lastClickedElementReference);
        }
      },

      /*                      clickedOnCollection: function(data) {
                              console.log("in clicked on collection", data, _EDITOR.getSelectedElements())
                            },*/

      //RClicks
      rClickedOnDiagram: function (data) {
        if (settings.onDiagramRightClick)
          settings.onDiagramRightClick();
      },

      rClickedOnElement: function (data) {

        var s = data.element._id;
        var r = 0;
        if (s && (s.substring(0, 4) == "Node"))
          r = parseInt(s.substring(4, s.length));
        else
        if (s && (s.substring(0, 4) == "Edge")) {
          r = parseInt(s.substring(4, s.length));
        }

        if (r && settings.onElementRightClick)
          settings.onElementRightClick(r);
      },

      rClickedOnCollection: function (data) {
        console.log("in  rclicked on collection ", data)
      },

      /*TODO                      keystrokes: function(data) {
                            console.log("keystroke pressed",data)
                            },*/

      newBoxCreated: function (data) {


        var rPaletteElement = parseInt(data.elementTypeId.substring(14));
        var x = data.presentation.attrs.x;
        var y = data.presentation.attrs.y;
        var w = data.width;
        var h = data.height;


        setTimeout(function () {
          _EDITOR.unSelectElements([data], true);
          _EDITOR.removeElements([data._id], true);
          if (settings.onNewBox) {
            settings.onNewBox({
              reference: rPaletteElement
            }, x, y, w, h);
          }

        }, 0);



      },


      newLineCreated: function (data) {

        var rPaletteElement = parseInt(data.elementTypeId.substring(14));

        var arr = [];
        for (var i = 0; i < data.line.attrs.points.length / 2; i++) {
          arr.push({
            x: Math.round(data.line.attrs.points[i * 2]),
            y: Math.round(data.line.attrs.points[i * 2 + 1])
          });
        }

        var rStart = parseInt(data.startElementId.substring(4));
        var rEnd = parseInt(data.endElementId.substring(4));

        setTimeout(function () {
          _EDITOR.unSelectElements([data], true);
          _EDITOR.removeElements([data._id], true);
          if (settings.onNewLine) {
            settings.onNewLine({
                reference: rPaletteElement
              }, {
                reference: rStart
              }, {
                reference: rEnd
              },
              arr);
          }

        }, 0);

      },


      elementResized: function (el) {
        var data = {
          boxes: [el.elementId]
        };
        console.log("in element resized ", data)
        if (settings.onElementsChange) {
          var arr = [];
          var elms = _EDITOR.getElements();
          for (var i = 0; i < data.boxes.length; i++) {
            var box = elms[data.boxes[i]];
            var location = new NodeLocation();
            var xy = box.getElementPosition();;
            location.setX(xy.x); // xy.x already contains added data.deltaX
            location.setY(xy.y); // xy.y already contains added data.deltaY
            location.setWidth(box.width);
            location.setHeight(box.height);
            arr.push({
              reference: parseInt(data.boxes[i].substring(4)),
              location: location.toString()
            });
          }

          settings.onElementsChange(_EDITOR, arr, true);
        }
      },

      collectionPositionChanged: function (data) {
        console.log("collection position changed ", data);


        if (settings.onElementsChange) {
          var arr = [];
          var elms = _EDITOR.getElements();
          for (var i = 0; i < data.boxes.length; i++) {
            var box = elms[data.boxes[i]];

            if (data.boxes[i].substring(0, 4) == "Node") {
              var location = new NodeLocation();
              var xy = box.getElementPosition();;
              location.setX(xy.x); // xy.x already contains added data.deltaX
              location.setY(xy.y); // xy.y already contains added data.deltaY
              location.setWidth(box.width);
              location.setHeight(box.height);
              arr.push({
                reference: parseInt(data.boxes[i].substring(4)),
                location: location.toString()
              });
            } else
            if (data.boxes[i].substring(0, 5) == "Label") {
              var style = new CompartmentStyle();
              var xy = box.getElementPosition();;
              style.setOffsetX(xy.x + 2); // xy.x already contains added data.deltaX
              style.setOffsetY(xy.y + 1); // xy.y already contains added data.deltaY
              style.setWidth(box.width);
              //                              style.setHeight(box.height-2);
              arr.push({
                reference: parseInt(data.boxes[i].substring(5)),
                style: style.toString()
              });
            }
          }
          for (var i = 0; i < data.lines.length; i++) {
            var line = data.lines[i];
            var location = new EdgeLocation();
            location.setPoints(line.points);
            arr.push({
              reference: parseInt(line.id.substring(4)),
              location: location.toString()
            });
          }

          settings.onElementsChange(_EDITOR, arr, true);
        }

      },




    }


  }; // ajoo_settings


  whenLibsLoaded(function () {
    if (settings.canvasWidth > $("#" + settings.diagramContentDiv).width())
      ajoo_settings.width = settings.canvasWidth;
    else
      ajoo_settings.width = $("#" + settings.diagramContentDiv).width();

    if (settings.canvasHeight > $("#" + settings.diagramContentDiv).height())
      ajoo_settings.height = settings.canvasHeight;
    else
      ajoo_settings.height = $("#" + settings.diagramContentDiv).height();

    myThis.w = ajoo_settings.width;
    myThis.h = ajoo_settings.height; // initial virtual w x h

    if (window._EDITOR) {
      window._EDITOR.removeElements(_EDITOR.getElements());
      window._EDITOR = null;
    }

    window[settings.diagramContentDiv].innerHTML = "";

    window._EDITOR = new AjooEditor(ajoo_settings);
    _EDITOR.onLabelMoved = settings.onLabelMoved;
    if (settings.onSurfaceReady) {
      // wait for AjooEditor to initialize (in setTimeout), and call settings.onSurfaceReady
      setTimeout(function () {
        settings.onSurfaceReady(myThis);
        delete settings.onSurfaceReady;
      }, 0);
    };

  });


  return this;

} // constructor

