package org.webappos.status;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import com.google.gson.JsonElement;

public class NoStatus extends UnicastRemoteObject implements IStatus, IRStatus {

	private static final long serialVersionUID = 1L;
	
	public NoStatus() throws RemoteException {
		super();
	}
	
	@Override
	public boolean setValue(String key, Object value) {
		return true;
	}
	
	@Override
	public JsonElement getValue(String key) {
		return null;
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
