package org.webappos.registry;

import java.rmi.Remote;
import java.rmi.RemoteException;

import com.google.gson.JsonElement;

public interface IRRegistry extends Remote {
	public String getUserLogin_R(String emailOrLogin) throws RemoteException;
	public JsonElement getValue_R(String key) throws RemoteException;	
	public boolean setValue_R(String key, Object value) throws RemoteException;
}
