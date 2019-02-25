package org.webappos.fs;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.webappos.server.ConfigStatic;

public class HomeFS implements IFileSystem {
	
	public static HomeFS ROOT_INSTANCE = new HomeFS(true);
	
	private String root;
	private boolean showHidden;
	
	public HomeFS() {
		root = ConfigStatic.HOME_DIR;
		showHidden = false;
	}

	public HomeFS(boolean _showHidden) {
		root = ConfigStatic.HOME_DIR;
		showHidden = _showHidden;
	}

	public HomeFS(String login, boolean _showHidden) {
		root = ConfigStatic.HOME_DIR+File.separator+login;
		showHidden = _showHidden;
	}

	@Override
	public boolean copyPath(String src, String dst) {
		// TODO: check mount points			
		
		src = root+File.separator+src;
		dst = root+File.separator+dst;
		
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

	@Override
	public boolean createDirectory(String path) {
		File f = new File(root+File.separator+path);
		f.mkdirs();
		return f.exists() && f.isDirectory();
	}

	@Override
	public boolean deletePath(String path) {
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

	@Override
	public InputStream downloadFile(String path) {
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

	@Override
	public boolean pathExists(String path) {
		path = root+File.separator+path;
		return new File(path).exists();
	}

	@Override
	public AllocationInfo getAllocationInfo() {
//		AllocationInfo ai = new AllocationInfo();
//		TODO: user quotas		
		return null;
	}

	@Override
	public List<PathInfo> listDirectory(String path) {
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

	@Override
	public PathInfo getPathInfo(String path) {
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

	@Override
	public boolean renamePath(String src, String dst) {
		src = root+File.separator+src;
		dst = root+File.separator+dst;
		
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

	@Override
	public List<String> searchForFiles(String startDirectory, String query) {
		startDirectory = root+File.separator+startDirectory;
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean uploadFile(String path, InputStream stream, long length, boolean overwrite) {
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

}
