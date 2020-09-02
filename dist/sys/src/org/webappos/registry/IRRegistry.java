package org.webappos.registry;

import java.lang.reflect.Method;
import java.rmi.Remote;
import java.rmi.RemoteException;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

public interface IRRegistry extends Remote {
	//public String getValue_R(String key) throws RemoteException;	
	//public boolean setValue_R(String key, String stringifiedValue) throws RemoteException;
	
	
	default public String getValue_R(String key) throws RemoteException {
		
		try {
		Method m = this.getClass().getMethod("getValue", String.class);
		JsonElement el = (JsonElement)m.invoke(this, key);		
		if (el==null)
			return null;
		return el.toString();
		}
		catch(Throwable t) {
			return null;
		}
	}


	default public boolean setValue_R(String key, String stringifiedValue) throws RemoteException {
		
        try {
		Method m = this.getClass().getMethod("setValue", String.class, Object.class);
		
		if (stringifiedValue==null)
			return (boolean) m.invoke(this, key, null);
			
		else {
            JsonParser parser = new JsonParser();
            try {
            	JsonElement jsonElement = parser.parse(stringifiedValue);
            	return (boolean) m.invoke(this, key, jsonElement);
            }
            catch(Throwable t) {
            	// assume a parse error; passing value as string
            	return (boolean) m.invoke(this, key, stringifiedValue);
            }
		}
        }
        catch(Throwable t) {
        	return false;
        }
	}
	
}
