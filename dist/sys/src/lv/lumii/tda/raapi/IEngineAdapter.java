package lv.lumii.tda.raapi;

/**
 * A common interface for accessing different adapters for engines. 
 */
public interface IEngineAdapter {
	/**
	 * Finds the engine with the given name and loads it.
	 * The search is platform-specific: a Java adapter should search for a jar implementing the
	 * engine, a DLL adapter should search for a DLL, and so on. The adapter has to use certain technologies to
	 * ensure the communication with the engine. For instance, Java Native Interface (JNI) can be used to
	 * access DLLs from Java. Or, some kind of inter-process communication can be used to access engines running
	 * as parallel processes. The adapter has to pass a pointer to RAAPI (or to an RAAPI wrapper) to the real engine.
	 * The engine will use this pointer to access its own interface metamodel and the corresponding
	 * model in the repository. If the engine is being loaded for the first time,
	 * then its interface metamodel (and, probably, some instances) has to be put into the repository.
	 * The engine can also associate itself with certain events.
	 * 
	 * @param engineName the name of the engine to load, e.g., "DialogEngine"
	 * @param raapi a pointer to RAAPI (in most cases, it will be TDA Kernel)
	 * @return whether the engine has been successfully loaded.
	 */
	boolean load(String engineName, RAAPI raapi);
	
	/**
	 * Executes the given command specified as a reference to the corresponding command object in a repository.
	 * @param rCommand a 64-bit to the command object in a repository.
	 * @return whether the operation succeeded.
	 */
	boolean executeCommand(long rCommand);
	
	
	/**
	 * Handles an event specified as a reference to the corresponding event object.
	 * This function is used only when the engine specifies itself as event handler in an on-attribute for some event.
	 * @param rEvent a 64-bit reference to the corresponding event object in a repository.
	 * @return whether the operation succeeded.
	 */
	boolean handleEvent(long rEvent);
	
	/**
	 * Unloads the engine in a platform-specific way.
	 */
	void unload();
}
