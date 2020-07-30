package org.webappos.service.filebrowser;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.webappos.antiattack.ValidityChecker;
import org.webappos.auth.UsersManager;
import org.webappos.fs.HomeFS;
import org.webappos.fs.IFileSystem.PathInfo;
import org.webappos.server.API;
import org.webappos.server.ConfigEx;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class FileBrowserServlet extends HttpServlet {

	private static Charset utf8_charset = Charset.forName("UTF-8");
	
	private static final long serialVersionUID = 1L;
	
	public static void setProperties(Properties p) { 
		// mimes are added automatically in  ServiceProperties.java		
	}
	
	
	private static String getPathMime(String path, boolean isDirectory) {
		if (!(API.config instanceof ConfigEx))
			return null;
		
		if (isDirectory) {
			String retVal = ((ConfigEx)API.config).mimes.get(path);
			return retVal;
		}
		int j = path.lastIndexOf('/');
		String lastName = path.substring(j+1); // works even when j==-1
		int k = lastName.lastIndexOf('.');
		String mime = null;
		if (k>=0) {
			mime = ((ConfigEx)API.config).mimes.get(lastName.substring(k+1));
		}
		
		return mime;
	}
	
	private boolean matches(String fileName, String mask) {
		if (fileName==null)
			return false;
		if ("*".equals(mask) || "*.*".equals(mask))
			return true;
		int i = mask.indexOf('*');
		if (i<0) {
			return fileName.equals(mask);
		}
		else {
			String sub1 = mask.substring(0, i);
			String sub2 = mask.substring(i+1);
			return fileName.startsWith(sub1) && fileName.endsWith(sub2);
		}
		
	}

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
		
		try {
			String path = request.getPathInfo();
			if (path==null)
				return;
			
			if (path.startsWith("/browse/")) {
				String q = request.getQueryString();
				if ((q==null)||(q.isEmpty()))
					q="?";
				else
					if (q.startsWith("?"))
						q+="&";
					else
						q="?"+q+"&";
				response.sendRedirect("/apps/filebrowser"+q+"path="+path.substring("/browse".length()));
				return;
			}
			
			String authorization = null;
			if (request instanceof HttpServletRequest) { 
				authorization = ((HttpServletRequest)request).getHeader("Authorization");
			}
			
			if (authorization == null) {
				//ValidityChecker.checkRelativePath(path, false); // allow empty relative path
				
				return;
			}
			
			if (!authorization.startsWith("webAppOS_token "))
				return;
			authorization = authorization.substring("webAppOS_token ".length());
			
			final String[] values = authorization.split(":",2);
			if (values.length<2)
				return;
			String login = values[0];
			String token = values[1];
			
/*			while (path.startsWith("/"))
				path = path.substring(1);
			int i = path.indexOf('/');
			if (i<0)
				return;
			String login = path.substring(0, i);
			path = path.substring(i+1);
			
			
			i = path.indexOf('/');
			if (i<0)
				i=path.length();
			String token = path.substring(0, i);
			path = path.substring(i);*/
			
			while (path.startsWith("/"))
				path = path.substring(1);
			
			boolean dirInfo=!path.endsWith("/");
			boolean rootInfo=false;
			if (path.equals("home") || path.isEmpty()) {
				path = "";
				rootInfo=true;
			}
			else {
				// for "//" we will return children
				while (path.endsWith("/"))
					path = path.substring(0, path.length()-1);
				
				while (path.startsWith("/"))
					path = path.substring(1);
				
				if (path.startsWith("home"))
					path = path.substring(4);
				
				while (path.startsWith("/"))
					path = path.substring(1);
			}
			
			
			
			// path doesn't start with "/" and doesn't end with "/"
			
			ValidityChecker.checkLogin(login, false);
			ValidityChecker.checkToken(token);
			ValidityChecker.checkRelativePath(path, true); // allow empty relative path
			
			if (!UsersManager.ws_token_OK(login, token, true))
				return;
						
			PathInfo info = HomeFS.ROOT_INSTANCE.getPathInfo(login+"/"+path);
			if (info==null)
				return;
			
			response.setCharacterEncoding("UTF-8");
			String retVal;
			if (rootInfo) {
				retVal="{\"id\":\"home\",\"dir\":"+rootInfo+",\"name\":\""+"My home"/*info.name*/+"\",\"size\":"+0+",\"cre\":"+info.modified+",\"mod\":"+info.modified+"}";
				response.getOutputStream().print(retVal);				
			}
			else if (dirInfo && info.isDirectory) {
				int j = path.lastIndexOf('/');
				
				String mime = getPathMime(path, true);
				String parId;
				if (j>=0)
					parId = "\\/home\\/"+path.substring(0, j).replace("/", "\\/");
				else
					parId = "\\home";
				retVal="{\"id\":\"\\/home\\/"+path.replace("/", "\\/")+"\",\"dir\":"+true+",\"parId\":\""+parId+"\",\"name\":\""+info.name+"\",\"size\":"+0+",\"cre\":"+info.modified+",\"mod\":"+info.modified+(mime==null?"":",\"mime\":\""+mime.replace("/", "\\/")+"\"")+"}";
				response.getOutputStream().print(retVal);									
			}
			else if (!info.isDirectory) {
				int j = path.lastIndexOf('/');
				
				String mime = getPathMime(path, false);
				
				String parId;
				if (path.isEmpty())
					parId = "\\/home";
				else
					parId = "\\/home\\/"+path.substring(0, j).replace("/", "\\/");

				
				retVal="{\"id\":\"\\/home\\/"+path.replace("/", "\\/")+"\",\"dir\":"+rootInfo+",\"parId\":\""+parId+"\",\"name\":\""+info.name+"\",\"size\":"+info.size+",\"cre\":"+info.modified+",\"mod\":"+info.modified+(mime==null?"":",\"mime\":\""+mime.replace("/", "\\/")+"\"")+"}";
				response.getOutputStream().print(retVal);
			}
			else {
				// listing directory
				List<PathInfo> list = HomeFS.ROOT_INSTANCE.listDirectory(login+"/"+path);
				if (list==null)
					return;
				
				String q = request.getQueryString();
				if ((q==null)||(q.isEmpty()))
					q="*";
				String[] arr = q.split(",");

				response.getOutputStream().print("[");
				boolean was=false;
				for (PathInfo pi : list) {
					if (!pi.isDirectory) {
						// matching...
						boolean matchOK = false;
						for (String mask : arr) {
							if (matches(pi.name, mask)) {
								matchOK = true;
								break;
							}
						}
						
						if (!matchOK)
							continue; // doesn't match
					}
					
					String item;
					String mime = null;
					mime = getPathMime(path+"/"+pi.name, pi.isDirectory);
					if (path.isEmpty())
						item="{\"id\":\""+"\\/home\\/"+pi.name+"\",\"dir\":"+pi.isDirectory+",\"parId\":\"\\/home\",\"name\":\""+pi.name+"\",\"size\":"+(pi.isDirectory?0:pi.size)+",\"cre\":"+info.modified+",\"mod\":"+pi.modified+(mime==null?"":",\"mime\":\""+mime.replace("/", "\\/")+"\"")+"}";
					else {
						item="{\"id\":\""+"\\/home\\/"+path.replace("/", "\\/")+"\\/"+pi.name+"\",\"dir\":"+pi.isDirectory+",\"parId\":\""+"\\/home\\/"+path.replace("/", "\\/")+"\",\"name\":\""+pi.name+"\",\"size\":"+pi.size+",\"cre\":"+pi.modified+",\"mod\":"+pi.modified+(mime==null?"":",\"mime\":\""+mime.replace("/", "\\/")+"\"")+"}";
					}
					if (was)
						response.getOutputStream().print(",");
					response.getOutputStream().print(item);
					was = true;
				}
				response.getOutputStream().print("]");
				
			}
		
		
			
		}
		catch(Throwable t) {
			return;
		}
		
	}

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
		try {
			String authorization = null;
			if (request instanceof HttpServletRequest) { 
				authorization = ((HttpServletRequest)request).getHeader("Authorization");
			}
			
			if (authorization == null) {
				//ValidityChecker.checkRelativePath(path, false); // allow empty relative path
				
				return;
			}
			
			if (request.getCharacterEncoding()!=null)
				if (!"utf-8".equalsIgnoreCase(request.getCharacterEncoding()))
					throw new RuntimeException("Bad/unsupported character encoding");
			
			if (!authorization.startsWith("webAppOS_token "))
				return;
			authorization = authorization.substring("webAppOS_token ".length());
			
			final String[] values = authorization.split(":",2);
			if (values.length<2)
				return;
			String login = values[0];
			String token = values[1];
	
			ValidityChecker.checkLogin(login, false);
			ValidityChecker.checkToken(token);
			
			if (!UsersManager.ws_token_OK(login, token, true))
				return;
			
			
			String data = IOUtils.toString(request.getInputStream(), utf8_charset);
			
			JsonParser parser = new JsonParser();
            JsonElement jsonElement = parser.parse(data);
            JsonObject json = jsonElement.getAsJsonObject();
            
            JsonElement dir = json.get("dir");
        	String parent = json.get("parId").getAsString();
        	String name = json.get("name").getAsString();
        	
        	if (!parent.startsWith("/home/") && !parent.equals("/home")) {
        		throw new RuntimeException("invalid parent path");            		
        	}        	
        	String parent2 = login + parent.substring("/home".length());            
            
            if (dir!=null && dir.getAsBoolean()) {
            	// directory
                        	            	
            	String newDir = parent+"/"+name; // for id, starting with "/home"
            	String newDir2 = parent2+"/"+name; // real path, starting with "/<login>"
            	int i = 0;
            	while (HomeFS.ROOT_INSTANCE.pathExists(newDir2)) {
            		i++;
            		newDir = parent + "/"+name+" "+i;
            		newDir2 = parent2+"/"+name+" "+i;
            		json.addProperty("name", name+" "+i);
            	}
            	
            	if (!HomeFS.ROOT_INSTANCE.createDirectory(newDir2))
            		throw new RuntimeException("directory not created");
            	
            	PathInfo pi = HomeFS.ROOT_INSTANCE.getPathInfo(newDir2);
            	if (pi==null)
            		throw new RuntimeException("directory not created");
            	json.addProperty("id", newDir);            	
            	json.addProperty("cre", pi.modified);
            	json.addProperty("mod", pi.modified);
            	response.getOutputStream().print(json.toString());
            }
            else {
            	// file
            	
            	String ext = "";
            	int i = name.lastIndexOf('.');
            	if (i>=0) {
            		ext = name.substring(i);
            		name = name.substring(0, i);
            	}
            	
            	String newFile = parent+"/"+name+ext; // for id, starting with "/home"
            	String newFile2 = parent2+"/"+name+ext; // real path, starting with "/<login>"
            	i = 0;
            	while (HomeFS.ROOT_INSTANCE.pathExists(newFile2)) {
            		i++;
            		newFile = parent + "/"+name+" "+i+ext;
            		newFile2 = parent2+"/"+name+" "+i+ext;
            		json.addProperty("name", name+" "+i+ext);
            	}
            	
            	
            	if (!HomeFS.ROOT_INSTANCE.uploadFile(newFile2, IOUtils.toInputStream("",utf8_charset), 0, true))
            		throw new RuntimeException("file not created");
            	
            	PathInfo pi = HomeFS.ROOT_INSTANCE.getPathInfo(newFile2);
            	if (pi==null)
            		throw new RuntimeException("file not created");
            	json.addProperty("id", newFile);            	
            	json.addProperty("cre", pi.modified);
            	json.addProperty("mod", pi.modified);
            	response.getOutputStream().print(json.toString());
            }
            
		}
		catch(Throwable t) {	
			response.getOutputStream().print("false");
		}
	
	}
	
	@Override
	public void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
		try {
			String authorization = null;
			if (request instanceof HttpServletRequest) { 
				authorization = ((HttpServletRequest)request).getHeader("Authorization");
			}
			
			if (authorization == null) {
				//ValidityChecker.checkRelativePath(path, false); // allow empty relative path
				
				return;
			}
			
			if (request.getCharacterEncoding()!=null)
				if (!"utf-8".equalsIgnoreCase(request.getCharacterEncoding()))
					throw new RuntimeException("Bad/unsupported character encoding");
			
			if (!authorization.startsWith("webAppOS_token "))
				return;
			authorization = authorization.substring("webAppOS_token ".length());
			
			final String[] values = authorization.split(":",2);
			if (values.length<2)
				return;
			String login = values[0];
			String token = values[1];
	
			ValidityChecker.checkLogin(login, false);
			ValidityChecker.checkToken(token);
			
			if (!UsersManager.ws_token_OK(login, token, true))
				return;
			
			
			String data = IOUtils.toString(request.getInputStream(), utf8_charset);
			
			JsonParser parser = new JsonParser();
            JsonElement jsonElement = parser.parse(data);
            JsonObject json = jsonElement.getAsJsonObject();

            String id = request.getPathInfo();//json.get("id").getAsString();
            while (id.startsWith("/"))
            	id = id.substring(1);
            id = "/"+id;
        	String parent = json.get("parId").getAsString();
        	String name = json.get("name").getAsString();
        	
        	if (!id.startsWith("/home/") && !id.equals("/home")) {
        		throw new RuntimeException("invalid id path");            		
        	}        	
        	if (!parent.startsWith("/home/") && !parent.equals("/home")) {
        		throw new RuntimeException("invalid parent path");            		
        	}        	
        	String id2 = login + id.substring("/home".length());
        	String parent2 = login + parent.substring("/home".length());
        	
        	
        	// id -> parId + "/" + name
        	if (!HomeFS.ROOT_INSTANCE.renamePath(id2, parent2+"/"+name))
        		throw new RuntimeException("rename/move error");
        	
            
           	PathInfo pi = HomeFS.ROOT_INSTANCE.getPathInfo(parent2+"/"+name);
            if (pi==null)
            	throw new RuntimeException("rename/move error");
            
//            json.addProperty("id", parent+"/"+name);            	
            json.addProperty("mod", pi.modified);
            
           	response.getOutputStream().print(json.toString());            
		}
		catch(Throwable t) {	
			response.getOutputStream().print("false");
		}
	
	}
	
	@Override
	public void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
		try {
			String path = request.getPathInfo();
			if (path==null)
				return;
			while (path.startsWith("/"))
				path = path.substring(1);
	
			if (path.equals("home"))
				return; // cannot delete the root directory
			
			if (path.startsWith("home/"))
					path = path.substring(5);
			
			
			String authorization = null;
			if (request instanceof HttpServletRequest) { 
				authorization = ((HttpServletRequest)request).getHeader("Authorization");
			}
			
			if (authorization == null) {
				//ValidityChecker.checkRelativePath(path, false); // allow empty relative path
				
				return;
			}
			
			if (!authorization.startsWith("webAppOS_token "))
				return;
			authorization = authorization.substring("webAppOS_token ".length());
			
			final String[] values = authorization.split(":",2);
			if (values.length<2)
				return;
			String login = values[0];
			String token = values[1];
	
			ValidityChecker.checkLogin(login, false);
			ValidityChecker.checkToken(token);
			ValidityChecker.checkRelativePath(path, false); // do not allow empty relative path - cannot delete the root
			
			if (!UsersManager.ws_token_OK(login, token, true))
				return;
			
			boolean b = HomeFS.ROOT_INSTANCE.deletePath(login+"/"+path);

			if (b)
				response.getOutputStream().print("[{\"msg\": \"item deleted\"}]");
			else
				response.getOutputStream().print("false");
		}
		catch(Throwable t) {	
		}
	}
	
}
