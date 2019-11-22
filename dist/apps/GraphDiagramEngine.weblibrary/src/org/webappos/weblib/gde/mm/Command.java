// automatically generated
package org.webappos.weblib.gde.mm; 

import java.util.*;
import lv.lumii.tda.raapi.RAAPI;

public class Command
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
				System.err.println("Unable to delete the object "+rObject+" of type Command since the RAAPI wrapper does not take care of this reference.");
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

	public boolean submit()
	{
		// return setSubmitter(org.webappos.weblib.gde.mm.Submitter.firstObject(factory));
		long SUBMITTER_ROLE = factory.raapi.findAssociationEnd(factory.COMMAND, "submitter");  
		boolean retVal = factory.raapi.createLink(rObject, org.webappos.weblib.gde.mm.Submitter.firstObject(factory).rObject, SUBMITTER_ROLE);
		factory.raapi.freeReference(SUBMITTER_ROLE);
		return retVal;
	}

	// package-visibility:
	Command(GraphDiagramEngineMetamodelFactory _factory)
	{
		factory = _factory;
		rObject = factory.raapi.createObject(factory.COMMAND);			
		takeReference = true;
		factory.wrappers.put(rObject, this);
	}

	public Command(GraphDiagramEngineMetamodelFactory _factory, long _rObject, boolean _takeReference)
	{
		factory = _factory;
		rObject = _rObject;
		takeReference = _takeReference;
		if (takeReference)
			factory.wrappers.put(rObject, this);
	}

	// iterator for instances...
	public static Iterable<? extends Command> allObjects()
	{
		return allObjects(GraphDiagramEngineMetamodelFactory.eINSTANCE);
	} 

	public static Iterable<? extends Command> allObjects(GraphDiagramEngineMetamodelFactory factory)
	{
		ArrayList<Command> retVal = new ArrayList<Command>();
		long it = factory.raapi.getIteratorForAllClassObjects(factory.COMMAND);
		if (it == 0)
			return retVal;
		long r = factory.raapi.resolveIteratorFirst(it);
		while (r != 0) {
 			Command o = (Command)factory.findOrCreateRAAPIReferenceWrapper(r, true);
			if (o == null)
				o = (Command)factory.findOrCreateRAAPIReferenceWrapper(Command.class, r, true);
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
		for (Command o : allObjects(factory))
			o.delete();
		return firstObject(factory) == null;
	}

	public static Command firstObject()
	{
		return firstObject(GraphDiagramEngineMetamodelFactory.eINSTANCE);
	} 

	public static Command firstObject(GraphDiagramEngineMetamodelFactory factory)
	{
		long it = factory.raapi.getIteratorForAllClassObjects(factory.COMMAND);
		if (it == 0)
			return null;
		long r = factory.raapi.resolveIteratorFirst(it);
		factory.raapi.freeIterator(it);
		if (r == 0)
			return null;
		else {
			Command  retVal = (Command)factory.findOrCreateRAAPIReferenceWrapper(r, true);
			if (retVal == null)
				retVal = (Command)factory.findOrCreateRAAPIReferenceWrapper(Command.class, r, true);
			return retVal;
		}
	} 
 
	public List<GraphDiagram> getGraphDiagram()
	{
		return new GraphDiagramEngineMetamodel_RAAPILinkedObjectsList<GraphDiagram>(factory, rObject, factory.COMMAND_GRAPHDIAGRAM); 
	}
	public boolean setGraphDiagram(GraphDiagram value)
	{
		boolean ok = true;
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.COMMAND_GRAPHDIAGRAM);
		// deleting previous links...
		if (it != 0) {
			ArrayList<Long> list = new ArrayList<Long>();
			long r = factory.raapi.resolveIteratorFirst(it);
			while (r != 0) {
				list.add(r);
				r = factory.raapi.resolveIteratorNext(it);
			}
			factory.raapi.freeIterator(it);
			for (Long rLinked : list)
				if (!factory.raapi.deleteLink(rObject, rLinked, factory.COMMAND_GRAPHDIAGRAM))
					ok = false;
		}
		if (value != null)
			if (!factory.raapi.createLink(rObject, value.rObject, factory.COMMAND_GRAPHDIAGRAM))
				ok = false;
		return ok;
	}
}
