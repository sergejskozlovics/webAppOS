package org.webappos.webcaller;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Map;

import org.webappos.webcaller.IWebCaller.WebCallDeclaration;
import org.webappos.webcaller.IWebCaller.WebCallSeed;

public interface IRWebCaller extends Remote {	
	public void enqueue_R(final WebCallSeed seed) throws RemoteException;
	public boolean webCallExists_R(String actionName) throws RemoteException;
	public WebCallDeclaration getWebCallDeclaration_R(String actionName) throws RemoteException;
	public Map<String, WebCallDeclaration> getWebCalls_R(String fullAppName) throws RemoteException;
}
