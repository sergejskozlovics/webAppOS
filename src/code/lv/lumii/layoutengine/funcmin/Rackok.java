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
 *
 * @author k
 */
class Rackok {

    static class arc {

	node head;           /* head */

	arc next;           /* next arc with the same tail */

	arc sister;
	byte deleted;
	byte r_cap;           /* residual capacity */

    };

    static class node {

	arc first;           // first outgoing arc
	int rank;
	double excess;           // excess of the node
    };
    node pnodes[];
    arc parcs[];
    int maxnum;//virsotnu skaits grafaa
    int arccount;//loku skaits grafaa
    int curarc;
    node queue[];

    void sourceReachable() {
	int qin = 0;
	for (int i = 0; i < maxnum; i++) {
	    node n = pnodes[i];
	    if (n.excess > 0) {
		queue[qin++] = n;
		n.rank = 0;
	    } else {
		n.rank = 1;
	    }
	}

	while (qin != 0) {
	    node n = queue[--qin];
	    for (arc e = n.first; e != null; e = e.next) {
		if (e.r_cap != 0) {
		    node n2 = e.head;
		    if (n2.rank != 0) {
			n2.rank = 0;
			queue[qin++] = n2;
		    }
		}
	    }
	}
    }

    void flow() {
	arc e;
	int stp = 0;
	//calculate node degree
	//find nodes with degree 1
	for (int i = 0; i < maxnum; i++) {
	    node n = pnodes[i];
	    int deg = 0;
	    for (e = n.first; e != null; e = e.next) {
		deg++;
	    }
	    n.rank = deg;
	    if (deg == 1) {
		queue[stp++] = n;
	    }
	}
	//delete nodes with degree 1
	while (stp != 0) {
	    node n = queue[--stp];
	    for (e = n.first; e != null; e = e.next) {
		if (e.deleted == 0) {
		    break;
		}
	    }
	    if (e == null) {
		continue;
	    }
	    node n2 = e.head;
	    double fl = n.excess;
	    if ((fl > 0) == (e.r_cap == 1)/* && fl*/) {
		e.sister.r_cap |= 1;
		n.excess = 0;
		n2.excess += fl;
	    }
	    e.sister.deleted = 1;
	    n2.rank--;
	    if (n2.rank == 1) {
		queue[stp++] = n2;
	    }
	}
    }

    Rackok(int ncount, int acount) {
	curarc = 0;
	arccount = acount;
	maxnum = ncount;
	pnodes = new node[maxnum];
	for (int i = 0; i < maxnum; i++) {
	    pnodes[i] = new node();
	}
	parcs = new arc[arccount * 2];
	for (int i = 0; i < arccount * 2; i++) {
	    parcs[i] = new arc();
	}
	queue = new node[maxnum];
	for (int i = 0; i < maxnum; i++) {
	    queue[i] = new node();
	}
    }

    void addArc(int n1, int n2) {
	parcs[curarc].r_cap = 1;
	parcs[curarc + 1].r_cap = 0;
	parcs[curarc].head = pnodes[n2];
	parcs[curarc + 1].head = pnodes[n1];
	parcs[curarc].sister = parcs[curarc + 1];
	parcs[curarc + 1].sister = parcs[curarc];

	parcs[curarc].next = pnodes[n1].first;
	pnodes[n1].first = parcs[curarc];
	parcs[curarc + 1].next = pnodes[n2].first;
	pnodes[n2].first = parcs[curarc + 1];
	parcs[curarc].deleted = 0;
	parcs[curarc + 1].deleted = 0;
	curarc += 2;
    }

    void setWeight(double weight[]) {
	for (int i = 0; i < maxnum; i++) {
	    pnodes[i].excess = weight[i];
	}
    }

    void getCut(byte out[]) {
	flow();
	sourceReachable();
	for (int i = 0; i < maxnum; i++) {
	    out[i] = (byte) pnodes[i].rank;
	}
    }
}
