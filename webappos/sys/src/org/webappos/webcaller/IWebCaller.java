package org.webappos.webcaller;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.CompletableFuture;


import lv.lumii.tda.raapi.RAAPI_Synchronizer;

/**
 * Used by the server-side bridge and server-side actions to make web calls. 

 * @author Sergejs Kozlovics
 *
 */
public interface IWebCaller {
	
	/**
	 * Specifies the calling convention for web calls. Currently, jsoncall and tdacall are supported.
	 * @author Sergejs Kozlovics
	 *
	 */
	public static enum CallingConventions { JSONCALL, TDACALL };
	/**
	 * A class for storing all necessary information required to make a web call.
	 * @author Sergejs Kozlovics
	 *
	 */
	public static class WebCallSeed implements Serializable {
		private static final long serialVersionUID = 1L; 
		// action name to call:
		public String actionName = null ;
		
		// argument:
		public CallingConventions callingConventions = CallingConventions.JSONCALL;		
		public String jsonArgument = null;
		public CompletableFuture<String> jsonResult = null; // if null, no result is expected
		public long tdaArgument = 0;
		
		// other known info:
		public String login = null;
  		public String project_id = null;
  		
  		// time-to-live: (how many times this seed can be enqueued)
  		public int timeToLive = 10;
	};

	/**
	 * A class used only by the server-side bridge to store information for web calls
	 * not originated at the server side. In certain cases, web calls originated at the server side
	 * can also be repackaged as SyncedWebCallSeed-s for technical reasons. 
	 * @author Sergejs Kozlovics
	 *
	 */
	public static class SyncedWebCallSeed extends WebCallSeed { // used only from the bridge		
		private static final long serialVersionUID = 1L;
		public RAAPI_Synchronizer singleSynchronizer = null; 
			// if null, use multi synchronizer from kernel;
			// otherwise, either singleSynchronizer or multi synchronizer from the kernel is used
			// depending on whether the action is single
		public SyncedWebCallSeed() {
			super();
		}
		public SyncedWebCallSeed(WebCallSeed seed) {
			this.actionName = seed.actionName;
			this.callingConventions = seed.callingConventions;
			this.jsonArgument = seed.jsonArgument;
			this.jsonResult = seed.jsonResult;
			this.tdaArgument = seed.tdaArgument;
			this.login = seed.login;
			this.project_id = seed.project_id;
			this.timeToLive = seed.timeToLive;
	  		if (seed instanceof SyncedWebCallSeed)
	  			this.singleSynchronizer = ((SyncedWebCallSeed) seed).singleSynchronizer;
		}
	}
	
	/**
	 * A class for storing one parsed web call declaration from some .webcalls file. 
	 * @author Sergejs Kozlovics
	 *
	 */
	public static class WebCallDeclaration implements Serializable {
		private static final long serialVersionUID = 1L;
		
		public WebCallDeclaration(String location) {
			if (location == null)
				return;
			int i = location.indexOf(':');
			if (i<0)
				return;
			resolvedInstructionSet = location.substring(0, i);
			resolvedLocation = location.substring(i+1);
			
			if (resolvedInstructionSet.indexOf("client")>=0)
				isClient = true;
		}
		
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
					"\"callingConventions\":\""+(callingConventions==CallingConventions.JSONCALL?"jsoncall":"tdacall")+"\""+
					"}";
		}
	}
	
	/**
	 * Enqueues a web call.
	 * @param seed full information required to make a web call
	 */
	public void enqueue(final WebCallSeed seed);
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
