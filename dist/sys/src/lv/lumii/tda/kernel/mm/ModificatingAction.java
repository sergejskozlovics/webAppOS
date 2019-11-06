// automatically generated
package lv.lumii.tda.kernel.mm; 

import java.util.*;
import lv.lumii.tda.raapi.RAAPI;

public class ModificatingAction
  	implements RAAPIReferenceWrapper
{
	protected UndoMetamodelFactory factory;
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
				System.err.println("Unable to delete the object "+rObject+" of type TDAKernel::ModificatingAction since the RAAPI wrapper does not take care of this reference.");
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
	ModificatingAction(UndoMetamodelFactory _factory)
	{
		factory = _factory;
		rObject = factory.raapi.createObject(factory.MODIFICATINGACTION);			
		takeReference = true;
		factory.wrappers.put(rObject, this);
	}

	public ModificatingAction(UndoMetamodelFactory _factory, long _rObject, boolean _takeReference)
	{
		factory = _factory;
		rObject = _rObject;
		takeReference = _takeReference;
		if (takeReference)
			factory.wrappers.put(rObject, this);
	}

	// iterator for instances...
	public static Iterable<? extends ModificatingAction> allObjects()
	{
		return allObjects(UndoMetamodelFactory.eINSTANCE);
	} 

	public static Iterable<? extends ModificatingAction> allObjects(UndoMetamodelFactory factory)
	{
		ArrayList<ModificatingAction> retVal = new ArrayList<ModificatingAction>();
		long it = factory.raapi.getIteratorForAllClassObjects(factory.MODIFICATINGACTION);
		if (it == 0)
			return retVal;
		long r = factory.raapi.resolveIteratorFirst(it);
		while (r != 0) {
 			ModificatingAction o = (ModificatingAction)factory.findOrCreateRAAPIReferenceWrapper(r, true);
			if (o == null)
				o = (ModificatingAction)factory.findOrCreateRAAPIReferenceWrapper(ModificatingAction.class, r, true);
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
		for (ModificatingAction o : allObjects(factory))
			o.delete();
		return firstObject(factory) == null;
	}

	public static ModificatingAction firstObject()
	{
		return firstObject(UndoMetamodelFactory.eINSTANCE);
	} 

	public static ModificatingAction firstObject(UndoMetamodelFactory factory)
	{
		long it = factory.raapi.getIteratorForAllClassObjects(factory.MODIFICATINGACTION);
		if (it == 0)
			return null;
		long r = factory.raapi.resolveIteratorFirst(it);
		factory.raapi.freeIterator(it);
		if (r == 0)
			return null;
		else {
			ModificatingAction  retVal = (ModificatingAction)factory.findOrCreateRAAPIReferenceWrapper(r, true);
			if (retVal == null)
				retVal = (ModificatingAction)factory.findOrCreateRAAPIReferenceWrapper(ModificatingAction.class, r, true);
			return retVal;
		}
	} 
 
	public Transaction getTransaction()
	{
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.MODIFICATINGACTION_TRANSACTION);
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
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.MODIFICATINGACTION_TRANSACTION);
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
				if (!factory.raapi.deleteLink(rObject, rLinked, factory.MODIFICATINGACTION_TRANSACTION))
					ok = false;
		}
		if (value != null)
			if (!factory.raapi.createLink(rObject, value.rObject, factory.MODIFICATINGACTION_TRANSACTION))
				ok = false;
		return ok;
	}
	public ModificatingActionCommand getModificatingActionCommand()
	{
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.MODIFICATINGACTION_MODIFICATINGACTIONCOMMAND);
		if (it == 0)
			return null;
		long r = factory.raapi.resolveIteratorFirst(it);
		factory.raapi.freeIterator(it);
		if (r != 0) {
			ModificatingActionCommand retVal = (ModificatingActionCommand)factory.findOrCreateRAAPIReferenceWrapper(r, true);
			if (retVal == null)
				retVal = (ModificatingActionCommand)factory.findOrCreateRAAPIReferenceWrapper(ModificatingActionCommand.class, r, true);
			return retVal;
		}
		else
			return null;
	}
	public boolean setModificatingActionCommand(ModificatingActionCommand value)
	{
		boolean ok = true;
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.MODIFICATINGACTION_MODIFICATINGACTIONCOMMAND);
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
				if (!factory.raapi.deleteLink(rObject, rLinked, factory.MODIFICATINGACTION_MODIFICATINGACTIONCOMMAND))
					ok = false;
		}
		if (value != null)
			if (!factory.raapi.createLink(rObject, value.rObject, factory.MODIFICATINGACTION_MODIFICATINGACTIONCOMMAND))
				ok = false;
		return ok;
	}
}
