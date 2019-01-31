/*                   
settings = {
	diagramDiv:string,
	readOnly:boolean,
	paletteElements:array of TDA repository objects in JSON syntax,
	backgroundColor:string,

	onNewBox:function(palette_element_object_in_TDA_JSON_syntax, x, y, w, h),
	onNewLine:function(palette_element_object_in_TDA_JSON_syntax,
          source_node_object_in_TDA_JSON_syntax,
          target_node_object_in_TDA_JSON_syntax,
          points: array of {x, y}
        ),
        onElementsMoved:function(arr_in_TDA_JSON_syntax),

        onElementsSelected:function(arr_in_TDA_JSON_syntax),
        onElementDoubleClick:function(reference),
        onElementRightClick:function(reference),
        onDiagramRightClick:function(),

        onceReady - called once, when the diagram loading has been finished and
                                  the initial layout has been performed

}

functions:
	addBox(tda.model.obj), // adds or updates
        updateBoxLocation = function(reference, x, y, w, h)
	removeBox(reference),

	addLine(tda.model.obj),
        updateLineLocation = function(reference, arr_of_{x,y}),
        updateLineLabelLocation = function(reference, x, y, w, h) // only visual part, not within layout
	removeLine(reference)

        selectElements(arr_of_{reference:nnn}) - via ajoo when data added...

        setLayoutManager(layout implementing IMCSDiagramLayout API, skipAdd)
          // initial layout manager is created by default;
          // when layout manager is changed and reAdd==true,
          // all existing boxes/lines are re-added
          // if null, switches to manual mode

        resetLayout(); // swithes to default
         =     updateCoos(gd, true);

        resetLayout(str); // swithes to "symmetrical, INVERSE_VERTICAL, universal"
         =     updateCoos(gd, false);

        refresh() - calls arrangeIncrementally, repaints;
                    must be called after calling any of the previous functions
                    (or after a bulk of functions);

        getBoxesSet - returns a set of references (set=object with fields reference:true)
        getLinesSet - returns a set of references (set=object with fields reference:true)
*/

IMCSDiagramEditor = function(settings) {
  var myThis = this;

  window[settings.diagramDiv].innerHTML = 
    '<div id="diagramContentDiv" onwheel="moveScrollWheel(event);" style="width:100%; height:100%; overflow:hidden; position:absolute; left:0;top:0;"></div>'+
    '<div id="theMoveWrap"'+
    '     onwheel="moveZoomWheel(event);"'+
    '     onmousedown="mouseMoveStart(event);"'+
    '     onmouseup="mouseMoveEnd(event);"'+
    '     onmousemove="mouseMove(event);"'+
    '     oncontextmenu="window.toggleMoving();return false;" style="margin-left:40px; opacity:0.2; position:absolute; left:0; top:0; right:0; bottom:0; background-color:cccccc; display:none;">'+
    '</div>'+
    '<div id="scrollbar2"></div>'+
    '<div id="scrollbar1"></div>'+
    '<div id="theZoomMoveDiv" align=center>'+
    '  <span id="theZoomValue" align=center style="display:none;">100%</span>'+
    '  <div id="theZoomDiv" align=center>'+
    '    <div id="theZoomIn" onclick="ajoo_de.zoomIn();showZoomValue();">&nbsp;+&nbsp;</div>'+
    '    <div id="theZoomOut" onclick="ajoo_de.zoomOut();showZoomValue();">&nbsp;&ndash;&nbsp;</div>'+
    '  </div>'+
    '  <div id="theMiddleDiv"></div>'+
    '  <div id="theMoveDiv" onclick="toggleMoving();">&nbsp;&#x2725;&nbsp;</div>'+
    '</div>'+
    ''+
    '<div id="thePleaseWaitDivWrap" style="z-index:9998; opacity:0.2; position:absolute; left:0; top:0; right:0; bottom:0; background-color:cccccc; display:none;"></div>'+
    '<div id="thePleaseWaitDiv" style="z-index:9999; opacity:0.99; position:absolute; left:200; top:100; background-color:eeeeee; padding:20;  border-style:ridge; display:none;">Please, wait...</div>'
  ;


  $(window).resize( function() {
    var f = function() {
	if (ajoo_de.initialized()) {
		    ajoo_de.setVisibleWidth($("#diagramContentDiv").width()-40); // -paletteSize
		    ajoo_de.setVisibleHeight($("#diagramContentDiv").height());
		    updateScrollBars(ajoo_de.getVisibleWidth(),ajoo_de.getVisibleHeight(), ajoo_de.getTotalWidth(), ajoo_de.getTotalHeight());
		    ajoo_de.repaint();
	}
	else
	        setTimeout(f, 10);
    };
    f();
  });

  var clonedBoxes = {}; // id -> cloned box
  var clonedLines = {}; // id -> cloned line

  var setCoos = function(coos) {
    // set new ajoo coos
    var arr=[];

    for (var i in clonedBoxes) {
      var el = clonedBoxes[i];
      var prevLocation = el.location.toString();

      el.location.setX(coos.boxes[el.reference].x);
      el.location.setY(coos.boxes[el.reference].y);
      el.location.setWidth(coos.boxes[el.reference].width);
      el.location.setHeight(coos.boxes[el.reference].height);

      var newLocation = el.location.toString();
      if (prevLocation != newLocation) {
        arr.push({reference:i, location:newLocation});
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
        arr.push({reference:i, location:newLocation});
      }
      ajoo_de.updateEdgeLocation(el.reference, el.location.getPointsArray());
       // elLastLocation[el.reference] = locationStr; // update elLastLocation
      if (el.compartment) {
        for (var i=0; i<el.compartment.length; i++) {
          var cmp = el.compartment[i];
          if (!coos.labels[cmp.reference])
            continue; // ignore, if label has no coos
          ajoo_de.updateLabelLocation(cmp.reference, coos.labels[cmp.reference].x+2, coos.labels[cmp.reference].y+1,
                                 coos.labels[cmp.reference].width-4, coos.labels[cmp.reference].height-2);
          var oldX = cmp.style.getOffsetX();
          var oldY = cmp.style.getOffsetY();
          cmp.style.setOffsetX(coos.labels[cmp.reference].x+2); // do not update width, it will be calculated each time
          cmp.style.setOffsetY(coos.labels[cmp.reference].y+1); // do not update height, it will be calculated each time
          if ((oldX != cmp.style.getOffsetX()) || (oldY != cmp.style.getOffsetY()))
            arr.push({reference:cmp.reference, style:cmp.style.toString()});
        }
      }

    }

    if (myThis.settings.onElementsMoved) {
      if (arr.length>0)
        myThis.settings.onElementsMoved(arr);
    }
  };

  var updateCoosOnMove = function(arr) {
    if (!myThis.layout) {
      if (settings.onElementsMoved) {
         settings.onElementsMoved(arr);
      }
      return;
    }

    for (var i=0; i<arr.length; i++) {
      var r = arr[i].reference;
      if (clonedBoxes[r]) {
        if (clonedBoxes[r].location.toString() != arr[i].location) {
          var loc = new NodeLocation(arr[i].location);
          myThis.layout.resizeBox(r, loc.getWidth(), loc.getHeight());
          myThis.layout.moveBox(r, loc.getX(), loc.getY());
        }
      }
      else
      if (clonedLines[r]) {
        if (clonedLines[r].location.toString() != arr[i].location) {
          var loc = new EdgeLocation(arr[i].location);
          try {
          myThis.layout.moveLine(r, clonedLines[r].start[0].reference, clonedLines[r].end[0].reference, loc.getPointsXY());
          }
          catch(t) {
            console.log("Exception during updateCoosOnMove/moveLine. Re-route line...",t);
            myThis.layout.moveLine(r, clonedLines[r].start[0].reference, clonedLines[r].end[0].reference);
  //          myThis.layout.moveLine(r, clonedLines[r].start[0].reference, clonedLines[r].end[0].reference, loc.getPointsXY());
//            myThis.resetLayout(null, true);
          }
          // !!!! we do not move line compartments (line labels), since in semi-automatic layout, they should be adjusted automatically
        }
      }
    }
    myThis.refresh();
  };

  this.settings = settings;
  var settingsForAjoo = {
    diagramContentDiv: "diagramContentDiv",
    readOnly: settings.readOnly,
    paletteElements: settings.paletteElements,
    backgroundColor: settings.backgroundColor,
    onSurfaceReady: function(ajooDgr) {
      // layout!
      myThis.refresh();

      if (settings.onceReady) {
        settings.onceReady();
        delete settings.onceReady;
      }
    },
    onNewBox: function(palette_element, x, y, w, h) {
      ajoo_de.selectElements([]);
      if (settings.onElementsSelected)
        settings.onElementsSelected([]);
      if (settings.onNewBox)
        settings.onNewBox(palette_element, x, y, w, h);
    },
    onNewLine: settings.onNewLine,
    onElementsChange: function(ajooDgr, arr, doLayout) {
       console.log("ajoo_de callback: onElementsChange", arr);

       if (settings.onElementsSelected)
         settings.onElementsSelected(arr);

       var t1 = now();

       // layout according to elements in the arr;
       updateCoosOnMove(arr);
       var t2 = now();
       console.log("CHANGE TIME ",(t2-t1));

    },
    onDiagramClick : function() {
       console.log("ajoo_de callback: onDiagramClick");
       if (settings.onElementsSelected)
         settings.onElementsSelected([]);
    },
    onDiagramRightClick : settings.onDiagramRightClick,
    onElementClick: function(r) {
      console.log("ajoo_de callback: onElementClick");

      var arr = [];
      arr.push({reference:r});

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

      if (settings.onElementsSelected)
        settings.onElementsSelected(arr);

    },
    onElementDoubleClick: settings.onElementDoubleClick,
    onElementRightClick: function(r) {
      if (settings.onElementsSelected)
        settings.onElementsSelected([{reference:r}]);
      if (settings.onElementRightClick)
        settings.onElementRightClick(r);
    },
    onRepaint: function() {
      showHideScrollBars();
//      window.updateScrollBars(ajoo_de.getVisibleWidth(), ajoo_de.getVisibleHeight(),
//         ajoo_de.getTotalWidth(), ajoo_de.getTotalHeight());
    },
    onLabelMoved: function(r, x, y, w, h) {
      console.log("LABEL MOVED",r, x, y, w, h);

      ajoo_de.updateLabelLocation(r,x,y,w,h);
      ajoo_de.repaint();
    }
  };

  window.ajoo_de = new AjooDiagramEditor(settingsForAjoo);

  this.layout = new IMCSDiagramLayout("UNIVERSAL", true);


  var addToLayout = function(fromScratch) {
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
    for (var r in clonedLines) {
      var el = clonedLines[r];
      var settings = getLayoutSettingsForEdge(null, el, true);
      if (!myThis.layout.addLine(el.reference, el.start[0].reference, el.end[0].reference, settings)) {
        if (!fromScratch)
          myThis.layout.moveLine(el.reference, el.start[0].reference, el.end[0].reference, settings.points);
      }
      else // adding compartments...
      if (el.compartment) {
        for (var i=0; i<el.compartment.length; i++) {
          var style = el.compartment[i].style;
          var w = getTextWidth(el.compartment[i].input, style.getFontFamily()+" "+style.getFontSizeInPt()+"pt")+8;
          var h = getFontHeight(style.getFontFamily(), style.getFontSizeInPt()*1.45+0.5)+2;
          myThis.layout.addLineLabel(el.compartment[i].reference, el.reference, w, h, style.getPlacement());
        }
      }
    }
  };


  var adjustIfFork = function(obj, el) {
	if (!obj.elemType)
		return;
	
	if (el.location.getWidth()==0)
          el.location.setWidth(110);

        if (el.location.getHeight()==0)
          el.location.setHeight(49);

        if (obj.elemType && (obj.elemType[0].caption=="HorizontalFork"))
          el.location.setHeight(4);

	if (obj.elemType && (obj.elemType[0].caption=="VerticalFork"))
          el.location.setWidth(4);

        var s = el.location.toString();
        if (s!=obj.location)
          obj.setLocation(s);
	
	/*if (node.elemType[0].caption=="HorizontalFork") {
		el.location.setHeight(3);
		node.setLocation(el.location.toString());
	}
	else
	if (node.elemType[0].caption=="VerticalFork") {
		el.location.setWidth(3);
		node.setLocation(el.location.toString());
	}*/
  };


  var getLayoutSettingsForEdge = function(obj/*repo*/, el/*cloned*/, usePoints)
  {
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
		  if (startNode.elemType && ((startNode.elemType[0].caption=="HorizontalFork") || (startNode.elemType[0].caption=="VerticalFork"))) {
			if (startNode.elemType[0].caption=="HorizontalFork")
			  startSides = 5;
			else
			  startSides = 10;
			el.lastStartSides = startSides;
		  }
		  if (endNode.elemType && ((endNode.elemType[0].caption=="HorizontalFork") || (endNode.elemType[0].caption=="VerticalFork"))) {
			if (endNode.elemType[0].caption=="HorizontalFork")
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
			return {lineType:"ORTHOGONAL", startSides:startSides, endSides:endSides, points:xyArr};
		}
		else
			return {lineType:"ORTHOGONAL", startSides:startSides, endSides:endSides};
  }

  ///// PUBLIC METHODS /////

  this.addBox = function(obj) {
    var el = cloneElement(obj);
    clonedBoxes[el.reference] = el;
    adjustIfFork(obj, el);
    ajoo_de.addNode(el);

    if (this.layout) {
      if (!this.layout.addBox(el.reference, el.location.getX(), el.location.getY(), el.location.getWidth(), el.location.getHeight())) {
      	this.layout.resizeBox(el.reference, el.location.getWidth(), el.location.getHeight());
      	this.layout.moveBox(el.reference, el.location.getX(), el.location.getY());
      }
    }
  };

  this.boxExists = function(reference) {
    return clonedBoxes[reference];
  };

  this.updateBoxLocation = function(reference, x, y, w, h) {
    // update ajoo and layout
    ajoo_de.updateNodeLocation(reference, x, y, w, h);
    if (this.layout) {

      if (!this.layout.addBox(reference, x, y, w, h)) {
try{
        this.layout.resizeBox(reference, w, h);
        this.layout.moveBox(reference, x, y);
}catch(t) {
        console.log("Exception during resizeBox/moveBox.",reference,clonedBoxes[reference],x,y,w,h, t);
//        this.resetLayout(null, true);
//        this.layout.resizeBox(reference, w, h);
//        this.layout.moveBox(reference, x, y);
}
      }

    }
  };

  this.updateLineLabelLocation = function(reference, x, y, w, h) {
    // update ajoo (but no layout)
    ajoo_de.updateLabelLocation(reference, x, y, w, h);
  };

  this.removeBox = function(reference) {
    // remove from map, from layout and from ajoo
    delete clonedBoxes[reference];
    if (this.layout)
      this.layout.removeBox(reference);
    ajoo_de.removeElements([{reference:reference}]);
  };

  this.addLine = function(obj) {

    var el = cloneElement(obj);
    clonedLines[el.reference] = el;
    var compartmentsArr = el.compartment;
    el.compartment = [];
//SK_LABEL    ajoo_de.addEdge(el);
    if (compartmentsArr) {
      for (var i=0; i<compartmentsArr.length; i++) {
        if (!compartmentsArr[i].input)
          continue;
        var value = compartmentsArr[i].input.split("\n").join("");
        compartmentsArr[i].input = "";
        compartmentsArr[i].input2 = value;
        if ((value.length>0) && (compartmentsArr[i].style.getIsVisible())) {
          el.compartment.push(compartmentsArr[i]);
          var w = getTextWidth(value, compartmentsArr[i].style.getFontFamily()+" "+compartmentsArr[i].style.getFontSizeInPt()+"pt")+8;
          compartmentsArr[i].style.setWidth(w);
        }
      }
    }
    ajoo_de.addEdge(el);
    var settings = getLayoutSettingsForEdge(obj, el, true); // stores startSides and endSides

    if (this.layout) {
      try {
        if (!this.layout.addLine(el.reference, el.start[0].reference, el.end[0].reference, settings)) {
          this.layout.moveLine(el.reference, el.start[0].reference, el.end[0].reference, settings.points);
        }
      }
      catch(t) {
        console.log("Exception during addLine. Reset...", t);
        this.resetLayout(null, true);
      }
    }

    if (el.compartment) {
      for (var i=0; i<el.compartment.length; i++) {
        var style = el.compartment[i].style;
        var w = style.getWidth();//getTextWidth(el.compartment[i].input, style.getFontFamily()+" "+style.getFontSizeInPt()+"pt")+8;
        var h = getFontHeight(style.getFontFamily(), style.getFontSizeInPt()*1.45+0.5)+2;
        if (this.layout) {
          setTimeout(function(el,i,w,h,style) {
            myThis.layout.addLineLabel(el.compartment[i].reference, el.reference, w, h, style.getPlacement());
          },0,el,i,w,h,style);
        }
        var loc = new NodeLocation();
        loc.setX(style.getOffsetX());
        loc.setY(style.getOffsetY());
        loc.setWidth(w); loc.setHeight(h);
        var st1 = new NodeStyle();
        st1.setLineWidth(0);
        st1.setFillColor(intToColor(colorToInt(myThis.settings.backgroundColor)));
        st1.setLineColor(intToColor(colorToInt(myThis.settings.backgroundColor)));
        var st2 = new CompartmentStyle();
        st2.setLineWidth(0);
        var lineLabelNode =  {
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

  this.lineExists = function(reference) {
    return clonedLines[reference];
  };

  this.updateLineLocation = function(reference, xyarr) { //, newSourceReference, newTargetReference) {
    // update ajoo and layout
    var arr = [];
    for (var i=0; i<xyarr.length; i++) {
      arr.push(xyarr[i].x);
      arr.push(xyarr[i].y);
    } 
    ajoo_de.updateEdgeLocation(reference, arr);


    if (this.layout) {
      this.layout.moveLine(reference, clonedLines[reference].start[0].reference, clonedLines[reference].end[0].reference, xyarr);
    }

    if (this.layout) {
      try {
        var el = clonedLines[reference];
        var settings = getLayoutSettingsForEdge(null, el, false);
        settings.points = xyarr;
        if (!this.layout.addLine(reference, clonedLines[reference].start[0].reference, clonedLines[reference].end[0].reference, settings)) {
          this.layout.moveLine(reference, clonedLines[reference].start[0].reference, clonedLines[reference].end[0].reference, xyarr);
        }
        else {
          // adding labels...
          if (el.compartment) {
            for (var i=0; i<el.compartment.length; i++) {
              var style = el.compartment[i].style;
              var w = getTextWidth(el.compartment[i].input, style.getFontFamily()+" "+style.getFontSizeInPt()+"pt")+8;
              var h = getFontHeight(style.getFontFamily(), style.getFontSizeInPt()*1.45+0.5)+2;
              this.layout.addLineLabel(el.compartment[i].reference, el.reference, w, h, style.getPlacement());
            }
          }
        }
      }
      catch(t) {
        console.log("Exception during addLine. Reset...", t);
        this.resetLayout(null, true);
      }
    }

  };


  this.removeLine = function(reference) {
    var arr = [];
    var el = clonedLines[reference];

    if (el.compartment) {
      for (var i=0; i<el.compartment.length; i++) {
         arr.push({reference:el.compartment[i].reference});
      }
    }

    delete clonedLines[reference];
    if (this.layout)
      this.layout.removeLine(reference);

    arr.push({reference:reference});
    ajoo_de.removeElements(arr);
  };

  this.selectElements = function(arr) {
    ajoo_de.selectElements(arr);
  };

  this.setLayoutManager = function(layout, skipAdd) {
    this.layout = layout;
    if ((layout == null) || (skipAdd))
      return;

    // adding... 
    addToLayout();

    // arrangeIncrementally will be called on refresh()
  };

  this.setDefaultLayoutManager = function(skipAdd) {
    this.layout = new IMCSDiagramLayout("UNIVERSAL", true);
    if (skipAdd)
      return;

    // adding... 
    addToLayout();
  };

  this.resetLayout = function(name, slow) {
    if (name)
      this.layout = new IMCSDiagramLayout(name, !slow);
    else
      this.layout = new IMCSDiagramLayout("UNIVERSAL", !slow);

    // adding and re-arranging...
    var fromScratch = false;
    if (name)
      fromScratch = true;

    addToLayout(fromScratch);
    if (name) {
      try {
        var coos = this.layout.arrangeFromScratch();
        setCoos(coos);
        this.resetLayout(); // incrementally...
      }
      catch(t) {
        console.log("Exception occurred during resetLayout()."); 
        if (!slow) {
          console.log("Switching to slow-mode...",t);
          this.resetLayout(name, true);
        }
      }
    }
    else {
      console.log("resetLayout incrementally, calling arrangeIncrementally..");
      var coos = this.layout.arrangeIncrementally();
    }
  };

  this.refresh = function() {
    setTimeout(function() { // waiting for line labels being added via setTimeout...
    ajoo_de.whenReady(function() {
      if (myThis.layout) {
        try {
          var t1=now();
          var coos = myThis.layout.arrangeIncrementally();
          var t2=now();
          setCoos(coos);
          var t3=now();
          console.log("arrng incrmt during refresh",(t2-t1),(t3-t2));
        }
        catch(t) {
          console.log("Exception occurred during refresh(). Switching to slow-mode...",t);
          myThis.resetLayout(null, true);
        }
      }
      ajoo_de.refresh();
      updateScrollBars(ajoo_de.getVisibleWidth(),ajoo_de.getVisibleHeight(), ajoo_de.getTotalWidth(), ajoo_de.getTotalHeight());
    });
    });
  };

  this.whenReady = function(f) {
    if (f)
      ajoo_de.whenReady(f);
  };

  this.getBoxesSet = function() {
    var retVal = {};
    for (var x in clonedBoxes)
      retVal[x] = true;
    return retVal;
  };

  this.getLinesSet = function() {
    var retVal = {};
    for (var x in clonedLines)
      retVal[x] = true;
    return retVal;
  };

};
