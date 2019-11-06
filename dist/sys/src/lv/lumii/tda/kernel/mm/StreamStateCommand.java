// automatically generated
package lv.lumii.tda.kernel.mm; 

import java.util.*;
import lv.lumii.tda.raapi.RAAPI;

public class StreamStateCommand
	extends UndoIgnoringClass
  	implements RAAPIReferenceWrapper
{
	/* these references are defined only in the top-most superclass:
	protected UndoMetamodelFactory factory;
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
				System.err.println("Unable to delete the object "+rObject+" of type TDAKernel::StreamStateCommand since the RAAPI wrapper does not take care of this reference.");
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
	StreamStateCommand(UndoMetamodelFactory _factory)
	{
		super(_factory, _factory.raapi.createObject(_factory.STREAMSTATECOMMAND), true);		
		factory = _factory;
		rObject = super.rObject;
		takeReference = true;
		factory.wrappers.put(rObject, this);
		/*
		factory = _factory;
		rObject = factory.raapi.createObject(factory.STREAMSTATECOMMAND);			
		takeReference = true;
		factory.wrappers.put(rObject, this);
		*/
	}

	public StreamStateCommand(UndoMetamodelFactory _factory, long _rObject, boolean _takeReference)
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
	public static Iterable<? extends StreamStateCommand> allObjects()
	{
		return allObjects(UndoMetamodelFactory.eINSTANCE);
	} 

	public static Iterable<? extends StreamStateCommand> allObjects(UndoMetamodelFactory factory)
	{
		ArrayList<StreamStateCommand> retVal = new ArrayList<StreamStateCommand>();
		long it = factory.raapi.getIteratorForAllClassObjects(factory.STREAMSTATECOMMAND);
		if (it == 0)
			return retVal;
		long r = factory.raapi.resolveIteratorFirst(it);
		while (r != 0) {
 			StreamStateCommand o = (StreamStateCommand)factory.findOrCreateRAAPIReferenceWrapper(r, true);
			if (o == null)
				o = (StreamStateCommand)factory.findOrCreateRAAPIReferenceWrapper(StreamStateCommand.class, r, true);
			if (o != null)
				retVal.add(o);
			r = factory.raapi.resolveIteratorNext(it);
		}
		factory.raapi.freeIterator(it);
		return retVal;
	}

	public static boolean deleteAllObjects()
	{
		return deleteAllObjects(UndoMetamodelFactory.eINSTANCE);
	}

	public static boolean deleteAllObjects(UndoMetamodelFactory factory)
	{
		for (StreamStateCommand o : allObjects(factory))
			o.delete();
		return firstObject(factory) == null;
	}

	public static StreamStateCommand firstObject()
	{
		return firstObject(UndoMetamodelFactory.eINSTANCE);
	} 

	public static StreamStateCommand firstObject(UndoMetamodelFactory factory)
	{
		long it = factory.raapi.getIteratorForAllClassObjects(factory.STREAMSTATECOMMAND);
		if (it == 0)
			return null;
		long r = factory.raapi.resolveIteratorFirst(it);
		factory.raapi.freeIterator(it);
		if (r == 0)
			return null;
		else {
			StreamStateCommand  retVal = (StreamStateCommand)factory.findOrCreateRAAPIReferenceWrapper(r, true);
			if (retVal == null)
				retVal = (StreamStateCommand)factory.findOrCreateRAAPIReferenceWrapper(StreamStateCommand.class, r, true);
			return retVal;
		}
	} 
 
	public StreamState getStreamState()
	{
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.STREAMSTATECOMMAND_STREAMSTATE);
		if (it == 0)
			return null;
		long r = factory.raapi.resolveIteratorFirst(it);
		factory.raapi.freeIterator(it);
		if (r != 0) {
			StreamState retVal = (StreamState)factory.findOrCreateRAAPIReferenceWrapper(r, true);
			if (retVal == null)
				retVal = (StreamState)factory.findOrCreateRAAPIReferenceWrapper(StreamState.class, r, true);
			return retVal;
		}
		else
			return null;
	}
	public boolean setStreamState(StreamState value)
	{
		boolean ok = true;
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.STREAMSTATECOMMAND_STREAMSTATE);
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
				if (!factory.raapi.deleteLink(rObject, rLinked, factory.STREAMSTATECOMMAND_STREAMSTATE))
					ok = false;
		}
		if (value != null)
			if (!factory.raapi.createLink(rObject, value.rObject, factory.STREAMSTATECOMMAND_STREAMSTATE))
				ok = false;
		return ok;
	}
	public Boolean getInUndo()
	{
		try { 
			String value = factory.raapi.getAttributeValue(rObject, factory.STREAMSTATECOMMAND_INUNDO);
			if (value == null)
				return null;
			return Boolean.parseBoolean(value);
		}
		catch (Throwable t)
		{
			return null;
		} 
	}
	public boolean setInUndo(Boolean value)
	{
		if (value == null)
			return factory.raapi.deleteAttributeValue(rObject, factory.STREAMSTATECOMMAND_INUNDO);
		return factory.raapi.setAttributeValue(rObject, factory.STREAMSTATECOMMAND_INUNDO, value.toString());
	}
}
