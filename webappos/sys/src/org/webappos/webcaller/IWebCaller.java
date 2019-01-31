package org.webappos.webcaller;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.CompletableFuture;


import lv.lumii.tda.kernel.TDAKernel;
import lv.lumii.tda.raapi.RAAPI_Synchronizer;

public interface IWebCaller {
	
	public static enum CallingConventions { JSONCALL, TDACALL };
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
	
	public void enqueue(final WebCallSeed seed);
	public boolean webCallExists(String actionName);
	public WebCallDeclaration getWebCallDeclaration(String actionName);
	public Map<String, WebCallDeclaration> getWebCalls(String fullAppName);
}
