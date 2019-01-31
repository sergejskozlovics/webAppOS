package org.webappos.adapters.service.webroot;

import java.io.File;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.webapp.WebAppContext;
import org.webappos.properties.ServiceProperties;
import org.webappos.server.IServiceAdapter;

public class ServiceAdapter implements IServiceAdapter {
	
	private Runnable onStopped = null;

	@Override
	public ContextHandler attachService(ServiceProperties svcProps, String path, Runnable onStopped, Runnable onHalted) {
		

		WebAppContext appContext = new WebAppContext();
		appContext.setWar(svcProps.service_dir+File.separator+"web-root");
		appContext.setContextPath(path);		
		
/*		ArrayList<String> arr = new ArrayList<String>();
		
		arr.add( svcProps.service_dir+File.separator+"web-root" ); // must always present, then other folders in the search path follow
		arr.add( Config.WEB_ROOT_DIR );
		
		ResourceCollection resources = new ResourceCollection(arr.toArray(new String[] {}));
		appContext.setBaseResource(resources);*/
		
		DefaultServlet servlet = new DefaultServlet();
		
		// NO CACHE
		ServletHolder holder = new ServletHolder(servlet);
		holder.setInitParameter("useFileMappedBuffer", "false");
		holder.setInitParameter("cacheControl", "max-age=0, public");
		appContext.addServlet(holder, "/");
		
		this.onStopped = onStopped;
		
		return appContext;
	}

	@Override
	public void stopService() {
		if (this.onStopped != null) {
			Runnable tmp = this.onStopped;
			this.onStopped = null;
			tmp.run();			
		}
	}

}
