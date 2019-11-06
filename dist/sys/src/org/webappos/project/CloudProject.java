package org.webappos.project;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.webappos.antiattack.ValidityChecker;
import org.webappos.fs.HomeFS;
import org.webappos.properties.WebAppProperties;
import org.webappos.server.API;

import lv.lumii.tda.kernel.IEventsCommandsHook;
import lv.lumii.tda.kernel.TDAKernel;
import lv.lumii.tda.raapi.RAAPI_Synchronizer;

public class CloudProject implements IProject {
	
	private static Logger logger =  LoggerFactory.getLogger(CloudProject.class);
	
	private TDAKernel tdaKernel = null;
	private String name = null;
	private String folder = null;
	private WebAppProperties appProps = null;
	
	@Override
	public boolean open(WebAppProperties _appProps, String name, String login, RAAPI_Synchronizer sync, IEventsCommandsHook hook) {
		if (tdaKernel != null)
			return false; // already open
		
		// name is project_id: login/subforlder/name.extension (zipped)
		
		try {
			ValidityChecker.checkRelativePath(name, false);
		}
		catch(Throwable t){
			return false; // for security reasons
		}		
		
		this.name = name;
		
		appProps = _appProps;
		
		int i = name.indexOf("/");
		int j = name.lastIndexOf(".");
		if ((i<0) || (j<0) || (i>j)) {
			logger.error("name (project_id) must be in the form user/subfolder/name.extension");
			cleanup(false, false);
			return false;
		}
		//String user = name.substring(0, i);
		String ext = name.substring(j+1);
		if (appProps == null) {
			logger.error("App not found for project_id "+name);
			cleanup(false, false);
			return false;			
		}

		folder = ProjectCache.getCloudProjectCacheDirectory(name, appProps);
		if (folder==null) {
			cleanup(false, false);
			return false;
		}
		
		System.err.println("SERVER KERNEL "+SERVER_REPOSITORY+":"+folder);
		tdaKernel = new TDAKernel();
		
		if (!tdaKernel.exists(DEFAULT_REPOSITORY+":"+folder) || !tdaKernel.open(SERVER_REPOSITORY+":"+folder)) {
			logger.error("Could not open the pivot repository in the folder "+folder+" (exists="+tdaKernel.exists(DEFAULT_REPOSITORY+":"+folder)+").");			
			cleanup(false, true);
			System.err.println("SERVER KERNEL OBLOM");
			return false;
		}
		
		System.err.println("SERVER KERNEL OK");
		postOpen(false, login, sync, hook);
		return true;
	}
	
		
	private String getFreeName(WebAppProperties appProps, String desiredName) {
		// desiredName is is format "user/New SomeApp project.ext";
		// returns in the same format
		if (desiredName == null)
			return null;
		
		int i = desiredName.indexOf("/");
		int j = desiredName.lastIndexOf(".");
		if ((i<0) || (j<0) || (i>j))
			return null;
						
		String login = desiredName.substring(0, i);
		String name = desiredName.substring(i+1, j).trim();
		if (name.isEmpty())
			name = "New "+appProps.app_displayed_name+" project";
		String ext = desiredName.substring(j+1).trim();
		if (ext.isEmpty()) {
			ext = appProps.project_extension;
		}
		if (ext == null)
			return null;
		
		boolean exists = HomeFS.ROOT_INSTANCE.pathExists(login+"/"+name+"."+ext);
		if (exists) {
			long l;
			for (l=1;;l++) {
				exists = HomeFS.ROOT_INSTANCE.pathExists(login+"/"+name+" "+l+"."+ext);
				if (!exists)
					break;
			}
			return login+"/"+name+" "+l+"."+ext;						
		}
		else			
			return login+"/"+name+"."+ext; 		
	}	

	@Override
	public boolean createFromTemplate(WebAppProperties _appProps, String templateName, String desiredName, String login, RAAPI_Synchronizer sync, IEventsCommandsHook hook) {		
		if ((templateName == null) || (desiredName == null))
			return false;

		if (tdaKernel != null)
			return false; // already open
		
		int i = templateName.indexOf(':');
		if (i<0)
			return false; // templateName must start from "apptemplate:", "publishedtemplate:" or "usertemplate:"
		
		try {
			ValidityChecker.checkRelativePath(templateName.substring(i+1), false);
			ValidityChecker.checkRelativePath(desiredName, false);
		}
		catch(Throwable t){
			return false; // for security reasons
		}		
		
		// desiredName must be in format "user/New SomeApp project.ext";		
		
		i = desiredName.indexOf("/");
		if (i<0)
			return false;
		String login1 = desiredName.substring(0, i);
		if (!login1.equals(login))
			return false;
				
		
		appProps = _appProps;
		

		InputStream is = appProps.openTemplateStream(login1, templateName);
		if (is==null) {
			cleanup(false, false);
			return false;
		}
		
		// search for free name
		this.name = getFreeName(appProps, desiredName);
		
		
		boolean b = false;
		try {
			b = HomeFS.ROOT_INSTANCE.uploadFile(name, is, is.available(), true);
		} catch (IOException e) {
		}

		try {
			is.close();
		}
		catch(Throwable t) {			
		}
		
		if (!b) {
			cleanup(false, false);
			return false;
		}
		
		folder = ProjectCache.getCloudProjectCacheDirectory(name, appProps);
		if (folder==null) {
			cleanup(false, false);
			return false;
		}
		
		
	
		tdaKernel = new TDAKernel();
		if (!tdaKernel.exists(DEFAULT_REPOSITORY+":"+folder) || !tdaKernel.open(SERVER_REPOSITORY+":"+folder)) {
			logger.error("Could not open the pivot repository in the folder "+folder+" (exists="+tdaKernel.exists(DEFAULT_REPOSITORY+":"+folder)+").");
			cleanup(false, true);
			return false;
		}

		
		
		postOpen(false, login, sync, hook);

		return true;
	}

	@Override
	public boolean boostrap(WebAppProperties _appProps, String desiredName, String login, RAAPI_Synchronizer sync, IEventsCommandsHook hook) {
		if (_appProps == null)
			return false;
		
		this.appProps = _appProps;
		
		if (desiredName == null)
			return false;
		
		if (tdaKernel != null)
			return false; // already open
		
		try {
			ValidityChecker.checkRelativePath(desiredName, false);
		}
		catch(Throwable t){
			cleanup(false, false);
			return false; // for security reasons
		}		
		
		
		if ((appProps.initial_webcall == null) || (appProps.initial_webcall.isEmpty())) {
			// initial webcall not found
			return false;
		}
		// desiredName is is format "user/New SomeApp project.ext";		

		// search for free name
		this.name = getFreeName(appProps, desiredName);
		
		folder = ProjectCache.getCloudProjectCacheDirectory(name, appProps, true);
		if (folder==null) {
			cleanup(false, false);
			return false;
		}
		
		tdaKernel = new TDAKernel();
				
		
		if (tdaKernel.exists(DEFAULT_REPOSITORY+":"+folder) || !tdaKernel.open(SERVER_REPOSITORY+":"+folder)) {
			logger.error("Could not create the pivot repository in the folder "+folder+" (exists="+tdaKernel.exists(DEFAULT_REPOSITORY+":"+folder)+").");
			cleanup(false, true);
			return false;
		}
		
		postOpen(true, login, sync, hook);
		
		return true;
	}
	
	private void postOpen(final boolean bootstrap, final String login, RAAPI_Synchronizer sync, IEventsCommandsHook hook) {
		if (appProps != null) {
			for (String awc : appProps.auto_webcalls)
				System.out.println("Executing auto webcall `"+awc+"'...");
		}

		
		// sending back the (possibly) updated project_id...
   		sync.syncRawAction(new double[] {0xFC}, RAAPI_Synchronizer.sharpenString(name));	
		
		/*ForegroundThread.runInForegroundThread(new Runnable() {

			@Override
			public void run() {*/
				tdaKernel.setEventsCommandsHook(null);
				
				tdaKernel.setPredefinedBits(API.config.project_user_predefined_bits_count, 0); // we have predefined bits 0000..000
				
				
				if (!tdaKernel.upgradeToTDA(bootstrap, login, false)) {
					String project_id = CloudProject.this.name; 
					cleanup(true, true);
					API.dataMemory.faultMRAM(project_id);
				}
				else {
					if (sync!=null)
						tdaKernel.sync(sync, 1); // our first remote client will have predefined bits 0000..001
				}
				
				tdaKernel.setEventsCommandsHook(hook);
			/*}
			
		}, login);*/
		
	}

	@Override
	public String getFolderName() {
		return folder;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public boolean setName(String _name) {
		if (_name.indexOf("..")>=0)
			return false; // for security reasons		
		
		if (ProjectCache.changeProjectId(name, _name)) {
			name = _name;
			return true;
		}
		else
			return false;
	}

	@Override
	public String getAppName() {
		if (appProps!=null)
			return appProps.app_full_name;
		else
			return null;
	}

	@Override
	public boolean save() {
		if (getName()==null)
			return false;
		
		if (tdaKernel == null)
			return false;

		if (!tdaKernel.startSave()) {
			logger.error("Could not start the save process.");
			return false;
		}
		if (!tdaKernel.finishSave()) {
			logger.error("Could not finish the save process.");
			return false;
		}

/*		File tmp;
		try {
			tmp = File.createTempFile("cloud_project_zip", ".tmp");
		} catch (IOException e1) {
			return false;
		}
		
		if (!ProjectCache.zip(new File(folder).toPath(), tmp))
			return false;
		
		InputStream is;
		try {
			is = new BufferedInputStream(new FileInputStream(tmp));
		} catch (FileNotFoundException e) {
			return false;
		}
		
		boolean b = HomeFS.ROOT_INSTANCE.uploadFile(name, is, tmp.length(), true);

		try {
			is.close();
		}
		catch(Throwable t) {			
		}
		
		tmp.delete();
				
		return b;*/
		
		return ProjectCache.zipAndUpload(new File(folder).getName()/*uuid*/, name, true);
	}

	private void cleanup(boolean close, boolean deleteFolder) {
		if (close && (tdaKernel!=null)) {
			tdaKernel.close();
		}
		
		if (deleteFolder && (folder!=null)) {
			File f = new File(folder);
			ProjectCache.deleteProjectCache(name, f.getName());
		}
		
		name = null;
		appProps = null;
		folder = null;
		tdaKernel = null;		
	}
	
	@Override
	public void close() {
		cleanup(true, false);
	}

	@Override
	public TDAKernel getTDAKernel() {
		return tdaKernel;
	}

}
