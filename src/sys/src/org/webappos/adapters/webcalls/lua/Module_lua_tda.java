package org.webappos.adapters.webcalls.lua;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
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
import org.webappos.antiattack.ValidityChecker;
import org.webappos.server.API;
import org.webappos.server.ConfigStatic;
import org.webappos.util.StackTrace;
import org.webappos.webcaller.IWebCaller;
import org.webappos.webcaller.WebCaller;

import lv.lumii.tda.kernel.TDAKernel;
import lv.lumii.tda.raapi.RAAPI;

import org.luaj.vm2.lib.LibFunction;
import org.luaj.vm2.lib.OneArgFunction;
import org.luaj.vm2.lib.ThreeArgFunction;
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
	synchronized public LuaValue call(LuaValue modName, LuaValue env) {
		LuaTable module = new LuaTable(0,30); // I think "new LuaTable()" instead of "(0, 30)" is OK
        module.set("GetProjectPath", new GetProjectPath());
        module.set("GetToolPath", new GetToolPath());
        module.set("GetRuntimePath", new GetRuntimePath());        
        module.set("FindPath", new FindPath());        
        module.set("EnqueueCommand", new EnqueueCommand());
        module.set("ExecuteCommand", new ExecuteCommand());
        module.set("BrowseForFile", new BrowseForFile());
        module.set("BrowseForFolder", new BrowseForFolder());
        module.set("BrowseForFolderAsList", new BrowseForFolderAsList());
        module.set("CallFunctionWithPleaseWaitWindow", new CallFunctionWithPleaseWaitWindow());
        module.set("SetProgressMessage", new SetProgressMessage());
        module.set("SetProgressBar", new SetProgressBar());
        module.set("BringTDAToForground", new BringTDAToForground());
        module.set("isWeb", LuaValue.TRUE);        
        module.set("inWebMode",LuaValue.valueOf(API.config.inWebMode));
        module.set("CopyFile",new CopyFile());
        module.set("DeleteFileOrFolder", new DeleteFileOrFolder());
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
		synchronized public LuaValue call() {
			String s = API.dataMemory.getProjectFolder(project_id);
			return LuaValue.valueOf(s);
		}

	}
	
	class GetToolPath extends ZeroArgFunction {		

		@Override
		synchronized public LuaValue call() {	
			return LuaValue.valueOf(ConfigStatic.APPS_DIR+File.separator+API.dataMemory.getProjectFullAppName(project_id));
		}

	}

	class GetRuntimePath extends ZeroArgFunction {		

		@Override
		synchronized public LuaValue call() {						
			return LuaValue.valueOf(ConfigStatic.BIN_DIR);
		}

	}

	class FindPath extends TwoArgFunction {
		
		private String searchRecursively(File where, String what) {
			for (File f: where.listFiles()) {
				if (!f.isDirectory())
					continue;
				if (f.getName().equals(what))
					return where.getAbsolutePath()+File.separator+what;
				else {
					String s = searchRecursively(f, what);
					if (s!=null)
						return s;
				}
			}
			return null;
		}

		@Override
		synchronized public LuaValue call(LuaValue _where, LuaValue _what) {
			String s = searchRecursively(new File(_where.tojstring()), _what.tojstring());
			if (s == null)
				return LuaValue.NIL;
			else
				return LuaValue.valueOf(s);
		}

	}

	public static class console_log extends OneArgFunction {		

		@Override
		synchronized public LuaValue call(LuaValue v) {
			System.err.println(v.toString());
			return null;
		}

	}


	public static class getenv extends OneArgFunction {		

		@Override
		synchronized public LuaValue call(LuaValue v) {
			return LuaValue.valueOf(System.getenv(v.tojstring()));
		}

	}


	public static class setenv extends TwoArgFunction {		

		@Override
		synchronized public LuaValue call(LuaValue arg1, LuaValue arg2) {
			// do nothing (TODO)
			return LuaValue.NONE;
		}

	}
	
	private static long javaHandle = 0;
	public static class retrieve_java_pipe_handle_pointer_address extends ZeroArgFunction {		

		@Override
		synchronized public LuaValue call() {
			return LuaValue.valueOf(javaHandle);
		}

	}
		
	public static class store_java_pipe_handle_pointer_address extends OneArgFunction {		

		@Override
		synchronized public LuaValue call(LuaValue v) {
			javaHandle = v.tolong();
			return LuaValue.NONE;
		}

	}

	public static class eval extends OneArgFunction {		

		@Override
		synchronized public LuaValue call(LuaValue v) {
			// TODO client jsonsbumit show please wait
			LuaValue luaLoad = globals.get("loadstring");
			LuaValue luaCompiledCode = luaLoad.call(LuaValue.valueOf("return "+v.tojstring()));
			LuaValue retVal = luaCompiledCode.call();
			return retVal;
		}

	}

	private boolean skipCommand(long r, long rCls) {
		long rAssoc = raapi.findAssociationEnd(rCls, "receiver"); // D#Form
		if (rAssoc==0)
			rAssoc = raapi.findAssociationEnd(rCls, "graphDiagram"); // GraphDiagram
		if (rAssoc==0)
			return false;		
		long rInvAssoc = raapi.getInverseAssociationEnd(rAssoc);
		if (rInvAssoc==0)
			return false;
		
		long rAttr = raapi.findAttribute(rCls, "info");
		if (rAttr != 0) {
			String val = raapi.getAttributeValue(r, rAttr);
			if (!"Refresh".equalsIgnoreCase(val))
				return false;
		}
		
		
		long it = raapi.getIteratorForLinkedObjects(r, rAssoc);
		long rr = raapi.resolveIteratorFirst(it);
		boolean retVal = false;
		
		while (rr!=0) {
			long itInv = raapi.getIteratorForLinkedObjects(rr, rInvAssoc);
			long rrInv = raapi.resolveIteratorFirst(itInv);
			rrInv = raapi.resolveIteratorNext(itInv);
			raapi.freeIterator(itInv);
			if (rrInv!=0) {
				// there are at least 2 commands, which means that we have
				// found some other command for the same D#Form/GraphDiagram
				retVal = true;
				logger.debug("Skipping object "+r);
				raapi.deleteObject(r); // deleting object
				break; 
			}

			rr = raapi.resolveIteratorNext(it);
		}
		raapi.freeIterator(it);
		return retVal;
	}

	public class EnqueueCommand extends OneArgFunction {		

		@Override
		synchronized public LuaValue call(LuaValue v) {
			
			/*
			System.out.println("lua replicate1 "+v.tolong());
			long r2 = ((TDAKernel)raapi).replicateEventOrCommand(v.tolong());
			System.out.println("lua replicate2 "+r2);*/
			
			long r2 = v.tolong();
			
			//boolean retVal = raapi.createLink(r2, rSubmitterObj, rCommandSubmitterAssoc);
			//return LuaValue.valueOf(retVal);
			
			TDAKernel.Owner o = ((TDAKernel)raapi).getOwner();
			
			long it = raapi.getIteratorForDirectObjectClasses(r2);
			long rCls = raapi.resolveIteratorFirst(it);		
			String className = raapi.getClassName(rCls);
			raapi.freeReference(rCls);
			raapi.freeIterator(it);
			
			if (className == null)
				return LuaValue.valueOf(false);
			
			
			if (className.equals("D#Command") && skipCommand(r2, rCls))
				return LuaValue.valueOf(true);
			if (className.equals("OkCmd") && skipCommand(r2, rCls))
				return LuaValue.valueOf(true);
				
			IWebCaller.WebCallSeed seed = new IWebCaller.WebCallSeed();
			
			logger.debug("lua enqueue "+className+" r="+r2);
			
			seed.actionName = className;
			
			seed.callingConventions = WebCaller.CallingConventions.TDACALL;
			seed.tdaArgument = r2;			
	
			if (o!=null) {
				seed.login = o.login;
				seed.project_id = o.project_id;
			}
	  		
	  		API.webCaller.enqueue(seed);
			
			return LuaValue.valueOf(true);
		}

	}
	

	public class ExecuteCommand extends OneArgFunction {		

		@Override
		synchronized public LuaValue call(LuaValue v) {
			
			/*
			System.out.println("lua replicate1 "+v.tolong());
			long r2 = ((TDAKernel)raapi).replicateEventOrCommand(v.tolong());
			System.out.println("lua replicate2 "+r2);*/
			
			long r2 = v.tolong();
			
			//boolean retVal = raapi.createLink(r2, rSubmitterObj, rCommandSubmitterAssoc);
			//return LuaValue.valueOf(retVal);
			
			TDAKernel.Owner o = ((TDAKernel)raapi).getOwner();
			
			long it = raapi.getIteratorForDirectObjectClasses(r2);
			long rCls = raapi.resolveIteratorFirst(it);		
			String className = raapi.getClassName(rCls);
			raapi.freeReference(rCls);
			raapi.freeIterator(it);
			
			if (className == null)
				return LuaValue.valueOf(false);
			
			if (className.equals("ExecTransfCmd")) {		//do we need it???		
				// executing now...
			
				long rAttr = raapi.findAttribute(rCls, "info");
				String location = raapi.getAttributeValue(r2, rAttr);
				
				if (location == null)
					return LuaValue.valueOf(false);	
				
				if (location.startsWith("lua_engine#"))
					location = location.substring("lua_engine#".length());
				
				String className2 = location.substring(0, location.lastIndexOf('.'));
				String funcName = location.substring(location.lastIndexOf('.')+1);
				String moduleName = className2;
				if (moduleName.lastIndexOf('.')>=0)
					moduleName = moduleName.substring(moduleName.lastIndexOf('.')+1);
						
				
				if (!funcName.endsWith("()")) {
					funcName += "()";
				}
				
				String code = moduleName+" = require(\""+className2+"\")\n"+moduleName+"."+funcName;
				if (true || logger.isDebugEnabled())
					code = "print(\"EXECTRANSF BEFORE REQUIRE "+className2+"\")\n"+moduleName+" = require(\""+className2+"\")\nprint(\"EXECTransf BEFORE LUA CODE "+moduleName+"."+funcName+"\")\n\n"+moduleName+"."+funcName+"\nprint(\"AFTER LUA CODE\")";
				try {
					LuaValue luaLoad = globals.get("loadstring");
					LuaValue luaCompiledCode = luaLoad.call(LuaValue.valueOf(code));
					luaCompiledCode.call();			
				}
				catch (Throwable t) {
					logger.error("Lua exception: "+t+"\nStackTrace="+StackTrace.get(t)+"\ncode=["+code+"]");
				}
				
				return LuaValue.valueOf(true);				
			}
			
			if (className.equals("D#Command") && skipCommand(r2, rCls))
				return LuaValue.valueOf(true);
			if (className.equals("OkCmd") && skipCommand(r2, rCls))
				return LuaValue.valueOf(true);

			
			IWebCaller.WebCallSeed seed = new IWebCaller.WebCallSeed();
			
			logger.debug("lua invokeNow "+className+" r="+r2);
			seed.actionName = className;
			
			seed.callingConventions = WebCaller.CallingConventions.TDACALL;
			seed.tdaArgument = r2;			
	
			if (o!=null) {
				seed.login = o.login;
				seed.project_id = o.project_id;
			}
	  		
	  		/*API.webCaller.enqueue(seed);*/
			
			API.webCaller.invokeNow(seed);
			
			return LuaValue.valueOf(true);
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
		synchronized public final LuaValue call() {
		return call(NIL, NIL, NIL, NIL, NIL);
		}

		@Override
		synchronized public final LuaValue call(LuaValue arg) {
		return call(arg, NIL, NIL, NIL, NIL);
		}

		@Override
		synchronized public LuaValue call(LuaValue arg1, LuaValue arg2) {
		return call(arg1, arg2, NIL, NIL, NIL);
		}

		@Override
		synchronized public LuaValue call(LuaValue arg1, LuaValue arg2, LuaValue arg3) {
		return call(arg1, arg2, arg3, NIL, NIL);
		}

		synchronized public LuaValue call(LuaValue arg1, LuaValue arg2, LuaValue arg3, LuaValue arg4) {
		return call(arg1, arg2, arg3, arg4, NIL);
		}

		@Override
		synchronized public Varargs invoke(Varargs varargs) {
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
		synchronized public Varargs invoke(Varargs v) {
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
					  		
					  		logger.debug("seed: "+seed.actionName+" "+seed.jsonArgument);
					  		
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
	

	class BrowseForFolder extends TwoArgFunction {
		// returns NIL if continued at the client-side
		
		@Override
		synchronized public LuaValue call(LuaValue _caption, LuaValue _startFolder) {
			if (API.config.inWebMode) {			
				File fs = new File(API.dataMemory.getProjectFolder(project_id)+File.separator+"selected.browseForFolder");
				File fc = new File(API.dataMemory.getProjectFolder(project_id)+File.separator+"cancelled.browseForFolder"); 
				if (fs.exists()) {
					String s = "";				
					try { 
						s = FileUtils.readFileToString(fs, "UTF-8");
					}
					catch(Throwable t) {								
					}
					fs.delete();
					if (fc.exists())
						fc.delete();
					
					if ("/home".equals(s))
						s="";
					
					try {
						ValidityChecker.checkRelativePath(s, true);
					} catch (Exception e) {
						LuaValue.valueOf(""); // cancelled (wrong path specified)
					}
					
					s = API.config.HOME_DIR+File.separator+login+File.separator+s;
					logger.debug("BrowseForFolder is returning ["+s+"]"); 
					return LuaValue.valueOf(s);
				}
				else {
					if (fc.exists()) {
						fc.delete();
						return LuaValue.valueOf(""); // cancelled
					}
					
					WebCaller.WebCallSeed seed = new WebCaller.WebCallSeed();
					
					seed.actionName = "browseForFolder";
					
					seed.callingConventions = WebCaller.CallingConventions.JSONCALL;
					seed.jsonArgument = "{\"caption\":\""+_caption.tojstring()+"\"}"; // _startFolder will be ignored; we will start from the user's home directory
					
					seed.login = login;
					seed.project_id = project_id;
			  		
			  		API.webCaller.enqueue(seed);
					logger.debug("lua_tda.BrowseForFolder: the folder has not been chosen yet, the client has to show the choser dialog and re-launch our transformation after the folder is chosen");
					return LuaValue.NIL; // continued at the client-side
				}
			
			}
			else {
				final JFileChooser fc = new JFileChooser();

				fc.setCurrentDirectory(new File(_startFolder.tojstring()));
				fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				
				String retVal = "";
				if (fc.showDialog(null, "Choose!") == JFileChooser.APPROVE_OPTION) {
						retVal = fc.getSelectedFile().getAbsolutePath();
				}
				
				logger.debug("BrowseForFolder retVal="+retVal);
				
				return LuaValue.valueOf(retVal);
				
			}
		}

	}

	class BrowseForFolderAsList extends TwoArgFunction {
		
		private String collectRecursively(File where, String prefix, ArrayList<String> arr) {
			for (File f: where.listFiles()) {
				if (!f.isDirectory())
					continue;
				if (f.getName().startsWith("."))
					continue;
				
				if (prefix.length()==0) {
					arr.add(f.getName());
					collectRecursively(f, f.getName(), arr);
				}
				else {
					arr.add(prefix+"/"+f.getName());
					collectRecursively(f, prefix+"/"+f.getName(), arr);
				}
			}
			return null;
		}
		
		@Override
		synchronized public LuaValue call(LuaValue _caption, LuaValue _startFolder) {
//test:			_startFolder = LuaValue.valueOf("D:\\webappos.org\\webappos\\home\\sk\\test_ar.owlgred_jr");
			if (API.config.inWebMode) {
				File fs = new File(API.dataMemory.getProjectFolder(project_id)+File.separator+"selected.browseForFolderAsList");
				File fc = new File(API.dataMemory.getProjectFolder(project_id)+File.separator+"cancelled.browseForFolderAsList");
				if (fs.exists()) {
					String s = "";				
					try { 
						s = FileUtils.readFileToString(fs, "UTF-8");
					}
					catch(Throwable t) {								
					}
					fs.delete();
					if (fc.exists())
						fc.delete();
					
					try {
						ValidityChecker.checkRelativePath(s, false);
					} catch (Exception e) {
						return LuaValue.valueOf("");
					}
					
					return LuaValue.valueOf(_startFolder.tojstring()+File.separator+s);
				}
				else {
					if (fc.exists()) {
						fc.delete();
						return LuaValue.valueOf(""); // cancelled
					}
					
					WebCaller.WebCallSeed seed = new WebCaller.WebCallSeed();
					
					seed.actionName = "browseForFolderAsList";
					
					seed.callingConventions = WebCaller.CallingConventions.JSONCALL;
					ArrayList<String> arr = new ArrayList<String>();
					seed.jsonArgument = "{\"caption\":\""+_caption.tojstring()+"\", \"items\":[";
					collectRecursively(new File(_startFolder.tojstring()), "", arr);
					// collect recursively and add to items
					for (int i=0; i<arr.size(); i++) {
						if (i==arr.size()-1)
							seed.jsonArgument+="\""+arr.get(i)+"\"";
						else
							seed.jsonArgument+="\""+arr.get(i)+"\", ";
					}
					seed.jsonArgument += "]}";
					
					seed.login = login;
					seed.project_id = project_id;
			  		
			  		API.webCaller.enqueue(seed);
					logger.debug("lua_tda.BrowseForFolderAsList: the folder (list item) has not been chosen yet, the client has to show the choser dialog and re-launch our transformation after the folder is chosen");

					return LuaValue.NIL; // continued at the client-side
				}
								
			}
			else {
				final JFileChooser fc = new JFileChooser();

				fc.setCurrentDirectory(new File(_startFolder.tojstring()));
				fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				
				String retVal = "";
				if (fc.showDialog(null, "Choose!") == JFileChooser.APPROVE_OPTION) {
						retVal = fc.getSelectedFile().getAbsolutePath();
				}
				
				logger.debug("BrowseForFolderAsList retVal="+retVal);
				
				return LuaValue.valueOf(retVal);
				
			}
		}

	}

	public class CallFunctionWithPleaseWaitWindow extends TwoArgFunction {		

		@Override
		synchronized public LuaValue call(LuaValue luaFunction, LuaValue luaArg) {
			
/*			WebCaller.WebCallSeed seed = new WebCaller.WebCallSeed();			
			seed.actionName = "showPleaseWait";			
			seed.callingConventions = WebCaller.CallingConventions.JSONCALL;
			seed.jsonArgument = "{\"message\":\"Please, wait\"}";			
			seed.login = login;
			seed.project_id = project_id;	  		
	  		API.webCaller.enqueue(seed);*/

			WebCaller.WebCallSeed seed = new WebCaller.WebCallSeed();			
			seed.actionName = "showPleaseWait";			
			seed.callingConventions = WebCaller.CallingConventions.JSONCALL;
			seed.jsonArgument = "{}";			
			seed.login = login;
			seed.project_id = project_id;	  		
	  		API.webCaller.invokeNow(seed);

			
	  		/*TDAKernel kernel = API.dataMemory.getTDAKernel(project_id);
	  		kernel.getSynchronizer().syncRawAction(new double[] {0xC0, 0}, "showPleaseWait/{}");*/
	  		
			LuaValue luaLoad = globals.get("loadstring");
			LuaValue luaCompiledCode = luaLoad.call(LuaValue.valueOf(luaFunction+"([["+luaArg.tojstring()+"]])"));
						
			LuaValue retVal = luaCompiledCode.call();

/*			seed = new WebCaller.WebCallSeed();			
			seed.actionName = "hidePleaseWait";			
			seed.callingConventions = WebCaller.CallingConventions.JSONCALL;
			seed.login = login;
			seed.project_id = project_id;	  		
	  		API.webCaller.enqueue(seed);*/
			
			seed = new WebCaller.WebCallSeed();			
			seed.actionName = "hidePleaseWait";			
			seed.callingConventions = WebCaller.CallingConventions.JSONCALL;
			seed.login = login;
			seed.jsonArgument="{}";
			seed.project_id = project_id;	  		
	  		API.webCaller.invokeNow(seed);
			
	  		//kernel.getSynchronizer().syncRawAction(new double[] {0xC0, 0}, "hidePleaseWait/{}");
			
			return retVal;
		}

	}
	
	
	public class SetProgressMessage extends OneArgFunction {		

		@Override
		synchronized public LuaValue call(LuaValue msg) {
			System.err.println(msg.tojstring());
/*			WebCaller.WebCallSeed seed = new WebCaller.WebCallSeed();			
			seed.actionName = "showPleaseWait";			
			seed.callingConventions = WebCaller.CallingConventions.JSONCALL;
			seed.jsonArgument = "{\"message\":\""+msg.tojstring()+"\"}";			
			seed.login = login;
			seed.project_id = project_id;	  		
	  		API.webCaller.enqueue(seed);*/
			
	  		/*TDAKernel kernel = API.dataMemory.getTDAKernel(project_id);
	  		
			String s = msg.tojstring();
			if ((s == null) || (s.isEmpty())) {
		  		kernel.getSynchronizer().syncRawAction(new double[] {0xC0, 0}, "hidePleaseWait/{}");				
			}
			else {	  		
				kernel.getSynchronizer().syncRawAction(new double[] {0xC0, 0}, "showPleaseWait/{\"message\":\""+msg.tojstring()+"\"}");
			}*/
			
			WebCaller.WebCallSeed seed = new WebCaller.WebCallSeed();			
			seed.actionName = "showPleaseWait";			
			seed.callingConventions = WebCaller.CallingConventions.JSONCALL;
			seed.login = login;
			seed.project_id = project_id;	  		
	  		
			String s = msg.tojstring();
			if ((s == null) || (s.isEmpty()))
				seed.jsonArgument="{}";
			else
				seed.jsonArgument="{\"message\":\""+msg.tojstring()+"\"}";
				
	  		API.webCaller.invokeNow(seed);
			return LuaValue.NONE;
		}

	}

	public class SetProgressBar extends OneArgFunction {		

		@Override
		synchronized public LuaValue call(LuaValue val) {
			System.err.println(val.toint());
			/*WebCaller.WebCallSeed seed = new WebCaller.WebCallSeed();			
			seed.actionName = "showPleaseWait";			
			seed.callingConventions = WebCaller.CallingConventions.JSONCALL;
			seed.jsonArgument = "{\"percentage\":\""+val.toint()+"\"}";			
			seed.login = login;
			seed.project_id = project_id;	  		
	  		API.webCaller.enqueue(seed);*/
			
	  		/*TDAKernel kernel = API.dataMemory.getTDAKernel(project_id);
	  		kernel.getSynchronizer().syncRawAction(new double[] {0xC0, 0}, "showPleaseWait/{\"percentage\":"+val.toint()+"}");*/
			
			WebCaller.WebCallSeed seed = new WebCaller.WebCallSeed();			
			seed.actionName = "showPleaseWait";			
			seed.callingConventions = WebCaller.CallingConventions.JSONCALL;
			seed.jsonArgument = "{\"percentage\":"+val.toint()+"}";			
			seed.login = login;
			seed.project_id = project_id;	  		
	  		API.webCaller.invokeNow(seed);
			
			return LuaValue.NONE;
		}

	}
	
	public class BringTDAToForground extends ZeroArgFunction {		

		@Override
		synchronized public LuaValue call() {
			return LuaValue.NONE;
		}

	}
	
	public class CopyFile extends ThreeArgFunction {		

		@Override
		synchronized public LuaValue call(LuaValue src_path, LuaValue target_path, LuaValue fail_on_exists) {
			
			try {
				Files.copy(Paths.get(src_path.tojstring()), Paths.get(target_path.tojstring()), fail_on_exists.toboolean()?StandardCopyOption.COPY_ATTRIBUTES:StandardCopyOption.REPLACE_EXISTING);
				return LuaValue.TRUE;
			}
			catch(Throwable t) {
				return LuaValue.FALSE;
			}
		}

	}
	
	public class DeleteFileOrFolder extends OneArgFunction {		

		@Override
		synchronized public LuaValue call(LuaValue path) {
			File f = new File(path.tojstring());
			if (!f.exists())
				return LuaValue.FALSE;
			if (f.isFile())
				return LuaValue.valueOf( f.delete() );
			
			if (f.isDirectory()) {
				try {
					FileUtils.deleteDirectory(f);
					return LuaValue.TRUE;					
				}
				catch(Throwable t)	{
					return LuaValue.FALSE;
				}
			}
			
			return LuaValue.FALSE;
		}

	}
	
}
