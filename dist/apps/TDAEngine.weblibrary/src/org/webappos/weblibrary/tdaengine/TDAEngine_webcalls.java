package org.webappos.weblibrary.tdaengine;

import java.net.MalformedURLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.webappos.server.API;
import org.webappos.webcaller.IWebCaller;
import org.webappos.webcaller.WebCaller;
import org.webappos.webmem.IWebMemory;
import org.webappos.webmem.WebMemoryContext;

import lv.lumii.tda.kernel.mmdparser.MetamodelInserter;
import lv.lumii.tda.raapi.RAAPIHelper;

public class TDAEngine_webcalls {

	private static Logger logger =  LoggerFactory.getLogger(TDAEngine_webcalls.class);
	
	public static void executeInsertMetamodelCommand(IWebMemory webmem, long rCommand) {
		long rCls = RAAPIHelper.getObjectClass(webmem, rCommand);
		long rAttr = webmem.findAttribute(rCls, "url");
		if (rAttr == 0) {
			webmem.freeReference(rCls);
			logger.error("Attribute 'url' not found in class InsertMetamodelCommand.");
			return;
		}
		String url_str = webmem.getAttributeValue(rCommand, rAttr);
		webmem.freeReference(rCls);
		webmem.freeReference(rAttr);
		
		try {
			StringBuffer errorMessages = new StringBuffer();
			boolean retVal = MetamodelInserter.insertMetamodel(new java.net.URL(url_str), webmem, errorMessages);
			if (!retVal || (errorMessages.length()>0))
				logger.error("Error inserting metamodel at "+url_str+"\n"+errorMessages.toString());
			return;
		} catch (MalformedURLException e) {
			logger.error("TDA Kernel: Error executing InsertMetamodelCommand. "+e.getMessage());
			return;
		}
	}
	
	public static void executeLaunchTransformationCommand(IWebMemory webmem, long rCommand) {
		WebMemoryContext ctx = webmem.getContext();
		
		long rCls = RAAPIHelper.getObjectClass(webmem, rCommand);
		long rAttr = webmem.findAttribute(rCls, "uri");
		if (rAttr == 0) {
			webmem.freeReference(rCls);
			logger.error("Attribute 'uri' not found in class LaunchTransformationCommand (or in its descendants).");
			return;
		}
		String transformationName = webmem.getAttributeValue(rCommand, rAttr);
		if (transformationName == null) {
			webmem.freeReference(rCls);
			logger.error("No transformation URI specified in the given instance of LaunchTransformationCommand.");
			return;
		}
		webmem.freeReference(rAttr);
		webmem.freeReference(rCls);
		
		long rArgument = 0;
		int j = transformationName.indexOf("):");
		int i = transformationName.indexOf('(');
		if ((i>=0) && (j>i)) {
			try {
				rArgument = Long.parseLong(transformationName.substring(i+1, j));
			}
			catch (Throwable t) {
			}
			transformationName = transformationName.substring(0, i) + transformationName.substring(j+1);
		}
		
		if (rArgument==0)
			rArgument = rCommand;
		
		IWebCaller.WebCallSeed seed = new IWebCaller.WebCallSeed();
		
		seed.actionName = transformationName;
		
		seed.callingConventions = WebCaller.CallingConventions.WEBMEMCALL;
		seed.webmemArgument = rArgument;

		if (ctx != null) {
			seed.login = ctx.login;
			seed.project_id = ctx.project_id;
		}
  		
  		API.webCaller.enqueue(seed);
		
	}

	public static void executeExecTransfCmd(IWebMemory webmem, long rCommand) {
		
		WebMemoryContext ctx = webmem.getContext();
		
		long rCls = RAAPIHelper.getObjectClass(webmem, rCommand);
		long rAttr = webmem.findAttribute(rCls, "info");
		if (rAttr == 0) {
			webmem.freeReference(rCls);
			logger.error("Attribute 'info' not found in class ExecTransfCommand.");
			return;
		}
		String transformationName = webmem.getAttributeValue(rCommand, rAttr);
		
		// legacy fix:
		if (transformationName.startsWith("lua_engine#lua."))
			transformationName = "lua:"+transformationName.substring(15);
		
		webmem.freeReference(rAttr);
		webmem.freeReference(rCls);
		
		long rArgument = 0;
		int j = transformationName.indexOf("):");
		int i = transformationName.indexOf('(');
		if ((i>=0) && (j>i)) {
			try {
				rArgument = Long.parseLong(transformationName.substring(i+1, j));
			}
			catch (Throwable t) {
			}
			transformationName = transformationName.substring(0, i) + transformationName.substring(j+1);
		}
		
		if (rArgument==0)
			rArgument = rCommand;
		
		
		IWebCaller.WebCallSeed seed = new IWebCaller.WebCallSeed();
		
		seed.actionName = transformationName;
		
		seed.callingConventions = WebCaller.CallingConventions.WEBMEMCALL;
		seed.webmemArgument = rArgument;

		if (ctx != null) {
			seed.login = ctx.login;
			seed.project_id = ctx.project_id;
		}
  		
		System.out.println("ExecTransfCmd "+transformationName+"("+rArgument+")");
  		API.webCaller.enqueue(seed);
		
	}

}
