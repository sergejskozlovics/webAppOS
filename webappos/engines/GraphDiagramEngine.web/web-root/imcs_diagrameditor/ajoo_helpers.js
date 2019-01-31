function addCSS(src)
{
    var headElement = document.getElementsByTagName('head').item(0);
    var s = document.createElement("link");
    s.rel="stylesheet";
    if ((src.charAt(0)=='/') || (src.indexOf("http")!=-1))
      s.href=src;
    else
      s.href = SCRIPT_FOLDER+src;
    headElement.appendChild(s);
}

var scriptsAdded = 0;
var scriptsStarted = false;


function getScriptFolder(filename) {
  var scriptElements = document.getElementsByTagName('script');
  for (var i = 0; i < scriptElements.length; i++) {
    var source = scriptElements[i].src;
    if (source.indexOf(filename) > -1) {
      var location = source.substring(0, source.indexOf(filename));
      return location;
    }
  }
  return false;
}

var SCRIPT_FOLDER = getScriptFolder("ajoo_helpers.js");

function addScript(src)
{
    var headElement = document.getElementsByTagName('head').item(0);
    var s = document.createElement("script");
    if ((src.charAt(0)=='/') || (src.indexOf("http")!=-1))
      s.src = src;
    else
      s.src = SCRIPT_FOLDER+src;
    s.onload = function() {
      scriptsAdded++;
    };
    headElement.appendChild(s);
}


var reset_variable = function() {
	return undefined;
}


function whenLibsLoaded(f, number)
{
  if (typeof number=='undefined')
    number = 52;
  if (scriptsStarted) {
    // checking if loaded...
    var condition;
    if (number == 52)
      condition = ((scriptsAdded < number) || (typeof AjooEditor == 'undefined') || (typeof Konva == 'undefined') || (typeof Box == 'undefined') || (typeof ACircle == 'undefined'));
    else
      condition = (scriptsAdded < number);

    if (condition) {
      setTimeout(function() {
        whenLibsLoaded(f, number);
      }, 200);
    }
    else {
      //$( document ).ready(f);
      f();
    }
  }
  else {
    scriptsStarted = true;
    // we assume that the dojo.js script is already loaded.
    // append some styles...
    addScript("https://cdn.rawgit.com/konvajs/konva/0.9.0/konva.min.js");

//!!!!
    addScript("https://cdnjs.cloudflare.com/ajax/libs/underscore.js/1.8.3/underscore-min.js");
//    addScript("https://ajax.googleapis.com/ajax/libs/jquery/2.1.3/jquery.min.js");
//    addScript("/jquery.js");
    addScript("az_helpers.js");
    addScript("scrollbar/jquery.tinyscrollbar.min.js");
    addCSS("scrollbar/tinyscrollbar.css");

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

    whenLibsLoaded(function() {
      addScript("ajoo/Elements/Boxes/DrawingShapes/shapes1.js");
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

      addCSS("https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css");
      addCSS("https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap-theme.min.css");

      addScript("https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/js/bootstrap.min.js");
      whenLibsLoaded(f);
    }, 20);
  }
}
