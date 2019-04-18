package org.webappos.service.webcalls;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.concurrent.CompletableFuture;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.webappos.antiattack.ValidityChecker;
import org.webappos.auth.UsersManager;
import org.webappos.server.API;
import org.webappos.webcaller.IWebCaller;

import lv.lumii.tda.kernel.TDAKernel;



@SuppressWarnings( "serial" )
public class WebCallsServlet extends HttpServlet
{
	private static Logger logger =  LoggerFactory.getLogger(WebCallsServlet.class);
	private static Charset utf8_charset = Charset.forName("UTF-8");
	
	
/*	public static void setProperties(Properties p) { // file service.properties for the given webAppOS service
		properties = p;
	}*/
	
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
		response.setContentType("application/json");
		
        String action = request.getPathInfo();        

        try {        	
            if ((action == null) || (action.isEmpty())) {
            	throw new RuntimeException("No webcall action specified");            	
            }
            
            if (action.startsWith("/"))
            	action = action.substring(1);
            
            ValidityChecker.checkActionName(action);
            
			if (request.getCharacterEncoding()!=null)
				if (!"utf-8".equalsIgnoreCase(request.getCharacterEncoding()))
					throw new RuntimeException("Bad/unsupported character encoding");
			
//			if (!"application/x-www-form-urlencoded".equals(request.getContentType()))
//				throw new RuntimeException("Bad enctype");
			
			if (!"application/json".equals(request.getContentType()))
				throw new RuntimeException("Bad enctype");
			
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
						
			TDAKernel kernel = null;
			
			if (login != null) {			
				ValidityChecker.checkLogin(login, false);
				ValidityChecker.checkToken(ws_token);
				if (project_id!=null) {
					ValidityChecker.checkRelativePath(project_id, false);				
					if (!UsersManager.projectOK(project_id, login, ws_token, true))
						throw new RuntimeException("Login/token invalid");
				}
				else {
					if (!UsersManager.ws_token_OK(login, ws_token, true))						
						throw new RuntimeException("Login/token invalid");
				}
				
				if (project_id!=null) {
					kernel = API.dataMemory.getTDAKernel(project_id);				
					if (kernel == null)
						throw new RuntimeException("Project not active");
					
										
					// validate project content...
					// !!!TODO: validate projectDir
					// validate other system-specific settings, e.g., language, country codes...

				}
			}
			
			
			String data = IOUtils.toString(request.getInputStream(), utf8_charset);
			
			// TODO: limit data length
        	
			String retVal;
			IWebCaller.WebCallDeclaration decl = API.webCaller.getWebCallDeclaration(action);
			if (decl == null)
				retVal = "{\"error\":\"Webcall action not found.\"}";
			else {
				if (decl.callingConventions == IWebCaller.CallingConventions.JSONCALL) {
					try {
						IWebCaller.WebCallSeed seed = new IWebCaller.WebCallSeed();
						seed.actionName = action;
						seed.jsonArgument = data;
						seed.login = login;
						seed.project_id = project_id;
						seed.jsonResult = new CompletableFuture<String>();
						API.webCaller.enqueue(seed);
						retVal = seed.jsonResult.get();
					}
					catch(Throwable t) {
						String msg = t.getMessage();
						t.printStackTrace();
						if (msg != null)
							retVal = "{\"error\":\""+msg+"\"}";
						else
							retVal = "{\"error\":\"Could not perform webcall action "+action+"\"}";
		        	}
				}
				else {
					// tdacall
					try {
						IWebCaller.WebCallSeed seed = new IWebCaller.WebCallSeed();
						seed.actionName = action;
						seed.callingConventions = IWebCaller.CallingConventions.TDACALL;
						seed.tdaArgument = Long.parseLong(data);
						seed.login = login;
						seed.project_id = project_id;
						seed.jsonResult = null;
						API.webCaller.enqueue(seed);
						retVal = "{}";
					}
					catch(Throwable t) {
						String msg = t.getMessage();
						t.printStackTrace();
						if (msg != null)
							retVal = "{\"error\":\""+msg+"\"}";
						else
							retVal = "{\"error\":\"Could not perform webcall action "+action+"\"}";
		        	}
				}
			}
        	response.getOutputStream().println(retVal+"");
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
				response.getOutputStream().println("{\"error\":\"+"+t.toString()+"\"");
			else
				response.getOutputStream().println("{\"error\":\"+"+msg+"\"");			
		}
    }
		
	
	
}