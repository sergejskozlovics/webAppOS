package lv.lumii.tda.kernel;

import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.io.*;
import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
//import org.webappos.util.ForegroundThread;
//import org.webappos.web.server.Config;

import lv.lumii.tda.kernel.mm.Submitter;
import lv.lumii.tda.kernel.mm.TDAKernelMetamodelFactory.ElementReferenceException;
import lv.lumii.tda.kernel.mmdparser.MetamodelInserter;
import lv.lumii.tda.raapi.IEngineAdapter;
import lv.lumii.tda.raapi.IRepository;
import lv.lumii.tda.raapi.ITransformationAdapter;
import lv.lumii.tda.raapi.RAAPI;
import lv.lumii.tda.raapi.RAAPIHelper;
import lv.lumii.tda.raapi.RAAPI_Synchronizable;
import lv.lumii.tda.raapi.RAAPI_Synchronizer;
import lv.lumii.tda.raapi.RAAPI_WR;

public class TDAKernel extends DelegatorToRepositoryBase implements IEventsCommandsHelper
{
	
	private static Logger logger =  LoggerFactory.getLogger(TDAKernel.class);
	
	//***** LEGACY TDA PATCHES *****//
	public long findClass(String name) {
		if ("Command".equals(name))
			name = "TDAKernel::Command";
		else
		if ("Event".equals(name))
			name = "TDAKernel::Event";
		return super.findClass(name);
	}
	
	
	//***** TDA KERNEL CONSTRUCTOR AND UUID-RELATED FUNCTIONS *****//
	private java.util.UUID uuid; 
	private static Map<java.util.UUID,TDAKernel> allTDAKernelsInThisJVM =
			new HashMap<java.util.UUID,TDAKernel>();
	
	
	private Owner owner = null;
	
	
	/*package*/IEventsCommandsHook hook = new IEventsCommandsHook() {
		private IEventsCommandsHook directHook = new DirectEventsCommandsHook();

		@Override
		public boolean handleEvent(TDAKernel kernel, long rEvent) {
			int res = kernel.tryToHandleKernelEvent(rEvent);
			if (res!=0)
				return (res==+1);
			else
				return directHook.handleEvent(kernel, rEvent);
		}

		@Override
		public boolean executeCommand(TDAKernel kernel, long rCommand) {
			int res = kernel.tryToExecuteKernelCommand(rCommand);
			if (res!=0)
				return (res==+1);
			else
				return directHook.executeCommand(kernel, rCommand);
		}
	};
	
		
	public TDAKernel()
	{
		super();
		
		// Generating a UUID and mapping it to this TDA Kernel
		synchronized(TDAKernel.class) {
			do {
				uuid = UUID.randomUUID();
			} while (allTDAKernelsInThisJVM.containsKey(uuid));
			allTDAKernelsInThisJVM.put(uuid, this);
		}
		
	}
	
	public static class Owner {
		public String login;
		public String project_id;
	}

	public void setOwner(Owner _owner) {
		this.owner = _owner;
	}
	
	public Owner getOwner() {
		return this.owner;
	}
	
	private String mainTransformation = null;
	public void setMainTransformation(String name) {
		mainTransformation = name;
	}
	
	public String getUUID()
	{
		return uuid.toString();
	}

	synchronized static public TDAKernel findTDAKernelByUUID(String uuidString)
	{
		try {
			java.util.UUID uuid = java.util.UUID.fromString(uuidString);
			return allTDAKernelsInThisJVM.get(uuid);
		}
		catch (Throwable t) {
			return null;
		}
	}
	
	
	private boolean finalizeCalled = false;
	synchronized public void finalize()
		// may be called several times
	{
		if (finalizeCalled)
			return;		
		finalizeCalled = true;
				
		// unmapping UUID from the list of TDA Kernels...
		synchronized(TDAKernel.class) {
			allTDAKernelsInThisJVM.remove(uuid);
		}
		
	}
	
	///// LISTENERS /////
/*	Set<IStringListener> listeners = new HashSet<IStringListener>();
	public void addListener(IStringListener l) {
		listeners.add(l);
	}
	public void removeListener(IStringListener l) {
		listeners.remove(l);
	}	
	private void log(String s)
	{
		logger.debug("TDAKernel log: "+s);
		for (IStringListener l : listeners)
			l.processString(s);
	}*/
	
	///// MANAGING ADAPTERS //////
	
	private HashMap<String, ITransformationAdapter> transformationType2adapter = new HashMap<String, ITransformationAdapter>();
	private HashMap<String, IEngineAdapter> engineName2adapter = new HashMap<String, IEngineAdapter>();
	private List<IEngineAdapter> reverseOrderedEngineAdapters = new LinkedList<IEngineAdapter>();
	
	public static String getAdapterTypeFromURI(String locationWithAdapterType)
	{		
		int i = locationWithAdapterType.indexOf(":");
		if (i==-1)
			return null; // incorrect location
		
		return locationWithAdapterType.substring(0, i).trim();
	}
	
	public static String getLocationFromURI(String locationWithAdapterType)
	{
		int i = locationWithAdapterType.indexOf(":");
		if (i==-1)
			return null; // incorrect location
		
		String location = locationWithAdapterType.substring(i+1);
		if (location.startsWith("//"))
			location = location.substring(2);
		
		return location;
	}
	
	/**
	 * Creates an instance of the given adapter type.
	 * 
	 * @param adapterType The name of some existing class implementing
	 * IRepository interface, or the short name of the adapter
	 * such as "jr", "mii_rep", or "ecore". 
	 * In the latter case, the fully qualified adapter class
	 * name is assumed to be lv.lumii.adapters.repository.<adapterType>.RepositoryAdapter.
	 * 
	 * @return a newly created instance of the repository adapter, or null
	 * on error. 
	 */
	public static IRepository newRepositoryAdapter(String adapterType, TDAKernel callerKernel)
	{
		try {
			Class<?> c = Class.forName("lv.lumii.tda.adapters.repository."
					+ adapterType
					+ ".RepositoryAdapter");
			
			Method m = null;
			try {
				m = c.getMethod("create");
			}
			catch(Throwable t) {
			}

			
			Method m1 = null;
			try {
				m1 = c.getMethod("create", TDAKernel.class);
			}
			catch(Throwable t) {
			}
			
			if ((m == null) && (m1==null)) {
				IRepository r = (IRepository)c.getConstructor().newInstance();
				return new DelegatorToRepositoryWithOptionalOperations(r);
			}
			else {
				if (m1 != null)
					return (IRepository)m1.invoke(c, callerKernel);
				else
					return (IRepository)m.invoke(c);
			}
		} catch (Throwable t) {
			logger.error("Could not initialize a repository adapter for `"+adapterType+"'.");
			return null;
		}		
	}
	
	public static IRepository newRepositoryAdapter(String adapterType) {
		return TDAKernel.newRepositoryAdapter(adapterType, (TDAKernel)null);
	}

	/**
	 * Creates an instance of the given transformation adapter type.
	 * 
	 * @param adapterType The name of some existing class implementing
	 * ITransformationAdapter interface, or the short name of the adapter
	 * such as "lquery", "atl", or "mola". 
	 * In the latter case, the fully qualified adapter class
	 * name is assumed to be lv.lumii.adapters.transformation.<adapterType>.TransformationAdapter.
	 * 
	 * @return a newly created instance of the transformation adapter, or null
	 * on error. 
	 */
	public static ITransformationAdapter newTransformationAdapter(String adapterType)
	{
		try {
			Class<?> c = Class.forName("lv.lumii.tda.adapters.transformation."
					+ adapterType
					+ ".TransformationAdapter");
			Object o = c.getConstructor().newInstance();
			return (ITransformationAdapter)(o);
		} catch (Throwable t) {
			logger.error("Could not initialize a transformation adapter for `"+adapterType+"'.");
			return null;
		}		
	}	
	
	/**
	 * Creates an instance of the given engine adapter type.
	 * 
	 * @param adapterType The name of some existing class implementing
	 * IEngineAdapter interface, or the short name of the adapter
	 * such as "dll", "dll32", or "net". 
	 * In the latter case, the fully qualified adapter class
	 * name is assumed to be lv.lumii.adapters.engine.<adapterType>.EngineAdapter.
	 * 
	 * @return a newly created instance of the engine adapter, or null
	 * on error. 
	 */
	public static IEngineAdapter newEngineAdapter(String adapterType)
	{
		try {
			Class<?> c = Class.forName("lv.lumii.tda.adapters.engine."
					+ adapterType
					+ ".EngineAdapter");
			
			return (IEngineAdapter)c.getConstructor().newInstance();
		} catch (Throwable t) {
			logger.error("Could not initialize a engine adapter for `"+adapterType+"'.");
			return null;
		}		
	}

	private boolean tryToLoadEngine(String engineName, String adapterTypeName)
	{
		IEngineAdapter adapter = newEngineAdapter(adapterTypeName);
		if (adapter == null)
			return false;
		
		// TODO: disable undo
		
		engineName2adapter.put(engineName, adapter); // putting into the map now, so the engine is available during load()
		reverseOrderedEngineAdapters.add(0, adapter); // insert before to preserve the reversed order
		
		delegate.setEngineBeingLoaded(engineName);
		boolean retVal = adapter.load(engineName, this);
		delegate.setEngineBeingLoaded(null);
		
		if (!retVal) {
			engineName2adapter.remove(engineName);
			reverseOrderedEngineAdapters.remove(adapter);
		}
		return retVal;		
	}
	
	/**
	 * Creates new or returns an existing engine adapter for the given engine name.
	 * 
	 * @param engineName the name of the engine (without adapter type)
	 * @return the engine adapter for the engine with the given name.
	 */
	public IEngineAdapter getEngineAdapter(String engineName)
	{
		IEngineAdapter adapter = engineName2adapter.get(engineName);
		if (adapter != null)
			return adapter;
		
	
		
		String adapterTypeName = System.getProperty("adapterFor"+engineName);
		if (adapterTypeName != null) {
			tryToLoadEngine(engineName, adapterTypeName);
			IEngineAdapter retVal = engineName2adapter.get(engineName);
			if (retVal == null)
				logger.error("Could not load engine "+engineName+" with the "+adapterTypeName+" adapter.");
			return retVal;
		}
		
		
		if (tryToLoadEngine(engineName, "web")) {
			return engineName2adapter.get(engineName);
		}
		
		if (tryToLoadEngine(engineName, "staticjava")) {
			return engineName2adapter.get(engineName);
		}
		
		if (tryToLoadEngine(engineName, "dll")) {
			return engineName2adapter.get(engineName);			
		}
		
/*		Set<Class<? extends lv.lumii.tda.raapi.IEngineAdapter>> classes = TDAKernel.getReflections().getSubTypesOf(lv.lumii.tda.raapi.IEngineAdapter.class);
		
		for (Class<? extends lv.lumii.tda.raapi.IEngineAdapter> c : classes) {
			//if (DEBUG) System.err.println("Engine adapter found "+c.getName());
			adapter = createAndLoadEngineAdapter(c.getName(), engineName);
			if (adapter != null) {
				delegator2.engineBeingLoaded = null;
				return adapter;
			}
		}*/
		
		
		return null;			
	}
	
	/**
	 * Creates new or returns an existing transformation adapter for the given transformation type (transformation language).
	 * 
	 * @param transformationType the transformation adapter type (corresponding to a transformation language), e.g., "lquery", "atl", "mola"
	 * @return a transformation adapter for the given transformation type (transformation language).
	 */
	public ITransformationAdapter getTransformationAdapter(String transformationType)
	{
		ITransformationAdapter adapter = transformationType2adapter.get(transformationType);
		if (adapter != null)
			return adapter;
		
		adapter = newTransformationAdapter(transformationType);
		if (adapter == null)
			return null;
		
		if (adapter.load(this)) {
			transformationType2adapter.put(transformationType, adapter);
			return adapter;
		}
		
		return null;			
	}
	
	private void cleanupAdapters()
	{
		for (IEngineAdapter adapter : reverseOrderedEngineAdapters) {
			adapter.unload();
		}
		
		for (ITransformationAdapter adapter : transformationType2adapter.values()) {
			adapter.unload();
		}
		
		transformationType2adapter.clear();
		engineName2adapter.clear();
		reverseOrderedEngineAdapters.clear();
	}
	
	///// FOR ADDING CACHE THAT IS ATTACHED TO THIS TDA KERNEL AND FREED BY JVM WHEN THE KERNEL IS CLOSED /////
	private Map<String, Object> cacheMap = new HashMap<String, Object>();
	public void storeCache(String id, Object cache) {
		if (mode == Mode.CLOSED_MODE) {
			return;
		}		
		
		if (cache == null)
			cacheMap.remove(id);
		else
			cacheMap.put(id, cache);
	}

	public void clearCache(String id) {
		if (mode == Mode.CLOSED_MODE) {
			return;
		}		
		
		cacheMap.remove(id);
	}

	public Object retrieveCache(String id) {
		if (mode == Mode.CLOSED_MODE) {
			return null;
		}		
		return cacheMap.get(id);
	}
	
	///// FOR WEB SUPPORT /////
	private RAAPI_Synchronizer synchronizer = null;
	
	///// MODE /////
	private enum Mode {		
		CLOSED_MODE,
		OPEN_REPOSITORY_MODE, // TDA Kernel works just as a repository, without events, commands, or TDA Kernel metamodel
		OPEN_TDA_MODE, // inserts TDA Kernel metamodel, handles events and commands
		SAVING_MODE
	}
	private Mode mode = Mode.CLOSED_MODE;

	///// TDA KERNEL METAMODEL /////
	public lv.lumii.tda.kernel.mm.TDAKernelMetamodelFactory KMM = new lv.lumii.tda.kernel.mm.TDAKernelMetamodelFactory();
	public lv.lumii.tda.ee.mm.EnvironmentEngineMetamodelFactory EEMM = new lv.lumii.tda.ee.mm.EnvironmentEngineMetamodelFactory();
	
	///// DELEGATE REPOSITORY /////
	private TDAKernelDelegate delegate = null;
	public IRepository getDelegate()
	{
		return delegate;
	}

	
	
	/**
	 * Opens the repository (using the repository adapter specified in the location prefix), but
	 * does not initialize TDA.
	 * @param locationWithAdapterType repository location, e.g., ecore:c:\\my_project\\myrepo.xmi
	 * @return whether the operation succeeded
	 */
	public boolean open(String locationWithAdapterType) {
		if (mode != Mode.CLOSED_MODE) {
			logger.error("Repository "+locationWithAdapterType+" is already open "+mode.toString());
			return false;
		}
		
		String adapterType = getAdapterTypeFromURI(locationWithAdapterType);
		
		
		IRepository r2 = newRepositoryAdapter(adapterType, this);
		if (r2 == null) {
			return false; // could not get the adapter
		}
		
		if (!(r2 instanceof RAAPI_WR))
			r2 = new DelegatorToRepositoryWithWritableReferences(r2);
		delegate = new TDAKernelDelegate(hook, r2, this);
		
		String location = getLocationFromURI(locationWithAdapterType);				
		
		if (!delegate.open(location)) {
			delegate = null;
			return false;
		}
		
		mode = Mode.OPEN_REPOSITORY_MODE;
		return true;
	}
	

	/**
	 * Checks whether the repository (using the repository adapter specified in the location prefix) exists.
	 * @param locationWithAdapterType repository location, e.g., ecore:c:\\my_project\\myrepo.xmi
	 * @return whether the repository exists
	 */
	public boolean exists(String locationWithAdapterType) {
		
		String adapterType = getAdapterTypeFromURI(locationWithAdapterType);
		
		IRepository d = newRepositoryAdapter(adapterType, this);
		if (d == null) {
			return false; // could not get the adapter
		}
		
		String location = getLocationFromURI(locationWithAdapterType);
		
		return d.exists(location);
	}
	
	
	/**
	 * Switches the currently open repository to TDA mode (TDAKernel metamodel is being added, events and commands are processed).
	 * @return whether the operation succeeded
	 */
	public boolean upgradeToTDA(boolean bootstrap, String login, boolean light)
	{
		if (mode != Mode.OPEN_REPOSITORY_MODE) {
			logger.error("A repository must be in OPEN_REPOSITORY_MODE");
			return false;
		}
		
		try {
			KMM.setRAAPI(this, "", true);
		} catch (ElementReferenceException e) {
			logger.error("Could not load TDA Kernel Metamodel.\n"+e.toString());
			return false;
		}

/*		if (!ForegroundThread.inForegroundThread()) {
			logger.error("You must be in the ForegroundThread to upgrade to TDA");
		}
		
		if ((login!=null) && (!login.equals(ForegroundThread.getActiveLogin()))) {
			logger.error("You must be in the ForegroundThread with login="+login+" (but the active login is "+ForegroundThread.getActiveLogin()+")");			
		}*/
		// Initializing TDA Kernel Metamodel with initial data...
		lv.lumii.tda.kernel.mm.TDAKernel kernelObj = lv.lumii.tda.kernel.mm.TDAKernel.firstObject(KMM);
		if (kernelObj == null)
			kernelObj = KMM.createTDAKernel();

		lv.lumii.tda.kernel.mm.Submitter submitterObj;
		Iterator<lv.lumii.tda.kernel.mm.Submitter> it = (Iterator<Submitter>) lv.lumii.tda.kernel.mm.Submitter.allObjects(KMM).iterator();
		if (it.hasNext()) {
			submitterObj = it.next();
			while (it.hasNext()) {
				it.next().delete();
			}
		}
		else
			submitterObj = KMM.createSubmitter();
		
		//lv.lumii.tda.kernel.mm.Submitter.deleteAllObjects(KMM);
		//lv.lumii.tda.kernel.mm.Submitter submitterObj = KMM.createSubmitter();
		
		
		delegate.setKMM(KMM);

		
		delegate.setEngineBeingLoaded("EnvironmentEngine");
		try {
			EEMM.setRAAPI(this, "", true);
		} catch (lv.lumii.tda.ee.mm.EnvironmentEngineMetamodelFactory.ElementReferenceException e) {
			logger.error("Could not load Environment Engine Metamodel.\n"+e.toString());
			delegate.setEngineBeingLoaded(null);
			KMM.unsetRAAPI();
			return false;
		}
		delegate.setEngineBeingLoaded(null);
		
		
		
		lv.lumii.tda.ee.mm.EnvironmentEngine eeObj = lv.lumii.tda.ee.mm.EnvironmentEngine.firstObject(EEMM);
		if (eeObj == null)
			eeObj = EEMM.createEnvironmentEngine();
		
		if ((kernelObj == null) || (submitterObj == null) || (eeObj==null)) {
			logger.error("Could not find/create the TDAKernel object, the Submitter object, and/or the Environment Engine object");
			KMM.unsetRAAPI();
			EEMM.unsetRAAPI();
			return false;
		}
		
		
		if (light) {
			mode = Mode.OPEN_TDA_MODE;
			return true;
		}
		
		
		// set projectDirectory and other TDA-related stuff
		eeObj.setLanguage(System.getProperty("user.language"));
		eeObj.setCountry(System.getProperty("user.country"));
		eeObj.setAnyUnsavedChanges(false);
		/*eeObj.setCommonBinDirectory(properties.getProperty("commonBinDirectory"));
		eeObj.setSpecificBinDirectory(properties.getProperty("specificBinDirectory"));
		eeObj.setProjectDirectory(properties.getProperty("projectDirectory"));
		eeObj.setCloudLocation(properties.getProperty("cloudLocation"));
		eeObj.setClientSessionId(properties.getProperty("clientSessionId"));
		try {
			eeObj.setClientActionIndex(Integer.parseInt(properties.getProperty("clientActionIndex")));
		}
		catch(Throwable t) {
		}*/
		
		// TODO: check client action index

		lv.lumii.tda.ee.mm.Option.deleteAllObjects(EEMM);
		lv.lumii.tda.ee.mm.Frame.deleteAllObjects(EEMM);
		
		lv.lumii.tda.kernel.mm.Command.deleteAllObjects(KMM);
		lv.lumii.tda.kernel.mm.Event.deleteAllObjects(KMM);
		
		/*
		
		if (bootstrap) {
			// Attaching ourself (EnvironmentEngine)...
			// This will initialize EEMM (Environment Engine Metamodel).
			
			lv.lumii.tda.kernel.mm.AttachEngineCommand attachCmd = KMM.createAttachEngineCommand();
			attachCmd.setName("EnvironmentEngine");
			attachCmd.submit();
			
			// Issuing ProjectCreatedEvent...
//			EEMM.createProjectCreatedEvent().submit();
			
		}
		else {
			
			lv.lumii.tda.kernel.mm.AttachEngineCommand attachCmd = KMM.createAttachEngineCommand();
			attachCmd.setName("EnvironmentEngine");
			attachCmd.submit();
			

			// storing the project directory in the EnvironmentEngine singleton instance...
			// Issuing ProjectOpenedEvent...
			// Engines that have already been attached when bootstrapping will be loaded automatically.
//					EEMM.createProjectOpenedEvent().submit();			
		}

*/
		mode = Mode.OPEN_TDA_MODE;
		return true;
	}

	/**
	 * Instructs the underlying model repository to switch to references, where the last
	 * predefinedBitsCount bits are of certain values predefinedBitsValues. Useful, when many clients
	 * access the same repository to avoid reference collisions.
	 * @param predefinedBitsCount how many bits for kernel references will be predefined from now (at least one bit, e.g., for even references)
	 * @param predefinedBitsValues the values of predefined bits (e.g., 0 for even references or 1 for odd, in case of 1 bit)
	 * @return whether the operation succeeded
	 */
	public synchronized boolean setPredefinedBits(int predefinedBitsCount, long predefinedBitsValues) {
		if (!(delegate instanceof RAAPI_WR)) {
			logger.error("The underlying repository does not support writable references, which are essential for setPredefinedBits. Use a repository that implements RAAPI_WR!");
			return false;
		}
		
		if (! ((RAAPI_WR)delegate).setPredefinedBits(predefinedBitsCount, predefinedBitsValues) ) {
			logger.error("Could not set predefined "+predefinedBitsCount+" bits!");
			return false;			
		}
		
		return true;
	}
	/**
	 * Sets the synchronization object, which will be used to sync this repository actions with some other module (e.g., web client). 
	 * @param _synchronizer an object that listens for repository modificating actions and synchronizes them
	 * @param syncExistingContentNow if the repository is being open for the first time (i.e., not re-open), set this parameter to true
	 * @param predefinedSynchronizerBitsValues the values of predefined bits for the synchronizer's end point (will be synchronized with max reference and current predefined bits count);
	 *  	ignored, if syncExistingContentNow is false
	 * @return a RAAPI_WR pointer that can be used to sync other module's actions with this repository
	 */
	public synchronized RAAPI_WR attachSynchronizer(RAAPI_Synchronizer _synchronizer, boolean syncExistingContentNow, long predefinedSynchronizerBitsValues) {
		if (_synchronizer == null)
			return null;
		if (!(delegate instanceof RAAPI_WR)) {
			logger.error("The underlying repository does not support writable references, which are essential for synchronization. Use a repository that implements RAAPI_WR!");
			return null;
		}
		
		int bits = ((RAAPI_WR)delegate).getPredefinedBitsCount();
		
		if ((1L << bits) < predefinedSynchronizerBitsValues) {		
			logger.error("The number of predefined bits is not sufficient for the given predefined bits values. Call setPredefinedBits with appropriate predefined bits count!");
			return null;			
		}
		
		RAAPI_Synchronizer synchronizer = _synchronizer;
		
		if (syncExistingContentNow && _synchronizer!=null) {
			
			assert (delegate instanceof RAAPI_Synchronizable);
			delegate.syncAll(synchronizer, predefinedSynchronizerBitsValues);
		} // if sync
		
		this.synchronizer = _synchronizer;
		return (RAAPI_WR)delegate;
			// we return delegate, thus, actions synchronized from the client won't be synchronized from the TDAKernel server back to the client
			// (i.e., we skip TDAKernel synchronization layer) 
	}
	
	
	public synchronized RAAPI_WR sync(RAAPI_Synchronizer s, long predefinedSynchronizerBitsValues) {
		if (s == null)
			return null;
		if (!(delegate instanceof RAAPI_WR)) {
			logger.error("The underlying repository does not support writable references, which are essential for synchronization. Use a repository that implements RAAPI_WR!");
			return null;
		}

		int bits = ((RAAPI_WR)delegate).getPredefinedBitsCount();
		
		if ((1L << bits) < predefinedSynchronizerBitsValues) {		
			logger.error("The number of predefined bits is not sufficient for the given predefined bits values. Call setPredefinedBits with appropriate predefined bits count!");
			return null;			
		}
		
		
		assert (delegate instanceof RAAPI_Synchronizable);

		delegate.syncAll(s, predefinedSynchronizerBitsValues);
		return (RAAPI_WR)delegate;
	}
	
	public synchronized RAAPI_Synchronizer getSynchronizer() {
		return synchronizer;
	}
	
	public void close()
	{
		
		if (mode == Mode.CLOSED_MODE) {
			return;
		}		
		
		if (mode == Mode.SAVING_MODE) {
			logger.error("Close during saving.");			
		}		

		if (mode == Mode.OPEN_TDA_MODE) {
			EEMM.createProjectClosingEvent().submit();
			
			KMM.unsetRAAPI();
			delegate.setKMM(null);
		}
		
		if (synchronizer!=null) 
			synchronizer.flush();
		synchronizer = null;
		
		cleanupAdapters();
		getDelegate().close();
		cacheMap.clear();
		
		
		mode = Mode.CLOSED_MODE;
		System.gc();
	}
	
	
	public boolean drop(String locationWithAdapterType) {
		String adapterType = getAdapterTypeFromURI(locationWithAdapterType);
		
		IRepository r2 = newRepositoryAdapter(adapterType, this);
		if (r2 == null) {
			return false; // could not get the adapter
		}
		
		String location = getLocationFromURI(locationWithAdapterType);				
		
		if (!r2.drop(location)) {
			return false;
		}
		
		return true;
	}
	
	// repository: just JR (but in function - to be replaced later)
	
	// repository: create(kernel) -> create()
	
	
	///// SYNCHRONIZING MODIFICATING ACTIONS /////
	
	@Override
	public long createClass (String name)
	{
		long retVal = getDelegate().createClass(name);
		if ((retVal!=0) && (synchronizer!=null))
			synchronizer.syncCreateClass(name, retVal);
		return retVal;		
	}
	
	@Override
	public boolean deleteClass(long r)
	{
		boolean retVal = getDelegate().deleteClass(r);
		if ((retVal) && (synchronizer!=null))
			synchronizer.syncDeleteClass(r);
		return retVal;		
	}
	
	@Override
	public boolean createGeneralization (long rSubClass, long rSuperClass)
	{
		boolean retVal = getDelegate().createGeneralization(rSubClass, rSuperClass);
		if ((retVal) && (synchronizer!=null))
			synchronizer.syncCreateGeneralization(rSubClass, rSuperClass);
		return retVal;
	}
		
	@Override
	public boolean deleteGeneralization(long rSubClass, long rSuperClass)
	{
		boolean retVal = getDelegate().deleteGeneralization(rSubClass, rSuperClass);
		if ((retVal) && (synchronizer!=null))
			synchronizer.syncDeleteGeneralization(rSubClass, rSuperClass);
		return retVal;		
	}

	@Override
	public long createObject(long rClass) {
		long retVal = getDelegate().createObject(rClass);
		if ((retVal!=0) && (synchronizer!=null))
			synchronizer.syncCreateObject(rClass, retVal);
		return retVal;				
	}
	
	@Override
	public boolean deleteObject(long r) {
		boolean retVal = getDelegate().deleteObject(r);
		if ((retVal) && (synchronizer!=null))
			synchronizer.syncDeleteObject(r);
		return retVal;				
	}
	
	@Override
	public boolean includeObjectInClass(long rObject, long rClass) {
		boolean retVal = getDelegate().includeObjectInClass(rObject, rClass);
		if ((retVal) && (synchronizer!=null))
			synchronizer.syncIncludeObjectInClass(rObject, rClass);
		return retVal;		
	}

	@Override
	public boolean excludeObjectFromClass(long rObject, long rClass) {
		boolean retVal = getDelegate().excludeObjectFromClass(rObject, rClass);
		if ((retVal) && (synchronizer!=null))
			synchronizer.syncExcludeObjectFromClass(rObject, rClass);
		return retVal;		
	}

	@Override
	public boolean moveObject (long rObject, long rToClass)
	{
		boolean retVal = getDelegate().moveObject(rObject, rToClass);
		if ((retVal) && (synchronizer!=null))
			synchronizer.syncMoveObject(rObject, rToClass);
		return retVal;		
	}

	@Override
	public long createAttribute(long rClass, String name, long type) {
		long retVal = getDelegate().createAttribute(rClass, name, type);
		if ((retVal!=0) && (synchronizer!=null))
			synchronizer.syncCreateAttribute(rClass, name, type, retVal);
		return retVal;				
	}
	
	@Override
	public boolean deleteAttribute(long r) {
		boolean retVal = getDelegate().deleteAttribute(r);
		if ((retVal) && (synchronizer!=null))
			synchronizer.syncDeleteAttribute(r);
		return retVal;				
	}

	@Override
	public boolean setAttributeValue(long rObject, long rAttribute, String value) {
		String oldValue = getDelegate().getAttributeValue(rObject, rAttribute);
		boolean retVal = getDelegate().setAttributeValue(rObject, rAttribute, value);
		if ((retVal) && (synchronizer!=null))
			synchronizer.syncSetAttributeValue(rObject, rAttribute, value, oldValue);
		return retVal;				
		
	}
	
	@Override
	public boolean deleteAttributeValue(long rObject, long rAttribute) {
		boolean retVal = getDelegate().deleteAttributeValue(rObject, rAttribute);
		if ((retVal) && (synchronizer!=null))
			synchronizer.syncDeleteAttributeValue(rObject, rAttribute);
		return retVal;				
		
	}

	@Override
	public long createAssociation (long rSourceClass, long rTargetClass, String sourceRoleName, String targetRoleName, boolean isComposition) 
	{
		long retVal = getDelegate().createAssociation(rSourceClass, rTargetClass, sourceRoleName, targetRoleName, isComposition);
		if ((retVal!=0) && (synchronizer!=null))
			synchronizer.syncCreateAssociation(rSourceClass, rTargetClass, sourceRoleName, targetRoleName, isComposition, retVal, getDelegate().getInverseAssociationEnd(retVal));
		return retVal;				
	}

	@Override
	public long createDirectedAssociation (long rSourceClass, long rTargetClass, String targetRoleName, boolean isComposition) 
	{
		long retVal = getDelegate().createDirectedAssociation(rSourceClass, rTargetClass, targetRoleName, isComposition);
		if ((retVal!=0) && (synchronizer!=null))
			synchronizer.syncCreateDirectedAssociation(rSourceClass, rTargetClass, targetRoleName, isComposition, retVal);
		return retVal;				
	}
	
	@Override
	public long createAdvancedAssociation(String name, boolean nAry, boolean associationClass)
	{		
		long retVal = getDelegate().createAdvancedAssociation(name, nAry, associationClass);
		if ((retVal!=0) && (synchronizer!=null))
			synchronizer.syncCreateAdvancedAssociation(name, nAry, associationClass, retVal);
		return retVal;				
	}
	
	@Override
	public boolean deleteAssociation(long r) {
		boolean retVal = getDelegate().deleteAssociation(r);
		if ((retVal) && (synchronizer!=null))
			synchronizer.syncDeleteAssociation(r);
		return retVal;				
	}

	public boolean creatingSubmitLink(long rSourceObject, long rTargetObject, long rAssociationEnd) {
		return delegate.creatingSubmitLink(rSourceObject, rTargetObject, rAssociationEnd);
	}
	
	public boolean isSubmitter(long r) {
		return delegate.isSubmitter(r);
	}
	
	public boolean isEvent(long r) {
		return delegate.isEvent(r);
	}
	
	public boolean isCommand(long r) {
		return delegate.isCommand(r);
	}
	
	@Override
	public boolean createLink (long rSourceObject, long rTargetObject, long rAssociationEnd)
	{
		// TODO: via creatingSubmitLink
		boolean retVal = getDelegate().createLink(rSourceObject, rTargetObject, rAssociationEnd);
		
		if ((retVal) && (synchronizer!=null)) {
			if (linkExists(rSourceObject, rTargetObject, rAssociationEnd))
				synchronizer.syncCreateLink(rSourceObject, rTargetObject, rAssociationEnd);
			// do not synchronize command/event links for already processed commands/events
		}
		return retVal;						
	}

	@Override
	public boolean createOrderedLink (long rSourceObject, long rTargetObject, long rAssociationEnd, int position)
	{
		boolean retVal = getDelegate().createOrderedLink(rSourceObject, rTargetObject, rAssociationEnd, position);
		if ((retVal) && (synchronizer!=null)) {
			if (linkExists(rSourceObject, rTargetObject, rAssociationEnd))
				synchronizer.syncCreateOrderedLink(rSourceObject, rTargetObject, rAssociationEnd, position);
			// do not synchronize command/event links for already processed commands/events
		}
		return retVal;						
	}

	@Override
	public boolean deleteLink (long rSourceObject, long rTargetObject, long rAssociationEnd) {
		boolean retVal = getDelegate().deleteLink(rSourceObject, rTargetObject, rAssociationEnd);
		if ((retVal) && (synchronizer!=null))
			synchronizer.syncDeleteLink(rSourceObject, rTargetObject, rAssociationEnd);
		return retVal;						
	}

	@Override
	public boolean launchMainTransformation(long rArgument) {
		if (mainTransformation == null)
			return false;
		int i = mainTransformation.indexOf(':');
		ITransformationAdapter adapter = this.getTransformationAdapter(mainTransformation.substring(0, i));
		if (adapter == null)
			return false;
		
 		return adapter.launchTransformation(mainTransformation.substring(i+1), rArgument);
	}

	@Override
	public int tryToExecuteKernelCommand(long rCommand) {
		RAAPI raapi = this;
		
		String className = RAAPIHelper.getObjectClassName(raapi, rCommand);
		
		if (className == null) {
			logger.error("TDA Kernel was unable to try to execute command "+rCommand+" since class name is null");
			return -1;
		}
		
		if (className.equals("TDAKernel::AttachEngineCommand")) {
			long rCls = RAAPIHelper.getObjectClass(raapi, rCommand);
			long rAttr = raapi.findAttribute(rCls, "name");
			if (rAttr == 0) {
				raapi.freeReference(rCls);
				logger.error("Attribute 'name' not found in class TDAKernel::AttachEngineCommand.");
				return -1;
			}
			String engineName = raapi.getAttributeValue(rCommand, rAttr);
			raapi.freeReference(rCls);
			raapi.freeReference(rAttr);
			
			boolean retVal = (this.getEngineAdapter(engineName) != null);
			
			if (retVal) {
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
				
			}
			
			return retVal?+1:-1;			
		}
		
		if (className.equals("TDAKernel::InsertMetamodelCommand")) {
			long rCls = RAAPIHelper.getObjectClass(raapi, rCommand);
			long rAttr = raapi.findAttribute(rCls, "url");
			if (rAttr == 0) {
				raapi.freeReference(rCls);
				logger.error("Attribute 'url' not found in class TDAKernel::InsertMetamodelCommand.");
				return -1;
			}
			String url_str = raapi.getAttributeValue(rCommand, rAttr);
			raapi.freeReference(rCls);
			raapi.freeReference(rAttr);
			
			try {
				StringBuffer errorMessages = new StringBuffer();
				boolean retVal = MetamodelInserter.insertMetamodel(new java.net.URL(url_str), raapi, errorMessages);
				if (!retVal || (errorMessages.length()>0))
					logger.error("Error inserting metamodel at "+url_str+"\n"+errorMessages.toString());
				return retVal?+1:-1;
			} catch (MalformedURLException e) {
				logger.error("TDA Kernel: Error executing InsertMetamodelCommand. "+e.getMessage());
				return -1;
			}
		}

		/*if (className.equals("TDAKernel::MountRepositoryCommand")) {
			long rCls = getObjectClass(raapi, r);
			long rAttr = raapi.findAttribute(rCls, "uri");
			if (rAttr == 0) {
				raapi.freeReference(rCls);
				System.err.println("Attribute uri of class TDAKernel::MountRepositoryCommand not found.");
				return false;
			}
			String uri = raapi.getAttributeValue(r, rAttr);
			raapi.freeReference(rAttr);
			
			rAttr = raapi.findAttribute(rCls, "mountPoint");
			if (rAttr == 0) {
				raapi.freeReference(rCls);
				System.err.println("Attribute mountPoint of class TDAKernel::MountRepositoryCommand not found.");
				return false;
			}
			String mountPoint = raapi.getAttributeValue(r, rAttr);
			raapi.freeReference(rAttr);
			
			raapi.freeReference(rCls);

			if (((Delegator6WithProxyReferences)getKernel().delegators[6]).mountRepository(uri, mountPoint)) {				
				return true;
			}
			else {
				System.err.println("TDA Kernel: Error executing MountRepositoryCommand (uri="+uri+", mountPoint="+mountPoint+").");
				return false;
			}
		}

		if (className.equals("TDAKernel::UnmountRepositoryCommand")) {
			long rCls = getObjectClass(raapi, r);
			
			long rAttr = raapi.findAttribute(rCls, "mountPoint");
			if (rAttr == 0) {
				raapi.freeReference(rCls);
				System.err.println("Attribute mountPoint of class TDAKernel::UnmountRepositoryCommand not found.");
				return false;
			}
			String mountPoint = raapi.getAttributeValue(r, rAttr);
			raapi.freeReference(rAttr);
			
			raapi.freeReference(rCls);

			if (((Delegator6WithProxyReferences)getKernel().delegators[6]).unmountRepository(mountPoint)) {				
				return true;
			}
			else {
				System.err.println("TDA Kernel: Error executing UnmountRepositoryCommand (mountPoint="+mountPoint+").");
				return false;
			}
		}*/
		
		long rLaunchTransformationCommand = raapi.findClass("TDAKernel::LaunchTransformationCommand"); 
		if (raapi.isKindOf(rCommand, rLaunchTransformationCommand)) {
			raapi.freeReference(rLaunchTransformationCommand);
			long rCls = RAAPIHelper.getObjectClass(raapi, rCommand);
			long rAttr = raapi.findAttribute(rCls, "uri");
			if (rAttr == 0) {
				raapi.freeReference(rCls);
				logger.error("Attribute 'uri' not found in class TDAKernel::LaunchTransformationCommand (or in its descendants).");
				return -1;
			}
			String transformationName = raapi.getAttributeValue(rCommand, rAttr);
			raapi.freeReference(rAttr);
			raapi.freeReference(rCls);
			
		
			String type = TDAKernel.getAdapterTypeFromURI(transformationName);
			final String location = TDAKernel.getLocationFromURI(transformationName);
			
			if (type == null) {
				logger.error("The transformation name "+transformationName+" is not in TDA 2 format.");
				return -1;
			}

			long _transformationArgument;
			String oldType = type;
			int i = type.indexOf('(');
			if ((i>=0) && type.endsWith(")")) {
				long parsed;
				try {
					parsed = Long.parseLong(type.substring(i+1, type.length()-1));
				}
				catch (Throwable t) {
					parsed = 0;
				}
				_transformationArgument = parsed;
				type = type.substring(0, i);
			}
			else
				_transformationArgument = rCommand;			
			
			long it =raapi.getIteratorForDirectObjectClasses(rCommand);
			long rrr = raapi.resolveIteratorFirst(it);
			raapi.freeIterator(it);
			
			it = raapi.getIteratorForDirectObjectClasses(_transformationArgument);
			rrr = raapi.resolveIteratorFirst(it);
			raapi.freeIterator(it);
			
			
			final long transformationArgument = _transformationArgument;
			
			final ITransformationAdapter adapter = this.getTransformationAdapter(type);
			if (adapter == null) {
				return -1;
			}
			
			return adapter.launchTransformation(location, transformationArgument)?+1:-1;
		}
		else
			raapi.freeReference(rLaunchTransformationCommand);
		
		long rLaunchTransformationInBackgroundCommand = raapi.findClass("TDAKernel::LaunchTransformationInBackgroundCommand"); 		
		if (raapi.isKindOf(rCommand, rLaunchTransformationInBackgroundCommand)) {
			raapi.freeReference(rLaunchTransformationInBackgroundCommand);
			long rCls = RAAPIHelper.getObjectClass(raapi, rCommand);
			long rAttr = raapi.findAttribute(rCls, "uri");
			if (rAttr == 0) {
				raapi.freeReference(rCls);
				logger.error("Attribute 'uri' not found in class TDAKernel::LaunchTransformationCommand (or in its descendants).");
				return -1;
			}
			String transformationName = raapi.getAttributeValue(rCommand, rAttr);
			raapi.freeReference(rAttr);
			raapi.freeReference(rCls);
			
		
			String type = TDAKernel.getAdapterTypeFromURI(transformationName);
			final String location = TDAKernel.getLocationFromURI(transformationName);
			
			if (type == null) {
				logger.error("The transformation name "+transformationName+" is not in TDA 2 format.");
				return -1;
			}

			final long transformationArgument;
			int i = type.indexOf('(');
			if ((i>=0) && type.endsWith(")")) {
				long parsed;
				try {
					parsed = Long.parseLong(type.substring(i+1, type.length()-1));
				}
				catch (Throwable t) {
					parsed = 0;
				}
				transformationArgument = parsed;
				type = type.substring(0, i);
			}
			else
				transformationArgument = rCommand;			
			
			final ITransformationAdapter adapter = this.getTransformationAdapter(type);
			if (adapter == null) {
				return -1;
			}
			
			
			new Thread() {
				public void run() {
					boolean result = adapter.launchTransformation(location, transformationArgument);
					if (!result)
						logger.error("launchTransformation for the background transformation `"+location+"' returned false");
					raapi.deleteObject(transformationArgument);
				}
			}.start();
			return +1;
		}
		else
			raapi.freeReference(rLaunchTransformationInBackgroundCommand);

		return 0; // non-TDAKernel command
	}

	@Override
	public int tryToHandleKernelEvent(long rEvent) {
		RAAPI raapi = this;
		String className = RAAPIHelper.getObjectClassName(raapi, rEvent);
		if (className == null)
			return -1;
		
		try {			
			if (className.equals("ProjectOpenedEvent")) {
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
					
					if (this.getEngineAdapter(engineName) == null) {
						logger.error("Could not load attached engine "+engineName);
						raapi.freeReference(rEngineObj);
						raapi.freeIterator(it);
						return 0;
					}
					
					raapi.freeReference(rEngineObj);
					rEngineObj = raapi.resolveIteratorNext(it);
				}
				raapi.freeIterator(it);
					
				return 0; // returning that this is not TDA Kernel event
			}
	
			return 0; // returning that this is not TDA Kernel event
		
		}
		catch(Throwable t) {
			logger.error("Error trying to handle TDA Kernel event - "+t.getMessage());
			return 0;
		}
		
	}

	@Override
	public String getEngineForEventOrCommand(String eventOrCommandName) {
		if (eventOrCommandName == null)
			return null;
		RAAPI raapi = this;
		long rKernelClass = raapi.findClass("TDAKernel::TDAKernel");
		if (rKernelClass == 0) {
			return null;
		}
		long rAttr = raapi.findAttribute(rKernelClass, "engineFor"+eventOrCommandName);
		if (rAttr == 0) {
			raapi.freeReference(rKernelClass);			
			return null;
		}
				
		long it = raapi.getIteratorForAllClassObjects(rKernelClass);
		if (it == 0) {
			raapi.freeReference(rAttr);
			raapi.freeReference(rKernelClass);
			return null;			
		}
		raapi.freeReference(rKernelClass);			
		
		long rKernel = raapi.resolveIteratorFirst(it);
		raapi.freeIterator(it);
		if (rKernel == 0) {
			raapi.freeReference(rAttr);
			return null;			
		}
		
		String engineName = raapi.getAttributeValue(rKernel, rAttr);
		raapi.freeReference(rAttr);
		raapi.freeReference(rKernel);

		return engineName;
	}

	@Override
	public String[] getEventHandlers(long rEvent) {
		String transformationName = null;
		
		RAAPI raapi = this;
		
		String className = RAAPIHelper.getObjectClassName(raapi, rEvent);
		if (className == null)
			return null;
		
		String shortClassName = className.substring( className.lastIndexOf(':')+1 );
		
		// searching on<EventName> in the context of the event...
		long rEventClass = RAAPIHelper.getObjectClass(raapi, rEvent);
		if (rEventClass != 0) {				
			long it = raapi.getIteratorForAllOutgoingAssociationEnds(rEventClass);
			if (it != 0) {
				long rAssoc = raapi.resolveIteratorFirst(it);
				while (rAssoc != 0) {
					
					long rCls2 = raapi.getTargetClass(rAssoc);
					long rAttr2 = raapi.findAttribute(rCls2, "on"+shortClassName);
					if ((rCls2 !=0) && (rAttr2 != 0)) {
					
						long itLinked = raapi.getIteratorForLinkedObjects(rEvent, rAssoc);
						long r2 = raapi.resolveIteratorFirst(itLinked);
						while (r2 != 0) {
							String val = raapi.getAttributeValue(r2, rAttr2);
							if (val != null)
								transformationName = val;
							
							raapi.freeReference(r2);
							if (transformationName != null)
								break;
							r2 = raapi.resolveIteratorNext(itLinked);
						}
						raapi.freeIterator(itLinked);
					}
					
					raapi.freeReference(rCls2);
					raapi.freeReference(rAttr2);
					
					raapi.freeReference(rAssoc);
					if (transformationName != null)
						break;
					rAssoc = raapi.resolveIteratorNext(rAssoc);
				}
				raapi.freeIterator(it);					
			}				
			raapi.freeReference(rEventClass);
		}
									
		
		// searching on<EventName> in the corresponding engine...
		if (transformationName == null) {
			String engineName = this.getEngineForEventOrCommand(shortClassName);
			if (engineName == null) {
				if (shortClassName.equals("SaveStartedEvent") || shortClassName.equals("SaveFinishedEvent") || shortClassName.equals("SaveFailedEvent"))
					engineName = "TDAKernel::TDAKernel";
				else					
					return null;
			}
					
			long rEngine = RAAPIHelper.getSingletonObject(raapi, engineName);
			if (rEngine == 0)
				return null;
			
			long rCls = RAAPIHelper.getObjectClass(raapi, rEngine);
			long rAttr = raapi.findAttribute(rCls, "on"+shortClassName);
			raapi.freeReference(rCls);
			if (rAttr == 0) {
				raapi.freeReference(rEngine);
				return null;
			}
			
			transformationName = raapi.getAttributeValue(rEngine, rAttr);
			raapi.freeReference(rEngine);
			raapi.freeReference(rAttr);
		}
		
		// TODO: search other possible places, where on<EventName> attribute can be found		
		
		if (transformationName != null) {
			ArrayList<String> arr = new ArrayList<String>();
			StringTokenizer tknz = new StringTokenizer(transformationName, ",;");
			while (tknz.hasMoreTokens()) {
			
				transformationName = tknz.nextToken();
				
				// legacy fix:
				if (transformationName.startsWith("lua_engine#lua."))
					transformationName = "lua:"+transformationName.substring(15);
				
				arr.add(transformationName);
			}
			
			return arr.toArray(new String[] {});
		}			
		else
			return null; // no transformation was assigned; nothing needed to be called
	}

	@Override
	public long replicateEventOrCommand(long rEvent) {
		return TDACopier.copyObject(this, rEvent);
	}
	
	
	public void setEventsCommandsHook(IEventsCommandsHook _hook) {
		hook = _hook;
		delegate.setEventsCommandsHook(hook);
	}
	
	public IEventsCommandsHook getEventsCommandsHook() {
		return hook;
	}
}
