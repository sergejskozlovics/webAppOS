package org.webappos.service.fileupload;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URLDecoder;
import java.nio.file.StandardCopyOption;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.webappos.antiattack.ValidityChecker;
import org.webappos.auth.UsersManager;
import org.webappos.fs.HomeFS;
import org.webappos.server.API;


@SuppressWarnings( "serial" )
public class FileUploadServlet extends HttpServlet
{
	
	private static Logger logger =  LoggerFactory.getLogger(FileUploadServlet.class);
	
	private String storeFile(String folder, String fileName, InputStream inputStream, long size, boolean overwrite)
	// returns new (upload) file name
	{
		try {
			String uploadFileName = fileName;
			
			if (!overwrite) {
		    	if (HomeFS.ROOT_INSTANCE.pathExists(folder+File.separator+uploadFileName)) {
		        	int i;
		        	for (i=1;;i++) {
		        		int j = fileName.lastIndexOf('.');
		        		if (j<0)
		        			j = fileName.length();
		        		
		        		uploadFileName = fileName.substring(0, j) + " (upload "+i+")"+fileName.substring(j);
		        		if (!HomeFS.ROOT_INSTANCE.pathExists(folder+File.separator+uploadFileName))
		        			break;
		        	}
		    	}
			}
	    
	    	try {
	    		boolean b = HomeFS.ROOT_INSTANCE.uploadFile(folder+"/"+uploadFileName, inputStream, size, overwrite);
	    		if (!b)
	    			return null;
	    	}
	    	catch (Throwable t) {
	    		throw t;
	    	}
	    	
			return uploadFileName;			
		}
		catch (Throwable t) {
			return null;
		}
		finally {
			try {
				inputStream.close();
			} catch (IOException e) {
			}			
		}
	}
	

	private String storeProjectFile(String projectFolder, String fileName, InputStream inputStream, boolean overwrite)
	// returns new (upload) file name
	{
		try {
			String uploadFileName = fileName;
			
			if (!overwrite) {
				File f = new File(projectFolder+File.separator+uploadFileName);
		    	if (f.exists()) {
		        	int i;
		        	for (i=1;;i++) {
		        		int j = fileName.lastIndexOf('.');
		        		if (j<0)
		        			j = fileName.length();
		        		
		        		uploadFileName = fileName.substring(0, j) + " (upload "+i+")"+fileName.substring(j);
		        		f = new File(projectFolder+File.separator+uploadFileName);
		        		if (!f.exists())
		        			break;
		        	}
		    	}
			}
	    
	    	try {
	    		File f = new File(projectFolder+File.separator+uploadFileName);	    		
	    		java.nio.file.Files.copy(inputStream, f.toPath(), StandardCopyOption.REPLACE_EXISTING);
	    	}
	    	catch (Throwable t) {
	    		throw t;
	    	}
	    	
			return uploadFileName;			
		}
		catch (Throwable t) {
			return null;
		}
		finally {
			try {
				inputStream.close();
			    //IOUtils.closeQuietly(inputStream);
			} catch (IOException e) {
			}			
		}
	}

	protected void doPost( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException
    {
        
        FileItemFactory factory = new DiskFileItemFactory();
        ServletFileUpload upload = new ServletFileUpload(factory);
        upload.setHeaderEncoding("UTF-8"); 
        List<FileItem> items;        
        
        StringBuffer uploaded = new StringBuffer();
        StringBuffer notUploaded = new StringBuffer();
		try {
			
			items = upload.parseRequest(request);
        
	        String targetDir = request.getPathInfo();
	        if (targetDir == null)
	        	targetDir = "";
	        
	        while (targetDir.startsWith("/"))
	        	targetDir = targetDir.substring(1);
	        
	        ValidityChecker.checkRelativePath(targetDir, false);
	        
			String login=null, ws_token=null, onready=null, project_id=null;
			
			// checking for attributes in query string...
			String qs = request.getQueryString();			
			if (qs!=null) {
				qs =  URLDecoder.decode(qs, "UTF-8");
				String[] arr2 = qs.split("&");
				for (String s : arr2) {
					if (s.startsWith("login=")) {
						login = s.substring("login=".length());
					}
					else
					if (s.startsWith("ws_token=")) {
						ws_token = s.substring("ws_token=".length());
					}
					else
					if (s.startsWith("onready=")) {
						onready = s.substring("onready=".length());
					}
					else
					if (s.startsWith("project_id=")) {
						project_id = s.substring("project_id=".length());
					}
				}
			}
	        
			ValidityChecker.checkLogin(login, false);
			ValidityChecker.checkToken(ws_token);
			
	        String projectFolder = null;
	        if (project_id!=null) {
	        	ValidityChecker.checkRelativePath(project_id, false);
	        	if (!project_id.startsWith(login+"/"))
	        		throw new RuntimeException("Project not owned");
	        	projectFolder = API.dataMemory.getProjectFolder(project_id);
	        	if (projectFolder == null)
	        		throw new RuntimeException("Project not active");
	        	
	        	if (targetDir.equals(project_id))
	        		targetDir = "";
	        	else
	        	if (targetDir.startsWith(project_id+"/"))
	        		targetDir = targetDir.substring(project_id.length()+1);
	        	else
	        		throw new RuntimeException("Invalid target path");
	        }
	        else {
	        	if (!targetDir.equals(login) && !targetDir.startsWith(login+"/"))
	        		throw new RuntimeException("Invalid target path");
	        	ValidityChecker.checkRelativePath(targetDir, false);
	        }
			if (!UsersManager.ws_token_OK(login, ws_token, true))						
				throw new RuntimeException("Login/token invalid");
            
	        if (!ServletFileUpload.isMultipartContent(request))
	        	throw new RuntimeException("This is not a multipart request.");
	        
	        
	        
	        Iterator<FileItem> iterator = items.iterator();
        	String customFileName = null;

	        
            while (iterator.hasNext()) {
            	
                FileItem item = iterator.next();
                
            	if ("custom_file_name".equals(item.getFieldName())) {
            		customFileName = item.getString();
            		continue;
            	}
                
                    String fileName = item.getName();
                                        
                    if (customFileName!=null) {
                    	fileName = customFileName;
                    	customFileName = null;
                    }
                    
                    if ("".equals(fileName))
                		continue;// throw new RuntimeException("Skipped "+item.getName());
                    
        	        ValidityChecker.checkFileName(fileName);        	    
        	        
        	        if (projectFolder!=null)
        	        	fileName = storeProjectFile(projectFolder+File.separator+targetDir, fileName, item.getInputStream(), true); // overwriting...
        	        else        	        	
        	        	fileName = storeFile(targetDir, fileName, item.getInputStream(), item.getSize(), false); // not overwriting...
                	
	                	
                	if (fileName == null) {
                		if (notUploaded.length()>0)
                    		notUploaded.append(',');
                		notUploaded.append("\""+item.getName()+"\"");
                	}
                	else {
                    	if (uploaded.length()>0)
                    		uploaded.append(',');
                    	if (fileName.equals(item.getName()))
                    		uploaded.append( "{ \"name\": \""+item.getName()+"\"} ");
                    	else
                    		uploaded.append( "{ \"name\": \""+item.getName()+"\", \"newName\": \""+fileName+"\" }");
                	}

	        
	        } // while
            
            if (notUploaded.length()==1)
            	throw new RuntimeException("File "+notUploaded.toString()+" was not uploaded.");
            else
            	if (notUploaded.length()>1) {
            		if (uploaded.length()>0)
            			throw new RuntimeException("Some files were not uploaded: "+notUploaded.toString()+".");
            		else
            			throw new RuntimeException("Upload failed.");
            	}
	        
            if ("resetcontent".equals(onready))
            	response.setStatus(HttpServletResponse.SC_RESET_CONTENT);
            else
            if ("nocontent".equals(onready))
            	response.setStatus(HttpServletResponse.SC_NO_CONTENT);
            else {
            	response.setCharacterEncoding("UTF-8");
            	response.setContentType("application/json");
            	response.getOutputStream().print("{\"uploaded\": ["+uploaded.toString()+"]}");
            }
            
		} catch (Throwable t) {
			logger.error(t.getMessage());
			if (logger.isTraceEnabled()) {
				StringWriter errors = new StringWriter();
				t.printStackTrace(new PrintWriter(errors));
				logger.trace(errors.toString());
			}
			
			String msg = t.getMessage();
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			if (msg == null)
				response.getOutputStream().print("{\"error\":\""+t.toString()+"\"");
			else
				response.getOutputStream().print("{\"error\":\""+msg+"\"");
			if (uploaded.length()>0)
				response.getOutputStream().print(",\"uploaded\": ["+uploaded.toString()+"]");
			response.getOutputStream().println("}");
		}
			        
    }
 
	protected void doGet ( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException {
        response.sendRedirect("https://webappos.org/dev/doc/files/API_Specifications/APIs_of_Bundled_Apps___Services/FileUpload_Service_API-txt.html");
	}
}