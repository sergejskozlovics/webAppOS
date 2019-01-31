package org.webappos.registry;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IRStatus extends Remote {
	public void setStatus_R(String key, String value) throws RemoteException;		
	public void setStatus_R(String key, String value, long expireSeconds) throws RemoteException;
}
