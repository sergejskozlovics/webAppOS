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

import java.util.ArrayList;
import java.util.LinkedList;


/**
 * This class implements the internal representation of the diagram graph
 * for the Flow Layout algorithm.
 *
 * @author paulis
 */
public class FlowLayoutGraph
{
	public int n;
	public int m;
	public ArrayList<FlowLayoutVertex> vertexList;
	public ArrayList<FlowLayoutEdge> edgeList;
	public ArrayList<ArrayList<Integer>> adjStruct;
	public double[] vX, vY, vW, vH;
	public double[] vX1, vX2, vY1, vY2;
	public int[] vC;
	

	/**
	 * This method is the class constructor. 
	 *
	 * @param n the number of vertices of the graph.
	 */
	public FlowLayoutGraph(int n)
	{
		this.n = n;
		this.m = 0;
		this.vertexList = new ArrayList<>(n);
		this.edgeList = new ArrayList<>();

		for (int v = 0; v < n; v++)
		{
			FlowLayoutVertex vertex = new FlowLayoutVertex();
			
			vertex.setIndex(v);
			vertex.setTag("" + v);
			this.vertexList.add(vertex);
		}
	}
	

	/**
	 * This method inserts a new edge to the graph. 
	 *
	 * @param v1 the edge from vertex index.
	 * @param v2 the edge to vertex index.
	 */
	public void insertNewEdge(int v1, int v2)
	{
		this.edgeList.add(new FlowLayoutEdge(v1, v2));
		this.m++;
	}


	/**
	 * This method builds the adjacency structure of the graph. 
	 */
	@SuppressWarnings("unchecked")
	public void buildAdjacencyStructure()
	{
		this.adjStruct = new ArrayList<>();

		for (int v = 0; v < n; v++)
			this.adjStruct.add(new ArrayList<Integer>());
			
		for (int e = 0; e < m; e++)
		{
			FlowLayoutEdge edge = (FlowLayoutEdge) edgeList.get(e);
			int v1 = edge.v1;
			int v2 = edge.v2;
		
			this.adjStruct.get(v1).add(v2);
			this.adjStruct.get(v2).add(v1);
		}
	}


	/**
	 * This method builds the directed adjacency structure of the graph. 
	 */
	public void buildDirectedAdjacencyStructure()
	{
		this.adjStruct = new ArrayList<>();

		for (int v = 0; v < n; v++)
			this.adjStruct.add(new ArrayList<Integer>());
			
		for (int e = 0; e < m; e++)
		{
			FlowLayoutEdge edge = (FlowLayoutEdge) edgeList.get(e);
			int v1 = edge.v1;
			int v2 = edge.v2;
		
			this.adjStruct.get(v1).add(v2);
		}
	}
	

	/**
	 * This method inserts a new edge to the graph
	 * if the new edge does not create cycles. 
	 *
	 * @param v1 the edge from vertex index.
	 * @param v2 the edge to vertex index.
	 */
	public void insertNewAcyclicEdge(int v1, int v2)
	{
		// At first a path from v2 to v1 is searched.
		
		boolean path = false;
		int[] dist = new int[n];
		LinkedList<Integer> vertexQueue = new LinkedList<>();
		
		for (int k = 0; k < n; k++)
			dist[k] = -1;
			
		vertexQueue.add(v2);
		dist[v2] = 0;
	
		while (!vertexQueue.isEmpty() && !path)
		{
			int v = (Integer) vertexQueue.pop();
	
			for (int w : this.adjStruct.get(v))
			{
				if (w == v1)
				{
					path = true;
					break;
				}

				if (dist[w] < 0)
				{
					vertexQueue.add(w);
					dist[w] = dist[v] + 1;
				}
			}
		}

		if (!path)
		{
			this.edgeList.add(new FlowLayoutEdge(v1, v2));
			this.m++;
			this.adjStruct.get(v1).add(v2);
		}
		else
		{
			this.edgeList.add(new FlowLayoutEdge(v2, v1));
			this.m++;
			this.adjStruct.get(v2).add(v1);
		}
	}
}
