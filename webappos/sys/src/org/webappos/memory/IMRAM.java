package org.webappos.memory;

import lv.lumii.tda.kernel.TDAKernel;

public interface IMRAM {

	/**
	 * Returns the TDA kernel (or some wrapper using webAppOS memory bus) associated with the given project id.
	 * @param project_id the project_id for which to get a TDA Kernel
	 * @return an initialized TDAKernel instance or null if the project is not active/not found
	 */
	public TDAKernel getTDAKernel(String project_id);

	/**
	 * Returns the cache folder for the given webAppOS project.
	 * 
	 * @param project_id the project_id for which to get the cache folder
	 * @return project cache folder, or null if the project is not active/not found
	 * @throws RemoteException
	 */	
	public String getProjectFolder(String project_id);
	
	/**
	 * Returns the full webAppOS app name used to open the given project.
	 * @param project_id the project_id for which to get the app using the project
	 * @return full app name or null, if the project is not active/not found
	 * @throws RemoteException
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
	 * Clears the MRAM slot for the given project and disconnects all users by calling onFault runnables (within slot.done()).
	 * @param project_id the project_id for which to perform "MRAM fault" action.
	 */
	public void faultMRAM(String project_id);
}
