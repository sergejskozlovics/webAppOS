package org.webappos.fs;

import java.io.InputStream;
import java.util.List;

// Path = File or Directory (recursively)
public interface IFileSystem {
	public class AllocationInfo {
		public long totalSpace;
		public long usedSpace;
	}
	public class PathInfo {
		public boolean isDirectory; 
		public long modified;
		public String name; // last name in the path
		public long size; // for directories 0
	}
	
	public boolean copyPath(String src, String dst);	
	public boolean createDirectory(String path);
	public boolean deletePath(String path); // for dirs - recursively	
	/**
	 *  
	 * @param path
	 * @return the input stream or null on error; the caller must close the inputstream after using it
	 */
	public InputStream downloadFile(String path);
	public boolean pathExists(String path);

	/**
	 * @return AllocationInfo object (or null, if not available/not implemented)
	 */
	public AllocationInfo getAllocationInfo();

	public List<PathInfo> listDirectory(String path);
//	public List<CloudMetaData> listDirectoryPartially(String path, long offset, long limit);

	public PathInfo getPathInfo(String path);
//	public InputStream getThumbnail(String path);

	public boolean renamePath(String src, String dst);
	public List<String> searchForFiles(String startDirectory, String query);
	/**
	 * Uploads a file from the given input stream (must be open).
	 * @param path
	 * @param stream
	 * @param length
	 * @param overwrite
	 * @return whether the operation succeeded; the caller must close the stream
	 */
	public boolean uploadFile(String path, InputStream stream, long length, boolean overwrite);
}
