package org.webappos.status;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

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

}
