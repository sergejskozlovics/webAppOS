package lv.lumii.dialoglayout.components;

import lv.lumii.layoutengine.funcmin.ExtendedQuadraticOptimizer;
import lv.lumii.dialoglayout.IMCSDialogLayout;
import lv.lumii.dialoglayout.components.utils.ComponentBounds;
import lv.lumii.dialoglayout.components.utils.Layout;

public class HorizontalBox extends Component {

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
			//eqo.addReducibleInequality(bounds.xl, first.getComponentBounds().xl, 0, -Layout.INFINITY);
			eqo.addEquality(first.getComponentBounds().xl, bounds.xl, 0);
			//eqo.addLinearDifference(bounds.xl, first.getComponentBounds().xl, coeff);
		}
		if (last!=null) {
			//eqo.addReducibleInequality(last.getComponentBounds().xr, bounds.xr, 0, -Layout.INFINITY);
			eqo.addEquality(bounds.xr, last.getComponentBounds().xr, 0);
			//eqo.addLinearDifference(last.getComponentBounds().xr, bounds.xr, coeff);
		}
	}
	
	public void writeConstraints(ExtendedQuadraticOptimizer eqo) {
		//consider component's size information
		bounds.writeConstraints(eqo);
		
		Component prevChild=null;
		Component firstChild=null;
		Component lastChild=null;
		
		//for every child
		for (Component child:getChildren()) {
			//consider child's size information
			child.writeConstraints(eqo);
			
			ComponentBounds childBounds=child.getComponentBounds();
			
			
			if (prevChild!=null) {
				//consider horizontal spacing
				int horSpace=prevChild.getComponentBounds().rightM+bounds.horSpace+childBounds.leftM;
				if (!(child instanceof Row) && !(child instanceof HorizontalBox))
					eqo.addEquality(prevChild.getComponentBounds().xr, childBounds.xl, horSpace);
				else {
					eqo.addEquality(this.getComponentBounds().xl, childBounds.xl, this.getComponentBounds().leftP+childBounds.leftM);
					eqo.addEquality(childBounds.xr, this.getComponentBounds().xr, this.getComponentBounds().rightP+childBounds.rightM);
				}
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
			//else
				//eqo.addInequality(bounds.xl, firstChild.getComponentBounds().xl,bounds.leftP+firstChild.getComponentBounds().leftM);
			
			if (firstChild.getClass()!=Column.class && bounds.horAlign.equals("RIGHT"))
				eqo.addEquality(prevChild.getComponentBounds().xr,bounds.xr,bounds.rightP+prevChild.getComponentBounds().rightM);
			//else
				//eqo.addInequality(prevChild.getComponentBounds().xr,bounds.xr,bounds.rightP+prevChild.getComponentBounds().rightM);
			
			if (bounds.horAlign.equals("CENTER"))
				eqo.addDoubleMeanDifference(bounds.xl, bounds.xr, firstChild.getComponentBounds().xl, lastChild.getComponentBounds().xr, Layout.ALIGNEMENTWEIGHT*1000);
			else
				eqo.addDoubleMeanDifference(bounds.xl, bounds.xr, firstChild.getComponentBounds().xl, lastChild.getComponentBounds().xr, Layout.ALIGNEMENTWEIGHT);
		}
	}

	public HorizontalBox(IMCSDialogLayout.ComponentCallback _callback, long _reference, Component _parent) {
		super(_callback, _reference, _parent);		
	}
}
