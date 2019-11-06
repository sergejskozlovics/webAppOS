package lv.lumii.tda.raapi;

/**
 * A common interface for accessing different adapters for transformations used to launch transformations written in different languages.
 */
public interface ITransformationAdapter {
	/**
	 * Loads the corresponding tool or library for launching model transformations written in the language the adapter supports.
	 * @param raapi a pointer to RAAPI (in most cases, it will be TDA Kernel) to access the repository
	 * @return whether the operation succeeded.
	 */
	boolean load (RAAPI raapi);	
	
	/**
	 * Launches the transformation at the given location.
	 * @param location an adapter-specific location of the transformation (e.g., a file name containing the transformation definition)
	 * @param argument specifies either the event object the transformation needs to handle, or an ExecuteTransformationCommand instance used to launch this transformation
	 * @return whether the operation succeeded.
	 */
	boolean launchTransformation (String location, long argument);
	
	/**
	 * Unloads the infrastructure used to launch transformations.
	 */
	void unload();
}
