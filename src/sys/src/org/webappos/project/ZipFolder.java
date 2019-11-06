package org.webappos.project;

import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.*;
import java.util.*;
import java.util.zip.*;

public class ZipFolder {
	
	private boolean isZipFileOpen = false;
	private File file = null;
	private Path folder = null;

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
