package org.webappos.adapters.webcalls.lua;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

import org.apache.commons.io.FileUtils;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;
import org.luaj.vm2.lib.ZeroArgFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.webappos.server.API;
import org.webappos.server.ConfigStatic;
import org.webappos.webcaller.WebCaller;

import lv.lumii.tda.kernel.TDAKernel;
import lv.lumii.tda.raapi.RAAPI;

import org.luaj.vm2.lib.LibFunction;
import org.luaj.vm2.lib.OneArgFunction;
import org.luaj.vm2.lib.TwoArgFunction;
import org.luaj.vm2.lib.VarArgFunction;

public class Module_lua_tda extends TwoArgFunction {
	private static Logger logger =  LoggerFactory.getLogger(Module_lua_tda.class);
	
	public static Module_lua_tda LIB = null;
	
	private static LuaTable globals;
	private RAAPI raapi;
	private String project_id;
	private String appFullName;
	private String login;
	
	private long rSubmitterCls = 0;
	private long rSubmitterObj = 0;
	private long rEventCls = 0;
	private long rCommandCls = 0;
	 
	private long rEventSubmitterAssoc = 0;
	private long rCommandSubmitterAssoc = 0;
	
    public Module_lua_tda(LuaTable _globals, RAAPI _raapi, String _project_id, String _appFullName, String _login) {
        LIB = this;
        globals = _globals;
        raapi = _raapi;
        project_id = _project_id;
        appFullName = _appFullName;
        login = _login;
        
		rSubmitterCls = raapi.findClass("TDAKernel::Submitter");
		long it = raapi.getIteratorForAllClassObjects(rSubmitterCls);
		rSubmitterObj = raapi.resolveIteratorFirst(it);
		raapi.freeIterator(it);
		
		rEventCls = raapi.findClass("TDAKernel::Event");
		rCommandCls = raapi.findClass("TDAKernel::Command");
		rEventSubmitterAssoc = raapi.findAssociationEnd(rEventCls, "submitter");
		rCommandSubmitterAssoc = raapi.findAssociationEnd(rCommandCls, "submitter");        
    }

	@Override
	public LuaValue call(LuaValue modName, LuaValue env) {
		LuaTable module = new LuaTable(0,30); // I think "new LuaTable()" instead of "(0, 30)" is OK
        module.set("GetProjectPath", new GetProjectPath());
        module.set("GetToolPath", new GetToolPath());
        module.set("GetRuntimePath", new GetRuntimePath());        
        module.set("EnqueueCommand", new ExecuteCommand());
        module.set("ExecuteCommand", new ExecuteCommand());
        module.set("BrowseForFile", new BrowseForFile());
        module.set("CallFunctionWithPleaseWaitWindow", new CallFunctionWithPleaseWaitWindow());
        module.set("SetProgressMessage", new SetProgressMessage());
        module.set("SetProgressBar", new SetProgressBar());
        module.set("BringTDAToForground", new BringTDAToForground());
        return module;		
	}

	private String getEEAttribute(String attrName) {
		long rEECls = raapi.findClass("EnvironmentEngine");
		long rAttr = raapi.findAttribute(rEECls, attrName);
		long it = raapi.getIteratorForDirectClassObjects(rEECls);
		long r = raapi.resolveIteratorFirst(it);
		raapi.freeIterator(it);
		
		String specificBin = raapi.getAttributeValue(r, rAttr);
		raapi.freeReference(r);
		raapi.freeReference(rAttr);
		raapi.freeReference(rEECls);
		return specificBin;
	}

	
	class GetProjectPath extends ZeroArgFunction {		

		@Override
		public LuaValue call() {
			String s = API.dataMemory.getProjectFolder(project_id);
			return LuaValue.valueOf(s);
		}

	}
	
	class GetToolPath extends ZeroArgFunction {		

		@Override
		public LuaValue call() {	
			return LuaValue.valueOf(ConfigStatic.APPS_DIR+File.separator+API.dataMemory.getProjectFullAppName(project_id));
		}

	}

	class GetRuntimePath extends ZeroArgFunction {		

		@Override
		public LuaValue call() {						
			return LuaValue.valueOf(ConfigStatic.BIN_DIR);
		}

	}


	public static class console_log extends OneArgFunction {		

		@Override
		public LuaValue call(LuaValue v) {
			System.err.println(v.toString());
			return null;
		}

	}


	public static class getenv extends OneArgFunction {		

		@Override
		public LuaValue call(LuaValue v) {
			return LuaValue.valueOf(System.getenv(v.tojstring()));
		}

	}


	public static class setenv extends TwoArgFunction {		

		@Override
		public LuaValue call(LuaValue arg1, LuaValue arg2) {
			// do nothing (TODO)
			return LuaValue.NONE;
		}

	}
	
	private static long javaHandle = 0;
	public static class retrieve_java_pipe_handle_pointer_address extends ZeroArgFunction {		

		@Override
		public LuaValue call() {
			return LuaValue.valueOf(javaHandle);
		}

	}
		
	public static class store_java_pipe_handle_pointer_address extends OneArgFunction {		

		@Override
		public LuaValue call(LuaValue v) {
			javaHandle = v.tolong();
			return LuaValue.NONE;
		}

	}

	public static class eval extends OneArgFunction {		

		@Override
		public LuaValue call(LuaValue v) {
			// TODO client jsonsbumit show please wait
			LuaValue luaLoad = globals.get("loadstring");
			LuaValue luaCompiledCode = luaLoad.call(LuaValue.valueOf("return "+v.tojstring()));
			LuaValue retVal = luaCompiledCode.call();
			return retVal;
		}

	}


	public class ExecuteCommand extends OneArgFunction {		

		@Override
		public LuaValue call(LuaValue v) {
			boolean retVal = raapi.createLink(v.tolong(), rSubmitterObj, rCommandSubmitterAssoc);
			return LuaValue.valueOf(retVal);
		}

	}
	
	
	abstract public class FiveArgFunction extends LibFunction {

		abstract public LuaValue call(LuaValue arg1, LuaValue arg2, LuaValue arg3, LuaValue arg4, LuaValue arg5);

		public FiveArgFunction() {
		}

		public FiveArgFunction(LuaValue env) {
			this .env = env;
		}

		@Override
		public final LuaValue call() {
		return call(NIL, NIL, NIL, NIL, NIL);
		}

		@Override
		public final LuaValue call(LuaValue arg) {
		return call(arg, NIL, NIL, NIL, NIL);
		}

		@Override
		public LuaValue call(LuaValue arg1, LuaValue arg2) {
		return call(arg1, arg2, NIL, NIL, NIL);
		}

		@Override
		public LuaValue call(LuaValue arg1, LuaValue arg2, LuaValue arg3) {
		return call(arg1, arg2, arg3, NIL, NIL);
		}

		public LuaValue call(LuaValue arg1, LuaValue arg2, LuaValue arg3, LuaValue arg4) {
		return call(arg1, arg2, arg3, arg4, NIL);
		}

		@Override
		public Varargs invoke(Varargs varargs) {
			return call(varargs.arg1(), varargs.arg(2), varargs.arg(3), varargs.arg(4), varargs.arg(5));
		}

	}	

/*
 * // Shows an open or save dialog. ;
// * The caption parameter specifies the caption of the window, e.g., "Please, select the file...". ;
// * The filter describes certain file types with the corresponding extensions. ;
//   Different file types are delimited by \n. ;
//   Filter example: "Word document (*.doc;*.rtf)\nC++ file(*.cpp;*.h;*.hpp)". ;
// * The startDirectory parameter specifies the initial directory. ;
// * The startFileName parameter specifies the default file name. ;
// * The save parameter specifies whether the dialog is save or open dialog. ;
extern "C" __declspec(dllimport) string __cdecl L0_Func_BrowseForFileEx(string caption, string filter, string start_directory, string start_file_name, bool save, int &filterIndex);
static int tda_BrowseForFile(lua_State *L) {
  const char * caption = luaL_checkstring(L, 1);
  const char * filter = luaL_checkstring(L, 2);
  const char * start_directory = luaL_checkstring(L, 3);
  const char * start_file_name = luaL_checkstring(L, 4);
  bool save = lua_toboolean(L, 5);

  int selection_index;
  string s = L0_Func_BrowseForFileEx(caption, filter, start_directory, start_file_name, save, selection_index);
  const char * file_path = s.c_str();
  

  lua_pushstring(L, file_path);
  lua_pushnumber(L, selection_index);
  
  return 2;
}
	
 */
	class MyFileFilter extends FileFilter {
		private String[] extensions;
		private String description;
		
		public MyFileFilter(String _description, String[] _extensions /* in lower case */) {
			description = _description;
			extensions = _extensions;
		}

		@Override
		public boolean accept(File f) {
			if (f.isDirectory()) {
		        return true;
		    }
			
			String s = f.getAbsolutePath().toLowerCase();
			for (String ext : extensions)
				if (s.endsWith(ext))
					return true;
			
			return false;
		}

		@Override
		public String getDescription() {
			if (description != null)
				return description;
			
			String s = "";
			for (String ext : extensions) {
				if (s.isEmpty())
					s = ext;
				else
					s += "; "+ext;
			}
			return "User-specific filter for "+s;
		}
		
	}

	class MyFileFilters {
		private ArrayList<ArrayList<String>> masks = new ArrayList<ArrayList<String>>();
		private ArrayList<MyFileFilter> filters = new ArrayList<MyFileFilter>(); 
		
		public MyFileFilters(String _masks) {
			String[] lines = _masks.split("\n");
			for (String s : lines) {
				int i = s.indexOf("(");
				int j = s.lastIndexOf(")");
				if ((i<0) || (j<0) || (i>=j))
					continue;
				String description = s.substring(0, i);
				String m = s.substring(i+1, j);
				String[] arr = m.split(";");				
				
				boolean wasAllMask = false;
				for (int k=0; k<arr.length; k++) {
					if ("*.*".equals(arr[k]) || "*".equals(arr[k])) {
						arr[k] = "";
						wasAllMask = true;
					}
					else
						if (arr[k].startsWith("*"))
							arr[k] = arr[k].substring(1); 
				}
				
				if (!wasAllMask)
					filters.add(new MyFileFilter(description, arr));
			}
		}
		
		public List<MyFileFilter> getFilters() {
			return filters;
		}

	}
	
	class BrowseForFile extends VarArgFunction {		
		@Override
		public Varargs invoke(Varargs v) {
			logger.debug("In BrowseForFile");
			
			LuaValue filterStr = v.arg(2);
			LuaValue isSave = v.arg(5);
			String[] arr = filterStr.toString().split("\n");
			
			int index = 0;
			if (!isSave.toboolean()) 
				index = arr.length; // we assume the *.* (all files) are appended, and we return it
			
			if (API.config.inWebMode) {
				try {
					
					if (isSave.toboolean()) {
						File fi = new File(API.dataMemory.getProjectFolder(project_id)+File.separator+"fordownload.index");
						File fb = new File(API.dataMemory.getProjectFolder(project_id)+File.separator+"fordownload.browseForFile");
						if (fi.exists() && fb.exists()) {
							fi.delete();
							fb.delete();
						}
						
						if ((arr.length<=1) || fi.exists()) {
							
							if (arr.length>1) {
								try { 
									String s = FileUtils.readFileToString(fi, "UTF-8");
									index = Integer.parseInt(s);
									if (index<0)
										index=0;
									if (index>arr.length)
										index=arr.length;
								}
								catch(Throwable t) {								
								}
							}
							// else: index==0
							
							WebCaller.WebCallSeed seed = new WebCaller.WebCallSeed();
							
							seed.actionName = "browseForFile_download";
							
							seed.callingConventions = WebCaller.CallingConventions.JSONCALL;
							
							String ext = "txt";
							String filter = arr[index];
							int i = filter.indexOf("(*.");
							if (i>=0) {
								int j = filter.indexOf(")", i+3);
								if (j>=0)
									ext = filter.substring(i+3, j);
							}
							
							seed.jsonArgument = ext;
							
							seed.login = login;
							seed.project_id = project_id;
					  		
					  		System.out.println("seed: "+seed.actionName+" "+seed.jsonArgument);
					  		
					  		API.webCaller.enqueue(seed);
							
							Varargs varargs = LuaValue.varargsOf(new LuaValue[] {
									LuaValue.valueOf(API.dataMemory.getProjectFolder(project_id)+File.separator+"fordownload.browseForFile"), LuaValue.valueOf(index)
						    });			
							return varargs;
						}
						else {
						
							WebCaller.WebCallSeed seed = new WebCaller.WebCallSeed();
							
							seed.actionName = "browseForFile_index";
							
							seed.callingConventions = WebCaller.CallingConventions.JSONCALL;
							seed.jsonArgument = filterStr.tojstring();
							
							seed.login = login;
							seed.project_id = project_id;
					  		
					  		API.webCaller.enqueue(seed);
					  		
							logger.debug("lua_tda.BrowseForFile: file not generated yet, the client has to specify the filter index and re-launch our transformation");
							Varargs varargs = LuaValue.varargsOf(new LuaValue[] {
									LuaValue.valueOf(""), LuaValue.valueOf(index)
						    });			
					  		return varargs;
						}				  		
				  		
					}
					else {
						File fi = new File(API.dataMemory.getProjectFolder(project_id)+File.separator+"uploaded.browseForFile");
						if (fi.exists()) {
							Varargs varargs = LuaValue.varargsOf(new LuaValue[] {
									LuaValue.valueOf(fi.getAbsolutePath()), LuaValue.valueOf(index)
						    });			
							return varargs;							
						}
						else {
							WebCaller.WebCallSeed seed = new WebCaller.WebCallSeed();
							
							seed.actionName = "browseForFileToUpload";
							
							seed.callingConventions = WebCaller.CallingConventions.JSONCALL;
							seed.jsonArgument = filterStr.tojstring();
							
							seed.login = login;
							seed.project_id = project_id;
					  		
					  		API.webCaller.enqueue(seed);
							logger.debug("lua_tda.BrowseForFile: file not uploaded yet, the client has to upload the file and re-launch our transformation");
							Varargs varargs = LuaValue.varargsOf(new LuaValue[] {
									LuaValue.valueOf(""), LuaValue.valueOf(index)
						    });			
					  		return varargs;
						}
					}
					
					
				}
				catch(Throwable t)
				{
					Varargs varargs = LuaValue.varargsOf(new LuaValue[] {
							LuaValue.valueOf(""), LuaValue.valueOf(index)
				    });			
			  		return varargs;
				}
				
			}
			else {
				final JFileChooser fc = new JFileChooser();
				
				for (MyFileFilter fltr : new MyFileFilters(filterStr.tojstring()).getFilters()) {
					fc.addChoosableFileFilter(fltr);
				}
				
				String retVal = "";
				if (isSave.toboolean()) {
					// save dialog					
					if (fc.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
						retVal = fc.getSelectedFile().getAbsolutePath();
					}
				}
				else { // open dialog
					if (fc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
						retVal = fc.getSelectedFile().getAbsolutePath();
					}
				}
				
				logger.debug("BrowseForFile retVal="+retVal);
				Varargs varargs = LuaValue.varargsOf(new LuaValue[] {
						LuaValue.valueOf(retVal),
						LuaValue.valueOf(index)
			    });
				
				return varargs;
			}
			// 	tda.BrowseForFile("Open", "OWL File (*.owl;*.xml;*.rdf)\nAll Files (*)", start_path or "", "", false)

//			return LuaValue.valueOf(raapi.createAssociation(arg0.tolong(), arg1.tolong(), arg2.toString(), arg3.toString(), arg4.toboolean()));
		}

	}
	

	public class CallFunctionWithPleaseWaitWindow extends TwoArgFunction {		

		@Override
		public LuaValue call(LuaValue luaFunction, LuaValue luaArg) {
			// TODO client jsonsbumit show please wait
			LuaValue luaLoad = globals.get("loadstring");
			LuaValue luaCompiledCode = luaLoad.call(LuaValue.valueOf(luaFunction+"([["+luaArg.tojstring()+"]])"));
			
			LuaValue retVal = luaCompiledCode.call();
			// TODO client jsonsbumit hide please wait
			
			return retVal;
		}

	}
	
	
	public class SetProgressMessage extends OneArgFunction {		

		@Override
		public LuaValue call(LuaValue msg) {
			System.err.println(msg.tojstring());
			return LuaValue.NONE;
		}

	}

	public class SetProgressBar extends OneArgFunction {		

		@Override
		public LuaValue call(LuaValue val) {
			System.err.println(val.toint());
			return LuaValue.NONE;
		}

	}
	
	public class BringTDAToForground extends ZeroArgFunction {		

		@Override
		public LuaValue call() {
			return LuaValue.NONE;
		}

	}
	
}
