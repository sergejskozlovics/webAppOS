package org.webappos.adapters.webcalls.lua;

import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.ZeroArgFunction;

import lv.lumii.tda.raapi.RAAPI;

import org.luaj.vm2.lib.OneArgFunction;
import org.luaj.vm2.lib.TwoArgFunction;

public class Module_lua_graphDiagram extends TwoArgFunction {
	
	public static Module_lua_graphDiagram LIB = null;
	
	private RAAPI raapi;
	
    public Module_lua_graphDiagram(RAAPI _raapi) {
        LIB = this;
        raapi = _raapi;
    }

	@Override
	synchronized public LuaValue call(LuaValue modName, LuaValue env) {
		LuaTable module = new LuaTable(0,30); // I think "new LuaTable()" instead of "(0, 30)" is OK
        module.set("GetNodeStyle", new GetNodeStyle());
        module.set("GetEdgeStyle", new GetEdgeStyle());
        module.set("GetPortStyle", new GetPortStyle());
        module.set("GetCompartmentStyle", new GetCompartmentStyle());
        module.set("IsOpenDiagram", new IsOpenDiagram());

        
        return module;		
	}


	class GetNodeStyle extends OneArgFunction {		

		@Override
		synchronized public LuaValue call(LuaValue arg0) {
			String s = arg0.tojstring();
			if (s==null)
				return LuaValue.valueOf("");
			s = s.replaceAll("[", "");
			s = s.replaceAll("]", "");
			StringBuffer sb = new StringBuffer();			
			String[] arr = s.split(";");
			
			sb.append("{"); int i=0;
			sb.append("shapeCode=\'"); sb.append(arr[i++]); sb.append("\',");
			sb.append("shapeStyle=\'"); sb.append(arr[i++]); sb.append("\',");
			sb.append("lineWidth=\'"); sb.append(arr[i++]); sb.append("\',");
			sb.append("dashLength=\'"); sb.append(arr[i++]); sb.append("\',");
			sb.append("breakLength=\'"); sb.append(arr[i++]); sb.append("\',");
			sb.append("alignment=\'"); sb.append(arr[i++]); sb.append("\',");
			sb.append("bkgColor=\'"); sb.append(arr[i++]); sb.append("\',");
			sb.append("lineColor=\'"); sb.append(arr[i++]); sb.append("\'");
			
			if ((arr.length==9)||(arr.length>8+5)) {
				sb.append(",");
				sb.append("dynamicTooltip=\'"); sb.append(arr[i++]);sb.append("\'"); 
			}
			if (arr.length>=8+5) {
				sb.append(",");
				sb.append("picture=\'"); sb.append(arr[i++]); sb.append("\',");
				sb.append("picStyle=\'"); sb.append(arr[i++]); sb.append("\',");
				sb.append("picPos=\'"); sb.append(arr[i++]); sb.append("\',");
				sb.append("width=\'"); sb.append(arr[i++]); sb.append("\',");
				sb.append("height=\'"); sb.append(arr[i++]);
			}
			
			sb.append("}");			
			return LuaValue.valueOf(sb.toString());
		}

	}

	class GetEdgeStyle extends OneArgFunction {		

		@Override
		synchronized public LuaValue call(LuaValue arg0) {
			String s = arg0.tojstring();
			if (s==null)
				return LuaValue.valueOf("");
			s = s.replaceAll("[", "");
			s = s.replaceAll("]", "");
			StringBuffer sb = new StringBuffer();			
			String[] arr = s.split(";");
			
			sb.append("{"); int i=0;
			sb.append("shapeCode=\'"); sb.append(arr[i++]); sb.append("\',");
			sb.append("shapeStyle=\'"); sb.append(arr[i++]); sb.append("\',");
			sb.append("lineWidth=\'"); sb.append(arr[i++]); sb.append("\',");
			sb.append("dashLength=\'"); sb.append(arr[i++]); sb.append("\',");
			sb.append("breakLength=\'"); sb.append(arr[i++]); sb.append("\',");
			sb.append("alignment=\'"); sb.append(arr[i++]); sb.append("\',");
			sb.append("bkgColor=\'"); sb.append(arr[i++]); sb.append("\',");
			sb.append("lineColor=\'"); sb.append(arr[i++]); sb.append("\',");
			sb.append("lineType=\'"); sb.append(arr[i++]); sb.append("\',");
			sb.append("lineDirection=\'"); sb.append(arr[i++]); sb.append("\',");			
			
			sb.append("startDirection=\'"); sb.append(arr[i++]); sb.append("\',");
			sb.append("endDirection=\'"); sb.append(arr[i++]); sb.append("\',");
			sb.append("dynamicTooltip=\'"); sb.append(arr[i++]); sb.append("\',");
			
			sb.append("startShapeCode=\'"); sb.append(arr[i++]); sb.append("\',");
			i++; // shapeStyle
			sb.append("startLineWidth=\'"); sb.append(arr[i++]); sb.append("\',");
			sb.append("startTotalWidth=\'"); sb.append(arr[i++]); sb.append("\',");
			sb.append("startTotalHeight=\'"); sb.append(arr[i++]); sb.append("\',");
			i++; // alignment
			sb.append("startBkgColor=\'"); sb.append(arr[i++]); sb.append("\',");
			sb.append("startLineColor=\'"); sb.append(arr[i++]); sb.append("\',");

			sb.append("endShapeCode=\'"); sb.append(arr[i++]); sb.append("\',");
			i++; // shapeStyle
			sb.append("endLineWidth=\'"); sb.append(arr[i++]); sb.append("\',");
			sb.append("endTotalWidth=\'"); sb.append(arr[i++]); sb.append("\',");
			sb.append("endTotalHeight=\'"); sb.append(arr[i++]); sb.append("\',");
			i++; // alignment
			sb.append("endBkgColor=\'"); sb.append(arr[i++]); sb.append("\',");
			sb.append("endLineColor=\'"); sb.append(arr[i++]); sb.append("\',");

			sb.append("middleShapeCode=\'"); sb.append(arr[i++]); sb.append("\',");
			i++; // shapeStyle
			sb.append("middleLineWidth=\'"); sb.append(arr[i++]); sb.append("\',");
			sb.append("middleTotalWidth=\'"); sb.append(arr[i++]); sb.append("\',");
			sb.append("middleTotalHeight=\'"); sb.append(arr[i++]); sb.append("\',");
			i++; // alignment
			sb.append("middleBkgColor=\'"); sb.append(arr[i++]); sb.append("\',");
			sb.append("middleLineColor=\'"); sb.append(arr[i++]); sb.append("\'");
			
			sb.append("}");			
			return LuaValue.valueOf(sb.toString());			
		}

	}
	class GetPortStyle extends OneArgFunction {		

		@Override
		synchronized public LuaValue call(LuaValue arg0) {			
			return new GetNodeStyle().call(arg0);
		}

	}
	class GetCompartmentStyle extends OneArgFunction {		

		@Override
		synchronized public LuaValue call(LuaValue arg0) {
			String s = arg0.tojstring();
			if (s==null)
				return LuaValue.valueOf("");
			s = s.replaceAll("[", "");
			s = s.replaceAll("]", "");
			StringBuffer sb = new StringBuffer();			
			String[] arr = s.split(";");
			
			sb.append("{"); int i=0;
			sb.append("caption=\'"); sb.append(arr[i++]); sb.append("\',");
			sb.append("alignment=\'"); sb.append(arr[i++]); sb.append("\',");
			sb.append("adjustment=\'"); sb.append(arr[i++]); sb.append("\',");
			sb.append("textDirection=\'"); sb.append(arr[i++]); sb.append("\',");
			i+=5; // shapeCode, shapeStyle, lineWidth, dashLength, breakLength
			sb.append("adornment=\'"); sb.append(arr[i++]); sb.append("\',");  //10
			
			i+=2; // bkgColor
			sb.append("width=\'"); sb.append(arr[i++]); sb.append("\',");
			sb.append("height=\'"); sb.append(arr[i++]); sb.append("\',");
			sb.append("xPos=\'"); sb.append(arr[i++]); sb.append("\',");
			sb.append("yPos=\'"); sb.append(arr[i++]); sb.append("\',"); //16
			
			sb.append("isVisible=\'"); sb.append(arr[i++]); sb.append("\',");
			sb.append("breakAtSpace=\'"); sb.append(arr[i++]); sb.append("\',");
			sb.append("compactVisible=\'"); sb.append(arr[i++]); sb.append("\',");
			sb.append("dynamicToolTip=\'"); sb.append(arr[i++]); sb.append("\',");
			sb.append("fontCharSet=\'"); sb.append(arr[i++]); sb.append("\',");
			sb.append("fontPitch=\'"); sb.append(arr[i++]); sb.append("\',");
			sb.append("fontSize=\'"); sb.append(arr[i++]); sb.append("\',");
			sb.append("fontStyle=\'"); sb.append(arr[i++]); sb.append("\',");
			sb.append("fontColor=\'"); sb.append(arr[i++]); sb.append("\',");
			sb.append("fontTypeFace=\'"); sb.append(arr[i++]); sb.append("\',");
			
			sb.append("picture=\'"); sb.append(arr[i++]); sb.append("\',");
			sb.append("picStyle=\'"); sb.append(arr[i++]); sb.append("\',");
			sb.append("picPos=\'"); sb.append(arr[i++]); sb.append("\',");
			sb.append("picWidth=\'"); sb.append(arr[i++]); sb.append("\',");
			sb.append("picHeight=\'"); sb.append(arr[i++]); sb.append("\'");
			
			sb.append("}");			
			return LuaValue.valueOf(sb.toString());
		}

	}
	class IsOpenDiagram extends OneArgFunction {		

		@Override
		synchronized public LuaValue call(LuaValue arg0) {
			long r = arg0.tolong();
			
			
			long rCls = raapi.findClass("GraphDiagram");
			long rAssoc = raapi.findAssociationEnd(rCls, "frame");
			
			long it = raapi.getIteratorForLinkedObjects(r, rAssoc);
			long rFirst = raapi.resolveIteratorFirst(it);
			raapi.freeIterator(it);
									
			return LuaValue.valueOf((rFirst!=0)?"open":"closed"); // if frame attached, then open
		}

	}

}
