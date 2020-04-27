package org.webappos.weblib.de;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.webappos.server.API;
import org.webappos.webcaller.WebCaller;
import org.webappos.webmem.IWebMemory;
import org.webappos.webmem.WebMemoryContext;

public class DialogEngine_webcalls {

	private static Logger logger =  LoggerFactory.getLogger(DialogEngine_webcalls.class);

	
	public static boolean onCloseFrameRequestedEvent(IWebMemory webmem, long r)
	{		
		long it = webmem.getIteratorForDirectObjectClasses(r);		
		long rEventCls = webmem.resolveIteratorFirst(it);
		webmem.freeIterator(it);		
		long rAssociationEnd = webmem.findAssociationEnd(rEventCls, "frame");		
		it = webmem.getIteratorForLinkedObjects(r, rAssociationEnd);
		long rFrame = webmem.resolveIteratorFirst(it);
		webmem.freeIterator(it);
		
		long rCommandCls = webmem.findClass("DetachFrameCommand");
		long rCmd = webmem.createObject(rCommandCls);
		webmem.createLink(rCmd, rFrame, webmem.findAssociationEnd(rCommandCls, "frame"));
		webmem.setAttributeValue(rCmd, webmem.findAttribute(rCommandCls, "permanently"), "true");
		
		long rAssocEnd2 = webmem.findAssociationEnd(rCommandCls, "submitter");
		long rSubmitterCls = webmem.getTargetClass(rAssocEnd2);
		it = webmem.getIteratorForDirectClassObjects(rSubmitterCls);
		long rSubmitter = webmem.resolveIteratorFirst(it);
		webmem.freeIterator(it);
		
		webmem.createLink(rCmd, rSubmitter, rAssocEnd2); // submit
				
		return true;
	}
		
	private static void ensureFrame(org.webappos.weblib.de.mm.DialogEngineMetamodelFactory factory, org.webappos.weblib.de.mm.D_SHARP_Form df, boolean modal) {		
		if (df==null)
			return;
		org.webappos.weblib.de.mm.Frame f = df.getFrame().get(0);
		if (f==null) {
			f = factory.createFrame();
			f.setCaption(df.getCaption());
			f.setIsClosable(true);
			f.setContentURI("html:DialogEngine.html?frameReference="+f.getRAAPIReference());
			f.setForm(df);
			if (modal) {
				f.setLocation("modalpopup[330*440]"); // will be resized by DialogEngine
			}
			else
				f.setLocation("popup");
			f.setOnCloseFrameRequestedEvent("DE.onCloseFrameRequestedEvent");
			
		}
	}
	
	public static boolean prepareCommand(IWebMemory webmem, long r)
	{
		System.out.println("DLG prepareCommand "+r);
		org.webappos.weblib.de.mm.DialogEngineMetamodelFactory factory = null;
		try {
	
			factory = new org.webappos.weblib.de.mm.DialogEngineMetamodelFactory();
			try {
				factory.setRAAPI(webmem, "", true);
			} catch (org.webappos.weblib.de.mm.DialogEngineMetamodelFactory.ElementReferenceException e) {
				logger.error("Could not access Dialog Engine Metamodel from Java.");
				return false;
			}
			
			if (r == 0)
				return false;
			org.webappos.weblib.de.mm.Command cmd = (org.webappos.weblib.de.mm.Command)factory.findOrCreateRAAPIReferenceWrapper(r, true);
			
			if (cmd instanceof org.webappos.weblib.de.mm.D_SHARP_Command) {
				org.webappos.weblib.de.mm.D_SHARP_Component cmpnt = ((org.webappos.weblib.de.mm.D_SHARP_Command) cmd).getReceiver().get(0);
				if (cmpnt == null) {
					
					for (org.webappos.weblib.de.mm.D_SHARP_Form f : org.webappos.weblib.de.mm.D_SHARP_Form.allObjects(factory)) {
						if (f.getFrame().isEmpty())
							continue;
						org.webappos.weblib.de.mm.Frame frame = f.getFrame().get(0);
						if (frame.getLocation().toLowerCase().startsWith("modal")) {
							cmpnt = f; // we've found the modal frame
							//do not break; continue to find the last form 
						}
					}
					
				}
				
				if (cmpnt != null) {
					if (((org.webappos.weblib.de.mm.D_SHARP_Command)cmd).getReceiver().isEmpty())
						((org.webappos.weblib.de.mm.D_SHARP_Command)cmd).setReceiver(cmpnt); // linking context (to be able to find the frame)
					
					if (cmpnt instanceof org.webappos.weblib.de.mm.D_SHARP_Form) {
						ensureFrame(factory, (org.webappos.weblib.de.mm.D_SHARP_Form)cmpnt, "ShowModal".equalsIgnoreCase(((org.webappos.weblib.de.mm.D_SHARP_Command) cmd).getInfo()));
					}
				}
				else
					logger.error("Component not found for D#Command "+((org.webappos.weblib.de.mm.D_SHARP_Command)cmd).getInfo()+"!");
			}
			
			WebCaller.WebCallSeed seed = new WebCaller.WebCallSeed();
			
			seed.actionName = "continueD#Command";
						
			
			seed.callingConventions = WebCaller.CallingConventions.WEBMEMCALL;
			seed.webmemArgument = webmem.replicateObject(r);
			
			WebMemoryContext o = webmem.getContext();

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
