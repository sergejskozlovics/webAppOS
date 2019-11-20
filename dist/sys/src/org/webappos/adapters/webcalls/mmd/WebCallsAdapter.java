package org.webappos.adapters.webcalls.mmd;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.webappos.properties.WebAppProperties;
import org.webappos.server.API;
import org.webappos.webcaller.IWebMemWebCallsAdapter;
import org.webappos.webmem.IWebMemory;

import lv.lumii.tda.kernel.mmdparser.MetamodelInserter;

public class WebCallsAdapter implements IWebMemWebCallsAdapter {
	private static Logger logger =  LoggerFactory.getLogger(WebCallsAdapter.class);
	
	
	@Override
	public void webmemcall(String location, String pwd, long rObject, IWebMemory raapi, String project_id, String appFullName,
			String login) {
		if (location == null)
			return;
		
		File f = new File(location);
		
		if (!f.exists())
			f = new File(pwd+File.separator+location);
		if (!f.exists() && appFullName!=null) {
			WebAppProperties props = API.propertiesManager.getWebAppPropertiesByFullName(appFullName);
			f = new File(props.app_dir+File.separator+location);
		}
		
		
		if (!f.exists())
			return;
		

		try {
			MetamodelInserter.insertMetamodel(f.toURI().toURL(), raapi);
			logger.debug("MMD inserted from "+f.toURI().toURL()+" into "+project_id);
		} catch (Throwable t) {
			logger.debug("Could not insert MMD from "+location+" into "+project_id);
		}

	}
}