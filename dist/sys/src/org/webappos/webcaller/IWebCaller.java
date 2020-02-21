package org.webappos.webcaller;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * Used by the server-side bridge and server-side web calls actions to make web calls. 

 * @author Sergejs Kozlovics
 *
 */
public interface IWebCaller {
	
	/**
	 * Specifies the calling convention for web calls. Currently, jsoncall and webmemcall are supported.
	 * @author Sergejs Kozlovics
	 *
	 */
	public static enum CallingConventions { JSONCALL, WEBMEMCALL };
	/**
	 * A class for storing all necessary information required to make a web call.
	 */
	public static class WebCallSeed implements Serializable {
		private static final long serialVersionUID = 1L; 
		// action name to call:
		public String actionName = null ;
		
		// argument:
		public CallingConventions callingConventions = CallingConventions.JSONCALL;		
		public String jsonArgument = null;
		public CompletableFuture<String> jsonResult = null; // if null, no result is expected
		public long webmemArgument = 0;
		
		// other known info:
		public String login = null;
  		public String project_id = null;
  		public String fullAppName = null;
  		
  		// time-to-live: (how many times this seed can be enqueued)
  		public int timeToLive = 10;
	};
	
	/**
	 * A class for storing one parsed web call declaration from some .webcalls file. 
	 * @author Sergejs Kozlovics
	 *
	 */
	public static class WebCallDeclaration implements Serializable {
		private static final long serialVersionUID = 1L;
		
		public WebCallDeclaration(String location, String _pwd) {
			pwd = _pwd;
			if (location == null)
				return;
			int i = location.indexOf(':');
			if (i<0)
				return;
			resolvedInstructionSet = location.substring(0, i);
			resolvedLocation = location.substring(i+1).replace("$PWD", pwd);
			
			if (resolvedInstructionSet.indexOf("client")>=0)
				isClient = true;
		}
		
		public String pwd = null; // the directory, where the web call declared (the location of the .webcall file)
		
		public boolean isPublic = false;
		public boolean isStatic = false;
		public boolean isInline = false;
		public boolean isSingle = false; 
		
		public CallingConventions callingConventions = CallingConventions.JSONCALL;
		
		public String resolvedInstructionSet = ""; // =adapter name; the web processor will initialize that adapter
		public boolean isClient = false; 
		public String resolvedLocation = "";	
		
		@Override
		public String toString() {
			return	"{"+
					"\"resolvedInstructionSet\":\""+resolvedInstructionSet+"\","+
					"\"resolvedLocation\":\""+resolvedLocation+"\","+
					"\"isPublic\":"+isPublic+","+
					"\"isStatic\":"+isStatic+","+
					"\"isInline\":"+isInline+","+
					"\"isSingle\":"+isSingle+","+
					"\"isClient\":"+isClient+","+
					"\"callingConventions\":\""+(callingConventions==CallingConventions.JSONCALL?"jsoncall":"webmemcall")+"\""+
					"}";
		}
	}
	
	/**
	 * Returns how many web calls have been enqueued for the given project (including the web call currently being executed).
	 * @param project_id the project id to check
	 * @return whether the length of the queue
	 */
	public int getQueueSize(String project_id);

	/**
	 * Enqueues a web call.
	 * @param seed the information required to make a web call
	 */
	public void enqueue(final WebCallSeed seed);
	
	/**
	 * Tries to invoke the given web call in the current web processor, bypassing the bridge.
	 * @param seed the information required to make a web call
	 * @return whether the operation succeeded (i.e., the adapter found and the call was made)
	 */
	public boolean invokeNow(final WebCallSeed seed);
	
	/**
	 * Checks whether the given web call action exists.
	 * @param actionName web call action name to check
	 * @return true, if the action was found, or false otherwise (also in case of an error)
	 */
	public boolean webCallExists(String actionName);
	/**
	 * Obtain a parsed web call declaration loaded from some .webcalls file.
	 * @param actionName web call action name to consider
	 * @return a parsed web call declaration
	 */
	public WebCallDeclaration getWebCallDeclaration(String actionName);
	/**
	 * Obtain all legitimate web calls that can be used within the given webAppOS app.
	 * Useful for the client side to know what it can call.
	 * @param fullAppName the full app name (with the .app suffix)
	 * @return a map: web call action name -&gt; parsed web call declaration
	 */
	public Map<String, WebCallDeclaration> getWebCalls(String fullAppName);
}
