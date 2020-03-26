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

import java.util.ArrayList;
import java.util.Arrays;

import lv.lumii.dialoglayout.IMCSDialogLayout;


/**
 * Contains methods to create the function and constraints used in Funcmin, as well as call Funcmin
 * and return the results.
 *
 * @author k
 */
public class QuadraticOptimizer {

    //<editor-fold defaultstate="collapsed" desc="attributes">
    /**
     * Coefficients for the second order terms in the function.
     */
    double quadraticCoefficients[];
    /**
     * Coefficients for the first order terms in the function.
     */
    double linearCoefficients[];
    /**
     * Starting values of the function variables.
     */
    double variables[];
    /**
     * The combined terms in the function to minimize. {@code firstObject} and {@code secondObject}
     * are the indices of the two variables, {@code weight} is the coefficient of the term.
     */
    ArrayList<CombinedTerm> combinedTerms;
    /**
     * The constraints of the function to minimize, in the form of <br>
     * x<sub>{@code secondObject}</sub>-x<sub>{@code firstObject}</sub>ā‰�{@code weight}.
     */
    ArrayList<CombinedTerm> inequalities;
    /**
     * Equality constraints of the function, in the form of <br>
     * x<sub>{@code secondObject}</sub>-x<sub>{@code firstObject}</sub>={@code weight}.
     */
    ArrayList<CombinedTerm> equalities;
    /**
     * The number of variables in the function to minimize.
     */
    int variableCount;
    /**
     * The allowed deviation from both the constraints and the minimum possible value of the
     * function.
     */
    double epsilon;
    /**
     * Whether the current starting values of the variables satisfy the current constraints.
     */
    boolean isInputCorrect;
    /*
     * The default value of {@code epsilon}.
     */
    static double DEFAULT_EPSILON = 0.001;
    int equalityTree[];
    double equalityDistance[];
    //</editor-fold>

    /**
     * A class containing combined terms consisting of two variables and a constant. Used both for
     * storing actual combined terms as well as inequalities.
     */
    static class CombinedTerm {

        //<editor-fold defaultstate="collapsed" desc="attributes">
        /**
         * The first variable of the combined term.
         */
        int firstObject;
        /**
         * The second variable of the combined term.
         */
        int secondObject;
        /**
         * The constant. The Coefficient if this is used as a combined term, the minimum difference
         * if as an inequality, the exact difference if as an equality.
         */
        double weight;
        /**
         * The minimum weight, used in starting value optimization.
         */
        double minWeight;
        /**
         * The value of minWeight for terms that do not have a minimum weight. Currently those are
         * only combined terms in the function to minimize, and their minWeight is never read.
         */
        static double COMBINED_TERM_UNDEFINED = Double.NEGATIVE_INFINITY;
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="constructors">
        /**
         * Creates a new combined term.
         */
        CombinedTerm() {
            minWeight = COMBINED_TERM_UNDEFINED;
        }

        /**
         * Creates a new combined term with the given variables and constant.
         *
         * @param firstObject the first variable of the combined term
         * @param secondObject the second variable of the combined term
         * @param weight the constant of the combined term
         */
        CombinedTerm(int firstObject, int secondObject, double weight) {
            this.firstObject = firstObject;
            this.secondObject = secondObject;
            this.weight = weight;
            minWeight = COMBINED_TERM_UNDEFINED;
        }

        /**
         * Creates a new combined term with the given variables and constant.
         *
         * @param firstObject the first variable of the combined term
         * @param secondObject the second variable of the combined term
         * @param weight the coefficient for two variable terms, the constant for (in-)equalities
         * @param minWeight the minimum weight of the combined term
         */
        CombinedTerm(int firstObject, int secondObject, double weight, double minWeight) {
            this.firstObject = firstObject;
            this.secondObject = secondObject;
            this.weight = weight;
            this.minWeight = minWeight;
        }
        //</editor-fold>
    };

    //<editor-fold defaultstate="collapsed" desc="constructors">
    /**
     * The constructor creates a zero function with no constraints.
     *
     * @param variableCount the number of variables in the function this {@code QuadraticOptimizer}
     * will optimize
     */
    public QuadraticOptimizer(int variableCount) {
        this.variableCount = variableCount;
        quadraticCoefficients = new double[variableCount];
        linearCoefficients = new double[variableCount];
        variables = new double[variableCount];
        combinedTerms = new ArrayList<>();
        inequalities = new ArrayList<>();
        equalities = new ArrayList<>();
        epsilon = DEFAULT_EPSILON;
        isInputCorrect = false;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="preferred methods for function manipulation">
    /**
     * Sets the initial value of the i<sub>th</sub> variable to {@code value}.
     *
     * @param i the index of the variable whose value to set
     * @param value the new value of the variable
     */
    public void setVariable(int i, double value) {
        variables[i] = value;
    }

    /**
     * Add a new term in the form of
     * <code> weight*(x<sub>i</sub>-x<sub>j</sub>)<sup>2</sup></code>. Use this method to minimize
     * the distance between two objects.
     *
     * @param i the first variable of the term
     * @param j the second variable of the term
     * @param weight the weight of this term
     */
    public void addQuadraticDifference(int i, int j, double weight) {
        quadraticCoefficients[i] += weight;
        quadraticCoefficients[j] += weight;
        combinedTerms.add(new CombinedTerm(i, j, -2 * weight));
    }

    /**
     * Add a new term in the form of
     * <code> weight*(x<sub>i</sub>-value)<sup>2</sup></code>. Use this method to minimize the drift
     * of an object.
     *
     * @param i the variable of the term
     * @param value the value to keep the variable close to
     * @param weight the weight of this term
     */
    public void addQuadraticConstantDifference(
            int i,
            double value,
            double weight) {
        quadraticCoefficients[i] += weight;
        linearCoefficients[i] += -2 * weight * value;
    }

    /**
     * Add a new term in the form of
     * <code> weight*(x<sub>j</sub>-x<sub>i</sub>)</code>. Use this method to minimize the distance
     * between two objects. This term does not have a minimum, therefore it should be constrained.
     *
     * @param i the first variable of the term
     * @param j the second variable of the term
     * @param weight the weight of this term
     */
    public void addLinearDifference(
            int i,
            int j,
            double weight) {
        addLinearTerm(i, -weight);
        addLinearTerm(j, weight);
    }

    /**
     * Add a new term in the form of
     * <code> weight*((x<sub>i</sub>+x<sub>j</sub>)/2-value)<sup>2</sup></code>. Use this method to
     * minimize the drift of the mass center of two objects.
     *
     * @param i the first variable of the term
     * @param j the second variable of the term
     * @param value the value to keep the mass center of the two objects close to
     * @param weight the weight of this term
     */
    public void addMeanDifference(
            int i,
            int j,
            double value,
            double weight) {
        quadraticCoefficients[i] += weight * 0.25;
        quadraticCoefficients[j] += weight * 0.25;
        combinedTerms.add(new CombinedTerm(i, j, 0.5 * weight));
        linearCoefficients[i] -= value * weight;
        linearCoefficients[j] -= value * weight;
    }

    /**
     * Add a new term in the form of <br>
     * <code> weight*((x<sub>i1</sub>+x<sub>j1</sub>)/2-(x<sub>i2</sub>+x<sub>j2</sub>)/2)<sup>2</sup></code>.
     * Use this method to minimize the distance between the mass centers of two object pairs.
     *
     * @param i1 the first variable of the first pair
     * @param j1 the second variable of the first pair
     * @param i2 the first variable of the second pair
     * @param j2 the second variable of the second pair
     * @param weight the weight of this term
     */
    public void addDoubleMeanDifference(
            int i1,
            int j1,
            int i2,
            int j2,
            double weight) {
        weight *= 0.25;

        quadraticCoefficients[i1] += weight;
        quadraticCoefficients[j1] += weight;
        quadraticCoefficients[i2] += weight;
        quadraticCoefficients[j2] += weight;

        weight *= 2;

        combinedTerms.add(new CombinedTerm(i1, j1, weight));
        combinedTerms.add(new CombinedTerm(i2, j2, weight));

        combinedTerms.add(new CombinedTerm(i1, i2, -weight));
        combinedTerms.add(new CombinedTerm(i1, j2, -weight));
        combinedTerms.add(new CombinedTerm(j1, i2, -weight));
        combinedTerms.add(new CombinedTerm(j1, j2, -weight));
    }

    /**
     * Add a new term in the form of <br>
     * <code> weight*((x<sub>i1</sub>+x<sub>j1</sub>)/2-(x<sub>i2</sub>+x<sub>j2</sub>)/2-value)<sup>2</sup></code>.
     * Use this method to minimize the drift between the mass centers of two object pairs.
     *
     * @param i1 the first variable of the first pair
     * @param j1 the second variable of the first pair
     * @param i2 the first variable of the second pair
     * @param j2 the second variable of the second pair
     * @param value the value to keep the distance between the two mass centers at
     * @param weight the weight of this term
     */
    public void addDoubleMeanConstantDifference(
            int i1,
            int j1,
            int i2,
            int j2,
            double value,
            double weight) {

        linearCoefficients[i1] -= weight * value;
        linearCoefficients[j1] -= weight * value;
        linearCoefficients[i2] += weight * value;
        linearCoefficients[j2] += weight * value;

        weight *= 0.25;

        quadraticCoefficients[i1] += weight;
        quadraticCoefficients[j1] += weight;
        quadraticCoefficients[i2] += weight;
        quadraticCoefficients[j2] += weight;

        weight *= 2;


        combinedTerms.add(new CombinedTerm(i1, j1, weight));
        combinedTerms.add(new CombinedTerm(i2, j2, weight));

        combinedTerms.add(new CombinedTerm(i1, i2, -weight));
        combinedTerms.add(new CombinedTerm(i1, j2, -weight));
        combinedTerms.add(new CombinedTerm(j1, i2, -weight));
        combinedTerms.add(new CombinedTerm(j1, j2, -weight));
    }

    /**
     * Add a new term in the form of <br>
     * <code> weight*((x<sub>i</sub>+x<sub>j</sub>)/2-x<sub>k</sub>)<sup>2</sup></code>. Use this
     * method to minimize the distance between the mass center of the (i, j) object pair and object
     * k.
     *
     * @param i the first variable of the pair
     * @param j the second variable of the pair
     * @param k the solitary object
     * @param weight the weight of this term
     */
    public void addMeanVariableDifference(
            int i,
            int j,
            int k,
            double weight) {

        combinedTerms.add(new CombinedTerm(i, k, -weight));
        combinedTerms.add(new CombinedTerm(j, k, -weight));
        quadraticCoefficients[k] += weight;

        weight *= 0.5;

        combinedTerms.add(new CombinedTerm(i, j, weight));

        weight *= 0.5;

        quadraticCoefficients[i] += weight;
        quadraticCoefficients[j] += weight;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="methods for constraint manipulation">
    /**
     * Adds an inequality constraint in the form of
     * <code>x<sub>j</sub>-x<sub>i</sub>ā‰�distance</code>.
     *
     * @param i the second variable of the inequality
     * @param j the first variable of the inequality
     * @param distance the minimum distance between the two variables
     */
    public void addInequality(
            int i,
            int j,
            double distance) {
        inequalities.add(new CombinedTerm(i, j, distance, distance));
    }

    /**
     * Adds an equality constraint in the form of
     * <code>x<sub>j</sub>-x<sub>i</sub>=distance</code>.
     *
     * @param i the second variable of the equality
     * @param j the first variable of the equality
     * @param distance the distance between the two variables
     */
    public void addEquality(
            int i,
            int j,
            double distance) {
        equalities.add(new CombinedTerm(i, j, distance));
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="base function methods, use them only if you know what you are doing">
    /**
     * Adds the quadratic term
     * <code>coefficient*x<sub>i</sub><sup>2</sup> to the function.
     *
     * @param i the index of the variable whose quadratic term to alter
     * @param coefficient the coefficient of the added quadratic term
     */
    public void addQuadraticTerm(int i, double coefficient) {
        quadraticCoefficients[i] += coefficient;
    }

    /**
     * Adds the linear term
     * <code>coefficient*x<sub>i</sub> to the function.
     *
     * @param i the index of the variable whose linear term to alter
     * @param coefficient the coefficient of the added linear term
     */
    public void addLinearTerm(int i, double coefficient) {
        linearCoefficients[i] += coefficient;
    }

    // This method adds the given combined term to the function
    // corresponding to the specified objects.
    /**
     * Adds the combined term
     * <code>coefficient*x<sub>i</sub>*x<sub>j</sub> to the function.
     *
     * @param i the index of the first variable of the combined term
     * @param j the index of the second variable of the combined term
     * @param coefficient the coefficient of the added combined term
     */
    public void addCombinedTerm(
            int i,
            int j,
            double coefficient) {
        combinedTerms.add(new CombinedTerm(i, j, coefficient));
    }
    //</editor-fold>

    /**
     * Sets the precision of the minimization, used both for constraints and the function's value.
     *
     * @param epsilon the new precision
     */
    public void setEpsilon(double epsilon) {
        this.epsilon = epsilon;
    }

    /**
     * Performs the function minimization using the given terms and constraints.
     *
     * @return an array of the new values of the function variables.
     */
    public double[] performOptimization() {
        double resultVariables[] = new double[variableCount];
        System.arraycopy(variables, 0, resultVariables, 0, variableCount);

        int combinedTermIs[] = new int[combinedTerms.size()];
        int combinedTermJs[] = new int[combinedTerms.size()];
        double combinedTermCoefficients[] = new double[combinedTerms.size()];

        for (int i = 0; i < combinedTerms.size(); i++) {
            combinedTermIs[i] = combinedTerms.get(i).firstObject;
            combinedTermJs[i] = combinedTerms.get(i).secondObject;
            combinedTermCoefficients[i] = combinedTerms.get(i).weight;
        }

        int constraintIs[] = new int[inequalities.size()];
        int constraintJs[] = new int[inequalities.size()];
        double constraintMinimums[] = new double[inequalities.size()];

        for (int i = 0; i < inequalities.size(); i++) {
            constraintIs[i] = inequalities.get(i).firstObject;
            constraintJs[i] = inequalities.get(i).secondObject;
            constraintMinimums[i] = inequalities.get(i).weight;
        }

//        ArrayList<CombinedTerm> originalInequalities = (ArrayList<CombinedTerm>) inequalities.clone();
//        int[] originalConstraintIs = constraintIs.clone();
//        int[] originalConstraintJs = constraintJs.clone();
//        double[] originalMinimums = constraintMinimums.clone();

        //Equality tree
        int eN = equalities.size();
        if (eN > 0) {
            equalityTree = new int[variableCount];
            equalityDistance = new double[variableCount];

            createEqualityTree(equalities);

            int v, k;

            for (k = 0; k < variableCount; k++) {
                if ((v = equalityTree[k]) >= 0) {
                    quadraticCoefficients[v] += quadraticCoefficients[k];
                    linearCoefficients[v] += linearCoefficients[k] + 2 * quadraticCoefficients[k] * equalityDistance[k];
                    quadraticCoefficients[k] = 0;
                    linearCoefficients[k] = 0;
                }
            }

            for (k = 0; k < combinedTerms.size(); k++) {
                int y1 = combinedTermIs[k];
                int y2 = combinedTermJs[k];
                double d1 = 0;
                double d2 = 0;

                if (equalityTree[y1] >= 0) {
                    d1 = equalityDistance[y1];
                    y1 = equalityTree[y1];
                }

                if (equalityTree[y2] >= 0) {
                    d2 = equalityDistance[y2];
                    y2 = equalityTree[y2];
                }

                combinedTermIs[k] = y1;
                combinedTermJs[k] = y2;
                linearCoefficients[y1] += combinedTermCoefficients[k] * d2;
                linearCoefficients[y2] += combinedTermCoefficients[k] * d1;
            }

            int k1 = 0;

            for (k = 0; k < inequalities.size(); k++) {
                int v1 = ((v = equalityTree[constraintIs[k]]) >= 0) ? v : constraintIs[k];
                int v2 = ((v = equalityTree[constraintJs[k]]) >= 0) ? v : constraintJs[k];

                if (v1 != v2) {
                    constraintMinimums[k1] = constraintMinimums[k] + equalityDistance[constraintIs[k]] - equalityDistance[constraintJs[k]];
                    constraintIs[k1] = v1;
                    constraintJs[k1] = v2;
                    k1++;
                }
            }

            inequalities = new ArrayList<>(inequalities.subList(0, k1));
        }// eoEquality tree
//        if (useCycleReducer) {
//            boolean error = false; 
//            for (int i = 0; i < inequalities.size(); i++) {
//                double d = resultVariables[constraintJs[i]] - resultVariables[constraintIs[i]] - constraintMinimums[i];
//                assert resultVariables[constraintJs[i]] - resultVariables[constraintIs[i]] >= constraintMinimums[i] - epsilon : d;
//                if(resultVariables[constraintJs[i]] - resultVariables[constraintIs[i]] < constraintMinimums[i] - epsilon) {
//                    System.out.println(d);
//                    error = true;
//                }
//                if(d < 0)
//                    System.out.println(d);
//            }
//            System.out.println("-------\n");
//
//            CycleReducer cycleReducer = new CycleReducer(variableCount, epsilon);
//            for (int i = 0; i < inequalities.size(); i++) {
//                cycleReducer.addConstraint(constraintJs[i], constraintIs[i],
//                        constraintMinimums[i], constraintMinimums[i]);
//            }
//            resultVariables = cycleReducer.getSolution();
//
//            if(resultVariables == null) {
//                System.out.println(originalInequalities.size());
//                for(int i = 0; i < originalInequalities.size(); i++) {
//                    if(variables[originalConstraintJs[i]] - variables[originalConstraintIs[i]] <= originalMinimums[i] - epsilon) {
//                      //  System.out.println(variables[originalConstraintJs[i]] + " " + variables[originalConstraintIs[i]] + " " + originalMinimums[i] + ": " + (variables[originalConstraintJs[i]] - variables[originalConstraintIs[i]] + " | " + epsilon));
//                    }
//                }
//                System.out.flush();
//            }
//
//          if(error && resultVariables != null)
//              System.out.println("Solved");
//
//            isInputCorrect = true;
//        }
//        IMCSDialogLayout.consoleLog("before funcmin vars="+variableCount);
        
        Funcmin kernel = new Funcmin(variableCount, inequalities.size(), combinedTerms.size());
        kernel.Minimize(variableCount,
                null,
                resultVariables,
                combinedTerms.size(),
                combinedTermIs,
                combinedTermJs,
                combinedTermCoefficients,
                quadraticCoefficients,
                linearCoefficients,
                inequalities.size(),
                constraintIs,
                constraintJs,
                constraintMinimums,
                epsilon,
                epsilon,
                !isInputCorrect);

//        IMCSDialogLayout.consoleLog("after funcmin "+resultVariables.length);
        //Equality tree
        if (eN > 0) {
            int v;
            for (int k = 0; k < variableCount; k++) {
                v = equalityTree[k];
                if (v >= 0) {
                    resultVariables[k] = resultVariables[v] + equalityDistance[k];
                }
            }
        }
        //eoEquality tree

        return resultVariables;
    }

    void createEqualityTree(ArrayList<CombinedTerm> equalities) {
        Arrays.fill(equalityTree, -1);

        //X(object2) - X(object1) = distance

        for (CombinedTerm term : equalities) {
            double d2 = getRootD(term.secondObject);
            int r2 = getRoot(term.secondObject);
            int r1 = getRoot(term.firstObject);

            if (r1 == r2) {
                //check for satisfyability
                double d1 = getRootD(term.firstObject);

                if (Math.abs(term.weight + d1 - d2) > epsilon) {
                    throw new IllegalArgumentException("Funcmin: equalities cannot be satisfied");
                }
            } else {
                equalityTree[r2] = term.firstObject;
                equalityDistance[r2] = term.weight - d2;
            }
        }

        // create single level tree
        for (int i = 0; i < variableCount; i++) {
            getRootD(i);
        }
    }

    int getRoot(int i) {
        while (equalityTree[i] >= 0) {
            i = equalityTree[i];
        }

        return i;
    }

    double getRootD(int i) {
        double d = 0;
        int root = i;

        while (equalityTree[root] >= 0) {
            d += equalityDistance[root];
            root = equalityTree[root];
        }

        if (i != root) {
            equalityTree[i] = root;
            equalityDistance[i] = d;
        }

        return d;
    }//eoEquality Tree
}
