package lv.lumii.tda.kernel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lv.lumii.tda.raapi.IRepository;
import lv.lumii.tda.raapi.RAAPI_Synchronizable;
import lv.lumii.tda.raapi.RAAPI_Synchronizer;
import lv.lumii.tda.raapi.RAAPI_WR;

public class TDAKernelDelegate extends DelegatorToRepositoryBase implements RAAPI_WR, RAAPI_Synchronizable  {
	private static Logger logger =  LoggerFactory.getLogger(TDAKernelDelegate.class);
		
	private long rSubmitterObject = 0;
	
	private TDAKernel tdaKernel;
	
	private IEventsCommandsHook hook;
	
	public TDAKernelDelegate(IEventsCommandsHook _hook, IRepository d, TDAKernel _kernel) {
		super();
		super.setDelegate(d);
		hook = _hook;
		tdaKernel = _kernel;
	}
	
	public void setEventsCommandsHook(long _rSubmitterObj, IEventsCommandsHook _hook) {
		rSubmitterObject = _rSubmitterObj;
		hook = _hook;
	}
	
	
	///// createLink also MANAGES EVENTS AND COMMANDS /////
	public boolean creatingSubmitLink(long rSourceObject, long rTargetObject, long rAssociationEnd) {
		return ((rSubmitterObject!=0) &&
		    ((rSourceObject == rSubmitterObject)||(rTargetObject == rSubmitterObject)));
	}

	public boolean isSubmitter(long r) {
		return ((rSubmitterObject!=0) && (r == rSubmitterObject));
	}
	
	public boolean isEvent(long r) {
		long it = this.getIteratorForDirectObjectClasses(r);
		long rCls = this.resolveIteratorFirst(it);
		this.freeIterator(it);
		
		String s = this.getClassName(rCls);
		if (s==null)
			return false;
		
		if (s.endsWith("Event") || s.endsWith("Evt"))
			return true;
		
		return this.isKindOf(r, this.findClass("Event"));
	}
	
	public boolean isCommand(long r) {
		long it = this.getIteratorForDirectObjectClasses(r);
		long rCls = this.resolveIteratorFirst(it);
		this.freeIterator(it);
		
		String s = this.getClassName(rCls);
		if (s==null)
			return false;
		if (s.endsWith("Command") || s.endsWith("Cmd"))
			return true;
		
		return this.isKindOf(r, this.findClass("Command"));
	}
	
	@Override
	public boolean createLink (long rSourceObject, long rTargetObject, long rAssociationEnd)
	{
		if ((rSubmitterObject!=0) &&
		    ((rSourceObject == rSubmitterObject)||(rTargetObject == rSubmitterObject))) {
			// processing as an event/command submission...			
					
			if (rSourceObject == rSubmitterObject) {
				if (isEvent(rTargetObject))
					return hook.handleEvent(this.tdaKernel, rTargetObject);
				else
					if (isCommand(rTargetObject))
						return hook.executeCommand(this.tdaKernel, rTargetObject);
					else
						return false;
			}
			else {
				if (isEvent(rSourceObject))
					return hook.handleEvent(this.tdaKernel, rSourceObject);
				else
					if (isCommand(rSourceObject))
						return hook.executeCommand(this.tdaKernel, rSourceObject);
					else
						return false;
			}
		}
		else {
			return getDelegate().createLink(rSourceObject, rTargetObject, rAssociationEnd);
		}
	}

	///// createLink also MANAGES EVENTS AND COMMANDS /////
	@Override
	public boolean createOrderedLink (long rSourceObject, long rTargetObject, long rAssociationEnd, int position)
	{
		if ((rSubmitterObject!=0) &&
		    ((rSourceObject == rSubmitterObject)||(rTargetObject == rSubmitterObject))) {
			// processing as an event/command submission...			

			if (rSourceObject == rSubmitterObject) {
				if (isEvent(rTargetObject))
					return hook.handleEvent(this.tdaKernel, rTargetObject);
				else
					if (isCommand(rTargetObject))
						return hook.executeCommand(this.tdaKernel, rTargetObject);
					else
						return false;
			}
			else {
				if (isEvent(rSourceObject))
					return hook.handleEvent(this.tdaKernel, rSourceObject);
				else
					if (isCommand(rSourceObject))
						return hook.executeCommand(this.tdaKernel, rSourceObject);
					else
						return false;
			}
		}
		else {
			return getDelegate().createOrderedLink(rSourceObject, rTargetObject, rAssociationEnd, position);
		}
	}

	

	@Override
	public long getMaxReference() {
		return ((RAAPI_WR)getDelegate()).getMaxReference();
	}

	@Override
	public boolean createClass(String name, long r) {
		return ((RAAPI_WR)getDelegate()).createClass(name, r);
	}

	@Override
	public boolean createObject(long rClass, long r) {
		return ((RAAPI_WR)getDelegate()).createObject(rClass, r);
	}

	@Override
	public boolean createAttribute(long rClass, String name, long rType, long r) {
		return ((RAAPI_WR)getDelegate()).createAttribute(rClass, name, rType, r);
	}

	@Override
	public boolean createAssociation(long rSourceClass, long rTargetClass, String sourceRole, String targetRole,
			boolean isComposition, long r, long rInv) {
		return ((RAAPI_WR)getDelegate()).createAssociation(rSourceClass, rTargetClass, sourceRole, targetRole, isComposition, r, rInv);
	}
	
	@Override
	public boolean createDirectedAssociation(long rSourceClass, long rTargetClass, String targetRole,
			boolean isComposition, long r) {
		return ((RAAPI_WR)getDelegate()).createDirectedAssociation(rSourceClass, rTargetClass, targetRole, isComposition, r);
	}

	@Override
	public boolean createAdvancedAssociation(String name, boolean nAry, boolean associationClass, long r) {
		return ((RAAPI_WR)getDelegate()).createAdvancedAssociation(name, nAry, associationClass, r);
	}
	

	@Override
	public void syncAll(RAAPI_Synchronizer synchronizer, long bitsValues) {
		
	
		if (getDelegate() instanceof RAAPI_Synchronizable) {
			logger.debug("Sync via syncAll started");
			long time1 = System.currentTimeMillis();
			((RAAPI_Synchronizable) delegate).syncAll(synchronizer, bitsValues);
			if (logger.isDebugEnabled()) {
				long time2 = System.currentTimeMillis();
				logger.debug("Sync time: "+(time2-time1));
			}
		}
		else {
				
			
			
			
			// sync data			
			logger.debug("Sync via traverse all started");
			
			long time1 = System.currentTimeMillis();
			// sync metamodel (without generalizations)
			long itC = getDelegate().getIteratorForClasses();
			if (itC!=0) {
				long rC = getDelegate().resolveIteratorFirst(itC);
				while (rC != 0) {
					synchronizer.syncCreateClass(getDelegate().getClassName(rC), rC);														
	
					long itAt = getDelegate().getIteratorForDirectAttributes(rC);
					if (itAt!=0) {
						long rAt = getDelegate().resolveIteratorFirst(itAt);
						while (rAt != 0) {
							synchronizer.syncCreateAttribute(rC, getDelegate().getAttributeName(rAt), getDelegate().getAttributeType(rAt), rAt);
							rAt = getDelegate().resolveIteratorNext(itAt);
						}
						getDelegate().freeIterator(itAt);
					}
	
					
					rC = getDelegate().resolveIteratorNext(itC);
				}
				getDelegate().freeIterator(itC);
			}
	
			// sync associations and generalizations...			
			itC = getDelegate().getIteratorForClasses();
			if (itC!=0) {
				long rC = getDelegate().resolveIteratorFirst(itC);
				while (rC != 0) {
					
					long itAO = getDelegate().getIteratorForDirectOutgoingAssociationEnds(rC);
					if (itAO!=0) {
						long rAO = getDelegate().resolveIteratorFirst(itAO);
						while (rAO != 0) {
							long rInv = getDelegate().getInverseAssociationEnd(rAO);
							if (rInv != 0) {
								// sync only once:
								if (((rInv > rAO)&&(!getDelegate().isComposition(rInv))) || getDelegate().isComposition(rAO))
									synchronizer.syncCreateAssociation(getDelegate().getSourceClass(rAO), getDelegate().getTargetClass(rAO), getDelegate().getRoleName(rInv), getDelegate().getRoleName(rAO), getDelegate().isComposition(rAO), rAO, rInv);
							}
							else
								synchronizer.syncCreateDirectedAssociation(getDelegate().getSourceClass(rAO), getDelegate().getTargetClass(rAO), getDelegate().getRoleName(rAO), getDelegate().isComposition(rAO), rAO);
							rAO = getDelegate().resolveIteratorNext(itAO);
						}
						getDelegate().freeIterator(itAO);
					}
	
					long itAI = getDelegate().getIteratorForDirectIngoingAssociationEnds(rC);
					if (itAI!=0) {
						long rAI = getDelegate().resolveIteratorFirst(itAI);
						while (rAI != 0) {
							long rInv = getDelegate().getInverseAssociationEnd(rAI);
							if (rInv != 0) {
								// sync only once:
								if (((rInv > rAI)&&(!getDelegate().isComposition(rInv))) || getDelegate().isComposition(rAI))
									synchronizer.syncCreateAssociation(getDelegate().getSourceClass(rAI), getDelegate().getTargetClass(rAI), getDelegate().getRoleName(rInv), getDelegate().getRoleName(rAI), getDelegate().isComposition(rAI), rAI, rInv);
							}
							else
								synchronizer.syncCreateDirectedAssociation(getDelegate().getSourceClass(rAI), getDelegate().getTargetClass(rAI), getDelegate().getRoleName(rAI), getDelegate().isComposition(rAI), rAI);
														
							rAI = getDelegate().resolveIteratorNext(itAI);
						}
						getDelegate().freeIterator(itAI);
					}
					
					long itSuper = getDelegate().getIteratorForDirectSuperClasses(rC);
					if (itSuper!=0) {
						long rSuper = getDelegate().resolveIteratorFirst(itSuper);
						while (rSuper != 0) {
							synchronizer.syncCreateGeneralization(rC, rSuper);
							rSuper = getDelegate().resolveIteratorNext(itSuper);
						}
						getDelegate().freeIterator(itSuper);
					}
					
					rC = getDelegate().resolveIteratorNext(itC);
				}
				getDelegate().freeIterator(itC);
			}
			
			// sync objects and attributes...
			itC = getDelegate().getIteratorForClasses();
			if (itC!=0) {
				long rC = getDelegate().resolveIteratorFirst(itC);
				while (rC != 0) {					
					long itO = getDelegate().getIteratorForDirectClassObjects(rC);
					if (itO!=0) {
						long rO = getDelegate().resolveIteratorFirst(itO);
						while (rO != 0) {
							synchronizer.syncCreateObject(rC, rO);
					
							long itAt = getDelegate().getIteratorForAllAttributes(rC);
							if (itAt!=0) {
								long rAt = getDelegate().resolveIteratorFirst(itAt);
								while (rAt != 0) {
									String value = getDelegate().getAttributeValue(rO, rAt);
									if (value != null)
										synchronizer.syncSetAttributeValue(rO, rAt, value, null);
									rAt = getDelegate().resolveIteratorNext(itAt);
								}
								getDelegate().freeIterator(itAt);
							}
		
		
							rO = getDelegate().resolveIteratorNext(itO);
						}
						getDelegate().freeIterator(itO);
					}				
					
					rC = getDelegate().resolveIteratorNext(itC);
				}
				getDelegate().freeIterator(itC);
			}
			
			// sync links...
			itC = getDelegate().getIteratorForClasses();
			if (itC!=0) {
				long rC = getDelegate().resolveIteratorFirst(itC);
				while (rC != 0) {					
					long itO = getDelegate().getIteratorForDirectClassObjects(rC);
					if (itO!=0) {
						long rO = getDelegate().resolveIteratorFirst(itO);
						while (rO != 0) {
		
							long itAO = getDelegate().getIteratorForAllOutgoingAssociationEnds(rC);
							if (itAO!=0) {
								long rAO = getDelegate().resolveIteratorFirst(itAO);
								while (rAO != 0) {
									
									long rInv = getDelegate().getInverseAssociationEnd(rAO);
									if ((rInv ==0) || (rAO < rInv)) {
										// do not create the same link twice;
										// we create just directed links, and links, where current assoc ref < inverse assoc ref
										long itLinked = getDelegate().getIteratorForLinkedObjects(rO, rAO);
										long rLinked = getDelegate().resolveIteratorFirst(itLinked);
										while (rLinked != 0) {
											synchronizer.syncCreateLink(rO, rLinked, rAO);
											rLinked = getDelegate().resolveIteratorNext(itLinked);
										}
										getDelegate().freeIterator(itLinked);
									}
									
									rAO = getDelegate().resolveIteratorNext(itAO);
								}
								getDelegate().freeIterator(itAO);
							}
		
							rO = getDelegate().resolveIteratorNext(itO);
						}
						getDelegate().freeIterator(itO);
					}				
					
					rC = getDelegate().resolveIteratorNext(itC);
				}
				getDelegate().freeIterator(itC);
			}
			
			logger.debug("Sync via traverse all ended");
			
			synchronizer.syncMaxReference(((RAAPI_WR)delegate).getMaxReference(), ((RAAPI_WR)delegate).getPredefinedBitsCount(), ((RAAPI_WR)delegate).getPredefinedBitsValues());
			if (logger.isDebugEnabled()) {
				long time2 = System.currentTimeMillis();
				logger.debug("Sync time: "+(time2-time1));
			}
		}
			
	}

	@Override
	public boolean setPredefinedBits(int bitsCount, long bitsValues) {
		return ((RAAPI_WR)getDelegate()).setPredefinedBits(bitsCount, bitsValues);
	}

	@Override
	public int getPredefinedBitsCount() {
		return ((RAAPI_WR)getDelegate()).getPredefinedBitsCount();
	}

	@Override
	public long getPredefinedBitsValues() {
		return ((RAAPI_WR)getDelegate()).getPredefinedBitsValues();
	}

}
