// automatically generated
package lv.lumii.tda.webgde.mm;
import lv.lumii.tda.raapi.RAAPI;
import java.util.*;

public class GraphDiagramEngineMetamodelFactory
{
	// for compatibility with ECore
	public static GraphDiagramEngineMetamodelFactory eINSTANCE = new GraphDiagramEngineMetamodelFactory();

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
			java.lang.reflect.Constructor<? extends RAAPIReferenceWrapper> c = cls1.getConstructor(GraphDiagramEngineMetamodelFactory.class, Long.TYPE, Boolean.TYPE);
			return (RAAPIReferenceWrapper)c.newInstance(this, rObject, takeReference);
		} catch (Throwable t1) {
			try {
				java.lang.reflect.Constructor<? extends RAAPIReferenceWrapper> c = cls.getConstructor(GraphDiagramEngineMetamodelFactory.class, Long.TYPE, Boolean.TYPE);
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

		if (raapi.isKindOf(rObject, ASYNCCOMMAND)) {
			if ((rCurClass == 0) || (raapi.isDerivedClass(ASYNCCOMMAND,rCurClass))) {
				retVal = AsyncCommand.class;
				rCurClass = ASYNCCOMMAND;
			}
		}
		if (raapi.isKindOf(rObject, COMMAND)) {
			if ((rCurClass == 0) || (raapi.isDerivedClass(COMMAND,rCurClass))) {
				retVal = Command.class;
				rCurClass = COMMAND;
			}
		}
		if (raapi.isKindOf(rObject, PRESENTATIONELEMENT)) {
			if ((rCurClass == 0) || (raapi.isDerivedClass(PRESENTATIONELEMENT,rCurClass))) {
				retVal = PresentationElement.class;
				rCurClass = PRESENTATIONELEMENT;
			}
		}
		if (raapi.isKindOf(rObject, GRAPHDIAGRAMTYPE)) {
			if ((rCurClass == 0) || (raapi.isDerivedClass(GRAPHDIAGRAMTYPE,rCurClass))) {
				retVal = GraphDiagramType.class;
				rCurClass = GRAPHDIAGRAMTYPE;
			}
		}
		if (raapi.isKindOf(rObject, GRAPHDIAGRAMENGINE)) {
			if ((rCurClass == 0) || (raapi.isDerivedClass(GRAPHDIAGRAMENGINE,rCurClass))) {
				retVal = GraphDiagramEngine.class;
				rCurClass = GRAPHDIAGRAMENGINE;
			}
		}
		if (raapi.isKindOf(rObject, ENGINE)) {
			if ((rCurClass == 0) || (raapi.isDerivedClass(ENGINE,rCurClass))) {
				retVal = Engine.class;
				rCurClass = ENGINE;
			}
		}
		if (raapi.isKindOf(rObject, OKCMD)) {
			if ((rCurClass == 0) || (raapi.isDerivedClass(OKCMD,rCurClass))) {
				retVal = OkCmd.class;
				rCurClass = OKCMD;
			}
		}
		if (raapi.isKindOf(rObject, POPUPCMD)) {
			if ((rCurClass == 0) || (raapi.isDerivedClass(POPUPCMD,rCurClass))) {
				retVal = PopUpCmd.class;
				rCurClass = POPUPCMD;
			}
		}
		if (raapi.isKindOf(rObject, ACTIVEDGRCMD)) {
			if ((rCurClass == 0) || (raapi.isDerivedClass(ACTIVEDGRCMD,rCurClass))) {
				retVal = ActiveDgrCmd.class;
				rCurClass = ACTIVEDGRCMD;
			}
		}
		if (raapi.isKindOf(rObject, ACTIVEDGRVIEWCMD)) {
			if ((rCurClass == 0) || (raapi.isDerivedClass(ACTIVEDGRVIEWCMD,rCurClass))) {
				retVal = ActiveDgrViewCmd.class;
				rCurClass = ACTIVEDGRVIEWCMD;
			}
		}
		if (raapi.isKindOf(rObject, PASTECMD)) {
			if ((rCurClass == 0) || (raapi.isDerivedClass(PASTECMD,rCurClass))) {
				retVal = PasteCmd.class;
				rCurClass = PASTECMD;
			}
		}
		if (raapi.isKindOf(rObject, UPDATEDGRCMD)) {
			if ((rCurClass == 0) || (raapi.isDerivedClass(UPDATEDGRCMD,rCurClass))) {
				retVal = UpdateDgrCmd.class;
				rCurClass = UPDATEDGRCMD;
			}
		}
		if (raapi.isKindOf(rObject, CLOSEDGRCMD)) {
			if ((rCurClass == 0) || (raapi.isDerivedClass(CLOSEDGRCMD,rCurClass))) {
				retVal = CloseDgrCmd.class;
				rCurClass = CLOSEDGRCMD;
			}
		}
		if (raapi.isKindOf(rObject, SAVEDGRCMD)) {
			if ((rCurClass == 0) || (raapi.isDerivedClass(SAVEDGRCMD,rCurClass))) {
				retVal = SaveDgrCmd.class;
				rCurClass = SAVEDGRCMD;
			}
		}
		if (raapi.isKindOf(rObject, ACTIVEELEMENTCMD)) {
			if ((rCurClass == 0) || (raapi.isDerivedClass(ACTIVEELEMENTCMD,rCurClass))) {
				retVal = ActiveElementCmd.class;
				rCurClass = ACTIVEELEMENTCMD;
			}
		}
		if (raapi.isKindOf(rObject, AFTERCONFIGCMD)) {
			if ((rCurClass == 0) || (raapi.isDerivedClass(AFTERCONFIGCMD,rCurClass))) {
				retVal = AfterConfigCmd.class;
				rCurClass = AFTERCONFIGCMD;
			}
		}
		if (raapi.isKindOf(rObject, SAVESTYLESCMD)) {
			if ((rCurClass == 0) || (raapi.isDerivedClass(SAVESTYLESCMD,rCurClass))) {
				retVal = SaveStylesCmd.class;
				rCurClass = SAVESTYLESCMD;
			}
		}
		if (raapi.isKindOf(rObject, STYLEDIALOGCMD)) {
			if ((rCurClass == 0) || (raapi.isDerivedClass(STYLEDIALOGCMD,rCurClass))) {
				retVal = StyleDialogCmd.class;
				rCurClass = STYLEDIALOGCMD;
			}
		}
		if (raapi.isKindOf(rObject, DEFAULTSTYLECMD)) {
			if ((rCurClass == 0) || (raapi.isDerivedClass(DEFAULTSTYLECMD,rCurClass))) {
				retVal = DefaultStyleCmd.class;
				rCurClass = DEFAULTSTYLECMD;
			}
		}
		if (raapi.isKindOf(rObject, REFRESHDGRCMD)) {
			if ((rCurClass == 0) || (raapi.isDerivedClass(REFRESHDGRCMD,rCurClass))) {
				retVal = RefreshDgrCmd.class;
				rCurClass = REFRESHDGRCMD;
			}
		}
		if (raapi.isKindOf(rObject, UPDATESTYLECMD)) {
			if ((rCurClass == 0) || (raapi.isDerivedClass(UPDATESTYLECMD,rCurClass))) {
				retVal = UpdateStyleCmd.class;
				rCurClass = UPDATESTYLECMD;
			}
		}
		if (raapi.isKindOf(rObject, REROUTECMD)) {
			if ((rCurClass == 0) || (raapi.isDerivedClass(REROUTECMD,rCurClass))) {
				retVal = RerouteCmd.class;
				rCurClass = REROUTECMD;
			}
		}
		if (raapi.isKindOf(rObject, ALIGNSIZECMD)) {
			if ((rCurClass == 0) || (raapi.isDerivedClass(ALIGNSIZECMD,rCurClass))) {
				retVal = AlignSizeCmd.class;
				rCurClass = ALIGNSIZECMD;
			}
		}
		if (raapi.isKindOf(rObject, POPUPELEMSELECTEVENT)) {
			if ((rCurClass == 0) || (raapi.isDerivedClass(POPUPELEMSELECTEVENT,rCurClass))) {
				retVal = PopUpElemSelectEvent.class;
				rCurClass = POPUPELEMSELECTEVENT;
			}
		}
		if (raapi.isKindOf(rObject, PASTEGRAPHCLIPBOARDEVENT)) {
			if ((rCurClass == 0) || (raapi.isDerivedClass(PASTEGRAPHCLIPBOARDEVENT,rCurClass))) {
				retVal = PasteGraphClipboardEvent.class;
				rCurClass = PASTEGRAPHCLIPBOARDEVENT;
			}
		}
		if (raapi.isKindOf(rObject, DELETECOLLECTIONEVENT)) {
			if ((rCurClass == 0) || (raapi.isDerivedClass(DELETECOLLECTIONEVENT,rCurClass))) {
				retVal = DeleteCollectionEvent.class;
				rCurClass = DELETECOLLECTIONEVENT;
			}
		}
		if (raapi.isKindOf(rObject, COPYCUTCOLLECTIONEVENT)) {
			if ((rCurClass == 0) || (raapi.isDerivedClass(COPYCUTCOLLECTIONEVENT,rCurClass))) {
				retVal = CopyCutCollectionEvent.class;
				rCurClass = COPYCUTCOLLECTIONEVENT;
			}
		}
		if (raapi.isKindOf(rObject, COPYCOLLECTIONEVENT)) {
			if ((rCurClass == 0) || (raapi.isDerivedClass(COPYCOLLECTIONEVENT,rCurClass))) {
				retVal = CopyCollectionEvent.class;
				rCurClass = COPYCOLLECTIONEVENT;
			}
		}
		if (raapi.isKindOf(rObject, MOVELINESTARTPOINTEVENT)) {
			if ((rCurClass == 0) || (raapi.isDerivedClass(MOVELINESTARTPOINTEVENT,rCurClass))) {
				retVal = MoveLineStartPointEvent.class;
				rCurClass = MOVELINESTARTPOINTEVENT;
			}
		}
		if (raapi.isKindOf(rObject, MOVELINEENDPOINTEVENT)) {
			if ((rCurClass == 0) || (raapi.isDerivedClass(MOVELINEENDPOINTEVENT,rCurClass))) {
				retVal = MoveLineEndPointEvent.class;
				rCurClass = MOVELINEENDPOINTEVENT;
			}
		}
		if (raapi.isKindOf(rObject, L2CLICKEVENT)) {
			if ((rCurClass == 0) || (raapi.isDerivedClass(L2CLICKEVENT,rCurClass))) {
				retVal = L2ClickEvent.class;
				rCurClass = L2CLICKEVENT;
			}
		}
		if (raapi.isKindOf(rObject, LCLICKEVENT)) {
			if ((rCurClass == 0) || (raapi.isDerivedClass(LCLICKEVENT,rCurClass))) {
				retVal = LClickEvent.class;
				rCurClass = LCLICKEVENT;
			}
		}
		if (raapi.isKindOf(rObject, RCLICKEVENT)) {
			if ((rCurClass == 0) || (raapi.isDerivedClass(RCLICKEVENT,rCurClass))) {
				retVal = RClickEvent.class;
				rCurClass = RCLICKEVENT;
			}
		}
		if (raapi.isKindOf(rObject, NEWLINEEVENT)) {
			if ((rCurClass == 0) || (raapi.isDerivedClass(NEWLINEEVENT,rCurClass))) {
				retVal = NewLineEvent.class;
				rCurClass = NEWLINEEVENT;
			}
		}
		if (raapi.isKindOf(rObject, NEWBOXEVENT)) {
			if ((rCurClass == 0) || (raapi.isDerivedClass(NEWBOXEVENT,rCurClass))) {
				retVal = NewBoxEvent.class;
				rCurClass = NEWBOXEVENT;
			}
		}
		if (raapi.isKindOf(rObject, EXECTRANSFEVENT)) {
			if ((rCurClass == 0) || (raapi.isDerivedClass(EXECTRANSFEVENT,rCurClass))) {
				retVal = ExecTransfEvent.class;
				rCurClass = EXECTRANSFEVENT;
			}
		}
		if (raapi.isKindOf(rObject, NEWPINEVENT)) {
			if ((rCurClass == 0) || (raapi.isDerivedClass(NEWPINEVENT,rCurClass))) {
				retVal = NewPinEvent.class;
				rCurClass = NEWPINEVENT;
			}
		}
		if (raapi.isKindOf(rObject, CHANGEPARENTEVENT)) {
			if ((rCurClass == 0) || (raapi.isDerivedClass(CHANGEPARENTEVENT,rCurClass))) {
				retVal = ChangeParentEvent.class;
				rCurClass = CHANGEPARENTEVENT;
			}
		}
		if (raapi.isKindOf(rObject, ACTIVATEDGREVENT)) {
			if ((rCurClass == 0) || (raapi.isDerivedClass(ACTIVATEDGREVENT,rCurClass))) {
				retVal = ActivateDgrEvent.class;
				rCurClass = ACTIVATEDGREVENT;
			}
		}
		if (raapi.isKindOf(rObject, CLOSEDGREVENT)) {
			if ((rCurClass == 0) || (raapi.isDerivedClass(CLOSEDGREVENT,rCurClass))) {
				retVal = CloseDgrEvent.class;
				rCurClass = CLOSEDGREVENT;
			}
		}
		if (raapi.isKindOf(rObject, OKSTYLEDIALOGEVENT)) {
			if ((rCurClass == 0) || (raapi.isDerivedClass(OKSTYLEDIALOGEVENT,rCurClass))) {
				retVal = OKStyleDialogEvent.class;
				rCurClass = OKSTYLEDIALOGEVENT;
			}
		}
		if (raapi.isKindOf(rObject, KEYDOWNEVENT)) {
			if ((rCurClass == 0) || (raapi.isDerivedClass(KEYDOWNEVENT,rCurClass))) {
				retVal = KeyDownEvent.class;
				rCurClass = KEYDOWNEVENT;
			}
		}
		if (raapi.isKindOf(rObject, NEWFREEBOXEVENT)) {
			if ((rCurClass == 0) || (raapi.isDerivedClass(NEWFREEBOXEVENT,rCurClass))) {
				retVal = NewFreeBoxEvent.class;
				rCurClass = NEWFREEBOXEVENT;
			}
		}
		if (raapi.isKindOf(rObject, NEWFREELINEEVENT)) {
			if ((rCurClass == 0) || (raapi.isDerivedClass(NEWFREELINEEVENT,rCurClass))) {
				retVal = NewFreeLineEvent.class;
				rCurClass = NEWFREELINEEVENT;
			}
		}
		if (raapi.isKindOf(rObject, FREEBOXEDITEDEVENT)) {
			if ((rCurClass == 0) || (raapi.isDerivedClass(FREEBOXEDITEDEVENT,rCurClass))) {
				retVal = FreeBoxEditedEvent.class;
				rCurClass = FREEBOXEDITEDEVENT;
			}
		}
		if (raapi.isKindOf(rObject, FREELINEEDITEDEVENT)) {
			if ((rCurClass == 0) || (raapi.isDerivedClass(FREELINEEDITEDEVENT,rCurClass))) {
				retVal = FreeLineEditedEvent.class;
				rCurClass = FREELINEEDITEDEVENT;
			}
		}
		if (raapi.isKindOf(rObject, TOOLBARELEMENTSELECTEVENT)) {
			if ((rCurClass == 0) || (raapi.isDerivedClass(TOOLBARELEMENTSELECTEVENT,rCurClass))) {
				retVal = ToolbarElementSelectEvent.class;
				rCurClass = TOOLBARELEMENTSELECTEVENT;
			}
		}
		if (raapi.isKindOf(rObject, STYLE)) {
			if ((rCurClass == 0) || (raapi.isDerivedClass(STYLE,rCurClass))) {
				retVal = Style.class;
				rCurClass = STYLE;
			}
		}
		if (raapi.isKindOf(rObject, GRAPHDIAGRAMSTYLE)) {
			if ((rCurClass == 0) || (raapi.isDerivedClass(GRAPHDIAGRAMSTYLE,rCurClass))) {
				retVal = GraphDiagramStyle.class;
				rCurClass = GRAPHDIAGRAMSTYLE;
			}
		}
		if (raapi.isKindOf(rObject, ELEMSTYLE)) {
			if ((rCurClass == 0) || (raapi.isDerivedClass(ELEMSTYLE,rCurClass))) {
				retVal = ElemStyle.class;
				rCurClass = ELEMSTYLE;
			}
		}
		if (raapi.isKindOf(rObject, NODESTYLE)) {
			if ((rCurClass == 0) || (raapi.isDerivedClass(NODESTYLE,rCurClass))) {
				retVal = NodeStyle.class;
				rCurClass = NODESTYLE;
			}
		}
		if (raapi.isKindOf(rObject, EDGESTYLE)) {
			if ((rCurClass == 0) || (raapi.isDerivedClass(EDGESTYLE,rCurClass))) {
				retVal = EdgeStyle.class;
				rCurClass = EDGESTYLE;
			}
		}
		if (raapi.isKindOf(rObject, PORTSTYLE)) {
			if ((rCurClass == 0) || (raapi.isDerivedClass(PORTSTYLE,rCurClass))) {
				retVal = PortStyle.class;
				rCurClass = PORTSTYLE;
			}
		}
		if (raapi.isKindOf(rObject, FREEBOXSTYLE)) {
			if ((rCurClass == 0) || (raapi.isDerivedClass(FREEBOXSTYLE,rCurClass))) {
				retVal = FreeBoxStyle.class;
				rCurClass = FREEBOXSTYLE;
			}
		}
		if (raapi.isKindOf(rObject, FREELINESTYLE)) {
			if ((rCurClass == 0) || (raapi.isDerivedClass(FREELINESTYLE,rCurClass))) {
				retVal = FreeLineStyle.class;
				rCurClass = FREELINESTYLE;
			}
		}
		if (raapi.isKindOf(rObject, COMPARTSTYLE)) {
			if ((rCurClass == 0) || (raapi.isDerivedClass(COMPARTSTYLE,rCurClass))) {
				retVal = CompartStyle.class;
				rCurClass = COMPARTSTYLE;
			}
		}
		if (raapi.isKindOf(rObject, POPUPDIAGRAM)) {
			if ((rCurClass == 0) || (raapi.isDerivedClass(POPUPDIAGRAM,rCurClass))) {
				retVal = PopUpDiagram.class;
				rCurClass = POPUPDIAGRAM;
			}
		}
		if (raapi.isKindOf(rObject, POPUPELEMENT)) {
			if ((rCurClass == 0) || (raapi.isDerivedClass(POPUPELEMENT,rCurClass))) {
				retVal = PopUpElement.class;
				rCurClass = POPUPELEMENT;
			}
		}
		if (raapi.isKindOf(rObject, PALETTE)) {
			if ((rCurClass == 0) || (raapi.isDerivedClass(PALETTE,rCurClass))) {
				retVal = Palette.class;
				rCurClass = PALETTE;
			}
		}
		if (raapi.isKindOf(rObject, PALETTEELEMENT)) {
			if ((rCurClass == 0) || (raapi.isDerivedClass(PALETTEELEMENT,rCurClass))) {
				retVal = PaletteElement.class;
				rCurClass = PALETTEELEMENT;
			}
		}
		if (raapi.isKindOf(rObject, PALETTEBOX)) {
			if ((rCurClass == 0) || (raapi.isDerivedClass(PALETTEBOX,rCurClass))) {
				retVal = PaletteBox.class;
				rCurClass = PALETTEBOX;
			}
		}
		if (raapi.isKindOf(rObject, PALETTELINE)) {
			if ((rCurClass == 0) || (raapi.isDerivedClass(PALETTELINE,rCurClass))) {
				retVal = PaletteLine.class;
				rCurClass = PALETTELINE;
			}
		}
		if (raapi.isKindOf(rObject, PALETTEPIN)) {
			if ((rCurClass == 0) || (raapi.isDerivedClass(PALETTEPIN,rCurClass))) {
				retVal = PalettePin.class;
				rCurClass = PALETTEPIN;
			}
		}
		if (raapi.isKindOf(rObject, PALETTEFREEBOX)) {
			if ((rCurClass == 0) || (raapi.isDerivedClass(PALETTEFREEBOX,rCurClass))) {
				retVal = PaletteFreeBox.class;
				rCurClass = PALETTEFREEBOX;
			}
		}
		if (raapi.isKindOf(rObject, PALETTEFREELINE)) {
			if ((rCurClass == 0) || (raapi.isDerivedClass(PALETTEFREELINE,rCurClass))) {
				retVal = PaletteFreeLine.class;
				rCurClass = PALETTEFREELINE;
			}
		}
		if (raapi.isKindOf(rObject, TOOLBAR)) {
			if ((rCurClass == 0) || (raapi.isDerivedClass(TOOLBAR,rCurClass))) {
				retVal = Toolbar.class;
				rCurClass = TOOLBAR;
			}
		}
		if (raapi.isKindOf(rObject, TOOLBARELEMENT)) {
			if ((rCurClass == 0) || (raapi.isDerivedClass(TOOLBARELEMENT,rCurClass))) {
				retVal = ToolbarElement.class;
				rCurClass = TOOLBARELEMENT;
			}
		}
		if (raapi.isKindOf(rObject, CURRENTDGRPOINTER)) {
			if ((rCurClass == 0) || (raapi.isDerivedClass(CURRENTDGRPOINTER,rCurClass))) {
				retVal = CurrentDgrPointer.class;
				rCurClass = CURRENTDGRPOINTER;
			}
		}
		if (raapi.isKindOf(rObject, GRAPHDIAGRAM)) {
			if ((rCurClass == 0) || (raapi.isDerivedClass(GRAPHDIAGRAM,rCurClass))) {
				retVal = GraphDiagram.class;
				rCurClass = GRAPHDIAGRAM;
			}
		}
		if (raapi.isKindOf(rObject, COLLECTION)) {
			if ((rCurClass == 0) || (raapi.isDerivedClass(COLLECTION,rCurClass))) {
				retVal = Collection.class;
				rCurClass = COLLECTION;
			}
		}
		if (raapi.isKindOf(rObject, ELEMENT)) {
			if ((rCurClass == 0) || (raapi.isDerivedClass(ELEMENT,rCurClass))) {
				retVal = Element.class;
				rCurClass = ELEMENT;
			}
		}
		if (raapi.isKindOf(rObject, NODE)) {
			if ((rCurClass == 0) || (raapi.isDerivedClass(NODE,rCurClass))) {
				retVal = Node.class;
				rCurClass = NODE;
			}
		}
		if (raapi.isKindOf(rObject, EDGE)) {
			if ((rCurClass == 0) || (raapi.isDerivedClass(EDGE,rCurClass))) {
				retVal = Edge.class;
				rCurClass = EDGE;
			}
		}
		if (raapi.isKindOf(rObject, PORT)) {
			if ((rCurClass == 0) || (raapi.isDerivedClass(PORT,rCurClass))) {
				retVal = Port.class;
				rCurClass = PORT;
			}
		}
		if (raapi.isKindOf(rObject, FREEBOX)) {
			if ((rCurClass == 0) || (raapi.isDerivedClass(FREEBOX,rCurClass))) {
				retVal = FreeBox.class;
				rCurClass = FREEBOX;
			}
		}
		if (raapi.isKindOf(rObject, FREELINE)) {
			if ((rCurClass == 0) || (raapi.isDerivedClass(FREELINE,rCurClass))) {
				retVal = FreeLine.class;
				rCurClass = FREELINE;
			}
		}
		if (raapi.isKindOf(rObject, COMPARTMENT)) {
			if ((rCurClass == 0) || (raapi.isDerivedClass(COMPARTMENT,rCurClass))) {
				retVal = Compartment.class;
				rCurClass = COMPARTMENT;
			}
		}
		if (raapi.isKindOf(rObject, EVENT)) {
			if ((rCurClass == 0) || (raapi.isDerivedClass(EVENT,rCurClass))) {
				retVal = Event.class;
				rCurClass = EVENT;
			}
		}
		if (raapi.isKindOf(rObject, FRAME)) {
			if ((rCurClass == 0) || (raapi.isDerivedClass(FRAME,rCurClass))) {
				retVal = Frame.class;
				rCurClass = FRAME;
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
		if (rClass == ASYNCCOMMAND)
			w = new AsyncCommand(this, rObject, takeReference);
		if (rClass == COMMAND)
			w = new Command(this, rObject, takeReference);
		if (rClass == PRESENTATIONELEMENT)
			w = new PresentationElement(this, rObject, takeReference);
		if (rClass == GRAPHDIAGRAMTYPE)
			w = new GraphDiagramType(this, rObject, takeReference);
		if (rClass == GRAPHDIAGRAMENGINE)
			w = new GraphDiagramEngine(this, rObject, takeReference);
		if (rClass == ENGINE)
			w = new Engine(this, rObject, takeReference);
		if (rClass == OKCMD)
			w = new OkCmd(this, rObject, takeReference);
		if (rClass == POPUPCMD)
			w = new PopUpCmd(this, rObject, takeReference);
		if (rClass == ACTIVEDGRCMD)
			w = new ActiveDgrCmd(this, rObject, takeReference);
		if (rClass == ACTIVEDGRVIEWCMD)
			w = new ActiveDgrViewCmd(this, rObject, takeReference);
		if (rClass == PASTECMD)
			w = new PasteCmd(this, rObject, takeReference);
		if (rClass == UPDATEDGRCMD)
			w = new UpdateDgrCmd(this, rObject, takeReference);
		if (rClass == CLOSEDGRCMD)
			w = new CloseDgrCmd(this, rObject, takeReference);
		if (rClass == SAVEDGRCMD)
			w = new SaveDgrCmd(this, rObject, takeReference);
		if (rClass == ACTIVEELEMENTCMD)
			w = new ActiveElementCmd(this, rObject, takeReference);
		if (rClass == AFTERCONFIGCMD)
			w = new AfterConfigCmd(this, rObject, takeReference);
		if (rClass == SAVESTYLESCMD)
			w = new SaveStylesCmd(this, rObject, takeReference);
		if (rClass == STYLEDIALOGCMD)
			w = new StyleDialogCmd(this, rObject, takeReference);
		if (rClass == DEFAULTSTYLECMD)
			w = new DefaultStyleCmd(this, rObject, takeReference);
		if (rClass == REFRESHDGRCMD)
			w = new RefreshDgrCmd(this, rObject, takeReference);
		if (rClass == UPDATESTYLECMD)
			w = new UpdateStyleCmd(this, rObject, takeReference);
		if (rClass == REROUTECMD)
			w = new RerouteCmd(this, rObject, takeReference);
		if (rClass == ALIGNSIZECMD)
			w = new AlignSizeCmd(this, rObject, takeReference);
		if (rClass == POPUPELEMSELECTEVENT)
			w = new PopUpElemSelectEvent(this, rObject, takeReference);
		if (rClass == PASTEGRAPHCLIPBOARDEVENT)
			w = new PasteGraphClipboardEvent(this, rObject, takeReference);
		if (rClass == DELETECOLLECTIONEVENT)
			w = new DeleteCollectionEvent(this, rObject, takeReference);
		if (rClass == COPYCUTCOLLECTIONEVENT)
			w = new CopyCutCollectionEvent(this, rObject, takeReference);
		if (rClass == COPYCOLLECTIONEVENT)
			w = new CopyCollectionEvent(this, rObject, takeReference);
		if (rClass == MOVELINESTARTPOINTEVENT)
			w = new MoveLineStartPointEvent(this, rObject, takeReference);
		if (rClass == MOVELINEENDPOINTEVENT)
			w = new MoveLineEndPointEvent(this, rObject, takeReference);
		if (rClass == L2CLICKEVENT)
			w = new L2ClickEvent(this, rObject, takeReference);
		if (rClass == LCLICKEVENT)
			w = new LClickEvent(this, rObject, takeReference);
		if (rClass == RCLICKEVENT)
			w = new RClickEvent(this, rObject, takeReference);
		if (rClass == NEWLINEEVENT)
			w = new NewLineEvent(this, rObject, takeReference);
		if (rClass == NEWBOXEVENT)
			w = new NewBoxEvent(this, rObject, takeReference);
		if (rClass == EXECTRANSFEVENT)
			w = new ExecTransfEvent(this, rObject, takeReference);
		if (rClass == NEWPINEVENT)
			w = new NewPinEvent(this, rObject, takeReference);
		if (rClass == CHANGEPARENTEVENT)
			w = new ChangeParentEvent(this, rObject, takeReference);
		if (rClass == ACTIVATEDGREVENT)
			w = new ActivateDgrEvent(this, rObject, takeReference);
		if (rClass == CLOSEDGREVENT)
			w = new CloseDgrEvent(this, rObject, takeReference);
		if (rClass == OKSTYLEDIALOGEVENT)
			w = new OKStyleDialogEvent(this, rObject, takeReference);
		if (rClass == KEYDOWNEVENT)
			w = new KeyDownEvent(this, rObject, takeReference);
		if (rClass == NEWFREEBOXEVENT)
			w = new NewFreeBoxEvent(this, rObject, takeReference);
		if (rClass == NEWFREELINEEVENT)
			w = new NewFreeLineEvent(this, rObject, takeReference);
		if (rClass == FREEBOXEDITEDEVENT)
			w = new FreeBoxEditedEvent(this, rObject, takeReference);
		if (rClass == FREELINEEDITEDEVENT)
			w = new FreeLineEditedEvent(this, rObject, takeReference);
		if (rClass == TOOLBARELEMENTSELECTEVENT)
			w = new ToolbarElementSelectEvent(this, rObject, takeReference);
		if (rClass == STYLE)
			w = new Style(this, rObject, takeReference);
		if (rClass == GRAPHDIAGRAMSTYLE)
			w = new GraphDiagramStyle(this, rObject, takeReference);
		if (rClass == ELEMSTYLE)
			w = new ElemStyle(this, rObject, takeReference);
		if (rClass == NODESTYLE)
			w = new NodeStyle(this, rObject, takeReference);
		if (rClass == EDGESTYLE)
			w = new EdgeStyle(this, rObject, takeReference);
		if (rClass == PORTSTYLE)
			w = new PortStyle(this, rObject, takeReference);
		if (rClass == FREEBOXSTYLE)
			w = new FreeBoxStyle(this, rObject, takeReference);
		if (rClass == FREELINESTYLE)
			w = new FreeLineStyle(this, rObject, takeReference);
		if (rClass == COMPARTSTYLE)
			w = new CompartStyle(this, rObject, takeReference);
		if (rClass == POPUPDIAGRAM)
			w = new PopUpDiagram(this, rObject, takeReference);
		if (rClass == POPUPELEMENT)
			w = new PopUpElement(this, rObject, takeReference);
		if (rClass == PALETTE)
			w = new Palette(this, rObject, takeReference);
		if (rClass == PALETTEELEMENT)
			w = new PaletteElement(this, rObject, takeReference);
		if (rClass == PALETTEBOX)
			w = new PaletteBox(this, rObject, takeReference);
		if (rClass == PALETTELINE)
			w = new PaletteLine(this, rObject, takeReference);
		if (rClass == PALETTEPIN)
			w = new PalettePin(this, rObject, takeReference);
		if (rClass == PALETTEFREEBOX)
			w = new PaletteFreeBox(this, rObject, takeReference);
		if (rClass == PALETTEFREELINE)
			w = new PaletteFreeLine(this, rObject, takeReference);
		if (rClass == TOOLBAR)
			w = new Toolbar(this, rObject, takeReference);
		if (rClass == TOOLBARELEMENT)
			w = new ToolbarElement(this, rObject, takeReference);
		if (rClass == CURRENTDGRPOINTER)
			w = new CurrentDgrPointer(this, rObject, takeReference);
		if (rClass == GRAPHDIAGRAM)
			w = new GraphDiagram(this, rObject, takeReference);
		if (rClass == COLLECTION)
			w = new Collection(this, rObject, takeReference);
		if (rClass == ELEMENT)
			w = new Element(this, rObject, takeReference);
		if (rClass == NODE)
			w = new Node(this, rObject, takeReference);
		if (rClass == EDGE)
			w = new Edge(this, rObject, takeReference);
		if (rClass == PORT)
			w = new Port(this, rObject, takeReference);
		if (rClass == FREEBOX)
			w = new FreeBox(this, rObject, takeReference);
		if (rClass == FREELINE)
			w = new FreeLine(this, rObject, takeReference);
		if (rClass == COMPARTMENT)
			w = new Compartment(this, rObject, takeReference);
		if (rClass == EVENT)
			w = new Event(this, rObject, takeReference);
		if (rClass == FRAME)
			w = new Frame(this, rObject, takeReference);
		if (w==null) {
		}
		if ((w != null) && takeReference)
			wrappers.put(rObject, w);
		return w;
	}

	public boolean deleteModel()
	{
		boolean ok = true;
		if (!AsyncCommand.deleteAllObjects(this))
			ok = false;
		if (!Command.deleteAllObjects(this))
			ok = false;
		if (!PresentationElement.deleteAllObjects(this))
			ok = false;
		if (!GraphDiagramType.deleteAllObjects(this))
			ok = false;
		if (!GraphDiagramEngine.deleteAllObjects(this))
			ok = false;
		if (!Engine.deleteAllObjects(this))
			ok = false;
		if (!OkCmd.deleteAllObjects(this))
			ok = false;
		if (!PopUpCmd.deleteAllObjects(this))
			ok = false;
		if (!ActiveDgrCmd.deleteAllObjects(this))
			ok = false;
		if (!ActiveDgrViewCmd.deleteAllObjects(this))
			ok = false;
		if (!PasteCmd.deleteAllObjects(this))
			ok = false;
		if (!UpdateDgrCmd.deleteAllObjects(this))
			ok = false;
		if (!CloseDgrCmd.deleteAllObjects(this))
			ok = false;
		if (!SaveDgrCmd.deleteAllObjects(this))
			ok = false;
		if (!ActiveElementCmd.deleteAllObjects(this))
			ok = false;
		if (!AfterConfigCmd.deleteAllObjects(this))
			ok = false;
		if (!SaveStylesCmd.deleteAllObjects(this))
			ok = false;
		if (!StyleDialogCmd.deleteAllObjects(this))
			ok = false;
		if (!DefaultStyleCmd.deleteAllObjects(this))
			ok = false;
		if (!RefreshDgrCmd.deleteAllObjects(this))
			ok = false;
		if (!UpdateStyleCmd.deleteAllObjects(this))
			ok = false;
		if (!RerouteCmd.deleteAllObjects(this))
			ok = false;
		if (!AlignSizeCmd.deleteAllObjects(this))
			ok = false;
		if (!PopUpElemSelectEvent.deleteAllObjects(this))
			ok = false;
		if (!PasteGraphClipboardEvent.deleteAllObjects(this))
			ok = false;
		if (!DeleteCollectionEvent.deleteAllObjects(this))
			ok = false;
		if (!CopyCutCollectionEvent.deleteAllObjects(this))
			ok = false;
		if (!CopyCollectionEvent.deleteAllObjects(this))
			ok = false;
		if (!MoveLineStartPointEvent.deleteAllObjects(this))
			ok = false;
		if (!MoveLineEndPointEvent.deleteAllObjects(this))
			ok = false;
		if (!L2ClickEvent.deleteAllObjects(this))
			ok = false;
		if (!LClickEvent.deleteAllObjects(this))
			ok = false;
		if (!RClickEvent.deleteAllObjects(this))
			ok = false;
		if (!NewLineEvent.deleteAllObjects(this))
			ok = false;
		if (!NewBoxEvent.deleteAllObjects(this))
			ok = false;
		if (!ExecTransfEvent.deleteAllObjects(this))
			ok = false;
		if (!NewPinEvent.deleteAllObjects(this))
			ok = false;
		if (!ChangeParentEvent.deleteAllObjects(this))
			ok = false;
		if (!ActivateDgrEvent.deleteAllObjects(this))
			ok = false;
		if (!CloseDgrEvent.deleteAllObjects(this))
			ok = false;
		if (!OKStyleDialogEvent.deleteAllObjects(this))
			ok = false;
		if (!KeyDownEvent.deleteAllObjects(this))
			ok = false;
		if (!NewFreeBoxEvent.deleteAllObjects(this))
			ok = false;
		if (!NewFreeLineEvent.deleteAllObjects(this))
			ok = false;
		if (!FreeBoxEditedEvent.deleteAllObjects(this))
			ok = false;
		if (!FreeLineEditedEvent.deleteAllObjects(this))
			ok = false;
		if (!ToolbarElementSelectEvent.deleteAllObjects(this))
			ok = false;
		if (!Style.deleteAllObjects(this))
			ok = false;
		if (!GraphDiagramStyle.deleteAllObjects(this))
			ok = false;
		if (!ElemStyle.deleteAllObjects(this))
			ok = false;
		if (!NodeStyle.deleteAllObjects(this))
			ok = false;
		if (!EdgeStyle.deleteAllObjects(this))
			ok = false;
		if (!PortStyle.deleteAllObjects(this))
			ok = false;
		if (!FreeBoxStyle.deleteAllObjects(this))
			ok = false;
		if (!FreeLineStyle.deleteAllObjects(this))
			ok = false;
		if (!CompartStyle.deleteAllObjects(this))
			ok = false;
		if (!PopUpDiagram.deleteAllObjects(this))
			ok = false;
		if (!PopUpElement.deleteAllObjects(this))
			ok = false;
		if (!Palette.deleteAllObjects(this))
			ok = false;
		if (!PaletteElement.deleteAllObjects(this))
			ok = false;
		if (!PaletteBox.deleteAllObjects(this))
			ok = false;
		if (!PaletteLine.deleteAllObjects(this))
			ok = false;
		if (!PalettePin.deleteAllObjects(this))
			ok = false;
		if (!PaletteFreeBox.deleteAllObjects(this))
			ok = false;
		if (!PaletteFreeLine.deleteAllObjects(this))
			ok = false;
		if (!Toolbar.deleteAllObjects(this))
			ok = false;
		if (!ToolbarElement.deleteAllObjects(this))
			ok = false;
		if (!CurrentDgrPointer.deleteAllObjects(this))
			ok = false;
		if (!GraphDiagram.deleteAllObjects(this))
			ok = false;
		if (!Collection.deleteAllObjects(this))
			ok = false;
		if (!Element.deleteAllObjects(this))
			ok = false;
		if (!Node.deleteAllObjects(this))
			ok = false;
		if (!Edge.deleteAllObjects(this))
			ok = false;
		if (!Port.deleteAllObjects(this))
			ok = false;
		if (!FreeBox.deleteAllObjects(this))
			ok = false;
		if (!FreeLine.deleteAllObjects(this))
			ok = false;
		if (!Compartment.deleteAllObjects(this))
			ok = false;
		if (!Event.deleteAllObjects(this))
			ok = false;
		if (!Frame.deleteAllObjects(this))
			ok = false;
		return ok; 
	}

	// RAAPI references:
	RAAPI raapi = null;
	public long ASYNCCOMMAND = 0;
	public long COMMAND = 0;
	  public long COMMAND_GRAPHDIAGRAM = 0;
	public long PRESENTATIONELEMENT = 0;
	public long GRAPHDIAGRAMTYPE = 0;
	  public long GRAPHDIAGRAMTYPE_GRAPHDIAGRAM = 0;
	public long GRAPHDIAGRAMENGINE = 0;
	  public long GRAPHDIAGRAMENGINE_ONPOPUPELEMSELECTEVENT = 0;
	  public long GRAPHDIAGRAMENGINE_ONPASTEGRAPHCLIPBOARDEVENT = 0;
	  public long GRAPHDIAGRAMENGINE_ONDELETECOLLECTIONEVENT = 0;
	  public long GRAPHDIAGRAMENGINE_ONCOPYCUTCOLLECTIONEVENT = 0;
	  public long GRAPHDIAGRAMENGINE_ONCOPYCOLLECTIONEVENT = 0;
	  public long GRAPHDIAGRAMENGINE_ONMOVELINESTARTPOINTEVENT = 0;
	  public long GRAPHDIAGRAMENGINE_ONMOVELINEENDPOINTEVENT = 0;
	  public long GRAPHDIAGRAMENGINE_ONL2CLICKEVENT = 0;
	  public long GRAPHDIAGRAMENGINE_ONLCLICKEVENT = 0;
	  public long GRAPHDIAGRAMENGINE_ONRCLICKEVENT = 0;
	  public long GRAPHDIAGRAMENGINE_ONNEWLINEEVENT = 0;
	  public long GRAPHDIAGRAMENGINE_ONNEWBOXEVENT = 0;
	  public long GRAPHDIAGRAMENGINE_ONEXECTRANSFEVENT = 0;
	  public long GRAPHDIAGRAMENGINE_ONNEWPINEVENT = 0;
	  public long GRAPHDIAGRAMENGINE_ONCHANGEPARENTEVENT = 0;
	  public long GRAPHDIAGRAMENGINE_ONACTIVATEDGREVENT = 0;
	  public long GRAPHDIAGRAMENGINE_ONCLOSEDGREVENT = 0;
	  public long GRAPHDIAGRAMENGINE_ONOKSTYLEDIALOGEVENT = 0;
	  public long GRAPHDIAGRAMENGINE_ONKEYDOWNEVENT = 0;
	  public long GRAPHDIAGRAMENGINE_ONNEWFREEBOXEVENT = 0;
	  public long GRAPHDIAGRAMENGINE_ONNEWFREELINEEVENT = 0;
	  public long GRAPHDIAGRAMENGINE_ONFREEBOXEDITEDEVENT = 0;
	  public long GRAPHDIAGRAMENGINE_ONFREELINEEDITEDEVENT = 0;
	public long ENGINE = 0;
	public long OKCMD = 0;
	  public long OKCMD_ELEMENT = 0;
	public long POPUPCMD = 0;
	  public long POPUPCMD_POPUPDIAGRAM = 0;
	public long ACTIVEDGRCMD = 0;
	public long ACTIVEDGRVIEWCMD = 0;
	public long PASTECMD = 0;
	  public long PASTECMD_ELEMENT = 0;
	public long UPDATEDGRCMD = 0;
	public long CLOSEDGRCMD = 0;
	public long SAVEDGRCMD = 0;
	public long ACTIVEELEMENTCMD = 0;
	  public long ACTIVEELEMENTCMD_ELEMENT = 0;
	public long AFTERCONFIGCMD = 0;
	public long SAVESTYLESCMD = 0;
	public long STYLEDIALOGCMD = 0;
	  public long STYLEDIALOGCMD_ELEMENT = 0;
	public long DEFAULTSTYLECMD = 0;
	  public long DEFAULTSTYLECMD_ELEMENT = 0;
	public long REFRESHDGRCMD = 0;
	public long UPDATESTYLECMD = 0;
	  public long UPDATESTYLECMD_ELEMENT = 0;
	  public long UPDATESTYLECMD_ELEMSTYLE = 0;
	public long REROUTECMD = 0;
	  public long REROUTECMD_ELEMENT = 0;
	public long ALIGNSIZECMD = 0;
	  public long ALIGNSIZECMD_ELEMENT = 0;
	public long POPUPELEMSELECTEVENT = 0;
	  public long POPUPELEMSELECTEVENT_POPUPELEMENT = 0;
	public long PASTEGRAPHCLIPBOARDEVENT = 0;
	public long DELETECOLLECTIONEVENT = 0;
	public long COPYCUTCOLLECTIONEVENT = 0;
	public long COPYCOLLECTIONEVENT = 0;
	public long MOVELINESTARTPOINTEVENT = 0;
	  public long MOVELINESTARTPOINTEVENT_TARGET = 0;
	  public long MOVELINESTARTPOINTEVENT_EDGE = 0;
	public long MOVELINEENDPOINTEVENT = 0;
	  public long MOVELINEENDPOINTEVENT_TARGET = 0;
	  public long MOVELINEENDPOINTEVENT_EDGE = 0;
	public long L2CLICKEVENT = 0;
	  public long L2CLICKEVENT_ELEMENT = 0;
	public long LCLICKEVENT = 0;
	  public long LCLICKEVENT_ELEMENT = 0;
	public long RCLICKEVENT = 0;
	  public long RCLICKEVENT_ELEMENT = 0;
	public long NEWLINEEVENT = 0;
	  public long NEWLINEEVENT_START = 0;
	  public long NEWLINEEVENT_END = 0;
	  public long NEWLINEEVENT_PALETTELINE = 0;
	public long NEWBOXEVENT = 0;
	  public long NEWBOXEVENT_PALETTEBOX = 0;
	  public long NEWBOXEVENT_NODE = 0;
	public long EXECTRANSFEVENT = 0;
	public long NEWPINEVENT = 0;
	  public long NEWPINEVENT_NODE = 0;
	  public long NEWPINEVENT_PALETTEPIN = 0;
	public long CHANGEPARENTEVENT = 0;
	  public long CHANGEPARENTEVENT_NODE = 0;
	  public long CHANGEPARENTEVENT_TARGET = 0;
	public long ACTIVATEDGREVENT = 0;
	  public long ACTIVATEDGREVENT_GRAPHDIAGRAM = 0;
	public long CLOSEDGREVENT = 0;
	public long OKSTYLEDIALOGEVENT = 0;
	public long KEYDOWNEVENT = 0;
	public long NEWFREEBOXEVENT = 0;
	  public long NEWFREEBOXEVENT_PALETTEFREEBOX = 0;
	public long NEWFREELINEEVENT = 0;
	  public long NEWFREELINEEVENT_PALETTEFREELINE = 0;
	public long FREEBOXEDITEDEVENT = 0;
	  public long FREEBOXEDITEDEVENT_ELEMENT = 0;
	public long FREELINEEDITEDEVENT = 0;
	  public long FREELINEEDITEDEVENT_ELEMENT = 0;
	public long TOOLBARELEMENTSELECTEVENT = 0;
	  public long TOOLBARELEMENTSELECTEVENT_TOOLBARELEMENT = 0;
	public long STYLE = 0;
	public long GRAPHDIAGRAMSTYLE = 0;
	  public long GRAPHDIAGRAMSTYLE_ID = 0;
	  public long GRAPHDIAGRAMSTYLE_CAPTION = 0;
	  public long GRAPHDIAGRAMSTYLE_BKGCOLOR = 0;
	  public long GRAPHDIAGRAMSTYLE_PRINTZOOM = 0;
	  public long GRAPHDIAGRAMSTYLE_SCREENZOOM = 0;
	  public long GRAPHDIAGRAMSTYLE_LAYOUTMODE = 0;
	  public long GRAPHDIAGRAMSTYLE_LAYOUTALGORITHM = 0;
	  public long GRAPHDIAGRAMSTYLE_GRAPHDIAGRAM = 0;
	public long ELEMSTYLE = 0;
	  public long ELEMSTYLE_ID = 0;
	  public long ELEMSTYLE_CAPTION = 0;
	  public long ELEMSTYLE_SHAPECODE = 0;
	  public long ELEMSTYLE_SHAPESTYLE = 0;
	  public long ELEMSTYLE_LINEWIDTH = 0;
	  public long ELEMSTYLE_DASHLENGTH = 0;
	  public long ELEMSTYLE_BREAKLENGTH = 0;
	  public long ELEMSTYLE_BKGCOLOR = 0;
	  public long ELEMSTYLE_LINECOLOR = 0;
	  public long ELEMSTYLE_DYNAMICTOOLTIP = 0;
	  public long ELEMSTYLE_ELEMENT = 0;
	  public long ELEMSTYLE_UPDATESTYLECMD = 0;
	public long NODESTYLE = 0;
	  public long NODESTYLE_PICTURE = 0;
	  public long NODESTYLE_PICWIDTH = 0;
	  public long NODESTYLE_PICHEIGHT = 0;
	  public long NODESTYLE_PICPOS = 0;
	  public long NODESTYLE_PICSTYLE = 0;
	  public long NODESTYLE_WIDTH = 0;
	  public long NODESTYLE_HEIGHT = 0;
	  public long NODESTYLE_ALIGNMENT = 0;
	public long EDGESTYLE = 0;
	  public long EDGESTYLE_STARTSHAPECODE = 0;
	  public long EDGESTYLE_STARTLINEWIDTH = 0;
	  public long EDGESTYLE_STARTBKGCOLOR = 0;
	  public long EDGESTYLE_STARTLINECOLOR = 0;
	  public long EDGESTYLE_LINETYPE = 0;
	  public long EDGESTYLE_ENDSHAPECODE = 0;
	  public long EDGESTYLE_ENDLINEWIDTH = 0;
	  public long EDGESTYLE_ENDBKGCOLOR = 0;
	  public long EDGESTYLE_ENDLINECOLOR = 0;
	  public long EDGESTYLE_MIDDLESHAPECODE = 0;
	  public long EDGESTYLE_MIDDLELINEWIDTH = 0;
	  public long EDGESTYLE_MIDDLEDASHLENGTH = 0;
	  public long EDGESTYLE_MIDDLEBREAKLENGTH = 0;
	  public long EDGESTYLE_MIDDLEBKGCOLOR = 0;
	  public long EDGESTYLE_MIDDLELINECOLOR = 0;
	public long PORTSTYLE = 0;
	  public long PORTSTYLE_PICTURE = 0;
	  public long PORTSTYLE_WIDTH = 0;
	  public long PORTSTYLE_HEIGHT = 0;
	  public long PORTSTYLE_ALIGNMENT = 0;
	  public long PORTSTYLE_PICPOS = 0;
	  public long PORTSTYLE_PICSTYLE = 0;
	public long FREEBOXSTYLE = 0;
	public long FREELINESTYLE = 0;
	public long COMPARTSTYLE = 0;
	  public long COMPARTSTYLE_ID = 0;
	  public long COMPARTSTYLE_CAPTION = 0;
	  public long COMPARTSTYLE_NR = 0;
	  public long COMPARTSTYLE_ALIGNMENT = 0;
	  public long COMPARTSTYLE_ADJUSTMENT = 0;
	  public long COMPARTSTYLE_PICTURE = 0;
	  public long COMPARTSTYLE_PICWIDTH = 0;
	  public long COMPARTSTYLE_PICHEIGHT = 0;
	  public long COMPARTSTYLE_PICPOS = 0;
	  public long COMPARTSTYLE_PICSTYLE = 0;
	  public long COMPARTSTYLE_ADORNMENT = 0;
	  public long COMPARTSTYLE_LINEWIDTH = 0;
	  public long COMPARTSTYLE_LINECOLOR = 0;
	  public long COMPARTSTYLE_FONTTYPEFACE = 0;
	  public long COMPARTSTYLE_FONTCHARSET = 0;
	  public long COMPARTSTYLE_FONTCOLOR = 0;
	  public long COMPARTSTYLE_FONTSIZE = 0;
	  public long COMPARTSTYLE_FONTPITCH = 0;
	  public long COMPARTSTYLE_FONTSTYLE = 0;
	  public long COMPARTSTYLE_ISVISIBLE = 0;
	  public long COMPARTSTYLE_LINESTARTDIRECTION = 0;
	  public long COMPARTSTYLE_LINEENDDIRECTION = 0;
	  public long COMPARTSTYLE_BREAKATSPACE = 0;
	  public long COMPARTSTYLE_COMPACTVISIBLE = 0;
	  public long COMPARTSTYLE_DYNAMICTOOLTIP = 0;
	  public long COMPARTSTYLE_COMPARTMENT = 0;
	public long POPUPDIAGRAM = 0;
	  public long POPUPDIAGRAM_POPUPCMD = 0;
	  public long POPUPDIAGRAM_POPUPELEMENT = 0;
	public long POPUPELEMENT = 0;
	  public long POPUPELEMENT_CAPTION = 0;
	  public long POPUPELEMENT_PROCEDURENAME = 0;
	  public long POPUPELEMENT_POPUPDIAGRAM = 0;
	  public long POPUPELEMENT_POPUPELEMSELECTEVENT = 0;
	public long PALETTE = 0;
	  public long PALETTE_GRAPHDIAGRAM = 0;
	  public long PALETTE_PALETTEELEMENT = 0;
	public long PALETTEELEMENT = 0;
	  public long PALETTEELEMENT_CAPTION = 0;
	  public long PALETTEELEMENT_PICTURE = 0;
	  public long PALETTEELEMENT_PALETTE = 0;
	public long PALETTEBOX = 0;
	  public long PALETTEBOX_NEWBOXEVENT = 0;
	public long PALETTELINE = 0;
	  public long PALETTELINE_NEWLINEEVENT = 0;
	public long PALETTEPIN = 0;
	  public long PALETTEPIN_NEWPINEVENT = 0;
	public long PALETTEFREEBOX = 0;
	  public long PALETTEFREEBOX_NEWFREEBOXEVENT = 0;
	public long PALETTEFREELINE = 0;
	  public long PALETTEFREELINE_NEWFREELINEEVENT = 0;
	public long TOOLBAR = 0;
	  public long TOOLBAR_GRAPHDIAGRAM = 0;
	  public long TOOLBAR_TOOLBARELEMENT = 0;
	public long TOOLBARELEMENT = 0;
	  public long TOOLBARELEMENT_CAPTION = 0;
	  public long TOOLBARELEMENT_PICTURE = 0;
	  public long TOOLBARELEMENT_PROCEDURENAME = 0;
	  public long TOOLBARELEMENT_TOOLBAR = 0;
	  public long TOOLBARELEMENT_TOOLBARELEMENTSELECTEVENT = 0;
	public long CURRENTDGRPOINTER = 0;
	  public long CURRENTDGRPOINTER_GRAPHDIAGRAM = 0;
	public long GRAPHDIAGRAM = 0;
	  public long GRAPHDIAGRAM_CAPTION = 0;
	  public long GRAPHDIAGRAM_STYLE = 0;
	  public long GRAPHDIAGRAM_GRAPHDGRTYPE = 0;
	  public long GRAPHDIAGRAM_ISREADONLY = 0;
	  public long GRAPHDIAGRAM_REMOTEID = 0;
	  public long GRAPHDIAGRAM_TARGETDIAGRAMREMOTEID = 0;
	  public long GRAPHDIAGRAM_TREEERRORICON = 0;
	  public long GRAPHDIAGRAM_TREEMULTIUSERICON = 0;
	  public long GRAPHDIAGRAM_MULTICOMMENT = 0;
	  public long GRAPHDIAGRAM_ISREADONLY2 = 0;
	  public long GRAPHDIAGRAM_BKGCOLOR = 0;
	  public long GRAPHDIAGRAM_PRINTZOOM = 0;
	  public long GRAPHDIAGRAM_SCREENZOOM = 0;
	  public long GRAPHDIAGRAM_LAYOUTMODE = 0;
	  public long GRAPHDIAGRAM_LAYOUTALGORITHM = 0;
	  public long GRAPHDIAGRAM_PALETTE = 0;
	  public long GRAPHDIAGRAM_TOOLBAR = 0;
	  public long GRAPHDIAGRAM_GRAPHDIAGRAMSTYLE = 0;
	  public long GRAPHDIAGRAM_GRAPHDIAGRAMTYPE = 0;
	  public long GRAPHDIAGRAM_CURRENTDGRPOINTER = 0;
	  public long GRAPHDIAGRAM_COLLECTION = 0;
	  public long GRAPHDIAGRAM_ELEMENT = 0;
	  public long GRAPHDIAGRAM_COMMAND = 0;
	  public long GRAPHDIAGRAM_ACTIVATEDGREVENT = 0;
	  public long GRAPHDIAGRAM_SOURCE = 0;
	  public long GRAPHDIAGRAM_PARENT = 0;
	  public long GRAPHDIAGRAM_FRAME = 0;
	public long COLLECTION = 0;
	  public long COLLECTION_GRAPHDIAGRAM = 0;
	  public long COLLECTION_ELEMENT = 0;
	public long ELEMENT = 0;
	  public long ELEMENT_STYLE = 0;
	  public long ELEMENT_LOCATION = 0;
	  public long ELEMENT_GRAPHDIAGRAM = 0;
	  public long ELEMENT_COLLECTION = 0;
	  public long ELEMENT_ELEMSTYLE = 0;
	  public long ELEMENT_OKCMD = 0;
	  public long ELEMENT_DEFAULTSTYLECMD = 0;
	  public long ELEMENT_PASTECMD = 0;
	  public long ELEMENT_ACTIVEELEMENTCMD = 0;
	  public long ELEMENT_STYLEDIALOGCMD = 0;
	  public long ELEMENT_REROUTECMD = 0;
	  public long ELEMENT_ALIGNSIZECMD = 0;
	  public long ELEMENT_L2CLICKEVENT = 0;
	  public long ELEMENT_LCLICKEVENT = 0;
	  public long ELEMENT_RCLICKEVENT = 0;
	  public long ELEMENT_NEWLINEEVENTS = 0;
	  public long ELEMENT_NEWLINEEVENTE = 0;
	  public long ELEMENT_MOVELINESTARTPOINTEVENTT = 0;
	  public long ELEMENT_MOVELINEENDPOINTEVENTT = 0;
	  public long ELEMENT_FREEBOXEDITEDEVENT = 0;
	  public long ELEMENT_FREELINEEDITEDEVENT = 0;
	  public long ELEMENT_COMPARTMENT = 0;
	  public long ELEMENT_ESTART = 0;
	  public long ELEMENT_EEND = 0;
	  public long ELEMENT_TARGET = 0;
	  public long ELEMENT_CHILD = 0;
	  public long ELEMENT_UPDATESTYLECMD = 0;
	public long NODE = 0;
	  public long NODE_COMPONENT = 0;
	  public long NODE_CONTAINER = 0;
	  public long NODE_PORT = 0;
	  public long NODE_NEWBOXEVENT = 0;
	  public long NODE_CHANGEPARENTEVENTN = 0;
	  public long NODE_CHANGEPARENTEVENTT = 0;
	  public long NODE_NEWPINEVENT = 0;
	public long EDGE = 0;
	  public long EDGE_MOVELINESTARTPOINTEVENTE = 0;
	  public long EDGE_MOVELINEENDPOINTEVENTE = 0;
	  public long EDGE_START = 0;
	  public long EDGE_END = 0;
	public long PORT = 0;
	  public long PORT_NODE = 0;
	public long FREEBOX = 0;
	  public long FREEBOX_FREEBOX_X = 0;
	  public long FREEBOX_FREEBOX_Y = 0;
	  public long FREEBOX_FREEBOX_W = 0;
	  public long FREEBOX_FREEBOX_H = 0;
	  public long FREEBOX_FREEBOX_Z = 0;
	public long FREELINE = 0;
	  public long FREELINE_FREELINE_X1 = 0;
	  public long FREELINE_FREELINE_Y1 = 0;
	  public long FREELINE_FREELINE_XN = 0;
	  public long FREELINE_FREELINE_YN = 0;
	  public long FREELINE_FREELINE_Z = 0;
	public long COMPARTMENT = 0;
	  public long COMPARTMENT_INPUT = 0;
	  public long COMPARTMENT_STYLE = 0;
	  public long COMPARTMENT_VALUE = 0;
	  public long COMPARTMENT_ISGROUP = 0;
	  public long COMPARTMENT_COMPARTSTYLE = 0;
	  public long COMPARTMENT_ELEMENT = 0;
	  public long COMPARTMENT_PARENTCOMPARTMENT = 0;
	  public long COMPARTMENT_SUBCOMPARTMENT = 0;
	public long EVENT = 0;
	public long FRAME = 0;
	  public long FRAME_CAPTION = 0;
	  public long FRAME_CONTENTURI = 0;
	  public long FRAME_LOCATION = 0;
	  public long FRAME_ISRESIZEABLE = 0;
	  public long FRAME_ISCLOSABLE = 0;
	  public long FRAME_ONFRAMEACTIVATEDEVENT = 0;
	  public long FRAME_ONFRAMEDEACTIVATINGEVENT = 0;
	  public long FRAME_ONFRAMERESIZEDEVENT = 0;
	  public long FRAME_ONCLOSEFRAMEREQUESTEDEVENT = 0;
	  public long FRAME_GRAPHDIAGRAM = 0;

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
			if (ASYNCCOMMAND != 0) {
				raapi.freeReference(ASYNCCOMMAND);
				ASYNCCOMMAND = 0;
			}
			if (COMMAND != 0) {
				raapi.freeReference(COMMAND);
				COMMAND = 0;
			}
	  		if (COMMAND_GRAPHDIAGRAM != 0) {
				raapi.freeReference(COMMAND_GRAPHDIAGRAM);
				COMMAND_GRAPHDIAGRAM = 0;
			}
			if (PRESENTATIONELEMENT != 0) {
				raapi.freeReference(PRESENTATIONELEMENT);
				PRESENTATIONELEMENT = 0;
			}
			if (GRAPHDIAGRAMTYPE != 0) {
				raapi.freeReference(GRAPHDIAGRAMTYPE);
				GRAPHDIAGRAMTYPE = 0;
			}
	  		if (GRAPHDIAGRAMTYPE_GRAPHDIAGRAM != 0) {
				raapi.freeReference(GRAPHDIAGRAMTYPE_GRAPHDIAGRAM);
				GRAPHDIAGRAMTYPE_GRAPHDIAGRAM = 0;
			}
			if (GRAPHDIAGRAMENGINE != 0) {
				raapi.freeReference(GRAPHDIAGRAMENGINE);
				GRAPHDIAGRAMENGINE = 0;
			}
	  		if (GRAPHDIAGRAMENGINE_ONPOPUPELEMSELECTEVENT != 0) {
				raapi.freeReference(GRAPHDIAGRAMENGINE_ONPOPUPELEMSELECTEVENT);
				GRAPHDIAGRAMENGINE_ONPOPUPELEMSELECTEVENT = 0;
			}
	  		if (GRAPHDIAGRAMENGINE_ONPASTEGRAPHCLIPBOARDEVENT != 0) {
				raapi.freeReference(GRAPHDIAGRAMENGINE_ONPASTEGRAPHCLIPBOARDEVENT);
				GRAPHDIAGRAMENGINE_ONPASTEGRAPHCLIPBOARDEVENT = 0;
			}
	  		if (GRAPHDIAGRAMENGINE_ONDELETECOLLECTIONEVENT != 0) {
				raapi.freeReference(GRAPHDIAGRAMENGINE_ONDELETECOLLECTIONEVENT);
				GRAPHDIAGRAMENGINE_ONDELETECOLLECTIONEVENT = 0;
			}
	  		if (GRAPHDIAGRAMENGINE_ONCOPYCUTCOLLECTIONEVENT != 0) {
				raapi.freeReference(GRAPHDIAGRAMENGINE_ONCOPYCUTCOLLECTIONEVENT);
				GRAPHDIAGRAMENGINE_ONCOPYCUTCOLLECTIONEVENT = 0;
			}
	  		if (GRAPHDIAGRAMENGINE_ONCOPYCOLLECTIONEVENT != 0) {
				raapi.freeReference(GRAPHDIAGRAMENGINE_ONCOPYCOLLECTIONEVENT);
				GRAPHDIAGRAMENGINE_ONCOPYCOLLECTIONEVENT = 0;
			}
	  		if (GRAPHDIAGRAMENGINE_ONMOVELINESTARTPOINTEVENT != 0) {
				raapi.freeReference(GRAPHDIAGRAMENGINE_ONMOVELINESTARTPOINTEVENT);
				GRAPHDIAGRAMENGINE_ONMOVELINESTARTPOINTEVENT = 0;
			}
	  		if (GRAPHDIAGRAMENGINE_ONMOVELINEENDPOINTEVENT != 0) {
				raapi.freeReference(GRAPHDIAGRAMENGINE_ONMOVELINEENDPOINTEVENT);
				GRAPHDIAGRAMENGINE_ONMOVELINEENDPOINTEVENT = 0;
			}
	  		if (GRAPHDIAGRAMENGINE_ONL2CLICKEVENT != 0) {
				raapi.freeReference(GRAPHDIAGRAMENGINE_ONL2CLICKEVENT);
				GRAPHDIAGRAMENGINE_ONL2CLICKEVENT = 0;
			}
	  		if (GRAPHDIAGRAMENGINE_ONLCLICKEVENT != 0) {
				raapi.freeReference(GRAPHDIAGRAMENGINE_ONLCLICKEVENT);
				GRAPHDIAGRAMENGINE_ONLCLICKEVENT = 0;
			}
	  		if (GRAPHDIAGRAMENGINE_ONRCLICKEVENT != 0) {
				raapi.freeReference(GRAPHDIAGRAMENGINE_ONRCLICKEVENT);
				GRAPHDIAGRAMENGINE_ONRCLICKEVENT = 0;
			}
	  		if (GRAPHDIAGRAMENGINE_ONNEWLINEEVENT != 0) {
				raapi.freeReference(GRAPHDIAGRAMENGINE_ONNEWLINEEVENT);
				GRAPHDIAGRAMENGINE_ONNEWLINEEVENT = 0;
			}
	  		if (GRAPHDIAGRAMENGINE_ONNEWBOXEVENT != 0) {
				raapi.freeReference(GRAPHDIAGRAMENGINE_ONNEWBOXEVENT);
				GRAPHDIAGRAMENGINE_ONNEWBOXEVENT = 0;
			}
	  		if (GRAPHDIAGRAMENGINE_ONEXECTRANSFEVENT != 0) {
				raapi.freeReference(GRAPHDIAGRAMENGINE_ONEXECTRANSFEVENT);
				GRAPHDIAGRAMENGINE_ONEXECTRANSFEVENT = 0;
			}
	  		if (GRAPHDIAGRAMENGINE_ONNEWPINEVENT != 0) {
				raapi.freeReference(GRAPHDIAGRAMENGINE_ONNEWPINEVENT);
				GRAPHDIAGRAMENGINE_ONNEWPINEVENT = 0;
			}
	  		if (GRAPHDIAGRAMENGINE_ONCHANGEPARENTEVENT != 0) {
				raapi.freeReference(GRAPHDIAGRAMENGINE_ONCHANGEPARENTEVENT);
				GRAPHDIAGRAMENGINE_ONCHANGEPARENTEVENT = 0;
			}
	  		if (GRAPHDIAGRAMENGINE_ONACTIVATEDGREVENT != 0) {
				raapi.freeReference(GRAPHDIAGRAMENGINE_ONACTIVATEDGREVENT);
				GRAPHDIAGRAMENGINE_ONACTIVATEDGREVENT = 0;
			}
	  		if (GRAPHDIAGRAMENGINE_ONCLOSEDGREVENT != 0) {
				raapi.freeReference(GRAPHDIAGRAMENGINE_ONCLOSEDGREVENT);
				GRAPHDIAGRAMENGINE_ONCLOSEDGREVENT = 0;
			}
	  		if (GRAPHDIAGRAMENGINE_ONOKSTYLEDIALOGEVENT != 0) {
				raapi.freeReference(GRAPHDIAGRAMENGINE_ONOKSTYLEDIALOGEVENT);
				GRAPHDIAGRAMENGINE_ONOKSTYLEDIALOGEVENT = 0;
			}
	  		if (GRAPHDIAGRAMENGINE_ONKEYDOWNEVENT != 0) {
				raapi.freeReference(GRAPHDIAGRAMENGINE_ONKEYDOWNEVENT);
				GRAPHDIAGRAMENGINE_ONKEYDOWNEVENT = 0;
			}
	  		if (GRAPHDIAGRAMENGINE_ONNEWFREEBOXEVENT != 0) {
				raapi.freeReference(GRAPHDIAGRAMENGINE_ONNEWFREEBOXEVENT);
				GRAPHDIAGRAMENGINE_ONNEWFREEBOXEVENT = 0;
			}
	  		if (GRAPHDIAGRAMENGINE_ONNEWFREELINEEVENT != 0) {
				raapi.freeReference(GRAPHDIAGRAMENGINE_ONNEWFREELINEEVENT);
				GRAPHDIAGRAMENGINE_ONNEWFREELINEEVENT = 0;
			}
	  		if (GRAPHDIAGRAMENGINE_ONFREEBOXEDITEDEVENT != 0) {
				raapi.freeReference(GRAPHDIAGRAMENGINE_ONFREEBOXEDITEDEVENT);
				GRAPHDIAGRAMENGINE_ONFREEBOXEDITEDEVENT = 0;
			}
	  		if (GRAPHDIAGRAMENGINE_ONFREELINEEDITEDEVENT != 0) {
				raapi.freeReference(GRAPHDIAGRAMENGINE_ONFREELINEEDITEDEVENT);
				GRAPHDIAGRAMENGINE_ONFREELINEEDITEDEVENT = 0;
			}
			if (ENGINE != 0) {
				raapi.freeReference(ENGINE);
				ENGINE = 0;
			}
			if (OKCMD != 0) {
				raapi.freeReference(OKCMD);
				OKCMD = 0;
			}
	  		if (OKCMD_ELEMENT != 0) {
				raapi.freeReference(OKCMD_ELEMENT);
				OKCMD_ELEMENT = 0;
			}
			if (POPUPCMD != 0) {
				raapi.freeReference(POPUPCMD);
				POPUPCMD = 0;
			}
	  		if (POPUPCMD_POPUPDIAGRAM != 0) {
				raapi.freeReference(POPUPCMD_POPUPDIAGRAM);
				POPUPCMD_POPUPDIAGRAM = 0;
			}
			if (ACTIVEDGRCMD != 0) {
				raapi.freeReference(ACTIVEDGRCMD);
				ACTIVEDGRCMD = 0;
			}
			if (ACTIVEDGRVIEWCMD != 0) {
				raapi.freeReference(ACTIVEDGRVIEWCMD);
				ACTIVEDGRVIEWCMD = 0;
			}
			if (PASTECMD != 0) {
				raapi.freeReference(PASTECMD);
				PASTECMD = 0;
			}
	  		if (PASTECMD_ELEMENT != 0) {
				raapi.freeReference(PASTECMD_ELEMENT);
				PASTECMD_ELEMENT = 0;
			}
			if (UPDATEDGRCMD != 0) {
				raapi.freeReference(UPDATEDGRCMD);
				UPDATEDGRCMD = 0;
			}
			if (CLOSEDGRCMD != 0) {
				raapi.freeReference(CLOSEDGRCMD);
				CLOSEDGRCMD = 0;
			}
			if (SAVEDGRCMD != 0) {
				raapi.freeReference(SAVEDGRCMD);
				SAVEDGRCMD = 0;
			}
			if (ACTIVEELEMENTCMD != 0) {
				raapi.freeReference(ACTIVEELEMENTCMD);
				ACTIVEELEMENTCMD = 0;
			}
	  		if (ACTIVEELEMENTCMD_ELEMENT != 0) {
				raapi.freeReference(ACTIVEELEMENTCMD_ELEMENT);
				ACTIVEELEMENTCMD_ELEMENT = 0;
			}
			if (AFTERCONFIGCMD != 0) {
				raapi.freeReference(AFTERCONFIGCMD);
				AFTERCONFIGCMD = 0;
			}
			if (SAVESTYLESCMD != 0) {
				raapi.freeReference(SAVESTYLESCMD);
				SAVESTYLESCMD = 0;
			}
			if (STYLEDIALOGCMD != 0) {
				raapi.freeReference(STYLEDIALOGCMD);
				STYLEDIALOGCMD = 0;
			}
	  		if (STYLEDIALOGCMD_ELEMENT != 0) {
				raapi.freeReference(STYLEDIALOGCMD_ELEMENT);
				STYLEDIALOGCMD_ELEMENT = 0;
			}
			if (DEFAULTSTYLECMD != 0) {
				raapi.freeReference(DEFAULTSTYLECMD);
				DEFAULTSTYLECMD = 0;
			}
	  		if (DEFAULTSTYLECMD_ELEMENT != 0) {
				raapi.freeReference(DEFAULTSTYLECMD_ELEMENT);
				DEFAULTSTYLECMD_ELEMENT = 0;
			}
			if (REFRESHDGRCMD != 0) {
				raapi.freeReference(REFRESHDGRCMD);
				REFRESHDGRCMD = 0;
			}
			if (UPDATESTYLECMD != 0) {
				raapi.freeReference(UPDATESTYLECMD);
				UPDATESTYLECMD = 0;
			}
	  		if (UPDATESTYLECMD_ELEMENT != 0) {
				raapi.freeReference(UPDATESTYLECMD_ELEMENT);
				UPDATESTYLECMD_ELEMENT = 0;
			}
	  		if (UPDATESTYLECMD_ELEMSTYLE != 0) {
				raapi.freeReference(UPDATESTYLECMD_ELEMSTYLE);
				UPDATESTYLECMD_ELEMSTYLE = 0;
			}
			if (REROUTECMD != 0) {
				raapi.freeReference(REROUTECMD);
				REROUTECMD = 0;
			}
	  		if (REROUTECMD_ELEMENT != 0) {
				raapi.freeReference(REROUTECMD_ELEMENT);
				REROUTECMD_ELEMENT = 0;
			}
			if (ALIGNSIZECMD != 0) {
				raapi.freeReference(ALIGNSIZECMD);
				ALIGNSIZECMD = 0;
			}
	  		if (ALIGNSIZECMD_ELEMENT != 0) {
				raapi.freeReference(ALIGNSIZECMD_ELEMENT);
				ALIGNSIZECMD_ELEMENT = 0;
			}
			if (POPUPELEMSELECTEVENT != 0) {
				raapi.freeReference(POPUPELEMSELECTEVENT);
				POPUPELEMSELECTEVENT = 0;
			}
	  		if (POPUPELEMSELECTEVENT_POPUPELEMENT != 0) {
				raapi.freeReference(POPUPELEMSELECTEVENT_POPUPELEMENT);
				POPUPELEMSELECTEVENT_POPUPELEMENT = 0;
			}
			if (PASTEGRAPHCLIPBOARDEVENT != 0) {
				raapi.freeReference(PASTEGRAPHCLIPBOARDEVENT);
				PASTEGRAPHCLIPBOARDEVENT = 0;
			}
			if (DELETECOLLECTIONEVENT != 0) {
				raapi.freeReference(DELETECOLLECTIONEVENT);
				DELETECOLLECTIONEVENT = 0;
			}
			if (COPYCUTCOLLECTIONEVENT != 0) {
				raapi.freeReference(COPYCUTCOLLECTIONEVENT);
				COPYCUTCOLLECTIONEVENT = 0;
			}
			if (COPYCOLLECTIONEVENT != 0) {
				raapi.freeReference(COPYCOLLECTIONEVENT);
				COPYCOLLECTIONEVENT = 0;
			}
			if (MOVELINESTARTPOINTEVENT != 0) {
				raapi.freeReference(MOVELINESTARTPOINTEVENT);
				MOVELINESTARTPOINTEVENT = 0;
			}
	  		if (MOVELINESTARTPOINTEVENT_TARGET != 0) {
				raapi.freeReference(MOVELINESTARTPOINTEVENT_TARGET);
				MOVELINESTARTPOINTEVENT_TARGET = 0;
			}
	  		if (MOVELINESTARTPOINTEVENT_EDGE != 0) {
				raapi.freeReference(MOVELINESTARTPOINTEVENT_EDGE);
				MOVELINESTARTPOINTEVENT_EDGE = 0;
			}
			if (MOVELINEENDPOINTEVENT != 0) {
				raapi.freeReference(MOVELINEENDPOINTEVENT);
				MOVELINEENDPOINTEVENT = 0;
			}
	  		if (MOVELINEENDPOINTEVENT_TARGET != 0) {
				raapi.freeReference(MOVELINEENDPOINTEVENT_TARGET);
				MOVELINEENDPOINTEVENT_TARGET = 0;
			}
	  		if (MOVELINEENDPOINTEVENT_EDGE != 0) {
				raapi.freeReference(MOVELINEENDPOINTEVENT_EDGE);
				MOVELINEENDPOINTEVENT_EDGE = 0;
			}
			if (L2CLICKEVENT != 0) {
				raapi.freeReference(L2CLICKEVENT);
				L2CLICKEVENT = 0;
			}
	  		if (L2CLICKEVENT_ELEMENT != 0) {
				raapi.freeReference(L2CLICKEVENT_ELEMENT);
				L2CLICKEVENT_ELEMENT = 0;
			}
			if (LCLICKEVENT != 0) {
				raapi.freeReference(LCLICKEVENT);
				LCLICKEVENT = 0;
			}
	  		if (LCLICKEVENT_ELEMENT != 0) {
				raapi.freeReference(LCLICKEVENT_ELEMENT);
				LCLICKEVENT_ELEMENT = 0;
			}
			if (RCLICKEVENT != 0) {
				raapi.freeReference(RCLICKEVENT);
				RCLICKEVENT = 0;
			}
	  		if (RCLICKEVENT_ELEMENT != 0) {
				raapi.freeReference(RCLICKEVENT_ELEMENT);
				RCLICKEVENT_ELEMENT = 0;
			}
			if (NEWLINEEVENT != 0) {
				raapi.freeReference(NEWLINEEVENT);
				NEWLINEEVENT = 0;
			}
	  		if (NEWLINEEVENT_START != 0) {
				raapi.freeReference(NEWLINEEVENT_START);
				NEWLINEEVENT_START = 0;
			}
	  		if (NEWLINEEVENT_END != 0) {
				raapi.freeReference(NEWLINEEVENT_END);
				NEWLINEEVENT_END = 0;
			}
	  		if (NEWLINEEVENT_PALETTELINE != 0) {
				raapi.freeReference(NEWLINEEVENT_PALETTELINE);
				NEWLINEEVENT_PALETTELINE = 0;
			}
			if (NEWBOXEVENT != 0) {
				raapi.freeReference(NEWBOXEVENT);
				NEWBOXEVENT = 0;
			}
	  		if (NEWBOXEVENT_PALETTEBOX != 0) {
				raapi.freeReference(NEWBOXEVENT_PALETTEBOX);
				NEWBOXEVENT_PALETTEBOX = 0;
			}
	  		if (NEWBOXEVENT_NODE != 0) {
				raapi.freeReference(NEWBOXEVENT_NODE);
				NEWBOXEVENT_NODE = 0;
			}
			if (EXECTRANSFEVENT != 0) {
				raapi.freeReference(EXECTRANSFEVENT);
				EXECTRANSFEVENT = 0;
			}
			if (NEWPINEVENT != 0) {
				raapi.freeReference(NEWPINEVENT);
				NEWPINEVENT = 0;
			}
	  		if (NEWPINEVENT_NODE != 0) {
				raapi.freeReference(NEWPINEVENT_NODE);
				NEWPINEVENT_NODE = 0;
			}
	  		if (NEWPINEVENT_PALETTEPIN != 0) {
				raapi.freeReference(NEWPINEVENT_PALETTEPIN);
				NEWPINEVENT_PALETTEPIN = 0;
			}
			if (CHANGEPARENTEVENT != 0) {
				raapi.freeReference(CHANGEPARENTEVENT);
				CHANGEPARENTEVENT = 0;
			}
	  		if (CHANGEPARENTEVENT_NODE != 0) {
				raapi.freeReference(CHANGEPARENTEVENT_NODE);
				CHANGEPARENTEVENT_NODE = 0;
			}
	  		if (CHANGEPARENTEVENT_TARGET != 0) {
				raapi.freeReference(CHANGEPARENTEVENT_TARGET);
				CHANGEPARENTEVENT_TARGET = 0;
			}
			if (ACTIVATEDGREVENT != 0) {
				raapi.freeReference(ACTIVATEDGREVENT);
				ACTIVATEDGREVENT = 0;
			}
	  		if (ACTIVATEDGREVENT_GRAPHDIAGRAM != 0) {
				raapi.freeReference(ACTIVATEDGREVENT_GRAPHDIAGRAM);
				ACTIVATEDGREVENT_GRAPHDIAGRAM = 0;
			}
			if (CLOSEDGREVENT != 0) {
				raapi.freeReference(CLOSEDGREVENT);
				CLOSEDGREVENT = 0;
			}
			if (OKSTYLEDIALOGEVENT != 0) {
				raapi.freeReference(OKSTYLEDIALOGEVENT);
				OKSTYLEDIALOGEVENT = 0;
			}
			if (KEYDOWNEVENT != 0) {
				raapi.freeReference(KEYDOWNEVENT);
				KEYDOWNEVENT = 0;
			}
			if (NEWFREEBOXEVENT != 0) {
				raapi.freeReference(NEWFREEBOXEVENT);
				NEWFREEBOXEVENT = 0;
			}
	  		if (NEWFREEBOXEVENT_PALETTEFREEBOX != 0) {
				raapi.freeReference(NEWFREEBOXEVENT_PALETTEFREEBOX);
				NEWFREEBOXEVENT_PALETTEFREEBOX = 0;
			}
			if (NEWFREELINEEVENT != 0) {
				raapi.freeReference(NEWFREELINEEVENT);
				NEWFREELINEEVENT = 0;
			}
	  		if (NEWFREELINEEVENT_PALETTEFREELINE != 0) {
				raapi.freeReference(NEWFREELINEEVENT_PALETTEFREELINE);
				NEWFREELINEEVENT_PALETTEFREELINE = 0;
			}
			if (FREEBOXEDITEDEVENT != 0) {
				raapi.freeReference(FREEBOXEDITEDEVENT);
				FREEBOXEDITEDEVENT = 0;
			}
	  		if (FREEBOXEDITEDEVENT_ELEMENT != 0) {
				raapi.freeReference(FREEBOXEDITEDEVENT_ELEMENT);
				FREEBOXEDITEDEVENT_ELEMENT = 0;
			}
			if (FREELINEEDITEDEVENT != 0) {
				raapi.freeReference(FREELINEEDITEDEVENT);
				FREELINEEDITEDEVENT = 0;
			}
	  		if (FREELINEEDITEDEVENT_ELEMENT != 0) {
				raapi.freeReference(FREELINEEDITEDEVENT_ELEMENT);
				FREELINEEDITEDEVENT_ELEMENT = 0;
			}
			if (TOOLBARELEMENTSELECTEVENT != 0) {
				raapi.freeReference(TOOLBARELEMENTSELECTEVENT);
				TOOLBARELEMENTSELECTEVENT = 0;
			}
	  		if (TOOLBARELEMENTSELECTEVENT_TOOLBARELEMENT != 0) {
				raapi.freeReference(TOOLBARELEMENTSELECTEVENT_TOOLBARELEMENT);
				TOOLBARELEMENTSELECTEVENT_TOOLBARELEMENT = 0;
			}
			if (STYLE != 0) {
				raapi.freeReference(STYLE);
				STYLE = 0;
			}
			if (GRAPHDIAGRAMSTYLE != 0) {
				raapi.freeReference(GRAPHDIAGRAMSTYLE);
				GRAPHDIAGRAMSTYLE = 0;
			}
	  		if (GRAPHDIAGRAMSTYLE_ID != 0) {
				raapi.freeReference(GRAPHDIAGRAMSTYLE_ID);
				GRAPHDIAGRAMSTYLE_ID = 0;
			}
	  		if (GRAPHDIAGRAMSTYLE_CAPTION != 0) {
				raapi.freeReference(GRAPHDIAGRAMSTYLE_CAPTION);
				GRAPHDIAGRAMSTYLE_CAPTION = 0;
			}
	  		if (GRAPHDIAGRAMSTYLE_BKGCOLOR != 0) {
				raapi.freeReference(GRAPHDIAGRAMSTYLE_BKGCOLOR);
				GRAPHDIAGRAMSTYLE_BKGCOLOR = 0;
			}
	  		if (GRAPHDIAGRAMSTYLE_PRINTZOOM != 0) {
				raapi.freeReference(GRAPHDIAGRAMSTYLE_PRINTZOOM);
				GRAPHDIAGRAMSTYLE_PRINTZOOM = 0;
			}
	  		if (GRAPHDIAGRAMSTYLE_SCREENZOOM != 0) {
				raapi.freeReference(GRAPHDIAGRAMSTYLE_SCREENZOOM);
				GRAPHDIAGRAMSTYLE_SCREENZOOM = 0;
			}
	  		if (GRAPHDIAGRAMSTYLE_LAYOUTMODE != 0) {
				raapi.freeReference(GRAPHDIAGRAMSTYLE_LAYOUTMODE);
				GRAPHDIAGRAMSTYLE_LAYOUTMODE = 0;
			}
	  		if (GRAPHDIAGRAMSTYLE_LAYOUTALGORITHM != 0) {
				raapi.freeReference(GRAPHDIAGRAMSTYLE_LAYOUTALGORITHM);
				GRAPHDIAGRAMSTYLE_LAYOUTALGORITHM = 0;
			}
	  		if (GRAPHDIAGRAMSTYLE_GRAPHDIAGRAM != 0) {
				raapi.freeReference(GRAPHDIAGRAMSTYLE_GRAPHDIAGRAM);
				GRAPHDIAGRAMSTYLE_GRAPHDIAGRAM = 0;
			}
			if (ELEMSTYLE != 0) {
				raapi.freeReference(ELEMSTYLE);
				ELEMSTYLE = 0;
			}
	  		if (ELEMSTYLE_ID != 0) {
				raapi.freeReference(ELEMSTYLE_ID);
				ELEMSTYLE_ID = 0;
			}
	  		if (ELEMSTYLE_CAPTION != 0) {
				raapi.freeReference(ELEMSTYLE_CAPTION);
				ELEMSTYLE_CAPTION = 0;
			}
	  		if (ELEMSTYLE_SHAPECODE != 0) {
				raapi.freeReference(ELEMSTYLE_SHAPECODE);
				ELEMSTYLE_SHAPECODE = 0;
			}
	  		if (ELEMSTYLE_SHAPESTYLE != 0) {
				raapi.freeReference(ELEMSTYLE_SHAPESTYLE);
				ELEMSTYLE_SHAPESTYLE = 0;
			}
	  		if (ELEMSTYLE_LINEWIDTH != 0) {
				raapi.freeReference(ELEMSTYLE_LINEWIDTH);
				ELEMSTYLE_LINEWIDTH = 0;
			}
	  		if (ELEMSTYLE_DASHLENGTH != 0) {
				raapi.freeReference(ELEMSTYLE_DASHLENGTH);
				ELEMSTYLE_DASHLENGTH = 0;
			}
	  		if (ELEMSTYLE_BREAKLENGTH != 0) {
				raapi.freeReference(ELEMSTYLE_BREAKLENGTH);
				ELEMSTYLE_BREAKLENGTH = 0;
			}
	  		if (ELEMSTYLE_BKGCOLOR != 0) {
				raapi.freeReference(ELEMSTYLE_BKGCOLOR);
				ELEMSTYLE_BKGCOLOR = 0;
			}
	  		if (ELEMSTYLE_LINECOLOR != 0) {
				raapi.freeReference(ELEMSTYLE_LINECOLOR);
				ELEMSTYLE_LINECOLOR = 0;
			}
	  		if (ELEMSTYLE_DYNAMICTOOLTIP != 0) {
				raapi.freeReference(ELEMSTYLE_DYNAMICTOOLTIP);
				ELEMSTYLE_DYNAMICTOOLTIP = 0;
			}
	  		if (ELEMSTYLE_ELEMENT != 0) {
				raapi.freeReference(ELEMSTYLE_ELEMENT);
				ELEMSTYLE_ELEMENT = 0;
			}
	  		if (ELEMSTYLE_UPDATESTYLECMD != 0) {
				raapi.freeReference(ELEMSTYLE_UPDATESTYLECMD);
				ELEMSTYLE_UPDATESTYLECMD = 0;
			}
			if (NODESTYLE != 0) {
				raapi.freeReference(NODESTYLE);
				NODESTYLE = 0;
			}
	  		if (NODESTYLE_PICTURE != 0) {
				raapi.freeReference(NODESTYLE_PICTURE);
				NODESTYLE_PICTURE = 0;
			}
	  		if (NODESTYLE_PICWIDTH != 0) {
				raapi.freeReference(NODESTYLE_PICWIDTH);
				NODESTYLE_PICWIDTH = 0;
			}
	  		if (NODESTYLE_PICHEIGHT != 0) {
				raapi.freeReference(NODESTYLE_PICHEIGHT);
				NODESTYLE_PICHEIGHT = 0;
			}
	  		if (NODESTYLE_PICPOS != 0) {
				raapi.freeReference(NODESTYLE_PICPOS);
				NODESTYLE_PICPOS = 0;
			}
	  		if (NODESTYLE_PICSTYLE != 0) {
				raapi.freeReference(NODESTYLE_PICSTYLE);
				NODESTYLE_PICSTYLE = 0;
			}
	  		if (NODESTYLE_WIDTH != 0) {
				raapi.freeReference(NODESTYLE_WIDTH);
				NODESTYLE_WIDTH = 0;
			}
	  		if (NODESTYLE_HEIGHT != 0) {
				raapi.freeReference(NODESTYLE_HEIGHT);
				NODESTYLE_HEIGHT = 0;
			}
	  		if (NODESTYLE_ALIGNMENT != 0) {
				raapi.freeReference(NODESTYLE_ALIGNMENT);
				NODESTYLE_ALIGNMENT = 0;
			}
			if (EDGESTYLE != 0) {
				raapi.freeReference(EDGESTYLE);
				EDGESTYLE = 0;
			}
	  		if (EDGESTYLE_STARTSHAPECODE != 0) {
				raapi.freeReference(EDGESTYLE_STARTSHAPECODE);
				EDGESTYLE_STARTSHAPECODE = 0;
			}
	  		if (EDGESTYLE_STARTLINEWIDTH != 0) {
				raapi.freeReference(EDGESTYLE_STARTLINEWIDTH);
				EDGESTYLE_STARTLINEWIDTH = 0;
			}
	  		if (EDGESTYLE_STARTBKGCOLOR != 0) {
				raapi.freeReference(EDGESTYLE_STARTBKGCOLOR);
				EDGESTYLE_STARTBKGCOLOR = 0;
			}
	  		if (EDGESTYLE_STARTLINECOLOR != 0) {
				raapi.freeReference(EDGESTYLE_STARTLINECOLOR);
				EDGESTYLE_STARTLINECOLOR = 0;
			}
	  		if (EDGESTYLE_LINETYPE != 0) {
				raapi.freeReference(EDGESTYLE_LINETYPE);
				EDGESTYLE_LINETYPE = 0;
			}
	  		if (EDGESTYLE_ENDSHAPECODE != 0) {
				raapi.freeReference(EDGESTYLE_ENDSHAPECODE);
				EDGESTYLE_ENDSHAPECODE = 0;
			}
	  		if (EDGESTYLE_ENDLINEWIDTH != 0) {
				raapi.freeReference(EDGESTYLE_ENDLINEWIDTH);
				EDGESTYLE_ENDLINEWIDTH = 0;
			}
	  		if (EDGESTYLE_ENDBKGCOLOR != 0) {
				raapi.freeReference(EDGESTYLE_ENDBKGCOLOR);
				EDGESTYLE_ENDBKGCOLOR = 0;
			}
	  		if (EDGESTYLE_ENDLINECOLOR != 0) {
				raapi.freeReference(EDGESTYLE_ENDLINECOLOR);
				EDGESTYLE_ENDLINECOLOR = 0;
			}
	  		if (EDGESTYLE_MIDDLESHAPECODE != 0) {
				raapi.freeReference(EDGESTYLE_MIDDLESHAPECODE);
				EDGESTYLE_MIDDLESHAPECODE = 0;
			}
	  		if (EDGESTYLE_MIDDLELINEWIDTH != 0) {
				raapi.freeReference(EDGESTYLE_MIDDLELINEWIDTH);
				EDGESTYLE_MIDDLELINEWIDTH = 0;
			}
	  		if (EDGESTYLE_MIDDLEDASHLENGTH != 0) {
				raapi.freeReference(EDGESTYLE_MIDDLEDASHLENGTH);
				EDGESTYLE_MIDDLEDASHLENGTH = 0;
			}
	  		if (EDGESTYLE_MIDDLEBREAKLENGTH != 0) {
				raapi.freeReference(EDGESTYLE_MIDDLEBREAKLENGTH);
				EDGESTYLE_MIDDLEBREAKLENGTH = 0;
			}
	  		if (EDGESTYLE_MIDDLEBKGCOLOR != 0) {
				raapi.freeReference(EDGESTYLE_MIDDLEBKGCOLOR);
				EDGESTYLE_MIDDLEBKGCOLOR = 0;
			}
	  		if (EDGESTYLE_MIDDLELINECOLOR != 0) {
				raapi.freeReference(EDGESTYLE_MIDDLELINECOLOR);
				EDGESTYLE_MIDDLELINECOLOR = 0;
			}
			if (PORTSTYLE != 0) {
				raapi.freeReference(PORTSTYLE);
				PORTSTYLE = 0;
			}
	  		if (PORTSTYLE_PICTURE != 0) {
				raapi.freeReference(PORTSTYLE_PICTURE);
				PORTSTYLE_PICTURE = 0;
			}
	  		if (PORTSTYLE_WIDTH != 0) {
				raapi.freeReference(PORTSTYLE_WIDTH);
				PORTSTYLE_WIDTH = 0;
			}
	  		if (PORTSTYLE_HEIGHT != 0) {
				raapi.freeReference(PORTSTYLE_HEIGHT);
				PORTSTYLE_HEIGHT = 0;
			}
	  		if (PORTSTYLE_ALIGNMENT != 0) {
				raapi.freeReference(PORTSTYLE_ALIGNMENT);
				PORTSTYLE_ALIGNMENT = 0;
			}
	  		if (PORTSTYLE_PICPOS != 0) {
				raapi.freeReference(PORTSTYLE_PICPOS);
				PORTSTYLE_PICPOS = 0;
			}
	  		if (PORTSTYLE_PICSTYLE != 0) {
				raapi.freeReference(PORTSTYLE_PICSTYLE);
				PORTSTYLE_PICSTYLE = 0;
			}
			if (FREEBOXSTYLE != 0) {
				raapi.freeReference(FREEBOXSTYLE);
				FREEBOXSTYLE = 0;
			}
			if (FREELINESTYLE != 0) {
				raapi.freeReference(FREELINESTYLE);
				FREELINESTYLE = 0;
			}
			if (COMPARTSTYLE != 0) {
				raapi.freeReference(COMPARTSTYLE);
				COMPARTSTYLE = 0;
			}
	  		if (COMPARTSTYLE_ID != 0) {
				raapi.freeReference(COMPARTSTYLE_ID);
				COMPARTSTYLE_ID = 0;
			}
	  		if (COMPARTSTYLE_CAPTION != 0) {
				raapi.freeReference(COMPARTSTYLE_CAPTION);
				COMPARTSTYLE_CAPTION = 0;
			}
	  		if (COMPARTSTYLE_NR != 0) {
				raapi.freeReference(COMPARTSTYLE_NR);
				COMPARTSTYLE_NR = 0;
			}
	  		if (COMPARTSTYLE_ALIGNMENT != 0) {
				raapi.freeReference(COMPARTSTYLE_ALIGNMENT);
				COMPARTSTYLE_ALIGNMENT = 0;
			}
	  		if (COMPARTSTYLE_ADJUSTMENT != 0) {
				raapi.freeReference(COMPARTSTYLE_ADJUSTMENT);
				COMPARTSTYLE_ADJUSTMENT = 0;
			}
	  		if (COMPARTSTYLE_PICTURE != 0) {
				raapi.freeReference(COMPARTSTYLE_PICTURE);
				COMPARTSTYLE_PICTURE = 0;
			}
	  		if (COMPARTSTYLE_PICWIDTH != 0) {
				raapi.freeReference(COMPARTSTYLE_PICWIDTH);
				COMPARTSTYLE_PICWIDTH = 0;
			}
	  		if (COMPARTSTYLE_PICHEIGHT != 0) {
				raapi.freeReference(COMPARTSTYLE_PICHEIGHT);
				COMPARTSTYLE_PICHEIGHT = 0;
			}
	  		if (COMPARTSTYLE_PICPOS != 0) {
				raapi.freeReference(COMPARTSTYLE_PICPOS);
				COMPARTSTYLE_PICPOS = 0;
			}
	  		if (COMPARTSTYLE_PICSTYLE != 0) {
				raapi.freeReference(COMPARTSTYLE_PICSTYLE);
				COMPARTSTYLE_PICSTYLE = 0;
			}
	  		if (COMPARTSTYLE_ADORNMENT != 0) {
				raapi.freeReference(COMPARTSTYLE_ADORNMENT);
				COMPARTSTYLE_ADORNMENT = 0;
			}
	  		if (COMPARTSTYLE_LINEWIDTH != 0) {
				raapi.freeReference(COMPARTSTYLE_LINEWIDTH);
				COMPARTSTYLE_LINEWIDTH = 0;
			}
	  		if (COMPARTSTYLE_LINECOLOR != 0) {
				raapi.freeReference(COMPARTSTYLE_LINECOLOR);
				COMPARTSTYLE_LINECOLOR = 0;
			}
	  		if (COMPARTSTYLE_FONTTYPEFACE != 0) {
				raapi.freeReference(COMPARTSTYLE_FONTTYPEFACE);
				COMPARTSTYLE_FONTTYPEFACE = 0;
			}
	  		if (COMPARTSTYLE_FONTCHARSET != 0) {
				raapi.freeReference(COMPARTSTYLE_FONTCHARSET);
				COMPARTSTYLE_FONTCHARSET = 0;
			}
	  		if (COMPARTSTYLE_FONTCOLOR != 0) {
				raapi.freeReference(COMPARTSTYLE_FONTCOLOR);
				COMPARTSTYLE_FONTCOLOR = 0;
			}
	  		if (COMPARTSTYLE_FONTSIZE != 0) {
				raapi.freeReference(COMPARTSTYLE_FONTSIZE);
				COMPARTSTYLE_FONTSIZE = 0;
			}
	  		if (COMPARTSTYLE_FONTPITCH != 0) {
				raapi.freeReference(COMPARTSTYLE_FONTPITCH);
				COMPARTSTYLE_FONTPITCH = 0;
			}
	  		if (COMPARTSTYLE_FONTSTYLE != 0) {
				raapi.freeReference(COMPARTSTYLE_FONTSTYLE);
				COMPARTSTYLE_FONTSTYLE = 0;
			}
	  		if (COMPARTSTYLE_ISVISIBLE != 0) {
				raapi.freeReference(COMPARTSTYLE_ISVISIBLE);
				COMPARTSTYLE_ISVISIBLE = 0;
			}
	  		if (COMPARTSTYLE_LINESTARTDIRECTION != 0) {
				raapi.freeReference(COMPARTSTYLE_LINESTARTDIRECTION);
				COMPARTSTYLE_LINESTARTDIRECTION = 0;
			}
	  		if (COMPARTSTYLE_LINEENDDIRECTION != 0) {
				raapi.freeReference(COMPARTSTYLE_LINEENDDIRECTION);
				COMPARTSTYLE_LINEENDDIRECTION = 0;
			}
	  		if (COMPARTSTYLE_BREAKATSPACE != 0) {
				raapi.freeReference(COMPARTSTYLE_BREAKATSPACE);
				COMPARTSTYLE_BREAKATSPACE = 0;
			}
	  		if (COMPARTSTYLE_COMPACTVISIBLE != 0) {
				raapi.freeReference(COMPARTSTYLE_COMPACTVISIBLE);
				COMPARTSTYLE_COMPACTVISIBLE = 0;
			}
	  		if (COMPARTSTYLE_DYNAMICTOOLTIP != 0) {
				raapi.freeReference(COMPARTSTYLE_DYNAMICTOOLTIP);
				COMPARTSTYLE_DYNAMICTOOLTIP = 0;
			}
	  		if (COMPARTSTYLE_COMPARTMENT != 0) {
				raapi.freeReference(COMPARTSTYLE_COMPARTMENT);
				COMPARTSTYLE_COMPARTMENT = 0;
			}
			if (POPUPDIAGRAM != 0) {
				raapi.freeReference(POPUPDIAGRAM);
				POPUPDIAGRAM = 0;
			}
	  		if (POPUPDIAGRAM_POPUPCMD != 0) {
				raapi.freeReference(POPUPDIAGRAM_POPUPCMD);
				POPUPDIAGRAM_POPUPCMD = 0;
			}
	  		if (POPUPDIAGRAM_POPUPELEMENT != 0) {
				raapi.freeReference(POPUPDIAGRAM_POPUPELEMENT);
				POPUPDIAGRAM_POPUPELEMENT = 0;
			}
			if (POPUPELEMENT != 0) {
				raapi.freeReference(POPUPELEMENT);
				POPUPELEMENT = 0;
			}
	  		if (POPUPELEMENT_CAPTION != 0) {
				raapi.freeReference(POPUPELEMENT_CAPTION);
				POPUPELEMENT_CAPTION = 0;
			}
	  		if (POPUPELEMENT_PROCEDURENAME != 0) {
				raapi.freeReference(POPUPELEMENT_PROCEDURENAME);
				POPUPELEMENT_PROCEDURENAME = 0;
			}
	  		if (POPUPELEMENT_POPUPDIAGRAM != 0) {
				raapi.freeReference(POPUPELEMENT_POPUPDIAGRAM);
				POPUPELEMENT_POPUPDIAGRAM = 0;
			}
	  		if (POPUPELEMENT_POPUPELEMSELECTEVENT != 0) {
				raapi.freeReference(POPUPELEMENT_POPUPELEMSELECTEVENT);
				POPUPELEMENT_POPUPELEMSELECTEVENT = 0;
			}
			if (PALETTE != 0) {
				raapi.freeReference(PALETTE);
				PALETTE = 0;
			}
	  		if (PALETTE_GRAPHDIAGRAM != 0) {
				raapi.freeReference(PALETTE_GRAPHDIAGRAM);
				PALETTE_GRAPHDIAGRAM = 0;
			}
	  		if (PALETTE_PALETTEELEMENT != 0) {
				raapi.freeReference(PALETTE_PALETTEELEMENT);
				PALETTE_PALETTEELEMENT = 0;
			}
			if (PALETTEELEMENT != 0) {
				raapi.freeReference(PALETTEELEMENT);
				PALETTEELEMENT = 0;
			}
	  		if (PALETTEELEMENT_CAPTION != 0) {
				raapi.freeReference(PALETTEELEMENT_CAPTION);
				PALETTEELEMENT_CAPTION = 0;
			}
	  		if (PALETTEELEMENT_PICTURE != 0) {
				raapi.freeReference(PALETTEELEMENT_PICTURE);
				PALETTEELEMENT_PICTURE = 0;
			}
	  		if (PALETTEELEMENT_PALETTE != 0) {
				raapi.freeReference(PALETTEELEMENT_PALETTE);
				PALETTEELEMENT_PALETTE = 0;
			}
			if (PALETTEBOX != 0) {
				raapi.freeReference(PALETTEBOX);
				PALETTEBOX = 0;
			}
	  		if (PALETTEBOX_NEWBOXEVENT != 0) {
				raapi.freeReference(PALETTEBOX_NEWBOXEVENT);
				PALETTEBOX_NEWBOXEVENT = 0;
			}
			if (PALETTELINE != 0) {
				raapi.freeReference(PALETTELINE);
				PALETTELINE = 0;
			}
	  		if (PALETTELINE_NEWLINEEVENT != 0) {
				raapi.freeReference(PALETTELINE_NEWLINEEVENT);
				PALETTELINE_NEWLINEEVENT = 0;
			}
			if (PALETTEPIN != 0) {
				raapi.freeReference(PALETTEPIN);
				PALETTEPIN = 0;
			}
	  		if (PALETTEPIN_NEWPINEVENT != 0) {
				raapi.freeReference(PALETTEPIN_NEWPINEVENT);
				PALETTEPIN_NEWPINEVENT = 0;
			}
			if (PALETTEFREEBOX != 0) {
				raapi.freeReference(PALETTEFREEBOX);
				PALETTEFREEBOX = 0;
			}
	  		if (PALETTEFREEBOX_NEWFREEBOXEVENT != 0) {
				raapi.freeReference(PALETTEFREEBOX_NEWFREEBOXEVENT);
				PALETTEFREEBOX_NEWFREEBOXEVENT = 0;
			}
			if (PALETTEFREELINE != 0) {
				raapi.freeReference(PALETTEFREELINE);
				PALETTEFREELINE = 0;
			}
	  		if (PALETTEFREELINE_NEWFREELINEEVENT != 0) {
				raapi.freeReference(PALETTEFREELINE_NEWFREELINEEVENT);
				PALETTEFREELINE_NEWFREELINEEVENT = 0;
			}
			if (TOOLBAR != 0) {
				raapi.freeReference(TOOLBAR);
				TOOLBAR = 0;
			}
	  		if (TOOLBAR_GRAPHDIAGRAM != 0) {
				raapi.freeReference(TOOLBAR_GRAPHDIAGRAM);
				TOOLBAR_GRAPHDIAGRAM = 0;
			}
	  		if (TOOLBAR_TOOLBARELEMENT != 0) {
				raapi.freeReference(TOOLBAR_TOOLBARELEMENT);
				TOOLBAR_TOOLBARELEMENT = 0;
			}
			if (TOOLBARELEMENT != 0) {
				raapi.freeReference(TOOLBARELEMENT);
				TOOLBARELEMENT = 0;
			}
	  		if (TOOLBARELEMENT_CAPTION != 0) {
				raapi.freeReference(TOOLBARELEMENT_CAPTION);
				TOOLBARELEMENT_CAPTION = 0;
			}
	  		if (TOOLBARELEMENT_PICTURE != 0) {
				raapi.freeReference(TOOLBARELEMENT_PICTURE);
				TOOLBARELEMENT_PICTURE = 0;
			}
	  		if (TOOLBARELEMENT_PROCEDURENAME != 0) {
				raapi.freeReference(TOOLBARELEMENT_PROCEDURENAME);
				TOOLBARELEMENT_PROCEDURENAME = 0;
			}
	  		if (TOOLBARELEMENT_TOOLBAR != 0) {
				raapi.freeReference(TOOLBARELEMENT_TOOLBAR);
				TOOLBARELEMENT_TOOLBAR = 0;
			}
	  		if (TOOLBARELEMENT_TOOLBARELEMENTSELECTEVENT != 0) {
				raapi.freeReference(TOOLBARELEMENT_TOOLBARELEMENTSELECTEVENT);
				TOOLBARELEMENT_TOOLBARELEMENTSELECTEVENT = 0;
			}
			if (CURRENTDGRPOINTER != 0) {
				raapi.freeReference(CURRENTDGRPOINTER);
				CURRENTDGRPOINTER = 0;
			}
	  		if (CURRENTDGRPOINTER_GRAPHDIAGRAM != 0) {
				raapi.freeReference(CURRENTDGRPOINTER_GRAPHDIAGRAM);
				CURRENTDGRPOINTER_GRAPHDIAGRAM = 0;
			}
			if (GRAPHDIAGRAM != 0) {
				raapi.freeReference(GRAPHDIAGRAM);
				GRAPHDIAGRAM = 0;
			}
	  		if (GRAPHDIAGRAM_CAPTION != 0) {
				raapi.freeReference(GRAPHDIAGRAM_CAPTION);
				GRAPHDIAGRAM_CAPTION = 0;
			}
	  		if (GRAPHDIAGRAM_STYLE != 0) {
				raapi.freeReference(GRAPHDIAGRAM_STYLE);
				GRAPHDIAGRAM_STYLE = 0;
			}
	  		if (GRAPHDIAGRAM_GRAPHDGRTYPE != 0) {
				raapi.freeReference(GRAPHDIAGRAM_GRAPHDGRTYPE);
				GRAPHDIAGRAM_GRAPHDGRTYPE = 0;
			}
	  		if (GRAPHDIAGRAM_ISREADONLY != 0) {
				raapi.freeReference(GRAPHDIAGRAM_ISREADONLY);
				GRAPHDIAGRAM_ISREADONLY = 0;
			}
	  		if (GRAPHDIAGRAM_REMOTEID != 0) {
				raapi.freeReference(GRAPHDIAGRAM_REMOTEID);
				GRAPHDIAGRAM_REMOTEID = 0;
			}
	  		if (GRAPHDIAGRAM_TARGETDIAGRAMREMOTEID != 0) {
				raapi.freeReference(GRAPHDIAGRAM_TARGETDIAGRAMREMOTEID);
				GRAPHDIAGRAM_TARGETDIAGRAMREMOTEID = 0;
			}
	  		if (GRAPHDIAGRAM_TREEERRORICON != 0) {
				raapi.freeReference(GRAPHDIAGRAM_TREEERRORICON);
				GRAPHDIAGRAM_TREEERRORICON = 0;
			}
	  		if (GRAPHDIAGRAM_TREEMULTIUSERICON != 0) {
				raapi.freeReference(GRAPHDIAGRAM_TREEMULTIUSERICON);
				GRAPHDIAGRAM_TREEMULTIUSERICON = 0;
			}
	  		if (GRAPHDIAGRAM_MULTICOMMENT != 0) {
				raapi.freeReference(GRAPHDIAGRAM_MULTICOMMENT);
				GRAPHDIAGRAM_MULTICOMMENT = 0;
			}
	  		if (GRAPHDIAGRAM_ISREADONLY2 != 0) {
				raapi.freeReference(GRAPHDIAGRAM_ISREADONLY2);
				GRAPHDIAGRAM_ISREADONLY2 = 0;
			}
	  		if (GRAPHDIAGRAM_BKGCOLOR != 0) {
				raapi.freeReference(GRAPHDIAGRAM_BKGCOLOR);
				GRAPHDIAGRAM_BKGCOLOR = 0;
			}
	  		if (GRAPHDIAGRAM_PRINTZOOM != 0) {
				raapi.freeReference(GRAPHDIAGRAM_PRINTZOOM);
				GRAPHDIAGRAM_PRINTZOOM = 0;
			}
	  		if (GRAPHDIAGRAM_SCREENZOOM != 0) {
				raapi.freeReference(GRAPHDIAGRAM_SCREENZOOM);
				GRAPHDIAGRAM_SCREENZOOM = 0;
			}
	  		if (GRAPHDIAGRAM_LAYOUTMODE != 0) {
				raapi.freeReference(GRAPHDIAGRAM_LAYOUTMODE);
				GRAPHDIAGRAM_LAYOUTMODE = 0;
			}
	  		if (GRAPHDIAGRAM_LAYOUTALGORITHM != 0) {
				raapi.freeReference(GRAPHDIAGRAM_LAYOUTALGORITHM);
				GRAPHDIAGRAM_LAYOUTALGORITHM = 0;
			}
	  		if (GRAPHDIAGRAM_PALETTE != 0) {
				raapi.freeReference(GRAPHDIAGRAM_PALETTE);
				GRAPHDIAGRAM_PALETTE = 0;
			}
	  		if (GRAPHDIAGRAM_TOOLBAR != 0) {
				raapi.freeReference(GRAPHDIAGRAM_TOOLBAR);
				GRAPHDIAGRAM_TOOLBAR = 0;
			}
	  		if (GRAPHDIAGRAM_GRAPHDIAGRAMSTYLE != 0) {
				raapi.freeReference(GRAPHDIAGRAM_GRAPHDIAGRAMSTYLE);
				GRAPHDIAGRAM_GRAPHDIAGRAMSTYLE = 0;
			}
	  		if (GRAPHDIAGRAM_GRAPHDIAGRAMTYPE != 0) {
				raapi.freeReference(GRAPHDIAGRAM_GRAPHDIAGRAMTYPE);
				GRAPHDIAGRAM_GRAPHDIAGRAMTYPE = 0;
			}
	  		if (GRAPHDIAGRAM_CURRENTDGRPOINTER != 0) {
				raapi.freeReference(GRAPHDIAGRAM_CURRENTDGRPOINTER);
				GRAPHDIAGRAM_CURRENTDGRPOINTER = 0;
			}
	  		if (GRAPHDIAGRAM_COLLECTION != 0) {
				raapi.freeReference(GRAPHDIAGRAM_COLLECTION);
				GRAPHDIAGRAM_COLLECTION = 0;
			}
	  		if (GRAPHDIAGRAM_ELEMENT != 0) {
				raapi.freeReference(GRAPHDIAGRAM_ELEMENT);
				GRAPHDIAGRAM_ELEMENT = 0;
			}
	  		if (GRAPHDIAGRAM_COMMAND != 0) {
				raapi.freeReference(GRAPHDIAGRAM_COMMAND);
				GRAPHDIAGRAM_COMMAND = 0;
			}
	  		if (GRAPHDIAGRAM_ACTIVATEDGREVENT != 0) {
				raapi.freeReference(GRAPHDIAGRAM_ACTIVATEDGREVENT);
				GRAPHDIAGRAM_ACTIVATEDGREVENT = 0;
			}
	  		if (GRAPHDIAGRAM_SOURCE != 0) {
				raapi.freeReference(GRAPHDIAGRAM_SOURCE);
				GRAPHDIAGRAM_SOURCE = 0;
			}
	  		if (GRAPHDIAGRAM_PARENT != 0) {
				raapi.freeReference(GRAPHDIAGRAM_PARENT);
				GRAPHDIAGRAM_PARENT = 0;
			}
	  		if (GRAPHDIAGRAM_FRAME != 0) {
				raapi.freeReference(GRAPHDIAGRAM_FRAME);
				GRAPHDIAGRAM_FRAME = 0;
			}
			if (COLLECTION != 0) {
				raapi.freeReference(COLLECTION);
				COLLECTION = 0;
			}
	  		if (COLLECTION_GRAPHDIAGRAM != 0) {
				raapi.freeReference(COLLECTION_GRAPHDIAGRAM);
				COLLECTION_GRAPHDIAGRAM = 0;
			}
	  		if (COLLECTION_ELEMENT != 0) {
				raapi.freeReference(COLLECTION_ELEMENT);
				COLLECTION_ELEMENT = 0;
			}
			if (ELEMENT != 0) {
				raapi.freeReference(ELEMENT);
				ELEMENT = 0;
			}
	  		if (ELEMENT_STYLE != 0) {
				raapi.freeReference(ELEMENT_STYLE);
				ELEMENT_STYLE = 0;
			}
	  		if (ELEMENT_LOCATION != 0) {
				raapi.freeReference(ELEMENT_LOCATION);
				ELEMENT_LOCATION = 0;
			}
	  		if (ELEMENT_GRAPHDIAGRAM != 0) {
				raapi.freeReference(ELEMENT_GRAPHDIAGRAM);
				ELEMENT_GRAPHDIAGRAM = 0;
			}
	  		if (ELEMENT_COLLECTION != 0) {
				raapi.freeReference(ELEMENT_COLLECTION);
				ELEMENT_COLLECTION = 0;
			}
	  		if (ELEMENT_ELEMSTYLE != 0) {
				raapi.freeReference(ELEMENT_ELEMSTYLE);
				ELEMENT_ELEMSTYLE = 0;
			}
	  		if (ELEMENT_OKCMD != 0) {
				raapi.freeReference(ELEMENT_OKCMD);
				ELEMENT_OKCMD = 0;
			}
	  		if (ELEMENT_DEFAULTSTYLECMD != 0) {
				raapi.freeReference(ELEMENT_DEFAULTSTYLECMD);
				ELEMENT_DEFAULTSTYLECMD = 0;
			}
	  		if (ELEMENT_PASTECMD != 0) {
				raapi.freeReference(ELEMENT_PASTECMD);
				ELEMENT_PASTECMD = 0;
			}
	  		if (ELEMENT_ACTIVEELEMENTCMD != 0) {
				raapi.freeReference(ELEMENT_ACTIVEELEMENTCMD);
				ELEMENT_ACTIVEELEMENTCMD = 0;
			}
	  		if (ELEMENT_STYLEDIALOGCMD != 0) {
				raapi.freeReference(ELEMENT_STYLEDIALOGCMD);
				ELEMENT_STYLEDIALOGCMD = 0;
			}
	  		if (ELEMENT_REROUTECMD != 0) {
				raapi.freeReference(ELEMENT_REROUTECMD);
				ELEMENT_REROUTECMD = 0;
			}
	  		if (ELEMENT_ALIGNSIZECMD != 0) {
				raapi.freeReference(ELEMENT_ALIGNSIZECMD);
				ELEMENT_ALIGNSIZECMD = 0;
			}
	  		if (ELEMENT_L2CLICKEVENT != 0) {
				raapi.freeReference(ELEMENT_L2CLICKEVENT);
				ELEMENT_L2CLICKEVENT = 0;
			}
	  		if (ELEMENT_LCLICKEVENT != 0) {
				raapi.freeReference(ELEMENT_LCLICKEVENT);
				ELEMENT_LCLICKEVENT = 0;
			}
	  		if (ELEMENT_RCLICKEVENT != 0) {
				raapi.freeReference(ELEMENT_RCLICKEVENT);
				ELEMENT_RCLICKEVENT = 0;
			}
	  		if (ELEMENT_NEWLINEEVENTS != 0) {
				raapi.freeReference(ELEMENT_NEWLINEEVENTS);
				ELEMENT_NEWLINEEVENTS = 0;
			}
	  		if (ELEMENT_NEWLINEEVENTE != 0) {
				raapi.freeReference(ELEMENT_NEWLINEEVENTE);
				ELEMENT_NEWLINEEVENTE = 0;
			}
	  		if (ELEMENT_MOVELINESTARTPOINTEVENTT != 0) {
				raapi.freeReference(ELEMENT_MOVELINESTARTPOINTEVENTT);
				ELEMENT_MOVELINESTARTPOINTEVENTT = 0;
			}
	  		if (ELEMENT_MOVELINEENDPOINTEVENTT != 0) {
				raapi.freeReference(ELEMENT_MOVELINEENDPOINTEVENTT);
				ELEMENT_MOVELINEENDPOINTEVENTT = 0;
			}
	  		if (ELEMENT_FREEBOXEDITEDEVENT != 0) {
				raapi.freeReference(ELEMENT_FREEBOXEDITEDEVENT);
				ELEMENT_FREEBOXEDITEDEVENT = 0;
			}
	  		if (ELEMENT_FREELINEEDITEDEVENT != 0) {
				raapi.freeReference(ELEMENT_FREELINEEDITEDEVENT);
				ELEMENT_FREELINEEDITEDEVENT = 0;
			}
	  		if (ELEMENT_COMPARTMENT != 0) {
				raapi.freeReference(ELEMENT_COMPARTMENT);
				ELEMENT_COMPARTMENT = 0;
			}
	  		if (ELEMENT_ESTART != 0) {
				raapi.freeReference(ELEMENT_ESTART);
				ELEMENT_ESTART = 0;
			}
	  		if (ELEMENT_EEND != 0) {
				raapi.freeReference(ELEMENT_EEND);
				ELEMENT_EEND = 0;
			}
	  		if (ELEMENT_TARGET != 0) {
				raapi.freeReference(ELEMENT_TARGET);
				ELEMENT_TARGET = 0;
			}
	  		if (ELEMENT_CHILD != 0) {
				raapi.freeReference(ELEMENT_CHILD);
				ELEMENT_CHILD = 0;
			}
	  		if (ELEMENT_UPDATESTYLECMD != 0) {
				raapi.freeReference(ELEMENT_UPDATESTYLECMD);
				ELEMENT_UPDATESTYLECMD = 0;
			}
			if (NODE != 0) {
				raapi.freeReference(NODE);
				NODE = 0;
			}
	  		if (NODE_COMPONENT != 0) {
				raapi.freeReference(NODE_COMPONENT);
				NODE_COMPONENT = 0;
			}
	  		if (NODE_CONTAINER != 0) {
				raapi.freeReference(NODE_CONTAINER);
				NODE_CONTAINER = 0;
			}
	  		if (NODE_PORT != 0) {
				raapi.freeReference(NODE_PORT);
				NODE_PORT = 0;
			}
	  		if (NODE_NEWBOXEVENT != 0) {
				raapi.freeReference(NODE_NEWBOXEVENT);
				NODE_NEWBOXEVENT = 0;
			}
	  		if (NODE_CHANGEPARENTEVENTN != 0) {
				raapi.freeReference(NODE_CHANGEPARENTEVENTN);
				NODE_CHANGEPARENTEVENTN = 0;
			}
	  		if (NODE_CHANGEPARENTEVENTT != 0) {
				raapi.freeReference(NODE_CHANGEPARENTEVENTT);
				NODE_CHANGEPARENTEVENTT = 0;
			}
	  		if (NODE_NEWPINEVENT != 0) {
				raapi.freeReference(NODE_NEWPINEVENT);
				NODE_NEWPINEVENT = 0;
			}
			if (EDGE != 0) {
				raapi.freeReference(EDGE);
				EDGE = 0;
			}
	  		if (EDGE_MOVELINESTARTPOINTEVENTE != 0) {
				raapi.freeReference(EDGE_MOVELINESTARTPOINTEVENTE);
				EDGE_MOVELINESTARTPOINTEVENTE = 0;
			}
	  		if (EDGE_MOVELINEENDPOINTEVENTE != 0) {
				raapi.freeReference(EDGE_MOVELINEENDPOINTEVENTE);
				EDGE_MOVELINEENDPOINTEVENTE = 0;
			}
	  		if (EDGE_START != 0) {
				raapi.freeReference(EDGE_START);
				EDGE_START = 0;
			}
	  		if (EDGE_END != 0) {
				raapi.freeReference(EDGE_END);
				EDGE_END = 0;
			}
			if (PORT != 0) {
				raapi.freeReference(PORT);
				PORT = 0;
			}
	  		if (PORT_NODE != 0) {
				raapi.freeReference(PORT_NODE);
				PORT_NODE = 0;
			}
			if (FREEBOX != 0) {
				raapi.freeReference(FREEBOX);
				FREEBOX = 0;
			}
	  		if (FREEBOX_FREEBOX_X != 0) {
				raapi.freeReference(FREEBOX_FREEBOX_X);
				FREEBOX_FREEBOX_X = 0;
			}
	  		if (FREEBOX_FREEBOX_Y != 0) {
				raapi.freeReference(FREEBOX_FREEBOX_Y);
				FREEBOX_FREEBOX_Y = 0;
			}
	  		if (FREEBOX_FREEBOX_W != 0) {
				raapi.freeReference(FREEBOX_FREEBOX_W);
				FREEBOX_FREEBOX_W = 0;
			}
	  		if (FREEBOX_FREEBOX_H != 0) {
				raapi.freeReference(FREEBOX_FREEBOX_H);
				FREEBOX_FREEBOX_H = 0;
			}
	  		if (FREEBOX_FREEBOX_Z != 0) {
				raapi.freeReference(FREEBOX_FREEBOX_Z);
				FREEBOX_FREEBOX_Z = 0;
			}
			if (FREELINE != 0) {
				raapi.freeReference(FREELINE);
				FREELINE = 0;
			}
	  		if (FREELINE_FREELINE_X1 != 0) {
				raapi.freeReference(FREELINE_FREELINE_X1);
				FREELINE_FREELINE_X1 = 0;
			}
	  		if (FREELINE_FREELINE_Y1 != 0) {
				raapi.freeReference(FREELINE_FREELINE_Y1);
				FREELINE_FREELINE_Y1 = 0;
			}
	  		if (FREELINE_FREELINE_XN != 0) {
				raapi.freeReference(FREELINE_FREELINE_XN);
				FREELINE_FREELINE_XN = 0;
			}
	  		if (FREELINE_FREELINE_YN != 0) {
				raapi.freeReference(FREELINE_FREELINE_YN);
				FREELINE_FREELINE_YN = 0;
			}
	  		if (FREELINE_FREELINE_Z != 0) {
				raapi.freeReference(FREELINE_FREELINE_Z);
				FREELINE_FREELINE_Z = 0;
			}
			if (COMPARTMENT != 0) {
				raapi.freeReference(COMPARTMENT);
				COMPARTMENT = 0;
			}
	  		if (COMPARTMENT_INPUT != 0) {
				raapi.freeReference(COMPARTMENT_INPUT);
				COMPARTMENT_INPUT = 0;
			}
	  		if (COMPARTMENT_STYLE != 0) {
				raapi.freeReference(COMPARTMENT_STYLE);
				COMPARTMENT_STYLE = 0;
			}
	  		if (COMPARTMENT_VALUE != 0) {
				raapi.freeReference(COMPARTMENT_VALUE);
				COMPARTMENT_VALUE = 0;
			}
	  		if (COMPARTMENT_ISGROUP != 0) {
				raapi.freeReference(COMPARTMENT_ISGROUP);
				COMPARTMENT_ISGROUP = 0;
			}
	  		if (COMPARTMENT_COMPARTSTYLE != 0) {
				raapi.freeReference(COMPARTMENT_COMPARTSTYLE);
				COMPARTMENT_COMPARTSTYLE = 0;
			}
	  		if (COMPARTMENT_ELEMENT != 0) {
				raapi.freeReference(COMPARTMENT_ELEMENT);
				COMPARTMENT_ELEMENT = 0;
			}
	  		if (COMPARTMENT_PARENTCOMPARTMENT != 0) {
				raapi.freeReference(COMPARTMENT_PARENTCOMPARTMENT);
				COMPARTMENT_PARENTCOMPARTMENT = 0;
			}
	  		if (COMPARTMENT_SUBCOMPARTMENT != 0) {
				raapi.freeReference(COMPARTMENT_SUBCOMPARTMENT);
				COMPARTMENT_SUBCOMPARTMENT = 0;
			}
			if (EVENT != 0) {
				raapi.freeReference(EVENT);
				EVENT = 0;
			}
			if (FRAME != 0) {
				raapi.freeReference(FRAME);
				FRAME = 0;
			}
	  		if (FRAME_CAPTION != 0) {
				raapi.freeReference(FRAME_CAPTION);
				FRAME_CAPTION = 0;
			}
	  		if (FRAME_CONTENTURI != 0) {
				raapi.freeReference(FRAME_CONTENTURI);
				FRAME_CONTENTURI = 0;
			}
	  		if (FRAME_LOCATION != 0) {
				raapi.freeReference(FRAME_LOCATION);
				FRAME_LOCATION = 0;
			}
	  		if (FRAME_ISRESIZEABLE != 0) {
				raapi.freeReference(FRAME_ISRESIZEABLE);
				FRAME_ISRESIZEABLE = 0;
			}
	  		if (FRAME_ISCLOSABLE != 0) {
				raapi.freeReference(FRAME_ISCLOSABLE);
				FRAME_ISCLOSABLE = 0;
			}
	  		if (FRAME_ONFRAMEACTIVATEDEVENT != 0) {
				raapi.freeReference(FRAME_ONFRAMEACTIVATEDEVENT);
				FRAME_ONFRAMEACTIVATEDEVENT = 0;
			}
	  		if (FRAME_ONFRAMEDEACTIVATINGEVENT != 0) {
				raapi.freeReference(FRAME_ONFRAMEDEACTIVATINGEVENT);
				FRAME_ONFRAMEDEACTIVATINGEVENT = 0;
			}
	  		if (FRAME_ONFRAMERESIZEDEVENT != 0) {
				raapi.freeReference(FRAME_ONFRAMERESIZEDEVENT);
				FRAME_ONFRAMERESIZEDEVENT = 0;
			}
	  		if (FRAME_ONCLOSEFRAMEREQUESTEDEVENT != 0) {
				raapi.freeReference(FRAME_ONCLOSEFRAMEREQUESTEDEVENT);
				FRAME_ONCLOSEFRAMEREQUESTEDEVENT = 0;
			}
	  		if (FRAME_GRAPHDIAGRAM != 0) {
				raapi.freeReference(FRAME_GRAPHDIAGRAM);
				FRAME_GRAPHDIAGRAM = 0;
			}
		}

		raapi = _raapi;

		if (raapi != null) {
			// initializing class references...
			ASYNCCOMMAND = raapi.findClass("TDAKernel::AsyncCommand");
			if ((ASYNCCOMMAND == 0) && (prefix != null))
				ASYNCCOMMAND = raapi.findClass(prefix+"TDAKernel::AsyncCommand");
			if ((ASYNCCOMMAND == 0) && insertMetamodel)
				ASYNCCOMMAND = raapi.createClass(prefix+"TDAKernel::AsyncCommand");
			if (ASYNCCOMMAND == 0) {				
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the class TDAKernel::AsyncCommand.");
			}
			COMMAND = raapi.findClass("TDAKernel::Command");
			if ((COMMAND == 0) && (prefix != null))
				COMMAND = raapi.findClass(prefix+"TDAKernel::Command");
			if ((COMMAND == 0) && insertMetamodel)
				COMMAND = raapi.createClass(prefix+"TDAKernel::Command");
			if (COMMAND == 0) {				
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the class TDAKernel::Command.");
			}
			PRESENTATIONELEMENT = raapi.findClass("PresentationElement");
			if ((PRESENTATIONELEMENT == 0) && (prefix != null))
				PRESENTATIONELEMENT = raapi.findClass(prefix+"PresentationElement");
			if ((PRESENTATIONELEMENT == 0) && insertMetamodel)
				PRESENTATIONELEMENT = raapi.createClass(prefix+"PresentationElement");
			if (PRESENTATIONELEMENT == 0) {				
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the class PresentationElement.");
			}
			GRAPHDIAGRAMTYPE = raapi.findClass("GraphDiagramType");
			if ((GRAPHDIAGRAMTYPE == 0) && (prefix != null))
				GRAPHDIAGRAMTYPE = raapi.findClass(prefix+"GraphDiagramType");
			if ((GRAPHDIAGRAMTYPE == 0) && insertMetamodel)
				GRAPHDIAGRAMTYPE = raapi.createClass(prefix+"GraphDiagramType");
			if (GRAPHDIAGRAMTYPE == 0) {				
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the class GraphDiagramType.");
			}
			GRAPHDIAGRAMENGINE = raapi.findClass("GraphDiagramEngine");
			if ((GRAPHDIAGRAMENGINE == 0) && (prefix != null))
				GRAPHDIAGRAMENGINE = raapi.findClass(prefix+"GraphDiagramEngine");
			if ((GRAPHDIAGRAMENGINE == 0) && insertMetamodel)
				GRAPHDIAGRAMENGINE = raapi.createClass(prefix+"GraphDiagramEngine");
			if (GRAPHDIAGRAMENGINE == 0) {				
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the class GraphDiagramEngine.");
			}
			ENGINE = raapi.findClass("TDAKernel::Engine");
			if ((ENGINE == 0) && (prefix != null))
				ENGINE = raapi.findClass(prefix+"TDAKernel::Engine");
			if ((ENGINE == 0) && insertMetamodel)
				ENGINE = raapi.createClass(prefix+"TDAKernel::Engine");
			if (ENGINE == 0) {				
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the class TDAKernel::Engine.");
			}
			OKCMD = raapi.findClass("OkCmd");
			if ((OKCMD == 0) && (prefix != null))
				OKCMD = raapi.findClass(prefix+"OkCmd");
			if ((OKCMD == 0) && insertMetamodel)
				OKCMD = raapi.createClass(prefix+"OkCmd");
			if (OKCMD == 0) {				
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the class OkCmd.");
			}
			POPUPCMD = raapi.findClass("PopUpCmd");
			if ((POPUPCMD == 0) && (prefix != null))
				POPUPCMD = raapi.findClass(prefix+"PopUpCmd");
			if ((POPUPCMD == 0) && insertMetamodel)
				POPUPCMD = raapi.createClass(prefix+"PopUpCmd");
			if (POPUPCMD == 0) {				
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the class PopUpCmd.");
			}
			ACTIVEDGRCMD = raapi.findClass("ActiveDgrCmd");
			if ((ACTIVEDGRCMD == 0) && (prefix != null))
				ACTIVEDGRCMD = raapi.findClass(prefix+"ActiveDgrCmd");
			if ((ACTIVEDGRCMD == 0) && insertMetamodel)
				ACTIVEDGRCMD = raapi.createClass(prefix+"ActiveDgrCmd");
			if (ACTIVEDGRCMD == 0) {				
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the class ActiveDgrCmd.");
			}
			ACTIVEDGRVIEWCMD = raapi.findClass("ActiveDgrViewCmd");
			if ((ACTIVEDGRVIEWCMD == 0) && (prefix != null))
				ACTIVEDGRVIEWCMD = raapi.findClass(prefix+"ActiveDgrViewCmd");
			if ((ACTIVEDGRVIEWCMD == 0) && insertMetamodel)
				ACTIVEDGRVIEWCMD = raapi.createClass(prefix+"ActiveDgrViewCmd");
			if (ACTIVEDGRVIEWCMD == 0) {				
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the class ActiveDgrViewCmd.");
			}
			PASTECMD = raapi.findClass("PasteCmd");
			if ((PASTECMD == 0) && (prefix != null))
				PASTECMD = raapi.findClass(prefix+"PasteCmd");
			if ((PASTECMD == 0) && insertMetamodel)
				PASTECMD = raapi.createClass(prefix+"PasteCmd");
			if (PASTECMD == 0) {				
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the class PasteCmd.");
			}
			UPDATEDGRCMD = raapi.findClass("UpdateDgrCmd");
			if ((UPDATEDGRCMD == 0) && (prefix != null))
				UPDATEDGRCMD = raapi.findClass(prefix+"UpdateDgrCmd");
			if ((UPDATEDGRCMD == 0) && insertMetamodel)
				UPDATEDGRCMD = raapi.createClass(prefix+"UpdateDgrCmd");
			if (UPDATEDGRCMD == 0) {				
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the class UpdateDgrCmd.");
			}
			CLOSEDGRCMD = raapi.findClass("CloseDgrCmd");
			if ((CLOSEDGRCMD == 0) && (prefix != null))
				CLOSEDGRCMD = raapi.findClass(prefix+"CloseDgrCmd");
			if ((CLOSEDGRCMD == 0) && insertMetamodel)
				CLOSEDGRCMD = raapi.createClass(prefix+"CloseDgrCmd");
			if (CLOSEDGRCMD == 0) {				
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the class CloseDgrCmd.");
			}
			SAVEDGRCMD = raapi.findClass("SaveDgrCmd");
			if ((SAVEDGRCMD == 0) && (prefix != null))
				SAVEDGRCMD = raapi.findClass(prefix+"SaveDgrCmd");
			if ((SAVEDGRCMD == 0) && insertMetamodel)
				SAVEDGRCMD = raapi.createClass(prefix+"SaveDgrCmd");
			if (SAVEDGRCMD == 0) {				
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the class SaveDgrCmd.");
			}
			ACTIVEELEMENTCMD = raapi.findClass("ActiveElementCmd");
			if ((ACTIVEELEMENTCMD == 0) && (prefix != null))
				ACTIVEELEMENTCMD = raapi.findClass(prefix+"ActiveElementCmd");
			if ((ACTIVEELEMENTCMD == 0) && insertMetamodel)
				ACTIVEELEMENTCMD = raapi.createClass(prefix+"ActiveElementCmd");
			if (ACTIVEELEMENTCMD == 0) {				
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the class ActiveElementCmd.");
			}
			AFTERCONFIGCMD = raapi.findClass("AfterConfigCmd");
			if ((AFTERCONFIGCMD == 0) && (prefix != null))
				AFTERCONFIGCMD = raapi.findClass(prefix+"AfterConfigCmd");
			if ((AFTERCONFIGCMD == 0) && insertMetamodel)
				AFTERCONFIGCMD = raapi.createClass(prefix+"AfterConfigCmd");
			if (AFTERCONFIGCMD == 0) {				
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the class AfterConfigCmd.");
			}
			SAVESTYLESCMD = raapi.findClass("SaveStylesCmd");
			if ((SAVESTYLESCMD == 0) && (prefix != null))
				SAVESTYLESCMD = raapi.findClass(prefix+"SaveStylesCmd");
			if ((SAVESTYLESCMD == 0) && insertMetamodel)
				SAVESTYLESCMD = raapi.createClass(prefix+"SaveStylesCmd");
			if (SAVESTYLESCMD == 0) {				
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the class SaveStylesCmd.");
			}
			STYLEDIALOGCMD = raapi.findClass("StyleDialogCmd");
			if ((STYLEDIALOGCMD == 0) && (prefix != null))
				STYLEDIALOGCMD = raapi.findClass(prefix+"StyleDialogCmd");
			if ((STYLEDIALOGCMD == 0) && insertMetamodel)
				STYLEDIALOGCMD = raapi.createClass(prefix+"StyleDialogCmd");
			if (STYLEDIALOGCMD == 0) {				
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the class StyleDialogCmd.");
			}
			DEFAULTSTYLECMD = raapi.findClass("DefaultStyleCmd");
			if ((DEFAULTSTYLECMD == 0) && (prefix != null))
				DEFAULTSTYLECMD = raapi.findClass(prefix+"DefaultStyleCmd");
			if ((DEFAULTSTYLECMD == 0) && insertMetamodel)
				DEFAULTSTYLECMD = raapi.createClass(prefix+"DefaultStyleCmd");
			if (DEFAULTSTYLECMD == 0) {				
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the class DefaultStyleCmd.");
			}
			REFRESHDGRCMD = raapi.findClass("RefreshDgrCmd");
			if ((REFRESHDGRCMD == 0) && (prefix != null))
				REFRESHDGRCMD = raapi.findClass(prefix+"RefreshDgrCmd");
			if ((REFRESHDGRCMD == 0) && insertMetamodel)
				REFRESHDGRCMD = raapi.createClass(prefix+"RefreshDgrCmd");
			if (REFRESHDGRCMD == 0) {				
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the class RefreshDgrCmd.");
			}
			UPDATESTYLECMD = raapi.findClass("UpdateStyleCmd");
			if ((UPDATESTYLECMD == 0) && (prefix != null))
				UPDATESTYLECMD = raapi.findClass(prefix+"UpdateStyleCmd");
			if ((UPDATESTYLECMD == 0) && insertMetamodel)
				UPDATESTYLECMD = raapi.createClass(prefix+"UpdateStyleCmd");
			if (UPDATESTYLECMD == 0) {				
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the class UpdateStyleCmd.");
			}
			REROUTECMD = raapi.findClass("RerouteCmd");
			if ((REROUTECMD == 0) && (prefix != null))
				REROUTECMD = raapi.findClass(prefix+"RerouteCmd");
			if ((REROUTECMD == 0) && insertMetamodel)
				REROUTECMD = raapi.createClass(prefix+"RerouteCmd");
			if (REROUTECMD == 0) {				
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the class RerouteCmd.");
			}
			ALIGNSIZECMD = raapi.findClass("AlignSizeCmd");
			if ((ALIGNSIZECMD == 0) && (prefix != null))
				ALIGNSIZECMD = raapi.findClass(prefix+"AlignSizeCmd");
			if ((ALIGNSIZECMD == 0) && insertMetamodel)
				ALIGNSIZECMD = raapi.createClass(prefix+"AlignSizeCmd");
			if (ALIGNSIZECMD == 0) {				
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the class AlignSizeCmd.");
			}
			POPUPELEMSELECTEVENT = raapi.findClass("PopUpElemSelectEvent");
			if ((POPUPELEMSELECTEVENT == 0) && (prefix != null))
				POPUPELEMSELECTEVENT = raapi.findClass(prefix+"PopUpElemSelectEvent");
			if ((POPUPELEMSELECTEVENT == 0) && insertMetamodel)
				POPUPELEMSELECTEVENT = raapi.createClass(prefix+"PopUpElemSelectEvent");
			if (POPUPELEMSELECTEVENT == 0) {				
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the class PopUpElemSelectEvent.");
			}
			PASTEGRAPHCLIPBOARDEVENT = raapi.findClass("PasteGraphClipboardEvent");
			if ((PASTEGRAPHCLIPBOARDEVENT == 0) && (prefix != null))
				PASTEGRAPHCLIPBOARDEVENT = raapi.findClass(prefix+"PasteGraphClipboardEvent");
			if ((PASTEGRAPHCLIPBOARDEVENT == 0) && insertMetamodel)
				PASTEGRAPHCLIPBOARDEVENT = raapi.createClass(prefix+"PasteGraphClipboardEvent");
			if (PASTEGRAPHCLIPBOARDEVENT == 0) {				
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the class PasteGraphClipboardEvent.");
			}
			DELETECOLLECTIONEVENT = raapi.findClass("DeleteCollectionEvent");
			if ((DELETECOLLECTIONEVENT == 0) && (prefix != null))
				DELETECOLLECTIONEVENT = raapi.findClass(prefix+"DeleteCollectionEvent");
			if ((DELETECOLLECTIONEVENT == 0) && insertMetamodel)
				DELETECOLLECTIONEVENT = raapi.createClass(prefix+"DeleteCollectionEvent");
			if (DELETECOLLECTIONEVENT == 0) {				
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the class DeleteCollectionEvent.");
			}
			COPYCUTCOLLECTIONEVENT = raapi.findClass("CopyCutCollectionEvent");
			if ((COPYCUTCOLLECTIONEVENT == 0) && (prefix != null))
				COPYCUTCOLLECTIONEVENT = raapi.findClass(prefix+"CopyCutCollectionEvent");
			if ((COPYCUTCOLLECTIONEVENT == 0) && insertMetamodel)
				COPYCUTCOLLECTIONEVENT = raapi.createClass(prefix+"CopyCutCollectionEvent");
			if (COPYCUTCOLLECTIONEVENT == 0) {				
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the class CopyCutCollectionEvent.");
			}
			COPYCOLLECTIONEVENT = raapi.findClass("CopyCollectionEvent");
			if ((COPYCOLLECTIONEVENT == 0) && (prefix != null))
				COPYCOLLECTIONEVENT = raapi.findClass(prefix+"CopyCollectionEvent");
			if ((COPYCOLLECTIONEVENT == 0) && insertMetamodel)
				COPYCOLLECTIONEVENT = raapi.createClass(prefix+"CopyCollectionEvent");
			if (COPYCOLLECTIONEVENT == 0) {				
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the class CopyCollectionEvent.");
			}
			MOVELINESTARTPOINTEVENT = raapi.findClass("MoveLineStartPointEvent");
			if ((MOVELINESTARTPOINTEVENT == 0) && (prefix != null))
				MOVELINESTARTPOINTEVENT = raapi.findClass(prefix+"MoveLineStartPointEvent");
			if ((MOVELINESTARTPOINTEVENT == 0) && insertMetamodel)
				MOVELINESTARTPOINTEVENT = raapi.createClass(prefix+"MoveLineStartPointEvent");
			if (MOVELINESTARTPOINTEVENT == 0) {				
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the class MoveLineStartPointEvent.");
			}
			MOVELINEENDPOINTEVENT = raapi.findClass("MoveLineEndPointEvent");
			if ((MOVELINEENDPOINTEVENT == 0) && (prefix != null))
				MOVELINEENDPOINTEVENT = raapi.findClass(prefix+"MoveLineEndPointEvent");
			if ((MOVELINEENDPOINTEVENT == 0) && insertMetamodel)
				MOVELINEENDPOINTEVENT = raapi.createClass(prefix+"MoveLineEndPointEvent");
			if (MOVELINEENDPOINTEVENT == 0) {				
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the class MoveLineEndPointEvent.");
			}
			L2CLICKEVENT = raapi.findClass("L2ClickEvent");
			if ((L2CLICKEVENT == 0) && (prefix != null))
				L2CLICKEVENT = raapi.findClass(prefix+"L2ClickEvent");
			if ((L2CLICKEVENT == 0) && insertMetamodel)
				L2CLICKEVENT = raapi.createClass(prefix+"L2ClickEvent");
			if (L2CLICKEVENT == 0) {				
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the class L2ClickEvent.");
			}
			LCLICKEVENT = raapi.findClass("LClickEvent");
			if ((LCLICKEVENT == 0) && (prefix != null))
				LCLICKEVENT = raapi.findClass(prefix+"LClickEvent");
			if ((LCLICKEVENT == 0) && insertMetamodel)
				LCLICKEVENT = raapi.createClass(prefix+"LClickEvent");
			if (LCLICKEVENT == 0) {				
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the class LClickEvent.");
			}
			RCLICKEVENT = raapi.findClass("RClickEvent");
			if ((RCLICKEVENT == 0) && (prefix != null))
				RCLICKEVENT = raapi.findClass(prefix+"RClickEvent");
			if ((RCLICKEVENT == 0) && insertMetamodel)
				RCLICKEVENT = raapi.createClass(prefix+"RClickEvent");
			if (RCLICKEVENT == 0) {				
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the class RClickEvent.");
			}
			NEWLINEEVENT = raapi.findClass("NewLineEvent");
			if ((NEWLINEEVENT == 0) && (prefix != null))
				NEWLINEEVENT = raapi.findClass(prefix+"NewLineEvent");
			if ((NEWLINEEVENT == 0) && insertMetamodel)
				NEWLINEEVENT = raapi.createClass(prefix+"NewLineEvent");
			if (NEWLINEEVENT == 0) {				
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the class NewLineEvent.");
			}
			NEWBOXEVENT = raapi.findClass("NewBoxEvent");
			if ((NEWBOXEVENT == 0) && (prefix != null))
				NEWBOXEVENT = raapi.findClass(prefix+"NewBoxEvent");
			if ((NEWBOXEVENT == 0) && insertMetamodel)
				NEWBOXEVENT = raapi.createClass(prefix+"NewBoxEvent");
			if (NEWBOXEVENT == 0) {				
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the class NewBoxEvent.");
			}
			EXECTRANSFEVENT = raapi.findClass("ExecTransfEvent");
			if ((EXECTRANSFEVENT == 0) && (prefix != null))
				EXECTRANSFEVENT = raapi.findClass(prefix+"ExecTransfEvent");
			if ((EXECTRANSFEVENT == 0) && insertMetamodel)
				EXECTRANSFEVENT = raapi.createClass(prefix+"ExecTransfEvent");
			if (EXECTRANSFEVENT == 0) {				
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the class ExecTransfEvent.");
			}
			NEWPINEVENT = raapi.findClass("NewPinEvent");
			if ((NEWPINEVENT == 0) && (prefix != null))
				NEWPINEVENT = raapi.findClass(prefix+"NewPinEvent");
			if ((NEWPINEVENT == 0) && insertMetamodel)
				NEWPINEVENT = raapi.createClass(prefix+"NewPinEvent");
			if (NEWPINEVENT == 0) {				
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the class NewPinEvent.");
			}
			CHANGEPARENTEVENT = raapi.findClass("ChangeParentEvent");
			if ((CHANGEPARENTEVENT == 0) && (prefix != null))
				CHANGEPARENTEVENT = raapi.findClass(prefix+"ChangeParentEvent");
			if ((CHANGEPARENTEVENT == 0) && insertMetamodel)
				CHANGEPARENTEVENT = raapi.createClass(prefix+"ChangeParentEvent");
			if (CHANGEPARENTEVENT == 0) {				
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the class ChangeParentEvent.");
			}
			ACTIVATEDGREVENT = raapi.findClass("ActivateDgrEvent");
			if ((ACTIVATEDGREVENT == 0) && (prefix != null))
				ACTIVATEDGREVENT = raapi.findClass(prefix+"ActivateDgrEvent");
			if ((ACTIVATEDGREVENT == 0) && insertMetamodel)
				ACTIVATEDGREVENT = raapi.createClass(prefix+"ActivateDgrEvent");
			if (ACTIVATEDGREVENT == 0) {				
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the class ActivateDgrEvent.");
			}
			CLOSEDGREVENT = raapi.findClass("CloseDgrEvent");
			if ((CLOSEDGREVENT == 0) && (prefix != null))
				CLOSEDGREVENT = raapi.findClass(prefix+"CloseDgrEvent");
			if ((CLOSEDGREVENT == 0) && insertMetamodel)
				CLOSEDGREVENT = raapi.createClass(prefix+"CloseDgrEvent");
			if (CLOSEDGREVENT == 0) {				
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the class CloseDgrEvent.");
			}
			OKSTYLEDIALOGEVENT = raapi.findClass("OKStyleDialogEvent");
			if ((OKSTYLEDIALOGEVENT == 0) && (prefix != null))
				OKSTYLEDIALOGEVENT = raapi.findClass(prefix+"OKStyleDialogEvent");
			if ((OKSTYLEDIALOGEVENT == 0) && insertMetamodel)
				OKSTYLEDIALOGEVENT = raapi.createClass(prefix+"OKStyleDialogEvent");
			if (OKSTYLEDIALOGEVENT == 0) {				
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the class OKStyleDialogEvent.");
			}
			KEYDOWNEVENT = raapi.findClass("KeyDownEvent");
			if ((KEYDOWNEVENT == 0) && (prefix != null))
				KEYDOWNEVENT = raapi.findClass(prefix+"KeyDownEvent");
			if ((KEYDOWNEVENT == 0) && insertMetamodel)
				KEYDOWNEVENT = raapi.createClass(prefix+"KeyDownEvent");
			if (KEYDOWNEVENT == 0) {				
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the class KeyDownEvent.");
			}
			NEWFREEBOXEVENT = raapi.findClass("NewFreeBoxEvent");
			if ((NEWFREEBOXEVENT == 0) && (prefix != null))
				NEWFREEBOXEVENT = raapi.findClass(prefix+"NewFreeBoxEvent");
			if ((NEWFREEBOXEVENT == 0) && insertMetamodel)
				NEWFREEBOXEVENT = raapi.createClass(prefix+"NewFreeBoxEvent");
			if (NEWFREEBOXEVENT == 0) {				
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the class NewFreeBoxEvent.");
			}
			NEWFREELINEEVENT = raapi.findClass("NewFreeLineEvent");
			if ((NEWFREELINEEVENT == 0) && (prefix != null))
				NEWFREELINEEVENT = raapi.findClass(prefix+"NewFreeLineEvent");
			if ((NEWFREELINEEVENT == 0) && insertMetamodel)
				NEWFREELINEEVENT = raapi.createClass(prefix+"NewFreeLineEvent");
			if (NEWFREELINEEVENT == 0) {				
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the class NewFreeLineEvent.");
			}
			FREEBOXEDITEDEVENT = raapi.findClass("FreeBoxEditedEvent");
			if ((FREEBOXEDITEDEVENT == 0) && (prefix != null))
				FREEBOXEDITEDEVENT = raapi.findClass(prefix+"FreeBoxEditedEvent");
			if ((FREEBOXEDITEDEVENT == 0) && insertMetamodel)
				FREEBOXEDITEDEVENT = raapi.createClass(prefix+"FreeBoxEditedEvent");
			if (FREEBOXEDITEDEVENT == 0) {				
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the class FreeBoxEditedEvent.");
			}
			FREELINEEDITEDEVENT = raapi.findClass("FreeLineEditedEvent");
			if ((FREELINEEDITEDEVENT == 0) && (prefix != null))
				FREELINEEDITEDEVENT = raapi.findClass(prefix+"FreeLineEditedEvent");
			if ((FREELINEEDITEDEVENT == 0) && insertMetamodel)
				FREELINEEDITEDEVENT = raapi.createClass(prefix+"FreeLineEditedEvent");
			if (FREELINEEDITEDEVENT == 0) {				
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the class FreeLineEditedEvent.");
			}
			TOOLBARELEMENTSELECTEVENT = raapi.findClass("ToolbarElementSelectEvent");
			if ((TOOLBARELEMENTSELECTEVENT == 0) && (prefix != null))
				TOOLBARELEMENTSELECTEVENT = raapi.findClass(prefix+"ToolbarElementSelectEvent");
			if ((TOOLBARELEMENTSELECTEVENT == 0) && insertMetamodel)
				TOOLBARELEMENTSELECTEVENT = raapi.createClass(prefix+"ToolbarElementSelectEvent");
			if (TOOLBARELEMENTSELECTEVENT == 0) {				
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the class ToolbarElementSelectEvent.");
			}
			STYLE = raapi.findClass("Style");
			if ((STYLE == 0) && (prefix != null))
				STYLE = raapi.findClass(prefix+"Style");
			if ((STYLE == 0) && insertMetamodel)
				STYLE = raapi.createClass(prefix+"Style");
			if (STYLE == 0) {				
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the class Style.");
			}
			GRAPHDIAGRAMSTYLE = raapi.findClass("GraphDiagramStyle");
			if ((GRAPHDIAGRAMSTYLE == 0) && (prefix != null))
				GRAPHDIAGRAMSTYLE = raapi.findClass(prefix+"GraphDiagramStyle");
			if ((GRAPHDIAGRAMSTYLE == 0) && insertMetamodel)
				GRAPHDIAGRAMSTYLE = raapi.createClass(prefix+"GraphDiagramStyle");
			if (GRAPHDIAGRAMSTYLE == 0) {				
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the class GraphDiagramStyle.");
			}
			ELEMSTYLE = raapi.findClass("ElemStyle");
			if ((ELEMSTYLE == 0) && (prefix != null))
				ELEMSTYLE = raapi.findClass(prefix+"ElemStyle");
			if ((ELEMSTYLE == 0) && insertMetamodel)
				ELEMSTYLE = raapi.createClass(prefix+"ElemStyle");
			if (ELEMSTYLE == 0) {				
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the class ElemStyle.");
			}
			NODESTYLE = raapi.findClass("NodeStyle");
			if ((NODESTYLE == 0) && (prefix != null))
				NODESTYLE = raapi.findClass(prefix+"NodeStyle");
			if ((NODESTYLE == 0) && insertMetamodel)
				NODESTYLE = raapi.createClass(prefix+"NodeStyle");
			if (NODESTYLE == 0) {				
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the class NodeStyle.");
			}
			EDGESTYLE = raapi.findClass("EdgeStyle");
			if ((EDGESTYLE == 0) && (prefix != null))
				EDGESTYLE = raapi.findClass(prefix+"EdgeStyle");
			if ((EDGESTYLE == 0) && insertMetamodel)
				EDGESTYLE = raapi.createClass(prefix+"EdgeStyle");
			if (EDGESTYLE == 0) {				
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the class EdgeStyle.");
			}
			PORTSTYLE = raapi.findClass("PortStyle");
			if ((PORTSTYLE == 0) && (prefix != null))
				PORTSTYLE = raapi.findClass(prefix+"PortStyle");
			if ((PORTSTYLE == 0) && insertMetamodel)
				PORTSTYLE = raapi.createClass(prefix+"PortStyle");
			if (PORTSTYLE == 0) {				
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the class PortStyle.");
			}
			FREEBOXSTYLE = raapi.findClass("FreeBoxStyle");
			if ((FREEBOXSTYLE == 0) && (prefix != null))
				FREEBOXSTYLE = raapi.findClass(prefix+"FreeBoxStyle");
			if ((FREEBOXSTYLE == 0) && insertMetamodel)
				FREEBOXSTYLE = raapi.createClass(prefix+"FreeBoxStyle");
			if (FREEBOXSTYLE == 0) {				
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the class FreeBoxStyle.");
			}
			FREELINESTYLE = raapi.findClass("FreeLineStyle");
			if ((FREELINESTYLE == 0) && (prefix != null))
				FREELINESTYLE = raapi.findClass(prefix+"FreeLineStyle");
			if ((FREELINESTYLE == 0) && insertMetamodel)
				FREELINESTYLE = raapi.createClass(prefix+"FreeLineStyle");
			if (FREELINESTYLE == 0) {				
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the class FreeLineStyle.");
			}
			COMPARTSTYLE = raapi.findClass("CompartStyle");
			if ((COMPARTSTYLE == 0) && (prefix != null))
				COMPARTSTYLE = raapi.findClass(prefix+"CompartStyle");
			if ((COMPARTSTYLE == 0) && insertMetamodel)
				COMPARTSTYLE = raapi.createClass(prefix+"CompartStyle");
			if (COMPARTSTYLE == 0) {				
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the class CompartStyle.");
			}
			POPUPDIAGRAM = raapi.findClass("PopUpDiagram");
			if ((POPUPDIAGRAM == 0) && (prefix != null))
				POPUPDIAGRAM = raapi.findClass(prefix+"PopUpDiagram");
			if ((POPUPDIAGRAM == 0) && insertMetamodel)
				POPUPDIAGRAM = raapi.createClass(prefix+"PopUpDiagram");
			if (POPUPDIAGRAM == 0) {				
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the class PopUpDiagram.");
			}
			POPUPELEMENT = raapi.findClass("PopUpElement");
			if ((POPUPELEMENT == 0) && (prefix != null))
				POPUPELEMENT = raapi.findClass(prefix+"PopUpElement");
			if ((POPUPELEMENT == 0) && insertMetamodel)
				POPUPELEMENT = raapi.createClass(prefix+"PopUpElement");
			if (POPUPELEMENT == 0) {				
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the class PopUpElement.");
			}
			PALETTE = raapi.findClass("Palette");
			if ((PALETTE == 0) && (prefix != null))
				PALETTE = raapi.findClass(prefix+"Palette");
			if ((PALETTE == 0) && insertMetamodel)
				PALETTE = raapi.createClass(prefix+"Palette");
			if (PALETTE == 0) {				
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the class Palette.");
			}
			PALETTEELEMENT = raapi.findClass("PaletteElement");
			if ((PALETTEELEMENT == 0) && (prefix != null))
				PALETTEELEMENT = raapi.findClass(prefix+"PaletteElement");
			if ((PALETTEELEMENT == 0) && insertMetamodel)
				PALETTEELEMENT = raapi.createClass(prefix+"PaletteElement");
			if (PALETTEELEMENT == 0) {				
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the class PaletteElement.");
			}
			PALETTEBOX = raapi.findClass("PaletteBox");
			if ((PALETTEBOX == 0) && (prefix != null))
				PALETTEBOX = raapi.findClass(prefix+"PaletteBox");
			if ((PALETTEBOX == 0) && insertMetamodel)
				PALETTEBOX = raapi.createClass(prefix+"PaletteBox");
			if (PALETTEBOX == 0) {				
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the class PaletteBox.");
			}
			PALETTELINE = raapi.findClass("PaletteLine");
			if ((PALETTELINE == 0) && (prefix != null))
				PALETTELINE = raapi.findClass(prefix+"PaletteLine");
			if ((PALETTELINE == 0) && insertMetamodel)
				PALETTELINE = raapi.createClass(prefix+"PaletteLine");
			if (PALETTELINE == 0) {				
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the class PaletteLine.");
			}
			PALETTEPIN = raapi.findClass("PalettePin");
			if ((PALETTEPIN == 0) && (prefix != null))
				PALETTEPIN = raapi.findClass(prefix+"PalettePin");
			if ((PALETTEPIN == 0) && insertMetamodel)
				PALETTEPIN = raapi.createClass(prefix+"PalettePin");
			if (PALETTEPIN == 0) {				
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the class PalettePin.");
			}
			PALETTEFREEBOX = raapi.findClass("PaletteFreeBox");
			if ((PALETTEFREEBOX == 0) && (prefix != null))
				PALETTEFREEBOX = raapi.findClass(prefix+"PaletteFreeBox");
			if ((PALETTEFREEBOX == 0) && insertMetamodel)
				PALETTEFREEBOX = raapi.createClass(prefix+"PaletteFreeBox");
			if (PALETTEFREEBOX == 0) {				
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the class PaletteFreeBox.");
			}
			PALETTEFREELINE = raapi.findClass("PaletteFreeLine");
			if ((PALETTEFREELINE == 0) && (prefix != null))
				PALETTEFREELINE = raapi.findClass(prefix+"PaletteFreeLine");
			if ((PALETTEFREELINE == 0) && insertMetamodel)
				PALETTEFREELINE = raapi.createClass(prefix+"PaletteFreeLine");
			if (PALETTEFREELINE == 0) {				
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the class PaletteFreeLine.");
			}
			TOOLBAR = raapi.findClass("Toolbar");
			if ((TOOLBAR == 0) && (prefix != null))
				TOOLBAR = raapi.findClass(prefix+"Toolbar");
			if ((TOOLBAR == 0) && insertMetamodel)
				TOOLBAR = raapi.createClass(prefix+"Toolbar");
			if (TOOLBAR == 0) {				
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the class Toolbar.");
			}
			TOOLBARELEMENT = raapi.findClass("ToolbarElement");
			if ((TOOLBARELEMENT == 0) && (prefix != null))
				TOOLBARELEMENT = raapi.findClass(prefix+"ToolbarElement");
			if ((TOOLBARELEMENT == 0) && insertMetamodel)
				TOOLBARELEMENT = raapi.createClass(prefix+"ToolbarElement");
			if (TOOLBARELEMENT == 0) {				
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the class ToolbarElement.");
			}
			CURRENTDGRPOINTER = raapi.findClass("CurrentDgrPointer");
			if ((CURRENTDGRPOINTER == 0) && (prefix != null))
				CURRENTDGRPOINTER = raapi.findClass(prefix+"CurrentDgrPointer");
			if ((CURRENTDGRPOINTER == 0) && insertMetamodel)
				CURRENTDGRPOINTER = raapi.createClass(prefix+"CurrentDgrPointer");
			if (CURRENTDGRPOINTER == 0) {				
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the class CurrentDgrPointer.");
			}
			GRAPHDIAGRAM = raapi.findClass("GraphDiagram");
			if ((GRAPHDIAGRAM == 0) && (prefix != null))
				GRAPHDIAGRAM = raapi.findClass(prefix+"GraphDiagram");
			if ((GRAPHDIAGRAM == 0) && insertMetamodel)
				GRAPHDIAGRAM = raapi.createClass(prefix+"GraphDiagram");
			if (GRAPHDIAGRAM == 0) {				
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the class GraphDiagram.");
			}
			COLLECTION = raapi.findClass("Collection");
			if ((COLLECTION == 0) && (prefix != null))
				COLLECTION = raapi.findClass(prefix+"Collection");
			if ((COLLECTION == 0) && insertMetamodel)
				COLLECTION = raapi.createClass(prefix+"Collection");
			if (COLLECTION == 0) {				
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the class Collection.");
			}
			ELEMENT = raapi.findClass("Element");
			if ((ELEMENT == 0) && (prefix != null))
				ELEMENT = raapi.findClass(prefix+"Element");
			if ((ELEMENT == 0) && insertMetamodel)
				ELEMENT = raapi.createClass(prefix+"Element");
			if (ELEMENT == 0) {				
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the class Element.");
			}
			NODE = raapi.findClass("Node");
			if ((NODE == 0) && (prefix != null))
				NODE = raapi.findClass(prefix+"Node");
			if ((NODE == 0) && insertMetamodel)
				NODE = raapi.createClass(prefix+"Node");
			if (NODE == 0) {				
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the class Node.");
			}
			EDGE = raapi.findClass("Edge");
			if ((EDGE == 0) && (prefix != null))
				EDGE = raapi.findClass(prefix+"Edge");
			if ((EDGE == 0) && insertMetamodel)
				EDGE = raapi.createClass(prefix+"Edge");
			if (EDGE == 0) {				
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the class Edge.");
			}
			PORT = raapi.findClass("Port");
			if ((PORT == 0) && (prefix != null))
				PORT = raapi.findClass(prefix+"Port");
			if ((PORT == 0) && insertMetamodel)
				PORT = raapi.createClass(prefix+"Port");
			if (PORT == 0) {				
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the class Port.");
			}
			FREEBOX = raapi.findClass("FreeBox");
			if ((FREEBOX == 0) && (prefix != null))
				FREEBOX = raapi.findClass(prefix+"FreeBox");
			if ((FREEBOX == 0) && insertMetamodel)
				FREEBOX = raapi.createClass(prefix+"FreeBox");
			if (FREEBOX == 0) {				
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the class FreeBox.");
			}
			FREELINE = raapi.findClass("FreeLine");
			if ((FREELINE == 0) && (prefix != null))
				FREELINE = raapi.findClass(prefix+"FreeLine");
			if ((FREELINE == 0) && insertMetamodel)
				FREELINE = raapi.createClass(prefix+"FreeLine");
			if (FREELINE == 0) {				
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the class FreeLine.");
			}
			COMPARTMENT = raapi.findClass("Compartment");
			if ((COMPARTMENT == 0) && (prefix != null))
				COMPARTMENT = raapi.findClass(prefix+"Compartment");
			if ((COMPARTMENT == 0) && insertMetamodel)
				COMPARTMENT = raapi.createClass(prefix+"Compartment");
			if (COMPARTMENT == 0) {				
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the class Compartment.");
			}
			EVENT = raapi.findClass("TDAKernel::Event");
			if ((EVENT == 0) && (prefix != null))
				EVENT = raapi.findClass(prefix+"TDAKernel::Event");
			if ((EVENT == 0) && insertMetamodel)
				EVENT = raapi.createClass(prefix+"TDAKernel::Event");
			if (EVENT == 0) {				
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the class TDAKernel::Event.");
			}
			FRAME = raapi.findClass("Frame");
			if ((FRAME == 0) && (prefix != null))
				FRAME = raapi.findClass(prefix+"Frame");
			if ((FRAME == 0) && insertMetamodel)
				FRAME = raapi.createClass(prefix+"Frame");
			if (FRAME == 0) {				
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the class Frame.");
			}

			// creating generalizations, if they do not exist...
			if (insertMetamodel) {
				if (!raapi.isDirectSubClass(ASYNCCOMMAND, COMMAND))
					if (!raapi.createGeneralization(ASYNCCOMMAND, COMMAND)) {
						setRAAPI(null, null, false); // freeing references initialized so far...
						throw new ElementReferenceException("Error creating a generalization between classes TDAKernel::AsyncCommand and TDAKernel::Command.");
					}
				if (!raapi.isDirectSubClass(GRAPHDIAGRAMENGINE, ENGINE))
					if (!raapi.createGeneralization(GRAPHDIAGRAMENGINE, ENGINE)) {
						setRAAPI(null, null, false); // freeing references initialized so far...
						throw new ElementReferenceException("Error creating a generalization between classes GraphDiagramEngine and TDAKernel::Engine.");
					}
				if (!raapi.isDirectSubClass(OKCMD, ASYNCCOMMAND))
					if (!raapi.createGeneralization(OKCMD, ASYNCCOMMAND)) {
						setRAAPI(null, null, false); // freeing references initialized so far...
						throw new ElementReferenceException("Error creating a generalization between classes OkCmd and TDAKernel::AsyncCommand.");
					}
				if (!raapi.isDirectSubClass(POPUPCMD, ASYNCCOMMAND))
					if (!raapi.createGeneralization(POPUPCMD, ASYNCCOMMAND)) {
						setRAAPI(null, null, false); // freeing references initialized so far...
						throw new ElementReferenceException("Error creating a generalization between classes PopUpCmd and TDAKernel::AsyncCommand.");
					}
				if (!raapi.isDirectSubClass(ACTIVEDGRCMD, ASYNCCOMMAND))
					if (!raapi.createGeneralization(ACTIVEDGRCMD, ASYNCCOMMAND)) {
						setRAAPI(null, null, false); // freeing references initialized so far...
						throw new ElementReferenceException("Error creating a generalization between classes ActiveDgrCmd and TDAKernel::AsyncCommand.");
					}
				if (!raapi.isDirectSubClass(ACTIVEDGRVIEWCMD, ASYNCCOMMAND))
					if (!raapi.createGeneralization(ACTIVEDGRVIEWCMD, ASYNCCOMMAND)) {
						setRAAPI(null, null, false); // freeing references initialized so far...
						throw new ElementReferenceException("Error creating a generalization between classes ActiveDgrViewCmd and TDAKernel::AsyncCommand.");
					}
				if (!raapi.isDirectSubClass(PASTECMD, ASYNCCOMMAND))
					if (!raapi.createGeneralization(PASTECMD, ASYNCCOMMAND)) {
						setRAAPI(null, null, false); // freeing references initialized so far...
						throw new ElementReferenceException("Error creating a generalization between classes PasteCmd and TDAKernel::AsyncCommand.");
					}
				if (!raapi.isDirectSubClass(UPDATEDGRCMD, ASYNCCOMMAND))
					if (!raapi.createGeneralization(UPDATEDGRCMD, ASYNCCOMMAND)) {
						setRAAPI(null, null, false); // freeing references initialized so far...
						throw new ElementReferenceException("Error creating a generalization between classes UpdateDgrCmd and TDAKernel::AsyncCommand.");
					}
				if (!raapi.isDirectSubClass(CLOSEDGRCMD, ASYNCCOMMAND))
					if (!raapi.createGeneralization(CLOSEDGRCMD, ASYNCCOMMAND)) {
						setRAAPI(null, null, false); // freeing references initialized so far...
						throw new ElementReferenceException("Error creating a generalization between classes CloseDgrCmd and TDAKernel::AsyncCommand.");
					}
				if (!raapi.isDirectSubClass(SAVEDGRCMD, ASYNCCOMMAND))
					if (!raapi.createGeneralization(SAVEDGRCMD, ASYNCCOMMAND)) {
						setRAAPI(null, null, false); // freeing references initialized so far...
						throw new ElementReferenceException("Error creating a generalization between classes SaveDgrCmd and TDAKernel::AsyncCommand.");
					}
				if (!raapi.isDirectSubClass(ACTIVEELEMENTCMD, ASYNCCOMMAND))
					if (!raapi.createGeneralization(ACTIVEELEMENTCMD, ASYNCCOMMAND)) {
						setRAAPI(null, null, false); // freeing references initialized so far...
						throw new ElementReferenceException("Error creating a generalization between classes ActiveElementCmd and TDAKernel::AsyncCommand.");
					}
				if (!raapi.isDirectSubClass(AFTERCONFIGCMD, ASYNCCOMMAND))
					if (!raapi.createGeneralization(AFTERCONFIGCMD, ASYNCCOMMAND)) {
						setRAAPI(null, null, false); // freeing references initialized so far...
						throw new ElementReferenceException("Error creating a generalization between classes AfterConfigCmd and TDAKernel::AsyncCommand.");
					}
				if (!raapi.isDirectSubClass(SAVESTYLESCMD, ASYNCCOMMAND))
					if (!raapi.createGeneralization(SAVESTYLESCMD, ASYNCCOMMAND)) {
						setRAAPI(null, null, false); // freeing references initialized so far...
						throw new ElementReferenceException("Error creating a generalization between classes SaveStylesCmd and TDAKernel::AsyncCommand.");
					}
				if (!raapi.isDirectSubClass(STYLEDIALOGCMD, ASYNCCOMMAND))
					if (!raapi.createGeneralization(STYLEDIALOGCMD, ASYNCCOMMAND)) {
						setRAAPI(null, null, false); // freeing references initialized so far...
						throw new ElementReferenceException("Error creating a generalization between classes StyleDialogCmd and TDAKernel::AsyncCommand.");
					}
				if (!raapi.isDirectSubClass(DEFAULTSTYLECMD, ASYNCCOMMAND))
					if (!raapi.createGeneralization(DEFAULTSTYLECMD, ASYNCCOMMAND)) {
						setRAAPI(null, null, false); // freeing references initialized so far...
						throw new ElementReferenceException("Error creating a generalization between classes DefaultStyleCmd and TDAKernel::AsyncCommand.");
					}
				if (!raapi.isDirectSubClass(REFRESHDGRCMD, ASYNCCOMMAND))
					if (!raapi.createGeneralization(REFRESHDGRCMD, ASYNCCOMMAND)) {
						setRAAPI(null, null, false); // freeing references initialized so far...
						throw new ElementReferenceException("Error creating a generalization between classes RefreshDgrCmd and TDAKernel::AsyncCommand.");
					}
				if (!raapi.isDirectSubClass(UPDATESTYLECMD, ASYNCCOMMAND))
					if (!raapi.createGeneralization(UPDATESTYLECMD, ASYNCCOMMAND)) {
						setRAAPI(null, null, false); // freeing references initialized so far...
						throw new ElementReferenceException("Error creating a generalization between classes UpdateStyleCmd and TDAKernel::AsyncCommand.");
					}
				if (!raapi.isDirectSubClass(REROUTECMD, ASYNCCOMMAND))
					if (!raapi.createGeneralization(REROUTECMD, ASYNCCOMMAND)) {
						setRAAPI(null, null, false); // freeing references initialized so far...
						throw new ElementReferenceException("Error creating a generalization between classes RerouteCmd and TDAKernel::AsyncCommand.");
					}
				if (!raapi.isDirectSubClass(ALIGNSIZECMD, ASYNCCOMMAND))
					if (!raapi.createGeneralization(ALIGNSIZECMD, ASYNCCOMMAND)) {
						setRAAPI(null, null, false); // freeing references initialized so far...
						throw new ElementReferenceException("Error creating a generalization between classes AlignSizeCmd and TDAKernel::AsyncCommand.");
					}
				if (!raapi.isDirectSubClass(POPUPELEMSELECTEVENT, EVENT))
					if (!raapi.createGeneralization(POPUPELEMSELECTEVENT, EVENT)) {
						setRAAPI(null, null, false); // freeing references initialized so far...
						throw new ElementReferenceException("Error creating a generalization between classes PopUpElemSelectEvent and TDAKernel::Event.");
					}
				if (!raapi.isDirectSubClass(PASTEGRAPHCLIPBOARDEVENT, EVENT))
					if (!raapi.createGeneralization(PASTEGRAPHCLIPBOARDEVENT, EVENT)) {
						setRAAPI(null, null, false); // freeing references initialized so far...
						throw new ElementReferenceException("Error creating a generalization between classes PasteGraphClipboardEvent and TDAKernel::Event.");
					}
				if (!raapi.isDirectSubClass(DELETECOLLECTIONEVENT, EVENT))
					if (!raapi.createGeneralization(DELETECOLLECTIONEVENT, EVENT)) {
						setRAAPI(null, null, false); // freeing references initialized so far...
						throw new ElementReferenceException("Error creating a generalization between classes DeleteCollectionEvent and TDAKernel::Event.");
					}
				if (!raapi.isDirectSubClass(COPYCUTCOLLECTIONEVENT, EVENT))
					if (!raapi.createGeneralization(COPYCUTCOLLECTIONEVENT, EVENT)) {
						setRAAPI(null, null, false); // freeing references initialized so far...
						throw new ElementReferenceException("Error creating a generalization between classes CopyCutCollectionEvent and TDAKernel::Event.");
					}
				if (!raapi.isDirectSubClass(COPYCOLLECTIONEVENT, EVENT))
					if (!raapi.createGeneralization(COPYCOLLECTIONEVENT, EVENT)) {
						setRAAPI(null, null, false); // freeing references initialized so far...
						throw new ElementReferenceException("Error creating a generalization between classes CopyCollectionEvent and TDAKernel::Event.");
					}
				if (!raapi.isDirectSubClass(MOVELINESTARTPOINTEVENT, EVENT))
					if (!raapi.createGeneralization(MOVELINESTARTPOINTEVENT, EVENT)) {
						setRAAPI(null, null, false); // freeing references initialized so far...
						throw new ElementReferenceException("Error creating a generalization between classes MoveLineStartPointEvent and TDAKernel::Event.");
					}
				if (!raapi.isDirectSubClass(MOVELINEENDPOINTEVENT, EVENT))
					if (!raapi.createGeneralization(MOVELINEENDPOINTEVENT, EVENT)) {
						setRAAPI(null, null, false); // freeing references initialized so far...
						throw new ElementReferenceException("Error creating a generalization between classes MoveLineEndPointEvent and TDAKernel::Event.");
					}
				if (!raapi.isDirectSubClass(L2CLICKEVENT, EVENT))
					if (!raapi.createGeneralization(L2CLICKEVENT, EVENT)) {
						setRAAPI(null, null, false); // freeing references initialized so far...
						throw new ElementReferenceException("Error creating a generalization between classes L2ClickEvent and TDAKernel::Event.");
					}
				if (!raapi.isDirectSubClass(LCLICKEVENT, EVENT))
					if (!raapi.createGeneralization(LCLICKEVENT, EVENT)) {
						setRAAPI(null, null, false); // freeing references initialized so far...
						throw new ElementReferenceException("Error creating a generalization between classes LClickEvent and TDAKernel::Event.");
					}
				if (!raapi.isDirectSubClass(RCLICKEVENT, EVENT))
					if (!raapi.createGeneralization(RCLICKEVENT, EVENT)) {
						setRAAPI(null, null, false); // freeing references initialized so far...
						throw new ElementReferenceException("Error creating a generalization between classes RClickEvent and TDAKernel::Event.");
					}
				if (!raapi.isDirectSubClass(NEWLINEEVENT, EVENT))
					if (!raapi.createGeneralization(NEWLINEEVENT, EVENT)) {
						setRAAPI(null, null, false); // freeing references initialized so far...
						throw new ElementReferenceException("Error creating a generalization between classes NewLineEvent and TDAKernel::Event.");
					}
				if (!raapi.isDirectSubClass(NEWBOXEVENT, EVENT))
					if (!raapi.createGeneralization(NEWBOXEVENT, EVENT)) {
						setRAAPI(null, null, false); // freeing references initialized so far...
						throw new ElementReferenceException("Error creating a generalization between classes NewBoxEvent and TDAKernel::Event.");
					}
				if (!raapi.isDirectSubClass(EXECTRANSFEVENT, EVENT))
					if (!raapi.createGeneralization(EXECTRANSFEVENT, EVENT)) {
						setRAAPI(null, null, false); // freeing references initialized so far...
						throw new ElementReferenceException("Error creating a generalization between classes ExecTransfEvent and TDAKernel::Event.");
					}
				if (!raapi.isDirectSubClass(NEWPINEVENT, EVENT))
					if (!raapi.createGeneralization(NEWPINEVENT, EVENT)) {
						setRAAPI(null, null, false); // freeing references initialized so far...
						throw new ElementReferenceException("Error creating a generalization between classes NewPinEvent and TDAKernel::Event.");
					}
				if (!raapi.isDirectSubClass(CHANGEPARENTEVENT, EVENT))
					if (!raapi.createGeneralization(CHANGEPARENTEVENT, EVENT)) {
						setRAAPI(null, null, false); // freeing references initialized so far...
						throw new ElementReferenceException("Error creating a generalization between classes ChangeParentEvent and TDAKernel::Event.");
					}
				if (!raapi.isDirectSubClass(ACTIVATEDGREVENT, EVENT))
					if (!raapi.createGeneralization(ACTIVATEDGREVENT, EVENT)) {
						setRAAPI(null, null, false); // freeing references initialized so far...
						throw new ElementReferenceException("Error creating a generalization between classes ActivateDgrEvent and TDAKernel::Event.");
					}
				if (!raapi.isDirectSubClass(CLOSEDGREVENT, EVENT))
					if (!raapi.createGeneralization(CLOSEDGREVENT, EVENT)) {
						setRAAPI(null, null, false); // freeing references initialized so far...
						throw new ElementReferenceException("Error creating a generalization between classes CloseDgrEvent and TDAKernel::Event.");
					}
				if (!raapi.isDirectSubClass(OKSTYLEDIALOGEVENT, EVENT))
					if (!raapi.createGeneralization(OKSTYLEDIALOGEVENT, EVENT)) {
						setRAAPI(null, null, false); // freeing references initialized so far...
						throw new ElementReferenceException("Error creating a generalization between classes OKStyleDialogEvent and TDAKernel::Event.");
					}
				if (!raapi.isDirectSubClass(KEYDOWNEVENT, EVENT))
					if (!raapi.createGeneralization(KEYDOWNEVENT, EVENT)) {
						setRAAPI(null, null, false); // freeing references initialized so far...
						throw new ElementReferenceException("Error creating a generalization between classes KeyDownEvent and TDAKernel::Event.");
					}
				if (!raapi.isDirectSubClass(NEWFREEBOXEVENT, EVENT))
					if (!raapi.createGeneralization(NEWFREEBOXEVENT, EVENT)) {
						setRAAPI(null, null, false); // freeing references initialized so far...
						throw new ElementReferenceException("Error creating a generalization between classes NewFreeBoxEvent and TDAKernel::Event.");
					}
				if (!raapi.isDirectSubClass(NEWFREELINEEVENT, EVENT))
					if (!raapi.createGeneralization(NEWFREELINEEVENT, EVENT)) {
						setRAAPI(null, null, false); // freeing references initialized so far...
						throw new ElementReferenceException("Error creating a generalization between classes NewFreeLineEvent and TDAKernel::Event.");
					}
				if (!raapi.isDirectSubClass(FREEBOXEDITEDEVENT, EVENT))
					if (!raapi.createGeneralization(FREEBOXEDITEDEVENT, EVENT)) {
						setRAAPI(null, null, false); // freeing references initialized so far...
						throw new ElementReferenceException("Error creating a generalization between classes FreeBoxEditedEvent and TDAKernel::Event.");
					}
				if (!raapi.isDirectSubClass(FREELINEEDITEDEVENT, EVENT))
					if (!raapi.createGeneralization(FREELINEEDITEDEVENT, EVENT)) {
						setRAAPI(null, null, false); // freeing references initialized so far...
						throw new ElementReferenceException("Error creating a generalization between classes FreeLineEditedEvent and TDAKernel::Event.");
					}
				if (!raapi.isDirectSubClass(TOOLBARELEMENTSELECTEVENT, EVENT))
					if (!raapi.createGeneralization(TOOLBARELEMENTSELECTEVENT, EVENT)) {
						setRAAPI(null, null, false); // freeing references initialized so far...
						throw new ElementReferenceException("Error creating a generalization between classes ToolbarElementSelectEvent and TDAKernel::Event.");
					}
				if (!raapi.isDirectSubClass(GRAPHDIAGRAMSTYLE, STYLE))
					if (!raapi.createGeneralization(GRAPHDIAGRAMSTYLE, STYLE)) {
						setRAAPI(null, null, false); // freeing references initialized so far...
						throw new ElementReferenceException("Error creating a generalization between classes GraphDiagramStyle and Style.");
					}
				if (!raapi.isDirectSubClass(ELEMSTYLE, STYLE))
					if (!raapi.createGeneralization(ELEMSTYLE, STYLE)) {
						setRAAPI(null, null, false); // freeing references initialized so far...
						throw new ElementReferenceException("Error creating a generalization between classes ElemStyle and Style.");
					}
				if (!raapi.isDirectSubClass(NODESTYLE, ELEMSTYLE))
					if (!raapi.createGeneralization(NODESTYLE, ELEMSTYLE)) {
						setRAAPI(null, null, false); // freeing references initialized so far...
						throw new ElementReferenceException("Error creating a generalization between classes NodeStyle and ElemStyle.");
					}
				if (!raapi.isDirectSubClass(EDGESTYLE, ELEMSTYLE))
					if (!raapi.createGeneralization(EDGESTYLE, ELEMSTYLE)) {
						setRAAPI(null, null, false); // freeing references initialized so far...
						throw new ElementReferenceException("Error creating a generalization between classes EdgeStyle and ElemStyle.");
					}
				if (!raapi.isDirectSubClass(PORTSTYLE, ELEMSTYLE))
					if (!raapi.createGeneralization(PORTSTYLE, ELEMSTYLE)) {
						setRAAPI(null, null, false); // freeing references initialized so far...
						throw new ElementReferenceException("Error creating a generalization between classes PortStyle and ElemStyle.");
					}
				if (!raapi.isDirectSubClass(FREEBOXSTYLE, NODESTYLE))
					if (!raapi.createGeneralization(FREEBOXSTYLE, NODESTYLE)) {
						setRAAPI(null, null, false); // freeing references initialized so far...
						throw new ElementReferenceException("Error creating a generalization between classes FreeBoxStyle and NodeStyle.");
					}
				if (!raapi.isDirectSubClass(FREELINESTYLE, EDGESTYLE))
					if (!raapi.createGeneralization(FREELINESTYLE, EDGESTYLE)) {
						setRAAPI(null, null, false); // freeing references initialized so far...
						throw new ElementReferenceException("Error creating a generalization between classes FreeLineStyle and EdgeStyle.");
					}
				if (!raapi.isDirectSubClass(COMPARTSTYLE, STYLE))
					if (!raapi.createGeneralization(COMPARTSTYLE, STYLE)) {
						setRAAPI(null, null, false); // freeing references initialized so far...
						throw new ElementReferenceException("Error creating a generalization between classes CompartStyle and Style.");
					}
				if (!raapi.isDirectSubClass(POPUPDIAGRAM, PRESENTATIONELEMENT))
					if (!raapi.createGeneralization(POPUPDIAGRAM, PRESENTATIONELEMENT)) {
						setRAAPI(null, null, false); // freeing references initialized so far...
						throw new ElementReferenceException("Error creating a generalization between classes PopUpDiagram and PresentationElement.");
					}
				if (!raapi.isDirectSubClass(POPUPELEMENT, PRESENTATIONELEMENT))
					if (!raapi.createGeneralization(POPUPELEMENT, PRESENTATIONELEMENT)) {
						setRAAPI(null, null, false); // freeing references initialized so far...
						throw new ElementReferenceException("Error creating a generalization between classes PopUpElement and PresentationElement.");
					}
				if (!raapi.isDirectSubClass(PALETTE, PRESENTATIONELEMENT))
					if (!raapi.createGeneralization(PALETTE, PRESENTATIONELEMENT)) {
						setRAAPI(null, null, false); // freeing references initialized so far...
						throw new ElementReferenceException("Error creating a generalization between classes Palette and PresentationElement.");
					}
				if (!raapi.isDirectSubClass(PALETTEELEMENT, PRESENTATIONELEMENT))
					if (!raapi.createGeneralization(PALETTEELEMENT, PRESENTATIONELEMENT)) {
						setRAAPI(null, null, false); // freeing references initialized so far...
						throw new ElementReferenceException("Error creating a generalization between classes PaletteElement and PresentationElement.");
					}
				if (!raapi.isDirectSubClass(PALETTEBOX, PALETTEELEMENT))
					if (!raapi.createGeneralization(PALETTEBOX, PALETTEELEMENT)) {
						setRAAPI(null, null, false); // freeing references initialized so far...
						throw new ElementReferenceException("Error creating a generalization between classes PaletteBox and PaletteElement.");
					}
				if (!raapi.isDirectSubClass(PALETTELINE, PALETTEELEMENT))
					if (!raapi.createGeneralization(PALETTELINE, PALETTEELEMENT)) {
						setRAAPI(null, null, false); // freeing references initialized so far...
						throw new ElementReferenceException("Error creating a generalization between classes PaletteLine and PaletteElement.");
					}
				if (!raapi.isDirectSubClass(PALETTEPIN, PALETTEELEMENT))
					if (!raapi.createGeneralization(PALETTEPIN, PALETTEELEMENT)) {
						setRAAPI(null, null, false); // freeing references initialized so far...
						throw new ElementReferenceException("Error creating a generalization between classes PalettePin and PaletteElement.");
					}
				if (!raapi.isDirectSubClass(PALETTEFREEBOX, PALETTEELEMENT))
					if (!raapi.createGeneralization(PALETTEFREEBOX, PALETTEELEMENT)) {
						setRAAPI(null, null, false); // freeing references initialized so far...
						throw new ElementReferenceException("Error creating a generalization between classes PaletteFreeBox and PaletteElement.");
					}
				if (!raapi.isDirectSubClass(PALETTEFREELINE, PALETTEELEMENT))
					if (!raapi.createGeneralization(PALETTEFREELINE, PALETTEELEMENT)) {
						setRAAPI(null, null, false); // freeing references initialized so far...
						throw new ElementReferenceException("Error creating a generalization between classes PaletteFreeLine and PaletteElement.");
					}
				if (!raapi.isDirectSubClass(TOOLBAR, PRESENTATIONELEMENT))
					if (!raapi.createGeneralization(TOOLBAR, PRESENTATIONELEMENT)) {
						setRAAPI(null, null, false); // freeing references initialized so far...
						throw new ElementReferenceException("Error creating a generalization between classes Toolbar and PresentationElement.");
					}
				if (!raapi.isDirectSubClass(TOOLBARELEMENT, PRESENTATIONELEMENT))
					if (!raapi.createGeneralization(TOOLBARELEMENT, PRESENTATIONELEMENT)) {
						setRAAPI(null, null, false); // freeing references initialized so far...
						throw new ElementReferenceException("Error creating a generalization between classes ToolbarElement and PresentationElement.");
					}
				if (!raapi.isDirectSubClass(GRAPHDIAGRAM, PRESENTATIONELEMENT))
					if (!raapi.createGeneralization(GRAPHDIAGRAM, PRESENTATIONELEMENT)) {
						setRAAPI(null, null, false); // freeing references initialized so far...
						throw new ElementReferenceException("Error creating a generalization between classes GraphDiagram and PresentationElement.");
					}
				if (!raapi.isDirectSubClass(ELEMENT, PRESENTATIONELEMENT))
					if (!raapi.createGeneralization(ELEMENT, PRESENTATIONELEMENT)) {
						setRAAPI(null, null, false); // freeing references initialized so far...
						throw new ElementReferenceException("Error creating a generalization between classes Element and PresentationElement.");
					}
				if (!raapi.isDirectSubClass(NODE, ELEMENT))
					if (!raapi.createGeneralization(NODE, ELEMENT)) {
						setRAAPI(null, null, false); // freeing references initialized so far...
						throw new ElementReferenceException("Error creating a generalization between classes Node and Element.");
					}
				if (!raapi.isDirectSubClass(EDGE, ELEMENT))
					if (!raapi.createGeneralization(EDGE, ELEMENT)) {
						setRAAPI(null, null, false); // freeing references initialized so far...
						throw new ElementReferenceException("Error creating a generalization between classes Edge and Element.");
					}
				if (!raapi.isDirectSubClass(PORT, ELEMENT))
					if (!raapi.createGeneralization(PORT, ELEMENT)) {
						setRAAPI(null, null, false); // freeing references initialized so far...
						throw new ElementReferenceException("Error creating a generalization between classes Port and Element.");
					}
				if (!raapi.isDirectSubClass(FREEBOX, ELEMENT))
					if (!raapi.createGeneralization(FREEBOX, ELEMENT)) {
						setRAAPI(null, null, false); // freeing references initialized so far...
						throw new ElementReferenceException("Error creating a generalization between classes FreeBox and Element.");
					}
				if (!raapi.isDirectSubClass(FREELINE, ELEMENT))
					if (!raapi.createGeneralization(FREELINE, ELEMENT)) {
						setRAAPI(null, null, false); // freeing references initialized so far...
						throw new ElementReferenceException("Error creating a generalization between classes FreeLine and Element.");
					}
				if (!raapi.isDirectSubClass(COMPARTMENT, PRESENTATIONELEMENT))
					if (!raapi.createGeneralization(COMPARTMENT, PRESENTATIONELEMENT)) {
						setRAAPI(null, null, false); // freeing references initialized so far...
						throw new ElementReferenceException("Error creating a generalization between classes Compartment and PresentationElement.");
					}
			}

			// initializing references for attributes and associations...
			COMMAND_GRAPHDIAGRAM = raapi.findAssociationEnd(COMMAND, "graphDiagram");
			if ((COMMAND_GRAPHDIAGRAM == 0) && insertMetamodel) {
				COMMAND_GRAPHDIAGRAM = raapi.createAssociation(COMMAND, GRAPHDIAGRAM, "command", "graphDiagram", false);
			}
			if (COMMAND_GRAPHDIAGRAM == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end graphDiagram of the class TDAKernel::Command.");
			}
			GRAPHDIAGRAMTYPE_GRAPHDIAGRAM = raapi.findAssociationEnd(GRAPHDIAGRAMTYPE, "graphDiagram");
			if ((GRAPHDIAGRAMTYPE_GRAPHDIAGRAM == 0) && insertMetamodel) {
				GRAPHDIAGRAMTYPE_GRAPHDIAGRAM = raapi.createAssociation(GRAPHDIAGRAMTYPE, GRAPHDIAGRAM, "graphDiagramType", "graphDiagram", false);
			}
			if (GRAPHDIAGRAMTYPE_GRAPHDIAGRAM == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end graphDiagram of the class GraphDiagramType.");
			}
			GRAPHDIAGRAMENGINE_ONPOPUPELEMSELECTEVENT = raapi.findAttribute(GRAPHDIAGRAMENGINE, "onPopUpElemSelectEvent");
			if ((GRAPHDIAGRAMENGINE_ONPOPUPELEMSELECTEVENT == 0) && insertMetamodel)
				GRAPHDIAGRAMENGINE_ONPOPUPELEMSELECTEVENT = raapi.createAttribute(GRAPHDIAGRAMENGINE, "onPopUpElemSelectEvent", raapi.findPrimitiveDataType("String"));
			if (GRAPHDIAGRAMENGINE_ONPOPUPELEMSELECTEVENT == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute onPopUpElemSelectEvent of the class GraphDiagramEngine.");
			}
			GRAPHDIAGRAMENGINE_ONPASTEGRAPHCLIPBOARDEVENT = raapi.findAttribute(GRAPHDIAGRAMENGINE, "onPasteGraphClipboardEvent");
			if ((GRAPHDIAGRAMENGINE_ONPASTEGRAPHCLIPBOARDEVENT == 0) && insertMetamodel)
				GRAPHDIAGRAMENGINE_ONPASTEGRAPHCLIPBOARDEVENT = raapi.createAttribute(GRAPHDIAGRAMENGINE, "onPasteGraphClipboardEvent", raapi.findPrimitiveDataType("String"));
			if (GRAPHDIAGRAMENGINE_ONPASTEGRAPHCLIPBOARDEVENT == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute onPasteGraphClipboardEvent of the class GraphDiagramEngine.");
			}
			GRAPHDIAGRAMENGINE_ONDELETECOLLECTIONEVENT = raapi.findAttribute(GRAPHDIAGRAMENGINE, "onDeleteCollectionEvent");
			if ((GRAPHDIAGRAMENGINE_ONDELETECOLLECTIONEVENT == 0) && insertMetamodel)
				GRAPHDIAGRAMENGINE_ONDELETECOLLECTIONEVENT = raapi.createAttribute(GRAPHDIAGRAMENGINE, "onDeleteCollectionEvent", raapi.findPrimitiveDataType("String"));
			if (GRAPHDIAGRAMENGINE_ONDELETECOLLECTIONEVENT == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute onDeleteCollectionEvent of the class GraphDiagramEngine.");
			}
			GRAPHDIAGRAMENGINE_ONCOPYCUTCOLLECTIONEVENT = raapi.findAttribute(GRAPHDIAGRAMENGINE, "onCopyCutCollectionEvent");
			if ((GRAPHDIAGRAMENGINE_ONCOPYCUTCOLLECTIONEVENT == 0) && insertMetamodel)
				GRAPHDIAGRAMENGINE_ONCOPYCUTCOLLECTIONEVENT = raapi.createAttribute(GRAPHDIAGRAMENGINE, "onCopyCutCollectionEvent", raapi.findPrimitiveDataType("String"));
			if (GRAPHDIAGRAMENGINE_ONCOPYCUTCOLLECTIONEVENT == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute onCopyCutCollectionEvent of the class GraphDiagramEngine.");
			}
			GRAPHDIAGRAMENGINE_ONCOPYCOLLECTIONEVENT = raapi.findAttribute(GRAPHDIAGRAMENGINE, "onCopyCollectionEvent");
			if ((GRAPHDIAGRAMENGINE_ONCOPYCOLLECTIONEVENT == 0) && insertMetamodel)
				GRAPHDIAGRAMENGINE_ONCOPYCOLLECTIONEVENT = raapi.createAttribute(GRAPHDIAGRAMENGINE, "onCopyCollectionEvent", raapi.findPrimitiveDataType("String"));
			if (GRAPHDIAGRAMENGINE_ONCOPYCOLLECTIONEVENT == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute onCopyCollectionEvent of the class GraphDiagramEngine.");
			}
			GRAPHDIAGRAMENGINE_ONMOVELINESTARTPOINTEVENT = raapi.findAttribute(GRAPHDIAGRAMENGINE, "onMoveLineStartPointEvent");
			if ((GRAPHDIAGRAMENGINE_ONMOVELINESTARTPOINTEVENT == 0) && insertMetamodel)
				GRAPHDIAGRAMENGINE_ONMOVELINESTARTPOINTEVENT = raapi.createAttribute(GRAPHDIAGRAMENGINE, "onMoveLineStartPointEvent", raapi.findPrimitiveDataType("String"));
			if (GRAPHDIAGRAMENGINE_ONMOVELINESTARTPOINTEVENT == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute onMoveLineStartPointEvent of the class GraphDiagramEngine.");
			}
			GRAPHDIAGRAMENGINE_ONMOVELINEENDPOINTEVENT = raapi.findAttribute(GRAPHDIAGRAMENGINE, "onMoveLineEndPointEvent");
			if ((GRAPHDIAGRAMENGINE_ONMOVELINEENDPOINTEVENT == 0) && insertMetamodel)
				GRAPHDIAGRAMENGINE_ONMOVELINEENDPOINTEVENT = raapi.createAttribute(GRAPHDIAGRAMENGINE, "onMoveLineEndPointEvent", raapi.findPrimitiveDataType("String"));
			if (GRAPHDIAGRAMENGINE_ONMOVELINEENDPOINTEVENT == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute onMoveLineEndPointEvent of the class GraphDiagramEngine.");
			}
			GRAPHDIAGRAMENGINE_ONL2CLICKEVENT = raapi.findAttribute(GRAPHDIAGRAMENGINE, "onL2ClickEvent");
			if ((GRAPHDIAGRAMENGINE_ONL2CLICKEVENT == 0) && insertMetamodel)
				GRAPHDIAGRAMENGINE_ONL2CLICKEVENT = raapi.createAttribute(GRAPHDIAGRAMENGINE, "onL2ClickEvent", raapi.findPrimitiveDataType("String"));
			if (GRAPHDIAGRAMENGINE_ONL2CLICKEVENT == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute onL2ClickEvent of the class GraphDiagramEngine.");
			}
			GRAPHDIAGRAMENGINE_ONLCLICKEVENT = raapi.findAttribute(GRAPHDIAGRAMENGINE, "onLClickEvent");
			if ((GRAPHDIAGRAMENGINE_ONLCLICKEVENT == 0) && insertMetamodel)
				GRAPHDIAGRAMENGINE_ONLCLICKEVENT = raapi.createAttribute(GRAPHDIAGRAMENGINE, "onLClickEvent", raapi.findPrimitiveDataType("String"));
			if (GRAPHDIAGRAMENGINE_ONLCLICKEVENT == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute onLClickEvent of the class GraphDiagramEngine.");
			}
			GRAPHDIAGRAMENGINE_ONRCLICKEVENT = raapi.findAttribute(GRAPHDIAGRAMENGINE, "onRClickEvent");
			if ((GRAPHDIAGRAMENGINE_ONRCLICKEVENT == 0) && insertMetamodel)
				GRAPHDIAGRAMENGINE_ONRCLICKEVENT = raapi.createAttribute(GRAPHDIAGRAMENGINE, "onRClickEvent", raapi.findPrimitiveDataType("String"));
			if (GRAPHDIAGRAMENGINE_ONRCLICKEVENT == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute onRClickEvent of the class GraphDiagramEngine.");
			}
			GRAPHDIAGRAMENGINE_ONNEWLINEEVENT = raapi.findAttribute(GRAPHDIAGRAMENGINE, "onNewLineEvent");
			if ((GRAPHDIAGRAMENGINE_ONNEWLINEEVENT == 0) && insertMetamodel)
				GRAPHDIAGRAMENGINE_ONNEWLINEEVENT = raapi.createAttribute(GRAPHDIAGRAMENGINE, "onNewLineEvent", raapi.findPrimitiveDataType("String"));
			if (GRAPHDIAGRAMENGINE_ONNEWLINEEVENT == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute onNewLineEvent of the class GraphDiagramEngine.");
			}
			GRAPHDIAGRAMENGINE_ONNEWBOXEVENT = raapi.findAttribute(GRAPHDIAGRAMENGINE, "onNewBoxEvent");
			if ((GRAPHDIAGRAMENGINE_ONNEWBOXEVENT == 0) && insertMetamodel)
				GRAPHDIAGRAMENGINE_ONNEWBOXEVENT = raapi.createAttribute(GRAPHDIAGRAMENGINE, "onNewBoxEvent", raapi.findPrimitiveDataType("String"));
			if (GRAPHDIAGRAMENGINE_ONNEWBOXEVENT == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute onNewBoxEvent of the class GraphDiagramEngine.");
			}
			GRAPHDIAGRAMENGINE_ONEXECTRANSFEVENT = raapi.findAttribute(GRAPHDIAGRAMENGINE, "onExecTransfEvent");
			if ((GRAPHDIAGRAMENGINE_ONEXECTRANSFEVENT == 0) && insertMetamodel)
				GRAPHDIAGRAMENGINE_ONEXECTRANSFEVENT = raapi.createAttribute(GRAPHDIAGRAMENGINE, "onExecTransfEvent", raapi.findPrimitiveDataType("String"));
			if (GRAPHDIAGRAMENGINE_ONEXECTRANSFEVENT == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute onExecTransfEvent of the class GraphDiagramEngine.");
			}
			GRAPHDIAGRAMENGINE_ONNEWPINEVENT = raapi.findAttribute(GRAPHDIAGRAMENGINE, "onNewPinEvent");
			if ((GRAPHDIAGRAMENGINE_ONNEWPINEVENT == 0) && insertMetamodel)
				GRAPHDIAGRAMENGINE_ONNEWPINEVENT = raapi.createAttribute(GRAPHDIAGRAMENGINE, "onNewPinEvent", raapi.findPrimitiveDataType("String"));
			if (GRAPHDIAGRAMENGINE_ONNEWPINEVENT == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute onNewPinEvent of the class GraphDiagramEngine.");
			}
			GRAPHDIAGRAMENGINE_ONCHANGEPARENTEVENT = raapi.findAttribute(GRAPHDIAGRAMENGINE, "onChangeParentEvent");
			if ((GRAPHDIAGRAMENGINE_ONCHANGEPARENTEVENT == 0) && insertMetamodel)
				GRAPHDIAGRAMENGINE_ONCHANGEPARENTEVENT = raapi.createAttribute(GRAPHDIAGRAMENGINE, "onChangeParentEvent", raapi.findPrimitiveDataType("String"));
			if (GRAPHDIAGRAMENGINE_ONCHANGEPARENTEVENT == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute onChangeParentEvent of the class GraphDiagramEngine.");
			}
			GRAPHDIAGRAMENGINE_ONACTIVATEDGREVENT = raapi.findAttribute(GRAPHDIAGRAMENGINE, "onActivateDgrEvent");
			if ((GRAPHDIAGRAMENGINE_ONACTIVATEDGREVENT == 0) && insertMetamodel)
				GRAPHDIAGRAMENGINE_ONACTIVATEDGREVENT = raapi.createAttribute(GRAPHDIAGRAMENGINE, "onActivateDgrEvent", raapi.findPrimitiveDataType("String"));
			if (GRAPHDIAGRAMENGINE_ONACTIVATEDGREVENT == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute onActivateDgrEvent of the class GraphDiagramEngine.");
			}
			GRAPHDIAGRAMENGINE_ONCLOSEDGREVENT = raapi.findAttribute(GRAPHDIAGRAMENGINE, "onCloseDgrEvent");
			if ((GRAPHDIAGRAMENGINE_ONCLOSEDGREVENT == 0) && insertMetamodel)
				GRAPHDIAGRAMENGINE_ONCLOSEDGREVENT = raapi.createAttribute(GRAPHDIAGRAMENGINE, "onCloseDgrEvent", raapi.findPrimitiveDataType("String"));
			if (GRAPHDIAGRAMENGINE_ONCLOSEDGREVENT == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute onCloseDgrEvent of the class GraphDiagramEngine.");
			}
			GRAPHDIAGRAMENGINE_ONOKSTYLEDIALOGEVENT = raapi.findAttribute(GRAPHDIAGRAMENGINE, "onOKStyleDialogEvent");
			if ((GRAPHDIAGRAMENGINE_ONOKSTYLEDIALOGEVENT == 0) && insertMetamodel)
				GRAPHDIAGRAMENGINE_ONOKSTYLEDIALOGEVENT = raapi.createAttribute(GRAPHDIAGRAMENGINE, "onOKStyleDialogEvent", raapi.findPrimitiveDataType("String"));
			if (GRAPHDIAGRAMENGINE_ONOKSTYLEDIALOGEVENT == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute onOKStyleDialogEvent of the class GraphDiagramEngine.");
			}
			GRAPHDIAGRAMENGINE_ONKEYDOWNEVENT = raapi.findAttribute(GRAPHDIAGRAMENGINE, "onKeyDownEvent");
			if ((GRAPHDIAGRAMENGINE_ONKEYDOWNEVENT == 0) && insertMetamodel)
				GRAPHDIAGRAMENGINE_ONKEYDOWNEVENT = raapi.createAttribute(GRAPHDIAGRAMENGINE, "onKeyDownEvent", raapi.findPrimitiveDataType("String"));
			if (GRAPHDIAGRAMENGINE_ONKEYDOWNEVENT == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute onKeyDownEvent of the class GraphDiagramEngine.");
			}
			GRAPHDIAGRAMENGINE_ONNEWFREEBOXEVENT = raapi.findAttribute(GRAPHDIAGRAMENGINE, "onNewFreeBoxEvent");
			if ((GRAPHDIAGRAMENGINE_ONNEWFREEBOXEVENT == 0) && insertMetamodel)
				GRAPHDIAGRAMENGINE_ONNEWFREEBOXEVENT = raapi.createAttribute(GRAPHDIAGRAMENGINE, "onNewFreeBoxEvent", raapi.findPrimitiveDataType("String"));
			if (GRAPHDIAGRAMENGINE_ONNEWFREEBOXEVENT == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute onNewFreeBoxEvent of the class GraphDiagramEngine.");
			}
			GRAPHDIAGRAMENGINE_ONNEWFREELINEEVENT = raapi.findAttribute(GRAPHDIAGRAMENGINE, "onNewFreeLineEvent");
			if ((GRAPHDIAGRAMENGINE_ONNEWFREELINEEVENT == 0) && insertMetamodel)
				GRAPHDIAGRAMENGINE_ONNEWFREELINEEVENT = raapi.createAttribute(GRAPHDIAGRAMENGINE, "onNewFreeLineEvent", raapi.findPrimitiveDataType("String"));
			if (GRAPHDIAGRAMENGINE_ONNEWFREELINEEVENT == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute onNewFreeLineEvent of the class GraphDiagramEngine.");
			}
			GRAPHDIAGRAMENGINE_ONFREEBOXEDITEDEVENT = raapi.findAttribute(GRAPHDIAGRAMENGINE, "onFreeBoxEditedEvent");
			if ((GRAPHDIAGRAMENGINE_ONFREEBOXEDITEDEVENT == 0) && insertMetamodel)
				GRAPHDIAGRAMENGINE_ONFREEBOXEDITEDEVENT = raapi.createAttribute(GRAPHDIAGRAMENGINE, "onFreeBoxEditedEvent", raapi.findPrimitiveDataType("String"));
			if (GRAPHDIAGRAMENGINE_ONFREEBOXEDITEDEVENT == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute onFreeBoxEditedEvent of the class GraphDiagramEngine.");
			}
			GRAPHDIAGRAMENGINE_ONFREELINEEDITEDEVENT = raapi.findAttribute(GRAPHDIAGRAMENGINE, "onFreeLineEditedEvent");
			if ((GRAPHDIAGRAMENGINE_ONFREELINEEDITEDEVENT == 0) && insertMetamodel)
				GRAPHDIAGRAMENGINE_ONFREELINEEDITEDEVENT = raapi.createAttribute(GRAPHDIAGRAMENGINE, "onFreeLineEditedEvent", raapi.findPrimitiveDataType("String"));
			if (GRAPHDIAGRAMENGINE_ONFREELINEEDITEDEVENT == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute onFreeLineEditedEvent of the class GraphDiagramEngine.");
			}
			OKCMD_ELEMENT = raapi.findAssociationEnd(OKCMD, "element");
			if ((OKCMD_ELEMENT == 0) && insertMetamodel) {
				OKCMD_ELEMENT = raapi.createAssociation(OKCMD, ELEMENT, "okCmd", "element", false);
			}
			if (OKCMD_ELEMENT == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end element of the class OkCmd.");
			}
			POPUPCMD_POPUPDIAGRAM = raapi.findAssociationEnd(POPUPCMD, "popUpDiagram");
			if ((POPUPCMD_POPUPDIAGRAM == 0) && insertMetamodel) {
				POPUPCMD_POPUPDIAGRAM = raapi.createAssociation(POPUPCMD, POPUPDIAGRAM, "popUpCmd", "popUpDiagram", false);
			}
			if (POPUPCMD_POPUPDIAGRAM == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end popUpDiagram of the class PopUpCmd.");
			}
			PASTECMD_ELEMENT = raapi.findAssociationEnd(PASTECMD, "element");
			if ((PASTECMD_ELEMENT == 0) && insertMetamodel) {
				PASTECMD_ELEMENT = raapi.createAssociation(PASTECMD, ELEMENT, "pasteCmd", "element", false);
			}
			if (PASTECMD_ELEMENT == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end element of the class PasteCmd.");
			}
			ACTIVEELEMENTCMD_ELEMENT = raapi.findAssociationEnd(ACTIVEELEMENTCMD, "element");
			if ((ACTIVEELEMENTCMD_ELEMENT == 0) && insertMetamodel) {
				ACTIVEELEMENTCMD_ELEMENT = raapi.createAssociation(ACTIVEELEMENTCMD, ELEMENT, "activeElementCmd", "element", false);
			}
			if (ACTIVEELEMENTCMD_ELEMENT == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end element of the class ActiveElementCmd.");
			}
			STYLEDIALOGCMD_ELEMENT = raapi.findAssociationEnd(STYLEDIALOGCMD, "element");
			if ((STYLEDIALOGCMD_ELEMENT == 0) && insertMetamodel) {
				STYLEDIALOGCMD_ELEMENT = raapi.createAssociation(STYLEDIALOGCMD, ELEMENT, "styleDialogCmd", "element", false);
			}
			if (STYLEDIALOGCMD_ELEMENT == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end element of the class StyleDialogCmd.");
			}
			DEFAULTSTYLECMD_ELEMENT = raapi.findAssociationEnd(DEFAULTSTYLECMD, "element");
			if ((DEFAULTSTYLECMD_ELEMENT == 0) && insertMetamodel) {
				DEFAULTSTYLECMD_ELEMENT = raapi.createAssociation(DEFAULTSTYLECMD, ELEMENT, "defaultStyleCmd", "element", false);
			}
			if (DEFAULTSTYLECMD_ELEMENT == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end element of the class DefaultStyleCmd.");
			}
			UPDATESTYLECMD_ELEMENT = raapi.findAssociationEnd(UPDATESTYLECMD, "element");
			if ((UPDATESTYLECMD_ELEMENT == 0) && insertMetamodel) {
				UPDATESTYLECMD_ELEMENT = raapi.createAssociation(UPDATESTYLECMD, ELEMENT, "updateStyleCmd", "element", false);
			}
			if (UPDATESTYLECMD_ELEMENT == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end element of the class UpdateStyleCmd.");
			}
			UPDATESTYLECMD_ELEMSTYLE = raapi.findAssociationEnd(UPDATESTYLECMD, "elemStyle");
			if ((UPDATESTYLECMD_ELEMSTYLE == 0) && insertMetamodel) {
				UPDATESTYLECMD_ELEMSTYLE = raapi.createAssociation(UPDATESTYLECMD, ELEMSTYLE, "updateStyleCmd", "elemStyle", false);
			}
			if (UPDATESTYLECMD_ELEMSTYLE == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end elemStyle of the class UpdateStyleCmd.");
			}
			REROUTECMD_ELEMENT = raapi.findAssociationEnd(REROUTECMD, "element");
			if ((REROUTECMD_ELEMENT == 0) && insertMetamodel) {
				REROUTECMD_ELEMENT = raapi.createAssociation(REROUTECMD, ELEMENT, "rerouteCmd", "element", false);
			}
			if (REROUTECMD_ELEMENT == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end element of the class RerouteCmd.");
			}
			ALIGNSIZECMD_ELEMENT = raapi.findAssociationEnd(ALIGNSIZECMD, "element");
			if ((ALIGNSIZECMD_ELEMENT == 0) && insertMetamodel) {
				ALIGNSIZECMD_ELEMENT = raapi.createAssociation(ALIGNSIZECMD, ELEMENT, "alignSizeCmd", "element", false);
			}
			if (ALIGNSIZECMD_ELEMENT == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end element of the class AlignSizeCmd.");
			}
			POPUPELEMSELECTEVENT_POPUPELEMENT = raapi.findAssociationEnd(POPUPELEMSELECTEVENT, "popUpElement");
			if ((POPUPELEMSELECTEVENT_POPUPELEMENT == 0) && insertMetamodel) {
				POPUPELEMSELECTEVENT_POPUPELEMENT = raapi.createAssociation(POPUPELEMSELECTEVENT, POPUPELEMENT, "popUpElemSelectEvent", "popUpElement", false);
			}
			if (POPUPELEMSELECTEVENT_POPUPELEMENT == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end popUpElement of the class PopUpElemSelectEvent.");
			}
			MOVELINESTARTPOINTEVENT_TARGET = raapi.findAssociationEnd(MOVELINESTARTPOINTEVENT, "target");
			if ((MOVELINESTARTPOINTEVENT_TARGET == 0) && insertMetamodel) {
				MOVELINESTARTPOINTEVENT_TARGET = raapi.createAssociation(MOVELINESTARTPOINTEVENT, ELEMENT, "moveLineStartPointEventT", "target", false);
			}
			if (MOVELINESTARTPOINTEVENT_TARGET == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end target of the class MoveLineStartPointEvent.");
			}
			MOVELINESTARTPOINTEVENT_EDGE = raapi.findAssociationEnd(MOVELINESTARTPOINTEVENT, "edge");
			if ((MOVELINESTARTPOINTEVENT_EDGE == 0) && insertMetamodel) {
				MOVELINESTARTPOINTEVENT_EDGE = raapi.createAssociation(MOVELINESTARTPOINTEVENT, EDGE, "moveLineStartPointEventE", "edge", false);
			}
			if (MOVELINESTARTPOINTEVENT_EDGE == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end edge of the class MoveLineStartPointEvent.");
			}
			MOVELINEENDPOINTEVENT_TARGET = raapi.findAssociationEnd(MOVELINEENDPOINTEVENT, "target");
			if ((MOVELINEENDPOINTEVENT_TARGET == 0) && insertMetamodel) {
				MOVELINEENDPOINTEVENT_TARGET = raapi.createAssociation(MOVELINEENDPOINTEVENT, ELEMENT, "moveLineEndPointEventT", "target", false);
			}
			if (MOVELINEENDPOINTEVENT_TARGET == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end target of the class MoveLineEndPointEvent.");
			}
			MOVELINEENDPOINTEVENT_EDGE = raapi.findAssociationEnd(MOVELINEENDPOINTEVENT, "edge");
			if ((MOVELINEENDPOINTEVENT_EDGE == 0) && insertMetamodel) {
				MOVELINEENDPOINTEVENT_EDGE = raapi.createAssociation(MOVELINEENDPOINTEVENT, EDGE, "moveLineEndPointEventE", "edge", false);
			}
			if (MOVELINEENDPOINTEVENT_EDGE == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end edge of the class MoveLineEndPointEvent.");
			}
			L2CLICKEVENT_ELEMENT = raapi.findAssociationEnd(L2CLICKEVENT, "element");
			if ((L2CLICKEVENT_ELEMENT == 0) && insertMetamodel) {
				L2CLICKEVENT_ELEMENT = raapi.createAssociation(L2CLICKEVENT, ELEMENT, "l2ClickEvent", "element", false);
			}
			if (L2CLICKEVENT_ELEMENT == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end element of the class L2ClickEvent.");
			}
			LCLICKEVENT_ELEMENT = raapi.findAssociationEnd(LCLICKEVENT, "element");
			if ((LCLICKEVENT_ELEMENT == 0) && insertMetamodel) {
				LCLICKEVENT_ELEMENT = raapi.createAssociation(LCLICKEVENT, ELEMENT, "lClickEvent", "element", false);
			}
			if (LCLICKEVENT_ELEMENT == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end element of the class LClickEvent.");
			}
			RCLICKEVENT_ELEMENT = raapi.findAssociationEnd(RCLICKEVENT, "element");
			if ((RCLICKEVENT_ELEMENT == 0) && insertMetamodel) {
				RCLICKEVENT_ELEMENT = raapi.createAssociation(RCLICKEVENT, ELEMENT, "rClickEvent", "element", false);
			}
			if (RCLICKEVENT_ELEMENT == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end element of the class RClickEvent.");
			}
			NEWLINEEVENT_START = raapi.findAssociationEnd(NEWLINEEVENT, "start");
			if ((NEWLINEEVENT_START == 0) && insertMetamodel) {
				NEWLINEEVENT_START = raapi.createAssociation(NEWLINEEVENT, ELEMENT, "newLineEventS", "start", false);
			}
			if (NEWLINEEVENT_START == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end start of the class NewLineEvent.");
			}
			NEWLINEEVENT_END = raapi.findAssociationEnd(NEWLINEEVENT, "end");
			if ((NEWLINEEVENT_END == 0) && insertMetamodel) {
				NEWLINEEVENT_END = raapi.createAssociation(NEWLINEEVENT, ELEMENT, "newLineEventE", "end", false);
			}
			if (NEWLINEEVENT_END == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end end of the class NewLineEvent.");
			}
			NEWLINEEVENT_PALETTELINE = raapi.findAssociationEnd(NEWLINEEVENT, "paletteLine");
			if ((NEWLINEEVENT_PALETTELINE == 0) && insertMetamodel) {
				NEWLINEEVENT_PALETTELINE = raapi.createAssociation(NEWLINEEVENT, PALETTELINE, "newLineEvent", "paletteLine", false);
			}
			if (NEWLINEEVENT_PALETTELINE == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end paletteLine of the class NewLineEvent.");
			}
			NEWBOXEVENT_PALETTEBOX = raapi.findAssociationEnd(NEWBOXEVENT, "paletteBox");
			if ((NEWBOXEVENT_PALETTEBOX == 0) && insertMetamodel) {
				NEWBOXEVENT_PALETTEBOX = raapi.createAssociation(NEWBOXEVENT, PALETTEBOX, "newBoxEvent", "paletteBox", false);
			}
			if (NEWBOXEVENT_PALETTEBOX == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end paletteBox of the class NewBoxEvent.");
			}
			NEWBOXEVENT_NODE = raapi.findAssociationEnd(NEWBOXEVENT, "node");
			if ((NEWBOXEVENT_NODE == 0) && insertMetamodel) {
				NEWBOXEVENT_NODE = raapi.createAssociation(NEWBOXEVENT, NODE, "newBoxEvent", "node", false);
			}
			if (NEWBOXEVENT_NODE == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end node of the class NewBoxEvent.");
			}
			NEWPINEVENT_NODE = raapi.findAssociationEnd(NEWPINEVENT, "node");
			if ((NEWPINEVENT_NODE == 0) && insertMetamodel) {
				NEWPINEVENT_NODE = raapi.createAssociation(NEWPINEVENT, NODE, "newPinEvent", "node", false);
			}
			if (NEWPINEVENT_NODE == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end node of the class NewPinEvent.");
			}
			NEWPINEVENT_PALETTEPIN = raapi.findAssociationEnd(NEWPINEVENT, "palettePin");
			if ((NEWPINEVENT_PALETTEPIN == 0) && insertMetamodel) {
				NEWPINEVENT_PALETTEPIN = raapi.createAssociation(NEWPINEVENT, PALETTEPIN, "newPinEvent", "palettePin", false);
			}
			if (NEWPINEVENT_PALETTEPIN == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end palettePin of the class NewPinEvent.");
			}
			CHANGEPARENTEVENT_NODE = raapi.findAssociationEnd(CHANGEPARENTEVENT, "node");
			if ((CHANGEPARENTEVENT_NODE == 0) && insertMetamodel) {
				CHANGEPARENTEVENT_NODE = raapi.createAssociation(CHANGEPARENTEVENT, NODE, "changeParentEventN", "node", false);
			}
			if (CHANGEPARENTEVENT_NODE == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end node of the class ChangeParentEvent.");
			}
			CHANGEPARENTEVENT_TARGET = raapi.findAssociationEnd(CHANGEPARENTEVENT, "target");
			if ((CHANGEPARENTEVENT_TARGET == 0) && insertMetamodel) {
				CHANGEPARENTEVENT_TARGET = raapi.createAssociation(CHANGEPARENTEVENT, NODE, "changeParentEventT", "target", false);
			}
			if (CHANGEPARENTEVENT_TARGET == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end target of the class ChangeParentEvent.");
			}
			ACTIVATEDGREVENT_GRAPHDIAGRAM = raapi.findAssociationEnd(ACTIVATEDGREVENT, "graphDiagram");
			if ((ACTIVATEDGREVENT_GRAPHDIAGRAM == 0) && insertMetamodel) {
				ACTIVATEDGREVENT_GRAPHDIAGRAM = raapi.createAssociation(ACTIVATEDGREVENT, GRAPHDIAGRAM, "activateDgrEvent", "graphDiagram", false);
			}
			if (ACTIVATEDGREVENT_GRAPHDIAGRAM == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end graphDiagram of the class ActivateDgrEvent.");
			}
			NEWFREEBOXEVENT_PALETTEFREEBOX = raapi.findAssociationEnd(NEWFREEBOXEVENT, "paletteFreeBox");
			if ((NEWFREEBOXEVENT_PALETTEFREEBOX == 0) && insertMetamodel) {
				NEWFREEBOXEVENT_PALETTEFREEBOX = raapi.createAssociation(NEWFREEBOXEVENT, PALETTEFREEBOX, "newFreeBoxEvent", "paletteFreeBox", false);
			}
			if (NEWFREEBOXEVENT_PALETTEFREEBOX == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end paletteFreeBox of the class NewFreeBoxEvent.");
			}
			NEWFREELINEEVENT_PALETTEFREELINE = raapi.findAssociationEnd(NEWFREELINEEVENT, "paletteFreeLine");
			if ((NEWFREELINEEVENT_PALETTEFREELINE == 0) && insertMetamodel) {
				NEWFREELINEEVENT_PALETTEFREELINE = raapi.createAssociation(NEWFREELINEEVENT, PALETTEFREELINE, "newFreeLineEvent", "paletteFreeLine", false);
			}
			if (NEWFREELINEEVENT_PALETTEFREELINE == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end paletteFreeLine of the class NewFreeLineEvent.");
			}
			FREEBOXEDITEDEVENT_ELEMENT = raapi.findAssociationEnd(FREEBOXEDITEDEVENT, "element");
			if ((FREEBOXEDITEDEVENT_ELEMENT == 0) && insertMetamodel) {
				FREEBOXEDITEDEVENT_ELEMENT = raapi.createAssociation(FREEBOXEDITEDEVENT, ELEMENT, "freeBoxEditedEvent", "element", false);
			}
			if (FREEBOXEDITEDEVENT_ELEMENT == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end element of the class FreeBoxEditedEvent.");
			}
			FREELINEEDITEDEVENT_ELEMENT = raapi.findAssociationEnd(FREELINEEDITEDEVENT, "element");
			if ((FREELINEEDITEDEVENT_ELEMENT == 0) && insertMetamodel) {
				FREELINEEDITEDEVENT_ELEMENT = raapi.createAssociation(FREELINEEDITEDEVENT, ELEMENT, "freeLineEditedEvent", "element", false);
			}
			if (FREELINEEDITEDEVENT_ELEMENT == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end element of the class FreeLineEditedEvent.");
			}
			TOOLBARELEMENTSELECTEVENT_TOOLBARELEMENT = raapi.findAssociationEnd(TOOLBARELEMENTSELECTEVENT, "toolbarElement");
			if ((TOOLBARELEMENTSELECTEVENT_TOOLBARELEMENT == 0) && insertMetamodel) {
				TOOLBARELEMENTSELECTEVENT_TOOLBARELEMENT = raapi.createAssociation(TOOLBARELEMENTSELECTEVENT, TOOLBARELEMENT, "toolbarElementSelectEvent", "toolbarElement", false);
			}
			if (TOOLBARELEMENTSELECTEVENT_TOOLBARELEMENT == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end toolbarElement of the class ToolbarElementSelectEvent.");
			}
			GRAPHDIAGRAMSTYLE_ID = raapi.findAttribute(GRAPHDIAGRAMSTYLE, "id");
			if ((GRAPHDIAGRAMSTYLE_ID == 0) && insertMetamodel)
				GRAPHDIAGRAMSTYLE_ID = raapi.createAttribute(GRAPHDIAGRAMSTYLE, "id", raapi.findPrimitiveDataType("String"));
			if (GRAPHDIAGRAMSTYLE_ID == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute id of the class GraphDiagramStyle.");
			}
			GRAPHDIAGRAMSTYLE_CAPTION = raapi.findAttribute(GRAPHDIAGRAMSTYLE, "caption");
			if ((GRAPHDIAGRAMSTYLE_CAPTION == 0) && insertMetamodel)
				GRAPHDIAGRAMSTYLE_CAPTION = raapi.createAttribute(GRAPHDIAGRAMSTYLE, "caption", raapi.findPrimitiveDataType("String"));
			if (GRAPHDIAGRAMSTYLE_CAPTION == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute caption of the class GraphDiagramStyle.");
			}
			GRAPHDIAGRAMSTYLE_BKGCOLOR = raapi.findAttribute(GRAPHDIAGRAMSTYLE, "bkgColor");
			if ((GRAPHDIAGRAMSTYLE_BKGCOLOR == 0) && insertMetamodel)
				GRAPHDIAGRAMSTYLE_BKGCOLOR = raapi.createAttribute(GRAPHDIAGRAMSTYLE, "bkgColor", raapi.findPrimitiveDataType("Integer"));
			if (GRAPHDIAGRAMSTYLE_BKGCOLOR == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute bkgColor of the class GraphDiagramStyle.");
			}
			GRAPHDIAGRAMSTYLE_PRINTZOOM = raapi.findAttribute(GRAPHDIAGRAMSTYLE, "printZoom");
			if ((GRAPHDIAGRAMSTYLE_PRINTZOOM == 0) && insertMetamodel)
				GRAPHDIAGRAMSTYLE_PRINTZOOM = raapi.createAttribute(GRAPHDIAGRAMSTYLE, "printZoom", raapi.findPrimitiveDataType("Integer"));
			if (GRAPHDIAGRAMSTYLE_PRINTZOOM == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute printZoom of the class GraphDiagramStyle.");
			}
			GRAPHDIAGRAMSTYLE_SCREENZOOM = raapi.findAttribute(GRAPHDIAGRAMSTYLE, "screenZoom");
			if ((GRAPHDIAGRAMSTYLE_SCREENZOOM == 0) && insertMetamodel)
				GRAPHDIAGRAMSTYLE_SCREENZOOM = raapi.createAttribute(GRAPHDIAGRAMSTYLE, "screenZoom", raapi.findPrimitiveDataType("Integer"));
			if (GRAPHDIAGRAMSTYLE_SCREENZOOM == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute screenZoom of the class GraphDiagramStyle.");
			}
			GRAPHDIAGRAMSTYLE_LAYOUTMODE = raapi.findAttribute(GRAPHDIAGRAMSTYLE, "layoutMode");
			if ((GRAPHDIAGRAMSTYLE_LAYOUTMODE == 0) && insertMetamodel)
				GRAPHDIAGRAMSTYLE_LAYOUTMODE = raapi.createAttribute(GRAPHDIAGRAMSTYLE, "layoutMode", raapi.findPrimitiveDataType("Integer"));
			if (GRAPHDIAGRAMSTYLE_LAYOUTMODE == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute layoutMode of the class GraphDiagramStyle.");
			}
			GRAPHDIAGRAMSTYLE_LAYOUTALGORITHM = raapi.findAttribute(GRAPHDIAGRAMSTYLE, "layoutAlgorithm");
			if ((GRAPHDIAGRAMSTYLE_LAYOUTALGORITHM == 0) && insertMetamodel)
				GRAPHDIAGRAMSTYLE_LAYOUTALGORITHM = raapi.createAttribute(GRAPHDIAGRAMSTYLE, "layoutAlgorithm", raapi.findPrimitiveDataType("Integer"));
			if (GRAPHDIAGRAMSTYLE_LAYOUTALGORITHM == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute layoutAlgorithm of the class GraphDiagramStyle.");
			}
			GRAPHDIAGRAMSTYLE_GRAPHDIAGRAM = raapi.findAssociationEnd(GRAPHDIAGRAMSTYLE, "graphDiagram");
			if ((GRAPHDIAGRAMSTYLE_GRAPHDIAGRAM == 0) && insertMetamodel) {
				GRAPHDIAGRAMSTYLE_GRAPHDIAGRAM = raapi.createAssociation(GRAPHDIAGRAMSTYLE, GRAPHDIAGRAM, "graphDiagramStyle", "graphDiagram", false);
			}
			if (GRAPHDIAGRAMSTYLE_GRAPHDIAGRAM == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end graphDiagram of the class GraphDiagramStyle.");
			}
			ELEMSTYLE_ID = raapi.findAttribute(ELEMSTYLE, "id");
			if ((ELEMSTYLE_ID == 0) && insertMetamodel)
				ELEMSTYLE_ID = raapi.createAttribute(ELEMSTYLE, "id", raapi.findPrimitiveDataType("String"));
			if (ELEMSTYLE_ID == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute id of the class ElemStyle.");
			}
			ELEMSTYLE_CAPTION = raapi.findAttribute(ELEMSTYLE, "caption");
			if ((ELEMSTYLE_CAPTION == 0) && insertMetamodel)
				ELEMSTYLE_CAPTION = raapi.createAttribute(ELEMSTYLE, "caption", raapi.findPrimitiveDataType("String"));
			if (ELEMSTYLE_CAPTION == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute caption of the class ElemStyle.");
			}
			ELEMSTYLE_SHAPECODE = raapi.findAttribute(ELEMSTYLE, "shapeCode");
			if ((ELEMSTYLE_SHAPECODE == 0) && insertMetamodel)
				ELEMSTYLE_SHAPECODE = raapi.createAttribute(ELEMSTYLE, "shapeCode", raapi.findPrimitiveDataType("Integer"));
			if (ELEMSTYLE_SHAPECODE == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute shapeCode of the class ElemStyle.");
			}
			ELEMSTYLE_SHAPESTYLE = raapi.findAttribute(ELEMSTYLE, "shapeStyle");
			if ((ELEMSTYLE_SHAPESTYLE == 0) && insertMetamodel)
				ELEMSTYLE_SHAPESTYLE = raapi.createAttribute(ELEMSTYLE, "shapeStyle", raapi.findPrimitiveDataType("Integer"));
			if (ELEMSTYLE_SHAPESTYLE == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute shapeStyle of the class ElemStyle.");
			}
			ELEMSTYLE_LINEWIDTH = raapi.findAttribute(ELEMSTYLE, "lineWidth");
			if ((ELEMSTYLE_LINEWIDTH == 0) && insertMetamodel)
				ELEMSTYLE_LINEWIDTH = raapi.createAttribute(ELEMSTYLE, "lineWidth", raapi.findPrimitiveDataType("Integer"));
			if (ELEMSTYLE_LINEWIDTH == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute lineWidth of the class ElemStyle.");
			}
			ELEMSTYLE_DASHLENGTH = raapi.findAttribute(ELEMSTYLE, "dashLength");
			if ((ELEMSTYLE_DASHLENGTH == 0) && insertMetamodel)
				ELEMSTYLE_DASHLENGTH = raapi.createAttribute(ELEMSTYLE, "dashLength", raapi.findPrimitiveDataType("Integer"));
			if (ELEMSTYLE_DASHLENGTH == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute dashLength of the class ElemStyle.");
			}
			ELEMSTYLE_BREAKLENGTH = raapi.findAttribute(ELEMSTYLE, "breakLength");
			if ((ELEMSTYLE_BREAKLENGTH == 0) && insertMetamodel)
				ELEMSTYLE_BREAKLENGTH = raapi.createAttribute(ELEMSTYLE, "breakLength", raapi.findPrimitiveDataType("Integer"));
			if (ELEMSTYLE_BREAKLENGTH == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute breakLength of the class ElemStyle.");
			}
			ELEMSTYLE_BKGCOLOR = raapi.findAttribute(ELEMSTYLE, "bkgColor");
			if ((ELEMSTYLE_BKGCOLOR == 0) && insertMetamodel)
				ELEMSTYLE_BKGCOLOR = raapi.createAttribute(ELEMSTYLE, "bkgColor", raapi.findPrimitiveDataType("Integer"));
			if (ELEMSTYLE_BKGCOLOR == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute bkgColor of the class ElemStyle.");
			}
			ELEMSTYLE_LINECOLOR = raapi.findAttribute(ELEMSTYLE, "lineColor");
			if ((ELEMSTYLE_LINECOLOR == 0) && insertMetamodel)
				ELEMSTYLE_LINECOLOR = raapi.createAttribute(ELEMSTYLE, "lineColor", raapi.findPrimitiveDataType("Integer"));
			if (ELEMSTYLE_LINECOLOR == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute lineColor of the class ElemStyle.");
			}
			ELEMSTYLE_DYNAMICTOOLTIP = raapi.findAttribute(ELEMSTYLE, "dynamicTooltip");
			if ((ELEMSTYLE_DYNAMICTOOLTIP == 0) && insertMetamodel)
				ELEMSTYLE_DYNAMICTOOLTIP = raapi.createAttribute(ELEMSTYLE, "dynamicTooltip", raapi.findPrimitiveDataType("Integer"));
			if (ELEMSTYLE_DYNAMICTOOLTIP == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute dynamicTooltip of the class ElemStyle.");
			}
			ELEMSTYLE_ELEMENT = raapi.findAssociationEnd(ELEMSTYLE, "element");
			if ((ELEMSTYLE_ELEMENT == 0) && insertMetamodel) {
				ELEMSTYLE_ELEMENT = raapi.createAssociation(ELEMSTYLE, ELEMENT, "elemStyle", "element", false);
			}
			if (ELEMSTYLE_ELEMENT == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end element of the class ElemStyle.");
			}
			ELEMSTYLE_UPDATESTYLECMD = raapi.findAssociationEnd(ELEMSTYLE, "updateStyleCmd");
			if ((ELEMSTYLE_UPDATESTYLECMD == 0) && insertMetamodel) {
				ELEMSTYLE_UPDATESTYLECMD = raapi.createAssociation(ELEMSTYLE, UPDATESTYLECMD, "elemStyle", "updateStyleCmd", false);
			}
			if (ELEMSTYLE_UPDATESTYLECMD == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end updateStyleCmd of the class ElemStyle.");
			}
			NODESTYLE_PICTURE = raapi.findAttribute(NODESTYLE, "picture");
			if ((NODESTYLE_PICTURE == 0) && insertMetamodel)
				NODESTYLE_PICTURE = raapi.createAttribute(NODESTYLE, "picture", raapi.findPrimitiveDataType("String"));
			if (NODESTYLE_PICTURE == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute picture of the class NodeStyle.");
			}
			NODESTYLE_PICWIDTH = raapi.findAttribute(NODESTYLE, "picWidth");
			if ((NODESTYLE_PICWIDTH == 0) && insertMetamodel)
				NODESTYLE_PICWIDTH = raapi.createAttribute(NODESTYLE, "picWidth", raapi.findPrimitiveDataType("Integer"));
			if (NODESTYLE_PICWIDTH == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute picWidth of the class NodeStyle.");
			}
			NODESTYLE_PICHEIGHT = raapi.findAttribute(NODESTYLE, "picHeight");
			if ((NODESTYLE_PICHEIGHT == 0) && insertMetamodel)
				NODESTYLE_PICHEIGHT = raapi.createAttribute(NODESTYLE, "picHeight", raapi.findPrimitiveDataType("Integer"));
			if (NODESTYLE_PICHEIGHT == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute picHeight of the class NodeStyle.");
			}
			NODESTYLE_PICPOS = raapi.findAttribute(NODESTYLE, "picPos");
			if ((NODESTYLE_PICPOS == 0) && insertMetamodel)
				NODESTYLE_PICPOS = raapi.createAttribute(NODESTYLE, "picPos", raapi.findPrimitiveDataType("Integer"));
			if (NODESTYLE_PICPOS == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute picPos of the class NodeStyle.");
			}
			NODESTYLE_PICSTYLE = raapi.findAttribute(NODESTYLE, "picStyle");
			if ((NODESTYLE_PICSTYLE == 0) && insertMetamodel)
				NODESTYLE_PICSTYLE = raapi.createAttribute(NODESTYLE, "picStyle", raapi.findPrimitiveDataType("Integer"));
			if (NODESTYLE_PICSTYLE == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute picStyle of the class NodeStyle.");
			}
			NODESTYLE_WIDTH = raapi.findAttribute(NODESTYLE, "width");
			if ((NODESTYLE_WIDTH == 0) && insertMetamodel)
				NODESTYLE_WIDTH = raapi.createAttribute(NODESTYLE, "width", raapi.findPrimitiveDataType("Integer"));
			if (NODESTYLE_WIDTH == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute width of the class NodeStyle.");
			}
			NODESTYLE_HEIGHT = raapi.findAttribute(NODESTYLE, "height");
			if ((NODESTYLE_HEIGHT == 0) && insertMetamodel)
				NODESTYLE_HEIGHT = raapi.createAttribute(NODESTYLE, "height", raapi.findPrimitiveDataType("Integer"));
			if (NODESTYLE_HEIGHT == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute height of the class NodeStyle.");
			}
			NODESTYLE_ALIGNMENT = raapi.findAttribute(NODESTYLE, "alignment");
			if ((NODESTYLE_ALIGNMENT == 0) && insertMetamodel)
				NODESTYLE_ALIGNMENT = raapi.createAttribute(NODESTYLE, "alignment", raapi.findPrimitiveDataType("Integer"));
			if (NODESTYLE_ALIGNMENT == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute alignment of the class NodeStyle.");
			}
			EDGESTYLE_STARTSHAPECODE = raapi.findAttribute(EDGESTYLE, "startShapeCode");
			if ((EDGESTYLE_STARTSHAPECODE == 0) && insertMetamodel)
				EDGESTYLE_STARTSHAPECODE = raapi.createAttribute(EDGESTYLE, "startShapeCode", raapi.findPrimitiveDataType("Integer"));
			if (EDGESTYLE_STARTSHAPECODE == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute startShapeCode of the class EdgeStyle.");
			}
			EDGESTYLE_STARTLINEWIDTH = raapi.findAttribute(EDGESTYLE, "startLineWidth");
			if ((EDGESTYLE_STARTLINEWIDTH == 0) && insertMetamodel)
				EDGESTYLE_STARTLINEWIDTH = raapi.createAttribute(EDGESTYLE, "startLineWidth", raapi.findPrimitiveDataType("Integer"));
			if (EDGESTYLE_STARTLINEWIDTH == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute startLineWidth of the class EdgeStyle.");
			}
			EDGESTYLE_STARTBKGCOLOR = raapi.findAttribute(EDGESTYLE, "startBkgColor");
			if ((EDGESTYLE_STARTBKGCOLOR == 0) && insertMetamodel)
				EDGESTYLE_STARTBKGCOLOR = raapi.createAttribute(EDGESTYLE, "startBkgColor", raapi.findPrimitiveDataType("Integer"));
			if (EDGESTYLE_STARTBKGCOLOR == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute startBkgColor of the class EdgeStyle.");
			}
			EDGESTYLE_STARTLINECOLOR = raapi.findAttribute(EDGESTYLE, "startLineColor");
			if ((EDGESTYLE_STARTLINECOLOR == 0) && insertMetamodel)
				EDGESTYLE_STARTLINECOLOR = raapi.createAttribute(EDGESTYLE, "startLineColor", raapi.findPrimitiveDataType("Integer"));
			if (EDGESTYLE_STARTLINECOLOR == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute startLineColor of the class EdgeStyle.");
			}
			EDGESTYLE_LINETYPE = raapi.findAttribute(EDGESTYLE, "lineType");
			if ((EDGESTYLE_LINETYPE == 0) && insertMetamodel)
				EDGESTYLE_LINETYPE = raapi.createAttribute(EDGESTYLE, "lineType", raapi.findPrimitiveDataType("Integer"));
			if (EDGESTYLE_LINETYPE == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute lineType of the class EdgeStyle.");
			}
			EDGESTYLE_ENDSHAPECODE = raapi.findAttribute(EDGESTYLE, "endShapeCode");
			if ((EDGESTYLE_ENDSHAPECODE == 0) && insertMetamodel)
				EDGESTYLE_ENDSHAPECODE = raapi.createAttribute(EDGESTYLE, "endShapeCode", raapi.findPrimitiveDataType("Integer"));
			if (EDGESTYLE_ENDSHAPECODE == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute endShapeCode of the class EdgeStyle.");
			}
			EDGESTYLE_ENDLINEWIDTH = raapi.findAttribute(EDGESTYLE, "endLineWidth");
			if ((EDGESTYLE_ENDLINEWIDTH == 0) && insertMetamodel)
				EDGESTYLE_ENDLINEWIDTH = raapi.createAttribute(EDGESTYLE, "endLineWidth", raapi.findPrimitiveDataType("Integer"));
			if (EDGESTYLE_ENDLINEWIDTH == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute endLineWidth of the class EdgeStyle.");
			}
			EDGESTYLE_ENDBKGCOLOR = raapi.findAttribute(EDGESTYLE, "endBkgColor");
			if ((EDGESTYLE_ENDBKGCOLOR == 0) && insertMetamodel)
				EDGESTYLE_ENDBKGCOLOR = raapi.createAttribute(EDGESTYLE, "endBkgColor", raapi.findPrimitiveDataType("Integer"));
			if (EDGESTYLE_ENDBKGCOLOR == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute endBkgColor of the class EdgeStyle.");
			}
			EDGESTYLE_ENDLINECOLOR = raapi.findAttribute(EDGESTYLE, "endLineColor");
			if ((EDGESTYLE_ENDLINECOLOR == 0) && insertMetamodel)
				EDGESTYLE_ENDLINECOLOR = raapi.createAttribute(EDGESTYLE, "endLineColor", raapi.findPrimitiveDataType("Integer"));
			if (EDGESTYLE_ENDLINECOLOR == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute endLineColor of the class EdgeStyle.");
			}
			EDGESTYLE_MIDDLESHAPECODE = raapi.findAttribute(EDGESTYLE, "middleShapeCode");
			if ((EDGESTYLE_MIDDLESHAPECODE == 0) && insertMetamodel)
				EDGESTYLE_MIDDLESHAPECODE = raapi.createAttribute(EDGESTYLE, "middleShapeCode", raapi.findPrimitiveDataType("Integer"));
			if (EDGESTYLE_MIDDLESHAPECODE == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute middleShapeCode of the class EdgeStyle.");
			}
			EDGESTYLE_MIDDLELINEWIDTH = raapi.findAttribute(EDGESTYLE, "middleLineWidth");
			if ((EDGESTYLE_MIDDLELINEWIDTH == 0) && insertMetamodel)
				EDGESTYLE_MIDDLELINEWIDTH = raapi.createAttribute(EDGESTYLE, "middleLineWidth", raapi.findPrimitiveDataType("Integer"));
			if (EDGESTYLE_MIDDLELINEWIDTH == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute middleLineWidth of the class EdgeStyle.");
			}
			EDGESTYLE_MIDDLEDASHLENGTH = raapi.findAttribute(EDGESTYLE, "middleDashLength");
			if ((EDGESTYLE_MIDDLEDASHLENGTH == 0) && insertMetamodel)
				EDGESTYLE_MIDDLEDASHLENGTH = raapi.createAttribute(EDGESTYLE, "middleDashLength", raapi.findPrimitiveDataType("Integer"));
			if (EDGESTYLE_MIDDLEDASHLENGTH == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute middleDashLength of the class EdgeStyle.");
			}
			EDGESTYLE_MIDDLEBREAKLENGTH = raapi.findAttribute(EDGESTYLE, "middleBreakLength");
			if ((EDGESTYLE_MIDDLEBREAKLENGTH == 0) && insertMetamodel)
				EDGESTYLE_MIDDLEBREAKLENGTH = raapi.createAttribute(EDGESTYLE, "middleBreakLength", raapi.findPrimitiveDataType("Integer"));
			if (EDGESTYLE_MIDDLEBREAKLENGTH == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute middleBreakLength of the class EdgeStyle.");
			}
			EDGESTYLE_MIDDLEBKGCOLOR = raapi.findAttribute(EDGESTYLE, "middleBkgColor");
			if ((EDGESTYLE_MIDDLEBKGCOLOR == 0) && insertMetamodel)
				EDGESTYLE_MIDDLEBKGCOLOR = raapi.createAttribute(EDGESTYLE, "middleBkgColor", raapi.findPrimitiveDataType("Integer"));
			if (EDGESTYLE_MIDDLEBKGCOLOR == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute middleBkgColor of the class EdgeStyle.");
			}
			EDGESTYLE_MIDDLELINECOLOR = raapi.findAttribute(EDGESTYLE, "middleLineColor");
			if ((EDGESTYLE_MIDDLELINECOLOR == 0) && insertMetamodel)
				EDGESTYLE_MIDDLELINECOLOR = raapi.createAttribute(EDGESTYLE, "middleLineColor", raapi.findPrimitiveDataType("Integer"));
			if (EDGESTYLE_MIDDLELINECOLOR == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute middleLineColor of the class EdgeStyle.");
			}
			PORTSTYLE_PICTURE = raapi.findAttribute(PORTSTYLE, "picture");
			if ((PORTSTYLE_PICTURE == 0) && insertMetamodel)
				PORTSTYLE_PICTURE = raapi.createAttribute(PORTSTYLE, "picture", raapi.findPrimitiveDataType("String"));
			if (PORTSTYLE_PICTURE == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute picture of the class PortStyle.");
			}
			PORTSTYLE_WIDTH = raapi.findAttribute(PORTSTYLE, "width");
			if ((PORTSTYLE_WIDTH == 0) && insertMetamodel)
				PORTSTYLE_WIDTH = raapi.createAttribute(PORTSTYLE, "width", raapi.findPrimitiveDataType("Integer"));
			if (PORTSTYLE_WIDTH == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute width of the class PortStyle.");
			}
			PORTSTYLE_HEIGHT = raapi.findAttribute(PORTSTYLE, "height");
			if ((PORTSTYLE_HEIGHT == 0) && insertMetamodel)
				PORTSTYLE_HEIGHT = raapi.createAttribute(PORTSTYLE, "height", raapi.findPrimitiveDataType("Integer"));
			if (PORTSTYLE_HEIGHT == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute height of the class PortStyle.");
			}
			PORTSTYLE_ALIGNMENT = raapi.findAttribute(PORTSTYLE, "alignment");
			if ((PORTSTYLE_ALIGNMENT == 0) && insertMetamodel)
				PORTSTYLE_ALIGNMENT = raapi.createAttribute(PORTSTYLE, "alignment", raapi.findPrimitiveDataType("Integer"));
			if (PORTSTYLE_ALIGNMENT == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute alignment of the class PortStyle.");
			}
			PORTSTYLE_PICPOS = raapi.findAttribute(PORTSTYLE, "picPos");
			if ((PORTSTYLE_PICPOS == 0) && insertMetamodel)
				PORTSTYLE_PICPOS = raapi.createAttribute(PORTSTYLE, "picPos", raapi.findPrimitiveDataType("Integer"));
			if (PORTSTYLE_PICPOS == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute picPos of the class PortStyle.");
			}
			PORTSTYLE_PICSTYLE = raapi.findAttribute(PORTSTYLE, "picStyle");
			if ((PORTSTYLE_PICSTYLE == 0) && insertMetamodel)
				PORTSTYLE_PICSTYLE = raapi.createAttribute(PORTSTYLE, "picStyle", raapi.findPrimitiveDataType("Integer"));
			if (PORTSTYLE_PICSTYLE == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute picStyle of the class PortStyle.");
			}
			COMPARTSTYLE_ID = raapi.findAttribute(COMPARTSTYLE, "id");
			if ((COMPARTSTYLE_ID == 0) && insertMetamodel)
				COMPARTSTYLE_ID = raapi.createAttribute(COMPARTSTYLE, "id", raapi.findPrimitiveDataType("String"));
			if (COMPARTSTYLE_ID == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute id of the class CompartStyle.");
			}
			COMPARTSTYLE_CAPTION = raapi.findAttribute(COMPARTSTYLE, "caption");
			if ((COMPARTSTYLE_CAPTION == 0) && insertMetamodel)
				COMPARTSTYLE_CAPTION = raapi.createAttribute(COMPARTSTYLE, "caption", raapi.findPrimitiveDataType("String"));
			if (COMPARTSTYLE_CAPTION == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute caption of the class CompartStyle.");
			}
			COMPARTSTYLE_NR = raapi.findAttribute(COMPARTSTYLE, "nr");
			if ((COMPARTSTYLE_NR == 0) && insertMetamodel)
				COMPARTSTYLE_NR = raapi.createAttribute(COMPARTSTYLE, "nr", raapi.findPrimitiveDataType("Integer"));
			if (COMPARTSTYLE_NR == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute nr of the class CompartStyle.");
			}
			COMPARTSTYLE_ALIGNMENT = raapi.findAttribute(COMPARTSTYLE, "alignment");
			if ((COMPARTSTYLE_ALIGNMENT == 0) && insertMetamodel)
				COMPARTSTYLE_ALIGNMENT = raapi.createAttribute(COMPARTSTYLE, "alignment", raapi.findPrimitiveDataType("Integer"));
			if (COMPARTSTYLE_ALIGNMENT == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute alignment of the class CompartStyle.");
			}
			COMPARTSTYLE_ADJUSTMENT = raapi.findAttribute(COMPARTSTYLE, "adjustment");
			if ((COMPARTSTYLE_ADJUSTMENT == 0) && insertMetamodel)
				COMPARTSTYLE_ADJUSTMENT = raapi.createAttribute(COMPARTSTYLE, "adjustment", raapi.findPrimitiveDataType("Integer"));
			if (COMPARTSTYLE_ADJUSTMENT == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute adjustment of the class CompartStyle.");
			}
			COMPARTSTYLE_PICTURE = raapi.findAttribute(COMPARTSTYLE, "picture");
			if ((COMPARTSTYLE_PICTURE == 0) && insertMetamodel)
				COMPARTSTYLE_PICTURE = raapi.createAttribute(COMPARTSTYLE, "picture", raapi.findPrimitiveDataType("String"));
			if (COMPARTSTYLE_PICTURE == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute picture of the class CompartStyle.");
			}
			COMPARTSTYLE_PICWIDTH = raapi.findAttribute(COMPARTSTYLE, "picWidth");
			if ((COMPARTSTYLE_PICWIDTH == 0) && insertMetamodel)
				COMPARTSTYLE_PICWIDTH = raapi.createAttribute(COMPARTSTYLE, "picWidth", raapi.findPrimitiveDataType("Integer"));
			if (COMPARTSTYLE_PICWIDTH == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute picWidth of the class CompartStyle.");
			}
			COMPARTSTYLE_PICHEIGHT = raapi.findAttribute(COMPARTSTYLE, "picHeight");
			if ((COMPARTSTYLE_PICHEIGHT == 0) && insertMetamodel)
				COMPARTSTYLE_PICHEIGHT = raapi.createAttribute(COMPARTSTYLE, "picHeight", raapi.findPrimitiveDataType("Integer"));
			if (COMPARTSTYLE_PICHEIGHT == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute picHeight of the class CompartStyle.");
			}
			COMPARTSTYLE_PICPOS = raapi.findAttribute(COMPARTSTYLE, "picPos");
			if ((COMPARTSTYLE_PICPOS == 0) && insertMetamodel)
				COMPARTSTYLE_PICPOS = raapi.createAttribute(COMPARTSTYLE, "picPos", raapi.findPrimitiveDataType("Integer"));
			if (COMPARTSTYLE_PICPOS == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute picPos of the class CompartStyle.");
			}
			COMPARTSTYLE_PICSTYLE = raapi.findAttribute(COMPARTSTYLE, "picStyle");
			if ((COMPARTSTYLE_PICSTYLE == 0) && insertMetamodel)
				COMPARTSTYLE_PICSTYLE = raapi.createAttribute(COMPARTSTYLE, "picStyle", raapi.findPrimitiveDataType("Integer"));
			if (COMPARTSTYLE_PICSTYLE == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute picStyle of the class CompartStyle.");
			}
			COMPARTSTYLE_ADORNMENT = raapi.findAttribute(COMPARTSTYLE, "adornment");
			if ((COMPARTSTYLE_ADORNMENT == 0) && insertMetamodel)
				COMPARTSTYLE_ADORNMENT = raapi.createAttribute(COMPARTSTYLE, "adornment", raapi.findPrimitiveDataType("Integer"));
			if (COMPARTSTYLE_ADORNMENT == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute adornment of the class CompartStyle.");
			}
			COMPARTSTYLE_LINEWIDTH = raapi.findAttribute(COMPARTSTYLE, "lineWidth");
			if ((COMPARTSTYLE_LINEWIDTH == 0) && insertMetamodel)
				COMPARTSTYLE_LINEWIDTH = raapi.createAttribute(COMPARTSTYLE, "lineWidth", raapi.findPrimitiveDataType("Integer"));
			if (COMPARTSTYLE_LINEWIDTH == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute lineWidth of the class CompartStyle.");
			}
			COMPARTSTYLE_LINECOLOR = raapi.findAttribute(COMPARTSTYLE, "lineColor");
			if ((COMPARTSTYLE_LINECOLOR == 0) && insertMetamodel)
				COMPARTSTYLE_LINECOLOR = raapi.createAttribute(COMPARTSTYLE, "lineColor", raapi.findPrimitiveDataType("Integer"));
			if (COMPARTSTYLE_LINECOLOR == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute lineColor of the class CompartStyle.");
			}
			COMPARTSTYLE_FONTTYPEFACE = raapi.findAttribute(COMPARTSTYLE, "fontTypeFace");
			if ((COMPARTSTYLE_FONTTYPEFACE == 0) && insertMetamodel)
				COMPARTSTYLE_FONTTYPEFACE = raapi.createAttribute(COMPARTSTYLE, "fontTypeFace", raapi.findPrimitiveDataType("String"));
			if (COMPARTSTYLE_FONTTYPEFACE == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute fontTypeFace of the class CompartStyle.");
			}
			COMPARTSTYLE_FONTCHARSET = raapi.findAttribute(COMPARTSTYLE, "fontCharSet");
			if ((COMPARTSTYLE_FONTCHARSET == 0) && insertMetamodel)
				COMPARTSTYLE_FONTCHARSET = raapi.createAttribute(COMPARTSTYLE, "fontCharSet", raapi.findPrimitiveDataType("Integer"));
			if (COMPARTSTYLE_FONTCHARSET == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute fontCharSet of the class CompartStyle.");
			}
			COMPARTSTYLE_FONTCOLOR = raapi.findAttribute(COMPARTSTYLE, "fontColor");
			if ((COMPARTSTYLE_FONTCOLOR == 0) && insertMetamodel)
				COMPARTSTYLE_FONTCOLOR = raapi.createAttribute(COMPARTSTYLE, "fontColor", raapi.findPrimitiveDataType("Integer"));
			if (COMPARTSTYLE_FONTCOLOR == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute fontColor of the class CompartStyle.");
			}
			COMPARTSTYLE_FONTSIZE = raapi.findAttribute(COMPARTSTYLE, "fontSize");
			if ((COMPARTSTYLE_FONTSIZE == 0) && insertMetamodel)
				COMPARTSTYLE_FONTSIZE = raapi.createAttribute(COMPARTSTYLE, "fontSize", raapi.findPrimitiveDataType("Integer"));
			if (COMPARTSTYLE_FONTSIZE == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute fontSize of the class CompartStyle.");
			}
			COMPARTSTYLE_FONTPITCH = raapi.findAttribute(COMPARTSTYLE, "fontPitch");
			if ((COMPARTSTYLE_FONTPITCH == 0) && insertMetamodel)
				COMPARTSTYLE_FONTPITCH = raapi.createAttribute(COMPARTSTYLE, "fontPitch", raapi.findPrimitiveDataType("Integer"));
			if (COMPARTSTYLE_FONTPITCH == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute fontPitch of the class CompartStyle.");
			}
			COMPARTSTYLE_FONTSTYLE = raapi.findAttribute(COMPARTSTYLE, "fontStyle");
			if ((COMPARTSTYLE_FONTSTYLE == 0) && insertMetamodel)
				COMPARTSTYLE_FONTSTYLE = raapi.createAttribute(COMPARTSTYLE, "fontStyle", raapi.findPrimitiveDataType("Integer"));
			if (COMPARTSTYLE_FONTSTYLE == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute fontStyle of the class CompartStyle.");
			}
			COMPARTSTYLE_ISVISIBLE = raapi.findAttribute(COMPARTSTYLE, "isVisible");
			if ((COMPARTSTYLE_ISVISIBLE == 0) && insertMetamodel)
				COMPARTSTYLE_ISVISIBLE = raapi.createAttribute(COMPARTSTYLE, "isVisible", raapi.findPrimitiveDataType("Integer"));
			if (COMPARTSTYLE_ISVISIBLE == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute isVisible of the class CompartStyle.");
			}
			COMPARTSTYLE_LINESTARTDIRECTION = raapi.findAttribute(COMPARTSTYLE, "lineStartDirection");
			if ((COMPARTSTYLE_LINESTARTDIRECTION == 0) && insertMetamodel)
				COMPARTSTYLE_LINESTARTDIRECTION = raapi.createAttribute(COMPARTSTYLE, "lineStartDirection", raapi.findPrimitiveDataType("Boolean"));
			if (COMPARTSTYLE_LINESTARTDIRECTION == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute lineStartDirection of the class CompartStyle.");
			}
			COMPARTSTYLE_LINEENDDIRECTION = raapi.findAttribute(COMPARTSTYLE, "lineEndDirection");
			if ((COMPARTSTYLE_LINEENDDIRECTION == 0) && insertMetamodel)
				COMPARTSTYLE_LINEENDDIRECTION = raapi.createAttribute(COMPARTSTYLE, "lineEndDirection", raapi.findPrimitiveDataType("Boolean"));
			if (COMPARTSTYLE_LINEENDDIRECTION == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute lineEndDirection of the class CompartStyle.");
			}
			COMPARTSTYLE_BREAKATSPACE = raapi.findAttribute(COMPARTSTYLE, "breakAtSpace");
			if ((COMPARTSTYLE_BREAKATSPACE == 0) && insertMetamodel)
				COMPARTSTYLE_BREAKATSPACE = raapi.createAttribute(COMPARTSTYLE, "breakAtSpace", raapi.findPrimitiveDataType("Integer"));
			if (COMPARTSTYLE_BREAKATSPACE == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute breakAtSpace of the class CompartStyle.");
			}
			COMPARTSTYLE_COMPACTVISIBLE = raapi.findAttribute(COMPARTSTYLE, "compactVisible");
			if ((COMPARTSTYLE_COMPACTVISIBLE == 0) && insertMetamodel)
				COMPARTSTYLE_COMPACTVISIBLE = raapi.createAttribute(COMPARTSTYLE, "compactVisible", raapi.findPrimitiveDataType("Integer"));
			if (COMPARTSTYLE_COMPACTVISIBLE == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute compactVisible of the class CompartStyle.");
			}
			COMPARTSTYLE_DYNAMICTOOLTIP = raapi.findAttribute(COMPARTSTYLE, "dynamicTooltip");
			if ((COMPARTSTYLE_DYNAMICTOOLTIP == 0) && insertMetamodel)
				COMPARTSTYLE_DYNAMICTOOLTIP = raapi.createAttribute(COMPARTSTYLE, "dynamicTooltip", raapi.findPrimitiveDataType("Integer"));
			if (COMPARTSTYLE_DYNAMICTOOLTIP == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute dynamicTooltip of the class CompartStyle.");
			}
			COMPARTSTYLE_COMPARTMENT = raapi.findAssociationEnd(COMPARTSTYLE, "compartment");
			if ((COMPARTSTYLE_COMPARTMENT == 0) && insertMetamodel) {
				COMPARTSTYLE_COMPARTMENT = raapi.createAssociation(COMPARTSTYLE, COMPARTMENT, "compartStyle", "compartment", false);
			}
			if (COMPARTSTYLE_COMPARTMENT == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end compartment of the class CompartStyle.");
			}
			POPUPDIAGRAM_POPUPCMD = raapi.findAssociationEnd(POPUPDIAGRAM, "popUpCmd");
			if ((POPUPDIAGRAM_POPUPCMD == 0) && insertMetamodel) {
				POPUPDIAGRAM_POPUPCMD = raapi.createAssociation(POPUPDIAGRAM, POPUPCMD, "popUpDiagram", "popUpCmd", false);
			}
			if (POPUPDIAGRAM_POPUPCMD == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end popUpCmd of the class PopUpDiagram.");
			}
			POPUPDIAGRAM_POPUPELEMENT = raapi.findAssociationEnd(POPUPDIAGRAM, "popUpElement");
			if ((POPUPDIAGRAM_POPUPELEMENT == 0) && insertMetamodel) {
				POPUPDIAGRAM_POPUPELEMENT = raapi.createAssociation(POPUPDIAGRAM, POPUPELEMENT, "popUpDiagram", "popUpElement", true);
			}
			if (POPUPDIAGRAM_POPUPELEMENT == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end popUpElement of the class PopUpDiagram.");
			}
			POPUPELEMENT_CAPTION = raapi.findAttribute(POPUPELEMENT, "caption");
			if ((POPUPELEMENT_CAPTION == 0) && insertMetamodel)
				POPUPELEMENT_CAPTION = raapi.createAttribute(POPUPELEMENT, "caption", raapi.findPrimitiveDataType("String"));
			if (POPUPELEMENT_CAPTION == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute caption of the class PopUpElement.");
			}
			POPUPELEMENT_PROCEDURENAME = raapi.findAttribute(POPUPELEMENT, "procedureName");
			if ((POPUPELEMENT_PROCEDURENAME == 0) && insertMetamodel)
				POPUPELEMENT_PROCEDURENAME = raapi.createAttribute(POPUPELEMENT, "procedureName", raapi.findPrimitiveDataType("String"));
			if (POPUPELEMENT_PROCEDURENAME == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute procedureName of the class PopUpElement.");
			}
			POPUPELEMENT_POPUPDIAGRAM = raapi.findAssociationEnd(POPUPELEMENT, "popUpDiagram");
			if ((POPUPELEMENT_POPUPDIAGRAM == 0) && insertMetamodel) {
				POPUPELEMENT_POPUPDIAGRAM = raapi.createAssociation(POPUPELEMENT, POPUPDIAGRAM, "popUpElement", "popUpDiagram", false);
			}
			if (POPUPELEMENT_POPUPDIAGRAM == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end popUpDiagram of the class PopUpElement.");
			}
			POPUPELEMENT_POPUPELEMSELECTEVENT = raapi.findAssociationEnd(POPUPELEMENT, "popUpElemSelectEvent");
			if ((POPUPELEMENT_POPUPELEMSELECTEVENT == 0) && insertMetamodel) {
				POPUPELEMENT_POPUPELEMSELECTEVENT = raapi.createAssociation(POPUPELEMENT, POPUPELEMSELECTEVENT, "popUpElement", "popUpElemSelectEvent", false);
			}
			if (POPUPELEMENT_POPUPELEMSELECTEVENT == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end popUpElemSelectEvent of the class PopUpElement.");
			}
			PALETTE_GRAPHDIAGRAM = raapi.findAssociationEnd(PALETTE, "graphDiagram");
			if ((PALETTE_GRAPHDIAGRAM == 0) && insertMetamodel) {
				PALETTE_GRAPHDIAGRAM = raapi.createAssociation(PALETTE, GRAPHDIAGRAM, "palette", "graphDiagram", false);
			}
			if (PALETTE_GRAPHDIAGRAM == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end graphDiagram of the class Palette.");
			}
			PALETTE_PALETTEELEMENT = raapi.findAssociationEnd(PALETTE, "paletteElement");
			if ((PALETTE_PALETTEELEMENT == 0) && insertMetamodel) {
				PALETTE_PALETTEELEMENT = raapi.createAssociation(PALETTE, PALETTEELEMENT, "palette", "paletteElement", true);
			}
			if (PALETTE_PALETTEELEMENT == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end paletteElement of the class Palette.");
			}
			PALETTEELEMENT_CAPTION = raapi.findAttribute(PALETTEELEMENT, "caption");
			if ((PALETTEELEMENT_CAPTION == 0) && insertMetamodel)
				PALETTEELEMENT_CAPTION = raapi.createAttribute(PALETTEELEMENT, "caption", raapi.findPrimitiveDataType("String"));
			if (PALETTEELEMENT_CAPTION == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute caption of the class PaletteElement.");
			}
			PALETTEELEMENT_PICTURE = raapi.findAttribute(PALETTEELEMENT, "picture");
			if ((PALETTEELEMENT_PICTURE == 0) && insertMetamodel)
				PALETTEELEMENT_PICTURE = raapi.createAttribute(PALETTEELEMENT, "picture", raapi.findPrimitiveDataType("String"));
			if (PALETTEELEMENT_PICTURE == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute picture of the class PaletteElement.");
			}
			PALETTEELEMENT_PALETTE = raapi.findAssociationEnd(PALETTEELEMENT, "palette");
			if ((PALETTEELEMENT_PALETTE == 0) && insertMetamodel) {
				PALETTEELEMENT_PALETTE = raapi.createAssociation(PALETTEELEMENT, PALETTE, "paletteElement", "palette", false);
			}
			if (PALETTEELEMENT_PALETTE == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end palette of the class PaletteElement.");
			}
			PALETTEBOX_NEWBOXEVENT = raapi.findAssociationEnd(PALETTEBOX, "newBoxEvent");
			if ((PALETTEBOX_NEWBOXEVENT == 0) && insertMetamodel) {
				PALETTEBOX_NEWBOXEVENT = raapi.createAssociation(PALETTEBOX, NEWBOXEVENT, "paletteBox", "newBoxEvent", false);
			}
			if (PALETTEBOX_NEWBOXEVENT == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end newBoxEvent of the class PaletteBox.");
			}
			PALETTELINE_NEWLINEEVENT = raapi.findAssociationEnd(PALETTELINE, "newLineEvent");
			if ((PALETTELINE_NEWLINEEVENT == 0) && insertMetamodel) {
				PALETTELINE_NEWLINEEVENT = raapi.createAssociation(PALETTELINE, NEWLINEEVENT, "paletteLine", "newLineEvent", false);
			}
			if (PALETTELINE_NEWLINEEVENT == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end newLineEvent of the class PaletteLine.");
			}
			PALETTEPIN_NEWPINEVENT = raapi.findAssociationEnd(PALETTEPIN, "newPinEvent");
			if ((PALETTEPIN_NEWPINEVENT == 0) && insertMetamodel) {
				PALETTEPIN_NEWPINEVENT = raapi.createAssociation(PALETTEPIN, NEWPINEVENT, "palettePin", "newPinEvent", false);
			}
			if (PALETTEPIN_NEWPINEVENT == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end newPinEvent of the class PalettePin.");
			}
			PALETTEFREEBOX_NEWFREEBOXEVENT = raapi.findAssociationEnd(PALETTEFREEBOX, "newFreeBoxEvent");
			if ((PALETTEFREEBOX_NEWFREEBOXEVENT == 0) && insertMetamodel) {
				PALETTEFREEBOX_NEWFREEBOXEVENT = raapi.createAssociation(PALETTEFREEBOX, NEWFREEBOXEVENT, "paletteFreeBox", "newFreeBoxEvent", false);
			}
			if (PALETTEFREEBOX_NEWFREEBOXEVENT == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end newFreeBoxEvent of the class PaletteFreeBox.");
			}
			PALETTEFREELINE_NEWFREELINEEVENT = raapi.findAssociationEnd(PALETTEFREELINE, "newFreeLineEvent");
			if ((PALETTEFREELINE_NEWFREELINEEVENT == 0) && insertMetamodel) {
				PALETTEFREELINE_NEWFREELINEEVENT = raapi.createAssociation(PALETTEFREELINE, NEWFREELINEEVENT, "paletteFreeLine", "newFreeLineEvent", false);
			}
			if (PALETTEFREELINE_NEWFREELINEEVENT == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end newFreeLineEvent of the class PaletteFreeLine.");
			}
			TOOLBAR_GRAPHDIAGRAM = raapi.findAssociationEnd(TOOLBAR, "graphDiagram");
			if ((TOOLBAR_GRAPHDIAGRAM == 0) && insertMetamodel) {
				TOOLBAR_GRAPHDIAGRAM = raapi.createAssociation(TOOLBAR, GRAPHDIAGRAM, "toolbar", "graphDiagram", false);
			}
			if (TOOLBAR_GRAPHDIAGRAM == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end graphDiagram of the class Toolbar.");
			}
			TOOLBAR_TOOLBARELEMENT = raapi.findAssociationEnd(TOOLBAR, "toolbarElement");
			if ((TOOLBAR_TOOLBARELEMENT == 0) && insertMetamodel) {
				TOOLBAR_TOOLBARELEMENT = raapi.createAssociation(TOOLBAR, TOOLBARELEMENT, "toolbar", "toolbarElement", true);
			}
			if (TOOLBAR_TOOLBARELEMENT == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end toolbarElement of the class Toolbar.");
			}
			TOOLBARELEMENT_CAPTION = raapi.findAttribute(TOOLBARELEMENT, "caption");
			if ((TOOLBARELEMENT_CAPTION == 0) && insertMetamodel)
				TOOLBARELEMENT_CAPTION = raapi.createAttribute(TOOLBARELEMENT, "caption", raapi.findPrimitiveDataType("String"));
			if (TOOLBARELEMENT_CAPTION == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute caption of the class ToolbarElement.");
			}
			TOOLBARELEMENT_PICTURE = raapi.findAttribute(TOOLBARELEMENT, "picture");
			if ((TOOLBARELEMENT_PICTURE == 0) && insertMetamodel)
				TOOLBARELEMENT_PICTURE = raapi.createAttribute(TOOLBARELEMENT, "picture", raapi.findPrimitiveDataType("String"));
			if (TOOLBARELEMENT_PICTURE == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute picture of the class ToolbarElement.");
			}
			TOOLBARELEMENT_PROCEDURENAME = raapi.findAttribute(TOOLBARELEMENT, "procedureName");
			if ((TOOLBARELEMENT_PROCEDURENAME == 0) && insertMetamodel)
				TOOLBARELEMENT_PROCEDURENAME = raapi.createAttribute(TOOLBARELEMENT, "procedureName", raapi.findPrimitiveDataType("String"));
			if (TOOLBARELEMENT_PROCEDURENAME == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute procedureName of the class ToolbarElement.");
			}
			TOOLBARELEMENT_TOOLBAR = raapi.findAssociationEnd(TOOLBARELEMENT, "toolbar");
			if ((TOOLBARELEMENT_TOOLBAR == 0) && insertMetamodel) {
				TOOLBARELEMENT_TOOLBAR = raapi.createAssociation(TOOLBARELEMENT, TOOLBAR, "toolbarElement", "toolbar", false);
			}
			if (TOOLBARELEMENT_TOOLBAR == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end toolbar of the class ToolbarElement.");
			}
			TOOLBARELEMENT_TOOLBARELEMENTSELECTEVENT = raapi.findAssociationEnd(TOOLBARELEMENT, "toolbarElementSelectEvent");
			if ((TOOLBARELEMENT_TOOLBARELEMENTSELECTEVENT == 0) && insertMetamodel) {
				TOOLBARELEMENT_TOOLBARELEMENTSELECTEVENT = raapi.createAssociation(TOOLBARELEMENT, TOOLBARELEMENTSELECTEVENT, "toolbarElement", "toolbarElementSelectEvent", false);
			}
			if (TOOLBARELEMENT_TOOLBARELEMENTSELECTEVENT == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end toolbarElementSelectEvent of the class ToolbarElement.");
			}
			CURRENTDGRPOINTER_GRAPHDIAGRAM = raapi.findAssociationEnd(CURRENTDGRPOINTER, "graphDiagram");
			if ((CURRENTDGRPOINTER_GRAPHDIAGRAM == 0) && insertMetamodel) {
				CURRENTDGRPOINTER_GRAPHDIAGRAM = raapi.createAssociation(CURRENTDGRPOINTER, GRAPHDIAGRAM, "currentDgrPointer", "graphDiagram", false);
			}
			if (CURRENTDGRPOINTER_GRAPHDIAGRAM == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end graphDiagram of the class CurrentDgrPointer.");
			}
			GRAPHDIAGRAM_CAPTION = raapi.findAttribute(GRAPHDIAGRAM, "caption");
			if ((GRAPHDIAGRAM_CAPTION == 0) && insertMetamodel)
				GRAPHDIAGRAM_CAPTION = raapi.createAttribute(GRAPHDIAGRAM, "caption", raapi.findPrimitiveDataType("String"));
			if (GRAPHDIAGRAM_CAPTION == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute caption of the class GraphDiagram.");
			}
			GRAPHDIAGRAM_STYLE = raapi.findAttribute(GRAPHDIAGRAM, "style");
			if ((GRAPHDIAGRAM_STYLE == 0) && insertMetamodel)
				GRAPHDIAGRAM_STYLE = raapi.createAttribute(GRAPHDIAGRAM, "style", raapi.findPrimitiveDataType("String"));
			if (GRAPHDIAGRAM_STYLE == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute style of the class GraphDiagram.");
			}
			GRAPHDIAGRAM_GRAPHDGRTYPE = raapi.findAttribute(GRAPHDIAGRAM, "graphDgrType");
			if ((GRAPHDIAGRAM_GRAPHDGRTYPE == 0) && insertMetamodel)
				GRAPHDIAGRAM_GRAPHDGRTYPE = raapi.createAttribute(GRAPHDIAGRAM, "graphDgrType", raapi.findPrimitiveDataType("String"));
			if (GRAPHDIAGRAM_GRAPHDGRTYPE == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute graphDgrType of the class GraphDiagram.");
			}
			GRAPHDIAGRAM_ISREADONLY = raapi.findAttribute(GRAPHDIAGRAM, "isReadOnly");
			if ((GRAPHDIAGRAM_ISREADONLY == 0) && insertMetamodel)
				GRAPHDIAGRAM_ISREADONLY = raapi.createAttribute(GRAPHDIAGRAM, "isReadOnly", raapi.findPrimitiveDataType("Boolean"));
			if (GRAPHDIAGRAM_ISREADONLY == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute isReadOnly of the class GraphDiagram.");
			}
			GRAPHDIAGRAM_REMOTEID = raapi.findAttribute(GRAPHDIAGRAM, "remoteId");
			if ((GRAPHDIAGRAM_REMOTEID == 0) && insertMetamodel)
				GRAPHDIAGRAM_REMOTEID = raapi.createAttribute(GRAPHDIAGRAM, "remoteId", raapi.findPrimitiveDataType("String"));
			if (GRAPHDIAGRAM_REMOTEID == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute remoteId of the class GraphDiagram.");
			}
			GRAPHDIAGRAM_TARGETDIAGRAMREMOTEID = raapi.findAttribute(GRAPHDIAGRAM, "targetDiagramRemoteId");
			if ((GRAPHDIAGRAM_TARGETDIAGRAMREMOTEID == 0) && insertMetamodel)
				GRAPHDIAGRAM_TARGETDIAGRAMREMOTEID = raapi.createAttribute(GRAPHDIAGRAM, "targetDiagramRemoteId", raapi.findPrimitiveDataType("String"));
			if (GRAPHDIAGRAM_TARGETDIAGRAMREMOTEID == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute targetDiagramRemoteId of the class GraphDiagram.");
			}
			GRAPHDIAGRAM_TREEERRORICON = raapi.findAttribute(GRAPHDIAGRAM, "treeErrorIcon");
			if ((GRAPHDIAGRAM_TREEERRORICON == 0) && insertMetamodel)
				GRAPHDIAGRAM_TREEERRORICON = raapi.createAttribute(GRAPHDIAGRAM, "treeErrorIcon", raapi.findPrimitiveDataType("String"));
			if (GRAPHDIAGRAM_TREEERRORICON == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute treeErrorIcon of the class GraphDiagram.");
			}
			GRAPHDIAGRAM_TREEMULTIUSERICON = raapi.findAttribute(GRAPHDIAGRAM, "treeMultiUserIcon");
			if ((GRAPHDIAGRAM_TREEMULTIUSERICON == 0) && insertMetamodel)
				GRAPHDIAGRAM_TREEMULTIUSERICON = raapi.createAttribute(GRAPHDIAGRAM, "treeMultiUserIcon", raapi.findPrimitiveDataType("String"));
			if (GRAPHDIAGRAM_TREEMULTIUSERICON == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute treeMultiUserIcon of the class GraphDiagram.");
			}
			GRAPHDIAGRAM_MULTICOMMENT = raapi.findAttribute(GRAPHDIAGRAM, "multiComment");
			if ((GRAPHDIAGRAM_MULTICOMMENT == 0) && insertMetamodel)
				GRAPHDIAGRAM_MULTICOMMENT = raapi.createAttribute(GRAPHDIAGRAM, "multiComment", raapi.findPrimitiveDataType("String"));
			if (GRAPHDIAGRAM_MULTICOMMENT == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute multiComment of the class GraphDiagram.");
			}
			GRAPHDIAGRAM_ISREADONLY2 = raapi.findAttribute(GRAPHDIAGRAM, "isReadOnly2");
			if ((GRAPHDIAGRAM_ISREADONLY2 == 0) && insertMetamodel)
				GRAPHDIAGRAM_ISREADONLY2 = raapi.createAttribute(GRAPHDIAGRAM, "isReadOnly2", raapi.findPrimitiveDataType("Boolean"));
			if (GRAPHDIAGRAM_ISREADONLY2 == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute isReadOnly2 of the class GraphDiagram.");
			}
			GRAPHDIAGRAM_BKGCOLOR = raapi.findAttribute(GRAPHDIAGRAM, "bkgColor");
			if ((GRAPHDIAGRAM_BKGCOLOR == 0) && insertMetamodel)
				GRAPHDIAGRAM_BKGCOLOR = raapi.createAttribute(GRAPHDIAGRAM, "bkgColor", raapi.findPrimitiveDataType("Integer"));
			if (GRAPHDIAGRAM_BKGCOLOR == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute bkgColor of the class GraphDiagram.");
			}
			GRAPHDIAGRAM_PRINTZOOM = raapi.findAttribute(GRAPHDIAGRAM, "printZoom");
			if ((GRAPHDIAGRAM_PRINTZOOM == 0) && insertMetamodel)
				GRAPHDIAGRAM_PRINTZOOM = raapi.createAttribute(GRAPHDIAGRAM, "printZoom", raapi.findPrimitiveDataType("Integer"));
			if (GRAPHDIAGRAM_PRINTZOOM == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute printZoom of the class GraphDiagram.");
			}
			GRAPHDIAGRAM_SCREENZOOM = raapi.findAttribute(GRAPHDIAGRAM, "screenZoom");
			if ((GRAPHDIAGRAM_SCREENZOOM == 0) && insertMetamodel)
				GRAPHDIAGRAM_SCREENZOOM = raapi.createAttribute(GRAPHDIAGRAM, "screenZoom", raapi.findPrimitiveDataType("Integer"));
			if (GRAPHDIAGRAM_SCREENZOOM == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute screenZoom of the class GraphDiagram.");
			}
			GRAPHDIAGRAM_LAYOUTMODE = raapi.findAttribute(GRAPHDIAGRAM, "layoutMode");
			if ((GRAPHDIAGRAM_LAYOUTMODE == 0) && insertMetamodel)
				GRAPHDIAGRAM_LAYOUTMODE = raapi.createAttribute(GRAPHDIAGRAM, "layoutMode", raapi.findPrimitiveDataType("Integer"));
			if (GRAPHDIAGRAM_LAYOUTMODE == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute layoutMode of the class GraphDiagram.");
			}
			GRAPHDIAGRAM_LAYOUTALGORITHM = raapi.findAttribute(GRAPHDIAGRAM, "layoutAlgorithm");
			if ((GRAPHDIAGRAM_LAYOUTALGORITHM == 0) && insertMetamodel)
				GRAPHDIAGRAM_LAYOUTALGORITHM = raapi.createAttribute(GRAPHDIAGRAM, "layoutAlgorithm", raapi.findPrimitiveDataType("Integer"));
			if (GRAPHDIAGRAM_LAYOUTALGORITHM == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute layoutAlgorithm of the class GraphDiagram.");
			}
			GRAPHDIAGRAM_PALETTE = raapi.findAssociationEnd(GRAPHDIAGRAM, "palette");
			if ((GRAPHDIAGRAM_PALETTE == 0) && insertMetamodel) {
				GRAPHDIAGRAM_PALETTE = raapi.createAssociation(GRAPHDIAGRAM, PALETTE, "graphDiagram", "palette", false);
			}
			if (GRAPHDIAGRAM_PALETTE == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end palette of the class GraphDiagram.");
			}
			GRAPHDIAGRAM_TOOLBAR = raapi.findAssociationEnd(GRAPHDIAGRAM, "toolbar");
			if ((GRAPHDIAGRAM_TOOLBAR == 0) && insertMetamodel) {
				GRAPHDIAGRAM_TOOLBAR = raapi.createAssociation(GRAPHDIAGRAM, TOOLBAR, "graphDiagram", "toolbar", false);
			}
			if (GRAPHDIAGRAM_TOOLBAR == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end toolbar of the class GraphDiagram.");
			}
			GRAPHDIAGRAM_GRAPHDIAGRAMSTYLE = raapi.findAssociationEnd(GRAPHDIAGRAM, "graphDiagramStyle");
			if ((GRAPHDIAGRAM_GRAPHDIAGRAMSTYLE == 0) && insertMetamodel) {
				GRAPHDIAGRAM_GRAPHDIAGRAMSTYLE = raapi.createAssociation(GRAPHDIAGRAM, GRAPHDIAGRAMSTYLE, "graphDiagram", "graphDiagramStyle", false);
			}
			if (GRAPHDIAGRAM_GRAPHDIAGRAMSTYLE == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end graphDiagramStyle of the class GraphDiagram.");
			}
			GRAPHDIAGRAM_GRAPHDIAGRAMTYPE = raapi.findAssociationEnd(GRAPHDIAGRAM, "graphDiagramType");
			if ((GRAPHDIAGRAM_GRAPHDIAGRAMTYPE == 0) && insertMetamodel) {
				GRAPHDIAGRAM_GRAPHDIAGRAMTYPE = raapi.createAssociation(GRAPHDIAGRAM, GRAPHDIAGRAMTYPE, "graphDiagram", "graphDiagramType", false);
			}
			if (GRAPHDIAGRAM_GRAPHDIAGRAMTYPE == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end graphDiagramType of the class GraphDiagram.");
			}
			GRAPHDIAGRAM_CURRENTDGRPOINTER = raapi.findAssociationEnd(GRAPHDIAGRAM, "currentDgrPointer");
			if ((GRAPHDIAGRAM_CURRENTDGRPOINTER == 0) && insertMetamodel) {
				GRAPHDIAGRAM_CURRENTDGRPOINTER = raapi.createAssociation(GRAPHDIAGRAM, CURRENTDGRPOINTER, "graphDiagram", "currentDgrPointer", false);
			}
			if (GRAPHDIAGRAM_CURRENTDGRPOINTER == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end currentDgrPointer of the class GraphDiagram.");
			}
			GRAPHDIAGRAM_COLLECTION = raapi.findAssociationEnd(GRAPHDIAGRAM, "collection");
			if ((GRAPHDIAGRAM_COLLECTION == 0) && insertMetamodel) {
				GRAPHDIAGRAM_COLLECTION = raapi.createAssociation(GRAPHDIAGRAM, COLLECTION, "graphDiagram", "collection", true);
			}
			if (GRAPHDIAGRAM_COLLECTION == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end collection of the class GraphDiagram.");
			}
			GRAPHDIAGRAM_ELEMENT = raapi.findAssociationEnd(GRAPHDIAGRAM, "element");
			if ((GRAPHDIAGRAM_ELEMENT == 0) && insertMetamodel) {
				GRAPHDIAGRAM_ELEMENT = raapi.createAssociation(GRAPHDIAGRAM, ELEMENT, "graphDiagram", "element", true);
			}
			if (GRAPHDIAGRAM_ELEMENT == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end element of the class GraphDiagram.");
			}
			GRAPHDIAGRAM_COMMAND = raapi.findAssociationEnd(GRAPHDIAGRAM, "command");
			if ((GRAPHDIAGRAM_COMMAND == 0) && insertMetamodel) {
				GRAPHDIAGRAM_COMMAND = raapi.createAssociation(GRAPHDIAGRAM, COMMAND, "graphDiagram", "command", false);
			}
			if (GRAPHDIAGRAM_COMMAND == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end command of the class GraphDiagram.");
			}
			GRAPHDIAGRAM_ACTIVATEDGREVENT = raapi.findAssociationEnd(GRAPHDIAGRAM, "activateDgrEvent");
			if ((GRAPHDIAGRAM_ACTIVATEDGREVENT == 0) && insertMetamodel) {
				GRAPHDIAGRAM_ACTIVATEDGREVENT = raapi.createAssociation(GRAPHDIAGRAM, ACTIVATEDGREVENT, "graphDiagram", "activateDgrEvent", false);
			}
			if (GRAPHDIAGRAM_ACTIVATEDGREVENT == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end activateDgrEvent of the class GraphDiagram.");
			}
			GRAPHDIAGRAM_SOURCE = raapi.findAssociationEnd(GRAPHDIAGRAM, "source");
			if ((GRAPHDIAGRAM_SOURCE == 0) && insertMetamodel) {
				GRAPHDIAGRAM_SOURCE = raapi.createAssociation(GRAPHDIAGRAM, ELEMENT, "target", "source", false);
			}
			if (GRAPHDIAGRAM_SOURCE == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end source of the class GraphDiagram.");
			}
			GRAPHDIAGRAM_PARENT = raapi.findAssociationEnd(GRAPHDIAGRAM, "parent");
			if ((GRAPHDIAGRAM_PARENT == 0) && insertMetamodel) {
				GRAPHDIAGRAM_PARENT = raapi.createAssociation(GRAPHDIAGRAM, ELEMENT, "child", "parent", false);
			}
			if (GRAPHDIAGRAM_PARENT == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end parent of the class GraphDiagram.");
			}
			GRAPHDIAGRAM_FRAME = raapi.findAssociationEnd(GRAPHDIAGRAM, "frame");
			if ((GRAPHDIAGRAM_FRAME == 0) && insertMetamodel) {
				GRAPHDIAGRAM_FRAME = raapi.createAssociation(GRAPHDIAGRAM, FRAME, "graphDiagram", "frame", false);
			}
			if (GRAPHDIAGRAM_FRAME == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end frame of the class GraphDiagram.");
			}
			COLLECTION_GRAPHDIAGRAM = raapi.findAssociationEnd(COLLECTION, "graphDiagram");
			if ((COLLECTION_GRAPHDIAGRAM == 0) && insertMetamodel) {
				COLLECTION_GRAPHDIAGRAM = raapi.createAssociation(COLLECTION, GRAPHDIAGRAM, "collection", "graphDiagram", false);
			}
			if (COLLECTION_GRAPHDIAGRAM == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end graphDiagram of the class Collection.");
			}
			COLLECTION_ELEMENT = raapi.findAssociationEnd(COLLECTION, "element");
			if ((COLLECTION_ELEMENT == 0) && insertMetamodel) {
				COLLECTION_ELEMENT = raapi.createAssociation(COLLECTION, ELEMENT, "collection", "element", true);
			}
			if (COLLECTION_ELEMENT == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end element of the class Collection.");
			}
			ELEMENT_STYLE = raapi.findAttribute(ELEMENT, "style");
			if ((ELEMENT_STYLE == 0) && insertMetamodel)
				ELEMENT_STYLE = raapi.createAttribute(ELEMENT, "style", raapi.findPrimitiveDataType("String"));
			if (ELEMENT_STYLE == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute style of the class Element.");
			}
			ELEMENT_LOCATION = raapi.findAttribute(ELEMENT, "location");
			if ((ELEMENT_LOCATION == 0) && insertMetamodel)
				ELEMENT_LOCATION = raapi.createAttribute(ELEMENT, "location", raapi.findPrimitiveDataType("String"));
			if (ELEMENT_LOCATION == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute location of the class Element.");
			}
			ELEMENT_GRAPHDIAGRAM = raapi.findAssociationEnd(ELEMENT, "graphDiagram");
			if ((ELEMENT_GRAPHDIAGRAM == 0) && insertMetamodel) {
				ELEMENT_GRAPHDIAGRAM = raapi.createAssociation(ELEMENT, GRAPHDIAGRAM, "element", "graphDiagram", false);
			}
			if (ELEMENT_GRAPHDIAGRAM == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end graphDiagram of the class Element.");
			}
			ELEMENT_COLLECTION = raapi.findAssociationEnd(ELEMENT, "collection");
			if ((ELEMENT_COLLECTION == 0) && insertMetamodel) {
				ELEMENT_COLLECTION = raapi.createAssociation(ELEMENT, COLLECTION, "element", "collection", false);
			}
			if (ELEMENT_COLLECTION == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end collection of the class Element.");
			}
			ELEMENT_ELEMSTYLE = raapi.findAssociationEnd(ELEMENT, "elemStyle");
			if ((ELEMENT_ELEMSTYLE == 0) && insertMetamodel) {
				ELEMENT_ELEMSTYLE = raapi.createAssociation(ELEMENT, ELEMSTYLE, "element", "elemStyle", false);
			}
			if (ELEMENT_ELEMSTYLE == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end elemStyle of the class Element.");
			}
			ELEMENT_OKCMD = raapi.findAssociationEnd(ELEMENT, "okCmd");
			if ((ELEMENT_OKCMD == 0) && insertMetamodel) {
				ELEMENT_OKCMD = raapi.createAssociation(ELEMENT, OKCMD, "element", "okCmd", false);
			}
			if (ELEMENT_OKCMD == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end okCmd of the class Element.");
			}
			ELEMENT_DEFAULTSTYLECMD = raapi.findAssociationEnd(ELEMENT, "defaultStyleCmd");
			if ((ELEMENT_DEFAULTSTYLECMD == 0) && insertMetamodel) {
				ELEMENT_DEFAULTSTYLECMD = raapi.createAssociation(ELEMENT, DEFAULTSTYLECMD, "element", "defaultStyleCmd", false);
			}
			if (ELEMENT_DEFAULTSTYLECMD == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end defaultStyleCmd of the class Element.");
			}
			ELEMENT_PASTECMD = raapi.findAssociationEnd(ELEMENT, "pasteCmd");
			if ((ELEMENT_PASTECMD == 0) && insertMetamodel) {
				ELEMENT_PASTECMD = raapi.createAssociation(ELEMENT, PASTECMD, "element", "pasteCmd", false);
			}
			if (ELEMENT_PASTECMD == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end pasteCmd of the class Element.");
			}
			ELEMENT_ACTIVEELEMENTCMD = raapi.findAssociationEnd(ELEMENT, "activeElementCmd");
			if ((ELEMENT_ACTIVEELEMENTCMD == 0) && insertMetamodel) {
				ELEMENT_ACTIVEELEMENTCMD = raapi.createAssociation(ELEMENT, ACTIVEELEMENTCMD, "element", "activeElementCmd", false);
			}
			if (ELEMENT_ACTIVEELEMENTCMD == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end activeElementCmd of the class Element.");
			}
			ELEMENT_STYLEDIALOGCMD = raapi.findAssociationEnd(ELEMENT, "styleDialogCmd");
			if ((ELEMENT_STYLEDIALOGCMD == 0) && insertMetamodel) {
				ELEMENT_STYLEDIALOGCMD = raapi.createAssociation(ELEMENT, STYLEDIALOGCMD, "element", "styleDialogCmd", false);
			}
			if (ELEMENT_STYLEDIALOGCMD == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end styleDialogCmd of the class Element.");
			}
			ELEMENT_REROUTECMD = raapi.findAssociationEnd(ELEMENT, "rerouteCmd");
			if ((ELEMENT_REROUTECMD == 0) && insertMetamodel) {
				ELEMENT_REROUTECMD = raapi.createAssociation(ELEMENT, REROUTECMD, "element", "rerouteCmd", false);
			}
			if (ELEMENT_REROUTECMD == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end rerouteCmd of the class Element.");
			}
			ELEMENT_ALIGNSIZECMD = raapi.findAssociationEnd(ELEMENT, "alignSizeCmd");
			if ((ELEMENT_ALIGNSIZECMD == 0) && insertMetamodel) {
				ELEMENT_ALIGNSIZECMD = raapi.createAssociation(ELEMENT, ALIGNSIZECMD, "element", "alignSizeCmd", false);
			}
			if (ELEMENT_ALIGNSIZECMD == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end alignSizeCmd of the class Element.");
			}
			ELEMENT_L2CLICKEVENT = raapi.findAssociationEnd(ELEMENT, "l2ClickEvent");
			if ((ELEMENT_L2CLICKEVENT == 0) && insertMetamodel) {
				ELEMENT_L2CLICKEVENT = raapi.createAssociation(ELEMENT, L2CLICKEVENT, "element", "l2ClickEvent", false);
			}
			if (ELEMENT_L2CLICKEVENT == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end l2ClickEvent of the class Element.");
			}
			ELEMENT_LCLICKEVENT = raapi.findAssociationEnd(ELEMENT, "lClickEvent");
			if ((ELEMENT_LCLICKEVENT == 0) && insertMetamodel) {
				ELEMENT_LCLICKEVENT = raapi.createAssociation(ELEMENT, LCLICKEVENT, "element", "lClickEvent", false);
			}
			if (ELEMENT_LCLICKEVENT == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end lClickEvent of the class Element.");
			}
			ELEMENT_RCLICKEVENT = raapi.findAssociationEnd(ELEMENT, "rClickEvent");
			if ((ELEMENT_RCLICKEVENT == 0) && insertMetamodel) {
				ELEMENT_RCLICKEVENT = raapi.createAssociation(ELEMENT, RCLICKEVENT, "element", "rClickEvent", false);
			}
			if (ELEMENT_RCLICKEVENT == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end rClickEvent of the class Element.");
			}
			ELEMENT_NEWLINEEVENTS = raapi.findAssociationEnd(ELEMENT, "newLineEventS");
			if ((ELEMENT_NEWLINEEVENTS == 0) && insertMetamodel) {
				ELEMENT_NEWLINEEVENTS = raapi.createAssociation(ELEMENT, NEWLINEEVENT, "start", "newLineEventS", false);
			}
			if (ELEMENT_NEWLINEEVENTS == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end newLineEventS of the class Element.");
			}
			ELEMENT_NEWLINEEVENTE = raapi.findAssociationEnd(ELEMENT, "newLineEventE");
			if ((ELEMENT_NEWLINEEVENTE == 0) && insertMetamodel) {
				ELEMENT_NEWLINEEVENTE = raapi.createAssociation(ELEMENT, NEWLINEEVENT, "end", "newLineEventE", false);
			}
			if (ELEMENT_NEWLINEEVENTE == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end newLineEventE of the class Element.");
			}
			ELEMENT_MOVELINESTARTPOINTEVENTT = raapi.findAssociationEnd(ELEMENT, "moveLineStartPointEventT");
			if ((ELEMENT_MOVELINESTARTPOINTEVENTT == 0) && insertMetamodel) {
				ELEMENT_MOVELINESTARTPOINTEVENTT = raapi.createAssociation(ELEMENT, MOVELINESTARTPOINTEVENT, "target", "moveLineStartPointEventT", false);
			}
			if (ELEMENT_MOVELINESTARTPOINTEVENTT == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end moveLineStartPointEventT of the class Element.");
			}
			ELEMENT_MOVELINEENDPOINTEVENTT = raapi.findAssociationEnd(ELEMENT, "moveLineEndPointEventT");
			if ((ELEMENT_MOVELINEENDPOINTEVENTT == 0) && insertMetamodel) {
				ELEMENT_MOVELINEENDPOINTEVENTT = raapi.createAssociation(ELEMENT, MOVELINEENDPOINTEVENT, "target", "moveLineEndPointEventT", false);
			}
			if (ELEMENT_MOVELINEENDPOINTEVENTT == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end moveLineEndPointEventT of the class Element.");
			}
			ELEMENT_FREEBOXEDITEDEVENT = raapi.findAssociationEnd(ELEMENT, "freeBoxEditedEvent");
			if ((ELEMENT_FREEBOXEDITEDEVENT == 0) && insertMetamodel) {
				ELEMENT_FREEBOXEDITEDEVENT = raapi.createAssociation(ELEMENT, FREEBOXEDITEDEVENT, "element", "freeBoxEditedEvent", false);
			}
			if (ELEMENT_FREEBOXEDITEDEVENT == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end freeBoxEditedEvent of the class Element.");
			}
			ELEMENT_FREELINEEDITEDEVENT = raapi.findAssociationEnd(ELEMENT, "freeLineEditedEvent");
			if ((ELEMENT_FREELINEEDITEDEVENT == 0) && insertMetamodel) {
				ELEMENT_FREELINEEDITEDEVENT = raapi.createAssociation(ELEMENT, FREELINEEDITEDEVENT, "element", "freeLineEditedEvent", false);
			}
			if (ELEMENT_FREELINEEDITEDEVENT == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end freeLineEditedEvent of the class Element.");
			}
			ELEMENT_COMPARTMENT = raapi.findAssociationEnd(ELEMENT, "compartment");
			if ((ELEMENT_COMPARTMENT == 0) && insertMetamodel) {
				ELEMENT_COMPARTMENT = raapi.createAssociation(ELEMENT, COMPARTMENT, "element", "compartment", true);
			}
			if (ELEMENT_COMPARTMENT == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end compartment of the class Element.");
			}
			ELEMENT_ESTART = raapi.findAssociationEnd(ELEMENT, "eStart");
			if ((ELEMENT_ESTART == 0) && insertMetamodel) {
				ELEMENT_ESTART = raapi.createAssociation(ELEMENT, EDGE, "start", "eStart", false);
			}
			if (ELEMENT_ESTART == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end eStart of the class Element.");
			}
			ELEMENT_EEND = raapi.findAssociationEnd(ELEMENT, "eEnd");
			if ((ELEMENT_EEND == 0) && insertMetamodel) {
				ELEMENT_EEND = raapi.createAssociation(ELEMENT, EDGE, "end", "eEnd", false);
			}
			if (ELEMENT_EEND == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end eEnd of the class Element.");
			}
			ELEMENT_TARGET = raapi.findAssociationEnd(ELEMENT, "target");
			if ((ELEMENT_TARGET == 0) && insertMetamodel) {
				ELEMENT_TARGET = raapi.createAssociation(ELEMENT, GRAPHDIAGRAM, "source", "target", false);
			}
			if (ELEMENT_TARGET == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end target of the class Element.");
			}
			ELEMENT_CHILD = raapi.findAssociationEnd(ELEMENT, "child");
			if ((ELEMENT_CHILD == 0) && insertMetamodel) {
				ELEMENT_CHILD = raapi.createAssociation(ELEMENT, GRAPHDIAGRAM, "parent", "child", false);
			}
			if (ELEMENT_CHILD == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end child of the class Element.");
			}
			ELEMENT_UPDATESTYLECMD = raapi.findAssociationEnd(ELEMENT, "updateStyleCmd");
			if ((ELEMENT_UPDATESTYLECMD == 0) && insertMetamodel) {
				ELEMENT_UPDATESTYLECMD = raapi.createAssociation(ELEMENT, UPDATESTYLECMD, "element", "updateStyleCmd", false);
			}
			if (ELEMENT_UPDATESTYLECMD == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end updateStyleCmd of the class Element.");
			}
			NODE_COMPONENT = raapi.findAssociationEnd(NODE, "component");
			if ((NODE_COMPONENT == 0) && insertMetamodel) {
				NODE_COMPONENT = raapi.createAssociation(NODE, NODE, "container", "component", true);
			}
			if (NODE_COMPONENT == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end component of the class Node.");
			}
			NODE_CONTAINER = raapi.findAssociationEnd(NODE, "container");
			if ((NODE_CONTAINER == 0) && insertMetamodel) {
				NODE_CONTAINER = raapi.createAssociation(NODE, NODE, "component", "container", false);
			}
			if (NODE_CONTAINER == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end container of the class Node.");
			}
			NODE_PORT = raapi.findAssociationEnd(NODE, "port");
			if ((NODE_PORT == 0) && insertMetamodel) {
				NODE_PORT = raapi.createAssociation(NODE, PORT, "node", "port", true);
			}
			if (NODE_PORT == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end port of the class Node.");
			}
			NODE_NEWBOXEVENT = raapi.findAssociationEnd(NODE, "newBoxEvent");
			if ((NODE_NEWBOXEVENT == 0) && insertMetamodel) {
				NODE_NEWBOXEVENT = raapi.createAssociation(NODE, NEWBOXEVENT, "node", "newBoxEvent", false);
			}
			if (NODE_NEWBOXEVENT == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end newBoxEvent of the class Node.");
			}
			NODE_CHANGEPARENTEVENTN = raapi.findAssociationEnd(NODE, "changeParentEventN");
			if ((NODE_CHANGEPARENTEVENTN == 0) && insertMetamodel) {
				NODE_CHANGEPARENTEVENTN = raapi.createAssociation(NODE, CHANGEPARENTEVENT, "node", "changeParentEventN", false);
			}
			if (NODE_CHANGEPARENTEVENTN == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end changeParentEventN of the class Node.");
			}
			NODE_CHANGEPARENTEVENTT = raapi.findAssociationEnd(NODE, "changeParentEventT");
			if ((NODE_CHANGEPARENTEVENTT == 0) && insertMetamodel) {
				NODE_CHANGEPARENTEVENTT = raapi.createAssociation(NODE, CHANGEPARENTEVENT, "target", "changeParentEventT", false);
			}
			if (NODE_CHANGEPARENTEVENTT == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end changeParentEventT of the class Node.");
			}
			NODE_NEWPINEVENT = raapi.findAssociationEnd(NODE, "newPinEvent");
			if ((NODE_NEWPINEVENT == 0) && insertMetamodel) {
				NODE_NEWPINEVENT = raapi.createAssociation(NODE, NEWPINEVENT, "node", "newPinEvent", false);
			}
			if (NODE_NEWPINEVENT == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end newPinEvent of the class Node.");
			}
			EDGE_MOVELINESTARTPOINTEVENTE = raapi.findAssociationEnd(EDGE, "moveLineStartPointEventE");
			if ((EDGE_MOVELINESTARTPOINTEVENTE == 0) && insertMetamodel) {
				EDGE_MOVELINESTARTPOINTEVENTE = raapi.createAssociation(EDGE, MOVELINESTARTPOINTEVENT, "edge", "moveLineStartPointEventE", false);
			}
			if (EDGE_MOVELINESTARTPOINTEVENTE == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end moveLineStartPointEventE of the class Edge.");
			}
			EDGE_MOVELINEENDPOINTEVENTE = raapi.findAssociationEnd(EDGE, "moveLineEndPointEventE");
			if ((EDGE_MOVELINEENDPOINTEVENTE == 0) && insertMetamodel) {
				EDGE_MOVELINEENDPOINTEVENTE = raapi.createAssociation(EDGE, MOVELINEENDPOINTEVENT, "edge", "moveLineEndPointEventE", false);
			}
			if (EDGE_MOVELINEENDPOINTEVENTE == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end moveLineEndPointEventE of the class Edge.");
			}
			EDGE_START = raapi.findAssociationEnd(EDGE, "start");
			if ((EDGE_START == 0) && insertMetamodel) {
				EDGE_START = raapi.createAssociation(EDGE, ELEMENT, "eStart", "start", false);
			}
			if (EDGE_START == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end start of the class Edge.");
			}
			EDGE_END = raapi.findAssociationEnd(EDGE, "end");
			if ((EDGE_END == 0) && insertMetamodel) {
				EDGE_END = raapi.createAssociation(EDGE, ELEMENT, "eEnd", "end", false);
			}
			if (EDGE_END == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end end of the class Edge.");
			}
			PORT_NODE = raapi.findAssociationEnd(PORT, "node");
			if ((PORT_NODE == 0) && insertMetamodel) {
				PORT_NODE = raapi.createAssociation(PORT, NODE, "port", "node", false);
			}
			if (PORT_NODE == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end node of the class Port.");
			}
			FREEBOX_FREEBOX_X = raapi.findAttribute(FREEBOX, "freeBox_x");
			if ((FREEBOX_FREEBOX_X == 0) && insertMetamodel)
				FREEBOX_FREEBOX_X = raapi.createAttribute(FREEBOX, "freeBox_x", raapi.findPrimitiveDataType("Integer"));
			if (FREEBOX_FREEBOX_X == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute freeBox_x of the class FreeBox.");
			}
			FREEBOX_FREEBOX_Y = raapi.findAttribute(FREEBOX, "freeBox_y");
			if ((FREEBOX_FREEBOX_Y == 0) && insertMetamodel)
				FREEBOX_FREEBOX_Y = raapi.createAttribute(FREEBOX, "freeBox_y", raapi.findPrimitiveDataType("Integer"));
			if (FREEBOX_FREEBOX_Y == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute freeBox_y of the class FreeBox.");
			}
			FREEBOX_FREEBOX_W = raapi.findAttribute(FREEBOX, "freeBox_w");
			if ((FREEBOX_FREEBOX_W == 0) && insertMetamodel)
				FREEBOX_FREEBOX_W = raapi.createAttribute(FREEBOX, "freeBox_w", raapi.findPrimitiveDataType("Integer"));
			if (FREEBOX_FREEBOX_W == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute freeBox_w of the class FreeBox.");
			}
			FREEBOX_FREEBOX_H = raapi.findAttribute(FREEBOX, "freeBox_h");
			if ((FREEBOX_FREEBOX_H == 0) && insertMetamodel)
				FREEBOX_FREEBOX_H = raapi.createAttribute(FREEBOX, "freeBox_h", raapi.findPrimitiveDataType("Integer"));
			if (FREEBOX_FREEBOX_H == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute freeBox_h of the class FreeBox.");
			}
			FREEBOX_FREEBOX_Z = raapi.findAttribute(FREEBOX, "freeBox_z");
			if ((FREEBOX_FREEBOX_Z == 0) && insertMetamodel)
				FREEBOX_FREEBOX_Z = raapi.createAttribute(FREEBOX, "freeBox_z", raapi.findPrimitiveDataType("Integer"));
			if (FREEBOX_FREEBOX_Z == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute freeBox_z of the class FreeBox.");
			}
			FREELINE_FREELINE_X1 = raapi.findAttribute(FREELINE, "freeLine_x1");
			if ((FREELINE_FREELINE_X1 == 0) && insertMetamodel)
				FREELINE_FREELINE_X1 = raapi.createAttribute(FREELINE, "freeLine_x1", raapi.findPrimitiveDataType("Integer"));
			if (FREELINE_FREELINE_X1 == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute freeLine_x1 of the class FreeLine.");
			}
			FREELINE_FREELINE_Y1 = raapi.findAttribute(FREELINE, "freeLine_y1");
			if ((FREELINE_FREELINE_Y1 == 0) && insertMetamodel)
				FREELINE_FREELINE_Y1 = raapi.createAttribute(FREELINE, "freeLine_y1", raapi.findPrimitiveDataType("Integer"));
			if (FREELINE_FREELINE_Y1 == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute freeLine_y1 of the class FreeLine.");
			}
			FREELINE_FREELINE_XN = raapi.findAttribute(FREELINE, "freeLine_xn");
			if ((FREELINE_FREELINE_XN == 0) && insertMetamodel)
				FREELINE_FREELINE_XN = raapi.createAttribute(FREELINE, "freeLine_xn", raapi.findPrimitiveDataType("Integer"));
			if (FREELINE_FREELINE_XN == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute freeLine_xn of the class FreeLine.");
			}
			FREELINE_FREELINE_YN = raapi.findAttribute(FREELINE, "freeLine_yn");
			if ((FREELINE_FREELINE_YN == 0) && insertMetamodel)
				FREELINE_FREELINE_YN = raapi.createAttribute(FREELINE, "freeLine_yn", raapi.findPrimitiveDataType("Integer"));
			if (FREELINE_FREELINE_YN == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute freeLine_yn of the class FreeLine.");
			}
			FREELINE_FREELINE_Z = raapi.findAttribute(FREELINE, "freeLine_z");
			if ((FREELINE_FREELINE_Z == 0) && insertMetamodel)
				FREELINE_FREELINE_Z = raapi.createAttribute(FREELINE, "freeLine_z", raapi.findPrimitiveDataType("Integer"));
			if (FREELINE_FREELINE_Z == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute freeLine_z of the class FreeLine.");
			}
			COMPARTMENT_INPUT = raapi.findAttribute(COMPARTMENT, "input");
			if ((COMPARTMENT_INPUT == 0) && insertMetamodel)
				COMPARTMENT_INPUT = raapi.createAttribute(COMPARTMENT, "input", raapi.findPrimitiveDataType("String"));
			if (COMPARTMENT_INPUT == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute input of the class Compartment.");
			}
			COMPARTMENT_STYLE = raapi.findAttribute(COMPARTMENT, "style");
			if ((COMPARTMENT_STYLE == 0) && insertMetamodel)
				COMPARTMENT_STYLE = raapi.createAttribute(COMPARTMENT, "style", raapi.findPrimitiveDataType("String"));
			if (COMPARTMENT_STYLE == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute style of the class Compartment.");
			}
			COMPARTMENT_VALUE = raapi.findAttribute(COMPARTMENT, "value");
			if ((COMPARTMENT_VALUE == 0) && insertMetamodel)
				COMPARTMENT_VALUE = raapi.createAttribute(COMPARTMENT, "value", raapi.findPrimitiveDataType("String"));
			if (COMPARTMENT_VALUE == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute value of the class Compartment.");
			}
			COMPARTMENT_ISGROUP = raapi.findAttribute(COMPARTMENT, "isGroup");
			if ((COMPARTMENT_ISGROUP == 0) && insertMetamodel)
				COMPARTMENT_ISGROUP = raapi.createAttribute(COMPARTMENT, "isGroup", raapi.findPrimitiveDataType("String"));
			if (COMPARTMENT_ISGROUP == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute isGroup of the class Compartment.");
			}
			COMPARTMENT_COMPARTSTYLE = raapi.findAssociationEnd(COMPARTMENT, "compartStyle");
			if ((COMPARTMENT_COMPARTSTYLE == 0) && insertMetamodel) {
				COMPARTMENT_COMPARTSTYLE = raapi.createAssociation(COMPARTMENT, COMPARTSTYLE, "compartment", "compartStyle", false);
			}
			if (COMPARTMENT_COMPARTSTYLE == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end compartStyle of the class Compartment.");
			}
			COMPARTMENT_ELEMENT = raapi.findAssociationEnd(COMPARTMENT, "element");
			if ((COMPARTMENT_ELEMENT == 0) && insertMetamodel) {
				COMPARTMENT_ELEMENT = raapi.createAssociation(COMPARTMENT, ELEMENT, "compartment", "element", false);
			}
			if (COMPARTMENT_ELEMENT == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end element of the class Compartment.");
			}
			COMPARTMENT_PARENTCOMPARTMENT = raapi.findAssociationEnd(COMPARTMENT, "parentCompartment");
			if ((COMPARTMENT_PARENTCOMPARTMENT == 0) && insertMetamodel) {
				COMPARTMENT_PARENTCOMPARTMENT = raapi.createAssociation(COMPARTMENT, COMPARTMENT, "subCompartment", "parentCompartment", false);
			}
			if (COMPARTMENT_PARENTCOMPARTMENT == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end parentCompartment of the class Compartment.");
			}
			COMPARTMENT_SUBCOMPARTMENT = raapi.findAssociationEnd(COMPARTMENT, "subCompartment");
			if ((COMPARTMENT_SUBCOMPARTMENT == 0) && insertMetamodel) {
				COMPARTMENT_SUBCOMPARTMENT = raapi.createAssociation(COMPARTMENT, COMPARTMENT, "parentCompartment", "subCompartment", false);
			}
			if (COMPARTMENT_SUBCOMPARTMENT == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end subCompartment of the class Compartment.");
			}
			FRAME_CAPTION = raapi.findAttribute(FRAME, "caption");
			if ((FRAME_CAPTION == 0) && insertMetamodel)
				FRAME_CAPTION = raapi.createAttribute(FRAME, "caption", raapi.findPrimitiveDataType("String"));
			if (FRAME_CAPTION == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute caption of the class Frame.");
			}
			FRAME_CONTENTURI = raapi.findAttribute(FRAME, "contentURI");
			if ((FRAME_CONTENTURI == 0) && insertMetamodel)
				FRAME_CONTENTURI = raapi.createAttribute(FRAME, "contentURI", raapi.findPrimitiveDataType("String"));
			if (FRAME_CONTENTURI == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute contentURI of the class Frame.");
			}
			FRAME_LOCATION = raapi.findAttribute(FRAME, "location");
			if ((FRAME_LOCATION == 0) && insertMetamodel)
				FRAME_LOCATION = raapi.createAttribute(FRAME, "location", raapi.findPrimitiveDataType("String"));
			if (FRAME_LOCATION == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute location of the class Frame.");
			}
			FRAME_ISRESIZEABLE = raapi.findAttribute(FRAME, "isResizeable");
			if ((FRAME_ISRESIZEABLE == 0) && insertMetamodel)
				FRAME_ISRESIZEABLE = raapi.createAttribute(FRAME, "isResizeable", raapi.findPrimitiveDataType("Boolean"));
			if (FRAME_ISRESIZEABLE == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute isResizeable of the class Frame.");
			}
			FRAME_ISCLOSABLE = raapi.findAttribute(FRAME, "isClosable");
			if ((FRAME_ISCLOSABLE == 0) && insertMetamodel)
				FRAME_ISCLOSABLE = raapi.createAttribute(FRAME, "isClosable", raapi.findPrimitiveDataType("Boolean"));
			if (FRAME_ISCLOSABLE == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute isClosable of the class Frame.");
			}
			FRAME_ONFRAMEACTIVATEDEVENT = raapi.findAttribute(FRAME, "onFrameActivatedEvent");
			if ((FRAME_ONFRAMEACTIVATEDEVENT == 0) && insertMetamodel)
				FRAME_ONFRAMEACTIVATEDEVENT = raapi.createAttribute(FRAME, "onFrameActivatedEvent", raapi.findPrimitiveDataType("String"));
			if (FRAME_ONFRAMEACTIVATEDEVENT == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute onFrameActivatedEvent of the class Frame.");
			}
			FRAME_ONFRAMEDEACTIVATINGEVENT = raapi.findAttribute(FRAME, "onFrameDeactivatingEvent");
			if ((FRAME_ONFRAMEDEACTIVATINGEVENT == 0) && insertMetamodel)
				FRAME_ONFRAMEDEACTIVATINGEVENT = raapi.createAttribute(FRAME, "onFrameDeactivatingEvent", raapi.findPrimitiveDataType("String"));
			if (FRAME_ONFRAMEDEACTIVATINGEVENT == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute onFrameDeactivatingEvent of the class Frame.");
			}
			FRAME_ONFRAMERESIZEDEVENT = raapi.findAttribute(FRAME, "onFrameResizedEvent");
			if ((FRAME_ONFRAMERESIZEDEVENT == 0) && insertMetamodel)
				FRAME_ONFRAMERESIZEDEVENT = raapi.createAttribute(FRAME, "onFrameResizedEvent", raapi.findPrimitiveDataType("String"));
			if (FRAME_ONFRAMERESIZEDEVENT == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute onFrameResizedEvent of the class Frame.");
			}
			FRAME_ONCLOSEFRAMEREQUESTEDEVENT = raapi.findAttribute(FRAME, "onCloseFrameRequestedEvent");
			if ((FRAME_ONCLOSEFRAMEREQUESTEDEVENT == 0) && insertMetamodel)
				FRAME_ONCLOSEFRAMEREQUESTEDEVENT = raapi.createAttribute(FRAME, "onCloseFrameRequestedEvent", raapi.findPrimitiveDataType("String"));
			if (FRAME_ONCLOSEFRAMEREQUESTEDEVENT == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute onCloseFrameRequestedEvent of the class Frame.");
			}
			FRAME_GRAPHDIAGRAM = raapi.findAssociationEnd(FRAME, "graphDiagram");
			if ((FRAME_GRAPHDIAGRAM == 0) && insertMetamodel) {
				FRAME_GRAPHDIAGRAM = raapi.createAssociation(FRAME, GRAPHDIAGRAM, "frame", "graphDiagram", false);
			}
			if (FRAME_GRAPHDIAGRAM == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end graphDiagram of the class Frame.");
			}
		}
	}

	public AsyncCommand createAsyncCommand()
	{
		AsyncCommand retVal = new AsyncCommand(this);
		wrappers.put(retVal.getRAAPIReference(), retVal);
		return retVal; 
	}
  
	public Command createCommand()
	{
		Command retVal = new Command(this);
		wrappers.put(retVal.getRAAPIReference(), retVal);
		return retVal; 
	}
  
	public PresentationElement createPresentationElement()
	{
		PresentationElement retVal = new PresentationElement(this);
		wrappers.put(retVal.getRAAPIReference(), retVal);
		return retVal; 
	}
  
	public GraphDiagramType createGraphDiagramType()
	{
		GraphDiagramType retVal = new GraphDiagramType(this);
		wrappers.put(retVal.getRAAPIReference(), retVal);
		return retVal; 
	}
  
	public GraphDiagramEngine createGraphDiagramEngine()
	{
		GraphDiagramEngine retVal = new GraphDiagramEngine(this);
		wrappers.put(retVal.getRAAPIReference(), retVal);
		return retVal; 
	}
  
	public Engine createEngine()
	{
		Engine retVal = new Engine(this);
		wrappers.put(retVal.getRAAPIReference(), retVal);
		return retVal; 
	}
  
	public OkCmd createOkCmd()
	{
		OkCmd retVal = new OkCmd(this);
		wrappers.put(retVal.getRAAPIReference(), retVal);
		return retVal; 
	}
  
	public PopUpCmd createPopUpCmd()
	{
		PopUpCmd retVal = new PopUpCmd(this);
		wrappers.put(retVal.getRAAPIReference(), retVal);
		return retVal; 
	}
  
	public ActiveDgrCmd createActiveDgrCmd()
	{
		ActiveDgrCmd retVal = new ActiveDgrCmd(this);
		wrappers.put(retVal.getRAAPIReference(), retVal);
		return retVal; 
	}
  
	public ActiveDgrViewCmd createActiveDgrViewCmd()
	{
		ActiveDgrViewCmd retVal = new ActiveDgrViewCmd(this);
		wrappers.put(retVal.getRAAPIReference(), retVal);
		return retVal; 
	}
  
	public PasteCmd createPasteCmd()
	{
		PasteCmd retVal = new PasteCmd(this);
		wrappers.put(retVal.getRAAPIReference(), retVal);
		return retVal; 
	}
  
	public UpdateDgrCmd createUpdateDgrCmd()
	{
		UpdateDgrCmd retVal = new UpdateDgrCmd(this);
		wrappers.put(retVal.getRAAPIReference(), retVal);
		return retVal; 
	}
  
	public CloseDgrCmd createCloseDgrCmd()
	{
		CloseDgrCmd retVal = new CloseDgrCmd(this);
		wrappers.put(retVal.getRAAPIReference(), retVal);
		return retVal; 
	}
  
	public SaveDgrCmd createSaveDgrCmd()
	{
		SaveDgrCmd retVal = new SaveDgrCmd(this);
		wrappers.put(retVal.getRAAPIReference(), retVal);
		return retVal; 
	}
  
	public ActiveElementCmd createActiveElementCmd()
	{
		ActiveElementCmd retVal = new ActiveElementCmd(this);
		wrappers.put(retVal.getRAAPIReference(), retVal);
		return retVal; 
	}
  
	public AfterConfigCmd createAfterConfigCmd()
	{
		AfterConfigCmd retVal = new AfterConfigCmd(this);
		wrappers.put(retVal.getRAAPIReference(), retVal);
		return retVal; 
	}
  
	public SaveStylesCmd createSaveStylesCmd()
	{
		SaveStylesCmd retVal = new SaveStylesCmd(this);
		wrappers.put(retVal.getRAAPIReference(), retVal);
		return retVal; 
	}
  
	public StyleDialogCmd createStyleDialogCmd()
	{
		StyleDialogCmd retVal = new StyleDialogCmd(this);
		wrappers.put(retVal.getRAAPIReference(), retVal);
		return retVal; 
	}
  
	public DefaultStyleCmd createDefaultStyleCmd()
	{
		DefaultStyleCmd retVal = new DefaultStyleCmd(this);
		wrappers.put(retVal.getRAAPIReference(), retVal);
		return retVal; 
	}
  
	public RefreshDgrCmd createRefreshDgrCmd()
	{
		RefreshDgrCmd retVal = new RefreshDgrCmd(this);
		wrappers.put(retVal.getRAAPIReference(), retVal);
		return retVal; 
	}
  
	public UpdateStyleCmd createUpdateStyleCmd()
	{
		UpdateStyleCmd retVal = new UpdateStyleCmd(this);
		wrappers.put(retVal.getRAAPIReference(), retVal);
		return retVal; 
	}
  
	public RerouteCmd createRerouteCmd()
	{
		RerouteCmd retVal = new RerouteCmd(this);
		wrappers.put(retVal.getRAAPIReference(), retVal);
		return retVal; 
	}
  
	public AlignSizeCmd createAlignSizeCmd()
	{
		AlignSizeCmd retVal = new AlignSizeCmd(this);
		wrappers.put(retVal.getRAAPIReference(), retVal);
		return retVal; 
	}
  
	public PopUpElemSelectEvent createPopUpElemSelectEvent()
	{
		PopUpElemSelectEvent retVal = new PopUpElemSelectEvent(this);
		wrappers.put(retVal.getRAAPIReference(), retVal);
		return retVal; 
	}
  
	public PasteGraphClipboardEvent createPasteGraphClipboardEvent()
	{
		PasteGraphClipboardEvent retVal = new PasteGraphClipboardEvent(this);
		wrappers.put(retVal.getRAAPIReference(), retVal);
		return retVal; 
	}
  
	public DeleteCollectionEvent createDeleteCollectionEvent()
	{
		DeleteCollectionEvent retVal = new DeleteCollectionEvent(this);
		wrappers.put(retVal.getRAAPIReference(), retVal);
		return retVal; 
	}
  
	public CopyCutCollectionEvent createCopyCutCollectionEvent()
	{
		CopyCutCollectionEvent retVal = new CopyCutCollectionEvent(this);
		wrappers.put(retVal.getRAAPIReference(), retVal);
		return retVal; 
	}
  
	public CopyCollectionEvent createCopyCollectionEvent()
	{
		CopyCollectionEvent retVal = new CopyCollectionEvent(this);
		wrappers.put(retVal.getRAAPIReference(), retVal);
		return retVal; 
	}
  
	public MoveLineStartPointEvent createMoveLineStartPointEvent()
	{
		MoveLineStartPointEvent retVal = new MoveLineStartPointEvent(this);
		wrappers.put(retVal.getRAAPIReference(), retVal);
		return retVal; 
	}
  
	public MoveLineEndPointEvent createMoveLineEndPointEvent()
	{
		MoveLineEndPointEvent retVal = new MoveLineEndPointEvent(this);
		wrappers.put(retVal.getRAAPIReference(), retVal);
		return retVal; 
	}
  
	public L2ClickEvent createL2ClickEvent()
	{
		L2ClickEvent retVal = new L2ClickEvent(this);
		wrappers.put(retVal.getRAAPIReference(), retVal);
		return retVal; 
	}
  
	public LClickEvent createLClickEvent()
	{
		LClickEvent retVal = new LClickEvent(this);
		wrappers.put(retVal.getRAAPIReference(), retVal);
		return retVal; 
	}
  
	public RClickEvent createRClickEvent()
	{
		RClickEvent retVal = new RClickEvent(this);
		wrappers.put(retVal.getRAAPIReference(), retVal);
		return retVal; 
	}
  
	public NewLineEvent createNewLineEvent()
	{
		NewLineEvent retVal = new NewLineEvent(this);
		wrappers.put(retVal.getRAAPIReference(), retVal);
		return retVal; 
	}
  
	public NewBoxEvent createNewBoxEvent()
	{
		NewBoxEvent retVal = new NewBoxEvent(this);
		wrappers.put(retVal.getRAAPIReference(), retVal);
		return retVal; 
	}
  
	public ExecTransfEvent createExecTransfEvent()
	{
		ExecTransfEvent retVal = new ExecTransfEvent(this);
		wrappers.put(retVal.getRAAPIReference(), retVal);
		return retVal; 
	}
  
	public NewPinEvent createNewPinEvent()
	{
		NewPinEvent retVal = new NewPinEvent(this);
		wrappers.put(retVal.getRAAPIReference(), retVal);
		return retVal; 
	}
  
	public ChangeParentEvent createChangeParentEvent()
	{
		ChangeParentEvent retVal = new ChangeParentEvent(this);
		wrappers.put(retVal.getRAAPIReference(), retVal);
		return retVal; 
	}
  
	public ActivateDgrEvent createActivateDgrEvent()
	{
		ActivateDgrEvent retVal = new ActivateDgrEvent(this);
		wrappers.put(retVal.getRAAPIReference(), retVal);
		return retVal; 
	}
  
	public CloseDgrEvent createCloseDgrEvent()
	{
		CloseDgrEvent retVal = new CloseDgrEvent(this);
		wrappers.put(retVal.getRAAPIReference(), retVal);
		return retVal; 
	}
  
	public OKStyleDialogEvent createOKStyleDialogEvent()
	{
		OKStyleDialogEvent retVal = new OKStyleDialogEvent(this);
		wrappers.put(retVal.getRAAPIReference(), retVal);
		return retVal; 
	}
  
	public KeyDownEvent createKeyDownEvent()
	{
		KeyDownEvent retVal = new KeyDownEvent(this);
		wrappers.put(retVal.getRAAPIReference(), retVal);
		return retVal; 
	}
  
	public NewFreeBoxEvent createNewFreeBoxEvent()
	{
		NewFreeBoxEvent retVal = new NewFreeBoxEvent(this);
		wrappers.put(retVal.getRAAPIReference(), retVal);
		return retVal; 
	}
  
	public NewFreeLineEvent createNewFreeLineEvent()
	{
		NewFreeLineEvent retVal = new NewFreeLineEvent(this);
		wrappers.put(retVal.getRAAPIReference(), retVal);
		return retVal; 
	}
  
	public FreeBoxEditedEvent createFreeBoxEditedEvent()
	{
		FreeBoxEditedEvent retVal = new FreeBoxEditedEvent(this);
		wrappers.put(retVal.getRAAPIReference(), retVal);
		return retVal; 
	}
  
	public FreeLineEditedEvent createFreeLineEditedEvent()
	{
		FreeLineEditedEvent retVal = new FreeLineEditedEvent(this);
		wrappers.put(retVal.getRAAPIReference(), retVal);
		return retVal; 
	}
  
	public ToolbarElementSelectEvent createToolbarElementSelectEvent()
	{
		ToolbarElementSelectEvent retVal = new ToolbarElementSelectEvent(this);
		wrappers.put(retVal.getRAAPIReference(), retVal);
		return retVal; 
	}
  
	public Style createStyle()
	{
		Style retVal = new Style(this);
		wrappers.put(retVal.getRAAPIReference(), retVal);
		return retVal; 
	}
  
	public GraphDiagramStyle createGraphDiagramStyle()
	{
		GraphDiagramStyle retVal = new GraphDiagramStyle(this);
		wrappers.put(retVal.getRAAPIReference(), retVal);
		return retVal; 
	}
  
	public ElemStyle createElemStyle()
	{
		ElemStyle retVal = new ElemStyle(this);
		wrappers.put(retVal.getRAAPIReference(), retVal);
		return retVal; 
	}
  
	public NodeStyle createNodeStyle()
	{
		NodeStyle retVal = new NodeStyle(this);
		wrappers.put(retVal.getRAAPIReference(), retVal);
		return retVal; 
	}
  
	public EdgeStyle createEdgeStyle()
	{
		EdgeStyle retVal = new EdgeStyle(this);
		wrappers.put(retVal.getRAAPIReference(), retVal);
		return retVal; 
	}
  
	public PortStyle createPortStyle()
	{
		PortStyle retVal = new PortStyle(this);
		wrappers.put(retVal.getRAAPIReference(), retVal);
		return retVal; 
	}
  
	public FreeBoxStyle createFreeBoxStyle()
	{
		FreeBoxStyle retVal = new FreeBoxStyle(this);
		wrappers.put(retVal.getRAAPIReference(), retVal);
		return retVal; 
	}
  
	public FreeLineStyle createFreeLineStyle()
	{
		FreeLineStyle retVal = new FreeLineStyle(this);
		wrappers.put(retVal.getRAAPIReference(), retVal);
		return retVal; 
	}
  
	public CompartStyle createCompartStyle()
	{
		CompartStyle retVal = new CompartStyle(this);
		wrappers.put(retVal.getRAAPIReference(), retVal);
		return retVal; 
	}
  
	public PopUpDiagram createPopUpDiagram()
	{
		PopUpDiagram retVal = new PopUpDiagram(this);
		wrappers.put(retVal.getRAAPIReference(), retVal);
		return retVal; 
	}
  
	public PopUpElement createPopUpElement()
	{
		PopUpElement retVal = new PopUpElement(this);
		wrappers.put(retVal.getRAAPIReference(), retVal);
		return retVal; 
	}
  
	public Palette createPalette()
	{
		Palette retVal = new Palette(this);
		wrappers.put(retVal.getRAAPIReference(), retVal);
		return retVal; 
	}
  
	public PaletteElement createPaletteElement()
	{
		PaletteElement retVal = new PaletteElement(this);
		wrappers.put(retVal.getRAAPIReference(), retVal);
		return retVal; 
	}
  
	public PaletteBox createPaletteBox()
	{
		PaletteBox retVal = new PaletteBox(this);
		wrappers.put(retVal.getRAAPIReference(), retVal);
		return retVal; 
	}
  
	public PaletteLine createPaletteLine()
	{
		PaletteLine retVal = new PaletteLine(this);
		wrappers.put(retVal.getRAAPIReference(), retVal);
		return retVal; 
	}
  
	public PalettePin createPalettePin()
	{
		PalettePin retVal = new PalettePin(this);
		wrappers.put(retVal.getRAAPIReference(), retVal);
		return retVal; 
	}
  
	public PaletteFreeBox createPaletteFreeBox()
	{
		PaletteFreeBox retVal = new PaletteFreeBox(this);
		wrappers.put(retVal.getRAAPIReference(), retVal);
		return retVal; 
	}
  
	public PaletteFreeLine createPaletteFreeLine()
	{
		PaletteFreeLine retVal = new PaletteFreeLine(this);
		wrappers.put(retVal.getRAAPIReference(), retVal);
		return retVal; 
	}
  
	public Toolbar createToolbar()
	{
		Toolbar retVal = new Toolbar(this);
		wrappers.put(retVal.getRAAPIReference(), retVal);
		return retVal; 
	}
  
	public ToolbarElement createToolbarElement()
	{
		ToolbarElement retVal = new ToolbarElement(this);
		wrappers.put(retVal.getRAAPIReference(), retVal);
		return retVal; 
	}
  
	public CurrentDgrPointer createCurrentDgrPointer()
	{
		CurrentDgrPointer retVal = new CurrentDgrPointer(this);
		wrappers.put(retVal.getRAAPIReference(), retVal);
		return retVal; 
	}
  
	public GraphDiagram createGraphDiagram()
	{
		GraphDiagram retVal = new GraphDiagram(this);
		wrappers.put(retVal.getRAAPIReference(), retVal);
		return retVal; 
	}
  
	public Collection createCollection()
	{
		Collection retVal = new Collection(this);
		wrappers.put(retVal.getRAAPIReference(), retVal);
		return retVal; 
	}
  
	public Element createElement()
	{
		Element retVal = new Element(this);
		wrappers.put(retVal.getRAAPIReference(), retVal);
		return retVal; 
	}
  
	public Node createNode()
	{
		Node retVal = new Node(this);
		wrappers.put(retVal.getRAAPIReference(), retVal);
		return retVal; 
	}
  
	public Edge createEdge()
	{
		Edge retVal = new Edge(this);
		wrappers.put(retVal.getRAAPIReference(), retVal);
		return retVal; 
	}
  
	public Port createPort()
	{
		Port retVal = new Port(this);
		wrappers.put(retVal.getRAAPIReference(), retVal);
		return retVal; 
	}
  
	public FreeBox createFreeBox()
	{
		FreeBox retVal = new FreeBox(this);
		wrappers.put(retVal.getRAAPIReference(), retVal);
		return retVal; 
	}
  
	public FreeLine createFreeLine()
	{
		FreeLine retVal = new FreeLine(this);
		wrappers.put(retVal.getRAAPIReference(), retVal);
		return retVal; 
	}
  
	public Compartment createCompartment()
	{
		Compartment retVal = new Compartment(this);
		wrappers.put(retVal.getRAAPIReference(), retVal);
		return retVal; 
	}
  
	public Event createEvent()
	{
		Event retVal = new Event(this);
		wrappers.put(retVal.getRAAPIReference(), retVal);
		return retVal; 
	}
  
	public Frame createFrame()
	{
		Frame retVal = new Frame(this);
		wrappers.put(retVal.getRAAPIReference(), retVal);
		return retVal; 
	}
  
}
