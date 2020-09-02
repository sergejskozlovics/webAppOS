package org.webappos.registry;

import java.rmi.RemoteException;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

public class IRegistryWrapper implements IRegistry {
	private IRRegistry delegate;
	
	public IRegistryWrapper(IRRegistry _delegate) {
		delegate = _delegate;
	}
	public JsonElement getValue(String key) {
		try {
			String stringifiedValue = delegate.getValue_R(key);
			if (stringifiedValue==null)
				return null;
			
            JsonParser parser = new JsonParser();
            return parser.parse(stringifiedValue);
    		
		} catch (RemoteException e) {
			return null;

		}
	}
	public boolean setValue(String key, Object value) {
		try {
			System.out.println("SET "+key+" -> "+value);
			return delegate.setValue_R(key, value.toString());
		} catch (RemoteException e) {
			e.printStackTrace();
			return false;
		}
	}

}
