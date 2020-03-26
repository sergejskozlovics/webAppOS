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
 * This class provides methods of processing graphs.
 *
 * @author paulis
 */
public class GraphProcessor
{
	/**
	 * This method calculates connected components of the given graph.
	 * 
	 * @param adjStructure The adjacency structure of the given graph.
	 * @param compVector The component characteristic vector.
	 *
	 * @return The number of components.
	 */
	public int calculateConnectedComponents(
		ArrayList<ArrayList<Integer>> adjStructure, int[] compVector)
	{
		int n = adjStructure.size();
		LinkedList<Integer> vertexQueue = new LinkedList<>();

		for (int v = 0; v < n; v++)
		{
			compVector[v] = -1;
		}
		
		int compNumber = -1;

		for (int v0 = 0; v0 < n; v0++)
		{
			if (compVector[v0] == -1)
			{
				compNumber++;
				
				vertexQueue.add(v0);
				compVector[v0] = compNumber; 
			
				while (!vertexQueue.isEmpty())
				{
					int v = (Integer) vertexQueue.pop();
			
					for (int w : adjStructure.get(v))
					{		
						if (compVector[w] == -1)
						{
							vertexQueue.add(w);
							compVector[w] = compNumber;
						}
					}
				}
			}
		}
			
		return compNumber + 1;
	}


	/**
	 * This method calculates the BFS-tree edges of a given graph.
	 * 
	 * @param adjStructure The adjacency structure representing the given graph.
	 * @param startVertex The start vertex.
	 *
	 * @return The BFS-tree edge list.
	 */
	public ArrayList<FlowLayoutEdge> calculateBFSTreeEdges(
		ArrayList<Integer>[] adjStructure, int startVertex)
	{
		int n = adjStructure.length; if (n == 0) return null;
		
		ArrayList<FlowLayoutEdge> treeEdges = new ArrayList<>();
		int[] dist = new int[n];
		LinkedList<Integer> vertexQueue = new LinkedList<>();
		
		for (int k = 0; k < n; k++)
			dist[k] = -1;
			
		vertexQueue.add(startVertex);
		dist[startVertex] = 0;

		while (!vertexQueue.isEmpty())
		{
			int v = (Integer) vertexQueue.pop();

			for (int k = 0; k < adjStructure[v].size(); k++)
			{
				int w = (Integer) adjStructure[v].get(k);

				if (dist[w] < 0)
				{
					vertexQueue.add(w);
					dist[w] = dist[v] + 1;
					treeEdges.add(new FlowLayoutEdge(v, w));
				}
			}
		}
			
		return treeEdges;
	}


	/**
	 * This method builds the diameter path of a tree.
	 * 
	 * @param adjStructure The adjacency structure representing the given tree.
	 *
	 * @return The diameter path.
	 */
	public ArrayList<Integer> buildTreeDiameterPath(
		ArrayList<Integer>[] adjStructure)
	{
		int n = adjStructure.length; if (n == 0) return new ArrayList<>();
		
		int[] dist = new int[n];
		int[] prev = new int[n];
		LinkedList<Integer> vertexQueue = new LinkedList<>();
		
		for (int k = 0; k < n; k++)
			dist[k] = -1;
			
		vertexQueue.add(0);
		dist[0] = 0;
	
		while (!vertexQueue.isEmpty())
		{
			int v = (Integer) vertexQueue.pop();
	
			for (int k = 0; k < adjStructure[v].size(); k++)
			{
				int w = (Integer) adjStructure[v].get(k);

				if (dist[w] < 0)
				{
					vertexQueue.add(w);
					dist[w] = dist[v] + 1;
				}
			}
		}
		
		int vMax = 0;
		int dMax = 0;
		
		for (int k = 0; k < n; k++)
			if (dMax < dist[k])
			{
				dMax = dist[k];
				vMax = k;
			}
			
		for (int k = 0; k < n; k++)
			dist[k] = prev[k] = -1;
			
		vertexQueue.add(vMax);
		dist[vMax] = 0;
		prev[vMax] = -1;
	
		while (!vertexQueue.isEmpty())
		{
			int v = (Integer) vertexQueue.pop();
	
			for (int k = 0; k < adjStructure[v].size(); k++)
			{
				int w = (Integer) adjStructure[v].get(k);

				if (dist[w] < 0)
				{
					vertexQueue.add(w);
					dist[w] = dist[v] + 1;
					prev[w] = v;
				}
			}
		}
		
		vMax = 0;
		dMax = 0;
		
		for (int k = 0; k < n; k++)
			if (dMax < dist[k])
			{
				dMax = dist[k];
				vMax = k;
			}

		ArrayList<Integer> path = new ArrayList<>();
		int v = vMax;

		do
		{
			path.add(v);
			v = prev[v];
		}
		while (v > -1);

		return path;
	}


	/**
	 * This method sorts the vertices of the given graph according the BFS order.
	 * 
	 * @param adjStructure The adjacency structure representing the given graph.
	 * @param v0 The start vertex.
	 *
	 * @return Vertices in the BFS order.
	 */
	public ArrayList<Integer> bfsSorting(ArrayList<Integer>[] adjStructure, int v0)
	{
		int n = adjStructure.length; if (n == 0) return null;
		
		int[] dist = new int[n];
		LinkedList<Integer> vertexQueue = new LinkedList<>();
		ArrayList<Integer> bfsList = new ArrayList<>(n);
		
		for (int k = 0; k < n; k++)
			dist[k] = -1;
			
		vertexQueue.add(v0);
		bfsList.add(v0);
		dist[v0] = 0;
	
		while (!vertexQueue.isEmpty())
		{
			int v = (Integer) vertexQueue.pop();
	
			for (int k = 0; k < adjStructure[v].size(); k++)
			{
				int w = (Integer) adjStructure[v].get(k);

				if (dist[w] < 0)
				{
					vertexQueue.add(w);
					bfsList.add(w);
					dist[w] = dist[v] + 1;
				}
			}
		}
		
		return bfsList;
	}


//		/**
//		 * This method builds the BFS layering of a given graph.
//		 * 
//		 * @param adjStructure The adjacency structure representing the given tree.
//		 * @param layer0 The start layer.
//		 *
//		 * @return The output layering.
//		 */
//		public ArrayList<ArrayList<Integer>> buildBFSLayering(ArrayList[] adjStructure, ArrayList layer0)
//		{
//			int n = adjStructure.length; if (n == 0) return null;
//			
//			int[] dist = new int[n];
//			int[] prev = new int[n];
//			LinkedList<Integer> vertexQueue = new LinkedList<>();
//			int dMax = 0;
//			
//			for (int k = 0; k < n; k++)
//				dist[k] = -1;
//				
//			for (int k = 0; k < layer0.size(); k++)
//			{
//				int v = (Integer) layer0.get(k);
//				
//				vertexQueue.add(v);
//				dist[v] = 0;
//			}
//	
//			while (!vertexQueue.isEmpty())
//			{
//				int v = (Integer) vertexQueue.pop();
//	
//				for (int k = 0; k < adjStructure[v].size(); k++)
//				{
//					int w = (Integer) adjStructure[v].get(k);
//
//					if (dist[w] < 0)
//					{
//						vertexQueue.add(w);
//						dist[w] = dist[v] + 1;
//						dMax = Math.max(dMax, dist[w]);
//					}
//				}
//			}
//
//			for (int v = 0; v < n; v++)
//			{
//				if (dist[v] < 0) dist[v] = dMax + 1;
//			}
//
//			ArrayList<ArrayList<Integer>> layering = new ArrayList<>();
//	                for(int k = 0; k <= dMax + 1; k++)
//	                {
//	                    layering.add(new ArrayList<Integer>());
//	                }
//
//			for (int v = 0; v < n; v++)
//			{
//				layering.get(dist[v]).add(v);
//			}
//			
//			return layering;
//		}
//
//
//	/**
//	 * This method builds a list of paths of a tree.
//	 * 
//	 * @param adjStructure The edges of the given tree.
//	 *
//	 * @return The array of paths.
//	 */
//	public LinkedList[] buildTreePaths(LinkedList edges)
//	{
//		ArrayList[] adjStructure = this.buildAdjacencyStructure(-1, edges);
//		int n = adjStructure.length;
//
//		// Vispirms uz dotajam virsotnem konstruejam grafu, kura skautnes
//		// nesavieno zarosanas virsotnes.
//		
//		LinkedList pathEdges = new LinkedList();
//		
//		for (Iterator edgeIter = edges.iterator(); edgeIter.hasNext();)
//		{
//			Point edge = (Point) edgeIter.next();
//			int v1 = edge.x;
//			int v2 = edge.y;
//			
//			if (adjStructure[v1].size() < 3 && adjStructure[v2].size() < 3)
//			{
//				pathEdges.add(edge);
//				//if (n == 4) System.out.println(v1 + " " + v2);
//			}
//		}
//		
//		// Jaunaja grafa atrodam sakarigas komponentes.
//		
//		ArrayList[] pathAdjStructure = this.buildAdjacencyStructure(n, pathEdges);
//		int[] pathComponents = new int[n];
//		int compNumber = this.calculateConnectedComponents(pathAdjStructure, pathComponents);
//		LinkedList[] treePathListArray = new LinkedList[compNumber];
//		
//		for (int k = 0; k < compNumber; k++)
//		{
//			treePathListArray[k] = new LinkedList();
//		}
//		
//		for (int v = 0; v < n; v++)
//		{
//			treePathListArray[pathComponents[v]].add(v);
//		}
//		
//		// Sakarigo komponensu virsotnu kopas, kuram ir apjoms lielaks par 2, sakartojam
//		// (varbut, papildinam ar sazarosanas virsotnem)
//		// un tas ari ir rezultats.
//
//		for (int k = 0; k < compNumber; k++)
//		{
//			if (treePathListArray[k].size() > 2)
//			{
//				int v = -1;
//				int vPrev = -1;
//				int vNext = -1;
//				
//				for (Iterator vertexIter = treePathListArray[k].iterator(); vertexIter.hasNext();)
//				{
//					v = (Integer) vertexIter.next();
//					
//					if (pathAdjStructure[v].size() == 1) break;
//				}
//				
//				treePathListArray[k].clear();
//				treePathListArray[k].add(v);
//
//				do
//				{
//					vNext = (Integer) pathAdjStructure[v].get(0);
//					
//					if (vNext == vPrev)
//					{
//						vNext = (Integer) pathAdjStructure[v].get(1);
//					}
//
//					treePathListArray[k].add(vNext);
//					
//					vPrev = v;
//					v = vNext;
//				}
//				while (pathAdjStructure[v].size() > 1);
//			}
//		}
//		
//		return treePathListArray;
//	}
}
