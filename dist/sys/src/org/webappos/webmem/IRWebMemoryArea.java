package org.webappos.webmem;

import java.rmi.Remote;
import java.rmi.RemoteException;


public interface IRWebMemoryArea extends Remote {

	/**
	 * Returns the cache folder for the given webAppOS project.
	 * 
	 * @param project_id the project_id for which to get the cache folder
	 * @return project cache folder, or null if the project is not active/not found
	 * @throws RemoteException
	 */	
	public String getProjectFolder_R(String project_id) throws RemoteException;
	
	/**
	 * Returns the full webAppOS app name used to open the given project.
	 * @param project_id the project_id for which to get the app using the project
	 * @return full app name or null, if the project is not active/not found
	 * @throws RemoteException
	 */
	public String getProjectFullAppName_R(String project_id) throws RemoteException;
	
	/**
	 * Renames an active project (and re-assocates the project cache folder with the new project file). 
	 * @param project_id active project id
	 * @param new_project_id desired project id
	 * @return whether the operation succeeded
	 */
	public boolean renameActiveProject_R(String project_id, String new_project_id) throws RemoteException;

	/**
	 * Clears the web memory slot for the given project and disconnects all users by calling onFault runnables (within slot.done()).
	 * @param project_id the project_id for which to perform "fault" action.
	 */
	public void webMemoryFault_R(String project_id) throws RemoteException;
	
	
	/**
	 * Syncs the given actions+strings via the synchronizer attached to web memory of the given project_id.
	 * @param project_id the project_id for which to sync changes with clients
	 * @param nActions the length of the actions array to sync
	 * @param actions the actions array corresponding to the AR repository format
	 * @param delimitedStrings the concatenated strings array corresponding to the AR repository format
	 * @throws RemoteException
	 */
	public void syncChanges_R(String project_id, int nActions, double[] actions, String delimitedStrings) throws RemoteException;
}
