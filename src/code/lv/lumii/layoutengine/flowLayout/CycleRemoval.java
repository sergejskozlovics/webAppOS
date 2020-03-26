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

package lv.lumii.layoutengine.flowLayout;

/**
 * This class implements the cycle removal algorithm from G.DiBattista, P.Eades, R.Tamassia, and
 * I.G.Tollis. Graph Drawing: Algorithms for the Visualization of Graphs, Prentice Hall, Upper
 * Saddle River, NJ, 1999.
 *
 * @author paulis
 */
public class CycleRemoval {

    private final int vN, eN;
    private final int[] eV1, eV2, Y;
    private int[] adjT, adj, outAdjT, outAdj, inAdjT, inAdj;
    private int vT, sT, lT, rB;
    private int[] vNext, vPrev, inDeg, outDeg;
    private int[] V, I, S, s, in;

    /**
     * This method is the class constructor.
     *
     * @param vN0 the number of vertices of the given graph.
     * @param eN0 the number of edges of the given graph.
     * @param eV10 the edge from vertex index.
     * @param eV20 the edge to vertex index.
     * @param Y0 the vertex coordinates that determine an incremental outcome.
     */
    public CycleRemoval(int vN0, int eN0, int[] eV10, int[] eV20, int[] Y0) {
        this.vN = vN0;
        this.eN = eN0;
        this.eV1 = eV10;
        this.eV2 = eV20;
        this.Y = Y0;
    }

    /**
     * This method performs the algorithmic steps of the cycle removal algorithm.
     *
     * @param eFlag the flag indicating if edge direction coincides with the resulting flow
     * direction (output).
     */
    public void RemoveDirectedCycles(boolean[] eFlag) {
        if (vN == 0) {
            return;
        }
        int k, v;

        for (k = 0; k < this.eN; k++) {
            eFlag[k] = this.eV1[k] != this.eV2[k];
        }

        this.makeOutAdjStruct();
        this.makeInAdjStruct();

        this.V = new int[vN];
        this.I = new int[vN];

        for (k = 0; k < this.vN; k++) {
            this.V[k] = k;
        }
        for (int kk = 0; kk < this.vN - 1; kk++) {
            for (k = kk + 1; k < this.vN; k++) {
                if (this.Y[this.V[kk]] > this.Y[this.V[k]]) {
                    v = this.V[kk];
                    this.V[kk] = this.V[k];
                    this.V[k] = v;
                }
            }
        }
        for (k = 0; k < this.vN; k++) {
            this.I[this.V[k]] = k;
        }
        this.vNext = new int[this.vN];
        this.vPrev = new int[this.vN];
        for (k = 0; k < this.vN - 1; k++) {
            this.vNext[k] = k + 1;
        }
        this.vNext[vN - 1] = -1;
        for (k = 1; k < this.vN; k++) {
            this.vPrev[k] = k - 1;
        }
        this.vPrev[0] = -1;
        this.vT = 0;
        this.inDeg = new int[this.vN];
        this.outDeg = new int[this.vN];
        for (k = 0; k < this.vN; k++) {
            this.inDeg[k] = this.inAdjT[k + 1] - this.inAdjT[k];
            this.outDeg[k] = outAdjT[k + 1] - this.outAdjT[k];
        }
        this.S = new int[this.vN];
        this.lT = -1;
        this.rB = this.vN;
        this.s = new int[this.vN];
        this.sT = -1;
        this.in = new int[this.vN];
        for (k = 0; k < this.vN; k++) {
            this.in[k] = 0;
        }

        while (this.vT > -1) {
            v = this.vT;
            while (v > -1) {
                if (this.isSink(v)) {
                    this.insert(v);
                }
                v = this.vNext[v];
            }
            while (this.sT > -1) {
                this.shiftSink();
            }
            v = this.vT;
            while (v > -1) {
                if (this.isSource(v)) {
                    this.insert(v);
                }
                v = this.vNext[v];
            }
            while (this.sT > -1) {
                this.shiftSource();
            }
            if (this.vT == -1) {
                break;
            }

            int vv = 0, d = -Integer.MAX_VALUE;
            v = this.vT;
            while (v > -1) {
                if (d < this.outDeg[v] - this.inDeg[v]) {
                    d = this.outDeg[v] - this.inDeg[v];
                    vv = v;
                }
                v = this.vNext[v];
            }

            int vn = this.vNext[vv], vp = this.vPrev[vv];
            if (vn > -1) {
                this.vPrev[vn] = vp;
            }
            if (vp > -1) {
                this.vNext[vp] = vn;
            } else {
                this.vT = vn;
            }
            this.V[this.I[vv]] = -1;
            this.I[vv] = -1;
            this.S[++this.lT] = vv;
        }

        for (k = 0; k < this.vN; k++) {
            this.I[this.S[k]] = k;
        }
        for (k = 0; k < this.eN; k++) {
            if (this.I[this.eV1[k]] > this.I[this.eV2[k]]) {
                eFlag[k] = false;
            }
        }
    }

    /**
     * This method builds an auxiliary structure of the outgoing edges.
     */
    private void makeOutAdjStruct() {
        int k;

        this.outAdjT = new int[this.vN + 1];
        this.outAdj = new int[this.eN];

        for (k = 1; k <= this.vN; k++) {
            this.outAdjT[k] = 0;
        }
        for (k = 0; k < this.eN; k++) {
            if (this.eV1[k] != this.eV2[k]) {
                this.outAdjT[this.eV1[k] + 1]++;
            }
        }
        this.outAdjT[0] = 0;
        for (k = 1; k < this.vN; k++) {
            this.outAdjT[k] += this.outAdjT[k - 1];
        }
        for (k = 0; k < this.eN; k++) {
            if (this.eV1[k] != this.eV2[k]) {
                this.outAdj[this.outAdjT[this.eV1[k]]++] = this.eV2[k];
            }
        }
        for (k = this.vN; k > 0; k--) {
            this.outAdjT[k] = this.outAdjT[k - 1];
        }
        this.outAdjT[0] = 0;
    }

    /**
     * This method builds an auxiliary structure of the ingoing edges.
     */
    private void makeInAdjStruct() {
        int k;

        this.inAdjT = new int[this.vN + 1];
        this.inAdj = new int[this.eN];

        for (k = 1; k <= this.vN; k++) {
            this.inAdjT[k] = 0;
        }
        for (k = 0; k < this.eN; k++) {
            if (this.eV1[k] != this.eV2[k]) {
                this.inAdjT[this.eV2[k] + 1]++;
            }
        }
        this.inAdjT[0] = 0;
        for (k = 1; k < this.vN; k++) {
            this.inAdjT[k] += this.inAdjT[k - 1];
        }
        for (k = 0; k < eN; k++) {
            if (this.eV1[k] != this.eV2[k]) {
                this.inAdj[this.inAdjT[this.eV2[k]]++] = this.eV1[k];
            }
        }
        for (k = this.vN; k > 0; k--) {
            this.inAdjT[k] = this.inAdjT[k - 1];
        }
        this.inAdjT[0] = 0;
    }

    /**
     * This method tells whether the input vertex is a source.
     *
     * @param v the input vertex index.
     * @return the flag indicating if the given vertex is a source.
     */
    private boolean isSource(int v) {
        for (int l = this.inAdjT[v]; l < this.inAdjT[v + 1]; l++) {
            if (this.I[this.inAdj[l]] > -1) {
                return false;
            }
        }
        for (int l = this.outAdjT[v]; l < this.outAdjT[v + 1]; l++) {
            if (this.I[this.outAdj[l]] > -1) {
                return true;
            }
        }
        return false;
    }

    /**
     * This method tells whether the input vertex is a sink.
     *
     * @param v the input vertex index.
     * @return the flag indicating if the given vertex is a sink.
     */
    private boolean isSink(int v) {
        for (int l = this.outAdjT[v]; l < this.outAdjT[v + 1]; l++) {
            if (this.I[this.outAdj[l]] > -1) {
                return false;
            }
        }
        return true;
    }

    /**
     * This auxiliary method inserts the input vertex into a specific structure.
     *
     * @param v the input vertex index.
     */
    private void insert(int v) {
        if (this.in[v]++ > 0) {
            return;
        }
        this.in[v] = 1;
        this.s[v] = this.sT;
        this.sT = v;
    }

    /**
     * This auxiliary method process source to modify specific structures of vertices.
     */
    private void shiftSource() {
        int v = this.sT;
        if (--this.in[v] > 0) {
            return;
        }
        int vn = this.vNext[v], vp = this.vPrev[v];
        if (vn > -1) {
            this.vPrev[vn] = vp;
        }
        if (vp > -1) {
            this.vNext[vp] = vn;
        } else {
            this.vT = vn;
        }
        this.V[this.I[v]] = -1;
        this.I[v] = -1;
        this.S[++this.lT] = v;
        this.sT = this.s[v];
        for (int l = this.outAdjT[v]; l < this.outAdjT[v + 1]; l++) {
            int w = this.outAdj[l];
            if (this.I[w] > -1 && this.isSource(w)) {
                this.insert(w);
            }
            this.inDeg[w]--;
        }
    }

    /**
     * This auxiliary method process sink to modify specific structures of vertices.
     */
    private void shiftSink() {
        int v = this.sT;
        if (--this.in[v] > 0) {
            return;
        }
        int vn = this.vNext[v], vp = this.vPrev[v];
        if (vn > -1) {
            this.vPrev[vn] = vp;
        }
        if (vp > -1) {
            this.vNext[vp] = vn;
        } else {
            this.vT = vn;
        }
        this.V[this.I[v]] = -1;
        this.I[v] = -1;
        this.S[--this.rB] = v;
        this.sT = this.s[v];
        for (int l = this.inAdjT[v]; l < this.inAdjT[v + 1]; l++) {
            int w = this.inAdj[l];
            if (this.I[w] > -1 && isSink(w)) {
                this.insert(w);
            }
            this.outDeg[w]--;
        }
    }
}
