package lv.lumii.dialoglayout.components;

import lv.lumii.layoutengine.funcmin.ExtendedQuadraticOptimizer;

import java.util.ArrayList;
import java.util.Arrays;

import lv.lumii.dialoglayout.IMCSDialogLayout;
import lv.lumii.dialoglayout.components.utils.ComponentBounds;
import lv.lumii.dialoglayout.components.utils.Layout;

public class Row extends Component {
	
	protected Row prevRow = null;
		
	public void writeGravity(ExtendedQuadraticOptimizer eqo, double coeff) {
		//get children count
		int comp_count=getChildren().length;
		//stores references to first and last child
		Component first=null;
		Component last=null;
		
		double newCoeff = coeff/2/(comp_count+1);
		
		//for every child
		for (Component child:getChildren()) {
			ComponentBounds childBounds=child.getComponentBounds();
			
			//add gravity to form's top and bottom borders
			eqo.addLinearDifference(bounds.xt, childBounds.xt, newCoeff);
			eqo.addLinearDifference(childBounds.xb, bounds.xb, newCoeff);
			
			//add child's inner gravity, update the gravity coefficient so that it wouldn't affect the form itself
			child.writeGravity(eqo, newCoeff);
			
			//update first and last child's references
			if (first==null)
				first=child;
			last=child;
		}
		
		//add gravity to form's left and right borders
		if (first!=null) {
			eqo.addReducibleInequality(bounds.xl, first.getComponentBounds().xl, 0, -Layout.INFINITY);
			//eqo.addLinearDifference(bounds.xl, first.getComponentBounds().xl, coeff);
			
			/*
			 * eqo.addQuadraticDifference(xr, xl, Layout.PREFERREDWEIGHT);
			eqo.addLinearDifference(xl, xr, -2*preW*Layout.PREFERREDWEIGHT);
			 */
		}
		if (last!=null) {
			eqo.addEquality(last.getComponentBounds().xr, bounds.xr, 0);
			eqo.addReducibleInequality(last.getComponentBounds().xr, bounds.xr, 0, -Layout.INFINITY);
			//eqo.addLinearDifference(last.getComponentBounds().xr, bounds.xr, coeff);
		}
	}
	
	public void writeConstraints(ExtendedQuadraticOptimizer eqo) {
		//consider component's size information
		bounds.writeConstraints(eqo);
		
		Component prevChild=null;
		Component firstChild=null;
		Component lastChild=null;
		
		Component[] prevRowChildren = null;
		if (prevRow != null)
			prevRowChildren = prevRow.getChildren();
		if (prevRowChildren == null)
			prevRowChildren = new Component[]{};
		int prevRowI=0;
		
		//for every child
		for (Component child:getChildren()) {
			//consider child's size information
			child.writeConstraints(eqo);
			
			ComponentBounds childBounds=child.getComponentBounds();
			
			// equalizing rows...
			if (prevRowI<prevRowChildren.length) {
				Component pChild = prevRowChildren[prevRowI];
				ComponentBounds pChildBounds = pChild.getComponentBounds();
				
				eqo.addReducibleInequality(childBounds.xl, pChildBounds.xl, 0, -Layout.MAXWIDTH);
				eqo.addReducibleInequality(pChildBounds.xl, childBounds.xl, 0, -Layout.MAXWIDTH);
				eqo.addReducibleInequality(pChildBounds.xl, childBounds.xr, 0, -Layout.MAXWIDTH);
				eqo.addReducibleInequality(childBounds.xl, pChildBounds.xr, 0, -Layout.MAXWIDTH);
//				eqo.addEquality(childBounds.xl, pChildBounds.xl, 0);
//				eqo.addEquality(childBounds.xr, pChildBounds.xr, 0);
				
				//eqo.addMeanDifference(childBounds.xl, pChildBounds.xl, 0, Layout.ALIGNEMENTWEIGHT);
				//eqo.addMeanDifference(childBounds.xr, pChildBounds.xr, 0, Layout.ALIGNEMENTWEIGHT);
				prevRowI++;
			}
			
			if (prevChild!=null) {
				//consider horizontal spacing
				int horSpace=prevChild.getComponentBounds().rightM+bounds.horSpace+childBounds.leftM;
				//eqo.addInequality(prevChild.getComponentBounds().xr, childBounds.xl, horSpace);
				eqo.addEquality(prevChild.getComponentBounds().xr, childBounds.xl, horSpace);
			} else
				firstChild=child;
			
			//consider vertical padding and vertical alignment
			if (child.getClass()!=Row.class && bounds.verAlign.equals("TOP"))
				eqo.addEquality(bounds.xt, childBounds.xt, bounds.topP+childBounds.topM);
			else
				eqo.addInequality(bounds.xt, childBounds.xt, bounds.topP+childBounds.topM);
			
			if (child.getClass()!=Row.class && bounds.verAlign.equals("BOTTOM"))
				eqo.addEquality(childBounds.xb, bounds.xb, bounds.bottomP+childBounds.bottomM);
			else
				eqo.addInequality(childBounds.xb, bounds.xb, bounds.bottomP+childBounds.bottomM);
			
			if (bounds.verAlign.equals("CENTER"))
				eqo.addDoubleMeanDifference(bounds.xt, bounds.xb, childBounds.xt, childBounds.xb, Layout.ALIGNEMENTWEIGHT);
			
			//update last and previous child
			lastChild=prevChild=child;
		}
			
		if (firstChild!=null) {
			//consider horizontal padding and horizontal alignment
			if (firstChild.getClass()!=Column.class && bounds.horAlign.equals("LEFT"))
				eqo.addEquality(bounds.xl, firstChild.getComponentBounds().xl,bounds.leftP+firstChild.getComponentBounds().leftM);
			else
				eqo.addInequality(bounds.xl, firstChild.getComponentBounds().xl,bounds.leftP+firstChild.getComponentBounds().leftM);
			
			if (firstChild.getClass()!=Column.class && bounds.horAlign.equals("RIGHT"))
				eqo.addEquality(prevChild.getComponentBounds().xr,bounds.xr,bounds.rightP+prevChild.getComponentBounds().rightM);
			else
				eqo.addInequality(prevChild.getComponentBounds().xr,bounds.xr,bounds.rightP+prevChild.getComponentBounds().rightM);
			
			if (bounds.horAlign.equals("CENTER"))
				eqo.addDoubleMeanDifference(bounds.xl, bounds.xr, firstChild.getComponentBounds().xl, lastChild.getComponentBounds().xr, Layout.ALIGNEMENTWEIGHT);
			else
				eqo.addDoubleMeanDifference(bounds.xl, bounds.xr, firstChild.getComponentBounds().xl, lastChild.getComponentBounds().xr, Layout.ALIGNEMENTWEIGHT);
		}
		
		if (prevRow != null) {
			if (parent!=null)
				eqo.addInequality(prevRow.getComponentBounds().xb, this.getComponentBounds().xt, parent.getComponentBounds().verSpace+prevRow.getComponentBounds().bottomM+this.getComponentBounds().topM);			
			else
				eqo.addInequality(prevRow.getComponentBounds().xb, this.getComponentBounds().xt, 0);			
		}
	}
	
	protected void updateChildrenSize() {		
		if (parent!=null)
			while (this.children.size() < parent.maxRowChildren) {
				// adding fake child...
				this.children.add( new LeafComponent(null, 0, this) );
			}
		if (this.prevRow!=null)
			this.prevRow.updateChildrenSize();
	}

	public Row(IMCSDialogLayout.ComponentCallback _callback, long _reference, Component _parent) {
		super(_callback, _reference, _parent);
		if (_parent!=null) {
			this.prevRow = _parent.lastRow;
			_parent.lastRow = this;
			if (this.children.size() > _parent.maxRowChildren)
				_parent.maxRowChildren = this.children.size();
			this.updateChildrenSize();
		}
	}
	
	public void reinitialize() {
		super.reinitialize();
		if (parent!=null) {
			if (this.children.size() > parent.maxRowChildren)
				parent.maxRowChildren = this.children.size();
			this.updateChildrenSize();
		}		
	}
	
	public void destroyChildrenRecursively(IMCSDialogLayout.ComponentCallback _callback) {
		super.destroyChildrenRecursively(_callback);
		this.prevRow = null; // since the previous row could be deleted	
	}
}
