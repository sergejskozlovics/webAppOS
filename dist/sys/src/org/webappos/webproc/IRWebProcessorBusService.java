package org.webappos.webproc;

import java.rmi.Remote;
import java.rmi.RemoteException;

import org.webappos.properties.IRPropertiesManager;
import org.webappos.registry.IRRegistry;
import org.webappos.server.ConfigStatic;
import org.webappos.status.IRStatus;
import org.webappos.webcaller.IRWebCaller;
import org.webappos.webmem.IRWebMemoryArea;

/**
 * Used by server-side and remote server-side web processors to register
 * themselves within the server-side bridge as well as to obtain pointers
 * to server-side API.
 * <br>
 * Implemented using Java RMI.
 * 
 * @author Sergejs Kozlovics
 *
 */
public interface IRWebProcessorBusService extends Remote {
	
	/**
	 * Called by web processors to register themselves within Web Processor Bus Service.
	 * @param webProcId the ID of the current web processor that wants to be registered;
	 *        this ID is passed to a web processor adapter by Web Processor Bus Service
	 *        when connecting to a web processor
	 * @param wpAPI a callback pointer to the current web processor implementing {@link IRWebProcessor} 
	 * @throws RemoteException on Web Processor Bus error
	 */
	public void registerWebProcessor(String webProcId, IRWebProcessor wpAPI) throws RemoteException;
	
	/**
	 * Called by each web processor after completing a web call to return the web call result to server-side Web Caller.
	 * After webCallFinished, Web Caller assumes that the current web processor is now available to execute other web calls.
	 * @param webProcId the ID of the current web processor, which has just finished a web call 
	 * @param webcallResult the result of the web call (a stringified JSON in case of "jsoncall" calling conventions, or
	 * null in case of "tdacall")
	 * @throws RemoteException on Web Processor Bus error
	 */
	public void webCallFinished(String webProcId, String webcallResult) throws RemoteException;
	
	/**
	 * Called when initializing server-side API inside web processors to obtain current webAppOS configuration (loaded from webappos.properties).
	 * @return a local (web-processor-specific) copy of webAppOS configuration
	 * @throws RemoteException on Web Processor Bus error
	 */
	public ConfigStatic getConfig() throws RemoteException;
	/**
	 * Called when initializing server-side API inside web processors to obtain the properties manager
	 * for accessing apps, engines, and services properties.
	 * @return a pointer to the properties manager via RMI
	 * @throws RemoteException on Web Processor Bus error
	 */
	public IRPropertiesManager getPropertiesManager() throws RemoteException;	
	/**
	 * Called when initializing server-side API inside web processors to obtain a pointer to web memory area API.
	 * @return a pointer to web memory area API via RMI
	 * @throws RemoteException on Web Processor Bus error
	 */
    public IRWebMemoryArea getWebMemoryArea() throws RemoteException;
	/**
	 * Called when initializing server-side API inside web processors to obtain a pointer to Registry API.
	 * @return a pointer to Registry API via RMI
	 * @throws RemoteException on Web Processor Bus error
	 */
    public IRRegistry getRegistry() throws RemoteException;
	/**
	 * Called when initializing server-side API inside web processors to obtain a pointer to Status API.
	 * @return a pointer to Status API via RMI
	 * @throws RemoteException on Web Processor Bus error
	 */
    public IRStatus getStatus() throws RemoteException;
	/**
	 * Called when initializing server-side API inside web processors to obtain a pointer to Web Caller API to be able
	 * to make web calls.
	 * @return a pointer to Web Caller API via RMI
	 * @throws RemoteException on Web Processor Bus error
	 */
    public IRWebCaller getWebCaller() throws RemoteException;
}
