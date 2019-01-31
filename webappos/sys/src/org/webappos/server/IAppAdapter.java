package org.webappos.server;

import org.eclipse.jetty.server.handler.ContextHandler;
import org.webappos.properties.AppProperties;

public interface IAppAdapter {
	
	public ContextHandler attachApp(AppProperties appProps);

}
