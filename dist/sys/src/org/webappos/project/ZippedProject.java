package org.webappos.project;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.webappos.antiattack.ValidityChecker;
import org.webappos.properties.WebAppProperties;
import org.webappos.properties.PropertiesManager;
import org.webappos.server.API;
import org.webappos.webcaller.IWebCaller;
import org.webappos.webcaller.WebCaller;

import lv.lumii.tda.kernel.IEventsCommandsHook;
import lv.lumii.tda.kernel.TDAKernel;
import lv.lumii.tda.raapi.IRepository;
import lv.lumii.tda.raapi.RAAPIHelper;
import lv.lumii.tda.raapi.RAAPI_Synchronizer;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;

public class ZippedProject implements IProject {

	private static Logger logger =  LoggerFactory.getLogger(ZippedProject.class);
	
	private boolean offline;

	private ZipFolder zipFolder = null;	// zipFolder is null, iff no project is open/created
	//private String toolName = null;
	private WebAppProperties appProps = null;
	private TDAKernel tdaKernel = null;
	
	private String offeredName = null;
	
	public ZippedProject(boolean _offline) {
		offline = _offline;
	}
	
	public ZippedProject() {
		offline = false;
	}
	
	public static boolean validateProjectFolder(String folder, String filename, WebAppProperties appProps) {
		try {
			ValidityChecker.checkProjectCacheDirectory(folder, appProps);
			return true;
		} catch (Exception e) {
			logger.warn("Project "+filename+" (folder "+folder+") could not be validated. This can be a security threat!"); 
			
			// inform user about possible threats and ask whether to continue
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("Confirmation Dialog");
			alert.setHeaderText("The validity check failed for the project "+filename+". Perhaps, the project code has been modified.");
			alert.setContentText("Are you sure to continue with this project? This may lead to launching malicious code stored in the project.");

			Optional<ButtonType> result = alert.showAndWait();
			return (result.get() == ButtonType.OK);
		}
		
	}
	
	@Override
	public boolean open(WebAppProperties _appProps, String filename, String login, RAAPI_Synchronizer sync, IEventsCommandsHook hook) {
		if (zipFolder != null) {
			return false; // already open
		}
				
		appProps = _appProps;

		int i = filename.lastIndexOf('.');
		if (i==-1) {
			logger.error("The project file name ("+filename+") does not have an extension. I could not determine the app used to create the project.");
			return false;
		}
		
		boolean legacyProject = "grt".equalsIgnoreCase(filename.substring(i+1)); 
		
		if (legacyProject) {
			
			File fXmlFile = new File(filename);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder;
			Document doc;
			try {
				dBuilder = dbFactory.newDocumentBuilder();
				doc = dBuilder.parse(fXmlFile);
			} catch (Throwable t) {
				logger.error("Could not open the legacy project "+filename+".\n"+t.getMessage());
				return false;
			}
		 
			//optional, but recommended
			//read this - http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
			doc.getDocumentElement().normalize();
		 

			String toolName = null;
			
			NodeList nList = doc.getElementsByTagName("Project");
			if ((nList != null) && (nList.getLength()>0) && (nList.item(0)instanceof Element)) {
				nList = ((Element)nList.item(0)).getElementsByTagName("Tools");
				if ((nList != null) && (nList.getLength()>0) && (nList.item(0)instanceof Element)) {
					nList = ((Element)nList.item(0)).getElementsByTagName("Name");
					if ((nList != null) && (nList.getLength()>0) && (nList.item(0)instanceof Element)) {
						toolName = nList.item(0).getTextContent();
					}
				}
			}
			
			if (toolName == null) {
				logger.error("Could not determine the tool from the legacy project file "+filename+".");
				return false;			
			}
			
			if (!toolName.endsWith(".webapp"))
					toolName += ".webapp";
			appProps = API.propertiesManager.getWebAppPropertiesByFullName(toolName);
		}
		else {
			if (appProps == null) {
				logger.error("Web app properties (webapp.properties) are not found for this project.");
				return false;
			}
		}
		
		logger.debug("appName="+appProps.app_full_name);
		
		zipFolder = new ZipFolder();
		
		if (legacyProject) {
			logger.debug("Converting legacy project started...");
			long time1 = System.currentTimeMillis();
			
			if (!zipFolder.createNew()) {
				logger.error("Could not create a temporary zip folder.");
				zipFolder = null;
			}

			boolean ok = true;
			try {
				org.apache.commons.io.FileUtils.copyDirectory(new File(new File(filename).getAbsolutePath()).getParentFile(), zipFolder.getFolder().toAbsolutePath().toFile());
			} catch (IOException e) {
				ok = false;
			}
				
			if (!ok) {
				logger.error("Could not copy the given legacy project into a temporary zip folder.");
				ZipFolder.deleteFolder(zipFolder.getFolder().toAbsolutePath());
				return false;
			}
			
			final String folder = zipFolder.getFolder().toAbsolutePath().toString();
			boolean result = (LegacyToCloudProjectConverter.convert(folder, folder)!=null);
			if (!result)
				logger.error("Converting legacy returned "+result);
			
			if (!result) {
				logger.error("Could not convert legacy project to webAppOS format.");
				ZipFolder.deleteFolder(zipFolder.getFolder().toAbsolutePath());
				return false;				
			}
			
			long time2 = System.currentTimeMillis();
			logger.debug("Converting legacy project finished in "+(time2-time1)+" ms");
			
		}
		else {		
			if (!zipFolder.open(new File(filename))) {
				logger.error("Could not unzip the project "+filename+" to a temporary folder.");
				zipFolder = null;
				return false;
			}
			
	/*		System.err.println("Checking for the need to convert Ecore...");
			IRepository k1 = TDAKernel.newRepositoryAdapter("ecore");
			if (k1 != null) {
				String location = zipFolder.getFolder().toUri().toString()+"/data.xmi";
				if (k1.exists(location)) {
					System.err.println("Ecore exists. Converting...");					
					boolean b = k1.open(location);
					if (b) {

						TDAKernel k2 = new TDAKernel();
						
						String targetLocation = IProject.DEFAULT_REPOSITORY+":"+zipFolder.getFolder().toUri().toString();
						
						if (k2.exists(targetLocation))
							k2.drop(targetLocation);
						
						b = k2.open(targetLocation);
						if (b) {
							b = lv.lumii.tda.kernel.TDACopier.makeCopy(k1, k2, !true);
							if (!b) {
								System.err.println("Copy failed.");			
							}
							k2.close();
						}
						else {
							System.err.println("Could not create target repository "+targetLocation);
						}
						
						
						k1.close();
					}
					else 
						System.err.println("Could not open the Ecore project in "+location);
					
					if (!b)
						return false;
				}
			}
			else
				System.err.println("Error: could not initialize a repository adapter for ecore.");*/			
		}
		
		String folder = zipFolder.getFolder().toAbsolutePath().toString();

		if (!validateProjectFolder(folder, filename, appProps)) {
			zipFolder.close();
			return false;
		}
		
		logger.debug("Opening the repository...");
		long time1 = System.currentTimeMillis();
		
		tdaKernel = new TDAKernel();
		if (!tdaKernel.exists(DEFAULT_REPOSITORY+":"+folder) || !tdaKernel.open(DEFAULT_REPOSITORY+":"+folder)) {
				logger.error("Could not open the pivot repository in the folder "+folder+" (exists="+tdaKernel.exists(DEFAULT_REPOSITORY+":"+folder)+").");
				zipFolder.close();
				zipFolder = null;
				tdaKernel = null;
				return false;
		}
		long time2 = System.currentTimeMillis();
		logger.debug("Opened in "+(time2-time1)+" ms");
		
		if (logger.isDebugEnabled()) {
			logger.debug("Open: The list of attached engines:");
			long rKernel = RAAPIHelper.getSingletonObject(tdaKernel, "TDAKernel::TDAKernel");
			long rKernelCls = RAAPIHelper.getObjectClass(tdaKernel, rKernel);
			long rKernelToEngineAssoc = tdaKernel.findAssociationEnd(rKernelCls, "attachedEngine");
			long it = tdaKernel.getIteratorForLinkedObjects(rKernel, rKernelToEngineAssoc);
			if (it!=0) {
				long rEngineObj = tdaKernel.resolveIteratorFirst(it);
				while (rEngineObj!=0) {
					logger.debug(" * "+RAAPIHelper.getObjectClassName(tdaKernel, rEngineObj));
					
					tdaKernel.freeReference(rEngineObj);
					rEngineObj = tdaKernel.resolveIteratorNext(it);
				}
				tdaKernel.freeIterator(it);
			}
			tdaKernel.freeReference(rKernel);
		}
		time2 = System.currentTimeMillis();
		logger.debug("Opened(2) in "+(time2-time1)+" ms");

		if (postOpen(false, login, sync, hook))
			return true;
		else {
			close();
			return false;
		}
	}

	@Override
	public boolean createFromTemplate(WebAppProperties _appProps, String template, String desiredName, String login, RAAPI_Synchronizer sync, IEventsCommandsHook hook) {
		if (zipFolder != null) {
			return false; // already open
		}
		
		if (_appProps == null)
			return false;
		
		
		logger.debug("createFromTemplate "+template);
		
		int i = template.lastIndexOf('.');
		if (i==-1) {
			logger.error("The template file name ("+template+") does not have an extension. I could not determine the tool used to create the project.");
			return false;
		}
		
		zipFolder = new ZipFolder();
		if (!zipFolder.createFromTemplate(new File(template))) {
			logger.error("Could not unzip the template "+template+" to a temporary folder.");
			zipFolder = null;
			return false;
		}
		
		appProps = _appProps;
		if (!validateProjectFolder(zipFolder.getFolder().toFile().getAbsolutePath(), template, appProps)) {
			zipFolder.close();
			return false;
		}
		
		String folder = zipFolder.getFolder().toAbsolutePath().toString();
		tdaKernel = new TDAKernel();
		if (!tdaKernel.exists(DEFAULT_REPOSITORY+":"+folder) || !tdaKernel.open(DEFAULT_REPOSITORY+":"+folder)) {
				logger.error("Could not open the pivot repository in the folder "+folder+".");
				zipFolder.close();
				zipFolder = null;
				tdaKernel = null;
				return false;
		}
		
		if (postOpen(false, login, sync, hook)) {
			setName(desiredName);
			return true;
		}
		else {
			close();
			return false;
		}
	}

	@Override
	public boolean boostrap(WebAppProperties _appProps, String desiredName, String login, RAAPI_Synchronizer sync, IEventsCommandsHook hook) {		
		if (_appProps == null) // must specify app properties
			return false;
		
		if (zipFolder != null) {
			return false; // already open
		}
		
		logger.debug("bootstrap "+_appProps.app_full_name);
		
		
		zipFolder = new ZipFolder();
		if (!zipFolder.createNew()) {
			System.err.println("Environment Engine: Could not create a temporary zip folder.");
			zipFolder = null;
		}
		
		appProps = _appProps;
		
		String folder = zipFolder.getFolder().toAbsolutePath().toString();
		String location = DEFAULT_REPOSITORY+":"+folder;
		
		tdaKernel = new TDAKernel();
		if (!tdaKernel.open(location)) {
			logger.error("Could not create the pivot repository in the folder "+folder+" (location="+location+").");
			zipFolder.close();
			zipFolder = null;
			tdaKernel = null;
			return false;
		}
		
		
		if (postOpen(true, login, sync, hook)) {
			setName(desiredName);
			return true;
		}
		else {
			close();
			return false;
		}
	}

	
	private boolean postOpen(boolean bootstrap, String login, RAAPI_Synchronizer sync, IEventsCommandsHook hook)
	{
		if (appProps != null) {
			for (String awc : appProps.auto_webcalls) {
				
				System.out.println("Executing auto webcall `"+awc+"'... ["+this.getName()+"]");
				IWebCaller.WebCallSeed seed = new IWebCaller.WebCallSeed();
				
				seed.actionName = awc;
				seed.project_id = this.getName();
				
				seed.callingConventions = WebCaller.CallingConventions.WEBMEMCALL;
				seed.webmemArgument = 0;			
		
		  		API.webCaller.enqueue(seed);
			}
		}
		
		if (offline) {
			return true; // do not call post-open for offline projects
		}
		logger.debug("postOpen...");
		
		tdaKernel.setPredefinedBits(1, 0); // for non-cloud projects
		if (sync!=null)
			tdaKernel.sync(sync, 1);
		
		tdaKernel.setEventsCommandsHook(hook);
		
		// patch for TDA1:
		File migrationDir = new File(zipFolder.getFolder().toAbsolutePath().toString()+File.separator+"Migration"); 
		if (!migrationDir.exists())
			migrationDir.mkdir();
		
		
		tdaKernel.upgradeToTDA(bootstrap, login, false);
		return true;
	}
	
	@Override
	public String getFolderName() {
		if (zipFolder == null)
			return null;
		Path f = zipFolder.getFolder();
		if (f == null)
			return null;
		return f.toString();
	}

	@Override
	public String getName() {
		if (offeredName != null)
			return offeredName;
		if (zipFolder == null)
			return null;
		File f = zipFolder.getFile();
		if (f == null)
			return "unnamed";
		return f.getAbsolutePath();
	}

	@Override
	public boolean setName(String _name) {
		if (zipFolder!=null)
			return zipFolder.setFile(new File(_name));
		else {
			offeredName = _name;
			return true;
		}
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
		if ((offeredName!=null) && (zipFolder!=null)) {
			if (!zipFolder.setFile(new File(offeredName)))
				return false;
			offeredName = null;
		}
		
		if (getName()==null)
			return false;
		
		if (zipFolder == null)
			return false; // not open
		
		if (tdaKernel == null)
			return false;

		if (logger.isDebugEnabled()) {
			logger.debug("Save: The list of attached engines:");
			long rKernel = RAAPIHelper.getSingletonObject(tdaKernel, "TDAKernel::TDAKernel");
			long rKernelCls = RAAPIHelper.getObjectClass(tdaKernel, rKernel);
			long rKernelToEngineAssoc = tdaKernel.findAssociationEnd(rKernelCls, "attachedEngine");
			long it = tdaKernel.getIteratorForLinkedObjects(rKernel, rKernelToEngineAssoc);
			long rEngineObj = tdaKernel.resolveIteratorFirst(it);
			while (rEngineObj!=0) {
				logger.debug(" * "+RAAPIHelper.getObjectClassName(tdaKernel, rEngineObj));
				
				tdaKernel.freeReference(rEngineObj);
				rEngineObj = tdaKernel.resolveIteratorNext(it);
			}
			tdaKernel.freeIterator(it);
			tdaKernel.freeReference(rKernel);
		}

		if (!tdaKernel.startSave()) {
			logger.error("Could not start the save process.");
			return false;
		}
		if (!tdaKernel.finishSave()) {
			logger.error("Could not finish the save process.");
			return false;
		}
		
		return zipFolder.save();
	}

	@Override
	public void close() {		
		tdaKernel.close();
		zipFolder.close();
		tdaKernel = null;
		zipFolder = null;
		appProps = null;		
		offeredName = null;
	}

	@Override
	public TDAKernel getTDAKernel() {
		return tdaKernel;
	}

}
