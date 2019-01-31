  var diagramMoving = false;

    window.updateScrollBars = function(visibleW, visibleH, totalW, totalH) {

      window.scrollbar2.outerHTML = 
        '<div id="scrollbar2">\
            <div class="scrollbar"><div class="track"><div class="thumb"><div class="end"></div></div></div></div>\
            <div id="scrollbar2viewport" class="viewport" style="width:'+(visibleW-15)+'px">\
              <div class="overview">\
                <div id="theHScrollerWidthDiv" style="width:'+(totalW+100)*ajoo_de.getZoomFactor()+'; height:1; left:0; position: absolute;"></div>\
              </div>\
            </div>\
        </div>';

      window.scrollbar1.outerHTML =
        '<div id="scrollbar1">\
            <div class="scrollbar"><div class="track"><div class="thumb"><div class="end"></div></div></div></div>\
            <div id="scrollbar1viewport" class="viewport" style="height:'+(visibleH-15)+'px">\
              <div class="overview">\
                <div id="theVScrollerHeightDiv" style="width:1; height:'+(totalH+60)*ajoo_de.getZoomFactor()+'; position: absolute;"></div>\
              </div>\
            </div>\
        </div>';

      $(document).ready(function() {
        $('#scrollbar1').tinyscrollbar({axis:'y'});
        $('#scrollbar2').tinyscrollbar({axis:'x'});

        window.scrollbar1move = function(ev)
        {
          var f = function(){ // setTimeout, since the track of the scrollbar has to be repainted
           var h = parseInt($("#scrollbar1").find(".scrollbar").css("height"));
           var th = parseInt($("#scrollbar1").find(".scrollbar").find(".track").find(".thumb").css("height"));
           var val = parseInt($("#scrollbar1").find(".scrollbar").find(".track").find(".thumb").css("top"));
//           var scrollbar1 = $("#scrollbar1").data("plugin_tinyscrollbar");
//           scrollbar1.thumbPosition=val;
//           var val = parseInt($("#scrollbar1viewport").find(".overview").css("top"));
           ajoo_de.setRelativeY(val/(h-th));
           ajoo_de.repaint();
          };
          if (ev)
            setTimeout(f, 0);
          else
            f();
        };

        $('#scrollbar1').bind("move", window.scrollbar1move);

        window.scrollbar2move = function(ev)
        {
          var f = function(){ // setTimeout, since the track of the scrollbar has to be repainted
           var w = parseInt($("#scrollbar2").find(".scrollbar").css("width"));
           var tw = parseInt($("#scrollbar2").find(".scrollbar").find(".track").find(".thumb").css("width"));
           var val = parseInt($("#scrollbar2").find(".scrollbar").find(".track").find(".thumb").css("left"));
           ajoo_de.setRelativeX(val/(w-tw));
           ajoo_de.repaint();
          };
          if (ev)
            setTimeout(f, 0);
          else
            f();
        };
        $('#scrollbar2').bind("move", window.scrollbar2move);

        showHideScrollBars();
      });
    };

    window.toggleMoving = function() {
      window.diagramMoving = !window.diagramMoving;
      if (window.diagramMoving) {
        $("#ajoo_palette").css("opacity","0.5");
        $("#ajoo_palette").css("pointer-events","none");
        $(theMoveDiv).css("color", "blue");
        $(theMoveWrap).show();
        $(theMoveWrap).css("cursor", "move");
      }
      else {
        $("#ajoo_palette").css("opacity","1.0");
        $("#ajoo_palette").css("pointer-events","auto");
        $(theMoveDiv).css("color", "black");
        $(theMoveWrap).css("cursor", "auto");
        $(theMoveWrap).hide();
      }
    };

    window.moveScrollWheel = function(ev) {
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
         return;
      }
      var val = parseInt($("#scrollbar1").find(".scrollbar").find(".track").find(".thumb").css("top"));
      if (ev.deltaY < 0) {
        val = val-10;
        if (val < 0)
          val = 0;
      }
      else
      if (ev.deltaY > 0) {
        val = val+10;
        var h = parseInt($("#scrollbar1").find(".scrollbar").css("height"));
        var th = parseInt($("#scrollbar1").find(".scrollbar").find(".track").find(".thumb").css("height"));
        var maxVal = h-th;
        if (val > maxVal)
          val = maxVal;
      }

      $("#scrollbar1").find(".scrollbar").find(".track").find(".thumb").css("top", val+"px");

      var scrollbar1 = $("#scrollbar1").data("plugin_tinyscrollbar");
      if (scrollbar1) scrollbar1.thumbPosition=val;

      window.scrollbar1move();
    };

    window.moveZoomWheel = function(ev) {
      var x = ev.clientX - 40; // without palette
      var y = ev.clientY;

      if (ev.deltaY < 0) {
        ajoo_de.zoomIn(x, y);showZoomValue();
      }
      else {
        ajoo_de.zoomOut(x, y);showZoomValue();
      }
    };

    window.mouseMoveStart = function(ev) {
      window.diagramDragging = true;
      window.diagramDraggingLastX = ev.clientX-40;
      window.diagramDraggingLastY = ev.clientY;
    };

    window.mouseMoveEnd = function(ev) {
      window.diagramDragging = false;
    };

    window.mouseMove = function(ev) {
      if (ev.buttons==0) {
        window.diagramDragging = false;
      }
      if (window.diagramDragging) {
        var curX = ev.clientX-40;
        var curY = ev.clientY;
        var x = ajoo_de.getX()*ajoo_de.getZoomFactor();
        var y = ajoo_de.getY()*ajoo_de.getZoomFactor();
        x -= (window.diagramDraggingLastX-curX);
        y -= (window.diagramDraggingLastY-curY);
        ajoo_de.setX(x/ajoo_de.getZoomFactor());
        ajoo_de.setY(y/ajoo_de.getZoomFactor());
        ajoo_de.repaint();
        window.diagramDraggingLastX = curX;
        window.diagramDraggingLastY = curY;
        showHideScrollBars(true); // set val for both scrollbars
      }
    };

    window.showZoomValue = function() {
      var d = new Date();
      window.zoomValueTime = d.getTime();
      theZoomValue.innerHTML = Math.round(ajoo_de.getZoomFactor()*100)+"%";
      $(theZoomValue).show();
      setTimeout(function() {
        var d = new Date();
        var curTime = d.getTime();
        if (curTime - window.zoomValueTime >= 2000)
          $(theZoomValue).hide();
      },2000);

      ajoo_de.setVisibleWidth($("#diagramContentDiv").width()-40); // -paletteSize
      ajoo_de.setVisibleHeight($("#diagramContentDiv").height());
      updateScrollBars(ajoo_de.getVisibleWidth(),ajoo_de.getVisibleHeight(), ajoo_de.getTotalWidth(), ajoo_de.getTotalHeight());
      showHideScrollBars(true);
    };

    window.showHideScrollBars = function(setVal) {
      if (ajoo_de.getTotalWidth()*ajoo_de.getZoomFactor()+100 < ajoo_de.getVisibleWidth())
        $('#scrollbar2').find(".scrollbar").addClass("disable");
      else
        $('#scrollbar2').find(".scrollbar").removeClass("disable");

      if (ajoo_de.getTotalHeight()*ajoo_de.getZoomFactor()+60 < ajoo_de.getVisibleHeight())
        $('#scrollbar1').find(".scrollbar").addClass("disable");
      else
        $('#scrollbar1').find(".scrollbar").removeClass("disable");

      // update scrollbar values...
      var h = parseInt($("#scrollbar1").find(".scrollbar").css("height"));
      var th = parseInt($("#scrollbar1").find(".scrollbar").find(".track").find(".thumb").css("height"));
      var val1 = (h-th)*ajoo_de.getRelativeY();
      $("#scrollbar1").find(".scrollbar").find(".track").find(".thumb").css("top", val1+"px");
      if (setVal) {
        var scrollbar1 = $("#scrollbar1").data("plugin_tinyscrollbar");
        if (scrollbar1) scrollbar1.thumbPosition=val1;
      }

      var w = parseInt($("#scrollbar2").find(".scrollbar").css("width"));
      var tw = parseInt($("#scrollbar2").find(".scrollbar").find(".track").find(".thumb").css("width"));
      var val2 = (w-tw)*ajoo_de.getRelativeX();
      $("#scrollbar2").find(".scrollbar").find(".track").find(".thumb").css("left", val2+"px");
      if (setVal) {
        var scrollbar2 = $("#scrollbar2").data("plugin_tinyscrollbar");
        if (scrollbar2) scrollbar2.thumbPosition=val2;
      }
    };


  function ShowPleaseWait(msg) {
	//if ((thePleaseWaitDivWrap.style.display == "block") && (thePleaseWaitDiv.innerHTML==msg))
		//return;
    thePleaseWaitDiv.innerHTML = msg;
    thePleaseWaitDivWrap.style.display = "block";
	if (!msg || (msg==""))
		thePleaseWaitDiv.style.display = "none";
	else
		thePleaseWaitDiv.style.display = "block";
    thePleaseWaitDiv.style.left = (thePleaseWaitDivWrap.clientWidth-thePleaseWaitDiv.clientWidth)/2;
    thePleaseWaitDiv.style.top = (thePleaseWaitDivWrap.clientHeight-thePleaseWaitDiv.clientHeight)/2;
	
	thePleaseWaitDiv.offsetHeight;
	thePleaseWaitDivWrap.offsetHeight;
	//$(window).trigger('resize');
  }

  function HidePleaseWait() {
    thePleaseWaitDivWrap.style.display = "none";
    thePleaseWaitDiv.style.display = "none";
  }


///// HELPERS /////

function sortCompartments(arr)
{
  arr.sort(function(a,b) {
    return a.style.getOffsetY() - b.style.getOffsetY();
  });

  return arr;
}

function collectSubCompartments(arr) {
// clones...
  if (typeof arr == "undefined")
    return [];

  var retVal = [];
  for (var i=0; i<arr.length; i++) {
    retVal.push(arr[i]);
  }

// collecting sub-compartments
  var j=0;
  while (j<retVal.length) {

    if (retVal[j].isGroup == "true") {
      // adding children to the end of the list
      var children = retVal[j].subCompartment; 

      retVal.splice(j, 1); // remove the current element...

      // adding new elements...
      for (var k=0; k<children.length; k++)
        retVal.push(children[k]);
    }
    else {
      if (retVal[j].input) {
        retVal[j] = webappos.js_util.clone_object_properties(retVal[j]);
        j++;
      }
      else
        retVal.splice(j, 1); // remove the current element...
    }
  }

  return retVal;
}


function cloneElement(_el)
{
    // INITIAL CLONING
    var el = webappos.js_util.clone_object_properties(_el);	
    el.className = _el.getClassName();
	if (!el.className)
		return null;
    if (el.className == 'Edge') {
      el.start = [{reference:_el.start[0].reference, className:_el.start[0].getClassName()}];
      el.end = [{reference:_el.end[0].reference,className:_el.end[0].getClassName()}];
    }

    el.hash = "";

    // SPECIFIC CLONING...
    if (el.className == 'Node') {
      // converting style and location strings to objects (decoding AZ)...
      el.compartment = collectSubCompartments(_el.compartment);
      if (!el.style || (el.style=="#")) {
        var defaultStyle = _el.elemStyle;

        if (defaultStyle && (defaultStyle.length>0)) {
          var s = new NodeStyle();
          s.setFillColor( intToColor2(parseInt(defaultStyle[0].bkgColor)) ); // !!! not RGB, but BGR
          s.setShapeAZ( defaultStyle.shapeCode );
          s.setDashLength( defaultStyle.dashLength );
          s.setDashBreakLength( defaultStyle.breakLength );
          s.setLineColorAZ( defaultStyle.lineColor );
          s.setLineWidth( defaultStyle.lineWidth );
          el.style = s.toString();
        }
      }
      el.style = new NodeStyle(el.style);
      el.hash += el.style.toString();

      if (!el.location && newBoxLocation)  { // setting the location for the newly created box
        tda.model[el.reference].setLocation(newBoxLocation);
        el.location = newBoxLocation;
        newBoxLocation = null;
      }
      el.location = new NodeLocation(el.location);

      if (typeof el.compartment != 'undefined') {

        for (var j=0; j<el.compartment.length; j++) {
          if (!el.compartment[j].style || (el.compartment[j].style=="#")) {

            var defaultStyle = _el.compartment[j].compartStyle;
            if (defaultStyle && (defaultStyle.length>0)) {
              var s = new CompartmentStyle();
              s.setFontColorAZ(defaultStyle[0].fontColor);
              s.setFontFamily(defaultStyle[0].fontTypeFace);
              s.setFontWeightAZ(defaultStyle[0].fontStyle);
              s.setFontSize(parseInt(defaultStyle[0].fontSize)+2);
              s.setIsVisible(defaultStyle[0].isVisible && (defaultStyle[0].isVisible != "0"));
              s.setAlignmentAZ(defaultStyle[0].alignment);
              s.setPlacementAZ(defaultStyle[0].adjustment);
              s.setLineWidth(defaultStyle[0].lineWidth);
              el.compartment[j].style = s.toString();
            }
          }

          // converting style string to an object (decoding AZ)...
          el.compartment[j].style = new CompartmentStyle(el.compartment[j].style);
          el.hash += "#"+el.compartment[j].reference+"#"+el.compartment[j].style.toString()+"#"+el.compartment[j].input+"#"+el.compartment[j].value;


        }

/*        el.compartment = el.compartment.sort(function(a,b) {
          return a.style.getOffsetY() - b.style.getOffsetY();
        });*/


      }


/*      if (el.location.getX()+el.location.getWidth() > maxX)
        maxX=el.location.getX()+el.location.getWidth();
      if (el.location.getY()+el.location.getHeight() > maxY)
        maxY=el.location.getY()+el.location.getHeight();*/

    }
    else
    if (el.className == 'Edge') {

      el.compartment = collectSubCompartments(_el.compartment);

      // converting style and location strings to objects (decoding AZ)...

      if (!el.location && newLineLocation)  { // setting the location for the newly created line
        tda.model[el.reference].setLocation(newLineLocation);
        el.location = newLineLocation;
        newLineLocation = null;

        window.newLineRef = el.reference;
      }


      if (!el.style || (el.style=="#")) {
        var defaultStyle = _el.elemStyle;

        if (defaultStyle && (defaultStyle.length>0)) {
          var s = new EdgeStyle();
          s.setFillColor( intToColor2(parseInt(defaultStyle[0].bkgColor)) ); // !!! not RGB, but BGR
          s.setShapeAZ( defaultStyle[0].shapeCode );
          s.setDashLength( defaultStyle[0].dashLength );
          s.setDashBreakLength( defaultStyle[0].breakLength );
          s.setLineColorAZ( defaultStyle[0].lineColor );
          s.setLineWidth( defaultStyle[0].lineWidth );

          
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
/*      for (var j in arr) {
        if (arr[j].x > maxX)
          maxX = arr[j].x;
        if (arr[j].y > maxY)
          maxY = arr[j].y;
      };*/

      if (typeof el.compartment != 'undefined') {

        for (var j=0; j<el.compartment.length; j++) {
//console.log("STYLE COMPR",el.compartment[j].reference,el.compartment[j].style);
          if (!el.compartment[j].style || (el.compartment[j].style=="#")) {

            var defaultStyle = el.compartment[j].compartStyle;
/*            var ccc;
            var defaultStyle;
            for (var i1=0; i1<_el.compartment.length; i1++) {
              if (_el.compartment[i1].reference == el.compartment[j].reference) {
                defaultStyle = _el.compartment[i1].compartStyle;
                ccc = _el.compartment[i1];
              }
              if (defaultStyle)
                break;
              // check sub-compartments...
              if (_el.compartment[i1].subCompartment) {
                 for (var i2=0; i2<_el.compartment[i1].subCompartment.length; i2++) {
                   if (_el.compartment[i1].subCompartment[i2].reference == el.compartment[j].reference) {
                     defaultStyle = _el.compartment[i1].subCompartment[i2].compartStyle;
                     ccc = _el.compartment[i1].subCompartment[i2];
                     break;
                   }
                 }
                 if (defaultStyle)
                   break;
              }
            }*/

            if (defaultStyle && (defaultStyle.length>0)) {
//console.log("DEFAULT STYLE for "+el.compartment[j].reference+" is "+defaultStyle[0].reference+" ADJ="+defaultStyle[0].adjustment,defaultStyle,el.compartment[j]);
              var s = new CompartmentStyle();
              s.setFontColorAZ(defaultStyle[0].fontColor);
              s.setFontFamily(defaultStyle[0].fontTypeFace);
              s.setFontWeightAZ(defaultStyle[0].fontStyle);
              s.setFontSize(parseInt(defaultStyle[0].fontSize)+2);
              s.setIsVisible(defaultStyle[0].isVisible && (defaultStyle[0].isVisible != "0"));
//              console.log("isVisible ="+s.getIsVisible());
              s.setAlignmentAZ(defaultStyle[0].alignment);
              s.setPlacementAZ(defaultStyle[0].adjustment);
              s.setLineWidth(defaultStyle[0].lineWidth);
              el.compartment[j].style = s.toString();
            }
          }

          // converting style string to an object (decoding AZ)...
          el.compartment[j].style = new CompartmentStyle(el.compartment[j].style);
          el.hash += "#"+el.compartment[j].reference+"#"+el.compartment[j].style.toString()+"#"+el.compartment[j].input+"#"+el.compartment[j].value;
        }

/*        el.compartment = el.compartment.sort(function(a,b) {
          return a.style.getOffsetY() - b.style.getOffsetY();
        });*/


      }

    }

    return el;
}
