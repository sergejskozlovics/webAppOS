package org.webappos.bridge;

import java.nio.ByteBuffer;
import java.util.Vector;
import org.slf4j.Logger;

import org.slf4j.LoggerFactory;

import lv.lumii.tda.raapi.RAAPI_Synchronizer;

public class MultiSynchronizer implements RAAPI_Synchronizer {
	
	private static Logger logger =  LoggerFactory.getLogger(MultiSynchronizer.class);
	
	private Vector<RAAPI_Synchronizer> list;
	private RAAPI_Synchronizer exclude;
	
	// takes a synchronized list of RAAPI_Synchronizers;
	// RAAPI_Synchronizers can be added and deleted from the list in parallel;
	// the exclude-synchronizer won't be called; it can be null, then all RAAPI_Synchronizers from the list will be called
	public MultiSynchronizer(Vector<RAAPI_Synchronizer> _list, RAAPI_Synchronizer _exclude) {
		list = _list;
		exclude = _exclude;
	}
	
	public MultiSynchronizer(Vector<RAAPI_Synchronizer> _list) {
		list = _list;
		exclude = null;		
	}
	
	@Override
	public void syncCreateClass(String name, long rClass) {
		synchronized (list) {
			for (RAAPI_Synchronizer s : list) if (s!=exclude)
				s.syncCreateClass(name, rClass);
		}
	}

	@Override
	public void syncDeleteClass(long rClass) {
		synchronized (list) {
			for (RAAPI_Synchronizer s : list) if (s!=exclude)
				s.syncDeleteClass(rClass);
		}
	}

	@Override
	public void syncCreateGeneralization(long rSub, long rSuper) {
		synchronized (list) {
			for (RAAPI_Synchronizer s : list) if (s!=exclude)
				s.syncCreateGeneralization(rSub, rSuper);
		}
	}

	@Override
	public void syncDeleteGeneralization(long rSub, long rSuper) {
		synchronized (list) {
			for (RAAPI_Synchronizer s : list) if (s!=exclude)
				s.syncDeleteGeneralization(rSub, rSuper);
		}
	}

	@Override
	public void syncCreateObject(long rClass, long rObject) {
		synchronized (list) {
			for (RAAPI_Synchronizer s : list) if (s!=exclude)
				s.syncCreateObject(rClass, rObject);
		}
	}

	@Override
	public void syncDeleteObject(long rObject) {
		synchronized (list) {
			for (RAAPI_Synchronizer s : list) if (s!=exclude)
				s.syncDeleteObject(rObject);
		}
	}

	@Override
	public void syncIncludeObjectInClass(long rObject, long rClass) {
		synchronized (list) {
			for (RAAPI_Synchronizer s : list) if (s!=exclude)
				s.syncIncludeObjectInClass(rObject, rClass);
		}
	}

	@Override
	public void syncExcludeObjectFromClass(long rObject, long rClass) {
		synchronized (list) {
			for (RAAPI_Synchronizer s : list) if (s!=exclude)
				s.syncExcludeObjectFromClass(rObject, rClass);
		}
	}

	@Override
	public void syncMoveObject(long rObject, long rClass) {
		synchronized (list) {
			for (RAAPI_Synchronizer s : list) if (s!=exclude)
				s.syncMoveObject(rObject, rClass);
		}
	}

	@Override
	public void syncCreateAttribute(long rClass, String name, long rPrimitiveType, long rAttr) {
		synchronized (list) {
			for (RAAPI_Synchronizer s : list) if (s!=exclude)
				s.syncCreateAttribute(rClass, name, rPrimitiveType, rAttr);
		}
	}

	@Override
	public void syncDeleteAttribute(long rAttr) {
		synchronized (list) {
			for (RAAPI_Synchronizer s : list) if (s!=exclude)
				s.syncDeleteAttribute(rAttr);
		}
	}

	@Override
	public void syncSetAttributeValue(long rObject, long rAttr, String value, String oldValue) {
		synchronized (list) {
			for (RAAPI_Synchronizer s : list) if (s!=exclude)
				s.syncSetAttributeValue(rObject, rAttr, value, oldValue);
		}
	}
	
	@Override
	public void syncDeleteAttributeValue(long rObject, long rAttr) {
		synchronized (list) {
			for (RAAPI_Synchronizer s : list) if (s!=exclude)
				s.syncDeleteAttributeValue(rObject, rAttr);
		}
	}

	@Override
	public void syncValidateAttributeValue(long rObject, long rAttr, String value) {
		synchronized (list) {
			for (RAAPI_Synchronizer s : list) if (s!=exclude)
				s.syncValidateAttributeValue(rObject, rAttr, value);
		}
	}

	@Override
	public void syncCreateAssociation(long rSourceClass, long rTargetClass, String sourceRoleName,
			String targetRoleName, boolean isComposition, long rAssoc, long rInvAssoc) {
		synchronized (list) {
			for (RAAPI_Synchronizer s : list) if (s!=exclude)
				s.syncCreateAssociation(rSourceClass, rTargetClass, sourceRoleName, targetRoleName, isComposition, rAssoc, rInvAssoc);
		}
	}

	@Override
	public void syncCreateDirectedAssociation(long rSourceClass, long rTargetClass, String targetRoleName,
			boolean isComposition, long rAssoc) {
		synchronized (list) {
			for (RAAPI_Synchronizer s : list) if (s!=exclude)
				s.syncCreateDirectedAssociation(rSourceClass, rTargetClass, targetRoleName, isComposition, rAssoc);
		}
	}

	@Override
	public void syncCreateAdvancedAssociation(String name, boolean nAry, boolean associationClass, long rAssoc) {
		synchronized (list) {
			for (RAAPI_Synchronizer s : list) if (s!=exclude)
				s.syncCreateAdvancedAssociation(name, nAry, associationClass, rAssoc);
		}
	}

	@Override
	public void syncDeleteAssociation(long rAssoc) {
		synchronized (list) {
			for (RAAPI_Synchronizer s : list) if (s!=exclude)
				s.syncDeleteAssociation(rAssoc);
		}
	}

	@Override
	public void syncCreateLink(long rSourceObject, long rTargetObject, long rAssociationEnd) {
		synchronized (list) {
			for (RAAPI_Synchronizer s : list) if (s!=exclude)
				s.syncCreateLink(rSourceObject, rTargetObject, rAssociationEnd);
		}
	}

	@Override
	public void syncCreateOrderedLink(long rSourceObject, long rTargetObject, long rAssociationEnd,
			long targetPosition) {
		synchronized (list) {
			for (RAAPI_Synchronizer s : list) if (s!=exclude)
				s.syncCreateOrderedLink(rSourceObject, rTargetObject, rAssociationEnd, targetPosition);
		}
	}

	@Override
	public void syncDeleteLink(long rSourceObject, long rTargetObject, long rAssociationEnd) {
		synchronized (list) {
			for (RAAPI_Synchronizer s : list) if (s!=exclude)
				s.syncDeleteLink(rSourceObject, rTargetObject, rAssociationEnd);
		}
	}

	@Override
	public void syncValidateLink(long rSourceObject, long rTargetObject, long rAssociationEnd) {
		synchronized (list) {
			for (RAAPI_Synchronizer s : list) if (s!=exclude)
				s.syncValidateLink(rSourceObject, rTargetObject, rAssociationEnd);
		}
	}

	@Override
	public void syncRawAction(double[] arr, String str) {
		synchronized (list) {
			for (RAAPI_Synchronizer s : list) if (s!=exclude)
				s.syncRawAction(arr, str);
		}
	}

	@Override
	public void syncBulk(double[] actions, String[] strings) {
		synchronized (list) {
			for (RAAPI_Synchronizer s : list) if (s!=exclude)
				s.syncBulk(actions, strings);
		}
	}

	@Override
	public void syncBulk(int nActions, double[] actions, int nStrings, String[] strings) {
		synchronized (list) {
			for (RAAPI_Synchronizer s : list) if (s!=exclude)
				s.syncBulk(nActions, actions, nStrings, strings);
		}
	}

/*	@Override
	public void syncBulk(ByteBuffer actions, String delimitedEscapedStrings) {
		synchronized (list) {
			for (RAAPI_Synchronizer s : list) if (s!=exclude)
				s.syncBulk(actions, delimitedEscapedStrings);
		}
	}*/

	@Override
	public void syncMaxReference(long r, int bitsCount, long bitsValues) {
		logger.error("syncMaxReference cannot be called on MultiSynchronizer!");
		throw new RuntimeException("syncMaxReference cannot be called on MultiSynchronizer!");
/*		synchronized (list) {
			for (RAAPI_Synchronizer s : list) if (s!=exclude)
				s.syncMaxReference(r);
		}*/
	}

	@Override
	public void sendString(String str) {
		synchronized (list) {
			for (RAAPI_Synchronizer s : list) if (s!=exclude)
				s.sendString(str);
		}
	}

	@Override
	public void flush() {
		synchronized (list) {
			for (RAAPI_Synchronizer s : list) if (s!=exclude)
				s.flush();
		}
	}

	@Override
	public void syncBulk(int nActions, double[] actions, String delimitedStrings) {
		synchronized (list) {
			for (RAAPI_Synchronizer s : list) if (s!=exclude)
				s.syncBulk(nActions, actions, delimitedStrings);
		}
	}

}
