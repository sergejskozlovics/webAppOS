package org.webappos.webproc;

import java.rmi.Remote;
import java.rmi.RemoteException;

import org.webappos.memory.IRMRAM;
import org.webappos.properties.IRPropertiesManager;
import org.webappos.registry.IRRegistry;
import org.webappos.server.ConfigStatic;
import org.webappos.status.IRStatus;
import org.webappos.webcaller.IRWebCaller;

public interface IRWebProcessorBusService extends Remote {
	
	public void registerWebProcessor(String webProcId, IRWebProcessor wpAPI) throws RemoteException;
	public void webCallFinished(String webProcId, String webcallResult) throws RemoteException;
	
	public ConfigStatic getConfig() throws RemoteException;
	public IRPropertiesManager getPropertiesManager() throws RemoteException;	
    public IRMRAM getMemory() throws RemoteException;
    public IRRegistry getRegistry() throws RemoteException;
    public IRStatus getStatus() throws RemoteException;
    public IRWebCaller getWebCaller() throws RemoteException;
}
