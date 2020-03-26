/*
*
* Copyright (c) 2013-2015 Institute of Mathematics and Computer Science, University of Latvia (IMCS UL). 
*
* This file is part of layoutengine
*
* You can redistribute it and/or modify it under the terms of the GNU General Public License 
* as published by the Free Software Foundation, either version 2 of the License,
* or (at your option) any later version.
*
* This file is also subject to the "Classpath" exception as mentioned in
* the COPYING file that accompanied this code.
*
* You should have received a copy of the GNU General Public License along with layoutengine. If not, see http://www.gnu.org/licenses/.
*
*/

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lv.lumii.layoutengine.funcmin;

import java.util.Arrays;

import lv.lumii.dialoglayout.IMCSDialogLayout;

/**
 * Extends the {@link QuadraticOptimizer} class to allow for some variables to
 * have a fixed value and for some inequalities to be unsatisfiable, as long as
 * satisfiable fallback is provided. Implemented as a separate class as it is
 * slightly slower due to the additional terms used.
 *
 * @author k
 */
public class ExtendedQuadraticOptimizer extends QuadraticOptimizer {

	private final static boolean LOG_DEBUG = false; 
    //<editor-fold defaultstate="collapsed" desc="attributes">
    /**
     * The id of the fictional element in relation to which the other elements
     * will remain fixed throughout the normalization.
     */
    int zeroId;
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="constructors">
    /**
     * The constructor creates a zero function with no constraints. Creates the
     * fictional variable used to affix other variables.
     *
     * @param variableCount the number of variables
     */
    public ExtendedQuadraticOptimizer(int variableCount) {
        super(variableCount + 1);
        zeroId = variableCount;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="methods for function construction">
    @Override
    public void addQuadraticConstantDifference(
            int i,
            double value,
            double weight) {
        addQuadraticDifference(zeroId, i, weight);
        addLinearDifference(i, zeroId, 2 * weight * value);
    }

    /**
     * Add a new term in the form of
     * <code> weight*((x<sub>i</sub>+x<sub>j</sub>)/2-value)<sup>2</sup></code>.
     * Use this method to minimize the drift of the mass center of two objects.
     *
     * @param i the first variable of the term
     * @param j the second variable of the term
     * @param value the value to keep the mass center of the two objects close
     * to
     * @param weight the weight of this term
     */
    @Override
    public void addMeanDifference(
            int i,
            int j,
            double value,
            double weight) {

        double d = weight * value;

        addLinearTerm(zeroId, 2 * d);
        addLinearTerm(i, -d);
        addLinearTerm(j, -d);
        addQuadraticTerm(zeroId, weight);
        addQuadraticTerm(i, 0.25 * weight);
        addQuadraticTerm(j, 0.25 * weight);
        addCombinedTerm(zeroId, i, -weight);
        addCombinedTerm(zeroId, j, -weight);
        addCombinedTerm(i, j, 0.5 * weight);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="methods for constraint manipulation">
    /**
     * Add an inequality constraint in the form of
     * <code>x<sub>i</sub>ā‰�value</code>.
     *
     * @param i the variable of the inequality
     * @param value the constant of the inequality
     */
    public void addConstantInequality(int i, double value) {
        addInequality(zeroId, i, value);
    }

    /**
     * Add an inequality constraint in the form of
     * <code>x<sub>i</sub>ā‰¤value</code>.
     *
     * @param i the variable of the inequality
     * @param value the constant of the inequality
     */
    public void addConstantReverseInequality(int i, double value) {
        addInequality(i, zeroId, -value);
    }

    // This method adds the following equality
    // X[variable] = value
    /**
     * Adds the following equality constraint:
     * <code>x<sub>i</sub>=value</code>.
     *
     * @param i the variable of the equality
     * @param value the constant of the equality
     */
    public void addConstantEquality(int i, double value) {
        addEquality(zeroId, i, value);
    }

    /**
     * Adds an inequality constraint in the form of
     * <code>x<sub>j</sub>-x<sub>i</sub>ā‰�distanceā‰�minDistance</code>. If the set
     * of constraints is unsatisfiable with {@code distance}, they are relaxed
     * towards {@code minDistance} until they are satisfiable.
     *
     * @param i the second variable of the inequality
     * @param j the first variable of the inequality
     * @param distance the minimum distance between the two variables
     * @param minDistance the minimum distance between the two variables if {@code distance}
     * is not possible.
     */
    public void addReducibleInequality(
            int i,
            int j,
            double distance,
            double minDistance) {
        inequalities.add(new CombinedTerm(i, j, distance, minDistance));
    }
    //</editor-fold>

    @Override
    public double[] performOptimization() {
    	return performOptimization(false);
    }
    
    public double[] performOptimization(boolean isDialog) {
    	
        CycleReducer solver = new CycleReducer(variableCount, this.epsilon);

        if (LOG_DEBUG) IMCSDialogLayout.consoleLog("CycleReducer solver = new CycleReducer("+variableCount+", "+this.epsilon+");");


        for (CombinedTerm inequality : inequalities) {
        	if (!isDialog || (inequality.minWeight>=0)) {
        		solver.addConstraint(inequality.secondObject, inequality.firstObject, inequality.minWeight, inequality.weight);
        		if (LOG_DEBUG) IMCSDialogLayout.consoleLog("solver.addConstraint("+inequality.secondObject+", "+inequality.firstObject+", "+inequality.minWeight+", "+inequality.weight+");");
        	}
        }


        for (CombinedTerm equality : equalities) {
            solver.addConstraint(equality.secondObject, equality.firstObject, equality.weight, equality.weight);
            solver.addConstraint(equality.firstObject, equality.secondObject, -equality.weight, -equality.weight);
            if (LOG_DEBUG) IMCSDialogLayout.consoleLog("solver.addConstraint("+equality.secondObject+", "+equality.firstObject+", "+equality.weight+", "+equality.weight+");//2a");
            if (LOG_DEBUG) IMCSDialogLayout.consoleLog("solver.addConstraint("+equality.firstObject+", "+equality.secondObject+", -"+equality.weight+", -"+equality.weight+");//2b");
        }

//        IMCSDialogLayout.consoleLog("double[] variables = solver.getSolution();");
        
        IMCSDialogLayout.printTime("imcs_de step 4");
        variables = solver.getSolution();
        IMCSDialogLayout.printTime("imcs_de step 5");

        
        if (variables == null) {
        	IMCSDialogLayout.consoleLog("Cycle reducer found no solution.");
            //throw new IllegalArgumentException("Cycle Reducer found no solution.");
        	return null;
        }

        /*
         * Set inequality actual length.
         */
        for (CombinedTerm inequality : inequalities) {
            double len = variables[inequality.secondObject] - variables[inequality.firstObject];

            if (len > inequality.weight) {
                len = inequality.weight;
            }

            inequality.weight = len;
        }


        //DEBUG
//	double eps = input->getPrecision()/2;
//
//	for(int ineqIter=0;ineqIter < inequalities->count;ineqIter++)
//	{
//		CombinedTerm * term = inequalities->itemPointer(ineqIter);
//
//		if (-initialX[term->firstObject] + initialX[term->secondObject] <
//			term->weight - eps)
//		{
//				throw "Funcmin: there is no feasible starting position";
//		}
//	}
//
//	for (int eqIter = 0;eqIter<equalities->count;eqIter++)
//	{
//		CombinedTerm * term = equalities->itemPointer(eqIter);
//
//		if (fabs(initialX[term->firstObject] -
//			initialX[term->secondObject] + term->weight) > eps)
//		{
//			throw "Funcmin: there is no feasible starting position";
//		}
//	}

        //END DEBUG
        

        isInputCorrect = true;

        IMCSDialogLayout.printTime("imcs_de step 6");

        double result[] = super.performOptimization();
        IMCSDialogLayout.printTime("imcs_de step 7");

        // the zero variable is the last one
        double zeroX = result[variableCount - 1];

        for (int i = 0; i < variableCount; i++) {
            result[i] -= zeroX;
        }

        return Arrays.copyOf(result, variableCount - 1);
    }
    
    public static void main(String args[]) { // test
    	CycleReducer solver = new CycleReducer(61, 0.1);
    	solver.addConstraint(2, 0, 116, 116);
    	//...

    	double[] variables = solver.getSolution();    	

        if (variables == null) {
            throw new IllegalArgumentException("Cycle Reducer found no solution.");
        }
        
        System.out.print("variables =");
        for (int i=0; i<variables.length; i++) {
        	System.out.print(" "+variables[i]);
        }
        System.out.println();
    }
}
