package lv.lumii.dialoglayout.components;

import lv.lumii.dialoglayout.IMCSDialogLayout;
import lv.lumii.layoutengine.funcmin.ExtendedQuadraticOptimizer;
import lv.lumii.dialoglayout.components.utils.ComponentBounds;
import lv.lumii.dialoglayout.components.utils.Layout;

public class Stack extends Component {

	public void writeGravity(ExtendedQuadraticOptimizer eqo,double coeff) {
		//get children count
		int comp_count=children.size();
		
		//??? double newCoeff = coeff/2; // assume comp_count==1, since all child components are with equal gravity
		double newCoeff = coeff/2/(comp_count+1);
		
		//for every child
		for (Component child:getChildren()) {
			ComponentBounds childBounds=child.getComponentBounds();
			
			//add gravity to form's left and right borders
			eqo.addLinearDifference(bounds.xl, childBounds.xl, newCoeff);
			eqo.addLinearDifference(childBounds.xr, bounds.xr, newCoeff);
			
			//add child's inner gravity, update the gravity coefficient so that it wouldn't affect the form itself
			child.writeGravity(eqo, newCoeff);
			
			eqo.addLinearDifference(bounds.xt, child.getComponentBounds().xt, newCoeff);
			eqo.addLinearDifference(child.getComponentBounds().xb, bounds.xb, newCoeff);
		}
		
	}
	
	public void writeConstraints(ExtendedQuadraticOptimizer eqo) {
		//consider component's size information
		bounds.writeConstraints(eqo);
				
		//consider information about all inner components
		for (Component child:getChildren()) {
			//consider child's size information
			child.writeConstraints(eqo);
			
			ComponentBounds childBounds=child.getComponentBounds();
			
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
			
		}
		

		Component prevChild = null;
		for (Component child:getChildren()) {
			if (child.getClass()!=Row.class && bounds.verAlign.equals("TOP"))
				eqo.addEquality(bounds.xt, child.getComponentBounds().xt,bounds.topP+child.getComponentBounds().topM);
			else
				eqo.addInequality(bounds.xt, child.getComponentBounds().xt,bounds.topP+child.getComponentBounds().topM);
			
			if (child.getClass()!=Row.class && bounds.verAlign.equals("BOTTOM"))
				eqo.addEquality(child.getComponentBounds().xb,bounds.xb,bounds.bottomP+child.getComponentBounds().bottomM);
			else
				eqo.addInequality(child.getComponentBounds().xb,bounds.xb,bounds.bottomP+child.getComponentBounds().bottomM);
			
			if (bounds.verAlign.equals("CENTER"))
				eqo.addDoubleMeanDifference(bounds.xt, bounds.xb, child.getComponentBounds().xt, child.getComponentBounds().xb, Layout.ALIGNEMENTWEIGHT);
			
			if (prevChild != null) {
				// alignment of children...
				ComponentBounds childBounds=child.getComponentBounds();
				ComponentBounds pChildBounds = prevChild.getComponentBounds();
				eqo.addEquality(childBounds.xl, pChildBounds.xl, 0);
				eqo.addEquality(childBounds.xr, pChildBounds.xr, 0);
				eqo.addEquality(childBounds.xt, pChildBounds.xt, 0);
				eqo.addEquality(childBounds.xb, pChildBounds.xb, 0);				
			}
			prevChild = child;
		}
	}

	public Stack(IMCSDialogLayout.ComponentCallback _callback, long _reference, Component _parent) {
		super(_callback, _reference, _parent);		
	}

}
