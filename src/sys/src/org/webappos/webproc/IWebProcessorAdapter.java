package org.webappos.webproc;

public interface IWebProcessorAdapter {
	/**
	 * Launches (local) or connects (remote) a web processor.
	 * 
	 * The adapter must register the underlying web processor by calling registerWebProcessor within Web Processor Bus Service.
	 *  
	 * @param location the adapter-specific location of the web processor code; for the "local" adapter, contains a Java class name;
	 * @param id the web processor ID provided by the server-side bridge; the underlying web processor must use this ID,
	 *        when communicating with the bridge via Web Processor Bus Service
	 * @param options additional (optional) options specified in webproctab
	 */
	public void connect(String location, String id, String options);	
	
	/**
	 * Disconnects the underlying web processor.
	 * 
	 * @param wpAPI a pointer to the corresponding IRWebProcessor implementation, which can be used
	 * while disconnecting (e.g., to terminate a running process)
	 */
	public void disconnect(IRWebProcessor wpAPI); // on shutdown or by request; must terminate the current execution
}
