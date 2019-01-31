colorToInt = function(s) {
  return parseInt(s.substring(1), 16);
};

intToColor = function(num) {

    var b = num & 0xFF,  
        g = (num & 0xFF00) >>> 8,
        r = (num & 0xFF0000) >>> 16;

    var sr = r.toString(16);
    while (sr.length < 2)
        sr = "0" + sr;
    var sg = g.toString(16);
    while (sg.length < 2)
        sg = "0" + sg;
    var sb = b.toString(16);
    while (sb.length < 2)
        sb = "0" + sb;

    return "#" + sb + sg + sr;
};

intToColor2 = function(num) {

    var r = num & 0xFF,
        g = (num & 0xFF00) >>> 8,
        b = (num & 0xFF0000) >>> 16;

  var sr = r.toString(16);
  while (sr.length<2)
   sr = "0"+sr;
  var sg = g.toString(16);
  while (sg.length<2)
   sg = "0"+sg;
  var sb = b.toString(16);
  while (sb.length<2)
   sb = "0"+sb;

  return "#" + sb+sg+sr;
};

intToRGBA = function(num) {
    num >>>= 0;
    var b = num & 0xFF,
        g = (num & 0xFF00) >>> 8,
        r = (num & 0xFF0000) >>> 16,
        a = ((num & 0xFF000000) >>> 24) / 255;
    return "rgba(" + [r, g, b, a].join(",") + ")";
};


function getTextWidth(s, fontInfo) {
  var canvas = document.createElement("canvas");
  var ctx = canvas.getContext("2d");
  ctx.font = fontInfo;//"20pt Arial";  // This can be set programmaticly from the element's font-style if desired
  var textWidth = ctx.measureText(s).width;
  return textWidth;
};


function getFontHeight(fontFamily, fontSize) {
  var body = document.getElementsByTagName("body")[0];
  var dummy = document.createElement("div");
  var dummyText = document.createTextNode("M");
  dummy.appendChild(dummyText);
  dummy.setAttribute("style", "font-family: " + fontFamily + "; font-size: " + fontSize + ";");
  body.appendChild(dummy);
  var result = dummy.offsetHeight;//dummy.offsetHeight;
  body.removeChild(dummy);
  return result;
};



var NodeStyle = function( /*String*/ style) {
    if (!style)
        style = "";

    /*private*/
    this.arr = style.split("[").join("").split("]").join("").split(";");
    /*private*/
    this.getAttr = function(i, defaultValue) {
        var value = defaultValue;
        if (i < this.arr.length) {
            value = this.arr[i];
            if (!value)
                value = defaultValue;
        }
        return value;
    };
    /*private*/
    this.setAttr = function(i, value) {
        while (i >= this.arr.length) {
            this.arr.push("");
        }
        this.arr[i] = value;
    };



    this.getFillColor = function() {
        return intToColor(parseInt(this.getAttr(6, 15792383)));
    };

    this.setFillColor = function(c) {
        if ((c.length > 0) && (c.charAt(0) == '#')) {
            c = c.substr(1);
        }
        return this.setAttr(6, parseInt(c, 16));
    };

    this.toString = function() {
        return this.arr.join(";");
    };


/* AZ shape codes:
  box_Rectangle = 1, 
  box_RoundRectangle = 2, 
  box_Parallelogram = 3, 
  box_Arrow = 4, 
  box_Ellipse = 5, 
  box_Hexagon = 6, 
  box_Trapeze = 7, 
  box_DownwardTrapeze = 8, 
  box_Diamond = 9, 
  box_Triangle = 10, 
  box_Note = 11, 
  box_InArrow = 12, 
  box_OutArrow = 13, 
  box_Octagon = 14, 
  box_LittleMan = 15, 
  box_BigArrow = 18, 
  box_Activity_State = 17, 
  box_Package = 16, 
  box_BlackLine = 19, 
  box_Component = 20, 
  box_VertCylinder = 21, 
  box_HorzCylinder = 22, 
  box_VertBlackLine = 23, 
  box_SandGlass = 24
*/
    /*private*/
    this.get_shape_mapping = function(shape_in) {

	var shape_mapping = {
						2/*box_RoundRectangle*/: "RoundRectangle",
						1/*box_Rectangle*/: "Rectangle",
						16/*box_Package*/: "Package",
						11/*box_Note*/: "Note",
						5/*box_Ellipse*/: "Circle", 
						3/*le_PureArrow*/: "Triangle",

						19/*box_BlackLine*/: "HorizontalLine",
						23/*box_VertBlackLine*/: "VerticalLine",
						//box_BlackLine: "Triangle",						
					};

	var shape_out = shape_mapping[shape_in];
	if (shape_out)
		return shape_out;
	else
		return "Rectangle";
    };

    /*private*/
    this.get_shape_mapping_inv = function(shape_in) {

	var shape_mapping = {
						RoundRectangle: 2,
						Rectangle: 1,
						Package: 16,
						Note: 11,
						Circle: 5, 
						Triangle: 3,

						HorizontalLine: 19,
						VerticalLine: 23,
					};

	var shape_out = shape_mapping[shape_in];
	if (shape_out)
		return shape_out;
	else
		return 1;
    };

    this.getShape = function() {
       return this.get_shape_mapping(parseInt(this.getAttr(0, 0)));
    };

    this.setShape = function(s) {
       this.setAttr(0, this.get_shape_mapping_inv(s));
    };

    this.setShapeAZ = function(v) {
       this.setAttr(0, v);
    };

    this.getDashLength = function() {
       return parseInt(this.getAttr(3, 0));
    };

    this.setDashLength = function(n) {
       this.setAttr(3, n+"");
    };

    this.getDashBreakLength = function() {
       return parseInt(this.getAttr(4, 0));
    };

    this.setDashBreakLength = function(n) {
       this.setAttr(4, n+"");
    };

    this.getLineColor = function() {
        return intToColor(parseInt(this.getAttr(7, 0)));
    };

    this.setLineColor = function(c) {
        if ((c.length > 0) && (c.charAt(0) == '#')) {
            c = c.substr(1);
        }
        return this.setAttr(7, parseInt(c, 16));
    };

    this.setLineColorAZ = function(c) {
        return this.setAttr(7, parseInt(c+""));
    };

    this.getLineWidth = function() {
        try {
          return parseInt(this.getAttr(2, 0));
        }
        catch(t) {
          return 1;
        }
    };

    this.setLineWidth = function(w) {
        return this.setAttr(2, w+"");
    };

/*
NOT MIGRATED:
typedef enum GEAdornmentCode
{
  ad_UnderLine = 1, 
  ad_BlackTriangle = 2, 
  ad_UpperLine = 3, 
  ad_DirectionArrow = 4, 
  ad_ReverseBlackTriangle = 5, 
  ad_ReverseDirectionArrow = 6, 
  ad_UnderLineM = 7, 
  ad_UpperLineM = 8, 
  ad_UnderLineB = 9, 
  ad_UpperLineB = 10
} GEAdornmentCode;

NOT MIGRATED:
typedef enum GEShapeStyle
{
  bs_Noborder = 1, 
  bs_Shadow = 2, 
  bs_Style3D = 4, 
  bs_Multiple = 8, 
  bs_Nobackground = 32, 
  ls_NotLinePen = 64
} GEShapeStyle;

*/

};


var NodeLocation = function( /*String*/ location) {
    if (!location)
        location = "";

    /*private*/
    this.arr = location.split(";");
    /*private*/
    this.getAttr = function(i, defaultValue) {
        var value = defaultValue;
        if (i < this.arr.length) {
            value = this.arr[i];
            if (!value)
                value = defaultValue;
        }
        return value;
    };
    /*private*/
    this.setAttr = function(i, value) {
        while (i >= this.arr.length) {
            this.arr.push("");
        }
        this.arr[i] = value;
    };

    this.getWidth = function() {
	try {
		return parseInt(this.getAttr(0, 0));
	}
	catch(t) { return 160; }
    };

    this.setWidth = function(w) {
        this.setAttr(0, w);
    };

    this.getHeight = function() {
        try {
	        return parseInt(this.getAttr(1, 0));
	}
	catch(t) { return 100; }
    };

    this.setHeight = function(h) {
        return this.setAttr(1, h);
    };

    this.getX = function() {
	try {
	        return parseInt(this.getAttr(2, 0));
	}
	catch(t) { return 0; }
    };

    this.setX = function(x) {
        return this.setAttr(2, x);
    };

    this.getY = function() {
	try {
	        return parseInt(this.getAttr(3, 0));
	}
	catch(t) { return 0; }
    };

    this.setY = function(y) {
        return this.setAttr(3, y);
    };

    this.toString = function() {
        return this.arr.join(";");
    };
};

var CompartmentStyle = function( /*String*/ style) {
    if (!style)
        style = "";

    /*private*/
    this.arr = style.split("[").join("").split("]").join("").split(";");
    /*private*/
    this.getAttr = function(i, defaultValue) {
        var value = defaultValue;
        if (i < this.arr.length) {
            value = this.arr[i];
            if (!value)
                value = defaultValue;
        }
        return value;
    };
    /*private*/
    this.setAttr = function(i, value) {
        while (i >= this.arr.length) {
            this.arr.push("");
        }
        this.arr[i] = value;
    };

    /*private*/
    this.getTextWidth = function(s) {
	var canvas = document.createElement("canvas");
	var ctx = canvas.getContext("2d");
	var fontInfo = this.getFontSizeInPt()+"pt "+this.getFontFamily();
	ctx.font = fontInfo;//"20pt Arial";  // This can be set programmaticly from the element's font-style if desired
	var textWidth = ctx.measureText(s).width;
	return textWidth;
    }


    this.getFontColor = function() {
        return intToColor(parseInt(this.getAttr(24, 0)));
    };


    this.setFontColor = function(c) {
        if ((c.length > 0) && (c.charAt(0) == '#')) {
            c = c.substr(1);
        }
        return this.setAttr(24, parseInt(c, 16));
    };

    this.setFontColorAZ = function(i) {
        return this.setAttr(24, i);
    };


    this.getFontFamily = function() {
        return this.getAttr(25, "Arial");
    };

    this.setFontFamily = function(newFamily) {
        return this.setAttr(25, newFamily);
    };

    this.getFontWeight = function() {
        var weight = "normal";
        var v = this.getAttr(23, 0);
        if (v == 0)
        ;
        else
        if (v == 1)
            weight = "bold";
        else
            weight = "italic";
        return weight;
    };

    this.setFontWeight = function(newWeight) {
        if (newWeight == "bold")
            this.setAttr(23, 1);
        else
        if (newWeight == "italic")
            this.setAttr(23, 2);
        else
            this.setAttr(23, 0); // normal
    };

    this.setFontWeightAZ = function(i) {
        this.setAttr(23, i);
    };

    this.getFontSizeInPt = function() {
        return Math.round(Math.abs(parseInt(this.getAttr(22, "11")) * 0.6));
    };

    this.setFontSizeInPt = function(newSize) {
        this.setAttr(22, Math.round(newSize/0.6) + "");
    };

    this.getFontSize = function() {
        return Math.abs(parseInt(this.getAttr(22, "11")));
    };

    this.setFontSize = function(newSize) {
        this.setAttr(22, newSize + "");
    };

    this.getOffsetX = function() {
	try {
	        return parseInt(this.getAttr(14, 0));
	}
	catch(t) { return 0; }
    };

    this.setOffsetX = function(x) {
        this.setAttr(14, x);
    };

    this.getOffsetY = function() {
        try {
		return parseInt(this.getAttr(15, 0));
	}
	catch(t) { return 0; }
    };

    this.setOffsetY = function(y) {
        this.setAttr(15, y);
    };

    this.getWidth = function() {
	try {
	        return parseInt(this.getAttr(12, 0));
	}
	catch(t) {
		return 0;
	}
    };

    this.setWidth = function(w) {
        this.setAttr(12, w);
    };

    this.getIsVisible = function() {
	try {
	        var visible = (parseInt(this.getAttr(16, 0))>0)?true:false;
	        if (this.arr.length<17)
	           visible = true;
	        return visible;
	}
	catch(t) {
		return false;
	}
    };

    this.setIsVisible = function(w) {
        if (w)
          this.setAttr(16, "1");
        else
          this.setAttr(16, "0");
    };


    this.getCompartmentName = function() {
        return this.getAttr(0, "");
    };

    this.setCompartmentName = function(newName) {
        return this.setAttr(0, newName);
    };

/*
typedef enum GETextAlignment
{
  dt_Left = 0, 
  dt_Right = 2, 
  dt_Center = 1, 
  dt_After = 3
} GETextAlignment;
*/

    this.getAlignment = function() {
        var map = {
           0: "left",
           1: "center",
           2: "right",
        };

	try {
	   var retVal = map[parseInt(this.getAttr(1, 0))];
	   if (retVal)
	     return retVal;
	}
	catch(t) {
	}
	return "left";
    };

    this.setAlignment = function(s) {
        var map = {
           left: 0,
           center: 1,
           right: 2,
        };

        var val = map[s];
        if (!val)
          val = 0;

        this.setAttr(1, val);
    };

    this.setAlignmentAZ = function(val) {
        this.setAttr(1, val);
    };

/*
typedef enum GELineCompartAdjustment
{
  lc_Start = 1, 
  lc_End = 2, 
  lc_Left = 4, 
  lc_Right = 8, 
  lc_Middle = 16, 
  lc_Any = 0xFFFFFFFF
} GELineCompartAdjustment;
*/

// placement is only for edges
    this.getPlacement = function() {
        var map = {
           5: "start-left",
           9: "start-right",
           6: "end-left",
           10: "end-right",
           20: "middle-left",
           24: "middle-right",
        };

	try {
	   var retVal = map[parseInt(this.getAttr(2, 0))];
	   if (retVal)
	     return retVal;
	}
	catch(t) {
	}
console.log("PLACEMENT DEFAULT ",this.getAttr(2, 0));
	return "end-left";
    };

    this.setPlacement = function(s) {
        var map = {
           "start-left": 5,
           "start-right": 9,
           "end-left": 6,
           "end-right": 10,
           "middle-left": 20,
           "middle-right": 24,
        };

        var val = map[s];
        if (!val)
          val = 6;

        this.setAttr(2, val);
    };

    this.setPlacementAZ = function(val) {
        this.setAttr(2, val);
    };


    this.getLineWidth = function() {
        try {
          return parseInt(this.getAttr(6, 0));
        }
        catch(t) {
          return 1;
        }
    };

    this.setLineWidth = function(w) {
        return this.setAttr(6, w+"");
    };

    this.toString = function() {
        return this.arr.join(";");
    };
};

var EdgeLocation = function(locationStr) {
    if (!locationStr)
	locationStr = "0";
    var pointsStr = locationStr.split("\\");
    this.n = parseInt(pointsStr[0]);
    this.pointsXY = [];
    this.pointsArray = [];

    for (var j = 0; j < this.n; j++) {
        var xyStr = pointsStr[j + 1].split(",");
        var xy = {
            x: parseInt(xyStr[0]),
            y: parseInt(xyStr[1])
        };
        this.pointsXY.push(xy);
        this.pointsArray.push(xy.x);
        this.pointsArray.push(xy.y);
    }

    this.getPointsXY = function() {
        var retVal = [];
        for (var i=0; i<this.pointsXY.length; i++)
           retVal.push({x:this.pointsXY[i].x, y:this.pointsXY[i].y});
        return retVal;
    };

    this.setPointsXY = function(val) {
      if (!val)
        return;
      this.pointsXY = [];
      for (var i=0; i<val.length; i++)
        this.pointsXY.push({x:val[i].x, y:val[i].y});

      this.n = val.length;

      this.pointsArray = [];
      var sgn=0;
      for (var i=0; i<this.pointsXY.length; i++) {
        this.pointsArray.push(this.pointsXY[i].x);
        this.pointsArray.push(this.pointsXY[i].y);
        if (i>=1) {
          var sgn1;
          if (val[i].x==val[i-1].x)
            sgn1 = 1;
          else
          if (val[i].y==val[i-1].y)
            sgn1 = -1;
          else {
            console.log("ERROR in points", val);
            webappos.js_util.print_stack_trace();
          }
          if ((sgn==0)||(sgn==-sgn1)) {
            sgn = sgn1;
          }
          else {
            console.log("ERROR in points alternation", val);
          }
        }
      }
    };

    this.setPoints = function(val) {
      this.n = Math.floor(val.length/2);
      this.pointsArray = val.slice();
      this.pointsXY = [];
      for (var i=0; i<this.n; i++) {
        this.pointsXY.push({x:Math.round(val[i*2]),y:Math.round(val[i*2+1])});
      }
    };

    this.getPointsArray = function() {
        return this.pointsArray.slice();
    };

    this.toString = function() {
        var s = this.n+"";
        for (var j=0; j<this.n; j++)
           s+="\\"+this.pointsXY[j].x+","+this.pointsXY[j].y;
        return s;
    };

};



var EdgeStyle = function( /*String*/ style) {
    if (!style)
        style = "";

    /*private*/
    this.arr = style.split("[").join("").split("]").join("").split(";");
    /*private*/
    this.getAttr = function(i, defaultValue) {
        var value = defaultValue;
        if (i < this.arr.length) {
            value = this.arr[i];
            if (!value)
                value = defaultValue;
        }
        return value;
    };
    /*private*/
    this.setAttr = function(i, value) {
        while (i >= this.arr.length) {
            this.arr.push("");
        }
        this.arr[i] = value;
    };



    this.getFillColor = function() {
        return intToColor(parseInt(this.getAttr(6, 15792383)));
    };

    this.setFillColor = function(c) {
        if ((c.length > 0) && (c.charAt(0) == '#')) {
            c = c.substr(1);
        }
        return this.setAttr(6, parseInt(c, 16));
    };

    this.toString = function() {
        return this.arr.join(";");
    };


/* AZ shape codes:
  box_Rectangle = 1, 
  box_RoundRectangle = 2, 
  box_Parallelogram = 3, 
  box_Arrow = 4, 
  box_Ellipse = 5, 
  box_Hexagon = 6, 
  box_Trapeze = 7, 
  box_DownwardTrapeze = 8, 
  box_Diamond = 9, 
  box_Triangle = 10, 
  box_Note = 11, 
  box_InArrow = 12, 
  box_OutArrow = 13, 
  box_Octagon = 14, 
  box_LittleMan = 15, 
  box_BigArrow = 18, 
  box_Activity_State = 17, 
  box_Package = 16, 
  box_BlackLine = 19, 
  box_Component = 20, 
  box_VertCylinder = 21, 
  box_HorzCylinder = 22, 
  box_VertBlackLine = 23, 
  box_SandGlass = 24
*/
    /*private*/
    this.get_shape_mapping = function(shape_in) {

	var shape_mapping = {
						2/*box_RoundRectangle*/: "RoundRectangle",
						1/*box_Rectangle*/: "Rectangle",
						16/*box_Package*/: "Package",
						11/*box_Note*/: "Note",
						5/*box_Ellipse*/: "Circle", 
						3/*le_PureArrow*/: "Triangle",

						19/*box_BlackLine*/: "HorizontalLine",
						23/*box_VertBlackLine*/: "VerticalLine",
						//box_BlackLine: "Triangle",						
					};

	var shape_out = shape_mapping[shape_in];
	if (shape_out)
		return shape_out;
	else
		return "Rectangle";
    };

    /*private*/
    this.get_shape_mapping_inv = function(shape_in) {

	var shape_mapping = {
						RoundRectangle: 2,
						Rectangle: 1,
						Package: 16,
						Note: 11,
						Circle: 5, 
						Triangle: 3,

						HorizontalLine: 19,
						VerticalLine: 23,
					};

	var shape_out = shape_mapping[shape_in];
	if (shape_out)
		return shape_out;
	else
		return 1;
    };

    this.getShape = function() {
       return this.get_shape_mapping(parseInt(this.getAttr(0, 0)));
    };

    this.setShape = function(s) {
       this.setAttr(0, this.get_shape_mapping_inv(s));
    };

    this.setShapeAZ = function(val) {
       this.setAttr(0, val);
    };

    this.getDashLength = function() {
       return parseInt(this.getAttr(3, 0));
    };

    this.setDashLength = function(n) {
       this.setAttr(3, n+"");
    };

    this.getDashBreakLength = function() {
       return parseInt(this.getAttr(4, 0));
    };

    this.setDashBreakLength = function(n) {
       this.setAttr(4, n+"");
    };

    this.getLineColor = function() {
        return intToColor(parseInt(this.getAttr(7, 0)));
    };

    this.setLineColor = function(c) {
        if ((c.length > 0) && (c.charAt(0) == '#')) {
            c = c.substr(1);
        }
        return this.setAttr(7, parseInt(c, 16));
    };

    this.setLineColorAZ = function(v) {
        return this.setAttr(7, v);
    };

    this.getLineWidth = function() {
        try {
          return parseInt(this.getAttr(2, 0));
        }
        catch(t) {
          return 1;
        }
    };

    this.setLineWidth = function(w) {
        return this.setAttr(2, w+"");
    };

    /*private*/
    this.get_line_end_mapping = function(shape_in) {

	var shape_mapping = {
							1/*le_None*/: "None",
							2/*le_Arrow*/: "Triangle",					
							10/*le_Diamond*/: "Diamond",
							11/*le_Triangle*/: "Triangle",
							3/*le_PureArrow*/: "Arrow",
					};

	var shape_out = shape_mapping[shape_in];
	if (shape_out)
		return shape_out;
	else
		return "Rectangle";
    };

    /*private*/
    this.get_line_end_mapping_inv = function(shape_in) {

	var shape_mapping = {
							None:1,
							Triangle:2,
							Diamond:10,
							Triangle:11,
							Arrow:3,
					};

	var shape_out = shape_mapping[shape_in];
	if (shape_out)
		return shape_out;
	else
		return 1;
    };



    this.getStartLabelStyle = function() {
        if (!this.startLabelStyle) {
	        var retVal = new NodeStyle(this.arr.slice(13,21).join(";"));
	        retVal.get_shape_mapping = this.get_line_end_mapping;
        	retVal.get_shape_mapping_inv = this.get_line_end_mapping_inv;
	        this.startLabelStyle=retVal;	
	}
	return this.startLabelStyle;
    };

    this.getEndLabelStyle = function() {
        if (!this.endLabelStyle) {
	        var retVal = new NodeStyle(this.arr.slice(21,29).join(";"));
        	retVal.get_shape_mapping = this.get_line_end_mapping;
	        retVal.get_shape_mapping_inv = this.get_line_end_mapping_inv;
        	this.endLabelStyle=retVal;
	}
	return this.endLabelStyle;
    };

    this.getMiddleLabelStyle = function() {
        if (!this.middleLabelStyle) {
	        var retVal = new NodeStyle(this.arr.slice(29,37).join(";"));
        	retVal.get_shape_mapping = this.get_line_end_mapping;
	        retVal.get_shape_mapping_inv = this.get_line_end_mapping_inv;
        	this.middleLabelStyle=retVal;
	}
	return this.middleLabelStyle;
    };

    this.setStartLabelStyleAZ = function(shapeCode, lineWidth, dashLength, breakLength, bkgColor, lineColor) {
       var off = 13;
       this.setAttr(off+0, shapeCode);
       this.setAttr(off+1, 0); //shapeStyle
       this.setAttr(off+2, lineWidth);
       this.setAttr(off+3, dashLength);
       this.setAttr(off+4, breakLength);
       this.setAttr(off+5, 0); // adornment
       this.setAttr(off+6, bkgColor); 
       this.setAttr(off+7, lineColor); 
    };

    this.setEndLabelStyleAZ = function(shapeCode, lineWidth, dashLength, breakLength, bkgColor, lineColor) {
       var off = 21;
       this.setAttr(off+0, shapeCode);
       this.setAttr(off+1, 0); //shapeStyle
       this.setAttr(off+2, lineWidth);
       this.setAttr(off+3, dashLength);
       this.setAttr(off+4, breakLength);
       this.setAttr(off+5, 0); // adornment
       this.setAttr(off+6, bkgColor); 
       this.setAttr(off+7, lineColor); 
    };

    this.setMiddleLabelStyleAZ = function(shapeCode, lineWidth, dashLength, breakLength, bkgColor, lineColor) {
       var off = 29;
       this.setAttr(off+0, shapeCode);
       this.setAttr(off+1, 0); //shapeStyle
       this.setAttr(off+2, lineWidth);
       this.setAttr(off+3, dashLength);
       this.setAttr(off+4, breakLength);
       this.setAttr(off+5, 0); // adornment
       this.setAttr(off+6, bkgColor); 
       this.setAttr(off+7, lineColor); 
    };

};

/* TODO: EdgeStyle

function get_line_end_shape(shape_in) {

	var line_end_shapes = {
							"le_None": "None",
							"le_Arrow": "Triangle",					
							"le_Diamond": "Diamond",
							"le_Triangle": "Triangle",
							"le_PureArrow": "Arrow",
						};

	var shape_out = line_end_shapes[shape_in];
	if (typeof shape_out != undefined)
		return shape_out;
	else
		return "None";
}

*/
