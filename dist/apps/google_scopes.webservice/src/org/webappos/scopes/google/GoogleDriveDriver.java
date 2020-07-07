package org.webappos.scopes.google;

import java.io.InputStream;
import java.util.List;

public class GoogleDriveDriver implements org.webappos.fs.IFileSystem {

	GoogleDriveDriver() {

	}

	@Override
	public boolean copyPath(String src, String dst) {
		return false;
	}

	@Override
	public boolean createDirectory(String path) {
		return false;
	}

	@Override
	public boolean deletePath(String path) {
		return false;
	}

	@Override
	public InputStream downloadFile(String path) {
		return null;
	}

	@Override
	public boolean pathExists(String path) {
		return false;
	}

	@Override
	public AllocationInfo getAllocationInfo() {
		return null;
	}

	@Override
	public List<PathInfo> listDirectory(String path) { // "" => root; "a/b" -> faili i folderi a/b/*.*
		return null; // if error
		// List<PathInfo> l = new LinkedList<PathInfo>();
		// l.add(pi);
		// return l;
	}

	@Override
	public PathInfo getPathInfo(String path) {
		return null;
	}

	@Override
	public boolean renamePath(String src, String dst) {
		return false;
	}

	@Override
	public List<String> searchForFiles(String startDirectory, String query) {
		return null;
	}

	@Override
	public boolean uploadFile(String path, InputStream stream, long length, boolean overwrite) {
		return false;
	}
}
