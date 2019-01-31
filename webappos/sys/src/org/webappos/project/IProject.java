package org.webappos.project;

import java.io.File;

import org.webappos.properties.AppProperties;
import org.webappos.server.ConfigStatic;

import lv.lumii.tda.kernel.IEventsCommandsHook;
import lv.lumii.tda.kernel.TDAKernel;
import lv.lumii.tda.raapi.RAAPI_Synchronizer;

/**
 * @author Sergejs Kozlovics
 * 
 * Most methods (open, createFromTemplate, bootstrap, save, and close) of IProject instances
 * have to be called in foreground thread (e.g., via SmartThreading or ForegroundThread class).
 * 
 * All names and paths (e.g. whether the cloud project is prefixed by a user login) have to be checked elsewhere.
 * We only validate cloud paths in order not to interfere with other users/projects. 
 *
 */
public interface IProject {
	
	public final static String DEFAULT_REPOSITORY = "ar";	
	public final static String CLIENT_REPOSITORY = "ar:shmclient"; 
	public final static String SERVER_REPOSITORY = "ar:shmserver";
//	public final static String REPOSITORY_POSTFIX = "/data.xmi";

	/*public final static String DEFAULT_REPOSITORY = "ar";	
	public final static String CLIENT_REPOSITORY = "ar:shmclient"; 
	public final static String SERVER_REPOSITORY = "ar:shmserver";*/

/*	public final static String DEFAULT_REPOSITORY = "ar";	
//	public final static String SERVER_REPOSITORY = "ar"; // used by CloudProject when opening repositories
	public final static String CLIENT_REPOSITORY = "ar"; // used by web processors when connecting to repositories of CloudProjects
	public final static String SERVER_REPOSITORY = "ar:shmserver"; // used by CloudProject when opening repositories
//	public final static String CLIENT_REPOSITORY = "ar:shmclient"; // used by web processors when connecting to repositories of CloudProjects
	*/
		
	/**
	 * Opens an existing project.
	 * @param name a project name from the end-user perspective - for cloud project: project_id (user/subfolder/projectName.extension);
	 *        for zipped project: full name of the zipped file
	 * @return whether the operation succeeded
	 */
	public boolean open(AppProperties appProps, String name, String login, RAAPI_Synchronizer sync, IEventsCommandsHook hook);
	
	
	/**
	 * Creates a new project from the given template.
	 * @param templateFileName the name of the zipped file to be used as a template for creating a new project; for zipped projects the name is ordinary filename; for cloud project - the name must be relative to the app templates search path
	 * @param desiredName the desired name of the project (project_id or file name); can be null only for zipped projects (for cloud projects we need a user name and a subfolder specified in the desiredName)
	 * @return whether the operation succeeded
	 */
	public boolean createFromTemplate(AppProperties appProps, String templateFileName, String desiredName, String login, RAAPI_Synchronizer sync, IEventsCommandsHook hook);
	
	/**
	 * Bootstraps a new project with the given app (initial_transformation property must be specified in app.properties)
	 * @param appProps app properties of the app that will be used to bootstrap the project
	 * @param desiredName the desired name of the project (project_id or file name); optional for zipped projects, required for cloud projects (for cloud projects we need a user name and a subfolder specified in the desiredName)
	 * @return whether the operation succeeded
	 */
	public boolean boostrap(AppProperties appProps, String desiredName, String login, RAAPI_Synchronizer sync, IEventsCommandsHook hook);

	
	/**
	 * Returns the folder name that stores extracted project files.
	 * @return for cloud project: the full path of the "_ar" folder;
	 * for zipped project: the full path of the temp folder with extracted project
	 */
	public String getFolderName();	
	
	/**
	 * Returns the project name.
	 * For cloud projects this is project_id user/subfolder/projectName.extension;
	 * for zipped projects this is the full name of the zipped file (or null, if this project has been just created and not saved yet,
	 * and setName not called).
	 * @return project name (project_id or full name of zipped file)
	 */
	public String getName();
	
	/**
	 * Sets the new name of the project.
	 * @param _name the new name of the project (project_id or full name of the zipped file)
	 * @return whether the operation succeeded (true in most cases)
	 */
	public boolean setName(String _name);	

	
	/**
	 * Saves the project. For cloud projects: saves the repository; for zipped project: saves the repository and zips the file.
	 * @return whether the operation succeeded (it will not succeed, if the project name is not specified)
	 */
	public boolean save();
	
	
	/**
	 * Closes the project. For cloud project: nothing for ordinary projects, deletes the folder for temp projects;
	 * for zipped projects: deletes the temporary folder.
	 */
	public void close();
	
	/**
	 * @return the underlying TDA Kernel of open project (or null, if project is not open)
	 */
	public TDAKernel getTDAKernel();
}
	
