package org.webappos.fs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.webappos.server.API;

import java.lang.reflect.Constructor;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class FSDriversManager {

	private static Logger logger =  LoggerFactory.getLogger(FSDriversManager.class);

	private static class FSDriver {
		String className = null;
		Constructor<?> constr = null;
	}
	private static Map<String, FSDriver > fs_drivers = new ConcurrentHashMap<String, FSDriver >(); // driver protocol name -> driver

	synchronized static public Set<String> getSupportedPrefixes() {
		return fs_drivers.keySet();
	}
	/**
	 * Registers a a file system driver, i.e., a Java class implementing the IFileSystem interface and
	 * having a string constructor.
	 * @param prefix the protocol (prefix) part to be used in location URIs to reference remote file systems, e.g., "gdrive" in "gdrive:my-location"
	 * @param className the Java class name implementing the driver
	 */
	synchronized static public void registerFileSystemDriver(String prefix, String className) {
		if ((prefix==null) || prefix.isEmpty())
			return;

		while (prefix.endsWith(":")) {
			prefix = prefix.substring(0, prefix.length()-1);
		}

		FSDriver drv = new FSDriver();
		drv.className = className;

		logger.info("Registering FS driver for protocol \""+prefix+"\" implemented in class "+className);

		fs_drivers.put(prefix, drv); // do not initialize the constructor, since the classpaths might not have been initialized yet
	}

	/**
	 * Initializes and returns a file system driver for the given prefix and remote location combined in the URI.
	 * When being initialized, the driver should take the credentials from the registry key xusers/[login]/[driver_name],
	 * which is initialized when requesting scopes.
	 * @param uri - a remote location with the prefix corresponding to the driver protocol, e.g., "gdrive:my-remote-folder"
	 * @return an initialized file system driver or null on error
	 */
	synchronized public static IFileSystem getFileSystemDriver(String uri) {
		if (uri==null)
			return null;

		int i = uri.indexOf(':');
		if (i<0)
			return null;

		String prefix = uri.substring(0, i);
		String remoteLocation = uri.substring(i+1);

		// TODO: cache

		FSDriver drv = fs_drivers.get(prefix);
		if (drv==null)
			return null;

		if (drv.constr==null) {
			Class<?> cls = API.classLoader.findClassByName(drv.className);
			if (cls==null) {
				fs_drivers.remove(prefix);
				return null;
			}

			try {
				drv.constr = cls.getConstructor(String.class);
			}
			catch(Throwable t) {

			}
		}
		if (drv.constr==null) {
			fs_drivers.remove(prefix);
			return null;
		}

		try {
			Object obj = drv.constr.newInstance(remoteLocation);
			if (obj instanceof IFileSystem)
				return (IFileSystem)obj;
		} catch (Throwable t) {
		}
		return null;
	}

}
