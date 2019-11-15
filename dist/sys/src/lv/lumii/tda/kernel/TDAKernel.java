package lv.lumii.tda.kernel;

import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.io.*;
import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
//import org.webappos.util.ForegroundThread;
//import org.webappos.web.server.Config;
import org.webappos.webmem.IWebMemory;
import org.webappos.webmem.WebMemoryContext;

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

public class TDAKernel extends DelegatorToRepositoryBase implements IWebMemory
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
	
	
	private WebMemoryContext webmemContext = null;
	
	
	private IEventsCommandsHook hook = null;	
		
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
	
	public void setContext(WebMemoryContext _ctx) {
		this.webmemContext = _ctx;
	}
	
	public WebMemoryContext getContext() {
		return this.webmemContext;
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
	
	@Override
	public void flush() {
		if (synchronizer!=null)
			synchronizer.flush();
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
		if (value==null) {
			if (oldValue==null)
				return true;
		}
		else
			if (value.equals(oldValue))
				return true;
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

	public void setEventsCommandsHook(IEventsCommandsHook _hook) {
		hook = _hook;
		delegate.setEventsCommandsHook(hook);
	}
	
	public IEventsCommandsHook getEventsCommandsHook() {
		return hook;
	}

}
