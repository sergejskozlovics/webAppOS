package org.webappos.weblib.gde;

import java.util.Iterator;
import java.util.ListIterator;

import org.codehaus.jettison.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.webappos.server.API;
import org.webappos.webcaller.WebCaller;
import org.webappos.weblib.gde.eemm.Option;
import org.webappos.weblib.gde.mm.GraphDiagramEngineMetamodelFactory;
import org.webappos.webmem.IWebMemory;
import org.webappos.webmem.WebMemoryContext;


public class GraphDiagramEngine_webcalls {
	
	
	private static Logger logger =  LoggerFactory.getLogger(GraphDiagramEngine_webcalls.class);
	
	public static boolean onFrameActivatedEvent(IWebMemory raapi, long r)
	{
		org.webappos.weblib.gde.mm.GraphDiagramEngineMetamodelFactory gdeFactory = raapi.elevate(org.webappos.weblib.gde.mm.GraphDiagramEngineMetamodelFactory.class, true);				
		org.webappos.weblib.gde.eemm.EnvironmentEngineMetamodelFactory eeFactory = raapi.elevate(org.webappos.weblib.gde.eemm.EnvironmentEngineMetamodelFactory.class, true);
		if (gdeFactory == null || eeFactory == null)
			return false;
		
		org.webappos.weblib.gde.eemm.FrameActivatedEvent ev = (org.webappos.weblib.gde.eemm.FrameActivatedEvent)eeFactory.findOrCreateRAAPIReferenceWrapper(r, false); 
		org.webappos.weblib.gde.eemm.Frame frame = ev.getFrame();
		if (frame==null) {
			eeFactory.unsetRAAPI();
			return false;
		}
		org.webappos.weblib.gde.mm.Frame frame2 = (org.webappos.weblib.gde.mm.Frame)gdeFactory.findOrCreateRAAPIReferenceWrapper(frame.getRAAPIReference(), false);
		
		org.webappos.weblib.gde.mm.CurrentDgrPointer ptr = org.webappos.weblib.gde.mm.CurrentDgrPointer.firstObject(gdeFactory);
		if (ptr == null)
			ptr = gdeFactory.createCurrentDgrPointer();
		
		org.webappos.weblib.gde.mm.GraphDiagram oldDgr = null;
		if (!ptr.getGraphDiagram().isEmpty())
			oldDgr = ptr.getGraphDiagram().get(0);
		
		// removing old toolbar...
		try {
		if (oldDgr != null) {			 
			if ((oldDgr.getToolbar()!=null) && !oldDgr.getToolbar().isEmpty()) {
				org.webappos.weblib.gde.mm.Toolbar t = oldDgr.getToolbar().get(0);
				
				Iterator<org.webappos.weblib.gde.eemm.Option> it_o = (Iterator<org.webappos.weblib.gde.eemm.Option>) org.webappos.weblib.gde.eemm.Option.allObjects(eeFactory).iterator();
				ListIterator<org.webappos.weblib.gde.mm.ToolbarElement> it_te = t.getToolbarElement().listIterator();//.iterator();
				
				org.webappos.weblib.gde.eemm.Option o = null;
				org.webappos.weblib.gde.mm.ToolbarElement te = null;
				

				while (it_te.hasNext()) {
					
					te = it_te.next();
					
					while (it_o.hasNext()) {
						o = it_o.next();
						if (("GDEOPTION"+te.getRAAPIReference()).equals(o.getId())) {
							o.delete();
							break;
						}
					}
				}
			}
		}
		}
		catch(Throwable t) {
			logger.error(t.toString()+", "+t.getMessage());			
		}
		
		ptr.getGraphDiagram().clear();
		
		// linking to the current graph diagram...
		if (frame2.getGraphDiagram().size()>0) {
			ptr.getGraphDiagram().add(frame2.getGraphDiagram().get(0));
		}	
			
		
		// adding new toolbar...
		if (!frame2.getGraphDiagram().get(0).getToolbar().isEmpty()) {
			org.webappos.weblib.gde.mm.Toolbar t = frame2.getGraphDiagram().get(0).getToolbar().get(0);
			
			Iterator<org.webappos.weblib.gde.eemm.Option> it_o = (Iterator<Option>) org.webappos.weblib.gde.eemm.Option.allObjects(eeFactory).iterator();
			Iterator<org.webappos.weblib.gde.mm.ToolbarElement> it_te = t.getToolbarElement().iterator();
			
			org.webappos.weblib.gde.eemm.Option o = null;
			org.webappos.weblib.gde.mm.ToolbarElement te = null;

			try {
			while (it_te.hasNext()) {
				te = it_te.next();
				o = eeFactory.createOption();
				o.setId("GDEOPTION"+te.getRAAPIReference());
				o.setCaption(te.getCaption());
				o.setImage(te.getPicture());
				o.setLocation("TOOLBAR");
				o.setOnOptionSelectedEvent("GDE.defaultHandlerForOptionSelectedEvent");
				
				o.setFrame(frame);
				org.webappos.weblib.gde.eemm.EnvironmentEngine ee = org.webappos.weblib.gde.eemm.EnvironmentEngine.firstObject(eeFactory);
				ee.getOption().add(o);
				
			}
			}
			catch(Throwable tt) {
				tt.printStackTrace();
			}
		}
		
		// issuing RefreshOptionsCommand...
		org.webappos.weblib.gde.eemm.RefreshOptionsCommand cmd = eeFactory.createRefreshOptionsCommand();
		cmd.setEnvironmentEngine(org.webappos.weblib.gde.eemm.EnvironmentEngine.firstObject(eeFactory));
		cmd.submit();		
		eeFactory.unsetRAAPI();
		gdeFactory.unsetRAAPI();
		
		return true;
	}
	
	public static boolean onOptionSelectedEvent(IWebMemory raapi, long r) { // for converting to ToolbarElementSelectEvent
		
		org.webappos.weblib.gde.eemm.EnvironmentEngineMetamodelFactory eeFactory = new org.webappos.weblib.gde.eemm.EnvironmentEngineMetamodelFactory();
		try {
			eeFactory.setRAAPI(raapi, "", true);
		} catch (Throwable e) {
			return false;
		}
		org.webappos.weblib.gde.mm.GraphDiagramEngineMetamodelFactory gdeFactory = new org.webappos.weblib.gde.mm.GraphDiagramEngineMetamodelFactory();
		try {
			gdeFactory.setRAAPI(raapi, "", true);
		} catch (Throwable e) {
			eeFactory.unsetRAAPI();
			return false;
		}
		
		org.webappos.weblib.gde.eemm.OptionSelectedEvent optEv = (org.webappos.weblib.gde.eemm.OptionSelectedEvent)eeFactory.findOrCreateRAAPIReferenceWrapper(r, false);
		org.webappos.weblib.gde.eemm.Option opt = optEv.getOption();
		
		long rId = 0;
		String id = null;
		if (opt != null)
			id = opt.getId();
		if ((id != null) && (id.startsWith("GDEOPTION"))) {
			rId = Long.parseLong( id.substring(9) );
		}
		
				
		org.webappos.weblib.gde.mm.ToolbarElementSelectEvent ev = gdeFactory.createToolbarElementSelectEvent();
		ev.setToolbarElement( (org.webappos.weblib.gde.mm.ToolbarElement) gdeFactory.findOrCreateRAAPIReferenceWrapper(rId, false) );
		ev.submit();
		
		//optEv.delete(); ??
		
		
		eeFactory.unsetRAAPI();
		gdeFactory.unsetRAAPI();
		
		return true;
	}

	
	public static boolean onCloseFrameRequestedEvent(IWebMemory webmem, long r)
	{
		org.webappos.weblib.gde.eemm.EnvironmentEngineMetamodelFactory eeFactory = new org.webappos.weblib.gde.eemm.EnvironmentEngineMetamodelFactory();
		try {
			eeFactory.setRAAPI(webmem, "", true);
		} catch (Throwable e) {
			e.printStackTrace();
			return false;
		}
		
		org.webappos.weblib.gde.eemm.CloseFrameRequestedEvent ev = (org.webappos.weblib.gde.eemm.CloseFrameRequestedEvent)eeFactory.findOrCreateRAAPIReferenceWrapper(r, false); 
		org.webappos.weblib.gde.eemm.Frame frame = ev.getFrame();
		
		
		
		org.webappos.weblib.gde.eemm.DetachFrameCommand dfc = eeFactory.createDetachFrameCommand();
		dfc.setFrame(frame);
		dfc.setPermanently(true);
		dfc.submit();
		
		eeFactory.unsetRAAPI();
		
		return true;
	}
	
	private static void ensureFrame(org.webappos.weblib.gde.mm.GraphDiagramEngineMetamodelFactory factory, org.webappos.weblib.gde.mm.GraphDiagram gd) {		
		if (gd==null)
			return;		
		org.webappos.weblib.gde.mm.Frame f = gd.getFrame().get(0);
		if (f==null) {
			f = factory.createFrame();
			f.setCaption("Graph Diagram");
			f.setContentURI("html:GraphDiagramEngine.html?frameReference="+f.getRAAPIReference());
			f.setGraphDiagram(gd);
			f.setLocation("CENTER");
			f.setOnFrameActivatedEvent("GDE.onFrameActivatedEvent");
			f.setOnCloseFrameRequestedEvent("GDE.onCloseFrameRequestedEvent");
		}
	}
	

	public static void prepareCommand(IWebMemory webmem, long r)
	{		
		if (r==0)
			return;
		
		org.webappos.weblib.gde.mm.GraphDiagramEngineMetamodelFactory factory = null;
		try {
	
			factory = new org.webappos.weblib.gde.mm.GraphDiagramEngineMetamodelFactory();
			try {
				factory.setRAAPI(webmem, "", true);
			} catch (org.webappos.weblib.gde.mm.GraphDiagramEngineMetamodelFactory.ElementReferenceException e) {
				e.printStackTrace();
				return;
			}
			
			org.webappos.weblib.gde.mm.Command cmd = (org.webappos.weblib.gde.mm.Command)factory.findOrCreateRAAPIReferenceWrapper(r, true);
			
			if (cmd instanceof org.webappos.weblib.gde.mm.SaveDgrCmd)
				return; // do not need to execute
			
			if ((cmd instanceof org.webappos.weblib.gde.mm.ActiveDgrCmd)
			  ||(cmd instanceof org.webappos.weblib.gde.mm.ActiveDgrViewCmd)) {
				org.webappos.weblib.gde.mm.GraphDiagram gd = cmd.getGraphDiagram().get(0);
				ensureFrame(factory, gd);				
			}
			else
			if ((cmd instanceof org.webappos.weblib.gde.mm.OkCmd)
			  ||(cmd instanceof org.webappos.weblib.gde.mm.UpdateDgrCmd)
			  ||(cmd instanceof org.webappos.weblib.gde.mm.RefreshDgrCmd)
			  ||(cmd instanceof org.webappos.weblib.gde.mm.ActiveElementCmd)
			){
				org.webappos.weblib.gde.mm.GraphDiagram gd = cmd.getGraphDiagram().get(0);
				
				if (gd == null) {
					org.webappos.weblib.gde.mm.CurrentDgrPointer ptr = org.webappos.weblib.gde.mm.CurrentDgrPointer.firstObject(factory);
					if (ptr==null) {
						gd = org.webappos.weblib.gde.mm.GraphDiagram.firstObject(factory);
						if (gd!=null) {
							try {
								ptr = factory.createCurrentDgrPointer();
								ptr.setGraphDiagram(gd);
								cmd.setGraphDiagram( gd );
							}
							catch (Throwable t) {}
						}
					}
					else {
						try {
							cmd.setGraphDiagram( org.webappos.weblib.gde.mm.CurrentDgrPointer.firstObject(factory).getGraphDiagram().get(0) );
						}
						catch (Throwable t) {}
					}
									
				}
				
				if (gd != null) {
					// do not execute UpdateDgrCmd before ActiveDgrCmd
					if ((cmd instanceof org.webappos.weblib.gde.mm.UpdateDgrCmd) && gd.getFrame().isEmpty()) {
						System.out.println("ignoring UpdateDgrCmd before activating");
						return;
					}
					
					ensureFrame(factory, gd);
				}
				
				if ((gd != null)&&(cmd instanceof org.webappos.weblib.gde.mm.ActiveElementCmd)) {
					gd.getCollection().clear();
					org.webappos.weblib.gde.mm.Collection c = factory.createCollection();
					c.getElement().addAll(  ((org.webappos.weblib.gde.mm.ActiveElementCmd)cmd).getElement() );
					gd.getCollection().add(c);
				}
			}
			else
			if 	(cmd instanceof org.webappos.weblib.gde.mm.CloseDgrCmd) {
				org.webappos.weblib.gde.mm.GraphDiagram gd = cmd.getGraphDiagram().get(0);
				if (gd == null) {
					System.out.println("CloseDgrCmd with no diagram called");
					return;
				}
				else {
					System.out.println("CloseDgrCmd gd="+gd.getRAAPIReference());
					if (gd.getFrame().isEmpty())
						System.out.println(" no frame specified");
					else
						System.out.println(" frame="+gd.getFrame().get(0).getRAAPIReference());
				}
			}

			
			
			WebCaller.WebCallSeed seed = new WebCaller.WebCallSeed();
			
			seed.actionName = "continue"+cmd.getClass().getSimpleName();
			
			seed.callingConventions = WebCaller.CallingConventions.WEBMEMCALL;
			seed.webmemArgument = webmem.replicateObject(r);			
			
			WebMemoryContext ctx = webmem.getContext();

			if (ctx != null) {
				seed.login = ctx.login;
				seed.project_id = ctx.project_id;
			}
	  		
	  		API.webCaller.invokeNow(seed);//.enqueue(seed);
			
		} catch (Throwable e) {			
			logger.error(e.toString()+", "+e.getMessage());
			e.printStackTrace();
		}			
		finally {
			if (factory != null)
				factory.unsetRAAPI();
		}
	}
	
	public static String layoutGraphDiagram(IWebMemory webmem, String _json) {
		// reads coordinates from the repository, lays out the diagram (json = stringified diagram reference),
		// and puts the new coordinates back into the repository (AZ location encoding);
		
		// may use (TDAKernel)raapi to store cache (previous layout data structures)
	
		org.webappos.weblib.gde.mm.GraphDiagramEngineMetamodelFactory factory = null;
		try {
			factory = new org.webappos.weblib.gde.mm.GraphDiagramEngineMetamodelFactory();
				
			try {
				JSONObject json;
				json = new JSONObject(_json);
		
				long r;
				try {
					r = json.getLong("reference");
					factory.setRAAPI(webmem, "", false);
				} catch (org.webappos.weblib.gde.mm.GraphDiagramEngineMetamodelFactory.ElementReferenceException e) {
					logger.error("setRAAPI exception: "+e.getMessage());
					return null;
				}

				org.webappos.weblib.gde.mm.GraphDiagram gd = (org.webappos.weblib.gde.mm.GraphDiagram)factory.findOrCreateRAAPIReferenceWrapper(r, true);
				
				String layoutName = json.getString("layoutName");
				
				
/*				Object cacheObj = ((TDAKernel)raapi).retrieveCache("LAYOUT"+r);
				IMCSDiagramLayout layout;
				if ((layoutName == null) && (cacheObj != null))
					layout = (IMCSDiagramLayout)cacheObj;
				else {
					layout = new IMCSDiagramLayout(layoutName);
					//layout.
					((TDAKernel)raapi).storeCache("LAYOUT"+r, layout);
				}*/
				
				// adding/re-adding boxes...
				
				// adding/re-adding lines...
				
				// adding/re-adding labels...			
				
				return "{ \"result\": true }";
			} catch (Throwable t) {
				return "{ \"result\" : false }";
			}
		}			
		finally {
			if (factory != null)
				factory.unsetRAAPI();
		}				
	}
}
