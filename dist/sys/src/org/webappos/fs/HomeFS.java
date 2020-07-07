package org.webappos.fs;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.webappos.server.API;
import org.webappos.server.ConfigEx;
import org.webappos.server.ConfigStatic;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HomeFS implements IFileSystem {
	
	public static HomeFS ROOT_INSTANCE = new HomeFS(true);
	
	private String root;
	private boolean showHidden;
	private boolean useUserDrivers;
	private boolean useGlobalDrivers;
	private boolean readOnly = false;
	
	protected void setReadOnly(boolean value) {
		this.readOnly = value;
	}
	
	private void collectMountPointsRecursively(JsonObject obj, String path, HashMap<String, IFileSystem> mountPoints) {
		for (String name : obj.keySet()) {
			JsonElement el = obj.get(name);
			if (el.isJsonObject()) {
				collectMountPointsRecursively(el.getAsJsonObject(), path+"/"+name, mountPoints);
			}
			else
			if (el.isJsonPrimitive()) {
				String uri = el.getAsString();
				if (API.config instanceof ConfigEx) {
					IFileSystem drv = FSDriversManager.getFileSystemDriver(uri);
					if (drv!=null)
						mountPoints.put(path+"/"+name, drv);
				}
			}
		}		
	}
	
	private void collectMountPoints(String registryKey, String login, HashMap<String, IFileSystem> mountPoints) {
		JsonElement el = API.registry.getValue(registryKey);
		if ((el==null) || (!el.isJsonObject()))
			return;
		collectMountPointsRecursively(el.getAsJsonObject(), login/*=start path*/, mountPoints);
	}
	
	private Pair<IFileSystem, String> findDriver(String location) {
		if (!useUserDrivers && !useGlobalDrivers)
			return null;

		location = location.replace('\\', '/');
		while (location.startsWith("/"))
			location = location.substring(1);
		String[] arr = location.split("/");

		
		// TODO: introduce cache
		HashMap<String, IFileSystem> mountPoints = new HashMap<String, IFileSystem>();
		if (useGlobalDrivers)
			collectMountPoints("users/all users/fs_mount_points", arr[0], mountPoints);
		if (useUserDrivers)
			collectMountPoints("users/"+arr[0]+"/fs_mount_points", arr[0], mountPoints); // the first path element is the user login

		
		String sofar = null;
		
		IFileSystem retKey = null;
		String retVal = null;
		for (String s : arr) {
			if (s.isEmpty())
				continue;
			
			if (sofar == null)
				sofar = s;
			else
				sofar += "/"+s;
			
			if (retVal == null)
				retVal = s;
			else
				retVal += "/"+s;
			IFileSystem drv = mountPoints.get(sofar);
			if (drv!=null) {
				retKey = drv;
				retVal = null; // starting collecting retVal from scratch
			}
		}
		
		if (retKey==null)
			return null;
		else
			return Pair.of(retKey, retVal==null?"":retVal);
	}
	
	public HomeFS() {
		root = ConfigStatic.HOME_DIR;
		showHidden = false;
		useGlobalDrivers = true;
		useUserDrivers = true;
	}

	public HomeFS(boolean _showHidden) {
		root = ConfigStatic.HOME_DIR;
		showHidden = _showHidden;
		useGlobalDrivers = true;
		useUserDrivers = true;
	}

	public HomeFS(String _homeDir, boolean _showHidden, boolean _useGlobalDrivers, boolean _useUserDrivers) {
		root = _homeDir;
		showHidden = _showHidden;
		useGlobalDrivers = _useGlobalDrivers;
		useUserDrivers = _useUserDrivers;
	}
	
	/*public HomeFS(String login, boolean _showHidden) {
		root = ConfigStatic.HOME_DIR+File.separator+login;
		showHidden = _showHidden;
		useDrivers = false;
	}*/

	@Override
	public boolean copyPath(String _src, String _dst) {
		
		if (readOnly)
			return false;
		
		
		Pair<IFileSystem, String> drv1 = findDriver(_src);
		Pair<IFileSystem, String> drv2 = findDriver(_dst);
		
		if ( (drv1 == null) && (drv2 == null)) {
			String src = root+File.separator+_src;
			String dst = root+File.separator+_dst;
			File fsrc = new File(src);
			if (!fsrc.exists())
				return false;
			
			if (fsrc.isDirectory()) {
				try {
					FileUtils.copyDirectory(fsrc, new File(dst));
				} catch (IOException e) {
					return false;
				}
				return true;
			}
			else {				
				try {
					FileUtils.copyFile(fsrc, new File(dst));
				} catch (IOException e) {
					return false;
				}
				return true;
			}			
		}
		else {
			if (drv1 == null) {
				drv1 = Pair.of(this, _src);
			}
			if (drv2 == null) {
				drv2 = Pair.of(this, _dst);
			}
			
			if (drv1.getKey()==drv2.getKey()) {
				return drv1.getKey().copyPath(drv1.getValue(), drv2.getValue());
			}		
			else {
				PathInfo info = drv1.getKey().getPathInfo(drv1.getValue());
				if (info==null)
					return false;
				if (info.isDirectory) {
					// ensure the target directory...
					if (!drv2.getKey().pathExists(drv2.getValue()))
						drv2.getKey().createDirectory(drv2.getValue());
					
					if (drv2.getKey().pathExists(drv2.getValue())) {
						info = drv2.getKey().getPathInfo(drv2.getValue());
						if (info==null || !info.isDirectory)
							return false; 
					}
					else
						return false;
					
					// copy recursively via this.copyPath...
					List<PathInfo> list = drv1.getKey().listDirectory(drv1.getValue());
					if (list==null)
						return false;
					boolean b = true;
					for (PathInfo pi : list) {
						if (!this.copyPath(_src+"/"+pi.name, _dst+"/"+pi.name))
							b=false;
					}
					return b;
				}
				else
					return drv2.getKey().uploadFile(drv2.getValue(), drv1.getKey().downloadFile(drv1.getValue()), info.size, true);
			}
		}
	}

	@Override
	public boolean createDirectory(String path) {
		if (readOnly)
			return false;

		Pair<IFileSystem, String> drv = findDriver(path);
		
		if (drv==null) {		
			File f = new File(root+File.separator+path);
			f.mkdirs();
			return f.exists() && f.isDirectory();
		}
		else
			return drv.getKey().createDirectory(drv.getValue());
	}

	@Override
	public boolean deletePath(String path) {
		if (readOnly)
			return false;

		Pair<IFileSystem, String> drv = findDriver(path);
		
		if (drv==null) {		
			path = root+File.separator+path;
			if (new File(path).exists()) {
				
				if (new File(path).isDirectory()) {
					try {
						FileUtils.deleteDirectory(new File(path));
					} catch (IOException e) {
						return false;
					}
					return true;
				}
				else {
					return new File(path).delete();
				}
			}
			else
				return true;
		}
		else
			return drv.getKey().deletePath(drv.getValue());
	}

	@Override
	public InputStream downloadFile(String path) {

		Pair<IFileSystem, String> drv = findDriver(path);
		
		if (drv==null) {
			path = root+File.separator+path;
			
			if (new File(path).isDirectory()) {
				return null; // cannot download directory
			}
			
			try {
				return FileUtils.openInputStream(new File(path));
			} catch (IOException e) {
				return null;
			}
		}
		else
			return drv.getKey().downloadFile(drv.getValue());
	}

	@Override
	public boolean pathExists(String path) {
		Pair<IFileSystem, String> drv = findDriver(path);
		
		if (drv==null) {
			path = root+File.separator+path;
			return new File(path).exists();
		}
		else
			return drv.getKey().pathExists(drv.getValue());
	}

	@Override
	public AllocationInfo getAllocationInfo() {
//		AllocationInfo ai = new AllocationInfo();
//		TODO: user quotas		
		return null;
	}

	@Override
	public List<PathInfo> listDirectory(String path) {
		Pair<IFileSystem, String> drv = findDriver(path);
		
		if (drv==null) {
			path = root+File.separator+path;
			ArrayList<PathInfo> arr = new ArrayList<PathInfo>();
			String[] list = new File(path).list();
			if (list == null)
				return null;
			for (String s : list) {
				if (s.equals(".") || s.equals(".."))
						continue;
				if (showHidden || !s.startsWith(".")) {
					PathInfo pi = new PathInfo();
					File f = new File(path+File.separator+s);
					pi.isDirectory = f.isDirectory();
					
					pi.modified = f.lastModified();
					try {
						BasicFileAttributes attr = Files.readAttributes(f.toPath(), BasicFileAttributes.class);
						pi.created = attr.creationTime().toMillis();
					}
					catch(Throwable t) {
						pi.created = pi.modified;
					}
					pi.name = s;
					pi.size = f.length();
					arr.add(pi);
				}
			}		
			return arr;
		}
		else
			return drv.getKey().listDirectory(drv.getValue());
	}

	@Override
	public PathInfo getPathInfo(String path) {
		Pair<IFileSystem, String> drv = findDriver(path);
		
		if (drv==null) {
			path = root+File.separator+path;
			File f = new File(path);
			if (!f.exists())
				return null;
			PathInfo pi = new PathInfo();
			pi.isDirectory = f.isDirectory();
			pi.modified = f.lastModified();
			pi.name = f.getName();
			pi.size = f.length();
			return pi;
		}
		else
			return drv.getKey().getPathInfo(drv.getValue());
	}

	@Override
	public boolean renamePath(String _src, String _dst) {
		
		if (readOnly)
			return false;

		
		Pair<IFileSystem, String> drv1 = findDriver(_src);
		Pair<IFileSystem, String> drv2 = findDriver(_dst);
		
		if ( (drv1 == null) && (drv2 == null)) {
			String src = root+File.separator+_src;
			String dst = root+File.separator+_dst;
			
			// delete dst
			File fdst = new File(dst); 
			if (fdst.exists()) {
				if (fdst.isDirectory()) {
					try {
						FileUtils.deleteDirectory(fdst);
					} catch (IOException e) {
						return false;
					}
				}
				else {
					if (!fdst.delete())
						return false;
				}
			}
	
			return new File(src).renameTo(new File(dst));
		}
		else {
			if (drv1 == null) {
				drv1 = Pair.of(this, _src);
			}
			if (drv2 == null) {
				drv2 = Pair.of(this, _dst);
			}
			
			if (drv1.getKey()==drv2.getKey()) {
				return drv1.getKey().renamePath(drv1.getValue(), drv2.getValue());
			}		
			else {
				PathInfo info = drv1.getKey().getPathInfo(drv1.getValue());
				if (info==null)
					return false;
				if (info.isDirectory) {
					// ensure the target directory...
					if (!drv2.getKey().pathExists(drv2.getValue()))
						drv2.getKey().createDirectory(drv2.getValue());
					
					if (drv2.getKey().pathExists(drv2.getValue())) {
						info = drv2.getKey().getPathInfo(drv2.getValue());
						if (info==null || !info.isDirectory)
							return false; 
					}
					else
						return false;
					
					// copy recursively via this.copyPath...
					List<PathInfo> list = drv1.getKey().listDirectory(drv1.getValue());
					if (list==null)
						return false;
					boolean b = true;
					for (PathInfo pi : list) {
						if (this.copyPath(_src+"/"+pi.name, _dst+"/"+pi.name)) {
							if (!this.deletePath(_src+"/"+pi.name))
								b = false;
						}
						else
							b=false;
					}
					if (b)
						this.deletePath(_src);
					return b;
				}
				else {
					boolean b = drv2.getKey().uploadFile(drv2.getValue(), drv1.getKey().downloadFile(drv1.getValue()), info.size, true);
					if (b) {
						b = drv1.getKey().deletePath(drv1.getValue());
					}
					return b;
				}
			}
		}
	}

	@Override
	public List<String> searchForFiles(String startDirectory, String query) {
		Pair<IFileSystem, String> drv = findDriver(startDirectory);
		
		if (drv==null) {
			startDirectory = root+File.separator+startDirectory;
			// not implemented yet
			return null;
		}
		else
			return drv.getKey().searchForFiles(drv.getValue(), query);
	}

	@Override
	public boolean uploadFile(String path, InputStream stream, long length, boolean overwrite) {		
		if (readOnly)
			return false;

		Pair<IFileSystem, String> drv = findDriver(path);
		
		if (drv==null) {
			path = root+File.separator+path;				
			File targetFile = new File(path);
			try {
				targetFile.getParentFile().mkdirs();
				OutputStream outStream = new FileOutputStream(targetFile);
				 
				byte[] buffer = new byte[8 * 1024];
				int bytesRead;
				while ((bytesRead = stream.read(buffer, 0, Math.min(length>Integer.MAX_VALUE?Integer.MAX_VALUE:(int)length, (int)buffer.length))) > 0) {
					outStream.write(buffer, 0, bytesRead);
					length -= bytesRead;
				}
				outStream.close();
				if (length>0) {
					targetFile.delete();
					return false;
				}
			}
			catch(Throwable t) {
				targetFile.delete();
				return false;
			}
			return true;
		}
		else
			return drv.getKey().uploadFile(drv.getValue(), stream, length, overwrite);
	}

}
