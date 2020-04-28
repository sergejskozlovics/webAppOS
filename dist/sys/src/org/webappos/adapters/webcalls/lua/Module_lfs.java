package org.webappos.adapters.webcalls.lua;

import java.nio.file.Files;
import java.io.File;
import java.io.IOException;

import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;
import org.luaj.vm2.lib.ZeroArgFunction;

import lv.lumii.tda.raapi.RAAPI;

import org.luaj.vm2.lib.LibFunction;
import org.luaj.vm2.lib.OneArgFunction;
import org.luaj.vm2.lib.TwoArgFunction;
import org.luaj.vm2.lib.VarArgFunction;

public class Module_lfs extends TwoArgFunction {
	
	public static Module_lfs LIB = null;
	
    public Module_lfs() {
        LIB = this;
    }

	@Override
	synchronized public LuaValue call(LuaValue modName, LuaValue env) {
		LuaTable module = new LuaTable(0,30); // I think "new LuaTable()" instead of "(0, 30)" is OK
        module.set("dir2", new dir2());
        module.set("mkdir", new mkdir());
        module.set("attributes", new attributes());
        return module;		
	}

	
	public class dir2 extends OneArgFunction {		

		@Override
		synchronized public LuaValue call(LuaValue path) {
			File f = new File(path.tojstring().replace('\\', File.separatorChar).replace('/', File.separatorChar));			
			LuaTable t = new LuaTable();
			int i =1;
			
			String[] arr = f.list();
			if (arr != null) {
			
				for (String s : arr) {
					t.set(i, s);
					i++;
				}
			}
			
			return t;
		}

	}
	
	public class mkdir extends OneArgFunction {		

		@Override
		synchronized public LuaValue call(LuaValue dirname) {
			boolean retval = false;
			File f = new File(dirname.tojstring().replace('\\', File.separatorChar).replace('/', File.separatorChar));
			try {
				Files.createDirectory(f.toPath());
				retval = true;
			} catch (IOException e) {
			}
			return LuaValue.valueOf(retval);
		}

	}
	
	public class attributes extends VarArgFunction {
		
		synchronized public Varargs invoke(Varargs v) {
			LuaValue filepath = v.arg(1);
			LuaValue aname = v.arg(2);
			
			File f = new File(filepath.tojstring().replace('\\', File.separatorChar).replace('/', File.separatorChar));
			
			if ((aname == null) || aname.isnil()) {				
				LuaTable t = new LuaTable();
				t.set("mode", f.isDirectory()?"directory":(f.isFile()?"file":null));
				return LuaValue.varargsOf(new LuaValue[] { t });
			}
			else {				
				LuaValue val;
				LuaValue err = LuaValue.NIL;
				
				String s = aname.tojstring();
				if ("mode".equals(s)) {
					if (f.isDirectory())
						val = LuaValue.valueOf("directory");
					else
					if (f.isFile())
						val = LuaValue.valueOf("file");
					else {
						val = LuaValue.NIL;
						err = LuaValue.valueOf("Neither file, nor directory.");
					}
				}
				else {
					val = LuaValue.NIL;
					err = LuaValue.valueOf("Currently only mode is supported.");					
				}
				
				return LuaValue.varargsOf(new LuaValue[] {val, err}); 						
			}
			
		}

	}

}
