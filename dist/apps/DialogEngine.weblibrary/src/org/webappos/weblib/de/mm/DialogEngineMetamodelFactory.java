// automatically generated
package org.webappos.weblib.de.mm;
import lv.lumii.tda.raapi.RAAPI;
import java.util.*;

public class DialogEngineMetamodelFactory
{
	// for compatibility with ECore
	public static DialogEngineMetamodelFactory eINSTANCE = new DialogEngineMetamodelFactory();

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
			java.lang.reflect.Constructor<? extends RAAPIReferenceWrapper> c = cls1.getConstructor(DialogEngineMetamodelFactory.class, Long.TYPE, Boolean.TYPE);
			return (RAAPIReferenceWrapper)c.newInstance(this, rObject, takeReference);
		} catch (Throwable t1) {
			try {
				java.lang.reflect.Constructor<? extends RAAPIReferenceWrapper> c = cls.getConstructor(DialogEngineMetamodelFactory.class, Long.TYPE, Boolean.TYPE);
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

		if (raapi.isKindOf(rObject, D_SHARP_GROUP)) {
			if ((rCurClass == 0) || (raapi.isDerivedClass(D_SHARP_GROUP,rCurClass))) {
				retVal = D_SHARP_Group.class;
				rCurClass = D_SHARP_GROUP;
			}
		}
		if (raapi.isKindOf(rObject, D_SHARP_EVENTSOURCE)) {
			if ((rCurClass == 0) || (raapi.isDerivedClass(D_SHARP_EVENTSOURCE,rCurClass))) {
				retVal = D_SHARP_EventSource.class;
				rCurClass = D_SHARP_EVENTSOURCE;
			}
		}
		if (raapi.isKindOf(rObject, D_SHARP_COMPONENT)) {
			if ((rCurClass == 0) || (raapi.isDerivedClass(D_SHARP_COMPONENT,rCurClass))) {
				retVal = D_SHARP_Component.class;
				rCurClass = D_SHARP_COMPONENT;
			}
		}
		if (raapi.isKindOf(rObject, D_SHARP_CONTAINER)) {
			if ((rCurClass == 0) || (raapi.isDerivedClass(D_SHARP_CONTAINER,rCurClass))) {
				retVal = D_SHARP_Container.class;
				rCurClass = D_SHARP_CONTAINER;
			}
		}
		if (raapi.isKindOf(rObject, D_SHARP_ROW)) {
			if ((rCurClass == 0) || (raapi.isDerivedClass(D_SHARP_ROW,rCurClass))) {
				retVal = D_SHARP_Row.class;
				rCurClass = D_SHARP_ROW;
			}
		}
		if (raapi.isKindOf(rObject, D_SHARP_COLUMN)) {
			if ((rCurClass == 0) || (raapi.isDerivedClass(D_SHARP_COLUMN,rCurClass))) {
				retVal = D_SHARP_Column.class;
				rCurClass = D_SHARP_COLUMN;
			}
		}
		if (raapi.isKindOf(rObject, D_SHARP_VERTICALBOX)) {
			if ((rCurClass == 0) || (raapi.isDerivedClass(D_SHARP_VERTICALBOX,rCurClass))) {
				retVal = D_SHARP_VerticalBox.class;
				rCurClass = D_SHARP_VERTICALBOX;
			}
		}
		if (raapi.isKindOf(rObject, D_SHARP_GROUPBOX)) {
			if ((rCurClass == 0) || (raapi.isDerivedClass(D_SHARP_GROUPBOX,rCurClass))) {
				retVal = D_SHARP_GroupBox.class;
				rCurClass = D_SHARP_GROUPBOX;
			}
		}
		if (raapi.isKindOf(rObject, D_SHARP_FORM)) {
			if ((rCurClass == 0) || (raapi.isDerivedClass(D_SHARP_FORM,rCurClass))) {
				retVal = D_SHARP_Form.class;
				rCurClass = D_SHARP_FORM;
			}
		}
		if (raapi.isKindOf(rObject, D_SHARP_TABLEDIAGRAM)) {
			if ((rCurClass == 0) || (raapi.isDerivedClass(D_SHARP_TABLEDIAGRAM,rCurClass))) {
				retVal = D_SHARP_TableDiagram.class;
				rCurClass = D_SHARP_TABLEDIAGRAM;
			}
		}
		if (raapi.isKindOf(rObject, D_SHARP_HORIZONTALBOX)) {
			if ((rCurClass == 0) || (raapi.isDerivedClass(D_SHARP_HORIZONTALBOX,rCurClass))) {
				retVal = D_SHARP_HorizontalBox.class;
				rCurClass = D_SHARP_HORIZONTALBOX;
			}
		}
		if (raapi.isKindOf(rObject, D_SHARP_HORIZONTALSCROLLBOX)) {
			if ((rCurClass == 0) || (raapi.isDerivedClass(D_SHARP_HORIZONTALSCROLLBOX,rCurClass))) {
				retVal = D_SHARP_HorizontalScrollBox.class;
				rCurClass = D_SHARP_HORIZONTALSCROLLBOX;
			}
		}
		if (raapi.isKindOf(rObject, D_SHARP_VERTICALSCROLLBOX)) {
			if ((rCurClass == 0) || (raapi.isDerivedClass(D_SHARP_VERTICALSCROLLBOX,rCurClass))) {
				retVal = D_SHARP_VerticalScrollBox.class;
				rCurClass = D_SHARP_VERTICALSCROLLBOX;
			}
		}
		if (raapi.isKindOf(rObject, D_SHARP_SCROLLBOX)) {
			if ((rCurClass == 0) || (raapi.isDerivedClass(D_SHARP_SCROLLBOX,rCurClass))) {
				retVal = D_SHARP_ScrollBox.class;
				rCurClass = D_SHARP_SCROLLBOX;
			}
		}
		if (raapi.isKindOf(rObject, D_SHARP_TAB)) {
			if ((rCurClass == 0) || (raapi.isDerivedClass(D_SHARP_TAB,rCurClass))) {
				retVal = D_SHARP_Tab.class;
				rCurClass = D_SHARP_TAB;
			}
		}
		if (raapi.isKindOf(rObject, D_SHARP_STACK)) {
			if ((rCurClass == 0) || (raapi.isDerivedClass(D_SHARP_STACK,rCurClass))) {
				retVal = D_SHARP_Stack.class;
				rCurClass = D_SHARP_STACK;
			}
		}
		if (raapi.isKindOf(rObject, D_SHARP_TABCONTAINER)) {
			if ((rCurClass == 0) || (raapi.isDerivedClass(D_SHARP_TABCONTAINER,rCurClass))) {
				retVal = D_SHARP_TabContainer.class;
				rCurClass = D_SHARP_TABCONTAINER;
			}
		}
		if (raapi.isKindOf(rObject, D_SHARP_VERTICALSCROLLBOXWRAPPER)) {
			if ((rCurClass == 0) || (raapi.isDerivedClass(D_SHARP_VERTICALSCROLLBOXWRAPPER,rCurClass))) {
				retVal = D_SHARP_VerticalScrollBoxWrapper.class;
				rCurClass = D_SHARP_VERTICALSCROLLBOXWRAPPER;
			}
		}
		if (raapi.isKindOf(rObject, D_SHARP_HORIZONTALSCROLLBOXWRAPPER)) {
			if ((rCurClass == 0) || (raapi.isDerivedClass(D_SHARP_HORIZONTALSCROLLBOXWRAPPER,rCurClass))) {
				retVal = D_SHARP_HorizontalScrollBoxWrapper.class;
				rCurClass = D_SHARP_HORIZONTALSCROLLBOXWRAPPER;
			}
		}
		if (raapi.isKindOf(rObject, D_SHARP_VERTICALSPLITBOX)) {
			if ((rCurClass == 0) || (raapi.isDerivedClass(D_SHARP_VERTICALSPLITBOX,rCurClass))) {
				retVal = D_SHARP_VerticalSplitBox.class;
				rCurClass = D_SHARP_VERTICALSPLITBOX;
			}
		}
		if (raapi.isKindOf(rObject, D_SHARP_HORIZONTALSPLITBOX)) {
			if ((rCurClass == 0) || (raapi.isDerivedClass(D_SHARP_HORIZONTALSPLITBOX,rCurClass))) {
				retVal = D_SHARP_HorizontalSplitBox.class;
				rCurClass = D_SHARP_HORIZONTALSPLITBOX;
			}
		}
		if (raapi.isKindOf(rObject, D_SHARP_LABEL)) {
			if ((rCurClass == 0) || (raapi.isDerivedClass(D_SHARP_LABEL,rCurClass))) {
				retVal = D_SHARP_Label.class;
				rCurClass = D_SHARP_LABEL;
			}
		}
		if (raapi.isKindOf(rObject, D_SHARP_RADIOBUTTON)) {
			if ((rCurClass == 0) || (raapi.isDerivedClass(D_SHARP_RADIOBUTTON,rCurClass))) {
				retVal = D_SHARP_RadioButton.class;
				rCurClass = D_SHARP_RADIOBUTTON;
			}
		}
		if (raapi.isKindOf(rObject, D_SHARP_LISTBOX)) {
			if ((rCurClass == 0) || (raapi.isDerivedClass(D_SHARP_LISTBOX,rCurClass))) {
				retVal = D_SHARP_ListBox.class;
				rCurClass = D_SHARP_LISTBOX;
			}
		}
		if (raapi.isKindOf(rObject, D_SHARP_VTABLETYPE)) {
			if ((rCurClass == 0) || (raapi.isDerivedClass(D_SHARP_VTABLETYPE,rCurClass))) {
				retVal = D_SHARP_VTableType.class;
				rCurClass = D_SHARP_VTABLETYPE;
			}
		}
		if (raapi.isKindOf(rObject, D_SHARP_VTABLE)) {
			if ((rCurClass == 0) || (raapi.isDerivedClass(D_SHARP_VTABLE,rCurClass))) {
				retVal = D_SHARP_VTable.class;
				rCurClass = D_SHARP_VTABLE;
			}
		}
		if (raapi.isKindOf(rObject, D_SHARP_VTABLEROW)) {
			if ((rCurClass == 0) || (raapi.isDerivedClass(D_SHARP_VTABLEROW,rCurClass))) {
				retVal = D_SHARP_VTableRow.class;
				rCurClass = D_SHARP_VTABLEROW;
			}
		}
		if (raapi.isKindOf(rObject, D_SHARP_TABLECOMPONENT)) {
			if ((rCurClass == 0) || (raapi.isDerivedClass(D_SHARP_TABLECOMPONENT,rCurClass))) {
				retVal = D_SHARP_TableComponent.class;
				rCurClass = D_SHARP_TABLECOMPONENT;
			}
		}
		if (raapi.isKindOf(rObject, D_SHARP_BUTTON)) {
			if ((rCurClass == 0) || (raapi.isDerivedClass(D_SHARP_BUTTON,rCurClass))) {
				retVal = D_SHARP_Button.class;
				rCurClass = D_SHARP_BUTTON;
			}
		}
		if (raapi.isKindOf(rObject, D_SHARP_CHECKBOX)) {
			if ((rCurClass == 0) || (raapi.isDerivedClass(D_SHARP_CHECKBOX,rCurClass))) {
				retVal = D_SHARP_CheckBox.class;
				rCurClass = D_SHARP_CHECKBOX;
			}
		}
		if (raapi.isKindOf(rObject, D_SHARP_COMBOBOX)) {
			if ((rCurClass == 0) || (raapi.isDerivedClass(D_SHARP_COMBOBOX,rCurClass))) {
				retVal = D_SHARP_ComboBox.class;
				rCurClass = D_SHARP_COMBOBOX;
			}
		}
		if (raapi.isKindOf(rObject, D_SHARP_ITEM)) {
			if ((rCurClass == 0) || (raapi.isDerivedClass(D_SHARP_ITEM,rCurClass))) {
				retVal = D_SHARP_Item.class;
				rCurClass = D_SHARP_ITEM;
			}
		}
		if (raapi.isKindOf(rObject, D_SHARP_IMAGE)) {
			if ((rCurClass == 0) || (raapi.isDerivedClass(D_SHARP_IMAGE,rCurClass))) {
				retVal = D_SHARP_Image.class;
				rCurClass = D_SHARP_IMAGE;
			}
		}
		if (raapi.isKindOf(rObject, D_SHARP_TEXTBOX)) {
			if ((rCurClass == 0) || (raapi.isDerivedClass(D_SHARP_TEXTBOX,rCurClass))) {
				retVal = D_SHARP_TextBox.class;
				rCurClass = D_SHARP_TEXTBOX;
			}
		}
		if (raapi.isKindOf(rObject, D_SHARP_INPUTFIELD)) {
			if ((rCurClass == 0) || (raapi.isDerivedClass(D_SHARP_INPUTFIELD,rCurClass))) {
				retVal = D_SHARP_InputField.class;
				rCurClass = D_SHARP_INPUTFIELD;
			}
		}
		if (raapi.isKindOf(rObject, D_SHARP_TEXTAREA)) {
			if ((rCurClass == 0) || (raapi.isDerivedClass(D_SHARP_TEXTAREA,rCurClass))) {
				retVal = D_SHARP_TextArea.class;
				rCurClass = D_SHARP_TEXTAREA;
			}
		}
		if (raapi.isKindOf(rObject, D_SHARP_TREE)) {
			if ((rCurClass == 0) || (raapi.isDerivedClass(D_SHARP_TREE,rCurClass))) {
				retVal = D_SHARP_Tree.class;
				rCurClass = D_SHARP_TREE;
			}
		}
		if (raapi.isKindOf(rObject, D_SHARP_MULTILINETEXTBOX)) {
			if ((rCurClass == 0) || (raapi.isDerivedClass(D_SHARP_MULTILINETEXTBOX,rCurClass))) {
				retVal = D_SHARP_MultiLineTextBox.class;
				rCurClass = D_SHARP_MULTILINETEXTBOX;
			}
		}
		if (raapi.isKindOf(rObject, D_SHARP_RICHTEXTAREA)) {
			if ((rCurClass == 0) || (raapi.isDerivedClass(D_SHARP_RICHTEXTAREA,rCurClass))) {
				retVal = D_SHARP_RichTextArea.class;
				rCurClass = D_SHARP_RICHTEXTAREA;
			}
		}
		if (raapi.isKindOf(rObject, D_SHARP_PROGRESSBAR)) {
			if ((rCurClass == 0) || (raapi.isDerivedClass(D_SHARP_PROGRESSBAR,rCurClass))) {
				retVal = D_SHARP_ProgressBar.class;
				rCurClass = D_SHARP_PROGRESSBAR;
			}
		}
		if (raapi.isKindOf(rObject, D_SHARP_IMAGEBUTTON)) {
			if ((rCurClass == 0) || (raapi.isDerivedClass(D_SHARP_IMAGEBUTTON,rCurClass))) {
				retVal = D_SHARP_ImageButton.class;
				rCurClass = D_SHARP_IMAGEBUTTON;
			}
		}
		if (raapi.isKindOf(rObject, D_SHARP_VTABLECOLUMNTYPE)) {
			if ((rCurClass == 0) || (raapi.isDerivedClass(D_SHARP_VTABLECOLUMNTYPE,rCurClass))) {
				retVal = D_SHARP_VTableColumnType.class;
				rCurClass = D_SHARP_VTABLECOLUMNTYPE;
			}
		}
		if (raapi.isKindOf(rObject, D_SHARP_VTABLECELL)) {
			if ((rCurClass == 0) || (raapi.isDerivedClass(D_SHARP_VTABLECELL,rCurClass))) {
				retVal = D_SHARP_VTableCell.class;
				rCurClass = D_SHARP_VTABLECELL;
			}
		}
		if (raapi.isKindOf(rObject, D_SHARP_EVENTHANDLER)) {
			if ((rCurClass == 0) || (raapi.isDerivedClass(D_SHARP_EVENTHANDLER,rCurClass))) {
				retVal = D_SHARP_EventHandler.class;
				rCurClass = D_SHARP_EVENTHANDLER;
			}
		}
		if (raapi.isKindOf(rObject, D_SHARP_COMMAND)) {
			if ((rCurClass == 0) || (raapi.isDerivedClass(D_SHARP_COMMAND,rCurClass))) {
				retVal = D_SHARP_Command.class;
				rCurClass = D_SHARP_COMMAND;
			}
		}
		if (raapi.isKindOf(rObject, D_SHARP_EVENT)) {
			if ((rCurClass == 0) || (raapi.isDerivedClass(D_SHARP_EVENT,rCurClass))) {
				retVal = D_SHARP_Event.class;
				rCurClass = D_SHARP_EVENT;
			}
		}
		if (raapi.isKindOf(rObject, D_SHARP_ROWMOVEDEVENT)) {
			if ((rCurClass == 0) || (raapi.isDerivedClass(D_SHARP_ROWMOVEDEVENT,rCurClass))) {
				retVal = D_SHARP_RowMovedEvent.class;
				rCurClass = D_SHARP_ROWMOVEDEVENT;
			}
		}
		if (raapi.isKindOf(rObject, D_SHARP_COLUMNMOVEDEVENT)) {
			if ((rCurClass == 0) || (raapi.isDerivedClass(D_SHARP_COLUMNMOVEDEVENT,rCurClass))) {
				retVal = D_SHARP_ColumnMovedEvent.class;
				rCurClass = D_SHARP_COLUMNMOVEDEVENT;
			}
		}
		if (raapi.isKindOf(rObject, D_SHARP_MULTILINETEXTBOXCHANGEEVENT)) {
			if ((rCurClass == 0) || (raapi.isDerivedClass(D_SHARP_MULTILINETEXTBOXCHANGEEVENT,rCurClass))) {
				retVal = D_SHARP_MultiLineTextBoxChangeEvent.class;
				rCurClass = D_SHARP_MULTILINETEXTBOXCHANGEEVENT;
			}
		}
		if (raapi.isKindOf(rObject, D_SHARP_TEXTLINE)) {
			if ((rCurClass == 0) || (raapi.isDerivedClass(D_SHARP_TEXTLINE,rCurClass))) {
				retVal = D_SHARP_TextLine.class;
				rCurClass = D_SHARP_TEXTLINE;
			}
		}
		if (raapi.isKindOf(rObject, D_SHARP_TREENODESELECTEVENT)) {
			if ((rCurClass == 0) || (raapi.isDerivedClass(D_SHARP_TREENODESELECTEVENT,rCurClass))) {
				retVal = D_SHARP_TreeNodeSelectEvent.class;
				rCurClass = D_SHARP_TREENODESELECTEVENT;
			}
		}
		if (raapi.isKindOf(rObject, D_SHARP_ADDTREENODECMD)) {
			if ((rCurClass == 0) || (raapi.isDerivedClass(D_SHARP_ADDTREENODECMD,rCurClass))) {
				retVal = D_SHARP_AddTreeNodeCmd.class;
				rCurClass = D_SHARP_ADDTREENODECMD;
			}
		}
		if (raapi.isKindOf(rObject, D_SHARP_DELETETREENODECMD)) {
			if ((rCurClass == 0) || (raapi.isDerivedClass(D_SHARP_DELETETREENODECMD,rCurClass))) {
				retVal = D_SHARP_DeleteTreeNodeCmd.class;
				rCurClass = D_SHARP_DELETETREENODECMD;
			}
		}
		if (raapi.isKindOf(rObject, D_SHARP_SELECTTREENODECMD)) {
			if ((rCurClass == 0) || (raapi.isDerivedClass(D_SHARP_SELECTTREENODECMD,rCurClass))) {
				retVal = D_SHARP_SelectTreeNodeCmd.class;
				rCurClass = D_SHARP_SELECTTREENODECMD;
			}
		}
		if (raapi.isKindOf(rObject, D_SHARP_EXPANDTREENODECMD)) {
			if ((rCurClass == 0) || (raapi.isDerivedClass(D_SHARP_EXPANDTREENODECMD,rCurClass))) {
				retVal = D_SHARP_ExpandTreeNodeCmd.class;
				rCurClass = D_SHARP_EXPANDTREENODECMD;
			}
		}
		if (raapi.isKindOf(rObject, D_SHARP_COLLAPSETREENODECMD)) {
			if ((rCurClass == 0) || (raapi.isDerivedClass(D_SHARP_COLLAPSETREENODECMD,rCurClass))) {
				retVal = D_SHARP_CollapseTreeNodeCmd.class;
				rCurClass = D_SHARP_COLLAPSETREENODECMD;
			}
		}
		if (raapi.isKindOf(rObject, D_SHARP_TREENODE)) {
			if ((rCurClass == 0) || (raapi.isDerivedClass(D_SHARP_TREENODE,rCurClass))) {
				retVal = D_SHARP_TreeNode.class;
				rCurClass = D_SHARP_TREENODE;
			}
		}
		if (raapi.isKindOf(rObject, D_SHARP_LISTBOXCHANGEEVENT)) {
			if ((rCurClass == 0) || (raapi.isDerivedClass(D_SHARP_LISTBOXCHANGEEVENT,rCurClass))) {
				retVal = D_SHARP_ListBoxChangeEvent.class;
				rCurClass = D_SHARP_LISTBOXCHANGEEVENT;
			}
		}
		if (raapi.isKindOf(rObject, D_SHARP_TABCHANGEEVENT)) {
			if ((rCurClass == 0) || (raapi.isDerivedClass(D_SHARP_TABCHANGEEVENT,rCurClass))) {
				retVal = D_SHARP_TabChangeEvent.class;
				rCurClass = D_SHARP_TABCHANGEEVENT;
			}
		}
		if (raapi.isKindOf(rObject, D_SHARP_TREENODEMOVEEVENT)) {
			if ((rCurClass == 0) || (raapi.isDerivedClass(D_SHARP_TREENODEMOVEEVENT,rCurClass))) {
				retVal = D_SHARP_TreeNodeMoveEvent.class;
				rCurClass = D_SHARP_TREENODEMOVEEVENT;
			}
		}
		if (raapi.isKindOf(rObject, D_SHARP_COPYTOCLIPBOARDCMD)) {
			if ((rCurClass == 0) || (raapi.isDerivedClass(D_SHARP_COPYTOCLIPBOARDCMD,rCurClass))) {
				retVal = D_SHARP_CopyToClipboardCmd.class;
				rCurClass = D_SHARP_COPYTOCLIPBOARDCMD;
			}
		}
		if (raapi.isKindOf(rObject, D_SHARP_KEYDOWNEVENT)) {
			if ((rCurClass == 0) || (raapi.isDerivedClass(D_SHARP_KEYDOWNEVENT,rCurClass))) {
				retVal = D_SHARP_KeyDownEvent.class;
				rCurClass = D_SHARP_KEYDOWNEVENT;
			}
		}
		if (raapi.isKindOf(rObject, D_SHARP_TREENODEEXPANDEDEVENT)) {
			if ((rCurClass == 0) || (raapi.isDerivedClass(D_SHARP_TREENODEEXPANDEDEVENT,rCurClass))) {
				retVal = D_SHARP_TreeNodeExpandedEvent.class;
				rCurClass = D_SHARP_TREENODEEXPANDEDEVENT;
			}
		}
		if (raapi.isKindOf(rObject, D_SHARP_TREENODECOLLAPSEDEVENT)) {
			if ((rCurClass == 0) || (raapi.isDerivedClass(D_SHARP_TREENODECOLLAPSEDEVENT,rCurClass))) {
				retVal = D_SHARP_TreeNodeCollapsedEvent.class;
				rCurClass = D_SHARP_TREENODECOLLAPSEDEVENT;
			}
		}
		if (raapi.isKindOf(rObject, COMMAND)) {
			if ((rCurClass == 0) || (raapi.isDerivedClass(COMMAND,rCurClass))) {
				retVal = Command.class;
				rCurClass = COMMAND;
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
		if (rClass == D_SHARP_GROUP)
			w = new D_SHARP_Group(this, rObject, takeReference);
		if (rClass == D_SHARP_EVENTSOURCE)
			w = new D_SHARP_EventSource(this, rObject, takeReference);
		if (rClass == D_SHARP_COMPONENT)
			w = new D_SHARP_Component(this, rObject, takeReference);
		if (rClass == D_SHARP_CONTAINER)
			w = new D_SHARP_Container(this, rObject, takeReference);
		if (rClass == D_SHARP_ROW)
			w = new D_SHARP_Row(this, rObject, takeReference);
		if (rClass == D_SHARP_COLUMN)
			w = new D_SHARP_Column(this, rObject, takeReference);
		if (rClass == D_SHARP_VERTICALBOX)
			w = new D_SHARP_VerticalBox(this, rObject, takeReference);
		if (rClass == D_SHARP_GROUPBOX)
			w = new D_SHARP_GroupBox(this, rObject, takeReference);
		if (rClass == D_SHARP_FORM)
			w = new D_SHARP_Form(this, rObject, takeReference);
		if (rClass == D_SHARP_TABLEDIAGRAM)
			w = new D_SHARP_TableDiagram(this, rObject, takeReference);
		if (rClass == D_SHARP_HORIZONTALBOX)
			w = new D_SHARP_HorizontalBox(this, rObject, takeReference);
		if (rClass == D_SHARP_HORIZONTALSCROLLBOX)
			w = new D_SHARP_HorizontalScrollBox(this, rObject, takeReference);
		if (rClass == D_SHARP_VERTICALSCROLLBOX)
			w = new D_SHARP_VerticalScrollBox(this, rObject, takeReference);
		if (rClass == D_SHARP_SCROLLBOX)
			w = new D_SHARP_ScrollBox(this, rObject, takeReference);
		if (rClass == D_SHARP_TAB)
			w = new D_SHARP_Tab(this, rObject, takeReference);
		if (rClass == D_SHARP_STACK)
			w = new D_SHARP_Stack(this, rObject, takeReference);
		if (rClass == D_SHARP_TABCONTAINER)
			w = new D_SHARP_TabContainer(this, rObject, takeReference);
		if (rClass == D_SHARP_VERTICALSCROLLBOXWRAPPER)
			w = new D_SHARP_VerticalScrollBoxWrapper(this, rObject, takeReference);
		if (rClass == D_SHARP_HORIZONTALSCROLLBOXWRAPPER)
			w = new D_SHARP_HorizontalScrollBoxWrapper(this, rObject, takeReference);
		if (rClass == D_SHARP_VERTICALSPLITBOX)
			w = new D_SHARP_VerticalSplitBox(this, rObject, takeReference);
		if (rClass == D_SHARP_HORIZONTALSPLITBOX)
			w = new D_SHARP_HorizontalSplitBox(this, rObject, takeReference);
		if (rClass == D_SHARP_LABEL)
			w = new D_SHARP_Label(this, rObject, takeReference);
		if (rClass == D_SHARP_RADIOBUTTON)
			w = new D_SHARP_RadioButton(this, rObject, takeReference);
		if (rClass == D_SHARP_LISTBOX)
			w = new D_SHARP_ListBox(this, rObject, takeReference);
		if (rClass == D_SHARP_VTABLETYPE)
			w = new D_SHARP_VTableType(this, rObject, takeReference);
		if (rClass == D_SHARP_VTABLE)
			w = new D_SHARP_VTable(this, rObject, takeReference);
		if (rClass == D_SHARP_VTABLEROW)
			w = new D_SHARP_VTableRow(this, rObject, takeReference);
		if (rClass == D_SHARP_TABLECOMPONENT)
			w = new D_SHARP_TableComponent(this, rObject, takeReference);
		if (rClass == D_SHARP_BUTTON)
			w = new D_SHARP_Button(this, rObject, takeReference);
		if (rClass == D_SHARP_CHECKBOX)
			w = new D_SHARP_CheckBox(this, rObject, takeReference);
		if (rClass == D_SHARP_COMBOBOX)
			w = new D_SHARP_ComboBox(this, rObject, takeReference);
		if (rClass == D_SHARP_ITEM)
			w = new D_SHARP_Item(this, rObject, takeReference);
		if (rClass == D_SHARP_IMAGE)
			w = new D_SHARP_Image(this, rObject, takeReference);
		if (rClass == D_SHARP_TEXTBOX)
			w = new D_SHARP_TextBox(this, rObject, takeReference);
		if (rClass == D_SHARP_INPUTFIELD)
			w = new D_SHARP_InputField(this, rObject, takeReference);
		if (rClass == D_SHARP_TEXTAREA)
			w = new D_SHARP_TextArea(this, rObject, takeReference);
		if (rClass == D_SHARP_TREE)
			w = new D_SHARP_Tree(this, rObject, takeReference);
		if (rClass == D_SHARP_MULTILINETEXTBOX)
			w = new D_SHARP_MultiLineTextBox(this, rObject, takeReference);
		if (rClass == D_SHARP_RICHTEXTAREA)
			w = new D_SHARP_RichTextArea(this, rObject, takeReference);
		if (rClass == D_SHARP_PROGRESSBAR)
			w = new D_SHARP_ProgressBar(this, rObject, takeReference);
		if (rClass == D_SHARP_IMAGEBUTTON)
			w = new D_SHARP_ImageButton(this, rObject, takeReference);
		if (rClass == D_SHARP_VTABLECOLUMNTYPE)
			w = new D_SHARP_VTableColumnType(this, rObject, takeReference);
		if (rClass == D_SHARP_VTABLECELL)
			w = new D_SHARP_VTableCell(this, rObject, takeReference);
		if (rClass == D_SHARP_EVENTHANDLER)
			w = new D_SHARP_EventHandler(this, rObject, takeReference);
		if (rClass == D_SHARP_COMMAND)
			w = new D_SHARP_Command(this, rObject, takeReference);
		if (rClass == D_SHARP_EVENT)
			w = new D_SHARP_Event(this, rObject, takeReference);
		if (rClass == D_SHARP_ROWMOVEDEVENT)
			w = new D_SHARP_RowMovedEvent(this, rObject, takeReference);
		if (rClass == D_SHARP_COLUMNMOVEDEVENT)
			w = new D_SHARP_ColumnMovedEvent(this, rObject, takeReference);
		if (rClass == D_SHARP_MULTILINETEXTBOXCHANGEEVENT)
			w = new D_SHARP_MultiLineTextBoxChangeEvent(this, rObject, takeReference);
		if (rClass == D_SHARP_TEXTLINE)
			w = new D_SHARP_TextLine(this, rObject, takeReference);
		if (rClass == D_SHARP_TREENODESELECTEVENT)
			w = new D_SHARP_TreeNodeSelectEvent(this, rObject, takeReference);
		if (rClass == D_SHARP_ADDTREENODECMD)
			w = new D_SHARP_AddTreeNodeCmd(this, rObject, takeReference);
		if (rClass == D_SHARP_DELETETREENODECMD)
			w = new D_SHARP_DeleteTreeNodeCmd(this, rObject, takeReference);
		if (rClass == D_SHARP_SELECTTREENODECMD)
			w = new D_SHARP_SelectTreeNodeCmd(this, rObject, takeReference);
		if (rClass == D_SHARP_EXPANDTREENODECMD)
			w = new D_SHARP_ExpandTreeNodeCmd(this, rObject, takeReference);
		if (rClass == D_SHARP_COLLAPSETREENODECMD)
			w = new D_SHARP_CollapseTreeNodeCmd(this, rObject, takeReference);
		if (rClass == D_SHARP_TREENODE)
			w = new D_SHARP_TreeNode(this, rObject, takeReference);
		if (rClass == D_SHARP_LISTBOXCHANGEEVENT)
			w = new D_SHARP_ListBoxChangeEvent(this, rObject, takeReference);
		if (rClass == D_SHARP_TABCHANGEEVENT)
			w = new D_SHARP_TabChangeEvent(this, rObject, takeReference);
		if (rClass == D_SHARP_TREENODEMOVEEVENT)
			w = new D_SHARP_TreeNodeMoveEvent(this, rObject, takeReference);
		if (rClass == D_SHARP_COPYTOCLIPBOARDCMD)
			w = new D_SHARP_CopyToClipboardCmd(this, rObject, takeReference);
		if (rClass == D_SHARP_KEYDOWNEVENT)
			w = new D_SHARP_KeyDownEvent(this, rObject, takeReference);
		if (rClass == D_SHARP_TREENODEEXPANDEDEVENT)
			w = new D_SHARP_TreeNodeExpandedEvent(this, rObject, takeReference);
		if (rClass == D_SHARP_TREENODECOLLAPSEDEVENT)
			w = new D_SHARP_TreeNodeCollapsedEvent(this, rObject, takeReference);
		if (rClass == COMMAND)
			w = new Command(this, rObject, takeReference);
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
		if (!D_SHARP_Group.deleteAllObjects(this))
			ok = false;
		if (!D_SHARP_EventSource.deleteAllObjects(this))
			ok = false;
		if (!D_SHARP_Component.deleteAllObjects(this))
			ok = false;
		if (!D_SHARP_Container.deleteAllObjects(this))
			ok = false;
		if (!D_SHARP_Row.deleteAllObjects(this))
			ok = false;
		if (!D_SHARP_Column.deleteAllObjects(this))
			ok = false;
		if (!D_SHARP_VerticalBox.deleteAllObjects(this))
			ok = false;
		if (!D_SHARP_GroupBox.deleteAllObjects(this))
			ok = false;
		if (!D_SHARP_Form.deleteAllObjects(this))
			ok = false;
		if (!D_SHARP_TableDiagram.deleteAllObjects(this))
			ok = false;
		if (!D_SHARP_HorizontalBox.deleteAllObjects(this))
			ok = false;
		if (!D_SHARP_HorizontalScrollBox.deleteAllObjects(this))
			ok = false;
		if (!D_SHARP_VerticalScrollBox.deleteAllObjects(this))
			ok = false;
		if (!D_SHARP_ScrollBox.deleteAllObjects(this))
			ok = false;
		if (!D_SHARP_Tab.deleteAllObjects(this))
			ok = false;
		if (!D_SHARP_Stack.deleteAllObjects(this))
			ok = false;
		if (!D_SHARP_TabContainer.deleteAllObjects(this))
			ok = false;
		if (!D_SHARP_VerticalScrollBoxWrapper.deleteAllObjects(this))
			ok = false;
		if (!D_SHARP_HorizontalScrollBoxWrapper.deleteAllObjects(this))
			ok = false;
		if (!D_SHARP_VerticalSplitBox.deleteAllObjects(this))
			ok = false;
		if (!D_SHARP_HorizontalSplitBox.deleteAllObjects(this))
			ok = false;
		if (!D_SHARP_Label.deleteAllObjects(this))
			ok = false;
		if (!D_SHARP_RadioButton.deleteAllObjects(this))
			ok = false;
		if (!D_SHARP_ListBox.deleteAllObjects(this))
			ok = false;
		if (!D_SHARP_VTableType.deleteAllObjects(this))
			ok = false;
		if (!D_SHARP_VTable.deleteAllObjects(this))
			ok = false;
		if (!D_SHARP_VTableRow.deleteAllObjects(this))
			ok = false;
		if (!D_SHARP_TableComponent.deleteAllObjects(this))
			ok = false;
		if (!D_SHARP_Button.deleteAllObjects(this))
			ok = false;
		if (!D_SHARP_CheckBox.deleteAllObjects(this))
			ok = false;
		if (!D_SHARP_ComboBox.deleteAllObjects(this))
			ok = false;
		if (!D_SHARP_Item.deleteAllObjects(this))
			ok = false;
		if (!D_SHARP_Image.deleteAllObjects(this))
			ok = false;
		if (!D_SHARP_TextBox.deleteAllObjects(this))
			ok = false;
		if (!D_SHARP_InputField.deleteAllObjects(this))
			ok = false;
		if (!D_SHARP_TextArea.deleteAllObjects(this))
			ok = false;
		if (!D_SHARP_Tree.deleteAllObjects(this))
			ok = false;
		if (!D_SHARP_MultiLineTextBox.deleteAllObjects(this))
			ok = false;
		if (!D_SHARP_RichTextArea.deleteAllObjects(this))
			ok = false;
		if (!D_SHARP_ProgressBar.deleteAllObjects(this))
			ok = false;
		if (!D_SHARP_ImageButton.deleteAllObjects(this))
			ok = false;
		if (!D_SHARP_VTableColumnType.deleteAllObjects(this))
			ok = false;
		if (!D_SHARP_VTableCell.deleteAllObjects(this))
			ok = false;
		if (!D_SHARP_EventHandler.deleteAllObjects(this))
			ok = false;
		if (!D_SHARP_Command.deleteAllObjects(this))
			ok = false;
		if (!D_SHARP_Event.deleteAllObjects(this))
			ok = false;
		if (!D_SHARP_RowMovedEvent.deleteAllObjects(this))
			ok = false;
		if (!D_SHARP_ColumnMovedEvent.deleteAllObjects(this))
			ok = false;
		if (!D_SHARP_MultiLineTextBoxChangeEvent.deleteAllObjects(this))
			ok = false;
		if (!D_SHARP_TextLine.deleteAllObjects(this))
			ok = false;
		if (!D_SHARP_TreeNodeSelectEvent.deleteAllObjects(this))
			ok = false;
		if (!D_SHARP_AddTreeNodeCmd.deleteAllObjects(this))
			ok = false;
		if (!D_SHARP_DeleteTreeNodeCmd.deleteAllObjects(this))
			ok = false;
		if (!D_SHARP_SelectTreeNodeCmd.deleteAllObjects(this))
			ok = false;
		if (!D_SHARP_ExpandTreeNodeCmd.deleteAllObjects(this))
			ok = false;
		if (!D_SHARP_CollapseTreeNodeCmd.deleteAllObjects(this))
			ok = false;
		if (!D_SHARP_TreeNode.deleteAllObjects(this))
			ok = false;
		if (!D_SHARP_ListBoxChangeEvent.deleteAllObjects(this))
			ok = false;
		if (!D_SHARP_TabChangeEvent.deleteAllObjects(this))
			ok = false;
		if (!D_SHARP_TreeNodeMoveEvent.deleteAllObjects(this))
			ok = false;
		if (!D_SHARP_CopyToClipboardCmd.deleteAllObjects(this))
			ok = false;
		if (!D_SHARP_KeyDownEvent.deleteAllObjects(this))
			ok = false;
		if (!D_SHARP_TreeNodeExpandedEvent.deleteAllObjects(this))
			ok = false;
		if (!D_SHARP_TreeNodeCollapsedEvent.deleteAllObjects(this))
			ok = false;
		if (!Command.deleteAllObjects(this))
			ok = false;
		if (!Event.deleteAllObjects(this))
			ok = false;
		if (!Frame.deleteAllObjects(this))
			ok = false;
		return ok; 
	}

	// RAAPI references:
	RAAPI raapi = null;
	public long D_SHARP_GROUP = 0;
	  public long D_SHARP_GROUP_COMPONENTWIDTHS = 0;
	  public long D_SHARP_GROUP_COMPONENTHEIGHTS = 0;
	  public long D_SHARP_GROUP_OWNER = 0;
	public long D_SHARP_EVENTSOURCE = 0;
	  public long D_SHARP_EVENTSOURCE_EVENTHANDLER = 0;
	  public long D_SHARP_EVENTSOURCE_EVENT = 0;
	public long D_SHARP_COMPONENT = 0;
	  public long D_SHARP_COMPONENT_ID = 0;
	  public long D_SHARP_COMPONENT_ENABLED = 0;
	  public long D_SHARP_COMPONENT_MINIMUMWIDTH = 0;
	  public long D_SHARP_COMPONENT_MINIMUMHEIGHT = 0;
	  public long D_SHARP_COMPONENT_PREFERREDWIDTH = 0;
	  public long D_SHARP_COMPONENT_PREFERREDHEIGHT = 0;
	  public long D_SHARP_COMPONENT_MAXIMUMWIDTH = 0;
	  public long D_SHARP_COMPONENT_MAXIMUMHEIGHT = 0;
	  public long D_SHARP_COMPONENT_LEFTMARGIN = 0;
	  public long D_SHARP_COMPONENT_RIGHTMARGIN = 0;
	  public long D_SHARP_COMPONENT_TOPMARGIN = 0;
	  public long D_SHARP_COMPONENT_BOTTOMMARGIN = 0;
	  public long D_SHARP_COMPONENT_MINIMUMRELATIVEWIDTH = 0;
	  public long D_SHARP_COMPONENT_MINIMUMRELATIVEHEIGHT = 0;
	  public long D_SHARP_COMPONENT_PREFERREDRELATIVEWIDTH = 0;
	  public long D_SHARP_COMPONENT_PREFERREDRELATIVEHEIGHT = 0;
	  public long D_SHARP_COMPONENT_MAXIMUMRELATIVEWIDTH = 0;
	  public long D_SHARP_COMPONENT_MAXIMUMRELATIVEHEIGHT = 0;
	  public long D_SHARP_COMPONENT_HORIZONTALSPAN = 0;
	  public long D_SHARP_COMPONENT_VERTICALSPAN = 0;
	  public long D_SHARP_COMPONENT_MINIMUMHORIZONTALSHRINKFACTOR = 0;
	  public long D_SHARP_COMPONENT_MAXIMUMHORIZONTALGROWFACTOR = 0;
	  public long D_SHARP_COMPONENT_MINIMUMVERTICALSHRINKFACTOR = 0;
	  public long D_SHARP_COMPONENT_MAXIMUMVERTICALGROWFACTOR = 0;
	  public long D_SHARP_COMPONENT_VISIBLE = 0;
	  public long D_SHARP_COMPONENT_HINT = 0;
	  public long D_SHARP_COMPONENT_READONLY = 0;
	  public long D_SHARP_COMPONENT_RELATIVEWIDTHGROUP = 0;
	  public long D_SHARP_COMPONENT_RELATIVEHEIGHTGROUP = 0;
	  public long D_SHARP_COMPONENT_FOCUSCONTAINER = 0;
	  public long D_SHARP_COMPONENT_COMMAND = 0;
	  public long D_SHARP_COMPONENT_OWNEDGROUP = 0;
	  public long D_SHARP_COMPONENT_CONTAINER = 0;
	  public long D_SHARP_COMPONENT_FORM = 0;
	  public long D_SHARP_COMPONENT_FOFORM = 0;
	public long D_SHARP_CONTAINER = 0;
	  public long D_SHARP_CONTAINER_HORIZONTALALIGNMENT = 0;
	  public long D_SHARP_CONTAINER_VERTICALALIGNMENT = 0;
	  public long D_SHARP_CONTAINER_HORIZONTALSPACING = 0;
	  public long D_SHARP_CONTAINER_VERTICALSPACING = 0;
	  public long D_SHARP_CONTAINER_LEFTBORDER = 0;
	  public long D_SHARP_CONTAINER_RIGHTBORDER = 0;
	  public long D_SHARP_CONTAINER_TOPBORDER = 0;
	  public long D_SHARP_CONTAINER_BOTTOMBORDER = 0;
	  public long D_SHARP_CONTAINER_LEFTPADDING = 0;
	  public long D_SHARP_CONTAINER_RIGHTPADDING = 0;
	  public long D_SHARP_CONTAINER_TOPPADDING = 0;
	  public long D_SHARP_CONTAINER_BOTTOMPADDING = 0;
	  public long D_SHARP_CONTAINER_FOCUS = 0;
	  public long D_SHARP_CONTAINER_COMPONENT = 0;
	public long D_SHARP_ROW = 0;
	public long D_SHARP_COLUMN = 0;
	public long D_SHARP_VERTICALBOX = 0;
	public long D_SHARP_GROUPBOX = 0;
	  public long D_SHARP_GROUPBOX_CAPTION = 0;
	public long D_SHARP_FORM = 0;
	  public long D_SHARP_FORM_CAPTION = 0;
	  public long D_SHARP_FORM_BUTTONCLICKONCLOSE = 0;
	  public long D_SHARP_FORM_HASMINIMIZEBUTTON = 0;
	  public long D_SHARP_FORM_HASMAXIMIZEBUTTON = 0;
	  public long D_SHARP_FORM_ISCLOSEBUTTONENABLED = 0;
	  public long D_SHARP_FORM_LEFT = 0;
	  public long D_SHARP_FORM_TOP = 0;
	  public long D_SHARP_FORM_WIDTH = 0;
	  public long D_SHARP_FORM_HEIGHT = 0;
	  public long D_SHARP_FORM_EDITABLE = 0;
	  public long D_SHARP_FORM_DEFAULTBUTTON = 0;
	  public long D_SHARP_FORM_CANCELBUTTON = 0;
	  public long D_SHARP_FORM_OWNEDEVENT = 0;
	  public long D_SHARP_FORM_FOCUSED = 0;
	  public long D_SHARP_FORM_CONSTANTFORMCALLINGBUTTON = 0;
	  public long D_SHARP_FORM_FOCUSORDER = 0;
	  public long D_SHARP_FORM_CALLINGBUTTON = 0;
	  public long D_SHARP_FORM_FRAME = 0;
	public long D_SHARP_TABLEDIAGRAM = 0;
	public long D_SHARP_HORIZONTALBOX = 0;
	public long D_SHARP_HORIZONTALSCROLLBOX = 0;
	public long D_SHARP_VERTICALSCROLLBOX = 0;
	public long D_SHARP_SCROLLBOX = 0;
	public long D_SHARP_TAB = 0;
	  public long D_SHARP_TAB_CAPTION = 0;
	  public long D_SHARP_TAB_TABCHANGEEVENT = 0;
	  public long D_SHARP_TAB_TABCONTAINER = 0;
	public long D_SHARP_STACK = 0;
	public long D_SHARP_TABCONTAINER = 0;
	  public long D_SHARP_TABCONTAINER_ACTIVETAB = 0;
	public long D_SHARP_VERTICALSCROLLBOXWRAPPER = 0;
	public long D_SHARP_HORIZONTALSCROLLBOXWRAPPER = 0;
	public long D_SHARP_VERTICALSPLITBOX = 0;
	public long D_SHARP_HORIZONTALSPLITBOX = 0;
	public long D_SHARP_LABEL = 0;
	  public long D_SHARP_LABEL_CAPTION = 0;
	public long D_SHARP_RADIOBUTTON = 0;
	  public long D_SHARP_RADIOBUTTON_CAPTION = 0;
	  public long D_SHARP_RADIOBUTTON_SELECTED = 0;
	public long D_SHARP_LISTBOX = 0;
	  public long D_SHARP_LISTBOX_MULTISELECT = 0;
	  public long D_SHARP_LISTBOX_ITEM = 0;
	  public long D_SHARP_LISTBOX_SELECTED = 0;
	public long D_SHARP_VTABLETYPE = 0;
	  public long D_SHARP_VTABLETYPE_EDITABLE = 0;
	  public long D_SHARP_VTABLETYPE_MOVABLEROWS = 0;
	  public long D_SHARP_VTABLETYPE_MOVABLECOLUMNS = 0;
	  public long D_SHARP_VTABLETYPE_VERTICALALIGNMENT = 0;
	  public long D_SHARP_VTABLETYPE_INSERTBUTTONCAPTION = 0;
	  public long D_SHARP_VTABLETYPE_DELETEBUTTONCAPTION = 0;
	  public long D_SHARP_VTABLETYPE_ROWHEIGHT = 0;
	  public long D_SHARP_VTABLETYPE_AUTOORDERROWS = 0;
	  public long D_SHARP_VTABLETYPE_AUTOORDERCOLUMNS = 0;
	  public long D_SHARP_VTABLETYPE_VTABLE = 0;
	  public long D_SHARP_VTABLETYPE_COLUMNTYPE = 0;
	  public long D_SHARP_VTABLETYPE_COLUMN = 0;
	  public long D_SHARP_VTABLETYPE_BUTTON = 0;
	  public long D_SHARP_VTABLETYPE_COLUMNMOVEDEVENT = 0;
	  public long D_SHARP_VTABLETYPE_BEFORECOLUMNMOVEDEVENT = 0;
	  public long D_SHARP_VTABLETYPE_AFTERCOLUMNMOVEDEVENT = 0;
	public long D_SHARP_VTABLE = 0;
	  public long D_SHARP_VTABLE_TYPE = 0;
	  public long D_SHARP_VTABLE_VTABLEROW = 0;
	  public long D_SHARP_VTABLE_COPYTOCLIPBOARD = 0;
	  public long D_SHARP_VTABLE_SELECTEDROW = 0;
	public long D_SHARP_VTABLEROW = 0;
	  public long D_SHARP_VTABLEROW_EDITED = 0;
	  public long D_SHARP_VTABLEROW_INSERTED = 0;
	  public long D_SHARP_VTABLEROW_DELETED = 0;
	  public long D_SHARP_VTABLEROW_VERTICALALIGNMENT = 0;
	  public long D_SHARP_VTABLEROW_VTABLE = 0;
	  public long D_SHARP_VTABLEROW_VTABLECELL = 0;
	  public long D_SHARP_VTABLEROW_ACTIVECELL = 0;
	  public long D_SHARP_VTABLEROW_PARENTTABLE = 0;
	  public long D_SHARP_VTABLEROW_ROWMOVEDEVENT = 0;
	  public long D_SHARP_VTABLEROW_BEFOREROWMOVEDEVENT = 0;
	  public long D_SHARP_VTABLEROW_AFTERROWMOVEDEVENT = 0;
	public long D_SHARP_TABLECOMPONENT = 0;
	  public long D_SHARP_TABLECOMPONENT_OUTLINECOLOR = 0;
	  public long D_SHARP_TABLECOMPONENT_VTABLECOLUMNTYPE = 0;
	  public long D_SHARP_TABLECOMPONENT_VTABLECOLUMNTYPEOWNER = 0;
	  public long D_SHARP_TABLECOMPONENT_VTABLECELL = 0;
	  public long D_SHARP_TABLECOMPONENT_VTABLECELLOWNER = 0;
	public long D_SHARP_BUTTON = 0;
	  public long D_SHARP_BUTTON_CAPTION = 0;
	  public long D_SHARP_BUTTON_CLOSEONCLICK = 0;
	  public long D_SHARP_BUTTON_DELETEONCLICK = 0;
	  public long D_SHARP_BUTTON_DEFAULTBUTTONFORM = 0;
	  public long D_SHARP_BUTTON_CANCELBUTTONFORM = 0;
	  public long D_SHARP_BUTTON_CONSTANTFORMONCLICK = 0;
	  public long D_SHARP_BUTTON_VTABLETYPE = 0;
	  public long D_SHARP_BUTTON_FORMONCLICK = 0;
	public long D_SHARP_CHECKBOX = 0;
	  public long D_SHARP_CHECKBOX_CAPTION = 0;
	  public long D_SHARP_CHECKBOX_CHECKED = 0;
	  public long D_SHARP_CHECKBOX_EDITABLE = 0;
	public long D_SHARP_COMBOBOX = 0;
	  public long D_SHARP_COMBOBOX_TEXT = 0;
	  public long D_SHARP_COMBOBOX_EDITABLE = 0;
	  public long D_SHARP_COMBOBOX_ITEM = 0;
	  public long D_SHARP_COMBOBOX_SELECTED = 0;
	public long D_SHARP_ITEM = 0;
	  public long D_SHARP_ITEM_VALUE = 0;
	  public long D_SHARP_ITEM_LISTBOX = 0;
	  public long D_SHARP_ITEM_COMBOBOX = 0;
	  public long D_SHARP_ITEM_PARENTCOMBOBOX = 0;
	  public long D_SHARP_ITEM_PARENTLISTBOX = 0;
	  public long D_SHARP_ITEM_VTABLECELL = 0;
	  public long D_SHARP_ITEM_SLISTBOXCHANGEEVENT = 0;
	  public long D_SHARP_ITEM_DLISTBOXCHANGEEVENT = 0;
	public long D_SHARP_IMAGE = 0;
	  public long D_SHARP_IMAGE_FILENAME = 0;
	  public long D_SHARP_IMAGE_EDITABLE = 0;
	public long D_SHARP_TEXTBOX = 0;
	  public long D_SHARP_TEXTBOX_TEXT = 0;
	  public long D_SHARP_TEXTBOX_MULTILINE = 0;
	  public long D_SHARP_TEXTBOX_EDITABLE = 0;
	public long D_SHARP_INPUTFIELD = 0;
	public long D_SHARP_TEXTAREA = 0;
	public long D_SHARP_TREE = 0;
	  public long D_SHARP_TREE_DRAGGABLENODES = 0;
	  public long D_SHARP_TREE_TREENODE = 0;
	  public long D_SHARP_TREE_SELECTED = 0;
	public long D_SHARP_MULTILINETEXTBOX = 0;
	  public long D_SHARP_MULTILINETEXTBOX_TEXTLINE = 0;
	  public long D_SHARP_MULTILINETEXTBOX_CURRENT = 0;
	public long D_SHARP_RICHTEXTAREA = 0;
	  public long D_SHARP_RICHTEXTAREA_FILENAME = 0;
	  public long D_SHARP_RICHTEXTAREA_ENCODEDCONTENT = 0;
	public long D_SHARP_PROGRESSBAR = 0;
	  public long D_SHARP_PROGRESSBAR_POSITION = 0;
	  public long D_SHARP_PROGRESSBAR_MINIMUMVALUE = 0;
	  public long D_SHARP_PROGRESSBAR_MAXIMUMVALUE = 0;
	public long D_SHARP_IMAGEBUTTON = 0;
	  public long D_SHARP_IMAGEBUTTON_FILENAME = 0;
	  public long D_SHARP_IMAGEBUTTON_CARDINALPOINT = 0;
	public long D_SHARP_VTABLECOLUMNTYPE = 0;
	  public long D_SHARP_VTABLECOLUMNTYPE_CAPTION = 0;
	  public long D_SHARP_VTABLECOLUMNTYPE_DEFAULTVALUE = 0;
	  public long D_SHARP_VTABLECOLUMNTYPE_EDITABLE = 0;
	  public long D_SHARP_VTABLECOLUMNTYPE_WIDTH = 0;
	  public long D_SHARP_VTABLECOLUMNTYPE_PREFERREDRELATIVEWIDTH = 0;
	  public long D_SHARP_VTABLECOLUMNTYPE_HORIZONTALALIGNMENT = 0;
	  public long D_SHARP_VTABLECOLUMNTYPE_VTABLETYPE = 0;
	  public long D_SHARP_VTABLECOLUMNTYPE_VTABLETYPEOWNER = 0;
	  public long D_SHARP_VTABLECOLUMNTYPE_VTABLECELL = 0;
	  public long D_SHARP_VTABLECOLUMNTYPE_DEFAULTCOMPONENTTYPE = 0;
	  public long D_SHARP_VTABLECOLUMNTYPE_DEFAULTCOMPONENT = 0;
	public long D_SHARP_VTABLECELL = 0;
	  public long D_SHARP_VTABLECELL_VALUE = 0;
	  public long D_SHARP_VTABLECELL_VTABLEROW = 0;
	  public long D_SHARP_VTABLECELL_PARENTROW = 0;
	  public long D_SHARP_VTABLECELL_SELECTEDITEM = 0;
	  public long D_SHARP_VTABLECELL_VTABLECOLUMNTYPE = 0;
	  public long D_SHARP_VTABLECELL_COMPONENTTYPE = 0;
	  public long D_SHARP_VTABLECELL_COMPONENT = 0;
	  public long D_SHARP_VTABLECELL_EVENT = 0;
	public long D_SHARP_EVENTHANDLER = 0;
	  public long D_SHARP_EVENTHANDLER_EVENTNAME = 0;
	  public long D_SHARP_EVENTHANDLER_TRANSFORMATIONNAME = 0;
	  public long D_SHARP_EVENTHANDLER_PROCEDURENAME = 0;
	  public long D_SHARP_EVENTHANDLER_EVENTSOURCE = 0;
	public long D_SHARP_COMMAND = 0;
	  public long D_SHARP_COMMAND_INFO = 0;
	  public long D_SHARP_COMMAND_RECEIVER = 0;
	public long D_SHARP_EVENT = 0;
	  public long D_SHARP_EVENT_EVENTNAME = 0;
	  public long D_SHARP_EVENT_INFO = 0;
	  public long D_SHARP_EVENT_FORM = 0;
	  public long D_SHARP_EVENT_SOURCE = 0;
	  public long D_SHARP_EVENT_VTABLECELL = 0;
	public long D_SHARP_ROWMOVEDEVENT = 0;
	  public long D_SHARP_ROWMOVEDEVENT_ROW = 0;
	  public long D_SHARP_ROWMOVEDEVENT_BEFORE = 0;
	  public long D_SHARP_ROWMOVEDEVENT_AFTER = 0;
	public long D_SHARP_COLUMNMOVEDEVENT = 0;
	  public long D_SHARP_COLUMNMOVEDEVENT_COLUMN = 0;
	  public long D_SHARP_COLUMNMOVEDEVENT_BEFORE = 0;
	  public long D_SHARP_COLUMNMOVEDEVENT_AFTER = 0;
	public long D_SHARP_MULTILINETEXTBOXCHANGEEVENT = 0;
	  public long D_SHARP_MULTILINETEXTBOXCHANGEEVENT_INSERTED = 0;
	  public long D_SHARP_MULTILINETEXTBOXCHANGEEVENT_DELETED = 0;
	  public long D_SHARP_MULTILINETEXTBOXCHANGEEVENT_EDITED = 0;
	public long D_SHARP_TEXTLINE = 0;
	  public long D_SHARP_TEXTLINE_TEXT = 0;
	  public long D_SHARP_TEXTLINE_INSERTED = 0;
	  public long D_SHARP_TEXTLINE_DELETED = 0;
	  public long D_SHARP_TEXTLINE_EDITED = 0;
	  public long D_SHARP_TEXTLINE_MULTILINETEXTBOX = 0;
	  public long D_SHARP_TEXTLINE_PARENTMULTILINETEXTBOX = 0;
	  public long D_SHARP_TEXTLINE_IMULTILINETEXTBOXCHANGEEVENT = 0;
	  public long D_SHARP_TEXTLINE_DMULTILINETEXTBOXCHANGEEVENT = 0;
	  public long D_SHARP_TEXTLINE_EMULTILINETEXTBOXCHANGEEVENT = 0;
	public long D_SHARP_TREENODESELECTEVENT = 0;
	  public long D_SHARP_TREENODESELECTEVENT_TREENODE = 0;
	  public long D_SHARP_TREENODESELECTEVENT_PREVIOUS = 0;
	public long D_SHARP_ADDTREENODECMD = 0;
	  public long D_SHARP_ADDTREENODECMD_TREENODE = 0;
	  public long D_SHARP_ADDTREENODECMD_PARENT = 0;
	  public long D_SHARP_ADDTREENODECMD_BEFORE = 0;
	public long D_SHARP_DELETETREENODECMD = 0;
	  public long D_SHARP_DELETETREENODECMD_TREENODE = 0;
	public long D_SHARP_SELECTTREENODECMD = 0;
	  public long D_SHARP_SELECTTREENODECMD_TREENODE = 0;
	public long D_SHARP_EXPANDTREENODECMD = 0;
	  public long D_SHARP_EXPANDTREENODECMD_TREENODE = 0;
	public long D_SHARP_COLLAPSETREENODECMD = 0;
	  public long D_SHARP_COLLAPSETREENODECMD_TREENODE = 0;
	public long D_SHARP_TREENODE = 0;
	  public long D_SHARP_TREENODE_TEXT = 0;
	  public long D_SHARP_TREENODE_EXPANDED = 0;
	  public long D_SHARP_TREENODE_ID = 0;
	  public long D_SHARP_TREENODE_TREE = 0;
	  public long D_SHARP_TREENODE_PARENTTREE = 0;
	  public long D_SHARP_TREENODE_CHILDNODE = 0;
	  public long D_SHARP_TREENODE_PARENTNODE = 0;
	  public long D_SHARP_TREENODE_TREENODESELECTEVENT = 0;
	  public long D_SHARP_TREENODE_PTREENODESELECTEVENT = 0;
	  public long D_SHARP_TREENODE_TADDTREENODECMD = 0;
	  public long D_SHARP_TREENODE_PADDTREENODECMD = 0;
	  public long D_SHARP_TREENODE_BADDTREENODECMD = 0;
	  public long D_SHARP_TREENODE_DELETETREENODECMD = 0;
	  public long D_SHARP_TREENODE_SELECTTREENODECMD = 0;
	  public long D_SHARP_TREENODE_EXPANDNODECMD = 0;
	  public long D_SHARP_TREENODE_COLLAPSETREENODECMD = 0;
	  public long D_SHARP_TREENODE_TTREENODEMOVEEVENT = 0;
	  public long D_SHARP_TREENODE_BTREENODEMOVEEVENT = 0;
	  public long D_SHARP_TREENODE_ATREENODEMOVEEVENT = 0;
	  public long D_SHARP_TREENODE_PTREENODEMOVEEVENT = 0;
	  public long D_SHARP_TREENODE_TREENODEEXPANDEDEVENT = 0;
	  public long D_SHARP_TREENODE_TREENODECOLLAPSEDEVENT = 0;
	public long D_SHARP_LISTBOXCHANGEEVENT = 0;
	  public long D_SHARP_LISTBOXCHANGEEVENT_SELECTED = 0;
	  public long D_SHARP_LISTBOXCHANGEEVENT_DESELECTED = 0;
	public long D_SHARP_TABCHANGEEVENT = 0;
	  public long D_SHARP_TABCHANGEEVENT_TAB = 0;
	public long D_SHARP_TREENODEMOVEEVENT = 0;
	  public long D_SHARP_TREENODEMOVEEVENT_TREENODE = 0;
	  public long D_SHARP_TREENODEMOVEEVENT_WASBEFORE = 0;
	  public long D_SHARP_TREENODEMOVEEVENT_WASAFTER = 0;
	  public long D_SHARP_TREENODEMOVEEVENT_PREVIOUSPARENT = 0;
	public long D_SHARP_COPYTOCLIPBOARDCMD = 0;
	  public long D_SHARP_COPYTOCLIPBOARDCMD_VTABLE = 0;
	public long D_SHARP_KEYDOWNEVENT = 0;
	  public long D_SHARP_KEYDOWNEVENT_KEYNAME = 0;
	public long D_SHARP_TREENODEEXPANDEDEVENT = 0;
	  public long D_SHARP_TREENODEEXPANDEDEVENT_TREENODE = 0;
	public long D_SHARP_TREENODECOLLAPSEDEVENT = 0;
	  public long D_SHARP_TREENODECOLLAPSEDEVENT_TREENODE = 0;
	public long COMMAND = 0;
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
	  public long FRAME_FORM = 0;

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
			if (D_SHARP_GROUP != 0) {
				raapi.freeReference(D_SHARP_GROUP);
				D_SHARP_GROUP = 0;
			}
	  		if (D_SHARP_GROUP_COMPONENTWIDTHS != 0) {
				raapi.freeReference(D_SHARP_GROUP_COMPONENTWIDTHS);
				D_SHARP_GROUP_COMPONENTWIDTHS = 0;
			}
	  		if (D_SHARP_GROUP_COMPONENTHEIGHTS != 0) {
				raapi.freeReference(D_SHARP_GROUP_COMPONENTHEIGHTS);
				D_SHARP_GROUP_COMPONENTHEIGHTS = 0;
			}
	  		if (D_SHARP_GROUP_OWNER != 0) {
				raapi.freeReference(D_SHARP_GROUP_OWNER);
				D_SHARP_GROUP_OWNER = 0;
			}
			if (D_SHARP_EVENTSOURCE != 0) {
				raapi.freeReference(D_SHARP_EVENTSOURCE);
				D_SHARP_EVENTSOURCE = 0;
			}
	  		if (D_SHARP_EVENTSOURCE_EVENTHANDLER != 0) {
				raapi.freeReference(D_SHARP_EVENTSOURCE_EVENTHANDLER);
				D_SHARP_EVENTSOURCE_EVENTHANDLER = 0;
			}
	  		if (D_SHARP_EVENTSOURCE_EVENT != 0) {
				raapi.freeReference(D_SHARP_EVENTSOURCE_EVENT);
				D_SHARP_EVENTSOURCE_EVENT = 0;
			}
			if (D_SHARP_COMPONENT != 0) {
				raapi.freeReference(D_SHARP_COMPONENT);
				D_SHARP_COMPONENT = 0;
			}
	  		if (D_SHARP_COMPONENT_ID != 0) {
				raapi.freeReference(D_SHARP_COMPONENT_ID);
				D_SHARP_COMPONENT_ID = 0;
			}
	  		if (D_SHARP_COMPONENT_ENABLED != 0) {
				raapi.freeReference(D_SHARP_COMPONENT_ENABLED);
				D_SHARP_COMPONENT_ENABLED = 0;
			}
	  		if (D_SHARP_COMPONENT_MINIMUMWIDTH != 0) {
				raapi.freeReference(D_SHARP_COMPONENT_MINIMUMWIDTH);
				D_SHARP_COMPONENT_MINIMUMWIDTH = 0;
			}
	  		if (D_SHARP_COMPONENT_MINIMUMHEIGHT != 0) {
				raapi.freeReference(D_SHARP_COMPONENT_MINIMUMHEIGHT);
				D_SHARP_COMPONENT_MINIMUMHEIGHT = 0;
			}
	  		if (D_SHARP_COMPONENT_PREFERREDWIDTH != 0) {
				raapi.freeReference(D_SHARP_COMPONENT_PREFERREDWIDTH);
				D_SHARP_COMPONENT_PREFERREDWIDTH = 0;
			}
	  		if (D_SHARP_COMPONENT_PREFERREDHEIGHT != 0) {
				raapi.freeReference(D_SHARP_COMPONENT_PREFERREDHEIGHT);
				D_SHARP_COMPONENT_PREFERREDHEIGHT = 0;
			}
	  		if (D_SHARP_COMPONENT_MAXIMUMWIDTH != 0) {
				raapi.freeReference(D_SHARP_COMPONENT_MAXIMUMWIDTH);
				D_SHARP_COMPONENT_MAXIMUMWIDTH = 0;
			}
	  		if (D_SHARP_COMPONENT_MAXIMUMHEIGHT != 0) {
				raapi.freeReference(D_SHARP_COMPONENT_MAXIMUMHEIGHT);
				D_SHARP_COMPONENT_MAXIMUMHEIGHT = 0;
			}
	  		if (D_SHARP_COMPONENT_LEFTMARGIN != 0) {
				raapi.freeReference(D_SHARP_COMPONENT_LEFTMARGIN);
				D_SHARP_COMPONENT_LEFTMARGIN = 0;
			}
	  		if (D_SHARP_COMPONENT_RIGHTMARGIN != 0) {
				raapi.freeReference(D_SHARP_COMPONENT_RIGHTMARGIN);
				D_SHARP_COMPONENT_RIGHTMARGIN = 0;
			}
	  		if (D_SHARP_COMPONENT_TOPMARGIN != 0) {
				raapi.freeReference(D_SHARP_COMPONENT_TOPMARGIN);
				D_SHARP_COMPONENT_TOPMARGIN = 0;
			}
	  		if (D_SHARP_COMPONENT_BOTTOMMARGIN != 0) {
				raapi.freeReference(D_SHARP_COMPONENT_BOTTOMMARGIN);
				D_SHARP_COMPONENT_BOTTOMMARGIN = 0;
			}
	  		if (D_SHARP_COMPONENT_MINIMUMRELATIVEWIDTH != 0) {
				raapi.freeReference(D_SHARP_COMPONENT_MINIMUMRELATIVEWIDTH);
				D_SHARP_COMPONENT_MINIMUMRELATIVEWIDTH = 0;
			}
	  		if (D_SHARP_COMPONENT_MINIMUMRELATIVEHEIGHT != 0) {
				raapi.freeReference(D_SHARP_COMPONENT_MINIMUMRELATIVEHEIGHT);
				D_SHARP_COMPONENT_MINIMUMRELATIVEHEIGHT = 0;
			}
	  		if (D_SHARP_COMPONENT_PREFERREDRELATIVEWIDTH != 0) {
				raapi.freeReference(D_SHARP_COMPONENT_PREFERREDRELATIVEWIDTH);
				D_SHARP_COMPONENT_PREFERREDRELATIVEWIDTH = 0;
			}
	  		if (D_SHARP_COMPONENT_PREFERREDRELATIVEHEIGHT != 0) {
				raapi.freeReference(D_SHARP_COMPONENT_PREFERREDRELATIVEHEIGHT);
				D_SHARP_COMPONENT_PREFERREDRELATIVEHEIGHT = 0;
			}
	  		if (D_SHARP_COMPONENT_MAXIMUMRELATIVEWIDTH != 0) {
				raapi.freeReference(D_SHARP_COMPONENT_MAXIMUMRELATIVEWIDTH);
				D_SHARP_COMPONENT_MAXIMUMRELATIVEWIDTH = 0;
			}
	  		if (D_SHARP_COMPONENT_MAXIMUMRELATIVEHEIGHT != 0) {
				raapi.freeReference(D_SHARP_COMPONENT_MAXIMUMRELATIVEHEIGHT);
				D_SHARP_COMPONENT_MAXIMUMRELATIVEHEIGHT = 0;
			}
	  		if (D_SHARP_COMPONENT_HORIZONTALSPAN != 0) {
				raapi.freeReference(D_SHARP_COMPONENT_HORIZONTALSPAN);
				D_SHARP_COMPONENT_HORIZONTALSPAN = 0;
			}
	  		if (D_SHARP_COMPONENT_VERTICALSPAN != 0) {
				raapi.freeReference(D_SHARP_COMPONENT_VERTICALSPAN);
				D_SHARP_COMPONENT_VERTICALSPAN = 0;
			}
	  		if (D_SHARP_COMPONENT_MINIMUMHORIZONTALSHRINKFACTOR != 0) {
				raapi.freeReference(D_SHARP_COMPONENT_MINIMUMHORIZONTALSHRINKFACTOR);
				D_SHARP_COMPONENT_MINIMUMHORIZONTALSHRINKFACTOR = 0;
			}
	  		if (D_SHARP_COMPONENT_MAXIMUMHORIZONTALGROWFACTOR != 0) {
				raapi.freeReference(D_SHARP_COMPONENT_MAXIMUMHORIZONTALGROWFACTOR);
				D_SHARP_COMPONENT_MAXIMUMHORIZONTALGROWFACTOR = 0;
			}
	  		if (D_SHARP_COMPONENT_MINIMUMVERTICALSHRINKFACTOR != 0) {
				raapi.freeReference(D_SHARP_COMPONENT_MINIMUMVERTICALSHRINKFACTOR);
				D_SHARP_COMPONENT_MINIMUMVERTICALSHRINKFACTOR = 0;
			}
	  		if (D_SHARP_COMPONENT_MAXIMUMVERTICALGROWFACTOR != 0) {
				raapi.freeReference(D_SHARP_COMPONENT_MAXIMUMVERTICALGROWFACTOR);
				D_SHARP_COMPONENT_MAXIMUMVERTICALGROWFACTOR = 0;
			}
	  		if (D_SHARP_COMPONENT_VISIBLE != 0) {
				raapi.freeReference(D_SHARP_COMPONENT_VISIBLE);
				D_SHARP_COMPONENT_VISIBLE = 0;
			}
	  		if (D_SHARP_COMPONENT_HINT != 0) {
				raapi.freeReference(D_SHARP_COMPONENT_HINT);
				D_SHARP_COMPONENT_HINT = 0;
			}
	  		if (D_SHARP_COMPONENT_READONLY != 0) {
				raapi.freeReference(D_SHARP_COMPONENT_READONLY);
				D_SHARP_COMPONENT_READONLY = 0;
			}
	  		if (D_SHARP_COMPONENT_RELATIVEWIDTHGROUP != 0) {
				raapi.freeReference(D_SHARP_COMPONENT_RELATIVEWIDTHGROUP);
				D_SHARP_COMPONENT_RELATIVEWIDTHGROUP = 0;
			}
	  		if (D_SHARP_COMPONENT_RELATIVEHEIGHTGROUP != 0) {
				raapi.freeReference(D_SHARP_COMPONENT_RELATIVEHEIGHTGROUP);
				D_SHARP_COMPONENT_RELATIVEHEIGHTGROUP = 0;
			}
	  		if (D_SHARP_COMPONENT_FOCUSCONTAINER != 0) {
				raapi.freeReference(D_SHARP_COMPONENT_FOCUSCONTAINER);
				D_SHARP_COMPONENT_FOCUSCONTAINER = 0;
			}
	  		if (D_SHARP_COMPONENT_COMMAND != 0) {
				raapi.freeReference(D_SHARP_COMPONENT_COMMAND);
				D_SHARP_COMPONENT_COMMAND = 0;
			}
	  		if (D_SHARP_COMPONENT_OWNEDGROUP != 0) {
				raapi.freeReference(D_SHARP_COMPONENT_OWNEDGROUP);
				D_SHARP_COMPONENT_OWNEDGROUP = 0;
			}
	  		if (D_SHARP_COMPONENT_CONTAINER != 0) {
				raapi.freeReference(D_SHARP_COMPONENT_CONTAINER);
				D_SHARP_COMPONENT_CONTAINER = 0;
			}
	  		if (D_SHARP_COMPONENT_FORM != 0) {
				raapi.freeReference(D_SHARP_COMPONENT_FORM);
				D_SHARP_COMPONENT_FORM = 0;
			}
	  		if (D_SHARP_COMPONENT_FOFORM != 0) {
				raapi.freeReference(D_SHARP_COMPONENT_FOFORM);
				D_SHARP_COMPONENT_FOFORM = 0;
			}
			if (D_SHARP_CONTAINER != 0) {
				raapi.freeReference(D_SHARP_CONTAINER);
				D_SHARP_CONTAINER = 0;
			}
	  		if (D_SHARP_CONTAINER_HORIZONTALALIGNMENT != 0) {
				raapi.freeReference(D_SHARP_CONTAINER_HORIZONTALALIGNMENT);
				D_SHARP_CONTAINER_HORIZONTALALIGNMENT = 0;
			}
	  		if (D_SHARP_CONTAINER_VERTICALALIGNMENT != 0) {
				raapi.freeReference(D_SHARP_CONTAINER_VERTICALALIGNMENT);
				D_SHARP_CONTAINER_VERTICALALIGNMENT = 0;
			}
	  		if (D_SHARP_CONTAINER_HORIZONTALSPACING != 0) {
				raapi.freeReference(D_SHARP_CONTAINER_HORIZONTALSPACING);
				D_SHARP_CONTAINER_HORIZONTALSPACING = 0;
			}
	  		if (D_SHARP_CONTAINER_VERTICALSPACING != 0) {
				raapi.freeReference(D_SHARP_CONTAINER_VERTICALSPACING);
				D_SHARP_CONTAINER_VERTICALSPACING = 0;
			}
	  		if (D_SHARP_CONTAINER_LEFTBORDER != 0) {
				raapi.freeReference(D_SHARP_CONTAINER_LEFTBORDER);
				D_SHARP_CONTAINER_LEFTBORDER = 0;
			}
	  		if (D_SHARP_CONTAINER_RIGHTBORDER != 0) {
				raapi.freeReference(D_SHARP_CONTAINER_RIGHTBORDER);
				D_SHARP_CONTAINER_RIGHTBORDER = 0;
			}
	  		if (D_SHARP_CONTAINER_TOPBORDER != 0) {
				raapi.freeReference(D_SHARP_CONTAINER_TOPBORDER);
				D_SHARP_CONTAINER_TOPBORDER = 0;
			}
	  		if (D_SHARP_CONTAINER_BOTTOMBORDER != 0) {
				raapi.freeReference(D_SHARP_CONTAINER_BOTTOMBORDER);
				D_SHARP_CONTAINER_BOTTOMBORDER = 0;
			}
	  		if (D_SHARP_CONTAINER_LEFTPADDING != 0) {
				raapi.freeReference(D_SHARP_CONTAINER_LEFTPADDING);
				D_SHARP_CONTAINER_LEFTPADDING = 0;
			}
	  		if (D_SHARP_CONTAINER_RIGHTPADDING != 0) {
				raapi.freeReference(D_SHARP_CONTAINER_RIGHTPADDING);
				D_SHARP_CONTAINER_RIGHTPADDING = 0;
			}
	  		if (D_SHARP_CONTAINER_TOPPADDING != 0) {
				raapi.freeReference(D_SHARP_CONTAINER_TOPPADDING);
				D_SHARP_CONTAINER_TOPPADDING = 0;
			}
	  		if (D_SHARP_CONTAINER_BOTTOMPADDING != 0) {
				raapi.freeReference(D_SHARP_CONTAINER_BOTTOMPADDING);
				D_SHARP_CONTAINER_BOTTOMPADDING = 0;
			}
	  		if (D_SHARP_CONTAINER_FOCUS != 0) {
				raapi.freeReference(D_SHARP_CONTAINER_FOCUS);
				D_SHARP_CONTAINER_FOCUS = 0;
			}
	  		if (D_SHARP_CONTAINER_COMPONENT != 0) {
				raapi.freeReference(D_SHARP_CONTAINER_COMPONENT);
				D_SHARP_CONTAINER_COMPONENT = 0;
			}
			if (D_SHARP_ROW != 0) {
				raapi.freeReference(D_SHARP_ROW);
				D_SHARP_ROW = 0;
			}
			if (D_SHARP_COLUMN != 0) {
				raapi.freeReference(D_SHARP_COLUMN);
				D_SHARP_COLUMN = 0;
			}
			if (D_SHARP_VERTICALBOX != 0) {
				raapi.freeReference(D_SHARP_VERTICALBOX);
				D_SHARP_VERTICALBOX = 0;
			}
			if (D_SHARP_GROUPBOX != 0) {
				raapi.freeReference(D_SHARP_GROUPBOX);
				D_SHARP_GROUPBOX = 0;
			}
	  		if (D_SHARP_GROUPBOX_CAPTION != 0) {
				raapi.freeReference(D_SHARP_GROUPBOX_CAPTION);
				D_SHARP_GROUPBOX_CAPTION = 0;
			}
			if (D_SHARP_FORM != 0) {
				raapi.freeReference(D_SHARP_FORM);
				D_SHARP_FORM = 0;
			}
	  		if (D_SHARP_FORM_CAPTION != 0) {
				raapi.freeReference(D_SHARP_FORM_CAPTION);
				D_SHARP_FORM_CAPTION = 0;
			}
	  		if (D_SHARP_FORM_BUTTONCLICKONCLOSE != 0) {
				raapi.freeReference(D_SHARP_FORM_BUTTONCLICKONCLOSE);
				D_SHARP_FORM_BUTTONCLICKONCLOSE = 0;
			}
	  		if (D_SHARP_FORM_HASMINIMIZEBUTTON != 0) {
				raapi.freeReference(D_SHARP_FORM_HASMINIMIZEBUTTON);
				D_SHARP_FORM_HASMINIMIZEBUTTON = 0;
			}
	  		if (D_SHARP_FORM_HASMAXIMIZEBUTTON != 0) {
				raapi.freeReference(D_SHARP_FORM_HASMAXIMIZEBUTTON);
				D_SHARP_FORM_HASMAXIMIZEBUTTON = 0;
			}
	  		if (D_SHARP_FORM_ISCLOSEBUTTONENABLED != 0) {
				raapi.freeReference(D_SHARP_FORM_ISCLOSEBUTTONENABLED);
				D_SHARP_FORM_ISCLOSEBUTTONENABLED = 0;
			}
	  		if (D_SHARP_FORM_LEFT != 0) {
				raapi.freeReference(D_SHARP_FORM_LEFT);
				D_SHARP_FORM_LEFT = 0;
			}
	  		if (D_SHARP_FORM_TOP != 0) {
				raapi.freeReference(D_SHARP_FORM_TOP);
				D_SHARP_FORM_TOP = 0;
			}
	  		if (D_SHARP_FORM_WIDTH != 0) {
				raapi.freeReference(D_SHARP_FORM_WIDTH);
				D_SHARP_FORM_WIDTH = 0;
			}
	  		if (D_SHARP_FORM_HEIGHT != 0) {
				raapi.freeReference(D_SHARP_FORM_HEIGHT);
				D_SHARP_FORM_HEIGHT = 0;
			}
	  		if (D_SHARP_FORM_EDITABLE != 0) {
				raapi.freeReference(D_SHARP_FORM_EDITABLE);
				D_SHARP_FORM_EDITABLE = 0;
			}
	  		if (D_SHARP_FORM_DEFAULTBUTTON != 0) {
				raapi.freeReference(D_SHARP_FORM_DEFAULTBUTTON);
				D_SHARP_FORM_DEFAULTBUTTON = 0;
			}
	  		if (D_SHARP_FORM_CANCELBUTTON != 0) {
				raapi.freeReference(D_SHARP_FORM_CANCELBUTTON);
				D_SHARP_FORM_CANCELBUTTON = 0;
			}
	  		if (D_SHARP_FORM_OWNEDEVENT != 0) {
				raapi.freeReference(D_SHARP_FORM_OWNEDEVENT);
				D_SHARP_FORM_OWNEDEVENT = 0;
			}
	  		if (D_SHARP_FORM_FOCUSED != 0) {
				raapi.freeReference(D_SHARP_FORM_FOCUSED);
				D_SHARP_FORM_FOCUSED = 0;
			}
	  		if (D_SHARP_FORM_CONSTANTFORMCALLINGBUTTON != 0) {
				raapi.freeReference(D_SHARP_FORM_CONSTANTFORMCALLINGBUTTON);
				D_SHARP_FORM_CONSTANTFORMCALLINGBUTTON = 0;
			}
	  		if (D_SHARP_FORM_FOCUSORDER != 0) {
				raapi.freeReference(D_SHARP_FORM_FOCUSORDER);
				D_SHARP_FORM_FOCUSORDER = 0;
			}
	  		if (D_SHARP_FORM_CALLINGBUTTON != 0) {
				raapi.freeReference(D_SHARP_FORM_CALLINGBUTTON);
				D_SHARP_FORM_CALLINGBUTTON = 0;
			}
	  		if (D_SHARP_FORM_FRAME != 0) {
				raapi.freeReference(D_SHARP_FORM_FRAME);
				D_SHARP_FORM_FRAME = 0;
			}
			if (D_SHARP_TABLEDIAGRAM != 0) {
				raapi.freeReference(D_SHARP_TABLEDIAGRAM);
				D_SHARP_TABLEDIAGRAM = 0;
			}
			if (D_SHARP_HORIZONTALBOX != 0) {
				raapi.freeReference(D_SHARP_HORIZONTALBOX);
				D_SHARP_HORIZONTALBOX = 0;
			}
			if (D_SHARP_HORIZONTALSCROLLBOX != 0) {
				raapi.freeReference(D_SHARP_HORIZONTALSCROLLBOX);
				D_SHARP_HORIZONTALSCROLLBOX = 0;
			}
			if (D_SHARP_VERTICALSCROLLBOX != 0) {
				raapi.freeReference(D_SHARP_VERTICALSCROLLBOX);
				D_SHARP_VERTICALSCROLLBOX = 0;
			}
			if (D_SHARP_SCROLLBOX != 0) {
				raapi.freeReference(D_SHARP_SCROLLBOX);
				D_SHARP_SCROLLBOX = 0;
			}
			if (D_SHARP_TAB != 0) {
				raapi.freeReference(D_SHARP_TAB);
				D_SHARP_TAB = 0;
			}
	  		if (D_SHARP_TAB_CAPTION != 0) {
				raapi.freeReference(D_SHARP_TAB_CAPTION);
				D_SHARP_TAB_CAPTION = 0;
			}
	  		if (D_SHARP_TAB_TABCHANGEEVENT != 0) {
				raapi.freeReference(D_SHARP_TAB_TABCHANGEEVENT);
				D_SHARP_TAB_TABCHANGEEVENT = 0;
			}
	  		if (D_SHARP_TAB_TABCONTAINER != 0) {
				raapi.freeReference(D_SHARP_TAB_TABCONTAINER);
				D_SHARP_TAB_TABCONTAINER = 0;
			}
			if (D_SHARP_STACK != 0) {
				raapi.freeReference(D_SHARP_STACK);
				D_SHARP_STACK = 0;
			}
			if (D_SHARP_TABCONTAINER != 0) {
				raapi.freeReference(D_SHARP_TABCONTAINER);
				D_SHARP_TABCONTAINER = 0;
			}
	  		if (D_SHARP_TABCONTAINER_ACTIVETAB != 0) {
				raapi.freeReference(D_SHARP_TABCONTAINER_ACTIVETAB);
				D_SHARP_TABCONTAINER_ACTIVETAB = 0;
			}
			if (D_SHARP_VERTICALSCROLLBOXWRAPPER != 0) {
				raapi.freeReference(D_SHARP_VERTICALSCROLLBOXWRAPPER);
				D_SHARP_VERTICALSCROLLBOXWRAPPER = 0;
			}
			if (D_SHARP_HORIZONTALSCROLLBOXWRAPPER != 0) {
				raapi.freeReference(D_SHARP_HORIZONTALSCROLLBOXWRAPPER);
				D_SHARP_HORIZONTALSCROLLBOXWRAPPER = 0;
			}
			if (D_SHARP_VERTICALSPLITBOX != 0) {
				raapi.freeReference(D_SHARP_VERTICALSPLITBOX);
				D_SHARP_VERTICALSPLITBOX = 0;
			}
			if (D_SHARP_HORIZONTALSPLITBOX != 0) {
				raapi.freeReference(D_SHARP_HORIZONTALSPLITBOX);
				D_SHARP_HORIZONTALSPLITBOX = 0;
			}
			if (D_SHARP_LABEL != 0) {
				raapi.freeReference(D_SHARP_LABEL);
				D_SHARP_LABEL = 0;
			}
	  		if (D_SHARP_LABEL_CAPTION != 0) {
				raapi.freeReference(D_SHARP_LABEL_CAPTION);
				D_SHARP_LABEL_CAPTION = 0;
			}
			if (D_SHARP_RADIOBUTTON != 0) {
				raapi.freeReference(D_SHARP_RADIOBUTTON);
				D_SHARP_RADIOBUTTON = 0;
			}
	  		if (D_SHARP_RADIOBUTTON_CAPTION != 0) {
				raapi.freeReference(D_SHARP_RADIOBUTTON_CAPTION);
				D_SHARP_RADIOBUTTON_CAPTION = 0;
			}
	  		if (D_SHARP_RADIOBUTTON_SELECTED != 0) {
				raapi.freeReference(D_SHARP_RADIOBUTTON_SELECTED);
				D_SHARP_RADIOBUTTON_SELECTED = 0;
			}
			if (D_SHARP_LISTBOX != 0) {
				raapi.freeReference(D_SHARP_LISTBOX);
				D_SHARP_LISTBOX = 0;
			}
	  		if (D_SHARP_LISTBOX_MULTISELECT != 0) {
				raapi.freeReference(D_SHARP_LISTBOX_MULTISELECT);
				D_SHARP_LISTBOX_MULTISELECT = 0;
			}
	  		if (D_SHARP_LISTBOX_ITEM != 0) {
				raapi.freeReference(D_SHARP_LISTBOX_ITEM);
				D_SHARP_LISTBOX_ITEM = 0;
			}
	  		if (D_SHARP_LISTBOX_SELECTED != 0) {
				raapi.freeReference(D_SHARP_LISTBOX_SELECTED);
				D_SHARP_LISTBOX_SELECTED = 0;
			}
			if (D_SHARP_VTABLETYPE != 0) {
				raapi.freeReference(D_SHARP_VTABLETYPE);
				D_SHARP_VTABLETYPE = 0;
			}
	  		if (D_SHARP_VTABLETYPE_EDITABLE != 0) {
				raapi.freeReference(D_SHARP_VTABLETYPE_EDITABLE);
				D_SHARP_VTABLETYPE_EDITABLE = 0;
			}
	  		if (D_SHARP_VTABLETYPE_MOVABLEROWS != 0) {
				raapi.freeReference(D_SHARP_VTABLETYPE_MOVABLEROWS);
				D_SHARP_VTABLETYPE_MOVABLEROWS = 0;
			}
	  		if (D_SHARP_VTABLETYPE_MOVABLECOLUMNS != 0) {
				raapi.freeReference(D_SHARP_VTABLETYPE_MOVABLECOLUMNS);
				D_SHARP_VTABLETYPE_MOVABLECOLUMNS = 0;
			}
	  		if (D_SHARP_VTABLETYPE_VERTICALALIGNMENT != 0) {
				raapi.freeReference(D_SHARP_VTABLETYPE_VERTICALALIGNMENT);
				D_SHARP_VTABLETYPE_VERTICALALIGNMENT = 0;
			}
	  		if (D_SHARP_VTABLETYPE_INSERTBUTTONCAPTION != 0) {
				raapi.freeReference(D_SHARP_VTABLETYPE_INSERTBUTTONCAPTION);
				D_SHARP_VTABLETYPE_INSERTBUTTONCAPTION = 0;
			}
	  		if (D_SHARP_VTABLETYPE_DELETEBUTTONCAPTION != 0) {
				raapi.freeReference(D_SHARP_VTABLETYPE_DELETEBUTTONCAPTION);
				D_SHARP_VTABLETYPE_DELETEBUTTONCAPTION = 0;
			}
	  		if (D_SHARP_VTABLETYPE_ROWHEIGHT != 0) {
				raapi.freeReference(D_SHARP_VTABLETYPE_ROWHEIGHT);
				D_SHARP_VTABLETYPE_ROWHEIGHT = 0;
			}
	  		if (D_SHARP_VTABLETYPE_AUTOORDERROWS != 0) {
				raapi.freeReference(D_SHARP_VTABLETYPE_AUTOORDERROWS);
				D_SHARP_VTABLETYPE_AUTOORDERROWS = 0;
			}
	  		if (D_SHARP_VTABLETYPE_AUTOORDERCOLUMNS != 0) {
				raapi.freeReference(D_SHARP_VTABLETYPE_AUTOORDERCOLUMNS);
				D_SHARP_VTABLETYPE_AUTOORDERCOLUMNS = 0;
			}
	  		if (D_SHARP_VTABLETYPE_VTABLE != 0) {
				raapi.freeReference(D_SHARP_VTABLETYPE_VTABLE);
				D_SHARP_VTABLETYPE_VTABLE = 0;
			}
	  		if (D_SHARP_VTABLETYPE_COLUMNTYPE != 0) {
				raapi.freeReference(D_SHARP_VTABLETYPE_COLUMNTYPE);
				D_SHARP_VTABLETYPE_COLUMNTYPE = 0;
			}
	  		if (D_SHARP_VTABLETYPE_COLUMN != 0) {
				raapi.freeReference(D_SHARP_VTABLETYPE_COLUMN);
				D_SHARP_VTABLETYPE_COLUMN = 0;
			}
	  		if (D_SHARP_VTABLETYPE_BUTTON != 0) {
				raapi.freeReference(D_SHARP_VTABLETYPE_BUTTON);
				D_SHARP_VTABLETYPE_BUTTON = 0;
			}
	  		if (D_SHARP_VTABLETYPE_COLUMNMOVEDEVENT != 0) {
				raapi.freeReference(D_SHARP_VTABLETYPE_COLUMNMOVEDEVENT);
				D_SHARP_VTABLETYPE_COLUMNMOVEDEVENT = 0;
			}
	  		if (D_SHARP_VTABLETYPE_BEFORECOLUMNMOVEDEVENT != 0) {
				raapi.freeReference(D_SHARP_VTABLETYPE_BEFORECOLUMNMOVEDEVENT);
				D_SHARP_VTABLETYPE_BEFORECOLUMNMOVEDEVENT = 0;
			}
	  		if (D_SHARP_VTABLETYPE_AFTERCOLUMNMOVEDEVENT != 0) {
				raapi.freeReference(D_SHARP_VTABLETYPE_AFTERCOLUMNMOVEDEVENT);
				D_SHARP_VTABLETYPE_AFTERCOLUMNMOVEDEVENT = 0;
			}
			if (D_SHARP_VTABLE != 0) {
				raapi.freeReference(D_SHARP_VTABLE);
				D_SHARP_VTABLE = 0;
			}
	  		if (D_SHARP_VTABLE_TYPE != 0) {
				raapi.freeReference(D_SHARP_VTABLE_TYPE);
				D_SHARP_VTABLE_TYPE = 0;
			}
	  		if (D_SHARP_VTABLE_VTABLEROW != 0) {
				raapi.freeReference(D_SHARP_VTABLE_VTABLEROW);
				D_SHARP_VTABLE_VTABLEROW = 0;
			}
	  		if (D_SHARP_VTABLE_COPYTOCLIPBOARD != 0) {
				raapi.freeReference(D_SHARP_VTABLE_COPYTOCLIPBOARD);
				D_SHARP_VTABLE_COPYTOCLIPBOARD = 0;
			}
	  		if (D_SHARP_VTABLE_SELECTEDROW != 0) {
				raapi.freeReference(D_SHARP_VTABLE_SELECTEDROW);
				D_SHARP_VTABLE_SELECTEDROW = 0;
			}
			if (D_SHARP_VTABLEROW != 0) {
				raapi.freeReference(D_SHARP_VTABLEROW);
				D_SHARP_VTABLEROW = 0;
			}
	  		if (D_SHARP_VTABLEROW_EDITED != 0) {
				raapi.freeReference(D_SHARP_VTABLEROW_EDITED);
				D_SHARP_VTABLEROW_EDITED = 0;
			}
	  		if (D_SHARP_VTABLEROW_INSERTED != 0) {
				raapi.freeReference(D_SHARP_VTABLEROW_INSERTED);
				D_SHARP_VTABLEROW_INSERTED = 0;
			}
	  		if (D_SHARP_VTABLEROW_DELETED != 0) {
				raapi.freeReference(D_SHARP_VTABLEROW_DELETED);
				D_SHARP_VTABLEROW_DELETED = 0;
			}
	  		if (D_SHARP_VTABLEROW_VERTICALALIGNMENT != 0) {
				raapi.freeReference(D_SHARP_VTABLEROW_VERTICALALIGNMENT);
				D_SHARP_VTABLEROW_VERTICALALIGNMENT = 0;
			}
	  		if (D_SHARP_VTABLEROW_VTABLE != 0) {
				raapi.freeReference(D_SHARP_VTABLEROW_VTABLE);
				D_SHARP_VTABLEROW_VTABLE = 0;
			}
	  		if (D_SHARP_VTABLEROW_VTABLECELL != 0) {
				raapi.freeReference(D_SHARP_VTABLEROW_VTABLECELL);
				D_SHARP_VTABLEROW_VTABLECELL = 0;
			}
	  		if (D_SHARP_VTABLEROW_ACTIVECELL != 0) {
				raapi.freeReference(D_SHARP_VTABLEROW_ACTIVECELL);
				D_SHARP_VTABLEROW_ACTIVECELL = 0;
			}
	  		if (D_SHARP_VTABLEROW_PARENTTABLE != 0) {
				raapi.freeReference(D_SHARP_VTABLEROW_PARENTTABLE);
				D_SHARP_VTABLEROW_PARENTTABLE = 0;
			}
	  		if (D_SHARP_VTABLEROW_ROWMOVEDEVENT != 0) {
				raapi.freeReference(D_SHARP_VTABLEROW_ROWMOVEDEVENT);
				D_SHARP_VTABLEROW_ROWMOVEDEVENT = 0;
			}
	  		if (D_SHARP_VTABLEROW_BEFOREROWMOVEDEVENT != 0) {
				raapi.freeReference(D_SHARP_VTABLEROW_BEFOREROWMOVEDEVENT);
				D_SHARP_VTABLEROW_BEFOREROWMOVEDEVENT = 0;
			}
	  		if (D_SHARP_VTABLEROW_AFTERROWMOVEDEVENT != 0) {
				raapi.freeReference(D_SHARP_VTABLEROW_AFTERROWMOVEDEVENT);
				D_SHARP_VTABLEROW_AFTERROWMOVEDEVENT = 0;
			}
			if (D_SHARP_TABLECOMPONENT != 0) {
				raapi.freeReference(D_SHARP_TABLECOMPONENT);
				D_SHARP_TABLECOMPONENT = 0;
			}
	  		if (D_SHARP_TABLECOMPONENT_OUTLINECOLOR != 0) {
				raapi.freeReference(D_SHARP_TABLECOMPONENT_OUTLINECOLOR);
				D_SHARP_TABLECOMPONENT_OUTLINECOLOR = 0;
			}
	  		if (D_SHARP_TABLECOMPONENT_VTABLECOLUMNTYPE != 0) {
				raapi.freeReference(D_SHARP_TABLECOMPONENT_VTABLECOLUMNTYPE);
				D_SHARP_TABLECOMPONENT_VTABLECOLUMNTYPE = 0;
			}
	  		if (D_SHARP_TABLECOMPONENT_VTABLECOLUMNTYPEOWNER != 0) {
				raapi.freeReference(D_SHARP_TABLECOMPONENT_VTABLECOLUMNTYPEOWNER);
				D_SHARP_TABLECOMPONENT_VTABLECOLUMNTYPEOWNER = 0;
			}
	  		if (D_SHARP_TABLECOMPONENT_VTABLECELL != 0) {
				raapi.freeReference(D_SHARP_TABLECOMPONENT_VTABLECELL);
				D_SHARP_TABLECOMPONENT_VTABLECELL = 0;
			}
	  		if (D_SHARP_TABLECOMPONENT_VTABLECELLOWNER != 0) {
				raapi.freeReference(D_SHARP_TABLECOMPONENT_VTABLECELLOWNER);
				D_SHARP_TABLECOMPONENT_VTABLECELLOWNER = 0;
			}
			if (D_SHARP_BUTTON != 0) {
				raapi.freeReference(D_SHARP_BUTTON);
				D_SHARP_BUTTON = 0;
			}
	  		if (D_SHARP_BUTTON_CAPTION != 0) {
				raapi.freeReference(D_SHARP_BUTTON_CAPTION);
				D_SHARP_BUTTON_CAPTION = 0;
			}
	  		if (D_SHARP_BUTTON_CLOSEONCLICK != 0) {
				raapi.freeReference(D_SHARP_BUTTON_CLOSEONCLICK);
				D_SHARP_BUTTON_CLOSEONCLICK = 0;
			}
	  		if (D_SHARP_BUTTON_DELETEONCLICK != 0) {
				raapi.freeReference(D_SHARP_BUTTON_DELETEONCLICK);
				D_SHARP_BUTTON_DELETEONCLICK = 0;
			}
	  		if (D_SHARP_BUTTON_DEFAULTBUTTONFORM != 0) {
				raapi.freeReference(D_SHARP_BUTTON_DEFAULTBUTTONFORM);
				D_SHARP_BUTTON_DEFAULTBUTTONFORM = 0;
			}
	  		if (D_SHARP_BUTTON_CANCELBUTTONFORM != 0) {
				raapi.freeReference(D_SHARP_BUTTON_CANCELBUTTONFORM);
				D_SHARP_BUTTON_CANCELBUTTONFORM = 0;
			}
	  		if (D_SHARP_BUTTON_CONSTANTFORMONCLICK != 0) {
				raapi.freeReference(D_SHARP_BUTTON_CONSTANTFORMONCLICK);
				D_SHARP_BUTTON_CONSTANTFORMONCLICK = 0;
			}
	  		if (D_SHARP_BUTTON_VTABLETYPE != 0) {
				raapi.freeReference(D_SHARP_BUTTON_VTABLETYPE);
				D_SHARP_BUTTON_VTABLETYPE = 0;
			}
	  		if (D_SHARP_BUTTON_FORMONCLICK != 0) {
				raapi.freeReference(D_SHARP_BUTTON_FORMONCLICK);
				D_SHARP_BUTTON_FORMONCLICK = 0;
			}
			if (D_SHARP_CHECKBOX != 0) {
				raapi.freeReference(D_SHARP_CHECKBOX);
				D_SHARP_CHECKBOX = 0;
			}
	  		if (D_SHARP_CHECKBOX_CAPTION != 0) {
				raapi.freeReference(D_SHARP_CHECKBOX_CAPTION);
				D_SHARP_CHECKBOX_CAPTION = 0;
			}
	  		if (D_SHARP_CHECKBOX_CHECKED != 0) {
				raapi.freeReference(D_SHARP_CHECKBOX_CHECKED);
				D_SHARP_CHECKBOX_CHECKED = 0;
			}
	  		if (D_SHARP_CHECKBOX_EDITABLE != 0) {
				raapi.freeReference(D_SHARP_CHECKBOX_EDITABLE);
				D_SHARP_CHECKBOX_EDITABLE = 0;
			}
			if (D_SHARP_COMBOBOX != 0) {
				raapi.freeReference(D_SHARP_COMBOBOX);
				D_SHARP_COMBOBOX = 0;
			}
	  		if (D_SHARP_COMBOBOX_TEXT != 0) {
				raapi.freeReference(D_SHARP_COMBOBOX_TEXT);
				D_SHARP_COMBOBOX_TEXT = 0;
			}
	  		if (D_SHARP_COMBOBOX_EDITABLE != 0) {
				raapi.freeReference(D_SHARP_COMBOBOX_EDITABLE);
				D_SHARP_COMBOBOX_EDITABLE = 0;
			}
	  		if (D_SHARP_COMBOBOX_ITEM != 0) {
				raapi.freeReference(D_SHARP_COMBOBOX_ITEM);
				D_SHARP_COMBOBOX_ITEM = 0;
			}
	  		if (D_SHARP_COMBOBOX_SELECTED != 0) {
				raapi.freeReference(D_SHARP_COMBOBOX_SELECTED);
				D_SHARP_COMBOBOX_SELECTED = 0;
			}
			if (D_SHARP_ITEM != 0) {
				raapi.freeReference(D_SHARP_ITEM);
				D_SHARP_ITEM = 0;
			}
	  		if (D_SHARP_ITEM_VALUE != 0) {
				raapi.freeReference(D_SHARP_ITEM_VALUE);
				D_SHARP_ITEM_VALUE = 0;
			}
	  		if (D_SHARP_ITEM_LISTBOX != 0) {
				raapi.freeReference(D_SHARP_ITEM_LISTBOX);
				D_SHARP_ITEM_LISTBOX = 0;
			}
	  		if (D_SHARP_ITEM_COMBOBOX != 0) {
				raapi.freeReference(D_SHARP_ITEM_COMBOBOX);
				D_SHARP_ITEM_COMBOBOX = 0;
			}
	  		if (D_SHARP_ITEM_PARENTCOMBOBOX != 0) {
				raapi.freeReference(D_SHARP_ITEM_PARENTCOMBOBOX);
				D_SHARP_ITEM_PARENTCOMBOBOX = 0;
			}
	  		if (D_SHARP_ITEM_PARENTLISTBOX != 0) {
				raapi.freeReference(D_SHARP_ITEM_PARENTLISTBOX);
				D_SHARP_ITEM_PARENTLISTBOX = 0;
			}
	  		if (D_SHARP_ITEM_VTABLECELL != 0) {
				raapi.freeReference(D_SHARP_ITEM_VTABLECELL);
				D_SHARP_ITEM_VTABLECELL = 0;
			}
	  		if (D_SHARP_ITEM_SLISTBOXCHANGEEVENT != 0) {
				raapi.freeReference(D_SHARP_ITEM_SLISTBOXCHANGEEVENT);
				D_SHARP_ITEM_SLISTBOXCHANGEEVENT = 0;
			}
	  		if (D_SHARP_ITEM_DLISTBOXCHANGEEVENT != 0) {
				raapi.freeReference(D_SHARP_ITEM_DLISTBOXCHANGEEVENT);
				D_SHARP_ITEM_DLISTBOXCHANGEEVENT = 0;
			}
			if (D_SHARP_IMAGE != 0) {
				raapi.freeReference(D_SHARP_IMAGE);
				D_SHARP_IMAGE = 0;
			}
	  		if (D_SHARP_IMAGE_FILENAME != 0) {
				raapi.freeReference(D_SHARP_IMAGE_FILENAME);
				D_SHARP_IMAGE_FILENAME = 0;
			}
	  		if (D_SHARP_IMAGE_EDITABLE != 0) {
				raapi.freeReference(D_SHARP_IMAGE_EDITABLE);
				D_SHARP_IMAGE_EDITABLE = 0;
			}
			if (D_SHARP_TEXTBOX != 0) {
				raapi.freeReference(D_SHARP_TEXTBOX);
				D_SHARP_TEXTBOX = 0;
			}
	  		if (D_SHARP_TEXTBOX_TEXT != 0) {
				raapi.freeReference(D_SHARP_TEXTBOX_TEXT);
				D_SHARP_TEXTBOX_TEXT = 0;
			}
	  		if (D_SHARP_TEXTBOX_MULTILINE != 0) {
				raapi.freeReference(D_SHARP_TEXTBOX_MULTILINE);
				D_SHARP_TEXTBOX_MULTILINE = 0;
			}
	  		if (D_SHARP_TEXTBOX_EDITABLE != 0) {
				raapi.freeReference(D_SHARP_TEXTBOX_EDITABLE);
				D_SHARP_TEXTBOX_EDITABLE = 0;
			}
			if (D_SHARP_INPUTFIELD != 0) {
				raapi.freeReference(D_SHARP_INPUTFIELD);
				D_SHARP_INPUTFIELD = 0;
			}
			if (D_SHARP_TEXTAREA != 0) {
				raapi.freeReference(D_SHARP_TEXTAREA);
				D_SHARP_TEXTAREA = 0;
			}
			if (D_SHARP_TREE != 0) {
				raapi.freeReference(D_SHARP_TREE);
				D_SHARP_TREE = 0;
			}
	  		if (D_SHARP_TREE_DRAGGABLENODES != 0) {
				raapi.freeReference(D_SHARP_TREE_DRAGGABLENODES);
				D_SHARP_TREE_DRAGGABLENODES = 0;
			}
	  		if (D_SHARP_TREE_TREENODE != 0) {
				raapi.freeReference(D_SHARP_TREE_TREENODE);
				D_SHARP_TREE_TREENODE = 0;
			}
	  		if (D_SHARP_TREE_SELECTED != 0) {
				raapi.freeReference(D_SHARP_TREE_SELECTED);
				D_SHARP_TREE_SELECTED = 0;
			}
			if (D_SHARP_MULTILINETEXTBOX != 0) {
				raapi.freeReference(D_SHARP_MULTILINETEXTBOX);
				D_SHARP_MULTILINETEXTBOX = 0;
			}
	  		if (D_SHARP_MULTILINETEXTBOX_TEXTLINE != 0) {
				raapi.freeReference(D_SHARP_MULTILINETEXTBOX_TEXTLINE);
				D_SHARP_MULTILINETEXTBOX_TEXTLINE = 0;
			}
	  		if (D_SHARP_MULTILINETEXTBOX_CURRENT != 0) {
				raapi.freeReference(D_SHARP_MULTILINETEXTBOX_CURRENT);
				D_SHARP_MULTILINETEXTBOX_CURRENT = 0;
			}
			if (D_SHARP_RICHTEXTAREA != 0) {
				raapi.freeReference(D_SHARP_RICHTEXTAREA);
				D_SHARP_RICHTEXTAREA = 0;
			}
	  		if (D_SHARP_RICHTEXTAREA_FILENAME != 0) {
				raapi.freeReference(D_SHARP_RICHTEXTAREA_FILENAME);
				D_SHARP_RICHTEXTAREA_FILENAME = 0;
			}
	  		if (D_SHARP_RICHTEXTAREA_ENCODEDCONTENT != 0) {
				raapi.freeReference(D_SHARP_RICHTEXTAREA_ENCODEDCONTENT);
				D_SHARP_RICHTEXTAREA_ENCODEDCONTENT = 0;
			}
			if (D_SHARP_PROGRESSBAR != 0) {
				raapi.freeReference(D_SHARP_PROGRESSBAR);
				D_SHARP_PROGRESSBAR = 0;
			}
	  		if (D_SHARP_PROGRESSBAR_POSITION != 0) {
				raapi.freeReference(D_SHARP_PROGRESSBAR_POSITION);
				D_SHARP_PROGRESSBAR_POSITION = 0;
			}
	  		if (D_SHARP_PROGRESSBAR_MINIMUMVALUE != 0) {
				raapi.freeReference(D_SHARP_PROGRESSBAR_MINIMUMVALUE);
				D_SHARP_PROGRESSBAR_MINIMUMVALUE = 0;
			}
	  		if (D_SHARP_PROGRESSBAR_MAXIMUMVALUE != 0) {
				raapi.freeReference(D_SHARP_PROGRESSBAR_MAXIMUMVALUE);
				D_SHARP_PROGRESSBAR_MAXIMUMVALUE = 0;
			}
			if (D_SHARP_IMAGEBUTTON != 0) {
				raapi.freeReference(D_SHARP_IMAGEBUTTON);
				D_SHARP_IMAGEBUTTON = 0;
			}
	  		if (D_SHARP_IMAGEBUTTON_FILENAME != 0) {
				raapi.freeReference(D_SHARP_IMAGEBUTTON_FILENAME);
				D_SHARP_IMAGEBUTTON_FILENAME = 0;
			}
	  		if (D_SHARP_IMAGEBUTTON_CARDINALPOINT != 0) {
				raapi.freeReference(D_SHARP_IMAGEBUTTON_CARDINALPOINT);
				D_SHARP_IMAGEBUTTON_CARDINALPOINT = 0;
			}
			if (D_SHARP_VTABLECOLUMNTYPE != 0) {
				raapi.freeReference(D_SHARP_VTABLECOLUMNTYPE);
				D_SHARP_VTABLECOLUMNTYPE = 0;
			}
	  		if (D_SHARP_VTABLECOLUMNTYPE_CAPTION != 0) {
				raapi.freeReference(D_SHARP_VTABLECOLUMNTYPE_CAPTION);
				D_SHARP_VTABLECOLUMNTYPE_CAPTION = 0;
			}
	  		if (D_SHARP_VTABLECOLUMNTYPE_DEFAULTVALUE != 0) {
				raapi.freeReference(D_SHARP_VTABLECOLUMNTYPE_DEFAULTVALUE);
				D_SHARP_VTABLECOLUMNTYPE_DEFAULTVALUE = 0;
			}
	  		if (D_SHARP_VTABLECOLUMNTYPE_EDITABLE != 0) {
				raapi.freeReference(D_SHARP_VTABLECOLUMNTYPE_EDITABLE);
				D_SHARP_VTABLECOLUMNTYPE_EDITABLE = 0;
			}
	  		if (D_SHARP_VTABLECOLUMNTYPE_WIDTH != 0) {
				raapi.freeReference(D_SHARP_VTABLECOLUMNTYPE_WIDTH);
				D_SHARP_VTABLECOLUMNTYPE_WIDTH = 0;
			}
	  		if (D_SHARP_VTABLECOLUMNTYPE_PREFERREDRELATIVEWIDTH != 0) {
				raapi.freeReference(D_SHARP_VTABLECOLUMNTYPE_PREFERREDRELATIVEWIDTH);
				D_SHARP_VTABLECOLUMNTYPE_PREFERREDRELATIVEWIDTH = 0;
			}
	  		if (D_SHARP_VTABLECOLUMNTYPE_HORIZONTALALIGNMENT != 0) {
				raapi.freeReference(D_SHARP_VTABLECOLUMNTYPE_HORIZONTALALIGNMENT);
				D_SHARP_VTABLECOLUMNTYPE_HORIZONTALALIGNMENT = 0;
			}
	  		if (D_SHARP_VTABLECOLUMNTYPE_VTABLETYPE != 0) {
				raapi.freeReference(D_SHARP_VTABLECOLUMNTYPE_VTABLETYPE);
				D_SHARP_VTABLECOLUMNTYPE_VTABLETYPE = 0;
			}
	  		if (D_SHARP_VTABLECOLUMNTYPE_VTABLETYPEOWNER != 0) {
				raapi.freeReference(D_SHARP_VTABLECOLUMNTYPE_VTABLETYPEOWNER);
				D_SHARP_VTABLECOLUMNTYPE_VTABLETYPEOWNER = 0;
			}
	  		if (D_SHARP_VTABLECOLUMNTYPE_VTABLECELL != 0) {
				raapi.freeReference(D_SHARP_VTABLECOLUMNTYPE_VTABLECELL);
				D_SHARP_VTABLECOLUMNTYPE_VTABLECELL = 0;
			}
	  		if (D_SHARP_VTABLECOLUMNTYPE_DEFAULTCOMPONENTTYPE != 0) {
				raapi.freeReference(D_SHARP_VTABLECOLUMNTYPE_DEFAULTCOMPONENTTYPE);
				D_SHARP_VTABLECOLUMNTYPE_DEFAULTCOMPONENTTYPE = 0;
			}
	  		if (D_SHARP_VTABLECOLUMNTYPE_DEFAULTCOMPONENT != 0) {
				raapi.freeReference(D_SHARP_VTABLECOLUMNTYPE_DEFAULTCOMPONENT);
				D_SHARP_VTABLECOLUMNTYPE_DEFAULTCOMPONENT = 0;
			}
			if (D_SHARP_VTABLECELL != 0) {
				raapi.freeReference(D_SHARP_VTABLECELL);
				D_SHARP_VTABLECELL = 0;
			}
	  		if (D_SHARP_VTABLECELL_VALUE != 0) {
				raapi.freeReference(D_SHARP_VTABLECELL_VALUE);
				D_SHARP_VTABLECELL_VALUE = 0;
			}
	  		if (D_SHARP_VTABLECELL_VTABLEROW != 0) {
				raapi.freeReference(D_SHARP_VTABLECELL_VTABLEROW);
				D_SHARP_VTABLECELL_VTABLEROW = 0;
			}
	  		if (D_SHARP_VTABLECELL_PARENTROW != 0) {
				raapi.freeReference(D_SHARP_VTABLECELL_PARENTROW);
				D_SHARP_VTABLECELL_PARENTROW = 0;
			}
	  		if (D_SHARP_VTABLECELL_SELECTEDITEM != 0) {
				raapi.freeReference(D_SHARP_VTABLECELL_SELECTEDITEM);
				D_SHARP_VTABLECELL_SELECTEDITEM = 0;
			}
	  		if (D_SHARP_VTABLECELL_VTABLECOLUMNTYPE != 0) {
				raapi.freeReference(D_SHARP_VTABLECELL_VTABLECOLUMNTYPE);
				D_SHARP_VTABLECELL_VTABLECOLUMNTYPE = 0;
			}
	  		if (D_SHARP_VTABLECELL_COMPONENTTYPE != 0) {
				raapi.freeReference(D_SHARP_VTABLECELL_COMPONENTTYPE);
				D_SHARP_VTABLECELL_COMPONENTTYPE = 0;
			}
	  		if (D_SHARP_VTABLECELL_COMPONENT != 0) {
				raapi.freeReference(D_SHARP_VTABLECELL_COMPONENT);
				D_SHARP_VTABLECELL_COMPONENT = 0;
			}
	  		if (D_SHARP_VTABLECELL_EVENT != 0) {
				raapi.freeReference(D_SHARP_VTABLECELL_EVENT);
				D_SHARP_VTABLECELL_EVENT = 0;
			}
			if (D_SHARP_EVENTHANDLER != 0) {
				raapi.freeReference(D_SHARP_EVENTHANDLER);
				D_SHARP_EVENTHANDLER = 0;
			}
	  		if (D_SHARP_EVENTHANDLER_EVENTNAME != 0) {
				raapi.freeReference(D_SHARP_EVENTHANDLER_EVENTNAME);
				D_SHARP_EVENTHANDLER_EVENTNAME = 0;
			}
	  		if (D_SHARP_EVENTHANDLER_TRANSFORMATIONNAME != 0) {
				raapi.freeReference(D_SHARP_EVENTHANDLER_TRANSFORMATIONNAME);
				D_SHARP_EVENTHANDLER_TRANSFORMATIONNAME = 0;
			}
	  		if (D_SHARP_EVENTHANDLER_PROCEDURENAME != 0) {
				raapi.freeReference(D_SHARP_EVENTHANDLER_PROCEDURENAME);
				D_SHARP_EVENTHANDLER_PROCEDURENAME = 0;
			}
	  		if (D_SHARP_EVENTHANDLER_EVENTSOURCE != 0) {
				raapi.freeReference(D_SHARP_EVENTHANDLER_EVENTSOURCE);
				D_SHARP_EVENTHANDLER_EVENTSOURCE = 0;
			}
			if (D_SHARP_COMMAND != 0) {
				raapi.freeReference(D_SHARP_COMMAND);
				D_SHARP_COMMAND = 0;
			}
	  		if (D_SHARP_COMMAND_INFO != 0) {
				raapi.freeReference(D_SHARP_COMMAND_INFO);
				D_SHARP_COMMAND_INFO = 0;
			}
	  		if (D_SHARP_COMMAND_RECEIVER != 0) {
				raapi.freeReference(D_SHARP_COMMAND_RECEIVER);
				D_SHARP_COMMAND_RECEIVER = 0;
			}
			if (D_SHARP_EVENT != 0) {
				raapi.freeReference(D_SHARP_EVENT);
				D_SHARP_EVENT = 0;
			}
	  		if (D_SHARP_EVENT_EVENTNAME != 0) {
				raapi.freeReference(D_SHARP_EVENT_EVENTNAME);
				D_SHARP_EVENT_EVENTNAME = 0;
			}
	  		if (D_SHARP_EVENT_INFO != 0) {
				raapi.freeReference(D_SHARP_EVENT_INFO);
				D_SHARP_EVENT_INFO = 0;
			}
	  		if (D_SHARP_EVENT_FORM != 0) {
				raapi.freeReference(D_SHARP_EVENT_FORM);
				D_SHARP_EVENT_FORM = 0;
			}
	  		if (D_SHARP_EVENT_SOURCE != 0) {
				raapi.freeReference(D_SHARP_EVENT_SOURCE);
				D_SHARP_EVENT_SOURCE = 0;
			}
	  		if (D_SHARP_EVENT_VTABLECELL != 0) {
				raapi.freeReference(D_SHARP_EVENT_VTABLECELL);
				D_SHARP_EVENT_VTABLECELL = 0;
			}
			if (D_SHARP_ROWMOVEDEVENT != 0) {
				raapi.freeReference(D_SHARP_ROWMOVEDEVENT);
				D_SHARP_ROWMOVEDEVENT = 0;
			}
	  		if (D_SHARP_ROWMOVEDEVENT_ROW != 0) {
				raapi.freeReference(D_SHARP_ROWMOVEDEVENT_ROW);
				D_SHARP_ROWMOVEDEVENT_ROW = 0;
			}
	  		if (D_SHARP_ROWMOVEDEVENT_BEFORE != 0) {
				raapi.freeReference(D_SHARP_ROWMOVEDEVENT_BEFORE);
				D_SHARP_ROWMOVEDEVENT_BEFORE = 0;
			}
	  		if (D_SHARP_ROWMOVEDEVENT_AFTER != 0) {
				raapi.freeReference(D_SHARP_ROWMOVEDEVENT_AFTER);
				D_SHARP_ROWMOVEDEVENT_AFTER = 0;
			}
			if (D_SHARP_COLUMNMOVEDEVENT != 0) {
				raapi.freeReference(D_SHARP_COLUMNMOVEDEVENT);
				D_SHARP_COLUMNMOVEDEVENT = 0;
			}
	  		if (D_SHARP_COLUMNMOVEDEVENT_COLUMN != 0) {
				raapi.freeReference(D_SHARP_COLUMNMOVEDEVENT_COLUMN);
				D_SHARP_COLUMNMOVEDEVENT_COLUMN = 0;
			}
	  		if (D_SHARP_COLUMNMOVEDEVENT_BEFORE != 0) {
				raapi.freeReference(D_SHARP_COLUMNMOVEDEVENT_BEFORE);
				D_SHARP_COLUMNMOVEDEVENT_BEFORE = 0;
			}
	  		if (D_SHARP_COLUMNMOVEDEVENT_AFTER != 0) {
				raapi.freeReference(D_SHARP_COLUMNMOVEDEVENT_AFTER);
				D_SHARP_COLUMNMOVEDEVENT_AFTER = 0;
			}
			if (D_SHARP_MULTILINETEXTBOXCHANGEEVENT != 0) {
				raapi.freeReference(D_SHARP_MULTILINETEXTBOXCHANGEEVENT);
				D_SHARP_MULTILINETEXTBOXCHANGEEVENT = 0;
			}
	  		if (D_SHARP_MULTILINETEXTBOXCHANGEEVENT_INSERTED != 0) {
				raapi.freeReference(D_SHARP_MULTILINETEXTBOXCHANGEEVENT_INSERTED);
				D_SHARP_MULTILINETEXTBOXCHANGEEVENT_INSERTED = 0;
			}
	  		if (D_SHARP_MULTILINETEXTBOXCHANGEEVENT_DELETED != 0) {
				raapi.freeReference(D_SHARP_MULTILINETEXTBOXCHANGEEVENT_DELETED);
				D_SHARP_MULTILINETEXTBOXCHANGEEVENT_DELETED = 0;
			}
	  		if (D_SHARP_MULTILINETEXTBOXCHANGEEVENT_EDITED != 0) {
				raapi.freeReference(D_SHARP_MULTILINETEXTBOXCHANGEEVENT_EDITED);
				D_SHARP_MULTILINETEXTBOXCHANGEEVENT_EDITED = 0;
			}
			if (D_SHARP_TEXTLINE != 0) {
				raapi.freeReference(D_SHARP_TEXTLINE);
				D_SHARP_TEXTLINE = 0;
			}
	  		if (D_SHARP_TEXTLINE_TEXT != 0) {
				raapi.freeReference(D_SHARP_TEXTLINE_TEXT);
				D_SHARP_TEXTLINE_TEXT = 0;
			}
	  		if (D_SHARP_TEXTLINE_INSERTED != 0) {
				raapi.freeReference(D_SHARP_TEXTLINE_INSERTED);
				D_SHARP_TEXTLINE_INSERTED = 0;
			}
	  		if (D_SHARP_TEXTLINE_DELETED != 0) {
				raapi.freeReference(D_SHARP_TEXTLINE_DELETED);
				D_SHARP_TEXTLINE_DELETED = 0;
			}
	  		if (D_SHARP_TEXTLINE_EDITED != 0) {
				raapi.freeReference(D_SHARP_TEXTLINE_EDITED);
				D_SHARP_TEXTLINE_EDITED = 0;
			}
	  		if (D_SHARP_TEXTLINE_MULTILINETEXTBOX != 0) {
				raapi.freeReference(D_SHARP_TEXTLINE_MULTILINETEXTBOX);
				D_SHARP_TEXTLINE_MULTILINETEXTBOX = 0;
			}
	  		if (D_SHARP_TEXTLINE_PARENTMULTILINETEXTBOX != 0) {
				raapi.freeReference(D_SHARP_TEXTLINE_PARENTMULTILINETEXTBOX);
				D_SHARP_TEXTLINE_PARENTMULTILINETEXTBOX = 0;
			}
	  		if (D_SHARP_TEXTLINE_IMULTILINETEXTBOXCHANGEEVENT != 0) {
				raapi.freeReference(D_SHARP_TEXTLINE_IMULTILINETEXTBOXCHANGEEVENT);
				D_SHARP_TEXTLINE_IMULTILINETEXTBOXCHANGEEVENT = 0;
			}
	  		if (D_SHARP_TEXTLINE_DMULTILINETEXTBOXCHANGEEVENT != 0) {
				raapi.freeReference(D_SHARP_TEXTLINE_DMULTILINETEXTBOXCHANGEEVENT);
				D_SHARP_TEXTLINE_DMULTILINETEXTBOXCHANGEEVENT = 0;
			}
	  		if (D_SHARP_TEXTLINE_EMULTILINETEXTBOXCHANGEEVENT != 0) {
				raapi.freeReference(D_SHARP_TEXTLINE_EMULTILINETEXTBOXCHANGEEVENT);
				D_SHARP_TEXTLINE_EMULTILINETEXTBOXCHANGEEVENT = 0;
			}
			if (D_SHARP_TREENODESELECTEVENT != 0) {
				raapi.freeReference(D_SHARP_TREENODESELECTEVENT);
				D_SHARP_TREENODESELECTEVENT = 0;
			}
	  		if (D_SHARP_TREENODESELECTEVENT_TREENODE != 0) {
				raapi.freeReference(D_SHARP_TREENODESELECTEVENT_TREENODE);
				D_SHARP_TREENODESELECTEVENT_TREENODE = 0;
			}
	  		if (D_SHARP_TREENODESELECTEVENT_PREVIOUS != 0) {
				raapi.freeReference(D_SHARP_TREENODESELECTEVENT_PREVIOUS);
				D_SHARP_TREENODESELECTEVENT_PREVIOUS = 0;
			}
			if (D_SHARP_ADDTREENODECMD != 0) {
				raapi.freeReference(D_SHARP_ADDTREENODECMD);
				D_SHARP_ADDTREENODECMD = 0;
			}
	  		if (D_SHARP_ADDTREENODECMD_TREENODE != 0) {
				raapi.freeReference(D_SHARP_ADDTREENODECMD_TREENODE);
				D_SHARP_ADDTREENODECMD_TREENODE = 0;
			}
	  		if (D_SHARP_ADDTREENODECMD_PARENT != 0) {
				raapi.freeReference(D_SHARP_ADDTREENODECMD_PARENT);
				D_SHARP_ADDTREENODECMD_PARENT = 0;
			}
	  		if (D_SHARP_ADDTREENODECMD_BEFORE != 0) {
				raapi.freeReference(D_SHARP_ADDTREENODECMD_BEFORE);
				D_SHARP_ADDTREENODECMD_BEFORE = 0;
			}
			if (D_SHARP_DELETETREENODECMD != 0) {
				raapi.freeReference(D_SHARP_DELETETREENODECMD);
				D_SHARP_DELETETREENODECMD = 0;
			}
	  		if (D_SHARP_DELETETREENODECMD_TREENODE != 0) {
				raapi.freeReference(D_SHARP_DELETETREENODECMD_TREENODE);
				D_SHARP_DELETETREENODECMD_TREENODE = 0;
			}
			if (D_SHARP_SELECTTREENODECMD != 0) {
				raapi.freeReference(D_SHARP_SELECTTREENODECMD);
				D_SHARP_SELECTTREENODECMD = 0;
			}
	  		if (D_SHARP_SELECTTREENODECMD_TREENODE != 0) {
				raapi.freeReference(D_SHARP_SELECTTREENODECMD_TREENODE);
				D_SHARP_SELECTTREENODECMD_TREENODE = 0;
			}
			if (D_SHARP_EXPANDTREENODECMD != 0) {
				raapi.freeReference(D_SHARP_EXPANDTREENODECMD);
				D_SHARP_EXPANDTREENODECMD = 0;
			}
	  		if (D_SHARP_EXPANDTREENODECMD_TREENODE != 0) {
				raapi.freeReference(D_SHARP_EXPANDTREENODECMD_TREENODE);
				D_SHARP_EXPANDTREENODECMD_TREENODE = 0;
			}
			if (D_SHARP_COLLAPSETREENODECMD != 0) {
				raapi.freeReference(D_SHARP_COLLAPSETREENODECMD);
				D_SHARP_COLLAPSETREENODECMD = 0;
			}
	  		if (D_SHARP_COLLAPSETREENODECMD_TREENODE != 0) {
				raapi.freeReference(D_SHARP_COLLAPSETREENODECMD_TREENODE);
				D_SHARP_COLLAPSETREENODECMD_TREENODE = 0;
			}
			if (D_SHARP_TREENODE != 0) {
				raapi.freeReference(D_SHARP_TREENODE);
				D_SHARP_TREENODE = 0;
			}
	  		if (D_SHARP_TREENODE_TEXT != 0) {
				raapi.freeReference(D_SHARP_TREENODE_TEXT);
				D_SHARP_TREENODE_TEXT = 0;
			}
	  		if (D_SHARP_TREENODE_EXPANDED != 0) {
				raapi.freeReference(D_SHARP_TREENODE_EXPANDED);
				D_SHARP_TREENODE_EXPANDED = 0;
			}
	  		if (D_SHARP_TREENODE_ID != 0) {
				raapi.freeReference(D_SHARP_TREENODE_ID);
				D_SHARP_TREENODE_ID = 0;
			}
	  		if (D_SHARP_TREENODE_TREE != 0) {
				raapi.freeReference(D_SHARP_TREENODE_TREE);
				D_SHARP_TREENODE_TREE = 0;
			}
	  		if (D_SHARP_TREENODE_PARENTTREE != 0) {
				raapi.freeReference(D_SHARP_TREENODE_PARENTTREE);
				D_SHARP_TREENODE_PARENTTREE = 0;
			}
	  		if (D_SHARP_TREENODE_CHILDNODE != 0) {
				raapi.freeReference(D_SHARP_TREENODE_CHILDNODE);
				D_SHARP_TREENODE_CHILDNODE = 0;
			}
	  		if (D_SHARP_TREENODE_PARENTNODE != 0) {
				raapi.freeReference(D_SHARP_TREENODE_PARENTNODE);
				D_SHARP_TREENODE_PARENTNODE = 0;
			}
	  		if (D_SHARP_TREENODE_TREENODESELECTEVENT != 0) {
				raapi.freeReference(D_SHARP_TREENODE_TREENODESELECTEVENT);
				D_SHARP_TREENODE_TREENODESELECTEVENT = 0;
			}
	  		if (D_SHARP_TREENODE_PTREENODESELECTEVENT != 0) {
				raapi.freeReference(D_SHARP_TREENODE_PTREENODESELECTEVENT);
				D_SHARP_TREENODE_PTREENODESELECTEVENT = 0;
			}
	  		if (D_SHARP_TREENODE_TADDTREENODECMD != 0) {
				raapi.freeReference(D_SHARP_TREENODE_TADDTREENODECMD);
				D_SHARP_TREENODE_TADDTREENODECMD = 0;
			}
	  		if (D_SHARP_TREENODE_PADDTREENODECMD != 0) {
				raapi.freeReference(D_SHARP_TREENODE_PADDTREENODECMD);
				D_SHARP_TREENODE_PADDTREENODECMD = 0;
			}
	  		if (D_SHARP_TREENODE_BADDTREENODECMD != 0) {
				raapi.freeReference(D_SHARP_TREENODE_BADDTREENODECMD);
				D_SHARP_TREENODE_BADDTREENODECMD = 0;
			}
	  		if (D_SHARP_TREENODE_DELETETREENODECMD != 0) {
				raapi.freeReference(D_SHARP_TREENODE_DELETETREENODECMD);
				D_SHARP_TREENODE_DELETETREENODECMD = 0;
			}
	  		if (D_SHARP_TREENODE_SELECTTREENODECMD != 0) {
				raapi.freeReference(D_SHARP_TREENODE_SELECTTREENODECMD);
				D_SHARP_TREENODE_SELECTTREENODECMD = 0;
			}
	  		if (D_SHARP_TREENODE_EXPANDNODECMD != 0) {
				raapi.freeReference(D_SHARP_TREENODE_EXPANDNODECMD);
				D_SHARP_TREENODE_EXPANDNODECMD = 0;
			}
	  		if (D_SHARP_TREENODE_COLLAPSETREENODECMD != 0) {
				raapi.freeReference(D_SHARP_TREENODE_COLLAPSETREENODECMD);
				D_SHARP_TREENODE_COLLAPSETREENODECMD = 0;
			}
	  		if (D_SHARP_TREENODE_TTREENODEMOVEEVENT != 0) {
				raapi.freeReference(D_SHARP_TREENODE_TTREENODEMOVEEVENT);
				D_SHARP_TREENODE_TTREENODEMOVEEVENT = 0;
			}
	  		if (D_SHARP_TREENODE_BTREENODEMOVEEVENT != 0) {
				raapi.freeReference(D_SHARP_TREENODE_BTREENODEMOVEEVENT);
				D_SHARP_TREENODE_BTREENODEMOVEEVENT = 0;
			}
	  		if (D_SHARP_TREENODE_ATREENODEMOVEEVENT != 0) {
				raapi.freeReference(D_SHARP_TREENODE_ATREENODEMOVEEVENT);
				D_SHARP_TREENODE_ATREENODEMOVEEVENT = 0;
			}
	  		if (D_SHARP_TREENODE_PTREENODEMOVEEVENT != 0) {
				raapi.freeReference(D_SHARP_TREENODE_PTREENODEMOVEEVENT);
				D_SHARP_TREENODE_PTREENODEMOVEEVENT = 0;
			}
	  		if (D_SHARP_TREENODE_TREENODEEXPANDEDEVENT != 0) {
				raapi.freeReference(D_SHARP_TREENODE_TREENODEEXPANDEDEVENT);
				D_SHARP_TREENODE_TREENODEEXPANDEDEVENT = 0;
			}
	  		if (D_SHARP_TREENODE_TREENODECOLLAPSEDEVENT != 0) {
				raapi.freeReference(D_SHARP_TREENODE_TREENODECOLLAPSEDEVENT);
				D_SHARP_TREENODE_TREENODECOLLAPSEDEVENT = 0;
			}
			if (D_SHARP_LISTBOXCHANGEEVENT != 0) {
				raapi.freeReference(D_SHARP_LISTBOXCHANGEEVENT);
				D_SHARP_LISTBOXCHANGEEVENT = 0;
			}
	  		if (D_SHARP_LISTBOXCHANGEEVENT_SELECTED != 0) {
				raapi.freeReference(D_SHARP_LISTBOXCHANGEEVENT_SELECTED);
				D_SHARP_LISTBOXCHANGEEVENT_SELECTED = 0;
			}
	  		if (D_SHARP_LISTBOXCHANGEEVENT_DESELECTED != 0) {
				raapi.freeReference(D_SHARP_LISTBOXCHANGEEVENT_DESELECTED);
				D_SHARP_LISTBOXCHANGEEVENT_DESELECTED = 0;
			}
			if (D_SHARP_TABCHANGEEVENT != 0) {
				raapi.freeReference(D_SHARP_TABCHANGEEVENT);
				D_SHARP_TABCHANGEEVENT = 0;
			}
	  		if (D_SHARP_TABCHANGEEVENT_TAB != 0) {
				raapi.freeReference(D_SHARP_TABCHANGEEVENT_TAB);
				D_SHARP_TABCHANGEEVENT_TAB = 0;
			}
			if (D_SHARP_TREENODEMOVEEVENT != 0) {
				raapi.freeReference(D_SHARP_TREENODEMOVEEVENT);
				D_SHARP_TREENODEMOVEEVENT = 0;
			}
	  		if (D_SHARP_TREENODEMOVEEVENT_TREENODE != 0) {
				raapi.freeReference(D_SHARP_TREENODEMOVEEVENT_TREENODE);
				D_SHARP_TREENODEMOVEEVENT_TREENODE = 0;
			}
	  		if (D_SHARP_TREENODEMOVEEVENT_WASBEFORE != 0) {
				raapi.freeReference(D_SHARP_TREENODEMOVEEVENT_WASBEFORE);
				D_SHARP_TREENODEMOVEEVENT_WASBEFORE = 0;
			}
	  		if (D_SHARP_TREENODEMOVEEVENT_WASAFTER != 0) {
				raapi.freeReference(D_SHARP_TREENODEMOVEEVENT_WASAFTER);
				D_SHARP_TREENODEMOVEEVENT_WASAFTER = 0;
			}
	  		if (D_SHARP_TREENODEMOVEEVENT_PREVIOUSPARENT != 0) {
				raapi.freeReference(D_SHARP_TREENODEMOVEEVENT_PREVIOUSPARENT);
				D_SHARP_TREENODEMOVEEVENT_PREVIOUSPARENT = 0;
			}
			if (D_SHARP_COPYTOCLIPBOARDCMD != 0) {
				raapi.freeReference(D_SHARP_COPYTOCLIPBOARDCMD);
				D_SHARP_COPYTOCLIPBOARDCMD = 0;
			}
	  		if (D_SHARP_COPYTOCLIPBOARDCMD_VTABLE != 0) {
				raapi.freeReference(D_SHARP_COPYTOCLIPBOARDCMD_VTABLE);
				D_SHARP_COPYTOCLIPBOARDCMD_VTABLE = 0;
			}
			if (D_SHARP_KEYDOWNEVENT != 0) {
				raapi.freeReference(D_SHARP_KEYDOWNEVENT);
				D_SHARP_KEYDOWNEVENT = 0;
			}
	  		if (D_SHARP_KEYDOWNEVENT_KEYNAME != 0) {
				raapi.freeReference(D_SHARP_KEYDOWNEVENT_KEYNAME);
				D_SHARP_KEYDOWNEVENT_KEYNAME = 0;
			}
			if (D_SHARP_TREENODEEXPANDEDEVENT != 0) {
				raapi.freeReference(D_SHARP_TREENODEEXPANDEDEVENT);
				D_SHARP_TREENODEEXPANDEDEVENT = 0;
			}
	  		if (D_SHARP_TREENODEEXPANDEDEVENT_TREENODE != 0) {
				raapi.freeReference(D_SHARP_TREENODEEXPANDEDEVENT_TREENODE);
				D_SHARP_TREENODEEXPANDEDEVENT_TREENODE = 0;
			}
			if (D_SHARP_TREENODECOLLAPSEDEVENT != 0) {
				raapi.freeReference(D_SHARP_TREENODECOLLAPSEDEVENT);
				D_SHARP_TREENODECOLLAPSEDEVENT = 0;
			}
	  		if (D_SHARP_TREENODECOLLAPSEDEVENT_TREENODE != 0) {
				raapi.freeReference(D_SHARP_TREENODECOLLAPSEDEVENT_TREENODE);
				D_SHARP_TREENODECOLLAPSEDEVENT_TREENODE = 0;
			}
			if (COMMAND != 0) {
				raapi.freeReference(COMMAND);
				COMMAND = 0;
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
	  		if (FRAME_FORM != 0) {
				raapi.freeReference(FRAME_FORM);
				FRAME_FORM = 0;
			}
		}

		raapi = _raapi;

		if (raapi != null) {
			// initializing class references...
			D_SHARP_GROUP = raapi.findClass("D#Group");
			if ((D_SHARP_GROUP == 0) && (prefix != null))
				D_SHARP_GROUP = raapi.findClass(prefix+"D#Group");
			if ((D_SHARP_GROUP == 0) && insertMetamodel)
				D_SHARP_GROUP = raapi.createClass(prefix+"D#Group");
			if (D_SHARP_GROUP == 0) {				
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the class D#Group.");
			}
			D_SHARP_EVENTSOURCE = raapi.findClass("D#EventSource");
			if ((D_SHARP_EVENTSOURCE == 0) && (prefix != null))
				D_SHARP_EVENTSOURCE = raapi.findClass(prefix+"D#EventSource");
			if ((D_SHARP_EVENTSOURCE == 0) && insertMetamodel)
				D_SHARP_EVENTSOURCE = raapi.createClass(prefix+"D#EventSource");
			if (D_SHARP_EVENTSOURCE == 0) {				
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the class D#EventSource.");
			}
			D_SHARP_COMPONENT = raapi.findClass("D#Component");
			if ((D_SHARP_COMPONENT == 0) && (prefix != null))
				D_SHARP_COMPONENT = raapi.findClass(prefix+"D#Component");
			if ((D_SHARP_COMPONENT == 0) && insertMetamodel)
				D_SHARP_COMPONENT = raapi.createClass(prefix+"D#Component");
			if (D_SHARP_COMPONENT == 0) {				
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the class D#Component.");
			}
			D_SHARP_CONTAINER = raapi.findClass("D#Container");
			if ((D_SHARP_CONTAINER == 0) && (prefix != null))
				D_SHARP_CONTAINER = raapi.findClass(prefix+"D#Container");
			if ((D_SHARP_CONTAINER == 0) && insertMetamodel)
				D_SHARP_CONTAINER = raapi.createClass(prefix+"D#Container");
			if (D_SHARP_CONTAINER == 0) {				
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the class D#Container.");
			}
			D_SHARP_ROW = raapi.findClass("D#Row");
			if ((D_SHARP_ROW == 0) && (prefix != null))
				D_SHARP_ROW = raapi.findClass(prefix+"D#Row");
			if ((D_SHARP_ROW == 0) && insertMetamodel)
				D_SHARP_ROW = raapi.createClass(prefix+"D#Row");
			if (D_SHARP_ROW == 0) {				
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the class D#Row.");
			}
			D_SHARP_COLUMN = raapi.findClass("D#Column");
			if ((D_SHARP_COLUMN == 0) && (prefix != null))
				D_SHARP_COLUMN = raapi.findClass(prefix+"D#Column");
			if ((D_SHARP_COLUMN == 0) && insertMetamodel)
				D_SHARP_COLUMN = raapi.createClass(prefix+"D#Column");
			if (D_SHARP_COLUMN == 0) {				
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the class D#Column.");
			}
			D_SHARP_VERTICALBOX = raapi.findClass("D#VerticalBox");
			if ((D_SHARP_VERTICALBOX == 0) && (prefix != null))
				D_SHARP_VERTICALBOX = raapi.findClass(prefix+"D#VerticalBox");
			if ((D_SHARP_VERTICALBOX == 0) && insertMetamodel)
				D_SHARP_VERTICALBOX = raapi.createClass(prefix+"D#VerticalBox");
			if (D_SHARP_VERTICALBOX == 0) {				
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the class D#VerticalBox.");
			}
			D_SHARP_GROUPBOX = raapi.findClass("D#GroupBox");
			if ((D_SHARP_GROUPBOX == 0) && (prefix != null))
				D_SHARP_GROUPBOX = raapi.findClass(prefix+"D#GroupBox");
			if ((D_SHARP_GROUPBOX == 0) && insertMetamodel)
				D_SHARP_GROUPBOX = raapi.createClass(prefix+"D#GroupBox");
			if (D_SHARP_GROUPBOX == 0) {				
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the class D#GroupBox.");
			}
			D_SHARP_FORM = raapi.findClass("D#Form");
			if ((D_SHARP_FORM == 0) && (prefix != null))
				D_SHARP_FORM = raapi.findClass(prefix+"D#Form");
			if ((D_SHARP_FORM == 0) && insertMetamodel)
				D_SHARP_FORM = raapi.createClass(prefix+"D#Form");
			if (D_SHARP_FORM == 0) {				
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the class D#Form.");
			}
			D_SHARP_TABLEDIAGRAM = raapi.findClass("D#TableDiagram");
			if ((D_SHARP_TABLEDIAGRAM == 0) && (prefix != null))
				D_SHARP_TABLEDIAGRAM = raapi.findClass(prefix+"D#TableDiagram");
			if ((D_SHARP_TABLEDIAGRAM == 0) && insertMetamodel)
				D_SHARP_TABLEDIAGRAM = raapi.createClass(prefix+"D#TableDiagram");
			if (D_SHARP_TABLEDIAGRAM == 0) {				
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the class D#TableDiagram.");
			}
			D_SHARP_HORIZONTALBOX = raapi.findClass("D#HorizontalBox");
			if ((D_SHARP_HORIZONTALBOX == 0) && (prefix != null))
				D_SHARP_HORIZONTALBOX = raapi.findClass(prefix+"D#HorizontalBox");
			if ((D_SHARP_HORIZONTALBOX == 0) && insertMetamodel)
				D_SHARP_HORIZONTALBOX = raapi.createClass(prefix+"D#HorizontalBox");
			if (D_SHARP_HORIZONTALBOX == 0) {				
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the class D#HorizontalBox.");
			}
			D_SHARP_HORIZONTALSCROLLBOX = raapi.findClass("D#HorizontalScrollBox");
			if ((D_SHARP_HORIZONTALSCROLLBOX == 0) && (prefix != null))
				D_SHARP_HORIZONTALSCROLLBOX = raapi.findClass(prefix+"D#HorizontalScrollBox");
			if ((D_SHARP_HORIZONTALSCROLLBOX == 0) && insertMetamodel)
				D_SHARP_HORIZONTALSCROLLBOX = raapi.createClass(prefix+"D#HorizontalScrollBox");
			if (D_SHARP_HORIZONTALSCROLLBOX == 0) {				
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the class D#HorizontalScrollBox.");
			}
			D_SHARP_VERTICALSCROLLBOX = raapi.findClass("D#VerticalScrollBox");
			if ((D_SHARP_VERTICALSCROLLBOX == 0) && (prefix != null))
				D_SHARP_VERTICALSCROLLBOX = raapi.findClass(prefix+"D#VerticalScrollBox");
			if ((D_SHARP_VERTICALSCROLLBOX == 0) && insertMetamodel)
				D_SHARP_VERTICALSCROLLBOX = raapi.createClass(prefix+"D#VerticalScrollBox");
			if (D_SHARP_VERTICALSCROLLBOX == 0) {				
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the class D#VerticalScrollBox.");
			}
			D_SHARP_SCROLLBOX = raapi.findClass("D#ScrollBox");
			if ((D_SHARP_SCROLLBOX == 0) && (prefix != null))
				D_SHARP_SCROLLBOX = raapi.findClass(prefix+"D#ScrollBox");
			if ((D_SHARP_SCROLLBOX == 0) && insertMetamodel)
				D_SHARP_SCROLLBOX = raapi.createClass(prefix+"D#ScrollBox");
			if (D_SHARP_SCROLLBOX == 0) {				
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the class D#ScrollBox.");
			}
			D_SHARP_TAB = raapi.findClass("D#Tab");
			if ((D_SHARP_TAB == 0) && (prefix != null))
				D_SHARP_TAB = raapi.findClass(prefix+"D#Tab");
			if ((D_SHARP_TAB == 0) && insertMetamodel)
				D_SHARP_TAB = raapi.createClass(prefix+"D#Tab");
			if (D_SHARP_TAB == 0) {				
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the class D#Tab.");
			}
			D_SHARP_STACK = raapi.findClass("D#Stack");
			if ((D_SHARP_STACK == 0) && (prefix != null))
				D_SHARP_STACK = raapi.findClass(prefix+"D#Stack");
			if ((D_SHARP_STACK == 0) && insertMetamodel)
				D_SHARP_STACK = raapi.createClass(prefix+"D#Stack");
			if (D_SHARP_STACK == 0) {				
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the class D#Stack.");
			}
			D_SHARP_TABCONTAINER = raapi.findClass("D#TabContainer");
			if ((D_SHARP_TABCONTAINER == 0) && (prefix != null))
				D_SHARP_TABCONTAINER = raapi.findClass(prefix+"D#TabContainer");
			if ((D_SHARP_TABCONTAINER == 0) && insertMetamodel)
				D_SHARP_TABCONTAINER = raapi.createClass(prefix+"D#TabContainer");
			if (D_SHARP_TABCONTAINER == 0) {				
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the class D#TabContainer.");
			}
			D_SHARP_VERTICALSCROLLBOXWRAPPER = raapi.findClass("D#VerticalScrollBoxWrapper");
			if ((D_SHARP_VERTICALSCROLLBOXWRAPPER == 0) && (prefix != null))
				D_SHARP_VERTICALSCROLLBOXWRAPPER = raapi.findClass(prefix+"D#VerticalScrollBoxWrapper");
			if ((D_SHARP_VERTICALSCROLLBOXWRAPPER == 0) && insertMetamodel)
				D_SHARP_VERTICALSCROLLBOXWRAPPER = raapi.createClass(prefix+"D#VerticalScrollBoxWrapper");
			if (D_SHARP_VERTICALSCROLLBOXWRAPPER == 0) {				
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the class D#VerticalScrollBoxWrapper.");
			}
			D_SHARP_HORIZONTALSCROLLBOXWRAPPER = raapi.findClass("D#HorizontalScrollBoxWrapper");
			if ((D_SHARP_HORIZONTALSCROLLBOXWRAPPER == 0) && (prefix != null))
				D_SHARP_HORIZONTALSCROLLBOXWRAPPER = raapi.findClass(prefix+"D#HorizontalScrollBoxWrapper");
			if ((D_SHARP_HORIZONTALSCROLLBOXWRAPPER == 0) && insertMetamodel)
				D_SHARP_HORIZONTALSCROLLBOXWRAPPER = raapi.createClass(prefix+"D#HorizontalScrollBoxWrapper");
			if (D_SHARP_HORIZONTALSCROLLBOXWRAPPER == 0) {				
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the class D#HorizontalScrollBoxWrapper.");
			}
			D_SHARP_VERTICALSPLITBOX = raapi.findClass("D#VerticalSplitBox");
			if ((D_SHARP_VERTICALSPLITBOX == 0) && (prefix != null))
				D_SHARP_VERTICALSPLITBOX = raapi.findClass(prefix+"D#VerticalSplitBox");
			if ((D_SHARP_VERTICALSPLITBOX == 0) && insertMetamodel)
				D_SHARP_VERTICALSPLITBOX = raapi.createClass(prefix+"D#VerticalSplitBox");
			if (D_SHARP_VERTICALSPLITBOX == 0) {				
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the class D#VerticalSplitBox.");
			}
			D_SHARP_HORIZONTALSPLITBOX = raapi.findClass("D#HorizontalSplitBox");
			if ((D_SHARP_HORIZONTALSPLITBOX == 0) && (prefix != null))
				D_SHARP_HORIZONTALSPLITBOX = raapi.findClass(prefix+"D#HorizontalSplitBox");
			if ((D_SHARP_HORIZONTALSPLITBOX == 0) && insertMetamodel)
				D_SHARP_HORIZONTALSPLITBOX = raapi.createClass(prefix+"D#HorizontalSplitBox");
			if (D_SHARP_HORIZONTALSPLITBOX == 0) {				
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the class D#HorizontalSplitBox.");
			}
			D_SHARP_LABEL = raapi.findClass("D#Label");
			if ((D_SHARP_LABEL == 0) && (prefix != null))
				D_SHARP_LABEL = raapi.findClass(prefix+"D#Label");
			if ((D_SHARP_LABEL == 0) && insertMetamodel)
				D_SHARP_LABEL = raapi.createClass(prefix+"D#Label");
			if (D_SHARP_LABEL == 0) {				
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the class D#Label.");
			}
			D_SHARP_RADIOBUTTON = raapi.findClass("D#RadioButton");
			if ((D_SHARP_RADIOBUTTON == 0) && (prefix != null))
				D_SHARP_RADIOBUTTON = raapi.findClass(prefix+"D#RadioButton");
			if ((D_SHARP_RADIOBUTTON == 0) && insertMetamodel)
				D_SHARP_RADIOBUTTON = raapi.createClass(prefix+"D#RadioButton");
			if (D_SHARP_RADIOBUTTON == 0) {				
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the class D#RadioButton.");
			}
			D_SHARP_LISTBOX = raapi.findClass("D#ListBox");
			if ((D_SHARP_LISTBOX == 0) && (prefix != null))
				D_SHARP_LISTBOX = raapi.findClass(prefix+"D#ListBox");
			if ((D_SHARP_LISTBOX == 0) && insertMetamodel)
				D_SHARP_LISTBOX = raapi.createClass(prefix+"D#ListBox");
			if (D_SHARP_LISTBOX == 0) {				
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the class D#ListBox.");
			}
			D_SHARP_VTABLETYPE = raapi.findClass("D#VTableType");
			if ((D_SHARP_VTABLETYPE == 0) && (prefix != null))
				D_SHARP_VTABLETYPE = raapi.findClass(prefix+"D#VTableType");
			if ((D_SHARP_VTABLETYPE == 0) && insertMetamodel)
				D_SHARP_VTABLETYPE = raapi.createClass(prefix+"D#VTableType");
			if (D_SHARP_VTABLETYPE == 0) {				
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the class D#VTableType.");
			}
			D_SHARP_VTABLE = raapi.findClass("D#VTable");
			if ((D_SHARP_VTABLE == 0) && (prefix != null))
				D_SHARP_VTABLE = raapi.findClass(prefix+"D#VTable");
			if ((D_SHARP_VTABLE == 0) && insertMetamodel)
				D_SHARP_VTABLE = raapi.createClass(prefix+"D#VTable");
			if (D_SHARP_VTABLE == 0) {				
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the class D#VTable.");
			}
			D_SHARP_VTABLEROW = raapi.findClass("D#VTableRow");
			if ((D_SHARP_VTABLEROW == 0) && (prefix != null))
				D_SHARP_VTABLEROW = raapi.findClass(prefix+"D#VTableRow");
			if ((D_SHARP_VTABLEROW == 0) && insertMetamodel)
				D_SHARP_VTABLEROW = raapi.createClass(prefix+"D#VTableRow");
			if (D_SHARP_VTABLEROW == 0) {				
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the class D#VTableRow.");
			}
			D_SHARP_TABLECOMPONENT = raapi.findClass("D#TableComponent");
			if ((D_SHARP_TABLECOMPONENT == 0) && (prefix != null))
				D_SHARP_TABLECOMPONENT = raapi.findClass(prefix+"D#TableComponent");
			if ((D_SHARP_TABLECOMPONENT == 0) && insertMetamodel)
				D_SHARP_TABLECOMPONENT = raapi.createClass(prefix+"D#TableComponent");
			if (D_SHARP_TABLECOMPONENT == 0) {				
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the class D#TableComponent.");
			}
			D_SHARP_BUTTON = raapi.findClass("D#Button");
			if ((D_SHARP_BUTTON == 0) && (prefix != null))
				D_SHARP_BUTTON = raapi.findClass(prefix+"D#Button");
			if ((D_SHARP_BUTTON == 0) && insertMetamodel)
				D_SHARP_BUTTON = raapi.createClass(prefix+"D#Button");
			if (D_SHARP_BUTTON == 0) {				
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the class D#Button.");
			}
			D_SHARP_CHECKBOX = raapi.findClass("D#CheckBox");
			if ((D_SHARP_CHECKBOX == 0) && (prefix != null))
				D_SHARP_CHECKBOX = raapi.findClass(prefix+"D#CheckBox");
			if ((D_SHARP_CHECKBOX == 0) && insertMetamodel)
				D_SHARP_CHECKBOX = raapi.createClass(prefix+"D#CheckBox");
			if (D_SHARP_CHECKBOX == 0) {				
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the class D#CheckBox.");
			}
			D_SHARP_COMBOBOX = raapi.findClass("D#ComboBox");
			if ((D_SHARP_COMBOBOX == 0) && (prefix != null))
				D_SHARP_COMBOBOX = raapi.findClass(prefix+"D#ComboBox");
			if ((D_SHARP_COMBOBOX == 0) && insertMetamodel)
				D_SHARP_COMBOBOX = raapi.createClass(prefix+"D#ComboBox");
			if (D_SHARP_COMBOBOX == 0) {				
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the class D#ComboBox.");
			}
			D_SHARP_ITEM = raapi.findClass("D#Item");
			if ((D_SHARP_ITEM == 0) && (prefix != null))
				D_SHARP_ITEM = raapi.findClass(prefix+"D#Item");
			if ((D_SHARP_ITEM == 0) && insertMetamodel)
				D_SHARP_ITEM = raapi.createClass(prefix+"D#Item");
			if (D_SHARP_ITEM == 0) {				
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the class D#Item.");
			}
			D_SHARP_IMAGE = raapi.findClass("D#Image");
			if ((D_SHARP_IMAGE == 0) && (prefix != null))
				D_SHARP_IMAGE = raapi.findClass(prefix+"D#Image");
			if ((D_SHARP_IMAGE == 0) && insertMetamodel)
				D_SHARP_IMAGE = raapi.createClass(prefix+"D#Image");
			if (D_SHARP_IMAGE == 0) {				
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the class D#Image.");
			}
			D_SHARP_TEXTBOX = raapi.findClass("D#TextBox");
			if ((D_SHARP_TEXTBOX == 0) && (prefix != null))
				D_SHARP_TEXTBOX = raapi.findClass(prefix+"D#TextBox");
			if ((D_SHARP_TEXTBOX == 0) && insertMetamodel)
				D_SHARP_TEXTBOX = raapi.createClass(prefix+"D#TextBox");
			if (D_SHARP_TEXTBOX == 0) {				
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the class D#TextBox.");
			}
			D_SHARP_INPUTFIELD = raapi.findClass("D#InputField");
			if ((D_SHARP_INPUTFIELD == 0) && (prefix != null))
				D_SHARP_INPUTFIELD = raapi.findClass(prefix+"D#InputField");
			if ((D_SHARP_INPUTFIELD == 0) && insertMetamodel)
				D_SHARP_INPUTFIELD = raapi.createClass(prefix+"D#InputField");
			if (D_SHARP_INPUTFIELD == 0) {				
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the class D#InputField.");
			}
			D_SHARP_TEXTAREA = raapi.findClass("D#TextArea");
			if ((D_SHARP_TEXTAREA == 0) && (prefix != null))
				D_SHARP_TEXTAREA = raapi.findClass(prefix+"D#TextArea");
			if ((D_SHARP_TEXTAREA == 0) && insertMetamodel)
				D_SHARP_TEXTAREA = raapi.createClass(prefix+"D#TextArea");
			if (D_SHARP_TEXTAREA == 0) {				
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the class D#TextArea.");
			}
			D_SHARP_TREE = raapi.findClass("D#Tree");
			if ((D_SHARP_TREE == 0) && (prefix != null))
				D_SHARP_TREE = raapi.findClass(prefix+"D#Tree");
			if ((D_SHARP_TREE == 0) && insertMetamodel)
				D_SHARP_TREE = raapi.createClass(prefix+"D#Tree");
			if (D_SHARP_TREE == 0) {				
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the class D#Tree.");
			}
			D_SHARP_MULTILINETEXTBOX = raapi.findClass("D#MultiLineTextBox");
			if ((D_SHARP_MULTILINETEXTBOX == 0) && (prefix != null))
				D_SHARP_MULTILINETEXTBOX = raapi.findClass(prefix+"D#MultiLineTextBox");
			if ((D_SHARP_MULTILINETEXTBOX == 0) && insertMetamodel)
				D_SHARP_MULTILINETEXTBOX = raapi.createClass(prefix+"D#MultiLineTextBox");
			if (D_SHARP_MULTILINETEXTBOX == 0) {				
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the class D#MultiLineTextBox.");
			}
			D_SHARP_RICHTEXTAREA = raapi.findClass("D#RichTextArea");
			if ((D_SHARP_RICHTEXTAREA == 0) && (prefix != null))
				D_SHARP_RICHTEXTAREA = raapi.findClass(prefix+"D#RichTextArea");
			if ((D_SHARP_RICHTEXTAREA == 0) && insertMetamodel)
				D_SHARP_RICHTEXTAREA = raapi.createClass(prefix+"D#RichTextArea");
			if (D_SHARP_RICHTEXTAREA == 0) {				
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the class D#RichTextArea.");
			}
			D_SHARP_PROGRESSBAR = raapi.findClass("D#ProgressBar");
			if ((D_SHARP_PROGRESSBAR == 0) && (prefix != null))
				D_SHARP_PROGRESSBAR = raapi.findClass(prefix+"D#ProgressBar");
			if ((D_SHARP_PROGRESSBAR == 0) && insertMetamodel)
				D_SHARP_PROGRESSBAR = raapi.createClass(prefix+"D#ProgressBar");
			if (D_SHARP_PROGRESSBAR == 0) {				
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the class D#ProgressBar.");
			}
			D_SHARP_IMAGEBUTTON = raapi.findClass("D#ImageButton");
			if ((D_SHARP_IMAGEBUTTON == 0) && (prefix != null))
				D_SHARP_IMAGEBUTTON = raapi.findClass(prefix+"D#ImageButton");
			if ((D_SHARP_IMAGEBUTTON == 0) && insertMetamodel)
				D_SHARP_IMAGEBUTTON = raapi.createClass(prefix+"D#ImageButton");
			if (D_SHARP_IMAGEBUTTON == 0) {				
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the class D#ImageButton.");
			}
			D_SHARP_VTABLECOLUMNTYPE = raapi.findClass("D#VTableColumnType");
			if ((D_SHARP_VTABLECOLUMNTYPE == 0) && (prefix != null))
				D_SHARP_VTABLECOLUMNTYPE = raapi.findClass(prefix+"D#VTableColumnType");
			if ((D_SHARP_VTABLECOLUMNTYPE == 0) && insertMetamodel)
				D_SHARP_VTABLECOLUMNTYPE = raapi.createClass(prefix+"D#VTableColumnType");
			if (D_SHARP_VTABLECOLUMNTYPE == 0) {				
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the class D#VTableColumnType.");
			}
			D_SHARP_VTABLECELL = raapi.findClass("D#VTableCell");
			if ((D_SHARP_VTABLECELL == 0) && (prefix != null))
				D_SHARP_VTABLECELL = raapi.findClass(prefix+"D#VTableCell");
			if ((D_SHARP_VTABLECELL == 0) && insertMetamodel)
				D_SHARP_VTABLECELL = raapi.createClass(prefix+"D#VTableCell");
			if (D_SHARP_VTABLECELL == 0) {				
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the class D#VTableCell.");
			}
			D_SHARP_EVENTHANDLER = raapi.findClass("D#EventHandler");
			if ((D_SHARP_EVENTHANDLER == 0) && (prefix != null))
				D_SHARP_EVENTHANDLER = raapi.findClass(prefix+"D#EventHandler");
			if ((D_SHARP_EVENTHANDLER == 0) && insertMetamodel)
				D_SHARP_EVENTHANDLER = raapi.createClass(prefix+"D#EventHandler");
			if (D_SHARP_EVENTHANDLER == 0) {				
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the class D#EventHandler.");
			}
			D_SHARP_COMMAND = raapi.findClass("D#Command");
			if ((D_SHARP_COMMAND == 0) && (prefix != null))
				D_SHARP_COMMAND = raapi.findClass(prefix+"D#Command");
			if ((D_SHARP_COMMAND == 0) && insertMetamodel)
				D_SHARP_COMMAND = raapi.createClass(prefix+"D#Command");
			if (D_SHARP_COMMAND == 0) {				
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the class D#Command.");
			}
			D_SHARP_EVENT = raapi.findClass("D#Event");
			if ((D_SHARP_EVENT == 0) && (prefix != null))
				D_SHARP_EVENT = raapi.findClass(prefix+"D#Event");
			if ((D_SHARP_EVENT == 0) && insertMetamodel)
				D_SHARP_EVENT = raapi.createClass(prefix+"D#Event");
			if (D_SHARP_EVENT == 0) {				
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the class D#Event.");
			}
			D_SHARP_ROWMOVEDEVENT = raapi.findClass("D#RowMovedEvent");
			if ((D_SHARP_ROWMOVEDEVENT == 0) && (prefix != null))
				D_SHARP_ROWMOVEDEVENT = raapi.findClass(prefix+"D#RowMovedEvent");
			if ((D_SHARP_ROWMOVEDEVENT == 0) && insertMetamodel)
				D_SHARP_ROWMOVEDEVENT = raapi.createClass(prefix+"D#RowMovedEvent");
			if (D_SHARP_ROWMOVEDEVENT == 0) {				
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the class D#RowMovedEvent.");
			}
			D_SHARP_COLUMNMOVEDEVENT = raapi.findClass("D#ColumnMovedEvent");
			if ((D_SHARP_COLUMNMOVEDEVENT == 0) && (prefix != null))
				D_SHARP_COLUMNMOVEDEVENT = raapi.findClass(prefix+"D#ColumnMovedEvent");
			if ((D_SHARP_COLUMNMOVEDEVENT == 0) && insertMetamodel)
				D_SHARP_COLUMNMOVEDEVENT = raapi.createClass(prefix+"D#ColumnMovedEvent");
			if (D_SHARP_COLUMNMOVEDEVENT == 0) {				
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the class D#ColumnMovedEvent.");
			}
			D_SHARP_MULTILINETEXTBOXCHANGEEVENT = raapi.findClass("D#MultiLineTextBoxChangeEvent");
			if ((D_SHARP_MULTILINETEXTBOXCHANGEEVENT == 0) && (prefix != null))
				D_SHARP_MULTILINETEXTBOXCHANGEEVENT = raapi.findClass(prefix+"D#MultiLineTextBoxChangeEvent");
			if ((D_SHARP_MULTILINETEXTBOXCHANGEEVENT == 0) && insertMetamodel)
				D_SHARP_MULTILINETEXTBOXCHANGEEVENT = raapi.createClass(prefix+"D#MultiLineTextBoxChangeEvent");
			if (D_SHARP_MULTILINETEXTBOXCHANGEEVENT == 0) {				
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the class D#MultiLineTextBoxChangeEvent.");
			}
			D_SHARP_TEXTLINE = raapi.findClass("D#TextLine");
			if ((D_SHARP_TEXTLINE == 0) && (prefix != null))
				D_SHARP_TEXTLINE = raapi.findClass(prefix+"D#TextLine");
			if ((D_SHARP_TEXTLINE == 0) && insertMetamodel)
				D_SHARP_TEXTLINE = raapi.createClass(prefix+"D#TextLine");
			if (D_SHARP_TEXTLINE == 0) {				
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the class D#TextLine.");
			}
			D_SHARP_TREENODESELECTEVENT = raapi.findClass("D#TreeNodeSelectEvent");
			if ((D_SHARP_TREENODESELECTEVENT == 0) && (prefix != null))
				D_SHARP_TREENODESELECTEVENT = raapi.findClass(prefix+"D#TreeNodeSelectEvent");
			if ((D_SHARP_TREENODESELECTEVENT == 0) && insertMetamodel)
				D_SHARP_TREENODESELECTEVENT = raapi.createClass(prefix+"D#TreeNodeSelectEvent");
			if (D_SHARP_TREENODESELECTEVENT == 0) {				
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the class D#TreeNodeSelectEvent.");
			}
			D_SHARP_ADDTREENODECMD = raapi.findClass("D#AddTreeNodeCmd");
			if ((D_SHARP_ADDTREENODECMD == 0) && (prefix != null))
				D_SHARP_ADDTREENODECMD = raapi.findClass(prefix+"D#AddTreeNodeCmd");
			if ((D_SHARP_ADDTREENODECMD == 0) && insertMetamodel)
				D_SHARP_ADDTREENODECMD = raapi.createClass(prefix+"D#AddTreeNodeCmd");
			if (D_SHARP_ADDTREENODECMD == 0) {				
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the class D#AddTreeNodeCmd.");
			}
			D_SHARP_DELETETREENODECMD = raapi.findClass("D#DeleteTreeNodeCmd");
			if ((D_SHARP_DELETETREENODECMD == 0) && (prefix != null))
				D_SHARP_DELETETREENODECMD = raapi.findClass(prefix+"D#DeleteTreeNodeCmd");
			if ((D_SHARP_DELETETREENODECMD == 0) && insertMetamodel)
				D_SHARP_DELETETREENODECMD = raapi.createClass(prefix+"D#DeleteTreeNodeCmd");
			if (D_SHARP_DELETETREENODECMD == 0) {				
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the class D#DeleteTreeNodeCmd.");
			}
			D_SHARP_SELECTTREENODECMD = raapi.findClass("D#SelectTreeNodeCmd");
			if ((D_SHARP_SELECTTREENODECMD == 0) && (prefix != null))
				D_SHARP_SELECTTREENODECMD = raapi.findClass(prefix+"D#SelectTreeNodeCmd");
			if ((D_SHARP_SELECTTREENODECMD == 0) && insertMetamodel)
				D_SHARP_SELECTTREENODECMD = raapi.createClass(prefix+"D#SelectTreeNodeCmd");
			if (D_SHARP_SELECTTREENODECMD == 0) {				
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the class D#SelectTreeNodeCmd.");
			}
			D_SHARP_EXPANDTREENODECMD = raapi.findClass("D#ExpandTreeNodeCmd");
			if ((D_SHARP_EXPANDTREENODECMD == 0) && (prefix != null))
				D_SHARP_EXPANDTREENODECMD = raapi.findClass(prefix+"D#ExpandTreeNodeCmd");
			if ((D_SHARP_EXPANDTREENODECMD == 0) && insertMetamodel)
				D_SHARP_EXPANDTREENODECMD = raapi.createClass(prefix+"D#ExpandTreeNodeCmd");
			if (D_SHARP_EXPANDTREENODECMD == 0) {				
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the class D#ExpandTreeNodeCmd.");
			}
			D_SHARP_COLLAPSETREENODECMD = raapi.findClass("D#CollapseTreeNodeCmd");
			if ((D_SHARP_COLLAPSETREENODECMD == 0) && (prefix != null))
				D_SHARP_COLLAPSETREENODECMD = raapi.findClass(prefix+"D#CollapseTreeNodeCmd");
			if ((D_SHARP_COLLAPSETREENODECMD == 0) && insertMetamodel)
				D_SHARP_COLLAPSETREENODECMD = raapi.createClass(prefix+"D#CollapseTreeNodeCmd");
			if (D_SHARP_COLLAPSETREENODECMD == 0) {				
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the class D#CollapseTreeNodeCmd.");
			}
			D_SHARP_TREENODE = raapi.findClass("D#TreeNode");
			if ((D_SHARP_TREENODE == 0) && (prefix != null))
				D_SHARP_TREENODE = raapi.findClass(prefix+"D#TreeNode");
			if ((D_SHARP_TREENODE == 0) && insertMetamodel)
				D_SHARP_TREENODE = raapi.createClass(prefix+"D#TreeNode");
			if (D_SHARP_TREENODE == 0) {				
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the class D#TreeNode.");
			}
			D_SHARP_LISTBOXCHANGEEVENT = raapi.findClass("D#ListBoxChangeEvent");
			if ((D_SHARP_LISTBOXCHANGEEVENT == 0) && (prefix != null))
				D_SHARP_LISTBOXCHANGEEVENT = raapi.findClass(prefix+"D#ListBoxChangeEvent");
			if ((D_SHARP_LISTBOXCHANGEEVENT == 0) && insertMetamodel)
				D_SHARP_LISTBOXCHANGEEVENT = raapi.createClass(prefix+"D#ListBoxChangeEvent");
			if (D_SHARP_LISTBOXCHANGEEVENT == 0) {				
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the class D#ListBoxChangeEvent.");
			}
			D_SHARP_TABCHANGEEVENT = raapi.findClass("D#TabChangeEvent");
			if ((D_SHARP_TABCHANGEEVENT == 0) && (prefix != null))
				D_SHARP_TABCHANGEEVENT = raapi.findClass(prefix+"D#TabChangeEvent");
			if ((D_SHARP_TABCHANGEEVENT == 0) && insertMetamodel)
				D_SHARP_TABCHANGEEVENT = raapi.createClass(prefix+"D#TabChangeEvent");
			if (D_SHARP_TABCHANGEEVENT == 0) {				
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the class D#TabChangeEvent.");
			}
			D_SHARP_TREENODEMOVEEVENT = raapi.findClass("D#TreeNodeMoveEvent");
			if ((D_SHARP_TREENODEMOVEEVENT == 0) && (prefix != null))
				D_SHARP_TREENODEMOVEEVENT = raapi.findClass(prefix+"D#TreeNodeMoveEvent");
			if ((D_SHARP_TREENODEMOVEEVENT == 0) && insertMetamodel)
				D_SHARP_TREENODEMOVEEVENT = raapi.createClass(prefix+"D#TreeNodeMoveEvent");
			if (D_SHARP_TREENODEMOVEEVENT == 0) {				
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the class D#TreeNodeMoveEvent.");
			}
			D_SHARP_COPYTOCLIPBOARDCMD = raapi.findClass("D#CopyToClipboardCmd");
			if ((D_SHARP_COPYTOCLIPBOARDCMD == 0) && (prefix != null))
				D_SHARP_COPYTOCLIPBOARDCMD = raapi.findClass(prefix+"D#CopyToClipboardCmd");
			if ((D_SHARP_COPYTOCLIPBOARDCMD == 0) && insertMetamodel)
				D_SHARP_COPYTOCLIPBOARDCMD = raapi.createClass(prefix+"D#CopyToClipboardCmd");
			if (D_SHARP_COPYTOCLIPBOARDCMD == 0) {				
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the class D#CopyToClipboardCmd.");
			}
			D_SHARP_KEYDOWNEVENT = raapi.findClass("D#KeyDownEvent");
			if ((D_SHARP_KEYDOWNEVENT == 0) && (prefix != null))
				D_SHARP_KEYDOWNEVENT = raapi.findClass(prefix+"D#KeyDownEvent");
			if ((D_SHARP_KEYDOWNEVENT == 0) && insertMetamodel)
				D_SHARP_KEYDOWNEVENT = raapi.createClass(prefix+"D#KeyDownEvent");
			if (D_SHARP_KEYDOWNEVENT == 0) {				
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the class D#KeyDownEvent.");
			}
			D_SHARP_TREENODEEXPANDEDEVENT = raapi.findClass("D#TreeNodeExpandedEvent");
			if ((D_SHARP_TREENODEEXPANDEDEVENT == 0) && (prefix != null))
				D_SHARP_TREENODEEXPANDEDEVENT = raapi.findClass(prefix+"D#TreeNodeExpandedEvent");
			if ((D_SHARP_TREENODEEXPANDEDEVENT == 0) && insertMetamodel)
				D_SHARP_TREENODEEXPANDEDEVENT = raapi.createClass(prefix+"D#TreeNodeExpandedEvent");
			if (D_SHARP_TREENODEEXPANDEDEVENT == 0) {				
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the class D#TreeNodeExpandedEvent.");
			}
			D_SHARP_TREENODECOLLAPSEDEVENT = raapi.findClass("D#TreeNodeCollapsedEvent");
			if ((D_SHARP_TREENODECOLLAPSEDEVENT == 0) && (prefix != null))
				D_SHARP_TREENODECOLLAPSEDEVENT = raapi.findClass(prefix+"D#TreeNodeCollapsedEvent");
			if ((D_SHARP_TREENODECOLLAPSEDEVENT == 0) && insertMetamodel)
				D_SHARP_TREENODECOLLAPSEDEVENT = raapi.createClass(prefix+"D#TreeNodeCollapsedEvent");
			if (D_SHARP_TREENODECOLLAPSEDEVENT == 0) {				
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the class D#TreeNodeCollapsedEvent.");
			}
			COMMAND = raapi.findClass("Command");
			if ((COMMAND == 0) && (prefix != null))
				COMMAND = raapi.findClass(prefix+"Command");
			if ((COMMAND == 0) && insertMetamodel)
				COMMAND = raapi.createClass(prefix+"Command");
			if (COMMAND == 0) {				
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the class Command.");
			}
			EVENT = raapi.findClass("Event");
			if ((EVENT == 0) && (prefix != null))
				EVENT = raapi.findClass(prefix+"Event");
			if ((EVENT == 0) && insertMetamodel)
				EVENT = raapi.createClass(prefix+"Event");
			if (EVENT == 0) {				
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the class Event.");
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
				if (!raapi.isDirectSubClass(D_SHARP_COMPONENT, D_SHARP_EVENTSOURCE))
					if (!raapi.createGeneralization(D_SHARP_COMPONENT, D_SHARP_EVENTSOURCE)) {
						setRAAPI(null, null, false); // freeing references initialized so far...
						throw new ElementReferenceException("Error creating a generalization between classes D_SHARP_Component and D_SHARP_EventSource.");
					}
				if (!raapi.isDirectSubClass(D_SHARP_CONTAINER, D_SHARP_COMPONENT))
					if (!raapi.createGeneralization(D_SHARP_CONTAINER, D_SHARP_COMPONENT)) {
						setRAAPI(null, null, false); // freeing references initialized so far...
						throw new ElementReferenceException("Error creating a generalization between classes D_SHARP_Container and D_SHARP_Component.");
					}
				if (!raapi.isDirectSubClass(D_SHARP_ROW, D_SHARP_CONTAINER))
					if (!raapi.createGeneralization(D_SHARP_ROW, D_SHARP_CONTAINER)) {
						setRAAPI(null, null, false); // freeing references initialized so far...
						throw new ElementReferenceException("Error creating a generalization between classes D_SHARP_Row and D_SHARP_Container.");
					}
				if (!raapi.isDirectSubClass(D_SHARP_COLUMN, D_SHARP_CONTAINER))
					if (!raapi.createGeneralization(D_SHARP_COLUMN, D_SHARP_CONTAINER)) {
						setRAAPI(null, null, false); // freeing references initialized so far...
						throw new ElementReferenceException("Error creating a generalization between classes D_SHARP_Column and D_SHARP_Container.");
					}
				if (!raapi.isDirectSubClass(D_SHARP_VERTICALBOX, D_SHARP_CONTAINER))
					if (!raapi.createGeneralization(D_SHARP_VERTICALBOX, D_SHARP_CONTAINER)) {
						setRAAPI(null, null, false); // freeing references initialized so far...
						throw new ElementReferenceException("Error creating a generalization between classes D_SHARP_VerticalBox and D_SHARP_Container.");
					}
				if (!raapi.isDirectSubClass(D_SHARP_GROUPBOX, D_SHARP_VERTICALBOX))
					if (!raapi.createGeneralization(D_SHARP_GROUPBOX, D_SHARP_VERTICALBOX)) {
						setRAAPI(null, null, false); // freeing references initialized so far...
						throw new ElementReferenceException("Error creating a generalization between classes D_SHARP_GroupBox and D_SHARP_VerticalBox.");
					}
				if (!raapi.isDirectSubClass(D_SHARP_FORM, D_SHARP_VERTICALBOX))
					if (!raapi.createGeneralization(D_SHARP_FORM, D_SHARP_VERTICALBOX)) {
						setRAAPI(null, null, false); // freeing references initialized so far...
						throw new ElementReferenceException("Error creating a generalization between classes D_SHARP_Form and D_SHARP_VerticalBox.");
					}
				if (!raapi.isDirectSubClass(D_SHARP_TABLEDIAGRAM, D_SHARP_FORM))
					if (!raapi.createGeneralization(D_SHARP_TABLEDIAGRAM, D_SHARP_FORM)) {
						setRAAPI(null, null, false); // freeing references initialized so far...
						throw new ElementReferenceException("Error creating a generalization between classes D_SHARP_TableDiagram and D_SHARP_Form.");
					}
				if (!raapi.isDirectSubClass(D_SHARP_HORIZONTALBOX, D_SHARP_CONTAINER))
					if (!raapi.createGeneralization(D_SHARP_HORIZONTALBOX, D_SHARP_CONTAINER)) {
						setRAAPI(null, null, false); // freeing references initialized so far...
						throw new ElementReferenceException("Error creating a generalization between classes D_SHARP_HorizontalBox and D_SHARP_Container.");
					}
				if (!raapi.isDirectSubClass(D_SHARP_HORIZONTALSCROLLBOX, D_SHARP_CONTAINER))
					if (!raapi.createGeneralization(D_SHARP_HORIZONTALSCROLLBOX, D_SHARP_CONTAINER)) {
						setRAAPI(null, null, false); // freeing references initialized so far...
						throw new ElementReferenceException("Error creating a generalization between classes D_SHARP_HorizontalScrollBox and D_SHARP_Container.");
					}
				if (!raapi.isDirectSubClass(D_SHARP_VERTICALSCROLLBOX, D_SHARP_CONTAINER))
					if (!raapi.createGeneralization(D_SHARP_VERTICALSCROLLBOX, D_SHARP_CONTAINER)) {
						setRAAPI(null, null, false); // freeing references initialized so far...
						throw new ElementReferenceException("Error creating a generalization between classes D_SHARP_VerticalScrollBox and D_SHARP_Container.");
					}
				if (!raapi.isDirectSubClass(D_SHARP_SCROLLBOX, D_SHARP_CONTAINER))
					if (!raapi.createGeneralization(D_SHARP_SCROLLBOX, D_SHARP_CONTAINER)) {
						setRAAPI(null, null, false); // freeing references initialized so far...
						throw new ElementReferenceException("Error creating a generalization between classes D_SHARP_ScrollBox and D_SHARP_Container.");
					}
				if (!raapi.isDirectSubClass(D_SHARP_TAB, D_SHARP_VERTICALBOX))
					if (!raapi.createGeneralization(D_SHARP_TAB, D_SHARP_VERTICALBOX)) {
						setRAAPI(null, null, false); // freeing references initialized so far...
						throw new ElementReferenceException("Error creating a generalization between classes D_SHARP_Tab and D_SHARP_VerticalBox.");
					}
				if (!raapi.isDirectSubClass(D_SHARP_STACK, D_SHARP_CONTAINER))
					if (!raapi.createGeneralization(D_SHARP_STACK, D_SHARP_CONTAINER)) {
						setRAAPI(null, null, false); // freeing references initialized so far...
						throw new ElementReferenceException("Error creating a generalization between classes D_SHARP_Stack and D_SHARP_Container.");
					}
				if (!raapi.isDirectSubClass(D_SHARP_TABCONTAINER, D_SHARP_STACK))
					if (!raapi.createGeneralization(D_SHARP_TABCONTAINER, D_SHARP_STACK)) {
						setRAAPI(null, null, false); // freeing references initialized so far...
						throw new ElementReferenceException("Error creating a generalization between classes D_SHARP_TabContainer and D_SHARP_Stack.");
					}
				if (!raapi.isDirectSubClass(D_SHARP_VERTICALSCROLLBOXWRAPPER, D_SHARP_VERTICALSCROLLBOX))
					if (!raapi.createGeneralization(D_SHARP_VERTICALSCROLLBOXWRAPPER, D_SHARP_VERTICALSCROLLBOX)) {
						setRAAPI(null, null, false); // freeing references initialized so far...
						throw new ElementReferenceException("Error creating a generalization between classes D_SHARP_VerticalScrollBoxWrapper and D_SHARP_VerticalScrollBox.");
					}
				if (!raapi.isDirectSubClass(D_SHARP_HORIZONTALSCROLLBOXWRAPPER, D_SHARP_HORIZONTALSCROLLBOX))
					if (!raapi.createGeneralization(D_SHARP_HORIZONTALSCROLLBOXWRAPPER, D_SHARP_HORIZONTALSCROLLBOX)) {
						setRAAPI(null, null, false); // freeing references initialized so far...
						throw new ElementReferenceException("Error creating a generalization between classes D_SHARP_HorizontalScrollBoxWrapper and D_SHARP_HorizontalScrollBox.");
					}
				if (!raapi.isDirectSubClass(D_SHARP_VERTICALSPLITBOX, D_SHARP_VERTICALBOX))
					if (!raapi.createGeneralization(D_SHARP_VERTICALSPLITBOX, D_SHARP_VERTICALBOX)) {
						setRAAPI(null, null, false); // freeing references initialized so far...
						throw new ElementReferenceException("Error creating a generalization between classes D_SHARP_VerticalSplitBox and D_SHARP_VerticalBox.");
					}
				if (!raapi.isDirectSubClass(D_SHARP_HORIZONTALSPLITBOX, D_SHARP_HORIZONTALBOX))
					if (!raapi.createGeneralization(D_SHARP_HORIZONTALSPLITBOX, D_SHARP_HORIZONTALBOX)) {
						setRAAPI(null, null, false); // freeing references initialized so far...
						throw new ElementReferenceException("Error creating a generalization between classes D_SHARP_HorizontalSplitBox and D_SHARP_HorizontalBox.");
					}
				if (!raapi.isDirectSubClass(D_SHARP_LABEL, D_SHARP_COMPONENT))
					if (!raapi.createGeneralization(D_SHARP_LABEL, D_SHARP_COMPONENT)) {
						setRAAPI(null, null, false); // freeing references initialized so far...
						throw new ElementReferenceException("Error creating a generalization between classes D_SHARP_Label and D_SHARP_Component.");
					}
				if (!raapi.isDirectSubClass(D_SHARP_RADIOBUTTON, D_SHARP_COMPONENT))
					if (!raapi.createGeneralization(D_SHARP_RADIOBUTTON, D_SHARP_COMPONENT)) {
						setRAAPI(null, null, false); // freeing references initialized so far...
						throw new ElementReferenceException("Error creating a generalization between classes D_SHARP_RadioButton and D_SHARP_Component.");
					}
				if (!raapi.isDirectSubClass(D_SHARP_LISTBOX, D_SHARP_COMPONENT))
					if (!raapi.createGeneralization(D_SHARP_LISTBOX, D_SHARP_COMPONENT)) {
						setRAAPI(null, null, false); // freeing references initialized so far...
						throw new ElementReferenceException("Error creating a generalization between classes D_SHARP_ListBox and D_SHARP_Component.");
					}
				if (!raapi.isDirectSubClass(D_SHARP_VTABLETYPE, D_SHARP_COMPONENT))
					if (!raapi.createGeneralization(D_SHARP_VTABLETYPE, D_SHARP_COMPONENT)) {
						setRAAPI(null, null, false); // freeing references initialized so far...
						throw new ElementReferenceException("Error creating a generalization between classes D_SHARP_VTableType and D_SHARP_Component.");
					}
				if (!raapi.isDirectSubClass(D_SHARP_VTABLE, D_SHARP_VTABLETYPE))
					if (!raapi.createGeneralization(D_SHARP_VTABLE, D_SHARP_VTABLETYPE)) {
						setRAAPI(null, null, false); // freeing references initialized so far...
						throw new ElementReferenceException("Error creating a generalization between classes D_SHARP_VTable and D_SHARP_VTableType.");
					}
				if (!raapi.isDirectSubClass(D_SHARP_TABLECOMPONENT, D_SHARP_COMPONENT))
					if (!raapi.createGeneralization(D_SHARP_TABLECOMPONENT, D_SHARP_COMPONENT)) {
						setRAAPI(null, null, false); // freeing references initialized so far...
						throw new ElementReferenceException("Error creating a generalization between classes D_SHARP_TableComponent and D_SHARP_Component.");
					}
				if (!raapi.isDirectSubClass(D_SHARP_BUTTON, D_SHARP_TABLECOMPONENT))
					if (!raapi.createGeneralization(D_SHARP_BUTTON, D_SHARP_TABLECOMPONENT)) {
						setRAAPI(null, null, false); // freeing references initialized so far...
						throw new ElementReferenceException("Error creating a generalization between classes D_SHARP_Button and D_SHARP_TableComponent.");
					}
				if (!raapi.isDirectSubClass(D_SHARP_CHECKBOX, D_SHARP_TABLECOMPONENT))
					if (!raapi.createGeneralization(D_SHARP_CHECKBOX, D_SHARP_TABLECOMPONENT)) {
						setRAAPI(null, null, false); // freeing references initialized so far...
						throw new ElementReferenceException("Error creating a generalization between classes D_SHARP_CheckBox and D_SHARP_TableComponent.");
					}
				if (!raapi.isDirectSubClass(D_SHARP_COMBOBOX, D_SHARP_TABLECOMPONENT))
					if (!raapi.createGeneralization(D_SHARP_COMBOBOX, D_SHARP_TABLECOMPONENT)) {
						setRAAPI(null, null, false); // freeing references initialized so far...
						throw new ElementReferenceException("Error creating a generalization between classes D_SHARP_ComboBox and D_SHARP_TableComponent.");
					}
				if (!raapi.isDirectSubClass(D_SHARP_IMAGE, D_SHARP_TABLECOMPONENT))
					if (!raapi.createGeneralization(D_SHARP_IMAGE, D_SHARP_TABLECOMPONENT)) {
						setRAAPI(null, null, false); // freeing references initialized so far...
						throw new ElementReferenceException("Error creating a generalization between classes D_SHARP_Image and D_SHARP_TableComponent.");
					}
				if (!raapi.isDirectSubClass(D_SHARP_TEXTBOX, D_SHARP_TABLECOMPONENT))
					if (!raapi.createGeneralization(D_SHARP_TEXTBOX, D_SHARP_TABLECOMPONENT)) {
						setRAAPI(null, null, false); // freeing references initialized so far...
						throw new ElementReferenceException("Error creating a generalization between classes D_SHARP_TextBox and D_SHARP_TableComponent.");
					}
				if (!raapi.isDirectSubClass(D_SHARP_INPUTFIELD, D_SHARP_TEXTBOX))
					if (!raapi.createGeneralization(D_SHARP_INPUTFIELD, D_SHARP_TEXTBOX)) {
						setRAAPI(null, null, false); // freeing references initialized so far...
						throw new ElementReferenceException("Error creating a generalization between classes D_SHARP_InputField and D_SHARP_TextBox.");
					}
				if (!raapi.isDirectSubClass(D_SHARP_TEXTAREA, D_SHARP_TEXTBOX))
					if (!raapi.createGeneralization(D_SHARP_TEXTAREA, D_SHARP_TEXTBOX)) {
						setRAAPI(null, null, false); // freeing references initialized so far...
						throw new ElementReferenceException("Error creating a generalization between classes D_SHARP_TextArea and D_SHARP_TextBox.");
					}
				if (!raapi.isDirectSubClass(D_SHARP_TREE, D_SHARP_COMPONENT))
					if (!raapi.createGeneralization(D_SHARP_TREE, D_SHARP_COMPONENT)) {
						setRAAPI(null, null, false); // freeing references initialized so far...
						throw new ElementReferenceException("Error creating a generalization between classes D_SHARP_Tree and D_SHARP_Component.");
					}
				if (!raapi.isDirectSubClass(D_SHARP_MULTILINETEXTBOX, D_SHARP_TEXTAREA))
					if (!raapi.createGeneralization(D_SHARP_MULTILINETEXTBOX, D_SHARP_TEXTAREA)) {
						setRAAPI(null, null, false); // freeing references initialized so far...
						throw new ElementReferenceException("Error creating a generalization between classes D_SHARP_MultiLineTextBox and D_SHARP_TextArea.");
					}
				if (!raapi.isDirectSubClass(D_SHARP_RICHTEXTAREA, D_SHARP_TEXTAREA))
					if (!raapi.createGeneralization(D_SHARP_RICHTEXTAREA, D_SHARP_TEXTAREA)) {
						setRAAPI(null, null, false); // freeing references initialized so far...
						throw new ElementReferenceException("Error creating a generalization between classes D_SHARP_RichTextArea and D_SHARP_TextArea.");
					}
				if (!raapi.isDirectSubClass(D_SHARP_PROGRESSBAR, D_SHARP_COMPONENT))
					if (!raapi.createGeneralization(D_SHARP_PROGRESSBAR, D_SHARP_COMPONENT)) {
						setRAAPI(null, null, false); // freeing references initialized so far...
						throw new ElementReferenceException("Error creating a generalization between classes D_SHARP_ProgressBar and D_SHARP_Component.");
					}
				if (!raapi.isDirectSubClass(D_SHARP_IMAGEBUTTON, D_SHARP_BUTTON))
					if (!raapi.createGeneralization(D_SHARP_IMAGEBUTTON, D_SHARP_BUTTON)) {
						setRAAPI(null, null, false); // freeing references initialized so far...
						throw new ElementReferenceException("Error creating a generalization between classes D_SHARP_ImageButton and D_SHARP_Button.");
					}
				if (!raapi.isDirectSubClass(D_SHARP_COMMAND, COMMAND))
					if (!raapi.createGeneralization(D_SHARP_COMMAND, COMMAND)) {
						setRAAPI(null, null, false); // freeing references initialized so far...
						throw new ElementReferenceException("Error creating a generalization between classes D_SHARP_Command and Command.");
					}
				if (!raapi.isDirectSubClass(D_SHARP_EVENT, EVENT))
					if (!raapi.createGeneralization(D_SHARP_EVENT, EVENT)) {
						setRAAPI(null, null, false); // freeing references initialized so far...
						throw new ElementReferenceException("Error creating a generalization between classes D_SHARP_Event and Event.");
					}
				if (!raapi.isDirectSubClass(D_SHARP_ROWMOVEDEVENT, D_SHARP_EVENT))
					if (!raapi.createGeneralization(D_SHARP_ROWMOVEDEVENT, D_SHARP_EVENT)) {
						setRAAPI(null, null, false); // freeing references initialized so far...
						throw new ElementReferenceException("Error creating a generalization between classes D_SHARP_RowMovedEvent and D_SHARP_Event.");
					}
				if (!raapi.isDirectSubClass(D_SHARP_COLUMNMOVEDEVENT, D_SHARP_EVENT))
					if (!raapi.createGeneralization(D_SHARP_COLUMNMOVEDEVENT, D_SHARP_EVENT)) {
						setRAAPI(null, null, false); // freeing references initialized so far...
						throw new ElementReferenceException("Error creating a generalization between classes D_SHARP_ColumnMovedEvent and D_SHARP_Event.");
					}
				if (!raapi.isDirectSubClass(D_SHARP_MULTILINETEXTBOXCHANGEEVENT, D_SHARP_EVENT))
					if (!raapi.createGeneralization(D_SHARP_MULTILINETEXTBOXCHANGEEVENT, D_SHARP_EVENT)) {
						setRAAPI(null, null, false); // freeing references initialized so far...
						throw new ElementReferenceException("Error creating a generalization between classes D_SHARP_MultiLineTextBoxChangeEvent and D_SHARP_Event.");
					}
				if (!raapi.isDirectSubClass(D_SHARP_TREENODESELECTEVENT, D_SHARP_EVENT))
					if (!raapi.createGeneralization(D_SHARP_TREENODESELECTEVENT, D_SHARP_EVENT)) {
						setRAAPI(null, null, false); // freeing references initialized so far...
						throw new ElementReferenceException("Error creating a generalization between classes D_SHARP_TreeNodeSelectEvent and D_SHARP_Event.");
					}
				if (!raapi.isDirectSubClass(D_SHARP_ADDTREENODECMD, D_SHARP_COMMAND))
					if (!raapi.createGeneralization(D_SHARP_ADDTREENODECMD, D_SHARP_COMMAND)) {
						setRAAPI(null, null, false); // freeing references initialized so far...
						throw new ElementReferenceException("Error creating a generalization between classes D_SHARP_AddTreeNodeCmd and D_SHARP_Command.");
					}
				if (!raapi.isDirectSubClass(D_SHARP_DELETETREENODECMD, D_SHARP_COMMAND))
					if (!raapi.createGeneralization(D_SHARP_DELETETREENODECMD, D_SHARP_COMMAND)) {
						setRAAPI(null, null, false); // freeing references initialized so far...
						throw new ElementReferenceException("Error creating a generalization between classes D_SHARP_DeleteTreeNodeCmd and D_SHARP_Command.");
					}
				if (!raapi.isDirectSubClass(D_SHARP_SELECTTREENODECMD, D_SHARP_COMMAND))
					if (!raapi.createGeneralization(D_SHARP_SELECTTREENODECMD, D_SHARP_COMMAND)) {
						setRAAPI(null, null, false); // freeing references initialized so far...
						throw new ElementReferenceException("Error creating a generalization between classes D_SHARP_SelectTreeNodeCmd and D_SHARP_Command.");
					}
				if (!raapi.isDirectSubClass(D_SHARP_EXPANDTREENODECMD, D_SHARP_COMMAND))
					if (!raapi.createGeneralization(D_SHARP_EXPANDTREENODECMD, D_SHARP_COMMAND)) {
						setRAAPI(null, null, false); // freeing references initialized so far...
						throw new ElementReferenceException("Error creating a generalization between classes D_SHARP_ExpandTreeNodeCmd and D_SHARP_Command.");
					}
				if (!raapi.isDirectSubClass(D_SHARP_COLLAPSETREENODECMD, D_SHARP_COMMAND))
					if (!raapi.createGeneralization(D_SHARP_COLLAPSETREENODECMD, D_SHARP_COMMAND)) {
						setRAAPI(null, null, false); // freeing references initialized so far...
						throw new ElementReferenceException("Error creating a generalization between classes D_SHARP_CollapseTreeNodeCmd and D_SHARP_Command.");
					}
				if (!raapi.isDirectSubClass(D_SHARP_LISTBOXCHANGEEVENT, D_SHARP_EVENT))
					if (!raapi.createGeneralization(D_SHARP_LISTBOXCHANGEEVENT, D_SHARP_EVENT)) {
						setRAAPI(null, null, false); // freeing references initialized so far...
						throw new ElementReferenceException("Error creating a generalization between classes D_SHARP_ListBoxChangeEvent and D_SHARP_Event.");
					}
				if (!raapi.isDirectSubClass(D_SHARP_TABCHANGEEVENT, D_SHARP_EVENT))
					if (!raapi.createGeneralization(D_SHARP_TABCHANGEEVENT, D_SHARP_EVENT)) {
						setRAAPI(null, null, false); // freeing references initialized so far...
						throw new ElementReferenceException("Error creating a generalization between classes D_SHARP_TabChangeEvent and D_SHARP_Event.");
					}
				if (!raapi.isDirectSubClass(D_SHARP_TREENODEMOVEEVENT, D_SHARP_EVENT))
					if (!raapi.createGeneralization(D_SHARP_TREENODEMOVEEVENT, D_SHARP_EVENT)) {
						setRAAPI(null, null, false); // freeing references initialized so far...
						throw new ElementReferenceException("Error creating a generalization between classes D_SHARP_TreeNodeMoveEvent and D_SHARP_Event.");
					}
				if (!raapi.isDirectSubClass(D_SHARP_COPYTOCLIPBOARDCMD, D_SHARP_COMMAND))
					if (!raapi.createGeneralization(D_SHARP_COPYTOCLIPBOARDCMD, D_SHARP_COMMAND)) {
						setRAAPI(null, null, false); // freeing references initialized so far...
						throw new ElementReferenceException("Error creating a generalization between classes D_SHARP_CopyToClipboardCmd and D_SHARP_Command.");
					}
				if (!raapi.isDirectSubClass(D_SHARP_KEYDOWNEVENT, D_SHARP_EVENT))
					if (!raapi.createGeneralization(D_SHARP_KEYDOWNEVENT, D_SHARP_EVENT)) {
						setRAAPI(null, null, false); // freeing references initialized so far...
						throw new ElementReferenceException("Error creating a generalization between classes D_SHARP_KeyDownEvent and D_SHARP_Event.");
					}
				if (!raapi.isDirectSubClass(D_SHARP_TREENODEEXPANDEDEVENT, D_SHARP_EVENT))
					if (!raapi.createGeneralization(D_SHARP_TREENODEEXPANDEDEVENT, D_SHARP_EVENT)) {
						setRAAPI(null, null, false); // freeing references initialized so far...
						throw new ElementReferenceException("Error creating a generalization between classes D_SHARP_TreeNodeExpandedEvent and D_SHARP_Event.");
					}
				if (!raapi.isDirectSubClass(D_SHARP_TREENODECOLLAPSEDEVENT, D_SHARP_EVENT))
					if (!raapi.createGeneralization(D_SHARP_TREENODECOLLAPSEDEVENT, D_SHARP_EVENT)) {
						setRAAPI(null, null, false); // freeing references initialized so far...
						throw new ElementReferenceException("Error creating a generalization between classes D_SHARP_TreeNodeCollapsedEvent and D_SHARP_Event.");
					}
			}

			// initializing references for attributes and associations...
			D_SHARP_GROUP_COMPONENTWIDTHS = raapi.findAssociationEnd(D_SHARP_GROUP, "componentWidths");
			if ((D_SHARP_GROUP_COMPONENTWIDTHS == 0) && insertMetamodel) {
				D_SHARP_GROUP_COMPONENTWIDTHS = raapi.createAssociation(D_SHARP_GROUP, D_SHARP_COMPONENT, "relativeWidthGroup", "componentWidths", false);
			}
			if (D_SHARP_GROUP_COMPONENTWIDTHS == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end componentWidths of the class D#Group.");
			}
			D_SHARP_GROUP_COMPONENTHEIGHTS = raapi.findAssociationEnd(D_SHARP_GROUP, "componentHeights");
			if ((D_SHARP_GROUP_COMPONENTHEIGHTS == 0) && insertMetamodel) {
				D_SHARP_GROUP_COMPONENTHEIGHTS = raapi.createAssociation(D_SHARP_GROUP, D_SHARP_COMPONENT, "relativeHeightGroup", "componentHeights", false);
			}
			if (D_SHARP_GROUP_COMPONENTHEIGHTS == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end componentHeights of the class D#Group.");
			}
			D_SHARP_GROUP_OWNER = raapi.findAssociationEnd(D_SHARP_GROUP, "owner");
			if ((D_SHARP_GROUP_OWNER == 0) && insertMetamodel) {
				D_SHARP_GROUP_OWNER = raapi.createAssociation(D_SHARP_GROUP, D_SHARP_COMPONENT, "ownedGroup", "owner", false);
			}
			if (D_SHARP_GROUP_OWNER == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end owner of the class D#Group.");
			}
			D_SHARP_EVENTSOURCE_EVENTHANDLER = raapi.findAssociationEnd(D_SHARP_EVENTSOURCE, "eventHandler");
			if ((D_SHARP_EVENTSOURCE_EVENTHANDLER == 0) && insertMetamodel) {
				D_SHARP_EVENTSOURCE_EVENTHANDLER = raapi.createAssociation(D_SHARP_EVENTSOURCE, D_SHARP_EVENTHANDLER, "eventSource", "eventHandler", false);
			}
			if (D_SHARP_EVENTSOURCE_EVENTHANDLER == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end eventHandler of the class D#EventSource.");
			}
			D_SHARP_EVENTSOURCE_EVENT = raapi.findAssociationEnd(D_SHARP_EVENTSOURCE, "event");
			if ((D_SHARP_EVENTSOURCE_EVENT == 0) && insertMetamodel) {
				D_SHARP_EVENTSOURCE_EVENT = raapi.createAssociation(D_SHARP_EVENTSOURCE, D_SHARP_EVENT, "source", "event", false);
			}
			if (D_SHARP_EVENTSOURCE_EVENT == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end event of the class D#EventSource.");
			}
			D_SHARP_COMPONENT_ID = raapi.findAttribute(D_SHARP_COMPONENT, "id");
			if ((D_SHARP_COMPONENT_ID == 0) && insertMetamodel)
				D_SHARP_COMPONENT_ID = raapi.createAttribute(D_SHARP_COMPONENT, "id", raapi.findPrimitiveDataType("String"));
			if (D_SHARP_COMPONENT_ID == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute id of the class D#Component.");
			}
			D_SHARP_COMPONENT_ENABLED = raapi.findAttribute(D_SHARP_COMPONENT, "enabled");
			if ((D_SHARP_COMPONENT_ENABLED == 0) && insertMetamodel)
				D_SHARP_COMPONENT_ENABLED = raapi.createAttribute(D_SHARP_COMPONENT, "enabled", raapi.findPrimitiveDataType("Boolean"));
			if (D_SHARP_COMPONENT_ENABLED == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute enabled of the class D#Component.");
			}
			D_SHARP_COMPONENT_MINIMUMWIDTH = raapi.findAttribute(D_SHARP_COMPONENT, "minimumWidth");
			if ((D_SHARP_COMPONENT_MINIMUMWIDTH == 0) && insertMetamodel)
				D_SHARP_COMPONENT_MINIMUMWIDTH = raapi.createAttribute(D_SHARP_COMPONENT, "minimumWidth", raapi.findPrimitiveDataType("Integer"));
			if (D_SHARP_COMPONENT_MINIMUMWIDTH == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute minimumWidth of the class D#Component.");
			}
			D_SHARP_COMPONENT_MINIMUMHEIGHT = raapi.findAttribute(D_SHARP_COMPONENT, "minimumHeight");
			if ((D_SHARP_COMPONENT_MINIMUMHEIGHT == 0) && insertMetamodel)
				D_SHARP_COMPONENT_MINIMUMHEIGHT = raapi.createAttribute(D_SHARP_COMPONENT, "minimumHeight", raapi.findPrimitiveDataType("Integer"));
			if (D_SHARP_COMPONENT_MINIMUMHEIGHT == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute minimumHeight of the class D#Component.");
			}
			D_SHARP_COMPONENT_PREFERREDWIDTH = raapi.findAttribute(D_SHARP_COMPONENT, "preferredWidth");
			if ((D_SHARP_COMPONENT_PREFERREDWIDTH == 0) && insertMetamodel)
				D_SHARP_COMPONENT_PREFERREDWIDTH = raapi.createAttribute(D_SHARP_COMPONENT, "preferredWidth", raapi.findPrimitiveDataType("Integer"));
			if (D_SHARP_COMPONENT_PREFERREDWIDTH == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute preferredWidth of the class D#Component.");
			}
			D_SHARP_COMPONENT_PREFERREDHEIGHT = raapi.findAttribute(D_SHARP_COMPONENT, "preferredHeight");
			if ((D_SHARP_COMPONENT_PREFERREDHEIGHT == 0) && insertMetamodel)
				D_SHARP_COMPONENT_PREFERREDHEIGHT = raapi.createAttribute(D_SHARP_COMPONENT, "preferredHeight", raapi.findPrimitiveDataType("Integer"));
			if (D_SHARP_COMPONENT_PREFERREDHEIGHT == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute preferredHeight of the class D#Component.");
			}
			D_SHARP_COMPONENT_MAXIMUMWIDTH = raapi.findAttribute(D_SHARP_COMPONENT, "maximumWidth");
			if ((D_SHARP_COMPONENT_MAXIMUMWIDTH == 0) && insertMetamodel)
				D_SHARP_COMPONENT_MAXIMUMWIDTH = raapi.createAttribute(D_SHARP_COMPONENT, "maximumWidth", raapi.findPrimitiveDataType("Integer"));
			if (D_SHARP_COMPONENT_MAXIMUMWIDTH == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute maximumWidth of the class D#Component.");
			}
			D_SHARP_COMPONENT_MAXIMUMHEIGHT = raapi.findAttribute(D_SHARP_COMPONENT, "maximumHeight");
			if ((D_SHARP_COMPONENT_MAXIMUMHEIGHT == 0) && insertMetamodel)
				D_SHARP_COMPONENT_MAXIMUMHEIGHT = raapi.createAttribute(D_SHARP_COMPONENT, "maximumHeight", raapi.findPrimitiveDataType("Integer"));
			if (D_SHARP_COMPONENT_MAXIMUMHEIGHT == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute maximumHeight of the class D#Component.");
			}
			D_SHARP_COMPONENT_LEFTMARGIN = raapi.findAttribute(D_SHARP_COMPONENT, "leftMargin");
			if ((D_SHARP_COMPONENT_LEFTMARGIN == 0) && insertMetamodel)
				D_SHARP_COMPONENT_LEFTMARGIN = raapi.createAttribute(D_SHARP_COMPONENT, "leftMargin", raapi.findPrimitiveDataType("Integer"));
			if (D_SHARP_COMPONENT_LEFTMARGIN == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute leftMargin of the class D#Component.");
			}
			D_SHARP_COMPONENT_RIGHTMARGIN = raapi.findAttribute(D_SHARP_COMPONENT, "rightMargin");
			if ((D_SHARP_COMPONENT_RIGHTMARGIN == 0) && insertMetamodel)
				D_SHARP_COMPONENT_RIGHTMARGIN = raapi.createAttribute(D_SHARP_COMPONENT, "rightMargin", raapi.findPrimitiveDataType("Integer"));
			if (D_SHARP_COMPONENT_RIGHTMARGIN == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute rightMargin of the class D#Component.");
			}
			D_SHARP_COMPONENT_TOPMARGIN = raapi.findAttribute(D_SHARP_COMPONENT, "topMargin");
			if ((D_SHARP_COMPONENT_TOPMARGIN == 0) && insertMetamodel)
				D_SHARP_COMPONENT_TOPMARGIN = raapi.createAttribute(D_SHARP_COMPONENT, "topMargin", raapi.findPrimitiveDataType("Integer"));
			if (D_SHARP_COMPONENT_TOPMARGIN == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute topMargin of the class D#Component.");
			}
			D_SHARP_COMPONENT_BOTTOMMARGIN = raapi.findAttribute(D_SHARP_COMPONENT, "bottomMargin");
			if ((D_SHARP_COMPONENT_BOTTOMMARGIN == 0) && insertMetamodel)
				D_SHARP_COMPONENT_BOTTOMMARGIN = raapi.createAttribute(D_SHARP_COMPONENT, "bottomMargin", raapi.findPrimitiveDataType("Integer"));
			if (D_SHARP_COMPONENT_BOTTOMMARGIN == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute bottomMargin of the class D#Component.");
			}
			D_SHARP_COMPONENT_MINIMUMRELATIVEWIDTH = raapi.findAttribute(D_SHARP_COMPONENT, "minimumRelativeWidth");
			if ((D_SHARP_COMPONENT_MINIMUMRELATIVEWIDTH == 0) && insertMetamodel)
				D_SHARP_COMPONENT_MINIMUMRELATIVEWIDTH = raapi.createAttribute(D_SHARP_COMPONENT, "minimumRelativeWidth", raapi.findPrimitiveDataType("EDouble"));
			if (D_SHARP_COMPONENT_MINIMUMRELATIVEWIDTH == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute minimumRelativeWidth of the class D#Component.");
			}
			D_SHARP_COMPONENT_MINIMUMRELATIVEHEIGHT = raapi.findAttribute(D_SHARP_COMPONENT, "minimumRelativeHeight");
			if ((D_SHARP_COMPONENT_MINIMUMRELATIVEHEIGHT == 0) && insertMetamodel)
				D_SHARP_COMPONENT_MINIMUMRELATIVEHEIGHT = raapi.createAttribute(D_SHARP_COMPONENT, "minimumRelativeHeight", raapi.findPrimitiveDataType("EDouble"));
			if (D_SHARP_COMPONENT_MINIMUMRELATIVEHEIGHT == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute minimumRelativeHeight of the class D#Component.");
			}
			D_SHARP_COMPONENT_PREFERREDRELATIVEWIDTH = raapi.findAttribute(D_SHARP_COMPONENT, "preferredRelativeWidth");
			if ((D_SHARP_COMPONENT_PREFERREDRELATIVEWIDTH == 0) && insertMetamodel)
				D_SHARP_COMPONENT_PREFERREDRELATIVEWIDTH = raapi.createAttribute(D_SHARP_COMPONENT, "preferredRelativeWidth", raapi.findPrimitiveDataType("EDouble"));
			if (D_SHARP_COMPONENT_PREFERREDRELATIVEWIDTH == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute preferredRelativeWidth of the class D#Component.");
			}
			D_SHARP_COMPONENT_PREFERREDRELATIVEHEIGHT = raapi.findAttribute(D_SHARP_COMPONENT, "preferredRelativeHeight");
			if ((D_SHARP_COMPONENT_PREFERREDRELATIVEHEIGHT == 0) && insertMetamodel)
				D_SHARP_COMPONENT_PREFERREDRELATIVEHEIGHT = raapi.createAttribute(D_SHARP_COMPONENT, "preferredRelativeHeight", raapi.findPrimitiveDataType("EDouble"));
			if (D_SHARP_COMPONENT_PREFERREDRELATIVEHEIGHT == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute preferredRelativeHeight of the class D#Component.");
			}
			D_SHARP_COMPONENT_MAXIMUMRELATIVEWIDTH = raapi.findAttribute(D_SHARP_COMPONENT, "maximumRelativeWidth");
			if ((D_SHARP_COMPONENT_MAXIMUMRELATIVEWIDTH == 0) && insertMetamodel)
				D_SHARP_COMPONENT_MAXIMUMRELATIVEWIDTH = raapi.createAttribute(D_SHARP_COMPONENT, "maximumRelativeWidth", raapi.findPrimitiveDataType("EDouble"));
			if (D_SHARP_COMPONENT_MAXIMUMRELATIVEWIDTH == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute maximumRelativeWidth of the class D#Component.");
			}
			D_SHARP_COMPONENT_MAXIMUMRELATIVEHEIGHT = raapi.findAttribute(D_SHARP_COMPONENT, "maximumRelativeHeight");
			if ((D_SHARP_COMPONENT_MAXIMUMRELATIVEHEIGHT == 0) && insertMetamodel)
				D_SHARP_COMPONENT_MAXIMUMRELATIVEHEIGHT = raapi.createAttribute(D_SHARP_COMPONENT, "maximumRelativeHeight", raapi.findPrimitiveDataType("EDouble"));
			if (D_SHARP_COMPONENT_MAXIMUMRELATIVEHEIGHT == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute maximumRelativeHeight of the class D#Component.");
			}
			D_SHARP_COMPONENT_HORIZONTALSPAN = raapi.findAttribute(D_SHARP_COMPONENT, "horizontalSpan");
			if ((D_SHARP_COMPONENT_HORIZONTALSPAN == 0) && insertMetamodel)
				D_SHARP_COMPONENT_HORIZONTALSPAN = raapi.createAttribute(D_SHARP_COMPONENT, "horizontalSpan", raapi.findPrimitiveDataType("Integer"));
			if (D_SHARP_COMPONENT_HORIZONTALSPAN == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute horizontalSpan of the class D#Component.");
			}
			D_SHARP_COMPONENT_VERTICALSPAN = raapi.findAttribute(D_SHARP_COMPONENT, "verticalSpan");
			if ((D_SHARP_COMPONENT_VERTICALSPAN == 0) && insertMetamodel)
				D_SHARP_COMPONENT_VERTICALSPAN = raapi.createAttribute(D_SHARP_COMPONENT, "verticalSpan", raapi.findPrimitiveDataType("Integer"));
			if (D_SHARP_COMPONENT_VERTICALSPAN == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute verticalSpan of the class D#Component.");
			}
			D_SHARP_COMPONENT_MINIMUMHORIZONTALSHRINKFACTOR = raapi.findAttribute(D_SHARP_COMPONENT, "minimumHorizontalShrinkFactor");
			if ((D_SHARP_COMPONENT_MINIMUMHORIZONTALSHRINKFACTOR == 0) && insertMetamodel)
				D_SHARP_COMPONENT_MINIMUMHORIZONTALSHRINKFACTOR = raapi.createAttribute(D_SHARP_COMPONENT, "minimumHorizontalShrinkFactor", raapi.findPrimitiveDataType("EDouble"));
			if (D_SHARP_COMPONENT_MINIMUMHORIZONTALSHRINKFACTOR == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute minimumHorizontalShrinkFactor of the class D#Component.");
			}
			D_SHARP_COMPONENT_MAXIMUMHORIZONTALGROWFACTOR = raapi.findAttribute(D_SHARP_COMPONENT, "maximumHorizontalGrowFactor");
			if ((D_SHARP_COMPONENT_MAXIMUMHORIZONTALGROWFACTOR == 0) && insertMetamodel)
				D_SHARP_COMPONENT_MAXIMUMHORIZONTALGROWFACTOR = raapi.createAttribute(D_SHARP_COMPONENT, "maximumHorizontalGrowFactor", raapi.findPrimitiveDataType("EDouble"));
			if (D_SHARP_COMPONENT_MAXIMUMHORIZONTALGROWFACTOR == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute maximumHorizontalGrowFactor of the class D#Component.");
			}
			D_SHARP_COMPONENT_MINIMUMVERTICALSHRINKFACTOR = raapi.findAttribute(D_SHARP_COMPONENT, "minimumVerticalShrinkFactor");
			if ((D_SHARP_COMPONENT_MINIMUMVERTICALSHRINKFACTOR == 0) && insertMetamodel)
				D_SHARP_COMPONENT_MINIMUMVERTICALSHRINKFACTOR = raapi.createAttribute(D_SHARP_COMPONENT, "minimumVerticalShrinkFactor", raapi.findPrimitiveDataType("EDouble"));
			if (D_SHARP_COMPONENT_MINIMUMVERTICALSHRINKFACTOR == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute minimumVerticalShrinkFactor of the class D#Component.");
			}
			D_SHARP_COMPONENT_MAXIMUMVERTICALGROWFACTOR = raapi.findAttribute(D_SHARP_COMPONENT, "maximumVerticalGrowFactor");
			if ((D_SHARP_COMPONENT_MAXIMUMVERTICALGROWFACTOR == 0) && insertMetamodel)
				D_SHARP_COMPONENT_MAXIMUMVERTICALGROWFACTOR = raapi.createAttribute(D_SHARP_COMPONENT, "maximumVerticalGrowFactor", raapi.findPrimitiveDataType("EDouble"));
			if (D_SHARP_COMPONENT_MAXIMUMVERTICALGROWFACTOR == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute maximumVerticalGrowFactor of the class D#Component.");
			}
			D_SHARP_COMPONENT_VISIBLE = raapi.findAttribute(D_SHARP_COMPONENT, "visible");
			if ((D_SHARP_COMPONENT_VISIBLE == 0) && insertMetamodel)
				D_SHARP_COMPONENT_VISIBLE = raapi.createAttribute(D_SHARP_COMPONENT, "visible", raapi.findPrimitiveDataType("Boolean"));
			if (D_SHARP_COMPONENT_VISIBLE == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute visible of the class D#Component.");
			}
			D_SHARP_COMPONENT_HINT = raapi.findAttribute(D_SHARP_COMPONENT, "hint");
			if ((D_SHARP_COMPONENT_HINT == 0) && insertMetamodel)
				D_SHARP_COMPONENT_HINT = raapi.createAttribute(D_SHARP_COMPONENT, "hint", raapi.findPrimitiveDataType("String"));
			if (D_SHARP_COMPONENT_HINT == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute hint of the class D#Component.");
			}
			D_SHARP_COMPONENT_READONLY = raapi.findAttribute(D_SHARP_COMPONENT, "readOnly");
			if ((D_SHARP_COMPONENT_READONLY == 0) && insertMetamodel)
				D_SHARP_COMPONENT_READONLY = raapi.createAttribute(D_SHARP_COMPONENT, "readOnly", raapi.findPrimitiveDataType("Boolean"));
			if (D_SHARP_COMPONENT_READONLY == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute readOnly of the class D#Component.");
			}
			D_SHARP_COMPONENT_RELATIVEWIDTHGROUP = raapi.findAssociationEnd(D_SHARP_COMPONENT, "relativeWidthGroup");
			if ((D_SHARP_COMPONENT_RELATIVEWIDTHGROUP == 0) && insertMetamodel) {
				D_SHARP_COMPONENT_RELATIVEWIDTHGROUP = raapi.createAssociation(D_SHARP_COMPONENT, D_SHARP_GROUP, "componentWidths", "relativeWidthGroup", false);
			}
			if (D_SHARP_COMPONENT_RELATIVEWIDTHGROUP == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end relativeWidthGroup of the class D#Component.");
			}
			D_SHARP_COMPONENT_RELATIVEHEIGHTGROUP = raapi.findAssociationEnd(D_SHARP_COMPONENT, "relativeHeightGroup");
			if ((D_SHARP_COMPONENT_RELATIVEHEIGHTGROUP == 0) && insertMetamodel) {
				D_SHARP_COMPONENT_RELATIVEHEIGHTGROUP = raapi.createAssociation(D_SHARP_COMPONENT, D_SHARP_GROUP, "componentHeights", "relativeHeightGroup", false);
			}
			if (D_SHARP_COMPONENT_RELATIVEHEIGHTGROUP == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end relativeHeightGroup of the class D#Component.");
			}
			D_SHARP_COMPONENT_FOCUSCONTAINER = raapi.findAssociationEnd(D_SHARP_COMPONENT, "focusContainer");
			if ((D_SHARP_COMPONENT_FOCUSCONTAINER == 0) && insertMetamodel) {
				D_SHARP_COMPONENT_FOCUSCONTAINER = raapi.createAssociation(D_SHARP_COMPONENT, D_SHARP_CONTAINER, "focus", "focusContainer", false);
			}
			if (D_SHARP_COMPONENT_FOCUSCONTAINER == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end focusContainer of the class D#Component.");
			}
			D_SHARP_COMPONENT_COMMAND = raapi.findAssociationEnd(D_SHARP_COMPONENT, "command");
			if ((D_SHARP_COMPONENT_COMMAND == 0) && insertMetamodel) {
				D_SHARP_COMPONENT_COMMAND = raapi.createAssociation(D_SHARP_COMPONENT, D_SHARP_COMMAND, "receiver", "command", false);
			}
			if (D_SHARP_COMPONENT_COMMAND == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end command of the class D#Component.");
			}
			D_SHARP_COMPONENT_OWNEDGROUP = raapi.findAssociationEnd(D_SHARP_COMPONENT, "ownedGroup");
			if ((D_SHARP_COMPONENT_OWNEDGROUP == 0) && insertMetamodel) {
				D_SHARP_COMPONENT_OWNEDGROUP = raapi.createAssociation(D_SHARP_COMPONENT, D_SHARP_GROUP, "owner", "ownedGroup", false);
			}
			if (D_SHARP_COMPONENT_OWNEDGROUP == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end ownedGroup of the class D#Component.");
			}
			D_SHARP_COMPONENT_CONTAINER = raapi.findAssociationEnd(D_SHARP_COMPONENT, "container");
			if ((D_SHARP_COMPONENT_CONTAINER == 0) && insertMetamodel) {
				D_SHARP_COMPONENT_CONTAINER = raapi.createAssociation(D_SHARP_COMPONENT, D_SHARP_CONTAINER, "component", "container", false);
			}
			if (D_SHARP_COMPONENT_CONTAINER == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end container of the class D#Component.");
			}
			D_SHARP_COMPONENT_FORM = raapi.findAssociationEnd(D_SHARP_COMPONENT, "form");
			if ((D_SHARP_COMPONENT_FORM == 0) && insertMetamodel) {
				D_SHARP_COMPONENT_FORM = raapi.createAssociation(D_SHARP_COMPONENT, D_SHARP_FORM, "focused", "form", false);
			}
			if (D_SHARP_COMPONENT_FORM == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end form of the class D#Component.");
			}
			D_SHARP_COMPONENT_FOFORM = raapi.findAssociationEnd(D_SHARP_COMPONENT, "foForm");
			if ((D_SHARP_COMPONENT_FOFORM == 0) && insertMetamodel) {
				D_SHARP_COMPONENT_FOFORM = raapi.createAssociation(D_SHARP_COMPONENT, D_SHARP_FORM, "focusOrder", "foForm", false);
			}
			if (D_SHARP_COMPONENT_FOFORM == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end foForm of the class D#Component.");
			}
			D_SHARP_CONTAINER_HORIZONTALALIGNMENT = raapi.findAttribute(D_SHARP_CONTAINER, "horizontalAlignment");
			if ((D_SHARP_CONTAINER_HORIZONTALALIGNMENT == 0) && insertMetamodel)
				D_SHARP_CONTAINER_HORIZONTALALIGNMENT = raapi.createAttribute(D_SHARP_CONTAINER, "horizontalAlignment", raapi.findPrimitiveDataType("Integer"));
			if (D_SHARP_CONTAINER_HORIZONTALALIGNMENT == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute horizontalAlignment of the class D#Container.");
			}
			D_SHARP_CONTAINER_VERTICALALIGNMENT = raapi.findAttribute(D_SHARP_CONTAINER, "verticalAlignment");
			if ((D_SHARP_CONTAINER_VERTICALALIGNMENT == 0) && insertMetamodel)
				D_SHARP_CONTAINER_VERTICALALIGNMENT = raapi.createAttribute(D_SHARP_CONTAINER, "verticalAlignment", raapi.findPrimitiveDataType("Integer"));
			if (D_SHARP_CONTAINER_VERTICALALIGNMENT == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute verticalAlignment of the class D#Container.");
			}
			D_SHARP_CONTAINER_HORIZONTALSPACING = raapi.findAttribute(D_SHARP_CONTAINER, "horizontalSpacing");
			if ((D_SHARP_CONTAINER_HORIZONTALSPACING == 0) && insertMetamodel)
				D_SHARP_CONTAINER_HORIZONTALSPACING = raapi.createAttribute(D_SHARP_CONTAINER, "horizontalSpacing", raapi.findPrimitiveDataType("Integer"));
			if (D_SHARP_CONTAINER_HORIZONTALSPACING == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute horizontalSpacing of the class D#Container.");
			}
			D_SHARP_CONTAINER_VERTICALSPACING = raapi.findAttribute(D_SHARP_CONTAINER, "verticalSpacing");
			if ((D_SHARP_CONTAINER_VERTICALSPACING == 0) && insertMetamodel)
				D_SHARP_CONTAINER_VERTICALSPACING = raapi.createAttribute(D_SHARP_CONTAINER, "verticalSpacing", raapi.findPrimitiveDataType("Integer"));
			if (D_SHARP_CONTAINER_VERTICALSPACING == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute verticalSpacing of the class D#Container.");
			}
			D_SHARP_CONTAINER_LEFTBORDER = raapi.findAttribute(D_SHARP_CONTAINER, "leftBorder");
			if ((D_SHARP_CONTAINER_LEFTBORDER == 0) && insertMetamodel)
				D_SHARP_CONTAINER_LEFTBORDER = raapi.createAttribute(D_SHARP_CONTAINER, "leftBorder", raapi.findPrimitiveDataType("Integer"));
			if (D_SHARP_CONTAINER_LEFTBORDER == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute leftBorder of the class D#Container.");
			}
			D_SHARP_CONTAINER_RIGHTBORDER = raapi.findAttribute(D_SHARP_CONTAINER, "rightBorder");
			if ((D_SHARP_CONTAINER_RIGHTBORDER == 0) && insertMetamodel)
				D_SHARP_CONTAINER_RIGHTBORDER = raapi.createAttribute(D_SHARP_CONTAINER, "rightBorder", raapi.findPrimitiveDataType("Integer"));
			if (D_SHARP_CONTAINER_RIGHTBORDER == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute rightBorder of the class D#Container.");
			}
			D_SHARP_CONTAINER_TOPBORDER = raapi.findAttribute(D_SHARP_CONTAINER, "topBorder");
			if ((D_SHARP_CONTAINER_TOPBORDER == 0) && insertMetamodel)
				D_SHARP_CONTAINER_TOPBORDER = raapi.createAttribute(D_SHARP_CONTAINER, "topBorder", raapi.findPrimitiveDataType("Integer"));
			if (D_SHARP_CONTAINER_TOPBORDER == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute topBorder of the class D#Container.");
			}
			D_SHARP_CONTAINER_BOTTOMBORDER = raapi.findAttribute(D_SHARP_CONTAINER, "bottomBorder");
			if ((D_SHARP_CONTAINER_BOTTOMBORDER == 0) && insertMetamodel)
				D_SHARP_CONTAINER_BOTTOMBORDER = raapi.createAttribute(D_SHARP_CONTAINER, "bottomBorder", raapi.findPrimitiveDataType("Integer"));
			if (D_SHARP_CONTAINER_BOTTOMBORDER == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute bottomBorder of the class D#Container.");
			}
			D_SHARP_CONTAINER_LEFTPADDING = raapi.findAttribute(D_SHARP_CONTAINER, "leftPadding");
			if ((D_SHARP_CONTAINER_LEFTPADDING == 0) && insertMetamodel)
				D_SHARP_CONTAINER_LEFTPADDING = raapi.createAttribute(D_SHARP_CONTAINER, "leftPadding", raapi.findPrimitiveDataType("Integer"));
			if (D_SHARP_CONTAINER_LEFTPADDING == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute leftPadding of the class D#Container.");
			}
			D_SHARP_CONTAINER_RIGHTPADDING = raapi.findAttribute(D_SHARP_CONTAINER, "rightPadding");
			if ((D_SHARP_CONTAINER_RIGHTPADDING == 0) && insertMetamodel)
				D_SHARP_CONTAINER_RIGHTPADDING = raapi.createAttribute(D_SHARP_CONTAINER, "rightPadding", raapi.findPrimitiveDataType("Integer"));
			if (D_SHARP_CONTAINER_RIGHTPADDING == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute rightPadding of the class D#Container.");
			}
			D_SHARP_CONTAINER_TOPPADDING = raapi.findAttribute(D_SHARP_CONTAINER, "topPadding");
			if ((D_SHARP_CONTAINER_TOPPADDING == 0) && insertMetamodel)
				D_SHARP_CONTAINER_TOPPADDING = raapi.createAttribute(D_SHARP_CONTAINER, "topPadding", raapi.findPrimitiveDataType("Integer"));
			if (D_SHARP_CONTAINER_TOPPADDING == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute topPadding of the class D#Container.");
			}
			D_SHARP_CONTAINER_BOTTOMPADDING = raapi.findAttribute(D_SHARP_CONTAINER, "bottomPadding");
			if ((D_SHARP_CONTAINER_BOTTOMPADDING == 0) && insertMetamodel)
				D_SHARP_CONTAINER_BOTTOMPADDING = raapi.createAttribute(D_SHARP_CONTAINER, "bottomPadding", raapi.findPrimitiveDataType("Integer"));
			if (D_SHARP_CONTAINER_BOTTOMPADDING == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute bottomPadding of the class D#Container.");
			}
			D_SHARP_CONTAINER_FOCUS = raapi.findAssociationEnd(D_SHARP_CONTAINER, "focus");
			if ((D_SHARP_CONTAINER_FOCUS == 0) && insertMetamodel) {
				D_SHARP_CONTAINER_FOCUS = raapi.createAssociation(D_SHARP_CONTAINER, D_SHARP_COMPONENT, "focusContainer", "focus", false);
			}
			if (D_SHARP_CONTAINER_FOCUS == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end focus of the class D#Container.");
			}
			D_SHARP_CONTAINER_COMPONENT = raapi.findAssociationEnd(D_SHARP_CONTAINER, "component");
			if ((D_SHARP_CONTAINER_COMPONENT == 0) && insertMetamodel) {
				D_SHARP_CONTAINER_COMPONENT = raapi.createAssociation(D_SHARP_CONTAINER, D_SHARP_COMPONENT, "container", "component", false);
			}
			if (D_SHARP_CONTAINER_COMPONENT == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end component of the class D#Container.");
			}
			D_SHARP_GROUPBOX_CAPTION = raapi.findAttribute(D_SHARP_GROUPBOX, "caption");
			if ((D_SHARP_GROUPBOX_CAPTION == 0) && insertMetamodel)
				D_SHARP_GROUPBOX_CAPTION = raapi.createAttribute(D_SHARP_GROUPBOX, "caption", raapi.findPrimitiveDataType("String"));
			if (D_SHARP_GROUPBOX_CAPTION == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute caption of the class D#GroupBox.");
			}
			D_SHARP_FORM_CAPTION = raapi.findAttribute(D_SHARP_FORM, "caption");
			if ((D_SHARP_FORM_CAPTION == 0) && insertMetamodel)
				D_SHARP_FORM_CAPTION = raapi.createAttribute(D_SHARP_FORM, "caption", raapi.findPrimitiveDataType("String"));
			if (D_SHARP_FORM_CAPTION == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute caption of the class D#Form.");
			}
			D_SHARP_FORM_BUTTONCLICKONCLOSE = raapi.findAttribute(D_SHARP_FORM, "buttonClickOnClose");
			if ((D_SHARP_FORM_BUTTONCLICKONCLOSE == 0) && insertMetamodel)
				D_SHARP_FORM_BUTTONCLICKONCLOSE = raapi.createAttribute(D_SHARP_FORM, "buttonClickOnClose", raapi.findPrimitiveDataType("Boolean"));
			if (D_SHARP_FORM_BUTTONCLICKONCLOSE == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute buttonClickOnClose of the class D#Form.");
			}
			D_SHARP_FORM_HASMINIMIZEBUTTON = raapi.findAttribute(D_SHARP_FORM, "hasMinimizeButton");
			if ((D_SHARP_FORM_HASMINIMIZEBUTTON == 0) && insertMetamodel)
				D_SHARP_FORM_HASMINIMIZEBUTTON = raapi.createAttribute(D_SHARP_FORM, "hasMinimizeButton", raapi.findPrimitiveDataType("Boolean"));
			if (D_SHARP_FORM_HASMINIMIZEBUTTON == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute hasMinimizeButton of the class D#Form.");
			}
			D_SHARP_FORM_HASMAXIMIZEBUTTON = raapi.findAttribute(D_SHARP_FORM, "hasMaximizeButton");
			if ((D_SHARP_FORM_HASMAXIMIZEBUTTON == 0) && insertMetamodel)
				D_SHARP_FORM_HASMAXIMIZEBUTTON = raapi.createAttribute(D_SHARP_FORM, "hasMaximizeButton", raapi.findPrimitiveDataType("Boolean"));
			if (D_SHARP_FORM_HASMAXIMIZEBUTTON == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute hasMaximizeButton of the class D#Form.");
			}
			D_SHARP_FORM_ISCLOSEBUTTONENABLED = raapi.findAttribute(D_SHARP_FORM, "isCloseButtonEnabled");
			if ((D_SHARP_FORM_ISCLOSEBUTTONENABLED == 0) && insertMetamodel)
				D_SHARP_FORM_ISCLOSEBUTTONENABLED = raapi.createAttribute(D_SHARP_FORM, "isCloseButtonEnabled", raapi.findPrimitiveDataType("Boolean"));
			if (D_SHARP_FORM_ISCLOSEBUTTONENABLED == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute isCloseButtonEnabled of the class D#Form.");
			}
			D_SHARP_FORM_LEFT = raapi.findAttribute(D_SHARP_FORM, "left");
			if ((D_SHARP_FORM_LEFT == 0) && insertMetamodel)
				D_SHARP_FORM_LEFT = raapi.createAttribute(D_SHARP_FORM, "left", raapi.findPrimitiveDataType("Integer"));
			if (D_SHARP_FORM_LEFT == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute left of the class D#Form.");
			}
			D_SHARP_FORM_TOP = raapi.findAttribute(D_SHARP_FORM, "top");
			if ((D_SHARP_FORM_TOP == 0) && insertMetamodel)
				D_SHARP_FORM_TOP = raapi.createAttribute(D_SHARP_FORM, "top", raapi.findPrimitiveDataType("Integer"));
			if (D_SHARP_FORM_TOP == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute top of the class D#Form.");
			}
			D_SHARP_FORM_WIDTH = raapi.findAttribute(D_SHARP_FORM, "width");
			if ((D_SHARP_FORM_WIDTH == 0) && insertMetamodel)
				D_SHARP_FORM_WIDTH = raapi.createAttribute(D_SHARP_FORM, "width", raapi.findPrimitiveDataType("Integer"));
			if (D_SHARP_FORM_WIDTH == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute width of the class D#Form.");
			}
			D_SHARP_FORM_HEIGHT = raapi.findAttribute(D_SHARP_FORM, "height");
			if ((D_SHARP_FORM_HEIGHT == 0) && insertMetamodel)
				D_SHARP_FORM_HEIGHT = raapi.createAttribute(D_SHARP_FORM, "height", raapi.findPrimitiveDataType("Integer"));
			if (D_SHARP_FORM_HEIGHT == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute height of the class D#Form.");
			}
			D_SHARP_FORM_EDITABLE = raapi.findAttribute(D_SHARP_FORM, "editable");
			if ((D_SHARP_FORM_EDITABLE == 0) && insertMetamodel)
				D_SHARP_FORM_EDITABLE = raapi.createAttribute(D_SHARP_FORM, "editable", raapi.findPrimitiveDataType("Boolean"));
			if (D_SHARP_FORM_EDITABLE == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute editable of the class D#Form.");
			}
			D_SHARP_FORM_DEFAULTBUTTON = raapi.findAssociationEnd(D_SHARP_FORM, "defaultButton");
			if ((D_SHARP_FORM_DEFAULTBUTTON == 0) && insertMetamodel) {
				D_SHARP_FORM_DEFAULTBUTTON = raapi.createAssociation(D_SHARP_FORM, D_SHARP_BUTTON, "defaultButtonForm", "defaultButton", false);
			}
			if (D_SHARP_FORM_DEFAULTBUTTON == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end defaultButton of the class D#Form.");
			}
			D_SHARP_FORM_CANCELBUTTON = raapi.findAssociationEnd(D_SHARP_FORM, "cancelButton");
			if ((D_SHARP_FORM_CANCELBUTTON == 0) && insertMetamodel) {
				D_SHARP_FORM_CANCELBUTTON = raapi.createAssociation(D_SHARP_FORM, D_SHARP_BUTTON, "cancelButtonForm", "cancelButton", false);
			}
			if (D_SHARP_FORM_CANCELBUTTON == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end cancelButton of the class D#Form.");
			}
			D_SHARP_FORM_OWNEDEVENT = raapi.findAssociationEnd(D_SHARP_FORM, "ownedEvent");
			if ((D_SHARP_FORM_OWNEDEVENT == 0) && insertMetamodel) {
				D_SHARP_FORM_OWNEDEVENT = raapi.createAssociation(D_SHARP_FORM, D_SHARP_EVENT, "form", "ownedEvent", false);
			}
			if (D_SHARP_FORM_OWNEDEVENT == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end ownedEvent of the class D#Form.");
			}
			D_SHARP_FORM_FOCUSED = raapi.findAssociationEnd(D_SHARP_FORM, "focused");
			if ((D_SHARP_FORM_FOCUSED == 0) && insertMetamodel) {
				D_SHARP_FORM_FOCUSED = raapi.createAssociation(D_SHARP_FORM, D_SHARP_COMPONENT, "form", "focused", false);
			}
			if (D_SHARP_FORM_FOCUSED == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end focused of the class D#Form.");
			}
			D_SHARP_FORM_CONSTANTFORMCALLINGBUTTON = raapi.findAssociationEnd(D_SHARP_FORM, "constantFormCallingButton");
			if ((D_SHARP_FORM_CONSTANTFORMCALLINGBUTTON == 0) && insertMetamodel) {
				D_SHARP_FORM_CONSTANTFORMCALLINGBUTTON = raapi.createAssociation(D_SHARP_FORM, D_SHARP_BUTTON, "constantFormOnClick", "constantFormCallingButton", false);
			}
			if (D_SHARP_FORM_CONSTANTFORMCALLINGBUTTON == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end constantFormCallingButton of the class D#Form.");
			}
			D_SHARP_FORM_FOCUSORDER = raapi.findAssociationEnd(D_SHARP_FORM, "focusOrder");
			if ((D_SHARP_FORM_FOCUSORDER == 0) && insertMetamodel) {
				D_SHARP_FORM_FOCUSORDER = raapi.createAssociation(D_SHARP_FORM, D_SHARP_COMPONENT, "foForm", "focusOrder", false);
			}
			if (D_SHARP_FORM_FOCUSORDER == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end focusOrder of the class D#Form.");
			}
			D_SHARP_FORM_CALLINGBUTTON = raapi.findAssociationEnd(D_SHARP_FORM, "callingButton");
			if ((D_SHARP_FORM_CALLINGBUTTON == 0) && insertMetamodel) {
				D_SHARP_FORM_CALLINGBUTTON = raapi.createAssociation(D_SHARP_FORM, D_SHARP_BUTTON, "formOnClick", "callingButton", false);
			}
			if (D_SHARP_FORM_CALLINGBUTTON == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end callingButton of the class D#Form.");
			}
			D_SHARP_FORM_FRAME = raapi.findAssociationEnd(D_SHARP_FORM, "frame");
			if ((D_SHARP_FORM_FRAME == 0) && insertMetamodel) {
				D_SHARP_FORM_FRAME = raapi.createAssociation(D_SHARP_FORM, FRAME, "form", "frame", false);
			}
			if (D_SHARP_FORM_FRAME == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end frame of the class D#Form.");
			}
			D_SHARP_TAB_CAPTION = raapi.findAttribute(D_SHARP_TAB, "caption");
			if ((D_SHARP_TAB_CAPTION == 0) && insertMetamodel)
				D_SHARP_TAB_CAPTION = raapi.createAttribute(D_SHARP_TAB, "caption", raapi.findPrimitiveDataType("String"));
			if (D_SHARP_TAB_CAPTION == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute caption of the class D#Tab.");
			}
			D_SHARP_TAB_TABCHANGEEVENT = raapi.findAssociationEnd(D_SHARP_TAB, "tabChangeEvent");
			if ((D_SHARP_TAB_TABCHANGEEVENT == 0) && insertMetamodel) {
				D_SHARP_TAB_TABCHANGEEVENT = raapi.createAssociation(D_SHARP_TAB, D_SHARP_TABCHANGEEVENT, "tab", "tabChangeEvent", false);
			}
			if (D_SHARP_TAB_TABCHANGEEVENT == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end tabChangeEvent of the class D#Tab.");
			}
			D_SHARP_TAB_TABCONTAINER = raapi.findAssociationEnd(D_SHARP_TAB, "tabContainer");
			if ((D_SHARP_TAB_TABCONTAINER == 0) && insertMetamodel) {
				D_SHARP_TAB_TABCONTAINER = raapi.createAssociation(D_SHARP_TAB, D_SHARP_TABCONTAINER, "activeTab", "tabContainer", false);
			}
			if (D_SHARP_TAB_TABCONTAINER == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end tabContainer of the class D#Tab.");
			}
			D_SHARP_TABCONTAINER_ACTIVETAB = raapi.findAssociationEnd(D_SHARP_TABCONTAINER, "activeTab");
			if ((D_SHARP_TABCONTAINER_ACTIVETAB == 0) && insertMetamodel) {
				D_SHARP_TABCONTAINER_ACTIVETAB = raapi.createAssociation(D_SHARP_TABCONTAINER, D_SHARP_TAB, "tabContainer", "activeTab", false);
			}
			if (D_SHARP_TABCONTAINER_ACTIVETAB == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end activeTab of the class D#TabContainer.");
			}
			D_SHARP_LABEL_CAPTION = raapi.findAttribute(D_SHARP_LABEL, "caption");
			if ((D_SHARP_LABEL_CAPTION == 0) && insertMetamodel)
				D_SHARP_LABEL_CAPTION = raapi.createAttribute(D_SHARP_LABEL, "caption", raapi.findPrimitiveDataType("String"));
			if (D_SHARP_LABEL_CAPTION == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute caption of the class D#Label.");
			}
			D_SHARP_RADIOBUTTON_CAPTION = raapi.findAttribute(D_SHARP_RADIOBUTTON, "caption");
			if ((D_SHARP_RADIOBUTTON_CAPTION == 0) && insertMetamodel)
				D_SHARP_RADIOBUTTON_CAPTION = raapi.createAttribute(D_SHARP_RADIOBUTTON, "caption", raapi.findPrimitiveDataType("String"));
			if (D_SHARP_RADIOBUTTON_CAPTION == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute caption of the class D#RadioButton.");
			}
			D_SHARP_RADIOBUTTON_SELECTED = raapi.findAttribute(D_SHARP_RADIOBUTTON, "selected");
			if ((D_SHARP_RADIOBUTTON_SELECTED == 0) && insertMetamodel)
				D_SHARP_RADIOBUTTON_SELECTED = raapi.createAttribute(D_SHARP_RADIOBUTTON, "selected", raapi.findPrimitiveDataType("Boolean"));
			if (D_SHARP_RADIOBUTTON_SELECTED == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute selected of the class D#RadioButton.");
			}
			D_SHARP_LISTBOX_MULTISELECT = raapi.findAttribute(D_SHARP_LISTBOX, "multiSelect");
			if ((D_SHARP_LISTBOX_MULTISELECT == 0) && insertMetamodel)
				D_SHARP_LISTBOX_MULTISELECT = raapi.createAttribute(D_SHARP_LISTBOX, "multiSelect", raapi.findPrimitiveDataType("Boolean"));
			if (D_SHARP_LISTBOX_MULTISELECT == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute multiSelect of the class D#ListBox.");
			}
			D_SHARP_LISTBOX_ITEM = raapi.findAssociationEnd(D_SHARP_LISTBOX, "item");
			if ((D_SHARP_LISTBOX_ITEM == 0) && insertMetamodel) {
				D_SHARP_LISTBOX_ITEM = raapi.createAssociation(D_SHARP_LISTBOX, D_SHARP_ITEM, "listBox", "item", false);
			}
			if (D_SHARP_LISTBOX_ITEM == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end item of the class D#ListBox.");
			}
			D_SHARP_LISTBOX_SELECTED = raapi.findAssociationEnd(D_SHARP_LISTBOX, "selected");
			if ((D_SHARP_LISTBOX_SELECTED == 0) && insertMetamodel) {
				D_SHARP_LISTBOX_SELECTED = raapi.createAssociation(D_SHARP_LISTBOX, D_SHARP_ITEM, "parentListBox", "selected", false);
			}
			if (D_SHARP_LISTBOX_SELECTED == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end selected of the class D#ListBox.");
			}
			D_SHARP_VTABLETYPE_EDITABLE = raapi.findAttribute(D_SHARP_VTABLETYPE, "editable");
			if ((D_SHARP_VTABLETYPE_EDITABLE == 0) && insertMetamodel)
				D_SHARP_VTABLETYPE_EDITABLE = raapi.createAttribute(D_SHARP_VTABLETYPE, "editable", raapi.findPrimitiveDataType("Boolean"));
			if (D_SHARP_VTABLETYPE_EDITABLE == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute editable of the class D#VTableType.");
			}
			D_SHARP_VTABLETYPE_MOVABLEROWS = raapi.findAttribute(D_SHARP_VTABLETYPE, "movableRows");
			if ((D_SHARP_VTABLETYPE_MOVABLEROWS == 0) && insertMetamodel)
				D_SHARP_VTABLETYPE_MOVABLEROWS = raapi.createAttribute(D_SHARP_VTABLETYPE, "movableRows", raapi.findPrimitiveDataType("Boolean"));
			if (D_SHARP_VTABLETYPE_MOVABLEROWS == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute movableRows of the class D#VTableType.");
			}
			D_SHARP_VTABLETYPE_MOVABLECOLUMNS = raapi.findAttribute(D_SHARP_VTABLETYPE, "movableColumns");
			if ((D_SHARP_VTABLETYPE_MOVABLECOLUMNS == 0) && insertMetamodel)
				D_SHARP_VTABLETYPE_MOVABLECOLUMNS = raapi.createAttribute(D_SHARP_VTABLETYPE, "movableColumns", raapi.findPrimitiveDataType("Boolean"));
			if (D_SHARP_VTABLETYPE_MOVABLECOLUMNS == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute movableColumns of the class D#VTableType.");
			}
			D_SHARP_VTABLETYPE_VERTICALALIGNMENT = raapi.findAttribute(D_SHARP_VTABLETYPE, "verticalAlignment");
			if ((D_SHARP_VTABLETYPE_VERTICALALIGNMENT == 0) && insertMetamodel)
				D_SHARP_VTABLETYPE_VERTICALALIGNMENT = raapi.createAttribute(D_SHARP_VTABLETYPE, "verticalAlignment", raapi.findPrimitiveDataType("Integer"));
			if (D_SHARP_VTABLETYPE_VERTICALALIGNMENT == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute verticalAlignment of the class D#VTableType.");
			}
			D_SHARP_VTABLETYPE_INSERTBUTTONCAPTION = raapi.findAttribute(D_SHARP_VTABLETYPE, "insertButtonCaption");
			if ((D_SHARP_VTABLETYPE_INSERTBUTTONCAPTION == 0) && insertMetamodel)
				D_SHARP_VTABLETYPE_INSERTBUTTONCAPTION = raapi.createAttribute(D_SHARP_VTABLETYPE, "insertButtonCaption", raapi.findPrimitiveDataType("String"));
			if (D_SHARP_VTABLETYPE_INSERTBUTTONCAPTION == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute insertButtonCaption of the class D#VTableType.");
			}
			D_SHARP_VTABLETYPE_DELETEBUTTONCAPTION = raapi.findAttribute(D_SHARP_VTABLETYPE, "deleteButtonCaption");
			if ((D_SHARP_VTABLETYPE_DELETEBUTTONCAPTION == 0) && insertMetamodel)
				D_SHARP_VTABLETYPE_DELETEBUTTONCAPTION = raapi.createAttribute(D_SHARP_VTABLETYPE, "deleteButtonCaption", raapi.findPrimitiveDataType("String"));
			if (D_SHARP_VTABLETYPE_DELETEBUTTONCAPTION == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute deleteButtonCaption of the class D#VTableType.");
			}
			D_SHARP_VTABLETYPE_ROWHEIGHT = raapi.findAttribute(D_SHARP_VTABLETYPE, "rowHeight");
			if ((D_SHARP_VTABLETYPE_ROWHEIGHT == 0) && insertMetamodel)
				D_SHARP_VTABLETYPE_ROWHEIGHT = raapi.createAttribute(D_SHARP_VTABLETYPE, "rowHeight", raapi.findPrimitiveDataType("Integer"));
			if (D_SHARP_VTABLETYPE_ROWHEIGHT == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute rowHeight of the class D#VTableType.");
			}
			D_SHARP_VTABLETYPE_AUTOORDERROWS = raapi.findAttribute(D_SHARP_VTABLETYPE, "autoOrderRows");
			if ((D_SHARP_VTABLETYPE_AUTOORDERROWS == 0) && insertMetamodel)
				D_SHARP_VTABLETYPE_AUTOORDERROWS = raapi.createAttribute(D_SHARP_VTABLETYPE, "autoOrderRows", raapi.findPrimitiveDataType("Boolean"));
			if (D_SHARP_VTABLETYPE_AUTOORDERROWS == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute autoOrderRows of the class D#VTableType.");
			}
			D_SHARP_VTABLETYPE_AUTOORDERCOLUMNS = raapi.findAttribute(D_SHARP_VTABLETYPE, "autoOrderColumns");
			if ((D_SHARP_VTABLETYPE_AUTOORDERCOLUMNS == 0) && insertMetamodel)
				D_SHARP_VTABLETYPE_AUTOORDERCOLUMNS = raapi.createAttribute(D_SHARP_VTABLETYPE, "autoOrderColumns", raapi.findPrimitiveDataType("Boolean"));
			if (D_SHARP_VTABLETYPE_AUTOORDERCOLUMNS == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute autoOrderColumns of the class D#VTableType.");
			}
			D_SHARP_VTABLETYPE_VTABLE = raapi.findAssociationEnd(D_SHARP_VTABLETYPE, "vTable");
			if ((D_SHARP_VTABLETYPE_VTABLE == 0) && insertMetamodel) {
				D_SHARP_VTABLETYPE_VTABLE = raapi.createAssociation(D_SHARP_VTABLETYPE, D_SHARP_VTABLE, "type", "vTable", false);
			}
			if (D_SHARP_VTABLETYPE_VTABLE == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end vTable of the class D#VTableType.");
			}
			D_SHARP_VTABLETYPE_COLUMNTYPE = raapi.findAssociationEnd(D_SHARP_VTABLETYPE, "columnType");
			if ((D_SHARP_VTABLETYPE_COLUMNTYPE == 0) && insertMetamodel) {
				D_SHARP_VTABLETYPE_COLUMNTYPE = raapi.createAssociation(D_SHARP_VTABLETYPE, D_SHARP_VTABLECOLUMNTYPE, "vTableType", "columnType", false);
			}
			if (D_SHARP_VTABLETYPE_COLUMNTYPE == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end columnType of the class D#VTableType.");
			}
			D_SHARP_VTABLETYPE_COLUMN = raapi.findAssociationEnd(D_SHARP_VTABLETYPE, "column");
			if ((D_SHARP_VTABLETYPE_COLUMN == 0) && insertMetamodel) {
				D_SHARP_VTABLETYPE_COLUMN = raapi.createAssociation(D_SHARP_VTABLETYPE, D_SHARP_VTABLECOLUMNTYPE, "vTableTypeOwner", "column", false);
			}
			if (D_SHARP_VTABLETYPE_COLUMN == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end column of the class D#VTableType.");
			}
			D_SHARP_VTABLETYPE_BUTTON = raapi.findAssociationEnd(D_SHARP_VTABLETYPE, "button");
			if ((D_SHARP_VTABLETYPE_BUTTON == 0) && insertMetamodel) {
				D_SHARP_VTABLETYPE_BUTTON = raapi.createAssociation(D_SHARP_VTABLETYPE, D_SHARP_BUTTON, "vTableType", "button", false);
			}
			if (D_SHARP_VTABLETYPE_BUTTON == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end button of the class D#VTableType.");
			}
			D_SHARP_VTABLETYPE_COLUMNMOVEDEVENT = raapi.findAssociationEnd(D_SHARP_VTABLETYPE, "columnMovedEvent");
			if ((D_SHARP_VTABLETYPE_COLUMNMOVEDEVENT == 0) && insertMetamodel) {
				D_SHARP_VTABLETYPE_COLUMNMOVEDEVENT = raapi.createAssociation(D_SHARP_VTABLETYPE, D_SHARP_COLUMNMOVEDEVENT, "column", "columnMovedEvent", false);
			}
			if (D_SHARP_VTABLETYPE_COLUMNMOVEDEVENT == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end columnMovedEvent of the class D#VTableType.");
			}
			D_SHARP_VTABLETYPE_BEFORECOLUMNMOVEDEVENT = raapi.findAssociationEnd(D_SHARP_VTABLETYPE, "beforeColumnMovedEvent");
			if ((D_SHARP_VTABLETYPE_BEFORECOLUMNMOVEDEVENT == 0) && insertMetamodel) {
				D_SHARP_VTABLETYPE_BEFORECOLUMNMOVEDEVENT = raapi.createAssociation(D_SHARP_VTABLETYPE, D_SHARP_COLUMNMOVEDEVENT, "before", "beforeColumnMovedEvent", false);
			}
			if (D_SHARP_VTABLETYPE_BEFORECOLUMNMOVEDEVENT == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end beforeColumnMovedEvent of the class D#VTableType.");
			}
			D_SHARP_VTABLETYPE_AFTERCOLUMNMOVEDEVENT = raapi.findAssociationEnd(D_SHARP_VTABLETYPE, "afterColumnMovedEvent");
			if ((D_SHARP_VTABLETYPE_AFTERCOLUMNMOVEDEVENT == 0) && insertMetamodel) {
				D_SHARP_VTABLETYPE_AFTERCOLUMNMOVEDEVENT = raapi.createAssociation(D_SHARP_VTABLETYPE, D_SHARP_COLUMNMOVEDEVENT, "after", "afterColumnMovedEvent", false);
			}
			if (D_SHARP_VTABLETYPE_AFTERCOLUMNMOVEDEVENT == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end afterColumnMovedEvent of the class D#VTableType.");
			}
			D_SHARP_VTABLE_TYPE = raapi.findAssociationEnd(D_SHARP_VTABLE, "type");
			if ((D_SHARP_VTABLE_TYPE == 0) && insertMetamodel) {
				D_SHARP_VTABLE_TYPE = raapi.createAssociation(D_SHARP_VTABLE, D_SHARP_VTABLETYPE, "vTable", "type", false);
			}
			if (D_SHARP_VTABLE_TYPE == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end type of the class D#VTable.");
			}
			D_SHARP_VTABLE_VTABLEROW = raapi.findAssociationEnd(D_SHARP_VTABLE, "vTableRow");
			if ((D_SHARP_VTABLE_VTABLEROW == 0) && insertMetamodel) {
				D_SHARP_VTABLE_VTABLEROW = raapi.createAssociation(D_SHARP_VTABLE, D_SHARP_VTABLEROW, "vTable", "vTableRow", false);
			}
			if (D_SHARP_VTABLE_VTABLEROW == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end vTableRow of the class D#VTable.");
			}
			D_SHARP_VTABLE_COPYTOCLIPBOARD = raapi.findAssociationEnd(D_SHARP_VTABLE, "copyToClipboard");
			if ((D_SHARP_VTABLE_COPYTOCLIPBOARD == 0) && insertMetamodel) {
				D_SHARP_VTABLE_COPYTOCLIPBOARD = raapi.createAssociation(D_SHARP_VTABLE, D_SHARP_COPYTOCLIPBOARDCMD, "vTable", "copyToClipboard", false);
			}
			if (D_SHARP_VTABLE_COPYTOCLIPBOARD == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end copyToClipboard of the class D#VTable.");
			}
			D_SHARP_VTABLE_SELECTEDROW = raapi.findAssociationEnd(D_SHARP_VTABLE, "selectedRow");
			if ((D_SHARP_VTABLE_SELECTEDROW == 0) && insertMetamodel) {
				D_SHARP_VTABLE_SELECTEDROW = raapi.createAssociation(D_SHARP_VTABLE, D_SHARP_VTABLEROW, "parentTable", "selectedRow", false);
			}
			if (D_SHARP_VTABLE_SELECTEDROW == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end selectedRow of the class D#VTable.");
			}
			D_SHARP_VTABLEROW_EDITED = raapi.findAttribute(D_SHARP_VTABLEROW, "edited");
			if ((D_SHARP_VTABLEROW_EDITED == 0) && insertMetamodel)
				D_SHARP_VTABLEROW_EDITED = raapi.createAttribute(D_SHARP_VTABLEROW, "edited", raapi.findPrimitiveDataType("Boolean"));
			if (D_SHARP_VTABLEROW_EDITED == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute edited of the class D#VTableRow.");
			}
			D_SHARP_VTABLEROW_INSERTED = raapi.findAttribute(D_SHARP_VTABLEROW, "inserted");
			if ((D_SHARP_VTABLEROW_INSERTED == 0) && insertMetamodel)
				D_SHARP_VTABLEROW_INSERTED = raapi.createAttribute(D_SHARP_VTABLEROW, "inserted", raapi.findPrimitiveDataType("Boolean"));
			if (D_SHARP_VTABLEROW_INSERTED == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute inserted of the class D#VTableRow.");
			}
			D_SHARP_VTABLEROW_DELETED = raapi.findAttribute(D_SHARP_VTABLEROW, "deleted");
			if ((D_SHARP_VTABLEROW_DELETED == 0) && insertMetamodel)
				D_SHARP_VTABLEROW_DELETED = raapi.createAttribute(D_SHARP_VTABLEROW, "deleted", raapi.findPrimitiveDataType("Boolean"));
			if (D_SHARP_VTABLEROW_DELETED == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute deleted of the class D#VTableRow.");
			}
			D_SHARP_VTABLEROW_VERTICALALIGNMENT = raapi.findAttribute(D_SHARP_VTABLEROW, "verticalAlignment");
			if ((D_SHARP_VTABLEROW_VERTICALALIGNMENT == 0) && insertMetamodel)
				D_SHARP_VTABLEROW_VERTICALALIGNMENT = raapi.createAttribute(D_SHARP_VTABLEROW, "verticalAlignment", raapi.findPrimitiveDataType("Integer"));
			if (D_SHARP_VTABLEROW_VERTICALALIGNMENT == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute verticalAlignment of the class D#VTableRow.");
			}
			D_SHARP_VTABLEROW_VTABLE = raapi.findAssociationEnd(D_SHARP_VTABLEROW, "vTable");
			if ((D_SHARP_VTABLEROW_VTABLE == 0) && insertMetamodel) {
				D_SHARP_VTABLEROW_VTABLE = raapi.createAssociation(D_SHARP_VTABLEROW, D_SHARP_VTABLE, "vTableRow", "vTable", false);
			}
			if (D_SHARP_VTABLEROW_VTABLE == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end vTable of the class D#VTableRow.");
			}
			D_SHARP_VTABLEROW_VTABLECELL = raapi.findAssociationEnd(D_SHARP_VTABLEROW, "vTableCell");
			if ((D_SHARP_VTABLEROW_VTABLECELL == 0) && insertMetamodel) {
				D_SHARP_VTABLEROW_VTABLECELL = raapi.createAssociation(D_SHARP_VTABLEROW, D_SHARP_VTABLECELL, "vTableRow", "vTableCell", false);
			}
			if (D_SHARP_VTABLEROW_VTABLECELL == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end vTableCell of the class D#VTableRow.");
			}
			D_SHARP_VTABLEROW_ACTIVECELL = raapi.findAssociationEnd(D_SHARP_VTABLEROW, "activeCell");
			if ((D_SHARP_VTABLEROW_ACTIVECELL == 0) && insertMetamodel) {
				D_SHARP_VTABLEROW_ACTIVECELL = raapi.createAssociation(D_SHARP_VTABLEROW, D_SHARP_VTABLECELL, "parentRow", "activeCell", false);
			}
			if (D_SHARP_VTABLEROW_ACTIVECELL == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end activeCell of the class D#VTableRow.");
			}
			D_SHARP_VTABLEROW_PARENTTABLE = raapi.findAssociationEnd(D_SHARP_VTABLEROW, "parentTable");
			if ((D_SHARP_VTABLEROW_PARENTTABLE == 0) && insertMetamodel) {
				D_SHARP_VTABLEROW_PARENTTABLE = raapi.createAssociation(D_SHARP_VTABLEROW, D_SHARP_VTABLE, "selectedRow", "parentTable", false);
			}
			if (D_SHARP_VTABLEROW_PARENTTABLE == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end parentTable of the class D#VTableRow.");
			}
			D_SHARP_VTABLEROW_ROWMOVEDEVENT = raapi.findAssociationEnd(D_SHARP_VTABLEROW, "rowMovedEvent");
			if ((D_SHARP_VTABLEROW_ROWMOVEDEVENT == 0) && insertMetamodel) {
				D_SHARP_VTABLEROW_ROWMOVEDEVENT = raapi.createAssociation(D_SHARP_VTABLEROW, D_SHARP_ROWMOVEDEVENT, "row", "rowMovedEvent", false);
			}
			if (D_SHARP_VTABLEROW_ROWMOVEDEVENT == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end rowMovedEvent of the class D#VTableRow.");
			}
			D_SHARP_VTABLEROW_BEFOREROWMOVEDEVENT = raapi.findAssociationEnd(D_SHARP_VTABLEROW, "beforeRowMovedEvent");
			if ((D_SHARP_VTABLEROW_BEFOREROWMOVEDEVENT == 0) && insertMetamodel) {
				D_SHARP_VTABLEROW_BEFOREROWMOVEDEVENT = raapi.createAssociation(D_SHARP_VTABLEROW, D_SHARP_ROWMOVEDEVENT, "before", "beforeRowMovedEvent", false);
			}
			if (D_SHARP_VTABLEROW_BEFOREROWMOVEDEVENT == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end beforeRowMovedEvent of the class D#VTableRow.");
			}
			D_SHARP_VTABLEROW_AFTERROWMOVEDEVENT = raapi.findAssociationEnd(D_SHARP_VTABLEROW, "afterRowMovedEvent");
			if ((D_SHARP_VTABLEROW_AFTERROWMOVEDEVENT == 0) && insertMetamodel) {
				D_SHARP_VTABLEROW_AFTERROWMOVEDEVENT = raapi.createAssociation(D_SHARP_VTABLEROW, D_SHARP_ROWMOVEDEVENT, "after", "afterRowMovedEvent", false);
			}
			if (D_SHARP_VTABLEROW_AFTERROWMOVEDEVENT == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end afterRowMovedEvent of the class D#VTableRow.");
			}
			D_SHARP_TABLECOMPONENT_OUTLINECOLOR = raapi.findAttribute(D_SHARP_TABLECOMPONENT, "outlineColor");
			if ((D_SHARP_TABLECOMPONENT_OUTLINECOLOR == 0) && insertMetamodel)
				D_SHARP_TABLECOMPONENT_OUTLINECOLOR = raapi.createAttribute(D_SHARP_TABLECOMPONENT, "outlineColor", raapi.findPrimitiveDataType("Integer"));
			if (D_SHARP_TABLECOMPONENT_OUTLINECOLOR == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute outlineColor of the class D#TableComponent.");
			}
			D_SHARP_TABLECOMPONENT_VTABLECOLUMNTYPE = raapi.findAssociationEnd(D_SHARP_TABLECOMPONENT, "vTableColumnType");
			if ((D_SHARP_TABLECOMPONENT_VTABLECOLUMNTYPE == 0) && insertMetamodel) {
				D_SHARP_TABLECOMPONENT_VTABLECOLUMNTYPE = raapi.createAssociation(D_SHARP_TABLECOMPONENT, D_SHARP_VTABLECOLUMNTYPE, "defaultComponentType", "vTableColumnType", false);
			}
			if (D_SHARP_TABLECOMPONENT_VTABLECOLUMNTYPE == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end vTableColumnType of the class D#TableComponent.");
			}
			D_SHARP_TABLECOMPONENT_VTABLECOLUMNTYPEOWNER = raapi.findAssociationEnd(D_SHARP_TABLECOMPONENT, "vTableColumnTypeOwner");
			if ((D_SHARP_TABLECOMPONENT_VTABLECOLUMNTYPEOWNER == 0) && insertMetamodel) {
				D_SHARP_TABLECOMPONENT_VTABLECOLUMNTYPEOWNER = raapi.createAssociation(D_SHARP_TABLECOMPONENT, D_SHARP_VTABLECOLUMNTYPE, "defaultComponent", "vTableColumnTypeOwner", false);
			}
			if (D_SHARP_TABLECOMPONENT_VTABLECOLUMNTYPEOWNER == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end vTableColumnTypeOwner of the class D#TableComponent.");
			}
			D_SHARP_TABLECOMPONENT_VTABLECELL = raapi.findAssociationEnd(D_SHARP_TABLECOMPONENT, "vTableCell");
			if ((D_SHARP_TABLECOMPONENT_VTABLECELL == 0) && insertMetamodel) {
				D_SHARP_TABLECOMPONENT_VTABLECELL = raapi.createAssociation(D_SHARP_TABLECOMPONENT, D_SHARP_VTABLECELL, "componentType", "vTableCell", false);
			}
			if (D_SHARP_TABLECOMPONENT_VTABLECELL == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end vTableCell of the class D#TableComponent.");
			}
			D_SHARP_TABLECOMPONENT_VTABLECELLOWNER = raapi.findAssociationEnd(D_SHARP_TABLECOMPONENT, "vTableCellOwner");
			if ((D_SHARP_TABLECOMPONENT_VTABLECELLOWNER == 0) && insertMetamodel) {
				D_SHARP_TABLECOMPONENT_VTABLECELLOWNER = raapi.createAssociation(D_SHARP_TABLECOMPONENT, D_SHARP_VTABLECELL, "component", "vTableCellOwner", false);
			}
			if (D_SHARP_TABLECOMPONENT_VTABLECELLOWNER == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end vTableCellOwner of the class D#TableComponent.");
			}
			D_SHARP_BUTTON_CAPTION = raapi.findAttribute(D_SHARP_BUTTON, "caption");
			if ((D_SHARP_BUTTON_CAPTION == 0) && insertMetamodel)
				D_SHARP_BUTTON_CAPTION = raapi.createAttribute(D_SHARP_BUTTON, "caption", raapi.findPrimitiveDataType("String"));
			if (D_SHARP_BUTTON_CAPTION == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute caption of the class D#Button.");
			}
			D_SHARP_BUTTON_CLOSEONCLICK = raapi.findAttribute(D_SHARP_BUTTON, "closeOnClick");
			if ((D_SHARP_BUTTON_CLOSEONCLICK == 0) && insertMetamodel)
				D_SHARP_BUTTON_CLOSEONCLICK = raapi.createAttribute(D_SHARP_BUTTON, "closeOnClick", raapi.findPrimitiveDataType("Boolean"));
			if (D_SHARP_BUTTON_CLOSEONCLICK == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute closeOnClick of the class D#Button.");
			}
			D_SHARP_BUTTON_DELETEONCLICK = raapi.findAttribute(D_SHARP_BUTTON, "deleteOnClick");
			if ((D_SHARP_BUTTON_DELETEONCLICK == 0) && insertMetamodel)
				D_SHARP_BUTTON_DELETEONCLICK = raapi.createAttribute(D_SHARP_BUTTON, "deleteOnClick", raapi.findPrimitiveDataType("Boolean"));
			if (D_SHARP_BUTTON_DELETEONCLICK == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute deleteOnClick of the class D#Button.");
			}
			D_SHARP_BUTTON_DEFAULTBUTTONFORM = raapi.findAssociationEnd(D_SHARP_BUTTON, "defaultButtonForm");
			if ((D_SHARP_BUTTON_DEFAULTBUTTONFORM == 0) && insertMetamodel) {
				D_SHARP_BUTTON_DEFAULTBUTTONFORM = raapi.createAssociation(D_SHARP_BUTTON, D_SHARP_FORM, "defaultButton", "defaultButtonForm", false);
			}
			if (D_SHARP_BUTTON_DEFAULTBUTTONFORM == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end defaultButtonForm of the class D#Button.");
			}
			D_SHARP_BUTTON_CANCELBUTTONFORM = raapi.findAssociationEnd(D_SHARP_BUTTON, "cancelButtonForm");
			if ((D_SHARP_BUTTON_CANCELBUTTONFORM == 0) && insertMetamodel) {
				D_SHARP_BUTTON_CANCELBUTTONFORM = raapi.createAssociation(D_SHARP_BUTTON, D_SHARP_FORM, "cancelButton", "cancelButtonForm", false);
			}
			if (D_SHARP_BUTTON_CANCELBUTTONFORM == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end cancelButtonForm of the class D#Button.");
			}
			D_SHARP_BUTTON_CONSTANTFORMONCLICK = raapi.findAssociationEnd(D_SHARP_BUTTON, "constantFormOnClick");
			if ((D_SHARP_BUTTON_CONSTANTFORMONCLICK == 0) && insertMetamodel) {
				D_SHARP_BUTTON_CONSTANTFORMONCLICK = raapi.createAssociation(D_SHARP_BUTTON, D_SHARP_FORM, "constantFormCallingButton", "constantFormOnClick", false);
			}
			if (D_SHARP_BUTTON_CONSTANTFORMONCLICK == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end constantFormOnClick of the class D#Button.");
			}
			D_SHARP_BUTTON_VTABLETYPE = raapi.findAssociationEnd(D_SHARP_BUTTON, "vTableType");
			if ((D_SHARP_BUTTON_VTABLETYPE == 0) && insertMetamodel) {
				D_SHARP_BUTTON_VTABLETYPE = raapi.createAssociation(D_SHARP_BUTTON, D_SHARP_VTABLETYPE, "button", "vTableType", false);
			}
			if (D_SHARP_BUTTON_VTABLETYPE == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end vTableType of the class D#Button.");
			}
			D_SHARP_BUTTON_FORMONCLICK = raapi.findAssociationEnd(D_SHARP_BUTTON, "formOnClick");
			if ((D_SHARP_BUTTON_FORMONCLICK == 0) && insertMetamodel) {
				D_SHARP_BUTTON_FORMONCLICK = raapi.createAssociation(D_SHARP_BUTTON, D_SHARP_FORM, "callingButton", "formOnClick", false);
			}
			if (D_SHARP_BUTTON_FORMONCLICK == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end formOnClick of the class D#Button.");
			}
			D_SHARP_CHECKBOX_CAPTION = raapi.findAttribute(D_SHARP_CHECKBOX, "caption");
			if ((D_SHARP_CHECKBOX_CAPTION == 0) && insertMetamodel)
				D_SHARP_CHECKBOX_CAPTION = raapi.createAttribute(D_SHARP_CHECKBOX, "caption", raapi.findPrimitiveDataType("String"));
			if (D_SHARP_CHECKBOX_CAPTION == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute caption of the class D#CheckBox.");
			}
			D_SHARP_CHECKBOX_CHECKED = raapi.findAttribute(D_SHARP_CHECKBOX, "checked");
			if ((D_SHARP_CHECKBOX_CHECKED == 0) && insertMetamodel)
				D_SHARP_CHECKBOX_CHECKED = raapi.createAttribute(D_SHARP_CHECKBOX, "checked", raapi.findPrimitiveDataType("Boolean"));
			if (D_SHARP_CHECKBOX_CHECKED == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute checked of the class D#CheckBox.");
			}
			D_SHARP_CHECKBOX_EDITABLE = raapi.findAttribute(D_SHARP_CHECKBOX, "editable");
			if ((D_SHARP_CHECKBOX_EDITABLE == 0) && insertMetamodel)
				D_SHARP_CHECKBOX_EDITABLE = raapi.createAttribute(D_SHARP_CHECKBOX, "editable", raapi.findPrimitiveDataType("Boolean"));
			if (D_SHARP_CHECKBOX_EDITABLE == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute editable of the class D#CheckBox.");
			}
			D_SHARP_COMBOBOX_TEXT = raapi.findAttribute(D_SHARP_COMBOBOX, "text");
			if ((D_SHARP_COMBOBOX_TEXT == 0) && insertMetamodel)
				D_SHARP_COMBOBOX_TEXT = raapi.createAttribute(D_SHARP_COMBOBOX, "text", raapi.findPrimitiveDataType("String"));
			if (D_SHARP_COMBOBOX_TEXT == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute text of the class D#ComboBox.");
			}
			D_SHARP_COMBOBOX_EDITABLE = raapi.findAttribute(D_SHARP_COMBOBOX, "editable");
			if ((D_SHARP_COMBOBOX_EDITABLE == 0) && insertMetamodel)
				D_SHARP_COMBOBOX_EDITABLE = raapi.createAttribute(D_SHARP_COMBOBOX, "editable", raapi.findPrimitiveDataType("Boolean"));
			if (D_SHARP_COMBOBOX_EDITABLE == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute editable of the class D#ComboBox.");
			}
			D_SHARP_COMBOBOX_ITEM = raapi.findAssociationEnd(D_SHARP_COMBOBOX, "item");
			if ((D_SHARP_COMBOBOX_ITEM == 0) && insertMetamodel) {
				D_SHARP_COMBOBOX_ITEM = raapi.createAssociation(D_SHARP_COMBOBOX, D_SHARP_ITEM, "comboBox", "item", false);
			}
			if (D_SHARP_COMBOBOX_ITEM == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end item of the class D#ComboBox.");
			}
			D_SHARP_COMBOBOX_SELECTED = raapi.findAssociationEnd(D_SHARP_COMBOBOX, "selected");
			if ((D_SHARP_COMBOBOX_SELECTED == 0) && insertMetamodel) {
				D_SHARP_COMBOBOX_SELECTED = raapi.createAssociation(D_SHARP_COMBOBOX, D_SHARP_ITEM, "parentComboBox", "selected", false);
			}
			if (D_SHARP_COMBOBOX_SELECTED == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end selected of the class D#ComboBox.");
			}
			D_SHARP_ITEM_VALUE = raapi.findAttribute(D_SHARP_ITEM, "value");
			if ((D_SHARP_ITEM_VALUE == 0) && insertMetamodel)
				D_SHARP_ITEM_VALUE = raapi.createAttribute(D_SHARP_ITEM, "value", raapi.findPrimitiveDataType("String"));
			if (D_SHARP_ITEM_VALUE == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute value of the class D#Item.");
			}
			D_SHARP_ITEM_LISTBOX = raapi.findAssociationEnd(D_SHARP_ITEM, "listBox");
			if ((D_SHARP_ITEM_LISTBOX == 0) && insertMetamodel) {
				D_SHARP_ITEM_LISTBOX = raapi.createAssociation(D_SHARP_ITEM, D_SHARP_LISTBOX, "item", "listBox", false);
			}
			if (D_SHARP_ITEM_LISTBOX == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end listBox of the class D#Item.");
			}
			D_SHARP_ITEM_COMBOBOX = raapi.findAssociationEnd(D_SHARP_ITEM, "comboBox");
			if ((D_SHARP_ITEM_COMBOBOX == 0) && insertMetamodel) {
				D_SHARP_ITEM_COMBOBOX = raapi.createAssociation(D_SHARP_ITEM, D_SHARP_COMBOBOX, "item", "comboBox", false);
			}
			if (D_SHARP_ITEM_COMBOBOX == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end comboBox of the class D#Item.");
			}
			D_SHARP_ITEM_PARENTCOMBOBOX = raapi.findAssociationEnd(D_SHARP_ITEM, "parentComboBox");
			if ((D_SHARP_ITEM_PARENTCOMBOBOX == 0) && insertMetamodel) {
				D_SHARP_ITEM_PARENTCOMBOBOX = raapi.createAssociation(D_SHARP_ITEM, D_SHARP_COMBOBOX, "selected", "parentComboBox", false);
			}
			if (D_SHARP_ITEM_PARENTCOMBOBOX == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end parentComboBox of the class D#Item.");
			}
			D_SHARP_ITEM_PARENTLISTBOX = raapi.findAssociationEnd(D_SHARP_ITEM, "parentListBox");
			if ((D_SHARP_ITEM_PARENTLISTBOX == 0) && insertMetamodel) {
				D_SHARP_ITEM_PARENTLISTBOX = raapi.createAssociation(D_SHARP_ITEM, D_SHARP_LISTBOX, "selected", "parentListBox", false);
			}
			if (D_SHARP_ITEM_PARENTLISTBOX == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end parentListBox of the class D#Item.");
			}
			D_SHARP_ITEM_VTABLECELL = raapi.findAssociationEnd(D_SHARP_ITEM, "vTableCell");
			if ((D_SHARP_ITEM_VTABLECELL == 0) && insertMetamodel) {
				D_SHARP_ITEM_VTABLECELL = raapi.createAssociation(D_SHARP_ITEM, D_SHARP_VTABLECELL, "selectedItem", "vTableCell", false);
			}
			if (D_SHARP_ITEM_VTABLECELL == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end vTableCell of the class D#Item.");
			}
			D_SHARP_ITEM_SLISTBOXCHANGEEVENT = raapi.findAssociationEnd(D_SHARP_ITEM, "sListBoxChangeEvent");
			if ((D_SHARP_ITEM_SLISTBOXCHANGEEVENT == 0) && insertMetamodel) {
				D_SHARP_ITEM_SLISTBOXCHANGEEVENT = raapi.createAssociation(D_SHARP_ITEM, D_SHARP_LISTBOXCHANGEEVENT, "selected", "sListBoxChangeEvent", false);
			}
			if (D_SHARP_ITEM_SLISTBOXCHANGEEVENT == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end sListBoxChangeEvent of the class D#Item.");
			}
			D_SHARP_ITEM_DLISTBOXCHANGEEVENT = raapi.findAssociationEnd(D_SHARP_ITEM, "dListBoxChangeEvent");
			if ((D_SHARP_ITEM_DLISTBOXCHANGEEVENT == 0) && insertMetamodel) {
				D_SHARP_ITEM_DLISTBOXCHANGEEVENT = raapi.createAssociation(D_SHARP_ITEM, D_SHARP_LISTBOXCHANGEEVENT, "deselected", "dListBoxChangeEvent", false);
			}
			if (D_SHARP_ITEM_DLISTBOXCHANGEEVENT == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end dListBoxChangeEvent of the class D#Item.");
			}
			D_SHARP_IMAGE_FILENAME = raapi.findAttribute(D_SHARP_IMAGE, "fileName");
			if ((D_SHARP_IMAGE_FILENAME == 0) && insertMetamodel)
				D_SHARP_IMAGE_FILENAME = raapi.createAttribute(D_SHARP_IMAGE, "fileName", raapi.findPrimitiveDataType("String"));
			if (D_SHARP_IMAGE_FILENAME == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute fileName of the class D#Image.");
			}
			D_SHARP_IMAGE_EDITABLE = raapi.findAttribute(D_SHARP_IMAGE, "editable");
			if ((D_SHARP_IMAGE_EDITABLE == 0) && insertMetamodel)
				D_SHARP_IMAGE_EDITABLE = raapi.createAttribute(D_SHARP_IMAGE, "editable", raapi.findPrimitiveDataType("Boolean"));
			if (D_SHARP_IMAGE_EDITABLE == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute editable of the class D#Image.");
			}
			D_SHARP_TEXTBOX_TEXT = raapi.findAttribute(D_SHARP_TEXTBOX, "text");
			if ((D_SHARP_TEXTBOX_TEXT == 0) && insertMetamodel)
				D_SHARP_TEXTBOX_TEXT = raapi.createAttribute(D_SHARP_TEXTBOX, "text", raapi.findPrimitiveDataType("String"));
			if (D_SHARP_TEXTBOX_TEXT == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute text of the class D#TextBox.");
			}
			D_SHARP_TEXTBOX_MULTILINE = raapi.findAttribute(D_SHARP_TEXTBOX, "multiLine");
			if ((D_SHARP_TEXTBOX_MULTILINE == 0) && insertMetamodel)
				D_SHARP_TEXTBOX_MULTILINE = raapi.createAttribute(D_SHARP_TEXTBOX, "multiLine", raapi.findPrimitiveDataType("Boolean"));
			if (D_SHARP_TEXTBOX_MULTILINE == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute multiLine of the class D#TextBox.");
			}
			D_SHARP_TEXTBOX_EDITABLE = raapi.findAttribute(D_SHARP_TEXTBOX, "editable");
			if ((D_SHARP_TEXTBOX_EDITABLE == 0) && insertMetamodel)
				D_SHARP_TEXTBOX_EDITABLE = raapi.createAttribute(D_SHARP_TEXTBOX, "editable", raapi.findPrimitiveDataType("Boolean"));
			if (D_SHARP_TEXTBOX_EDITABLE == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute editable of the class D#TextBox.");
			}
			D_SHARP_TREE_DRAGGABLENODES = raapi.findAttribute(D_SHARP_TREE, "draggableNodes");
			if ((D_SHARP_TREE_DRAGGABLENODES == 0) && insertMetamodel)
				D_SHARP_TREE_DRAGGABLENODES = raapi.createAttribute(D_SHARP_TREE, "draggableNodes", raapi.findPrimitiveDataType("Boolean"));
			if (D_SHARP_TREE_DRAGGABLENODES == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute draggableNodes of the class D#Tree.");
			}
			D_SHARP_TREE_TREENODE = raapi.findAssociationEnd(D_SHARP_TREE, "treeNode");
			if ((D_SHARP_TREE_TREENODE == 0) && insertMetamodel) {
				D_SHARP_TREE_TREENODE = raapi.createAssociation(D_SHARP_TREE, D_SHARP_TREENODE, "tree", "treeNode", false);
			}
			if (D_SHARP_TREE_TREENODE == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end treeNode of the class D#Tree.");
			}
			D_SHARP_TREE_SELECTED = raapi.findAssociationEnd(D_SHARP_TREE, "selected");
			if ((D_SHARP_TREE_SELECTED == 0) && insertMetamodel) {
				D_SHARP_TREE_SELECTED = raapi.createAssociation(D_SHARP_TREE, D_SHARP_TREENODE, "parentTree", "selected", false);
			}
			if (D_SHARP_TREE_SELECTED == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end selected of the class D#Tree.");
			}
			D_SHARP_MULTILINETEXTBOX_TEXTLINE = raapi.findAssociationEnd(D_SHARP_MULTILINETEXTBOX, "textLine");
			if ((D_SHARP_MULTILINETEXTBOX_TEXTLINE == 0) && insertMetamodel) {
				D_SHARP_MULTILINETEXTBOX_TEXTLINE = raapi.createAssociation(D_SHARP_MULTILINETEXTBOX, D_SHARP_TEXTLINE, "multiLineTextBox", "textLine", false);
			}
			if (D_SHARP_MULTILINETEXTBOX_TEXTLINE == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end textLine of the class D#MultiLineTextBox.");
			}
			D_SHARP_MULTILINETEXTBOX_CURRENT = raapi.findAssociationEnd(D_SHARP_MULTILINETEXTBOX, "current");
			if ((D_SHARP_MULTILINETEXTBOX_CURRENT == 0) && insertMetamodel) {
				D_SHARP_MULTILINETEXTBOX_CURRENT = raapi.createAssociation(D_SHARP_MULTILINETEXTBOX, D_SHARP_TEXTLINE, "parentMultiLineTextBox", "current", false);
			}
			if (D_SHARP_MULTILINETEXTBOX_CURRENT == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end current of the class D#MultiLineTextBox.");
			}
			D_SHARP_RICHTEXTAREA_FILENAME = raapi.findAttribute(D_SHARP_RICHTEXTAREA, "fileName");
			if ((D_SHARP_RICHTEXTAREA_FILENAME == 0) && insertMetamodel)
				D_SHARP_RICHTEXTAREA_FILENAME = raapi.createAttribute(D_SHARP_RICHTEXTAREA, "fileName", raapi.findPrimitiveDataType("String"));
			if (D_SHARP_RICHTEXTAREA_FILENAME == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute fileName of the class D#RichTextArea.");
			}
			D_SHARP_RICHTEXTAREA_ENCODEDCONTENT = raapi.findAttribute(D_SHARP_RICHTEXTAREA, "encodedContent");
			if ((D_SHARP_RICHTEXTAREA_ENCODEDCONTENT == 0) && insertMetamodel)
				D_SHARP_RICHTEXTAREA_ENCODEDCONTENT = raapi.createAttribute(D_SHARP_RICHTEXTAREA, "encodedContent", raapi.findPrimitiveDataType("String"));
			if (D_SHARP_RICHTEXTAREA_ENCODEDCONTENT == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute encodedContent of the class D#RichTextArea.");
			}
			D_SHARP_PROGRESSBAR_POSITION = raapi.findAttribute(D_SHARP_PROGRESSBAR, "position");
			if ((D_SHARP_PROGRESSBAR_POSITION == 0) && insertMetamodel)
				D_SHARP_PROGRESSBAR_POSITION = raapi.createAttribute(D_SHARP_PROGRESSBAR, "position", raapi.findPrimitiveDataType("Integer"));
			if (D_SHARP_PROGRESSBAR_POSITION == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute position of the class D#ProgressBar.");
			}
			D_SHARP_PROGRESSBAR_MINIMUMVALUE = raapi.findAttribute(D_SHARP_PROGRESSBAR, "minimumValue");
			if ((D_SHARP_PROGRESSBAR_MINIMUMVALUE == 0) && insertMetamodel)
				D_SHARP_PROGRESSBAR_MINIMUMVALUE = raapi.createAttribute(D_SHARP_PROGRESSBAR, "minimumValue", raapi.findPrimitiveDataType("Integer"));
			if (D_SHARP_PROGRESSBAR_MINIMUMVALUE == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute minimumValue of the class D#ProgressBar.");
			}
			D_SHARP_PROGRESSBAR_MAXIMUMVALUE = raapi.findAttribute(D_SHARP_PROGRESSBAR, "maximumValue");
			if ((D_SHARP_PROGRESSBAR_MAXIMUMVALUE == 0) && insertMetamodel)
				D_SHARP_PROGRESSBAR_MAXIMUMVALUE = raapi.createAttribute(D_SHARP_PROGRESSBAR, "maximumValue", raapi.findPrimitiveDataType("Integer"));
			if (D_SHARP_PROGRESSBAR_MAXIMUMVALUE == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute maximumValue of the class D#ProgressBar.");
			}
			D_SHARP_IMAGEBUTTON_FILENAME = raapi.findAttribute(D_SHARP_IMAGEBUTTON, "fileName");
			if ((D_SHARP_IMAGEBUTTON_FILENAME == 0) && insertMetamodel)
				D_SHARP_IMAGEBUTTON_FILENAME = raapi.createAttribute(D_SHARP_IMAGEBUTTON, "fileName", raapi.findPrimitiveDataType("String"));
			if (D_SHARP_IMAGEBUTTON_FILENAME == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute fileName of the class D#ImageButton.");
			}
			D_SHARP_IMAGEBUTTON_CARDINALPOINT = raapi.findAttribute(D_SHARP_IMAGEBUTTON, "cardinalPoint");
			if ((D_SHARP_IMAGEBUTTON_CARDINALPOINT == 0) && insertMetamodel)
				D_SHARP_IMAGEBUTTON_CARDINALPOINT = raapi.createAttribute(D_SHARP_IMAGEBUTTON, "cardinalPoint", raapi.findPrimitiveDataType("String"));
			if (D_SHARP_IMAGEBUTTON_CARDINALPOINT == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute cardinalPoint of the class D#ImageButton.");
			}
			D_SHARP_VTABLECOLUMNTYPE_CAPTION = raapi.findAttribute(D_SHARP_VTABLECOLUMNTYPE, "caption");
			if ((D_SHARP_VTABLECOLUMNTYPE_CAPTION == 0) && insertMetamodel)
				D_SHARP_VTABLECOLUMNTYPE_CAPTION = raapi.createAttribute(D_SHARP_VTABLECOLUMNTYPE, "caption", raapi.findPrimitiveDataType("String"));
			if (D_SHARP_VTABLECOLUMNTYPE_CAPTION == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute caption of the class D#VTableColumnType.");
			}
			D_SHARP_VTABLECOLUMNTYPE_DEFAULTVALUE = raapi.findAttribute(D_SHARP_VTABLECOLUMNTYPE, "defaultValue");
			if ((D_SHARP_VTABLECOLUMNTYPE_DEFAULTVALUE == 0) && insertMetamodel)
				D_SHARP_VTABLECOLUMNTYPE_DEFAULTVALUE = raapi.createAttribute(D_SHARP_VTABLECOLUMNTYPE, "defaultValue", raapi.findPrimitiveDataType("String"));
			if (D_SHARP_VTABLECOLUMNTYPE_DEFAULTVALUE == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute defaultValue of the class D#VTableColumnType.");
			}
			D_SHARP_VTABLECOLUMNTYPE_EDITABLE = raapi.findAttribute(D_SHARP_VTABLECOLUMNTYPE, "editable");
			if ((D_SHARP_VTABLECOLUMNTYPE_EDITABLE == 0) && insertMetamodel)
				D_SHARP_VTABLECOLUMNTYPE_EDITABLE = raapi.createAttribute(D_SHARP_VTABLECOLUMNTYPE, "editable", raapi.findPrimitiveDataType("Boolean"));
			if (D_SHARP_VTABLECOLUMNTYPE_EDITABLE == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute editable of the class D#VTableColumnType.");
			}
			D_SHARP_VTABLECOLUMNTYPE_WIDTH = raapi.findAttribute(D_SHARP_VTABLECOLUMNTYPE, "width");
			if ((D_SHARP_VTABLECOLUMNTYPE_WIDTH == 0) && insertMetamodel)
				D_SHARP_VTABLECOLUMNTYPE_WIDTH = raapi.createAttribute(D_SHARP_VTABLECOLUMNTYPE, "width", raapi.findPrimitiveDataType("Integer"));
			if (D_SHARP_VTABLECOLUMNTYPE_WIDTH == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute width of the class D#VTableColumnType.");
			}
			D_SHARP_VTABLECOLUMNTYPE_PREFERREDRELATIVEWIDTH = raapi.findAttribute(D_SHARP_VTABLECOLUMNTYPE, "preferredRelativeWidth");
			if ((D_SHARP_VTABLECOLUMNTYPE_PREFERREDRELATIVEWIDTH == 0) && insertMetamodel)
				D_SHARP_VTABLECOLUMNTYPE_PREFERREDRELATIVEWIDTH = raapi.createAttribute(D_SHARP_VTABLECOLUMNTYPE, "preferredRelativeWidth", raapi.findPrimitiveDataType("EDouble"));
			if (D_SHARP_VTABLECOLUMNTYPE_PREFERREDRELATIVEWIDTH == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute preferredRelativeWidth of the class D#VTableColumnType.");
			}
			D_SHARP_VTABLECOLUMNTYPE_HORIZONTALALIGNMENT = raapi.findAttribute(D_SHARP_VTABLECOLUMNTYPE, "horizontalAlignment");
			if ((D_SHARP_VTABLECOLUMNTYPE_HORIZONTALALIGNMENT == 0) && insertMetamodel)
				D_SHARP_VTABLECOLUMNTYPE_HORIZONTALALIGNMENT = raapi.createAttribute(D_SHARP_VTABLECOLUMNTYPE, "horizontalAlignment", raapi.findPrimitiveDataType("Integer"));
			if (D_SHARP_VTABLECOLUMNTYPE_HORIZONTALALIGNMENT == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute horizontalAlignment of the class D#VTableColumnType.");
			}
			D_SHARP_VTABLECOLUMNTYPE_VTABLETYPE = raapi.findAssociationEnd(D_SHARP_VTABLECOLUMNTYPE, "vTableType");
			if ((D_SHARP_VTABLECOLUMNTYPE_VTABLETYPE == 0) && insertMetamodel) {
				D_SHARP_VTABLECOLUMNTYPE_VTABLETYPE = raapi.createAssociation(D_SHARP_VTABLECOLUMNTYPE, D_SHARP_VTABLETYPE, "columnType", "vTableType", false);
			}
			if (D_SHARP_VTABLECOLUMNTYPE_VTABLETYPE == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end vTableType of the class D#VTableColumnType.");
			}
			D_SHARP_VTABLECOLUMNTYPE_VTABLETYPEOWNER = raapi.findAssociationEnd(D_SHARP_VTABLECOLUMNTYPE, "vTableTypeOwner");
			if ((D_SHARP_VTABLECOLUMNTYPE_VTABLETYPEOWNER == 0) && insertMetamodel) {
				D_SHARP_VTABLECOLUMNTYPE_VTABLETYPEOWNER = raapi.createAssociation(D_SHARP_VTABLECOLUMNTYPE, D_SHARP_VTABLETYPE, "column", "vTableTypeOwner", false);
			}
			if (D_SHARP_VTABLECOLUMNTYPE_VTABLETYPEOWNER == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end vTableTypeOwner of the class D#VTableColumnType.");
			}
			D_SHARP_VTABLECOLUMNTYPE_VTABLECELL = raapi.findAssociationEnd(D_SHARP_VTABLECOLUMNTYPE, "vTableCell");
			if ((D_SHARP_VTABLECOLUMNTYPE_VTABLECELL == 0) && insertMetamodel) {
				D_SHARP_VTABLECOLUMNTYPE_VTABLECELL = raapi.createAssociation(D_SHARP_VTABLECOLUMNTYPE, D_SHARP_VTABLECELL, "vTableColumnType", "vTableCell", false);
			}
			if (D_SHARP_VTABLECOLUMNTYPE_VTABLECELL == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end vTableCell of the class D#VTableColumnType.");
			}
			D_SHARP_VTABLECOLUMNTYPE_DEFAULTCOMPONENTTYPE = raapi.findAssociationEnd(D_SHARP_VTABLECOLUMNTYPE, "defaultComponentType");
			if ((D_SHARP_VTABLECOLUMNTYPE_DEFAULTCOMPONENTTYPE == 0) && insertMetamodel) {
				D_SHARP_VTABLECOLUMNTYPE_DEFAULTCOMPONENTTYPE = raapi.createAssociation(D_SHARP_VTABLECOLUMNTYPE, D_SHARP_TABLECOMPONENT, "vTableColumnType", "defaultComponentType", false);
			}
			if (D_SHARP_VTABLECOLUMNTYPE_DEFAULTCOMPONENTTYPE == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end defaultComponentType of the class D#VTableColumnType.");
			}
			D_SHARP_VTABLECOLUMNTYPE_DEFAULTCOMPONENT = raapi.findAssociationEnd(D_SHARP_VTABLECOLUMNTYPE, "defaultComponent");
			if ((D_SHARP_VTABLECOLUMNTYPE_DEFAULTCOMPONENT == 0) && insertMetamodel) {
				D_SHARP_VTABLECOLUMNTYPE_DEFAULTCOMPONENT = raapi.createAssociation(D_SHARP_VTABLECOLUMNTYPE, D_SHARP_TABLECOMPONENT, "vTableColumnTypeOwner", "defaultComponent", false);
			}
			if (D_SHARP_VTABLECOLUMNTYPE_DEFAULTCOMPONENT == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end defaultComponent of the class D#VTableColumnType.");
			}
			D_SHARP_VTABLECELL_VALUE = raapi.findAttribute(D_SHARP_VTABLECELL, "value");
			if ((D_SHARP_VTABLECELL_VALUE == 0) && insertMetamodel)
				D_SHARP_VTABLECELL_VALUE = raapi.createAttribute(D_SHARP_VTABLECELL, "value", raapi.findPrimitiveDataType("String"));
			if (D_SHARP_VTABLECELL_VALUE == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute value of the class D#VTableCell.");
			}
			D_SHARP_VTABLECELL_VTABLEROW = raapi.findAssociationEnd(D_SHARP_VTABLECELL, "vTableRow");
			if ((D_SHARP_VTABLECELL_VTABLEROW == 0) && insertMetamodel) {
				D_SHARP_VTABLECELL_VTABLEROW = raapi.createAssociation(D_SHARP_VTABLECELL, D_SHARP_VTABLEROW, "vTableCell", "vTableRow", false);
			}
			if (D_SHARP_VTABLECELL_VTABLEROW == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end vTableRow of the class D#VTableCell.");
			}
			D_SHARP_VTABLECELL_PARENTROW = raapi.findAssociationEnd(D_SHARP_VTABLECELL, "parentRow");
			if ((D_SHARP_VTABLECELL_PARENTROW == 0) && insertMetamodel) {
				D_SHARP_VTABLECELL_PARENTROW = raapi.createAssociation(D_SHARP_VTABLECELL, D_SHARP_VTABLEROW, "activeCell", "parentRow", false);
			}
			if (D_SHARP_VTABLECELL_PARENTROW == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end parentRow of the class D#VTableCell.");
			}
			D_SHARP_VTABLECELL_SELECTEDITEM = raapi.findAssociationEnd(D_SHARP_VTABLECELL, "selectedItem");
			if ((D_SHARP_VTABLECELL_SELECTEDITEM == 0) && insertMetamodel) {
				D_SHARP_VTABLECELL_SELECTEDITEM = raapi.createAssociation(D_SHARP_VTABLECELL, D_SHARP_ITEM, "vTableCell", "selectedItem", false);
			}
			if (D_SHARP_VTABLECELL_SELECTEDITEM == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end selectedItem of the class D#VTableCell.");
			}
			D_SHARP_VTABLECELL_VTABLECOLUMNTYPE = raapi.findAssociationEnd(D_SHARP_VTABLECELL, "vTableColumnType");
			if ((D_SHARP_VTABLECELL_VTABLECOLUMNTYPE == 0) && insertMetamodel) {
				D_SHARP_VTABLECELL_VTABLECOLUMNTYPE = raapi.createAssociation(D_SHARP_VTABLECELL, D_SHARP_VTABLECOLUMNTYPE, "vTableCell", "vTableColumnType", false);
			}
			if (D_SHARP_VTABLECELL_VTABLECOLUMNTYPE == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end vTableColumnType of the class D#VTableCell.");
			}
			D_SHARP_VTABLECELL_COMPONENTTYPE = raapi.findAssociationEnd(D_SHARP_VTABLECELL, "componentType");
			if ((D_SHARP_VTABLECELL_COMPONENTTYPE == 0) && insertMetamodel) {
				D_SHARP_VTABLECELL_COMPONENTTYPE = raapi.createAssociation(D_SHARP_VTABLECELL, D_SHARP_TABLECOMPONENT, "vTableCell", "componentType", false);
			}
			if (D_SHARP_VTABLECELL_COMPONENTTYPE == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end componentType of the class D#VTableCell.");
			}
			D_SHARP_VTABLECELL_COMPONENT = raapi.findAssociationEnd(D_SHARP_VTABLECELL, "component");
			if ((D_SHARP_VTABLECELL_COMPONENT == 0) && insertMetamodel) {
				D_SHARP_VTABLECELL_COMPONENT = raapi.createAssociation(D_SHARP_VTABLECELL, D_SHARP_TABLECOMPONENT, "vTableCellOwner", "component", false);
			}
			if (D_SHARP_VTABLECELL_COMPONENT == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end component of the class D#VTableCell.");
			}
			D_SHARP_VTABLECELL_EVENT = raapi.findAssociationEnd(D_SHARP_VTABLECELL, "event");
			if ((D_SHARP_VTABLECELL_EVENT == 0) && insertMetamodel) {
				D_SHARP_VTABLECELL_EVENT = raapi.createAssociation(D_SHARP_VTABLECELL, D_SHARP_EVENT, "vTableCell", "event", false);
			}
			if (D_SHARP_VTABLECELL_EVENT == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end event of the class D#VTableCell.");
			}
			D_SHARP_EVENTHANDLER_EVENTNAME = raapi.findAttribute(D_SHARP_EVENTHANDLER, "eventName");
			if ((D_SHARP_EVENTHANDLER_EVENTNAME == 0) && insertMetamodel)
				D_SHARP_EVENTHANDLER_EVENTNAME = raapi.createAttribute(D_SHARP_EVENTHANDLER, "eventName", raapi.findPrimitiveDataType("String"));
			if (D_SHARP_EVENTHANDLER_EVENTNAME == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute eventName of the class D#EventHandler.");
			}
			D_SHARP_EVENTHANDLER_TRANSFORMATIONNAME = raapi.findAttribute(D_SHARP_EVENTHANDLER, "transformationName");
			if ((D_SHARP_EVENTHANDLER_TRANSFORMATIONNAME == 0) && insertMetamodel)
				D_SHARP_EVENTHANDLER_TRANSFORMATIONNAME = raapi.createAttribute(D_SHARP_EVENTHANDLER, "transformationName", raapi.findPrimitiveDataType("String"));
			if (D_SHARP_EVENTHANDLER_TRANSFORMATIONNAME == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute transformationName of the class D#EventHandler.");
			}
			D_SHARP_EVENTHANDLER_PROCEDURENAME = raapi.findAttribute(D_SHARP_EVENTHANDLER, "procedureName");
			if ((D_SHARP_EVENTHANDLER_PROCEDURENAME == 0) && insertMetamodel)
				D_SHARP_EVENTHANDLER_PROCEDURENAME = raapi.createAttribute(D_SHARP_EVENTHANDLER, "procedureName", raapi.findPrimitiveDataType("String"));
			if (D_SHARP_EVENTHANDLER_PROCEDURENAME == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute procedureName of the class D#EventHandler.");
			}
			D_SHARP_EVENTHANDLER_EVENTSOURCE = raapi.findAssociationEnd(D_SHARP_EVENTHANDLER, "eventSource");
			if ((D_SHARP_EVENTHANDLER_EVENTSOURCE == 0) && insertMetamodel) {
				D_SHARP_EVENTHANDLER_EVENTSOURCE = raapi.createAssociation(D_SHARP_EVENTHANDLER, D_SHARP_EVENTSOURCE, "eventHandler", "eventSource", false);
			}
			if (D_SHARP_EVENTHANDLER_EVENTSOURCE == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end eventSource of the class D#EventHandler.");
			}
			D_SHARP_COMMAND_INFO = raapi.findAttribute(D_SHARP_COMMAND, "info");
			if ((D_SHARP_COMMAND_INFO == 0) && insertMetamodel)
				D_SHARP_COMMAND_INFO = raapi.createAttribute(D_SHARP_COMMAND, "info", raapi.findPrimitiveDataType("String"));
			if (D_SHARP_COMMAND_INFO == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute info of the class D#Command.");
			}
			D_SHARP_COMMAND_RECEIVER = raapi.findAssociationEnd(D_SHARP_COMMAND, "receiver");
			if ((D_SHARP_COMMAND_RECEIVER == 0) && insertMetamodel) {
				D_SHARP_COMMAND_RECEIVER = raapi.createAssociation(D_SHARP_COMMAND, D_SHARP_COMPONENT, "command", "receiver", false);
			}
			if (D_SHARP_COMMAND_RECEIVER == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end receiver of the class D#Command.");
			}
			D_SHARP_EVENT_EVENTNAME = raapi.findAttribute(D_SHARP_EVENT, "eventName");
			if ((D_SHARP_EVENT_EVENTNAME == 0) && insertMetamodel)
				D_SHARP_EVENT_EVENTNAME = raapi.createAttribute(D_SHARP_EVENT, "eventName", raapi.findPrimitiveDataType("String"));
			if (D_SHARP_EVENT_EVENTNAME == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute eventName of the class D#Event.");
			}
			D_SHARP_EVENT_INFO = raapi.findAttribute(D_SHARP_EVENT, "info");
			if ((D_SHARP_EVENT_INFO == 0) && insertMetamodel)
				D_SHARP_EVENT_INFO = raapi.createAttribute(D_SHARP_EVENT, "info", raapi.findPrimitiveDataType("String"));
			if (D_SHARP_EVENT_INFO == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute info of the class D#Event.");
			}
			D_SHARP_EVENT_FORM = raapi.findAssociationEnd(D_SHARP_EVENT, "form");
			if ((D_SHARP_EVENT_FORM == 0) && insertMetamodel) {
				D_SHARP_EVENT_FORM = raapi.createAssociation(D_SHARP_EVENT, D_SHARP_FORM, "ownedEvent", "form", false);
			}
			if (D_SHARP_EVENT_FORM == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end form of the class D#Event.");
			}
			D_SHARP_EVENT_SOURCE = raapi.findAssociationEnd(D_SHARP_EVENT, "source");
			if ((D_SHARP_EVENT_SOURCE == 0) && insertMetamodel) {
				D_SHARP_EVENT_SOURCE = raapi.createAssociation(D_SHARP_EVENT, D_SHARP_EVENTSOURCE, "event", "source", false);
			}
			if (D_SHARP_EVENT_SOURCE == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end source of the class D#Event.");
			}
			D_SHARP_EVENT_VTABLECELL = raapi.findAssociationEnd(D_SHARP_EVENT, "vTableCell");
			if ((D_SHARP_EVENT_VTABLECELL == 0) && insertMetamodel) {
				D_SHARP_EVENT_VTABLECELL = raapi.createAssociation(D_SHARP_EVENT, D_SHARP_VTABLECELL, "event", "vTableCell", false);
			}
			if (D_SHARP_EVENT_VTABLECELL == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end vTableCell of the class D#Event.");
			}
			D_SHARP_ROWMOVEDEVENT_ROW = raapi.findAssociationEnd(D_SHARP_ROWMOVEDEVENT, "row");
			if ((D_SHARP_ROWMOVEDEVENT_ROW == 0) && insertMetamodel) {
				D_SHARP_ROWMOVEDEVENT_ROW = raapi.createAssociation(D_SHARP_ROWMOVEDEVENT, D_SHARP_VTABLEROW, "rowMovedEvent", "row", false);
			}
			if (D_SHARP_ROWMOVEDEVENT_ROW == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end row of the class D#RowMovedEvent.");
			}
			D_SHARP_ROWMOVEDEVENT_BEFORE = raapi.findAssociationEnd(D_SHARP_ROWMOVEDEVENT, "before");
			if ((D_SHARP_ROWMOVEDEVENT_BEFORE == 0) && insertMetamodel) {
				D_SHARP_ROWMOVEDEVENT_BEFORE = raapi.createAssociation(D_SHARP_ROWMOVEDEVENT, D_SHARP_VTABLEROW, "beforeRowMovedEvent", "before", false);
			}
			if (D_SHARP_ROWMOVEDEVENT_BEFORE == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end before of the class D#RowMovedEvent.");
			}
			D_SHARP_ROWMOVEDEVENT_AFTER = raapi.findAssociationEnd(D_SHARP_ROWMOVEDEVENT, "after");
			if ((D_SHARP_ROWMOVEDEVENT_AFTER == 0) && insertMetamodel) {
				D_SHARP_ROWMOVEDEVENT_AFTER = raapi.createAssociation(D_SHARP_ROWMOVEDEVENT, D_SHARP_VTABLEROW, "afterRowMovedEvent", "after", false);
			}
			if (D_SHARP_ROWMOVEDEVENT_AFTER == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end after of the class D#RowMovedEvent.");
			}
			D_SHARP_COLUMNMOVEDEVENT_COLUMN = raapi.findAssociationEnd(D_SHARP_COLUMNMOVEDEVENT, "column");
			if ((D_SHARP_COLUMNMOVEDEVENT_COLUMN == 0) && insertMetamodel) {
				D_SHARP_COLUMNMOVEDEVENT_COLUMN = raapi.createAssociation(D_SHARP_COLUMNMOVEDEVENT, D_SHARP_VTABLETYPE, "columnMovedEvent", "column", false);
			}
			if (D_SHARP_COLUMNMOVEDEVENT_COLUMN == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end column of the class D#ColumnMovedEvent.");
			}
			D_SHARP_COLUMNMOVEDEVENT_BEFORE = raapi.findAssociationEnd(D_SHARP_COLUMNMOVEDEVENT, "before");
			if ((D_SHARP_COLUMNMOVEDEVENT_BEFORE == 0) && insertMetamodel) {
				D_SHARP_COLUMNMOVEDEVENT_BEFORE = raapi.createAssociation(D_SHARP_COLUMNMOVEDEVENT, D_SHARP_VTABLETYPE, "beforeColumnMovedEvent", "before", false);
			}
			if (D_SHARP_COLUMNMOVEDEVENT_BEFORE == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end before of the class D#ColumnMovedEvent.");
			}
			D_SHARP_COLUMNMOVEDEVENT_AFTER = raapi.findAssociationEnd(D_SHARP_COLUMNMOVEDEVENT, "after");
			if ((D_SHARP_COLUMNMOVEDEVENT_AFTER == 0) && insertMetamodel) {
				D_SHARP_COLUMNMOVEDEVENT_AFTER = raapi.createAssociation(D_SHARP_COLUMNMOVEDEVENT, D_SHARP_VTABLETYPE, "afterColumnMovedEvent", "after", false);
			}
			if (D_SHARP_COLUMNMOVEDEVENT_AFTER == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end after of the class D#ColumnMovedEvent.");
			}
			D_SHARP_MULTILINETEXTBOXCHANGEEVENT_INSERTED = raapi.findAssociationEnd(D_SHARP_MULTILINETEXTBOXCHANGEEVENT, "inserted");
			if ((D_SHARP_MULTILINETEXTBOXCHANGEEVENT_INSERTED == 0) && insertMetamodel) {
				D_SHARP_MULTILINETEXTBOXCHANGEEVENT_INSERTED = raapi.createAssociation(D_SHARP_MULTILINETEXTBOXCHANGEEVENT, D_SHARP_TEXTLINE, "iMultiLineTextBoxChangeEvent", "inserted", false);
			}
			if (D_SHARP_MULTILINETEXTBOXCHANGEEVENT_INSERTED == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end inserted of the class D#MultiLineTextBoxChangeEvent.");
			}
			D_SHARP_MULTILINETEXTBOXCHANGEEVENT_DELETED = raapi.findAssociationEnd(D_SHARP_MULTILINETEXTBOXCHANGEEVENT, "deleted");
			if ((D_SHARP_MULTILINETEXTBOXCHANGEEVENT_DELETED == 0) && insertMetamodel) {
				D_SHARP_MULTILINETEXTBOXCHANGEEVENT_DELETED = raapi.createAssociation(D_SHARP_MULTILINETEXTBOXCHANGEEVENT, D_SHARP_TEXTLINE, "dMultiLineTextBoxChangeEvent", "deleted", false);
			}
			if (D_SHARP_MULTILINETEXTBOXCHANGEEVENT_DELETED == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end deleted of the class D#MultiLineTextBoxChangeEvent.");
			}
			D_SHARP_MULTILINETEXTBOXCHANGEEVENT_EDITED = raapi.findAssociationEnd(D_SHARP_MULTILINETEXTBOXCHANGEEVENT, "edited");
			if ((D_SHARP_MULTILINETEXTBOXCHANGEEVENT_EDITED == 0) && insertMetamodel) {
				D_SHARP_MULTILINETEXTBOXCHANGEEVENT_EDITED = raapi.createAssociation(D_SHARP_MULTILINETEXTBOXCHANGEEVENT, D_SHARP_TEXTLINE, "eMultiLineTextBoxChangeEvent", "edited", false);
			}
			if (D_SHARP_MULTILINETEXTBOXCHANGEEVENT_EDITED == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end edited of the class D#MultiLineTextBoxChangeEvent.");
			}
			D_SHARP_TEXTLINE_TEXT = raapi.findAttribute(D_SHARP_TEXTLINE, "text");
			if ((D_SHARP_TEXTLINE_TEXT == 0) && insertMetamodel)
				D_SHARP_TEXTLINE_TEXT = raapi.createAttribute(D_SHARP_TEXTLINE, "text", raapi.findPrimitiveDataType("String"));
			if (D_SHARP_TEXTLINE_TEXT == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute text of the class D#TextLine.");
			}
			D_SHARP_TEXTLINE_INSERTED = raapi.findAttribute(D_SHARP_TEXTLINE, "inserted");
			if ((D_SHARP_TEXTLINE_INSERTED == 0) && insertMetamodel)
				D_SHARP_TEXTLINE_INSERTED = raapi.createAttribute(D_SHARP_TEXTLINE, "inserted", raapi.findPrimitiveDataType("Boolean"));
			if (D_SHARP_TEXTLINE_INSERTED == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute inserted of the class D#TextLine.");
			}
			D_SHARP_TEXTLINE_DELETED = raapi.findAttribute(D_SHARP_TEXTLINE, "deleted");
			if ((D_SHARP_TEXTLINE_DELETED == 0) && insertMetamodel)
				D_SHARP_TEXTLINE_DELETED = raapi.createAttribute(D_SHARP_TEXTLINE, "deleted", raapi.findPrimitiveDataType("Boolean"));
			if (D_SHARP_TEXTLINE_DELETED == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute deleted of the class D#TextLine.");
			}
			D_SHARP_TEXTLINE_EDITED = raapi.findAttribute(D_SHARP_TEXTLINE, "edited");
			if ((D_SHARP_TEXTLINE_EDITED == 0) && insertMetamodel)
				D_SHARP_TEXTLINE_EDITED = raapi.createAttribute(D_SHARP_TEXTLINE, "edited", raapi.findPrimitiveDataType("Boolean"));
			if (D_SHARP_TEXTLINE_EDITED == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute edited of the class D#TextLine.");
			}
			D_SHARP_TEXTLINE_MULTILINETEXTBOX = raapi.findAssociationEnd(D_SHARP_TEXTLINE, "multiLineTextBox");
			if ((D_SHARP_TEXTLINE_MULTILINETEXTBOX == 0) && insertMetamodel) {
				D_SHARP_TEXTLINE_MULTILINETEXTBOX = raapi.createAssociation(D_SHARP_TEXTLINE, D_SHARP_MULTILINETEXTBOX, "textLine", "multiLineTextBox", false);
			}
			if (D_SHARP_TEXTLINE_MULTILINETEXTBOX == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end multiLineTextBox of the class D#TextLine.");
			}
			D_SHARP_TEXTLINE_PARENTMULTILINETEXTBOX = raapi.findAssociationEnd(D_SHARP_TEXTLINE, "parentMultiLineTextBox");
			if ((D_SHARP_TEXTLINE_PARENTMULTILINETEXTBOX == 0) && insertMetamodel) {
				D_SHARP_TEXTLINE_PARENTMULTILINETEXTBOX = raapi.createAssociation(D_SHARP_TEXTLINE, D_SHARP_MULTILINETEXTBOX, "current", "parentMultiLineTextBox", false);
			}
			if (D_SHARP_TEXTLINE_PARENTMULTILINETEXTBOX == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end parentMultiLineTextBox of the class D#TextLine.");
			}
			D_SHARP_TEXTLINE_IMULTILINETEXTBOXCHANGEEVENT = raapi.findAssociationEnd(D_SHARP_TEXTLINE, "iMultiLineTextBoxChangeEvent");
			if ((D_SHARP_TEXTLINE_IMULTILINETEXTBOXCHANGEEVENT == 0) && insertMetamodel) {
				D_SHARP_TEXTLINE_IMULTILINETEXTBOXCHANGEEVENT = raapi.createAssociation(D_SHARP_TEXTLINE, D_SHARP_MULTILINETEXTBOXCHANGEEVENT, "inserted", "iMultiLineTextBoxChangeEvent", false);
			}
			if (D_SHARP_TEXTLINE_IMULTILINETEXTBOXCHANGEEVENT == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end iMultiLineTextBoxChangeEvent of the class D#TextLine.");
			}
			D_SHARP_TEXTLINE_DMULTILINETEXTBOXCHANGEEVENT = raapi.findAssociationEnd(D_SHARP_TEXTLINE, "dMultiLineTextBoxChangeEvent");
			if ((D_SHARP_TEXTLINE_DMULTILINETEXTBOXCHANGEEVENT == 0) && insertMetamodel) {
				D_SHARP_TEXTLINE_DMULTILINETEXTBOXCHANGEEVENT = raapi.createAssociation(D_SHARP_TEXTLINE, D_SHARP_MULTILINETEXTBOXCHANGEEVENT, "deleted", "dMultiLineTextBoxChangeEvent", false);
			}
			if (D_SHARP_TEXTLINE_DMULTILINETEXTBOXCHANGEEVENT == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end dMultiLineTextBoxChangeEvent of the class D#TextLine.");
			}
			D_SHARP_TEXTLINE_EMULTILINETEXTBOXCHANGEEVENT = raapi.findAssociationEnd(D_SHARP_TEXTLINE, "eMultiLineTextBoxChangeEvent");
			if ((D_SHARP_TEXTLINE_EMULTILINETEXTBOXCHANGEEVENT == 0) && insertMetamodel) {
				D_SHARP_TEXTLINE_EMULTILINETEXTBOXCHANGEEVENT = raapi.createAssociation(D_SHARP_TEXTLINE, D_SHARP_MULTILINETEXTBOXCHANGEEVENT, "edited", "eMultiLineTextBoxChangeEvent", false);
			}
			if (D_SHARP_TEXTLINE_EMULTILINETEXTBOXCHANGEEVENT == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end eMultiLineTextBoxChangeEvent of the class D#TextLine.");
			}
			D_SHARP_TREENODESELECTEVENT_TREENODE = raapi.findAssociationEnd(D_SHARP_TREENODESELECTEVENT, "treeNode");
			if ((D_SHARP_TREENODESELECTEVENT_TREENODE == 0) && insertMetamodel) {
				D_SHARP_TREENODESELECTEVENT_TREENODE = raapi.createAssociation(D_SHARP_TREENODESELECTEVENT, D_SHARP_TREENODE, "treeNodeSelectEvent", "treeNode", false);
			}
			if (D_SHARP_TREENODESELECTEVENT_TREENODE == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end treeNode of the class D#TreeNodeSelectEvent.");
			}
			D_SHARP_TREENODESELECTEVENT_PREVIOUS = raapi.findAssociationEnd(D_SHARP_TREENODESELECTEVENT, "previous");
			if ((D_SHARP_TREENODESELECTEVENT_PREVIOUS == 0) && insertMetamodel) {
				D_SHARP_TREENODESELECTEVENT_PREVIOUS = raapi.createAssociation(D_SHARP_TREENODESELECTEVENT, D_SHARP_TREENODE, "pTreeNodeSelectEvent", "previous", false);
			}
			if (D_SHARP_TREENODESELECTEVENT_PREVIOUS == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end previous of the class D#TreeNodeSelectEvent.");
			}
			D_SHARP_ADDTREENODECMD_TREENODE = raapi.findAssociationEnd(D_SHARP_ADDTREENODECMD, "treeNode");
			if ((D_SHARP_ADDTREENODECMD_TREENODE == 0) && insertMetamodel) {
				D_SHARP_ADDTREENODECMD_TREENODE = raapi.createAssociation(D_SHARP_ADDTREENODECMD, D_SHARP_TREENODE, "tAddTreeNodeCmd", "treeNode", false);
			}
			if (D_SHARP_ADDTREENODECMD_TREENODE == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end treeNode of the class D#AddTreeNodeCmd.");
			}
			D_SHARP_ADDTREENODECMD_PARENT = raapi.findAssociationEnd(D_SHARP_ADDTREENODECMD, "parent");
			if ((D_SHARP_ADDTREENODECMD_PARENT == 0) && insertMetamodel) {
				D_SHARP_ADDTREENODECMD_PARENT = raapi.createAssociation(D_SHARP_ADDTREENODECMD, D_SHARP_TREENODE, "pAddTreeNodeCmd", "parent", false);
			}
			if (D_SHARP_ADDTREENODECMD_PARENT == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end parent of the class D#AddTreeNodeCmd.");
			}
			D_SHARP_ADDTREENODECMD_BEFORE = raapi.findAssociationEnd(D_SHARP_ADDTREENODECMD, "before");
			if ((D_SHARP_ADDTREENODECMD_BEFORE == 0) && insertMetamodel) {
				D_SHARP_ADDTREENODECMD_BEFORE = raapi.createAssociation(D_SHARP_ADDTREENODECMD, D_SHARP_TREENODE, "bAddTreeNodeCmd", "before", false);
			}
			if (D_SHARP_ADDTREENODECMD_BEFORE == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end before of the class D#AddTreeNodeCmd.");
			}
			D_SHARP_DELETETREENODECMD_TREENODE = raapi.findAssociationEnd(D_SHARP_DELETETREENODECMD, "treeNode");
			if ((D_SHARP_DELETETREENODECMD_TREENODE == 0) && insertMetamodel) {
				D_SHARP_DELETETREENODECMD_TREENODE = raapi.createAssociation(D_SHARP_DELETETREENODECMD, D_SHARP_TREENODE, "deleteTreeNodeCmd", "treeNode", false);
			}
			if (D_SHARP_DELETETREENODECMD_TREENODE == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end treeNode of the class D#DeleteTreeNodeCmd.");
			}
			D_SHARP_SELECTTREENODECMD_TREENODE = raapi.findAssociationEnd(D_SHARP_SELECTTREENODECMD, "treeNode");
			if ((D_SHARP_SELECTTREENODECMD_TREENODE == 0) && insertMetamodel) {
				D_SHARP_SELECTTREENODECMD_TREENODE = raapi.createAssociation(D_SHARP_SELECTTREENODECMD, D_SHARP_TREENODE, "selectTreeNodeCmd", "treeNode", false);
			}
			if (D_SHARP_SELECTTREENODECMD_TREENODE == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end treeNode of the class D#SelectTreeNodeCmd.");
			}
			D_SHARP_EXPANDTREENODECMD_TREENODE = raapi.findAssociationEnd(D_SHARP_EXPANDTREENODECMD, "treeNode");
			if ((D_SHARP_EXPANDTREENODECMD_TREENODE == 0) && insertMetamodel) {
				D_SHARP_EXPANDTREENODECMD_TREENODE = raapi.createAssociation(D_SHARP_EXPANDTREENODECMD, D_SHARP_TREENODE, "expandNodeCmd", "treeNode", false);
			}
			if (D_SHARP_EXPANDTREENODECMD_TREENODE == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end treeNode of the class D#ExpandTreeNodeCmd.");
			}
			D_SHARP_COLLAPSETREENODECMD_TREENODE = raapi.findAssociationEnd(D_SHARP_COLLAPSETREENODECMD, "treeNode");
			if ((D_SHARP_COLLAPSETREENODECMD_TREENODE == 0) && insertMetamodel) {
				D_SHARP_COLLAPSETREENODECMD_TREENODE = raapi.createAssociation(D_SHARP_COLLAPSETREENODECMD, D_SHARP_TREENODE, "collapseTreeNodeCmd", "treeNode", false);
			}
			if (D_SHARP_COLLAPSETREENODECMD_TREENODE == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end treeNode of the class D#CollapseTreeNodeCmd.");
			}
			D_SHARP_TREENODE_TEXT = raapi.findAttribute(D_SHARP_TREENODE, "text");
			if ((D_SHARP_TREENODE_TEXT == 0) && insertMetamodel)
				D_SHARP_TREENODE_TEXT = raapi.createAttribute(D_SHARP_TREENODE, "text", raapi.findPrimitiveDataType("String"));
			if (D_SHARP_TREENODE_TEXT == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute text of the class D#TreeNode.");
			}
			D_SHARP_TREENODE_EXPANDED = raapi.findAttribute(D_SHARP_TREENODE, "expanded");
			if ((D_SHARP_TREENODE_EXPANDED == 0) && insertMetamodel)
				D_SHARP_TREENODE_EXPANDED = raapi.createAttribute(D_SHARP_TREENODE, "expanded", raapi.findPrimitiveDataType("Boolean"));
			if (D_SHARP_TREENODE_EXPANDED == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute expanded of the class D#TreeNode.");
			}
			D_SHARP_TREENODE_ID = raapi.findAttribute(D_SHARP_TREENODE, "id");
			if ((D_SHARP_TREENODE_ID == 0) && insertMetamodel)
				D_SHARP_TREENODE_ID = raapi.createAttribute(D_SHARP_TREENODE, "id", raapi.findPrimitiveDataType("String"));
			if (D_SHARP_TREENODE_ID == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute id of the class D#TreeNode.");
			}
			D_SHARP_TREENODE_TREE = raapi.findAssociationEnd(D_SHARP_TREENODE, "tree");
			if ((D_SHARP_TREENODE_TREE == 0) && insertMetamodel) {
				D_SHARP_TREENODE_TREE = raapi.createAssociation(D_SHARP_TREENODE, D_SHARP_TREE, "treeNode", "tree", false);
			}
			if (D_SHARP_TREENODE_TREE == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end tree of the class D#TreeNode.");
			}
			D_SHARP_TREENODE_PARENTTREE = raapi.findAssociationEnd(D_SHARP_TREENODE, "parentTree");
			if ((D_SHARP_TREENODE_PARENTTREE == 0) && insertMetamodel) {
				D_SHARP_TREENODE_PARENTTREE = raapi.createAssociation(D_SHARP_TREENODE, D_SHARP_TREE, "selected", "parentTree", false);
			}
			if (D_SHARP_TREENODE_PARENTTREE == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end parentTree of the class D#TreeNode.");
			}
			D_SHARP_TREENODE_CHILDNODE = raapi.findAssociationEnd(D_SHARP_TREENODE, "childNode");
			if ((D_SHARP_TREENODE_CHILDNODE == 0) && insertMetamodel) {
				D_SHARP_TREENODE_CHILDNODE = raapi.createAssociation(D_SHARP_TREENODE, D_SHARP_TREENODE, "parentNode", "childNode", false);
			}
			if (D_SHARP_TREENODE_CHILDNODE == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end childNode of the class D#TreeNode.");
			}
			D_SHARP_TREENODE_PARENTNODE = raapi.findAssociationEnd(D_SHARP_TREENODE, "parentNode");
			if ((D_SHARP_TREENODE_PARENTNODE == 0) && insertMetamodel) {
				D_SHARP_TREENODE_PARENTNODE = raapi.createAssociation(D_SHARP_TREENODE, D_SHARP_TREENODE, "childNode", "parentNode", false);
			}
			if (D_SHARP_TREENODE_PARENTNODE == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end parentNode of the class D#TreeNode.");
			}
			D_SHARP_TREENODE_TREENODESELECTEVENT = raapi.findAssociationEnd(D_SHARP_TREENODE, "treeNodeSelectEvent");
			if ((D_SHARP_TREENODE_TREENODESELECTEVENT == 0) && insertMetamodel) {
				D_SHARP_TREENODE_TREENODESELECTEVENT = raapi.createAssociation(D_SHARP_TREENODE, D_SHARP_TREENODESELECTEVENT, "treeNode", "treeNodeSelectEvent", false);
			}
			if (D_SHARP_TREENODE_TREENODESELECTEVENT == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end treeNodeSelectEvent of the class D#TreeNode.");
			}
			D_SHARP_TREENODE_PTREENODESELECTEVENT = raapi.findAssociationEnd(D_SHARP_TREENODE, "pTreeNodeSelectEvent");
			if ((D_SHARP_TREENODE_PTREENODESELECTEVENT == 0) && insertMetamodel) {
				D_SHARP_TREENODE_PTREENODESELECTEVENT = raapi.createAssociation(D_SHARP_TREENODE, D_SHARP_TREENODESELECTEVENT, "previous", "pTreeNodeSelectEvent", false);
			}
			if (D_SHARP_TREENODE_PTREENODESELECTEVENT == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end pTreeNodeSelectEvent of the class D#TreeNode.");
			}
			D_SHARP_TREENODE_TADDTREENODECMD = raapi.findAssociationEnd(D_SHARP_TREENODE, "tAddTreeNodeCmd");
			if ((D_SHARP_TREENODE_TADDTREENODECMD == 0) && insertMetamodel) {
				D_SHARP_TREENODE_TADDTREENODECMD = raapi.createAssociation(D_SHARP_TREENODE, D_SHARP_ADDTREENODECMD, "treeNode", "tAddTreeNodeCmd", false);
			}
			if (D_SHARP_TREENODE_TADDTREENODECMD == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end tAddTreeNodeCmd of the class D#TreeNode.");
			}
			D_SHARP_TREENODE_PADDTREENODECMD = raapi.findAssociationEnd(D_SHARP_TREENODE, "pAddTreeNodeCmd");
			if ((D_SHARP_TREENODE_PADDTREENODECMD == 0) && insertMetamodel) {
				D_SHARP_TREENODE_PADDTREENODECMD = raapi.createAssociation(D_SHARP_TREENODE, D_SHARP_ADDTREENODECMD, "parent", "pAddTreeNodeCmd", false);
			}
			if (D_SHARP_TREENODE_PADDTREENODECMD == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end pAddTreeNodeCmd of the class D#TreeNode.");
			}
			D_SHARP_TREENODE_BADDTREENODECMD = raapi.findAssociationEnd(D_SHARP_TREENODE, "bAddTreeNodeCmd");
			if ((D_SHARP_TREENODE_BADDTREENODECMD == 0) && insertMetamodel) {
				D_SHARP_TREENODE_BADDTREENODECMD = raapi.createAssociation(D_SHARP_TREENODE, D_SHARP_ADDTREENODECMD, "before", "bAddTreeNodeCmd", false);
			}
			if (D_SHARP_TREENODE_BADDTREENODECMD == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end bAddTreeNodeCmd of the class D#TreeNode.");
			}
			D_SHARP_TREENODE_DELETETREENODECMD = raapi.findAssociationEnd(D_SHARP_TREENODE, "deleteTreeNodeCmd");
			if ((D_SHARP_TREENODE_DELETETREENODECMD == 0) && insertMetamodel) {
				D_SHARP_TREENODE_DELETETREENODECMD = raapi.createAssociation(D_SHARP_TREENODE, D_SHARP_DELETETREENODECMD, "treeNode", "deleteTreeNodeCmd", false);
			}
			if (D_SHARP_TREENODE_DELETETREENODECMD == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end deleteTreeNodeCmd of the class D#TreeNode.");
			}
			D_SHARP_TREENODE_SELECTTREENODECMD = raapi.findAssociationEnd(D_SHARP_TREENODE, "selectTreeNodeCmd");
			if ((D_SHARP_TREENODE_SELECTTREENODECMD == 0) && insertMetamodel) {
				D_SHARP_TREENODE_SELECTTREENODECMD = raapi.createAssociation(D_SHARP_TREENODE, D_SHARP_SELECTTREENODECMD, "treeNode", "selectTreeNodeCmd", false);
			}
			if (D_SHARP_TREENODE_SELECTTREENODECMD == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end selectTreeNodeCmd of the class D#TreeNode.");
			}
			D_SHARP_TREENODE_EXPANDNODECMD = raapi.findAssociationEnd(D_SHARP_TREENODE, "expandNodeCmd");
			if ((D_SHARP_TREENODE_EXPANDNODECMD == 0) && insertMetamodel) {
				D_SHARP_TREENODE_EXPANDNODECMD = raapi.createAssociation(D_SHARP_TREENODE, D_SHARP_EXPANDTREENODECMD, "treeNode", "expandNodeCmd", false);
			}
			if (D_SHARP_TREENODE_EXPANDNODECMD == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end expandNodeCmd of the class D#TreeNode.");
			}
			D_SHARP_TREENODE_COLLAPSETREENODECMD = raapi.findAssociationEnd(D_SHARP_TREENODE, "collapseTreeNodeCmd");
			if ((D_SHARP_TREENODE_COLLAPSETREENODECMD == 0) && insertMetamodel) {
				D_SHARP_TREENODE_COLLAPSETREENODECMD = raapi.createAssociation(D_SHARP_TREENODE, D_SHARP_COLLAPSETREENODECMD, "treeNode", "collapseTreeNodeCmd", false);
			}
			if (D_SHARP_TREENODE_COLLAPSETREENODECMD == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end collapseTreeNodeCmd of the class D#TreeNode.");
			}
			D_SHARP_TREENODE_TTREENODEMOVEEVENT = raapi.findAssociationEnd(D_SHARP_TREENODE, "tTreeNodeMoveEvent");
			if ((D_SHARP_TREENODE_TTREENODEMOVEEVENT == 0) && insertMetamodel) {
				D_SHARP_TREENODE_TTREENODEMOVEEVENT = raapi.createAssociation(D_SHARP_TREENODE, D_SHARP_TREENODEMOVEEVENT, "treeNode", "tTreeNodeMoveEvent", false);
			}
			if (D_SHARP_TREENODE_TTREENODEMOVEEVENT == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end tTreeNodeMoveEvent of the class D#TreeNode.");
			}
			D_SHARP_TREENODE_BTREENODEMOVEEVENT = raapi.findAssociationEnd(D_SHARP_TREENODE, "bTreeNodeMoveEvent");
			if ((D_SHARP_TREENODE_BTREENODEMOVEEVENT == 0) && insertMetamodel) {
				D_SHARP_TREENODE_BTREENODEMOVEEVENT = raapi.createAssociation(D_SHARP_TREENODE, D_SHARP_TREENODEMOVEEVENT, "wasBefore", "bTreeNodeMoveEvent", false);
			}
			if (D_SHARP_TREENODE_BTREENODEMOVEEVENT == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end bTreeNodeMoveEvent of the class D#TreeNode.");
			}
			D_SHARP_TREENODE_ATREENODEMOVEEVENT = raapi.findAssociationEnd(D_SHARP_TREENODE, "aTreeNodeMoveEvent");
			if ((D_SHARP_TREENODE_ATREENODEMOVEEVENT == 0) && insertMetamodel) {
				D_SHARP_TREENODE_ATREENODEMOVEEVENT = raapi.createAssociation(D_SHARP_TREENODE, D_SHARP_TREENODEMOVEEVENT, "wasAfter", "aTreeNodeMoveEvent", false);
			}
			if (D_SHARP_TREENODE_ATREENODEMOVEEVENT == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end aTreeNodeMoveEvent of the class D#TreeNode.");
			}
			D_SHARP_TREENODE_PTREENODEMOVEEVENT = raapi.findAssociationEnd(D_SHARP_TREENODE, "pTreeNodeMoveEvent");
			if ((D_SHARP_TREENODE_PTREENODEMOVEEVENT == 0) && insertMetamodel) {
				D_SHARP_TREENODE_PTREENODEMOVEEVENT = raapi.createAssociation(D_SHARP_TREENODE, D_SHARP_TREENODEMOVEEVENT, "previousParent", "pTreeNodeMoveEvent", false);
			}
			if (D_SHARP_TREENODE_PTREENODEMOVEEVENT == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end pTreeNodeMoveEvent of the class D#TreeNode.");
			}
			D_SHARP_TREENODE_TREENODEEXPANDEDEVENT = raapi.findAssociationEnd(D_SHARP_TREENODE, "treeNodeExpandedEvent");
			if ((D_SHARP_TREENODE_TREENODEEXPANDEDEVENT == 0) && insertMetamodel) {
				D_SHARP_TREENODE_TREENODEEXPANDEDEVENT = raapi.createAssociation(D_SHARP_TREENODE, D_SHARP_TREENODEEXPANDEDEVENT, "treeNode", "treeNodeExpandedEvent", false);
			}
			if (D_SHARP_TREENODE_TREENODEEXPANDEDEVENT == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end treeNodeExpandedEvent of the class D#TreeNode.");
			}
			D_SHARP_TREENODE_TREENODECOLLAPSEDEVENT = raapi.findAssociationEnd(D_SHARP_TREENODE, "treeNodeCollapsedEvent");
			if ((D_SHARP_TREENODE_TREENODECOLLAPSEDEVENT == 0) && insertMetamodel) {
				D_SHARP_TREENODE_TREENODECOLLAPSEDEVENT = raapi.createAssociation(D_SHARP_TREENODE, D_SHARP_TREENODECOLLAPSEDEVENT, "treeNode", "treeNodeCollapsedEvent", false);
			}
			if (D_SHARP_TREENODE_TREENODECOLLAPSEDEVENT == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end treeNodeCollapsedEvent of the class D#TreeNode.");
			}
			D_SHARP_LISTBOXCHANGEEVENT_SELECTED = raapi.findAssociationEnd(D_SHARP_LISTBOXCHANGEEVENT, "selected");
			if ((D_SHARP_LISTBOXCHANGEEVENT_SELECTED == 0) && insertMetamodel) {
				D_SHARP_LISTBOXCHANGEEVENT_SELECTED = raapi.createAssociation(D_SHARP_LISTBOXCHANGEEVENT, D_SHARP_ITEM, "sListBoxChangeEvent", "selected", false);
			}
			if (D_SHARP_LISTBOXCHANGEEVENT_SELECTED == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end selected of the class D#ListBoxChangeEvent.");
			}
			D_SHARP_LISTBOXCHANGEEVENT_DESELECTED = raapi.findAssociationEnd(D_SHARP_LISTBOXCHANGEEVENT, "deselected");
			if ((D_SHARP_LISTBOXCHANGEEVENT_DESELECTED == 0) && insertMetamodel) {
				D_SHARP_LISTBOXCHANGEEVENT_DESELECTED = raapi.createAssociation(D_SHARP_LISTBOXCHANGEEVENT, D_SHARP_ITEM, "dListBoxChangeEvent", "deselected", false);
			}
			if (D_SHARP_LISTBOXCHANGEEVENT_DESELECTED == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end deselected of the class D#ListBoxChangeEvent.");
			}
			D_SHARP_TABCHANGEEVENT_TAB = raapi.findAssociationEnd(D_SHARP_TABCHANGEEVENT, "tab");
			if ((D_SHARP_TABCHANGEEVENT_TAB == 0) && insertMetamodel) {
				D_SHARP_TABCHANGEEVENT_TAB = raapi.createAssociation(D_SHARP_TABCHANGEEVENT, D_SHARP_TAB, "tabChangeEvent", "tab", false);
			}
			if (D_SHARP_TABCHANGEEVENT_TAB == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end tab of the class D#TabChangeEvent.");
			}
			D_SHARP_TREENODEMOVEEVENT_TREENODE = raapi.findAssociationEnd(D_SHARP_TREENODEMOVEEVENT, "treeNode");
			if ((D_SHARP_TREENODEMOVEEVENT_TREENODE == 0) && insertMetamodel) {
				D_SHARP_TREENODEMOVEEVENT_TREENODE = raapi.createAssociation(D_SHARP_TREENODEMOVEEVENT, D_SHARP_TREENODE, "tTreeNodeMoveEvent", "treeNode", false);
			}
			if (D_SHARP_TREENODEMOVEEVENT_TREENODE == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end treeNode of the class D#TreeNodeMoveEvent.");
			}
			D_SHARP_TREENODEMOVEEVENT_WASBEFORE = raapi.findAssociationEnd(D_SHARP_TREENODEMOVEEVENT, "wasBefore");
			if ((D_SHARP_TREENODEMOVEEVENT_WASBEFORE == 0) && insertMetamodel) {
				D_SHARP_TREENODEMOVEEVENT_WASBEFORE = raapi.createAssociation(D_SHARP_TREENODEMOVEEVENT, D_SHARP_TREENODE, "bTreeNodeMoveEvent", "wasBefore", false);
			}
			if (D_SHARP_TREENODEMOVEEVENT_WASBEFORE == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end wasBefore of the class D#TreeNodeMoveEvent.");
			}
			D_SHARP_TREENODEMOVEEVENT_WASAFTER = raapi.findAssociationEnd(D_SHARP_TREENODEMOVEEVENT, "wasAfter");
			if ((D_SHARP_TREENODEMOVEEVENT_WASAFTER == 0) && insertMetamodel) {
				D_SHARP_TREENODEMOVEEVENT_WASAFTER = raapi.createAssociation(D_SHARP_TREENODEMOVEEVENT, D_SHARP_TREENODE, "aTreeNodeMoveEvent", "wasAfter", false);
			}
			if (D_SHARP_TREENODEMOVEEVENT_WASAFTER == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end wasAfter of the class D#TreeNodeMoveEvent.");
			}
			D_SHARP_TREENODEMOVEEVENT_PREVIOUSPARENT = raapi.findAssociationEnd(D_SHARP_TREENODEMOVEEVENT, "previousParent");
			if ((D_SHARP_TREENODEMOVEEVENT_PREVIOUSPARENT == 0) && insertMetamodel) {
				D_SHARP_TREENODEMOVEEVENT_PREVIOUSPARENT = raapi.createAssociation(D_SHARP_TREENODEMOVEEVENT, D_SHARP_TREENODE, "pTreeNodeMoveEvent", "previousParent", false);
			}
			if (D_SHARP_TREENODEMOVEEVENT_PREVIOUSPARENT == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end previousParent of the class D#TreeNodeMoveEvent.");
			}
			D_SHARP_COPYTOCLIPBOARDCMD_VTABLE = raapi.findAssociationEnd(D_SHARP_COPYTOCLIPBOARDCMD, "vTable");
			if ((D_SHARP_COPYTOCLIPBOARDCMD_VTABLE == 0) && insertMetamodel) {
				D_SHARP_COPYTOCLIPBOARDCMD_VTABLE = raapi.createAssociation(D_SHARP_COPYTOCLIPBOARDCMD, D_SHARP_VTABLE, "copyToClipboard", "vTable", false);
			}
			if (D_SHARP_COPYTOCLIPBOARDCMD_VTABLE == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end vTable of the class D#CopyToClipboardCmd.");
			}
			D_SHARP_KEYDOWNEVENT_KEYNAME = raapi.findAttribute(D_SHARP_KEYDOWNEVENT, "keyName");
			if ((D_SHARP_KEYDOWNEVENT_KEYNAME == 0) && insertMetamodel)
				D_SHARP_KEYDOWNEVENT_KEYNAME = raapi.createAttribute(D_SHARP_KEYDOWNEVENT, "keyName", raapi.findPrimitiveDataType("String"));
			if (D_SHARP_KEYDOWNEVENT_KEYNAME == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the attibute keyName of the class D#KeyDownEvent.");
			}
			D_SHARP_TREENODEEXPANDEDEVENT_TREENODE = raapi.findAssociationEnd(D_SHARP_TREENODEEXPANDEDEVENT, "treeNode");
			if ((D_SHARP_TREENODEEXPANDEDEVENT_TREENODE == 0) && insertMetamodel) {
				D_SHARP_TREENODEEXPANDEDEVENT_TREENODE = raapi.createAssociation(D_SHARP_TREENODEEXPANDEDEVENT, D_SHARP_TREENODE, "treeNodeExpandedEvent", "treeNode", false);
			}
			if (D_SHARP_TREENODEEXPANDEDEVENT_TREENODE == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end treeNode of the class D#TreeNodeExpandedEvent.");
			}
			D_SHARP_TREENODECOLLAPSEDEVENT_TREENODE = raapi.findAssociationEnd(D_SHARP_TREENODECOLLAPSEDEVENT, "treeNode");
			if ((D_SHARP_TREENODECOLLAPSEDEVENT_TREENODE == 0) && insertMetamodel) {
				D_SHARP_TREENODECOLLAPSEDEVENT_TREENODE = raapi.createAssociation(D_SHARP_TREENODECOLLAPSEDEVENT, D_SHARP_TREENODE, "treeNodeCollapsedEvent", "treeNode", false);
			}
			if (D_SHARP_TREENODECOLLAPSEDEVENT_TREENODE == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end treeNode of the class D#TreeNodeCollapsedEvent.");
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
			FRAME_FORM = raapi.findAssociationEnd(FRAME, "form");
			if ((FRAME_FORM == 0) && insertMetamodel) {
				FRAME_FORM = raapi.createAssociation(FRAME, D_SHARP_FORM, "frame", "form", false);
			}
			if (FRAME_FORM == 0) {
				setRAAPI(null, null, false); // freeing references initialized so far...
				throw new ElementReferenceException("Error obtaining a reference for the association end form of the class Frame.");
			}
		}
	}

	public D_SHARP_Group createD_SHARP_Group()
	{
		D_SHARP_Group retVal = new D_SHARP_Group(this);
		wrappers.put(retVal.getRAAPIReference(), retVal);
		return retVal; 
	}
  
	public D_SHARP_EventSource createD_SHARP_EventSource()
	{
		D_SHARP_EventSource retVal = new D_SHARP_EventSource(this);
		wrappers.put(retVal.getRAAPIReference(), retVal);
		return retVal; 
	}
  
	public D_SHARP_Component createD_SHARP_Component()
	{
		D_SHARP_Component retVal = new D_SHARP_Component(this);
		wrappers.put(retVal.getRAAPIReference(), retVal);
		return retVal; 
	}
  
	public D_SHARP_Container createD_SHARP_Container()
	{
		D_SHARP_Container retVal = new D_SHARP_Container(this);
		wrappers.put(retVal.getRAAPIReference(), retVal);
		return retVal; 
	}
  
	public D_SHARP_Row createD_SHARP_Row()
	{
		D_SHARP_Row retVal = new D_SHARP_Row(this);
		wrappers.put(retVal.getRAAPIReference(), retVal);
		return retVal; 
	}
  
	public D_SHARP_Column createD_SHARP_Column()
	{
		D_SHARP_Column retVal = new D_SHARP_Column(this);
		wrappers.put(retVal.getRAAPIReference(), retVal);
		return retVal; 
	}
  
	public D_SHARP_VerticalBox createD_SHARP_VerticalBox()
	{
		D_SHARP_VerticalBox retVal = new D_SHARP_VerticalBox(this);
		wrappers.put(retVal.getRAAPIReference(), retVal);
		return retVal; 
	}
  
	public D_SHARP_GroupBox createD_SHARP_GroupBox()
	{
		D_SHARP_GroupBox retVal = new D_SHARP_GroupBox(this);
		wrappers.put(retVal.getRAAPIReference(), retVal);
		return retVal; 
	}
  
	public D_SHARP_Form createD_SHARP_Form()
	{
		D_SHARP_Form retVal = new D_SHARP_Form(this);
		wrappers.put(retVal.getRAAPIReference(), retVal);
		return retVal; 
	}
  
	public D_SHARP_TableDiagram createD_SHARP_TableDiagram()
	{
		D_SHARP_TableDiagram retVal = new D_SHARP_TableDiagram(this);
		wrappers.put(retVal.getRAAPIReference(), retVal);
		return retVal; 
	}
  
	public D_SHARP_HorizontalBox createD_SHARP_HorizontalBox()
	{
		D_SHARP_HorizontalBox retVal = new D_SHARP_HorizontalBox(this);
		wrappers.put(retVal.getRAAPIReference(), retVal);
		return retVal; 
	}
  
	public D_SHARP_HorizontalScrollBox createD_SHARP_HorizontalScrollBox()
	{
		D_SHARP_HorizontalScrollBox retVal = new D_SHARP_HorizontalScrollBox(this);
		wrappers.put(retVal.getRAAPIReference(), retVal);
		return retVal; 
	}
  
	public D_SHARP_VerticalScrollBox createD_SHARP_VerticalScrollBox()
	{
		D_SHARP_VerticalScrollBox retVal = new D_SHARP_VerticalScrollBox(this);
		wrappers.put(retVal.getRAAPIReference(), retVal);
		return retVal; 
	}
  
	public D_SHARP_ScrollBox createD_SHARP_ScrollBox()
	{
		D_SHARP_ScrollBox retVal = new D_SHARP_ScrollBox(this);
		wrappers.put(retVal.getRAAPIReference(), retVal);
		return retVal; 
	}
  
	public D_SHARP_Tab createD_SHARP_Tab()
	{
		D_SHARP_Tab retVal = new D_SHARP_Tab(this);
		wrappers.put(retVal.getRAAPIReference(), retVal);
		return retVal; 
	}
  
	public D_SHARP_Stack createD_SHARP_Stack()
	{
		D_SHARP_Stack retVal = new D_SHARP_Stack(this);
		wrappers.put(retVal.getRAAPIReference(), retVal);
		return retVal; 
	}
  
	public D_SHARP_TabContainer createD_SHARP_TabContainer()
	{
		D_SHARP_TabContainer retVal = new D_SHARP_TabContainer(this);
		wrappers.put(retVal.getRAAPIReference(), retVal);
		return retVal; 
	}
  
	public D_SHARP_VerticalScrollBoxWrapper createD_SHARP_VerticalScrollBoxWrapper()
	{
		D_SHARP_VerticalScrollBoxWrapper retVal = new D_SHARP_VerticalScrollBoxWrapper(this);
		wrappers.put(retVal.getRAAPIReference(), retVal);
		return retVal; 
	}
  
	public D_SHARP_HorizontalScrollBoxWrapper createD_SHARP_HorizontalScrollBoxWrapper()
	{
		D_SHARP_HorizontalScrollBoxWrapper retVal = new D_SHARP_HorizontalScrollBoxWrapper(this);
		wrappers.put(retVal.getRAAPIReference(), retVal);
		return retVal; 
	}
  
	public D_SHARP_VerticalSplitBox createD_SHARP_VerticalSplitBox()
	{
		D_SHARP_VerticalSplitBox retVal = new D_SHARP_VerticalSplitBox(this);
		wrappers.put(retVal.getRAAPIReference(), retVal);
		return retVal; 
	}
  
	public D_SHARP_HorizontalSplitBox createD_SHARP_HorizontalSplitBox()
	{
		D_SHARP_HorizontalSplitBox retVal = new D_SHARP_HorizontalSplitBox(this);
		wrappers.put(retVal.getRAAPIReference(), retVal);
		return retVal; 
	}
  
	public D_SHARP_Label createD_SHARP_Label()
	{
		D_SHARP_Label retVal = new D_SHARP_Label(this);
		wrappers.put(retVal.getRAAPIReference(), retVal);
		return retVal; 
	}
  
	public D_SHARP_RadioButton createD_SHARP_RadioButton()
	{
		D_SHARP_RadioButton retVal = new D_SHARP_RadioButton(this);
		wrappers.put(retVal.getRAAPIReference(), retVal);
		return retVal; 
	}
  
	public D_SHARP_ListBox createD_SHARP_ListBox()
	{
		D_SHARP_ListBox retVal = new D_SHARP_ListBox(this);
		wrappers.put(retVal.getRAAPIReference(), retVal);
		return retVal; 
	}
  
	public D_SHARP_VTableType createD_SHARP_VTableType()
	{
		D_SHARP_VTableType retVal = new D_SHARP_VTableType(this);
		wrappers.put(retVal.getRAAPIReference(), retVal);
		return retVal; 
	}
  
	public D_SHARP_VTable createD_SHARP_VTable()
	{
		D_SHARP_VTable retVal = new D_SHARP_VTable(this);
		wrappers.put(retVal.getRAAPIReference(), retVal);
		return retVal; 
	}
  
	public D_SHARP_VTableRow createD_SHARP_VTableRow()
	{
		D_SHARP_VTableRow retVal = new D_SHARP_VTableRow(this);
		wrappers.put(retVal.getRAAPIReference(), retVal);
		return retVal; 
	}
  
	public D_SHARP_TableComponent createD_SHARP_TableComponent()
	{
		D_SHARP_TableComponent retVal = new D_SHARP_TableComponent(this);
		wrappers.put(retVal.getRAAPIReference(), retVal);
		return retVal; 
	}
  
	public D_SHARP_Button createD_SHARP_Button()
	{
		D_SHARP_Button retVal = new D_SHARP_Button(this);
		wrappers.put(retVal.getRAAPIReference(), retVal);
		return retVal; 
	}
  
	public D_SHARP_CheckBox createD_SHARP_CheckBox()
	{
		D_SHARP_CheckBox retVal = new D_SHARP_CheckBox(this);
		wrappers.put(retVal.getRAAPIReference(), retVal);
		return retVal; 
	}
  
	public D_SHARP_ComboBox createD_SHARP_ComboBox()
	{
		D_SHARP_ComboBox retVal = new D_SHARP_ComboBox(this);
		wrappers.put(retVal.getRAAPIReference(), retVal);
		return retVal; 
	}
  
	public D_SHARP_Item createD_SHARP_Item()
	{
		D_SHARP_Item retVal = new D_SHARP_Item(this);
		wrappers.put(retVal.getRAAPIReference(), retVal);
		return retVal; 
	}
  
	public D_SHARP_Image createD_SHARP_Image()
	{
		D_SHARP_Image retVal = new D_SHARP_Image(this);
		wrappers.put(retVal.getRAAPIReference(), retVal);
		return retVal; 
	}
  
	public D_SHARP_TextBox createD_SHARP_TextBox()
	{
		D_SHARP_TextBox retVal = new D_SHARP_TextBox(this);
		wrappers.put(retVal.getRAAPIReference(), retVal);
		return retVal; 
	}
  
	public D_SHARP_InputField createD_SHARP_InputField()
	{
		D_SHARP_InputField retVal = new D_SHARP_InputField(this);
		wrappers.put(retVal.getRAAPIReference(), retVal);
		return retVal; 
	}
  
	public D_SHARP_TextArea createD_SHARP_TextArea()
	{
		D_SHARP_TextArea retVal = new D_SHARP_TextArea(this);
		wrappers.put(retVal.getRAAPIReference(), retVal);
		return retVal; 
	}
  
	public D_SHARP_Tree createD_SHARP_Tree()
	{
		D_SHARP_Tree retVal = new D_SHARP_Tree(this);
		wrappers.put(retVal.getRAAPIReference(), retVal);
		return retVal; 
	}
  
	public D_SHARP_MultiLineTextBox createD_SHARP_MultiLineTextBox()
	{
		D_SHARP_MultiLineTextBox retVal = new D_SHARP_MultiLineTextBox(this);
		wrappers.put(retVal.getRAAPIReference(), retVal);
		return retVal; 
	}
  
	public D_SHARP_RichTextArea createD_SHARP_RichTextArea()
	{
		D_SHARP_RichTextArea retVal = new D_SHARP_RichTextArea(this);
		wrappers.put(retVal.getRAAPIReference(), retVal);
		return retVal; 
	}
  
	public D_SHARP_ProgressBar createD_SHARP_ProgressBar()
	{
		D_SHARP_ProgressBar retVal = new D_SHARP_ProgressBar(this);
		wrappers.put(retVal.getRAAPIReference(), retVal);
		return retVal; 
	}
  
	public D_SHARP_ImageButton createD_SHARP_ImageButton()
	{
		D_SHARP_ImageButton retVal = new D_SHARP_ImageButton(this);
		wrappers.put(retVal.getRAAPIReference(), retVal);
		return retVal; 
	}
  
	public D_SHARP_VTableColumnType createD_SHARP_VTableColumnType()
	{
		D_SHARP_VTableColumnType retVal = new D_SHARP_VTableColumnType(this);
		wrappers.put(retVal.getRAAPIReference(), retVal);
		return retVal; 
	}
  
	public D_SHARP_VTableCell createD_SHARP_VTableCell()
	{
		D_SHARP_VTableCell retVal = new D_SHARP_VTableCell(this);
		wrappers.put(retVal.getRAAPIReference(), retVal);
		return retVal; 
	}
  
	public D_SHARP_EventHandler createD_SHARP_EventHandler()
	{
		D_SHARP_EventHandler retVal = new D_SHARP_EventHandler(this);
		wrappers.put(retVal.getRAAPIReference(), retVal);
		return retVal; 
	}
  
	public D_SHARP_Command createD_SHARP_Command()
	{
		D_SHARP_Command retVal = new D_SHARP_Command(this);
		wrappers.put(retVal.getRAAPIReference(), retVal);
		return retVal; 
	}
  
	public D_SHARP_Event createD_SHARP_Event()
	{
		D_SHARP_Event retVal = new D_SHARP_Event(this);
		wrappers.put(retVal.getRAAPIReference(), retVal);
		return retVal; 
	}
  
	public D_SHARP_RowMovedEvent createD_SHARP_RowMovedEvent()
	{
		D_SHARP_RowMovedEvent retVal = new D_SHARP_RowMovedEvent(this);
		wrappers.put(retVal.getRAAPIReference(), retVal);
		return retVal; 
	}
  
	public D_SHARP_ColumnMovedEvent createD_SHARP_ColumnMovedEvent()
	{
		D_SHARP_ColumnMovedEvent retVal = new D_SHARP_ColumnMovedEvent(this);
		wrappers.put(retVal.getRAAPIReference(), retVal);
		return retVal; 
	}
  
	public D_SHARP_MultiLineTextBoxChangeEvent createD_SHARP_MultiLineTextBoxChangeEvent()
	{
		D_SHARP_MultiLineTextBoxChangeEvent retVal = new D_SHARP_MultiLineTextBoxChangeEvent(this);
		wrappers.put(retVal.getRAAPIReference(), retVal);
		return retVal; 
	}
  
	public D_SHARP_TextLine createD_SHARP_TextLine()
	{
		D_SHARP_TextLine retVal = new D_SHARP_TextLine(this);
		wrappers.put(retVal.getRAAPIReference(), retVal);
		return retVal; 
	}
  
	public D_SHARP_TreeNodeSelectEvent createD_SHARP_TreeNodeSelectEvent()
	{
		D_SHARP_TreeNodeSelectEvent retVal = new D_SHARP_TreeNodeSelectEvent(this);
		wrappers.put(retVal.getRAAPIReference(), retVal);
		return retVal; 
	}
  
	public D_SHARP_AddTreeNodeCmd createD_SHARP_AddTreeNodeCmd()
	{
		D_SHARP_AddTreeNodeCmd retVal = new D_SHARP_AddTreeNodeCmd(this);
		wrappers.put(retVal.getRAAPIReference(), retVal);
		return retVal; 
	}
  
	public D_SHARP_DeleteTreeNodeCmd createD_SHARP_DeleteTreeNodeCmd()
	{
		D_SHARP_DeleteTreeNodeCmd retVal = new D_SHARP_DeleteTreeNodeCmd(this);
		wrappers.put(retVal.getRAAPIReference(), retVal);
		return retVal; 
	}
  
	public D_SHARP_SelectTreeNodeCmd createD_SHARP_SelectTreeNodeCmd()
	{
		D_SHARP_SelectTreeNodeCmd retVal = new D_SHARP_SelectTreeNodeCmd(this);
		wrappers.put(retVal.getRAAPIReference(), retVal);
		return retVal; 
	}
  
	public D_SHARP_ExpandTreeNodeCmd createD_SHARP_ExpandTreeNodeCmd()
	{
		D_SHARP_ExpandTreeNodeCmd retVal = new D_SHARP_ExpandTreeNodeCmd(this);
		wrappers.put(retVal.getRAAPIReference(), retVal);
		return retVal; 
	}
  
	public D_SHARP_CollapseTreeNodeCmd createD_SHARP_CollapseTreeNodeCmd()
	{
		D_SHARP_CollapseTreeNodeCmd retVal = new D_SHARP_CollapseTreeNodeCmd(this);
		wrappers.put(retVal.getRAAPIReference(), retVal);
		return retVal; 
	}
  
	public D_SHARP_TreeNode createD_SHARP_TreeNode()
	{
		D_SHARP_TreeNode retVal = new D_SHARP_TreeNode(this);
		wrappers.put(retVal.getRAAPIReference(), retVal);
		return retVal; 
	}
  
	public D_SHARP_ListBoxChangeEvent createD_SHARP_ListBoxChangeEvent()
	{
		D_SHARP_ListBoxChangeEvent retVal = new D_SHARP_ListBoxChangeEvent(this);
		wrappers.put(retVal.getRAAPIReference(), retVal);
		return retVal; 
	}
  
	public D_SHARP_TabChangeEvent createD_SHARP_TabChangeEvent()
	{
		D_SHARP_TabChangeEvent retVal = new D_SHARP_TabChangeEvent(this);
		wrappers.put(retVal.getRAAPIReference(), retVal);
		return retVal; 
	}
  
	public D_SHARP_TreeNodeMoveEvent createD_SHARP_TreeNodeMoveEvent()
	{
		D_SHARP_TreeNodeMoveEvent retVal = new D_SHARP_TreeNodeMoveEvent(this);
		wrappers.put(retVal.getRAAPIReference(), retVal);
		return retVal; 
	}
  
	public D_SHARP_CopyToClipboardCmd createD_SHARP_CopyToClipboardCmd()
	{
		D_SHARP_CopyToClipboardCmd retVal = new D_SHARP_CopyToClipboardCmd(this);
		wrappers.put(retVal.getRAAPIReference(), retVal);
		return retVal; 
	}
  
	public D_SHARP_KeyDownEvent createD_SHARP_KeyDownEvent()
	{
		D_SHARP_KeyDownEvent retVal = new D_SHARP_KeyDownEvent(this);
		wrappers.put(retVal.getRAAPIReference(), retVal);
		return retVal; 
	}
  
	public D_SHARP_TreeNodeExpandedEvent createD_SHARP_TreeNodeExpandedEvent()
	{
		D_SHARP_TreeNodeExpandedEvent retVal = new D_SHARP_TreeNodeExpandedEvent(this);
		wrappers.put(retVal.getRAAPIReference(), retVal);
		return retVal; 
	}
  
	public D_SHARP_TreeNodeCollapsedEvent createD_SHARP_TreeNodeCollapsedEvent()
	{
		D_SHARP_TreeNodeCollapsedEvent retVal = new D_SHARP_TreeNodeCollapsedEvent(this);
		wrappers.put(retVal.getRAAPIReference(), retVal);
		return retVal; 
	}
  
	public Command createCommand()
	{
		Command retVal = new Command(this);
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
