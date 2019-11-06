package org.webappos.server;

import org.eclipse.jetty.server.handler.ContextHandler;
import org.webappos.properties.AppProperties;

public interface IAppAdapter {
	
	/**
	 * Initializes the corresponding app.
	 * @param appProps the contents of the app.properties file
	 * @return a context handler to be embedded within the webAppOS server or null, if the app has been launched by its own;
	 * on error, throws some exception
	 */
	public ContextHandler attachApp(AppProperties appProps);
}
