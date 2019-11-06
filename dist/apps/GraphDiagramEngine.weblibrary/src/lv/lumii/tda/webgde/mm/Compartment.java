// automatically generated
package lv.lumii.tda.webgde.mm; 

import java.util.*;
import lv.lumii.tda.raapi.RAAPI;

public class Compartment
	extends PresentationElement
  	implements RAAPIReferenceWrapper
{
	/* these references are defined only in the top-most superclass:
	protected GraphDiagramEngineMetamodelFactory factory;
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
				System.err.println("Unable to delete the object "+rObject+" of type Compartment since the RAAPI wrapper does not take care of this reference.");
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
	Compartment(GraphDiagramEngineMetamodelFactory _factory)
	{
		super(_factory, _factory.raapi.createObject(_factory.COMPARTMENT), true);		
		factory = _factory;
		rObject = super.rObject;
		takeReference = true;
		factory.wrappers.put(rObject, this);
		/*
		factory = _factory;
		rObject = factory.raapi.createObject(factory.COMPARTMENT);			
		takeReference = true;
		factory.wrappers.put(rObject, this);
		*/
	}

	public Compartment(GraphDiagramEngineMetamodelFactory _factory, long _rObject, boolean _takeReference)
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
	public static Iterable<? extends Compartment> allObjects()
	{
		return allObjects(GraphDiagramEngineMetamodelFactory.eINSTANCE);
	} 

	public static Iterable<? extends Compartment> allObjects(GraphDiagramEngineMetamodelFactory factory)
	{
		ArrayList<Compartment> retVal = new ArrayList<Compartment>();
		long it = factory.raapi.getIteratorForAllClassObjects(factory.COMPARTMENT);
		if (it == 0)
			return retVal;
		long r = factory.raapi.resolveIteratorFirst(it);
		while (r != 0) {
 			Compartment o = (Compartment)factory.findOrCreateRAAPIReferenceWrapper(r, true);
			if (o == null)
				o = (Compartment)factory.findOrCreateRAAPIReferenceWrapper(Compartment.class, r, true);
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
		for (Compartment o : allObjects(factory))
			o.delete();
		return firstObject(factory) == null;
	}

	public static Compartment firstObject()
	{
		return firstObject(GraphDiagramEngineMetamodelFactory.eINSTANCE);
	} 

	public static Compartment firstObject(GraphDiagramEngineMetamodelFactory factory)
	{
		long it = factory.raapi.getIteratorForAllClassObjects(factory.COMPARTMENT);
		if (it == 0)
			return null;
		long r = factory.raapi.resolveIteratorFirst(it);
		factory.raapi.freeIterator(it);
		if (r == 0)
			return null;
		else {
			Compartment  retVal = (Compartment)factory.findOrCreateRAAPIReferenceWrapper(r, true);
			if (retVal == null)
				retVal = (Compartment)factory.findOrCreateRAAPIReferenceWrapper(Compartment.class, r, true);
			return retVal;
		}
	} 
 
	public String getInput()
	{
		return factory.raapi.getAttributeValue(rObject, factory.COMPARTMENT_INPUT);
	}
	public boolean setInput(String value)
	{
		if (value == null)
			return factory.raapi.deleteAttributeValue(rObject, factory.COMPARTMENT_INPUT);
		return factory.raapi.setAttributeValue(rObject, factory.COMPARTMENT_INPUT, value.toString());
	}
	public String getStyle()
	{
		return factory.raapi.getAttributeValue(rObject, factory.COMPARTMENT_STYLE);
	}
	public boolean setStyle(String value)
	{
		if (value == null)
			return factory.raapi.deleteAttributeValue(rObject, factory.COMPARTMENT_STYLE);
		return factory.raapi.setAttributeValue(rObject, factory.COMPARTMENT_STYLE, value.toString());
	}
	public String getValue()
	{
		return factory.raapi.getAttributeValue(rObject, factory.COMPARTMENT_VALUE);
	}
	public boolean setValue(String value)
	{
		if (value == null)
			return factory.raapi.deleteAttributeValue(rObject, factory.COMPARTMENT_VALUE);
		return factory.raapi.setAttributeValue(rObject, factory.COMPARTMENT_VALUE, value.toString());
	}
	public String getIsGroup()
	{
		return factory.raapi.getAttributeValue(rObject, factory.COMPARTMENT_ISGROUP);
	}
	public boolean setIsGroup(String value)
	{
		if (value == null)
			return factory.raapi.deleteAttributeValue(rObject, factory.COMPARTMENT_ISGROUP);
		return factory.raapi.setAttributeValue(rObject, factory.COMPARTMENT_ISGROUP, value.toString());
	}
	public List<CompartStyle> getCompartStyle()
	{
		return new GraphDiagramEngineMetamodel_RAAPILinkedObjectsList<CompartStyle>(factory, rObject, factory.COMPARTMENT_COMPARTSTYLE); 
	}
	public boolean setCompartStyle(CompartStyle value)
	{
		boolean ok = true;
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.COMPARTMENT_COMPARTSTYLE);
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
				if (!factory.raapi.deleteLink(rObject, rLinked, factory.COMPARTMENT_COMPARTSTYLE))
					ok = false;
		}
		if (value != null)
			if (!factory.raapi.createLink(rObject, value.rObject, factory.COMPARTMENT_COMPARTSTYLE))
				ok = false;
		return ok;
	}
	public Element getElement()
	{
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.COMPARTMENT_ELEMENT);
		if (it == 0)
			return null;
		long r = factory.raapi.resolveIteratorFirst(it);
		factory.raapi.freeIterator(it);
		if (r != 0) {
			Element retVal = (Element)factory.findOrCreateRAAPIReferenceWrapper(r, true);
			if (retVal == null)
				retVal = (Element)factory.findOrCreateRAAPIReferenceWrapper(Element.class, r, true);
			return retVal;
		}
		else
			return null;
	}
	public boolean setElement(Element value)
	{
		boolean ok = true;
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.COMPARTMENT_ELEMENT);
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
				if (!factory.raapi.deleteLink(rObject, rLinked, factory.COMPARTMENT_ELEMENT))
					ok = false;
		}
		if (value != null)
			if (!factory.raapi.createLink(rObject, value.rObject, factory.COMPARTMENT_ELEMENT))
				ok = false;
		return ok;
	}
	public List<Compartment> getParentCompartment()
	{
		return new GraphDiagramEngineMetamodel_RAAPILinkedObjectsList<Compartment>(factory, rObject, factory.COMPARTMENT_PARENTCOMPARTMENT); 
	}
	public boolean setParentCompartment(Compartment value)
	{
		boolean ok = true;
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.COMPARTMENT_PARENTCOMPARTMENT);
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
				if (!factory.raapi.deleteLink(rObject, rLinked, factory.COMPARTMENT_PARENTCOMPARTMENT))
					ok = false;
		}
		if (value != null)
			if (!factory.raapi.createLink(rObject, value.rObject, factory.COMPARTMENT_PARENTCOMPARTMENT))
				ok = false;
		return ok;
	}
	public List<Compartment> getSubCompartment()
	{
		return new GraphDiagramEngineMetamodel_RAAPILinkedObjectsList<Compartment>(factory, rObject, factory.COMPARTMENT_SUBCOMPARTMENT); 
	}
	public boolean setSubCompartment(Compartment value)
	{
		boolean ok = true;
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.COMPARTMENT_SUBCOMPARTMENT);
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
				if (!factory.raapi.deleteLink(rObject, rLinked, factory.COMPARTMENT_SUBCOMPARTMENT))
					ok = false;
		}
		if (value != null)
			if (!factory.raapi.createLink(rObject, value.rObject, factory.COMPARTMENT_SUBCOMPARTMENT))
				ok = false;
		return ok;
	}
}
