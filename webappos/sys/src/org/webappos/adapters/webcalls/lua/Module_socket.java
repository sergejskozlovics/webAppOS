package org.webappos.adapters.webcalls.lua;


import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;

import org.luaj.vm2.lib.TwoArgFunction;

public class Module_socket extends TwoArgFunction {
	
	public static Module_socket LIB = null;
	
    public Module_socket() {
        LIB = this;
    }

	@Override
	synchronized public LuaValue call(LuaValue modName, LuaValue env) {
		LuaTable module = new LuaTable(0,30); // I think "new LuaTable()" instead of "(0, 30)" is OK
//        module.set("dir2", new dir2());
        return module;		
	}

	
/*	public class dir2 extends OneArgFunction {		

		@Override
		synchronized public LuaValue call(LuaValue path) {
			File f = new File(path.tojstring());			
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

	}*/
	
}
