package org.webappos.server;

import org.eclipse.jetty.server.handler.ContextHandler;
import org.webappos.properties.ServiceProperties;

public interface IServiceAdapter {
	
	/**
	 * Initializes the corresponding service.
	 * @param svcProps the contents of the service.properties file
	 * @param path the URL path to bind with this service
	 * @param onStopped the function to be called when the service was terminated via stopService call (onStopped can be null)
	 * @param onHalted the function to be called when the service halted (e.g., the process died); onHalted can be null
	 * @return a context handler to be embedded within the webAppOS server or null, if the service has been launched by its own;
	 * on error, throws some exception
	 */
	public ContextHandler attachService(ServiceProperties svcProps, String path, Runnable onStopped, Runnable onHalted);
	
	/**
	 * Stops the corresponding service (if not stopped yet).
	 * The function must wait for the service to stop (or forcefully terminate the service, if waiting was too long).
	 */
	public void stopService();

}
