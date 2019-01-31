package org.webappos.service.filedownload;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URLDecoder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.webappos.antiattack.ValidityChecker;
import org.webappos.auth.UsersManager;
import org.webappos.fs.HomeFS;
import org.webappos.memory.MRAM;
import org.webappos.server.API;


@SuppressWarnings( "serial" )
public class FileDownloadServlet extends HttpServlet
{
	private static Logger logger =  LoggerFactory.getLogger(FileDownloadServlet.class);
	
	public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
		
		try {
			
	        String pathToDownload = request.getPathInfo();
	        if (pathToDownload == null)
	        	pathToDownload = "";
	        
	        if (pathToDownload.startsWith("/"))
	        	pathToDownload = pathToDownload.substring(1);
	        
	        ValidityChecker.checkRelativePath(pathToDownload, false);
	        
			String login=null, ws_token=null, project_id=null;
			
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
					if (s.startsWith("project_id=")) {
						project_id = s.substring("project_id=".length());
					}
				}
			}
	        
			ValidityChecker.checkLogin(login, false);
			ValidityChecker.checkToken(ws_token);
			
			if (!UsersManager.ws_token_OK(login, ws_token, true))						
				throw new RuntimeException("Login/token invalid");
			// TODO: validate access token if downloading from another user
			
	        String projectFolder = null;
	        if (project_id!=null) {
	        	ValidityChecker.checkRelativePath(project_id, false);
	        	if (!project_id.startsWith(login+"/"))
	        		throw new RuntimeException("Project not owned");
	        	projectFolder = API.memory.getProjectFolder(project_id);
	        	if (projectFolder == null)
	        		throw new RuntimeException("Project not active");
	        	
	        	if (!pathToDownload.startsWith(project_id+"/"))
	        		throw new RuntimeException("Invalid path to download");
	        	pathToDownload = pathToDownload.substring(project_id.length()+1);
	        	
	        	if (pathToDownload.isEmpty())
	        		throw new RuntimeException("Invalid path to download");
	        	
	        	File f = new File(projectFolder + File.separator + pathToDownload);
	        	if (!f.exists())
	        		throw new RuntimeException("Invalid path to download");
	        	
	        	InputStream is = null;
	        	OutputStream out = null;
	        	try {
		        	is = new FileInputStream(f);
		        	response.setContentType("application/octet-stream");
		        	out = response.getOutputStream();
		            IOUtils.copy(is, response.getOutputStream());
	        	}
	            catch(Throwable t) {
	            	
	            }
		        finally {
		        	if (is!=null)
		        		try { is.close(); } catch (Throwable t) {} 
		        	if (out!=null)
		        		try { out.close(); } catch (Throwable t) {} 
		        }
	            
	        }
	        else {
				if (!pathToDownload.startsWith(login+"/"))
					throw new RuntimeException("Invalid path to download");
				if (!HomeFS.ROOT_INSTANCE.pathExists(pathToDownload))
					throw new RuntimeException("The requested path ("+pathToDownload+") not found.");
		                
		    	
		        java.io.InputStream is = null;
		        OutputStream out = null;
		
		        try {
		        	is = HomeFS.ROOT_INSTANCE.downloadFile(pathToDownload);
	            	response.setContentType("application/octet-stream");
	            	
	            	//TODO: guess content-type
		            // String mimeType = getServletContext().getMimeType(f.getAbsolutePath());
		            // response.setContentType(mimeType);
		            out = response.getOutputStream();
		            IOUtils.copy(is, out);
		        }
		        catch (Throwable t) {
		        	
		        }
		        finally {
		        	if (is!=null)
		        		try { is.close(); } catch (Throwable t) {} 
		        	if (out!=null)
		        		try { out.close(); } catch (Throwable t) {} 
		        }
	        }

		}
		catch (Throwable t) {
			logger.error(t.getMessage());
			t.printStackTrace();
			if (logger.isTraceEnabled()) {
				StringWriter errors = new StringWriter();
				t.printStackTrace(new PrintWriter(errors));
				logger.trace(errors.toString());
			}
			String msg = t.getMessage();
			if (msg == null)
				response.getOutputStream().println("{\"error\":\"+"+t.toString()+"\"");
			else
				response.getOutputStream().println("{\"error\":\"+"+msg+"\"");			
		}
    }	
}