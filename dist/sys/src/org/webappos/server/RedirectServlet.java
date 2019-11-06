package org.webappos.server;

import java.io.*;
import java.sql.Date;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
 
public class RedirectServlet extends HttpServlet{
	
	private static Logger logger =  LoggerFactory.getLogger(RedirectServlet.class);
	
    private String target; 
    int httpPort, httpsPort;
	
    public RedirectServlet(String redirectAddrWithoutProtocolAndPath, int httpPort) {
    	target = redirectAddrWithoutProtocolAndPath;
    	this.httpPort = httpPort;
    	this.httpsPort = -2; // http only
    }
    
    public RedirectServlet(String redirectAddrWithoutProtocolAndPath, int httpPort, int httpsPort) {
    	target = redirectAddrWithoutProtocolAndPath;
    	this.httpPort = httpPort;
    	this.httpsPort = httpsPort;
    }
    
    public void init(ServletConfig config)throws ServletException{         
        super.init(config);         
    }
    public void doGet(HttpServletRequest req,HttpServletResponse resp)
            throws ServletException,IOException{
    	
    	
    	String protocol;
    	int port;
    	if (req.isSecure()) {
    		if (httpsPort <= -2) {
    			protocol = "http://"; // the target server does not provide separate https port
        		port=httpPort;    		
    		}
    		else {
    			protocol = "https://";
    			port=httpsPort;
    		}
    	}
    	else {
    		protocol = "http://";
    		port=httpPort;    		
    	}
    	
    	String s = req.getPathInfo();
    	if (port<0) // -1 means that we preserve the same port
    		port = req.getServerPort();
    	
    	String qs = req.getQueryString();
    	if (qs!=null) {
    		if (s==null)
    			s = "?"+qs;
    		else
    			s+="?"+qs;
    	}
    	
    	logger.debug("REDIRECT SERVER PORT "+req.getServerPort());
    	if (s!=null) {
    		logger.debug("REDIRECTING "+req.getRequestURI()+" to "+protocol+target+":"+port+s);
    		resp.sendRedirect(protocol+target+":"+port+s);
    	}
    	else {
    		logger.debug("REDIRECTING "+req.getRequestURI()+" to "+protocol+target+":"+port);
    		resp.sendRedirect(protocol+target+":"+port);
    	}
    	
    }
}