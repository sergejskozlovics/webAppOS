package org.webappos.antiattack;

import java.io.File;

import org.webappos.properties.WebAppProperties;
import org.webappos.properties.PropertiesManager;

public class ValidityChecker {
	
	// For developers: All exception messages MUST NOT contain provided argument values! Use simple non-concatenated strings.
	// Arguments, if attached to a message, could be printed out in the user's browser by the caller. 
	// This can compromise security!
	
	public static void checkLogin(String login, boolean allow_standalone) throws Exception {
		checkLogin(null, login, allow_standalone);
	}
	
	
	
	public static void checkLogin(String email, String login, boolean allow_standalone) throws Exception {
		if ((login==null) || (login.isEmpty()))
			throw new Exception("Invalid login format");
				
		if (email!=null)
			checkLogin(null, email, false); // check email as login
		
		if (login.equals("standalone")) {
			if (!allow_standalone)
				throw new Exception("Invalid login format");
		}
		
		if (login.indexOf(' ')>=0)
			throw new Exception("Invalid login format: spaces are not allowed"); // spaces are not allowed
		
		checkFileNameLike(login, "Invalid login format: login cannot be used for directory name"); // login can be used as subfolder name
		
		int i=0;
		for (i=0; i<login.length(); i++) {
			char c=login.charAt(i); 
			if (c=='@') {
				if ((email==null)||(email.isEmpty()))
					continue; // if email not specified, never mind;
				if (login.equals(email))
					return; // @ is already allowed if login equals email
				else
					throw new Exception("Invalid login format");
			}
			else
			if (c=='.') {
				if ((i>0) && (login.charAt(i-1)=='.')) {
					throw new Exception("Invalid login format"); // don't allow two sequential dots
				}
			}
			else
			if (Character.isAlphabetic(c)||Character.isDigit(c)||(c=='_')||(c=='-'))
				; // ok
			else
				throw new Exception("Invalid login format");
		}		
	}
	
	public static void checkEmail(String email) throws Exception {		
	    if (email==null) 
	    	throw new Exception("E-mail must be specified");
	    
	    email = email.trim();
	    
	    if (email.isEmpty())
	    	throw new Exception("E-mail must be specified");
	    
    	int i = email.indexOf('@');
    	int ii = email.lastIndexOf('@');
    	int j = email.lastIndexOf('.');
    	if ((i<=0) || (j<0) || (i+2>=j) || (j==email.length()) || (i<ii))
	    	throw new Exception("Unsupported e-mail format");		
    	
		for (i=0; i<email.length(); i++) {
			char c=email.charAt(i);
			if (c=='.') {
				if ((i>0) && (email.charAt(i-1)=='.'))
					throw new Exception("Unsupported e-mail format"); // don't allow two sequential dots
			}
			else
			if (Character.isAlphabetic(c)||Character.isDigit(c)||(c=='_')||(c=='-')||(c=='@'))
				continue;
			else
				throw new Exception("Unsupported e-mail format");
		}
	}
	
	public static void checkTokenLike(String value, String message, boolean allowEmpty) throws Exception {
		if (!allowEmpty) {
			if (value==null)
				throw new Exception(message);
			value=value.trim();
			if (value.isEmpty())
				throw new Exception(message);
		}
	    	    
		for (int i=0; i<value.length(); i++) {
			char c=value.charAt(i); 
			if (Character.isAlphabetic(c)||Character.isDigit(c)||(c=='-')||(c=='_'))
				continue;
			else
				throw new Exception(message);
		}
	}
	
	public static void checkToken(String value) throws Exception {
		checkTokenLike(value, "Ivalid token", false);
	}

	static void checkFileNameLike(String name, String message) throws Exception {
		if ((name==null) || (name.indexOf('/')>=0) || (name.indexOf('\\')>=0))			
			throw new Exception(message);
		try {
			checkRelativePath(name, false); // don't allow empty
		}
		catch(Throwable t) {
			throw new Exception(message);
		}
	}
	
	public static void checkFileName(String name) throws Exception {
		checkFileNameLike(name, "Unsupported file/folder name");
	}
	
	public static void checkRelativePath(String path, boolean allowEmpty) throws Exception {
		if (path==null)
			throw new Exception("Path must be specified");
		if (path.isEmpty()&&!allowEmpty)
			throw new Exception("Path must be specified");
		for (int i=0; i<path.length(); i++) {
			char c=path.charAt(i);
			if (c=='.') {
				if ((i>0) && (path.charAt(i-1)=='.'))
					throw new Exception("Unsupported path format"); // don't allow two sequential dots
			}
			else
			if (Character.isAlphabetic(c)||Character.isDigit(c)||(c=='_')||(c=='-')||(c=='+')||(c=='@')||(c=='/')||(c=='\\')||(c=='.')||(c==',')||(c==' ')||(c=='(')||(c==')'))
				continue;
			else
				throw new Exception("Unsupported path format "+path);
		}
		// $ and & were not allowed already;
		// we also don't allow ":" and ";" (for Windows this will be relative path, thus ":" after disk letter will be accepted when
		// this path is appended to some prefix
		
		while (path.startsWith("/")||path.startsWith("\\"))
			path = path.substring(1);
		while (path.endsWith("/")||path.endsWith("\\"))
			path = path.substring(0, path.length()-1);
		
		int j = path.lastIndexOf('/');
		int k = path.lastIndexOf('\\');
		if (k>j)
			j=k;
		String name = path;
		if (j>=0)
			name = path.substring(j+1).toLowerCase();
		
		if ((name.isEmpty()&&!allowEmpty) || name.equals("con")||name.equals("prn")||name.equals("aux")||
				name.equals("com")||name.equals("lpt")||name.startsWith("nul") || name.equals("undefined"))
			throw new Exception("Unsupported path format");
	}
	
	// cacheDir value must be validated before
	public static void checkProjectCacheDirectory(String cacheDir, WebAppProperties appProps) throws Exception {
		if (appProps==null)
			throw new Exception("Bad app");
		// checking for .shm files
		File f = new File(cacheDir);
		if (!f.exists() || !f.isDirectory())
			throw new Exception("Bad cache dir");
		
		if (new File(cacheDir+File.separator+"ar.common.shm").exists() ||
				new File(cacheDir+File.separator+"ar.chars.shm").exists() ||
				new File(cacheDir+File.separator+"ar.actions.shm").exists() ||
				new File(cacheDir+File.separator+"ar.strings.shm").exists()) {
			throw new Exception("Insecure cache directory");
		}
		
		// TODO: validate checksums in subfolders
	}

	public static void checkWebCallActionName(String name) throws Exception {
		checkFileNameLike(name, "Illegal web call action name");
	}
	
}
