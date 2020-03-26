package client;

import com.google.gwt.core.client.EntryPoint;


import lv.lumii.layoutengine.ArrangeData;
import lv.lumii.layoutengine.Box;
import lv.lumii.layoutengine.Diagram;
import lv.lumii.layoutengine.LayoutConstraints;
import lv.lumii.layoutengine.Line;
import lv.lumii.layoutengine.OutsideLabel;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.HashMap;

import java.util.*;

import com.google.gwt.core.client.JavaScriptObject;


public class IMCSLayoutEngineEntryPoint implements EntryPoint  {



	native void registerWrapper() /*-{

		IMCSDiagramLayout = function(arrangeStyle, fastAdd) {
		  this.jObj = @lv.lumii.diagramlayout.IMCSDiagramLayout::new(Ljava/lang/String;)(arrangeStyle);

                  this.jObj.@lv.lumii.diagramlayout.IMCSDiagramLayout::fastAdd = fastAdd;
		  this.addBox = function(boxId, x, y, w, h) {
		  // adds a box with the given id, x, y, width, and height to the layout;
                  // returns whether the operation succeeded;
                     return this.jObj.@lv.lumii.diagramlayout.IMCSDiagramLayout::addBox(DDDDD)(boxId, x, y, w, h);
		  };

		  this.addLine = function(lineId, srcId, tgtId, settings) {
		  // adds an orthogonal line connecting the two boxes with the given ids to the layout;
                  // returns whether the operation succeeded;
                     var jPoints = null;
                     if (settings.points && (settings.points.length>=2)) {
		        jPoints = @java.util.ArrayList::new()();
		        for (var i=0; i<settings.points.length; i++) {
		          var jPoint = @java.awt.geom.Point2D.Double::new(DD)(settings.points[i].x, settings.points[i].y);
                          jPoints.@java.util.ArrayList::add(Ljava/lang/Object;)(jPoint);
		        }
                     }

                     return this.jObj.@lv.lumii.diagramlayout.IMCSDiagramLayout::addLine(
                       DDDLjava/lang/String;IILjava/util/ArrayList;)(lineId, srcId, tgtId,
                         settings.lineType, settings.startSides?settings.startSides:15,
                         settings.endSides?settings.endSides:15, jPoints);
		  };

		  this.addLineLabel = function(labelId, lineId, w, h, placement) {
		     return this.jObj.@lv.lumii.diagramlayout.IMCSDiagramLayout::addLineLabel(
                       DDDDLjava/lang/String;)(labelId, lineId, w, h, placement);
		  };

		  this.removeLine = function(lineId) {
		    var al = this.jObj.@lv.lumii.diagramlayout.IMCSDiagramLayout::removeLine(D)(lineId);
		    if (!al)
		      return null;
	            var retVal = [];

                    var n = al.@java.util.ArrayList::size()();
                    for (var i=0; i<n; i++) {
                      var id = al.@java.util.ArrayList::get(I)(i);
                      retVal.push(id);
                    }

                    return retVal;
		  };

		  this.removeBox = function(boxId) {
		    var al = this.jObj.@lv.lumii.diagramlayout.IMCSDiagramLayout::removeBox(D)(boxId);
		    if (!al)
		      return null;
	            var retVal = [];

                    var n = al.@java.util.ArrayList::size()();
                    for (var i=0; i<n; i++) {
                      var id = al.@java.util.ArrayList::get(I)(i);
                      retVal.push(id);
                    }

                    return retVal;
                  };

                  this.moveBox = function(boxId, newX, newY) {
		  // sets new desired coordinates for the box with the given id;
		  // the layout is not re-arranged (call arrange() after
                  // the desired coordinates of all the desired boxes are set);
                  // returns whether the operation succeeded;

		    return this.jObj.@lv.lumii.diagramlayout.IMCSDiagramLayout::moveBox(DDD)(boxId, newX, newY);
                  };

                  this.resizeBox = function(boxId, w, h) {
		  // sets new desired dimensions for the box with the given id;
		  // the layout is not re-arranged (call arrange() after
                  // the desired dimensions of all the desired boxes are set);
                  // returns whether the operation succeeded;
		    return this.jObj.@lv.lumii.diagramlayout.IMCSDiagramLayout::resizeBox(DDD)(boxId, w, h);
                  };

                  this.moveLine = function(lineId, srcId, tgtId, points) {
		  // sets new line start and end boxes and (optionally) line points;
		  // the points are specified as an array of objects with the x and y attributes;
		  // the layout is not re-arranged (call arrange() after all
                  // desired manipulations are called);
                  // returns whether the operation succeeded;

                     var jPoints = null;
                     if (points) {
		        jPoints = @java.util.ArrayList::new()();
		        for (var i=0; i<points.length; i++) {
		          var jPoint = @java.awt.geom.Point2D.Double::new(DD)(points[i].x, points[i].y);
                          jPoints.@java.util.ArrayList::add(Ljava/lang/Object;)(jPoint);
		        }
                     }

                     return this.jObj.@lv.lumii.diagramlayout.IMCSDiagramLayout::moveLine(
                       DDDLjava/util/ArrayList;)(lineId, srcId, tgtId, jPoints);
                  };

		  this.arrangeIncrementally = function() {
		  // arranges the diagram taking into a consideration recently added elements
                  // and trying to preserve existing coordinates;
		  // returns an objects with the "boxes", "lines", and "labels" maps
                  // containing information about the layout;
                  // the boxes map is in the form <id> -> {x, y, width, height};
                  // the lines map is in the form <id> -> [ {x:x1,y:y1}, {x:x2,y:y2}, ... ]
                  // the labels map is in the form <id> -> {x, y, width, height};
                    return this.convertLayoutInfo(
                      this.jObj.@lv.lumii.diagramlayout.IMCSDiagramLayout::arrangeIncrementally()()
                    );
                  };

		  this.arrangeFromScratch = function() {
		  // arranges the diagram from scratch not preserving existing coordinates;
		  // returns an objects with the "boxes", "lines", and "labels" maps
                  // containing information about the layout;
                  // the boxes map is in the form <id> -> {x, y, width, height};
                  // the lines map is in the form <id> -> [ {x:x1,y:y1}, {x:x2,y:y2}, ... ]
                  // the labels map is in the form <id> -> {x, y, width, height};

                    return this.convertLayoutInfo(
                      this.jObj.@lv.lumii.diagramlayout.IMCSDiagramLayout::arrangeFromScratch()()
                    );
                  };

		  this.getLayout = function() {
		  // returns an objects with the "boxes", "lines", and "labels" maps
                  // containing information about the layout;
                  // the boxes map is in the form <id> -> {x, y, width, height};
                  // the lines map is in the form <id> -> [ {x:x1,y:y1}, {x:x2,y:y2}, ... ]
                  // the labels map is in the form <id> -> {x, y, width, height};

                    return this.convertLayoutInfo(
                      this.jObj.@lv.lumii.diagramlayout.IMCSDiagramLayout::getLayout()()
                    );
                  };

                  this.convertLayoutInfo = function(info) {
		    retVal = { boxes: {}, lines: {}, labels: {} };

		    var m = info.@lv.lumii.diagramlayout.IMCSDiagramLayout.LayoutInfo::boxes;
		    var s = m.@java.util.Map::keySet()();
		    var it = s.@java.lang.Iterable::iterator()();
		    while (it.@java.util.Iterator::hasNext()()) {
		      var key = it.@java.util.Iterator::next()();
		      var item = m.@java.util.Map::get(Ljava/lang/Object;)(key);
                      retVal.boxes[key.@java.lang.Long::doubleValue()()] = {
                        x : item.@java.awt.geom.Rectangle2D.Double::x,
                        y : item.@java.awt.geom.Rectangle2D.Double::y,
                        width : item.@java.awt.geom.Rectangle2D.Double::width,
                        height : item.@java.awt.geom.Rectangle2D.Double::height,
                      };
		    };

		    m = info.@lv.lumii.diagramlayout.IMCSDiagramLayout.LayoutInfo::lines;
		    s = m.@java.util.Map::keySet()();
		    it = s.@java.lang.Iterable::iterator()();
		    while (it.@java.util.Iterator::hasNext()()) {
		      var key = it.@java.util.Iterator::next()();
		      var item = m.@java.util.Map::get(Ljava/lang/Object;)(key);
		      var n = item.@java.util.ArrayList::size()();
		      var arr = [];
		      for (var i=0; i<n; i++) {
		        var p = item.@java.util.ArrayList::get(I)(i);
		        arr.push( {
		          x : p.@java.awt.geom.Point2D.Double::x,
		          y : p.@java.awt.geom.Point2D.Double::y
		        });
		      };

                      retVal.lines[key.@java.lang.Long::doubleValue()()] = arr;
		    };

		    m = info.@lv.lumii.diagramlayout.IMCSDiagramLayout.LayoutInfo::labels;
		    s = m.@java.util.Map::keySet()();
		    it = s.@java.lang.Iterable::iterator()();
		    while (it.@java.util.Iterator::hasNext()()) {
		      var key = it.@java.util.Iterator::next()();
		      var item = m.@java.util.Map::get(Ljava/lang/Object;)(key);
                      retVal.labels[key.@java.lang.Long::doubleValue()()] = {
                        x : item.@java.awt.geom.Rectangle2D.Double::x,
                        y : item.@java.awt.geom.Rectangle2D.Double::y,
                        width : item.@java.awt.geom.Rectangle2D.Double::width,
                        height : item.@java.awt.geom.Rectangle2D.Double::height,
                      };
		    };

                    return retVal;
                  };

		  return this; // JS constructor
                };

                IMCSDialogLayout = function(jsCallback) {
                  this.jThis = @lv.lumii.dialoglayout.IMCSDialogLayout::new(Ljava/lang/Object;)(jsCallback);

                  this.loadStarted = function(rComponent) {
                     this.jThis.@lv.lumii.dialoglayout.IMCSDialogLayout::loadStarted(D)(rComponent);
                  };

                  this.loadFinished = function(rComponent) {
                     this.jThis.@lv.lumii.dialoglayout.IMCSDialogLayout::loadFinished(D)(rComponent);
                  };

                  this.loadAndLayout = function(rForm) {
                    this.jThis.@lv.lumii.dialoglayout.IMCSDialogLayout::loadAndLayout(D)(rForm);
                  };

                  this.refreshAndLayout = function(rRootComponent, formWidth, formHeight) {
                    this.jThis.@lv.lumii.dialoglayout.IMCSDialogLayout::refreshAndLayout(DII)(rRootComponent, formWidth, formHeight);
                  };

                  return this;
                };

	}-*/;


        public static native void consoleLog( String s ) 
    /*-{ console.log( s ); }-*/;

	public void onModuleLoad() { 
		registerWrapper();
	}

}

