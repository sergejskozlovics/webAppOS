// automatically generated
package org.webappos.weblib.de.mm; 

import java.util.*;
import lv.lumii.tda.raapi.RAAPI;

public class D_SHARP_TreeNode
  	implements RAAPIReferenceWrapper
{
	protected DialogEngineMetamodelFactory factory;
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
				System.err.println("Unable to delete the object "+rObject+" of type D_SHARP_TreeNode since the RAAPI wrapper does not take care of this reference.");
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
	D_SHARP_TreeNode(DialogEngineMetamodelFactory _factory)
	{
		factory = _factory;
		rObject = factory.raapi.createObject(factory.D_SHARP_TREENODE);			
		takeReference = true;
		factory.wrappers.put(rObject, this);
	}

	public D_SHARP_TreeNode(DialogEngineMetamodelFactory _factory, long _rObject, boolean _takeReference)
	{
		factory = _factory;
		rObject = _rObject;
		takeReference = _takeReference;
		if (takeReference)
			factory.wrappers.put(rObject, this);
	}

	// iterator for instances...
	public static Iterable<? extends D_SHARP_TreeNode> allObjects()
	{
		return allObjects(DialogEngineMetamodelFactory.eINSTANCE);
	} 

	public static Iterable<? extends D_SHARP_TreeNode> allObjects(DialogEngineMetamodelFactory factory)
	{
		ArrayList<D_SHARP_TreeNode> retVal = new ArrayList<D_SHARP_TreeNode>();
		long it = factory.raapi.getIteratorForAllClassObjects(factory.D_SHARP_TREENODE);
		if (it == 0)
			return retVal;
		long r = factory.raapi.resolveIteratorFirst(it);
		while (r != 0) {
 			D_SHARP_TreeNode o = (D_SHARP_TreeNode)factory.findOrCreateRAAPIReferenceWrapper(r, true);
			if (o == null)
				o = (D_SHARP_TreeNode)factory.findOrCreateRAAPIReferenceWrapper(D_SHARP_TreeNode.class, r, true);
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
		for (D_SHARP_TreeNode o : allObjects(factory))
			o.delete();
		return firstObject(factory) == null;
	}

	public static D_SHARP_TreeNode firstObject()
	{
		return firstObject(DialogEngineMetamodelFactory.eINSTANCE);
	} 

	public static D_SHARP_TreeNode firstObject(DialogEngineMetamodelFactory factory)
	{
		long it = factory.raapi.getIteratorForAllClassObjects(factory.D_SHARP_TREENODE);
		if (it == 0)
			return null;
		long r = factory.raapi.resolveIteratorFirst(it);
		factory.raapi.freeIterator(it);
		if (r == 0)
			return null;
		else {
			D_SHARP_TreeNode  retVal = (D_SHARP_TreeNode)factory.findOrCreateRAAPIReferenceWrapper(r, true);
			if (retVal == null)
				retVal = (D_SHARP_TreeNode)factory.findOrCreateRAAPIReferenceWrapper(D_SHARP_TreeNode.class, r, true);
			return retVal;
		}
	} 
 
	public String getText()
	{
		return factory.raapi.getAttributeValue(rObject, factory.D_SHARP_TREENODE_TEXT);
	}
	public boolean setText(String value)
	{
		if (value == null)
			return factory.raapi.deleteAttributeValue(rObject, factory.D_SHARP_TREENODE_TEXT);
		return factory.raapi.setAttributeValue(rObject, factory.D_SHARP_TREENODE_TEXT, value.toString());
	}
	public Boolean getExpanded()
	{
		try { 
			String value = factory.raapi.getAttributeValue(rObject, factory.D_SHARP_TREENODE_EXPANDED);
			if (value == null)
				return null;
			return Boolean.parseBoolean(value);
		}
		catch (Throwable t)
		{
			return null;
		} 
	}
	public boolean setExpanded(Boolean value)
	{
		if (value == null)
			return factory.raapi.deleteAttributeValue(rObject, factory.D_SHARP_TREENODE_EXPANDED);
		return factory.raapi.setAttributeValue(rObject, factory.D_SHARP_TREENODE_EXPANDED, value.toString());
	}
	public String getId()
	{
		return factory.raapi.getAttributeValue(rObject, factory.D_SHARP_TREENODE_ID);
	}
	public boolean setId(String value)
	{
		if (value == null)
			return factory.raapi.deleteAttributeValue(rObject, factory.D_SHARP_TREENODE_ID);
		return factory.raapi.setAttributeValue(rObject, factory.D_SHARP_TREENODE_ID, value.toString());
	}
	public D_SHARP_Tree getTree()
	{
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.D_SHARP_TREENODE_TREE);
		if (it == 0)
			return null;
		long r = factory.raapi.resolveIteratorFirst(it);
		factory.raapi.freeIterator(it);
		if (r != 0) {
			D_SHARP_Tree retVal = (D_SHARP_Tree)factory.findOrCreateRAAPIReferenceWrapper(r, true);
			if (retVal == null)
				retVal = (D_SHARP_Tree)factory.findOrCreateRAAPIReferenceWrapper(D_SHARP_Tree.class, r, true);
			return retVal;
		}
		else
			return null;
	}
	public boolean setTree(D_SHARP_Tree value)
	{
		boolean ok = true;
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.D_SHARP_TREENODE_TREE);
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
				if (!factory.raapi.deleteLink(rObject, rLinked, factory.D_SHARP_TREENODE_TREE))
					ok = false;
		}
		if (value != null)
			if (!factory.raapi.createLink(rObject, value.rObject, factory.D_SHARP_TREENODE_TREE))
				ok = false;
		return ok;
	}
	public List<D_SHARP_Tree> getParentTree()
	{
		return new DialogEngineMetamodel_RAAPILinkedObjectsList<D_SHARP_Tree>(factory, rObject, factory.D_SHARP_TREENODE_PARENTTREE); 
	}
	public boolean setParentTree(D_SHARP_Tree value)
	{
		boolean ok = true;
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.D_SHARP_TREENODE_PARENTTREE);
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
				if (!factory.raapi.deleteLink(rObject, rLinked, factory.D_SHARP_TREENODE_PARENTTREE))
					ok = false;
		}
		if (value != null)
			if (!factory.raapi.createLink(rObject, value.rObject, factory.D_SHARP_TREENODE_PARENTTREE))
				ok = false;
		return ok;
	}
	public List<D_SHARP_TreeNode> getChildNode()
	{
		return new DialogEngineMetamodel_RAAPILinkedObjectsList<D_SHARP_TreeNode>(factory, rObject, factory.D_SHARP_TREENODE_CHILDNODE); 
	}
	public boolean setChildNode(D_SHARP_TreeNode value)
	{
		boolean ok = true;
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.D_SHARP_TREENODE_CHILDNODE);
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
				if (!factory.raapi.deleteLink(rObject, rLinked, factory.D_SHARP_TREENODE_CHILDNODE))
					ok = false;
		}
		if (value != null)
			if (!factory.raapi.createLink(rObject, value.rObject, factory.D_SHARP_TREENODE_CHILDNODE))
				ok = false;
		return ok;
	}
	public D_SHARP_TreeNode getParentNode()
	{
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.D_SHARP_TREENODE_PARENTNODE);
		if (it == 0)
			return null;
		long r = factory.raapi.resolveIteratorFirst(it);
		factory.raapi.freeIterator(it);
		if (r != 0) {
			D_SHARP_TreeNode retVal = (D_SHARP_TreeNode)factory.findOrCreateRAAPIReferenceWrapper(r, true);
			if (retVal == null)
				retVal = (D_SHARP_TreeNode)factory.findOrCreateRAAPIReferenceWrapper(D_SHARP_TreeNode.class, r, true);
			return retVal;
		}
		else
			return null;
	}
	public boolean setParentNode(D_SHARP_TreeNode value)
	{
		boolean ok = true;
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.D_SHARP_TREENODE_PARENTNODE);
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
				if (!factory.raapi.deleteLink(rObject, rLinked, factory.D_SHARP_TREENODE_PARENTNODE))
					ok = false;
		}
		if (value != null)
			if (!factory.raapi.createLink(rObject, value.rObject, factory.D_SHARP_TREENODE_PARENTNODE))
				ok = false;
		return ok;
	}
	public List<D_SHARP_TreeNodeSelectEvent> getTreeNodeSelectEvent()
	{
		return new DialogEngineMetamodel_RAAPILinkedObjectsList<D_SHARP_TreeNodeSelectEvent>(factory, rObject, factory.D_SHARP_TREENODE_TREENODESELECTEVENT); 
	}
	public boolean setTreeNodeSelectEvent(D_SHARP_TreeNodeSelectEvent value)
	{
		boolean ok = true;
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.D_SHARP_TREENODE_TREENODESELECTEVENT);
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
				if (!factory.raapi.deleteLink(rObject, rLinked, factory.D_SHARP_TREENODE_TREENODESELECTEVENT))
					ok = false;
		}
		if (value != null)
			if (!factory.raapi.createLink(rObject, value.rObject, factory.D_SHARP_TREENODE_TREENODESELECTEVENT))
				ok = false;
		return ok;
	}
	public List<D_SHARP_TreeNodeSelectEvent> getPTreeNodeSelectEvent()
	{
		return new DialogEngineMetamodel_RAAPILinkedObjectsList<D_SHARP_TreeNodeSelectEvent>(factory, rObject, factory.D_SHARP_TREENODE_PTREENODESELECTEVENT); 
	}
	public boolean setPTreeNodeSelectEvent(D_SHARP_TreeNodeSelectEvent value)
	{
		boolean ok = true;
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.D_SHARP_TREENODE_PTREENODESELECTEVENT);
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
				if (!factory.raapi.deleteLink(rObject, rLinked, factory.D_SHARP_TREENODE_PTREENODESELECTEVENT))
					ok = false;
		}
		if (value != null)
			if (!factory.raapi.createLink(rObject, value.rObject, factory.D_SHARP_TREENODE_PTREENODESELECTEVENT))
				ok = false;
		return ok;
	}
	public List<D_SHARP_AddTreeNodeCmd> getTAddTreeNodeCmd()
	{
		return new DialogEngineMetamodel_RAAPILinkedObjectsList<D_SHARP_AddTreeNodeCmd>(factory, rObject, factory.D_SHARP_TREENODE_TADDTREENODECMD); 
	}
	public boolean setTAddTreeNodeCmd(D_SHARP_AddTreeNodeCmd value)
	{
		boolean ok = true;
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.D_SHARP_TREENODE_TADDTREENODECMD);
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
				if (!factory.raapi.deleteLink(rObject, rLinked, factory.D_SHARP_TREENODE_TADDTREENODECMD))
					ok = false;
		}
		if (value != null)
			if (!factory.raapi.createLink(rObject, value.rObject, factory.D_SHARP_TREENODE_TADDTREENODECMD))
				ok = false;
		return ok;
	}
	public List<D_SHARP_AddTreeNodeCmd> getPAddTreeNodeCmd()
	{
		return new DialogEngineMetamodel_RAAPILinkedObjectsList<D_SHARP_AddTreeNodeCmd>(factory, rObject, factory.D_SHARP_TREENODE_PADDTREENODECMD); 
	}
	public boolean setPAddTreeNodeCmd(D_SHARP_AddTreeNodeCmd value)
	{
		boolean ok = true;
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.D_SHARP_TREENODE_PADDTREENODECMD);
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
				if (!factory.raapi.deleteLink(rObject, rLinked, factory.D_SHARP_TREENODE_PADDTREENODECMD))
					ok = false;
		}
		if (value != null)
			if (!factory.raapi.createLink(rObject, value.rObject, factory.D_SHARP_TREENODE_PADDTREENODECMD))
				ok = false;
		return ok;
	}
	public List<D_SHARP_AddTreeNodeCmd> getBAddTreeNodeCmd()
	{
		return new DialogEngineMetamodel_RAAPILinkedObjectsList<D_SHARP_AddTreeNodeCmd>(factory, rObject, factory.D_SHARP_TREENODE_BADDTREENODECMD); 
	}
	public boolean setBAddTreeNodeCmd(D_SHARP_AddTreeNodeCmd value)
	{
		boolean ok = true;
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.D_SHARP_TREENODE_BADDTREENODECMD);
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
				if (!factory.raapi.deleteLink(rObject, rLinked, factory.D_SHARP_TREENODE_BADDTREENODECMD))
					ok = false;
		}
		if (value != null)
			if (!factory.raapi.createLink(rObject, value.rObject, factory.D_SHARP_TREENODE_BADDTREENODECMD))
				ok = false;
		return ok;
	}
	public List<D_SHARP_DeleteTreeNodeCmd> getDeleteTreeNodeCmd()
	{
		return new DialogEngineMetamodel_RAAPILinkedObjectsList<D_SHARP_DeleteTreeNodeCmd>(factory, rObject, factory.D_SHARP_TREENODE_DELETETREENODECMD); 
	}
	public boolean setDeleteTreeNodeCmd(D_SHARP_DeleteTreeNodeCmd value)
	{
		boolean ok = true;
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.D_SHARP_TREENODE_DELETETREENODECMD);
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
				if (!factory.raapi.deleteLink(rObject, rLinked, factory.D_SHARP_TREENODE_DELETETREENODECMD))
					ok = false;
		}
		if (value != null)
			if (!factory.raapi.createLink(rObject, value.rObject, factory.D_SHARP_TREENODE_DELETETREENODECMD))
				ok = false;
		return ok;
	}
	public List<D_SHARP_SelectTreeNodeCmd> getSelectTreeNodeCmd()
	{
		return new DialogEngineMetamodel_RAAPILinkedObjectsList<D_SHARP_SelectTreeNodeCmd>(factory, rObject, factory.D_SHARP_TREENODE_SELECTTREENODECMD); 
	}
	public boolean setSelectTreeNodeCmd(D_SHARP_SelectTreeNodeCmd value)
	{
		boolean ok = true;
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.D_SHARP_TREENODE_SELECTTREENODECMD);
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
				if (!factory.raapi.deleteLink(rObject, rLinked, factory.D_SHARP_TREENODE_SELECTTREENODECMD))
					ok = false;
		}
		if (value != null)
			if (!factory.raapi.createLink(rObject, value.rObject, factory.D_SHARP_TREENODE_SELECTTREENODECMD))
				ok = false;
		return ok;
	}
	public List<D_SHARP_ExpandTreeNodeCmd> getExpandNodeCmd()
	{
		return new DialogEngineMetamodel_RAAPILinkedObjectsList<D_SHARP_ExpandTreeNodeCmd>(factory, rObject, factory.D_SHARP_TREENODE_EXPANDNODECMD); 
	}
	public boolean setExpandNodeCmd(D_SHARP_ExpandTreeNodeCmd value)
	{
		boolean ok = true;
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.D_SHARP_TREENODE_EXPANDNODECMD);
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
				if (!factory.raapi.deleteLink(rObject, rLinked, factory.D_SHARP_TREENODE_EXPANDNODECMD))
					ok = false;
		}
		if (value != null)
			if (!factory.raapi.createLink(rObject, value.rObject, factory.D_SHARP_TREENODE_EXPANDNODECMD))
				ok = false;
		return ok;
	}
	public List<D_SHARP_CollapseTreeNodeCmd> getCollapseTreeNodeCmd()
	{
		return new DialogEngineMetamodel_RAAPILinkedObjectsList<D_SHARP_CollapseTreeNodeCmd>(factory, rObject, factory.D_SHARP_TREENODE_COLLAPSETREENODECMD); 
	}
	public boolean setCollapseTreeNodeCmd(D_SHARP_CollapseTreeNodeCmd value)
	{
		boolean ok = true;
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.D_SHARP_TREENODE_COLLAPSETREENODECMD);
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
				if (!factory.raapi.deleteLink(rObject, rLinked, factory.D_SHARP_TREENODE_COLLAPSETREENODECMD))
					ok = false;
		}
		if (value != null)
			if (!factory.raapi.createLink(rObject, value.rObject, factory.D_SHARP_TREENODE_COLLAPSETREENODECMD))
				ok = false;
		return ok;
	}
	public List<D_SHARP_TreeNodeMoveEvent> getTTreeNodeMoveEvent()
	{
		return new DialogEngineMetamodel_RAAPILinkedObjectsList<D_SHARP_TreeNodeMoveEvent>(factory, rObject, factory.D_SHARP_TREENODE_TTREENODEMOVEEVENT); 
	}
	public boolean setTTreeNodeMoveEvent(D_SHARP_TreeNodeMoveEvent value)
	{
		boolean ok = true;
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.D_SHARP_TREENODE_TTREENODEMOVEEVENT);
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
				if (!factory.raapi.deleteLink(rObject, rLinked, factory.D_SHARP_TREENODE_TTREENODEMOVEEVENT))
					ok = false;
		}
		if (value != null)
			if (!factory.raapi.createLink(rObject, value.rObject, factory.D_SHARP_TREENODE_TTREENODEMOVEEVENT))
				ok = false;
		return ok;
	}
	public List<D_SHARP_TreeNodeMoveEvent> getBTreeNodeMoveEvent()
	{
		return new DialogEngineMetamodel_RAAPILinkedObjectsList<D_SHARP_TreeNodeMoveEvent>(factory, rObject, factory.D_SHARP_TREENODE_BTREENODEMOVEEVENT); 
	}
	public boolean setBTreeNodeMoveEvent(D_SHARP_TreeNodeMoveEvent value)
	{
		boolean ok = true;
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.D_SHARP_TREENODE_BTREENODEMOVEEVENT);
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
				if (!factory.raapi.deleteLink(rObject, rLinked, factory.D_SHARP_TREENODE_BTREENODEMOVEEVENT))
					ok = false;
		}
		if (value != null)
			if (!factory.raapi.createLink(rObject, value.rObject, factory.D_SHARP_TREENODE_BTREENODEMOVEEVENT))
				ok = false;
		return ok;
	}
	public List<D_SHARP_TreeNodeMoveEvent> getATreeNodeMoveEvent()
	{
		return new DialogEngineMetamodel_RAAPILinkedObjectsList<D_SHARP_TreeNodeMoveEvent>(factory, rObject, factory.D_SHARP_TREENODE_ATREENODEMOVEEVENT); 
	}
	public boolean setATreeNodeMoveEvent(D_SHARP_TreeNodeMoveEvent value)
	{
		boolean ok = true;
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.D_SHARP_TREENODE_ATREENODEMOVEEVENT);
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
				if (!factory.raapi.deleteLink(rObject, rLinked, factory.D_SHARP_TREENODE_ATREENODEMOVEEVENT))
					ok = false;
		}
		if (value != null)
			if (!factory.raapi.createLink(rObject, value.rObject, factory.D_SHARP_TREENODE_ATREENODEMOVEEVENT))
				ok = false;
		return ok;
	}
	public List<D_SHARP_TreeNodeMoveEvent> getPTreeNodeMoveEvent()
	{
		return new DialogEngineMetamodel_RAAPILinkedObjectsList<D_SHARP_TreeNodeMoveEvent>(factory, rObject, factory.D_SHARP_TREENODE_PTREENODEMOVEEVENT); 
	}
	public boolean setPTreeNodeMoveEvent(D_SHARP_TreeNodeMoveEvent value)
	{
		boolean ok = true;
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.D_SHARP_TREENODE_PTREENODEMOVEEVENT);
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
				if (!factory.raapi.deleteLink(rObject, rLinked, factory.D_SHARP_TREENODE_PTREENODEMOVEEVENT))
					ok = false;
		}
		if (value != null)
			if (!factory.raapi.createLink(rObject, value.rObject, factory.D_SHARP_TREENODE_PTREENODEMOVEEVENT))
				ok = false;
		return ok;
	}
	public List<D_SHARP_TreeNodeExpandedEvent> getTreeNodeExpandedEvent()
	{
		return new DialogEngineMetamodel_RAAPILinkedObjectsList<D_SHARP_TreeNodeExpandedEvent>(factory, rObject, factory.D_SHARP_TREENODE_TREENODEEXPANDEDEVENT); 
	}
	public boolean setTreeNodeExpandedEvent(D_SHARP_TreeNodeExpandedEvent value)
	{
		boolean ok = true;
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.D_SHARP_TREENODE_TREENODEEXPANDEDEVENT);
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
				if (!factory.raapi.deleteLink(rObject, rLinked, factory.D_SHARP_TREENODE_TREENODEEXPANDEDEVENT))
					ok = false;
		}
		if (value != null)
			if (!factory.raapi.createLink(rObject, value.rObject, factory.D_SHARP_TREENODE_TREENODEEXPANDEDEVENT))
				ok = false;
		return ok;
	}
	public List<D_SHARP_TreeNodeCollapsedEvent> getTreeNodeCollapsedEvent()
	{
		return new DialogEngineMetamodel_RAAPILinkedObjectsList<D_SHARP_TreeNodeCollapsedEvent>(factory, rObject, factory.D_SHARP_TREENODE_TREENODECOLLAPSEDEVENT); 
	}
	public boolean setTreeNodeCollapsedEvent(D_SHARP_TreeNodeCollapsedEvent value)
	{
		boolean ok = true;
		long it = factory.raapi.getIteratorForLinkedObjects(rObject, factory.D_SHARP_TREENODE_TREENODECOLLAPSEDEVENT);
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
				if (!factory.raapi.deleteLink(rObject, rLinked, factory.D_SHARP_TREENODE_TREENODECOLLAPSEDEVENT))
					ok = false;
		}
		if (value != null)
			if (!factory.raapi.createLink(rObject, value.rObject, factory.D_SHARP_TREENODE_TREENODECOLLAPSEDEVENT))
				ok = false;
		return ok;
	}
}
