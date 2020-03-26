package lv.lumii.dialoglayout.components.utils;

import java.util.Map;

import lv.lumii.dialoglayout.IMCSDialogLayout;
import lv.lumii.dialoglayout.components.Component;
import lv.lumii.layoutengine.funcmin.ExtendedQuadraticOptimizer;

public class Layout {
	//constants for quadratic optimization
	public static final double MAXWIDTH=9999;
	public static final double MAXHEIGHT=9999;
	public static final double PREFERREDWEIGHT=10000;
	public static final double ALIGNEMENTWEIGHT=100000.0;
	//public static final double GRAVITY=PREFERREDWEIGHT*100;/**100000*/;
	//public static final double GRAVITY_STEP=1.0;//0.01;
	public static final double INITIAL_GRAVITY_WEIGHT = 10000000000.0;
	public static final double RELATIVEPREFERREDWEIGHT=1;
	
	// RELATIVE_SIZE_WEIGHT = 1.0;
	public static final double INFINITY=100000000;
	
	public static int updateIndicesFor(int i, Component c){
		//update components indices
		i=c.getComponentBounds().updateIndicesFrom(i);
		//for every child update child's indices
		if (c.getChildren()!=null) {
			for (Component child:c.getChildren())
				i=updateIndicesFor(i,child);
		}
		return i;
	}
	
	public static int updateIndicesFor(int i, Component c, double[] lastSolution, Map<Integer, Double> newInitialValues){
		//update components indices
		i=c.getComponentBounds().updateIndicesFrom(i, lastSolution, newInitialValues);
		if (c.getChildren()!=null) {
			for (Component child:c.getChildren())
				i=updateIndicesFor(i,child);
		}
		return i;
	}
}
