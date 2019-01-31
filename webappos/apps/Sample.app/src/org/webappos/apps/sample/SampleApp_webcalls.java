package org.webappos.apps.sample;

import java.io.File;

import org.webappos.server.API;
import org.webappos.server.ConfigStatic;
import org.webappos.webcaller.IWebCaller;
import org.webappos.webcaller.IWebCaller.WebCallSeed;

import lv.lumii.tda.raapi.RAAPI;

public class SampleApp_webcalls {
	
	public static void bootstrap(RAAPI raapi, String project_id, long r)
	{
		System.out.println("in sample app bootstrap r="+r+", raapi="+raapi+", project_id="+project_id);
		
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
		cmd.setUrl(ConfigStatic.APPS_DIR + File.separator+API.memory.getProjectFullAppName(project_id)+"Sample.mmd");
		cmd.submit();

		org.webappos.sample.mm.SampleMetamodelFactory samplemmFactory = new org.webappos.sample.mm.SampleMetamodelFactory();
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
