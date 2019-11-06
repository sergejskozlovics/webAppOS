package org.webappos.service.webdav;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.webappos.antiattack.ValidityChecker;
import org.webappos.fs.HomeFS;
import org.webappos.fs.IFileSystem.PathInfo;
import org.webappos.server.ConfigStatic;

import net.sf.webdav.ITransaction;
import net.sf.webdav.IWebdavStore;
import net.sf.webdav.StoredObject;
import net.sf.webdav.exceptions.WebdavException;

public class HomeFS_WebdavStore implements IWebdavStore {
	
	private static Logger logger =  LoggerFactory.getLogger(HomeFS_WebdavStore.class);
	
	private HomeFS fs;

	public HomeFS_WebdavStore(File _root) {
		
		if ((_root==null) || !new File(ConfigStatic.HOME_DIR).getAbsolutePath().equals(_root.getAbsolutePath()))
				logger.warn("HomeFS webdav store constructor - ignoring provided root "+_root.getAbsolutePath()+", always use webAppOS home dir "+ConfigStatic.HOME_DIR);
		// ignore _root
		
	    fs = new HomeFS();
	}

	@Override
	public void destroy() {
	}

	@Override
	public ITransaction begin(Principal principal) {
		return new ITransaction() {

			@Override
			public Principal getPrincipal() {
				return principal;
			}
			
		};
	}

	@Override
	public void checkAuthentication(ITransaction transaction) {
	}

	@Override
	public void commit(ITransaction transaction) {
	}

	@Override
	public void rollback(ITransaction transaction) {
	}

	@Override
	public void createFolder(ITransaction transaction, String folderUri) {
		try {
			ValidityChecker.checkRelativePath(folderUri, false);
		} catch (Exception e) {
			throw new WebdavException("invalid resource URI"); 
		}
		fs.createDirectory(transaction.getPrincipal().getName()+"/"+folderUri);
	}

	@Override
	public void createResource(ITransaction transaction, String resourceUri) {
		try {
			ValidityChecker.checkRelativePath(resourceUri, false);
		} catch (Exception e) {
			throw new WebdavException("invalid resource URI");
		}
		if (!fs.uploadFile(transaction.getPrincipal().getName()+"/"+resourceUri, new ByteArrayInputStream(new byte[] {}), 0, true))
			throw new WebdavException("cannot create file " + resourceUri); 
	}

	@Override
	public InputStream getResourceContent(ITransaction transaction, String resourceUri) {
		try {
			ValidityChecker.checkRelativePath(resourceUri, false);
		} catch (Exception e) {
			throw new WebdavException("invalid resource URI");
		}
		return fs.downloadFile(transaction.getPrincipal().getName()+"/"+resourceUri);
	}

	@Override
	public long setResourceContent(ITransaction transaction, String resourceUri, InputStream content,
			String contentType, String characterEncoding) {
		try {
			ValidityChecker.checkRelativePath(resourceUri, false);
		} catch (Exception e) {
			throw new WebdavException("invalid resource URI");
		}
		long length = 0;
		try {
			length = content.available();
		} catch (IOException e) {
		}
		if (fs.uploadFile(transaction.getPrincipal().getName()+"/"+resourceUri, content, length, true))
			return length;
		else
			throw new WebdavException("cannot upload file " + resourceUri); 
	}

	@Override
	public String[] getChildrenNames(ITransaction transaction, String folderUri) {
		try {
			ValidityChecker.checkRelativePath(folderUri, true);
		} catch (Exception e) {
			throw new WebdavException("invalid resource URI");
		}
		List<PathInfo> list = fs.listDirectory(transaction.getPrincipal().getName()+"/"+folderUri);
		if (list==null)
			return null;
		ArrayList<String> arr = new ArrayList<String>();
		for (PathInfo pi : list) {
			arr.add(pi.name);
		}
		return arr.toArray(new String[] {});
	}

	@Override
	public long getResourceLength(ITransaction transaction, String path) {
		try {
			ValidityChecker.checkRelativePath(path, true);
		} catch (Exception e) {
			throw new WebdavException("invalid resource URI");
		}
		PathInfo pi = fs.getPathInfo(transaction.getPrincipal().getName()+"/"+path);
		if (pi==null)
			return 0;
		else
			return pi.size;
	}

	@Override
	public void removeObject(ITransaction transaction, String uri) {
		try {
			ValidityChecker.checkRelativePath(uri, false);
		} catch (Exception e) {
			throw new WebdavException("invalid resource URI");
		}
		if (!fs.deletePath(transaction.getPrincipal().getName()+"/"+uri))
			throw new WebdavException("cannot delete file or directory " + uri); 
	}

	@Override
	public StoredObject getStoredObject(ITransaction transaction, String uri) {
		try {
			ValidityChecker.checkRelativePath(uri, true);
		} catch (Exception e) {
			throw new WebdavException("invalid resource URI");
		}
		PathInfo pi = fs.getPathInfo(transaction.getPrincipal().getName()+"/"+uri);
		if (pi==null)
			return null;
		
		StoredObject retVal = new StoredObject();
		retVal.setFolder(pi.isDirectory);
		retVal.setCreationDate(new Date(pi.modified)); // !!!TODO
		retVal.setLastModified(new Date(pi.modified));
		retVal.setResourceLength(pi.size);

        return retVal;		
	}

}
