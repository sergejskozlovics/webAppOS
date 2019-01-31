package org.webappos.fs;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.cloudrail.si.interfaces.CloudStorage;
import com.cloudrail.si.types.CloudMetaData;
import com.cloudrail.si.types.SpaceAllocation;

public class CloudStorageWrapper implements IFileSystem {
	
	private CloudStorage storage;
	
	public CloudStorageWrapper(CloudStorage _storage) {
		storage = _storage;
	}

	@Override
	public boolean copyPath(String src, String dst) {
		try {
			storage.copy(src, dst);
			return true;
		}
		catch(Throwable t) {
			return false;
		}
	}

	@Override
	public boolean createDirectory(String path) {
		try {
			storage.createFolder(path);
			return true;
		}
		catch(Throwable t) {
			return false;
		}
	}

	@Override
	public boolean deletePath(String path) {
		try {
			storage.delete(path);
			return true;
		}
		catch(Throwable t) {
			return false;
		}
	}

	@Override
	public InputStream downloadFile(String path) {
		try {
			return storage.download(path);
		}
		catch(Throwable t) {
			return null;
		}
	}

	@Override
	public boolean pathExists(String path) {
		try {
			return storage.exists(path);
		}
		catch(Throwable t) {
			return false;
		}
	}

	@Override
	public AllocationInfo getAllocationInfo() {
		try {
			SpaceAllocation sa = storage.getAllocation();
			AllocationInfo ai = new AllocationInfo();
			ai.totalSpace = sa.getTotal();
			ai.usedSpace = sa.getUsed();
			return ai;
		}
		catch(Throwable t) {
			return null;
		}
	}

	@Override
	public List<PathInfo> listDirectory(String path) {
		try {
			List<CloudMetaData> list = storage.getChildren(path);
			if (list==null)
				return null;
			ArrayList<PathInfo> arr = new ArrayList<PathInfo>();
			for (CloudMetaData md : list) {
				PathInfo pi = new PathInfo();
				pi.isDirectory = md.getFolder();
				pi.modified = md.getModifiedAt();
				pi.name = md.getName();
				pi.size = md.getSize();
				arr.add(pi);
			}
			return arr;
		}
		catch(Throwable t) {
			return null;
		}
	}

	@Override
	public PathInfo getPathInfo(String path) {
		try {
			CloudMetaData md = storage.getMetadata(path);
			if (md==null)
				return null;
			PathInfo pi = new PathInfo();
			pi.isDirectory = md.getFolder();
			pi.modified = md.getModifiedAt();
			pi.name = md.getName();
			pi.size = md.getSize();
			return pi;
		}
		catch(Throwable t) {
			return null;
		}
	}

	@Override
	public boolean renamePath(String src, String dst) {
		try {
			storage.move(src, dst);
			return true;
		}
		catch(Throwable t) {
			return false;
		}
	}

	@Override
	public List<String> searchForFiles(String startDirectory, String query) {
		try {
			List<CloudMetaData> list = storage.search(startDirectory+"/"+query);
			if (list==null)
				return null;
			ArrayList<String> arr = new ArrayList<String>();
			for (CloudMetaData md : list) {
				arr.add(md.getPath());
			}
			return arr;
		}
		catch(Throwable t) {
			return null;
		}
	}

	@Override
	public boolean uploadFile(String path, InputStream stream, long length, boolean overwrite) {
		try {
			storage.upload(path, stream, length, overwrite);
			return true;
		}
		catch(Throwable t) {
			return false;
		}
	}
	

}
