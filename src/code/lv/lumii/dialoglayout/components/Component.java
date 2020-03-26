package lv.lumii.dialoglayout.components;

import lv.lumii.layoutengine.funcmin.ExtendedQuadraticOptimizer;

import java.util.ArrayList;

import lv.lumii.dialoglayout.IMCSDialogLayout;
import lv.lumii.dialoglayout.components.utils.ComponentBounds;

public abstract class Component { // base class
	protected IMCSDialogLayout.ComponentCallback callback;
	protected long reference;
	protected Component parent;
	protected ArrayList<Component> children = new ArrayList<Component>();//Component[] children; // always non-null
	protected ComponentBounds bounds = null;
	
	protected Row lastRow = null; // for row inter-operation
	protected Column lastColumn = null; // for column inter-operation
	protected int maxRowChildren = 0;
	protected int maxColumnChildren = 0;
	
	public static Component createComponent(IMCSDialogLayout.ComponentCallback _callback, long _reference, Component _parent) {
		String typeName = null;
		
		if (_callback!=null)
			typeName = _callback.getLayoutName(_reference);
		
		if ((typeName == null) || (typeName.isEmpty()))
			return new LeafComponent(_callback, _reference, _parent);
		
		if (typeName.equals("VerticalBox"))
			return new VerticalBox(_callback, _reference, _parent);
		if (typeName.equals("HorizontalBox"))
			return new HorizontalBox(_callback, _reference, _parent);
		if (typeName.equals("Row"))
			return new Row(_callback, _reference, _parent);
		if (typeName.equals("Column"))
			return new Column(_callback, _reference, _parent);
		if (typeName.equals("Stack"))
			return new Stack(_callback, _reference, _parent);
		//TODO: "VerticalScrollBox", "HorizontalScrollBox", "ScrollBox"
		
		
		return new VerticalBox(_callback, _reference, _parent); // default
	}
	
	public Component(IMCSDialogLayout.ComponentCallback _callback, long _reference, Component _parent) { // also loads the component
		callback = _callback;
		reference = _reference;
		
		parent = _parent;
		reinitialize();
	}
	
	public void reinitialize() {
		if ((callback!=null) && (reference!=0))
			callback.load(reference, parent==null?0:parent.reference);
		
		long[] arr = null;
		if (callback!=null)
			arr = callback.getChildren(reference);
		
		if (arr != null) {
//			children = new Component[arr.length];
			for (int i=0; i<arr.length; i++) {
				long rChild = arr[i];
				Component child = createComponent(callback, rChild, this);
				//children[i] = child;
				children.add(child);
			} // for		
		}
		else {
//			children = new Component[0];
		}		
		
		if (parent instanceof Stack) {
			children.add(new HorizontalBox(null, 0, this)); // adding a new horizontal box to the tab; it will fill the space at the bottom of the tab
		}
	}
	
	public void destroyChildrenRecursively(IMCSDialogLayout.ComponentCallback _callback) {
		
		for (Component child : children) {
			child.destroyChildrenRecursively(_callback);
			if (_callback != null)
				_callback.destroy(child.reference);
		}
		children.clear();
		lastRow = null;
		lastColumn = null;
	}
	
	public long getReference() {//reference to a component in the model
		return reference;
	}
	
	public Component getParent() {
		return parent;
	}
	
	public Component[] getChildren() { // returns array of children
		return children.toArray(new Component[]{});
	}
	
	public ComponentBounds getComponentBounds() {
		if (bounds == null) {
			for (Component child : children)
				child.getComponentBounds();
			if ((callback != null) && (reference!=0))
				bounds = callback.getBounds(reference);
			else
				bounds = new ComponentBounds();
			
			if ((bounds.maxW!=null) && (bounds.minW!=null) && (bounds.maxW<bounds.minW))
				bounds.maxW = bounds.minW;
			
			if ((bounds.maxH!=null) && (bounds.minH!=null) && (bounds.maxH<bounds.minH))
				bounds.maxH = bounds.minH;
		}
		return bounds;
	}
	
	public void clearBoundsRecursively() {
		bounds = null;
		for (Component child : children)
			child.clearBoundsRecursively();
	}
	
	
	//public abstract void resize(double[] vars, Component lastSibling);
	public void resize(double[] vars, Component lastSibling) {
		double left=(vars[bounds.xl]);
		double top=(vars[bounds.xt]);
		double right=(vars[bounds.xr]);
		double bottom=(vars[bounds.xb]);
		
		String anchor = null;
		if (this.callback!=null)
			anchor = this.callback.getAnchor(this.reference);
		
		if ("zero".equalsIgnoreCase(anchor)) {
			// do not adjust
		}
		else
		if ("sibling".equalsIgnoreCase(anchor)) {
			if (lastSibling != null) {
				if ((parent!=null) && (parent instanceof HorizontalBox || parent instanceof Row) && ((this instanceof Row)/* || (this instanceof HorizontalBox)*/)) {
					top=(vars[bounds.xt])-(vars[lastSibling.getComponentBounds().xb]);
					bottom=(vars[bounds.xb])-(vars[lastSibling.getComponentBounds().xb]);
					left=(vars[bounds.xl])-(vars[parent.getComponentBounds().xl]);								
					right=(vars[bounds.xr])-(vars[parent.getComponentBounds().xl]);					
				}
				else
				if ((parent!=null) && (parent instanceof VerticalBox || parent instanceof Column) && ((this instanceof Column)/* || (this instanceof VerticalBox)*/)) {
					left=(vars[bounds.xl])-(vars[lastSibling.getComponentBounds().xr]);
					right=(vars[bounds.xr])-(vars[lastSibling.getComponentBounds().xr]);
					top=(vars[bounds.xt])-(vars[parent.getComponentBounds().xt]);
					bottom=(vars[bounds.xb])-(vars[parent.getComponentBounds().xt]);					
				}
				else
				if ((parent!=null) && ((parent instanceof Row) || (parent instanceof HorizontalBox))) {
					left=(vars[bounds.xl])-(vars[lastSibling.getComponentBounds().xr]);
					right=(vars[bounds.xr])-(vars[lastSibling.getComponentBounds().xr]);
					top=(vars[bounds.xt])-(vars[parent.getComponentBounds().xt]);
					bottom=(vars[bounds.xb])-(vars[parent.getComponentBounds().xt]);
				}
				else
				if ((parent!=null) && ((parent instanceof Column) || (parent instanceof VerticalBox))) {			
					top=(vars[bounds.xt])-(vars[lastSibling.getComponentBounds().xb]);
					bottom=(vars[bounds.xb])-(vars[lastSibling.getComponentBounds().xb]);
					left=(vars[bounds.xl])-(vars[parent.getComponentBounds().xl]);								
					right=(vars[bounds.xr])-(vars[parent.getComponentBounds().xl]);
				}
				else
				/*if (this instanceof Stack)*/ {
					left=(vars[bounds.xl])-(vars[lastSibling.getComponentBounds().xr]);								
					top=(vars[bounds.xt])-(vars[lastSibling.getComponentBounds().xb]);
					right=(vars[bounds.xr])-(vars[lastSibling.getComponentBounds().xr]);
					bottom=(vars[bounds.xb])-(vars[lastSibling.getComponentBounds().xb]);									
				}
			}
			else
			if (parent != null) {
				//resize according to parent's rectangle
				left=(vars[bounds.xl])-(vars[parent.getComponentBounds().xl]);
				top=(vars[bounds.xt])-(vars[parent.getComponentBounds().xt]);
				right=(vars[bounds.xr])-(vars[parent.getComponentBounds().xl]);
				bottom=(vars[bounds.xb])-(vars[parent.getComponentBounds().xt]);
			}
			
		}
		else { // assume "parent"
			if (parent != null) {
				//resize according to parent's rectangle
				left=(vars[bounds.xl])-(vars[parent.getComponentBounds().xl]);
				top=(vars[bounds.xt])-(vars[parent.getComponentBounds().xt]);
				right=(vars[bounds.xr])-(vars[parent.getComponentBounds().xl]);
				bottom=(vars[bounds.xb])-(vars[parent.getComponentBounds().xt]);
			}
		}
		
		if ((reference!=0) && (this.callback!=null))
			this.callback.layout(reference, (int)Math.round(left),(int)Math.round(top),(int)Math.round(right-left),(int)Math.round(bottom-top));
				
		Component sibling = null;
		//resize children
		for (Component child:getChildren()) {
			child.resize(vars, sibling);
			sibling = child;
		}
	}
	
	
	public void writeConstraints(ExtendedQuadraticOptimizer eqo) {
		bounds.writeConstraints(eqo);
	}
	
	public void writeGravity(ExtendedQuadraticOptimizer eqo,double coeff)
	{			
	}
	
	public void unload(boolean childrenOnly) {
		for (Component c : children)
			c.unload(false); // recurse for children; force child unload
		
		if (!childrenOnly) {// unload the root only if asked
			if ((this.callback!=null) && (this.reference!=0))
				this.callback.destroy(this.reference);
		}
	}
	
	public Component find(long reference) {
		if (reference == this.reference)
			return this;
		else
			if (children != null) {
				for (Component c : children) {
					Component found = c.find(reference);
					if (found!=null)
						return found;
				}
				return null;
			}
			else
				return null;
	}
}