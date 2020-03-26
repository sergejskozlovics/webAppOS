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

import java.util.Random;

/**
 *
 * @author k
 */
class TGraph {

    int nodecnt;
    int edgecnt;
    int maxedges;
    int increment;
    int nodestart[];
    edgeelem edges[];

    static class edgeelem {

	int enode;
	int next;
    }

    TGraph(int nodes, int startedgescnt) {
	nodecnt = nodes;
	maxedges = startedgescnt;
	increment = startedgescnt;
	nodestart = new int[nodes];
	edges = new edgeelem[maxedges];
	for (int i = 0; i < maxedges; i++) {
	    edges[i] = new edgeelem();
	}
	FreeAll();
    }

    final void FreeAll() {
	edgecnt = 0;
	for (int i = 0; i < nodecnt; i++) {
	    nodestart[i] = -1;
	}
    }

    void AddEdge(int snode, int enode) {
	if (edgecnt >= maxedges) {
	    edgeelem tmp[] = new edgeelem[maxedges + increment];
	    System.arraycopy(edges, 0, tmp, 0, edgecnt);
	    edges = tmp;
	    maxedges += increment;
	    increment *= 2;
	    //return;//should do something
	}
	edges[edgecnt].enode = enode;
	edges[edgecnt].next = nodestart[snode];
	nodestart[snode] = edgecnt++;
    }

    void PutEdge(int snode, int enode, int ind) {
	if (ind >= maxedges) {
	    return;//error
	}
	edges[ind].enode = enode;
	edges[ind].next = nodestart[snode];
	nodestart[snode] = ind;
    }

    void AddUndirectEdge(int snode, int enode) {
	AddEdge(snode, enode);
	AddEdge(enode, snode);
    }

    void PutUndirectEdge(int snode, int enode, int ind) {
	PutEdge(snode, enode, ind * 2);
	PutEdge(enode, snode, ind * 2 + 1);
    }

    int diameterA() {
	int d = 0;
	Random r = new Random();
	int i = r.nextInt(nodecnt);
	for (;;) {
	    Integer enode = null;
	    int d1 = BFS(i, enode);
	    if (d1 > d) {
		d = d1;
		i = enode;
	    } else {
		break;
	    }
	}

	return d;
    }

    int diameter() {
	int d = 0;
	for (int i = 0; i < nodecnt; i++) {
	    Integer enode = null;
	    int d1 = BFS(i, enode);
	    if (d1 > d) {
		d = d1;
	    }
	}
	return d;
    }

    int BFS(int startNode, Integer endNode) {
	int lab[] = new int[nodecnt];
	int queue[] = new int[nodecnt];
	int qin = 0, qout = 0;
	lab[startNode] = 1;
	queue[qin++] = startNode;
	int n = 0;
	while (qin > qout) {
	    n = queue[qout++];
	    int clab = lab[n] + 1;
	    int e = nodestart[n];
	    while (e >= 0) {
		int n2 = edges[e].enode;
		if (lab[n2] == 0) {
		    lab[n2] = clab;
		    queue[qin++] = n2;
		}
		e = edges[e].next;
	    }
	}

	int res = lab[n];
	endNode = n;
	return res;
    }
}
