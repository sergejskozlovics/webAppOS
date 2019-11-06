// automatically generated
package lv.lumii.tda.ee.mm; 

import java.util.*;
import lv.lumii.tda.raapi.RAAPI;

public class EnvironmentEngine
	extends Engine
  	implements RAAPIReferenceWrapper
{
	/* these references are defined only in the top-most superclass:
	protected EnvironmentEngineMetamodelFactory factory;
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
		super(_factory, _factory.raapi.createObject(_factory.ENVIRONMENTENGINE), true);		
		factory = _factory;
		rObject = super.rObject;
		takeReference = true;
		factory.wrappers.put(rObject, this);
		/*
		factory = _factory;
		rObject = factory.raapi.createObject(factory.ENVIRONMENTENGINE);			
		takeReference = true;
		factory.wrappers.put(rObject, this);
		*/
	}

	public EnvironmentEngine(EnvironmentEngineMetamodelFactory _factory, long _rObject, boolean _takeReference)
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
 
	public String getLanguage()
	{
		return factory.raapi.getAttributeValue(rObject, factory.ENVIRONMENTENGINE_LANGUAGE);
	}
	public boolean setLanguage(String value)
	{
		if (value == null)
			return factory.raapi.deleteAttributeValue(rObject, factory.ENVIRONMENTENGINE_LANGUAGE);
		return factory.raapi.setAttributeValue(rObject, factory.ENVIRONMENTENGINE_LANGUAGE, value.toString());
	}
	public String getCountry()
	{
		return factory.raapi.getAttributeValue(rObject, factory.ENVIRONMENTENGINE_COUNTRY);
	}
	public boolean setCountry(String value)
	{
		if (value == null)
			return factory.raapi.deleteAttributeValue(rObject, factory.ENVIRONMENTENGINE_COUNTRY);
		return factory.raapi.setAttributeValue(rObject, factory.ENVIRONMENTENGINE_COUNTRY, value.toString());
	}
	public Boolean getAnyUnsavedChanges()
	{
		try { 
			String value = factory.raapi.getAttributeValue(rObject, factory.ENVIRONMENTENGINE_ANYUNSAVEDCHANGES);
			if (value == null)
				return null;
			return Boolean.parseBoolean(value);
		}
		catch (Throwable t)
		{
			return null;
		} 
	}
	public boolean setAnyUnsavedChanges(Boolean value)
	{
		if (value == null)
			return factory.raapi.deleteAttributeValue(rObject, factory.ENVIRONMENTENGINE_ANYUNSAVEDCHANGES);
		return factory.raapi.setAttributeValue(rObject, factory.ENVIRONMENTENGINE_ANYUNSAVEDCHANGES, value.toString());
	}
	public String getCommonBinDirectory()
	{
		return factory.raapi.getAttributeValue(rObject, factory.ENVIRONMENTENGINE_COMMONBINDIRECTORY);
	}
	public boolean setCommonBinDirectory(String value)
	{
		if (value == null)
			return factory.raapi.deleteAttributeValue(rObject, factory.ENVIRONMENTENGINE_COMMONBINDIRECTORY);
		return factory.raapi.setAttributeValue(rObject, factory.ENVIRONMENTENGINE_COMMONBINDIRECTORY, value.toString());
	}
	public String getSpecificBinDirectory()
	{
		return factory.raapi.getAttributeValue(rObject, factory.ENVIRONMENTENGINE_SPECIFICBINDIRECTORY);
	}
	public boolean setSpecificBinDirectory(String value)
	{
		if (value == null)
			return factory.raapi.deleteAttributeValue(rObject, factory.ENVIRONMENTENGINE_SPECIFICBINDIRECTORY);
		return factory.raapi.setAttributeValue(rObject, factory.ENVIRONMENTENGINE_SPECIFICBINDIRECTORY, value.toString());
	}
	public String getProjectDirectory()
	{
		return factory.raapi.getAttributeValue(rObject, factory.ENVIRONMENTENGINE_PROJECTDIRECTORY);
	}
	public boolean setProjectDirectory(String value)
	{
		if (value == null)
			return factory.raapi.deleteAttributeValue(rObject, factory.ENVIRONMENTENGINE_PROJECTDIRECTORY);
		return factory.raapi.setAttributeValue(rObject, factory.ENVIRONMENTENGINE_PROJECTDIRECTORY, value.toString());
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
	public String getDefaultSaveDirectory()
	{
		return factory.raapi.getAttributeValue(rObject, factory.ENVIRONMENTENGINE_DEFAULTSAVEDIRECTORY);
	}
	public boolean setDefaultSaveDirectory(String value)
	{
		if (value == null)
			return factory.raapi.deleteAttributeValue(rObject, factory.ENVIRONMENTENGINE_DEFAULTSAVEDIRECTORY);
		return factory.raapi.setAttributeValue(rObject, factory.ENVIRONMENTENGINE_DEFAULTSAVEDIRECTORY, value.toString());
	}
	public String getStatus()
	{
		return factory.raapi.getAttributeValue(rObject, factory.ENVIRONMENTENGINE_STATUS);
	}
	public boolean setStatus(String value)
	{
		if (value == null)
			return factory.raapi.deleteAttributeValue(rObject, factory.ENVIRONMENTENGINE_STATUS);
		return factory.raapi.setAttributeValue(rObject, factory.ENVIRONMENTENGINE_STATUS, value.toString());
	}
	public String getCloudLocation()
	{
		return factory.raapi.getAttributeValue(rObject, factory.ENVIRONMENTENGINE_CLOUDLOCATION);
	}
	public boolean setCloudLocation(String value)
	{
		if (value == null)
			return factory.raapi.deleteAttributeValue(rObject, factory.ENVIRONMENTENGINE_CLOUDLOCATION);
		return factory.raapi.setAttributeValue(rObject, factory.ENVIRONMENTENGINE_CLOUDLOCATION, value.toString());
	}
	public String getClientSessionId()
	{
		return factory.raapi.getAttributeValue(rObject, factory.ENVIRONMENTENGINE_CLIENTSESSIONID);
	}
	public boolean setClientSessionId(String value)
	{
		if (value == null)
			return factory.raapi.deleteAttributeValue(rObject, factory.ENVIRONMENTENGINE_CLIENTSESSIONID);
		return factory.raapi.setAttributeValue(rObject, factory.ENVIRONMENTENGINE_CLIENTSESSIONID, value.toString());
	}
	public Integer getClientActionIndex()
	{
		try { 
			String value = factory.raapi.getAttributeValue(rObject, factory.ENVIRONMENTENGINE_CLIENTACTIONINDEX);
			if (value == null)
				return null;
			return Integer.parseInt(value);
		}
		catch (Throwable t)
		{
			return null;
		} 
	}
	public boolean setClientActionIndex(Integer value)
	{
		if (value == null)
			return factory.raapi.deleteAttributeValue(rObject, factory.ENVIRONMENTENGINE_CLIENTACTIONINDEX);
		return factory.raapi.setAttributeValue(rObject, factory.ENVIRONMENTENGINE_CLIENTACTIONINDEX, value.toString());
	}
}
