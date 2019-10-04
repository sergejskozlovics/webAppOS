package org.webappos.apps.helloworld;

import org.webappos.apps.helloworld.mm.HelloWorldMetamodelFactory;
import org.webappos.server.API;
import org.webappos.webcaller.IWebCaller;
import org.webappos.webcaller.IWebCaller.WebCallSeed;

import lv.lumii.tda.raapi.RAAPI;

public class HelloWorld {
	
	public static void initial(RAAPI raapi, String project_id, long r)
	{		
		
		try {
			HelloWorldMetamodelFactory HWMM = new HelloWorldMetamodelFactory();
			HWMM.setRAAPI(raapi, "", true);
			
			org.webappos.apps.helloworld.mm.HelloWorld objectWithMessage = org.webappos.apps.helloworld.mm.HelloWorld.firstObject(HWMM);
			if (objectWithMessage==null) {
				objectWithMessage = HWMM.createHelloWorld();
				objectWithMessage.setMessage("Hello for the first time!");
			}
			else
				objectWithMessage.setMessage("Hello again!");
			
			WebCallSeed seed = new WebCallSeed();
			seed.actionName = "ShowMessageFromJSON";
			seed.project_id = project_id;
			seed.jsonArgument = "{\"message\":\""+objectWithMessage.getMessage()+"\"}";
			seed.callingConventions = IWebCaller.CallingConventions.JSONCALL;
			API.webCaller.enqueue(seed);

			WebCallSeed seed2 = new WebCallSeed();
			seed2.actionName = "ShowMessageFromWebMemory";
			seed2.project_id = project_id;
			seed2.tdaArgument = objectWithMessage.getRAAPIReference();
			seed2.callingConventions = IWebCaller.CallingConventions.TDACALL;
			API.webCaller.enqueue(seed2);

		} catch (Throwable e) {
			WebCallSeed seed = new WebCallSeed();
			seed.actionName = "ShowMessageFromJSON";
			seed.project_id = project_id;
			seed.jsonArgument = "{\"message\":\"An exception occurred - "+e.getMessage()+"\"}";
			seed.callingConventions = IWebCaller.CallingConventions.JSONCALL;
			API.webCaller.enqueue(seed);
		}

	}

	public static String addWorld(String s)
	{
		if (s==null)
			return "{\"error\":\"Null string passed.\"}";
		else
			return "{\"result\":\""+s.replace("Hello", "Hello, world, ")+"\"}";
	}

}
