package lv.lumii.dialoglayout.components;

import lv.lumii.dialoglayout.IMCSDialogLayout;
import lv.lumii.layoutengine.funcmin.ExtendedQuadraticOptimizer;
import lv.lumii.dialoglayout.components.utils.ComponentBounds;
import lv.lumii.dialoglayout.components.utils.Layout;

public class VerticalBox extends Component {

	public void writeGravity(ExtendedQuadraticOptimizer eqo,double coeff) {
//		IMCSDialogLayout.consoleLog("VBOX GRAVITY "+this.reference+" "+coeff);
		
		//get children count
		int comp_count=children.size();
		//stores references to first and last child
		Component first=null;
		Component last=null;
		
		double newCoeff = coeff/2/(comp_count+1);
		
		//for every child
		for (Component child:getChildren()) {
			ComponentBounds childBounds=child.getComponentBounds();
			
			//add gravity to form's left and right borders
			
			eqo.addLinearDifference(bounds.xl, childBounds.xl, newCoeff);
			eqo.addLinearDifference(childBounds.xr, bounds.xr, newCoeff);
			
			//eqo.addQuadraticDifference(bounds.xl, childBounds.xl, Math.sqrt(coeff)*2);
			//eqo.addQuadraticDifference(childBounds.xr, bounds.xr, Math.sqrt(coeff)*2);
			
			//add gravity to form's left and right borders
			//eqo.addLinearDifference(bounds.xl, childBounds.xl, newCoeff);
			//eqo.addLinearDifference(childBounds.xr, bounds.xr, newCoeff);
			
			//eqo.addEquality(bounds.xl, childBounds.xl, 0);
			//eqo.addEquality(childBounds.xr, bounds.xr, 0);
					
			//eqo.addReducibleInequality(bounds.xl, childBounds.xl, 0, -Layout.INFINITY);
			//eqo.addReducibleInequality(childBounds.xr, bounds.xr, 0, -Layout.INFINITY);
//			eqo.addQuadraticDifference(bounds.xl, childBounds.xl, newCoeff);
//			eqo.addQuadraticDifference(childBounds.xr, bounds.xr, newCoeff);
			
			eqo.addDoubleMeanDifference(bounds.xl, bounds.xr, childBounds.xl, childBounds.xr, Layout.ALIGNEMENTWEIGHT);
			
			
//			IMCSDialogLayout.consoleLog("VBOX GRAVITY "+this.reference+"<->"+child.reference+" "+coeff+" "+newCoeff);
			
			//add child's inner gravity, update the gravity coefficient so that it wouldn't affect the form itself
			child.writeGravity(eqo, newCoeff);
			
			//update first and last child's references
			if (first==null)
				first=child;
			last=child;
		}
		
		//add gravity to form's top and bottom borders
		if (first!=null) {
			//eqo.addReducibleInequality(bounds.xt, first.getComponentBounds().xt, 0, -Layout.INFINITY);
			//eqo.addEquality(first.getComponentBounds().xt, bounds.xt, 0);
			eqo.addLinearDifference(bounds.xt, first.getComponentBounds().xt, coeff);
		}
		if (last!=null) {
			//eqo.addReducibleInequality(last.getComponentBounds().xb, bounds.xb, 0, -Layout.INFINITY);
			//eqo.addEquality(bounds.xb, last.getComponentBounds().xb, 0);
			eqo.addLinearDifference(last.getComponentBounds().xb, bounds.xb, coeff);
		}
	}
	
	public void writeConstraints(ExtendedQuadraticOptimizer eqo) {
		//consider component's size information
		bounds.writeConstraints(eqo);
		
		
		if (this.parent == null) { // if this is a form...
			//consider form's insets
			eqo.addConstantInequality(bounds.xl, 0); //getInsets().left);
			eqo.addConstantInequality(bounds.xt, 0); //getInsets().top);
				// currently we try to locate the form at the position (0, 0)
				// all other positions are relative, thus, the form can be freely moved as needed
		}
		
		Component prevChild=null;
		Component firstChild=null;
		Component lastChild=null;
		
		//consider information about all inner components
		for (Component child:getChildren()) {
			//consider child's size information
			child.writeConstraints(eqo);
			
			ComponentBounds childBounds=child.getComponentBounds();
			
			if (prevChild!=null) {
				//consider vertical spacing
				int verSpace=prevChild.getComponentBounds().bottomM+bounds.verSpace+childBounds.topM;
				eqo.addEquality(prevChild.getComponentBounds().xb, childBounds.xt, verSpace);
			} else
				firstChild=child;
			
			//consider horizontal padding and horizontal alignment 
			if (child.getClass()!=Column.class && bounds.horAlign.equals("LEFT"))
				eqo.addEquality(bounds.xl, childBounds.xl, bounds.leftP+childBounds.leftM);
			else
				eqo.addInequality(bounds.xl, childBounds.xl, bounds.leftP+childBounds.leftM);
			
			if (child.getClass()!=Column.class && bounds.horAlign.equals("RIGHT"))
				eqo.addEquality(childBounds.xr, bounds.xr, bounds.rightP+childBounds.rightM);
			else
				eqo.addInequality(childBounds.xr, bounds.xr, bounds.rightP+childBounds.rightM);
			
			if (bounds.horAlign.equals("CENTER"))
				eqo.addDoubleMeanDifference(bounds.xl, bounds.xr, childBounds.xl, childBounds.xr, Layout.ALIGNEMENTWEIGHT);
			
			//update last and previous child
			lastChild=prevChild=child;
		}
		
		if (firstChild!=null) {
			//consider vertical padding and vertical alignment 
			if (firstChild.getClass()!=Row.class && bounds.verAlign.equals("TOP"))
				eqo.addEquality(bounds.xt, firstChild.getComponentBounds().xt,bounds.topP+firstChild.getComponentBounds().topM);
			else
				eqo.addInequality(bounds.xt, firstChild.getComponentBounds().xt,bounds.topP+firstChild.getComponentBounds().topM);
			
			if (firstChild.getClass()!=Row.class && bounds.verAlign.equals("BOTTOM"))
				eqo.addEquality(lastChild.getComponentBounds().xb,bounds.xb,bounds.bottomP+lastChild.getComponentBounds().bottomM);
			else
				eqo.addInequality(lastChild.getComponentBounds().xb,bounds.xb,bounds.bottomP+lastChild.getComponentBounds().bottomM);
			
			if (bounds.verAlign.equals("CENTER"))
				eqo.addDoubleMeanDifference(bounds.xt, bounds.xb, firstChild.getComponentBounds().xt, lastChild.getComponentBounds().xb, Layout.ALIGNEMENTWEIGHT);
		}
		
		if (this.parent == null) // if this is a form...
			writeGravity(eqo,Layout.INITIAL_GRAVITY_WEIGHT);
	}

	public VerticalBox(IMCSDialogLayout.ComponentCallback _callback, long _reference, Component _parent) {
		super(_callback, _reference, _parent);		
	}

}
