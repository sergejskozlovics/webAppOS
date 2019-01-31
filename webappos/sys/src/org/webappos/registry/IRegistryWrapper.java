package org.webappos.registry;

import java.rmi.RemoteException;

import com.google.gson.JsonElement;

public class IRegistryWrapper implements IRegistry {
	private IRRegistry delegate;
	
	public IRegistryWrapper(IRRegistry _delegate) {
		delegate = _delegate;
	}
	public String getUserLogin(String emailOrLogin) {
		try {
			return delegate.getUserLogin_R(emailOrLogin);
		} catch (RemoteException e) {
			return null;
		}
	}
	public JsonElement getValue(String key) {
		try {
			return delegate.getValue_R(key);
		} catch (RemoteException e) {
			return null;

		}
	}
	public boolean setValue(String key, Object value) {
		try {
			return delegate.setValue_R(key, value);
		} catch (RemoteException e) {
			return false;
		}
	}

}
