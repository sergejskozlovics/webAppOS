package org.webappos.bridge;

import java.io.File;
import java.net.MalformedURLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.webappos.server.API;
import org.webappos.server.ConfigStatic;
import org.webappos.webcaller.IWebCaller;
import org.webappos.webcaller.WebCaller;

import lv.lumii.tda.kernel.IEventsCommandsHook;
import lv.lumii.tda.kernel.TDAKernel;
import lv.lumii.tda.kernel.TDAKernelDelegate;
import lv.lumii.tda.kernel.mmdparser.MetamodelInserter;
import lv.lumii.tda.raapi.IRepository;
import lv.lumii.tda.raapi.RAAPI;
import lv.lumii.tda.raapi.RAAPIHelper;
import lv.lumii.tda.raapi.RAAPI_Synchronizer;

public class BridgeEventsCommandsHook implements IEventsCommandsHook {

	private static Logger logger =  LoggerFactory.getLogger(BridgeEventsCommandsHook.class);

	
	public static BridgeEventsCommandsHook INSTANCE = new BridgeEventsCommandsHook();
	
	@Override
	synchronized public boolean handleEvent(TDAKernel kernel, long rEvent) {
		logger.trace("Caught TDA Kernel event "+rEvent);
		
		try {
			
			TDAKernel.Owner o = kernel.getOwner();
	
			String[] handlers = kernel.getEventHandlers(rEvent);
			if (handlers == null)
				return false;
			if (handlers.length==0)
				return true;
			
			long it = kernel.getIteratorForDirectObjectClasses(rEvent);
			long rCls = kernel.resolveIteratorFirst(it);		
			String className = kernel.getClassName(rCls);
			kernel.freeReference(rCls);
			kernel.freeIterator(it);
			if (className == null)
				return false;
			
			for (String handler : handlers) {
	
				long rEvent2 = kernel.replicateEventOrCommand(rEvent);
				if (rEvent2 == 0)
					return false;
						
				IWebCaller.WebCallSeed seed = new IWebCaller.WebCallSeed();
				
				seed.actionName = handler;
				
				seed.callingConventions = WebCaller.CallingConventions.TDACALL;
				seed.tdaArgument = rEvent2;
				
		
				if (o!=null) {
					seed.login = o.login;
					seed.project_id = o.project_id;
				}
		  		
				if (kernel.getSynchronizer()!=null)
					kernel.getSynchronizer().flush();
		  		API.webCaller.enqueue(seed);
			}
	  		
			return true; // we forwarded this event to the foreground thread
		}
		finally {
			kernel.deleteObject(rEvent);
		}
	}

	@Override
	synchronized public boolean executeCommand(TDAKernel kernel, long rCommand) {		
		logger.trace("Caught TDA Kernel command "+rCommand);
		
		try {
			
			TDAKernel.Owner o = kernel.getOwner();
			
			long it = kernel.getIteratorForDirectObjectClasses(rCommand);
			long rCls = kernel.resolveIteratorFirst(it);		
			String className = kernel.getClassName(rCls);
			kernel.freeReference(rCls);
			kernel.freeIterator(it);
			
			if (className == null)
				return false;
			
			long rCommand2 = kernel.replicateEventOrCommand(rCommand);
			if (rCommand2 == 0)
				return false;
			
			IWebCaller.WebCallSeed seed = new IWebCaller.WebCallSeed();
			
			seed.actionName = className;
			
			seed.callingConventions = WebCaller.CallingConventions.TDACALL;
			seed.tdaArgument = rCommand2;
			
	
			if (o!=null) {
				seed.login = o.login;
				seed.project_id = o.project_id;
			}
	  		
	  		API.webCaller.enqueue(seed);
	  		
			return true; // we forwarded this event to the foreground thread
		}
		finally {
			kernel.deleteObject(rCommand);
		}
	}
	
	synchronized public boolean handleSyncedEvent(TDAKernel kernel, long rEvent, RAAPI_Synchronizer singleSynchronizer, String login, String project_id, String fullAppName) {
		logger.trace("Caught synced event "+rEvent);
		
		try {
			String[] handlers = kernel.getEventHandlers(rEvent);
			if (handlers == null)
				return false;
			if (handlers.length==0)
				return true;
			
			long it = kernel.getIteratorForDirectObjectClasses(rEvent);
			long rCls = kernel.resolveIteratorFirst(it);		
			String className = kernel.getClassName(rCls);
			kernel.freeReference(rCls);
			kernel.freeIterator(it);
			if (className == null)
				return false;
			
			for (String handler : handlers) {
	
				long rEvent2 = kernel.replicateEventOrCommand(rEvent);
				if (rEvent2 == 0)
					return false;
				
				WebCaller.SyncedWebCallSeed seed = new WebCaller.SyncedWebCallSeed();
				
				seed.actionName = handler;
				
				seed.callingConventions = WebCaller.CallingConventions.TDACALL;
				seed.tdaArgument = rEvent2;
				
				seed.singleSynchronizer = singleSynchronizer;
				seed.login = login;
				seed.project_id = project_id;
		  		
				if (kernel.getSynchronizer()!=null)
					kernel.getSynchronizer().flush();
		  		API.webCaller.enqueue(seed);
			}
	  		
			return true; // we forwarded this event to the foreground thread
		}
		finally {
			kernel.deleteObject(rEvent);
		}
	}
	
	synchronized public boolean executeSyncedCommand(TDAKernel kernel, long rCommand, RAAPI_Synchronizer singleSynchronizer, String login, String project_id, String fullAppName) {
		try {
			long it = kernel.getIteratorForDirectObjectClasses(rCommand);
			long rCls = kernel.resolveIteratorFirst(it);		
			String className = kernel.getClassName(rCls);
			kernel.freeReference(rCls);
			kernel.freeIterator(it);
			if (className == null)
				return false;
			
			long rCommand2 = kernel.replicateEventOrCommand(rCommand);
			if (rCommand2 == 0)
				return false;
			
			WebCaller.SyncedWebCallSeed seed = new WebCaller.SyncedWebCallSeed();
			
			System.out.println("ENQUEUE SYNCED CMD "+className);
			seed.actionName = className;
			
			seed.callingConventions = WebCaller.CallingConventions.TDACALL;
			seed.tdaArgument = rCommand2;
			
			seed.singleSynchronizer = singleSynchronizer;
			seed.login = login;
			seed.project_id = project_id;
	  		
	  		API.webCaller.enqueue(seed);
	  		
			return true; // we forwarded this event to the foreground thread
		}
		finally {
			kernel.deleteObject(rCommand);
		}
	}
	
	private static boolean insertEngineMetamodel(String engineName, RAAPI raapi)
	{
		File mmd = new File(ConfigStatic.APPS_DIR+File.separator+engineName+File.separator+engineName+".mmd");
		if (!mmd.exists())
			mmd = new File(ConfigStatic.APPS_DIR+File.separator+engineName+File.separator+engineName+"Metamodel.mmd");
		if (!mmd.exists())
			mmd = new File(ConfigStatic.APPS_DIR+File.separator+engineName+File.separator+engineName+".ecore");
		if (!mmd.exists())
			mmd = new File(ConfigStatic.APPS_DIR+File.separator+engineName+File.separator+engineName+"Metamodel.ecore");
		

		long rEngineCls = raapi.findClass(engineName);
		if (rEngineCls != 0)
			raapi.freeReference(rEngineCls);
		else {
			// trying to insert engine metamodel...
			boolean ok = false;
			try {
				ok = MetamodelInserter.insertMetamodel(mmd.toURI().toURL(), raapi);
			} catch (MalformedURLException e) {
			}
			
			if (!ok) {
				logger.error("Could not insert metamodel from "+mmd);
				return false;
			}
		}
		
		return true;
	}


	public static void executeAttachEngineCommand(RAAPI raapi, long rCommand) {
		TDAKernel.Owner o = raapi instanceof TDAKernel?((TDAKernel)raapi).getOwner():null;		
		
		long rCls = RAAPIHelper.getObjectClass(raapi, rCommand);
		long rAttr = raapi.findAttribute(rCls, "name");
		if (rAttr == 0) {
			raapi.freeReference(rCls);
			logger.error("Attribute 'name' not found in class TDAKernel::AttachEngineCommand.");
			return;
		}
		String engineName = raapi.getAttributeValue(rCommand, rAttr);
		raapi.freeReference(rCls);
		raapi.freeReference(rAttr);

		TDAKernelDelegate d = null;
		if (raapi instanceof TDAKernel) {
			IRepository r = ((TDAKernel) raapi).getDelegate();
			if (r instanceof TDAKernelDelegate)
				d = (TDAKernelDelegate)r;
		}
		
		if (d != null)
			d.setEngineBeingLoaded(engineName);
		insertEngineMetamodel(engineName, raapi);
		if (d != null)
			d.setEngineBeingLoaded(null);
		
		// linking engine instance to the TDA Kernel instance...												
		long rEngineCls = raapi.findClass(engineName);
		if (rEngineCls == 0) {
			rEngineCls = raapi.createClass(engineName);
		}
		
		long rEngineSuperCls = raapi.findClass("TDAKernel::Engine");
		if (!raapi.isDerivedClass(rEngineCls, rEngineSuperCls)) {
			raapi.createGeneralization(rEngineCls, rEngineSuperCls);
		}
		raapi.freeReference(rEngineSuperCls);
		
		long rEngineObj = 0;
		long it = raapi.getIteratorForAllClassObjects(rEngineCls);
		if (it!=0) {
			rEngineObj = raapi.resolveIteratorFirst(it);
			raapi.freeIterator(it);
		}
		if (rEngineObj == 0)
			rEngineObj = raapi.createObject(rEngineCls);
		
		
		long rKernel = RAAPIHelper.getSingletonObject(raapi, "TDAKernel::TDAKernel");
		long rKernelCls = RAAPIHelper.getObjectClass(raapi, rKernel);
		long rKernelToEngineAssoc = raapi.findAssociationEnd(rKernelCls, "attachedEngine");
		
		if (!raapi.linkExists(rKernel, rEngineObj, rKernelToEngineAssoc))
			raapi.createLink(rKernel, rEngineObj, rKernelToEngineAssoc);
		raapi.freeReference(rKernel);
		raapi.freeReference(rKernelCls);
		raapi.freeReference(rEngineCls);
		raapi.freeReference(rEngineObj);
		raapi.freeReference(rKernelToEngineAssoc);
		
		
		if (API.webCaller.webCallExists("load"+engineName)) {
			WebCaller.WebCallSeed seed = new WebCaller.WebCallSeed();
			
			seed.actionName = "load"+engineName;
			
			seed.callingConventions = WebCaller.CallingConventions.TDACALL;
			seed.tdaArgument = rEngineObj;
			

			if (o != null) {
				seed.login = o.login;
				seed.project_id = o.project_id;
			}
	  		
	  		API.webCaller.enqueue(seed);
		}
				
			
	}

	public static void executeInsertMetamodelCommand(RAAPI raapi, long rCommand) {
		long rCls = RAAPIHelper.getObjectClass(raapi, rCommand);
		long rAttr = raapi.findAttribute(rCls, "url");
		if (rAttr == 0) {
			raapi.freeReference(rCls);
			logger.error("Attribute 'url' not found in class TDAKernel::InsertMetamodelCommand.");
			return;
		}
		String url_str = raapi.getAttributeValue(rCommand, rAttr);
		raapi.freeReference(rCls);
		raapi.freeReference(rAttr);
		
		try {
			StringBuffer errorMessages = new StringBuffer();
			boolean retVal = MetamodelInserter.insertMetamodel(new java.net.URL(url_str), raapi, errorMessages);
			if (!retVal || (errorMessages.length()>0))
				logger.error("Error inserting metamodel at "+url_str+"\n"+errorMessages.toString());
			return;
		} catch (MalformedURLException e) {
			logger.error("TDA Kernel: Error executing InsertMetamodelCommand. "+e.getMessage());
			return;
		}
	}
	
	public static void executeLaunchTransformationCommand(RAAPI raapi, long rCommand) {
		if (!(raapi instanceof TDAKernel))
			return;
		
		TDAKernel.Owner o = ((TDAKernel)raapi).getOwner();
		
		long rCls = RAAPIHelper.getObjectClass(raapi, rCommand);
		long rAttr = raapi.findAttribute(rCls, "uri");
		if (rAttr == 0) {
			raapi.freeReference(rCls);
			logger.error("Attribute 'uri' not found in class TDAKernel::LaunchTransformationCommand (or in its descendants).");
			return;
		}
		String transformationName = raapi.getAttributeValue(rCommand, rAttr);
		if (transformationName == null) {
			raapi.freeReference(rCls);
			logger.error("No transformation URI specified in the given instance of TDAKernel::LaunchTransformationCommand.");
			return;
		}
		raapi.freeReference(rAttr);
		raapi.freeReference(rCls);
		
		long rArgument = 0;
		int j = transformationName.indexOf("):");
		int i = transformationName.indexOf('(');
		if ((i>=0) && (j>i)) {
			try {
				rArgument = Long.parseLong(transformationName.substring(i+1, j));
			}
			catch (Throwable t) {
			}
			transformationName = transformationName.substring(0, i) + transformationName.substring(j+1);
		}
		
		if (rArgument==0)
			rArgument = rCommand;
		
		IWebCaller.WebCallSeed seed = new IWebCaller.WebCallSeed();
		
		seed.actionName = transformationName;
		
		seed.callingConventions = WebCaller.CallingConventions.TDACALL;
		seed.tdaArgument = rArgument;

		if (o != null) {
			seed.login = o.login;
			seed.project_id = o.project_id;
		}
  		
  		API.webCaller.enqueue(seed);
		
	}

	public static void executeExecTransfCmd(RAAPI raapi, long rCommand) {
		if (!(raapi instanceof TDAKernel))
			return;
		
		TDAKernel.Owner o = ((TDAKernel)raapi).getOwner();
		
		long rCls = RAAPIHelper.getObjectClass(raapi, rCommand);
		long rAttr = raapi.findAttribute(rCls, "info");
		if (rAttr == 0) {
			raapi.freeReference(rCls);
			logger.error("Attribute 'info' not found in class ExecTransfCommand.");
			return;
		}
		String transformationName = raapi.getAttributeValue(rCommand, rAttr);
		
		// legacy fix:
		if (transformationName.startsWith("lua_engine#lua."))
			transformationName = "lua:"+transformationName.substring(15);
		
		raapi.freeReference(rAttr);
		raapi.freeReference(rCls);
		
		long rArgument = 0;
		int j = transformationName.indexOf("):");
		int i = transformationName.indexOf('(');
		if ((i>=0) && (j>i)) {
			try {
				rArgument = Long.parseLong(transformationName.substring(i+1, j));
			}
			catch (Throwable t) {
			}
			transformationName = transformationName.substring(0, i) + transformationName.substring(j+1);
		}
		
		if (rArgument==0)
			rArgument = rCommand;
		
		
		IWebCaller.WebCallSeed seed = new IWebCaller.WebCallSeed();
		
		seed.actionName = transformationName;
		
		seed.callingConventions = WebCaller.CallingConventions.TDACALL;
		seed.tdaArgument = rArgument;

		if (o != null) {
			seed.login = o.login;
			seed.project_id = o.project_id;
		}
  		
		System.out.println("ExecTransfCmd "+transformationName+"("+rArgument+")");
  		API.webCaller.enqueue(seed);
		
	}

	public static void prepareProjectOpenedEvent(String dummyLocation, RAAPI raapi, long rEvent) {

		if (!(raapi instanceof TDAKernel))
			return;
		
		TDAKernel.Owner o = ((TDAKernel)raapi).getOwner();
		
		/* TODO: project_upgrade via webcalls
		// Executing project_upgrade transformations...
		long rEECls = raapi.findClass("EnvironmentEngine");
		if (rEECls != 0) {
			long rAttr = raapi.findAttribute(rEECls, "specificBinDirectory");
			long rAttr2 = raapi.findAttribute(rEECls, "lastToolVersion");
			if (rAttr2 == 0) {
				rAttr2 = raapi.createAttribute(rEECls, "lastToolVersion", raapi.findPrimitiveDataType("String"));					
			}
			if (rAttr != 0) {
				long it = raapi.getIteratorForAllClassObjects(rEECls);
				long rEEObj = 0;
				if (it!=0) {
					rEEObj = raapi.resolveIteratorFirst(it);
					raapi.freeIterator(it);
				}
				if (rEEObj != 0) {
					String toolBin = raapi.getAttributeValue(rEEObj, rAttr);
					File f =new File(toolBin+File.separator+"project_upgrade"+File.separator+"project_upgrade.properties");
					if ((toolBin != null) && f.exists()) {
						
						
						
						Properties p = new Properties();
						try {
							p.load(new BufferedInputStream(new FileInputStream(f)));
							
							String lastToolVersion = raapi.getAttributeValue(rEEObj, rAttr2);
							
							boolean allOK = true;
							
							for (Object _key : p.keySet()) {
								String key = (String)_key;
								if ((lastToolVersion == null) || (lastToolVersion.compareTo(key)<0)) {
									String transformationName = p.getProperty(key);
									logger.debug("Executing project_upgrade transformation "+key+" -> "+transformationName+"...");
									String type = TDAKernel.getAdapterTypeFromURI(transformationName);
									String location = TDAKernel.getLocationFromURI(transformationName);
									
									ITransformationAdapter adapter = this.getTransformationAdapter(type);
									if (adapter != null) {
										boolean ok = false;
										try {
											 ok = adapter.launchTransformation(location, 0);
										}
										catch (Throwable t) {}
										logger.debug("Execution of project_upgrade transformation "+transformationName+" returned "+ok);
										if (!ok)
											allOK = false;
										if (allOK)
											raapi.setAttributeValue(rEEObj, rAttr2, key);
									}
								}
								else {
									logger.debug("DO NOT executing already performed project_upgrade transformation "+key+".");
								}
							}
							
							
						}
						catch (Throwable t) {
							// ignore
						}
						
					}
				}
					
				raapi.freeReference(rAttr);
			}
			raapi.freeReference(rEECls);
		}
		*/
		
		
		// load already attached engines...
			
		long rKernelObj = RAAPIHelper.getSingletonObject(raapi, "TDAKernel::TDAKernel");
		long rKernelCls = RAAPIHelper.getObjectClass(raapi, rKernelObj);
		long rAssoc = raapi.findAssociationEnd(rKernelCls, "attachedEngine");
		
		long it = raapi.getIteratorForLinkedObjects(rKernelObj, rAssoc);
		
		raapi.freeReference(rKernelObj);
		raapi.freeReference(rKernelCls);
		raapi.freeReference(rAssoc);
		
		long rEngineObj = raapi.resolveIteratorFirst(it);
		while (rEngineObj != 0) {
			long rEngineCls = RAAPIHelper.getObjectClass(raapi, rEngineObj);
			String engineName = raapi.getClassName(rEngineCls);
			raapi.freeReference(rEngineCls);
			
			if (API.webCaller.webCallExists("load"+engineName)) {
				IWebCaller.WebCallSeed seed = new IWebCaller.WebCallSeed();
				
				seed.actionName = "load"+engineName;
				
				seed.callingConventions = WebCaller.CallingConventions.TDACALL;
				seed.tdaArgument = rEngineObj;
				

				if (o != null) {
					seed.login = o.login;
					seed.project_id = o.project_id;
				}
		  		
		  		API.webCaller.enqueue(seed);
			}
			
			raapi.freeReference(rEngineObj);
			rEngineObj = raapi.resolveIteratorNext(it);
		}
		raapi.freeIterator(it);
	}
}
