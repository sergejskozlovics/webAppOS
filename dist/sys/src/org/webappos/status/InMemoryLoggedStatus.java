package org.webappos.status;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import org.webappos.server.ConfigStatic;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class InMemoryLoggedStatus extends UnicastRemoteObject implements IStatus, IRStatus {

	private static final long serialVersionUID = 1L;
	private static PrintWriter w = null;
	
	private static JsonObject obj = new JsonObject();
	private static JsonParser parser = new JsonParser();
	
	public InMemoryLoggedStatus() throws RemoteException {
		super();
		new File(ConfigStatic.VAR_DIR).mkdirs();
		//File f = new File(Config.VAR_DIR+File.separator+"status-"+(new java.util.Date()).toString().replaceAll(":", "_")+".txt");
		File f = new File(ConfigStatic.VAR_DIR+File.separator+"status.log");
		try {
			w = new PrintWriter(f);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public boolean setValue(String key, Object value) {
		if ((key==null) || (key.isEmpty()))
			return false;
		
		JsonObject o = obj;
		JsonElement el;
		String[] arr = key.split("/");
		for (int i=0; i<arr.length-1; i++) {
			el = null;
			el = o.get(arr[i]);
			if (el instanceof JsonObject)
				o = (JsonObject)el;
			else {
				o.remove(arr[i]);
				JsonObject child = new JsonObject();
				o.add(arr[i], child);				
				o = child;
			}
		}
		
		o.remove(arr[arr.length-1]);		
		if (value!=null) {
			if (value instanceof JsonElement)
				o.add(arr[arr.length-1], (JsonElement)value);
			else {
				try {
					el = parser.parse(value.toString());
					o.add(arr[arr.length-1], el);
				}
				catch(Throwable t) {
					if (value instanceof Boolean)
						o.addProperty(arr[arr.length-1], ((Boolean) value).booleanValue());
					else
						if (value instanceof Integer)
							o.addProperty(arr[arr.length-1], ((Number) value).intValue());
						else
							o.addProperty(arr[arr.length-1], value.toString());
							
				}
			}
		}
		
		if (w!=null) {
			if (value==null)
				w.println(key+"=");
			else
				w.println(key+"="+value.toString());
			w.flush();
		}
		
		return true;
	}
	
	@Override
	public JsonElement getValue(String key) {
		if ((key==null) || (key.isEmpty()))
			return obj;
		
		JsonElement el = obj;
		String[] arr = key.split("/");
		for (int i=0; i<arr.length; i++) {
			if (el instanceof JsonObject)
				el = ((JsonObject)el).get(arr[i]);
			else {
				return null;
			}
		}
		
		return el;
	}

	@Override
	public boolean setValue_R(String key, Object value) throws RemoteException {
		return setValue(key, value);		
	}

	@Override
	public JsonElement getValue_R(String key) throws RemoteException {
		return getValue(key);		
	}
	
}
