// automatically generated
package lv.lumii.tda.webde.mm; 

import java.util.*;
import lv.lumii.tda.raapi.RAAPI;

public class D_SHARP_Component
	extends D_SHARP_EventSource
  	implements RAAPIReferenceWrapper
{
	/* these references are defined only in the top-most superclass:
	protected DialogEngineMetamodelFactory factory;
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
				System.err.println("Unable to delete the object "+rObject+" of type D_SHARP_Component since the RAAPI wrapper does not take care of this reference.");
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
	D_SHARP_Component(DialogEngineMetamodelFactory _factory)
	{
		super(_factory, _factory.raapi.createObject(_factory.D_SHARP_COMPONENT), true);		
		factory = _factory;
		rObject = super.rObject;
		takeReference = true;
		factory.wrappers.put(rObject, this);
		/*
		factory = _factory;
		rObject = factory.raapi.createObject(factory.D_SHARP_COMPONENT);			
		takeReference = true;
		factory.wrappers.put(rObject, this);
		*/
	}

	public D_SHARP_Component(DialogEngineMetamodelFactory _factory, long _rObject, boolean _takeReference)
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
	public static Iterable<? extends D_SHARP_Component> allObjects()
	{
		return allObjects(DialogEngineMetamodelFactory.eINSTANCE);
	} 

	public static Iterable<? extends D_SHARP_Component> allObjects(DialogEngineMetamodelFactory factory)
	{
		ArrayList<D_SHARP_Component> retVal = new ArrayList<D_SHARP_Component>();
		long it = factory.raapi.getIteratorForAllClassObjects(factory.D_SHARP_COMPONENT);
		if (it == 0)
			return retVal;
		long r = factory.raapi.resolveIteratorFirst(it);
		while (r != 0) {
 			D_SHARP_Component o = (D_SHARP_Component)factory.findOrCreateRAAPIReferenceWrapper(r, true);
			if (o == null)
				o = (D_SHARP_Component)factory.findOrCreateRAAPIReferenceWrapper(D_SHARP_Component.class, r, true);
			if (o != null)
				retVal.add(o);
			r = factory.raapi.resolveIteratorNext(it);
		}
		factory.raapi.freeIterator(it);
		return retVal;
	}

	public static boolean deleteAllObjects()
	{
		return deleteAllObjects(DialogEngineMetamodelFactory.eINSTANCE);
	}

	public static boolean deleteAllObjects(DialogEngineMetamodelFactory factory)
	{
		for (D_SHARP_Component o : allObjects(factory))
			o.delete();
		return firstObject(factory) == null;
	}

	public static D_SHARP_Component firstObject()
	{
		return firstObject(DialogEngineMetamodelFactory.eINSTANCE);
	} 

	public static D_SHARP_Component firstObject(DialogEngineMetamodelFactory factory)
	{
		long it = factory.raapi.getIteratorForAllClassObjects(factory.D_SHARP_COMPONENT);
		if (it == 0)
			return null;
		long r = factory.raapi.resolveIteratorFirst(it);
		factory.raapi.freeIterator(it);
		if (r == 0)
			return null;
		else {
			D_SHARP_Component  retVal = (D_SHARP_Component)factory.findOrCreateRAAPIReferenceWrapper(r, true);
			if (retVal == null)
				retVal = (D_SHARP_Component)factory.findOrCreateRAAPIReferenceWrapper(D_SHARP_Component.class, r, true);
			return retVal;
		}
	} 
 
	public String getId()
	{
		return factory.raapi.getAttributeValue(rObject, factory.D_SHARP_COMPONENT_ID);
	}
	public boolean setId(String value)
	{
		if (value == null)
			return factory.raapi.deleteAttributeValue(rObject, factory.D_SHARP_COMPONENT_ID);
		return factory.raapi.setAttributeValue(rObject, factory.D_SHARP_COMPONENT_ID, value.toString());
	}
	public Boolean getEnabled()
	{
		try { 
			String value = factory.raapi.getAttributeValue(rObject, factory.D_SHARP_COMPONENT_ENABLED);
			if (value == null)
				return null;
			return Boolean.parseBoolean(value);
		}
		catch (Throwable t)
		{
			return null;
		} 
	}
	public boolean setEnabled(Boolean value)
	{
		if (value == null)
			return factory.raapi.deleteAttributeValue(rObject, factory.D_SHARP_COMPONENT_ENABLED);
		return factory.raapi.setAttributeValue(rObject, factory.D_SHARP_COMPONENT_ENABLED, value.toString());
	}
	public Integer getMinimumWidth()
	{
		try { 
			String value = factory.raapi.getAttributeValue(rObject, factory.D_SHARP_COMPONENT_MINIMUMWIDTH);
			if (value == null)
				return null;
			return Integer.parseInt(value);
		}
		catch (Throwable t)
		{
			return null;
		} 
	}
	public boolean setMinimumWidth(Integer value)
	{
		if (value == null)
			return factory.raapi.deleteAttributeValue(rObject, factory.D_SHARP_COMPONENT_MINIMUMWIDTH);
		return factory.raapi.setAttributeValue(rObject, factory.D_SHARP_COMPONENT_MINIMUMWIDTH, value.toString());
	}
	public Integer getMinimumHeight()
	{
		try { 
			String value = factory.raapi.getAttributeValue(rObject, factory.D_SHARP_COMPONENT_MINIMUMHEIGHT);
			if (value == null)
				return null;
			return Integer.parseInt(value);
		}
		catch (Throwable t)
		{
			return null;
		} 
	}
	public boolean setMinimumHeight(Integer value)
	{
		if (value == null)
			return factory.raapi.deleteAttributeValue(rObject, factory.D_SHARP_COMPONENT_MINIMUMHEIGHT);
		return factory.raapi.setAttributeValue(rObject, factory.D_SHARP_COMPONENT_MINIMUMHEIGHT, value.toString());
	}
	public Integer getPreferredWidth()
	{
		try { 
			String value = factory.raapi.getAttributeValue(rObject, factory.D_SHARP_COMPONENT_PREFERREDWIDTH);
			if (value == null)
				return null;
			return Integer.parseInt(value);
		}
		catch (Throwable t)
		{
			return null;
		} 
	}
	public boolean setPreferredWidth(Integer value)
	{
		if (value == null)
			return factory.raapi.deleteAttributeValue(rObject, factory.D_SHARP_COMPONENT_PREFERREDWIDTH);
		return factory.raapi.setAttributeValue(rObject, factory.D_SHARP_COMPONENT_PREFERREDWIDTH, value.toString());
	}
	public Integer getPreferredHeight()
	{
		try { 
			String value = factory.raapi.getAttributeValue(rObject, factory.D_SHARP_COMPONENT_PREFERREDHEIGHT);
			if (value == null)
				return null;
			return Integer.parseInt(value);
		}
		catch (Throwable t)
		{
			return null;
		} 
	}
	public boolean setPreferredHeight(Integer value)
	{
		if (value == null)
			return factory.raapi.deleteAttributeValue(rObject, factory.D_SHARP_COMPONENT_PREFERREDHEIGHT);
		return factory.raapi.setAttributeValue(rObject, factory.D_SHARP_COMPONENT_PREFERREDHEIGHT, value.toString());
	}
	public Integer getMaximumWidth()
	{
		try { 
			String value = factory.raapi.getAttributeValue(rObject, factory.D_SHARP_COMPONENT_MAXIMUMWIDTH);
			if (value == null)
				return null;
			return Integer.parseInt(value);
		}
		catch (Throwable t)
		{
			return null;
		} 
	}
	public boolean setMaximumWidth(Integer value)
	{
		if (value == null)
			return factory.raapi.deleteAttributeValue(rObject, factory.D_SHARP_COMPONENT_MAXIMUMWIDTH);
		return factory.raapi.setAttributeValue(rObject, factory.D_SHARP_COMPONENT_MAXIMUMWIDTH, value.toString());
	}
	public Integer getMaximumHeight()
	{
		try { 
			String value = factory.raapi.getAttributeValue(rObject, factory.D_SHARP_COMPONENT_MAXIMUMHEIGHT);
			if (value == null)
				return null;
			return Integer.parseInt(value);
		}
		catch (Throwable t)
		{
			return null;
		} 
	}
	public boolean setMaximumHeight(Integer value)
	{
		if (value == null)
			return factory.raapi.deleteAttributeValue(rObject, factory.D_SHARP_COMPONENT_MAXIMUMHEIGHT);
		return factory.raapi.setAttributeValue(rObject, factory.D_SHARP_COMPONENT_MAXIMUMHEIGHT, value.toString());
	}
	public Integer getLeftMargin()
	{
		try { 
			String value = factory.raapi.getAttributeValue(rObject, factory.D_SHARP_COMPONENT_LEFTMARGIN);
			if (value == null)
				return null;
			return Integer.parseInt(value);
		}
		catch (Throwable t)
		{
			return null;
		} 
	}
	public boolean setLeftMargin(Integer value)
	{
		if (value == null)
			return factory.raapi.deleteAttributeValue(rObject, factory.D_SHARP_COMPONENT_LEFTMARGIN);
		return factory.raapi.setAttributeValue(rObject, factory.D_SHARP_COMPONENT_LEFTMARGIN, value.toString());
	}
	public Integer getRightMargin()
	{
		try { 
			String value = factory.raapi.getAttributeValue(rObject, factory.D_SHARP_COMPONENT_RIGHTMARGIN);
			if (value == null)
				return null;
			return Integer.parseInt(value);
		}
		catch (Throwable t)
		{
			return null;
		} 
	}
	public boolean setRightMargin(Integer value)
	{
		if (value == null)
			return factory.raapi.deleteAttributeValue(rObject, factory.D_SHARP_COMPONENT_RIGHTMARGIN);
		return factory.raapi.setAttributeValue(rObject, factory.D_SHARP_COMPONENT_RIGHTMARGIN, value.toString());
	}
	public Integer getTopMargin()
	{
		try { 
			String value = factory.raapi.getAttributeValue(rObject, factory.D_SHARP_COMPONENT_TOPMARGIN);
			if (value == null)
				return null;
			return Integer.parseInt(value);
		}
		catch (Throwable t)
		{
			return null;
		} 
	}
	public boolean setTopMargin(Integer value)
	{
		if (value == null)
			return factory.raapi.deleteAttributeValue(rObject, factory.D_SHARP_COMPONENT_TOPMARGIN);
		return factory.raapi.setAttributeValue(rObject, factory.D_SHARP_COMPONENT_TOPMARGIN, value.toString());
	}
	public Integer getBottomMargin()
	{
		try { 
			String value = factory.raapi.getAttributeValue(rObject, factory.D_SHARP_COMPONENT_BOTTOMMARGIN);
			if (value == null)
				return null;
			return Integer.parseInt(value);
		}
		catch (Throwable t)
		{
			return null;
		} 
	}
	public boolean setBottomMargin(Integer value)
	{
		if (value == null)
			return factory.raapi.deleteAttributeValue(rObject, factory.D_SHARP_COMPONENT_BOTTOMMARGIN);
		return factory.raapi.setAttributeValue(rObject, factory.D_SHARP_COMPONENT_BOTTOMMARGIN, value.toString());
	}
	public Double getMinimumRelativeWidth()
	{
		try { 
			String value = factory.raapi.getAttributeValue(rObject, factory.D_SHARP_COMPONENT_MINIMUMRELATIVEWIDTH);
			if (value == null)
				return null;
			return Double.parseDouble(value);
		}
		catch (Throwable t)
		{
			return null;
		} 
	}
	public boolean setMinimumRelativeWidth(Double value)
	{
		if (value == null)
			return factory.raapi.deleteAttributeValue(rObject, factory.D_SHARP_COMPONENT_MINIMUMRELATIVEWIDTH);
		return factory.raapi.setAttributeValue(rObject, factory.D_SHARP_COMPONENT_MINIMUMRELATIVEWIDTH, value.toString());
	}
	public Double getMinimumRelativeHeight()
	{
		try { 
			String value = factory.raapi.getAttributeValue(rObject, factory.D_SHARP_COMPONENT_MINIMUMRELATIVEHEIGHT);
			if (value == null)
				return null;
			return Double.parseDouble(value);
		}
		catch (Throwable t)
		{
			return null;
		} 
	}
	public boolean setMinimumRelativeHeight(Double value)
	{
		if (value == null)
			return factory.raapi.deleteAttributeValue(rObject, factory.D_SHARP_COMPONENT_MINIMUMRELATIVEHEIGHT);
		return factory.raapi.setAttributeValue(rObject, factory.D_SHARP_COMPONENT_MINIMUMRELATIVEHEIGHT, value.toString());
	}
	public Double getPreferredRelativeWidth()
	{
		try { 
			String value = factory.raapi.getAttributeValue(rObject, factory.D_SHARP_COMPONENT_PREFERREDRELATIVEWIDTH);
			if (value == null)
				return null;
			return Double.parseDouble(value);
		}
		catch (Throwable t)
		{
			return null;
		} 
	}
	public boolean setPreferredRelativeWidth(Double value)
	{
		if (value == null)
			return factory.raapi.deleteAttributeValue(rObject, factory.D_SHARP_COMPONENT_PREFERREDRELATIVEWIDTH);
		return factory.raapi.setAttributeValue(rObject, factory.D_SHARP_COMPONENT_PREFERREDRELATIVEWIDTH, value.toString());
	}
	public Double getPreferredRelativeHeight()
	{
		try { 
			String value = factory.raapi.getAttributeValue(rObject, factory.D_SHARP_COMPONENT_PREFERREDRELATIVEHEIGHT);
			if (value == null)
				return null;
			return Double.parseDouble(value);
		}
		catch (Throwable t)
		{
			return null;
		} 
	}
	public boolean setPreferredRelativeHeight(Double value)
	{
		if (value == null)
			return factory.raapi.deleteAttributeValue(rObject, factory.D_SHARP_COMPONENT_PREFERREDRELATIVEHEIGHT);
		return factory.raapi.setAttributeValue(rObject, factory.D_SHARP_COMPONENT_PREFERREDRELATIVEHEIGHT, value.toString());
	}
	public Double getMaximumRelativeWidth()
	{
		try { 
			String value = factory.raapi.getAttributeValue(rObject, factory.D_SHARP_COMPONENT_MAXIMUMRELATIVEWIDTH);
			if (value == null)
				return null;
			return Double.parseDouble(value);
		}
		catch (Throwable t)
		{
			return null;
		} 
	}
	public boolean setMaximumRelativeWidth(Double value)
	{
		if (value == null)
			return factory.raapi.deleteAttributeValue(rObject, factory.D_SHARP_COMPONENT_MAXIMUMRELATIVEWIDTH);
		return factory.raapi.setAttributeValue(rObject, factory.D_SHARP_COMPONENT_MAXIMUMRELATIVEWIDTH, value.toString());
	}
	public Double getMaximumRelativeHeight()
	{
		try { 
			String value = factory.raapi.getAttributeValue(rObject, factory.D_SHARP_COMPONENT_MAXIMUMRELATIVEHEIGHT);
			if (value == null)
				return null;
			return Double.parseDouble(value);
		}
		catch (Throwable t)
		{
			return null;
		} 
	}
	public boolean setMaximumRelativeHeight(Double value)
	{
		if (value == null)
			return factory.raapi.deleteAttributeValue(rObject, factory.D_SHARP_COMPONENT_MAXIMUMRELATIVEHEIGHT);
		return factory.raapi.setAttributeValue(rObject, factory.D_SHARP_COMPONENT_MAXIMUMRELATIVEHEIGHT, value.toString());
	}
	public Integer getHorizontalSpan()
	{
		try { 
			String value = factory.raapi.getAttributeValue(rObject, factory.D_SHARP_COMPONENT_HORIZONTALSPAN);
			if (value == null)
				return null;
			return Integer.parseInt(value);
		}
		catch (Throwable t)
		{
			return null;
		} 
	}
	public boolean setHorizontalSpan(Integer value)
	{
		if (value == null)
			return factory.raapi.deleteAttributeValue(rObject, factory.D_SHARP_COMPONENT_HORIZONTALSPAN);
		return factory.raapi.setAttributeValue(rObject, factory.D_SHARP_COMPONENT_HORIZONTALSPAN, value.toString());
	}
	public Integer getVerticalSpan()
	{
		try { 
			String value = factory.raapi.getAttributeValue(rObject, factory.D_SHARP_COMPONENT_VERTICALSPAN);
			if (value == null)
				return null;
			return Integer.parseInt(value);
		}
		catch (Throwable t)
		{
			return null;
		} 
	}
	public boolean setVerticalSpan(Integer value)
	{
		if (value == null)
			return factory.raapi.deleteAttributeValue(rObject, factory.D_SHARP_COMPONENT_VERTICALSPAN);
		return factory.raapi.setAttributeValue(rObject, factory.D_SHARP_COMPONENT_VERTICALSPAN, value.toString());
	}
	public Double getMinimumHorizontalShrinkFactor()
	{
		try { 
			String value = factory.raapi.getAttributeValue(rObject, factory.D_SHARP_COMPONENT_MINIMUMHORIZONTALSHRINKFACTOR);
			if (value == null)
				return null;
			return Double.parseDouble(value);
		}
		catch (Throwable t)
		{
			return null;
		} 
	}
	public boolean setMinimumHorizontalShrinkFactor(Double value)
	{
		if (value == null)
			return factory.raapi.deleteAttributeValue(rObject, factory.D_SHARP_COMPONENT_MINIMUMHORIZONTALSHRINKFACTOR);
		return factory.raapi.setAttributeValue(rObject, factory.D_SHARP_COMPONENT_MINIMUMHORIZONTALSHRINKFACTOR, value.toString());
	}
	public Double getMaximumHorizontalGrowFactor()
	{
		try { 
			String value = factory.raapi.getAttributeValue(rObject, factory.D_SHARP_COMPONENT_MAXIMUMHORIZONTALGROWFACTOR);
			if (value == null)
				return null;
			return Double.parseDouble(value);
		}
		catch (Throwable t)
		{
			return null;
		} 
	}
	public boolean setMaximumHorizontalGrowFactor(Double value)
	{
		if (value == null)
			return factory.raapi.deleteAttributeValue(rObject, factory.D_SHARP_COMPONENT_MAXIMUMHORIZONTALGROWFACTOR);
		return factory.raapi.setAttributeValue(rObject, factory.D_SHARP_COMPONENT_MAXIMUMHORIZONTALGROWFACTOR, value.toString());
	}
	public Double getMinimumVerticalShrinkFactor()
	{
		try { 
			String value = factory.raapi.getAttributeValue(rObject, factory.D_SHARP_COMPONENT_MINIMUMVERTICALSHRINKFACTOR);
			if (value == null)
				return null;
			return Double.parseDouble(value);
		}
		catch (Throwable t)
		{
			return null;
		} 
	}
	public boolean setMinimumVerticalShrinkFactor(Double value)
	{
		if (value == null)
			return factory.raapi.deleteAttributeValue(rObject, factory.D_SHARP_COMPONENT_MINIMUMVERTICALSHRINKFACTOR);
		return factory.raapi.setAttributeValue(rObject, factory.D_SHARP_COMPONENT_MINIMUMVERTICALSHRINKFACTOR, value.toString());
	}
	public Double getMaximumVerticalGrowFactor()
	{
		try { 
			String value = factory.raapi.getAttributeValue(rObject, factory.D_SHARP_COMPONENT_MAXIMUMVERTICALGROWFACTOR);
			if (value == null)
				return null;
			return Double.parseDouble(value);
		}
		catch (Throwable t)
		{
			return null;
		} 
	}
	public boolean setMaximumVerticalGrowFactor(Double value)
	{
		if (value == null)
			return factory.raapi.deleteAttributeValue(rObject, factory.D_SHARP_COMPONENT_MAXIMUMVERTICALGROWFACTOR);
		return factory.raapi.setAttributeValue(rObject, factory.D_SHARP_COMPONENT_MAXIMUMVERTICALGROWFACTOR, value.toString());
	}
	public Boolean getVisible()
	{
		try { 
			String value = factory.raapi.getAttributeValue(rObject, factory.D_SHARP_COMPONENT_VISIBLE);
			if (value == null)
				return null;
			return Boolean.parseBoolean(value);
		}
		catch (Throwable t)
		{
			return null;
		} 
	}
	public boolean setVisible(Boolean value)
	{
		if (value == null)
			return factory.raapi.deleteAttributeValue(rObject, factory.D_SHARP_COMPONENT_VISIBLE);
		return factory.raapi.setAttributeValue(rObject, factory.D_SHARP_COMPONENT_VISIBLE, value.toString());
	}
	public String getHint()
	{
		return factory.raapi.getAttributeValue(rObject, factory.D_SHARP_COMPONENT_HINT);
	}
	public boolean setHint(String value)
	{
		if (value == null)
			return factory.raapi.deleteAttributeValue(rObject, factory.D_SHARP_COMPONENT_HINT);
		return factory.raapi.setAttributeValue(rObject, factory.D_SHARP_COMPONENT_HINT, value.toString());
	}
	public Boolean getReadOnly()
	{
		try { 
			String value = factory.raapi.getAttributeValue(rObject, factory.D_SHARP_COMPONENT_READONLY);
			if (value == null)
				return null;
			return Boolean.parseBoolean(value);
		}
		catch (Throwable t)
		{
			return null;
		} 
	}
	public boolean setReadOnly(Boolean value)
	{
		if (value == null)
			return factory.raapi.deleteAttributeValue(rObject, factory.D_SHARP_COMPONENT_READONLY);
		return factory.raapi.setAttributeValue(rObject, factory.D_SHARP_COMPONENT_READONLY, value.toString());
	}
	public List<D_SHARP_Group> getRelativeWidthGroup()
	{
		return new DialogEngineMetamodel_RAAPILinkedObjectsList<D_SHARP_Group>(factory, rObject, factory.D_SHARP_COMPONENT_RELATIVEWIDTHGROUP); 
	}
	public boolean setRelativeWidthGroup(D_SHARP_Group value)
	{
		boolean ok = true;
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.D_SHARP_COMPONENT_RELATIVEWIDTHGROUP);
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
				if (!factory.raapi.deleteLink(rObject, rLinked, factory.D_SHARP_COMPONENT_RELATIVEWIDTHGROUP))
					ok = false;
		}
		if (value != null)
			if (!factory.raapi.createLink(rObject, value.rObject, factory.D_SHARP_COMPONENT_RELATIVEWIDTHGROUP))
				ok = false;
		return ok;
	}
	public List<D_SHARP_Group> getRelativeHeightGroup()
	{
		return new DialogEngineMetamodel_RAAPILinkedObjectsList<D_SHARP_Group>(factory, rObject, factory.D_SHARP_COMPONENT_RELATIVEHEIGHTGROUP); 
	}
	public boolean setRelativeHeightGroup(D_SHARP_Group value)
	{
		boolean ok = true;
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.D_SHARP_COMPONENT_RELATIVEHEIGHTGROUP);
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
				if (!factory.raapi.deleteLink(rObject, rLinked, factory.D_SHARP_COMPONENT_RELATIVEHEIGHTGROUP))
					ok = false;
		}
		if (value != null)
			if (!factory.raapi.createLink(rObject, value.rObject, factory.D_SHARP_COMPONENT_RELATIVEHEIGHTGROUP))
				ok = false;
		return ok;
	}
	public List<D_SHARP_Container> getFocusContainer()
	{
		return new DialogEngineMetamodel_RAAPILinkedObjectsList<D_SHARP_Container>(factory, rObject, factory.D_SHARP_COMPONENT_FOCUSCONTAINER); 
	}
	public boolean setFocusContainer(D_SHARP_Container value)
	{
		boolean ok = true;
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.D_SHARP_COMPONENT_FOCUSCONTAINER);
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
				if (!factory.raapi.deleteLink(rObject, rLinked, factory.D_SHARP_COMPONENT_FOCUSCONTAINER))
					ok = false;
		}
		if (value != null)
			if (!factory.raapi.createLink(rObject, value.rObject, factory.D_SHARP_COMPONENT_FOCUSCONTAINER))
				ok = false;
		return ok;
	}
	public List<D_SHARP_Command> getCommand()
	{
		return new DialogEngineMetamodel_RAAPILinkedObjectsList<D_SHARP_Command>(factory, rObject, factory.D_SHARP_COMPONENT_COMMAND); 
	}
	public boolean setCommand(D_SHARP_Command value)
	{
		boolean ok = true;
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.D_SHARP_COMPONENT_COMMAND);
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
				if (!factory.raapi.deleteLink(rObject, rLinked, factory.D_SHARP_COMPONENT_COMMAND))
					ok = false;
		}
		if (value != null)
			if (!factory.raapi.createLink(rObject, value.rObject, factory.D_SHARP_COMPONENT_COMMAND))
				ok = false;
		return ok;
	}
	public List<D_SHARP_Group> getOwnedGroup()
	{
		return new DialogEngineMetamodel_RAAPILinkedObjectsList<D_SHARP_Group>(factory, rObject, factory.D_SHARP_COMPONENT_OWNEDGROUP); 
	}
	public boolean setOwnedGroup(D_SHARP_Group value)
	{
		boolean ok = true;
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.D_SHARP_COMPONENT_OWNEDGROUP);
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
				if (!factory.raapi.deleteLink(rObject, rLinked, factory.D_SHARP_COMPONENT_OWNEDGROUP))
					ok = false;
		}
		if (value != null)
			if (!factory.raapi.createLink(rObject, value.rObject, factory.D_SHARP_COMPONENT_OWNEDGROUP))
				ok = false;
		return ok;
	}
	public D_SHARP_Container getContainer()
	{
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.D_SHARP_COMPONENT_CONTAINER);
		if (it == 0)
			return null;
		long r = factory.raapi.resolveIteratorFirst(it);
		factory.raapi.freeIterator(it);
		if (r != 0) {
			D_SHARP_Container retVal = (D_SHARP_Container)factory.findOrCreateRAAPIReferenceWrapper(r, true);
			if (retVal == null)
				retVal = (D_SHARP_Container)factory.findOrCreateRAAPIReferenceWrapper(D_SHARP_Container.class, r, true);
			return retVal;
		}
		else
			return null;
	}
	public boolean setContainer(D_SHARP_Container value)
	{
		boolean ok = true;
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.D_SHARP_COMPONENT_CONTAINER);
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
				if (!factory.raapi.deleteLink(rObject, rLinked, factory.D_SHARP_COMPONENT_CONTAINER))
					ok = false;
		}
		if (value != null)
			if (!factory.raapi.createLink(rObject, value.rObject, factory.D_SHARP_COMPONENT_CONTAINER))
				ok = false;
		return ok;
	}
	public List<D_SHARP_Form> getForm()
	{
		return new DialogEngineMetamodel_RAAPILinkedObjectsList<D_SHARP_Form>(factory, rObject, factory.D_SHARP_COMPONENT_FORM); 
	}
	public boolean setForm(D_SHARP_Form value)
	{
		boolean ok = true;
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.D_SHARP_COMPONENT_FORM);
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
				if (!factory.raapi.deleteLink(rObject, rLinked, factory.D_SHARP_COMPONENT_FORM))
					ok = false;
		}
		if (value != null)
			if (!factory.raapi.createLink(rObject, value.rObject, factory.D_SHARP_COMPONENT_FORM))
				ok = false;
		return ok;
	}
	public List<D_SHARP_Form> getFoForm()
	{
		return new DialogEngineMetamodel_RAAPILinkedObjectsList<D_SHARP_Form>(factory, rObject, factory.D_SHARP_COMPONENT_FOFORM); 
	}
	public boolean setFoForm(D_SHARP_Form value)
	{
		boolean ok = true;
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.D_SHARP_COMPONENT_FOFORM);
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
				if (!factory.raapi.deleteLink(rObject, rLinked, factory.D_SHARP_COMPONENT_FOFORM))
					ok = false;
		}
		if (value != null)
			if (!factory.raapi.createLink(rObject, value.rObject, factory.D_SHARP_COMPONENT_FOFORM))
				ok = false;
		return ok;
	}
}
