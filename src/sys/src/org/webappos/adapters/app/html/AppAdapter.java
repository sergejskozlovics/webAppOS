package org.webappos.adapters.app.html;

import java.io.File;
import java.util.ArrayList;

import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.resource.ResourceCollection;
import org.eclipse.jetty.webapp.WebAppContext;
import org.webappos.properties.AppProperties;
import org.webappos.server.ConfigStatic;
import org.webappos.server.IAppAdapter;

public class AppAdapter implements IAppAdapter {

	@Override
	public ContextHandler attachApp(AppProperties appProps) {
		
		WebAppContext appContext = new WebAppContext();
		appContext.setWar(ConfigStatic.APPS_DIR+File.separator+appProps.app_full_name+File.separator+"web-root");
		appContext.setContextPath("/");		
		appContext.getMimeTypes().addMimeMapping("mjs", "application/javascript");
		
		ArrayList<String> arr = new ArrayList<String>();
		
		arr.add( ConfigStatic.APPS_DIR+File.separator+appProps.app_full_name+File.separator+"web-root" ); // must always present, then other folders in the search path follow
		arr.add( ConfigStatic.WEB_ROOT_DIR );
		for (int i=0; i<appProps.requires_engines.length; i++) {
			String dir = ConfigStatic.ENGINES_DIR+File.separator+appProps.requires_engines[i]+".web"+File.separator+"web-root"; 
			if (new File(dir).isDirectory()) 
				arr.add(dir);
			
			
		}
		
		arr.add( ConfigStatic.WEB_ROOT_CACHE_DIR);
		
		ResourceCollection resources = new ResourceCollection(arr.toArray(new String[] {}));
		appContext.setBaseResource(resources);
		
		DefaultServlet servlet = new DefaultServlet();
		
		// NO CACHE
		ServletHolder holder = new ServletHolder(servlet);
		holder.setInitParameter("useFileMappedBuffer", "false");
		holder.setInitParameter("cacheControl", "max-age=0, public");
		appContext.addServlet(holder, "/");
		
		
		// CORS ALLOW (NOT WORKING)
/*		FilterHolder filterHolder = new FilterHolder(CrossOriginFilter.class);
	    filterHolder.setInitParameter("allowedOrigins", "*");
	    filterHolder.setInitParameter("allowedMethods", "GET, POST");
	    appContext.addFilter(filterHolder, "*", null);*/
		
/*		FilterHolder cors = appContext.addFilter(CrossOriginFilter.class,"*",EnumSet.of(DispatcherType.REQUEST));
		cors.setInitParameter(CrossOriginFilter.ALLOWED_ORIGINS_PARAM, "*");
		cors.setInitParameter(CrossOriginFilter.ACCESS_CONTROL_ALLOW_ORIGIN_HEADER, "*");
		cors.setInitParameter(CrossOriginFilter.ALLOWED_METHODS_PARAM, "GET,POST,DELETE,PUT,HEAD");
		cors.setInitParameter(CrossOriginFilter.ALLOWED_HEADERS_PARAM, "X-Requested-With,Content-Type,Accept,Origin");*/
		
		return appContext;
	}


}
