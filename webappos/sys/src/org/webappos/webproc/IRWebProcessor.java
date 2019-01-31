package org.webappos.webproc;

import java.rmi.Remote;
import java.rmi.RemoteException;

import org.webappos.webcaller.IWebCaller;

public interface IRWebProcessor extends Remote {
	/**
	 * Asks the web processor whether it supports the given instruction set.
	 * @param name the name of the instruction set
	 * @return whether the given web processor supports the given instruction set
	 * @throws RemoteException
	 */
	public boolean hasInstructionSet(String name) throws RemoteException;
	
	/**
	 * Starts (asynchronously) the given webcall corresponding to the given webcall declaration.
	 * The startWebCall must return immediately, thus, the caller (WebCaller from the web server bridge) can continue
	 * serving other webcalls.
	 * After the webcall is completed, the web processor must inform the Web Processor Bus Service
	 * about the completion by calling webProcessorIdle. 
	 * @param seed the call seed containing the information about the call (action name, calling conventions, etc.)
	 * @param declaration the webcall declaration (instructions set, resolved code location, etc.)
	 * @throws RemoteException
	 */
	public void startWebCall(IWebCaller.WebCallSeed seed, IWebCaller.WebCallDeclaration declaration) throws RemoteException;
	
	/**
	 * Informs the web processor that it is being disconnected. The web processor must terminate
	 * the web call execution requested earlier via startWebCall (if any in progress). <p>
	 * 
	 * This function is intended to be called from the web processor adapter, which was used to connect to this web processor.
	 * Since the connection will be lost, disconnect() is expected to throw an exception.
	 * 
	 * @throws RemoteException
	 */
	public void disconnect() throws RemoteException; 
}
