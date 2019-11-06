package org.webappos.fs;

import java.io.InputStream;
import java.util.List;

// Path = File or Directory (recursively)
/**
 * File System API.
 * Used by webAppOS apps and services to access Home File System.
 * This API is also implemented in file system drivers, which provide access to remote file systems that
 * can be mounted in the webAppOS user home directory.
 */
public interface IFileSystem {
	/**
	 * Stores space usage info. Used by {@link getAllocationInfo()}. 
	 */
	public class AllocationInfo {
		public long totalSpace;
		public long usedSpace;
	}
	
	/**
	 * Stores a path element (one level).
	 */
	public class PathInfo {
		/**
		 * whether the given path element represents a directory
		 */
		public boolean isDirectory; 
		/**
		 * datetime when created
		 */
		public long created;
		/**
		 * datetime when modified
		 */
		public long modified;
		/**
		 * the simple name (the last name in the path) 
		 */
		public String name; // last name in the path
		/**
		 * the size of the file (for directories, always 0)
		 */
		public long size; // for directories 0
	}
	
	/**
	 * Copies the given file or folder.
	 * @param src source path (relative to the file system root)
	 * @param dst target path (relative to the file system root)
	 * @return whether the operation succeeded
	 */
	public boolean copyPath(String src, String dst);
	
	/** 
	 * Creates the given directory.
	 * @param path the directory to create (relative to the file system root)
	 * @return whether the operation succeeded
	 */
	public boolean createDirectory(String path);
	
	/**
	 * Deletes the given file or directory.
	 * @param path the file or directory to delete (relative to the file system root); directories are deleted recursively
	 * @return whether the operation succeeded
	 */
	public boolean deletePath(String path); // for dirs - recursively
	
	/**
	 * Reads the content of the given file.
	 * @param path the file name to download (relative to the file system root)
	 * @return the input stream or null on error; the caller must close the returned input stream after using it!
	 */
	public InputStream downloadFile(String path);
	
	/**
	 * Checks whether the given file or directory exists.
	 * @param path the file or directory name to check (relative to the file system root)
	 * @return true, if the given path exists, or false otherwise (also in case of an error)
	 */
	public boolean pathExists(String path);

	/**
	 * Gets the allocation info for the current file system.
	 * @return AllocationInfo object (or null, if allocation info is not available or not implemented)
	 */
	public AllocationInfo getAllocationInfo();

	/**
	 * Traverses the given directory (non-recursively) and returns the list of files and subdirectories.
	 * @param path directory name to list (relative to the file system root)
	 * @return the list of files and subdirectories, or null on error
	 */
	public List<PathInfo> listDirectory(String path);
//	public List<CloudMetaData> listDirectoryPartially(String path, long offset, long limit);

	/**
	 * Returns meta information for the given file or folder.
	 * @param path the file or directory name to consider (relative to the file system root)
	 * @return the PathInfo object or null on error
	 */
	public PathInfo getPathInfo(String path);
//	public InputStream getThumbnail(String path);

	/**
	 * Renames or physically moves the given file or folder to a new location.
	 * @param src source path (relative to the file system root)
	 * @param dst target path (relative to the file system root)
	 * @return whether the operation succeeded
	 */
	public boolean renamePath(String src, String dst);
	
	public List<String> searchForFiles(String startDirectory, String query);
	
	/**
	 * Uploads a file from the given input stream (the stream must be open and will be left open). 
	 * @param path the target file name (relative to the file system root)
	 * @param stream the source stream 
	 * @param length the length (in bytes) to read from the stream
	 * @param overwrite whether to overwrite the target file, if exists
	 * @return whether the operation succeeded; the caller must close the stream
	 */
	public boolean uploadFile(String path, InputStream stream, long length, boolean overwrite);
}
