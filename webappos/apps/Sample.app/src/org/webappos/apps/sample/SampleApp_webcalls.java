package org.webappos.apps.sample;

import java.io.File;
import java.net.MalformedURLException;

import org.webappos.server.API;
import org.webappos.server.ConfigStatic;
import org.webappos.webcaller.IWebCaller;
import org.webappos.webcaller.IWebCaller.WebCallSeed;

import lv.lumii.tda.raapi.RAAPI;

public class SampleApp_webcalls {
	
	public static void bootstrap(RAAPI raapi, String project_id, long r)
	{
		System.out.println("in sample app bootstrap r="+r+", raapi="+raapi+", project_id="+project_id);
		
		/*		if (raapi.findClass("HelloWorld")==0) {
		// if the class HelloWorld does not exist,
		// inserting the HelloWorld.ecore metamodel (optional, since the HWMM below will insert the metamodel in any case)
	
		try {
			lv.lumii.tda.kernel.mm.TDAKernelMetamodelFactory KMM=null;			
			KMM = new lv.lumii.tda.kernel.mm.TDAKernelMetamodelFactory();
			KMM.setRAAPI(raapi, "", true);
			
			lv.lumii.tda.kernel.mm.InsertMetamodelCommand cmd = KMM.createInsertMetamodelCommand();
			File f = new File(ConfigStatic.APPS_DIR + File.separator+API.dataMemory.getProjectFullAppName(project_id)+File.separator+"HelloWorld.ecore");
			cmd.setUrl(f.toURI().toURL().toString());
			cmd.submit();

		}
		catch(Throwable t) {
			t.printStackTrace();				
		}
	}*/

		lv.lumii.tda.kernel.mm.TDAKernelMetamodelFactory kmmFactory = new lv.lumii.tda.kernel.mm.TDAKernelMetamodelFactory();
		try {
			kmmFactory.setRAAPI(raapi, "", true);
		} catch (Throwable e) {
			return;
		}
		
		lv.lumii.tda.ee.mm.EnvironmentEngineMetamodelFactory eemmFactory = new lv.lumii.tda.ee.mm.EnvironmentEngineMetamodelFactory();
		try {
			eemmFactory.setRAAPI(raapi, "", true);
		} catch (Throwable e) {
			kmmFactory.unsetRAAPI();
			return;
		}
		
		lv.lumii.tda.kernel.mm.InsertMetamodelCommand cmd = kmmFactory.createInsertMetamodelCommand();
		// file: 
		File f = new File(ConfigStatic.APPS_DIR + File.separator+API.dataMemory.getProjectFullAppName(project_id)+File.separator+"Sample.mmd");
		try {
			cmd.setUrl(f.toURI().toURL().toString());
		} catch (MalformedURLException e) {
			cmd.setUrl("file:///"+f.getAbsolutePath());
		}
		cmd.submit();

		org.webappos.sample.mm.SampleMetamodelFactory samplemmFactory = new org.webappos.sample.mm.SampleMetamodelFactory();
		try {
			samplemmFactory.setRAAPI(raapi, "", true);
		} catch (Throwable e) {
			eemmFactory.unsetRAAPI();
			kmmFactory.unsetRAAPI();			
			return;
		}
		org.webappos.sample.mm.Counter counter = org.webappos.sample.mm.Counter.firstObject(samplemmFactory);
		
		
		if (counter == null)
			counter = samplemmFactory.createCounter();
		counter.setCount(0);
		
		lv.lumii.tda.ee.mm.EnvironmentEngine ee = lv.lumii.tda.ee.mm.EnvironmentEngine.firstObject(eemmFactory);
		ee.setOnProjectOpenedEvent("onOpen");
		
		WebCallSeed seed = new WebCallSeed();
		seed.actionName = "onOpen";
		seed.project_id = project_id;
		seed.tdaArgument = r;
		seed.callingConventions = IWebCaller.CallingConventions.TDACALL;
		API.webCaller.enqueue(seed);
	}

}
