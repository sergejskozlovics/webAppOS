// automatically generated
package lv.lumii.tda.kernel.mm;
import lv.lumii.tda.raapi.RAAPI;
import java.util.*;

public class TDAKernelMetamodelFactory
{
	// for compatibility with ECore
	public static TDAKernelMetamodelFactory eINSTANCE = new TDAKernelMetamodelFactory();

	HashMap<Long, RAAPIReferenceWrapper> wrappers =
		new HashMap<Long, RAAPIReferenceWrapper>();

	public RAAPIReferenceWrapper findOrCreateRAAPIReferenceWrapper(Class<? extends RAAPIReferenceWrapper> cls, long rObject, boolean takeReference)
	// if takeReference==true, takes care about freeing rObject
	{
		RAAPIReferenceWrapper w = wrappers.get(rObject);
		if (w != null) {
			if (takeReference)
				raapi.freeReference(rObject);
			return w;
		}

		Class<? extends RAAPIReferenceWrapper> cls1 = findClosestType(rObject);
				
		try {
			java.lang.reflect.Constructor<? extends RAAPIReferenceWrapper> c = cls1.getConstructor(TDAKernelMetamodelFactory.class, Long.TYPE, Boolean.TYPE);
			return (RAAPIReferenceWrapper)c.newInstance(this, rObject, takeReference);
		} catch (Throwable t1) {
			try {
				java.lang.reflect.Constructor<? extends RAAPIReferenceWrapper> c = cls.getConstructor(TDAKernelMetamodelFactory.class, Long.TYPE, Boolean.TYPE);
				return (RAAPIReferenceWrapper)c.newInstance(this, rObject, takeReference);				
			} catch (Throwable t) {
				return null;
			}
		}

	}

	public Class<? extends RAAPIReferenceWrapper> findClosestType(long rObject)
	{
		Class<? extends RAAPIReferenceWrapper> retVal = null;
		long rCurClass = 0;

		if (raapi.isKindOf(rObject, PROXYREFERENCE)) {
			if ((rCurClass == 0) || (raapi.isDerivedClass(PROXYREFERENCE,rCurClass))) {
				retVal = ProxyReference.class;
				rCurClass = PROXYREFERENCE;
			}
		}
		if (raapi.isKindOf(rObject, TDAKERNEL)) {
			if ((rCurClass == 0) || (raapi.isDerivedClass(TDAKERNEL,rCurClass))) {
				retVal = TDAKernel.class;
				rCurClass = TDAKERNEL;
			}
		}
		if (raapi.isKindOf(rObject, COMMAND)) {
			if ((rCurClass == 0) || (raapi.isDerivedClass(COMMAND,rCurClass))) {
				retVal = Command.class;
				rCurClass = COMMAND;
			}
		}
		if (raapi.isKindOf(rObject, SUBMITTER)) {
			if ((rCurClass == 0) || (raapi.isDerivedClass(SUBMITTER,rCurClass))) {
				retVal = Submitter.class;
				rCurClass = SUBMITTER;
			}
		}
		if (raapi.isKindOf(rObject, EVENT)) {
			if ((rCurClass == 0) || (raapi.isDerivedClass(EVENT,rCurClass))) {
				retVal = Event.class;
				rCurClass = EVENT;
			}
		}
		if (raapi.isKindOf(rObject, ATTACHENGINECOMMAND)) {
			if ((rCurClass == 0) || (raapi.isDerivedClass(ATTACHENGINECOMMAND,rCurClass))) {
				retVal = AttachEngineCommand.class;
				rCurClass = ATTACHENGINECOMMAND;
			}
		}
		if (raapi.isKindOf(rObject, ENGINE)) {
			if ((rCurClass == 0) || (raapi.isDerivedClass(ENGINE,rCurClass))) {
				retVal = Engine.class;
				rCurClass = ENGINE;
			}
		}
		if (raapi.isKindOf(rObject, LAUNCHTRANSFORMATIONCOMMAND)) {
			if ((rCurClass == 0) || (raapi.isDerivedClass(LAUNCHTRANSFORMATIONCOMMAND,rCurClass))) {
				retVal = LaunchTransformationCommand.class;
				rCurClass = LAUNCHTRANSFORMATIONCOMMAND;
			}
		}
		if (raapi.isKindOf(rObject, MOUNTREPOSITORYCOMMAND)) {
			if ((rCurClass == 0) || (raapi.isDerivedClass(MOUNTREPOSITORYCOMMAND,rCurClass))) {
				retVal = MountRepositoryCommand.class;
				rCurClass = MOUNTREPOSITORYCOMMAND;
			}
		}
		if (raapi.isKindOf(rObject, UNMOUNTREPOSITORYCOMMAND)) {
			if ((rCurClass == 0) || (raapi.isDerivedClass(UNMOUNTREPOSITORYCOMMAND,rCurClass))) {
				retVal = UnmountRepositoryCommand.class;
				rCurClass = UNMOUNTREPOSITORYCOMMAND;
			}
		}
		if (raapi.isKindOf(rObject, SAVECOMMAND)) {
			if ((rCurClass == 0) || (raapi.isDerivedClass(SAVECOMMAND,rCurClass))) {
				retVal = SaveCommand.class;
				rCurClass = SAVECOMMAND;
			}
		}
		if (raapi.isKindOf(rObject, INTERDIRECTEDLINK)) {
			if ((rCurClass == 0) || (raapi.isDerivedClass(INTERDIRECTEDLINK,rCurClass))) {
				retVal = InterDirectedLink.class;
				rCurClass = INTERDIRECTEDLINK;
			}
		}
		if (raapi.isKindOf(rObject, INTERATTRIBUTEVALUE)) {
			if ((rCurClass == 0) || (raapi.isDerivedClass(INTERATTRIBUTEVALUE,rCurClass))) {
				retVal = InterAttributeValue.class;
				rCurClass = INTERATTRIBUTEVALUE;
			}
		}
		if (raapi.isKindOf(rObject, INTERDIRECTEDASSOCIATION)) {
			if ((rCurClass == 0) || (raapi.isDerivedClass(INTERDIRECTEDASSOCIATION,rCurClass))) {
				retVal = InterDirectedAssociation.class;
				rCurClass = INTERDIRECTEDASSOCIATION;
			}
		}
		if (raapi.isKindOf(rObject, REPOSITORY)) {
			if ((rCurClass == 0) || (raapi.isDerivedClass(REPOSITORY,rCurClass))) {
				retVal = Repository.class;
				rCurClass = REPOSITORY;
			}
		}
		if (raapi.isKindOf(rObject, PACKAGE)) {
			if ((rCurClass == 0) || (raapi.isDerivedClass(PACKAGE,rCurClass))) {
				retVal = Package.class;
				rCurClass = PACKAGE;
			}
		}
		if (raapi.isKindOf(rObject, INSERTMETAMODELCOMMAND)) {
			if ((rCurClass == 0) || (raapi.isDerivedClass(INSERTMETAMODELCOMMAND,rCurClass))) {
				retVal = InsertMetamodelCommand.class;
				rCurClass = INSERTMETAMODELCOMMAND;
			}
		}
		if (raapi.isKindOf(rObject, SAVESTARTEDEVENT)) {
			if ((rCurClass == 0) || (raapi.isDerivedClass(SAVESTARTEDEVENT,rCurClass))) {
				retVal = SaveStartedEvent.class;
				rCurClass = SAVESTARTEDEVENT;
			}
		}
		if (raapi.isKindOf(rObject, SAVEFINISHEDEVENT)) {
			if ((rCurClass == 0) || (raapi.isDerivedClass(SAVEFINISHEDEVENT,rCurClass))) {
				retVal = SaveFinishedEvent.class;
				rCurClass = SAVEFINISHEDEVENT;
			}
		}
		if (raapi.isKindOf(rObject, SAVEFAILEDEVENT)) {
			if ((rCurClass == 0) || (raapi.isDerivedClass(SAVEFAILEDEVENT,rCurClass))) {
				retVal = SaveFailedEvent.class;
				rCurClass = SAVEFAILEDEVENT;
			}
		}
		if (raapi.isKindOf(rObject, ASYNCCOMMAND)) {
			if ((rCurClass == 0) || (raapi.isDerivedClass(ASYNCCOMMAND,rCurClass))) {
				retVal = AsyncCommand.class;
				rCurClass = ASYNCCOMMAND;
			}
		}
		if (raapi.isKindOf(rObject, LAUNCHTRANSFORMATIONINBACKGROUNDCOMMAND)) {
			if ((rCurClass == 0) || (raapi.isDerivedClass(LAUNCHTRANSFORMATIONINBACKGROUNDCOMMAND,rCurClass))) {
				retVal = LaunchTransformationInBackgroundCommand.class;
				rCurClass = LAUNCHTRANSFORMATIONINBACKGROUNDCOMMAND;
			}
		}

		return retVal; 
	}

	public RAAPIReferenceWrapper findOrCreateRAAPIReferenceWrapper(long rObject, boolean takeReference)
		// if takeReference==true, takes care about freeing rObject
	{
		RAAPIReferenceWrapper w = wrappers.get(rObject);
		if (w != null) {
			if (takeReference)
				raapi.freeReference(rObject);
			return w;
		}
		long it = raapi.getIteratorForDirectObjectClasses(rObject);
		if (it == 0)
			return null;		
		long rClass = raapi.resolveIteratorFirst(it);
		raapi.freeIterator(it);
		if (rClass == 0)
			return null;
		if (rClass == PROXYREFERENCE)
			w = new ProxyReference(this, rObject, takeReference);
		if (rClass == TDAKERNEL)
			w = new TDAKernel(this, rObject, takeReference);
		if (rClass == COMMAND)
			w = new Command(this, rObject, takeReference);
		if (rClass == SUBMITTER)
			w = new Submitter(this, rObject, takeReference);
		if (rClass == EVENT)
			w = new Event(this, rObject, takeReference);
		if (rClass == ATTACHENGINECOMMAND)
			w = new AttachEngineCommand(this, rObject, takeReference);
		if (rClass == ENGINE)
			w = new Engine(this, rObject, takeReference);
		if (rClass == LAUNCHTRANSFORMATIONCOMMAND)
			w = new LaunchTransformationCommand(this, rObject, takeReference);
		if (rClass == MOUNTREPOSITORYCOMMAND)
			w = new MountRepositoryCommand(this, rObject, takeReference);
		if (rClass == UNMOUNTREPOSITORYCOMMAND)
			w = new UnmountRepositoryCommand(this, rObject, takeReference);
		if (rClass == SAVECOMMAND)
			w = new SaveCommand(this, rObject, takeReference);
		if (rClass == INTERDIRECTEDLINK)
			w = new InterDirectedLink(this, rObject, takeReference);
		if (rClass == INTERATTRIBUTEVALUE)
			w = new InterAttributeValue(this, rObject, takeReference);
		if (rClass == INTERDIRECTEDASSOCIATION)
			w = new InterDirectedAssociation(this, rObject, takeReference);
		if (rClass == REPOSITORY)
			w = new Repository(this, rObject, takeReference);
		if (rClass == PACKAGE)
			w = new Package(this, rObject, takeReference);
		if (rClass == INSERTMETAMODELCOMMAND)
			w = new InsertMetamodelCommand(this, rObject, takeReference);
		if (rClass == SAVESTARTEDEVENT)
			w = new SaveStartedEvent(this, rObject, takeReference);
		if (rClass == SAVEFINISHEDEVENT)
			w = new SaveFinishedEvent(this, rObject, takeReference);
		if (rClass == SAVEFAILEDEVENT)
			w = new SaveFailedEvent(this, rObject, takeReference);
		if (rClass == ASYNCCOMMAND)
			w = new AsyncCommand(this, rObject, takeReference);
		if (rClass == LAUNCHTRANSFORMATIONINBACKGROUNDCOMMAND)
			w = new LaunchTransformationInBackgroundCommand(this, rObject, takeReference);
		if (w==null) {
		}
		if ((w != null) && takeReference)
			wrappers.put(rObject, w);
		return w;
	}

	public boolean deleteModel()
	{
		boolean ok = true;
		if (!ProxyReference.deleteAllObjects(this))
			ok = false;
		if (!TDAKernel.deleteAllObjects(this))
			ok = false;
		if (!Command.deleteAllObjects(this))
			ok = false;
		if (!Submitter.deleteAllObjects(this))
			ok = false;
		if (!Event.deleteAllObjects(this))
			ok = false;
		if (!AttachEngineCommand.deleteAllObjects(this))
			ok = false;
		if (!Engine.deleteAllObjects(this))
			ok = false;
		if (!LaunchTransformationCommand.deleteAllObjects(this))
			ok = false;
		if (!MountRepositoryCommand.deleteAllObjects(this))
			ok = false;
		if (!UnmountRepositoryCommand.deleteAllObjects(this))
			ok = false;
		if (!SaveCommand.deleteAllObjects(this))
			ok = false;
		if (!InterDirectedLink.deleteAllObjects(this))
			ok = false;
		if (!InterAttributeValue.deleteAllObjects(this))
			ok = false;
		if (!InterDirectedAssociation.deleteAllObjects(this))
			ok = false;
		if (!Repository.deleteAllObjects(this))
			ok = false;
		if (!Package.deleteAllObjects(this))
			ok = false;
		if (!InsertMetamodelCommand.deleteAllObjects(this))
			ok = false;
		if (!SaveStartedEvent.deleteAllObjects(this))
			ok = false;
		if (!SaveFinishedEvent.deleteAllObjects(this))
			ok = false;
		if (!SaveFailedEvent.deleteAllObjects(this))
			ok = false;
		if (!AsyncCommand.deleteAllObjects(this))
			ok = false;
		if (!LaunchTransformationInBackgroundCommand.deleteAllObjects(this))
			ok = false;
		return ok; 
	}

	// RAAPI references:
	RAAPI raapi = null;
	public long PROXYREFERENCE = 0;
	  public long PROXYREFERENCE_SERIALIZEDDOMESTICREFERENCE = 0;
	  public long PROXYREFERENCE_OUTGOINGLINK = 0;
	  public long PROXYREFERENCE_INGOINGLINK = 0;
	  public long PROXYREFERENCE_INTERDIRECTEDLINK = 0;
	  public long PROXYREFERENCE_INTERATTRIBUTEVALUE = 0;
	  public long PROXYREFERENCE_VALUE = 0;
	  public long PROXYREFERENCE_INTERSUPERCLASS = 0;
	  public long PROXYREFERENCE_INTERSUBCLASS = 0;
	  public long PROXYREFERENCE_OUTGOINGASSOCIATION = 0;
	  public long PROXYREFERENCE_INGOINGASSOCIATION = 0;
	  public long PROXYREFERENCE_PACKAGE = 0;
	  public long PROXYREFERENCE_INTERTYPE = 0;
	  public long PROXYREFERENCE_INTERINSTANCE = 0;
	  public long PROXYREFERENCE_NEXTINTERDIRECTEDLINK = 0;
	  public long PROXYREFERENCE_REPOSITORY = 0;
	public long TDAKERNEL = 0;
	  public long TDAKERNEL_ATTACHEDENGINE = 0;
	  public long TDAKERNEL_ROOTPACKAGE = 0;
	  public long TDAKERNEL_ONSAVESTARTEDEVENT = 0;
	  public long TDAKERNEL_ONSAVEFINISHEDEVENT = 0;
	  public long TDAKERNEL_ONSAVEFAILEDEVENT = 0;
	public long COMMAND = 0;
	  public long COMMAND_INFO = 0;
	  public long COMMAND_SUBMITTER = 0;
	public long SUBMITTER = 0;
	  public long SUBMITTER_EVENT = 0;
	  public long SUBMITTER_COMMAND = 0;
	public long EVENT = 0;
	  public long EVENT_SUBMITTER = 0;
	  public long EVENT_INFO = 0;
	public long ATTACHENGINECOMMAND = 0;
	  public long ATTACHENGINECOMMAND_NAME = 0;
	public long ENGINE = 0;
	  public long ENGINE_KERNEL = 0;
	public long LAUNCHTRANSFORMATIONCOMMAND = 0;
	  public long LAUNCHTRANSFORMATIONCOMMAND_URI = 0;
	public long MOUNTREPOSITORYCOMMAND = 0;
	  public long MOUNTREPOSITORYCOMMAND_URI = 0;
	  public long MOUNTREPOSITORYCOMMAND_MOUNTPOINT = 0;
	public long UNMOUNTREPOSITORYCOMMAND = 0;
	  public long UNMOUNTREPOSITORYCOMMAND_MOUNTPOINT = 0;
	public long SAVECOMMAND = 0;
	public long INTERDIRECTEDLINK = 0;
	  public long INTERDIRECTEDLINK_SOURCEOBJECT = 0;
	  public long INTERDIRECTEDLINK_TARGETOBJECT = 0;
	  public long INTERDIRECTEDLINK_INVERSE = 0;
	  public long INTERDIRECTEDLINK_INTERDIRECTEDLINK = 0;
	  public long INTERDIRECTEDLINK_ASSOCIATION = 0;
	  public long INTERDIRECTEDLINK_PREVIOUSTARGETOBJECT = 0;
	public long INTERATTRIBUTEVALUE = 0;
	  public long INTERATTRIBUTEVALUE_OBJECT = 0;
	  public long INTERATTRIBUTEVALUE_ATTRIBUTE = 0;
	  public long INTERATTRIBUTEVALUE_VALUE = 0;
	public long INTERDIRECTEDASSOCIATION = 0;
	  public long INTERDIRECTEDASSOCIATION_TARGETROLE = 0;
	  public long INTERDIRECTEDASSOCIATION_ISCOMPOSITION = 0;
	  public long INTERDIRECTEDASSOCIATION_SOURCECLASS = 0;
	  public long INTERDIRECTEDASSOCIATION_TARGETCLASS = 0;
	  public long INTERDIRECTEDASSOCIATION_INVERSE = 0;
	public long REPOSITORY = 0;
	  public long REPOSITORY_MOUNTPOINT = 0;
	  public long REPOSITORY_PACKAGE = 0;
	  public long REPOSITORY_URI = 0;
	  public long REPOSITORY_PROXYREFERENCE = 0;
	  public long REPOSITORY_COVEREDPACKAGE = 0;
	public long PACKAGE = 0;
	  public long PACKAGE_MOUNTEDREPOSITORY = 0;
	  public long PACKAGE_PARENT = 0;
	  public long PACKAGE_CHILD = 0;
	  public long PACKAGE_ASSOCIATEDREPOSITORY = 0;
	  public long PACKAGE_KERNEL = 0;
	  public long PACKAGE_PROXYREFERENCE = 0;
	  public long PACKAGE_SIMPLENAME = 0;
	  public long PACKAGE_STACKEDREPOSITORY = 0;
	public long INSERTMETAMODELCOMMAND = 0;
	  public long INSERTMETAMODELCOMMAND_URL = 0;
	public long SAVESTARTEDEVENT = 0;
	public long SAVEFINISHEDEVENT = 0;
	public long SAVEFAILEDEVENT = 0;
	public long ASYNCCOMMAND = 0;
	public long LAUNCHTRANSFORMATIONINBACKGROUNDCOMMAND = 0;
	  public long LAUNCHTRANSFORMATIONINBACKGROUNDCOMMAND_URI = 0;

	public class ElementReferenceException extends Exception
	{
		private static final long serialVersionUID = 1L;
		public ElementReferenceException(String msg)
		{
			super(msg);
		}
	}

	public void unsetRAAPI()
	{
		try {
			setRAAPI(null, null, false);
		}
		catch (Throwable t)
		{
		}
	}

	public RAAPI getRAAPI()
	{
		return raapi;
	}

	public void setRAAPI(RAAPI _raapi, String prefix, boolean insertMetamodel) throws ElementReferenceException // set RAAPI to null to free references
	{
		if (raapi != null) {
			// freeing object-level references...
			for (Long r : wrappers.keySet())
				raapi.freeReference(r);
			wrappers.clear();
			// freeing class-level references...
			if (PROXYREFERENCE != 0) {
				raapi.freeReference(PROXYREFERENCE);
				PROXYREFERENCE = 0;
			}
	  		if (PROXYREFERENCE_SERIALIZEDDOMESTICREFERENCE != 0) {
				raapi.freeReference(PROXYREFERENCE_SERIALIZEDDOMESTICREFERENCE);
				PROXYREFERENCE_SERIALIZEDDOMESTICREFERENCE = 0;
			}
	  		if (PROXYREFERENCE_OUTGOINGLINK != 0) {
				raapi.freeReference(PROXYREFERENCE_OUTGOINGLINK);
				PROXYREFERENCE_OUTGOINGLINK = 0;
			}
	  		if (PROXYREFERENCE_INGOINGLINK != 0) {
				raapi.freeReference(PROXYREFERENCE_INGOINGLINK);
				PROXYREFERENCE_INGOINGLINK = 0;
			}
	  		if (PROXYREFERENCE_INTERDIRECTEDLINK != 0) {
				raapi.freeReference(PROXYREFERENCE_INTERDIRECTEDLINK);
				PROXYREFERENCE_INTERDIRECTEDLINK = 0;
			}
	  		if (PROXYREFERENCE_INTERATTRIBUTEVALUE != 0) {
				raapi.freeReference(PROXYREFERENCE_INTERATTRIBUTEVALUE);
				PROXYREFERENCE_INTERATTRIBUTEVALUE = 0;
			}
	  		if (PROXYREFERENCE_VALUE != 0) {
				raapi.freeReference(PROXYREFERENCE_VALUE);
				PROXYREFERENCE_VALUE = 0;
			}
	  		if (PROXYREFERENCE_INTERSUPERCLASS != 0) {
				raapi.freeReference(PROXYREFERENCE_INTERSUPERCLASS);
				PROXYREFERENCE_INTERSUPERCLASS = 0;
			}
	  		if (PROXYREFERENCE_INTERSUBCLASS != 0) {
				raapi.freeReference(PROXYREFERENCE_INTERSUBCLASS);
				PROXYREFERENCE_INTERSUBCLASS = 0;
			}
	  		if (PROXYREFERENCE_OUTGOINGASSOCIATION != 0) {
				raapi.freeReference(PROXYREFERENCE_OUTGOINGASSOCIATION);
				PROXYREFERENCE_OUTGOINGASSOCIATION = 0;
			}
	  		if (PROXYREFERENCE_INGOINGASSOCIATION != 0) {
				raapi.freeReference(PROXYREFERENCE_INGOINGASSOCIATION);
				PROXYREFERENCE_INGOINGASSOCIATION = 0;
			}
	  		if (PROXYREFERENCE_PACKAGE != 0) {
				raapi.freeReference(PROXYREFERENCE_PACKAGE);
				PROXYREFERENCE_PACKAGE = 0;
			}
	  		if (PROXYREFERENCE_INTERTYPE != 0) {
				raapi.freeReference(PROXYREFERENCE_INTERTYPE);
				PROXYREFERENCE_INTERTYPE = 0;
			}
	  		if (PROXYREFERENCE_INTERINSTANCE != 0) {
				raapi.freeReference(PROXYREFERENCE_INTERINSTANCE);
				PROXYREFERENCE_INTERINSTANCE = 0;
			}
	  		if (PROXYREFERENCE_NEXTINTERDIRECTEDLINK != 0) {
				raapi.freeReference(PROXYREFERENCE_NEXTINTERDIRECTEDLINK);
				PROXYREFERENCE_NEXTINTERDIRECTEDLINK = 0;
			}
	  		if (PROXYREFERENCE_REPOSITORY != 0) {
				raapi.freeReference(PROXYREFERENCE_REPOSITORY);
				PROXYREFERENCE_REPOSITORY = 0;
			}
			if (TDAKERNEL != 0) {
				raapi.freeReference(TDAKERNEL);
				TDAKERNEL = 0;
			}
	  		if (TDAKERNEL_ATTACHEDENGINE != 0) {
				raapi.freeReference(TDAKERNEL_ATTACHEDENGINE);
				TDAKERNEL_ATTACHEDENGINE = 0;
			}
	  		if (TDAKERNEL_ROOTPACKAGE != 0) {
				raapi.freeReference(TDAKERNEL_ROOTPACKAGE);
				TDAKERNEL_ROOTPACKAGE = 0;
			}
	  		if (TDAKERNEL_ONSAVESTARTEDEVENT != 0) {
				raapi.freeReference(TDAKERNEL_ONSAVESTARTEDEVENT);
				TDAKERNEL_ONSAVESTARTEDEVENT = 0;
			}
	  		if (TDAKERNEL_ONSAVEFINISHEDEVENT != 0) {
				raapi.freeReference(TDAKERNEL_ONSAVEFINISHEDEVENT);
				TDAKERNEL_ONSAVEFINISHEDEVENT = 0;
			}
	  		if (TDAKERNEL_ONSAVEFAILEDEVENT != 0) {
				raapi.freeReference(TDAKERNEL_ONSAVEFAILEDEVENT);
				TDAKERNEL_ONSAVEFAILEDEVENT = 0;
			}
			if (COMMAND != 0) {
				raapi.freeReference(COMMAND);
				COMMAND = 0;
			}
	  		if (COMMAND_INFO != 0) {
				raapi.freeReference(COMMAND_INFO);
				COMMAND_INFO = 0;
			}
	  		if (COMMAND_SUBMITTER != 0) {
				raapi.freeReference(COMMAND_SUBMITTER);
				COMMAND_SUBMITTER = 0;
			}
			if (SUBMITTER != 0) {
				raapi.freeReference(SUBMITTER);
				SUBMITTER = 0;
			}
	  		if (SUBMITTER_EVENT != 0) {
				raapi.freeReference(SUBMITTER_EVENT);
				SUBMITTER_EVENT = 0;
			}
	  		if (SUBMITTER_COMMAND != 0) {
				raapi.freeReference(SUBMITTER_COMMAND);
				SUBMITTER_COMMAND = 0;
			}
			if (EVENT != 0) {
				raapi.freeReference(EVENT);
				EVENT = 0;
			}
	  		if (EVENT_SUBMITTER != 0) {
				raapi.freeReference(EVENT_SUBMITTER);
				EVENT_SUBMITTER = 0;
			}
	  		if (EVENT_INFO != 0) {
				raapi.freeReference(EVENT_INFO);
				EVENT_INFO = 0;
			}
			if (ATTACHENGINECOMMAND != 0) {
				raapi.freeReference(ATTACHENGINECOMMAND);
				ATTACHENGINECOMMAND = 0;
			}
	  		if (ATTACHENGINECOMMAND_NAME != 0) {
				raapi.freeReference(ATTACHENGINECOMMAND_NAME);
				ATTACHENGINECOMMAND_NAME = 0;
			}
			if (ENGINE != 0) {
				raapi.freeReference(ENGINE);
				ENGINE = 0;
			}
	  		if (ENGINE_KERNEL != 0) {
				raapi.freeReference(ENGINE_KERNEL);
				ENGINE_KERNEL = 0;
			}
			if (LAUNCHTRANSFORMATIONCOMMAND != 0) {
				raapi.freeReference(LAUNCHTRANSFORMATIONCOMMAND);
				LAUNCHTRANSFORMATIONCOMMAND = 0;
			}
	  		if (LAUNCHTRANSFORMATIONCOMMAND_URI != 0) {
				raapi.freeReference(LAUNCHTRANSFORMATIONCOMMAND_URI);
				LAUNCHTRANSFORMATIONCOMMAND_URI = 0;
			}
			if (MOUNTREPOSITORYCOMMAND != 0) {
				raapi.freeReference(MOUNTREPOSITORYCOMMAND);
				MOUNTREPOSITORYCOMMAND = 0;
			}
	  		if (MOUNTREPOSITORYCOMMAND_URI != 0) {
				raapi.freeReference(MOUNTREPOSITORYCOMMAND_URI);
				MOUNTREPOSITORYCOMMAND_URI = 0;
			}
	  		if (MOUNTREPOSITORYCOMMAND_MOUNTPOINT != 0) {
				raapi.freeReference(MOUNTREPOSITORYCOMMAND_MOUNTPOINT);
				MOUNTREPOSITORYCOMMAND_MOUNTPOINT = 0;
			}
			if (UNMOUNTREPOSITORYCOMMAND != 0) {
				raapi.freeReference(UNMOUNTREPOSITORYCOMMAND);
				UNMOUNTREPOSITORYCOMMAND = 0;
			}
	  		if (UNMOUNTREPOSITORYCOMMAND_MOUNTPOINT != 0) {
				raapi.freeReference(UNMOUNTREPOSITORYCOMMAND_MOUNTPOINT);
				UNMOUNTREPOSITORYCOMMAND_MOUNTPOINT = 0;
			}
			if (SAVECOMMAND != 0) {
				raapi.freeReference(SAVECOMMAND);
				SAVECOMMAND = 0;
			}
			if (INTERDIRECTEDLINK != 0) {
				raapi.freeReference(INTERDIRECTEDLINK);
				INTERDIRECTEDLINK = 0;
			}
	  		if (INTERDIRECTEDLINK_SOURCEOBJECT != 0) {
				raapi.freeReference(INTERDIRECTEDLINK_SOURCEOBJECT);
				INTERDIRECTEDLINK_SOURCEOBJECT = 0;
			}
	  		if (INTERDIRECTEDLINK_TARGETOBJECT != 0) {
				raapi.freeReference(INTERDIRECTEDLINK_TARGETOBJECT);
				INTERDIRECTEDLINK_TARGETOBJECT = 0;
			}
	  		if (INTERDIRECTEDLINK_INVERSE != 0) {
				raapi.freeReference(INTERDIRECTEDLINK_INVERSE);
				INTERDIRECTEDLINK_INVERSE = 0;
			}
	  		if (INTERDIRECTEDLINK_INTERDIRECTEDLINK != 0) {
				raapi.freeReference(INTERDIRECTEDLINK_INTERDIRECTEDLINK);
				INTERDIRECTEDLINK_INTERDIRECTEDLINK = 0;
			}
	  		if (INTERDIRECTEDLINK_ASSOCIATION != 0) {
				raapi.freeReference(INTERDIRECTEDLINK_ASSOCIATION);
				INTERDIRECTEDLINK_ASSOCIATION = 0;
			}
	  		if (INTERDIRECTEDLINK_PREVIOUSTARGETOBJECT != 0) {
				raapi.freeReference(INTERDIRECTEDLINK_PREVIOUSTARGETOBJECT);
				INTERDIRECTEDLINK_PREVIOUSTARGETOBJECT = 0;
			}
			if (INTERATTRIBUTEVALUE != 0) {
				raapi.freeReference(INTERATTRIBUTEVALUE);
				INTERATTRIBUTEVALUE = 0;
			}
	  		if (INTERATTRIBUTEVALUE_OBJECT != 0) {
				raapi.freeReference(INTERATTRIBUTEVALUE_OBJECT);
				INTERATTRIBUTEVALUE_OBJECT = 0;
			}
	  		if (INTERATTRIBUTEVALUE_ATTRIBUTE != 0) {
				raapi.freeReference(INTERATTRIBUTEVALUE_ATTRIBUTE);
				INTERATTRIBUTEVALUE_ATTRIBUTE = 0;
			}
	  		if (INTERATTRIBUTEVALUE_VALUE != 0) {
				raapi.freeReference(INTERATTRIBUTEVALUE_VALUE);
				INTERATTRIBUTEVALUE_VALUE = 0;
			}
			if (INTERDIRECTEDASSOCIATION != 0) {
				raapi.freeReference(INTERDIRECTEDASSOCIATION);
				INTERDIRECTEDASSOCIATION = 0;
			}
	  		if (INTERDIRECTEDASSOCIATION_TARGETROLE != 0) {
				raapi.freeReference(INTERDIRECTEDASSOCIATION_TARGETROLE);
				INTERDIRECTEDASSOCIATION_TARGETROLE = 0;
			}
	  		if (INTERDIRECTEDASSOCIATION_ISCOMPOSITION != 0) {
				raapi.freeReference(INTERDIRECTEDASSOCIATION_ISCOMPOSITION);
				INTERDIRECTEDASSOCIATION_ISCOMPOSITION = 0;
			}
	  		if (INTERDIRECTEDASSOCIATION_SOURCECLASS != 0) {
				raapi.freeReference(INTERDIRECTEDASSOCIATION_SOURCECLASS);
				INTERDIRECTEDASSOCIATION_SOURCECLASS = 0;
			}
	  		if (INTERDIRECTEDASSOCIATION_TARGETCLASS != 0) {
				raapi.freeReference(INTERDIRECTEDASSOCIATION_TARGETCLASS);
				INTERDIRECTEDASSOCIATION_TARGETCLASS = 0;
			}
	  		if (INTERDIRECTEDASSOCIATION_INVERSE != 0) {
				raapi.freeReference(INTERDIRECTEDASSOCIATION_INVERSE);
				INTERDIRECTEDASSOCIATION_INVERSE = 0;
			}
			if (REPOSITORY != 0) {
				raapi.freeReference(REPOSITORY);
				REPOSITORY = 0;
			}
	  		if (REPOSITORY_MOUNTPOINT != 0) {
				raapi.freeReference(REPOSITORY_MOUNTPOINT);
				REPOSITORY_MOUNTPOINT = 0;
			}
	  		if (REPOSITORY_PACKAGE != 0) {
				raapi.freeReference(REPOSITORY_PACKAGE);
				REPOSITORY_PACKAGE = 0;
			}
	  		if (REPOSITORY_URI != 0) {
				raapi.freeReference(REPOSITORY_URI);
				REPOSITORY_URI = 0;
			}
	  		if (REPOSITORY_PROXYREFERENCE != 0) {
				raapi.freeReference(REPOSITORY_PROXYREFERENCE);
				REPOSITORY_PROXYREFERENCE = 0;
			}
	  		if (REPOSITORY_COVEREDPACKAGE != 0) {
				raapi.freeReference(REPOSITORY_COVEREDPACKAGE);
				REPOSITORY_COVEREDPACKAGE = 0;
			}
			if (PACKAGE != 0) {
				raapi.freeReference(PACKAGE);
				PACKAGE = 0;
			}
	  		if (PACKAGE_MOUNTEDREPOSITORY != 0) {
				raapi.freeReference(PACKAGE_MOUNTEDREPOSITORY);
				PACKAGE_MOUNTEDREPOSITORY = 0;
			}
	  		if (PACKAGE_PARENT != 0) {
				raapi.freeReference(PACKAGE_PARENT);
				PACKAGE_PARENT = 0;
			}
	  		if (PACKAGE_CHILD != 0) {
				raapi.freeReference(PACKAGE_CHILD);
				PACKAGE_CHILD = 0;
			}
	  		if (PACKAGE_ASSOCIATEDREPOSITORY != 0) {
				raapi.freeReference(PACKAGE_ASSOCIATEDREPOSITORY);
				PACKAGE_ASSOCIATEDREPOSITORY = 0;
			}
	  		if (PACKAGE_KERNEL != 0) {
				raapi.freeReference(PACKAGE_KERNEL);
				PACKAGE_KERNEL = 0;
			}
	  		if (PACKAGE_PROXYREFERENCE != 0) {
				raapi.freeReference(PACKAGE_PROXYREFERENCE);
				PACKAGE_PROXYREFERENCE = 0;
			}
	  		if (PACKAGE_SIMPLENAME != 0) {
				raapi.freeReference(PACKAGE_SIMPLENAME);
				PACKAGE_SIMPLENAME = 0;
			}
	  		if (PACKAGE_STACKEDREPOSITORY != 0) {
				raapi.freeReference(PACKAGE_STACKEDREPOSITORY);
				PACKAGE_STACKEDREPOSITORY = 0;
			}
			if (INSERTMETAMODELCOMMAND != 0) {
				raapi.freeReference(INSERTMETAMODELCOMMAND);
				INSERTMETAMODELCOMMAND = 0;
			}
	  		if (INSERTMETAMODELCOMMAND_URL != 0) {
				raapi.freeReference(INSERTMETAMODELCOMMAND_URL);
				INSERTMETAMODELCOMMAND_URL = 0;
			}
			if (SAVESTARTEDEVENT != 0) {
				raapi.freeReference(SAVESTARTEDEVENT);
				SAVESTARTEDEVENT = 0;
			}
			if (SAVEFINISHEDEVENT != 0) {
				raapi.freeReference(SAVEFINISHEDEVENT);
				SAVEFINISHEDEVENT = 0;
			}
			if (SAVEFAILEDEVENT != 0) {
				raapi.freeReference(SAVEFAILEDEVENT);
				SAVEFAILEDEVENT = 0;
			}
			if (ASYNCCOMMAND != 0) {
				raapi.freeReference(ASYNCCOMMAND);
				ASYNCCOMMAND = 0;
			}
			if (LAUNCHTRANSFORMATIONINBACKGROUNDCOMMAND != 0) {
				raapi.freeReference(LAUNCHTRANSFORMATIONINBACKGROUNDCOMMAND);
				LAUNCHTRANSFORMATIONINBACKGROUNDCOMMAND = 0;
			}
	  		if (LAUNCHTRANSFORMATIONINBACKGROUNDCOMMAND_URI != 0) {
				raapi.freeReference(LAUNCHTRANSFORMATIONINBACKGROUNDCOMMAND_URI);
				LAUNCHTRANSFORMATIONINBACKGROUNDCOMMAND_URI = 0;
			}
		}

		raapi = _raapi;

		if (raapi != null) {
			// initializing class references...
			PROXYREFERENCE = raapi.findClass("TDAKernel::ProxyReference");
			if ((PROXYREFERENCE == 0) && (prefix != null))
				PROXYREFERENCE = raapi.findClass(prefix+"TDAKernel::ProxyReference");
			if ((PROXYREFERENCE == 0) && insertMetamodel)
				PROXYREFERENCE = raapi.createClass(prefix+"TDAKernel::ProxyReference");
			if (PROXYREFERENCE == 0) {				
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the class TDAKernel::ProxyReference.");
			}
			TDAKERNEL = raapi.findClass("TDAKernel::TDAKernel");
			if ((TDAKERNEL == 0) && (prefix != null))
				TDAKERNEL = raapi.findClass(prefix+"TDAKernel::TDAKernel");
			if ((TDAKERNEL == 0) && insertMetamodel)
				TDAKERNEL = raapi.createClass(prefix+"TDAKernel::TDAKernel");
			if (TDAKERNEL == 0) {				
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the class TDAKernel::TDAKernel.");
			}
			COMMAND = raapi.findClass("TDAKernel::Command");
			if ((COMMAND == 0) && (prefix != null))
				COMMAND = raapi.findClass(prefix+"TDAKernel::Command");
			if ((COMMAND == 0) && insertMetamodel)
				COMMAND = raapi.createClass(prefix+"TDAKernel::Command");
			if (COMMAND == 0) {				
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the class TDAKernel::Command.");
			}
			SUBMITTER = raapi.findClass("TDAKernel::Submitter");
			if ((SUBMITTER == 0) && (prefix != null))
				SUBMITTER = raapi.findClass(prefix+"TDAKernel::Submitter");
			if ((SUBMITTER == 0) && insertMetamodel)
				SUBMITTER = raapi.createClass(prefix+"TDAKernel::Submitter");
			if (SUBMITTER == 0) {				
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the class TDAKernel::Submitter.");
			}
			EVENT = raapi.findClass("TDAKernel::Event");
			if ((EVENT == 0) && (prefix != null))
				EVENT = raapi.findClass(prefix+"TDAKernel::Event");
			if ((EVENT == 0) && insertMetamodel)
				EVENT = raapi.createClass(prefix+"TDAKernel::Event");
			if (EVENT == 0) {				
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the class TDAKernel::Event.");
			}
			ATTACHENGINECOMMAND = raapi.findClass("TDAKernel::AttachEngineCommand");
			if ((ATTACHENGINECOMMAND == 0) && (prefix != null))
				ATTACHENGINECOMMAND = raapi.findClass(prefix+"TDAKernel::AttachEngineCommand");
			if ((ATTACHENGINECOMMAND == 0) && insertMetamodel)
				ATTACHENGINECOMMAND = raapi.createClass(prefix+"TDAKernel::AttachEngineCommand");
			if (ATTACHENGINECOMMAND == 0) {				
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the class TDAKernel::AttachEngineCommand.");
			}
			ENGINE = raapi.findClass("TDAKernel::Engine");
			if ((ENGINE == 0) && (prefix != null))
				ENGINE = raapi.findClass(prefix+"TDAKernel::Engine");
			if ((ENGINE == 0) && insertMetamodel)
				ENGINE = raapi.createClass(prefix+"TDAKernel::Engine");
			if (ENGINE == 0) {				
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the class TDAKernel::Engine.");
			}
			LAUNCHTRANSFORMATIONCOMMAND = raapi.findClass("TDAKernel::LaunchTransformationCommand");
			if ((LAUNCHTRANSFORMATIONCOMMAND == 0) && (prefix != null))
				LAUNCHTRANSFORMATIONCOMMAND = raapi.findClass(prefix+"TDAKernel::LaunchTransformationCommand");
			if ((LAUNCHTRANSFORMATIONCOMMAND == 0) && insertMetamodel)
				LAUNCHTRANSFORMATIONCOMMAND = raapi.createClass(prefix+"TDAKernel::LaunchTransformationCommand");
			if (LAUNCHTRANSFORMATIONCOMMAND == 0) {				
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the class TDAKernel::LaunchTransformationCommand.");
			}
			MOUNTREPOSITORYCOMMAND = raapi.findClass("TDAKernel::MountRepositoryCommand");
			if ((MOUNTREPOSITORYCOMMAND == 0) && (prefix != null))
				MOUNTREPOSITORYCOMMAND = raapi.findClass(prefix+"TDAKernel::MountRepositoryCommand");
			if ((MOUNTREPOSITORYCOMMAND == 0) && insertMetamodel)
				MOUNTREPOSITORYCOMMAND = raapi.createClass(prefix+"TDAKernel::MountRepositoryCommand");
			if (MOUNTREPOSITORYCOMMAND == 0) {				
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the class TDAKernel::MountRepositoryCommand.");
			}
			UNMOUNTREPOSITORYCOMMAND = raapi.findClass("TDAKernel::UnmountRepositoryCommand");
			if ((UNMOUNTREPOSITORYCOMMAND == 0) && (prefix != null))
				UNMOUNTREPOSITORYCOMMAND = raapi.findClass(prefix+"TDAKernel::UnmountRepositoryCommand");
			if ((UNMOUNTREPOSITORYCOMMAND == 0) && insertMetamodel)
				UNMOUNTREPOSITORYCOMMAND = raapi.createClass(prefix+"TDAKernel::UnmountRepositoryCommand");
			if (UNMOUNTREPOSITORYCOMMAND == 0) {				
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the class TDAKernel::UnmountRepositoryCommand.");
			}
			SAVECOMMAND = raapi.findClass("TDAKernel::SaveCommand");
			if ((SAVECOMMAND == 0) && (prefix != null))
				SAVECOMMAND = raapi.findClass(prefix+"TDAKernel::SaveCommand");
			if ((SAVECOMMAND == 0) && insertMetamodel)
				SAVECOMMAND = raapi.createClass(prefix+"TDAKernel::SaveCommand");
			if (SAVECOMMAND == 0) {				
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the class TDAKernel::SaveCommand.");
			}
			INTERDIRECTEDLINK = raapi.findClass("TDAKernel::InterDirectedLink");
			if ((INTERDIRECTEDLINK == 0) && (prefix != null))
				INTERDIRECTEDLINK = raapi.findClass(prefix+"TDAKernel::InterDirectedLink");
			if ((INTERDIRECTEDLINK == 0) && insertMetamodel)
				INTERDIRECTEDLINK = raapi.createClass(prefix+"TDAKernel::InterDirectedLink");
			if (INTERDIRECTEDLINK == 0) {				
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the class TDAKernel::InterDirectedLink.");
			}
			INTERATTRIBUTEVALUE = raapi.findClass("TDAKernel::InterAttributeValue");
			if ((INTERATTRIBUTEVALUE == 0) && (prefix != null))
				INTERATTRIBUTEVALUE = raapi.findClass(prefix+"TDAKernel::InterAttributeValue");
			if ((INTERATTRIBUTEVALUE == 0) && insertMetamodel)
				INTERATTRIBUTEVALUE = raapi.createClass(prefix+"TDAKernel::InterAttributeValue");
			if (INTERATTRIBUTEVALUE == 0) {				
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the class TDAKernel::InterAttributeValue.");
			}
			INTERDIRECTEDASSOCIATION = raapi.findClass("TDAKernel::InterDirectedAssociation");
			if ((INTERDIRECTEDASSOCIATION == 0) && (prefix != null))
				INTERDIRECTEDASSOCIATION = raapi.findClass(prefix+"TDAKernel::InterDirectedAssociation");
			if ((INTERDIRECTEDASSOCIATION == 0) && insertMetamodel)
				INTERDIRECTEDASSOCIATION = raapi.createClass(prefix+"TDAKernel::InterDirectedAssociation");
			if (INTERDIRECTEDASSOCIATION == 0) {				
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the class TDAKernel::InterDirectedAssociation.");
			}
			REPOSITORY = raapi.findClass("TDAKernel::Repository");
			if ((REPOSITORY == 0) && (prefix != null))
				REPOSITORY = raapi.findClass(prefix+"TDAKernel::Repository");
			if ((REPOSITORY == 0) && insertMetamodel)
				REPOSITORY = raapi.createClass(prefix+"TDAKernel::Repository");
			if (REPOSITORY == 0) {				
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the class TDAKernel::Repository.");
			}
			PACKAGE = raapi.findClass("TDAKernel::Package");
			if ((PACKAGE == 0) && (prefix != null))
				PACKAGE = raapi.findClass(prefix+"TDAKernel::Package");
			if ((PACKAGE == 0) && insertMetamodel)
				PACKAGE = raapi.createClass(prefix+"TDAKernel::Package");
			if (PACKAGE == 0) {				
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the class TDAKernel::Package.");
			}
			INSERTMETAMODELCOMMAND = raapi.findClass("TDAKernel::InsertMetamodelCommand");
			if ((INSERTMETAMODELCOMMAND == 0) && (prefix != null))
				INSERTMETAMODELCOMMAND = raapi.findClass(prefix+"TDAKernel::InsertMetamodelCommand");
			if ((INSERTMETAMODELCOMMAND == 0) && insertMetamodel)
				INSERTMETAMODELCOMMAND = raapi.createClass(prefix+"TDAKernel::InsertMetamodelCommand");
			if (INSERTMETAMODELCOMMAND == 0) {				
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the class TDAKernel::InsertMetamodelCommand.");
			}
			SAVESTARTEDEVENT = raapi.findClass("TDAKernel::SaveStartedEvent");
			if ((SAVESTARTEDEVENT == 0) && (prefix != null))
				SAVESTARTEDEVENT = raapi.findClass(prefix+"TDAKernel::SaveStartedEvent");
			if ((SAVESTARTEDEVENT == 0) && insertMetamodel)
				SAVESTARTEDEVENT = raapi.createClass(prefix+"TDAKernel::SaveStartedEvent");
			if (SAVESTARTEDEVENT == 0) {				
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the class TDAKernel::SaveStartedEvent.");
			}
			SAVEFINISHEDEVENT = raapi.findClass("TDAKernel::SaveFinishedEvent");
			if ((SAVEFINISHEDEVENT == 0) && (prefix != null))
				SAVEFINISHEDEVENT = raapi.findClass(prefix+"TDAKernel::SaveFinishedEvent");
			if ((SAVEFINISHEDEVENT == 0) && insertMetamodel)
				SAVEFINISHEDEVENT = raapi.createClass(prefix+"TDAKernel::SaveFinishedEvent");
			if (SAVEFINISHEDEVENT == 0) {				
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the class TDAKernel::SaveFinishedEvent.");
			}
			SAVEFAILEDEVENT = raapi.findClass("TDAKernel::SaveFailedEvent");
			if ((SAVEFAILEDEVENT == 0) && (prefix != null))
				SAVEFAILEDEVENT = raapi.findClass(prefix+"TDAKernel::SaveFailedEvent");
			if ((SAVEFAILEDEVENT == 0) && insertMetamodel)
				SAVEFAILEDEVENT = raapi.createClass(prefix+"TDAKernel::SaveFailedEvent");
			if (SAVEFAILEDEVENT == 0) {				
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the class TDAKernel::SaveFailedEvent.");
			}
			ASYNCCOMMAND = raapi.findClass("TDAKernel::AsyncCommand");
			if ((ASYNCCOMMAND == 0) && (prefix != null))
				ASYNCCOMMAND = raapi.findClass(prefix+"TDAKernel::AsyncCommand");
			if ((ASYNCCOMMAND == 0) && insertMetamodel)
				ASYNCCOMMAND = raapi.createClass(prefix+"TDAKernel::AsyncCommand");
			if (ASYNCCOMMAND == 0) {				
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the class TDAKernel::AsyncCommand.");
			}
			LAUNCHTRANSFORMATIONINBACKGROUNDCOMMAND = raapi.findClass("TDAKernel::LaunchTransformationInBackgroundCommand");
			if ((LAUNCHTRANSFORMATIONINBACKGROUNDCOMMAND == 0) && (prefix != null))
				LAUNCHTRANSFORMATIONINBACKGROUNDCOMMAND = raapi.findClass(prefix+"TDAKernel::LaunchTransformationInBackgroundCommand");
			if ((LAUNCHTRANSFORMATIONINBACKGROUNDCOMMAND == 0) && insertMetamodel)
				LAUNCHTRANSFORMATIONINBACKGROUNDCOMMAND = raapi.createClass(prefix+"TDAKernel::LaunchTransformationInBackgroundCommand");
			if (LAUNCHTRANSFORMATIONINBACKGROUNDCOMMAND == 0) {				
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the class TDAKernel::LaunchTransformationInBackgroundCommand.");
			}

			// creating generalizations, if they do not exist...
			if (insertMetamodel) {
				if (!raapi.isDirectSubClass(ATTACHENGINECOMMAND, COMMAND))
					if (!raapi.createGeneralization(ATTACHENGINECOMMAND, COMMAND)) {
						setRAAPI(null, null, false); // freeing references initialized so far...
						throw new ElementReferenceException("Error creating a generalization between classes TDAKernel::AttachEngineCommand and TDAKernel::Command.");
					}
				if (!raapi.isDirectSubClass(LAUNCHTRANSFORMATIONCOMMAND, COMMAND))
					if (!raapi.createGeneralization(LAUNCHTRANSFORMATIONCOMMAND, COMMAND)) {
						setRAAPI(null, null, false); // freeing references initialized so far...
						throw new ElementReferenceException("Error creating a generalization between classes TDAKernel::LaunchTransformationCommand and TDAKernel::Command.");
					}
				if (!raapi.isDirectSubClass(MOUNTREPOSITORYCOMMAND, COMMAND))
					if (!raapi.createGeneralization(MOUNTREPOSITORYCOMMAND, COMMAND)) {
						setRAAPI(null, null, false); // freeing references initialized so far...
						throw new ElementReferenceException("Error creating a generalization between classes TDAKernel::MountRepositoryCommand and TDAKernel::Command.");
					}
				if (!raapi.isDirectSubClass(UNMOUNTREPOSITORYCOMMAND, COMMAND))
					if (!raapi.createGeneralization(UNMOUNTREPOSITORYCOMMAND, COMMAND)) {
						setRAAPI(null, null, false); // freeing references initialized so far...
						throw new ElementReferenceException("Error creating a generalization between classes TDAKernel::UnmountRepositoryCommand and TDAKernel::Command.");
					}
				if (!raapi.isDirectSubClass(SAVECOMMAND, COMMAND))
					if (!raapi.createGeneralization(SAVECOMMAND, COMMAND)) {
						setRAAPI(null, null, false); // freeing references initialized so far...
						throw new ElementReferenceException("Error creating a generalization between classes TDAKernel::SaveCommand and TDAKernel::Command.");
					}
				if (!raapi.isDirectSubClass(INTERDIRECTEDASSOCIATION, PROXYREFERENCE))
					if (!raapi.createGeneralization(INTERDIRECTEDASSOCIATION, PROXYREFERENCE)) {
						setRAAPI(null, null, false); // freeing references initialized so far...
						throw new ElementReferenceException("Error creating a generalization between classes TDAKernel::InterDirectedAssociation and TDAKernel::ProxyReference.");
					}
				if (!raapi.isDirectSubClass(INSERTMETAMODELCOMMAND, COMMAND))
					if (!raapi.createGeneralization(INSERTMETAMODELCOMMAND, COMMAND)) {
						setRAAPI(null, null, false); // freeing references initialized so far...
						throw new ElementReferenceException("Error creating a generalization between classes TDAKernel::InsertMetamodelCommand and TDAKernel::Command.");
					}
				if (!raapi.isDirectSubClass(SAVESTARTEDEVENT, EVENT))
					if (!raapi.createGeneralization(SAVESTARTEDEVENT, EVENT)) {
						setRAAPI(null, null, false); // freeing references initialized so far...
						throw new ElementReferenceException("Error creating a generalization between classes TDAKernel::SaveStartedEvent and TDAKernel::Event.");
					}
				if (!raapi.isDirectSubClass(SAVEFINISHEDEVENT, EVENT))
					if (!raapi.createGeneralization(SAVEFINISHEDEVENT, EVENT)) {
						setRAAPI(null, null, false); // freeing references initialized so far...
						throw new ElementReferenceException("Error creating a generalization between classes TDAKernel::SaveFinishedEvent and TDAKernel::Event.");
					}
				if (!raapi.isDirectSubClass(SAVEFAILEDEVENT, EVENT))
					if (!raapi.createGeneralization(SAVEFAILEDEVENT, EVENT)) {
						setRAAPI(null, null, false); // freeing references initialized so far...
						throw new ElementReferenceException("Error creating a generalization between classes TDAKernel::SaveFailedEvent and TDAKernel::Event.");
					}
				if (!raapi.isDirectSubClass(ASYNCCOMMAND, COMMAND))
					if (!raapi.createGeneralization(ASYNCCOMMAND, COMMAND)) {
						setRAAPI(null, null, false); // freeing references initialized so far...
						throw new ElementReferenceException("Error creating a generalization between classes TDAKernel::AsyncCommand and TDAKernel::Command.");
					}
				if (!raapi.isDirectSubClass(LAUNCHTRANSFORMATIONINBACKGROUNDCOMMAND, ASYNCCOMMAND))
					if (!raapi.createGeneralization(LAUNCHTRANSFORMATIONINBACKGROUNDCOMMAND, ASYNCCOMMAND)) {
						setRAAPI(null, null, false); // freeing references initialized so far...
						throw new ElementReferenceException("Error creating a generalization between classes TDAKernel::LaunchTransformationInBackgroundCommand and TDAKernel::AsyncCommand.");
					}
			}

			// initializing references for attributes and associations...
			PROXYREFERENCE_SERIALIZEDDOMESTICREFERENCE = raapi.findAttribute(PROXYREFERENCE, "serializedDomesticReference");
			if ((PROXYREFERENCE_SERIALIZEDDOMESTICREFERENCE == 0) && insertMetamodel)
				PROXYREFERENCE_SERIALIZEDDOMESTICREFERENCE = raapi.createAttribute(PROXYREFERENCE, "serializedDomesticReference", raapi.findPrimitiveDataType("String"));
			if (PROXYREFERENCE_SERIALIZEDDOMESTICREFERENCE == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute serializedDomesticReference of the class TDAKernel::ProxyReference.");
			}
			PROXYREFERENCE_OUTGOINGLINK = raapi.findAssociationEnd(PROXYREFERENCE, "outgoingLink");
			if ((PROXYREFERENCE_OUTGOINGLINK == 0) && insertMetamodel) {
				PROXYREFERENCE_OUTGOINGLINK = raapi.createAssociation(PROXYREFERENCE, INTERDIRECTEDLINK, "sourceObject", "outgoingLink", true);
			}
			if (PROXYREFERENCE_OUTGOINGLINK == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end outgoingLink of the class TDAKernel::ProxyReference.");
			}
			PROXYREFERENCE_INGOINGLINK = raapi.findAssociationEnd(PROXYREFERENCE, "ingoingLink");
			if ((PROXYREFERENCE_INGOINGLINK == 0) && insertMetamodel) {
				PROXYREFERENCE_INGOINGLINK = raapi.createAssociation(PROXYREFERENCE, INTERDIRECTEDLINK, "targetObject", "ingoingLink", true);
			}
			if (PROXYREFERENCE_INGOINGLINK == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end ingoingLink of the class TDAKernel::ProxyReference.");
			}
			PROXYREFERENCE_INTERDIRECTEDLINK = raapi.findAssociationEnd(PROXYREFERENCE, "interDirectedLink");
			if ((PROXYREFERENCE_INTERDIRECTEDLINK == 0) && insertMetamodel) {
				PROXYREFERENCE_INTERDIRECTEDLINK = raapi.createAssociation(PROXYREFERENCE, INTERDIRECTEDLINK, "association", "interDirectedLink", true);
			}
			if (PROXYREFERENCE_INTERDIRECTEDLINK == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end interDirectedLink of the class TDAKernel::ProxyReference.");
			}
			PROXYREFERENCE_INTERATTRIBUTEVALUE = raapi.findAssociationEnd(PROXYREFERENCE, "interAttributeValue");
			if ((PROXYREFERENCE_INTERATTRIBUTEVALUE == 0) && insertMetamodel) {
				PROXYREFERENCE_INTERATTRIBUTEVALUE = raapi.createAssociation(PROXYREFERENCE, INTERATTRIBUTEVALUE, "object", "interAttributeValue", true);
			}
			if (PROXYREFERENCE_INTERATTRIBUTEVALUE == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end interAttributeValue of the class TDAKernel::ProxyReference.");
			}
			PROXYREFERENCE_VALUE = raapi.findAssociationEnd(PROXYREFERENCE, "value");
			if ((PROXYREFERENCE_VALUE == 0) && insertMetamodel) {
				PROXYREFERENCE_VALUE = raapi.createAssociation(PROXYREFERENCE, INTERATTRIBUTEVALUE, "attribute", "value", true);
			}
			if (PROXYREFERENCE_VALUE == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end value of the class TDAKernel::ProxyReference.");
			}
			PROXYREFERENCE_INTERSUPERCLASS = raapi.findAssociationEnd(PROXYREFERENCE, "interSuperClass");
			if ((PROXYREFERENCE_INTERSUPERCLASS == 0) && insertMetamodel) {
				PROXYREFERENCE_INTERSUPERCLASS = raapi.createAssociation(PROXYREFERENCE, PROXYREFERENCE, "interSubClass", "interSuperClass", false);
			}
			if (PROXYREFERENCE_INTERSUPERCLASS == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end interSuperClass of the class TDAKernel::ProxyReference.");
			}
			PROXYREFERENCE_INTERSUBCLASS = raapi.findAssociationEnd(PROXYREFERENCE, "interSubClass");
			if ((PROXYREFERENCE_INTERSUBCLASS == 0) && insertMetamodel) {
				PROXYREFERENCE_INTERSUBCLASS = raapi.createAssociation(PROXYREFERENCE, PROXYREFERENCE, "interSuperClass", "interSubClass", false);
			}
			if (PROXYREFERENCE_INTERSUBCLASS == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end interSubClass of the class TDAKernel::ProxyReference.");
			}
			PROXYREFERENCE_OUTGOINGASSOCIATION = raapi.findAssociationEnd(PROXYREFERENCE, "outgoingAssociation");
			if ((PROXYREFERENCE_OUTGOINGASSOCIATION == 0) && insertMetamodel) {
				PROXYREFERENCE_OUTGOINGASSOCIATION = raapi.createAssociation(PROXYREFERENCE, INTERDIRECTEDASSOCIATION, "sourceClass", "outgoingAssociation", true);
			}
			if (PROXYREFERENCE_OUTGOINGASSOCIATION == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end outgoingAssociation of the class TDAKernel::ProxyReference.");
			}
			PROXYREFERENCE_INGOINGASSOCIATION = raapi.findAssociationEnd(PROXYREFERENCE, "ingoingAssociation");
			if ((PROXYREFERENCE_INGOINGASSOCIATION == 0) && insertMetamodel) {
				PROXYREFERENCE_INGOINGASSOCIATION = raapi.createAssociation(PROXYREFERENCE, INTERDIRECTEDASSOCIATION, "targetClass", "ingoingAssociation", true);
			}
			if (PROXYREFERENCE_INGOINGASSOCIATION == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end ingoingAssociation of the class TDAKernel::ProxyReference.");
			}
			PROXYREFERENCE_PACKAGE = raapi.findAssociationEnd(PROXYREFERENCE, "package");
			if ((PROXYREFERENCE_PACKAGE == 0) && insertMetamodel) {
				PROXYREFERENCE_PACKAGE = raapi.createAssociation(PROXYREFERENCE, PACKAGE, "proxyReference", "package", false);
			}
			if (PROXYREFERENCE_PACKAGE == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end package of the class TDAKernel::ProxyReference.");
			}
			PROXYREFERENCE_INTERTYPE = raapi.findAssociationEnd(PROXYREFERENCE, "interType");
			if ((PROXYREFERENCE_INTERTYPE == 0) && insertMetamodel) {
				PROXYREFERENCE_INTERTYPE = raapi.createAssociation(PROXYREFERENCE, PROXYREFERENCE, "interInstance", "interType", false);
			}
			if (PROXYREFERENCE_INTERTYPE == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end interType of the class TDAKernel::ProxyReference.");
			}
			PROXYREFERENCE_INTERINSTANCE = raapi.findAssociationEnd(PROXYREFERENCE, "interInstance");
			if ((PROXYREFERENCE_INTERINSTANCE == 0) && insertMetamodel) {
				PROXYREFERENCE_INTERINSTANCE = raapi.createAssociation(PROXYREFERENCE, PROXYREFERENCE, "interType", "interInstance", false);
			}
			if (PROXYREFERENCE_INTERINSTANCE == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end interInstance of the class TDAKernel::ProxyReference.");
			}
			PROXYREFERENCE_NEXTINTERDIRECTEDLINK = raapi.findAssociationEnd(PROXYREFERENCE, "nextInterDirectedLink");
			if ((PROXYREFERENCE_NEXTINTERDIRECTEDLINK == 0) && insertMetamodel) {
				PROXYREFERENCE_NEXTINTERDIRECTEDLINK = raapi.createAssociation(PROXYREFERENCE, INTERDIRECTEDLINK, "previousTargetObject", "nextInterDirectedLink", false);
			}
			if (PROXYREFERENCE_NEXTINTERDIRECTEDLINK == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end nextInterDirectedLink of the class TDAKernel::ProxyReference.");
			}
			PROXYREFERENCE_REPOSITORY = raapi.findAssociationEnd(PROXYREFERENCE, "repository");
			if ((PROXYREFERENCE_REPOSITORY == 0) && insertMetamodel) {
				PROXYREFERENCE_REPOSITORY = raapi.createAssociation(PROXYREFERENCE, REPOSITORY, "proxyReference", "repository", false);
			}
			if (PROXYREFERENCE_REPOSITORY == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end repository of the class TDAKernel::ProxyReference.");
			}
			TDAKERNEL_ATTACHEDENGINE = raapi.findAssociationEnd(TDAKERNEL, "attachedEngine");
			if ((TDAKERNEL_ATTACHEDENGINE == 0) && insertMetamodel) {
				TDAKERNEL_ATTACHEDENGINE = raapi.createAssociation(TDAKERNEL, ENGINE, "kernel", "attachedEngine", false);
			}
			if (TDAKERNEL_ATTACHEDENGINE == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end attachedEngine of the class TDAKernel::TDAKernel.");
			}
			TDAKERNEL_ROOTPACKAGE = raapi.findAssociationEnd(TDAKERNEL, "rootPackage");
			if ((TDAKERNEL_ROOTPACKAGE == 0) && insertMetamodel) {
				TDAKERNEL_ROOTPACKAGE = raapi.createAssociation(TDAKERNEL, PACKAGE, "kernel", "rootPackage", false);
			}
			if (TDAKERNEL_ROOTPACKAGE == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end rootPackage of the class TDAKernel::TDAKernel.");
			}
			TDAKERNEL_ONSAVESTARTEDEVENT = raapi.findAttribute(TDAKERNEL, "onSaveStartedEvent");
			if ((TDAKERNEL_ONSAVESTARTEDEVENT == 0) && insertMetamodel)
				TDAKERNEL_ONSAVESTARTEDEVENT = raapi.createAttribute(TDAKERNEL, "onSaveStartedEvent", raapi.findPrimitiveDataType("String"));
			if (TDAKERNEL_ONSAVESTARTEDEVENT == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute onSaveStartedEvent of the class TDAKernel::TDAKernel.");
			}
			TDAKERNEL_ONSAVEFINISHEDEVENT = raapi.findAttribute(TDAKERNEL, "onSaveFinishedEvent");
			if ((TDAKERNEL_ONSAVEFINISHEDEVENT == 0) && insertMetamodel)
				TDAKERNEL_ONSAVEFINISHEDEVENT = raapi.createAttribute(TDAKERNEL, "onSaveFinishedEvent", raapi.findPrimitiveDataType("String"));
			if (TDAKERNEL_ONSAVEFINISHEDEVENT == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute onSaveFinishedEvent of the class TDAKernel::TDAKernel.");
			}
			TDAKERNEL_ONSAVEFAILEDEVENT = raapi.findAttribute(TDAKERNEL, "onSaveFailedEvent");
			if ((TDAKERNEL_ONSAVEFAILEDEVENT == 0) && insertMetamodel)
				TDAKERNEL_ONSAVEFAILEDEVENT = raapi.createAttribute(TDAKERNEL, "onSaveFailedEvent", raapi.findPrimitiveDataType("String"));
			if (TDAKERNEL_ONSAVEFAILEDEVENT == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute onSaveFailedEvent of the class TDAKernel::TDAKernel.");
			}
			COMMAND_INFO = raapi.findAttribute(COMMAND, "info");
			if ((COMMAND_INFO == 0) && insertMetamodel)
				COMMAND_INFO = raapi.createAttribute(COMMAND, "info", raapi.findPrimitiveDataType("String"));
			if (COMMAND_INFO == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute info of the class TDAKernel::Command.");
			}
			COMMAND_SUBMITTER = raapi.findAssociationEnd(COMMAND, "submitter");
			if ((COMMAND_SUBMITTER == 0) && insertMetamodel) {
				COMMAND_SUBMITTER = raapi.createAssociation(COMMAND, SUBMITTER, "command", "submitter", false);
			}
			if (COMMAND_SUBMITTER == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end submitter of the class TDAKernel::Command.");
			}
			SUBMITTER_EVENT = raapi.findAssociationEnd(SUBMITTER, "event");
			if ((SUBMITTER_EVENT == 0) && insertMetamodel) {
				SUBMITTER_EVENT = raapi.createAssociation(SUBMITTER, EVENT, "submitter", "event", false);
			}
			if (SUBMITTER_EVENT == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end event of the class TDAKernel::Submitter.");
			}
			SUBMITTER_COMMAND = raapi.findAssociationEnd(SUBMITTER, "command");
			if ((SUBMITTER_COMMAND == 0) && insertMetamodel) {
				SUBMITTER_COMMAND = raapi.createAssociation(SUBMITTER, COMMAND, "submitter", "command", false);
			}
			if (SUBMITTER_COMMAND == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end command of the class TDAKernel::Submitter.");
			}
			EVENT_SUBMITTER = raapi.findAssociationEnd(EVENT, "submitter");
			if ((EVENT_SUBMITTER == 0) && insertMetamodel) {
				EVENT_SUBMITTER = raapi.createAssociation(EVENT, SUBMITTER, "event", "submitter", false);
			}
			if (EVENT_SUBMITTER == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end submitter of the class TDAKernel::Event.");
			}
			EVENT_INFO = raapi.findAttribute(EVENT, "info");
			if ((EVENT_INFO == 0) && insertMetamodel)
				EVENT_INFO = raapi.createAttribute(EVENT, "info", raapi.findPrimitiveDataType("String"));
			if (EVENT_INFO == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute info of the class TDAKernel::Event.");
			}
			ATTACHENGINECOMMAND_NAME = raapi.findAttribute(ATTACHENGINECOMMAND, "name");
			if ((ATTACHENGINECOMMAND_NAME == 0) && insertMetamodel)
				ATTACHENGINECOMMAND_NAME = raapi.createAttribute(ATTACHENGINECOMMAND, "name", raapi.findPrimitiveDataType("String"));
			if (ATTACHENGINECOMMAND_NAME == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute name of the class TDAKernel::AttachEngineCommand.");
			}
			ENGINE_KERNEL = raapi.findAssociationEnd(ENGINE, "kernel");
			if ((ENGINE_KERNEL == 0) && insertMetamodel) {
				ENGINE_KERNEL = raapi.createAssociation(ENGINE, TDAKERNEL, "attachedEngine", "kernel", false);
			}
			if (ENGINE_KERNEL == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end kernel of the class TDAKernel::Engine.");
			}
			LAUNCHTRANSFORMATIONCOMMAND_URI = raapi.findAttribute(LAUNCHTRANSFORMATIONCOMMAND, "uri");
			if ((LAUNCHTRANSFORMATIONCOMMAND_URI == 0) && insertMetamodel)
				LAUNCHTRANSFORMATIONCOMMAND_URI = raapi.createAttribute(LAUNCHTRANSFORMATIONCOMMAND, "uri", raapi.findPrimitiveDataType("String"));
			if (LAUNCHTRANSFORMATIONCOMMAND_URI == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute uri of the class TDAKernel::LaunchTransformationCommand.");
			}
			MOUNTREPOSITORYCOMMAND_URI = raapi.findAttribute(MOUNTREPOSITORYCOMMAND, "uri");
			if ((MOUNTREPOSITORYCOMMAND_URI == 0) && insertMetamodel)
				MOUNTREPOSITORYCOMMAND_URI = raapi.createAttribute(MOUNTREPOSITORYCOMMAND, "uri", raapi.findPrimitiveDataType("String"));
			if (MOUNTREPOSITORYCOMMAND_URI == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute uri of the class TDAKernel::MountRepositoryCommand.");
			}
			MOUNTREPOSITORYCOMMAND_MOUNTPOINT = raapi.findAttribute(MOUNTREPOSITORYCOMMAND, "mountPoint");
			if ((MOUNTREPOSITORYCOMMAND_MOUNTPOINT == 0) && insertMetamodel)
				MOUNTREPOSITORYCOMMAND_MOUNTPOINT = raapi.createAttribute(MOUNTREPOSITORYCOMMAND, "mountPoint", raapi.findPrimitiveDataType("String"));
			if (MOUNTREPOSITORYCOMMAND_MOUNTPOINT == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute mountPoint of the class TDAKernel::MountRepositoryCommand.");
			}
			UNMOUNTREPOSITORYCOMMAND_MOUNTPOINT = raapi.findAttribute(UNMOUNTREPOSITORYCOMMAND, "mountPoint");
			if ((UNMOUNTREPOSITORYCOMMAND_MOUNTPOINT == 0) && insertMetamodel)
				UNMOUNTREPOSITORYCOMMAND_MOUNTPOINT = raapi.createAttribute(UNMOUNTREPOSITORYCOMMAND, "mountPoint", raapi.findPrimitiveDataType("String"));
			if (UNMOUNTREPOSITORYCOMMAND_MOUNTPOINT == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute mountPoint of the class TDAKernel::UnmountRepositoryCommand.");
			}
			INTERDIRECTEDLINK_SOURCEOBJECT = raapi.findAssociationEnd(INTERDIRECTEDLINK, "sourceObject");
			if ((INTERDIRECTEDLINK_SOURCEOBJECT == 0) && insertMetamodel) {
				INTERDIRECTEDLINK_SOURCEOBJECT = raapi.createAssociation(INTERDIRECTEDLINK, PROXYREFERENCE, "outgoingLink", "sourceObject", false);
			}
			if (INTERDIRECTEDLINK_SOURCEOBJECT == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end sourceObject of the class TDAKernel::InterDirectedLink.");
			}
			INTERDIRECTEDLINK_TARGETOBJECT = raapi.findAssociationEnd(INTERDIRECTEDLINK, "targetObject");
			if ((INTERDIRECTEDLINK_TARGETOBJECT == 0) && insertMetamodel) {
				INTERDIRECTEDLINK_TARGETOBJECT = raapi.createAssociation(INTERDIRECTEDLINK, PROXYREFERENCE, "ingoingLink", "targetObject", false);
			}
			if (INTERDIRECTEDLINK_TARGETOBJECT == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end targetObject of the class TDAKernel::InterDirectedLink.");
			}
			INTERDIRECTEDLINK_INVERSE = raapi.findAssociationEnd(INTERDIRECTEDLINK, "inverse");
			if ((INTERDIRECTEDLINK_INVERSE == 0) && insertMetamodel) {
				INTERDIRECTEDLINK_INVERSE = raapi.createAssociation(INTERDIRECTEDLINK, INTERDIRECTEDLINK, "interDirectedLink", "inverse", false);
			}
			if (INTERDIRECTEDLINK_INVERSE == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end inverse of the class TDAKernel::InterDirectedLink.");
			}
			INTERDIRECTEDLINK_INTERDIRECTEDLINK = raapi.findAssociationEnd(INTERDIRECTEDLINK, "interDirectedLink");
			if ((INTERDIRECTEDLINK_INTERDIRECTEDLINK == 0) && insertMetamodel) {
				INTERDIRECTEDLINK_INTERDIRECTEDLINK = raapi.createAssociation(INTERDIRECTEDLINK, INTERDIRECTEDLINK, "inverse", "interDirectedLink", false);
			}
			if (INTERDIRECTEDLINK_INTERDIRECTEDLINK == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end interDirectedLink of the class TDAKernel::InterDirectedLink.");
			}
			INTERDIRECTEDLINK_ASSOCIATION = raapi.findAssociationEnd(INTERDIRECTEDLINK, "association");
			if ((INTERDIRECTEDLINK_ASSOCIATION == 0) && insertMetamodel) {
				INTERDIRECTEDLINK_ASSOCIATION = raapi.createAssociation(INTERDIRECTEDLINK, PROXYREFERENCE, "interDirectedLink", "association", false);
			}
			if (INTERDIRECTEDLINK_ASSOCIATION == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end association of the class TDAKernel::InterDirectedLink.");
			}
			INTERDIRECTEDLINK_PREVIOUSTARGETOBJECT = raapi.findAssociationEnd(INTERDIRECTEDLINK, "previousTargetObject");
			if ((INTERDIRECTEDLINK_PREVIOUSTARGETOBJECT == 0) && insertMetamodel) {
				INTERDIRECTEDLINK_PREVIOUSTARGETOBJECT = raapi.createAssociation(INTERDIRECTEDLINK, PROXYREFERENCE, "nextInterDirectedLink", "previousTargetObject", false);
			}
			if (INTERDIRECTEDLINK_PREVIOUSTARGETOBJECT == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end previousTargetObject of the class TDAKernel::InterDirectedLink.");
			}
			INTERATTRIBUTEVALUE_OBJECT = raapi.findAssociationEnd(INTERATTRIBUTEVALUE, "object");
			if ((INTERATTRIBUTEVALUE_OBJECT == 0) && insertMetamodel) {
				INTERATTRIBUTEVALUE_OBJECT = raapi.createAssociation(INTERATTRIBUTEVALUE, PROXYREFERENCE, "interAttributeValue", "object", false);
			}
			if (INTERATTRIBUTEVALUE_OBJECT == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end object of the class TDAKernel::InterAttributeValue.");
			}
			INTERATTRIBUTEVALUE_ATTRIBUTE = raapi.findAssociationEnd(INTERATTRIBUTEVALUE, "attribute");
			if ((INTERATTRIBUTEVALUE_ATTRIBUTE == 0) && insertMetamodel) {
				INTERATTRIBUTEVALUE_ATTRIBUTE = raapi.createAssociation(INTERATTRIBUTEVALUE, PROXYREFERENCE, "value", "attribute", false);
			}
			if (INTERATTRIBUTEVALUE_ATTRIBUTE == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end attribute of the class TDAKernel::InterAttributeValue.");
			}
			INTERATTRIBUTEVALUE_VALUE = raapi.findAttribute(INTERATTRIBUTEVALUE, "value");
			if ((INTERATTRIBUTEVALUE_VALUE == 0) && insertMetamodel)
				INTERATTRIBUTEVALUE_VALUE = raapi.createAttribute(INTERATTRIBUTEVALUE, "value", raapi.findPrimitiveDataType("String"));
			if (INTERATTRIBUTEVALUE_VALUE == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute value of the class TDAKernel::InterAttributeValue.");
			}
			INTERDIRECTEDASSOCIATION_TARGETROLE = raapi.findAttribute(INTERDIRECTEDASSOCIATION, "targetRole");
			if ((INTERDIRECTEDASSOCIATION_TARGETROLE == 0) && insertMetamodel)
				INTERDIRECTEDASSOCIATION_TARGETROLE = raapi.createAttribute(INTERDIRECTEDASSOCIATION, "targetRole", raapi.findPrimitiveDataType("String"));
			if (INTERDIRECTEDASSOCIATION_TARGETROLE == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute targetRole of the class TDAKernel::InterDirectedAssociation.");
			}
			INTERDIRECTEDASSOCIATION_ISCOMPOSITION = raapi.findAttribute(INTERDIRECTEDASSOCIATION, "isComposition");
			if ((INTERDIRECTEDASSOCIATION_ISCOMPOSITION == 0) && insertMetamodel)
				INTERDIRECTEDASSOCIATION_ISCOMPOSITION = raapi.createAttribute(INTERDIRECTEDASSOCIATION, "isComposition", raapi.findPrimitiveDataType("Boolean"));
			if (INTERDIRECTEDASSOCIATION_ISCOMPOSITION == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute isComposition of the class TDAKernel::InterDirectedAssociation.");
			}
			INTERDIRECTEDASSOCIATION_SOURCECLASS = raapi.findAssociationEnd(INTERDIRECTEDASSOCIATION, "sourceClass");
			if ((INTERDIRECTEDASSOCIATION_SOURCECLASS == 0) && insertMetamodel) {
				INTERDIRECTEDASSOCIATION_SOURCECLASS = raapi.createAssociation(INTERDIRECTEDASSOCIATION, PROXYREFERENCE, "outgoingAssociation", "sourceClass", false);
			}
			if (INTERDIRECTEDASSOCIATION_SOURCECLASS == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end sourceClass of the class TDAKernel::InterDirectedAssociation.");
			}
			INTERDIRECTEDASSOCIATION_TARGETCLASS = raapi.findAssociationEnd(INTERDIRECTEDASSOCIATION, "targetClass");
			if ((INTERDIRECTEDASSOCIATION_TARGETCLASS == 0) && insertMetamodel) {
				INTERDIRECTEDASSOCIATION_TARGETCLASS = raapi.createAssociation(INTERDIRECTEDASSOCIATION, PROXYREFERENCE, "ingoingAssociation", "targetClass", false);
			}
			if (INTERDIRECTEDASSOCIATION_TARGETCLASS == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end targetClass of the class TDAKernel::InterDirectedAssociation.");
			}
			INTERDIRECTEDASSOCIATION_INVERSE = raapi.findAssociationEnd(INTERDIRECTEDASSOCIATION, "inverse");
			if ((INTERDIRECTEDASSOCIATION_INVERSE == 0) && insertMetamodel) {
				INTERDIRECTEDASSOCIATION_INVERSE = raapi.createDirectedAssociation(INTERDIRECTEDASSOCIATION, INTERDIRECTEDASSOCIATION, "inverse", false);
			} 
			if (INTERDIRECTEDASSOCIATION_INVERSE == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end inverse of the class TDAKernel::InterDirectedAssociation.");
			}
			REPOSITORY_MOUNTPOINT = raapi.findAssociationEnd(REPOSITORY, "mountPoint");
			if ((REPOSITORY_MOUNTPOINT == 0) && insertMetamodel) {
				REPOSITORY_MOUNTPOINT = raapi.createAssociation(REPOSITORY, PACKAGE, "mountedRepository", "mountPoint", false);
			}
			if (REPOSITORY_MOUNTPOINT == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end mountPoint of the class TDAKernel::Repository.");
			}
			REPOSITORY_PACKAGE = raapi.findAssociationEnd(REPOSITORY, "package");
			if ((REPOSITORY_PACKAGE == 0) && insertMetamodel) {
				REPOSITORY_PACKAGE = raapi.createAssociation(REPOSITORY, PACKAGE, "associatedRepository", "package", false);
			}
			if (REPOSITORY_PACKAGE == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end package of the class TDAKernel::Repository.");
			}
			REPOSITORY_URI = raapi.findAttribute(REPOSITORY, "uri");
			if ((REPOSITORY_URI == 0) && insertMetamodel)
				REPOSITORY_URI = raapi.createAttribute(REPOSITORY, "uri", raapi.findPrimitiveDataType("String"));
			if (REPOSITORY_URI == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute uri of the class TDAKernel::Repository.");
			}
			REPOSITORY_PROXYREFERENCE = raapi.findAssociationEnd(REPOSITORY, "proxyReference");
			if ((REPOSITORY_PROXYREFERENCE == 0) && insertMetamodel) {
				REPOSITORY_PROXYREFERENCE = raapi.createAssociation(REPOSITORY, PROXYREFERENCE, "repository", "proxyReference", false);
			}
			if (REPOSITORY_PROXYREFERENCE == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end proxyReference of the class TDAKernel::Repository.");
			}
			REPOSITORY_COVEREDPACKAGE = raapi.findAssociationEnd(REPOSITORY, "coveredPackage");
			if ((REPOSITORY_COVEREDPACKAGE == 0) && insertMetamodel) {
				REPOSITORY_COVEREDPACKAGE = raapi.createAssociation(REPOSITORY, PACKAGE, "stackedRepository", "coveredPackage", false);
			}
			if (REPOSITORY_COVEREDPACKAGE == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end coveredPackage of the class TDAKernel::Repository.");
			}
			PACKAGE_MOUNTEDREPOSITORY = raapi.findAssociationEnd(PACKAGE, "mountedRepository");
			if ((PACKAGE_MOUNTEDREPOSITORY == 0) && insertMetamodel) {
				PACKAGE_MOUNTEDREPOSITORY = raapi.createAssociation(PACKAGE, REPOSITORY, "mountPoint", "mountedRepository", false);
			}
			if (PACKAGE_MOUNTEDREPOSITORY == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end mountedRepository of the class TDAKernel::Package.");
			}
			PACKAGE_PARENT = raapi.findAssociationEnd(PACKAGE, "parent");
			if ((PACKAGE_PARENT == 0) && insertMetamodel) {
				PACKAGE_PARENT = raapi.createAssociation(PACKAGE, PACKAGE, "child", "parent", false);
			}
			if (PACKAGE_PARENT == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end parent of the class TDAKernel::Package.");
			}
			PACKAGE_CHILD = raapi.findAssociationEnd(PACKAGE, "child");
			if ((PACKAGE_CHILD == 0) && insertMetamodel) {
				PACKAGE_CHILD = raapi.createAssociation(PACKAGE, PACKAGE, "parent", "child", false);
			}
			if (PACKAGE_CHILD == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end child of the class TDAKernel::Package.");
			}
			PACKAGE_ASSOCIATEDREPOSITORY = raapi.findAssociationEnd(PACKAGE, "associatedRepository");
			if ((PACKAGE_ASSOCIATEDREPOSITORY == 0) && insertMetamodel) {
				PACKAGE_ASSOCIATEDREPOSITORY = raapi.createAssociation(PACKAGE, REPOSITORY, "package", "associatedRepository", false);
			}
			if (PACKAGE_ASSOCIATEDREPOSITORY == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end associatedRepository of the class TDAKernel::Package.");
			}
			PACKAGE_KERNEL = raapi.findAssociationEnd(PACKAGE, "kernel");
			if ((PACKAGE_KERNEL == 0) && insertMetamodel) {
				PACKAGE_KERNEL = raapi.createAssociation(PACKAGE, TDAKERNEL, "rootPackage", "kernel", false);
			}
			if (PACKAGE_KERNEL == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end kernel of the class TDAKernel::Package.");
			}
			PACKAGE_PROXYREFERENCE = raapi.findAssociationEnd(PACKAGE, "proxyReference");
			if ((PACKAGE_PROXYREFERENCE == 0) && insertMetamodel) {
				PACKAGE_PROXYREFERENCE = raapi.createAssociation(PACKAGE, PROXYREFERENCE, "package", "proxyReference", false);
			}
			if (PACKAGE_PROXYREFERENCE == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end proxyReference of the class TDAKernel::Package.");
			}
			PACKAGE_SIMPLENAME = raapi.findAttribute(PACKAGE, "simpleName");
			if ((PACKAGE_SIMPLENAME == 0) && insertMetamodel)
				PACKAGE_SIMPLENAME = raapi.createAttribute(PACKAGE, "simpleName", raapi.findPrimitiveDataType("String"));
			if (PACKAGE_SIMPLENAME == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute simpleName of the class TDAKernel::Package.");
			}
			PACKAGE_STACKEDREPOSITORY = raapi.findAssociationEnd(PACKAGE, "stackedRepository");
			if ((PACKAGE_STACKEDREPOSITORY == 0) && insertMetamodel) {
				PACKAGE_STACKEDREPOSITORY = raapi.createAssociation(PACKAGE, REPOSITORY, "coveredPackage", "stackedRepository", false);
			}
			if (PACKAGE_STACKEDREPOSITORY == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end stackedRepository of the class TDAKernel::Package.");
			}
			INSERTMETAMODELCOMMAND_URL = raapi.findAttribute(INSERTMETAMODELCOMMAND, "url");
			if ((INSERTMETAMODELCOMMAND_URL == 0) && insertMetamodel)
				INSERTMETAMODELCOMMAND_URL = raapi.createAttribute(INSERTMETAMODELCOMMAND, "url", raapi.findPrimitiveDataType("String"));
			if (INSERTMETAMODELCOMMAND_URL == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute url of the class TDAKernel::InsertMetamodelCommand.");
			}
			LAUNCHTRANSFORMATIONINBACKGROUNDCOMMAND_URI = raapi.findAttribute(LAUNCHTRANSFORMATIONINBACKGROUNDCOMMAND, "uri");
			if ((LAUNCHTRANSFORMATIONINBACKGROUNDCOMMAND_URI == 0) && insertMetamodel)
				LAUNCHTRANSFORMATIONINBACKGROUNDCOMMAND_URI = raapi.createAttribute(LAUNCHTRANSFORMATIONINBACKGROUNDCOMMAND, "uri", raapi.findPrimitiveDataType("String"));
			if (LAUNCHTRANSFORMATIONINBACKGROUNDCOMMAND_URI == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute uri of the class TDAKernel::LaunchTransformationInBackgroundCommand.");
			}
		}
	}

	public ProxyReference createProxyReference()
	{
		ProxyReference retVal = new ProxyReference(this);
		wrappers.put(retVal.getRAAPIReference(), retVal);
		return retVal; 
	}
  
	public TDAKernel createTDAKernel()
	{
		TDAKernel retVal = new TDAKernel(this);
		wrappers.put(retVal.getRAAPIReference(), retVal);
		return retVal; 
	}
  
	public Command createCommand()
	{
		Command retVal = new Command(this);
		wrappers.put(retVal.getRAAPIReference(), retVal);
		return retVal; 
	}
  
	public Submitter createSubmitter()
	{
		Submitter retVal = new Submitter(this);
		wrappers.put(retVal.getRAAPIReference(), retVal);
		return retVal; 
	}
  
	public Event createEvent()
	{
		Event retVal = new Event(this);
		wrappers.put(retVal.getRAAPIReference(), retVal);
		return retVal; 
	}
  
	public AttachEngineCommand createAttachEngineCommand()
	{
		AttachEngineCommand retVal = new AttachEngineCommand(this);
		wrappers.put(retVal.getRAAPIReference(), retVal);
		return retVal; 
	}
  
	public Engine createEngine()
	{
		Engine retVal = new Engine(this);
		wrappers.put(retVal.getRAAPIReference(), retVal);
		return retVal; 
	}
  
	public LaunchTransformationCommand createLaunchTransformationCommand()
	{
		LaunchTransformationCommand retVal = new LaunchTransformationCommand(this);
		wrappers.put(retVal.getRAAPIReference(), retVal);
		return retVal; 
	}
  
	public MountRepositoryCommand createMountRepositoryCommand()
	{
		MountRepositoryCommand retVal = new MountRepositoryCommand(this);
		wrappers.put(retVal.getRAAPIReference(), retVal);
		return retVal; 
	}
  
	public UnmountRepositoryCommand createUnmountRepositoryCommand()
	{
		UnmountRepositoryCommand retVal = new UnmountRepositoryCommand(this);
		wrappers.put(retVal.getRAAPIReference(), retVal);
		return retVal; 
	}
  
	public SaveCommand createSaveCommand()
	{
		SaveCommand retVal = new SaveCommand(this);
		wrappers.put(retVal.getRAAPIReference(), retVal);
		return retVal; 
	}
  
	public InterDirectedLink createInterDirectedLink()
	{
		InterDirectedLink retVal = new InterDirectedLink(this);
		wrappers.put(retVal.getRAAPIReference(), retVal);
		return retVal; 
	}
  
	public InterAttributeValue createInterAttributeValue()
	{
		InterAttributeValue retVal = new InterAttributeValue(this);
		wrappers.put(retVal.getRAAPIReference(), retVal);
		return retVal; 
	}
  
	public InterDirectedAssociation createInterDirectedAssociation()
	{
		InterDirectedAssociation retVal = new InterDirectedAssociation(this);
		wrappers.put(retVal.getRAAPIReference(), retVal);
		return retVal; 
	}
  
	public Repository createRepository()
	{
		Repository retVal = new Repository(this);
		wrappers.put(retVal.getRAAPIReference(), retVal);
		return retVal; 
	}
  
	public Package createPackage()
	{
		Package retVal = new Package(this);
		wrappers.put(retVal.getRAAPIReference(), retVal);
		return retVal; 
	}
  
	public InsertMetamodelCommand createInsertMetamodelCommand()
	{
		InsertMetamodelCommand retVal = new InsertMetamodelCommand(this);
		wrappers.put(retVal.getRAAPIReference(), retVal);
		return retVal; 
	}
  
	public SaveStartedEvent createSaveStartedEvent()
	{
		SaveStartedEvent retVal = new SaveStartedEvent(this);
		wrappers.put(retVal.getRAAPIReference(), retVal);
		return retVal; 
	}
  
	public SaveFinishedEvent createSaveFinishedEvent()
	{
		SaveFinishedEvent retVal = new SaveFinishedEvent(this);
		wrappers.put(retVal.getRAAPIReference(), retVal);
		return retVal; 
	}
  
	public SaveFailedEvent createSaveFailedEvent()
	{
		SaveFailedEvent retVal = new SaveFailedEvent(this);
		wrappers.put(retVal.getRAAPIReference(), retVal);
		return retVal; 
	}
  
	public AsyncCommand createAsyncCommand()
	{
		AsyncCommand retVal = new AsyncCommand(this);
		wrappers.put(retVal.getRAAPIReference(), retVal);
		return retVal; 
	}
  
	public LaunchTransformationInBackgroundCommand createLaunchTransformationInBackgroundCommand()
	{
		LaunchTransformationInBackgroundCommand retVal = new LaunchTransformationInBackgroundCommand(this);
		wrappers.put(retVal.getRAAPIReference(), retVal);
		return retVal; 
	}
  
}
