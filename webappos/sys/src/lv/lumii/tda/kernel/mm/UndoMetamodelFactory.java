// automatically generated
package lv.lumii.tda.kernel.mm;
import lv.lumii.tda.raapi.RAAPI;
import java.util.*;

public class UndoMetamodelFactory
{
	// for compatibility with ECore
	public static UndoMetamodelFactory eINSTANCE = new UndoMetamodelFactory();

	HashMap<Long, RAAPIReferenceWrapper> wrappers =
		new HashMap<Long, RAAPIReferenceWrapper>();

	public RAAPIReferenceWrapper findOrCreateRAAPIReferenceWrapper(Class<? extends RAAPIReferenceWrapper> cls, long rObject, boolean takeReference)
	// if takeReference==true, takes care about freeing rObject
	{
		RAAPIReferenceWrapper w = wrappers.get(rObject);
		if (w != null) {
			if (takeReference)
				raapi.freeReference(rObject);
			return w;
		}

		Class<? extends RAAPIReferenceWrapper> cls1 = findClosestType(rObject);
				
		try {
			java.lang.reflect.Constructor<? extends RAAPIReferenceWrapper> c = cls1.getConstructor(UndoMetamodelFactory.class, Long.TYPE, Boolean.TYPE);
			return (RAAPIReferenceWrapper)c.newInstance(this, rObject, takeReference);
		} catch (Throwable t1) {
			try {
				java.lang.reflect.Constructor<? extends RAAPIReferenceWrapper> c = cls.getConstructor(UndoMetamodelFactory.class, Long.TYPE, Boolean.TYPE);
				return (RAAPIReferenceWrapper)c.newInstance(this, rObject, takeReference);				
			} catch (Throwable t) {
				return null;
			}
		}

	}

	public Class<? extends RAAPIReferenceWrapper> findClosestType(long rObject)
	{
		Class<? extends RAAPIReferenceWrapper> retVal = null;
		long rCurClass = 0;

		if (raapi.isKindOf(rObject, UNDOHISTORY)) {
			if ((rCurClass == 0) || (raapi.isDerivedClass(UNDOHISTORY,rCurClass))) {
				retVal = UndoHistory.class;
				rCurClass = UNDOHISTORY;
			}
		}
		if (raapi.isKindOf(rObject, TRANSACTION)) {
			if ((rCurClass == 0) || (raapi.isDerivedClass(TRANSACTION,rCurClass))) {
				retVal = Transaction.class;
				rCurClass = TRANSACTION;
			}
		}
		if (raapi.isKindOf(rObject, HISTORYSTREAM)) {
			if ((rCurClass == 0) || (raapi.isDerivedClass(HISTORYSTREAM,rCurClass))) {
				retVal = HistoryStream.class;
				rCurClass = HISTORYSTREAM;
			}
		}
		if (raapi.isKindOf(rObject, STREAMSTATE)) {
			if ((rCurClass == 0) || (raapi.isDerivedClass(STREAMSTATE,rCurClass))) {
				retVal = StreamState.class;
				rCurClass = STREAMSTATE;
			}
		}
		if (raapi.isKindOf(rObject, MODIFICATINGACTION)) {
			if ((rCurClass == 0) || (raapi.isDerivedClass(MODIFICATINGACTION,rCurClass))) {
				retVal = ModificatingAction.class;
				rCurClass = MODIFICATINGACTION;
			}
		}
		if (raapi.isKindOf(rObject, MODIFICATINGACTIONCOMMAND)) {
			if ((rCurClass == 0) || (raapi.isDerivedClass(MODIFICATINGACTIONCOMMAND,rCurClass))) {
				retVal = ModificatingActionCommand.class;
				rCurClass = MODIFICATINGACTIONCOMMAND;
			}
		}
		if (raapi.isKindOf(rObject, STREAMSTATECOMMAND)) {
			if ((rCurClass == 0) || (raapi.isDerivedClass(STREAMSTATECOMMAND,rCurClass))) {
				retVal = StreamStateCommand.class;
				rCurClass = STREAMSTATECOMMAND;
			}
		}
		if (raapi.isKindOf(rObject, UNDOIGNORINGCLASS)) {
			if ((rCurClass == 0) || (raapi.isDerivedClass(UNDOIGNORINGCLASS,rCurClass))) {
				retVal = UndoIgnoringClass.class;
				rCurClass = UNDOIGNORINGCLASS;
			}
		}

		return retVal; 
	}

	public RAAPIReferenceWrapper findOrCreateRAAPIReferenceWrapper(long rObject, boolean takeReference)
		// if takeReference==true, takes care about freeing rObject
	{
		RAAPIReferenceWrapper w = wrappers.get(rObject);
		if (w != null) {
			if (takeReference)
				raapi.freeReference(rObject);
			return w;
		}
		long it = raapi.getIteratorForDirectObjectClasses(rObject);
		if (it == 0)
			return null;		
		long rClass = raapi.resolveIteratorFirst(it);
		raapi.freeIterator(it);
		if (rClass == 0)
			return null;
		if (rClass == UNDOHISTORY)
			w = new UndoHistory(this, rObject, takeReference);
		if (rClass == TRANSACTION)
			w = new Transaction(this, rObject, takeReference);
		if (rClass == HISTORYSTREAM)
			w = new HistoryStream(this, rObject, takeReference);
		if (rClass == STREAMSTATE)
			w = new StreamState(this, rObject, takeReference);
		if (rClass == MODIFICATINGACTION)
			w = new ModificatingAction(this, rObject, takeReference);
		if (rClass == MODIFICATINGACTIONCOMMAND)
			w = new ModificatingActionCommand(this, rObject, takeReference);
		if (rClass == STREAMSTATECOMMAND)
			w = new StreamStateCommand(this, rObject, takeReference);
		if (rClass == UNDOIGNORINGCLASS)
			w = new UndoIgnoringClass(this, rObject, takeReference);
		if (w==null) {
		}
		if ((w != null) && takeReference)
			wrappers.put(rObject, w);
		return w;
	}

	public boolean deleteModel()
	{
		boolean ok = true;
		if (!UndoHistory.deleteAllObjects(this))
			ok = false;
		if (!Transaction.deleteAllObjects(this))
			ok = false;
		if (!HistoryStream.deleteAllObjects(this))
			ok = false;
		if (!StreamState.deleteAllObjects(this))
			ok = false;
		if (!ModificatingAction.deleteAllObjects(this))
			ok = false;
		if (!ModificatingActionCommand.deleteAllObjects(this))
			ok = false;
		if (!StreamStateCommand.deleteAllObjects(this))
			ok = false;
		if (!UndoIgnoringClass.deleteAllObjects(this))
			ok = false;
		return ok; 
	}

	// RAAPI references:
	RAAPI raapi = null;
	public long UNDOHISTORY = 0;
	  public long UNDOHISTORY_TRANSACTION = 0;
	  public long UNDOHISTORY_CURRENTTRANSACTION = 0;
	  public long UNDOHISTORY_HISTORYSTREAM = 0;
	public long TRANSACTION = 0;
	  public long TRANSACTION_MODIFICATINGACTION = 0;
	  public long TRANSACTION_DESCRIPTION = 0;
	  public long TRANSACTION_UNDODEPENDENCY = 0;
	  public long TRANSACTION_REDODEPENDENCY = 0;
	  public long TRANSACTION_STREAMSTATE = 0;
	  public long TRANSACTION_UNDOHISTORY = 0;
	public long HISTORYSTREAM = 0;
	  public long HISTORYSTREAM_CURRENTSTATE = 0;
	  public long HISTORYSTREAM_UNDOHISTORY = 0;
	  public long HISTORYSTREAM_STREAMSTATE = 0;
	public long STREAMSTATE = 0;
	  public long STREAMSTATE_CURRENTSTATESTREAM = 0;
	  public long STREAMSTATE_PREVIOUS = 0;
	  public long STREAMSTATE_EARLYNEXT = 0;
	  public long STREAMSTATE_NEXT = 0;
	  public long STREAMSTATE_LATENEXT = 0;
	  public long STREAMSTATE_TRANSACTION = 0;
	  public long STREAMSTATE_STREAMSTATECOMMAND = 0;
	  public long STREAMSTATE_HISTORYSTREAM = 0;
	public long MODIFICATINGACTION = 0;
	  public long MODIFICATINGACTION_TRANSACTION = 0;
	  public long MODIFICATINGACTION_MODIFICATINGACTIONCOMMAND = 0;
	public long MODIFICATINGACTIONCOMMAND = 0;
	  public long MODIFICATINGACTIONCOMMAND_MODIFICATINGACTION = 0;
	public long STREAMSTATECOMMAND = 0;
	  public long STREAMSTATECOMMAND_STREAMSTATE = 0;
	  public long STREAMSTATECOMMAND_INUNDO = 0;
	public long UNDOIGNORINGCLASS = 0;

	public class ElementReferenceException extends Exception
	{
		private static final long serialVersionUID = 1L;
		public ElementReferenceException(String msg)
		{
			super(msg);
		}
	}

	public void unsetRAAPI()
	{
		try {
			setRAAPI(null, null, false);
		}
		catch (Throwable t)
		{
		}
	}

	public RAAPI getRAAPI()
	{
		return raapi;
	}

	public void setRAAPI(RAAPI _raapi, String prefix, boolean insertMetamodel) throws ElementReferenceException // set RAAPI to null to free references
	{
		if (raapi != null) {
			// freeing object-level references...
			for (Long r : wrappers.keySet())
				raapi.freeReference(r);
			wrappers.clear();
			// freeing class-level references...
			if (UNDOHISTORY != 0) {
				raapi.freeReference(UNDOHISTORY);
				UNDOHISTORY = 0;
			}
	  		if (UNDOHISTORY_TRANSACTION != 0) {
				raapi.freeReference(UNDOHISTORY_TRANSACTION);
				UNDOHISTORY_TRANSACTION = 0;
			}
	  		if (UNDOHISTORY_CURRENTTRANSACTION != 0) {
				raapi.freeReference(UNDOHISTORY_CURRENTTRANSACTION);
				UNDOHISTORY_CURRENTTRANSACTION = 0;
			}
	  		if (UNDOHISTORY_HISTORYSTREAM != 0) {
				raapi.freeReference(UNDOHISTORY_HISTORYSTREAM);
				UNDOHISTORY_HISTORYSTREAM = 0;
			}
			if (TRANSACTION != 0) {
				raapi.freeReference(TRANSACTION);
				TRANSACTION = 0;
			}
	  		if (TRANSACTION_MODIFICATINGACTION != 0) {
				raapi.freeReference(TRANSACTION_MODIFICATINGACTION);
				TRANSACTION_MODIFICATINGACTION = 0;
			}
	  		if (TRANSACTION_DESCRIPTION != 0) {
				raapi.freeReference(TRANSACTION_DESCRIPTION);
				TRANSACTION_DESCRIPTION = 0;
			}
	  		if (TRANSACTION_UNDODEPENDENCY != 0) {
				raapi.freeReference(TRANSACTION_UNDODEPENDENCY);
				TRANSACTION_UNDODEPENDENCY = 0;
			}
	  		if (TRANSACTION_REDODEPENDENCY != 0) {
				raapi.freeReference(TRANSACTION_REDODEPENDENCY);
				TRANSACTION_REDODEPENDENCY = 0;
			}
	  		if (TRANSACTION_STREAMSTATE != 0) {
				raapi.freeReference(TRANSACTION_STREAMSTATE);
				TRANSACTION_STREAMSTATE = 0;
			}
	  		if (TRANSACTION_UNDOHISTORY != 0) {
				raapi.freeReference(TRANSACTION_UNDOHISTORY);
				TRANSACTION_UNDOHISTORY = 0;
			}
			if (HISTORYSTREAM != 0) {
				raapi.freeReference(HISTORYSTREAM);
				HISTORYSTREAM = 0;
			}
	  		if (HISTORYSTREAM_CURRENTSTATE != 0) {
				raapi.freeReference(HISTORYSTREAM_CURRENTSTATE);
				HISTORYSTREAM_CURRENTSTATE = 0;
			}
	  		if (HISTORYSTREAM_UNDOHISTORY != 0) {
				raapi.freeReference(HISTORYSTREAM_UNDOHISTORY);
				HISTORYSTREAM_UNDOHISTORY = 0;
			}
	  		if (HISTORYSTREAM_STREAMSTATE != 0) {
				raapi.freeReference(HISTORYSTREAM_STREAMSTATE);
				HISTORYSTREAM_STREAMSTATE = 0;
			}
			if (STREAMSTATE != 0) {
				raapi.freeReference(STREAMSTATE);
				STREAMSTATE = 0;
			}
	  		if (STREAMSTATE_CURRENTSTATESTREAM != 0) {
				raapi.freeReference(STREAMSTATE_CURRENTSTATESTREAM);
				STREAMSTATE_CURRENTSTATESTREAM = 0;
			}
	  		if (STREAMSTATE_PREVIOUS != 0) {
				raapi.freeReference(STREAMSTATE_PREVIOUS);
				STREAMSTATE_PREVIOUS = 0;
			}
	  		if (STREAMSTATE_EARLYNEXT != 0) {
				raapi.freeReference(STREAMSTATE_EARLYNEXT);
				STREAMSTATE_EARLYNEXT = 0;
			}
	  		if (STREAMSTATE_NEXT != 0) {
				raapi.freeReference(STREAMSTATE_NEXT);
				STREAMSTATE_NEXT = 0;
			}
	  		if (STREAMSTATE_LATENEXT != 0) {
				raapi.freeReference(STREAMSTATE_LATENEXT);
				STREAMSTATE_LATENEXT = 0;
			}
	  		if (STREAMSTATE_TRANSACTION != 0) {
				raapi.freeReference(STREAMSTATE_TRANSACTION);
				STREAMSTATE_TRANSACTION = 0;
			}
	  		if (STREAMSTATE_STREAMSTATECOMMAND != 0) {
				raapi.freeReference(STREAMSTATE_STREAMSTATECOMMAND);
				STREAMSTATE_STREAMSTATECOMMAND = 0;
			}
	  		if (STREAMSTATE_HISTORYSTREAM != 0) {
				raapi.freeReference(STREAMSTATE_HISTORYSTREAM);
				STREAMSTATE_HISTORYSTREAM = 0;
			}
			if (MODIFICATINGACTION != 0) {
				raapi.freeReference(MODIFICATINGACTION);
				MODIFICATINGACTION = 0;
			}
	  		if (MODIFICATINGACTION_TRANSACTION != 0) {
				raapi.freeReference(MODIFICATINGACTION_TRANSACTION);
				MODIFICATINGACTION_TRANSACTION = 0;
			}
	  		if (MODIFICATINGACTION_MODIFICATINGACTIONCOMMAND != 0) {
				raapi.freeReference(MODIFICATINGACTION_MODIFICATINGACTIONCOMMAND);
				MODIFICATINGACTION_MODIFICATINGACTIONCOMMAND = 0;
			}
			if (MODIFICATINGACTIONCOMMAND != 0) {
				raapi.freeReference(MODIFICATINGACTIONCOMMAND);
				MODIFICATINGACTIONCOMMAND = 0;
			}
	  		if (MODIFICATINGACTIONCOMMAND_MODIFICATINGACTION != 0) {
				raapi.freeReference(MODIFICATINGACTIONCOMMAND_MODIFICATINGACTION);
				MODIFICATINGACTIONCOMMAND_MODIFICATINGACTION = 0;
			}
			if (STREAMSTATECOMMAND != 0) {
				raapi.freeReference(STREAMSTATECOMMAND);
				STREAMSTATECOMMAND = 0;
			}
	  		if (STREAMSTATECOMMAND_STREAMSTATE != 0) {
				raapi.freeReference(STREAMSTATECOMMAND_STREAMSTATE);
				STREAMSTATECOMMAND_STREAMSTATE = 0;
			}
	  		if (STREAMSTATECOMMAND_INUNDO != 0) {
				raapi.freeReference(STREAMSTATECOMMAND_INUNDO);
				STREAMSTATECOMMAND_INUNDO = 0;
			}
			if (UNDOIGNORINGCLASS != 0) {
				raapi.freeReference(UNDOIGNORINGCLASS);
				UNDOIGNORINGCLASS = 0;
			}
		}

		raapi = _raapi;

		if (raapi != null) {
			// initializing class references...
			UNDOHISTORY = raapi.findClass("TDAKernel::UndoHistory");
			if ((UNDOHISTORY == 0) && (prefix != null))
				UNDOHISTORY = raapi.findClass(prefix+"TDAKernel::UndoHistory");
			if ((UNDOHISTORY == 0) && insertMetamodel)
				UNDOHISTORY = raapi.createClass(prefix+"TDAKernel::UndoHistory");
			if (UNDOHISTORY == 0) {				
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the class TDAKernel::UndoHistory.");
			}
			TRANSACTION = raapi.findClass("TDAKernel::Transaction");
			if ((TRANSACTION == 0) && (prefix != null))
				TRANSACTION = raapi.findClass(prefix+"TDAKernel::Transaction");
			if ((TRANSACTION == 0) && insertMetamodel)
				TRANSACTION = raapi.createClass(prefix+"TDAKernel::Transaction");
			if (TRANSACTION == 0) {				
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the class TDAKernel::Transaction.");
			}
			HISTORYSTREAM = raapi.findClass("TDAKernel::HistoryStream");
			if ((HISTORYSTREAM == 0) && (prefix != null))
				HISTORYSTREAM = raapi.findClass(prefix+"TDAKernel::HistoryStream");
			if ((HISTORYSTREAM == 0) && insertMetamodel)
				HISTORYSTREAM = raapi.createClass(prefix+"TDAKernel::HistoryStream");
			if (HISTORYSTREAM == 0) {				
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the class TDAKernel::HistoryStream.");
			}
			STREAMSTATE = raapi.findClass("TDAKernel::StreamState");
			if ((STREAMSTATE == 0) && (prefix != null))
				STREAMSTATE = raapi.findClass(prefix+"TDAKernel::StreamState");
			if ((STREAMSTATE == 0) && insertMetamodel)
				STREAMSTATE = raapi.createClass(prefix+"TDAKernel::StreamState");
			if (STREAMSTATE == 0) {				
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the class TDAKernel::StreamState.");
			}
			MODIFICATINGACTION = raapi.findClass("TDAKernel::ModificatingAction");
			if ((MODIFICATINGACTION == 0) && (prefix != null))
				MODIFICATINGACTION = raapi.findClass(prefix+"TDAKernel::ModificatingAction");
			if ((MODIFICATINGACTION == 0) && insertMetamodel)
				MODIFICATINGACTION = raapi.createClass(prefix+"TDAKernel::ModificatingAction");
			if (MODIFICATINGACTION == 0) {				
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the class TDAKernel::ModificatingAction.");
			}
			MODIFICATINGACTIONCOMMAND = raapi.findClass("TDAKernel::ModificatingActionCommand");
			if ((MODIFICATINGACTIONCOMMAND == 0) && (prefix != null))
				MODIFICATINGACTIONCOMMAND = raapi.findClass(prefix+"TDAKernel::ModificatingActionCommand");
			if ((MODIFICATINGACTIONCOMMAND == 0) && insertMetamodel)
				MODIFICATINGACTIONCOMMAND = raapi.createClass(prefix+"TDAKernel::ModificatingActionCommand");
			if (MODIFICATINGACTIONCOMMAND == 0) {				
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the class TDAKernel::ModificatingActionCommand.");
			}
			STREAMSTATECOMMAND = raapi.findClass("TDAKernel::StreamStateCommand");
			if ((STREAMSTATECOMMAND == 0) && (prefix != null))
				STREAMSTATECOMMAND = raapi.findClass(prefix+"TDAKernel::StreamStateCommand");
			if ((STREAMSTATECOMMAND == 0) && insertMetamodel)
				STREAMSTATECOMMAND = raapi.createClass(prefix+"TDAKernel::StreamStateCommand");
			if (STREAMSTATECOMMAND == 0) {				
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the class TDAKernel::StreamStateCommand.");
			}
			UNDOIGNORINGCLASS = raapi.findClass("TDAKernel::UndoIgnoringClass");
			if ((UNDOIGNORINGCLASS == 0) && (prefix != null))
				UNDOIGNORINGCLASS = raapi.findClass(prefix+"TDAKernel::UndoIgnoringClass");
			if ((UNDOIGNORINGCLASS == 0) && insertMetamodel)
				UNDOIGNORINGCLASS = raapi.createClass(prefix+"TDAKernel::UndoIgnoringClass");
			if (UNDOIGNORINGCLASS == 0) {				
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the class TDAKernel::UndoIgnoringClass.");
			}

			// creating generalizations, if they do not exist...
			if (insertMetamodel) {
				if (!raapi.isDirectSubClass(UNDOHISTORY, UNDOIGNORINGCLASS))
					if (!raapi.createGeneralization(UNDOHISTORY, UNDOIGNORINGCLASS)) {
						setRAAPI(null, null, false); // freeing references initialized so far...
						throw new ElementReferenceException("Error creating a generalization between classes TDAKernel::UndoHistory and TDAKernel::UndoIgnoringClass.");
					}
				if (!raapi.isDirectSubClass(TRANSACTION, UNDOIGNORINGCLASS))
					if (!raapi.createGeneralization(TRANSACTION, UNDOIGNORINGCLASS)) {
						setRAAPI(null, null, false); // freeing references initialized so far...
						throw new ElementReferenceException("Error creating a generalization between classes TDAKernel::Transaction and TDAKernel::UndoIgnoringClass.");
					}
				if (!raapi.isDirectSubClass(HISTORYSTREAM, UNDOIGNORINGCLASS))
					if (!raapi.createGeneralization(HISTORYSTREAM, UNDOIGNORINGCLASS)) {
						setRAAPI(null, null, false); // freeing references initialized so far...
						throw new ElementReferenceException("Error creating a generalization between classes TDAKernel::HistoryStream and TDAKernel::UndoIgnoringClass.");
					}
				if (!raapi.isDirectSubClass(STREAMSTATE, UNDOIGNORINGCLASS))
					if (!raapi.createGeneralization(STREAMSTATE, UNDOIGNORINGCLASS)) {
						setRAAPI(null, null, false); // freeing references initialized so far...
						throw new ElementReferenceException("Error creating a generalization between classes TDAKernel::StreamState and TDAKernel::UndoIgnoringClass.");
					}
				if (!raapi.isDirectSubClass(STREAMSTATECOMMAND, UNDOIGNORINGCLASS))
					if (!raapi.createGeneralization(STREAMSTATECOMMAND, UNDOIGNORINGCLASS)) {
						setRAAPI(null, null, false); // freeing references initialized so far...
						throw new ElementReferenceException("Error creating a generalization between classes TDAKernel::StreamStateCommand and TDAKernel::UndoIgnoringClass.");
					}
			}

			// initializing references for attributes and associations...
			UNDOHISTORY_TRANSACTION = raapi.findAssociationEnd(UNDOHISTORY, "transaction");
			if ((UNDOHISTORY_TRANSACTION == 0) && insertMetamodel) {
				UNDOHISTORY_TRANSACTION = raapi.createAssociation(UNDOHISTORY, TRANSACTION, "undoHistory", "transaction", true);
			}
			if (UNDOHISTORY_TRANSACTION == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end transaction of the class TDAKernel::UndoHistory.");
			}
			UNDOHISTORY_CURRENTTRANSACTION = raapi.findAssociationEnd(UNDOHISTORY, "currentTransaction");
			if ((UNDOHISTORY_CURRENTTRANSACTION == 0) && insertMetamodel) {
				UNDOHISTORY_CURRENTTRANSACTION = raapi.createDirectedAssociation(UNDOHISTORY, TRANSACTION, "currentTransaction", false);
			}
			if (UNDOHISTORY_CURRENTTRANSACTION == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end currentTransaction of the class TDAKernel::UndoHistory.");
			}
			UNDOHISTORY_HISTORYSTREAM = raapi.findAssociationEnd(UNDOHISTORY, "historyStream");
			if ((UNDOHISTORY_HISTORYSTREAM == 0) && insertMetamodel) {
				UNDOHISTORY_HISTORYSTREAM = raapi.createAssociation(UNDOHISTORY, HISTORYSTREAM, "undoHistory", "historyStream", true);
			}
			if (UNDOHISTORY_HISTORYSTREAM == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end historyStream of the class TDAKernel::UndoHistory.");
			}
			TRANSACTION_MODIFICATINGACTION = raapi.findAssociationEnd(TRANSACTION, "modificatingAction");
			if ((TRANSACTION_MODIFICATINGACTION == 0) && insertMetamodel) {
				TRANSACTION_MODIFICATINGACTION = raapi.createAssociation(TRANSACTION, MODIFICATINGACTION, "transaction", "modificatingAction", true);
			}
			if (TRANSACTION_MODIFICATINGACTION == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end modificatingAction of the class TDAKernel::Transaction.");
			}
			TRANSACTION_DESCRIPTION = raapi.findAttribute(TRANSACTION, "description");
			if ((TRANSACTION_DESCRIPTION == 0) && insertMetamodel)
				TRANSACTION_DESCRIPTION = raapi.createAttribute(TRANSACTION, "description", raapi.findPrimitiveDataType("String"));
			if (TRANSACTION_DESCRIPTION == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute description of the class TDAKernel::Transaction.");
			}
			TRANSACTION_UNDODEPENDENCY = raapi.findAssociationEnd(TRANSACTION, "undoDependency");
			if ((TRANSACTION_UNDODEPENDENCY == 0) && insertMetamodel) {
				TRANSACTION_UNDODEPENDENCY = raapi.createAssociation(TRANSACTION, TRANSACTION, "redoDependency", "undoDependency", false);
			}
			if (TRANSACTION_UNDODEPENDENCY == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end undoDependency of the class TDAKernel::Transaction.");
			}
			TRANSACTION_REDODEPENDENCY = raapi.findAssociationEnd(TRANSACTION, "redoDependency");
			if ((TRANSACTION_REDODEPENDENCY == 0) && insertMetamodel) {
				TRANSACTION_REDODEPENDENCY = raapi.createAssociation(TRANSACTION, TRANSACTION, "undoDependency", "redoDependency", false);
			}
			if (TRANSACTION_REDODEPENDENCY == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end redoDependency of the class TDAKernel::Transaction.");
			}
			TRANSACTION_STREAMSTATE = raapi.findAssociationEnd(TRANSACTION, "streamState");
			if ((TRANSACTION_STREAMSTATE == 0) && insertMetamodel) {
				TRANSACTION_STREAMSTATE = raapi.createAssociation(TRANSACTION, STREAMSTATE, "transaction", "streamState", false);
			}
			if (TRANSACTION_STREAMSTATE == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end streamState of the class TDAKernel::Transaction.");
			}
			TRANSACTION_UNDOHISTORY = raapi.findAssociationEnd(TRANSACTION, "undoHistory");
			if ((TRANSACTION_UNDOHISTORY == 0) && insertMetamodel) {
				TRANSACTION_UNDOHISTORY = raapi.createAssociation(TRANSACTION, UNDOHISTORY, "transaction", "undoHistory", false);
			}
			if (TRANSACTION_UNDOHISTORY == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end undoHistory of the class TDAKernel::Transaction.");
			}
			HISTORYSTREAM_CURRENTSTATE = raapi.findAssociationEnd(HISTORYSTREAM, "currentState");
			if ((HISTORYSTREAM_CURRENTSTATE == 0) && insertMetamodel) {
				HISTORYSTREAM_CURRENTSTATE = raapi.createAssociation(HISTORYSTREAM, STREAMSTATE, "currentStateStream", "currentState", false);
			}
			if (HISTORYSTREAM_CURRENTSTATE == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end currentState of the class TDAKernel::HistoryStream.");
			}
			HISTORYSTREAM_UNDOHISTORY = raapi.findAssociationEnd(HISTORYSTREAM, "undoHistory");
			if ((HISTORYSTREAM_UNDOHISTORY == 0) && insertMetamodel) {
				HISTORYSTREAM_UNDOHISTORY = raapi.createAssociation(HISTORYSTREAM, UNDOHISTORY, "historyStream", "undoHistory", false);
			}
			if (HISTORYSTREAM_UNDOHISTORY == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end undoHistory of the class TDAKernel::HistoryStream.");
			}
			HISTORYSTREAM_STREAMSTATE = raapi.findAssociationEnd(HISTORYSTREAM, "streamState");
			if ((HISTORYSTREAM_STREAMSTATE == 0) && insertMetamodel) {
				HISTORYSTREAM_STREAMSTATE = raapi.createAssociation(HISTORYSTREAM, STREAMSTATE, "historyStream", "streamState", true);
			}
			if (HISTORYSTREAM_STREAMSTATE == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end streamState of the class TDAKernel::HistoryStream.");
			}
			STREAMSTATE_CURRENTSTATESTREAM = raapi.findAssociationEnd(STREAMSTATE, "currentStateStream");
			if ((STREAMSTATE_CURRENTSTATESTREAM == 0) && insertMetamodel) {
				STREAMSTATE_CURRENTSTATESTREAM = raapi.createAssociation(STREAMSTATE, HISTORYSTREAM, "currentState", "currentStateStream", false);
			}
			if (STREAMSTATE_CURRENTSTATESTREAM == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end currentStateStream of the class TDAKernel::StreamState.");
			}
			STREAMSTATE_PREVIOUS = raapi.findAssociationEnd(STREAMSTATE, "previous");
			if ((STREAMSTATE_PREVIOUS == 0) && insertMetamodel) {
				STREAMSTATE_PREVIOUS = raapi.createDirectedAssociation(STREAMSTATE, STREAMSTATE, "previous", false);
			}
			if (STREAMSTATE_PREVIOUS == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end previous of the class TDAKernel::StreamState.");
			}
			STREAMSTATE_EARLYNEXT = raapi.findAssociationEnd(STREAMSTATE, "earlyNext");
			if ((STREAMSTATE_EARLYNEXT == 0) && insertMetamodel) {
				STREAMSTATE_EARLYNEXT = raapi.createDirectedAssociation(STREAMSTATE, STREAMSTATE, "earlyNext", false);
			}
			if (STREAMSTATE_EARLYNEXT == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end earlyNext of the class TDAKernel::StreamState.");
			}
			STREAMSTATE_NEXT = raapi.findAssociationEnd(STREAMSTATE, "next");
			if ((STREAMSTATE_NEXT == 0) && insertMetamodel) {
				STREAMSTATE_NEXT = raapi.createDirectedAssociation(STREAMSTATE, STREAMSTATE, "next", false);
			}
			if (STREAMSTATE_NEXT == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end next of the class TDAKernel::StreamState.");
			}
			STREAMSTATE_LATENEXT = raapi.findAssociationEnd(STREAMSTATE, "lateNext");
			if ((STREAMSTATE_LATENEXT == 0) && insertMetamodel) {
				STREAMSTATE_LATENEXT = raapi.createDirectedAssociation(STREAMSTATE, STREAMSTATE, "lateNext", false);
			}
			if (STREAMSTATE_LATENEXT == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end lateNext of the class TDAKernel::StreamState.");
			}
			STREAMSTATE_TRANSACTION = raapi.findAssociationEnd(STREAMSTATE, "transaction");
			if ((STREAMSTATE_TRANSACTION == 0) && insertMetamodel) {
				STREAMSTATE_TRANSACTION = raapi.createAssociation(STREAMSTATE, TRANSACTION, "streamState", "transaction", false);
			}
			if (STREAMSTATE_TRANSACTION == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end transaction of the class TDAKernel::StreamState.");
			}
			STREAMSTATE_STREAMSTATECOMMAND = raapi.findAssociationEnd(STREAMSTATE, "streamStateCommand");
			if ((STREAMSTATE_STREAMSTATECOMMAND == 0) && insertMetamodel) {
				STREAMSTATE_STREAMSTATECOMMAND = raapi.createAssociation(STREAMSTATE, STREAMSTATECOMMAND, "streamState", "streamStateCommand", false);
			}
			if (STREAMSTATE_STREAMSTATECOMMAND == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end streamStateCommand of the class TDAKernel::StreamState.");
			}
			STREAMSTATE_HISTORYSTREAM = raapi.findAssociationEnd(STREAMSTATE, "historyStream");
			if ((STREAMSTATE_HISTORYSTREAM == 0) && insertMetamodel) {
				STREAMSTATE_HISTORYSTREAM = raapi.createAssociation(STREAMSTATE, HISTORYSTREAM, "streamState", "historyStream", false);
			}
			if (STREAMSTATE_HISTORYSTREAM == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end historyStream of the class TDAKernel::StreamState.");
			}
			MODIFICATINGACTION_TRANSACTION = raapi.findAssociationEnd(MODIFICATINGACTION, "transaction");
			if ((MODIFICATINGACTION_TRANSACTION == 0) && insertMetamodel) {
				MODIFICATINGACTION_TRANSACTION = raapi.createAssociation(MODIFICATINGACTION, TRANSACTION, "modificatingAction", "transaction", false);
			}
			if (MODIFICATINGACTION_TRANSACTION == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end transaction of the class TDAKernel::ModificatingAction.");
			}
			MODIFICATINGACTION_MODIFICATINGACTIONCOMMAND = raapi.findAssociationEnd(MODIFICATINGACTION, "modificatingActionCommand");
			if ((MODIFICATINGACTION_MODIFICATINGACTIONCOMMAND == 0) && insertMetamodel) {
				MODIFICATINGACTION_MODIFICATINGACTIONCOMMAND = raapi.createAssociation(MODIFICATINGACTION, MODIFICATINGACTIONCOMMAND, "modificatingAction", "modificatingActionCommand", false);
			}
			if (MODIFICATINGACTION_MODIFICATINGACTIONCOMMAND == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end modificatingActionCommand of the class TDAKernel::ModificatingAction.");
			}
			MODIFICATINGACTIONCOMMAND_MODIFICATINGACTION = raapi.findAssociationEnd(MODIFICATINGACTIONCOMMAND, "modificatingAction");
			if ((MODIFICATINGACTIONCOMMAND_MODIFICATINGACTION == 0) && insertMetamodel) {
				MODIFICATINGACTIONCOMMAND_MODIFICATINGACTION = raapi.createAssociation(MODIFICATINGACTIONCOMMAND, MODIFICATINGACTION, "modificatingActionCommand", "modificatingAction", false);
			}
			if (MODIFICATINGACTIONCOMMAND_MODIFICATINGACTION == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end modificatingAction of the class TDAKernel::ModificatingActionCommand.");
			}
			STREAMSTATECOMMAND_STREAMSTATE = raapi.findAssociationEnd(STREAMSTATECOMMAND, "streamState");
			if ((STREAMSTATECOMMAND_STREAMSTATE == 0) && insertMetamodel) {
				STREAMSTATECOMMAND_STREAMSTATE = raapi.createAssociation(STREAMSTATECOMMAND, STREAMSTATE, "streamStateCommand", "streamState", false);
			}
			if (STREAMSTATECOMMAND_STREAMSTATE == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end streamState of the class TDAKernel::StreamStateCommand.");
			}
			STREAMSTATECOMMAND_INUNDO = raapi.findAttribute(STREAMSTATECOMMAND, "inUndo");
			if ((STREAMSTATECOMMAND_INUNDO == 0) && insertMetamodel)
				STREAMSTATECOMMAND_INUNDO = raapi.createAttribute(STREAMSTATECOMMAND, "inUndo", raapi.findPrimitiveDataType("Boolean"));
			if (STREAMSTATECOMMAND_INUNDO == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute inUndo of the class TDAKernel::StreamStateCommand.");
			}
		}
	}

	public UndoHistory createUndoHistory()
	{
		UndoHistory retVal = new UndoHistory(this);
		wrappers.put(retVal.getRAAPIReference(), retVal);
		return retVal; 
	}
  
	public Transaction createTransaction()
	{
		Transaction retVal = new Transaction(this);
		wrappers.put(retVal.getRAAPIReference(), retVal);
		return retVal; 
	}
  
	public HistoryStream createHistoryStream()
	{
		HistoryStream retVal = new HistoryStream(this);
		wrappers.put(retVal.getRAAPIReference(), retVal);
		return retVal; 
	}
  
	public StreamState createStreamState()
	{
		StreamState retVal = new StreamState(this);
		wrappers.put(retVal.getRAAPIReference(), retVal);
		return retVal; 
	}
  
	public ModificatingAction createModificatingAction()
	{
		ModificatingAction retVal = new ModificatingAction(this);
		wrappers.put(retVal.getRAAPIReference(), retVal);
		return retVal; 
	}
  
	public ModificatingActionCommand createModificatingActionCommand()
	{
		ModificatingActionCommand retVal = new ModificatingActionCommand(this);
		wrappers.put(retVal.getRAAPIReference(), retVal);
		return retVal; 
	}
  
	public StreamStateCommand createStreamStateCommand()
	{
		StreamStateCommand retVal = new StreamStateCommand(this);
		wrappers.put(retVal.getRAAPIReference(), retVal);
		return retVal; 
	}
  
	public UndoIgnoringClass createUndoIgnoringClass()
	{
		UndoIgnoringClass retVal = new UndoIgnoringClass(this);
		wrappers.put(retVal.getRAAPIReference(), retVal);
		return retVal; 
	}
  
}
