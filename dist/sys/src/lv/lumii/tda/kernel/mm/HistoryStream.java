// automatically generated
package lv.lumii.tda.kernel.mm; 

import java.util.*;
import lv.lumii.tda.raapi.RAAPI;

public class HistoryStream
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
				System.err.println("Unable to delete the object "+rObject+" of type TDAKernel::HistoryStream since the RAAPI wrapper does not take care of this reference.");
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
	HistoryStream(UndoMetamodelFactory _factory)
	{
		super(_factory, _factory.raapi.createObject(_factory.HISTORYSTREAM), true);		
		factory = _factory;
		rObject = super.rObject;
		takeReference = true;
		factory.wrappers.put(rObject, this);
		/*
		factory = _factory;
		rObject = factory.raapi.createObject(factory.HISTORYSTREAM);			
		takeReference = true;
		factory.wrappers.put(rObject, this);
		*/
	}

	public HistoryStream(UndoMetamodelFactory _factory, long _rObject, boolean _takeReference)
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
	public static Iterable<? extends HistoryStream> allObjects()
	{
		return allObjects(UndoMetamodelFactory.eINSTANCE);
	} 

	public static Iterable<? extends HistoryStream> allObjects(UndoMetamodelFactory factory)
	{
		ArrayList<HistoryStream> retVal = new ArrayList<HistoryStream>();
		long it = factory.raapi.getIteratorForAllClassObjects(factory.HISTORYSTREAM);
		if (it == 0)
			return retVal;
		long r = factory.raapi.resolveIteratorFirst(it);
		while (r != 0) {
 			HistoryStream o = (HistoryStream)factory.findOrCreateRAAPIReferenceWrapper(r, true);
			if (o == null)
				o = (HistoryStream)factory.findOrCreateRAAPIReferenceWrapper(HistoryStream.class, r, true);
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
		for (HistoryStream o : allObjects(factory))
			o.delete();
		return firstObject(factory) == null;
	}

	public static HistoryStream firstObject()
	{
		return firstObject(UndoMetamodelFactory.eINSTANCE);
	} 

	public static HistoryStream firstObject(UndoMetamodelFactory factory)
	{
		long it = factory.raapi.getIteratorForAllClassObjects(factory.HISTORYSTREAM);
		if (it == 0)
			return null;
		long r = factory.raapi.resolveIteratorFirst(it);
		factory.raapi.freeIterator(it);
		if (r == 0)
			return null;
		else {
			HistoryStream  retVal = (HistoryStream)factory.findOrCreateRAAPIReferenceWrapper(r, true);
			if (retVal == null)
				retVal = (HistoryStream)factory.findOrCreateRAAPIReferenceWrapper(HistoryStream.class, r, true);
			return retVal;
		}
	} 
 
	public StreamState getCurrentState()
	{
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.HISTORYSTREAM_CURRENTSTATE);
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
	public boolean setCurrentState(StreamState value)
	{
		boolean ok = true;
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.HISTORYSTREAM_CURRENTSTATE);
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
				if (!factory.raapi.deleteLink(rObject, rLinked, factory.HISTORYSTREAM_CURRENTSTATE))
					ok = false;
		}
		if (value != null)
			if (!factory.raapi.createLink(rObject, value.rObject, factory.HISTORYSTREAM_CURRENTSTATE))
				ok = false;
		return ok;
	}
	public UndoHistory getUndoHistory()
	{
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.HISTORYSTREAM_UNDOHISTORY);
		if (it == 0)
			return null;
		long r = factory.raapi.resolveIteratorFirst(it);
		factory.raapi.freeIterator(it);
		if (r != 0) {
			UndoHistory retVal = (UndoHistory)factory.findOrCreateRAAPIReferenceWrapper(r, true);
			if (retVal == null)
				retVal = (UndoHistory)factory.findOrCreateRAAPIReferenceWrapper(UndoHistory.class, r, true);
			return retVal;
		}
		else
			return null;
	}
	public boolean setUndoHistory(UndoHistory value)
	{
		boolean ok = true;
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.HISTORYSTREAM_UNDOHISTORY);
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
				if (!factory.raapi.deleteLink(rObject, rLinked, factory.HISTORYSTREAM_UNDOHISTORY))
					ok = false;
		}
		if (value != null)
			if (!factory.raapi.createLink(rObject, value.rObject, factory.HISTORYSTREAM_UNDOHISTORY))
				ok = false;
		return ok;
	}
	public List<StreamState> getStreamState()
	{
		return new UndoMetamodel_RAAPILinkedObjectsList<StreamState>(factory, rObject, factory.HISTORYSTREAM_STREAMSTATE); 
	}
	public boolean setStreamState(StreamState value)
	{
		boolean ok = true;
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.HISTORYSTREAM_STREAMSTATE);
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
				if (!factory.raapi.deleteLink(rObject, rLinked, factory.HISTORYSTREAM_STREAMSTATE))
					ok = false;
		}
		if (value != null)
			if (!factory.raapi.createLink(rObject, value.rObject, factory.HISTORYSTREAM_STREAMSTATE))
				ok = false;
		return ok;
	}
}
