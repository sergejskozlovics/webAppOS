// automatically generated
package lv.lumii.tda.webgde.mm; 

import java.util.*;
import lv.lumii.tda.raapi.RAAPI;

public class GraphDiagramEngine
	extends Engine
  	implements RAAPIReferenceWrapper
{
	/* these references are defined only in the top-most superclass:
	protected GraphDiagramEngineMetamodelFactory factory;
	protected long rObject = 0;
	protected boolean takeReference;
	*/

	public RAAPI getRAAPI()
	{
		return factory.raapi;
	}
	public long getRAAPIReference()
	{
		return rObject;
	}

	public boolean delete()
	{
		if (rObject != 0) {
			if (!takeReference) {
				System.err.println("Unable to delete the object "+rObject+" of type GraphDiagramEngine since the RAAPI wrapper does not take care of this reference.");
				return false;
			}
			factory.wrappers.remove(rObject);
			boolean retVal = factory.raapi.deleteObject(rObject);
			if (retVal) {
				rObject = 0;
			}
			else
				factory.wrappers.put(rObject, this); // putting back
			return retVal;
		}
		else
			return false;
	}

	public void finalize()
	{
		if (rObject != 0) {
			if (takeReference) {
				factory.wrappers.remove(rObject);
				factory.raapi.freeReference(rObject);
			}
			rObject = 0;
		}
	}


	// package-visibility:
	GraphDiagramEngine(GraphDiagramEngineMetamodelFactory _factory)
	{
		super(_factory, _factory.raapi.createObject(_factory.GRAPHDIAGRAMENGINE), true);		
		factory = _factory;
		rObject = super.rObject;
		takeReference = true;
		factory.wrappers.put(rObject, this);
		/*
		factory = _factory;
		rObject = factory.raapi.createObject(factory.GRAPHDIAGRAMENGINE);			
		takeReference = true;
		factory.wrappers.put(rObject, this);
		*/
	}

	public GraphDiagramEngine(GraphDiagramEngineMetamodelFactory _factory, long _rObject, boolean _takeReference)
	{
		super(_factory, _rObject, _takeReference);
		/*
		factory = _factory;
		rObject = _rObject;
		takeReference = _takeReference;
		if (takeReference)
			factory.wrappers.put(rObject, this);
		*/
	}

	// iterator for instances...
	public static Iterable<? extends GraphDiagramEngine> allObjects()
	{
		return allObjects(GraphDiagramEngineMetamodelFactory.eINSTANCE);
	} 

	public static Iterable<? extends GraphDiagramEngine> allObjects(GraphDiagramEngineMetamodelFactory factory)
	{
		ArrayList<GraphDiagramEngine> retVal = new ArrayList<GraphDiagramEngine>();
		long it = factory.raapi.getIteratorForAllClassObjects(factory.GRAPHDIAGRAMENGINE);
		if (it == 0)
			return retVal;
		long r = factory.raapi.resolveIteratorFirst(it);
		while (r != 0) {
 			GraphDiagramEngine o = (GraphDiagramEngine)factory.findOrCreateRAAPIReferenceWrapper(r, true);
			if (o == null)
				o = (GraphDiagramEngine)factory.findOrCreateRAAPIReferenceWrapper(GraphDiagramEngine.class, r, true);
			if (o != null)
				retVal.add(o);
			r = factory.raapi.resolveIteratorNext(it);
		}
		factory.raapi.freeIterator(it);
		return retVal;
	}

	public static boolean deleteAllObjects()
	{
		return deleteAllObjects(GraphDiagramEngineMetamodelFactory.eINSTANCE);
	}

	public static boolean deleteAllObjects(GraphDiagramEngineMetamodelFactory factory)
	{
		for (GraphDiagramEngine o : allObjects(factory))
			o.delete();
		return firstObject(factory) == null;
	}

	public static GraphDiagramEngine firstObject()
	{
		return firstObject(GraphDiagramEngineMetamodelFactory.eINSTANCE);
	} 

	public static GraphDiagramEngine firstObject(GraphDiagramEngineMetamodelFactory factory)
	{
		long it = factory.raapi.getIteratorForAllClassObjects(factory.GRAPHDIAGRAMENGINE);
		if (it == 0)
			return null;
		long r = factory.raapi.resolveIteratorFirst(it);
		factory.raapi.freeIterator(it);
		if (r == 0)
			return null;
		else {
			GraphDiagramEngine  retVal = (GraphDiagramEngine)factory.findOrCreateRAAPIReferenceWrapper(r, true);
			if (retVal == null)
				retVal = (GraphDiagramEngine)factory.findOrCreateRAAPIReferenceWrapper(GraphDiagramEngine.class, r, true);
			return retVal;
		}
	} 
 
	public String getOnPopUpElemSelectEvent()
	{
		return factory.raapi.getAttributeValue(rObject, factory.GRAPHDIAGRAMENGINE_ONPOPUPELEMSELECTEVENT);
	}
	public boolean setOnPopUpElemSelectEvent(String value)
	{
		if (value == null)
			return factory.raapi.deleteAttributeValue(rObject, factory.GRAPHDIAGRAMENGINE_ONPOPUPELEMSELECTEVENT);
		return factory.raapi.setAttributeValue(rObject, factory.GRAPHDIAGRAMENGINE_ONPOPUPELEMSELECTEVENT, value.toString());
	}
	public String getOnPasteGraphClipboardEvent()
	{
		return factory.raapi.getAttributeValue(rObject, factory.GRAPHDIAGRAMENGINE_ONPASTEGRAPHCLIPBOARDEVENT);
	}
	public boolean setOnPasteGraphClipboardEvent(String value)
	{
		if (value == null)
			return factory.raapi.deleteAttributeValue(rObject, factory.GRAPHDIAGRAMENGINE_ONPASTEGRAPHCLIPBOARDEVENT);
		return factory.raapi.setAttributeValue(rObject, factory.GRAPHDIAGRAMENGINE_ONPASTEGRAPHCLIPBOARDEVENT, value.toString());
	}
	public String getOnDeleteCollectionEvent()
	{
		return factory.raapi.getAttributeValue(rObject, factory.GRAPHDIAGRAMENGINE_ONDELETECOLLECTIONEVENT);
	}
	public boolean setOnDeleteCollectionEvent(String value)
	{
		if (value == null)
			return factory.raapi.deleteAttributeValue(rObject, factory.GRAPHDIAGRAMENGINE_ONDELETECOLLECTIONEVENT);
		return factory.raapi.setAttributeValue(rObject, factory.GRAPHDIAGRAMENGINE_ONDELETECOLLECTIONEVENT, value.toString());
	}
	public String getOnCopyCutCollectionEvent()
	{
		return factory.raapi.getAttributeValue(rObject, factory.GRAPHDIAGRAMENGINE_ONCOPYCUTCOLLECTIONEVENT);
	}
	public boolean setOnCopyCutCollectionEvent(String value)
	{
		if (value == null)
			return factory.raapi.deleteAttributeValue(rObject, factory.GRAPHDIAGRAMENGINE_ONCOPYCUTCOLLECTIONEVENT);
		return factory.raapi.setAttributeValue(rObject, factory.GRAPHDIAGRAMENGINE_ONCOPYCUTCOLLECTIONEVENT, value.toString());
	}
	public String getOnCopyCollectionEvent()
	{
		return factory.raapi.getAttributeValue(rObject, factory.GRAPHDIAGRAMENGINE_ONCOPYCOLLECTIONEVENT);
	}
	public boolean setOnCopyCollectionEvent(String value)
	{
		if (value == null)
			return factory.raapi.deleteAttributeValue(rObject, factory.GRAPHDIAGRAMENGINE_ONCOPYCOLLECTIONEVENT);
		return factory.raapi.setAttributeValue(rObject, factory.GRAPHDIAGRAMENGINE_ONCOPYCOLLECTIONEVENT, value.toString());
	}
	public String getOnMoveLineStartPointEvent()
	{
		return factory.raapi.getAttributeValue(rObject, factory.GRAPHDIAGRAMENGINE_ONMOVELINESTARTPOINTEVENT);
	}
	public boolean setOnMoveLineStartPointEvent(String value)
	{
		if (value == null)
			return factory.raapi.deleteAttributeValue(rObject, factory.GRAPHDIAGRAMENGINE_ONMOVELINESTARTPOINTEVENT);
		return factory.raapi.setAttributeValue(rObject, factory.GRAPHDIAGRAMENGINE_ONMOVELINESTARTPOINTEVENT, value.toString());
	}
	public String getOnMoveLineEndPointEvent()
	{
		return factory.raapi.getAttributeValue(rObject, factory.GRAPHDIAGRAMENGINE_ONMOVELINEENDPOINTEVENT);
	}
	public boolean setOnMoveLineEndPointEvent(String value)
	{
		if (value == null)
			return factory.raapi.deleteAttributeValue(rObject, factory.GRAPHDIAGRAMENGINE_ONMOVELINEENDPOINTEVENT);
		return factory.raapi.setAttributeValue(rObject, factory.GRAPHDIAGRAMENGINE_ONMOVELINEENDPOINTEVENT, value.toString());
	}
	public String getOnL2ClickEvent()
	{
		return factory.raapi.getAttributeValue(rObject, factory.GRAPHDIAGRAMENGINE_ONL2CLICKEVENT);
	}
	public boolean setOnL2ClickEvent(String value)
	{
		if (value == null)
			return factory.raapi.deleteAttributeValue(rObject, factory.GRAPHDIAGRAMENGINE_ONL2CLICKEVENT);
		return factory.raapi.setAttributeValue(rObject, factory.GRAPHDIAGRAMENGINE_ONL2CLICKEVENT, value.toString());
	}
	public String getOnLClickEvent()
	{
		return factory.raapi.getAttributeValue(rObject, factory.GRAPHDIAGRAMENGINE_ONLCLICKEVENT);
	}
	public boolean setOnLClickEvent(String value)
	{
		if (value == null)
			return factory.raapi.deleteAttributeValue(rObject, factory.GRAPHDIAGRAMENGINE_ONLCLICKEVENT);
		return factory.raapi.setAttributeValue(rObject, factory.GRAPHDIAGRAMENGINE_ONLCLICKEVENT, value.toString());
	}
	public String getOnRClickEvent()
	{
		return factory.raapi.getAttributeValue(rObject, factory.GRAPHDIAGRAMENGINE_ONRCLICKEVENT);
	}
	public boolean setOnRClickEvent(String value)
	{
		if (value == null)
			return factory.raapi.deleteAttributeValue(rObject, factory.GRAPHDIAGRAMENGINE_ONRCLICKEVENT);
		return factory.raapi.setAttributeValue(rObject, factory.GRAPHDIAGRAMENGINE_ONRCLICKEVENT, value.toString());
	}
	public String getOnNewLineEvent()
	{
		return factory.raapi.getAttributeValue(rObject, factory.GRAPHDIAGRAMENGINE_ONNEWLINEEVENT);
	}
	public boolean setOnNewLineEvent(String value)
	{
		if (value == null)
			return factory.raapi.deleteAttributeValue(rObject, factory.GRAPHDIAGRAMENGINE_ONNEWLINEEVENT);
		return factory.raapi.setAttributeValue(rObject, factory.GRAPHDIAGRAMENGINE_ONNEWLINEEVENT, value.toString());
	}
	public String getOnNewBoxEvent()
	{
		return factory.raapi.getAttributeValue(rObject, factory.GRAPHDIAGRAMENGINE_ONNEWBOXEVENT);
	}
	public boolean setOnNewBoxEvent(String value)
	{
		if (value == null)
			return factory.raapi.deleteAttributeValue(rObject, factory.GRAPHDIAGRAMENGINE_ONNEWBOXEVENT);
		return factory.raapi.setAttributeValue(rObject, factory.GRAPHDIAGRAMENGINE_ONNEWBOXEVENT, value.toString());
	}
	public String getOnExecTransfEvent()
	{
		return factory.raapi.getAttributeValue(rObject, factory.GRAPHDIAGRAMENGINE_ONEXECTRANSFEVENT);
	}
	public boolean setOnExecTransfEvent(String value)
	{
		if (value == null)
			return factory.raapi.deleteAttributeValue(rObject, factory.GRAPHDIAGRAMENGINE_ONEXECTRANSFEVENT);
		return factory.raapi.setAttributeValue(rObject, factory.GRAPHDIAGRAMENGINE_ONEXECTRANSFEVENT, value.toString());
	}
	public String getOnNewPinEvent()
	{
		return factory.raapi.getAttributeValue(rObject, factory.GRAPHDIAGRAMENGINE_ONNEWPINEVENT);
	}
	public boolean setOnNewPinEvent(String value)
	{
		if (value == null)
			return factory.raapi.deleteAttributeValue(rObject, factory.GRAPHDIAGRAMENGINE_ONNEWPINEVENT);
		return factory.raapi.setAttributeValue(rObject, factory.GRAPHDIAGRAMENGINE_ONNEWPINEVENT, value.toString());
	}
	public String getOnChangeParentEvent()
	{
		return factory.raapi.getAttributeValue(rObject, factory.GRAPHDIAGRAMENGINE_ONCHANGEPARENTEVENT);
	}
	public boolean setOnChangeParentEvent(String value)
	{
		if (value == null)
			return factory.raapi.deleteAttributeValue(rObject, factory.GRAPHDIAGRAMENGINE_ONCHANGEPARENTEVENT);
		return factory.raapi.setAttributeValue(rObject, factory.GRAPHDIAGRAMENGINE_ONCHANGEPARENTEVENT, value.toString());
	}
	public String getOnActivateDgrEvent()
	{
		return factory.raapi.getAttributeValue(rObject, factory.GRAPHDIAGRAMENGINE_ONACTIVATEDGREVENT);
	}
	public boolean setOnActivateDgrEvent(String value)
	{
		if (value == null)
			return factory.raapi.deleteAttributeValue(rObject, factory.GRAPHDIAGRAMENGINE_ONACTIVATEDGREVENT);
		return factory.raapi.setAttributeValue(rObject, factory.GRAPHDIAGRAMENGINE_ONACTIVATEDGREVENT, value.toString());
	}
	public String getOnCloseDgrEvent()
	{
		return factory.raapi.getAttributeValue(rObject, factory.GRAPHDIAGRAMENGINE_ONCLOSEDGREVENT);
	}
	public boolean setOnCloseDgrEvent(String value)
	{
		if (value == null)
			return factory.raapi.deleteAttributeValue(rObject, factory.GRAPHDIAGRAMENGINE_ONCLOSEDGREVENT);
		return factory.raapi.setAttributeValue(rObject, factory.GRAPHDIAGRAMENGINE_ONCLOSEDGREVENT, value.toString());
	}
	public String getOnOKStyleDialogEvent()
	{
		return factory.raapi.getAttributeValue(rObject, factory.GRAPHDIAGRAMENGINE_ONOKSTYLEDIALOGEVENT);
	}
	public boolean setOnOKStyleDialogEvent(String value)
	{
		if (value == null)
			return factory.raapi.deleteAttributeValue(rObject, factory.GRAPHDIAGRAMENGINE_ONOKSTYLEDIALOGEVENT);
		return factory.raapi.setAttributeValue(rObject, factory.GRAPHDIAGRAMENGINE_ONOKSTYLEDIALOGEVENT, value.toString());
	}
	public String getOnKeyDownEvent()
	{
		return factory.raapi.getAttributeValue(rObject, factory.GRAPHDIAGRAMENGINE_ONKEYDOWNEVENT);
	}
	public boolean setOnKeyDownEvent(String value)
	{
		if (value == null)
			return factory.raapi.deleteAttributeValue(rObject, factory.GRAPHDIAGRAMENGINE_ONKEYDOWNEVENT);
		return factory.raapi.setAttributeValue(rObject, factory.GRAPHDIAGRAMENGINE_ONKEYDOWNEVENT, value.toString());
	}
	public String getOnNewFreeBoxEvent()
	{
		return factory.raapi.getAttributeValue(rObject, factory.GRAPHDIAGRAMENGINE_ONNEWFREEBOXEVENT);
	}
	public boolean setOnNewFreeBoxEvent(String value)
	{
		if (value == null)
			return factory.raapi.deleteAttributeValue(rObject, factory.GRAPHDIAGRAMENGINE_ONNEWFREEBOXEVENT);
		return factory.raapi.setAttributeValue(rObject, factory.GRAPHDIAGRAMENGINE_ONNEWFREEBOXEVENT, value.toString());
	}
	public String getOnNewFreeLineEvent()
	{
		return factory.raapi.getAttributeValue(rObject, factory.GRAPHDIAGRAMENGINE_ONNEWFREELINEEVENT);
	}
	public boolean setOnNewFreeLineEvent(String value)
	{
		if (value == null)
			return factory.raapi.deleteAttributeValue(rObject, factory.GRAPHDIAGRAMENGINE_ONNEWFREELINEEVENT);
		return factory.raapi.setAttributeValue(rObject, factory.GRAPHDIAGRAMENGINE_ONNEWFREELINEEVENT, value.toString());
	}
	public String getOnFreeBoxEditedEvent()
	{
		return factory.raapi.getAttributeValue(rObject, factory.GRAPHDIAGRAMENGINE_ONFREEBOXEDITEDEVENT);
	}
	public boolean setOnFreeBoxEditedEvent(String value)
	{
		if (value == null)
			return factory.raapi.deleteAttributeValue(rObject, factory.GRAPHDIAGRAMENGINE_ONFREEBOXEDITEDEVENT);
		return factory.raapi.setAttributeValue(rObject, factory.GRAPHDIAGRAMENGINE_ONFREEBOXEDITEDEVENT, value.toString());
	}
	public String getOnFreeLineEditedEvent()
	{
		return factory.raapi.getAttributeValue(rObject, factory.GRAPHDIAGRAMENGINE_ONFREELINEEDITEDEVENT);
	}
	public boolean setOnFreeLineEditedEvent(String value)
	{
		if (value == null)
			return factory.raapi.deleteAttributeValue(rObject, factory.GRAPHDIAGRAMENGINE_ONFREELINEEDITEDEVENT);
		return factory.raapi.setAttributeValue(rObject, factory.GRAPHDIAGRAMENGINE_ONFREELINEEDITEDEVENT, value.toString());
	}
}
