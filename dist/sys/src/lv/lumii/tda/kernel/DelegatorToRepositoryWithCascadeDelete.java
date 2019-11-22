package lv.lumii.tda.kernel;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import lv.lumii.tda.raapi.IRepository;
import lv.lumii.tda.raapi.RAAPI;

public class DelegatorToRepositoryWithCascadeDelete extends DelegatorToRepositoryBase {
	
	public DelegatorToRepositoryWithCascadeDelete(IRepository _delegate)
	{
		super();
		setDelegate(_delegate);
	}
	
	synchronized public boolean deleteGeneralization(long rSubClass, long rSuperClass) {
		
		long itAttr;
		long rAttr;
		
		// deleting attribute values of superclass attributes...
		itAttr = this.getIteratorForAllAttributes(rSuperClass);
		rAttr = this.resolveIteratorFirst(itAttr);
		while (rAttr!=0) {
			long it;
			long r;
			
			it = this.getIteratorForAllClassObjects(rSubClass);
			r = this.resolveIteratorFirst(it);
			while (r!=0) {
				this.deleteAttributeValue(r, rAttr);
				this.freeReference(r);
				r = this.resolveIteratorNext(it);
			}
			this.freeIterator(it);			
			this.freeReference(rAttr);
			rAttr = this.resolveIteratorNext(itAttr);
		}
		this.freeIterator(itAttr);
		
		// deleting direct links of superclass associations...
		long itAssoc;
		long rAssoc;
		itAssoc = this.getIteratorForAllOutgoingAssociationEnds(rSuperClass);
		rAssoc = this.resolveIteratorFirst(itAssoc);
		while (rAssoc!=0) {
			long it;
			long r;
			
			it = this.getIteratorForAllClassObjects(rSubClass);
			r = this.resolveIteratorFirst(it);
			while (r!=0) {			
				long itLinked;
				long rLinked;
				
				itLinked = this.getIteratorForLinkedObjects(r, rAssoc);
				List<Long> linkedObjs = new LinkedList<Long>();
				rLinked = this.resolveIteratorFirst(itLinked);
				while (rLinked != 0) {
					linkedObjs.add(rLinked);
					rLinked = this.resolveIteratorNext(itLinked);
				}
				this.freeIterator(itLinked);
				
				for (Long rLinked2 : linkedObjs) {
					this.deleteLink(r, rLinked2, rAssoc);
					if (this.isComposition(rAssoc))
						this.deleteObject(rLinked2);
					else
						this.freeReference(rLinked2);					
				}				
				
				this.freeReference(r);
				r = this.resolveIteratorNext(it);
			}
			this.freeIterator(it);
			this.freeReference(rAssoc);
			rAssoc = this.resolveIteratorNext(itAssoc);
		}
		this.freeIterator(itAssoc);

		// deleting inverse links of superclass associations...
		itAssoc = this.getIteratorForAllIngoingAssociationEnds(rSuperClass);
		rAssoc = this.resolveIteratorFirst(itAssoc);
		while (rAssoc!=0) {
			
			long rSourceClass = this.getSourceClass(rAssoc);
			
			long it;
			long r;
			
			it = this.getIteratorForAllClassObjects(rSourceClass);
			r = this.resolveIteratorFirst(it);
			while (r!=0) {			
				long itLinked;
				long rLinked;
				
				itLinked = this.getIteratorForLinkedObjects(r, rAssoc);
				rLinked = this.resolveIteratorFirst(itLinked);
				while (rLinked != 0) {
					if (this.isKindOf(rLinked, rSubClass)) {
						this.deleteLink(r, rLinked, rAssoc);
						if (this.isComposition(rAssoc))
							this.deleteObject(rLinked);
						else
							this.freeReference(rLinked);
					}
					else
						this.freeReference(rLinked);
					rLinked = this.resolveIteratorNext(itLinked);
				}
				this.freeIterator(itLinked);
				this.freeReference(r);
				r = this.resolveIteratorNext(it);
			}
			this.freeIterator(it);
			this.freeReference(rAssoc);
			rAssoc = this.resolveIteratorNext(itAssoc);
		}
		this.freeIterator(itAssoc);
		
		return super.deleteGeneralization(rSubClass, rSuperClass);
	}
	

	@Override
	synchronized public boolean deleteAttribute(long rAttr) {
		long rClass = this.getAttributeDomain(rAttr);
		
		long it;
		long r;
		
		it = this.getIteratorForAllClassObjects(rClass);
		r = this.resolveIteratorFirst(it);
		while (r!=0) {
			this.deleteAttributeValue(r, rAttr);
			this.freeReference(r);
			r = this.resolveIteratorNext(it);
		}
		this.freeIterator(it);
		this.freeReference(rClass);
		
		return super.deleteAttribute(rAttr);
	}

	@Override
	synchronized public boolean deleteAssociation(long rAssoc) {
		
		// deleting links in one direction
		// (in the other direction they must be deleted by the underlying repository
		// or its adapter)...
		long rClass = this.getSourceClass(rAssoc);
		
		long it;
		long r;
		
		it = this.getIteratorForAllClassObjects(rClass);
		r = this.resolveIteratorFirst(it);
		while (r!=0) {			
			long itLinked;
			long rLinked;
			
			itLinked = this.getIteratorForLinkedObjects(r, rAssoc);
			rLinked = this.resolveIteratorFirst(itLinked);
			while (rLinked != 0) {
				this.deleteLink(r, rLinked, rAssoc);
				if (this.isComposition(rAssoc))
					this.deleteObject(rLinked);
				else
					this.freeReference(rLinked);
				
				rLinked = this.resolveIteratorNext(itLinked);
			}
			this.freeIterator(itLinked);
			this.freeReference(r);			
			r = this.resolveIteratorNext(it);
		}
		this.freeIterator(it);
		this.freeReference(rClass);
		boolean retVal = super.deleteAssociation(rAssoc);
		return retVal;
	}
	
	@Override
	synchronized public boolean deleteClass(long rClass) {
		
		long it;
		long r;
		
		for (;;) {
			it = this.getIteratorForDirectSubClasses(rClass);
			if (it == 0)
				break;
			r = this.resolveIteratorFirst(it);
			this.freeIterator(it);			
			if (r != 0) {
				boolean ok = this.deleteGeneralization(r, rClass);
				if (!this.deleteClass(r))
					ok = false;
				if (!ok)
					return false;
			}
			if (r == 0)
				break;			
		}
	
		for (;;) {
			it = this.getIteratorForDirectAttributes(rClass);
			if (it == 0)
				break;
			r = this.resolveIteratorFirst(it);
			this.freeIterator(it);
			if (r != 0) {
				if (!this.deleteAttribute(r)) {
					return false;
				}
			}
			else
				break;
		}
		
		/*JOptionPane.showMessageDialog(null, "d3");
		it = this.getIteratorForDirectAttributes(rClass);
		if (it != 0) {
			r = this.resolveIteratorFirst(it);
			while (r != 0) {
				JOptionPane.showMessageDialog(null, "d4a "+this.getAttributeName(r));
				this.deleteAttribute(r);
				JOptionPane.showMessageDialog(null, "d4b ");
				r = this.resolveIteratorNext(it);
			}
			JOptionPane.showMessageDialog(null, "d4c");
			this.freeIterator(it);
			JOptionPane.showMessageDialog(null, "d4d");
		}*/
		it = this.getIteratorForDirectOutgoingAssociationEnds(rClass);
		r = this.resolveIteratorFirst(it);
		while (r!=0) {
			this.deleteAssociation(r);
			r = this.resolveIteratorNext(it);
		}
		this.freeIterator(it);

		it = this.getIteratorForDirectIngoingAssociationEnds(rClass);
		r = this.resolveIteratorFirst(it);
		while (r!=0) {
			this.deleteAssociation(r);
			r = this.resolveIteratorNext(it);
		}
		this.freeIterator(it);
		
		it = this.getIteratorForDirectSuperClasses(rClass);
		r = this.resolveIteratorFirst(it);
		while (r!=0) {
			this.deleteGeneralization(rClass, r);
			this.freeReference(r);
			r = this.resolveIteratorNext(it);
		}
		this.freeIterator(it);

		for(;;) {
			it = this.getIteratorForDirectClassObjects(rClass);
			r = this.resolveIteratorFirst(it);
			this.freeIterator(it);
			if (r==0)
				break;
			if (!this.deleteObject(r)) {
				this.freeReference(r);
				break;
			}
		}
		
/*		it = this.getIteratorForDirectClassObjects(rClass);
		r = this.resolveIteratorFirst(it);
		while (r!=0) {
			this.deleteObject(r);
			r = this.resolveIteratorNext(it);
		}
		this.freeIterator(it);*/
		
		boolean retVal = super.deleteClass(rClass);
		return retVal;
	}
	
	@Override
	synchronized public boolean deleteObject(long rObject) {
		
		// deleting the context of the object, but keeping the object
		// to avoid infinite recursion on deleteObject()
		cascadeDeleteObject(rObject, this, true, false);
		
		// forwarding the deletion to the delegate
		boolean retVal = super.deleteObject(rObject);
		return retVal;
	}

	synchronized public static boolean cascadeDeleteObject(long rObject, RAAPI raapi, boolean keepObject, boolean keepContainments) {		
		long itCls;
		long rCls;

		itCls = raapi.getIteratorForDirectObjectClasses(rObject);
		rCls = raapi.resolveIteratorFirst(itCls);
		
		while (rCls!=0) {			
			long itA;
			long rA;
			
			
			itA = raapi.getIteratorForAllAttributes(rCls);
			rA = raapi.resolveIteratorFirst(itA);
			while (rA != 0) {
				raapi.deleteAttributeValue(rObject, rA);
				raapi.freeReference(rA);
				rA = raapi.resolveIteratorNext(itA);
			}
			raapi.freeIterator(itA);
			
			// deleting links corresponding to all outgoing associations (including bi-directional)...
			itA = raapi.getIteratorForAllOutgoingAssociationEnds(rCls);
			rA = raapi.resolveIteratorFirst(itA);
			while (rA != 0) {
				long itLinked;
				long rLinked;
				
				/*itLinked = raapi.getIteratorForLinkedObjects(rObject, rA);
				rLinked = raapi.resolveIteratorFirst(itLinked);
				while (rLinked != 0) {
					
					if (!keepContainments && raapi.isComposition(rA)) {
						raapi.deleteObject(rLinked);
						// raapi.deleteLink(rObject, rLinked, rA);
						//   ^- link should have been deleted with the linked object
					}
					else {
						JOptionPane.showMessageDialog(null, "kuku: cascadeDeleteObject7d "+rLinked);
						raapi.deleteLink(rObject, rLinked, rA);
						JOptionPane.showMessageDialog(null, "kuku: cascadeDeleteObject7e "+rLinked);
						raapi.freeReference(rLinked);
						JOptionPane.showMessageDialog(null, "kuku: cascadeDeleteObject7f "+rLinked);
					}
					
					rLinked = raapi.resolveIteratorNext(itLinked);
				}
				JOptionPane.showMessageDialog(null, "kuku: cascadeDeleteObject7g ");
				raapi.freeIterator(itLinked);
				JOptionPane.showMessageDialog(null, "kuku: cascadeDeleteObject7h ");*/
				
				/*SK for (;;) {
					itLinked = raapi.getIteratorForLinkedObjects(rObject, rA);
					if (itLinked == 0)
						break;
					rLinked = raapi.resolveIteratorFirst(itLinked);
					raapi.freeIterator(itLinked);
					if (rLinked == 0)
						break;
						
					if (!keepContainments && raapi.isComposition(rA)) {
						if (rLinked == rObject)
							raapi.freeReference(rLinked);
						else
							raapi.deleteObject(rLinked);
							// raapi.deleteLink(rObject, rLinked, rA);
							//   ^- link should have been deleted with the linked object
					}
					else {
						raapi.deleteLink(rObject, rLinked, rA);
						raapi.freeReference(rLinked);
					}
				}*/
				ArrayList<Long> l = new ArrayList<Long>();
				itLinked = raapi.getIteratorForLinkedObjects(rObject, rA);
				if (itLinked == 0)
					break;
				rLinked = raapi.resolveIteratorFirst(itLinked);
				while (rLinked != 0) {
					if (rLinked == rObject) {
						deleteLinkRecursively(raapi, rObject, rObject, rA);
						raapi.freeReference(rLinked);
					}
					else
						l.add(rLinked);
					rLinked = raapi.resolveIteratorNext(itLinked);
				}
				raapi.freeIterator(itLinked);
				
					
				if (!keepContainments && raapi.isComposition(rA)) {
					for (int i=l.size()-1; i>=0; i--) { // reverse order
						
						deleteLinkRecursively(raapi, rObject, l.get(i), rA);						
						//raapi.deleteObject(l.get(i));
						
						cascadeDeleteObject(l.get(i), raapi, false, false);
						raapi.freeReference(l.get(i));
					}
				}
				else {
					for (int i=l.size()-1; i>=0; i--) {
						//raapi.deleteLink(rObject, l.get(i), rA);
						
						deleteLinkRecursively(raapi, rObject, l.get(i), rA);
						
						raapi.freeReference(l.get(i));
					}
				}
				
				raapi.freeReference(rA);
				
				rA = raapi.resolveIteratorNext(itA);
			}
			raapi.freeIterator(itA);
			

			// deleting links corresponding to remaining unidirectional associations...
			// (bi-directional ones have just been processed)
			itA = raapi.getIteratorForAllIngoingAssociationEnds(rCls);
			rA = raapi.resolveIteratorFirst(itA);
			while (rA != 0) {				
				
				long rAInv = raapi.getInverseAssociationEnd(rA);
				if (rAInv == 0) {
				
					long itSrcObj;
					long rSrcObj;
					
					long rSrcCls = raapi.getSourceClass(rA);
					
					itSrcObj = raapi.getIteratorForAllClassObjects(rSrcCls);
					rSrcObj = raapi.resolveIteratorFirst(itSrcObj);
					while (rSrcObj != 0) {
						if (raapi.linkExists(rSrcObj, rObject, rA))
							deleteLinkRecursively(raapi, rSrcObj, rObject, rA);  //TODO
						
						rSrcObj = raapi.resolveIteratorNext(itSrcObj);
					}
					raapi.freeIterator(itSrcObj);
					raapi.freeReference(rA);
				}
				else {
					raapi.freeReference(rAInv);
					raapi.freeReference(rA);
				}
				
				rA = raapi.resolveIteratorNext(itA);
			}
			raapi.freeIterator(itA);
			
			raapi.freeReference(rCls);
			
			rCls = raapi.resolveIteratorNext(itCls);
		}
		raapi.freeIterator(itCls);
		
		if (keepObject)
			return true;
		
		// Ensuring the object is only of one type:
		itCls = raapi.getIteratorForDirectObjectClasses(rObject);
		if (itCls != 0) {
			rCls = raapi.resolveIteratorFirst(itCls);
			if (rCls != 0) {
				// keeping the first type...
				raapi.freeReference(rCls);
				// excluding the object from other types...
				rCls = raapi.resolveIteratorNext(itCls);			
				while (rCls!=0) {
					raapi.excludeObjectFromClass(rObject, rCls);
					raapi.freeReference(rCls);			
					rCls = raapi.resolveIteratorNext(itCls);
				}
			}
			raapi.freeIterator(itCls);
		}
		
		if (keepObject)
			return true;
		else		
			return raapi.deleteObject(rObject);
	}
	
	/*private static class DeleteLinkAction {
		long rSourceObject;
		long rTargetObject;
		long rAssociationEnd;
		public DeleteLinkAction(long s, long t, long a)
		{
			rSourceObject = s;
			rTargetObject = t;
			rAssociationEnd = a;
		}
	}*/

	synchronized public static boolean deleteLinkRecursively(RAAPI raapi, long rSourceObject, long rTargetObject, long rAssociationEnd)
	{
		RAAPITransaction t = new RAAPITransaction(raapi, new IReferenceMapper() {			

			@Override
			public long getStableReference(long r) {
				return r;
			}

			@Override
			public long getUnstableReference(long rStableReference) {
				return rStableReference;
			}

			@Override
			public boolean redirectStableReference(
					long rOldStableReference, long rNewUnstableReference) {
				return false;
			}

			@Override
			public void releaseStableReference(long rStableReference) {
			}
			
		});

		
		//ArrayList<DeleteLinkAction>	linksToRecover = new ArrayList<DeleteLinkAction>();
		boolean retVal = deleteLinkRecursively(t, rSourceObject, rTargetObject, rAssociationEnd, false, true);
				
		retVal = retVal && raapi.deleteLink(rSourceObject, rTargetObject, rAssociationEnd);
		/*for (int i = linksToRecover.size()-1; i>=0; i--) {
			DeleteLinkAction a = linksToRecover.get(i);
			raapi.createLink(a.rSourceObject, a.rTargetObject, a.rAssociationEnd);
		}*/
		t.undo();
		
		return retVal;
	}
	
	synchronized static String getObjectClassName(RAAPI raapi, long rObject)
	{
		long it = raapi.getIteratorForDirectObjectClasses(rObject);
		if (it == 0)
			return null;
		long rCls = raapi.resolveIteratorFirst(it);
		raapi.freeIterator(it);
		if (rCls == 0)
			return null;
		
		String clsName = raapi.getClassName(rCls);
		raapi.freeReference(rCls);
		
		return clsName;
	}

		
	private static boolean deleteLinkRecursively(RAAPI raapi, long rSourceObject, long rTargetObject, long rAssociationEnd, boolean lastLink, boolean keepLink/*, ArrayList<DeleteLinkAction> linksToRecover*/)
	{
				
		// Collecting linked objects that are after the given target object...
		ArrayList<Long> furtherTargetObjects = new ArrayList<Long>();
		
		if (!lastLink) {
			// TODO: make this fast: get target object index, get iterator, get objects at indexes i+1...n-1
			long it = raapi.getIteratorForLinkedObjects(rSourceObject, rAssociationEnd);
			if (it == 0)
				return false;
			long r = raapi.resolveIteratorFirst(it);
			boolean targetReached = false;
			while (r!=0) {
				if (r==rTargetObject) {
					targetReached = true;
					raapi.freeReference(r);
				}
				else
					if (targetReached)
						furtherTargetObjects.add(r);
					else
						raapi.freeReference(r);
				r = raapi.resolveIteratorNext(it);
			}
			raapi.freeIterator(it);


			// deleting further links (recursively)...
			for (int i=furtherTargetObjects.size()-1; i>=0; i--) {
				deleteLinkRecursively(raapi, rSourceObject, furtherTargetObjects.get(i), rAssociationEnd, true, false);
//				linksToRecover.add(new DeleteLinkAction(rSourceObject, furtherTargetObjects.get(i), rAssociationEnd));
			}
			
			// Freeing references of the further objects...
			for (Long l : furtherTargetObjects)
				raapi.freeReference(l);
			
		}

		// Collecting inverse further source links and deleting them...
		ArrayList<Long> furtherSourceObjects = new ArrayList<Long>();
		long rInverseAssociationEnd = raapi.getInverseAssociationEnd(rAssociationEnd);
		if (rInverseAssociationEnd != 0) {

			long it = raapi.getIteratorForLinkedObjects(rTargetObject, rInverseAssociationEnd);
			if (it != 0) {
				long r = raapi.resolveIteratorFirst(it);
				boolean sourceReached = false;
				while (r!=0) {
					if (r==rSourceObject) {
						sourceReached = true;
						raapi.freeReference(r);
					}
					else
						if (sourceReached)
							furtherSourceObjects.add(r);
						else
							raapi.freeReference(r);
					r = raapi.resolveIteratorNext(it);
				}
				raapi.freeIterator(it);
			}
	
	
			// deleting further source links (recursively)...
			for (int i=furtherSourceObjects.size()-1; i>=0; i--) {
				deleteLinkRecursively(raapi, rTargetObject, furtherSourceObjects.get(i), rInverseAssociationEnd, true, false);
//				linksToRecover.add(new DeleteLinkAction(rTargetObject, furtherSourceObjects.get(i), rInverseAssociationEnd));
			}

			// Freeing references of the inverse further objects...
			for (Long l : furtherSourceObjects)
				raapi.freeReference(l);
			
			raapi.freeReference(rInverseAssociationEnd);
			
		}
		
		// The actual link deletion...
		if (keepLink)
			return true;
		else
			return raapi.deleteLink(rSourceObject, rTargetObject, rAssociationEnd);
	}
	
	@Override
	synchronized public boolean deleteLink(long rSourceObject, long rTargetObject, long rAssociationEnd)
	{
/*		if (getDelegate() instanceof Delegator4WithTriggers) {
			if ( ((Delegator5WithUndo)(((Delegator4WithTriggers)getDelegate()).getDelegate())).inUndoModel(rSourceObject)
					||
					((Delegator5WithUndo)(((Delegator4WithTriggers)getDelegate()).getDelegate())).inUndoModel(rTargetObject) ) {
				return super.deleteLink(rSourceObject, rTargetObject, rAssociationEnd);
			}
			else
				return deleteLinkRecursively(this.getDelegate(), rSourceObject, rTargetObject, rAssociationEnd);
}
		else*/
			return deleteLinkRecursively(this.getDelegate(), rSourceObject, rTargetObject, rAssociationEnd);
	}

}
