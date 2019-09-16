package lv.lumii.tda.webde;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.webappos.server.API;
import org.webappos.util.StackTrace;
import org.webappos.webcaller.WebCaller;

import lv.lumii.tda.kernel.TDAKernel;
import lv.lumii.tda.raapi.RAAPI;

public class DialogEngine_webcalls {

	private static Logger logger =  LoggerFactory.getLogger(DialogEngine_webcalls.class);

/*	public static boolean onFrameActivatedEvent(RAAPI raapi, long r)
	{
		if (DEBUG) System.err.println("frame activated from GDE_jsonsubmit");
		lv.lumii.tda.ee.mm.EnvironmentEngineMetamodelFactory eeFactory = new lv.lumii.tda.ee.mm.EnvironmentEngineMetamodelFactory();
		try {
			eeFactory.setRAAPI(raapi, "", true);
		} catch (Throwable e) {
			return false;
		}
		lv.lumii.tda.webde.mm.GraphDiagramEngineMetamodelFactory gdeFactory = new lv.lumii.tda.webde.mm.GraphDiagramEngineMetamodelFactory();
		try {
			gdeFactory.setRAAPI(raapi, "", true);
		} catch (Throwable e) {
			eeFactory.unsetRAAPI();
			return false;
		}
		
		lv.lumii.tda.ee.mm.FrameActivatedEvent ev = (lv.lumii.tda.ee.mm.FrameActivatedEvent)eeFactory.findOrCreateRAAPIReferenceWrapper(r, false); 
		lv.lumii.tda.ee.mm.Frame frame = ev.getFrame();
		lv.lumii.tda.webde.mm.Frame frame2 = (lv.lumii.tda.webde.mm.Frame)gdeFactory.findOrCreateRAAPIReferenceWrapper(frame.getRAAPIReference(), false);
		

		lv.lumii.tda.webde.mm.CurrentDgrPointer ptr = lv.lumii.tda.webde.mm.CurrentDgrPointer.firstObject(gdeFactory);
		if (ptr == null)
			ptr = gdeFactory.createCurrentDgrPointer();
		
		ptr.getGraphDiagram().clear();
		
		
		// linking to the current graph diagram...
		if (frame2.getGraphDiagram().size()>0) {
			if (DEBUG) System.err.println(" GRAPH DIAGRAM "+frame2.getGraphDiagram().get(0).getRAAPIReference());
			ptr.getGraphDiagram().add(frame2.getGraphDiagram().get(0));			
		}				
		
		
		eeFactory.unsetRAAPI();
		gdeFactory.unsetRAAPI();
		
		return true;
	}*/

	
	public static boolean onCloseFrameRequestedEvent(RAAPI raapi, long r)
	{
		lv.lumii.tda.ee.mm.EnvironmentEngineMetamodelFactory eeFactory = new lv.lumii.tda.ee.mm.EnvironmentEngineMetamodelFactory();
		try {
			eeFactory.setRAAPI(raapi, "", true);
		} catch (Throwable e) {
			e.printStackTrace();
			return false;
		}
		
		lv.lumii.tda.ee.mm.CloseFrameRequestedEvent ev = (lv.lumii.tda.ee.mm.CloseFrameRequestedEvent)eeFactory.findOrCreateRAAPIReferenceWrapper(r, false); 
		lv.lumii.tda.ee.mm.Frame frame = ev.getFrame();
		
		
		
		lv.lumii.tda.ee.mm.DetachFrameCommand dfc = eeFactory.createDetachFrameCommand();
		dfc.setFrame(frame);
		dfc.setPermanently(true);
		dfc.submit();
		
		
		//if (DEBUG) System.err.println("deleting frame with content uri = "+frame.getContentURI());
		//frame.delete();		
		
		eeFactory.unsetRAAPI();
		
		return true;
	}
		
	private static void ensureFrame(lv.lumii.tda.webde.mm.DialogEngineMetamodelFactory factory, lv.lumii.tda.webde.mm.D_SHARP_Form df, boolean modal) {		
		if (df==null)
			return;
		lv.lumii.tda.webde.mm.Frame f = df.getFrame().get(0);
		if (f==null) {
			f = factory.createFrame();
			f.setCaption(df.getCaption());
			f.setIsClosable(true);
			f.setContentURI("html:DialogEngine.html?frameReference="+f.getRAAPIReference());
			f.setForm(df);
			if (modal) {
				f.setLocation("modalpopup[0*0]"); // will be resized by DialogEngine
			}
			else
				f.setLocation("popup");
			f.setOnCloseFrameRequestedEvent("DE.onCloseFrameRequestedEvent");
			
		}
	}
	
	public static boolean prepareCommand(RAAPI raapi, long r)
	{
		System.out.println("DLG prepareCommand "+r);
		lv.lumii.tda.webde.mm.DialogEngineMetamodelFactory factory = null;
		try {
	
			factory = new lv.lumii.tda.webde.mm.DialogEngineMetamodelFactory();
			try {
				factory.setRAAPI(raapi, "", true);
			} catch (lv.lumii.tda.webde.mm.DialogEngineMetamodelFactory.ElementReferenceException e) {
				logger.error("Could not access Dialog Engine Metamodel from Java.");
				return false;
			}
			
			if (r == 0)
				return false;
			lv.lumii.tda.webde.mm.Command cmd = (lv.lumii.tda.webde.mm.Command)factory.findOrCreateRAAPIReferenceWrapper(r, true);
			
			if (cmd instanceof lv.lumii.tda.webde.mm.D_SHARP_Command) {
				lv.lumii.tda.webde.mm.D_SHARP_Component cmpnt = ((lv.lumii.tda.webde.mm.D_SHARP_Command) cmd).getReceiver().get(0);
				if (cmpnt == null) {
					
					for (lv.lumii.tda.webde.mm.D_SHARP_Form f : lv.lumii.tda.webde.mm.D_SHARP_Form.allObjects(factory)) {
						if (f.getFrame().isEmpty())
							continue;
						lv.lumii.tda.webde.mm.Frame frame = f.getFrame().get(0);
						if (frame.getLocation().toLowerCase().startsWith("modal")) {
							cmpnt = f; // we've found the modal frame
							//do not break; continue to find the last form 
						}
					}
					
				}
				
				if (cmpnt != null) {
					if (((lv.lumii.tda.webde.mm.D_SHARP_Command)cmd).getReceiver().isEmpty())
						((lv.lumii.tda.webde.mm.D_SHARP_Command)cmd).setReceiver(cmpnt); // linking context (to be able to find the frame)
					
					long rit = raapi.getIteratorForDirectObjectClasses(cmpnt.getRAAPIReference());
					long rCls = raapi.resolveIteratorFirst(rit);
					raapi.freeIterator(rit);
					if (cmpnt instanceof lv.lumii.tda.webde.mm.D_SHARP_Form) {
						ensureFrame(factory, (lv.lumii.tda.webde.mm.D_SHARP_Form)cmpnt, "ShowModal".equalsIgnoreCase(((lv.lumii.tda.webde.mm.D_SHARP_Command) cmd).getInfo()));
					}
				}
				else
					logger.error("Component not found for D#Command "+((lv.lumii.tda.webde.mm.D_SHARP_Command)cmd).getInfo()+"!");
			}
			
			if (!(raapi instanceof TDAKernel))
				return false;
			
			WebCaller.WebCallSeed seed = new WebCaller.WebCallSeed();
			
			seed.actionName = "continueD#Command";
						
			
			seed.callingConventions = WebCaller.CallingConventions.TDACALL;
//			System.out.println("replicate1 "+r);
			seed.tdaArgument = ((TDAKernel)raapi).replicateEventOrCommand(r);
//			System.out.println("replicate2 "+seed.tdaArgument);
			
			TDAKernel.Owner o = ((TDAKernel)raapi).getOwner();

			if (o != null) {
				seed.login = o.login;
				seed.project_id = o.project_id;
			}
	  		
	  		API.webCaller.enqueue(seed);
			
			return true;
				
		} catch (Throwable e) {
			logger.error("An exception occurred.");
			return false;
		}			
		finally {
			if (factory != null)
				factory.unsetRAAPI();
		}
	}

}
