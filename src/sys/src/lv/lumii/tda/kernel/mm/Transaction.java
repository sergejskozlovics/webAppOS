// automatically generated
package lv.lumii.tda.kernel.mm; 

import java.util.*;
import lv.lumii.tda.raapi.RAAPI;

public class Transaction
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
				System.err.println("Unable to delete the object "+rObject+" of type TDAKernel::Transaction since the RAAPI wrapper does not take care of this reference.");
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
	Transaction(UndoMetamodelFactory _factory)
	{
		super(_factory, _factory.raapi.createObject(_factory.TRANSACTION), true);		
		factory = _factory;
		rObject = super.rObject;
		takeReference = true;
		factory.wrappers.put(rObject, this);
		/*
		factory = _factory;
		rObject = factory.raapi.createObject(factory.TRANSACTION);			
		takeReference = true;
		factory.wrappers.put(rObject, this);
		*/
	}

	public Transaction(UndoMetamodelFactory _factory, long _rObject, boolean _takeReference)
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
	public static Iterable<? extends Transaction> allObjects()
	{
		return allObjects(UndoMetamodelFactory.eINSTANCE);
	} 

	public static Iterable<? extends Transaction> allObjects(UndoMetamodelFactory factory)
	{
		ArrayList<Transaction> retVal = new ArrayList<Transaction>();
		long it = factory.raapi.getIteratorForAllClassObjects(factory.TRANSACTION);
		if (it == 0)
			return retVal;
		long r = factory.raapi.resolveIteratorFirst(it);
		while (r != 0) {
 			Transaction o = (Transaction)factory.findOrCreateRAAPIReferenceWrapper(r, true);
			if (o == null)
				o = (Transaction)factory.findOrCreateRAAPIReferenceWrapper(Transaction.class, r, true);
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
		for (Transaction o : allObjects(factory))
			o.delete();
		return firstObject(factory) == null;
	}

	public static Transaction firstObject()
	{
		return firstObject(UndoMetamodelFactory.eINSTANCE);
	} 

	public static Transaction firstObject(UndoMetamodelFactory factory)
	{
		long it = factory.raapi.getIteratorForAllClassObjects(factory.TRANSACTION);
		if (it == 0)
			return null;
		long r = factory.raapi.resolveIteratorFirst(it);
		factory.raapi.freeIterator(it);
		if (r == 0)
			return null;
		else {
			Transaction  retVal = (Transaction)factory.findOrCreateRAAPIReferenceWrapper(r, true);
			if (retVal == null)
				retVal = (Transaction)factory.findOrCreateRAAPIReferenceWrapper(Transaction.class, r, true);
			return retVal;
		}
	} 
 
	public List<ModificatingAction> getModificatingAction()
	{
		return new UndoMetamodel_RAAPILinkedObjectsList<ModificatingAction>(factory, rObject, factory.TRANSACTION_MODIFICATINGACTION); 
	}
	public boolean setModificatingAction(ModificatingAction value)
	{
		boolean ok = true;
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.TRANSACTION_MODIFICATINGACTION);
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
				if (!factory.raapi.deleteLink(rObject, rLinked, factory.TRANSACTION_MODIFICATINGACTION))
					ok = false;
		}
		if (value != null)
			if (!factory.raapi.createLink(rObject, value.rObject, factory.TRANSACTION_MODIFICATINGACTION))
				ok = false;
		return ok;
	}
	public String getDescription()
	{
		return factory.raapi.getAttributeValue(rObject, factory.TRANSACTION_DESCRIPTION);
	}
	public boolean setDescription(String value)
	{
		if (value == null)
			return factory.raapi.deleteAttributeValue(rObject, factory.TRANSACTION_DESCRIPTION);
		return factory.raapi.setAttributeValue(rObject, factory.TRANSACTION_DESCRIPTION, value.toString());
	}
	public List<Transaction> getUndoDependency()
	{
		return new UndoMetamodel_RAAPILinkedObjectsList<Transaction>(factory, rObject, factory.TRANSACTION_UNDODEPENDENCY); 
	}
	public boolean setUndoDependency(Transaction value)
	{
		boolean ok = true;
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.TRANSACTION_UNDODEPENDENCY);
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
				if (!factory.raapi.deleteLink(rObject, rLinked, factory.TRANSACTION_UNDODEPENDENCY))
					ok = false;
		}
		if (value != null)
			if (!factory.raapi.createLink(rObject, value.rObject, factory.TRANSACTION_UNDODEPENDENCY))
				ok = false;
		return ok;
	}
	public List<Transaction> getRedoDependency()
	{
		return new UndoMetamodel_RAAPILinkedObjectsList<Transaction>(factory, rObject, factory.TRANSACTION_REDODEPENDENCY); 
	}
	public boolean setRedoDependency(Transaction value)
	{
		boolean ok = true;
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.TRANSACTION_REDODEPENDENCY);
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
				if (!factory.raapi.deleteLink(rObject, rLinked, factory.TRANSACTION_REDODEPENDENCY))
					ok = false;
		}
		if (value != null)
			if (!factory.raapi.createLink(rObject, value.rObject, factory.TRANSACTION_REDODEPENDENCY))
				ok = false;
		return ok;
	}
	public List<StreamState> getStreamState()
	{
		return new UndoMetamodel_RAAPILinkedObjectsList<StreamState>(factory, rObject, factory.TRANSACTION_STREAMSTATE); 
	}
	public boolean setStreamState(StreamState value)
	{
		boolean ok = true;
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.TRANSACTION_STREAMSTATE);
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
				if (!factory.raapi.deleteLink(rObject, rLinked, factory.TRANSACTION_STREAMSTATE))
					ok = false;
		}
		if (value != null)
			if (!factory.raapi.createLink(rObject, value.rObject, factory.TRANSACTION_STREAMSTATE))
				ok = false;
		return ok;
	}
	public UndoHistory getUndoHistory()
	{
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.TRANSACTION_UNDOHISTORY);
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
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.TRANSACTION_UNDOHISTORY);
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
				if (!factory.raapi.deleteLink(rObject, rLinked, factory.TRANSACTION_UNDOHISTORY))
					ok = false;
		}
		if (value != null)
			if (!factory.raapi.createLink(rObject, value.rObject, factory.TRANSACTION_UNDOHISTORY))
				ok = false;
		return ok;
	}
}
