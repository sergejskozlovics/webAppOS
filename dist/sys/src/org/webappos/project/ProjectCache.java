package org.webappos.project;

import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.*;
import java.util.*;
import java.util.zip.*;

import org.apache.commons.io.FileUtils;
import org.webappos.antiattack.ValidityChecker;
import org.webappos.fs.HomeFS;
import org.webappos.fs.IFileSystem;
import org.webappos.properties.WebAppProperties;
import org.webappos.properties.PropertiesManager;
import org.webappos.server.API;
import org.webappos.server.ConfigStatic;

import com.google.gson.JsonElement;



/**
 * We use webAppOS Registry to store info about projects and associated cache.
 * 
 * projects/uuid document is {
 *   "homefs_path":"login/subdir/name.extension",
 *   "modified":12345,
 *   ...
 * }
 * 
 * projects/project_id=projects/login/subdir/name.extension setting value is "uuid"
 * 
 * @author Sergejs Kozlovics
 *
 */
public class ProjectCache {
	
	private boolean isZipFileOpen = false;
	private File file = null;
	private Path folder = null;

	/**
	 * Generates a unique project cache uuid and creates a folder in <webappos-root>/cache/projects/<returned-uuid>
	 * @return uuid
	 */
	private static String newProjectCacheUUID() {
		
		String uuid;
		uuid = UUID.randomUUID().toString();
		File f = new File(ConfigStatic.PROJECTS_CACHE_DIR+File.separator+File.separator+uuid); 
		while (f.exists()) {
			uuid = UUID.randomUUID().toString();
			f = new File(ConfigStatic.PROJECTS_CACHE_DIR+File.separator+File.separator+uuid);
		}

		f.mkdirs();
		
		return uuid;
	}
		
	
	private static String downloadUnzipValidate(String project_id, WebAppProperties appProps, String uuid, boolean create) {		
		if (appProps == null)
			return null;
		
		File f = new File(ConfigStatic.PROJECTS_CACHE_DIR+File.separator+uuid); 
		if (create) {
			if (!f.exists()) {
				return f.mkdirs()?f.getAbsolutePath():null;
			}
			return f.getAbsolutePath();
		}
		else {
			InputStream is = HomeFS.ROOT_INSTANCE.downloadFile(project_id);
			if (is == null)
				return null;	
			
			// extract zip from is, validate
			boolean b = ProjectCache.unzip(is, ConfigStatic.PROJECTS_CACHE_DIR+File.separator+uuid); // closes is
			if (!b)
				return null;
			
			try {
				ValidityChecker.checkProjectCacheDirectory(f.getAbsolutePath(), appProps);
			} catch (Exception e) {
				try {
					FileUtils.deleteDirectory(f);
				} catch (Throwable t) {
				}
				return null;
			}
		}		
		
		// setting registry values...
		API.registry.setValue("projects/"+project_id, uuid);
		API.registry.setValue("projects/"+uuid+"/homefs_path", project_id);
		IFileSystem.PathInfo info = HomeFS.ROOT_INSTANCE.getPathInfo(project_id);
		if (info!=null)
			API.registry.setValue("projects/"+uuid+"/modified", info.modified);
		
		return f.getAbsolutePath();
	}
	
	public static boolean zipAndUpload(String uuid, String project_id, boolean overwrite) {
		Path src = new File(ConfigStatic.PROJECTS_CACHE_DIR+File.separator+uuid).toPath();
		File tmp;
		try {
			tmp = File.createTempFile("project_cache_zip", ".tmp");
		} catch (IOException e) {
			return false;
		}
		if (!ProjectCache.zip(src, tmp))
			return false;		
		
		InputStream is;
		try {
			is = new BufferedInputStream(new FileInputStream(tmp));
		} catch (FileNotFoundException e) {
			tmp.delete();
			return false;
		}
		
		boolean b = HomeFS.ROOT_INSTANCE.uploadFile(project_id, is, tmp.length(), false);
		tmp.delete();
		try {
			is.close();
		}
		catch(Throwable t) {			
		}
		
		if (!b)
			return false;
		
		IFileSystem.PathInfo info = HomeFS.ROOT_INSTANCE.getPathInfo(project_id);
		if (info!=null)
			API.registry.setValue("projects/"+uuid+"/modified", info.modified);
		return true;
	}
	
	
	

	public static String getCloudProjectCacheDirectory(String project_id, WebAppProperties appProps) {
		return getCloudProjectCacheDirectory(project_id, appProps, false);
	}
	
	/**
	 * Downloads/extracts the project with the given project_id into a project cache directory.
	 * @param project_id in the form login/subdir/name.extension (must be validated)
	 * @param appName application name to open project with
	 * @param create
	 * @return full path of the project cache directory, or null on error
	 */
	public static String getCloudProjectCacheDirectory(String project_id, WebAppProperties appProps, boolean create) {		
		JsonElement el = API.registry.getValue("projects/"+project_id);
		String uuid = null;
		if (el!=null)
			uuid=el.getAsString();
		if ((uuid==null)||(uuid.isEmpty())) {
			// not found, creating
			uuid = newProjectCacheUUID();
			API.registry.setValue("projects/"+project_id, uuid);
			
			// download + unzip + validate
			return downloadUnzipValidate(project_id, appProps, uuid, create);			
		}

		File fcache = new File(ConfigStatic.PROJECTS_CACHE_DIR+File.separator+uuid);
		if (!fcache.exists()) {
			fcache.mkdirs();
			
			// download + unzip + validate
			return downloadUnzipValidate(project_id, appProps, uuid, create);			
		}
		
		
		el = API.registry.getValue("projects/"+uuid+"/modified");
		long cacheModified = fcache.lastModified();
		
		try {
			cacheModified = el.getAsLong();
		}
		catch(Throwable t) {			
		}
		
		IFileSystem.PathInfo info = HomeFS.ROOT_INSTANCE.getPathInfo(project_id);
		if (info==null) {
			// could not get project file info
			return fcache.getAbsolutePath();
		}
		
		if (info.modified>cacheModified) {
			return downloadUnzipValidate(project_id, appProps, uuid, false);
		}
		else
			return fcache.getAbsolutePath();
	}
	
	public static void deleteProjectCache(String project_id, String uuid) {
		API.registry.setValue("projects/"+project_id, null); 		
		API.registry.setValue("projects/"+uuid, null);
		System.out.println("Deleting cache "+ConfigStatic.PROJECTS_CACHE_DIR+File.separator+uuid);
		try {
			FileUtils.deleteDirectory(new File(ConfigStatic.PROJECTS_CACHE_DIR+File.separator+uuid));
		} catch (Throwable t) {
		}			
		
	}
	
	public static boolean changeProjectId(String project_id, String new_project_id) {
		JsonElement uuid = API.registry.getValue("projects/"+project_id);
		if ((uuid==null) || (uuid.getAsString().isEmpty())) {
			// no project cache for this project_id; just rename/move it
			return HomeFS.ROOT_INSTANCE.renamePath(project_id, new_project_id);
		}
		
		String s = uuid.getAsString();
		System.err.println("UUUID is "+s);
		
		// else we have project cache uuid
		
		if (!HomeFS.ROOT_INSTANCE.renamePath(project_id, new_project_id))
			return false;
		
		// adjusting Registry settings		
		// 1) remove old link
		API.registry.setValue("projects/"+project_id, null); 		
		// 2) create new link
		API.registry.setValue("projects/"+s+"/homefs_path", new_project_id);
		API.registry.setValue("projects/"+new_project_id, s);
		
		return true;
	}
	
	public static boolean zip(Path folder, File zipFile)
	{
		FileOutputStream fos;
		try {
			fos = new FileOutputStream(zipFile);
		} catch (FileNotFoundException e) {
			return false;
		}
		ZipOutputStream zos = new ZipOutputStream(fos);
		
		boolean result = zipToZipStream(folder, "", zos);
		try { zos.close(); } catch (IOException e) { return false; }
		try { fos.close(); } catch (IOException e) {}		
		return result;
	}
		
	private static boolean zipToZipStream(Path folder, String prefix, ZipOutputStream zos)
	{
		boolean result = true;
				
		File[] files = folder.toFile().listFiles();
		if (files==null)
			return false;
		
		for (int i = 0; i < files.length; i++) {
			if (files[i].isDirectory()) {
				ZipEntry e = new ZipEntry(prefix+files[i].getName()+"/");
				try {
					zos.putNextEntry(e);
				} catch (IOException e1) {
					result = false;
				}
				try { zos.closeEntry();	} catch (IOException e2) {}
				result &= zipToZipStream(files[i].toPath(), prefix+files[i].getName()+"/", zos);
			}
			else {
				if (files[i].getName().endsWith(".shm"))
					continue; // don't save .shm files
                byte[] buffer = new byte[1024];
                FileInputStream fis = null;
				try {
					fis = new FileInputStream(files[i]);
				} catch (FileNotFoundException e) {
				}
				if (fis != null) {
	                try {
						zos.putNextEntry(new ZipEntry(prefix+files[i].getName()));
					} catch (IOException e) {
						result = false;
					}
	                
	                if (result) {			
	                	int length;
	                	try {
							while ((length = fis.read(buffer)) > 0) {
								zos.write(buffer, 0, length);
							}
						} catch (IOException e) {
							result = false;
						}
	                }
	                try { zos.closeEntry();	} catch (IOException e) {}
	                try { fis.close(); } catch (IOException e) {}
				}
	        }
			if (!result)
				return false;
		}

		return true;
	}
	
	public static boolean unzip(InputStream is, String destFolder) {
		
		boolean result = true;
		
		try {
			ZipInputStream zin = null;
			try {
				zin = new ZipInputStream(is);
			    ZipEntry zipEntry = null;
			    while (result && ((zipEntry = zin.getNextEntry()) != null)) {			    	
			    	  if (zipEntry.getName().endsWith(".shm"))
			    		  continue;
			    	  File fEntry = new File(destFolder+File.separator+zipEntry.getName());
			    	  // Creating folders for this zip entry...
			    	  if (zipEntry.isDirectory()) {
			              fEntry.mkdirs();
			          }
			    	  else {			    		  
				            fEntry.getParentFile().mkdirs();
				            try {
							  fEntry.createNewFile();
							} catch (IOException e) {
							  result = false;
							}
		
				            if (result) {
				            	try {
							        BufferedOutputStream bos = null;
					            	try {
							    	  // Extracting...
								      int size;
								      byte[] buffer = new byte[2048];
									  try {
										bos = new BufferedOutputStream(
										      new FileOutputStream(fEntry), buffer.length);
									  } catch (FileNotFoundException e) {
										  result = false;
									  }
									  if (result) {
										  try {
											while ((size = zin.read(buffer, 0, buffer.length)) != -1) {
												  bos.write(buffer, 0, size);
											}
					 					    bos.flush();
										  } catch (IOException e) {
											  result = false;
										  }
									      
									  }
					            	}
					            	finally {
					            		if (bos!=null)
					            			try { bos.close(); } catch (IOException e) {}
					            	}
				            	}
				            	catch(Throwable t) {
				            		result = false;
				            	}
				            }
			    	  }
					zin.closeEntry();
			    }
			}
			finally {
				if (zin!=null)
					try {zin.close();}catch(Throwable t){}
				try {is.close();}catch(Throwable t){}
			}
		}
		catch(Throwable t) {
			result = false;
		}
		
		
		if (!result) {
			File f = new File(destFolder);
			if (f.exists()) {
				try {
					FileUtils.deleteDirectory(f);
				} catch (IOException e) {
				}
			}
		}
	    
			
		return result;
	}
	
	public static boolean unzip(File _zipFile, Path folder)
	{
		ZipFile zipFile = null;
		try {
			zipFile = new ZipFile(_zipFile.getAbsolutePath());
		} catch(Throwable t) {
			return false;
		}
		
		assert(zipFile != null);
		boolean result = true;
		
	    Enumeration<? extends ZipEntry> enumeration = zipFile.entries();
	    while (result && enumeration.hasMoreElements()) {
	      ZipEntry zipEntry = enumeration.nextElement();
	      BufferedInputStream bis = null;
	      try {
	    	  bis = new BufferedInputStream(zipFile.getInputStream(zipEntry));
	      } catch (Throwable t) {
	    	  result = false;
	      }
	      if (result) {
	    	  File fEntry = new File(folder.toAbsolutePath().toString()+File.separator+zipEntry.getName());
	    	  // Creating folders for this zip entry...
	    	  if (zipEntry.isDirectory()) {
	              fEntry.mkdirs();
	          }
	    	  else {
		    	if (zipEntry.getName().endsWith(".shm"))
		    	   continue;
	            fEntry.getParentFile().mkdirs();
	            try {
				  fEntry.createNewFile();
				} catch (IOException e) {
				  result = false;
				}
	            if (result) {
		    	  // Extracting...
			      int size;
			      byte[] buffer = new byte[2048];
			      BufferedOutputStream bos = null;
				  try {
					bos = new BufferedOutputStream(
					      new FileOutputStream(fEntry), buffer.length);
				  } catch (FileNotFoundException e) {
					  result = false;
				  }
				  if (result) {
					  try {
						while ((size = bis.read(buffer, 0, buffer.length)) != -1) {
							  bos.write(buffer, 0, size);
						}
 					    bos.flush();
					  } catch (IOException e) {
						  result = false;
					  }
				      try { bos.close(); } catch (IOException e) {}
				  }
	    	    }
		        try {	bis.close(); } catch (IOException e) {}
	    	  }
	      }
	    }
	    
	    try { zipFile.close(); } catch (IOException e) {}
	    return result;
	}
	
	public static boolean deleteFolder(Path folder)
	{
		try {
			Files.walkFileTree(folder, new SimpleFileVisitor<Path>() {
				@Override
				public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
					Files.delete(file);
					return FileVisitResult.CONTINUE;
				}

				@Override
				public FileVisitResult postVisitDirectory(Path dir, IOException e) throws IOException {
					Files.delete(dir);
					return FileVisitResult.CONTINUE;
				}
					
			});
		} catch (IOException e) {
		}
		return !folder.toFile().exists();
	}
	
	public boolean createNew()
	{
		if (isZipFileOpen)
			return false;
		
		try {
			folder = Files.createTempDirectory("tda2_temporary_");
		} catch (IOException e) {
			return false;
		}
		
		file = null; // save() will not work; must call saveAs() first
		isZipFileOpen = true;
		return true;
		
	}
	
	public boolean createFromTemplate(File zipFile)
	{
		boolean retVal = open(zipFile);
		file = null;
		return retVal;
	}
	
	public boolean open(File zipFile)
	{
		if (isZipFileOpen)
			return false;
		
		try {
			folder = Files.createTempDirectory("tda2_temporary_");
		} catch (IOException e) {
			return false;
		}
		
		if (ZippedToCloudProjectConverter.convert(zipFile.getAbsolutePath(), folder.toString(), IProject.DEFAULT_REPOSITORY) == null)		 {
			deleteFolder(folder);
			folder = null;
			return false;
		}
		
		file = zipFile;
		isZipFileOpen = true;
		return true;
	}
	
	public boolean isOpen()
	{
		return isZipFileOpen;
	}
	
	public File getFile()
	{
		return file;
	}
	
	public Path getFolder()
	{
		return folder;
	}
	
	public boolean save()
	{
		return saveAs(file);
	}

	public boolean saveAs(File otherZipFile)
	{
		return saveAs(otherZipFile, true);
	}

	public boolean saveAs(File otherZipFile, boolean updateFileName)
	{
		if (otherZipFile == null)
			return false;
		boolean result = zip(folder, otherZipFile);
		if (result && updateFileName)
			file = otherZipFile;
		return result;
	}
	
	public boolean setFile(File f)
	{
		if (f == null)
			return false;
		if (!f.exists()) {
			try {
				if (f.createNewFile()) {
					file = f;
					return true;
				}
				else
					return false;
			} catch (IOException e) {
				return false;
			}
		}
		file = f;
		return true;
	}
	
	public void close()
	{
		if (!isZipFileOpen)
			return;

		deleteFolder(folder);
		
		file = null;
		folder = null;		
		isZipFileOpen = false;
	}
}
