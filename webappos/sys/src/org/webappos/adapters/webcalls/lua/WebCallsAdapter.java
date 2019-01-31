package org.webappos.adapters.webcalls.lua;

import java.io.File;
import lv.lumii.tda.raapi.RAAPI;

import org.luaj.vm2.*;
import org.luaj.vm2.lib.jse.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.webappos.properties.PropertiesManager;
import org.webappos.server.API;
import org.webappos.util.StackTrace;
import org.webappos.webcaller.ITdaWebCallsAdapter;


public class WebCallsAdapter implements ITdaWebCallsAdapter {
	private static Logger logger =  LoggerFactory.getLogger(WebCallsAdapter.class);
	
	@Override
	public void tdacall(String location, long rObject, RAAPI raapi, String project_id, String appFullName,
			String login) {

		 
		long time1 =System.currentTimeMillis();
		String specificBin = API.propertiesManager.getAppDirectory(appFullName);
		String projectDirectory = API.memory.getProjectFolder(project_id);
		
		LuaTable globals;
		
		try {

			globals = JsePlatform.debugGlobals();//standardGlobals();
			
			
			// adding TDA built-in lua libs (modules)						
			//globals..package_.loaded.set("mine", mine);
			
			LuaValue lua_raapi = globals.load(new Module_lua_raapi(raapi));
			globals.get("package").get("loaded").set("lua_raapi", lua_raapi);
			globals.set("lua_raapi", lua_raapi);
			
			LuaValue lua_tda = globals.load(new Module_lua_tda(globals, raapi, project_id, appFullName, login));
			globals.get("package").get("loaded").set("lua_tda", lua_tda);
			globals.set("lua_tda", lua_tda);
			
			LuaValue lua_graphDiagram = globals.load(new Module_lua_graphDiagram(raapi));
			globals.get("package").get("loaded").set("lua_graphDiagram", lua_graphDiagram);
			globals.set("lua_graphDiagram", lua_graphDiagram);
			
			LuaValue lua_java = globals.load(new Module_lua_java(raapi, appFullName));
			globals.get("package").get("loaded").set("lua_java", lua_java);
			globals.set("lua_java", lua_java);
			
			LuaValue lfs = globals.load(new Module_lfs());
			globals.get("package").get("loaded").set("lfs", lfs);
			globals.set("lfs", lfs);

			LuaValue socket = globals.load(new Module_socket());
			globals.get("package").get("loaded").set("socket", socket);
			globals.set("socket", socket);

			globals.set("console_log", new Module_lua_tda.console_log());
			globals.set("getenv", new Module_lua_tda.getenv());
			globals.set("setenv", new Module_lua_tda.setenv());
			globals.set("retrieve_java_pipe_handle_pointer_address", new Module_lua_tda.retrieve_java_pipe_handle_pointer_address());
			globals.set("store_java_pipe_handle_pointer_address", new Module_lua_tda.store_java_pipe_handle_pointer_address());
			globals.set("eval", new Module_lua_tda.eval());
			
			
			
			String paths = 
			    specificBin+"/lua/?.lua;"
			    +specificBin+"/lua/libs/?.lua;"
				+specificBin+"/bin/lua/?.lua;"
				+specificBin+"/bin/lua/libs/?.lua;"
				+projectDirectory+"/Plugins/?.lua";
			
			paths = paths.replace('\\', '/');
			
			LuaValue luaLoad = globals.get("loadstring");
			LuaValue luaCompiledCode = luaLoad.call(LuaValue.valueOf("package.path = package.path .. ';"+paths+"'"));
			luaCompiledCode.call();			
						
			LuaValue luaCompiledCode2 = luaLoad.call(LuaValue.valueOf("string.gfind = string.gmatch"));
			luaCompiledCode2.call();
			
			
			LuaValue luaCompiledCode3 = luaLoad.call(LuaValue.valueOf(
					"function table_iter(t)\n"
					+ "local i = 0\n"
					+ "local n = table.getn(t)\n"
					+ "  return function()\n"
					+ "    i=i+1\n"
					+ "    if i <= n then return t[i] end\n"
					+ "  end\n"
					+ "end\n"
					+ "lfs.dir = function(s) return table_iter(lfs.dir2(s)) end"));
			luaCompiledCode3.call();		
			
			
			LuaValue luaCompiledCode4 = luaLoad.call(LuaValue.valueOf( // TODO!!!
					"function execute_in_new_thread(name)\n"
					+ "return loadstring('return '..name..'()')"
					+ "end\n"));
			luaCompiledCode4.call();		
			
			
		}
		catch (Throwable t) {
			logger.error("Lua not loaded. Throwable = "+t+"\n StackTrace="+StackTrace.get(t));
			return;
		}

		
		
		
		if ("main".equals(location)) {
			if (specificBin != null) {
				int i = specificBin.lastIndexOf("/");
				int j = specificBin.lastIndexOf("\\");
				if (j>i)
					i=j;
				if (i>0)
					specificBin = specificBin.substring(i+1); 
				location = specificBin+".create_project";
				
				File f = new File(specificBin+File.separator+"lua"+File.separator+specificBin+".lua");
				if (!f.exists()) {
					logger.error("lua adapter: main could not be called since "+f.getAbsolutePath()+" does not exist!");
					return;
				}
			}
			else
				return;
			
		}
		
				
		String className = location.substring(0, location.lastIndexOf('.'));
		String funcName = location.substring(location.lastIndexOf('.')+1);
		String moduleName = className;
		if (moduleName.lastIndexOf('.')>=0)
			moduleName = moduleName.substring(moduleName.lastIndexOf('.')+1);
				
		long time2 =System.currentTimeMillis();
		
		long time3 = 0;
//		String code = moduleName+" = require(\""+className+"\")\nprint(\"BEFORE LUA TR "+moduleName+"."+funcName+"\")\n\nprint(5)\n"+moduleName+"."+funcName+"(lQuery("+rObject+"))\nprint(\"AFTER LUA TR\")";
		if (!funcName.endsWith("()"))
			funcName += "(lQuery("+rObject+"))";
		String code = moduleName+" = require(\""+className+"\")\n"+moduleName+"."+funcName;
		try {
			LuaValue luaLoad = globals.get("loadstring");
			LuaValue luaCompiledCode = luaLoad.call(LuaValue.valueOf(code));
			time3 = System.currentTimeMillis();
			luaCompiledCode.call();			
		}
		catch (Throwable t) {
			logger.error("Lua exception: "+t+"\nStackTrace="+StackTrace.get(t)+"\ncode=["+code+"]");
		}
		
		long time4 =System.currentTimeMillis();
		// System.out.println("LUA TIMES: INIT="+(time2-time1)+" COMPILE="+(time3-time2)+" RUN="+(time4-time3));
	
	}

}
