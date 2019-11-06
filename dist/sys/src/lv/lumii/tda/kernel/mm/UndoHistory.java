// automatically generated
package lv.lumii.tda.kernel.mm; 

import java.util.*;
import lv.lumii.tda.raapi.RAAPI;

public class UndoHistory
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
				System.err.println("Unable to delete the object "+rObject+" of type TDAKernel::UndoHistory since the RAAPI wrapper does not take care of this reference.");
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
	UndoHistory(UndoMetamodelFactory _factory)
	{
		super(_factory, _factory.raapi.createObject(_factory.UNDOHISTORY), true);		
		factory = _factory;
		rObject = super.rObject;
		takeReference = true;
		factory.wrappers.put(rObject, this);
		/*
		factory = _factory;
		rObject = factory.raapi.createObject(factory.UNDOHISTORY);			
		takeReference = true;
		factory.wrappers.put(rObject, this);
		*/
	}

	public UndoHistory(UndoMetamodelFactory _factory, long _rObject, boolean _takeReference)
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
	public static Iterable<? extends UndoHistory> allObjects()
	{
		return allObjects(UndoMetamodelFactory.eINSTANCE);
	} 

	public static Iterable<? extends UndoHistory> allObjects(UndoMetamodelFactory factory)
	{
		ArrayList<UndoHistory> retVal = new ArrayList<UndoHistory>();
		long it = factory.raapi.getIteratorForAllClassObjects(factory.UNDOHISTORY);
		if (it == 0)
			return retVal;
		long r = factory.raapi.resolveIteratorFirst(it);
		while (r != 0) {
 			UndoHistory o = (UndoHistory)factory.findOrCreateRAAPIReferenceWrapper(r, true);
			if (o == null)
				o = (UndoHistory)factory.findOrCreateRAAPIReferenceWrapper(UndoHistory.class, r, true);
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
		for (UndoHistory o : allObjects(factory))
			o.delete();
		return firstObject(factory) == null;
	}

	public static UndoHistory firstObject()
	{
		return firstObject(UndoMetamodelFactory.eINSTANCE);
	} 

	public static UndoHistory firstObject(UndoMetamodelFactory factory)
	{
		long it = factory.raapi.getIteratorForAllClassObjects(factory.UNDOHISTORY);
		if (it == 0)
			return null;
		long r = factory.raapi.resolveIteratorFirst(it);
		factory.raapi.freeIterator(it);
		if (r == 0)
			return null;
		else {
			UndoHistory  retVal = (UndoHistory)factory.findOrCreateRAAPIReferenceWrapper(r, true);
			if (retVal == null)
				retVal = (UndoHistory)factory.findOrCreateRAAPIReferenceWrapper(UndoHistory.class, r, true);
			return retVal;
		}
	} 
 
	public List<Transaction> getTransaction()
	{
		return new UndoMetamodel_RAAPILinkedObjectsList<Transaction>(factory, rObject, factory.UNDOHISTORY_TRANSACTION); 
	}
	public boolean setTransaction(Transaction value)
	{
		boolean ok = true;
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.UNDOHISTORY_TRANSACTION);
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
				if (!factory.raapi.deleteLink(rObject, rLinked, factory.UNDOHISTORY_TRANSACTION))
					ok = false;
		}
		if (value != null)
			if (!factory.raapi.createLink(rObject, value.rObject, factory.UNDOHISTORY_TRANSACTION))
				ok = false;
		return ok;
	}
	public Transaction getCurrentTransaction()
	{
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.UNDOHISTORY_CURRENTTRANSACTION);
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
	public boolean setCurrentTransaction(Transaction value)
	{
		boolean ok = true;
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.UNDOHISTORY_CURRENTTRANSACTION);
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
				if (!factory.raapi.deleteLink(rObject, rLinked, factory.UNDOHISTORY_CURRENTTRANSACTION))
					ok = false;
		}
		if (value != null)
			if (!factory.raapi.createLink(rObject, value.rObject, factory.UNDOHISTORY_CURRENTTRANSACTION))
				ok = false;
		return ok;
	}
	public List<HistoryStream> getHistoryStream()
	{
		return new UndoMetamodel_RAAPILinkedObjectsList<HistoryStream>(factory, rObject, factory.UNDOHISTORY_HISTORYSTREAM); 
	}
	public boolean setHistoryStream(HistoryStream value)
	{
		boolean ok = true;
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.UNDOHISTORY_HISTORYSTREAM);
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
				if (!factory.raapi.deleteLink(rObject, rLinked, factory.UNDOHISTORY_HISTORYSTREAM))
					ok = false;
		}
		if (value != null)
			if (!factory.raapi.createLink(rObject, value.rObject, factory.UNDOHISTORY_HISTORYSTREAM))
				ok = false;
		return ok;
	}
}
