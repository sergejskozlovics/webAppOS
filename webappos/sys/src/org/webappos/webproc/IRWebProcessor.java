package org.webappos.webproc;

import java.rmi.Remote;
import java.rmi.RemoteException;

import org.webappos.webcaller.IWebCaller;

/**
 * IRWebProcessor API has to be implemented by webAppOS web processors.
 * The webAppOS server-side bridge will use IRWebProcessor to delegate
 * web calls to web processors. The communication channel is called Web Processor Bus.
 * 
 * @author Sergejs Kozlovics
 *
 */
public interface IRWebProcessor extends Remote {
	/**
	 * Asks the web processor whether it supports the given instruction set.
	 *
	 * @param name the name of the instruction set
	 * @return whether the given web processor supports the given instruction set
	 * @throws RemoteException on Web Processor Bus error
	 */
	public boolean hasInstructionSet(String name) throws RemoteException;
	
	/**
	 * Asks the web processor can create some per-project cache for the given instruction set.
	 * If yes, the web caller will constantly use the same web processor, if web calls for the same
	 * project_id and instruction set are submitted.
	 *
	 * @param name the name of the instruction set
	 * @return whether the given web processor can create some cache for the given instruction set
	 * @throws RemoteException on Web Processor Bus error
	 */
	public boolean perProjectCachedInstructionSet(String name) throws RemoteException;
	
	/**
	 * Starts (asynchronously) the given webcall corresponding to the given webcall declaration.
	 * The startWebCall must return immediately, thus, the caller (WebCaller from the web server bridge) can continue
	 * serving other webcalls.
	 * After the webcall is completed, the web processor must inform the Web Processor Bus Service
	 * about the completion by calling webProcessorIdle. 
	 * @param seed the call seed containing the information about the call (action name, calling conventions, etc.)
	 * @param declaration the webcall declaration (instructions set, resolved code location, etc.)
	 * @throws RemoteException on Web Processor Bus error
	 */
	public void startWebCall(IWebCaller.WebCallSeed seed, IWebCaller.WebCallDeclaration declaration) throws RemoteException;
	
	/**
	 * Informs the web processor that it is being disconnected. The web processor must terminate
	 * the web call execution requested earlier via startWebCall (if any in progress). <p>
	 * 
	 * This function is intended to be called from the web processor adapter, which was used to connect to this web processor.
	 * Since the connection will be lost, disconnect() is expected to throw an exception.
	 * 
	 * @throws RemoteException on Web Processor Bus error
	 */
	public void disconnect() throws RemoteException; 
}
