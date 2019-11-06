// automatically generated
package lv.lumii.tda.ee.mm;
import lv.lumii.tda.raapi.RAAPI;
import java.util.*;

public class EnvironmentEngineMetamodelFactory
{
	// for compatibility with ECore
	public static EnvironmentEngineMetamodelFactory eINSTANCE = new EnvironmentEngineMetamodelFactory();

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
			java.lang.reflect.Constructor<? extends RAAPIReferenceWrapper> c = cls1.getConstructor(EnvironmentEngineMetamodelFactory.class, Long.TYPE, Boolean.TYPE);
			return (RAAPIReferenceWrapper)c.newInstance(this, rObject, takeReference);
		} catch (Throwable t1) {
			try {
				java.lang.reflect.Constructor<? extends RAAPIReferenceWrapper> c = cls.getConstructor(EnvironmentEngineMetamodelFactory.class, Long.TYPE, Boolean.TYPE);
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

		if (raapi.isKindOf(rObject, ENGINE)) {
			if ((rCurClass == 0) || (raapi.isDerivedClass(ENGINE,rCurClass))) {
				retVal = Engine.class;
				rCurClass = ENGINE;
			}
		}
		if (raapi.isKindOf(rObject, ATTACHFRAMECOMMAND)) {
			if ((rCurClass == 0) || (raapi.isDerivedClass(ATTACHFRAMECOMMAND,rCurClass))) {
				retVal = AttachFrameCommand.class;
				rCurClass = ATTACHFRAMECOMMAND;
			}
		}
		if (raapi.isKindOf(rObject, FRAMEACTIVATEDEVENT)) {
			if ((rCurClass == 0) || (raapi.isDerivedClass(FRAMEACTIVATEDEVENT,rCurClass))) {
				retVal = FrameActivatedEvent.class;
				rCurClass = FRAMEACTIVATEDEVENT;
			}
		}
		if (raapi.isKindOf(rObject, FRAMEDEACTIVATINGEVENT)) {
			if ((rCurClass == 0) || (raapi.isDerivedClass(FRAMEDEACTIVATINGEVENT,rCurClass))) {
				retVal = FrameDeactivatingEvent.class;
				rCurClass = FRAMEDEACTIVATINGEVENT;
			}
		}
		if (raapi.isKindOf(rObject, CLOSEFRAMEREQUESTEDEVENT)) {
			if ((rCurClass == 0) || (raapi.isDerivedClass(CLOSEFRAMEREQUESTEDEVENT,rCurClass))) {
				retVal = CloseFrameRequestedEvent.class;
				rCurClass = CLOSEFRAMEREQUESTEDEVENT;
			}
		}
		if (raapi.isKindOf(rObject, DETACHFRAMECOMMAND)) {
			if ((rCurClass == 0) || (raapi.isDerivedClass(DETACHFRAMECOMMAND,rCurClass))) {
				retVal = DetachFrameCommand.class;
				rCurClass = DETACHFRAMECOMMAND;
			}
		}
		if (raapi.isKindOf(rObject, REFRESHOPTIONSCOMMAND)) {
			if ((rCurClass == 0) || (raapi.isDerivedClass(REFRESHOPTIONSCOMMAND,rCurClass))) {
				retVal = RefreshOptionsCommand.class;
				rCurClass = REFRESHOPTIONSCOMMAND;
			}
		}
		if (raapi.isKindOf(rObject, OPTIONSELECTEDEVENT)) {
			if ((rCurClass == 0) || (raapi.isDerivedClass(OPTIONSELECTEDEVENT,rCurClass))) {
				retVal = OptionSelectedEvent.class;
				rCurClass = OPTIONSELECTEDEVENT;
			}
		}
		if (raapi.isKindOf(rObject, PROJECTCREATEDEVENT)) {
			if ((rCurClass == 0) || (raapi.isDerivedClass(PROJECTCREATEDEVENT,rCurClass))) {
				retVal = ProjectCreatedEvent.class;
				rCurClass = PROJECTCREATEDEVENT;
			}
		}
		if (raapi.isKindOf(rObject, PROJECTOPENEDEVENT)) {
			if ((rCurClass == 0) || (raapi.isDerivedClass(PROJECTOPENEDEVENT,rCurClass))) {
				retVal = ProjectOpenedEvent.class;
				rCurClass = PROJECTOPENEDEVENT;
			}
		}
		if (raapi.isKindOf(rObject, CLOSEPROJECTCOMMAND)) {
			if ((rCurClass == 0) || (raapi.isDerivedClass(CLOSEPROJECTCOMMAND,rCurClass))) {
				retVal = CloseProjectCommand.class;
				rCurClass = CLOSEPROJECTCOMMAND;
			}
		}
		if (raapi.isKindOf(rObject, PROJECTCLOSINGEVENT)) {
			if ((rCurClass == 0) || (raapi.isDerivedClass(PROJECTCLOSINGEVENT,rCurClass))) {
				retVal = ProjectClosingEvent.class;
				rCurClass = PROJECTCLOSINGEVENT;
			}
		}
		if (raapi.isKindOf(rObject, FRAMERESIZEDEVENT)) {
			if ((rCurClass == 0) || (raapi.isDerivedClass(FRAMERESIZEDEVENT,rCurClass))) {
				retVal = FrameResizedEvent.class;
				rCurClass = FRAMERESIZEDEVENT;
			}
		}
		if (raapi.isKindOf(rObject, OPTION)) {
			if ((rCurClass == 0) || (raapi.isDerivedClass(OPTION,rCurClass))) {
				retVal = Option.class;
				rCurClass = OPTION;
			}
		}
		if (raapi.isKindOf(rObject, FRAME)) {
			if ((rCurClass == 0) || (raapi.isDerivedClass(FRAME,rCurClass))) {
				retVal = Frame.class;
				rCurClass = FRAME;
			}
		}
		if (raapi.isKindOf(rObject, ENVIRONMENTENGINE)) {
			if ((rCurClass == 0) || (raapi.isDerivedClass(ENVIRONMENTENGINE,rCurClass))) {
				retVal = EnvironmentEngine.class;
				rCurClass = ENVIRONMENTENGINE;
			}
		}
		if (raapi.isKindOf(rObject, EVENT)) {
			if ((rCurClass == 0) || (raapi.isDerivedClass(EVENT,rCurClass))) {
				retVal = Event.class;
				rCurClass = EVENT;
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
		if (raapi.isKindOf(rObject, ATTACHENGINECOMMAND)) {
			if ((rCurClass == 0) || (raapi.isDerivedClass(ATTACHENGINECOMMAND,rCurClass))) {
				retVal = AttachEngineCommand.class;
				rCurClass = ATTACHENGINECOMMAND;
			}
		}
		if (raapi.isKindOf(rObject, LAUNCHTRANSFORMATIONINFOREGROUNDCOMMAND)) {
			if ((rCurClass == 0) || (raapi.isDerivedClass(LAUNCHTRANSFORMATIONINFOREGROUNDCOMMAND,rCurClass))) {
				retVal = LaunchTransformationInForegroundCommand.class;
				rCurClass = LAUNCHTRANSFORMATIONINFOREGROUNDCOMMAND;
			}
		}
		if (raapi.isKindOf(rObject, ACTIVATEFRAMECOMMAND)) {
			if ((rCurClass == 0) || (raapi.isDerivedClass(ACTIVATEFRAMECOMMAND,rCurClass))) {
				retVal = ActivateFrameCommand.class;
				rCurClass = ACTIVATEFRAMECOMMAND;
			}
		}
		if (raapi.isKindOf(rObject, LAUNCHGUITRANSFORMATIONCOMMAND)) {
			if ((rCurClass == 0) || (raapi.isDerivedClass(LAUNCHGUITRANSFORMATIONCOMMAND,rCurClass))) {
				retVal = LaunchGUITransformationCommand.class;
				rCurClass = LAUNCHGUITRANSFORMATIONCOMMAND;
			}
		}
		if (raapi.isKindOf(rObject, SHOWINFORMATIONBARCOMMAND)) {
			if ((rCurClass == 0) || (raapi.isDerivedClass(SHOWINFORMATIONBARCOMMAND,rCurClass))) {
				retVal = ShowInformationBarCommand.class;
				rCurClass = SHOWINFORMATIONBARCOMMAND;
			}
		}
		if (raapi.isKindOf(rObject, UNDOIGNORINGCLASS)) {
			if ((rCurClass == 0) || (raapi.isDerivedClass(UNDOIGNORINGCLASS,rCurClass))) {
				retVal = UndoIgnoringClass.class;
				rCurClass = UNDOIGNORINGCLASS;
			}
		}
		if (raapi.isKindOf(rObject, ASYNCCOMMAND)) {
			if ((rCurClass == 0) || (raapi.isDerivedClass(ASYNCCOMMAND,rCurClass))) {
				retVal = AsyncCommand.class;
				rCurClass = ASYNCCOMMAND;
			}
		}
		if (raapi.isKindOf(rObject, ENQUEUEFOREGROUNDTRANSFORMATIONCOMMAND)) {
			if ((rCurClass == 0) || (raapi.isDerivedClass(ENQUEUEFOREGROUNDTRANSFORMATIONCOMMAND,rCurClass))) {
				retVal = EnqueueForegroundTransformationCommand.class;
				rCurClass = ENQUEUEFOREGROUNDTRANSFORMATIONCOMMAND;
			}
		}
		if (raapi.isKindOf(rObject, POSTMESSAGETOFRAMECOMMAND)) {
			if ((rCurClass == 0) || (raapi.isDerivedClass(POSTMESSAGETOFRAMECOMMAND,rCurClass))) {
				retVal = PostMessageToFrameCommand.class;
				rCurClass = POSTMESSAGETOFRAMECOMMAND;
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
		if (rClass == ENGINE)
			w = new Engine(this, rObject, takeReference);
		if (rClass == ATTACHFRAMECOMMAND)
			w = new AttachFrameCommand(this, rObject, takeReference);
		if (rClass == FRAMEACTIVATEDEVENT)
			w = new FrameActivatedEvent(this, rObject, takeReference);
		if (rClass == FRAMEDEACTIVATINGEVENT)
			w = new FrameDeactivatingEvent(this, rObject, takeReference);
		if (rClass == CLOSEFRAMEREQUESTEDEVENT)
			w = new CloseFrameRequestedEvent(this, rObject, takeReference);
		if (rClass == DETACHFRAMECOMMAND)
			w = new DetachFrameCommand(this, rObject, takeReference);
		if (rClass == REFRESHOPTIONSCOMMAND)
			w = new RefreshOptionsCommand(this, rObject, takeReference);
		if (rClass == OPTIONSELECTEDEVENT)
			w = new OptionSelectedEvent(this, rObject, takeReference);
		if (rClass == PROJECTCREATEDEVENT)
			w = new ProjectCreatedEvent(this, rObject, takeReference);
		if (rClass == PROJECTOPENEDEVENT)
			w = new ProjectOpenedEvent(this, rObject, takeReference);
		if (rClass == CLOSEPROJECTCOMMAND)
			w = new CloseProjectCommand(this, rObject, takeReference);
		if (rClass == PROJECTCLOSINGEVENT)
			w = new ProjectClosingEvent(this, rObject, takeReference);
		if (rClass == FRAMERESIZEDEVENT)
			w = new FrameResizedEvent(this, rObject, takeReference);
		if (rClass == OPTION)
			w = new Option(this, rObject, takeReference);
		if (rClass == FRAME)
			w = new Frame(this, rObject, takeReference);
		if (rClass == ENVIRONMENTENGINE)
			w = new EnvironmentEngine(this, rObject, takeReference);
		if (rClass == EVENT)
			w = new Event(this, rObject, takeReference);
		if (rClass == COMMAND)
			w = new Command(this, rObject, takeReference);
		if (rClass == SUBMITTER)
			w = new Submitter(this, rObject, takeReference);
		if (rClass == ATTACHENGINECOMMAND)
			w = new AttachEngineCommand(this, rObject, takeReference);
		if (rClass == LAUNCHTRANSFORMATIONINFOREGROUNDCOMMAND)
			w = new LaunchTransformationInForegroundCommand(this, rObject, takeReference);
		if (rClass == ACTIVATEFRAMECOMMAND)
			w = new ActivateFrameCommand(this, rObject, takeReference);
		if (rClass == LAUNCHGUITRANSFORMATIONCOMMAND)
			w = new LaunchGUITransformationCommand(this, rObject, takeReference);
		if (rClass == SHOWINFORMATIONBARCOMMAND)
			w = new ShowInformationBarCommand(this, rObject, takeReference);
		if (rClass == UNDOIGNORINGCLASS)
			w = new UndoIgnoringClass(this, rObject, takeReference);
		if (rClass == ASYNCCOMMAND)
			w = new AsyncCommand(this, rObject, takeReference);
		if (rClass == ENQUEUEFOREGROUNDTRANSFORMATIONCOMMAND)
			w = new EnqueueForegroundTransformationCommand(this, rObject, takeReference);
		if (rClass == POSTMESSAGETOFRAMECOMMAND)
			w = new PostMessageToFrameCommand(this, rObject, takeReference);
		if (w==null) {
		}
		if ((w != null) && takeReference)
			wrappers.put(rObject, w);
		return w;
	}

	public boolean deleteModel()
	{
		boolean ok = true;
		if (!Engine.deleteAllObjects(this))
			ok = false;
		if (!AttachFrameCommand.deleteAllObjects(this))
			ok = false;
		if (!FrameActivatedEvent.deleteAllObjects(this))
			ok = false;
		if (!FrameDeactivatingEvent.deleteAllObjects(this))
			ok = false;
		if (!CloseFrameRequestedEvent.deleteAllObjects(this))
			ok = false;
		if (!DetachFrameCommand.deleteAllObjects(this))
			ok = false;
		if (!RefreshOptionsCommand.deleteAllObjects(this))
			ok = false;
		if (!OptionSelectedEvent.deleteAllObjects(this))
			ok = false;
		if (!ProjectCreatedEvent.deleteAllObjects(this))
			ok = false;
		if (!ProjectOpenedEvent.deleteAllObjects(this))
			ok = false;
		if (!CloseProjectCommand.deleteAllObjects(this))
			ok = false;
		if (!ProjectClosingEvent.deleteAllObjects(this))
			ok = false;
		if (!FrameResizedEvent.deleteAllObjects(this))
			ok = false;
		if (!Option.deleteAllObjects(this))
			ok = false;
		if (!Frame.deleteAllObjects(this))
			ok = false;
		if (!EnvironmentEngine.deleteAllObjects(this))
			ok = false;
		if (!Event.deleteAllObjects(this))
			ok = false;
		if (!Command.deleteAllObjects(this))
			ok = false;
		if (!Submitter.deleteAllObjects(this))
			ok = false;
		if (!AttachEngineCommand.deleteAllObjects(this))
			ok = false;
		if (!LaunchTransformationInForegroundCommand.deleteAllObjects(this))
			ok = false;
		if (!ActivateFrameCommand.deleteAllObjects(this))
			ok = false;
		if (!LaunchGUITransformationCommand.deleteAllObjects(this))
			ok = false;
		if (!ShowInformationBarCommand.deleteAllObjects(this))
			ok = false;
		if (!UndoIgnoringClass.deleteAllObjects(this))
			ok = false;
		if (!AsyncCommand.deleteAllObjects(this))
			ok = false;
		if (!EnqueueForegroundTransformationCommand.deleteAllObjects(this))
			ok = false;
		if (!PostMessageToFrameCommand.deleteAllObjects(this))
			ok = false;
		return ok; 
	}

	// RAAPI references:
	RAAPI raapi = null;
	public long ENGINE = 0;
	public long ATTACHFRAMECOMMAND = 0;
	  public long ATTACHFRAMECOMMAND_FRAME = 0;
	public long FRAMEACTIVATEDEVENT = 0;
	  public long FRAMEACTIVATEDEVENT_FRAME = 0;
	public long FRAMEDEACTIVATINGEVENT = 0;
	  public long FRAMEDEACTIVATINGEVENT_FRAME = 0;
	public long CLOSEFRAMEREQUESTEDEVENT = 0;
	  public long CLOSEFRAMEREQUESTEDEVENT_FRAME = 0;
	  public long CLOSEFRAMEREQUESTEDEVENT_FORCE = 0;
	public long DETACHFRAMECOMMAND = 0;
	  public long DETACHFRAMECOMMAND_FRAME = 0;
	  public long DETACHFRAMECOMMAND_PERMANENTLY = 0;
	public long REFRESHOPTIONSCOMMAND = 0;
	  public long REFRESHOPTIONSCOMMAND_ENVIRONMENTENGINE = 0;
	public long OPTIONSELECTEDEVENT = 0;
	  public long OPTIONSELECTEDEVENT_OPTION = 0;
	public long PROJECTCREATEDEVENT = 0;
	public long PROJECTOPENEDEVENT = 0;
	public long CLOSEPROJECTCOMMAND = 0;
	  public long CLOSEPROJECTCOMMAND_SILENT = 0;
	public long PROJECTCLOSINGEVENT = 0;
	public long FRAMERESIZEDEVENT = 0;
	  public long FRAMERESIZEDEVENT_FRAME = 0;
	  public long FRAMERESIZEDEVENT_WIDTH = 0;
	  public long FRAMERESIZEDEVENT_HEIGHT = 0;
	public long OPTION = 0;
	  public long OPTION_CAPTION = 0;
	  public long OPTION_IMAGE = 0;
	  public long OPTION_ID = 0;
	  public long OPTION_LOCATION = 0;
	  public long OPTION_ONOPTIONSELECTEDEVENT = 0;
	  public long OPTION_ENVIRONMENTENGINE = 0;
	  public long OPTION_OPTIONSELECTEDEVENT = 0;
	  public long OPTION_PARENT = 0;
	  public long OPTION_CHILD = 0;
	  public long OPTION_FRAME = 0;
	  public long OPTION_ISENABLED = 0;
	public long FRAME = 0;
	  public long FRAME_CAPTION = 0;
	  public long FRAME_CONTENTURI = 0;
	  public long FRAME_LOCATION = 0;
	  public long FRAME_ISRESIZEABLE = 0;
	  public long FRAME_ISCLOSABLE = 0;
	  public long FRAME_ONFRAMEACTIVATEDEVENT = 0;
	  public long FRAME_ONFRAMEDEACTIVATINGEVENT = 0;
	  public long FRAME_ONFRAMERESIZEDEVENT = 0;
	  public long FRAME_ONCLOSEFRAMEREQUESTEDEVENT = 0;
	  public long FRAME_ENVIRONMENTENGINE = 0;
	  public long FRAME_ATTACHFRAMECOMMAND = 0;
	  public long FRAME_DETACHFRAMECOMMAND = 0;
	  public long FRAME_FRAMEACTIVATEDEVENT = 0;
	  public long FRAME_FRAMEDEACTIVATINGEVENT = 0;
	  public long FRAME_CLOSEFRAMEREQUESTEDEVENT = 0;
	  public long FRAME_FRAMERESIZEDEVENT = 0;
	  public long FRAME_OPTION = 0;
	  public long FRAME_POSTMESSAGETOFRAMECOMMAND = 0;
	  public long FRAME_ACTIVATEFRAMECOMMAND = 0;
	public long ENVIRONMENTENGINE = 0;
	  public long ENVIRONMENTENGINE_LANGUAGE = 0;
	  public long ENVIRONMENTENGINE_COUNTRY = 0;
	  public long ENVIRONMENTENGINE_ANYUNSAVEDCHANGES = 0;
	  public long ENVIRONMENTENGINE_COMMONBINDIRECTORY = 0;
	  public long ENVIRONMENTENGINE_SPECIFICBINDIRECTORY = 0;
	  public long ENVIRONMENTENGINE_PROJECTDIRECTORY = 0;
	  public long ENVIRONMENTENGINE_ONPROJECTOPENEDEVENT = 0;
	  public long ENVIRONMENTENGINE_ONPROJECTCLOSINGEVENT = 0;
	  public long ENVIRONMENTENGINE_OPTION = 0;
	  public long ENVIRONMENTENGINE_FRAME = 0;
	  public long ENVIRONMENTENGINE_REFRESHOPTIONSCOMMAND = 0;
	  public long ENVIRONMENTENGINE_DEFAULTSAVEDIRECTORY = 0;
	  public long ENVIRONMENTENGINE_STATUS = 0;
	  public long ENVIRONMENTENGINE_CLOUDLOCATION = 0;
	  public long ENVIRONMENTENGINE_CLIENTSESSIONID = 0;
	  public long ENVIRONMENTENGINE_CLIENTACTIONINDEX = 0;
	public long EVENT = 0;
	  public long EVENT_SUBMITTER = 0;
	public long COMMAND = 0;
	  public long COMMAND_SUBMITTER = 0;
	public long SUBMITTER = 0;
	  public long SUBMITTER_EVENT = 0;
	  public long SUBMITTER_COMMAND = 0;
	public long ATTACHENGINECOMMAND = 0;
	  public long ATTACHENGINECOMMAND_NAME = 0;
	public long LAUNCHTRANSFORMATIONINFOREGROUNDCOMMAND = 0;
	  public long LAUNCHTRANSFORMATIONINFOREGROUNDCOMMAND_URI = 0;
	  public long LAUNCHTRANSFORMATIONINFOREGROUNDCOMMAND_MESSAGE = 0;
	  public long LAUNCHTRANSFORMATIONINFOREGROUNDCOMMAND_PROGRESS = 0;
	public long ACTIVATEFRAMECOMMAND = 0;
	  public long ACTIVATEFRAMECOMMAND_FRAME = 0;
	public long LAUNCHGUITRANSFORMATIONCOMMAND = 0;
	  public long LAUNCHGUITRANSFORMATIONCOMMAND_URI = 0;
	public long SHOWINFORMATIONBARCOMMAND = 0;
	  public long SHOWINFORMATIONBARCOMMAND_MESSAGE = 0;
	public long UNDOIGNORINGCLASS = 0;
	public long ASYNCCOMMAND = 0;
	public long ENQUEUEFOREGROUNDTRANSFORMATIONCOMMAND = 0;
	public long POSTMESSAGETOFRAMECOMMAND = 0;
	  public long POSTMESSAGETOFRAMECOMMAND_MESSAGEURI = 0;
	  public long POSTMESSAGETOFRAMECOMMAND_FRAME = 0;

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
			if (ENGINE != 0) {
				raapi.freeReference(ENGINE);
				ENGINE = 0;
			}
			if (ATTACHFRAMECOMMAND != 0) {
				raapi.freeReference(ATTACHFRAMECOMMAND);
				ATTACHFRAMECOMMAND = 0;
			}
	  		if (ATTACHFRAMECOMMAND_FRAME != 0) {
				raapi.freeReference(ATTACHFRAMECOMMAND_FRAME);
				ATTACHFRAMECOMMAND_FRAME = 0;
			}
			if (FRAMEACTIVATEDEVENT != 0) {
				raapi.freeReference(FRAMEACTIVATEDEVENT);
				FRAMEACTIVATEDEVENT = 0;
			}
	  		if (FRAMEACTIVATEDEVENT_FRAME != 0) {
				raapi.freeReference(FRAMEACTIVATEDEVENT_FRAME);
				FRAMEACTIVATEDEVENT_FRAME = 0;
			}
			if (FRAMEDEACTIVATINGEVENT != 0) {
				raapi.freeReference(FRAMEDEACTIVATINGEVENT);
				FRAMEDEACTIVATINGEVENT = 0;
			}
	  		if (FRAMEDEACTIVATINGEVENT_FRAME != 0) {
				raapi.freeReference(FRAMEDEACTIVATINGEVENT_FRAME);
				FRAMEDEACTIVATINGEVENT_FRAME = 0;
			}
			if (CLOSEFRAMEREQUESTEDEVENT != 0) {
				raapi.freeReference(CLOSEFRAMEREQUESTEDEVENT);
				CLOSEFRAMEREQUESTEDEVENT = 0;
			}
	  		if (CLOSEFRAMEREQUESTEDEVENT_FRAME != 0) {
				raapi.freeReference(CLOSEFRAMEREQUESTEDEVENT_FRAME);
				CLOSEFRAMEREQUESTEDEVENT_FRAME = 0;
			}
	  		if (CLOSEFRAMEREQUESTEDEVENT_FORCE != 0) {
				raapi.freeReference(CLOSEFRAMEREQUESTEDEVENT_FORCE);
				CLOSEFRAMEREQUESTEDEVENT_FORCE = 0;
			}
			if (DETACHFRAMECOMMAND != 0) {
				raapi.freeReference(DETACHFRAMECOMMAND);
				DETACHFRAMECOMMAND = 0;
			}
	  		if (DETACHFRAMECOMMAND_FRAME != 0) {
				raapi.freeReference(DETACHFRAMECOMMAND_FRAME);
				DETACHFRAMECOMMAND_FRAME = 0;
			}
	  		if (DETACHFRAMECOMMAND_PERMANENTLY != 0) {
				raapi.freeReference(DETACHFRAMECOMMAND_PERMANENTLY);
				DETACHFRAMECOMMAND_PERMANENTLY = 0;
			}
			if (REFRESHOPTIONSCOMMAND != 0) {
				raapi.freeReference(REFRESHOPTIONSCOMMAND);
				REFRESHOPTIONSCOMMAND = 0;
			}
	  		if (REFRESHOPTIONSCOMMAND_ENVIRONMENTENGINE != 0) {
				raapi.freeReference(REFRESHOPTIONSCOMMAND_ENVIRONMENTENGINE);
				REFRESHOPTIONSCOMMAND_ENVIRONMENTENGINE = 0;
			}
			if (OPTIONSELECTEDEVENT != 0) {
				raapi.freeReference(OPTIONSELECTEDEVENT);
				OPTIONSELECTEDEVENT = 0;
			}
	  		if (OPTIONSELECTEDEVENT_OPTION != 0) {
				raapi.freeReference(OPTIONSELECTEDEVENT_OPTION);
				OPTIONSELECTEDEVENT_OPTION = 0;
			}
			if (PROJECTCREATEDEVENT != 0) {
				raapi.freeReference(PROJECTCREATEDEVENT);
				PROJECTCREATEDEVENT = 0;
			}
			if (PROJECTOPENEDEVENT != 0) {
				raapi.freeReference(PROJECTOPENEDEVENT);
				PROJECTOPENEDEVENT = 0;
			}
			if (CLOSEPROJECTCOMMAND != 0) {
				raapi.freeReference(CLOSEPROJECTCOMMAND);
				CLOSEPROJECTCOMMAND = 0;
			}
	  		if (CLOSEPROJECTCOMMAND_SILENT != 0) {
				raapi.freeReference(CLOSEPROJECTCOMMAND_SILENT);
				CLOSEPROJECTCOMMAND_SILENT = 0;
			}
			if (PROJECTCLOSINGEVENT != 0) {
				raapi.freeReference(PROJECTCLOSINGEVENT);
				PROJECTCLOSINGEVENT = 0;
			}
			if (FRAMERESIZEDEVENT != 0) {
				raapi.freeReference(FRAMERESIZEDEVENT);
				FRAMERESIZEDEVENT = 0;
			}
	  		if (FRAMERESIZEDEVENT_FRAME != 0) {
				raapi.freeReference(FRAMERESIZEDEVENT_FRAME);
				FRAMERESIZEDEVENT_FRAME = 0;
			}
	  		if (FRAMERESIZEDEVENT_WIDTH != 0) {
				raapi.freeReference(FRAMERESIZEDEVENT_WIDTH);
				FRAMERESIZEDEVENT_WIDTH = 0;
			}
	  		if (FRAMERESIZEDEVENT_HEIGHT != 0) {
				raapi.freeReference(FRAMERESIZEDEVENT_HEIGHT);
				FRAMERESIZEDEVENT_HEIGHT = 0;
			}
			if (OPTION != 0) {
				raapi.freeReference(OPTION);
				OPTION = 0;
			}
	  		if (OPTION_CAPTION != 0) {
				raapi.freeReference(OPTION_CAPTION);
				OPTION_CAPTION = 0;
			}
	  		if (OPTION_IMAGE != 0) {
				raapi.freeReference(OPTION_IMAGE);
				OPTION_IMAGE = 0;
			}
	  		if (OPTION_ID != 0) {
				raapi.freeReference(OPTION_ID);
				OPTION_ID = 0;
			}
	  		if (OPTION_LOCATION != 0) {
				raapi.freeReference(OPTION_LOCATION);
				OPTION_LOCATION = 0;
			}
	  		if (OPTION_ONOPTIONSELECTEDEVENT != 0) {
				raapi.freeReference(OPTION_ONOPTIONSELECTEDEVENT);
				OPTION_ONOPTIONSELECTEDEVENT = 0;
			}
	  		if (OPTION_ENVIRONMENTENGINE != 0) {
				raapi.freeReference(OPTION_ENVIRONMENTENGINE);
				OPTION_ENVIRONMENTENGINE = 0;
			}
	  		if (OPTION_OPTIONSELECTEDEVENT != 0) {
				raapi.freeReference(OPTION_OPTIONSELECTEDEVENT);
				OPTION_OPTIONSELECTEDEVENT = 0;
			}
	  		if (OPTION_PARENT != 0) {
				raapi.freeReference(OPTION_PARENT);
				OPTION_PARENT = 0;
			}
	  		if (OPTION_CHILD != 0) {
				raapi.freeReference(OPTION_CHILD);
				OPTION_CHILD = 0;
			}
	  		if (OPTION_FRAME != 0) {
				raapi.freeReference(OPTION_FRAME);
				OPTION_FRAME = 0;
			}
	  		if (OPTION_ISENABLED != 0) {
				raapi.freeReference(OPTION_ISENABLED);
				OPTION_ISENABLED = 0;
			}
			if (FRAME != 0) {
				raapi.freeReference(FRAME);
				FRAME = 0;
			}
	  		if (FRAME_CAPTION != 0) {
				raapi.freeReference(FRAME_CAPTION);
				FRAME_CAPTION = 0;
			}
	  		if (FRAME_CONTENTURI != 0) {
				raapi.freeReference(FRAME_CONTENTURI);
				FRAME_CONTENTURI = 0;
			}
	  		if (FRAME_LOCATION != 0) {
				raapi.freeReference(FRAME_LOCATION);
				FRAME_LOCATION = 0;
			}
	  		if (FRAME_ISRESIZEABLE != 0) {
				raapi.freeReference(FRAME_ISRESIZEABLE);
				FRAME_ISRESIZEABLE = 0;
			}
	  		if (FRAME_ISCLOSABLE != 0) {
				raapi.freeReference(FRAME_ISCLOSABLE);
				FRAME_ISCLOSABLE = 0;
			}
	  		if (FRAME_ONFRAMEACTIVATEDEVENT != 0) {
				raapi.freeReference(FRAME_ONFRAMEACTIVATEDEVENT);
				FRAME_ONFRAMEACTIVATEDEVENT = 0;
			}
	  		if (FRAME_ONFRAMEDEACTIVATINGEVENT != 0) {
				raapi.freeReference(FRAME_ONFRAMEDEACTIVATINGEVENT);
				FRAME_ONFRAMEDEACTIVATINGEVENT = 0;
			}
	  		if (FRAME_ONFRAMERESIZEDEVENT != 0) {
				raapi.freeReference(FRAME_ONFRAMERESIZEDEVENT);
				FRAME_ONFRAMERESIZEDEVENT = 0;
			}
	  		if (FRAME_ONCLOSEFRAMEREQUESTEDEVENT != 0) {
				raapi.freeReference(FRAME_ONCLOSEFRAMEREQUESTEDEVENT);
				FRAME_ONCLOSEFRAMEREQUESTEDEVENT = 0;
			}
	  		if (FRAME_ENVIRONMENTENGINE != 0) {
				raapi.freeReference(FRAME_ENVIRONMENTENGINE);
				FRAME_ENVIRONMENTENGINE = 0;
			}
	  		if (FRAME_ATTACHFRAMECOMMAND != 0) {
				raapi.freeReference(FRAME_ATTACHFRAMECOMMAND);
				FRAME_ATTACHFRAMECOMMAND = 0;
			}
	  		if (FRAME_DETACHFRAMECOMMAND != 0) {
				raapi.freeReference(FRAME_DETACHFRAMECOMMAND);
				FRAME_DETACHFRAMECOMMAND = 0;
			}
	  		if (FRAME_FRAMEACTIVATEDEVENT != 0) {
				raapi.freeReference(FRAME_FRAMEACTIVATEDEVENT);
				FRAME_FRAMEACTIVATEDEVENT = 0;
			}
	  		if (FRAME_FRAMEDEACTIVATINGEVENT != 0) {
				raapi.freeReference(FRAME_FRAMEDEACTIVATINGEVENT);
				FRAME_FRAMEDEACTIVATINGEVENT = 0;
			}
	  		if (FRAME_CLOSEFRAMEREQUESTEDEVENT != 0) {
				raapi.freeReference(FRAME_CLOSEFRAMEREQUESTEDEVENT);
				FRAME_CLOSEFRAMEREQUESTEDEVENT = 0;
			}
	  		if (FRAME_FRAMERESIZEDEVENT != 0) {
				raapi.freeReference(FRAME_FRAMERESIZEDEVENT);
				FRAME_FRAMERESIZEDEVENT = 0;
			}
	  		if (FRAME_OPTION != 0) {
				raapi.freeReference(FRAME_OPTION);
				FRAME_OPTION = 0;
			}
	  		if (FRAME_POSTMESSAGETOFRAMECOMMAND != 0) {
				raapi.freeReference(FRAME_POSTMESSAGETOFRAMECOMMAND);
				FRAME_POSTMESSAGETOFRAMECOMMAND = 0;
			}
	  		if (FRAME_ACTIVATEFRAMECOMMAND != 0) {
				raapi.freeReference(FRAME_ACTIVATEFRAMECOMMAND);
				FRAME_ACTIVATEFRAMECOMMAND = 0;
			}
			if (ENVIRONMENTENGINE != 0) {
				raapi.freeReference(ENVIRONMENTENGINE);
				ENVIRONMENTENGINE = 0;
			}
	  		if (ENVIRONMENTENGINE_LANGUAGE != 0) {
				raapi.freeReference(ENVIRONMENTENGINE_LANGUAGE);
				ENVIRONMENTENGINE_LANGUAGE = 0;
			}
	  		if (ENVIRONMENTENGINE_COUNTRY != 0) {
				raapi.freeReference(ENVIRONMENTENGINE_COUNTRY);
				ENVIRONMENTENGINE_COUNTRY = 0;
			}
	  		if (ENVIRONMENTENGINE_ANYUNSAVEDCHANGES != 0) {
				raapi.freeReference(ENVIRONMENTENGINE_ANYUNSAVEDCHANGES);
				ENVIRONMENTENGINE_ANYUNSAVEDCHANGES = 0;
			}
	  		if (ENVIRONMENTENGINE_COMMONBINDIRECTORY != 0) {
				raapi.freeReference(ENVIRONMENTENGINE_COMMONBINDIRECTORY);
				ENVIRONMENTENGINE_COMMONBINDIRECTORY = 0;
			}
	  		if (ENVIRONMENTENGINE_SPECIFICBINDIRECTORY != 0) {
				raapi.freeReference(ENVIRONMENTENGINE_SPECIFICBINDIRECTORY);
				ENVIRONMENTENGINE_SPECIFICBINDIRECTORY = 0;
			}
	  		if (ENVIRONMENTENGINE_PROJECTDIRECTORY != 0) {
				raapi.freeReference(ENVIRONMENTENGINE_PROJECTDIRECTORY);
				ENVIRONMENTENGINE_PROJECTDIRECTORY = 0;
			}
	  		if (ENVIRONMENTENGINE_ONPROJECTOPENEDEVENT != 0) {
				raapi.freeReference(ENVIRONMENTENGINE_ONPROJECTOPENEDEVENT);
				ENVIRONMENTENGINE_ONPROJECTOPENEDEVENT = 0;
			}
	  		if (ENVIRONMENTENGINE_ONPROJECTCLOSINGEVENT != 0) {
				raapi.freeReference(ENVIRONMENTENGINE_ONPROJECTCLOSINGEVENT);
				ENVIRONMENTENGINE_ONPROJECTCLOSINGEVENT = 0;
			}
	  		if (ENVIRONMENTENGINE_OPTION != 0) {
				raapi.freeReference(ENVIRONMENTENGINE_OPTION);
				ENVIRONMENTENGINE_OPTION = 0;
			}
	  		if (ENVIRONMENTENGINE_FRAME != 0) {
				raapi.freeReference(ENVIRONMENTENGINE_FRAME);
				ENVIRONMENTENGINE_FRAME = 0;
			}
	  		if (ENVIRONMENTENGINE_REFRESHOPTIONSCOMMAND != 0) {
				raapi.freeReference(ENVIRONMENTENGINE_REFRESHOPTIONSCOMMAND);
				ENVIRONMENTENGINE_REFRESHOPTIONSCOMMAND = 0;
			}
	  		if (ENVIRONMENTENGINE_DEFAULTSAVEDIRECTORY != 0) {
				raapi.freeReference(ENVIRONMENTENGINE_DEFAULTSAVEDIRECTORY);
				ENVIRONMENTENGINE_DEFAULTSAVEDIRECTORY = 0;
			}
	  		if (ENVIRONMENTENGINE_STATUS != 0) {
				raapi.freeReference(ENVIRONMENTENGINE_STATUS);
				ENVIRONMENTENGINE_STATUS = 0;
			}
	  		if (ENVIRONMENTENGINE_CLOUDLOCATION != 0) {
				raapi.freeReference(ENVIRONMENTENGINE_CLOUDLOCATION);
				ENVIRONMENTENGINE_CLOUDLOCATION = 0;
			}
	  		if (ENVIRONMENTENGINE_CLIENTSESSIONID != 0) {
				raapi.freeReference(ENVIRONMENTENGINE_CLIENTSESSIONID);
				ENVIRONMENTENGINE_CLIENTSESSIONID = 0;
			}
	  		if (ENVIRONMENTENGINE_CLIENTACTIONINDEX != 0) {
				raapi.freeReference(ENVIRONMENTENGINE_CLIENTACTIONINDEX);
				ENVIRONMENTENGINE_CLIENTACTIONINDEX = 0;
			}
			if (EVENT != 0) {
				raapi.freeReference(EVENT);
				EVENT = 0;
			}
	  		if (EVENT_SUBMITTER != 0) {
				raapi.freeReference(EVENT_SUBMITTER);
				EVENT_SUBMITTER = 0;
			}
			if (COMMAND != 0) {
				raapi.freeReference(COMMAND);
				COMMAND = 0;
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
			if (ATTACHENGINECOMMAND != 0) {
				raapi.freeReference(ATTACHENGINECOMMAND);
				ATTACHENGINECOMMAND = 0;
			}
	  		if (ATTACHENGINECOMMAND_NAME != 0) {
				raapi.freeReference(ATTACHENGINECOMMAND_NAME);
				ATTACHENGINECOMMAND_NAME = 0;
			}
			if (LAUNCHTRANSFORMATIONINFOREGROUNDCOMMAND != 0) {
				raapi.freeReference(LAUNCHTRANSFORMATIONINFOREGROUNDCOMMAND);
				LAUNCHTRANSFORMATIONINFOREGROUNDCOMMAND = 0;
			}
	  		if (LAUNCHTRANSFORMATIONINFOREGROUNDCOMMAND_URI != 0) {
				raapi.freeReference(LAUNCHTRANSFORMATIONINFOREGROUNDCOMMAND_URI);
				LAUNCHTRANSFORMATIONINFOREGROUNDCOMMAND_URI = 0;
			}
	  		if (LAUNCHTRANSFORMATIONINFOREGROUNDCOMMAND_MESSAGE != 0) {
				raapi.freeReference(LAUNCHTRANSFORMATIONINFOREGROUNDCOMMAND_MESSAGE);
				LAUNCHTRANSFORMATIONINFOREGROUNDCOMMAND_MESSAGE = 0;
			}
	  		if (LAUNCHTRANSFORMATIONINFOREGROUNDCOMMAND_PROGRESS != 0) {
				raapi.freeReference(LAUNCHTRANSFORMATIONINFOREGROUNDCOMMAND_PROGRESS);
				LAUNCHTRANSFORMATIONINFOREGROUNDCOMMAND_PROGRESS = 0;
			}
			if (ACTIVATEFRAMECOMMAND != 0) {
				raapi.freeReference(ACTIVATEFRAMECOMMAND);
				ACTIVATEFRAMECOMMAND = 0;
			}
	  		if (ACTIVATEFRAMECOMMAND_FRAME != 0) {
				raapi.freeReference(ACTIVATEFRAMECOMMAND_FRAME);
				ACTIVATEFRAMECOMMAND_FRAME = 0;
			}
			if (LAUNCHGUITRANSFORMATIONCOMMAND != 0) {
				raapi.freeReference(LAUNCHGUITRANSFORMATIONCOMMAND);
				LAUNCHGUITRANSFORMATIONCOMMAND = 0;
			}
	  		if (LAUNCHGUITRANSFORMATIONCOMMAND_URI != 0) {
				raapi.freeReference(LAUNCHGUITRANSFORMATIONCOMMAND_URI);
				LAUNCHGUITRANSFORMATIONCOMMAND_URI = 0;
			}
			if (SHOWINFORMATIONBARCOMMAND != 0) {
				raapi.freeReference(SHOWINFORMATIONBARCOMMAND);
				SHOWINFORMATIONBARCOMMAND = 0;
			}
	  		if (SHOWINFORMATIONBARCOMMAND_MESSAGE != 0) {
				raapi.freeReference(SHOWINFORMATIONBARCOMMAND_MESSAGE);
				SHOWINFORMATIONBARCOMMAND_MESSAGE = 0;
			}
			if (UNDOIGNORINGCLASS != 0) {
				raapi.freeReference(UNDOIGNORINGCLASS);
				UNDOIGNORINGCLASS = 0;
			}
			if (ASYNCCOMMAND != 0) {
				raapi.freeReference(ASYNCCOMMAND);
				ASYNCCOMMAND = 0;
			}
			if (ENQUEUEFOREGROUNDTRANSFORMATIONCOMMAND != 0) {
				raapi.freeReference(ENQUEUEFOREGROUNDTRANSFORMATIONCOMMAND);
				ENQUEUEFOREGROUNDTRANSFORMATIONCOMMAND = 0;
			}
			if (POSTMESSAGETOFRAMECOMMAND != 0) {
				raapi.freeReference(POSTMESSAGETOFRAMECOMMAND);
				POSTMESSAGETOFRAMECOMMAND = 0;
			}
	  		if (POSTMESSAGETOFRAMECOMMAND_MESSAGEURI != 0) {
				raapi.freeReference(POSTMESSAGETOFRAMECOMMAND_MESSAGEURI);
				POSTMESSAGETOFRAMECOMMAND_MESSAGEURI = 0;
			}
	  		if (POSTMESSAGETOFRAMECOMMAND_FRAME != 0) {
				raapi.freeReference(POSTMESSAGETOFRAMECOMMAND_FRAME);
				POSTMESSAGETOFRAMECOMMAND_FRAME = 0;
			}
		}

		raapi = _raapi;

		if (raapi != null) {
			// initializing class references...
			ENGINE = raapi.findClass("TDAKernel::Engine");
			if ((ENGINE == 0) && (prefix != null))
				ENGINE = raapi.findClass(prefix+"TDAKernel::Engine");
			if ((ENGINE == 0) && insertMetamodel)
				ENGINE = raapi.createClass(prefix+"TDAKernel::Engine");
			if (ENGINE == 0) {				
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the class TDAKernel::Engine.");
			}
			ATTACHFRAMECOMMAND = raapi.findClass("AttachFrameCommand");
			if ((ATTACHFRAMECOMMAND == 0) && (prefix != null))
				ATTACHFRAMECOMMAND = raapi.findClass(prefix+"AttachFrameCommand");
			if ((ATTACHFRAMECOMMAND == 0) && insertMetamodel)
				ATTACHFRAMECOMMAND = raapi.createClass(prefix+"AttachFrameCommand");
			if (ATTACHFRAMECOMMAND == 0) {				
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the class AttachFrameCommand.");
			}
			FRAMEACTIVATEDEVENT = raapi.findClass("FrameActivatedEvent");
			if ((FRAMEACTIVATEDEVENT == 0) && (prefix != null))
				FRAMEACTIVATEDEVENT = raapi.findClass(prefix+"FrameActivatedEvent");
			if ((FRAMEACTIVATEDEVENT == 0) && insertMetamodel)
				FRAMEACTIVATEDEVENT = raapi.createClass(prefix+"FrameActivatedEvent");
			if (FRAMEACTIVATEDEVENT == 0) {				
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the class FrameActivatedEvent.");
			}
			FRAMEDEACTIVATINGEVENT = raapi.findClass("FrameDeactivatingEvent");
			if ((FRAMEDEACTIVATINGEVENT == 0) && (prefix != null))
				FRAMEDEACTIVATINGEVENT = raapi.findClass(prefix+"FrameDeactivatingEvent");
			if ((FRAMEDEACTIVATINGEVENT == 0) && insertMetamodel)
				FRAMEDEACTIVATINGEVENT = raapi.createClass(prefix+"FrameDeactivatingEvent");
			if (FRAMEDEACTIVATINGEVENT == 0) {				
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the class FrameDeactivatingEvent.");
			}
			CLOSEFRAMEREQUESTEDEVENT = raapi.findClass("CloseFrameRequestedEvent");
			if ((CLOSEFRAMEREQUESTEDEVENT == 0) && (prefix != null))
				CLOSEFRAMEREQUESTEDEVENT = raapi.findClass(prefix+"CloseFrameRequestedEvent");
			if ((CLOSEFRAMEREQUESTEDEVENT == 0) && insertMetamodel)
				CLOSEFRAMEREQUESTEDEVENT = raapi.createClass(prefix+"CloseFrameRequestedEvent");
			if (CLOSEFRAMEREQUESTEDEVENT == 0) {				
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the class CloseFrameRequestedEvent.");
			}
			DETACHFRAMECOMMAND = raapi.findClass("DetachFrameCommand");
			if ((DETACHFRAMECOMMAND == 0) && (prefix != null))
				DETACHFRAMECOMMAND = raapi.findClass(prefix+"DetachFrameCommand");
			if ((DETACHFRAMECOMMAND == 0) && insertMetamodel)
				DETACHFRAMECOMMAND = raapi.createClass(prefix+"DetachFrameCommand");
			if (DETACHFRAMECOMMAND == 0) {				
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the class DetachFrameCommand.");
			}
			REFRESHOPTIONSCOMMAND = raapi.findClass("RefreshOptionsCommand");
			if ((REFRESHOPTIONSCOMMAND == 0) && (prefix != null))
				REFRESHOPTIONSCOMMAND = raapi.findClass(prefix+"RefreshOptionsCommand");
			if ((REFRESHOPTIONSCOMMAND == 0) && insertMetamodel)
				REFRESHOPTIONSCOMMAND = raapi.createClass(prefix+"RefreshOptionsCommand");
			if (REFRESHOPTIONSCOMMAND == 0) {				
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the class RefreshOptionsCommand.");
			}
			OPTIONSELECTEDEVENT = raapi.findClass("OptionSelectedEvent");
			if ((OPTIONSELECTEDEVENT == 0) && (prefix != null))
				OPTIONSELECTEDEVENT = raapi.findClass(prefix+"OptionSelectedEvent");
			if ((OPTIONSELECTEDEVENT == 0) && insertMetamodel)
				OPTIONSELECTEDEVENT = raapi.createClass(prefix+"OptionSelectedEvent");
			if (OPTIONSELECTEDEVENT == 0) {				
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the class OptionSelectedEvent.");
			}
			PROJECTCREATEDEVENT = raapi.findClass("ProjectCreatedEvent");
			if ((PROJECTCREATEDEVENT == 0) && (prefix != null))
				PROJECTCREATEDEVENT = raapi.findClass(prefix+"ProjectCreatedEvent");
			if ((PROJECTCREATEDEVENT == 0) && insertMetamodel)
				PROJECTCREATEDEVENT = raapi.createClass(prefix+"ProjectCreatedEvent");
			if (PROJECTCREATEDEVENT == 0) {				
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the class ProjectCreatedEvent.");
			}
			PROJECTOPENEDEVENT = raapi.findClass("ProjectOpenedEvent");
			if ((PROJECTOPENEDEVENT == 0) && (prefix != null))
				PROJECTOPENEDEVENT = raapi.findClass(prefix+"ProjectOpenedEvent");
			if ((PROJECTOPENEDEVENT == 0) && insertMetamodel)
				PROJECTOPENEDEVENT = raapi.createClass(prefix+"ProjectOpenedEvent");
			if (PROJECTOPENEDEVENT == 0) {				
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the class ProjectOpenedEvent.");
			}
			CLOSEPROJECTCOMMAND = raapi.findClass("CloseProjectCommand");
			if ((CLOSEPROJECTCOMMAND == 0) && (prefix != null))
				CLOSEPROJECTCOMMAND = raapi.findClass(prefix+"CloseProjectCommand");
			if ((CLOSEPROJECTCOMMAND == 0) && insertMetamodel)
				CLOSEPROJECTCOMMAND = raapi.createClass(prefix+"CloseProjectCommand");
			if (CLOSEPROJECTCOMMAND == 0) {				
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the class CloseProjectCommand.");
			}
			PROJECTCLOSINGEVENT = raapi.findClass("ProjectClosingEvent");
			if ((PROJECTCLOSINGEVENT == 0) && (prefix != null))
				PROJECTCLOSINGEVENT = raapi.findClass(prefix+"ProjectClosingEvent");
			if ((PROJECTCLOSINGEVENT == 0) && insertMetamodel)
				PROJECTCLOSINGEVENT = raapi.createClass(prefix+"ProjectClosingEvent");
			if (PROJECTCLOSINGEVENT == 0) {				
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the class ProjectClosingEvent.");
			}
			FRAMERESIZEDEVENT = raapi.findClass("FrameResizedEvent");
			if ((FRAMERESIZEDEVENT == 0) && (prefix != null))
				FRAMERESIZEDEVENT = raapi.findClass(prefix+"FrameResizedEvent");
			if ((FRAMERESIZEDEVENT == 0) && insertMetamodel)
				FRAMERESIZEDEVENT = raapi.createClass(prefix+"FrameResizedEvent");
			if (FRAMERESIZEDEVENT == 0) {				
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the class FrameResizedEvent.");
			}
			OPTION = raapi.findClass("Option");
			if ((OPTION == 0) && (prefix != null))
				OPTION = raapi.findClass(prefix+"Option");
			if ((OPTION == 0) && insertMetamodel)
				OPTION = raapi.createClass(prefix+"Option");
			if (OPTION == 0) {				
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the class Option.");
			}
			FRAME = raapi.findClass("Frame");
			if ((FRAME == 0) && (prefix != null))
				FRAME = raapi.findClass(prefix+"Frame");
			if ((FRAME == 0) && insertMetamodel)
				FRAME = raapi.createClass(prefix+"Frame");
			if (FRAME == 0) {				
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the class Frame.");
			}
			ENVIRONMENTENGINE = raapi.findClass("EnvironmentEngine");
			if ((ENVIRONMENTENGINE == 0) && (prefix != null))
				ENVIRONMENTENGINE = raapi.findClass(prefix+"EnvironmentEngine");
			if ((ENVIRONMENTENGINE == 0) && insertMetamodel)
				ENVIRONMENTENGINE = raapi.createClass(prefix+"EnvironmentEngine");
			if (ENVIRONMENTENGINE == 0) {				
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the class EnvironmentEngine.");
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
			ATTACHENGINECOMMAND = raapi.findClass("TDAKernel::AttachEngineCommand");
			if ((ATTACHENGINECOMMAND == 0) && (prefix != null))
				ATTACHENGINECOMMAND = raapi.findClass(prefix+"TDAKernel::AttachEngineCommand");
			if ((ATTACHENGINECOMMAND == 0) && insertMetamodel)
				ATTACHENGINECOMMAND = raapi.createClass(prefix+"TDAKernel::AttachEngineCommand");
			if (ATTACHENGINECOMMAND == 0) {				
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the class TDAKernel::AttachEngineCommand.");
			}
			LAUNCHTRANSFORMATIONINFOREGROUNDCOMMAND = raapi.findClass("LaunchTransformationInForegroundCommand");
			if ((LAUNCHTRANSFORMATIONINFOREGROUNDCOMMAND == 0) && (prefix != null))
				LAUNCHTRANSFORMATIONINFOREGROUNDCOMMAND = raapi.findClass(prefix+"LaunchTransformationInForegroundCommand");
			if ((LAUNCHTRANSFORMATIONINFOREGROUNDCOMMAND == 0) && insertMetamodel)
				LAUNCHTRANSFORMATIONINFOREGROUNDCOMMAND = raapi.createClass(prefix+"LaunchTransformationInForegroundCommand");
			if (LAUNCHTRANSFORMATIONINFOREGROUNDCOMMAND == 0) {				
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the class LaunchTransformationInForegroundCommand.");
			}
			ACTIVATEFRAMECOMMAND = raapi.findClass("ActivateFrameCommand");
			if ((ACTIVATEFRAMECOMMAND == 0) && (prefix != null))
				ACTIVATEFRAMECOMMAND = raapi.findClass(prefix+"ActivateFrameCommand");
			if ((ACTIVATEFRAMECOMMAND == 0) && insertMetamodel)
				ACTIVATEFRAMECOMMAND = raapi.createClass(prefix+"ActivateFrameCommand");
			if (ACTIVATEFRAMECOMMAND == 0) {				
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the class ActivateFrameCommand.");
			}
			LAUNCHGUITRANSFORMATIONCOMMAND = raapi.findClass("LaunchGUITransformationCommand");
			if ((LAUNCHGUITRANSFORMATIONCOMMAND == 0) && (prefix != null))
				LAUNCHGUITRANSFORMATIONCOMMAND = raapi.findClass(prefix+"LaunchGUITransformationCommand");
			if ((LAUNCHGUITRANSFORMATIONCOMMAND == 0) && insertMetamodel)
				LAUNCHGUITRANSFORMATIONCOMMAND = raapi.createClass(prefix+"LaunchGUITransformationCommand");
			if (LAUNCHGUITRANSFORMATIONCOMMAND == 0) {				
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the class LaunchGUITransformationCommand.");
			}
			SHOWINFORMATIONBARCOMMAND = raapi.findClass("ShowInformationBarCommand");
			if ((SHOWINFORMATIONBARCOMMAND == 0) && (prefix != null))
				SHOWINFORMATIONBARCOMMAND = raapi.findClass(prefix+"ShowInformationBarCommand");
			if ((SHOWINFORMATIONBARCOMMAND == 0) && insertMetamodel)
				SHOWINFORMATIONBARCOMMAND = raapi.createClass(prefix+"ShowInformationBarCommand");
			if (SHOWINFORMATIONBARCOMMAND == 0) {				
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the class ShowInformationBarCommand.");
			}
			UNDOIGNORINGCLASS = raapi.findClass("TDAKernel::UndoIgnoringClass");
			if ((UNDOIGNORINGCLASS == 0) && (prefix != null))
				UNDOIGNORINGCLASS = raapi.findClass(prefix+"TDAKernel::UndoIgnoringClass");
			if ((UNDOIGNORINGCLASS == 0) && insertMetamodel)
				UNDOIGNORINGCLASS = raapi.createClass(prefix+"TDAKernel::UndoIgnoringClass");
			if (UNDOIGNORINGCLASS == 0) {				
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the class TDAKernel::UndoIgnoringClass.");
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
			ENQUEUEFOREGROUNDTRANSFORMATIONCOMMAND = raapi.findClass("EnqueueForegroundTransformationCommand");
			if ((ENQUEUEFOREGROUNDTRANSFORMATIONCOMMAND == 0) && (prefix != null))
				ENQUEUEFOREGROUNDTRANSFORMATIONCOMMAND = raapi.findClass(prefix+"EnqueueForegroundTransformationCommand");
			if ((ENQUEUEFOREGROUNDTRANSFORMATIONCOMMAND == 0) && insertMetamodel)
				ENQUEUEFOREGROUNDTRANSFORMATIONCOMMAND = raapi.createClass(prefix+"EnqueueForegroundTransformationCommand");
			if (ENQUEUEFOREGROUNDTRANSFORMATIONCOMMAND == 0) {				
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the class EnqueueForegroundTransformationCommand.");
			}
			POSTMESSAGETOFRAMECOMMAND = raapi.findClass("PostMessageToFrameCommand");
			if ((POSTMESSAGETOFRAMECOMMAND == 0) && (prefix != null))
				POSTMESSAGETOFRAMECOMMAND = raapi.findClass(prefix+"PostMessageToFrameCommand");
			if ((POSTMESSAGETOFRAMECOMMAND == 0) && insertMetamodel)
				POSTMESSAGETOFRAMECOMMAND = raapi.createClass(prefix+"PostMessageToFrameCommand");
			if (POSTMESSAGETOFRAMECOMMAND == 0) {				
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the class PostMessageToFrameCommand.");
			}

			// creating generalizations, if they do not exist...
			if (insertMetamodel) {
				if (!raapi.isDirectSubClass(ATTACHFRAMECOMMAND, ASYNCCOMMAND))
					if (!raapi.createGeneralization(ATTACHFRAMECOMMAND, ASYNCCOMMAND)) {
						setRAAPI(null, null, false); // freeing references initialized so far...
						throw new ElementReferenceException("Error creating a generalization between classes AttachFrameCommand and TDAKernel::AsyncCommand.");
					}
				if (!raapi.isDirectSubClass(FRAMEACTIVATEDEVENT, EVENT))
					if (!raapi.createGeneralization(FRAMEACTIVATEDEVENT, EVENT)) {
						setRAAPI(null, null, false); // freeing references initialized so far...
						throw new ElementReferenceException("Error creating a generalization between classes FrameActivatedEvent and TDAKernel::Event.");
					}
				if (!raapi.isDirectSubClass(FRAMEDEACTIVATINGEVENT, EVENT))
					if (!raapi.createGeneralization(FRAMEDEACTIVATINGEVENT, EVENT)) {
						setRAAPI(null, null, false); // freeing references initialized so far...
						throw new ElementReferenceException("Error creating a generalization between classes FrameDeactivatingEvent and TDAKernel::Event.");
					}
				if (!raapi.isDirectSubClass(CLOSEFRAMEREQUESTEDEVENT, EVENT))
					if (!raapi.createGeneralization(CLOSEFRAMEREQUESTEDEVENT, EVENT)) {
						setRAAPI(null, null, false); // freeing references initialized so far...
						throw new ElementReferenceException("Error creating a generalization between classes CloseFrameRequestedEvent and TDAKernel::Event.");
					}
				if (!raapi.isDirectSubClass(DETACHFRAMECOMMAND, ASYNCCOMMAND))
					if (!raapi.createGeneralization(DETACHFRAMECOMMAND, ASYNCCOMMAND)) {
						setRAAPI(null, null, false); // freeing references initialized so far...
						throw new ElementReferenceException("Error creating a generalization between classes DetachFrameCommand and TDAKernel::AsyncCommand.");
					}
				if (!raapi.isDirectSubClass(REFRESHOPTIONSCOMMAND, COMMAND))
					if (!raapi.createGeneralization(REFRESHOPTIONSCOMMAND, COMMAND)) {
						setRAAPI(null, null, false); // freeing references initialized so far...
						throw new ElementReferenceException("Error creating a generalization between classes RefreshOptionsCommand and TDAKernel::Command.");
					}
				if (!raapi.isDirectSubClass(OPTIONSELECTEDEVENT, EVENT))
					if (!raapi.createGeneralization(OPTIONSELECTEDEVENT, EVENT)) {
						setRAAPI(null, null, false); // freeing references initialized so far...
						throw new ElementReferenceException("Error creating a generalization between classes OptionSelectedEvent and TDAKernel::Event.");
					}
				if (!raapi.isDirectSubClass(PROJECTCREATEDEVENT, EVENT))
					if (!raapi.createGeneralization(PROJECTCREATEDEVENT, EVENT)) {
						setRAAPI(null, null, false); // freeing references initialized so far...
						throw new ElementReferenceException("Error creating a generalization between classes ProjectCreatedEvent and TDAKernel::Event.");
					}
				if (!raapi.isDirectSubClass(PROJECTOPENEDEVENT, EVENT))
					if (!raapi.createGeneralization(PROJECTOPENEDEVENT, EVENT)) {
						setRAAPI(null, null, false); // freeing references initialized so far...
						throw new ElementReferenceException("Error creating a generalization between classes ProjectOpenedEvent and TDAKernel::Event.");
					}
				if (!raapi.isDirectSubClass(CLOSEPROJECTCOMMAND, COMMAND))
					if (!raapi.createGeneralization(CLOSEPROJECTCOMMAND, COMMAND)) {
						setRAAPI(null, null, false); // freeing references initialized so far...
						throw new ElementReferenceException("Error creating a generalization between classes CloseProjectCommand and TDAKernel::Command.");
					}
				if (!raapi.isDirectSubClass(PROJECTCLOSINGEVENT, EVENT))
					if (!raapi.createGeneralization(PROJECTCLOSINGEVENT, EVENT)) {
						setRAAPI(null, null, false); // freeing references initialized so far...
						throw new ElementReferenceException("Error creating a generalization between classes ProjectClosingEvent and TDAKernel::Event.");
					}
				if (!raapi.isDirectSubClass(FRAMERESIZEDEVENT, EVENT))
					if (!raapi.createGeneralization(FRAMERESIZEDEVENT, EVENT)) {
						setRAAPI(null, null, false); // freeing references initialized so far...
						throw new ElementReferenceException("Error creating a generalization between classes FrameResizedEvent and TDAKernel::Event.");
					}
				if (!raapi.isDirectSubClass(OPTION, UNDOIGNORINGCLASS))
					if (!raapi.createGeneralization(OPTION, UNDOIGNORINGCLASS)) {
						setRAAPI(null, null, false); // freeing references initialized so far...
						throw new ElementReferenceException("Error creating a generalization between classes Option and TDAKernel::UndoIgnoringClass.");
					}
				if (!raapi.isDirectSubClass(FRAME, UNDOIGNORINGCLASS))
					if (!raapi.createGeneralization(FRAME, UNDOIGNORINGCLASS)) {
						setRAAPI(null, null, false); // freeing references initialized so far...
						throw new ElementReferenceException("Error creating a generalization between classes Frame and TDAKernel::UndoIgnoringClass.");
					}
				if (!raapi.isDirectSubClass(ENVIRONMENTENGINE, ENGINE))
					if (!raapi.createGeneralization(ENVIRONMENTENGINE, ENGINE)) {
						setRAAPI(null, null, false); // freeing references initialized so far...
						throw new ElementReferenceException("Error creating a generalization between classes EnvironmentEngine and TDAKernel::Engine.");
					}
				if (!raapi.isDirectSubClass(ATTACHENGINECOMMAND, COMMAND))
					if (!raapi.createGeneralization(ATTACHENGINECOMMAND, COMMAND)) {
						setRAAPI(null, null, false); // freeing references initialized so far...
						throw new ElementReferenceException("Error creating a generalization between classes TDAKernel::AttachEngineCommand and TDAKernel::Command.");
					}
				if (!raapi.isDirectSubClass(LAUNCHTRANSFORMATIONINFOREGROUNDCOMMAND, ASYNCCOMMAND))
					if (!raapi.createGeneralization(LAUNCHTRANSFORMATIONINFOREGROUNDCOMMAND, ASYNCCOMMAND)) {
						setRAAPI(null, null, false); // freeing references initialized so far...
						throw new ElementReferenceException("Error creating a generalization between classes LaunchTransformationInForegroundCommand and TDAKernel::AsyncCommand.");
					}
				if (!raapi.isDirectSubClass(ACTIVATEFRAMECOMMAND, ASYNCCOMMAND))
					if (!raapi.createGeneralization(ACTIVATEFRAMECOMMAND, ASYNCCOMMAND)) {
						setRAAPI(null, null, false); // freeing references initialized so far...
						throw new ElementReferenceException("Error creating a generalization between classes ActivateFrameCommand and TDAKernel::AsyncCommand.");
					}
				if (!raapi.isDirectSubClass(LAUNCHGUITRANSFORMATIONCOMMAND, COMMAND))
					if (!raapi.createGeneralization(LAUNCHGUITRANSFORMATIONCOMMAND, COMMAND)) {
						setRAAPI(null, null, false); // freeing references initialized so far...
						throw new ElementReferenceException("Error creating a generalization between classes LaunchGUITransformationCommand and TDAKernel::Command.");
					}
				if (!raapi.isDirectSubClass(SHOWINFORMATIONBARCOMMAND, COMMAND))
					if (!raapi.createGeneralization(SHOWINFORMATIONBARCOMMAND, COMMAND)) {
						setRAAPI(null, null, false); // freeing references initialized so far...
						throw new ElementReferenceException("Error creating a generalization between classes ShowInformationBarCommand and TDAKernel::Command.");
					}
				if (!raapi.isDirectSubClass(ASYNCCOMMAND, COMMAND))
					if (!raapi.createGeneralization(ASYNCCOMMAND, COMMAND)) {
						setRAAPI(null, null, false); // freeing references initialized so far...
						throw new ElementReferenceException("Error creating a generalization between classes TDAKernel::AsyncCommand and TDAKernel::Command.");
					}
				if (!raapi.isDirectSubClass(ENQUEUEFOREGROUNDTRANSFORMATIONCOMMAND, LAUNCHTRANSFORMATIONINFOREGROUNDCOMMAND))
					if (!raapi.createGeneralization(ENQUEUEFOREGROUNDTRANSFORMATIONCOMMAND, LAUNCHTRANSFORMATIONINFOREGROUNDCOMMAND)) {
						setRAAPI(null, null, false); // freeing references initialized so far...
						throw new ElementReferenceException("Error creating a generalization between classes EnqueueForegroundTransformationCommand and LaunchTransformationInForegroundCommand.");
					}
				if (!raapi.isDirectSubClass(POSTMESSAGETOFRAMECOMMAND, ASYNCCOMMAND))
					if (!raapi.createGeneralization(POSTMESSAGETOFRAMECOMMAND, ASYNCCOMMAND)) {
						setRAAPI(null, null, false); // freeing references initialized so far...
						throw new ElementReferenceException("Error creating a generalization between classes PostMessageToFrameCommand and TDAKernel::AsyncCommand.");
					}
			}

			// initializing references for attributes and associations...
			ATTACHFRAMECOMMAND_FRAME = raapi.findAssociationEnd(ATTACHFRAMECOMMAND, "frame");
			if ((ATTACHFRAMECOMMAND_FRAME == 0) && insertMetamodel) {
				ATTACHFRAMECOMMAND_FRAME = raapi.createAssociation(ATTACHFRAMECOMMAND, FRAME, "attachFrameCommand", "frame", false);
			}
			if (ATTACHFRAMECOMMAND_FRAME == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end frame of the class AttachFrameCommand.");
			}
			FRAMEACTIVATEDEVENT_FRAME = raapi.findAssociationEnd(FRAMEACTIVATEDEVENT, "frame");
			if ((FRAMEACTIVATEDEVENT_FRAME == 0) && insertMetamodel) {
				FRAMEACTIVATEDEVENT_FRAME = raapi.createAssociation(FRAMEACTIVATEDEVENT, FRAME, "frameActivatedEvent", "frame", false);
			}
			if (FRAMEACTIVATEDEVENT_FRAME == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end frame of the class FrameActivatedEvent.");
			}
			FRAMEDEACTIVATINGEVENT_FRAME = raapi.findAssociationEnd(FRAMEDEACTIVATINGEVENT, "frame");
			if ((FRAMEDEACTIVATINGEVENT_FRAME == 0) && insertMetamodel) {
				FRAMEDEACTIVATINGEVENT_FRAME = raapi.createAssociation(FRAMEDEACTIVATINGEVENT, FRAME, "frameDeactivatingEvent", "frame", false);
			}
			if (FRAMEDEACTIVATINGEVENT_FRAME == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end frame of the class FrameDeactivatingEvent.");
			}
			CLOSEFRAMEREQUESTEDEVENT_FRAME = raapi.findAssociationEnd(CLOSEFRAMEREQUESTEDEVENT, "frame");
			if ((CLOSEFRAMEREQUESTEDEVENT_FRAME == 0) && insertMetamodel) {
				CLOSEFRAMEREQUESTEDEVENT_FRAME = raapi.createAssociation(CLOSEFRAMEREQUESTEDEVENT, FRAME, "closeFrameRequestedEvent", "frame", false);
			}
			if (CLOSEFRAMEREQUESTEDEVENT_FRAME == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end frame of the class CloseFrameRequestedEvent.");
			}
			CLOSEFRAMEREQUESTEDEVENT_FORCE = raapi.findAttribute(CLOSEFRAMEREQUESTEDEVENT, "force");
			if ((CLOSEFRAMEREQUESTEDEVENT_FORCE == 0) && insertMetamodel)
				CLOSEFRAMEREQUESTEDEVENT_FORCE = raapi.createAttribute(CLOSEFRAMEREQUESTEDEVENT, "force", raapi.findPrimitiveDataType("Boolean"));
			if (CLOSEFRAMEREQUESTEDEVENT_FORCE == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute force of the class CloseFrameRequestedEvent.");
			}
			DETACHFRAMECOMMAND_FRAME = raapi.findAssociationEnd(DETACHFRAMECOMMAND, "frame");
			if ((DETACHFRAMECOMMAND_FRAME == 0) && insertMetamodel) {
				DETACHFRAMECOMMAND_FRAME = raapi.createAssociation(DETACHFRAMECOMMAND, FRAME, "detachFrameCommand", "frame", false);
			}
			if (DETACHFRAMECOMMAND_FRAME == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end frame of the class DetachFrameCommand.");
			}
			DETACHFRAMECOMMAND_PERMANENTLY = raapi.findAttribute(DETACHFRAMECOMMAND, "permanently");
			if ((DETACHFRAMECOMMAND_PERMANENTLY == 0) && insertMetamodel)
				DETACHFRAMECOMMAND_PERMANENTLY = raapi.createAttribute(DETACHFRAMECOMMAND, "permanently", raapi.findPrimitiveDataType("Boolean"));
			if (DETACHFRAMECOMMAND_PERMANENTLY == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute permanently of the class DetachFrameCommand.");
			}
			REFRESHOPTIONSCOMMAND_ENVIRONMENTENGINE = raapi.findAssociationEnd(REFRESHOPTIONSCOMMAND, "environmentEngine");
			if ((REFRESHOPTIONSCOMMAND_ENVIRONMENTENGINE == 0) && insertMetamodel) {
				REFRESHOPTIONSCOMMAND_ENVIRONMENTENGINE = raapi.createAssociation(REFRESHOPTIONSCOMMAND, ENVIRONMENTENGINE, "refreshOptionsCommand", "environmentEngine", false);
			}
			if (REFRESHOPTIONSCOMMAND_ENVIRONMENTENGINE == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end environmentEngine of the class RefreshOptionsCommand.");
			}
			OPTIONSELECTEDEVENT_OPTION = raapi.findAssociationEnd(OPTIONSELECTEDEVENT, "option");
			if ((OPTIONSELECTEDEVENT_OPTION == 0) && insertMetamodel) {
				OPTIONSELECTEDEVENT_OPTION = raapi.createAssociation(OPTIONSELECTEDEVENT, OPTION, "optionSelectedEvent", "option", false);
			}
			if (OPTIONSELECTEDEVENT_OPTION == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end option of the class OptionSelectedEvent.");
			}
			CLOSEPROJECTCOMMAND_SILENT = raapi.findAttribute(CLOSEPROJECTCOMMAND, "silent");
			if ((CLOSEPROJECTCOMMAND_SILENT == 0) && insertMetamodel)
				CLOSEPROJECTCOMMAND_SILENT = raapi.createAttribute(CLOSEPROJECTCOMMAND, "silent", raapi.findPrimitiveDataType("Boolean"));
			if (CLOSEPROJECTCOMMAND_SILENT == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute silent of the class CloseProjectCommand.");
			}
			FRAMERESIZEDEVENT_FRAME = raapi.findAssociationEnd(FRAMERESIZEDEVENT, "frame");
			if ((FRAMERESIZEDEVENT_FRAME == 0) && insertMetamodel) {
				FRAMERESIZEDEVENT_FRAME = raapi.createAssociation(FRAMERESIZEDEVENT, FRAME, "frameResizedEvent", "frame", false);
			}
			if (FRAMERESIZEDEVENT_FRAME == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end frame of the class FrameResizedEvent.");
			}
			FRAMERESIZEDEVENT_WIDTH = raapi.findAttribute(FRAMERESIZEDEVENT, "width");
			if ((FRAMERESIZEDEVENT_WIDTH == 0) && insertMetamodel)
				FRAMERESIZEDEVENT_WIDTH = raapi.createAttribute(FRAMERESIZEDEVENT, "width", raapi.findPrimitiveDataType("Integer"));
			if (FRAMERESIZEDEVENT_WIDTH == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute width of the class FrameResizedEvent.");
			}
			FRAMERESIZEDEVENT_HEIGHT = raapi.findAttribute(FRAMERESIZEDEVENT, "height");
			if ((FRAMERESIZEDEVENT_HEIGHT == 0) && insertMetamodel)
				FRAMERESIZEDEVENT_HEIGHT = raapi.createAttribute(FRAMERESIZEDEVENT, "height", raapi.findPrimitiveDataType("Integer"));
			if (FRAMERESIZEDEVENT_HEIGHT == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute height of the class FrameResizedEvent.");
			}
			OPTION_CAPTION = raapi.findAttribute(OPTION, "caption");
			if ((OPTION_CAPTION == 0) && insertMetamodel)
				OPTION_CAPTION = raapi.createAttribute(OPTION, "caption", raapi.findPrimitiveDataType("String"));
			if (OPTION_CAPTION == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute caption of the class Option.");
			}
			OPTION_IMAGE = raapi.findAttribute(OPTION, "image");
			if ((OPTION_IMAGE == 0) && insertMetamodel)
				OPTION_IMAGE = raapi.createAttribute(OPTION, "image", raapi.findPrimitiveDataType("String"));
			if (OPTION_IMAGE == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute image of the class Option.");
			}
			OPTION_ID = raapi.findAttribute(OPTION, "id");
			if ((OPTION_ID == 0) && insertMetamodel)
				OPTION_ID = raapi.createAttribute(OPTION, "id", raapi.findPrimitiveDataType("String"));
			if (OPTION_ID == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute id of the class Option.");
			}
			OPTION_LOCATION = raapi.findAttribute(OPTION, "location");
			if ((OPTION_LOCATION == 0) && insertMetamodel)
				OPTION_LOCATION = raapi.createAttribute(OPTION, "location", raapi.findPrimitiveDataType("String"));
			if (OPTION_LOCATION == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute location of the class Option.");
			}
			OPTION_ONOPTIONSELECTEDEVENT = raapi.findAttribute(OPTION, "onOptionSelectedEvent");
			if ((OPTION_ONOPTIONSELECTEDEVENT == 0) && insertMetamodel)
				OPTION_ONOPTIONSELECTEDEVENT = raapi.createAttribute(OPTION, "onOptionSelectedEvent", raapi.findPrimitiveDataType("String"));
			if (OPTION_ONOPTIONSELECTEDEVENT == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute onOptionSelectedEvent of the class Option.");
			}
			OPTION_ENVIRONMENTENGINE = raapi.findAssociationEnd(OPTION, "environmentEngine");
			if ((OPTION_ENVIRONMENTENGINE == 0) && insertMetamodel) {
				OPTION_ENVIRONMENTENGINE = raapi.createAssociation(OPTION, ENVIRONMENTENGINE, "option", "environmentEngine", false);
			}
			if (OPTION_ENVIRONMENTENGINE == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end environmentEngine of the class Option.");
			}
			OPTION_OPTIONSELECTEDEVENT = raapi.findAssociationEnd(OPTION, "optionSelectedEvent");
			if ((OPTION_OPTIONSELECTEDEVENT == 0) && insertMetamodel) {
				OPTION_OPTIONSELECTEDEVENT = raapi.createAssociation(OPTION, OPTIONSELECTEDEVENT, "option", "optionSelectedEvent", false);
			}
			if (OPTION_OPTIONSELECTEDEVENT == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end optionSelectedEvent of the class Option.");
			}
			OPTION_PARENT = raapi.findAssociationEnd(OPTION, "parent");
			if ((OPTION_PARENT == 0) && insertMetamodel) {
				OPTION_PARENT = raapi.createAssociation(OPTION, OPTION, "child", "parent", false);
			}
			if (OPTION_PARENT == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end parent of the class Option.");
			}
			OPTION_CHILD = raapi.findAssociationEnd(OPTION, "child");
			if ((OPTION_CHILD == 0) && insertMetamodel) {
				OPTION_CHILD = raapi.createAssociation(OPTION, OPTION, "parent", "child", true);
			}
			if (OPTION_CHILD == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end child of the class Option.");
			}
			OPTION_FRAME = raapi.findAssociationEnd(OPTION, "frame");
			if ((OPTION_FRAME == 0) && insertMetamodel) {
				OPTION_FRAME = raapi.createAssociation(OPTION, FRAME, "option", "frame", false);
			}
			if (OPTION_FRAME == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end frame of the class Option.");
			}
			OPTION_ISENABLED = raapi.findAttribute(OPTION, "isEnabled");
			if ((OPTION_ISENABLED == 0) && insertMetamodel)
				OPTION_ISENABLED = raapi.createAttribute(OPTION, "isEnabled", raapi.findPrimitiveDataType("Boolean"));
			if (OPTION_ISENABLED == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute isEnabled of the class Option.");
			}
			FRAME_CAPTION = raapi.findAttribute(FRAME, "caption");
			if ((FRAME_CAPTION == 0) && insertMetamodel)
				FRAME_CAPTION = raapi.createAttribute(FRAME, "caption", raapi.findPrimitiveDataType("String"));
			if (FRAME_CAPTION == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute caption of the class Frame.");
			}
			FRAME_CONTENTURI = raapi.findAttribute(FRAME, "contentURI");
			if ((FRAME_CONTENTURI == 0) && insertMetamodel)
				FRAME_CONTENTURI = raapi.createAttribute(FRAME, "contentURI", raapi.findPrimitiveDataType("String"));
			if (FRAME_CONTENTURI == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute contentURI of the class Frame.");
			}
			FRAME_LOCATION = raapi.findAttribute(FRAME, "location");
			if ((FRAME_LOCATION == 0) && insertMetamodel)
				FRAME_LOCATION = raapi.createAttribute(FRAME, "location", raapi.findPrimitiveDataType("String"));
			if (FRAME_LOCATION == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute location of the class Frame.");
			}
			FRAME_ISRESIZEABLE = raapi.findAttribute(FRAME, "isResizeable");
			if ((FRAME_ISRESIZEABLE == 0) && insertMetamodel)
				FRAME_ISRESIZEABLE = raapi.createAttribute(FRAME, "isResizeable", raapi.findPrimitiveDataType("Boolean"));
			if (FRAME_ISRESIZEABLE == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute isResizeable of the class Frame.");
			}
			FRAME_ISCLOSABLE = raapi.findAttribute(FRAME, "isClosable");
			if ((FRAME_ISCLOSABLE == 0) && insertMetamodel)
				FRAME_ISCLOSABLE = raapi.createAttribute(FRAME, "isClosable", raapi.findPrimitiveDataType("Boolean"));
			if (FRAME_ISCLOSABLE == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute isClosable of the class Frame.");
			}
			FRAME_ONFRAMEACTIVATEDEVENT = raapi.findAttribute(FRAME, "onFrameActivatedEvent");
			if ((FRAME_ONFRAMEACTIVATEDEVENT == 0) && insertMetamodel)
				FRAME_ONFRAMEACTIVATEDEVENT = raapi.createAttribute(FRAME, "onFrameActivatedEvent", raapi.findPrimitiveDataType("String"));
			if (FRAME_ONFRAMEACTIVATEDEVENT == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute onFrameActivatedEvent of the class Frame.");
			}
			FRAME_ONFRAMEDEACTIVATINGEVENT = raapi.findAttribute(FRAME, "onFrameDeactivatingEvent");
			if ((FRAME_ONFRAMEDEACTIVATINGEVENT == 0) && insertMetamodel)
				FRAME_ONFRAMEDEACTIVATINGEVENT = raapi.createAttribute(FRAME, "onFrameDeactivatingEvent", raapi.findPrimitiveDataType("String"));
			if (FRAME_ONFRAMEDEACTIVATINGEVENT == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute onFrameDeactivatingEvent of the class Frame.");
			}
			FRAME_ONFRAMERESIZEDEVENT = raapi.findAttribute(FRAME, "onFrameResizedEvent");
			if ((FRAME_ONFRAMERESIZEDEVENT == 0) && insertMetamodel)
				FRAME_ONFRAMERESIZEDEVENT = raapi.createAttribute(FRAME, "onFrameResizedEvent", raapi.findPrimitiveDataType("String"));
			if (FRAME_ONFRAMERESIZEDEVENT == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute onFrameResizedEvent of the class Frame.");
			}
			FRAME_ONCLOSEFRAMEREQUESTEDEVENT = raapi.findAttribute(FRAME, "onCloseFrameRequestedEvent");
			if ((FRAME_ONCLOSEFRAMEREQUESTEDEVENT == 0) && insertMetamodel)
				FRAME_ONCLOSEFRAMEREQUESTEDEVENT = raapi.createAttribute(FRAME, "onCloseFrameRequestedEvent", raapi.findPrimitiveDataType("String"));
			if (FRAME_ONCLOSEFRAMEREQUESTEDEVENT == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute onCloseFrameRequestedEvent of the class Frame.");
			}
			FRAME_ENVIRONMENTENGINE = raapi.findAssociationEnd(FRAME, "environmentEngine");
			if ((FRAME_ENVIRONMENTENGINE == 0) && insertMetamodel) {
				FRAME_ENVIRONMENTENGINE = raapi.createAssociation(FRAME, ENVIRONMENTENGINE, "frame", "environmentEngine", false);
			}
			if (FRAME_ENVIRONMENTENGINE == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end environmentEngine of the class Frame.");
			}
			FRAME_ATTACHFRAMECOMMAND = raapi.findAssociationEnd(FRAME, "attachFrameCommand");
			if ((FRAME_ATTACHFRAMECOMMAND == 0) && insertMetamodel) {
				FRAME_ATTACHFRAMECOMMAND = raapi.createAssociation(FRAME, ATTACHFRAMECOMMAND, "frame", "attachFrameCommand", false);
			}
			if (FRAME_ATTACHFRAMECOMMAND == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end attachFrameCommand of the class Frame.");
			}
			FRAME_DETACHFRAMECOMMAND = raapi.findAssociationEnd(FRAME, "detachFrameCommand");
			if ((FRAME_DETACHFRAMECOMMAND == 0) && insertMetamodel) {
				FRAME_DETACHFRAMECOMMAND = raapi.createAssociation(FRAME, DETACHFRAMECOMMAND, "frame", "detachFrameCommand", false);
			}
			if (FRAME_DETACHFRAMECOMMAND == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end detachFrameCommand of the class Frame.");
			}
			FRAME_FRAMEACTIVATEDEVENT = raapi.findAssociationEnd(FRAME, "frameActivatedEvent");
			if ((FRAME_FRAMEACTIVATEDEVENT == 0) && insertMetamodel) {
				FRAME_FRAMEACTIVATEDEVENT = raapi.createAssociation(FRAME, FRAMEACTIVATEDEVENT, "frame", "frameActivatedEvent", false);
			}
			if (FRAME_FRAMEACTIVATEDEVENT == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end frameActivatedEvent of the class Frame.");
			}
			FRAME_FRAMEDEACTIVATINGEVENT = raapi.findAssociationEnd(FRAME, "frameDeactivatingEvent");
			if ((FRAME_FRAMEDEACTIVATINGEVENT == 0) && insertMetamodel) {
				FRAME_FRAMEDEACTIVATINGEVENT = raapi.createAssociation(FRAME, FRAMEDEACTIVATINGEVENT, "frame", "frameDeactivatingEvent", false);
			}
			if (FRAME_FRAMEDEACTIVATINGEVENT == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end frameDeactivatingEvent of the class Frame.");
			}
			FRAME_CLOSEFRAMEREQUESTEDEVENT = raapi.findAssociationEnd(FRAME, "closeFrameRequestedEvent");
			if ((FRAME_CLOSEFRAMEREQUESTEDEVENT == 0) && insertMetamodel) {
				FRAME_CLOSEFRAMEREQUESTEDEVENT = raapi.createAssociation(FRAME, CLOSEFRAMEREQUESTEDEVENT, "frame", "closeFrameRequestedEvent", false);
			}
			if (FRAME_CLOSEFRAMEREQUESTEDEVENT == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end closeFrameRequestedEvent of the class Frame.");
			}
			FRAME_FRAMERESIZEDEVENT = raapi.findAssociationEnd(FRAME, "frameResizedEvent");
			if ((FRAME_FRAMERESIZEDEVENT == 0) && insertMetamodel) {
				FRAME_FRAMERESIZEDEVENT = raapi.createAssociation(FRAME, FRAMERESIZEDEVENT, "frame", "frameResizedEvent", false);
			}
			if (FRAME_FRAMERESIZEDEVENT == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end frameResizedEvent of the class Frame.");
			}
			FRAME_OPTION = raapi.findAssociationEnd(FRAME, "option");
			if ((FRAME_OPTION == 0) && insertMetamodel) {
				FRAME_OPTION = raapi.createAssociation(FRAME, OPTION, "frame", "option", false);
			}
			if (FRAME_OPTION == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end option of the class Frame.");
			}
			FRAME_POSTMESSAGETOFRAMECOMMAND = raapi.findAssociationEnd(FRAME, "postMessageToFrameCommand");
			if ((FRAME_POSTMESSAGETOFRAMECOMMAND == 0) && insertMetamodel) {
				FRAME_POSTMESSAGETOFRAMECOMMAND = raapi.createAssociation(FRAME, POSTMESSAGETOFRAMECOMMAND, "frame", "postMessageToFrameCommand", false);
			}
			if (FRAME_POSTMESSAGETOFRAMECOMMAND == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end postMessageToFrameCommand of the class Frame.");
			}
			FRAME_ACTIVATEFRAMECOMMAND = raapi.findAssociationEnd(FRAME, "activateFrameCommand");
			if ((FRAME_ACTIVATEFRAMECOMMAND == 0) && insertMetamodel) {
				FRAME_ACTIVATEFRAMECOMMAND = raapi.createAssociation(FRAME, ACTIVATEFRAMECOMMAND, "frame", "activateFrameCommand", false);
			}
			if (FRAME_ACTIVATEFRAMECOMMAND == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end activateFrameCommand of the class Frame.");
			}
			ENVIRONMENTENGINE_LANGUAGE = raapi.findAttribute(ENVIRONMENTENGINE, "language");
			if ((ENVIRONMENTENGINE_LANGUAGE == 0) && insertMetamodel)
				ENVIRONMENTENGINE_LANGUAGE = raapi.createAttribute(ENVIRONMENTENGINE, "language", raapi.findPrimitiveDataType("String"));
			if (ENVIRONMENTENGINE_LANGUAGE == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute language of the class EnvironmentEngine.");
			}
			ENVIRONMENTENGINE_COUNTRY = raapi.findAttribute(ENVIRONMENTENGINE, "country");
			if ((ENVIRONMENTENGINE_COUNTRY == 0) && insertMetamodel)
				ENVIRONMENTENGINE_COUNTRY = raapi.createAttribute(ENVIRONMENTENGINE, "country", raapi.findPrimitiveDataType("String"));
			if (ENVIRONMENTENGINE_COUNTRY == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute country of the class EnvironmentEngine.");
			}
			ENVIRONMENTENGINE_ANYUNSAVEDCHANGES = raapi.findAttribute(ENVIRONMENTENGINE, "anyUnsavedChanges");
			if ((ENVIRONMENTENGINE_ANYUNSAVEDCHANGES == 0) && insertMetamodel)
				ENVIRONMENTENGINE_ANYUNSAVEDCHANGES = raapi.createAttribute(ENVIRONMENTENGINE, "anyUnsavedChanges", raapi.findPrimitiveDataType("Boolean"));
			if (ENVIRONMENTENGINE_ANYUNSAVEDCHANGES == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute anyUnsavedChanges of the class EnvironmentEngine.");
			}
			ENVIRONMENTENGINE_COMMONBINDIRECTORY = raapi.findAttribute(ENVIRONMENTENGINE, "commonBinDirectory");
			if ((ENVIRONMENTENGINE_COMMONBINDIRECTORY == 0) && insertMetamodel)
				ENVIRONMENTENGINE_COMMONBINDIRECTORY = raapi.createAttribute(ENVIRONMENTENGINE, "commonBinDirectory", raapi.findPrimitiveDataType("String"));
			if (ENVIRONMENTENGINE_COMMONBINDIRECTORY == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute commonBinDirectory of the class EnvironmentEngine.");
			}
			ENVIRONMENTENGINE_SPECIFICBINDIRECTORY = raapi.findAttribute(ENVIRONMENTENGINE, "specificBinDirectory");
			if ((ENVIRONMENTENGINE_SPECIFICBINDIRECTORY == 0) && insertMetamodel)
				ENVIRONMENTENGINE_SPECIFICBINDIRECTORY = raapi.createAttribute(ENVIRONMENTENGINE, "specificBinDirectory", raapi.findPrimitiveDataType("String"));
			if (ENVIRONMENTENGINE_SPECIFICBINDIRECTORY == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute specificBinDirectory of the class EnvironmentEngine.");
			}
			ENVIRONMENTENGINE_PROJECTDIRECTORY = raapi.findAttribute(ENVIRONMENTENGINE, "projectDirectory");
			if ((ENVIRONMENTENGINE_PROJECTDIRECTORY == 0) && insertMetamodel)
				ENVIRONMENTENGINE_PROJECTDIRECTORY = raapi.createAttribute(ENVIRONMENTENGINE, "projectDirectory", raapi.findPrimitiveDataType("String"));
			if (ENVIRONMENTENGINE_PROJECTDIRECTORY == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute projectDirectory of the class EnvironmentEngine.");
			}
			ENVIRONMENTENGINE_ONPROJECTOPENEDEVENT = raapi.findAttribute(ENVIRONMENTENGINE, "onProjectOpenedEvent");
			if ((ENVIRONMENTENGINE_ONPROJECTOPENEDEVENT == 0) && insertMetamodel)
				ENVIRONMENTENGINE_ONPROJECTOPENEDEVENT = raapi.createAttribute(ENVIRONMENTENGINE, "onProjectOpenedEvent", raapi.findPrimitiveDataType("String"));
			if (ENVIRONMENTENGINE_ONPROJECTOPENEDEVENT == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute onProjectOpenedEvent of the class EnvironmentEngine.");
			}
			ENVIRONMENTENGINE_ONPROJECTCLOSINGEVENT = raapi.findAttribute(ENVIRONMENTENGINE, "onProjectClosingEvent");
			if ((ENVIRONMENTENGINE_ONPROJECTCLOSINGEVENT == 0) && insertMetamodel)
				ENVIRONMENTENGINE_ONPROJECTCLOSINGEVENT = raapi.createAttribute(ENVIRONMENTENGINE, "onProjectClosingEvent", raapi.findPrimitiveDataType("String"));
			if (ENVIRONMENTENGINE_ONPROJECTCLOSINGEVENT == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute onProjectClosingEvent of the class EnvironmentEngine.");
			}
			ENVIRONMENTENGINE_OPTION = raapi.findAssociationEnd(ENVIRONMENTENGINE, "option");
			if ((ENVIRONMENTENGINE_OPTION == 0) && insertMetamodel) {
				ENVIRONMENTENGINE_OPTION = raapi.createAssociation(ENVIRONMENTENGINE, OPTION, "environmentEngine", "option", true);
			}
			if (ENVIRONMENTENGINE_OPTION == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end option of the class EnvironmentEngine.");
			}
			ENVIRONMENTENGINE_FRAME = raapi.findAssociationEnd(ENVIRONMENTENGINE, "frame");
			if ((ENVIRONMENTENGINE_FRAME == 0) && insertMetamodel) {
				ENVIRONMENTENGINE_FRAME = raapi.createAssociation(ENVIRONMENTENGINE, FRAME, "environmentEngine", "frame", true);
			}
			if (ENVIRONMENTENGINE_FRAME == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end frame of the class EnvironmentEngine.");
			}
			ENVIRONMENTENGINE_REFRESHOPTIONSCOMMAND = raapi.findAssociationEnd(ENVIRONMENTENGINE, "refreshOptionsCommand");
			if ((ENVIRONMENTENGINE_REFRESHOPTIONSCOMMAND == 0) && insertMetamodel) {
				ENVIRONMENTENGINE_REFRESHOPTIONSCOMMAND = raapi.createAssociation(ENVIRONMENTENGINE, REFRESHOPTIONSCOMMAND, "environmentEngine", "refreshOptionsCommand", false);
			}
			if (ENVIRONMENTENGINE_REFRESHOPTIONSCOMMAND == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end refreshOptionsCommand of the class EnvironmentEngine.");
			}
			ENVIRONMENTENGINE_DEFAULTSAVEDIRECTORY = raapi.findAttribute(ENVIRONMENTENGINE, "defaultSaveDirectory");
			if ((ENVIRONMENTENGINE_DEFAULTSAVEDIRECTORY == 0) && insertMetamodel)
				ENVIRONMENTENGINE_DEFAULTSAVEDIRECTORY = raapi.createAttribute(ENVIRONMENTENGINE, "defaultSaveDirectory", raapi.findPrimitiveDataType("String"));
			if (ENVIRONMENTENGINE_DEFAULTSAVEDIRECTORY == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute defaultSaveDirectory of the class EnvironmentEngine.");
			}
			ENVIRONMENTENGINE_STATUS = raapi.findAttribute(ENVIRONMENTENGINE, "status");
			if ((ENVIRONMENTENGINE_STATUS == 0) && insertMetamodel)
				ENVIRONMENTENGINE_STATUS = raapi.createAttribute(ENVIRONMENTENGINE, "status", raapi.findPrimitiveDataType("String"));
			if (ENVIRONMENTENGINE_STATUS == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute status of the class EnvironmentEngine.");
			}
			ENVIRONMENTENGINE_CLOUDLOCATION = raapi.findAttribute(ENVIRONMENTENGINE, "cloudLocation");
			if ((ENVIRONMENTENGINE_CLOUDLOCATION == 0) && insertMetamodel)
				ENVIRONMENTENGINE_CLOUDLOCATION = raapi.createAttribute(ENVIRONMENTENGINE, "cloudLocation", raapi.findPrimitiveDataType("String"));
			if (ENVIRONMENTENGINE_CLOUDLOCATION == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute cloudLocation of the class EnvironmentEngine.");
			}
			ENVIRONMENTENGINE_CLIENTSESSIONID = raapi.findAttribute(ENVIRONMENTENGINE, "clientSessionId");
			if ((ENVIRONMENTENGINE_CLIENTSESSIONID == 0) && insertMetamodel)
				ENVIRONMENTENGINE_CLIENTSESSIONID = raapi.createAttribute(ENVIRONMENTENGINE, "clientSessionId", raapi.findPrimitiveDataType("String"));
			if (ENVIRONMENTENGINE_CLIENTSESSIONID == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute clientSessionId of the class EnvironmentEngine.");
			}
			ENVIRONMENTENGINE_CLIENTACTIONINDEX = raapi.findAttribute(ENVIRONMENTENGINE, "clientActionIndex");
			if ((ENVIRONMENTENGINE_CLIENTACTIONINDEX == 0) && insertMetamodel)
				ENVIRONMENTENGINE_CLIENTACTIONINDEX = raapi.createAttribute(ENVIRONMENTENGINE, "clientActionIndex", raapi.findPrimitiveDataType("Integer"));
			if (ENVIRONMENTENGINE_CLIENTACTIONINDEX == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute clientActionIndex of the class EnvironmentEngine.");
			}
			EVENT_SUBMITTER = raapi.findAssociationEnd(EVENT, "submitter");
			if ((EVENT_SUBMITTER == 0) && insertMetamodel) {
				EVENT_SUBMITTER = raapi.createAssociation(EVENT, SUBMITTER, "event", "submitter", false);
			}
			if (EVENT_SUBMITTER == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end submitter of the class TDAKernel::Event.");
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
			ATTACHENGINECOMMAND_NAME = raapi.findAttribute(ATTACHENGINECOMMAND, "name");
			if ((ATTACHENGINECOMMAND_NAME == 0) && insertMetamodel)
				ATTACHENGINECOMMAND_NAME = raapi.createAttribute(ATTACHENGINECOMMAND, "name", raapi.findPrimitiveDataType("String"));
			if (ATTACHENGINECOMMAND_NAME == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute name of the class TDAKernel::AttachEngineCommand.");
			}
			LAUNCHTRANSFORMATIONINFOREGROUNDCOMMAND_URI = raapi.findAttribute(LAUNCHTRANSFORMATIONINFOREGROUNDCOMMAND, "uri");
			if ((LAUNCHTRANSFORMATIONINFOREGROUNDCOMMAND_URI == 0) && insertMetamodel)
				LAUNCHTRANSFORMATIONINFOREGROUNDCOMMAND_URI = raapi.createAttribute(LAUNCHTRANSFORMATIONINFOREGROUNDCOMMAND, "uri", raapi.findPrimitiveDataType("String"));
			if (LAUNCHTRANSFORMATIONINFOREGROUNDCOMMAND_URI == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute uri of the class LaunchTransformationInForegroundCommand.");
			}
			LAUNCHTRANSFORMATIONINFOREGROUNDCOMMAND_MESSAGE = raapi.findAttribute(LAUNCHTRANSFORMATIONINFOREGROUNDCOMMAND, "message");
			if ((LAUNCHTRANSFORMATIONINFOREGROUNDCOMMAND_MESSAGE == 0) && insertMetamodel)
				LAUNCHTRANSFORMATIONINFOREGROUNDCOMMAND_MESSAGE = raapi.createAttribute(LAUNCHTRANSFORMATIONINFOREGROUNDCOMMAND, "message", raapi.findPrimitiveDataType("String"));
			if (LAUNCHTRANSFORMATIONINFOREGROUNDCOMMAND_MESSAGE == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute message of the class LaunchTransformationInForegroundCommand.");
			}
			LAUNCHTRANSFORMATIONINFOREGROUNDCOMMAND_PROGRESS = raapi.findAttribute(LAUNCHTRANSFORMATIONINFOREGROUNDCOMMAND, "progress");
			if ((LAUNCHTRANSFORMATIONINFOREGROUNDCOMMAND_PROGRESS == 0) && insertMetamodel)
				LAUNCHTRANSFORMATIONINFOREGROUNDCOMMAND_PROGRESS = raapi.createAttribute(LAUNCHTRANSFORMATIONINFOREGROUNDCOMMAND, "progress", raapi.findPrimitiveDataType("Integer"));
			if (LAUNCHTRANSFORMATIONINFOREGROUNDCOMMAND_PROGRESS == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute progress of the class LaunchTransformationInForegroundCommand.");
			}
			ACTIVATEFRAMECOMMAND_FRAME = raapi.findAssociationEnd(ACTIVATEFRAMECOMMAND, "frame");
			if ((ACTIVATEFRAMECOMMAND_FRAME == 0) && insertMetamodel) {
				ACTIVATEFRAMECOMMAND_FRAME = raapi.createAssociation(ACTIVATEFRAMECOMMAND, FRAME, "activateFrameCommand", "frame", false);
			}
			if (ACTIVATEFRAMECOMMAND_FRAME == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end frame of the class ActivateFrameCommand.");
			}
			LAUNCHGUITRANSFORMATIONCOMMAND_URI = raapi.findAttribute(LAUNCHGUITRANSFORMATIONCOMMAND, "uri");
			if ((LAUNCHGUITRANSFORMATIONCOMMAND_URI == 0) && insertMetamodel)
				LAUNCHGUITRANSFORMATIONCOMMAND_URI = raapi.createAttribute(LAUNCHGUITRANSFORMATIONCOMMAND, "uri", raapi.findPrimitiveDataType("String"));
			if (LAUNCHGUITRANSFORMATIONCOMMAND_URI == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute uri of the class LaunchGUITransformationCommand.");
			}
			SHOWINFORMATIONBARCOMMAND_MESSAGE = raapi.findAttribute(SHOWINFORMATIONBARCOMMAND, "message");
			if ((SHOWINFORMATIONBARCOMMAND_MESSAGE == 0) && insertMetamodel)
				SHOWINFORMATIONBARCOMMAND_MESSAGE = raapi.createAttribute(SHOWINFORMATIONBARCOMMAND, "message", raapi.findPrimitiveDataType("String"));
			if (SHOWINFORMATIONBARCOMMAND_MESSAGE == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute message of the class ShowInformationBarCommand.");
			}
			POSTMESSAGETOFRAMECOMMAND_MESSAGEURI = raapi.findAttribute(POSTMESSAGETOFRAMECOMMAND, "messageURI");
			if ((POSTMESSAGETOFRAMECOMMAND_MESSAGEURI == 0) && insertMetamodel)
				POSTMESSAGETOFRAMECOMMAND_MESSAGEURI = raapi.createAttribute(POSTMESSAGETOFRAMECOMMAND, "messageURI", raapi.findPrimitiveDataType("String"));
			if (POSTMESSAGETOFRAMECOMMAND_MESSAGEURI == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute messageURI of the class PostMessageToFrameCommand.");
			}
			POSTMESSAGETOFRAMECOMMAND_FRAME = raapi.findAssociationEnd(POSTMESSAGETOFRAMECOMMAND, "frame");
			if ((POSTMESSAGETOFRAMECOMMAND_FRAME == 0) && insertMetamodel) {
				POSTMESSAGETOFRAMECOMMAND_FRAME = raapi.createAssociation(POSTMESSAGETOFRAMECOMMAND, FRAME, "postMessageToFrameCommand", "frame", false);
			}
			if (POSTMESSAGETOFRAMECOMMAND_FRAME == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end frame of the class PostMessageToFrameCommand.");
			}
		}
	}

	public Engine createEngine()
	{
		Engine retVal = new Engine(this);
		wrappers.put(retVal.getRAAPIReference(), retVal);
		return retVal; 
	}
  
	public AttachFrameCommand createAttachFrameCommand()
	{
		AttachFrameCommand retVal = new AttachFrameCommand(this);
		wrappers.put(retVal.getRAAPIReference(), retVal);
		return retVal; 
	}
  
	public FrameActivatedEvent createFrameActivatedEvent()
	{
		FrameActivatedEvent retVal = new FrameActivatedEvent(this);
		wrappers.put(retVal.getRAAPIReference(), retVal);
		return retVal; 
	}
  
	public FrameDeactivatingEvent createFrameDeactivatingEvent()
	{
		FrameDeactivatingEvent retVal = new FrameDeactivatingEvent(this);
		wrappers.put(retVal.getRAAPIReference(), retVal);
		return retVal; 
	}
  
	public CloseFrameRequestedEvent createCloseFrameRequestedEvent()
	{
		CloseFrameRequestedEvent retVal = new CloseFrameRequestedEvent(this);
		wrappers.put(retVal.getRAAPIReference(), retVal);
		return retVal; 
	}
  
	public DetachFrameCommand createDetachFrameCommand()
	{
		DetachFrameCommand retVal = new DetachFrameCommand(this);
		wrappers.put(retVal.getRAAPIReference(), retVal);
		return retVal; 
	}
  
	public RefreshOptionsCommand createRefreshOptionsCommand()
	{
		RefreshOptionsCommand retVal = new RefreshOptionsCommand(this);
		wrappers.put(retVal.getRAAPIReference(), retVal);
		return retVal; 
	}
  
	public OptionSelectedEvent createOptionSelectedEvent()
	{
		OptionSelectedEvent retVal = new OptionSelectedEvent(this);
		wrappers.put(retVal.getRAAPIReference(), retVal);
		return retVal; 
	}
  
	public ProjectCreatedEvent createProjectCreatedEvent()
	{
		ProjectCreatedEvent retVal = new ProjectCreatedEvent(this);
		wrappers.put(retVal.getRAAPIReference(), retVal);
		return retVal; 
	}
  
	public ProjectOpenedEvent createProjectOpenedEvent()
	{
		ProjectOpenedEvent retVal = new ProjectOpenedEvent(this);
		wrappers.put(retVal.getRAAPIReference(), retVal);
		return retVal; 
	}
  
	public CloseProjectCommand createCloseProjectCommand()
	{
		CloseProjectCommand retVal = new CloseProjectCommand(this);
		wrappers.put(retVal.getRAAPIReference(), retVal);
		return retVal; 
	}
  
	public ProjectClosingEvent createProjectClosingEvent()
	{
		ProjectClosingEvent retVal = new ProjectClosingEvent(this);
		wrappers.put(retVal.getRAAPIReference(), retVal);
		return retVal; 
	}
  
	public FrameResizedEvent createFrameResizedEvent()
	{
		FrameResizedEvent retVal = new FrameResizedEvent(this);
		wrappers.put(retVal.getRAAPIReference(), retVal);
		return retVal; 
	}
  
	public Option createOption()
	{
		Option retVal = new Option(this);
		wrappers.put(retVal.getRAAPIReference(), retVal);
		return retVal; 
	}
  
	public Frame createFrame()
	{
		Frame retVal = new Frame(this);
		wrappers.put(retVal.getRAAPIReference(), retVal);
		return retVal; 
	}
  
	public EnvironmentEngine createEnvironmentEngine()
	{
		EnvironmentEngine retVal = new EnvironmentEngine(this);
		wrappers.put(retVal.getRAAPIReference(), retVal);
		return retVal; 
	}
  
	public Event createEvent()
	{
		Event retVal = new Event(this);
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
  
	public AttachEngineCommand createAttachEngineCommand()
	{
		AttachEngineCommand retVal = new AttachEngineCommand(this);
		wrappers.put(retVal.getRAAPIReference(), retVal);
		return retVal; 
	}
  
	public LaunchTransformationInForegroundCommand createLaunchTransformationInForegroundCommand()
	{
		LaunchTransformationInForegroundCommand retVal = new LaunchTransformationInForegroundCommand(this);
		wrappers.put(retVal.getRAAPIReference(), retVal);
		return retVal; 
	}
  
	public ActivateFrameCommand createActivateFrameCommand()
	{
		ActivateFrameCommand retVal = new ActivateFrameCommand(this);
		wrappers.put(retVal.getRAAPIReference(), retVal);
		return retVal; 
	}
  
	public LaunchGUITransformationCommand createLaunchGUITransformationCommand()
	{
		LaunchGUITransformationCommand retVal = new LaunchGUITransformationCommand(this);
		wrappers.put(retVal.getRAAPIReference(), retVal);
		return retVal; 
	}
  
	public ShowInformationBarCommand createShowInformationBarCommand()
	{
		ShowInformationBarCommand retVal = new ShowInformationBarCommand(this);
		wrappers.put(retVal.getRAAPIReference(), retVal);
		return retVal; 
	}
  
	public UndoIgnoringClass createUndoIgnoringClass()
	{
		UndoIgnoringClass retVal = new UndoIgnoringClass(this);
		wrappers.put(retVal.getRAAPIReference(), retVal);
		return retVal; 
	}
  
	public AsyncCommand createAsyncCommand()
	{
		AsyncCommand retVal = new AsyncCommand(this);
		wrappers.put(retVal.getRAAPIReference(), retVal);
		return retVal; 
	}
  
	public EnqueueForegroundTransformationCommand createEnqueueForegroundTransformationCommand()
	{
		EnqueueForegroundTransformationCommand retVal = new EnqueueForegroundTransformationCommand(this);
		wrappers.put(retVal.getRAAPIReference(), retVal);
		return retVal; 
	}
  
	public PostMessageToFrameCommand createPostMessageToFrameCommand()
	{
		PostMessageToFrameCommand retVal = new PostMessageToFrameCommand(this);
		wrappers.put(retVal.getRAAPIReference(), retVal);
		return retVal; 
	}
  
}
