package org.webappos.service.status;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.webappos.antiattack.ValidityChecker;
import org.webappos.auth.UsersManager;
import org.webappos.properties.AppProperties;
import org.webappos.server.API;
import org.webappos.server.ConfigStatic;
import org.webappos.webcaller.IWebCaller;
import org.webappos.webcaller.IWebCaller.WebCallDeclaration;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import lv.lumii.tda.kernel.TDAKernel;



@SuppressWarnings( "serial" )
public class StatusServlet extends HttpServlet
{
	private static Logger logger =  LoggerFactory.getLogger(StatusServlet.class);
	private static Charset utf8_charset = Charset.forName("UTF-8");
	
	
/*	public static void setProperties(Properties p) { // file service.properties for the given webAppOS service
		properties = p;
	}*/
	
	
	/**
	 * In accordance with the Post-Redirect-Get pattern, we do not write
	 * the response text directly, but redirect to certain HTML 
	 * (see https://en.wikipedia.org/wiki/Post/Redirect/Get).
	 * 
	 * @param fullAppName - the full name of the app used to make this web call
	 * @param app_url_name - the url name of the app used to make this web call
	 * @param htmlName - HTML file name w/o extension; the file should contain the "<!--[field-name]-->" comments to be replaced with actual values
	 * @param obj - the JSON object, which fields will be used to replace the <!--[field-name]--> comments
	 * @param response - the response stream
	 * @param redirect - whether to redirect or just write to the output stream
	 * @throws IOException
	 */
	private void pipeToHTML(String fullAppName, String app_url_name, String htmlName, JsonObject obj, HttpServletResponse response, boolean redirect) throws IOException {
		
		if (obj==null || fullAppName==null) {
			if (app_url_name == null)
				response.sendRedirect(htmlName); // direct redirect
			else
				response.sendRedirect("/apps/"+app_url_name+"/"+htmlName+".html");
			return;
		}
		
		
		
		try {
			String s = new String(Files.readAllBytes(Paths.get(ConfigStatic.APPS_DIR+File.separator+fullAppName+File.separator+"web-root"+File.separator+htmlName+".html")), "UTF-8");
			String orig = s;
			for (String key : obj.keySet())
				s = s.replace("<!--"+key+"-->", obj.get(key).getAsString());
			
			if (orig.equals(s)) {
				// nothing was replaced; redirecting to the original file
				if (redirect)
					response.sendRedirect("/apps/"+app_url_name+"/"+htmlName+".html");
				else
					response.getOutputStream().println(s);
			}
			else {
				if (redirect) {
					int hash = s.hashCode();
					File f = new File(ConfigStatic.WEB_ROOT_CACHE_DIR+File.separator+app_url_name+".cache"+File.separator+htmlName+hash+".html");
					if (!f.exists()) {
						f.getParentFile().mkdirs();
						Files.write(f.toPath(), s.getBytes(utf8_charset));
					}
					if (f.exists())
						response.sendRedirect("/apps/"+app_url_name+"/"+app_url_name+".cache/"+htmlName+hash+".html");
					else
						response.sendRedirect("/apps/"+app_url_name+"/"+htmlName+".html");					
				}
				else
					response.getOutputStream().println(s);
			}
			
		}
		catch(Throwable tt) {				
			response.sendRedirect("/apps/"+app_url_name+"/"+htmlName+".html");					
		}
		
	}
	
	public void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
		
        try {        	
            
			if (request.getCharacterEncoding()!=null)
				if (!"utf-8".equalsIgnoreCase(request.getCharacterEncoding()))
					throw new RuntimeException("Bad/unsupported character encoding");
						
			
			String login=null, ws_token=null;
			
			// checking for attributes in query string...
			String qs = request.getQueryString();
			if (qs==null)
				throw new RuntimeException("Unauthenticated");
			else {
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
				}
			}
			
			if (!UsersManager.ws_token_OK(login, ws_token, true))						
				throw new RuntimeException("Login/token invalid");
			
			if (!UsersManager.userInGroup(login, "admin"))
				throw new RuntimeException("Not an admin");
						
			// sending result without pipes..
			response.setContentType("application/json");
			response.getOutputStream().println(API.status.getValue("").toString());
		}
		catch(Throwable t) {
			logger.error(t.getMessage());
			t.printStackTrace();
			if (logger.isTraceEnabled()) {
				StringWriter errors = new StringWriter();
				t.printStackTrace(new PrintWriter(errors));
				logger.trace(errors.toString());
			}
			String msg = t.getMessage();
			if (msg == null)
				msg = t.toString();
			
			JsonObject result = new JsonObject();
			result.addProperty("error", msg);

			response.setContentType("application/json");			
			response.getOutputStream().println(result.toString());
		}
    }
	
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {		
		processRequest(request, response);
    }
		
	
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
		processRequest(request, response);
    }
		
	
}