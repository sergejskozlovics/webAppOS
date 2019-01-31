// automatically generated from .mtl
package lv.lumii.tda.webgde.mm; 

import java.util.*;
import lv.lumii.tda.raapi.RAAPI;

public class Submitter
  	implements RAAPIReferenceWrapper
{
	protected GraphDiagramEngineMetamodelFactory factory;
	protected long rObject = 0;
	protected boolean takeReference;

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
				System.err.println("Unable to delete the object "+rObject+" of type TDAKernel::Submitter since the RAAPI wrapper does not take care of this reference.");
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
	Submitter(GraphDiagramEngineMetamodelFactory _factory)
	{
		factory = _factory;
		long SUBMITTER = factory.raapi.findClass("TDAKernel::Submitter"); 
		rObject = factory.raapi.createObject(SUBMITTER);
		factory.raapi.freeReference(SUBMITTER);
		takeReference = true;
		factory.wrappers.put(rObject, this);
	}

	public Submitter(GraphDiagramEngineMetamodelFactory _factory, long _rObject, boolean _takeReference)
	{
		factory = _factory;
		rObject = _rObject;
		takeReference = _takeReference;
		if (takeReference)
			factory.wrappers.put(rObject, this);
	}

	// iterator for instances...
	public static Iterable<? extends Submitter> allObjects()
	{
		return allObjects(GraphDiagramEngineMetamodelFactory.eINSTANCE);
	} 

	public static Iterable<? extends Submitter> allObjects(GraphDiagramEngineMetamodelFactory factory)
	{
		ArrayList<Submitter> retVal = new ArrayList<Submitter>();
		long SUBMITTER = factory.raapi.findClass("TDAKernel::Submitter"); 
		long it = factory.raapi.getIteratorForAllClassObjects(SUBMITTER);
		factory.raapi.freeReference(SUBMITTER);
		if (it == 0)
			return retVal;
		long r = factory.raapi.resolveIteratorFirst(it);
		while (r != 0) {
 			Submitter o = (Submitter)factory.findOrCreateRAAPIReferenceWrapper(r, true);
			if (o == null)
				o = (Submitter)factory.findOrCreateRAAPIReferenceWrapper(Submitter.class, r, true);
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
		for (Submitter o : allObjects(factory))
			o.delete();
		return firstObject(factory) == null;
	}

	public static Submitter firstObject()
	{
		return firstObject(GraphDiagramEngineMetamodelFactory.eINSTANCE);
	} 

	public static Submitter firstObject(GraphDiagramEngineMetamodelFactory factory)
	{
		long SUBMITTER = factory.raapi.findClass("TDAKernel::Submitter"); 
		long it = factory.raapi.getIteratorForAllClassObjects(SUBMITTER);
		factory.raapi.freeReference(SUBMITTER);
		if (it == 0)
			return null;
		long r = factory.raapi.resolveIteratorFirst(it);
		factory.raapi.freeIterator(it);
		if (r == 0)
			return null;
		else {
			Submitter  retVal = (Submitter)factory.findOrCreateRAAPIReferenceWrapper(r, true);
			if (retVal == null)
				retVal = (Submitter)factory.findOrCreateRAAPIReferenceWrapper(Submitter.class, r, true);
			return retVal;
		}
	} 

	public List<Event> getEvent()
	{
		long SUBMITTER = factory.raapi.findClass("TDAKernel::Submitter");
		long SUBMITTER_EVENT = factory.raapi.findAssociationEnd(SUBMITTER, "event");  
		List<Event> retVal = new GraphDiagramEngineMetamodel_RAAPILinkedObjectsList<Event>(factory, rObject, SUBMITTER_EVENT);
		factory.raapi.freeReference(SUBMITTER);
		factory.raapi.freeReference(SUBMITTER_EVENT);
		return retVal; 
	}
	public List<Command> getCommand()
	{
		long SUBMITTER = factory.raapi.findClass("TDAKernel::Submitter");
		long SUBMITTER_COMMAND = factory.raapi.findAssociationEnd(SUBMITTER, "command");  
		List<Command> retVal = new GraphDiagramEngineMetamodel_RAAPILinkedObjectsList<Command>(factory, rObject, SUBMITTER_COMMAND); 
		factory.raapi.freeReference(SUBMITTER);
		factory.raapi.freeReference(SUBMITTER_COMMAND);
		return retVal;
	}
}
