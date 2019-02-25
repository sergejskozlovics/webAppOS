package org.webappos.status;

import java.rmi.RemoteException;

public class IStatusWrapper implements IStatus {
	
	private IRStatus delegate;
	
	public IStatusWrapper(IRStatus _delegate) {
		delegate = _delegate;
	}

	public void setStatus(String key, String value) {
		try {
			delegate.setStatus_R(key, value);
		} catch (RemoteException e) {
		}
	}

	public void setStatus(String key, String value, long expireSeconds) {
		try {
			delegate.setStatus_R(key, value, expireSeconds);
		} catch (RemoteException e) {
		}
	}


}
