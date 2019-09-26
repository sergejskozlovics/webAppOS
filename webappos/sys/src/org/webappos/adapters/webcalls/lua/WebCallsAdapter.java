package org.webappos.adapters.webcalls.lua;

import java.io.File;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import lv.lumii.tda.raapi.RAAPI;

import org.luaj.vm2.*;
import org.luaj.vm2.compiler.LuaC;
import org.luaj.vm2.lib.*;
import org.luaj.vm2.lib.jse.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.webappos.properties.AppProperties;
import org.webappos.server.API;
import org.webappos.server.ConfigStatic;
import org.webappos.util.StackTrace;
import org.webappos.webcaller.ITdaWebCallsAdapter;

import java.util.PriorityQueue;

public class WebCallsAdapter implements ITdaWebCallsAdapter {
	private static Logger logger =  LoggerFactory.getLogger(WebCallsAdapter.class);
		
	private static int MAX_CACHE = 10;
	private class LuaCachedEnvironment {
		public String project_id;
		public long lastAccessed;
		public LuaTable globals;
		public LuaValue lua_raapi;
		public LuaValue lua_tda;
		public LuaValue lua_graphDiagram;
		public LuaValue lua_java;
	}
	private static Module_lfs lfs = new Module_lfs();
	private static Module_socket socket = new Module_socket();
	private static Module_lua_tda.console_log console_log = new Module_lua_tda.console_log();
	private static Module_lua_tda.getenv getenv = new Module_lua_tda.getenv();
	private static Module_lua_tda.setenv setenv = new Module_lua_tda.setenv();
	private static Module_lua_tda.retrieve_java_pipe_handle_pointer_address retrieve_java_pipe_handle_pointer_address = new Module_lua_tda.retrieve_java_pipe_handle_pointer_address();
	private static Module_lua_tda.store_java_pipe_handle_pointer_address store_java_pipe_handle_pointer_address = new Module_lua_tda.store_java_pipe_handle_pointer_address();
	private static Module_lua_tda.eval eval = new Module_lua_tda.eval();
	
	
	private static PriorityQueue<LuaCachedEnvironment> queue = new PriorityQueue<LuaCachedEnvironment>(11, new Comparator<LuaCachedEnvironment>() {

		@Override
		public int compare(LuaCachedEnvironment o1, LuaCachedEnvironment o2) {
			if (o1.lastAccessed == o2.lastAccessed)
				return 0;
			if (o1.lastAccessed < o2.lastAccessed)
				return -1;
			else
				return +1;
		}
		
	});
	private static Map<String, LuaCachedEnvironment> cache = new HashMap<String, LuaCachedEnvironment>();

	static {
		logger.trace("Lua web calls adapter...");
		LuaC.install();
		//LuaJC.install();
		
		// Loading lua_classes for all available apps...
		
		for (File f : new File(ConfigStatic.APPS_DIR).listFiles()) {
			if (f.isDirectory() && f.getName().endsWith(".app")) {
				String dir = API.propertiesManager.getAppDirectory(f.getName());
				if (dir==null)
					continue;
				dir += "/lua_classes";
				API.classLoader.loadLuaClasses(dir);
			}
		}
        				
	}
	
	private LuaTable newGlobals() {
			LuaTable _G = new LuaTable();
			_G.load(new JseBaseLib());
			_G.load(new PackageLib());
			_G.load(new TableLib());
			_G.load(new StringLib());
			_G.load(new CoroutineLib());
			_G.load(new JseMathLib());
			_G.load(new JseIoLib());
			_G.load(new JseOsLib());
			_G.load(new LuajavaLib());
			_G.load(new DebugLib());
			
			return _G;				
	}
	
	
	public static class DateFunc extends OneArgFunction {		

		@Override
		public LuaValue call(LuaValue v) {
			Date d = new Date();
			return LuaValue.valueOf( d.toString().replace('.', '_').replace(':', '_') );
		}

	}
	
	@Override
	public void tdacall(String location, String pwd, long rObject, RAAPI raapi, String project_id, String appFullName,
			String login) {

		 
		long time1 =System.currentTimeMillis();
		String specificBin = API.propertiesManager.getAppDirectory(appFullName);
		
		LuaCachedEnvironment luaCachedEnv = cache.get(project_id);
		if (luaCachedEnv == null) {
			
			while (cache.size() >= MAX_CACHE) {
				// removing the element that has not accessed for the longest time...
				LuaCachedEnvironment toRemove = queue.poll();
				logger.trace("Removing from cache "+toRemove.project_id+"...");
				cache.remove(toRemove.project_id);
			}
			
			try {
				luaCachedEnv = new LuaCachedEnvironment();
				luaCachedEnv.project_id = project_id;
				luaCachedEnv.globals = newGlobals();
				LuaThread.setGlobals(luaCachedEnv.globals);
				cache.put(project_id, luaCachedEnv);
	
				luaCachedEnv.lua_raapi = new Module_lua_raapi(raapi);
				
				luaCachedEnv.lua_tda = new Module_lua_tda(luaCachedEnv.globals, raapi, project_id, appFullName, login);
				
				luaCachedEnv.lua_graphDiagram = new Module_lua_graphDiagram(raapi);
				
				luaCachedEnv.lua_java = new Module_lua_java(raapi, appFullName);
				

				LuaValue lua_raapi1 = luaCachedEnv.globals.load(luaCachedEnv.lua_raapi);
				luaCachedEnv.globals.get("package").get("loaded").set("lua_raapi", lua_raapi1);
				luaCachedEnv.globals.set("lua_raapi", lua_raapi1);

				LuaValue lua_tda1 = luaCachedEnv.globals.load(luaCachedEnv.lua_tda);
				luaCachedEnv.globals.get("package").get("loaded").set("lua_tda", lua_tda1);
				luaCachedEnv.globals.set("lua_tda", lua_tda1);
				
				LuaValue lua_graphDiagram1 = luaCachedEnv.globals.load(luaCachedEnv.lua_graphDiagram);
				luaCachedEnv.globals.get("package").get("loaded").set("lua_graphDiagram", lua_graphDiagram1);
				luaCachedEnv.globals.set("lua_graphDiagram", lua_graphDiagram1);

				LuaValue lua_java1 = luaCachedEnv.globals.load(luaCachedEnv.lua_java);
				luaCachedEnv.globals.get("package").get("loaded").set("lua_java", lua_java1);
				luaCachedEnv.globals.set("lua_java", lua_java1);

				LuaValue lfs1 = luaCachedEnv.globals.load(lfs);
				luaCachedEnv.globals.get("package").get("loaded").set("lfs", lfs1);
				luaCachedEnv.globals.set("lfs", lfs1);
	
				LuaValue socket1 = luaCachedEnv.globals.load(socket);
				luaCachedEnv.globals.get("package").get("loaded").set("socket", socket1);
				luaCachedEnv.globals.set("socket", socket1);
	
				luaCachedEnv.globals.set("console_log", console_log);
				luaCachedEnv.globals.set("getenv", getenv);
				luaCachedEnv.globals.set("setenv", setenv);
				luaCachedEnv.globals.set("retrieve_java_pipe_handle_pointer_address", retrieve_java_pipe_handle_pointer_address);
				luaCachedEnv.globals.set("store_java_pipe_handle_pointer_address", store_java_pipe_handle_pointer_address);
				luaCachedEnv.globals.set("eval", eval);
				
				luaCachedEnv.globals.set("os_date", new DateFunc());
				
				API.classLoader.loadLuaClasses(specificBin+"/lua_classes");
				
				LuaValue luaLoad = luaCachedEnv.globals.get("loadstring");
				
				String paths = specificBin+"/lua_src/?.lua;"
								+specificBin+"/lua_src/libs/?.lua;"
								+specificBin+"/lua_classes/?.class;"
								+specificBin+"/lua/?.lua;"
								+specificBin+"/lua/libs/?.lua;"
								+specificBin+"/lua/?.class;"
								+pwd+"/?.lua;"
								+pwd+"/?.class;"
								+ConfigStatic.ROOT_DIR+"/lib/lua/?.lua";
				
				AppProperties props = API.propertiesManager.getAppPropertiesByFullName(appFullName);
				paths += props.properties.getProperty("lua_paths","");
					
					paths = paths.replace('\\', '/');
					
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
					
					
					LuaValue luaCompiledCode4 = luaLoad.call(LuaValue.valueOf( // TODO (or not needed?)
							"function execute_in_new_thread(name)\n"
							+ "return loadstring('return '..name..'()')"
							+ "end\n"));
					luaCompiledCode4.call();		
					LuaValue luaCompiledCode5 = luaLoad.call(LuaValue.valueOf( // TODO (or not needed?)
							"os.date = os_date\n"));
					luaCompiledCode5.call();		
				
			}
			catch (Throwable t) {
				logger.error("Lua not loaded. Throwable = "+t+"\n StackTrace="+StackTrace.get(t));
				return;
			}
		} // if luaCachedEnv == null
		else
			LuaThread.setGlobals(luaCachedEnv.globals);

		// change priority
		queue.remove(luaCachedEnv);
		luaCachedEnv.lastAccessed = new Date().getTime();
		logger.trace("Updating Lua cache priority for "+luaCachedEnv.project_id);
		queue.add(luaCachedEnv);

		
		
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
		if (!funcName.endsWith("()")) {
			if (rObject!=0)
				funcName += "(lQuery("+rObject+"))";
			else
				funcName += "()";
		}
		
		String code = moduleName+" = require(\""+className+"\")\n"+moduleName+"."+funcName;
		if (true || logger.isDebugEnabled())
			code = "print(\"BEFORE REQUIRE "+className+"\")\n"+moduleName+" = require(\""+className+"\")\nprint(\"BEFORE LUA CODE "+moduleName+"."+funcName+"\")\n\n"+moduleName+"."+funcName+"\nprint(\"AFTER LUA CODE\")";
		try {
			LuaValue luaLoad = luaCachedEnv.globals.get("loadstring");
			LuaValue luaCompiledCode = luaLoad.call(LuaValue.valueOf(code));
			time3 = System.currentTimeMillis();
			luaCompiledCode.call();			
		}
		catch (Throwable t) {
			logger.error("Lua exception: "+t+"\nStackTrace="+StackTrace.get(t)+"\ncode=["+code+"]");
		}
		
		long time4 =System.currentTimeMillis();
		logger.debug("LUA TIMES: INIT="+(time2-time1)+" COMPILE="+(time3-time2)+" RUN="+(time4-time3));	
	}



	/**
	 * This function indicates that this web calls adapter can store cache per project.
	 * By calling clearCache, that cache can be forgotten.
	 * @param project_id the project_id for which to forget (clear) the cache
	 */
	synchronized public static void clearCache(String project_id) {
		System.out.println("Removing from cache "+project_id+" (by request)...");
		logger.trace("Removing from cache "+project_id+" (by request)...");
		cache.remove(project_id);
	}

}
