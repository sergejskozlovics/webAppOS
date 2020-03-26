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

package lv.lumii.layoutengine;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import lv.lumii.layoutengine.LayoutConstraints.GridLayoutConstraints;

import lv.lumii.layoutengine.flowLayout.CycleRemoval;
import lv.lumii.layoutengine.flowLayout.FlowLayoutEdge;
import lv.lumii.layoutengine.flowLayout.FlowLayoutGraph;
import lv.lumii.layoutengine.flowLayout.FlowLayoutVertex;
import lv.lumii.layoutengine.flowLayout.GraphProcessor;
import lv.lumii.layoutengine.flowLayout.horizontalOrdering.LayeredGraph;
import lv.lumii.layoutengine.flowLayout.horizontalOrdering.LayeredNode;
import lv.lumii.layoutengine.flowLayout.horizontalOrdering.OrderingAlgorithm;
import lv.lumii.layoutengine.flowLayout.horizontalOrdering.OrderingInput;
import lv.lumii.layoutengine.funcmin.ExtendedQuadraticOptimizer;
import lv.lumii.layoutengine.obstacleGraph.ObstacleGraph;
import lv.lumii.layoutengine.obstacleGraph.Segment;
import lv.lumii.layoutengine.util.Pair;

/**
 * This class implements the details of the flow layout algorithm.
 * The algorithm consists of tree main parts:
 * (1) building an acyclic graph of the flow by inverting some amount of diagram graph edges,
 * (2) calculating vertex vertical coordinates minimizing vertical extent of edges
 * and requiring some minimum difference between coordinates of edge endpoints,
 * (3) calculating vertex vertical coordinates to avoid redundant edge crossings.
 *
 * @author paulis
 */
class FlowLayoutKernelAlgorithm
{
	/**
	 * This variable stores the desired separation of nodes along the flow direction.
	 */
	private double flowSpacing;

	/**
	 * This variable stores the index of the required direction of the flow.
	 */
	private int flowDirection;

	/**
	 * This variable stores the flag indicating if nodes are positioned in levels.
	 */
	private boolean flowLevels;

	/**
	 * This variable stores the number of diagram graph vertices.
	 */
	private int N;

	/**
	 * At the basic direction of the diagram flow this array element
	 * stores the average x-coordinate of the given node neighbors.
	 */
	private double[] nodeExternalX;

	/**
	 * At the basic direction of the diagram flow this variable stores the required
	 * x-coordinate of the diagram center, when the diagram is nested.
	 */
	private double centerX;

	/**
	 * At the basic direction of the diagram flow this variable stores the
	 * number of required columns.
	 */
	private int columnNumber = 0;

	/**
	 * At the basic direction of the diagram flow this variable stores the
	 * number of required rows.
	 */
	private int rowNumber = 0;

	/**
	 * At the basic direction of the diagram flow this array element stores
	 * the required column of the node.
	 */
	private int[] nodePredeterminedColumn;

	/**
	 * At the basic direction of the diagram flow this array element stores
	 * the required row of the node.
	 */
	private int[] nodePredeterminedRow;

	/**
	 * At the basic direction of the diagram flow this ArrayList element stores
	 * the nodes specified for the corresponding column.
	 */
	private ArrayList< ArrayList <Integer>> columnNodes;

	/**
	 * At the basic direction of the diagram flow this ArrayList element stores
	 * the nodes specified for the corresponding row.
	 */
	private ArrayList< ArrayList <Integer>>  rowNodes;

	/**
	 * This variable stores the internal representation of the diagram graph.
	 */
	private FlowLayoutGraph G;

	/**
	 * This array element stores the flag indicating if the corresponding edge is directed.
	 */
	private boolean[] eDirected;

	/**
	 * This array element stores the flag indicating if the corresponding edge direction
	 * coincides with the flow direction.
	 */
	private boolean[] eFlow;

	/**
	 * This array element stores the index of the connected component of the diagram graph
	 * comprising the corresponding vertex.
	 */
	private int[] vCompIndex;

	/**
	 * This variable stores the number of the connected component of the diagram graph.
	 */
	private int compN;

	/**
	 * This variable implements the map of the indices of the connected components
	 * of the diagram graph sorted by x-coordinates of component external vertices
	 * to the original indices of components. 
	 */
	Integer[] sortIndComp;


	/**
	 * This method prepares the input data for internal calculations
	 * and lays out the given diagram. 
	 *
	 * @param centerX the required x-coordinate of the diagram center,
	 * when the diagram is nested (input).
	 * @param centerY the required y-coordinate of the diagram center,
	 * when the diagram is nested (input).
	 * @param nodeX the calculated x-coordinate of node center.
	 * @param nodeY the calculated y-coordinate of node center.
	 * @param nodeW the given node width (input).
	 * @param nodeH the given node height (input).
	 * @param nodeExternalX the average x-coordinate of the given node neighbors
	 * already positioned outside the nest (input).
	 * @param nodeExternalY the average y-coordinate of the given node neighbors
	 * already positioned outside the nest (input).
	 * @param constraints the common grid data (input).
	 * @param nodeRow the required row of the node (input).
	 * @param nodeCol the required column of the node (input).
	 * @param edgeSourceId from node index of the edge (input).
	 * @param edgeTargetId to node index of the edge (input).
	 * @param eDirected the flag indicating if the edge is directed (input).
	 * @param flowSpacing the desired separation of nodes along the flow direction (input)
	 * @param flowDirection the index of the required direction of the flow (input).
	 * @param flowLevels the flag indicating if nodes are positioned in levels (input).
     */
    public void performLayout(
            double centerX, double centerY,
            double nodeX[], double nodeY[],
            double nodeW[], double nodeH[],
            double nodeExternalX[], double nodeExternalY[], 
            GridLayoutConstraints constraints,
            int nodeRow[], int nodeCol[],
            int edgeSourceId[], int edgeTargetId[],
            boolean[] eDirected,
            double flowSpacing,
    		int flowDirection,
    		boolean flowLevels)
	{
    	this.eDirected = eDirected;
    	this.flowSpacing = flowSpacing;
    	this.flowDirection = flowDirection;
		this.flowLevels = flowLevels;
    	int nodeN = nodeW.length;

    	this.nodeExternalX = new double[nodeN];
		
		for (int node = 0; node < nodeN; node++)
		{
			if (flowDirection == 1 || flowDirection == -1)
            {
				this.nodeExternalX[node] = nodeExternalX[node];
				this.centerX = centerX;
            }
			else
            {
				this.nodeExternalX[node] = nodeExternalY[node];
				this.centerX = centerY;
            }
		}
		
		this.nodePredeterminedColumn = new int[nodeN];
		this.nodePredeterminedRow = new int[nodeN];
		
		for (int node = 0; node < nodeN; node++)
		{
			this.nodePredeterminedColumn[node] = -1;
			this.nodePredeterminedRow[node] = -1;
		}
		
		if (constraints != null)
		{
            if (flowDirection == 1 || flowDirection == -1)
            {
            	this.columnNumber = constraints.getColumnCount();
            	this.rowNumber = constraints.getRowCount();
            }
            else
            {
            	this.columnNumber = constraints.getRowCount();
            	this.rowNumber = constraints.getColumnCount();
            }
		
			this.columnNodes = new ArrayList<>();
			this.rowNodes = new ArrayList<>();
			
			for (int col = 0; col < this.columnNumber; col++)
			{
				this.columnNodes.add(new ArrayList<Integer>());
			}
			
			for (int row = 0; row < this.rowNumber; row++)
			{
				this.rowNodes.add(new ArrayList<Integer>());
			}
			
			for (int node = 0; node < nodeN; node++)
			{
				if (flowDirection == 1)
	            {
					this.nodePredeterminedColumn[node] = nodeCol[node];
					this.nodePredeterminedRow[node] = nodeRow[node];
	            }
				if (flowDirection == -1)
	            {
					this.nodePredeterminedColumn[node] = nodeCol[node];
					this.nodePredeterminedRow[node] = 
						(nodeRow[node] == -1)? -1: constraints.getRowCount() - 1 - nodeRow[node];
	            }
				if (flowDirection == 2)
	            {
					this.nodePredeterminedColumn[node] = nodeRow[node];
					this.nodePredeterminedRow[node] = nodeCol[node];
	            }
				if (flowDirection == -2)
	            {
					this.nodePredeterminedColumn[node] = nodeRow[node];
					this.nodePredeterminedRow[node] =
						(nodeCol[node] == -1)? -1: constraints.getColumnCount() - 1 - nodeCol[node];
	            }
			}
		}

    	this.buildGraph(flowDirection, nodeW, nodeH, edgeSourceId, edgeTargetId);
		this.flowLayout(this.G, this.nodeExternalX);
		this.returnflowDirectionGeometry(flowDirection, nodeX, nodeY);
	}
	

	/**
	 * This method builds the internal representation of the given diagram graph. 
	 *
	 * @param flowDirection the index of the required direction of the flow (input).
	 * @param nW the given node width (input).
	 * @param nH the given node height (input).
	 * @param eV1 the edge from node index (input).
	 * @param eV2 the edge to node index (input).
	 */
	public void buildGraph(int flowDirection, double[] nW, double[] nH, int[] eV1, int[] eV2)
	{
		this.N = nW.length;
		this.G = new FlowLayoutGraph(N);
		this.G.vX = new double[this.N];
		this.G.vY = new double[this.N];
		this.G.vW = new double[this.N];
		this.G.vH = new double[this.N];
		
		for (int v = 0; v < this.N; v++)
		{
            if (flowDirection == 1 || flowDirection == -1)
            {
                this.G.vW[v] = nW[v];
    			this.G.vH[v] = nH[v];
            }
            else
            {
                this.G.vW[v] = nH[v];
    			this.G.vH[v] = nW[v];
            }

		}
		
		for (int e = 0; e < eV1.length; e++)
		{
        	this.G.insertNewEdge(eV1[e], eV2[e]);
		}
	}
	

	/**
	 * This method calculates the resulting node positions of the given diagram. 
	 *
	 * @param flowDirection the index of the required direction of the flow (input).
	 * @param nX the calculated x-coordinate of node center.
	 * @param nY the calculated y-coordinate of node center.
	 */
	public void returnflowDirectionGeometry(int flowDirection, double[] nX, double[] nY)
	{
		for (int v = 0; v < this.N; v++)
		{
            if (flowDirection == 1)
            {
				nX[v] = this.G.vX[v];
				nY[v] = this.G.vY[v];
            }
            if (flowDirection == -1)
            {
				nX[v] = this.G.vX[v];
				nY[v] = -this.G.vY[v];
            }
            if (flowDirection == 2)
            {
				nX[v] = this.G.vY[v];
				nY[v] = this.G.vX[v];
            }
            if (flowDirection == -2)
            {
				nX[v] = -this.G.vY[v];
				nY[v] = this.G.vX[v];
            }
		}
	}
	

	/**
	 * This method implements the basic version of the flow layout algorithm
	 * at the top to bottom flow direction. All input data are adjusted to this case.
	 * The vertex vertical coordinates are assigned from the specific vertical
	 * ordering of vertices after removing cycles by edge inversion.
	 * The horizontal coordinates are assigned by initializing based on
	 * PQ-tree ordering followed by barycentric postprocessing. 
	 *
	 * @param g the graph to be laid out (input/output).
	 * @param nodeExternalX the average x-coordinate of the given node neighbors
	 * already positioned outside the nest (input).
	 */
	private void flowLayout(FlowLayoutGraph g, double[] nodeExternalX)
	{
		int[] eV1 = new int[g.m];
		int[] eV2 = new int[g.m];
		int[] y = new int[g.n];
		this.eFlow = new boolean[g.m];
		int[] eV1Dir = new int[g.m];
		int[] eV2Dir = new int[g.m];
		int[] eDirIndex = new int[g.m];
		int eDirM = 0;
		
		for (int e = 0; e < g.m; e++)
		{
			FlowLayoutEdge edge = (FlowLayoutEdge) g.edgeList.get(e);
			
			eV1[e] = edge.v1;
			eV2[e] = edge.v2;
			
			if (this.eDirected[e])
			{
				eV1Dir[eDirM] = edge.v1;
				eV2Dir[eDirM] = edge.v2;
				eDirIndex[eDirM] = e;
				eDirM++;
			}
		}
		
		boolean[] eFlowDir = new boolean[eDirM];

//		System.out.println(g.n + " " + g.m);
//		System.out.println("edges");
//		for (int e = 0; e < g.m; e++)
//		{
//			System.out.println(eV1[e] + " " + eV2[e]);
//		}

		for (int v = 0; v < g.n; v++)
		{
			y[v] = 0;
		}
		
		CycleRemoval cycleRemoval = new CycleRemoval(g.n, eDirM, eV1Dir, eV2Dir, y); 
		
		cycleRemoval.RemoveDirectedCycles(eFlowDir);

		for (int eDir = 0; eDir < eDirM; eDir++)
		{
			this.eFlow[eDirIndex[eDir]] = eFlowDir[eDir];
		}

		g.buildAdjacencyStructure();

		GraphProcessor graphProcessor = new GraphProcessor();
		this.vCompIndex = new int[g.n];
		
		this.compN = graphProcessor.calculateConnectedComponents(g.adjStruct, this.vCompIndex);
		
		final double[] compExternalX = new double[this.compN];
		int[] compExternalXN = new int[this.compN];
		this.sortIndComp = new Integer[this.compN];
		
		for (int k = 0; k < this.compN; k++)
		{
			compExternalX[k] = Double.NaN;
			this.sortIndComp[k] = k; 
		}
		
		for (int v = 0; v < g.n; v++)
		{
			if (!Double.isNaN(nodeExternalX[v]))
			{
				int cInd = this.vCompIndex[v];
			
				if (Double.isNaN(compExternalX[cInd]))
				{
					compExternalX[cInd] = nodeExternalX[v];
					compExternalXN[cInd] = 1;
				}
				else
				{
					compExternalX[cInd] += nodeExternalX[v];
					compExternalXN[cInd]++;
				}
			}
		}

		for (int k = 0; k < this.compN; k++)
		{
			if (!Double.isNaN(compExternalX[k]))
			{
				compExternalX[k] /= compExternalXN[k];
			}
		}

		Arrays.sort(this.sortIndComp, new Comparator<Integer>()
		{
                        @Override
			public int compare(Integer k1, Integer k2)
			{
				double x1 = compExternalX[k1];
				double x2 = compExternalX[k2];
					
				if (Double.isNaN(x1) || Double.isNaN(x2))
				{
					return 0;
				}
				else
				{
					if (x1 < x2)
					{
						return -1;
					}
					else if(x1 > x2)
					{
						return 1;
					}
					
					return 0;
				}
			}
		});
		
		this.assignFlowLayoutVerticalCoordinates(g,
			eV1, eV2, eFlow);
		
		this.assignFlowLayoutHorizontalCoordinates(g, 
			eV1, eV2, nodeExternalX);

//		int cNumber = 0;
//		
//		for (int e1 = 0; e1 < g.m; e1++)
//		{
//			int v11 = eV1[e1];
//			int v21 = eV2[e1];
//			
//			double x11 = g.vX[v11];
//			double y11 = g.vY[v11];
//			double x21 = g.vX[v21];
//			double y21 = g.vY[v21];
//			
//			for (int e2 = e1 + 1; e2 < g.m; e2++)
//			{
//				int v12 = eV1[e2];
//				int v22 = eV2[e2];
//				
//				double x12 = g.vX[v12];
//				double y12 = g.vY[v12];
//				double x22 = g.vX[v22];
//				double y22 = g.vY[v22];
//				
//				if (SegSegIntersec(x11, y11, x21, y21, x12, y12, x22, y22))
//					cNumber++;
//			}
//		}
//		
//		System.out.println(cNumber);
	}


	/**
	 * This method assigns vertical ordering of vertices of the cycle-free 
	 * input graph such that the edges with specified flags point downwards 
	 * and other edges point upwards. The total length of edges (the difference 
	 * between vertex indices within ordering) is minimized.
	 *
	 * @param g the graph to be laid out (input/output).
	 * @param eV1 the edge from vertex index (input).
	 * @param eV2 the edge to vertex index (input).
	 * @param eFlow the flag indicating if edge direction coincides
	 * with the flow direction (input).
	 */
	private void assignFlowLayoutVerticalCoordinates(FlowLayoutGraph g,
		int[] eV1, int[] eV2,
		boolean[] eFlow)
	{
		int vN = g.n;
		int eN = g.m;
		double[] y;
		
		ExtendedQuadraticOptimizer optimizer;

		if (this.flowLevels)
		{
			if (this.rowNumber <= 1)
			{
				optimizer = new ExtendedQuadraticOptimizer(vN);
	
				for (int e = 0; e < eN; e++)
				{
					if (eV1[e] != eV2[e] && eFlow[e] && this.eDirected[e])
					{
						optimizer.addLinearDifference(eV1[e], eV2[e], 1);
						optimizer.addInequality(eV1[e], eV2[e], 1);
					}
					else if (eV1[e] != eV2[e] && !eFlow[e] && this.eDirected[e])
					{
						optimizer.addLinearDifference(eV2[e], eV1[e], 1);
						optimizer.addInequality(eV2[e], eV1[e], 1);
					}

					if (eV1[e] != eV2[e] && !this.eDirected[e])
					{
						optimizer.addQuadraticDifference(eV1[e], eV2[e], 1);
					}
				}
	
				y = optimizer.performOptimization();
				
				double[] yMin = new double[this.compN];
				
				for (int k = 0; k < this.compN; k++)
				{
					yMin[k] = Double.MAX_VALUE;
				}
				
				for (int v = 0; v < vN; v++)
				{
					yMin[this.vCompIndex[v]] = Math.min(yMin[this.vCompIndex[v]], y[v]);
				}
				
				for (int v = 0; v < vN; v++)
				{
					y[v] = Math.floor(y[v] - yMin[this.vCompIndex[v]]);
				}
			}
			else
			{	
				FlowLayoutGraph cg = this.buildRowConstraintGraph(vN, eN, eV1, eV2, eFlow);
				
				optimizer = new ExtendedQuadraticOptimizer(cg.n);
		
				for (int e = 0; e < cg.m; e++)
				{
					FlowLayoutEdge edge = (FlowLayoutEdge) cg.edgeList.get(e);
					int v1 = edge.v1;
					int v2 = edge.v2;
	
					optimizer.addLinearDifference(v1, v2, 1);
					
					if (v1 < vN && vN <= v2)
						optimizer.addInequality(v1, v2, 0);
					else if (vN <= v1 && v2 < vN)
						optimizer.addInequality(v1, v2, 1);
					else
						optimizer.addInequality(v1, v2, 1);
				}
				
				for (int e = 0; e < g.m; e++)
				{
					if (!this.eDirected[e])
					{
						optimizer.addQuadraticDifference(eV1[e], eV2[e], 1);
					}
				}
	
				y = optimizer.performOptimization();
			}
			
			//for (int v = 0; v < vN; v++) System.out.println(y[v] + " " + this.G.vH[v]);

			double h1h2Max = 0;
			
			for (int v1 = 0; v1 < g.n; v1++)
			{
				for (int v2 = v1 + 1; v2 < g.n; v2++)
				{
					if (0.5 < Math.abs(y[v2] - y[v1]) && Math.abs(y[v2] - y[v1]) < 1.5)
					{
						h1h2Max = Math.max(h1h2Max, g.vH[v1] + g.vH[v2]);
					}
				}
			}

			for (int v = 0; v < g.n; v++)
			{
				g.vY[v] = y[v] * (h1h2Max / 2 + this.flowSpacing);		
			}
		}
		else
		{
			if (this.rowNumber <= 1)
			{
				optimizer = new ExtendedQuadraticOptimizer(vN);
	
				for (int e = 0; e < eN; e++)
				{
					if (eV1[e] != eV2[e] && eFlow[e] && this.eDirected[e])
					{
						optimizer.addInequality(eV1[e], eV2[e], 
								this.G.vH[eV1[e]]/2 + this.G.vH[eV2[e]]/2 + this.flowSpacing);
					}
					else if (eV1[e] != eV2[e] && !eFlow[e] && this.eDirected[e])
					{
						optimizer.addInequality(eV2[e], eV1[e], 
								this.G.vH[eV1[e]]/2 + this.G.vH[eV2[e]]/2 + this.flowSpacing);
					}
					
					if (eV1[e] != eV2[e])
					{
						optimizer.addQuadraticDifference(eV1[e], eV2[e], 1);
					}
				}
	
				y = optimizer.performOptimization();
				
				double[] yMin = new double[this.compN];
				
				for (int k = 0; k < this.compN; k++)
				{
					yMin[k] = Double.MAX_VALUE;
				}
				
				for (int v = 0; v < vN; v++)
				{
					yMin[this.vCompIndex[v]] = Math.min(yMin[this.vCompIndex[v]], y[v]);
				}
				
				for (int v = 0; v < vN; v++)
				{
					y[v] -= yMin[this.vCompIndex[v]];
				}
			}
			else
			{	
				FlowLayoutGraph cg = this.buildRowConstraintGraph(vN, eN, eV1, eV2, eFlow);
				
				optimizer = new ExtendedQuadraticOptimizer(cg.n);
				
				for (int e = 0; e < cg.m; e++)
				{
					FlowLayoutEdge edge = (FlowLayoutEdge) cg.edgeList.get(e);
					int v1 = edge.v1;
					int v2 = edge.v2;
	
					optimizer.addQuadraticDifference(v1, v2, 1);
					
					if (v1 < vN && vN <= v2)
						optimizer.addInequality(v1, v2, this.G.vH[v1]/2);
					else if (vN <= v1 && v2 < vN)
						optimizer.addInequality(v1, v2, this.G.vH[v2]/2);
					else
						optimizer.addInequality(v1, v2, this.G.vH[v1]/2 + this.G.vH[v2]/2 + this.flowSpacing);
				}
				
				for (int e = 0; e < g.m; e++)
				{
					if (!this.eDirected[e])
					{
						optimizer.addQuadraticDifference(eV1[e], eV2[e], 1);
					}
				}
	
				y = optimizer.performOptimization();
			}
                    System.arraycopy(y, 0, g.vY, 0, g.n);
		}
	}

	
	/**
	 * This method builds a set of constraints for function minimizing stored as a graph.
	 * The adjusted row data are given via the corresponding class variables.
	 *
	 * @param vN the number of the diagram graph vertices (input).
	 * @param eN the number of the diagram graph edges (input).
	 * @param eV1 the diagram graph edge from vertex index (input).
	 * @param eV2 the diagram graph edge to vertex index (input).
	 * @param eFlow the flag indicating if edge direction coincides with
	 * the flow direction (input).
	 *
	 * @return the row constraint graph.
	 */
	private FlowLayoutGraph buildRowConstraintGraph(
		int vN, int eN, int[] eV1, int[] eV2, boolean[] eFlow)
	{
		FlowLayoutGraph cg = new FlowLayoutGraph(vN + this.rowNumber - 1);

		for (int v = 0; v < vN; v++)
		{
			if (-1 < this.nodePredeterminedRow[v] && this.nodePredeterminedRow[v] < this.rowNumber - 1)
			{
				cg.insertNewEdge(v, vN + this.nodePredeterminedRow[v]);
			}
			
			if (this.nodePredeterminedRow[v] > 0)
			{
				cg.insertNewEdge(vN + this.nodePredeterminedRow[v] - 1, v);
			}
		}
		
		cg.buildDirectedAdjacencyStructure();
		
		for (int e = 0; e < eN; e++)
		{
			if (eV1[e] != eV2[e] && eFlow[e] && this.eDirected[e])
			{
				cg.insertNewAcyclicEdge(eV1[e], eV2[e]);
			}
		}
		
		for (int e = 0; e < eN; e++)
		{
			if (eV1[e] != eV2[e] && !eFlow[e] && this.eDirected[e])
			{
				cg.insertNewAcyclicEdge(eV2[e], eV1[e]);
			}
		}
		
//		System.out.println();
//		System.out.println("cg.n = " + cg.n + " cg.m = " + cg.m);
//
//		for (int e = 0; e < cg.m; e++)
//		{
//			FlowLayoutEdge edge = (FlowLayoutEdge) cg.edgeList.get(e);
//			int v1 = edge.v1;
//			int v2 = edge.v2;
//			
//			System.out.println(v1 + " " + v2);
//		}
		
		return cg;
	}


	/**
	 * This method calculates the vertex x-coordinates of the given diagram graph.
	 * The coordinates are assigned at two stages: first, the initializing based on
	 * PQ-tree ordering is performed, second, the coordinates are postprocessed
	 * by the baricentric approach. 
	 * The adjusted column data are given via the corresponding class variables.
	 *
	 * @param g the graph to be laid out (input/output).
	 * @param eV1 the diagram graph edge from vertex index (input).
	 * @param eV2 the diagram graph edge to vertex index (input).
	 * @param nodeExternalX the average x-coordinate of the given vertex neighbors
	 * already positioned outside the nest (input).
	 */
	private void assignFlowLayoutHorizontalCoordinates(FlowLayoutGraph g, 
		int[] eV1, int[] eV2, 
		double[] nodeExternalX)
	{
		if (this.columnNumber > 1)
		{
			for (int col = 0; col < this.columnNumber; col++)
			{
				this.columnNodes.get(col).clear();
			}
	
			for (int v = 0; v < g.n; v++)
			{
				int col = this.nodePredeterminedColumn[v];
				
				if (col > -1)
				{
					this.columnNodes.get(col).add(v);
				}
			}
		}

		for (int v = 0; v < g.n; v++)
		{
			g.vX[v] = this.centerX;		
		}

		this.assignFlowLayoutHorizontalInitialPQTreeOrderingCoordinates(g);
		this.normalizeFlowLayoutHorizontalCoordinates(g, eV1, eV2);

//		this.assignFlowLayoutHorizontalInitialTreeDiameterPathOrderingCoordinates(g);
//		this.normalizeFlowLayoutHorizontalCoordinates(g);

		this.assignFlowLayoutHorizontalOrderingCoordinates(
				g, nodeExternalX, eV1, eV2);
		this.normalizeFlowLayoutHorizontalCoordinates(g, eV1, eV2);
	}


	/**
	 * This method calculates the vertex initial x-coordinates of the given diagram graph.
	 * The coordinates are assigned by the PQ-tree ordering method processing
	 * an auxiliary layered graph.
	 *
	 * @param g the graph to be laid out initially (input/output).
	 */
	private void assignFlowLayoutHorizontalInitialPQTreeOrderingCoordinates(
		final FlowLayoutGraph g)
	{
//		try
//		{
//			PrintWriter outEdges = new PrintWriter(new FileWriter("edges.txt"));
//
//	    	System.out.println("outEdges");
//
//	    	for (int e = 0; e < g.m; e++)
//		    {
//		    	int v1 = g.edgeList.get(e).v1;
//		    	int v2 = g.edgeList.get(e).v2;
//		    	
//		    	outEdges.println(v1 + " " + v2);
//		    }
//
//			outEdges.close();
//		}
//		catch(Exception e)
//		{
//			e.printStackTrace();
//		}

		int vN = g.n;
		int eN = g.m;
		Integer[] indV = new Integer[vN];
		int[] vLev = new int[vN];
	
		for (int v = 0; v < vN; v++)
		{
			indV[v] = v;		
		}

		Arrays.sort(indV, new Comparator<Integer>()
		{
                        @Override
			public int compare(Integer o1, Integer o2)
			{
				if (g.vY[o1] < g.vY[o2])
				{
					return -1;
				}
				else if(g.vY[o1] > g.vY[o2])
				{
					return 1;
				}
				
				return 0;
			}
		});

		vLev[indV[0]] = 0;
		
		for (int i = 1; i < vN; i++)
		{
			int v = indV[i];
			int vPrev = indV[i - 1];

			vLev[v] = vLev[vPrev];		
			
			if (Math.abs(g.vY[v] - g.vY[vPrev]) > this.flowSpacing)
			{
				vLev[v]++;		
			}
		}

		int levelNumber = vLev[indV[vN - 1]] + 1;

		LayeredGraph layeredGraph = new LayeredGraph(levelNumber);
		LayeredNode[] layeredNodeArray = new LayeredNode[vN];
		
		for (int v = 0; v < vN; v++)
		{
			LayeredNode levelNode = new LayeredNode(); 
			
			levelNode.setLevelNumber(vLev[v]);
			levelNode.setOriginalVertex(
					(FlowLayoutVertex) (((ArrayList) g.vertexList).get(v)));
			levelNode.setDummy(false);
			
			layeredNodeArray[v] = levelNode;
			layeredGraph.addLevelNode(vLev[v], levelNode);
		}

		for (int l = 0; l < layeredGraph.getNumberOfLevels(); l++)
		{
			ArrayList levelNodes = layeredGraph.getLevelNodeList(l);
		}
		
		for (int e = 0; e < eN; e++)
		{
			FlowLayoutEdge edge = g.edgeList.get(e);
			
			if (Math.abs(vLev[edge.v2] - vLev[edge.v1]) == 1)
			{	
				if (this.eFlow[e])
				{	
					layeredGraph.addEdge(
						layeredNodeArray[edge.v1], layeredNodeArray[edge.v2]);
				}
				else
				{	
					layeredGraph.addEdge(
						layeredNodeArray[edge.v2], layeredNodeArray[edge.v1]);
				}
			}
		}

//		for (int l = 0; l < levelNumber; l++)
//		{
//			System.out.print(l + ":");
//
//			ArrayList levelNodes = layeredGraph.getLevelNodeList(l);
//			
//			for (int k = 0; k < levelNodes.size(); k++)
//			{
//				LayeredNode levelNode = (LayeredNode) levelNodes.get(k);
//				int v = (levelNode.getOriginalNode()).getIndex();
//				LayeredEdge inEdge = levelNode.inEdge();
//				int vIn = -1;
//				if (inEdge != null)
//				{
//					LayeredNode inNode = inEdge.getOtherNode(levelNode);
//					vIn = inNode.getOriginalNode().getIndex();
//				}
//				System.out.print(" (" + v + " " + vIn + " (");
//				for (Iterator iter = levelNode.inOutEdgeIterator(); iter.hasNext();)
//				{
//					LayeredEdge incEdge = (LayeredEdge) iter.next();
//					LayeredNode adjNode = incEdge.getOtherNode(levelNode);
//					int vAdj = adjNode.getOriginalNode().getIndex();
//					System.out.print(" " + vAdj);
//				}
//				
//				System.out.print("))");
//			}
//			System.out.println();
//		}
		
		OrderingInput orderingInput = new OrderingInput();
		OrderingAlgorithm orderingAlgorithm = new OrderingAlgorithm();
		
		orderingInput.setLayeredGraph(layeredGraph);
		orderingAlgorithm.setInput(orderingInput);
		orderingAlgorithm.run();

//		for (int l = 0; l < levelNumber; l++)
//		{
//			System.out.print(l + ":");
//
//			ArrayList levelNodes = layeredGraph.getLevelNodeList(l);
//			
//			for (int k = 0; k < levelNodes.size(); k++)
//			{
//				int v = (((LayeredNode) levelNodes.get(k)).getOriginalNode()).getIndex();
//				System.out.print(" " + v);
//			}
//			System.out.println();
//		}

//		System.out.println("pec");
//
//		for (int l = 0; l < layeredGraph.getNumberOfLevels(); l++)
//		{
//			ArrayList levelNodes = layeredGraph.getLevelNodeList(l);
//			for (int k = 0; k < levelNodes.size(); k++)
//			{
//				LayeredNode node = (LayeredNode) levelNodes.get(k);
//				System.out.println(k + " " + node.getLevelNumber() + " " + node);
//				System.out.println(node.getOriginalNode());
//				
//				for (Iterator iter = node.inOutEdgeIterator(); iter.hasNext();)
//				{
//					LayeredEdge edge = (LayeredEdge) iter.next();
//					System.out.println("edge " + edge);
//					
//					LayeredNode otherNode = edge.getOtherNode(node);
//					System.out.println("otherNode " + otherNode);
//				}
//			}
//		}

		for (int l = 0; l < levelNumber; l++)
		{
			ArrayList levelNodes = layeredGraph.getLevelNodeList(l);
			
			for (int k = 0; k < levelNodes.size(); k++)
			{
				g.vX[(((LayeredNode) levelNodes.get(k)).getOriginalVertex()).getIndex()] = k;
			}
		}

		double xMin = Double.MAX_VALUE;
		double xMax = -Double.MAX_VALUE;
		
		for (int v = 0; v < g.n; v++)
		{
			xMin = Math.min(xMin, g.vX[v]);
			xMax = Math.max(xMax, g.vX[v]);
		}

		for (int v = 0; v < g.n; v++)
		{
			g.vX[v] += this.centerX - (xMin + xMax)/2;		
		}
	}


//	/**
//	 * This method assigns...
//	 */
//	private void assignFlowLayoutHorizontalInitialTreeDiameterPathOrderingCoordinates(
//			final FlowLayoutGraph g)
//	{
//		for (int v = 0; v < g.n; v++)
//		{
//			g.vX[v] = 0;		
//		}
//		
//		g.buildAdjacencyStructure();
//		
//		GraphProcessor graphProcessor = new GraphProcessor();
//
//		ArrayList<Integer> bfsList = graphProcessor.bfsSorting(g.adjStruct, 0);
//		ArrayList<FlowLayoutEdge> bfsTreeEdgeList = 
//			graphProcessor.calculateBFSTreeEdges(g.adjStruct, bfsList.get(g.n / 2));
//		
//		FlowLayoutGraph bfsTreeGraph = new FlowLayoutGraph(g.n);
//		
//		for (int k = 0; k < bfsTreeEdgeList.size(); k++)
//		{
//			FlowLayoutEdge edge = bfsTreeEdgeList.get(k);
//			
//			bfsTreeGraph.insertNewEdge(edge.v1, edge.v2);
//		}
//
//		bfsTreeGraph.buildAdjacencyStructure();
//		
//		ArrayList<Integer> path = 
//			graphProcessor.buildTreeDiameterPath(bfsTreeGraph.adjStruct); 
//		
//		FlowLayoutGraph workingGraph = new FlowLayoutGraph(bfsTreeGraph.n);
//		
//		for (int e = 0; e < bfsTreeGraph.m; e++)
//		{
//			FlowLayoutEdge edge = bfsTreeGraph.edgeList.get(e);
//			int v1 = edge.v1;
//			int v2 = edge.v2;
//			boolean isInPath = false;  
//			
//			for (int k = 1; k < path.size(); k++)
//			{
//				int pV1 = (int) path.get(k - 1);
//				int pV2 = (int) path.get(k);
//				
//				if ((v1 == pV1 && v2 == pV2) ||
//					(v1 == pV2 && v2 == pV1))
//				{
//					isInPath = true;
//					break;
//				}
//			}
//			
//			if (!isInPath)
//			{
//				workingGraph.insertNewEdge(v1, v2);
//			}
//		}
//		
//		int[] compVector = new int[bfsTreeGraph.n]; 
//		
//		workingGraph.buildAdjacencyStructure();
//		graphProcessor.calculateConnectedComponents(workingGraph.adjStruct, compVector);
//		
//		HashSet<Integer>[] components = new HashSet[path.size()];
//		int [] compIndex = new int[path.size()];
//
//		for (int c = 0; c < path.size(); c++)
//		{
//			components[c] = new HashSet<Integer>();
//		}
//
//		for (int v = 0; v < bfsTreeGraph.n; v++)
//		{
//			components[compVector[v]].add(v);
//		}
//
//		for (int c = 0; c < path.size(); c++)
//		{
//			for (int k = 0; k < path.size(); k++)
//			{
//				int pV = (int) path.get(k);
//				
//				if (components[c].contains(pV))
//				{
//					compIndex[c] = k;
//					break;
//				}
//			}
//		}
//
//		for (int v = 0; v < g.n; v++)
//		{
//			g.vX[v] = compIndex[compVector[v]];
//		}
//	}

	
	/**
	 * This method process the vertex initial x-coordinates of the given diagram graph.
	 * The coordinates are processed by the iterative baricentric approach.
	 * The connected components, and normalizing ensuring correct vertex spacings
	 * and column vertex ordering are managed at each iteration.   
	 * The adjusted column data are given via the corresponding class variables.
	 *
	 * @param g the graph to be laid out (input/output).
	 * @param nodeExternalX the average x-coordinate of the given vertex neighbors
	 * already positioned outside the nest (input).
	 * @param eV1 the diagram graph edge from vertex index (input).
	 * @param eV2 the diagram graph edge to vertex index (input).
	 */
	private void assignFlowLayoutHorizontalOrderingCoordinates(
			final FlowLayoutGraph g, double[] nodeExternalX,
			int[] eV1, int[] eV2)
	{
		int[] vInd = new int[g.n];
		Integer[] indV = new Integer[g.n];
	
		for (int v = 0; v < g.n; v++)
		{
			indV[v] = v;		
		}
	
		Arrays.sort(indV, new Comparator<Integer>()
		{
                        @Override
			public int compare(Integer o1, Integer o2)
			{
				if (g.vY[o1] < g.vY[o2])
				{
					return -1;
				}
				else if(g.vY[o1] > g.vY[o2])
				{
					return 1;
				}
				
				return 0;
			}
		});
	
		for (int i = 0; i < g.n; i++)
		{
			vInd[indV[i]] = i;		
		}

//			System.out.println("* " + spacing);
//			for (int v = 0; v < g.n; v++)
//			{
//				System.out.println(v + ": " + g.vX[v] + " " + g.vY[v]);
//			}

		g.buildAdjacencyStructure();
		
		double[] vXnew = new double[g.n];
		double wOut = 200;
	
		for (int iter = 0; iter < 20; iter++)
		{
			for (int ind = 0; ind < g.n; ind++)
			{
				int v = indV[ind];
				double wk = g.adjStruct.get(v).size();

				if (this.nodePredeterminedColumn[v] > -1) wk *= 10;
				
				vXnew[v] = wk*g.vX[v];
				
				for (int w : g.adjStruct.get(v))
				{
					if (vInd[w] < ind)
					{
                                                int wSize = g.adjStruct.get(w).size();
						vXnew[v] += wSize*g.vX[w];
						wk += wSize;
					}
				}
				
				if (Double.isNaN(nodeExternalX[v]))
				{
					if (wk > 0)
					{
						vXnew[v] /= wk;
					}
					else
					{
						vXnew[v] = g.vX[v];
					}
				}
				else
				{
					vXnew[v] += wOut*nodeExternalX[v];
					vXnew[v] /= wOut + wk;
				}
			}
                    System.arraycopy(vXnew, 0, g.vX, 0, g.n);

			this.separateComponents(g.vX);
	        
			this.normalizeFlowLayoutHorizontalCoordinates(g, eV1, eV2);
		
			for (int ind = g.n - 1; ind > -1; ind--)
			{
				int v = indV[ind];
				double wk = g.adjStruct.get(v).size();

				if (this.nodePredeterminedColumn[v] > -1) wk *= 10;
				
				vXnew[v] = wk*g.vX[v];
				
				for (int w : g.adjStruct.get(v))
				{
					if (vInd[w] > ind)
					{
                                                int wSize = g.adjStruct.get(w).size();
						vXnew[v] += wSize*g.vX[w];
						wk += wSize;
					}
				}
				
				if (Double.isNaN(nodeExternalX[v]))
				{
					if (wk > 0)
					{
						vXnew[v] /= wk;
					}
					else
					{
						vXnew[v] = g.vX[v];
					}
				}
				else
				{
					vXnew[v] += wOut*nodeExternalX[v];
					vXnew[v] /= wOut + wk;
				}
			}
                    System.arraycopy(vXnew, 0, g.vX, 0, g.n);

			this.separateComponents(g.vX);
	        
			this.normalizeFlowLayoutHorizontalCoordinates(g, eV1, eV2);
		}
	}


	/**
	 * This method separates the connected components in horizontal direction.   
	 * The component structure is given via the corresponding class variables.
	 *
	 * @param x the x-coordinate of the given vertex (input/output).
	 */
	private void separateComponents(double[] x)
	{
		int vN = this.vCompIndex.length;
		double[] xMin = new double[this.compN];
		double[] xMax = new double[this.compN];
		double[] compX = new double[this.compN];
		
		for (int k = 0; k < this.compN; k++)
		{
			xMin[k] = Double.MAX_VALUE;
			xMax[k] = -Double.MAX_VALUE;
		}
		
		for (int v = 0; v < vN; v++)
		{
			int cInd = this.vCompIndex[v];

			xMin[cInd] = Math.min(xMin[cInd], x[v]);
			xMax[cInd] = Math.max(xMax[cInd], x[v]);
		}
		
		for (int v = 0; v < vN; v++)
		{
			x[v] -= xMin[this.vCompIndex[v]];
		}
		
		compX[this.sortIndComp[0]] = 0;
		
		for (int k = 1; k < this.compN; k++)
		{
			compX[this.sortIndComp[k]] = compX[this.sortIndComp[k - 1]] +
				xMax[this.sortIndComp[k - 1]] - xMin[this.sortIndComp[k - 1]] + 1;
		}
		
		for (int v = 0; v < vN; v++)
		{
			x[v] += compX[this.vCompIndex[v]];
		}
	}


	/**
	 * This method normalizes vertex x-coordinates ensuring correct vertex spacings
	 * and column vertex ordering.   
	 * The adjusted column data are given via the corresponding class variables.
	 *
	 * @param g the graph to be laid out (input/output).
	 * @param eV1 the diagram graph edge from vertex index (input).
	 * @param eV2 the diagram graph edge to vertex index (input).
	 */
	private void normalizeFlowLayoutHorizontalCoordinates(FlowLayoutGraph g, int[] eV1, int[] eV2)
	{
		if (this.columnNumber > 1)
        {
        	this.repairColumns(g);
        }

//		System.out.println("normalize *");
//		for (int v = 0; v < g.n; v++)
//		{
//			System.out.println(g.vX[v] + " " + g.vY[v] + " " + g.vW[v] + " " + g.vH[v]);
//		}

		int csN = (this.columnNumber == 0)? 0: this.columnNumber - 1;
		
		ExtendedQuadraticOptimizer optimizer = new ExtendedQuadraticOptimizer(g.n + csN);

		for (int e = 0; e < g.m; e++)
		{
			optimizer.addQuadraticDifference(eV1[e], eV2[e], 1);
		}

		if (csN > 0)
		{
			for (int col = 0; col < this.columnNumber - 1; col++)
			{
				for (int k = 0; k < this.columnNodes.get(col).size(); k++)
				{
					int v = this.columnNodes.get(col).get(k);
					
					optimizer.addInequality(v, g.n + col, g.vW[v]/2);
				}
			}

			for (int col = 1; col < this.columnNumber; col++)
			{
				for (int k = 0; k < this.columnNodes.get(col).size(); k++)
				{
					int v = this.columnNodes.get(col).get(k);
					
					optimizer.addInequality(g.n + col - 1, v, g.vW[v]/2);
				}
			}
		}
		
		ArrayList<Segment> segments = new ArrayList<>();
		HashMap<Segment, Integer> segmentIndexMap = new HashMap<>();

		for (int v = 0; v < g.n; v++)
		{
			Segment s = new Segment(g.vX[v], g.vY[v] - g.vH[v] / 2, g.vY[v] + g.vH[v] / 2);
			
			segments.add(s);
			segmentIndexMap.put(s, v);
		}
		
		ArrayList<Pair<Segment, Segment>> segmentPairList =
			                     ObstacleGraph.findObstacleGraph(segments);

		for (int k = 0; k < segmentPairList.size(); k++)
		{
			Pair<Segment, Segment> segmentPair = segmentPairList.get(k);
			int v1 = (int) segmentIndexMap.get(segmentPair.getFirst());
			int v2 = (int) segmentIndexMap.get(segmentPair.getSecond());
			
			optimizer.addInequality(v1, v2,	g.vW[v1]/2 + g.vW[v2]/2 + 10);
		}

		double[] x = optimizer.performOptimization();
            System.arraycopy(x, 0, g.vX, 0, g.n);
	        
//			System.out.println("normalize **");
//			for (int v = 0; v < g.n; v++)
//			{
//				System.out.println(g.vX[v] + " " + g.vY[v] + " " + g.vW[v] + " " + g.vH[v]);
//			}

		double xMin = Double.MAX_VALUE;
		double xMax = -Double.MAX_VALUE;
		
		for (int v = 0; v < g.n; v++)
		{
			xMin = Math.min(xMin, g.vX[v]);
			xMax = Math.max(xMax, g.vX[v]);
		}

		for (int v = 0; v < g.n; v++)
		{
			g.vX[v] += this.centerX - (xMin + xMax)/2;		
		}
        
//		System.out.println("normalize ***");
//		for (int v = 0; v < g.n; v++)
//		{
//			System.out.println(g.vX[v]);
//		}
	}


	/**
	 * This method recalculates vertex x-coordinates ensuring correct
	 * column vertex ordering.
	 * The adjusted column data are given via the corresponding class variables.
	 *
	 * @param g the graph to be laid out (input/output).
	 */
	private void repairColumns(final FlowLayoutGraph g)
	{
//		System.out.println("repair*");
//		for (int v = 0; v < g.n; v++)
//		{
//			System.out.println(g.vX[v] + " " + g.vW[v]);
//		}

		int columnNodeNumber = 0;

		for (int col = 0; col < this.columnNumber; col++)
		{
			columnNodeNumber +=this.columnNodes.get(col).size();
		}
		
		double[] colomnNodeXArray = new double[columnNodeNumber]; 
		int colomnNodeXArrayIndex = 0; 

		for (int col = 0; col < this.columnNumber; col++)
		{
			for (int k = 0; k < this.columnNodes.get(col).size(); k++)
			{
				colomnNodeXArray[colomnNodeXArrayIndex++] = g.vX[(int) this.columnNodes.get(col).get(k)];
			}
		}
		
		Arrays.sort(colomnNodeXArray);

		for (int k = 1; k < columnNodeNumber; k++)
		{
			colomnNodeXArray[k] = Math.max(colomnNodeXArray[k - 1] + 1, colomnNodeXArray[k]);
		}

		colomnNodeXArrayIndex = 0; 

		for (int col = 0; col < this.columnNumber; col++)
		{
			for (int k = 0; k < this.columnNodes.get(col).size(); k++)
			{
				g.vX[(int) this.columnNodes.get(col).get(k)] = colomnNodeXArray[colomnNodeXArrayIndex++];
			}
		}

//		System.out.println("repair**");
//		for (int v = 0; v < g.n; v++)
//		{
//			System.out.println(g.vX[v] + " " + g.vW[v]);
//		}
	}

	
	/**
	 * This is an auxiliary method for intersection calculation of two line segments.   
	 *
	 * @param rx1 the x-coordinate of the first end of the first segment (input).
	 * @param ry1 the y-coordinate of the first end of the first segment (input).
	 * @param rx2 the x-coordinate of the second end of the first segment (input).
	 * @param ry2 the y-coordinate of the second end of the first segment (input).
	 * @param sx1 the x-coordinate of the first end of the second segment (input).
	 * @param sy1 the y-coordinate of the first end of the second segment (input).
	 * @param sx2 the x-coordinate of the second end of the second segment (input).
	 * @param sy2 the y-coordinate of the second end of the second segment (input).
	 *
	 * @return the flag indicating if the given segments intersect.
	 */
	private boolean SegSegIntersec(double rx1, double ry1, double rx2, double ry2,
			double sx1, double sy1, double sx2, double sy2)
		{
		 double eps=0.001;
		 double rx=rx2-rx1, ry=ry2-ry1;
		 double sx=sx2-sx1, sy=sy2-sy1;
		 double d=sx*ry-sy*rx;
		 double d1=(rx1-sx1)*ry-(ry1-sy1)*rx;
		 double d2=(rx1-sx1)*sy-(ry1-sy1)*sx;
		 
		 if (Math.abs(d) < eps) return false;

		 boolean b;

		 if (d > 0)
		 	b=((d1 >= eps) && (d1 <= d-eps) && (d2 >= eps) && (d2 <= d-eps));
		 else
		 	b=((d1 <= -eps) && (d1 >= d+eps) && (d2 <= -eps) && (d2 >= d+eps));

		 return b;
		}
}
