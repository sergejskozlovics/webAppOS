// automatically generated
package lv.lumii.tda.kernel.mm; 

import java.util.*;
import lv.lumii.tda.raapi.RAAPI;

public class ModificatingActionCommand
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
				System.err.println("Unable to delete the object "+rObject+" of type TDAKernel::ModificatingActionCommand since the RAAPI wrapper does not take care of this reference.");
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
	ModificatingActionCommand(UndoMetamodelFactory _factory)
	{
		factory = _factory;
		rObject = factory.raapi.createObject(factory.MODIFICATINGACTIONCOMMAND);			
		takeReference = true;
		factory.wrappers.put(rObject, this);
	}

	public ModificatingActionCommand(UndoMetamodelFactory _factory, long _rObject, boolean _takeReference)
	{
		factory = _factory;
		rObject = _rObject;
		takeReference = _takeReference;
		if (takeReference)
			factory.wrappers.put(rObject, this);
	}

	// iterator for instances...
	public static Iterable<? extends ModificatingActionCommand> allObjects()
	{
		return allObjects(UndoMetamodelFactory.eINSTANCE);
	} 

	public static Iterable<? extends ModificatingActionCommand> allObjects(UndoMetamodelFactory factory)
	{
		ArrayList<ModificatingActionCommand> retVal = new ArrayList<ModificatingActionCommand>();
		long it = factory.raapi.getIteratorForAllClassObjects(factory.MODIFICATINGACTIONCOMMAND);
		if (it == 0)
			return retVal;
		long r = factory.raapi.resolveIteratorFirst(it);
		while (r != 0) {
 			ModificatingActionCommand o = (ModificatingActionCommand)factory.findOrCreateRAAPIReferenceWrapper(r, true);
			if (o == null)
				o = (ModificatingActionCommand)factory.findOrCreateRAAPIReferenceWrapper(ModificatingActionCommand.class, r, true);
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
		for (ModificatingActionCommand o : allObjects(factory))
			o.delete();
		return firstObject(factory) == null;
	}

	public static ModificatingActionCommand firstObject()
	{
		return firstObject(UndoMetamodelFactory.eINSTANCE);
	} 

	public static ModificatingActionCommand firstObject(UndoMetamodelFactory factory)
	{
		long it = factory.raapi.getIteratorForAllClassObjects(factory.MODIFICATINGACTIONCOMMAND);
		if (it == 0)
			return null;
		long r = factory.raapi.resolveIteratorFirst(it);
		factory.raapi.freeIterator(it);
		if (r == 0)
			return null;
		else {
			ModificatingActionCommand  retVal = (ModificatingActionCommand)factory.findOrCreateRAAPIReferenceWrapper(r, true);
			if (retVal == null)
				retVal = (ModificatingActionCommand)factory.findOrCreateRAAPIReferenceWrapper(ModificatingActionCommand.class, r, true);
			return retVal;
		}
	} 
 
	public ModificatingAction getModificatingAction()
	{
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.MODIFICATINGACTIONCOMMAND_MODIFICATINGACTION);
		if (it == 0)
			return null;
		long r = factory.raapi.resolveIteratorFirst(it);
		factory.raapi.freeIterator(it);
		if (r != 0) {
			ModificatingAction retVal = (ModificatingAction)factory.findOrCreateRAAPIReferenceWrapper(r, true);
			if (retVal == null)
				retVal = (ModificatingAction)factory.findOrCreateRAAPIReferenceWrapper(ModificatingAction.class, r, true);
			return retVal;
		}
		else
			return null;
	}
	public boolean setModificatingAction(ModificatingAction value)
	{
		boolean ok = true;
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.MODIFICATINGACTIONCOMMAND_MODIFICATINGACTION);
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
				if (!factory.raapi.deleteLink(rObject, rLinked, factory.MODIFICATINGACTIONCOMMAND_MODIFICATINGACTION))
					ok = false;
		}
		if (value != null)
			if (!factory.raapi.createLink(rObject, value.rObject, factory.MODIFICATINGACTIONCOMMAND_MODIFICATINGACTION))
				ok = false;
		return ok;
	}
}
