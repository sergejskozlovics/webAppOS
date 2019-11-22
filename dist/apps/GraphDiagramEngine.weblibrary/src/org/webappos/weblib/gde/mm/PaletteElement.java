// automatically generated
package org.webappos.weblib.gde.mm; 

import java.util.*;
import lv.lumii.tda.raapi.RAAPI;

public class PaletteElement
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
				System.err.println("Unable to delete the object "+rObject+" of type PaletteElement since the RAAPI wrapper does not take care of this reference.");
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
	PaletteElement(GraphDiagramEngineMetamodelFactory _factory)
	{
		super(_factory, _factory.raapi.createObject(_factory.PALETTEELEMENT), true);		
		factory = _factory;
		rObject = super.rObject;
		takeReference = true;
		factory.wrappers.put(rObject, this);
		/*
		factory = _factory;
		rObject = factory.raapi.createObject(factory.PALETTEELEMENT);			
		takeReference = true;
		factory.wrappers.put(rObject, this);
		*/
	}

	public PaletteElement(GraphDiagramEngineMetamodelFactory _factory, long _rObject, boolean _takeReference)
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
	public static Iterable<? extends PaletteElement> allObjects()
	{
		return allObjects(GraphDiagramEngineMetamodelFactory.eINSTANCE);
	} 

	public static Iterable<? extends PaletteElement> allObjects(GraphDiagramEngineMetamodelFactory factory)
	{
		ArrayList<PaletteElement> retVal = new ArrayList<PaletteElement>();
		long it = factory.raapi.getIteratorForAllClassObjects(factory.PALETTEELEMENT);
		if (it == 0)
			return retVal;
		long r = factory.raapi.resolveIteratorFirst(it);
		while (r != 0) {
 			PaletteElement o = (PaletteElement)factory.findOrCreateRAAPIReferenceWrapper(r, true);
			if (o == null)
				o = (PaletteElement)factory.findOrCreateRAAPIReferenceWrapper(PaletteElement.class, r, true);
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
		for (PaletteElement o : allObjects(factory))
			o.delete();
		return firstObject(factory) == null;
	}

	public static PaletteElement firstObject()
	{
		return firstObject(GraphDiagramEngineMetamodelFactory.eINSTANCE);
	} 

	public static PaletteElement firstObject(GraphDiagramEngineMetamodelFactory factory)
	{
		long it = factory.raapi.getIteratorForAllClassObjects(factory.PALETTEELEMENT);
		if (it == 0)
			return null;
		long r = factory.raapi.resolveIteratorFirst(it);
		factory.raapi.freeIterator(it);
		if (r == 0)
			return null;
		else {
			PaletteElement  retVal = (PaletteElement)factory.findOrCreateRAAPIReferenceWrapper(r, true);
			if (retVal == null)
				retVal = (PaletteElement)factory.findOrCreateRAAPIReferenceWrapper(PaletteElement.class, r, true);
			return retVal;
		}
	} 
 
	public String getCaption()
	{
		return factory.raapi.getAttributeValue(rObject, factory.PALETTEELEMENT_CAPTION);
	}
	public boolean setCaption(String value)
	{
		if (value == null)
			return factory.raapi.deleteAttributeValue(rObject, factory.PALETTEELEMENT_CAPTION);
		return factory.raapi.setAttributeValue(rObject, factory.PALETTEELEMENT_CAPTION, value.toString());
	}
	public String getPicture()
	{
		return factory.raapi.getAttributeValue(rObject, factory.PALETTEELEMENT_PICTURE);
	}
	public boolean setPicture(String value)
	{
		if (value == null)
			return factory.raapi.deleteAttributeValue(rObject, factory.PALETTEELEMENT_PICTURE);
		return factory.raapi.setAttributeValue(rObject, factory.PALETTEELEMENT_PICTURE, value.toString());
	}
	public Palette getPalette()
	{
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.PALETTEELEMENT_PALETTE);
		if (it == 0)
			return null;
		long r = factory.raapi.resolveIteratorFirst(it);
		factory.raapi.freeIterator(it);
		if (r != 0) {
			Palette retVal = (Palette)factory.findOrCreateRAAPIReferenceWrapper(r, true);
			if (retVal == null)
				retVal = (Palette)factory.findOrCreateRAAPIReferenceWrapper(Palette.class, r, true);
			return retVal;
		}
		else
			return null;
	}
	public boolean setPalette(Palette value)
	{
		boolean ok = true;
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.PALETTEELEMENT_PALETTE);
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
				if (!factory.raapi.deleteLink(rObject, rLinked, factory.PALETTEELEMENT_PALETTE))
					ok = false;
		}
		if (value != null)
			if (!factory.raapi.createLink(rObject, value.rObject, factory.PALETTEELEMENT_PALETTE))
				ok = false;
		return ok;
	}
}
