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

package lv.lumii.layoutengine.springembedder;



/**
 * This class implements a simple adjacency structure for graph
 * representation. In should be used only in spring embedder.
 */

final class GraphAdjacency
{

	/**
	 * This constructor creates a graph adjacency representation with
	 * given node and edge count. If the graph is undirected the edge
	 * count must be doubled.
	 */

	GraphAdjacency(int nodes, int edges)
	{
		nodecnt = nodes;
		maxedges = edges;
		increment = edges + 1;
		nodestart = new int[nodes];
		enodes = new int[edges];
		next = new int[edges];
		freeAll();
	}


	/**
	 * This method creates a directed edge from snode to enode
	 */

	void addEdge(int snode, int enode)
	{
		if (edgecnt >= maxedges)
		{
			//allocate a bigger place for edges
			int tmpNext[] = new int[maxedges + increment];
			int tmpEnodes[] = new int[maxedges + increment];
			System.arraycopy(next, 0, tmpNext, 0, maxedges);
			System.arraycopy(enodes, 0, tmpEnodes, 0, maxedges);
			next = tmpNext;
			enodes = tmpEnodes;
			maxedges += increment;
			increment *= 2;
		}

		enodes[edgecnt] = enode;
		next[edgecnt] = nodestart[snode];
		nodestart[snode] = edgecnt++;
	}


	/**
	 * This method creates an undirected edge between snode and enode
	 */

	void addUndirectEdge(int snode, int enode)
	{
		addEdge(snode, enode);
		addEdge(enode, snode);
	}


	/**
	 * This method deletes all edges
	 */

	void freeAll()
	{
		edgecnt = 0;

		for (int i = 0 ; i < nodecnt ; i++)
			nodestart[i] = -1;
	}


	/**
	 * This method deletes an edge going from n1 to n2
	 */

	void delEdge(int n1, int n2)
	{
		int e = nodestart[n1];
		int prev = -1;

		while (e >= 0)
		{
			int en = enodes[e];

			if (en == n2)
			{
				if (prev >= 0)
				{
					next[prev] = next[e];
				}
				else
				{
					nodestart[n1] = next[e];
				}

				break;
			}

			prev = e;
			e = next[e];
		}
	}

// --------------------------------------------------------------------
// Section: instance variables
// --------------------------------------------------------------------

	/** node count */
	int nodecnt;

	/** edge count */
	int edgecnt;

	// maximum number of edges
	int maxedges;

	/** the first edge of each node;
	 * -1 if the node has no edges */
	int[] nodestart;

	/** the target nodes of the edges */
	int[] enodes;

	/** the next edge of an edge
	 * -1 if no more edges */
	int[] next;

	// amount to increase the edge array
	int increment;
}


;
