/**
 * @fileOverview IMCS Diagram Editor library
 * @author IMCS, UL (Sergejs Kozlovics and other staff)
 * @version 1.1
 */


///// JS Util /////
    function is_function(functionToCheck) {
      return functionToCheck && Object.prototype.toString.call(functionToCheck) === '[object Function]';
    };

    function clone_object_properties(obj) {
      if (null == obj || "object" != typeof obj) return obj;
      var copy = {};//obj.constructor();
      for (var attr in obj) {
        if (obj.hasOwnProperty(attr) && (!is_function(obj[attr]))) copy[attr] = obj[attr];
      }
      return copy;
    };


///// Script/CSS helpers >>> /////
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

var imcsdeScriptsAdded = 0;
var imcsdeScriptsStarted = false;
var imcsdeScriptsFinished = false;

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

var SCRIPT_FOLDER = getScriptFolder("IMCSDiagramEditor.js");

function addScript(src) {
  var headElement = document.getElementsByTagName('head').item(0);
  var s = document.createElement("script");
  if ((src.charAt(0) == '/') || (src.indexOf("http") != -1))
    s.src = src;
  else
    s.src = SCRIPT_FOLDER + src;
  s.onload = function () {
    imcsdeScriptsAdded++;
  };
  s.onerror = function () {
    console.log("script " + src + " not loaded");
    alert("script " + src + " not loaded");
  };
  headElement.appendChild(s);
}
///// <<< Script/CSS helpers /////

if (typeof jQuery==='undefined') {
  addScript('lib/jquery.js');
}

if (typeof IMCSDiagramLayout==='undefined')
  addScript('lib/imcs_layoutengine.js');

if (typeof tinyscrollbar==='undefined') {
  addScript('lib/tinyscrollbar.js');
  addCSS('lib/tinyscrollbar.css');
}

addScript('az.js');
addScript('AjooDiagramEditor.js');

//For future (via JS6 modules):
//import { * } from './az.js';
//import AjooDiagramEditor from './AjooDiagramEditor.js';



///// Repository/JSON traverse helpers >>> /////

function sortCompartments(arr) {
  arr.sort(function (a, b) {
    return a.style.getOffsetY() - b.style.getOffsetY();
  });

  return arr;
}

function collectSubCompartments(arr) {
  // clones...
  if (typeof arr == "undefined")
    return [];

  var retVal = [];
  for (var i = 0; i < arr.length; i++) {
    retVal.push(arr[i]);
  }

  // collecting sub-compartments
  var j = 0;
  while (j < retVal.length) {

    if (retVal[j].isGroup == "true") {
      // adding children to the end of the list
      var children = retVal[j].subCompartment;

      retVal.splice(j, 1); // remove the current element...

      // adding new elements...
      if (children) {
        for (var k = 0; k < children.length; k++)
          retVal.push(children[k]);
      }
    } else {
      if (retVal[j].input) {
        retVal[j] = clone_object_properties(retVal[j]);
        j++;
      } else
        retVal.splice(j, 1); // remove the current element...
    }
  }

  return retVal;
}


function cloneElement(_el) {
  // INITIAL CLONING
  var el = clone_object_properties(_el);
  if (!el.className)
    el.className = _el.getClassName();
  if (!el.className)
    return null;
  if (el.className == 'Edge') {
    el.start = [{
      reference: _el.start[0].reference,
      className: _el.start[0].className
    }];
    el.end = [{
      reference: _el.end[0].reference,
      className: _el.end[0].className
    }];
    if (!el.start[0].className)
      el.start[0].className = _el.start[0].getClassName();
    if (!el.end[0].className)
      el.end[0].className = _el.end[0].getClassName();
  }

  el.hash = "";

  // SPECIFIC CLONING...
  if (el.className == 'Node') {
    // converting style and location strings to objects (decoding AZ)...
    el.compartment = collectSubCompartments(_el.compartment);
    if (!el.style || (el.style == "#")) {
      var defaultStyle = _el.elemStyle;

      if (defaultStyle && (defaultStyle.length > 0)) {
        var s = new NodeStyle();
        s.setFillColor(intToColor2(parseInt(defaultStyle[0].bkgColor))); // !!! not RGB, but BGR
        s.setShapeAZ(defaultStyle.shapeCode);
        s.setDashLength(defaultStyle.dashLength);
        s.setDashBreakLength(defaultStyle.breakLength);
        s.setLineColorAZ(defaultStyle.lineColor);
        s.setLineWidth(defaultStyle.lineWidth);
        el.style = s.toString();
      }
    }
    el.style = new NodeStyle(el.style);
    el.hash += el.style.toString();

    if (!el.location && newBoxLocation) { // setting the location for the newly created box
      tda.model[el.reference].setLocation(newBoxLocation);
      el.location = newBoxLocation;
      newBoxLocation = null;
    }
    el.location = new NodeLocation(el.location);

    if (typeof el.compartment != 'undefined') {

      for (var j = 0; j < el.compartment.length; j++) {
        if (!el.compartment[j].style || (el.compartment[j].style == "#")) {

          var defaultStyle = _el.compartment[j].compartStyle;
          if (defaultStyle && (defaultStyle.length > 0)) {
            var s = new CompartmentStyle();
            s.setFontColorAZ(defaultStyle[0].fontColor);
            s.setFontFamily(defaultStyle[0].fontTypeFace);
            s.setFontWeightAZ(defaultStyle[0].fontStyle);
            s.setFontSize(parseInt(defaultStyle[0].fontSize) + 2);
            s.setIsVisible(defaultStyle[0].isVisible && (defaultStyle[0].isVisible != "0"));
            s.setAlignmentAZ(defaultStyle[0].alignment);
            s.setPlacementAZ(defaultStyle[0].adjustment);
            s.setLineWidth(defaultStyle[0].lineWidth);
            el.compartment[j].style = s.toString();
          }
        }

        // converting style string to an object (decoding AZ)...
        el.compartment[j].style = new CompartmentStyle(el.compartment[j].style);
        el.hash += "#" + el.compartment[j].reference + "#" + el.compartment[j].style.toString() + "#" + el.compartment[j].input + "#" + el.compartment[j].value;


      }

    }


  } else
  if (el.className == 'Edge') {

    el.compartment = collectSubCompartments(_el.compartment);

    // converting style and location strings to objects (decoding AZ)...

    if (!el.location && newLineLocation) { // setting the location for the newly created line
      tda.model[el.reference].setLocation(newLineLocation);
      el.location = newLineLocation;
      newLineLocation = null;

      window.newLineRef = el.reference;
    }


    if (!el.style || (el.style == "#")) {
      var defaultStyle = _el.elemStyle;

      if (defaultStyle && (defaultStyle.length > 0)) {
        var s = new EdgeStyle();
        s.setFillColor(intToColor2(parseInt(defaultStyle[0].bkgColor))); // !!! not RGB, but BGR
        s.setShapeAZ(defaultStyle[0].shapeCode);
        s.setDashLength(defaultStyle[0].dashLength);
        s.setDashBreakLength(defaultStyle[0].breakLength);
        s.setLineColorAZ(defaultStyle[0].lineColor);
        s.setLineWidth(defaultStyle[0].lineWidth);


        s.setStartLabelStyleAZ(
          defaultStyle[0].startShapeCode,
          defaultStyle[0].startLineWidth,
          defaultStyle[0].startDashLength,
          defaultStyle[0].startBreakLength,
          defaultStyle[0].startBkgColor,
          defaultStyle[0].startLineColor);
        //args are (shapeCode, lineWidth, dashLength, breakLength, bkgColor, lineColor)

        s.setEndLabelStyleAZ(
          defaultStyle[0].endShapeCode,
          defaultStyle[0].endLineWidth,
          defaultStyle[0].endDashLength,
          defaultStyle[0].endBreakLength,
          defaultStyle[0].endBkgColor,
          defaultStyle[0].endLineColor);

        s.setMiddleLabelStyleAZ(
          defaultStyle[0].middleShapeCode,
          defaultStyle[0].middleLineWidth,
          defaultStyle[0].middleDashLength,
          defaultStyle[0].middleBreakLength,
          defaultStyle[0].middleBkgColor,
          defaultStyle[0].middleLineColor);

        el.style = s.toString();
      }
    }
    el.style = new EdgeStyle(el.style);
    el.hash += el.style.toString();

    if (newLineLocation) {
      tda.model[el.reference].setLocation(newLineLocation);
      el.location = newLineLocation;
      newLineLocation = null;
    }
    el.location = new EdgeLocation(el.location);

    var arr = el.location.getPointsXY();

    if (typeof el.compartment != 'undefined') {

      for (var j = 0; j < el.compartment.length; j++) {
        if (!el.compartment[j].style || (el.compartment[j].style == "#")) {

          var defaultStyle = el.compartment[j].compartStyle;

          if (defaultStyle && (defaultStyle.length > 0)) {
            var s = new CompartmentStyle();
            s.setFontColorAZ(defaultStyle[0].fontColor);
            s.setFontFamily(defaultStyle[0].fontTypeFace);
            s.setFontWeightAZ(defaultStyle[0].fontStyle);
            s.setFontSize(parseInt(defaultStyle[0].fontSize) + 2);
            s.setIsVisible(defaultStyle[0].isVisible && (defaultStyle[0].isVisible != "0"));
            s.setAlignmentAZ(defaultStyle[0].alignment);
            s.setPlacementAZ(defaultStyle[0].adjustment);
            s.setLineWidth(defaultStyle[0].lineWidth);
            el.compartment[j].style = s.toString();
          }
        }

        // converting style string to an object (decoding AZ)...
        el.compartment[j].style = new CompartmentStyle(el.compartment[j].style);
        el.hash += "#" + el.compartment[j].reference + "#" + el.compartment[j].style.toString() + "#" + el.compartment[j].input + "#" + el.compartment[j].value;
      }


    }

  }

  return el;
}
///// <<< Repository/JSON traverse helpers /////


///// Helpers for scrolling and moving the diagram >>> /////
window.diagramMoving = false;

window.updateScrollBars = function (visibleW, visibleH, totalW, totalH) {

  window.scrollbar2.outerHTML =
    '<div id="scrollbar2">\
        <div class="scrollbar"><div class="track"><div class="thumb"><div class="end"></div></div></div></div>\
        <div id="scrollbar2viewport" class="viewport" style="width:' + (visibleW - 15) + 'px">\
          <div class="overview">\
            <div id="theHScrollerWidthDiv" style="width:' + (totalW + 100) * ajoo_de.getZoomFactor() + '; height:1; left:0; position: absolute;"></div>\
          </div>\
        </div>\
    </div>';

  window.scrollbar1.outerHTML =
    '<div id="scrollbar1">\
        <div class="scrollbar"><div class="track"><div class="thumb"><div class="end"></div></div></div></div>\
        <div id="scrollbar1viewport" class="viewport" style="height:' + (visibleH - 15) + 'px">\
          <div class="overview">\
            <div id="theVScrollerHeightDiv" style="width:1; height:' + (totalH + 60) * ajoo_de.getZoomFactor() + '; position: absolute;"></div>\
          </div>\
        </div>\
    </div>';

  var f = function () {
    if (typeof window.tinyscrollbar==='undefined') {
      setTimeout(f, 200);
      return;
    }

    tinyscrollbar(document.getElementById('scrollbar1'), {
      axis: 'y'
    });
    tinyscrollbar(document.getElementById('scrollbar2'), {
      axis: 'x'
    });

/*variant with jQuery:
    $('#scrollbar1').tinyscrollbar({
      axis: 'y'
    });
    $('#scrollbar2').tinyscrollbar({
      axis: 'x'
    });*/

    window.scrollbar1move = function (ev) {
      if ($('#scrollbar1').find(".scrollbar").hasClass("disable"))
        return;

      var f = function () { // setTimeout, since the track of the scrollbar has to be repainted
        var h = parseInt($("#scrollbar1").find(".scrollbar").css("height"));
        var th = parseInt($("#scrollbar1").find(".scrollbar").find(".track").find(".thumb").css("height"));
        var val = parseInt($("#scrollbar1").find(".scrollbar").find(".track").find(".thumb").css("top"));
        //           var scrollbar1 = $("#scrollbar1").data("plugin_tinyscrollbar");
        //           scrollbar1.thumbPosition=val;
        //           var val = parseInt($("#scrollbar1viewport").find(".overview").css("top"));
        ajoo_de.setRelativeY(val / (h - th));
        ajoo_de.repaint();
      };
      if (ev)
        setTimeout(f, 0);
      else
        f();
    };

//    $('#scrollbar1').bind("move", window.scrollbar1move);
    document.getElementById('scrollbar1').addEventListener("move", window.scrollbar1move, false);

    window.scrollbar2move = function (ev) {
      var f = function () { // setTimeout, since the track of the scrollbar has to be repainted
        var w = parseInt($("#scrollbar2").find(".scrollbar").css("width"));
        var tw = parseInt($("#scrollbar2").find(".scrollbar").find(".track").find(".thumb").css("width"));
        var val = parseInt($("#scrollbar2").find(".scrollbar").find(".track").find(".thumb").css("left"));
        ajoo_de.setRelativeX(val / (w - tw));
        ajoo_de.repaint();
      };
      if (ev)
        setTimeout(f, 0);
      else
        f();
    };
//    $('#scrollbar2').bind("move", window.scrollbar2move);
    document.getElementById('scrollbar2').addEventListener("move", window.scrollbar2move, false);

    showHideScrollBars();
  };

  $(document).ready(f);
};

window.toggleMoving = function () {
  window.diagramMoving = !window.diagramMoving;
  if (window.diagramMoving) {
    $("#ajoo_palette").css("opacity", "0.5");
    $("#ajoo_palette").css("pointer-events", "none");
    $(theMoveDiv).css("color", "blue");
    $(theMoveWrap).show();
    $(theMoveWrap).css("cursor", "move");
  } else {
    $("#ajoo_palette").css("opacity", "1.0");
    $("#ajoo_palette").css("pointer-events", "auto");
    $(theMoveDiv).css("color", "black");
    $(theMoveWrap).css("cursor", "auto");
    $(theMoveWrap).hide();
  }
};

window.moveScrollWheel = function (ev) {
  if (ajoo_de.scrollPalette(ev))
    return;
  var val = parseInt($("#scrollbar1").find(".scrollbar").find(".track").find(".thumb").css("top"));
  if (ev.deltaY < 0) {
    val = val - 10;
    if (val < 0)
      val = 0;
  } else
  if (ev.deltaY > 0) {
    val = val + 10;
    var h = parseInt($("#scrollbar1").find(".scrollbar").css("height"));
    var th = parseInt($("#scrollbar1").find(".scrollbar").find(".track").find(".thumb").css("height"));
    var maxVal = h - th;
    if (val > maxVal)
      val = maxVal;
  }

  $("#scrollbar1").find(".scrollbar").find(".track").find(".thumb").css("top", val + "px");

  var scrollbar1 = $("#scrollbar1").data("plugin_tinyscrollbar");
  if (scrollbar1) scrollbar1.thumbPosition = val;
  window.scrollbar1move();
};

window.moveZoomWheel = function (ev) {
  var x = ev.clientX - 40; // without palette
  var y = ev.clientY;

  if (ev.deltaY < 0) {
    ajoo_de.zoomIn(x, y);
    showZoomValue();
  } else {
    ajoo_de.zoomOut(x, y);
    showZoomValue();
  }
};

window.mouseMoveStart = function (ev) {
  window.diagramDragging = true;
  window.diagramDraggingLastX = ev.clientX - 40;
  window.diagramDraggingLastY = ev.clientY;
};

window.mouseMoveEnd = function (ev) {
  window.diagramDragging = false;
};

window.mouseMove = function (ev) {
  if (ev.buttons == 0) {
    window.diagramDragging = false;
  }
  if (window.diagramDragging) {
    var curX = ev.clientX - 40;
    var curY = ev.clientY;
    var x = ajoo_de.getX() * ajoo_de.getZoomFactor();
    var y = ajoo_de.getY() * ajoo_de.getZoomFactor();
    x -= (window.diagramDraggingLastX - curX);
    y -= (window.diagramDraggingLastY - curY);
    ajoo_de.setX(x / ajoo_de.getZoomFactor());
    ajoo_de.setY(y / ajoo_de.getZoomFactor());
    ajoo_de.repaint();
    window.diagramDraggingLastX = curX;
    window.diagramDraggingLastY = curY;
    showHideScrollBars(true); // set val for both scrollbars
  }
};

window.showZoomValue = function () {
  var d = new Date();
  window.zoomValueTime = d.getTime();
  theZoomValue.innerHTML = Math.round(ajoo_de.getZoomFactor() * 100) + "%";
  $(theZoomValue).show();
  setTimeout(function () {
    var d = new Date();
    var curTime = d.getTime();
    if (curTime - window.zoomValueTime >= 2000)
      $(theZoomValue).hide();
  }, 2000);

  ajoo_de.setVisibleWidth($("#diagramContentDiv").width() - 40); // -paletteSize
  ajoo_de.setVisibleHeight($("#diagramContentDiv").height());
  updateScrollBars(ajoo_de.getVisibleWidth(), ajoo_de.getVisibleHeight(), ajoo_de.getTotalWidth(), ajoo_de.getTotalHeight());
  showHideScrollBars(true);
};

window.showHideScrollBars = function (setVal) {
  if (ajoo_de.getTotalWidth() * ajoo_de.getZoomFactor() + 100 < ajoo_de.getVisibleWidth())
    $('#scrollbar2').find(".scrollbar").addClass("disable");
  else
    $('#scrollbar2').find(".scrollbar").removeClass("disable");

  if (ajoo_de.getTotalHeight() * ajoo_de.getZoomFactor() + 60 < ajoo_de.getVisibleHeight())
    $('#scrollbar1').find(".scrollbar").addClass("disable");
  else
    $('#scrollbar1').find(".scrollbar").removeClass("disable");

  // update scrollbar values...
  var h = parseInt($("#scrollbar1").find(".scrollbar").css("height"));
  var th = parseInt($("#scrollbar1").find(".scrollbar").find(".track").find(".thumb").css("height"));
  var val1 = (h - th) * ajoo_de.getRelativeY();
  $("#scrollbar1").find(".scrollbar").find(".track").find(".thumb").css("top", val1 + "px");
  if (setVal) {
    var scrollbar1 = $("#scrollbar1").data("plugin_tinyscrollbar");
    if (scrollbar1) scrollbar1.thumbPosition = val1;
  }

  var w = parseInt($("#scrollbar2").find(".scrollbar").css("width"));
  var tw = parseInt($("#scrollbar2").find(".scrollbar").find(".track").find(".thumb").css("width"));
  var val2 = (w - tw) * ajoo_de.getRelativeX();
  $("#scrollbar2").find(".scrollbar").find(".track").find(".thumb").css("left", val2 + "px");
  if (setVal) {
    var scrollbar2 = $("#scrollbar2").data("plugin_tinyscrollbar");
    if (scrollbar2) scrollbar2.thumbPosition = val2;
  }
};

///// <<< Helpers for scrolling and moving the diagram /////

/**
 * Creates a new JavaScript graphical diagram editor with layout capabilities.<br/>
 * Currently the diagram editor has the following restrictions:
 * <ul>
 * <li> only orthogonal lines are supported; </li>
 * <li> no inner boxes (box-in-a-box); </li>
 * <li> only one diagram per HTML page; </li>
 * <li> several canvas are used, however, canvas size is limited to the visible area, while the diagram can be much larger. </li>
 * </ul>
 * <p>
 * The built-in default layout manager is activated by default, thus, the diagram is adjusted automatically in such a way that boxes and lines do not overlap.
 * The default layout manager can be replaced by a user-defined layout manager or even switched off for manual mode (where the diagram is not adjusted).
 * <p>
 * To minimize diagram adjustments and repaintings, the diagram IS NOT refreshed after any single operation such as 
 * addBox, addLine, updateBoxLocation, updateLineLocation, etc.
 * Multiple modifications should to be performed in bulk, and then the refresh() function has to be called explicitly.
 * <p>
 * Instead of just adjusting the diagram, it can be re-arranged from scratch by calling resetLayout.
 * <p>
 * 
 * 
 * @constructor
 * @param {object} settings - a JavaScript/JSON object contatining configuration settings for the diagram.
 * The fields are:
 * <table cellspacing=0 cellpadding=0>
 * <tr><td>diagramDiv</td><td>HTML div element id, where to put the diagram</td></tr>
 * <tr><td>readOnly</td><td>a boolean value denoting where the diagram is read-only or editable</td></tr>
 * <tr><td>backgroundColor</td><td>background color of the diagram, e.g. "#f0f8ff"</td></tr>
 * <tr><td>paletteElements</td>
 *     <td>an array of palette elements in the format:<br/>
 *         <code>{<br/>
 *           &nbsp;&nbsp;reference: &lt;a number representing the id of this palette element&gt;,<br/>
 *           &nbsp;&nbsp;className: "PaletteBox" or "PaletteLine",<br/>
 *           &nbsp;&nbsp;picture: "a/path/relative/to/&lt;script-folder&gt;/images"<br/>
 *         }<br/></code>
 *     </td>
 * </tr>
 * <tr><td>onNewBox</td>
 *     <td>an function to be called when a new box is being created from a palette element; this box has to be explicitly added to the diagram via addBox (or ingored, e.g., if this box is not allowed)<br/>
 *         <code>function (palette_element_reference, x, y, width, height)</code>
 *     </td>
 * </tr>
 * <tr><td>onNewLine</td>
 *     <td>an function to be called when a new line is being created from a palette element; this line has to be explicitly added to the diagram via addLine (or ignored, e.g., if this line is not allowed)<br/>
 *         <code>function(palette_element_reference, source_box_reference, target_box_reference, points)</code><br/>
           points is an array in the form [{x:x1, y:y1},{x:x2,y:y2},{x:xN,y:yN}]<br/>
 *     </td>
 * </tr>
 * <tr><td>onElementsMoved</td>
 *     <td>an function to be called when some boxes and/or lines changed their positions<br/>
 *         <code>function(rElementsArr)</code><br/>
 *         each element in the array is in the format:<br/>
 *         <code>{<br/>
 *         &nbsp;&nbsp;reference: &lt;a number representing the id of this diagram element&gt;,<br/>
 *         &nbsp;&nbsp;location: "width;height;left;top" for a box or "N\x1,y1\x2,y2\xN,yN" for lines<br/>
 * TODO!: className, width, height, left, top, points<br/>
 *         }<br/></code>
 *     </td>
 * </tr>
 * <tr><td>onElementsSelected</td>
 *     <td>an function to be called when the set of selected boxes and lines changes<br/>
 *         <code>function(rElementsArr)</code><br/>
 *         each element in the array is in the format:<br/>
 *         <code>{<br/>
 *         reference: &lt;a number representing the id of this diagram element&gt;,<br/>
 *         }<br/></code>
 *     </td>
 * </tr>
 * <tr><td>onElementDoubleClick</td>
 *     <td>an function to be called when a double click is performed on an element<br/>
 *         <code>function(reference)</code><br/>
 *         reference is a number representing the id of the clicked diagram element
 *     </td>
 * </tr>
 * <tr><td>onElementRightClick</td>
 *     <td>an function to be called when a right click is performed on an element<br/>
 *         <code>function(reference)</code><br/>
 *         reference is a number representing the id of the clicked diagram element
 *     </td>
 * </tr>
 * <tr><td>onDiagramRightClick</td>
 *     <td>an function to be called when a right click is performed somewhere on a diagram, but not on an element<br/>
 *         <code>function()</code><br/>
 *     </td>
 * </tr>
 * <tr><td>onceReady</td>
 *     <td>a function to be called when the diagram loading has been finished and
 *         the initial layout has been performed<br/>
 *         <code>function()</code><br/>
 *     </td>
 * </tr>  
 * <tr><td>showPleaseWait</td>
 *     <td>a function to be called when the diagram needs to display some message and block user input
 *         (e.g., to inform the used to wait while the diagram is being re-arranged)<br/>
 *         <code>function(message)</code><br/>
 *     </td>
 * </tr>  
 * <tr><td>hidePleaseWait</td>
 *     <td>a function to be called when the "please, wait" message is not needed any more<br/>
 *         <code>function()</code><br/>
 *     </td>
 * </tr>  
 * </table>
 */

var IMCSDiagramEditor = function(settings) { // class
  var myThis = this;

  // DEBUG/LOG DIAGRAM ==>
  var LOG_DIAGRAM = false;
  var pointsToJ = function (points) {
    var i;
    var s = "new ArrayList<Point2D.Double>(Arrays.asList(new Point2D.Double[] {";
    for (i = 0; i < points.length; i++) {
      if (i > 0)
        s += ",";
      s += "new Point2D.Double(" + points[i].x + "," + points[i].y + ")";
    }
    s += "}))";
    return s;
  }
  var logDiagram = function () {
    if (!LOG_DIAGRAM)
      return; // skip log
    var s = "";
    for (var r in clonedBoxes) {
      var el = clonedBoxes[r];
      s += "layout.addBox(" + el.reference + ", " + el.location.getX() + ", " + el.location.getY() + ", " + el.location.getWidth() + ", " + el.location.getHeight() + ");\n";
    }
    for (var r in clonedLines) {
      var el = clonedLines[r];
      var settings = getLayoutSettingsForEdge(null, el, true);
      s += "layout.addLine(" + el.reference + ", " + el.start[0].reference + ", " + el.end[0].reference + ", \"" + settings.lineType + "\", " + settings.startSides + ", " + settings.endSides + ", " + pointsToJ(settings.points) + ");\n";
    }
    for (var r in clonedLines) {
      var el = clonedLines[r];
      var settings = getLayoutSettingsForEdge(null, el, true);
      if (el.compartment) {
        for (var i = 0; i < el.compartment.length; i++) {
          var style = el.compartment[i].style;
          var w = getTextWidth(el.compartment[i].input, style.getFontFamily() + " " + style.getFontSizeInPt() + "pt") + 10;
          var h = getFontHeight(style.getFontFamily(), style.getFontSizeInPt() * 1.45 + 0.5) + 2;
          s += "layout.addLineLabel(" + el.compartment[i].reference + ", " + el.reference + ", " + w + ", " + h + ", \"" + style.getPlacement() + "\");\n";
        }
      }
    }

    var k = 1;
    while (tda.fileExists("diagram" + k + ".java"))
      k++;
    tda.fileUploadFromString("diagram" + k + ".java", s);
  }
  // <== DEBUG/LOG DIAGRAM

  window[settings.diagramDiv].innerHTML =
    '<div id="diagramContentDiv" onwheel="moveScrollWheel(event);" style="width:100%; height:100%; overflow:hidden; position:absolute; left:0;top:0;"></div>' +
    '<div id="theMoveWrap"' +
    '     onwheel="moveZoomWheel(event);"' +
    '     onmousedown="mouseMoveStart(event);"' +
    '     onmouseup="mouseMoveEnd(event);"' +
    '     onmousemove="mouseMove(event);"' +
    '     oncontextmenu="window.toggleMoving();return false;" style="margin-left:40px; opacity:0.2; position:absolute; left:0; top:0; right:0; bottom:0; background-color:cccccc; display:none;">' +
    '</div>' +
    '<div id="scrollbar2"></div>' +
    '<div id="scrollbar1"></div>' +
    '<div id="theZoomMoveDiv" align=center>' +
    '  <span id="theZoomValue" align=center style="display:none;">100%</span>' +
    '  <div id="theZoomDiv" align=center>' +
    '    <div id="theZoomIn" onclick="ajoo_de.zoomIn();showZoomValue();">&nbsp;+&nbsp;</div>' +
    '    <div id="theZoomOut" onclick="ajoo_de.zoomOut();showZoomValue();">&nbsp;&ndash;&nbsp;</div>' +
    '  </div>' +
    '  <div id="theMiddleDiv"></div>' +
    '  <div id="theMoveDiv" onclick="toggleMoving();">&nbsp;&#x2725;&nbsp;</div>' +
    '</div>';


  $(window).resize(function () {
    var f = function () {
      if (ajoo_de.initialized()) {
        ajoo_de.setVisibleWidth($("#diagramContentDiv").width() - 40); // -paletteSize
        ajoo_de.setVisibleHeight($("#diagramContentDiv").height());
        updateScrollBars(ajoo_de.getVisibleWidth(), ajoo_de.getVisibleHeight(), ajoo_de.getTotalWidth(), ajoo_de.getTotalHeight());
        ajoo_de.repaint();
      } else
        setTimeout(f, 10);
    };
    f();
  });

  var clonedBoxes = {}; // id -> cloned box
  var clonedLines = {}; // id -> cloned line

  // Simulating layout thread ==>
  // simulating a specific thread for adding boxes and lines in the correct order;
  // first, we add boxes, then lines, then line labels
  var layoutThread = null;
  var layoutRunnables = [];
  var layoutLastTouched = 0;
  var runInLayoutThread = function (order, f) { // 0=add box, 1=add line, 2=add line label, 4=after 1,2,3
    if ((order >= 4) && (layoutRunnables.length == 0)) {
      setTimeout(f, 0); // run without delay
      return;
    }
    while (layoutRunnables.length <= order)
      layoutRunnables.push([]);
    layoutRunnables[order].push(f);

    var d = new Date();
    layoutLastTouched = d.getTime();
    if (!layoutThread) {
      layoutThread = function () {
        var d = new Date();
        var cur = d.getTime();
        // at least 100 ms must be skipped before we execute all collected runnables
        if (cur - layoutLastTouched < 100) {
          setTimeout(layoutThread, 100);
          return;
        }
        for (var i = 0; i < layoutRunnables.length; i++) {
          for (var j = 0; j < layoutRunnables[i].length; j++) {
            //console.log("runnable " + i + " " + j + " of " + layoutRunnables[i].length);
            layoutRunnables[i][j]();
          }
        }
        if (myThis.settings.hidePleaseWait)
          myThis.settings.hidePleaseWait();

        layoutRunnables = [];
        layoutThread = null;
        layoutLastTouched = 0;
      };
      if (myThis.settings.showPleaseWait)
         myThis.settings.showPleaseWait("Adjusting layout...");
      layoutThread(); // run the thread
    }

  };
  // <== Simulating layout thread

  /*
   * Sets the given coordinates to Ajoo elements and calls the onElementsMoved callback (if necessary)
   * @private
   * @param coos a JS object in the format:
   * {
   *   boxes: { "box-id": {x:x, y:y, width:w, height:h}, ...},
   *   lines: { "line-id": [{x:x1,y:y1},{x:x2,y:y2},...{x:xN,y:yN}], ...},
   *   labels: { "label-id": {x:x, y:y, width:w, height:h}, ...}
   * }
   */
  var setCoos = function (coos) {
    // set new ajoo coos
    var arr = [];

    //console.log("set coos", JSON.stringify(coos));

    for (var i in clonedBoxes) {
      var el = clonedBoxes[i];
      var prevLocation = el.location.toString();
      //console.log("IMCSDE "+el.reference+" "+";;;"+JSON.stringify(coos.boxes[el.reference]));
      el.location.setX(coos.boxes[el.reference].x);
      el.location.setY(coos.boxes[el.reference].y);
      el.location.setWidth(coos.boxes[el.reference].width);
      el.location.setHeight(coos.boxes[el.reference].height);

      var newLocation = el.location.toString();
      if (prevLocation != newLocation) {
        arr.push({
          reference: i,
          location: newLocation
        });
      }

      // update also when prevLocation==newLocation, since Ajoo could have repainted
      // the node with other coos, and then we could re-arrange it back to the previous position,
      // thus, ajoo needs to repaint the node back again
      ajoo_de.updateNodeLocation(el.reference, coos.boxes[el.reference].x, coos.boxes[el.reference].y,
        coos.boxes[el.reference].width, coos.boxes[el.reference].height);
    }

    for (var i in clonedLines) {
      var el = clonedLines[i];
      var prevLocation = el.location.toString();
      el.location.setPointsXY(coos.lines[el.reference]);
      var newLocation = el.location.toString();
      if (prevLocation != newLocation) {
        arr.push({
          reference: i,
          location: newLocation
        });
      }
      ajoo_de.updateEdgeLocation(el.reference, el.location.getPointsArray());
      // elLastLocation[el.reference] = locationStr; // update elLastLocation
      if (el.compartment) {
        for (var i = 0; i < el.compartment.length; i++) {
          var cmp = el.compartment[i];
          if (!coos.labels[cmp.reference])
            continue; // ignore, if label has no coos
          ajoo_de.updateLabelLocation(cmp.reference, coos.labels[cmp.reference].x + 2, coos.labels[cmp.reference].y + 1,
            coos.labels[cmp.reference].width - 4, coos.labels[cmp.reference].height - 2);
          var oldX = cmp.style.getOffsetX();
          var oldY = cmp.style.getOffsetY();
          cmp.style.setOffsetX(coos.labels[cmp.reference].x + 2); // do not update width, it will be calculated each time
          cmp.style.setOffsetY(coos.labels[cmp.reference].y + 1); // do not update height, it will be calculated each time
          if ((oldX != cmp.style.getOffsetX()) || (oldY != cmp.style.getOffsetY()))
            arr.push({
              reference: cmp.reference,
              style: cmp.style.toString()
            });
        }
      }

    }

    if (myThis.settings.onElementsMoved) {
      if (arr.length > 0)
        myThis.settings.onElementsMoved(arr);
    }
  };

  /**
   * Updates the layout depending on new coordinates of the moved elements, and calls refresh.
   * @private
   * @param {array} arr an array of moved elements; each element is in the format:
   * {
   *   reference:<element-id>,
   *   location:"width;height;left;top" for a box or "N\x1,y1\x2,y2\xN,yN" for lines
   * }
   */
  var updateLayoutAndRefreshOnMove = function (arr) {
    if (!myThis.layout) {
      if (settings.onElementsMoved) {
        settings.onElementsMoved(arr);
      }
      return;
    }

    for (var i = 0; i < arr.length; i++) {
      var r = arr[i].reference;
      if (clonedBoxes[r]) {
        if (clonedBoxes[r].location.toString() != arr[i].location) {
          var loc = new NodeLocation(arr[i].location);
          myThis.layout.resizeBox(r, loc.getWidth(), loc.getHeight());
          myThis.layout.moveBox(r, loc.getX(), loc.getY());
        }
      } else
      if (clonedLines[r]) {
        if (clonedLines[r].location.toString() != arr[i].location) {
          var loc = new EdgeLocation(arr[i].location);
          try {
            myThis.layout.moveLine(r, clonedLines[r].start[0].reference, clonedLines[r].end[0].reference, loc.getPointsXY());
          } catch (t) {
            console.log("Exception during updateLayoutAndRefreshOnMove/moveLine. Re-route line...", t);
            myThis.layout.moveLine(r, clonedLines[r].start[0].reference, clonedLines[r].end[0].reference);
          }
          // we do not move line compartments (line labels), since in semi-automatic layout, they should be adjusted automatically
        }
      }
    }
    myThis.refresh();
  };

  this.settings = settings;

  /**
   * Settings for Ajoo (created from the settings object passed to us)
   * @private
   */
  var settingsForAjoo = {
    diagramContentDiv: "diagramContentDiv",
    readOnly: settings.readOnly,
    paletteElements: settings.paletteElements,
    backgroundColor: settings.backgroundColor,
    onSurfaceReady: function (ajooDgr) {
      // layout!
      myThis.refresh();

      if (settings.onceReady) {
        settings.onceReady();
        delete settings.onceReady;
      }
    },
    onNewBox: function (ajooDgr, palette_element, x, y, w, h) {
      ajoo_de.selectElements([]);

      this.onSelectionChange(ajooDgr, []);

      if (settings.onNewBox)
        settings.onNewBox(palette_element.reference, x, y, w, h);
    },
    onNewLine: function (ajooDgr, paletteElement, src, tgt, points) {
      if (settings.onNewLine)
        settings.onNewLine(paletteElement.reference, src.reference, tgt.reference, points);
    },
    onElementsChange: function (ajooDgr, arr) {
//      console.log("ajoo_de callback: onElementsChange", arr);

      this.onSelectionChange(ajooDgr, arr);

      var t1 = now();

      // re-layout and refresh according to elements in the arr;
      updateLayoutAndRefreshOnMove(arr);
      var t2 = now();
//      console.log("CHANGE TIME ", (t2 - t1));

    },
    onSelectionChange: function (ajooDgr, arr) {
//      console.log("ajoo_de callback: onSelectionChange", arr);
      if (!settings.onElementsSelected)
        return; // no callback specified

      arr = arr.sort(function(a,b) {
        return (a.reference-b.reference);
      });

      if (this.prevSelectionArr) {
        // compare the previous prevSelectionArr with the current arr
        var equal=true;
        if (this.prevSelectionArr.length==arr.length) {
          for (var i=0; i<this.prevSelectionArr.length; i++) {
            if (this.prevSelectionArr[i].reference!=arr[i].reference) {
              equal=false;
              break;
            }
          }
          if (equal)
            return; // do not need to call the callback, if both arrays are equal
        }
      }

      this.prevSelectionArr = arr;

      settings.onElementsSelected(arr);
    },

    onDiagramClick: function (ajooDgr) {
//      console.log("ajoo_de callback: onDiagramClick");
      this.onSelectionChange(ajooDgr, []);
    },
    onDiagramRightClick: function(ajooDgr) {
//      console.log("ajoo_de callback: onDiagramRightClick");
      this.onSelectionChange(ajooDgr, []);
      if (settings.onDiagramRightClick)
        settings.onDiagramRightClick();
    },
    onElementClick: function (ajooDgr, r) {
//      console.log("ajoo_de callback: onElementClick");

      var arr = [];
      arr.push({
        reference: r
      });

      // selecting also line compartments, if any...
      /*      var el = clonedLines[r];
            if (el) { // if line...
              if (el.compartment) {
                for (var i=0; i<el.compartment.length; i++) {
                  arr.push({reference:el.compartment[i].reference});
                }
                ajoo_de.selectElements(arr);
              }
            }*/

      this.onSelectionChange(ajooDgr, arr);

    },
    onElementDoubleClick: function(ajooDgr, r) {
      this.onSelectionChange(ajooDgr, [{
          reference: r
      }]);
      if (settings.onElementDoubleClick)
	settings.onElementDoubleClick(r);
    },
    onElementRightClick: function (ajooDgr, r) {

      this.onSelectionChange(ajooDgr, [{
          reference: r
      }]);
      if (settings.onElementRightClick)
        settings.onElementRightClick(r);
    },
    onRepaint: function (ajooDgr) {
      showHideScrollBars();
      //      window.updateScrollBars(ajoo_de.getVisibleWidth(), ajoo_de.getVisibleHeight(),
      //         ajoo_de.getTotalWidth(), ajoo_de.getTotalHeight());
    },
    onLabelMoved: function (ajooDgr, r, x, y, w, h) {
      ajoo_de.updateLabelLocation(r, x, y, w, h);
      ajoo_de.repaint();
    }
  };

  window.ajoo_de = new AjooDiagramEditor(settingsForAjoo);

  this.layout = new IMCSDiagramLayout("UNIVERSAL", true);

  /**
   * Adds all cloned elements to the current layout.
   * @private
   * @param {boolean} fromScratch whether the layout will be arranged from scratch (thus, we can skip adding previously known coordinates to the layout)
   */
  var addClonedToLayout = function (fromScratch) {
    if (!myThis.layout)
      return;
    for (var r in clonedBoxes) {
      var el = clonedBoxes[r];
      if (!myThis.layout.addBox(el.reference, el.location.getX(), el.location.getY(), el.location.getWidth(), el.location.getHeight())) {
        myThis.layout.resizeBox(el.reference, el.location.getWidth(), el.location.getHeight());
        if (!fromScratch)
          myThis.layout.moveBox(el.reference, el.location.getX(), el.location.getY());
      }
    }
    var cmparr = [];

    for (var r in clonedLines) {
      var el = clonedLines[r];
      var settings = getLayoutSettingsForEdge(null, el, true);
      if (!myThis.layout.addLine(el.reference, el.start[0].reference, el.end[0].reference, settings)) {
        if (!fromScratch) {
          myThis.layout.moveLine(el.reference, el.start[0].reference, el.end[0].reference, settings.points);
        }
      } else
        cmparr.push(el);
    }

    // adding compartments...
    for (var r in cmparr) {
      var el = cmparr[r];
      if (el.compartment) {
        for (var i = 0; i < el.compartment.length; i++) {
          var style = el.compartment[i].style;
          var w = getTextWidth(el.compartment[i].input, style.getFontFamily() + " " + style.getFontSizeInPt() + "pt") + 10;
          var h = getFontHeight(style.getFontFamily(), style.getFontSizeInPt() * 1.45 + 0.5) + 2;
          myThis.layout.addLineLabel(el.compartment[i].reference, el.reference, w, h, style.getPlacement());
        }
      }
    }

  };


  /**
   * Adjusts width/height depending on whether the element is a vertical/horizontal fork.
   * @private
   * @param obj repository element, which can be connected to its type via the elemType link
   * @param el cloned element corresponding to the given repository element
   */
  var adjustIfFork = function (obj, el) {
    if (!obj.elemType)
      return;

    if (el.location.getWidth() == 0)
      el.location.setWidth(110);

    if (el.location.getHeight() == 0)
      el.location.setHeight(49);

    if (obj.elemType && (obj.elemType[0].caption == "HorizontalFork"))
      el.location.setHeight(4);

    if (obj.elemType && (obj.elemType[0].caption == "VerticalFork"))
      el.location.setWidth(4);

    var s = el.location.toString();
    if (s != obj.location)
      obj.setLocation(s);
  };


  /**
   * Adjusts width/height depending on whether the element is a vertical/horizontal fork.
   * @private
   * @param obj repository element, which can be connected to its type via the elemType link
   * @param el cloned element corresponding to the given repository element
   * @param usePoints whether to returns the points array in the return object
   * @returns {object} a JS object in the form { lineType: "ORTHOGONAL", startSides:15, endsSides:15, points: [{x:x1,y:y1},{x:x2,y:y2},{x:xN,y:yN}]}
   */
  var getLayoutSettingsForEdge = function (obj /*repo*/ , el /*cloned*/ , usePoints) {
    var startNode = el.start[0];
    var endNode = el.end[0];
    if (obj) {
      startNode = obj.start[0];
      endNode = obj.end[0];
    }
    var startSides = 15;
    if (el.lastStartSides)
      startSides = el.lastStartSides;
    var endSides = 15;
    if (el.lastEndSides)
      endSides = el.lastEndSides;
    if (startNode.elemType && ((startNode.elemType[0].caption == "HorizontalFork") || (startNode.elemType[0].caption == "VerticalFork"))) {
      if (startNode.elemType[0].caption == "HorizontalFork")
        startSides = 5;
      else
        startSides = 10;
      el.lastStartSides = startSides;
    }
    if (endNode.elemType && ((endNode.elemType[0].caption == "HorizontalFork") || (endNode.elemType[0].caption == "VerticalFork"))) {
      if (endNode.elemType[0].caption == "HorizontalFork")
        endSides = 5;
      else
        endSides = 10;
      el.lastEndSides = endSides;
    }

    if (usePoints) {
      // adjust points...
      var xyArr = el.location.getPointsXY();
      /*var L = xyArr.length-1;
      if (startNode.elemType && (startNode.elemType[0].caption=="HorizontalFork")) {
      	var nl = new NodeLocation(startNode.location);
      	if (xyArr[0].y > nl.getY()+nl.getHeight())
      		xyArr[0].y = nl.getY()+nl.getHeight();
      	if (xyArr[0].y < nl.getY())
      		xyArr[0].y = nl.getY();
      }
      else
      if (endNode.elemType && (endNode.elemType[0].caption=="HorizontalFork")) {
      	var nl = new NodeLocation(endNode.location);
      	if (xyArr[L].y > nl.getY()+nl.getHeight())
      		xyArr[L].y = nl.getY()+nl.getHeight();
      	if (xyArr[L].y < nl.getY())
      		xyArr[L].y = nl.getY();
      }*/
      //return {lineType:"ORTHOGONAL", startSides:startSides, endSides:endSides, points:el.location.getPointsXY()};
      return {
        lineType: "ORTHOGONAL",
        startSides: startSides,
        endSides: endSides,
        points: xyArr
      };
    } else
      return {
        lineType: "ORTHOGONAL",
        startSides: startSides,
        endSides: endSides
      };
  }

  ///// PUBLIC METHODS /////

  /**
   * Adds (or updates) a box to (in) this diagram editor. Call refresh() after finishing adding boxes and lines to adjust (incrementally) the diagram.
   * @param {object} obj a JS (a repository) object representing a box.
   * The fields are:
   * <table cellspacing=0 cellpadding=0>
   * <tr><td>reference</td><td>a number representing the id of this box</td></tr>
   * <tr><td>location</td><td>box dimensions and coordinates in the form "width;height;left;top" //TODO!</td></tr>
   * <tr><td>style</td><td>box style in the form<br/>
   * {<br/>
   * fillColor:,<br/>
   * lineColor:,<br/>
   * lineWidth:,<br/>
   * shape:	"Rectangle" or "RoundRectangle" or "Package" or "Note" or "Circle" or "Triangle" or "HorizontalLine" or "VerticalLine",<br/>
   * dashLength:,<br/>
   * dashBreakLength,<br/>
   * }<br/>
   * Alternatively, style value can be a string in legacy AZ node style encoding.
   * </td></tr>
   * <tr><td>compartment</td><td>an array of compartments, where each compartment is in the form:<br/>
   * <code>
   * {<br/>
   * reference: &lt;compartment id&gt;,<br/>
   * input: "compartment text",<br/>
   * style: {<br/>
   * alignment: "left" or "center" or "right",<br/>
   * fontColor:,<br/>
   * fontFamily:,<br/>
   * fontSize: &lt;number in pt&gt;<br/>
   * fontWeight: "bold" or "italic" or "normal",<br/>
   * lineWidth:,<br/>
   * isVisible:,<br/>
   * } // alternatively, the style can be a string in legacy AZ compartment style encoding<br/>
   * }<br/>
   * </code>
   * </td></tr>
   * <tr><td>elemType</td><td>undefined or [{caption:"HorizontalFork" or "VerticalFork"}] for specifying a fork</td></tr>
   * </table>
   */
  this.addBox = function (obj) {
    var el = cloneElement(obj);
    if (!el.className)
      el.className = "Node";
    clonedBoxes[el.reference] = el;
    adjustIfFork(obj, el);
    ajoo_de.addNode(el);
    if (this.layout) {
      runInLayoutThread(0, function () {
        if (!myThis.layout.addBox(el.reference, el.location.getX(), el.location.getY(), el.location.getWidth(), el.location.getHeight())) {
          myThis.layout.resizeBox(el.reference, el.location.getWidth(), el.location.getHeight());
          myThis.layout.moveBox(el.reference, el.location.getX(), el.location.getY());
        }
      });
    }
  };

  this.boxExists = function (reference) {
    return clonedBoxes[reference];
  };

  /**
   * Updates location of the given box. Call refresh() after updating all box and line locations to adjust the diagram.
   * @param {number} reference box id
   * @param {number} x new left coordinate
   * @param {number} y new top coordinate
   * @param {number} w new width
   * @param {number} h new height
   */
  this.updateBoxLocation = function (reference, x, y, w, h) {
    // update ajoo and layout
    ajoo_de.updateNodeLocation(reference, x, y, w, h);
    if (this.layout) {

      if (!this.layout.addBox(reference, x, y, w, h)) {
        try {
          this.layout.resizeBox(reference, w, h);
          this.layout.moveBox(reference, x, y);
        } catch (t) {
          console.log("Exception during resizeBox/moveBox.", reference, clonedBoxes[reference], x, y, w, h, t);
        }
      }

    }
  };

  /**
   * Temporarily changes location of the given line label (=line compartment) on the screen until the next refresh (since adjusting the diagram moves line labels).
   * @param {number} reference box id
   * @param {number} x new left coordinate
   * @param {number} y new top coordinate
   * @param {number} w new width
   * @param {number} h new height
   */
  this.updateLineLabelLocation = function (reference, x, y, w, h) {
    // update ajoo (but no layout)
    ajoo_de.updateLabelLocation(reference, x, y, w, h);
  };

  /**
   * Removes the given box from the diagram (and from internal layout structures). You may wish to call refresh() after deletions to adjust the diagram.
   * @param {number} reference box id
   */
  this.removeBox = function (reference) {
    // remove from map, from layout and from ajoo
    delete clonedBoxes[reference];
    if (this.layout)
      this.layout.removeBox(reference);
    ajoo_de.removeElements([{
      reference: reference
    }]);
  };

  /**
   * Adds (or updates) a line to (in) this diagram editor. Call refresh() after finishing adding boxes and lines to adjust the diagram.
   * @param {object} obj a JS (a repository) object representing a line.
   * The fields are:
   * <table cellspacing=0 cellpadding=0>
   * <tr><td>reference</td><td>a number representing the id of this line</td></tr>
   * <tr><td>location</td><td>line coordinates in the form "N\x1,y1\x2,y2\xN,yN" //TODO!</td></tr>
   * <tr><td>start</td><td>[box-object] or [{className:"Node",reference:&lt;id&gt;,elemType:undefined or [{caption:"HorizontalFork" or "VerticalFork"}]}] //TODO! startNode</td></tr>
   * <tr><td>end</td><td>[box-object] or [{className:"Node",reference:&lt;id&gt;,elemType:undefined or [{caption:"HorizontalFork" or "VerticalFork"}]}] //TODO! startNode</td></tr>
   * <tr><td>style</td><td>line style in the form<br/>
   * {<br/>
   * lineColor:,<br/>
   * lineWidth:,<br/>
   * startStyle:{<br/>
   * shape:	"None" or "Triangle" or "Diamond" or "Triangle" or "Arrow",<br/>
   * lineColor:<br/>
   * lineWidth:<br/>
   * fillColor:<br/>
   * }<br/>
   * endStyle:{...},<br/>
   * dashLength:,<br/>
   * dashBreakLength,<br/>
   * }<br/>
   * Alternatively, style value can be a string in legacy AZ edge style encoding.
   * </td></tr>
   * <tr><td>compartment</td><td>an array of compartments, where each compartment is in the form:<br/>
   * <code>
   * {<br/>
   * reference: &lt;compartment id&gt;,<br/>
   * input: "compartment text",<br/>
   * style: {<br/>
   * fontColor:,<br/>
   * fontFamily:,<br/>
   * fontSize: &lt;number in pt&gt;<br/>
   * fontWeight: "bold" or "italic" or "normal",<br/>
   * placement: "start-left" or "start-right" or "end-left" or  "end-right" or "middle-left" or "middle-right",<br/>
   * lineWidth:,<br/>
   * isVisible:,<br/>
   * } // alternatively, the style can be a string in legacy AZ compartment style encoding<br/>
   * }<br/>
   * </code>
   * </td></tr>
   * </table>
   */

  this.addLine = function (obj) {

    var el = cloneElement(obj);
    if (!el.className)
      el.className = "Edge";
    clonedLines[el.reference] = el;
    var compartmentsArr = el.compartment;
    el.compartment = [];
    if (compartmentsArr) {
      for (var i = 0; i < compartmentsArr.length; i++) {
        if (!compartmentsArr[i].input)
          continue;
        var value = compartmentsArr[i].input.split("\n").join("");
        compartmentsArr[i].input = "";
        compartmentsArr[i].input2 = value;
        if ((value.length > 0) && (compartmentsArr[i].style.getIsVisible())) {
          el.compartment.push(compartmentsArr[i]);
          var w = getTextWidth(value, compartmentsArr[i].style.getFontFamily() + " " + compartmentsArr[i].style.getFontSizeInPt() + "pt") + 10;
          compartmentsArr[i].style.setWidth(w);
        }
      }
    }
    ajoo_de.addEdge(el);
    var settings = getLayoutSettingsForEdge(obj, el, true); // stores startSides and endSides

    if (this.layout) {
      runInLayoutThread(1, function () {
        try {
          if (!myThis.layout.addLine(el.reference, el.start[0].reference, el.end[0].reference, settings)) {
            myThis.layout.moveLine(el.reference, el.start[0].reference, el.end[0].reference, settings.points);
          }
        } catch (t) {
          console.log("Exception during addLine. Reset...", t);
          myThis.resetLayout(null, true);
        }
      });
    }

    if (el.compartment) {
      for (var i = 0; i < el.compartment.length; i++) {
        var style = el.compartment[i].style;
        //var w = style.getWidth();//getTextWidth(el.compartment[i].input, style.getFontFamily()+" "+style.getFontSizeInPt()+"pt")+8;
        var w = getTextWidth(el.compartment[i].input2, style.getFontFamily() + " " + style.getFontSizeInPt() + "pt") + 10;
        var h = getFontHeight(style.getFontFamily(), style.getFontSizeInPt() * 1.45 + 0.5) + 2;
        if (this.layout) {
          setTimeout(function (el, i, w, h, style) {
            runInLayoutThread(2, function () {
              myThis.layout.addLineLabel(el.compartment[i].reference, el.reference, w, h, style.getPlacement());
            });
          }, 0, el, i, w, h, style);
          /*          var rComp=el.compartment[i].reference;
                    var r=el.reference;
                    var placement = style.getPlacement();
                    runInLayoutThread(2, function () {
                      myThis.layout.addLineLabel(rComp, r, w, h, placement);
                    });*/
        }
        var loc = new NodeLocation();
        loc.setX(style.getOffsetX());
        loc.setY(style.getOffsetY());
        loc.setWidth(w);
        loc.setHeight(h);
        var st1 = new NodeStyle();
        st1.setLineWidth(0);
        st1.setFillColor(intToColor(colorToInt(myThis.settings.backgroundColor)));
        st1.setLineColor(intToColor(colorToInt(myThis.settings.backgroundColor)));
        var st2 = new CompartmentStyle();
        st2.setLineWidth(0);
        var lineLabelNode = {
          className: "Label",
          isLineLabel: true,
          reference: el.compartment[i].reference,
          location: loc,
          style: st1,
          compartment: [{
            reference: el.compartment[i].reference,
            input: el.compartment[i].input2,
            style: st2
          }],
        };

        ajoo_de.addNode(lineLabelNode);
      } // for compartments...
    } // if el.compartment
  };

  /**
   * Checks whether the line has already been added to this diagram editor.
   * @param {number} reference line id
   * @returns {boolean} whether a line with the given id exists
   */
  this.lineExists = function (reference) {
    return clonedLines[reference];
  };

  /**
   * Sets points for the given line. Call refresh() after updating all box and line locations to adjust the diagram.
   * @param {number} reference line id
   * @param {array} points array in the form [{x:x1,y:y1}, {x:x2,y:y2}, {x:xN,y:yN}]
   */
  this.updateLineLocation = function (reference, xyarr) { //, newSourceReference, newTargetReference) {
    // update ajoo and layout
    var arr = [];
    for (var i = 0; i < xyarr.length; i++) {
      arr.push(xyarr[i].x);
      arr.push(xyarr[i].y);
    }
    ajoo_de.updateEdgeLocation(reference, arr);

    if (this.layout) {
      try {
        var el = clonedLines[reference];
        var settings = getLayoutSettingsForEdge(null, el, false);
        settings.points = xyarr;
        if (!this.layout.addLine(reference, clonedLines[reference].start[0].reference, clonedLines[reference].end[0].reference, settings)) {
          this.layout.moveLine(reference, clonedLines[reference].start[0].reference, clonedLines[reference].end[0].reference, xyarr);
        } else {
          // adding labels...
          if (el.compartment) {
            for (var i = 0; i < el.compartment.length; i++) {
              var style = el.compartment[i].style;
              var w = getTextWidth(el.compartment[i].input, style.getFontFamily() + " " + style.getFontSizeInPt() + "pt") + 10;
              var h = getFontHeight(style.getFontFamily(), style.getFontSizeInPt() * 1.45 + 0.5) + 2;
              this.layout.addLineLabel(el.compartment[i].reference, el.reference, w, h, style.getPlacement());
            }
          }
        }
      } catch (t) {
        console.log("Exception during addLine. Reset...", t);
        this.resetLayout(null, true);
      }
    }

  };

  /**
   * Removes the given line from the diagram (and from internal layout structures). You may wish to call refresh() after deletions to re-arrange the diagram.
   * @param {number} reference line id
   */
  this.removeLine = function (reference) {
    var arr = [];
    var el = clonedLines[reference];

    if (el.compartment) {
      for (var i = 0; i < el.compartment.length; i++) {
        arr.push({
          reference: el.compartment[i].reference
        });
      }
    }

    delete clonedLines[reference];
    if (this.layout)
      this.layout.removeLine(reference);

    arr.push({
      reference: reference
    });
    ajoo_de.removeElements(arr);
  };

  /**
   * Visually selects the given diagram elements.
   * @param {array} arr array of boxes and/or lines to be selected; each array element must have the reference attribute
   */
  this.selectElements = function (arr) {
    ajoo_de.selectElements(arr);
  };

  /**
   * Sets user-defined layout manager or switches to the manual mode (if no manager is specified).
   * In manual mode, diagram won't be re-arranged on refresh; boxes and line may overlap.
   * @param {object} layout user-defined layout manager implementing the same API as ICMSDiagramLayout or null for the manual mode
   * @param {boolean} skipAdd whether to skip adding existing boxes and lines to the user-defined layout manager (they are added by default); 
   *                          this option is useful if the user-defined layout manager has been already initialized with the existing boxes and lines
   */
  this.setLayoutManager = function (layout, skipAdd) {
    this.layout = layout;
    if ((layout == null) || (skipAdd))
      return;

    addClonedToLayout();

    // arrangeIncrementally will be called on refresh()
  };

  /**
   * Sets the built-in default layout manager. Useful from switching back from the manual mode to the semi-automatic mode, where the diagram
   * is automatically re-arranged on refresh.
   * @param {boolean} skipAdd whether to skip adding existing boxes and lines to the default layout manager (they are added by default);
   *                          perhaps, this option is only useful when other coordinates will be assigned to existing boxes and lines right away
   */
  this.setDefaultLayoutManager = function (skipAdd) {
    this.layout = new IMCSDiagramLayout("UNIVERSAL", true);
    if (skipAdd)
      return;

    addClonedToLayout();
  };

  /**
   * Re-arrange the diagram from scratch using the given arrangment name. Call refresh() then to repaint the diagram.
   * @param {string} name “UNIVERSAL”, “SYMMETRIC”, “VERTICAL” (for an edge a→b the layout algorithm will return y_max[a]≤y_min[b]), “INVERSE_VERTICAL”, “HORIZONTAL”, or “INVERSE_HORIZONTAL”;
   * if name not specified, the diagram is only adjusted incrementally.
   */
  this.resetLayout = function (name, slow) {
    if (name)
      this.layout = new IMCSDiagramLayout(name, !slow);
    else
      this.layout = new IMCSDiagramLayout("UNIVERSAL", !slow);

    // adding and re-arranging...
    var fromScratch = false;
    if (name)
      addClonedToLayout(fromScratch);
    if (name) {
      try {
        var coos = this.layout.arrangeFromScratch();
        setCoos(coos);
      } catch (t) {
        console.log("Exception occurred during resetLayout().");
        if (!slow) {
          console.log("Switching to slow-mode...", t);
          this.resetLayout(name, true);
        }
      }
    } else {
      console.log("resetLayout incrementally, calling arrangeIncrementally..");
      var coos = this.layout.arrangeIncrementally();
    }
  };

  /**
   * Re-arranges and repaints the diagram (asynchronously). In manual mode (when no layout manager is preset) the diagram is just repainted.
   * @param {function} fAfter a function to be called after the diagram is repainted
   */
  this.refresh = function (fAfter) {
//    console.log("refresh()");
    runInLayoutThread(4, function () { // waiting for all elements to be added to the diagram...
      ajoo_de.whenReady(function () {
        logDiagram();
        setTimeout(function () {
          runInLayoutThread(4, function () {
            if (myThis.layout) {
              try {
//                console.log("refresh: arranging incrementally...");
                var t1 = now();
                var coos = myThis.layout.arrangeIncrementally();
                var t2 = now();
                setCoos(coos);
                var t3 = now();
//                console.log("arranged incrementally in " + (t2 - t1) + " ms, setCoos in " + (t3 - t2)+" ms");
              } catch (t) {
                console.log("Exception occurred during refresh(). Switching to slow-mode...", t);
                myThis.resetLayout(null, true);
              }
            }
            ajoo_de.refresh();
            updateScrollBars(ajoo_de.getVisibleWidth(), ajoo_de.getVisibleHeight(), ajoo_de.getTotalWidth(), ajoo_de.getTotalHeight());
            if (fAfter) {
              ajoo_de.whenReady(function () {
                fAfter();
              });
            }
          });
        }, 0);
      });
    });
  };

  /**
   * @returns {set-of-references} the set of box ids in this diagram editor; the set is in the form { &lt;box-reference1&gt;: true, &lt;box-reference2&gt;: true, ...}
   */
  this.getBoxesSet = function () {
    var retVal = {};
    for (var x in clonedBoxes)
      retVal[x] = true;
    return retVal;
  };

  /**
   * @returns {set-of-references} the set of line ids in this diagram editor; the set is in the form { &lt;line-reference1&gt;: true, &lt;line-reference2&gt;: true, ...}
   */
  this.getLinesSet = function () {
    var retVal = {};
    for (var x in clonedLines)
      retVal[x] = true;
    return retVal;
  };

};

