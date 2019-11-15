package org.webappos.webmem;

import org.webappos.webmem.IWebMemory;

/**
 * Interface used by the server-side bridge and by server-side web processors to access server-side web memory slots.

 * @author Sergejs Kozlovics
 *
 */
public interface IWebMemoryArea {

	/**
	 * Returns an object (it can be a wrapper) that can be used to access web memory of the given project.
	 * @param project_id the project_id for which web memory to get access 
	 * @return a pointer to web memory or null if the project is not active/not found
	 */
	public IWebMemory getWebMemory(String project_id);

	/**
	 * Returns the cache folder for the given webAppOS project.
	 * 
	 * @param project_id the project_id for which to get the cache folder
	 * @return project cache folder, or null if the project is not active/not found
	 */	
	public String getProjectFolder(String project_id);
	
	/**
	 * Returns the full webAppOS app name used to open the given project.
	 * @param project_id the project_id for which to get the app using the project
	 * @return full app name or null, if the project is not active/not found
	 */
	public String getProjectFullAppName(String project_id);
	
	/**
	 * Renames an active project (and re-assocates the project cache folder with the new project file). 
	 * @param project_id active project id
	 * @param new_project_id desired project id
	 * @return whether the operation succeeded
	 */
	public boolean renameActiveProject(String project_id, String new_project_id);

	/**
	 * Clears the web memory slot for the given project and disconnects all users by calling onFault runnables (within slot.done()).
	 * @param project_id the project_id for which to perform "web memory fault" action.
	 */
	public void webMemoryFault(String project_id);
}
