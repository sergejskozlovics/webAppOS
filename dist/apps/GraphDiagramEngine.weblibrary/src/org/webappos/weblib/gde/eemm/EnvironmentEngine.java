// automatically generated
package org.webappos.weblib.gde.eemm; 

import java.util.*;
import lv.lumii.tda.raapi.RAAPI;

public class EnvironmentEngine
  	implements RAAPIReferenceWrapper
{
	protected EnvironmentEngineMetamodelFactory factory;
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
				System.err.println("Unable to delete the object "+rObject+" of type EnvironmentEngine since the RAAPI wrapper does not take care of this reference.");
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
	EnvironmentEngine(EnvironmentEngineMetamodelFactory _factory)
	{
		factory = _factory;
		rObject = factory.raapi.createObject(factory.ENVIRONMENTENGINE);			
		takeReference = true;
		factory.wrappers.put(rObject, this);
	}

	public EnvironmentEngine(EnvironmentEngineMetamodelFactory _factory, long _rObject, boolean _takeReference)
	{
		factory = _factory;
		rObject = _rObject;
		takeReference = _takeReference;
		if (takeReference)
			factory.wrappers.put(rObject, this);
	}

	// iterator for instances...
	public static Iterable<? extends EnvironmentEngine> allObjects()
	{
		return allObjects(EnvironmentEngineMetamodelFactory.eINSTANCE);
	} 

	public static Iterable<? extends EnvironmentEngine> allObjects(EnvironmentEngineMetamodelFactory factory)
	{
		ArrayList<EnvironmentEngine> retVal = new ArrayList<EnvironmentEngine>();
		long it = factory.raapi.getIteratorForAllClassObjects(factory.ENVIRONMENTENGINE);
		if (it == 0)
			return retVal;
		long r = factory.raapi.resolveIteratorFirst(it);
		while (r != 0) {
 			EnvironmentEngine o = (EnvironmentEngine)factory.findOrCreateRAAPIReferenceWrapper(r, true);
			if (o == null)
				o = (EnvironmentEngine)factory.findOrCreateRAAPIReferenceWrapper(EnvironmentEngine.class, r, true);
			if (o != null)
				retVal.add(o);
			r = factory.raapi.resolveIteratorNext(it);
		}
		factory.raapi.freeIterator(it);
		return retVal;
	}

	public static boolean deleteAllObjects()
	{
		return deleteAllObjects(EnvironmentEngineMetamodelFactory.eINSTANCE);
	}

	public static boolean deleteAllObjects(EnvironmentEngineMetamodelFactory factory)
	{
		for (EnvironmentEngine o : allObjects(factory))
			o.delete();
		return firstObject(factory) == null;
	}

	public static EnvironmentEngine firstObject()
	{
		return firstObject(EnvironmentEngineMetamodelFactory.eINSTANCE);
	} 

	public static EnvironmentEngine firstObject(EnvironmentEngineMetamodelFactory factory)
	{
		long it = factory.raapi.getIteratorForAllClassObjects(factory.ENVIRONMENTENGINE);
		if (it == 0)
			return null;
		long r = factory.raapi.resolveIteratorFirst(it);
		factory.raapi.freeIterator(it);
		if (r == 0)
			return null;
		else {
			EnvironmentEngine  retVal = (EnvironmentEngine)factory.findOrCreateRAAPIReferenceWrapper(r, true);
			if (retVal == null)
				retVal = (EnvironmentEngine)factory.findOrCreateRAAPIReferenceWrapper(EnvironmentEngine.class, r, true);
			return retVal;
		}
	} 
 
	public String getOnProjectOpenedEvent()
	{
		return factory.raapi.getAttributeValue(rObject, factory.ENVIRONMENTENGINE_ONPROJECTOPENEDEVENT);
	}
	public boolean setOnProjectOpenedEvent(String value)
	{
		if (value == null)
			return factory.raapi.deleteAttributeValue(rObject, factory.ENVIRONMENTENGINE_ONPROJECTOPENEDEVENT);
		return factory.raapi.setAttributeValue(rObject, factory.ENVIRONMENTENGINE_ONPROJECTOPENEDEVENT, value.toString());
	}
	public String getOnProjectClosingEvent()
	{
		return factory.raapi.getAttributeValue(rObject, factory.ENVIRONMENTENGINE_ONPROJECTCLOSINGEVENT);
	}
	public boolean setOnProjectClosingEvent(String value)
	{
		if (value == null)
			return factory.raapi.deleteAttributeValue(rObject, factory.ENVIRONMENTENGINE_ONPROJECTCLOSINGEVENT);
		return factory.raapi.setAttributeValue(rObject, factory.ENVIRONMENTENGINE_ONPROJECTCLOSINGEVENT, value.toString());
	}
	public List<Option> getOption()
	{
		return new EnvironmentEngineMetamodel_RAAPILinkedObjectsList<Option>(factory, rObject, factory.ENVIRONMENTENGINE_OPTION); 
	}
	public boolean setOption(Option value)
	{
		boolean ok = true;
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.ENVIRONMENTENGINE_OPTION);
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
				if (!factory.raapi.deleteLink(rObject, rLinked, factory.ENVIRONMENTENGINE_OPTION))
					ok = false;
		}
		if (value != null)
			if (!factory.raapi.createLink(rObject, value.rObject, factory.ENVIRONMENTENGINE_OPTION))
				ok = false;
		return ok;
	}
	public List<Frame> getFrame()
	{
		return new EnvironmentEngineMetamodel_RAAPILinkedObjectsList<Frame>(factory, rObject, factory.ENVIRONMENTENGINE_FRAME); 
	}
	public boolean setFrame(Frame value)
	{
		boolean ok = true;
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.ENVIRONMENTENGINE_FRAME);
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
				if (!factory.raapi.deleteLink(rObject, rLinked, factory.ENVIRONMENTENGINE_FRAME))
					ok = false;
		}
		if (value != null)
			if (!factory.raapi.createLink(rObject, value.rObject, factory.ENVIRONMENTENGINE_FRAME))
				ok = false;
		return ok;
	}
	public List<RefreshOptionsCommand> getRefreshOptionsCommand()
	{
		return new EnvironmentEngineMetamodel_RAAPILinkedObjectsList<RefreshOptionsCommand>(factory, rObject, factory.ENVIRONMENTENGINE_REFRESHOPTIONSCOMMAND); 
	}
	public boolean setRefreshOptionsCommand(RefreshOptionsCommand value)
	{
		boolean ok = true;
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.ENVIRONMENTENGINE_REFRESHOPTIONSCOMMAND);
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
				if (!factory.raapi.deleteLink(rObject, rLinked, factory.ENVIRONMENTENGINE_REFRESHOPTIONSCOMMAND))
					ok = false;
		}
		if (value != null)
			if (!factory.raapi.createLink(rObject, value.rObject, factory.ENVIRONMENTENGINE_REFRESHOPTIONSCOMMAND))
				ok = false;
		return ok;
	}
}
