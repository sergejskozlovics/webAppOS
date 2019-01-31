// automatically generated
package lv.lumii.tda.kernel.mm; 

import java.util.*;
import lv.lumii.tda.raapi.RAAPI;

public class StreamState
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
				System.err.println("Unable to delete the object "+rObject+" of type TDAKernel::StreamState since the RAAPI wrapper does not take care of this reference.");
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
	StreamState(UndoMetamodelFactory _factory)
	{
		super(_factory, _factory.raapi.createObject(_factory.STREAMSTATE), true);		
		factory = _factory;
		rObject = super.rObject;
		takeReference = true;
		factory.wrappers.put(rObject, this);
		/*
		factory = _factory;
		rObject = factory.raapi.createObject(factory.STREAMSTATE);			
		takeReference = true;
		factory.wrappers.put(rObject, this);
		*/
	}

	public StreamState(UndoMetamodelFactory _factory, long _rObject, boolean _takeReference)
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
	public static Iterable<? extends StreamState> allObjects()
	{
		return allObjects(UndoMetamodelFactory.eINSTANCE);
	} 

	public static Iterable<? extends StreamState> allObjects(UndoMetamodelFactory factory)
	{
		ArrayList<StreamState> retVal = new ArrayList<StreamState>();
		long it = factory.raapi.getIteratorForAllClassObjects(factory.STREAMSTATE);
		if (it == 0)
			return retVal;
		long r = factory.raapi.resolveIteratorFirst(it);
		while (r != 0) {
 			StreamState o = (StreamState)factory.findOrCreateRAAPIReferenceWrapper(r, true);
			if (o == null)
				o = (StreamState)factory.findOrCreateRAAPIReferenceWrapper(StreamState.class, r, true);
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
		for (StreamState o : allObjects(factory))
			o.delete();
		return firstObject(factory) == null;
	}

	public static StreamState firstObject()
	{
		return firstObject(UndoMetamodelFactory.eINSTANCE);
	} 

	public static StreamState firstObject(UndoMetamodelFactory factory)
	{
		long it = factory.raapi.getIteratorForAllClassObjects(factory.STREAMSTATE);
		if (it == 0)
			return null;
		long r = factory.raapi.resolveIteratorFirst(it);
		factory.raapi.freeIterator(it);
		if (r == 0)
			return null;
		else {
			StreamState  retVal = (StreamState)factory.findOrCreateRAAPIReferenceWrapper(r, true);
			if (retVal == null)
				retVal = (StreamState)factory.findOrCreateRAAPIReferenceWrapper(StreamState.class, r, true);
			return retVal;
		}
	} 
 
	public HistoryStream getCurrentStateStream()
	{
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.STREAMSTATE_CURRENTSTATESTREAM);
		if (it == 0)
			return null;
		long r = factory.raapi.resolveIteratorFirst(it);
		factory.raapi.freeIterator(it);
		if (r != 0) {
			HistoryStream retVal = (HistoryStream)factory.findOrCreateRAAPIReferenceWrapper(r, true);
			if (retVal == null)
				retVal = (HistoryStream)factory.findOrCreateRAAPIReferenceWrapper(HistoryStream.class, r, true);
			return retVal;
		}
		else
			return null;
	}
	public boolean setCurrentStateStream(HistoryStream value)
	{
		boolean ok = true;
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.STREAMSTATE_CURRENTSTATESTREAM);
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
				if (!factory.raapi.deleteLink(rObject, rLinked, factory.STREAMSTATE_CURRENTSTATESTREAM))
					ok = false;
		}
		if (value != null)
			if (!factory.raapi.createLink(rObject, value.rObject, factory.STREAMSTATE_CURRENTSTATESTREAM))
				ok = false;
		return ok;
	}
	public StreamState getPrevious()
	{
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.STREAMSTATE_PREVIOUS);
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
	public boolean setPrevious(StreamState value)
	{
		boolean ok = true;
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.STREAMSTATE_PREVIOUS);
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
				if (!factory.raapi.deleteLink(rObject, rLinked, factory.STREAMSTATE_PREVIOUS))
					ok = false;
		}
		if (value != null)
			if (!factory.raapi.createLink(rObject, value.rObject, factory.STREAMSTATE_PREVIOUS))
				ok = false;
		return ok;
	}
	public List<StreamState> getEarlyNext()
	{
		return new UndoMetamodel_RAAPILinkedObjectsList<StreamState>(factory, rObject, factory.STREAMSTATE_EARLYNEXT); 
	}
	public boolean setEarlyNext(StreamState value)
	{
		boolean ok = true;
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.STREAMSTATE_EARLYNEXT);
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
				if (!factory.raapi.deleteLink(rObject, rLinked, factory.STREAMSTATE_EARLYNEXT))
					ok = false;
		}
		if (value != null)
			if (!factory.raapi.createLink(rObject, value.rObject, factory.STREAMSTATE_EARLYNEXT))
				ok = false;
		return ok;
	}
	public StreamState getNext()
	{
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.STREAMSTATE_NEXT);
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
	public boolean setNext(StreamState value)
	{
		boolean ok = true;
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.STREAMSTATE_NEXT);
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
				if (!factory.raapi.deleteLink(rObject, rLinked, factory.STREAMSTATE_NEXT))
					ok = false;
		}
		if (value != null)
			if (!factory.raapi.createLink(rObject, value.rObject, factory.STREAMSTATE_NEXT))
				ok = false;
		return ok;
	}
	public List<StreamState> getLateNext()
	{
		return new UndoMetamodel_RAAPILinkedObjectsList<StreamState>(factory, rObject, factory.STREAMSTATE_LATENEXT); 
	}
	public boolean setLateNext(StreamState value)
	{
		boolean ok = true;
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.STREAMSTATE_LATENEXT);
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
				if (!factory.raapi.deleteLink(rObject, rLinked, factory.STREAMSTATE_LATENEXT))
					ok = false;
		}
		if (value != null)
			if (!factory.raapi.createLink(rObject, value.rObject, factory.STREAMSTATE_LATENEXT))
				ok = false;
		return ok;
	}
	public Transaction getTransaction()
	{
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.STREAMSTATE_TRANSACTION);
		if (it == 0)
			return null;
		long r = factory.raapi.resolveIteratorFirst(it);
		factory.raapi.freeIterator(it);
		if (r != 0) {
			Transaction retVal = (Transaction)factory.findOrCreateRAAPIReferenceWrapper(r, true);
			if (retVal == null)
				retVal = (Transaction)factory.findOrCreateRAAPIReferenceWrapper(Transaction.class, r, true);
			return retVal;
		}
		else
			return null;
	}
	public boolean setTransaction(Transaction value)
	{
		boolean ok = true;
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.STREAMSTATE_TRANSACTION);
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
				if (!factory.raapi.deleteLink(rObject, rLinked, factory.STREAMSTATE_TRANSACTION))
					ok = false;
		}
		if (value != null)
			if (!factory.raapi.createLink(rObject, value.rObject, factory.STREAMSTATE_TRANSACTION))
				ok = false;
		return ok;
	}
	public StreamStateCommand getStreamStateCommand()
	{
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.STREAMSTATE_STREAMSTATECOMMAND);
		if (it == 0)
			return null;
		long r = factory.raapi.resolveIteratorFirst(it);
		factory.raapi.freeIterator(it);
		if (r != 0) {
			StreamStateCommand retVal = (StreamStateCommand)factory.findOrCreateRAAPIReferenceWrapper(r, true);
			if (retVal == null)
				retVal = (StreamStateCommand)factory.findOrCreateRAAPIReferenceWrapper(StreamStateCommand.class, r, true);
			return retVal;
		}
		else
			return null;
	}
	public boolean setStreamStateCommand(StreamStateCommand value)
	{
		boolean ok = true;
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.STREAMSTATE_STREAMSTATECOMMAND);
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
				if (!factory.raapi.deleteLink(rObject, rLinked, factory.STREAMSTATE_STREAMSTATECOMMAND))
					ok = false;
		}
		if (value != null)
			if (!factory.raapi.createLink(rObject, value.rObject, factory.STREAMSTATE_STREAMSTATECOMMAND))
				ok = false;
		return ok;
	}
	public HistoryStream getHistoryStream()
	{
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.STREAMSTATE_HISTORYSTREAM);
		if (it == 0)
			return null;
		long r = factory.raapi.resolveIteratorFirst(it);
		factory.raapi.freeIterator(it);
		if (r != 0) {
			HistoryStream retVal = (HistoryStream)factory.findOrCreateRAAPIReferenceWrapper(r, true);
			if (retVal == null)
				retVal = (HistoryStream)factory.findOrCreateRAAPIReferenceWrapper(HistoryStream.class, r, true);
			return retVal;
		}
		else
			return null;
	}
	public boolean setHistoryStream(HistoryStream value)
	{
		boolean ok = true;
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.STREAMSTATE_HISTORYSTREAM);
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
				if (!factory.raapi.deleteLink(rObject, rLinked, factory.STREAMSTATE_HISTORYSTREAM))
					ok = false;
		}
		if (value != null)
			if (!factory.raapi.createLink(rObject, value.rObject, factory.STREAMSTATE_HISTORYSTREAM))
				ok = false;
		return ok;
	}
}
