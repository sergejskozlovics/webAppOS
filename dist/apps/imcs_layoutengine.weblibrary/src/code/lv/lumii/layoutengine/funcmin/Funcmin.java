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

/**
 * The function minimization kernel.
 *
 * @author k
 */
class Funcmin {

    TGraph constr, wconstr;
    TGraph func;
    int vN, cN, fN;
    double vX[];
    int cI[], cJ[];
    double cL[];
    int compN;
    int vComp[];
    int queue[];
    int aaFI[], aaFJ[];
    double aaF[], aF[], bF[];
    double waaF[], waF[], wbF[];
    double cX[], wcL[];
    double Eps, cEps, dEps;
    boolean inQueue[];
    byte i0[];
    double opt[];
    double vD[];
    byte racOut[];
    int iterY;
    int comptree[];
    double compX[];

    /**
     * Corrects the starting values to satisfy the given constraints.
     *
     * @throws IllegalArgumentException the given constraints cannot be
     * satisfied
     */
    void ReviseVarValues() throws IllegalArgumentException {
        int sk[] = new int[vN];
        for(int i = 0; i < vN; i++) {
        	sk[i] = 0;
        }
        
        int k;
        for (k = 0; k < cN; k++) {
            sk[cI[k]]++;
        }
        int k1 = 0, k2 = 0;
        for (k = 0; k < vN; k++) {
            if (sk[k] == 0) {
                queue[k2++] = k;
            }
        }

        while (k1 < k2) {
            k = queue[k1++];
            int e = constr.nodestart[k];
            while (e >= 0) {
                if ((e & 1) == 1) {
                    int v = constr.edges[e].enode;
                    if (--sk[v] == 0) {
                        queue[k2++] = v;
                    }
                    double c = cL[e >> 1] / 2.0;
                    if (vX[v] > vX[k] - c) {
                        vX[v] = vX[k] - c;
                    }
                }
                e = constr.edges[e].next;
            }
        }
        for (k = 0; k < vN; k++) {
            if (sk[k] != 0) {
                throw new IllegalArgumentException("Funcmin: graph not acyclic");
            }
        }

        for (int i = 0; i < vN; i++) {
            sk[i] = 0;
        }
        for (k = 0; k < cN; k++) {
            sk[cJ[k]]++;
        }
        k1 = k2 = 0;
        for (k = 0; k < vN; k++) {
            if (sk[k] == 0) {
                queue[k2++] = k;
            }
        }

        while (k1 < k2) {
            k = queue[k1++];
            int e = constr.nodestart[k];
            while (e >= 0) {
                if ((e & 1) == 0) {
                    int v = constr.edges[e].enode;
                    if (--sk[v] == 0) {
                        queue[k2++] = v;
                    }
                    double c = cL[e >> 1];
                    if (vX[v] < vX[k] + c) {
                        vX[v] = vX[k] + c;
                    }
                }
                e = constr.edges[e].next;
            }
        }
    }

    void CompDFS(int v) {
        int k1 = 1;
        queue[0] = v;
        vComp[v] = compN;
        while (k1 != 0) {
            int k = queue[--k1];
            int e = constr.nodestart[k];
            while (e >= 0) {
                int s = constr.edges[e].enode;
                if (i0[e >> 1] != 0) {
                    if (vComp[s] < 0) {
                        vComp[s] = compN;
                        queue[k1++] = s;
                    } else {
                        i0[e >> 1] &= (1 << (e & 1));
                    }
                }
                e = constr.edges[e].next;
            }
        }

    }

    void FindComp() {
        compN = 0;
        for (int i = 0; i < cN; i++) {
            i0[i] = (byte) (i0[i] != 0 ? 3 : 0);
        }
        for (int i = 0; i < vN; i++) {
            vComp[i] = -1;
        }
        for (int v = 0; v < vN; v++) {
            if (vComp[v] < 0) {
                CompDFS(v);
                compN++;
            }
        }
    }

    void OptimizeFunc(int i) {
        int e = func.nodestart[i];
        while (e >= 0) {
            int v = func.edges[e].enode;
            opt[v] += waaF[e >> 1];
            e = func.edges[e].next;
        }

        e = func.nodestart[i];
        int prev = -1;
        while (e >= 0) {
            int v = func.edges[e].enode;
            int oe = e;
            e = func.edges[e].next;
            if (Math.abs(opt[v]) >= dEps) {
                waaF[oe >> 1] = opt[v];
                prev = oe;
            } else {
                if (prev >= 0) {
                    func.edges[prev].next = e;
                } else {
                    func.nodestart[i] = e;
                }
                waaF[oe >> 1] = 0;
            }
            opt[v] = 0;
        }
    }

    void AddFuncEdge(int n) {
        int iStart = aaFI[n];
        int jStart = aaFJ[n];
        int i = vComp[iStart];
        int j = vComp[jStart];
        double f = aaF[n];
        wbF[i] += f * vX[jStart];
        wbF[j] += f * vX[iStart];
        if (i == j) {
            waF[i] += f;
            return;
        }

        waaF[func.edgecnt >> 1] = f;
        func.AddUndirectEdge(i, j);
    }

    void AddRelEdge(int k) {
        int iStart = cI[k];
        int jStart = cJ[k];
        int i = vComp[iStart];
        int j = vComp[jStart];
        if (i == j) {
            wconstr.edgecnt += 2;
            return;
        }
        double f = cL[k] + vX[iStart] - vX[jStart];

        wcL[wconstr.edgecnt >> 1] = f;
        wconstr.AddUndirectEdge(i, j);
    }

    void Stick(int i, int j) {
        double d = cX[i] - cX[j];

        // change components
        compX[i] = d;
        comptree[i] = j;

        waF[j] += waF[i];
        wbF[j] += wbF[i] + 2 * d * waF[i];

        // del func edges from i to j
        int e = func.nodestart[j];
        int prev = -1;
        while (e >= 0) {
            int v = func.edges[e].enode;
            int oe = e;
            e = func.edges[e].next;
            if (v == i) {
                double c1 = waaF[oe >> 1];
                waF[j] += c1;
                wbF[j] += c1 * d;
                if (prev >= 0) {
                    func.edges[prev].next = e;
                } else {
                    func.nodestart[j] = e;
                }
            } else {
                prev = oe;
            }
        }

        // realloc func edges from i to other components to j
        e = func.nodestart[i];
        while (e >= 0) {
            int v = func.edges[e].enode;
            int e1 = func.edges[e].next;
            if (v != j) {
                func.edges[e].next = func.nodestart[j];
                func.nodestart[j] = e;
                func.edges[e ^ 1].enode = j;
                wbF[v] += waaF[e >> 1] * d;
            }
            e = e1;
        }
        func.nodestart[i] = -1;

        // delete edges from j to i
        e = wconstr.nodestart[j];
        prev = -1;
        while (e >= 0) {
            int v = wconstr.edges[e].enode;
            int oe = e;
            e = wconstr.edges[e].next;
            if (v == i) {
                if (prev >= 0) {
                    wconstr.edges[prev].next = e;
                } else {
                    wconstr.nodestart[j] = e;
                }
            } else {
                prev = oe;
            }
        }

        // realloc edges from i to other components to j
        e = wconstr.nodestart[i];
        while (e >= 0) {
            int v = wconstr.edges[e].enode;
            int e1 = wconstr.edges[e].next;
            if (v != j) {
                wconstr.edges[e].next = wconstr.nodestart[j];
                wconstr.nodestart[j] = e;
                wconstr.edges[e ^ 1].enode = j;
                if ((e & 1) == 1) {
                    wcL[e >> 1] -= d;
                } else {
                    wcL[e >> 1] += d;
                }
            }
            e = e1;
        }
        wconstr.nodestart[i] = -1;
    }

    int Shift(int i) {
        double s = 0;
        int e = func.nodestart[i];
        while (e >= 0) {
            s += waaF[e >> 1] * cX[func.edges[e].enode];
            e = func.edges[e].next;
        }

        double X;
        if (waF[i] < dEps) {
            double d = wbF[i] + s;
            if (Math.abs(d) < dEps) {
                return -1;
            }
            if (d < 0) {
                X = 1E20;
            } else {
                X = -1E20;
            }
        } else {
            X = -(wbF[i] + s) / (waF[i] * 2);
        }

        double ox = cX[i];

        e = wconstr.nodestart[i];
        int j = -1;

        int e1 = 0;
        while (e >= 0) {
            int v = wconstr.edges[e].enode;
            double L = wcL[e >> 1];
            if ((e & 1) == 1) {
                if (cX[v] > X - L + cEps) {
                    X = cX[v] + L;
                    j = v;
                    e1 = e;
                }
            } else if (cX[v] < X + L - cEps) {
                X = cX[v] - L;
                j = v;
                e1 = e;
            }
            e = wconstr.edges[e].next;
        }
//    if(fabs(X)>1E10)
//        throw "err";
        cX[i] = X;
        if (j >= 0) {
            i0[e1 >> 1] = 1;
            Stick(i, j);
            return j;
        }
        if (func.nodestart[i] < 0) {
            return -1;
        }
        if (Math.abs(ox - X) <= Eps) {
            return -1;
        }
        return i;
    }

    void ShiftAll() {
        FindComp();

        for (int i = 0; i < compN; i++) {
            waF[i] = 0;
            wbF[i] = 0;
            cX[i] = 0;
        }

        for (int i = 0; i < vN; i++) {
            waF[vComp[i]] += aF[i];
            wbF[vComp[i]] += bF[i] + 2 * vX[i] * aF[i];
        }

        func.FreeAll();
        for (int i = 0; i < fN; i++) {
            AddFuncEdge(i);
        }
        for (int i = 0; i < compN; i++) {
            OptimizeFunc(i);
        }

        wconstr.FreeAll();
        for (int i = 0; i < cN; i++) {
            AddRelEdge(i);
        }

        int qsize = vN + 1;
        int k1 = 0, k2 = 0;
        for (int i = 0; i < compN; i++) {
            queue[k1++] = i;
            inQueue[i] = true;
        }
        /*
         * for(int i=0;i<vN;i++){ int ii=vComp[order[vN-1-i]]; if(!inQueue[ii]){
         * queue[k1++]=ii; inQueue[ii]=true; } }
         */

        while (k1 != k2) {
            int k = queue[k2++];
            inQueue[k] = false;
            if (k2 >= qsize) {
                k2 = 0;
            }
            k = Shift(k);
            if (k >= 0) {
                if (!inQueue[k]) {
                    queue[k1++] = k;
                    if (k1 >= qsize) {
                        k1 = 0;
                    }
                    inQueue[k] = true;
                }
                //add neighbours
                int e = func.nodestart[k];
                while (e >= 0) {
                    int v = func.edges[e].enode;
                    if (!inQueue[v]) {
                        queue[k1++] = v;
                        if (k1 >= qsize) {
                            k1 = 0;
                        }
                        inQueue[v] = true;
                    }
                    e = func.edges[e].next;
                }
            }
        }

        for (int i = 0; i < vN; i++) {
            int c = vComp[i];
            k1 = 0;
            while (comptree[c] >= 0) {
                queue[k1++] = c;
                c = comptree[c];
            }
            double d = cX[c];
            while (k1 != 0) {
                int k = queue[--k1];
                d += compX[k];
                cX[k] = d;
                comptree[k] = -1;
            }

            vX[i] += d;
        }
    }

    void CalcI0() {
        for (int i = 0; i < cN; i++) {
            if (vX[cJ[i]] - vX[cI[i]] <= cL[i] + cEps) {
                i0[i] = 1;
            } else {
                i0[i] = 0;
            }
        }
    }

    boolean Cut() {
        for (int i = 0; i < vN; i++) {
            vD[i] = -2 * aF[i] * vX[i] - bF[i];
        }
        for (int k = 0; k < fN; k++) {
            int i = aaFI[k];
            int j = aaFJ[k];
            vD[i] -= aaF[k] * vX[j];
            vD[j] -= aaF[k] * vX[i];
        }

        Rackok r = new Rackok(vN, vN);

        for (int i = 0; i < cN; i++) {
            if (i0[i] != 0) {
                r.addArc(cI[i], cJ[i]);
            }
        }

        r.setWeight(vD);
        r.getCut(racOut);

        boolean endf = false;

        for (int k = 0; k < cN; k++) {
            if (i0[k] != 0) {
                if (racOut[cI[k]] != racOut[cJ[k]]) {
                    i0[k] = 0;
                    endf = true;
                }
            }
        }

        return endf;
    }

    void RecurseCycles(int k) {
        queue[k] = 1;
        int e = constr.nodestart[k];
        while (e >= 0) {
            if ((e & 1) == 1) {
                int v = constr.edges[e].enode;
                if (queue[v] == 1) {
                    i0[e >> 1] = 1;
                } else if (queue[v] == 0) {
                    RecurseCycles(v);
                }
            }
            e = constr.edges[e].next;
        }
        queue[k] = 2;
    }

    void RemoveCycles() {
        for (int i = 0; i < vN; i++) {
            queue[i] = 0;
        }
        for (int i = 0; i < cN; i++) {
            i0[i] = 0;
        }

        for (int i = 0; i < vN; i++) {
            if (queue[i] == 0) {
                RecurseCycles(i);
            }
        }

        int k1 = 0;
        for (int k = 0; k < cN; k++) {
            int v1 = cI[k];
            int v2 = cJ[k];
            if (i0[k] == 0) {
                cI[k1] = v1;
                cJ[k1] = v2;
                cL[k1] = cL[k];
                k1++;
            }
        }
        cN = k1;
        constr.FreeAll();
        for (int i = 0; i < cN; i++) {
            constr.AddUndirectEdge(cI[i], cJ[i]);
        }
    }

    /**
     * Finds the minimum value of a second order {@code variableCount}-variable
     * function such that the variables satisfy the given conditions.
     *
     * @param variableCount the number of variables in the function to minimize
     * @param equivalenceIndices the i<sub>th</sub> element of this array
     * corresponds to the index of a variable that the i<sub>th</sub> variable
     * should be equivalent to. If constraints of this kind are not used, this
     * should be {@code null}
     * @param startValues the starting values of the variables, usually all
     * zeroes
     * @param combinedTermCount the number of terms in the function in the form
     * of b<sub>ij</sub>*x<sub>i</sub>*x<sub>j</sub>
     * @param combinedTermIs the indices of the first variables in the
     * function's two variable terms
     * @param combinedTermJs the indices of the second variables in the
     * function's two variable terms
     * @param combinedTermCoefficients the coefficients of the function's two
     * variable terms, the i<sub>th</sub> element corresponds to the coefficient
     * for x<sub>combinedTermIs[i]</sub>x<sub>combinedTermJs[i]</sub>
     * @param quadraticTermCoefficients the coefficients of the function's
     * second order single variable terms, the i<sub>th</sub> element
     * corresponds to the coefficient for x<sub>i</sub><sup>2</sup>
     * @param linearTermCoefficients the coefficients of the function's first
     * order terms, the i<sub>th</sub> element corresponds to the coefficient
     * for x<sub>i</sub>
     * @param constraintCount the number of constraints in the form
     * x<sub>j</sub>-x<sub>i</sub>???d
     * @param constraintIs the indices of the second variables in the
     * constraints
     * @param constraintJs the indices of the first variables in the constraints
     * @param constraintMinimums the minimum values of the constraints
     * @param constraintEpsilon the maximum amount that a constraint can be
     * below its minimum
     * @param minimumEpsilon the maximum amount that the functions value can be
     * above its minimum (default 0.001)
     * @param performStartingValueCorrection whether the starting values should
     * be corrected to satisfy the given constraints
     * @return the values of the given variables after minimization.
     * @throws IllegalArgumentException throws an exception if the given
     * constraint graph is not acyclic ??? the given constraints are impossible to
     * satisfy.
     */
    double[] Minimize(
            int variableCount,
            int equivalenceIndices[],
            double startValues[],
            int combinedTermCount,
            int combinedTermIs[],
            int combinedTermJs[],
            double combinedTermCoefficients[],
            double quadraticTermCoefficients[],
            double linearTermCoefficients[],
            int constraintCount,
            int constraintIs[],
            int constraintJs[],
            double constraintMinimums[],
            double constraintEpsilon,
            double minimumEpsilon,
            boolean performStartingValueCorrection) throws IllegalArgumentException {
        if (variableCount == 0) {
            return startValues;
        }

        int v;
        if (equivalenceIndices != null) {
            for (int k = 0; k < variableCount; k++) {
                if ((v = equivalenceIndices[k]) >= 0) {
                    quadraticTermCoefficients[v] += quadraticTermCoefficients[k];
                    linearTermCoefficients[v] += linearTermCoefficients[k];
                    quadraticTermCoefficients[k] = 0;
                    linearTermCoefficients[k] = 0;
                }
            }
            for (int k = 0; k < combinedTermCount; k++) {
                if ((v = equivalenceIndices[combinedTermIs[k]]) >= 0) {
                    combinedTermIs[k] = v;
                }
                if ((v = equivalenceIndices[combinedTermJs[k]]) >= 0) {
                    combinedTermJs[k] = v;
                }
            }
            int k1 = 0;
            for (int k = 0; k < constraintCount; k++) {
                int v1 = ((v = equivalenceIndices[constraintIs[k]]) >= 0) ? v : constraintIs[k];
                int v2 = ((v = equivalenceIndices[constraintJs[k]]) >= 0) ? v : constraintJs[k];
                if (v1 != v2) {
                    constraintIs[k1] = v1;
                    constraintJs[k1] = v2;
                    constraintMinimums[k1] = constraintMinimums[k];
                    k1++;
                }
            }
            constraintCount = k1;
        }

        Eps = minimumEpsilon;
        cEps = constraintEpsilon;
        dEps = minimumEpsilon;
        cN = constraintCount;
        vN = variableCount;
        fN = combinedTermCount;

        for (int i = 0; i < cN; i++) {
            constr.AddUndirectEdge(constraintIs[i], constraintJs[i]);
        }

        waaF = new double[fN];
        waF = new double[vN];
        wbF = new double[vN];
        wcL = new double[cN];
        cX = new double[vN];
        inQueue = new boolean[vN];
        i0 = new byte[cN];
        vD = new double[vN];
        racOut = new byte[vN];
        opt = new double[vN];
        queue = new int[vN + 1];
        comptree = new int[vN];
        for (int i = 0; i < vN; i++) {
            comptree[i] = -1;
        }
        compX = new double[vN];

        cI = constraintIs;
        cJ = constraintJs;
        cL = constraintMinimums;
//    if(equivalenceIndices) RemoveCycles();

        vX = new double[vN];
        System.arraycopy(startValues, 0, vX, 0, vN);
        aaFI = combinedTermIs;
        aaFJ = combinedTermJs;
        aaF = combinedTermCoefficients;
        aF = quadraticTermCoefficients;
        bF = linearTermCoefficients;

        //correct starting position
        if (performStartingValueCorrection) {
            ReviseVarValues();
        }

        vComp = new int[vN];
        CalcI0();
        //iterate
        int iter = 0;
        iterY = 0;

        do {
            ShiftAll();
            iter++;
            if (iter > vN) {
//            throw "error";
                break;//error
            }
        } while (Cut());

        if (performStartingValueCorrection) {
            ReviseVarValues();
        }
        System.arraycopy(vX, 0, startValues, 0, vN);

        if (equivalenceIndices != null) {
            for (int k = 0; k < variableCount; k++) {
                if ((v = equivalenceIndices[k]) >= 0) {
                    startValues[k] = startValues[v];
                }
            }
        }

        return startValues;
    }

    /**
     * Creates a new {@code Funcmin} object for function minimization. The
     * creation parameters will determine that maximum complexity of the
     * functions this {@code Funcmin} will be able to minimize.
     *
     * @param vN the maximum number of variables in the function
     * @param cN the maximum number of constraints the minimization will be
     * under
     * @param fN the maximum number of two variable second order terms in the
     * function
     */
    Funcmin(int vN, int cN, int fN) {
        func = new TGraph(vN, fN * 2);
        constr = new TGraph(vN, cN * 2);
        wconstr = new TGraph(vN, cN * 2);
    }
}
