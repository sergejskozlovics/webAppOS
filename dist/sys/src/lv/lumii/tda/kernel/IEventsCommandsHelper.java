package lv.lumii.tda.kernel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lv.lumii.tda.raapi.IEngineAdapter;
import lv.lumii.tda.raapi.ITransformationAdapter;
import lv.lumii.tda.raapi.RAAPI;

public interface IEventsCommandsHelper {	
	
	/**
	 * Creates new or returns an existing engine adapter for the given engine name.
	 * 
	 * @param engineName the name of the engine (without adapter type)
	 * @return the engine adapter for the engine with the given name.
	 */
	IEngineAdapter getEngineAdapter(String engineName);
	
	/**
	 * Creates new or returns an existing transformation adapter for the given transformation type (transformation language).
	 * 
	 * @param transformationType the transformation adapter type (corresponding to a transformation language), e.g., "lquery", "atl", "mola"
	 * @return a transformation adapter for the given transformation type (transformation language).
	 */
	ITransformationAdapter getTransformationAdapter(String transformationType);	

	/**
	 * Tries to run the main transformation (the main transformation adapter is configured separately, e.g., when initializing TDAKernel).
	 * @param rArgument an int64 value representing a reference to some object in the repository to
	 *        pass as an argument
	 * @return whether the operation succeeded
	 */
	public boolean launchMainTransformation(long rArgument);
	
	
	/**
	 * Executes the given TDA Kernel command (unless this is not a TDA Kernel command)
	 * @param rCommand RAAPI reference to a command object
	 * @return +1 on success, -1 on error, 0 - if that was not TDA Kernel command 
	 */
	public int tryToExecuteKernelCommand(long rCommand);
	
	/**
	 * Executes the given TDA Kernel event (unless this is not a TDA Kernel event).
	 * If the event is ProjectOpenedEvent (from EE), performs project upgrade and attaches already attached engines
	 * (and returns 0, since this event has still to be handled by a transformation).
	 * @param rEvent RAAPI reference to an event object
	 * @return +1 on success, -1 on error, 0 - if that was not TDA Kernel event 
	 */
	public int tryToHandleKernelEvent(long rEvent);
	
	
	/**
	 * Detects the engine, in which metamodel the given event or command is defined.
	 * @param eventOrCommandName
	 * @return engine name for the given event/command, or null on error
	 */
	public String getEngineForEventOrCommand(String eventOrCommandName);
	
	/**
	 * Gets the list of transformations to be called when handling the given event.
	 * @param rEvent a reference to an event object
	 * @return the list (array) of transformations (URI pattern, e.g., lua:transformation-path or engine:EngineName)
	 */
	String[] getEventHandlers(long rEvent);
	
	/**
	 * Replicates event objects (attributes and links), thus the original object can be deleted, and
	 * we can continue using the replicated object. Useful for async even handlers.
	 * @param rEvent the event object to replicate
	 * @return the replicated object reference (or 0 on error)
	 */
	long replicateEventOrCommand(long rEvent);
	
	
	/**
	 * Associates the given command (or event) with the given engine
	 * @param raapi TDAKernel or repository
	 * @param rCommandOrEventCls a reference to a command or event class
	 * @param engineName engine name, the given command/event has to be associated with
	 */
	public static void associateCommandOrEventWithEngine(RAAPI raapi, long rCommandOrEventCls, String engineName)
	{
		long rKernelClass = raapi.findClass("TDAKernel::TDAKernel");
		if (rKernelClass == 0) {
			LoggerFactory.getLogger(IEventsCommandsHelper.class).error("Could not find class TDAKernel::TDAKernel.");
			return; // error
		}
		long it = raapi.getIteratorForAllClassObjects(rKernelClass);
		if (it == 0) {
			raapi.freeReference(rKernelClass);
			LoggerFactory.getLogger(IEventsCommandsHelper.class).error("Could not find the TDAKernel::TDAKernel instance.");
			return; // error			
		}
		long rKernel = raapi.resolveIteratorFirst(it);
		raapi.freeIterator(it);
		if (rKernel == 0) {
			raapi.freeReference(rKernelClass);
			LoggerFactory.getLogger(IEventsCommandsHelper.class).error("Could not find the TDAKernel::TDAKernel instance.");
			return; // error			
		}
		long rType = raapi.findPrimitiveDataType("String");
		if (rType == 0) {
			raapi.freeReference(rKernelClass);
			raapi.freeReference(rKernel);
			LoggerFactory.getLogger(IEventsCommandsHelper.class).error("Could not create attribute 'engineFor"+raapi.getClassName(rCommandOrEventCls)+"' for TDAKernel::TDAKernel since type String not found.");
			return; // error						
		}
		long rAttr = raapi.createAttribute(rKernelClass, "engineFor"+raapi.getClassName(rCommandOrEventCls), rType);
		raapi.freeReference(rType);
		if (rAttr == 0) {
			raapi.freeReference(rKernelClass);
			raapi.freeReference(rKernel);
			LoggerFactory.getLogger(IEventsCommandsHelper.class).error("Could not create attribute 'engineFor"+raapi.getClassName(rCommandOrEventCls)+"' for TDAKernel::TDAKernel.");
			return; // error						
		}
		if (!raapi.setAttributeValue(rKernel, rAttr, engineName)) {
			raapi.freeReference(rAttr);
			raapi.freeReference(rKernelClass);
			raapi.freeReference(rKernel);
			LoggerFactory.getLogger(IEventsCommandsHelper.class).error("Could not set attribute value for 'engineFor"+raapi.getClassName(rCommandOrEventCls)+"'.");
			return; // error
		}
		
		raapi.freeReference(rAttr);
		raapi.freeReference(rKernelClass);
		raapi.freeReference(rKernel);
		
		// associating subclasses (recursively)...
		it = raapi.getIteratorForDirectSubClasses(rCommandOrEventCls);
		if (it != 0) {
			long r = raapi.resolveIteratorFirst(it);
			while (r != 0) {
				associateCommandOrEventWithEngine(raapi, r, engineName);				
				raapi.freeReference(r);
				r = raapi.resolveIteratorNext(it);
			}
					
			raapi.freeIterator(it);
		}
	}

}
