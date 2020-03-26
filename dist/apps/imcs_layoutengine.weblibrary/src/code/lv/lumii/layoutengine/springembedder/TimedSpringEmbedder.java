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


//import java.awt.Graphics;
import java.util.List;
import java.util.Random;
//import javax.swing.JFrame;
import lv.lumii.layoutengine.AbstractContainer;
import lv.lumii.layoutengine.LayoutConstraints;
import lv.lumii.layoutengine.LayoutConstraints.GridLayoutConstraints;
import lv.lumii.layoutengine.util.LayoutLine;
import lv.lumii.layoutengine.funcmin.QuadraticOptimizer;


/**
 * This class defines the spring embedder class.
 * The spring embedder layout uses a heuristical force directed
 * algorithm for producing graph layouts. There are two kinds of forces:
 * repulsive between any two nodes, and attractive between nodes
 * connected with an edge. The equilibruim of these forces defines the
 * layout.
 */

public final class TimedSpringEmbedder
{

	/**
	 * This constructor creates a spring embedder class with n nodes and m
	 * edges. It also initializes the random generator with the defined
	 * seed.
	 *
	 * @param nodes         Number of nodes
	 * @param edges         Number of edges
	 * @param RandSeed  Random seed
	 */

	public TimedSpringEmbedder(int nodes, int edges, int RandSeed)
	{
		this.nodeCount = nodes;
		this.edgeCount = edges;
		force = new double[nodes][4];
		oldForce = new double[nodes][3];

		// create the nodes and edges
		this.nodes = new SpringNode[nodes];
		this.edges = new SpringEdge[edges];

		for (int i = 0 ; i < nodes ; i++)
		{
			this.nodes[i] = new SpringNode();
		}

		for (int i = 0 ; i < edges ; i++)
		{
			this.edges[i] = new SpringEdge();
		}

		//allocate space for the random permutation
		this.nodePermutation = new int[nodes];

		// allocate the graph structure
		this.g = new GraphAdjacency(nodes, edges * 2);

		// create the random generator with the specified seed
		this.randomGenerator = new Random(RandSeed);
	}


	/**
	 *
	 * @param spacing           desired separation of nodes (input)
	 * @param nodeX              x coordinates of node centers (output,
	 * 						 	input when incr=true)
	 * @param nodeY              y coordinates of node centers (output,
	 * 							 input when incr=true)
	 * @param nodeRadius  node radius (input)
	 * @param edgeSourceId             from node index of the edges (input)
	 * @param edgeTargetId             to node index of the edges (input)
	 * @param incremental     incremental mode
	 * @param lockedNodes     flag if the node is locked
         * @param edgeStrength the strength of each edge. Default is 1.
         * @param realNodeCount all anodes with indices bigger than this are 
         * considered to be dummy. Repulsive forces are not calculated for dummy nodes.
	 * all edges
	 */
	public void layout(double spacing,
		double nodeX[], double nodeY[],
		double nodeRadius[],
		int edgeSourceId[], int edgeTargetId[],
		boolean incremental,
		boolean[] lockedNodes,
		double degreeModifier,
		double [] edgeStrength,
                int realNodeCount,
                List<AbstractContainer> boxes,
                LayoutConstraints constr,
                List<LayoutLine> layoutLines)
	{
		//some simple cases
		if (nodeCount <= 0)
		{
			return;
		}

		if (nodeCount == 1)
		{
			nodeX[0] = nodeY[0] = 0;
			return;
		}
                
                this.layoutLines = layoutLines;                
                this.boxes = boxes.toArray(new AbstractContainer[0]);
                this.constraints = constr;               
                this.realNodesCount = realNodeCount;

		initLayout(spacing, nodeX, nodeY, nodeRadius,
			edgeSourceId, edgeTargetId, incremental, lockedNodes,
			degreeModifier, edgeStrength);

                // reset grid data
                resetGrid();
                
		// do the job
		iterations();

		// transfer back the node coordinates
		for (int i = 0 ; i < nodeCount ; i++)
		{
			if (!isNodeLocked[i])
			{
				nodeX[i] = nodes[i].x;
				nodeY[i] = nodes[i].y;
			}
		}
	}
        
        /**
         * initialize grid rows and columns to ensure layout stability
         */
        private void resetGrid()
        {
            if(this.constraints==null || this.constraints.getType() == LayoutConstraints.ConstraintType.NONE) return;
            
            if(this.constraints instanceof GridLayoutConstraints)
            {
                GridLayoutConstraints constr = (GridLayoutConstraints) this.constraints;

                // set row and column positions to 0
                for(int i=0;i<constr.getColumnCount()-1;i++)
                {
                    constr._setColumnRight(i+1, 0);
                }

                for(int i=0;i<constr.getRowCount()-1;i++)
                {
                    constr._setRowBottom(i+1, 0);
                }

            }
        }


	/**
	 * does only initalization part of layout
	 * @param spacing           desired separation of nodes (input)
	 * @param nodeX              x coordinates of node centers (output,
	 * 						 	input when incr=true)
	 * @param nodeY              y coordinates of node centers (output,
	 * 							 input when incr=true)
	 * @param nodeRadius  node radius (input)
	 * @param edgeSourceId             from node index of the edges (input)
	 * @param edgeTargetId             to node index of the edges (input)
	 * @param incremental     incremental mode
	 * @param lockedNodes     flag if the node is locked
	 * all edges
	 */
	public void initLayout(double spacing,
		double nodeX[], double nodeY[],
		double nodeRadius[],
		int edgeSourceId[], int edgeTargetId[],
		boolean incremental,
		boolean[] lockedNodes,
		double degreeModifier,
		double [] edgeStrength)
	{
		//some simple cases
		if (nodeCount <= 0)
		{
			return;
		}

		isNodeLocked = lockedNodes;
		this.nodeDegreeModifier = degreeModifier;

		if(isNodeLocked == null)
		{
			isNodeLocked = new boolean[nodeCount];
		}

		// calculate the radius of the nodes
		for (int i = 0 ; i < nodeCount ; i++)
		{
			nodes[i].r = nodeRadius[i] + spacing;
			if (nodes[i].r < 1)
			{
				nodes[i].r = 1;
			}
		}

		// transfer edges to the internal structures
		// remove self loops and multi-edges
		transferEdges(edgeSourceId, edgeTargetId, edgeStrength);

		this.incremental = incremental;

		// trasfer the node coordinates for the incremental layout
		// add some random number to be sure that no two nodes are at the
		// same position

		for (int i = 0 ; i < nodeCount ; i++)
		{
			nodes[i].x = nodeX[i];// + randomGenerator.nextDouble()-0.5;
			nodes[i].y = nodeY[i];// + randomGenerator.nextDouble()-0.5;
		}

		//allocate space for the random permutation
		this.nodePermutationN = new SpringNode[this.realNodesCount];
		System.arraycopy(nodes, 0, nodePermutationN, 0, realNodesCount);

		// claculate various values
		prepareGraph();
	}


	/**
	 * updates the graph data
	 * @param spacing           desired separation of nodes (input)
	 * @param nodeX              x coordinates of node centers (output,
	 * 						 	input when incr=true)
	 * @param nodeY              y coordinates of node centers (output,
	 * 							 input when incr=true)
	 * @param nodeRadius         node radius (input)
	 * @param lockedNodes     flag if the node is locked
	 * higher temperature means that node is moving slower
	 */
	public void updateLayoutData(double spacing,
		double nodeX[], double nodeY[],
		double nodeRadius[],
		boolean[] lockedNodes,
		double degreeModifier,
		double [] edgeStrength)
	{
		isNodeLocked = lockedNodes;
		this.nodeDegreeModifier = degreeModifier;

		if(isNodeLocked == null)
		{
			isNodeLocked = new boolean[nodeCount];
		}

		// calculate the radius of the nodes
		for (int i = 0 ; i < nodeCount ; i++)
		{
			nodes[i].r = nodeRadius[i] + spacing;

			if (nodes[i].r < 1)
			{
				nodes[i].r = 1;
			}
		}


		// trasfer the node coordinates for the incremental layout
		// add some random number to be sure that no two nodes are at the
		// same position

		for (int i = 0 ; i < nodeCount ; i++)
		{
			nodes[i].x = nodeX[i];
			nodes[i].y = nodeY[i];
		}

		for (int i = 0 ; i < edgeCount ; i++)
		{
			edges[i].strength = edgeStrength[i];
		}

		// claculate various values
		adjustNodeRadius();
	}

	public void timedLayout(double nodeX[], double nodeY[], double time)
	{
		//calculate the iteration count
		int iter;
		//forceReduction
		temperature1 = 1;

		if(time < 0.1)
		{
			iter = 2;
			forceReductionFactor = Math.min(forceReductionFactor, time*10);
			//temperature1 = Math.max(1, 0.04/Math.max(0.001, time));
		}
		else
		{
			//temperature1 = 1.0/(time*9+0.1);
			iter = (int) (2 + time*10);
		}

		temperature = unitTemperature;

		while(iter > 0)
		{
			relax();
			iter--;
		}

		// transfer back the node coordinates
		for (int i = 0 ; i < nodeCount ; i++)
		{
			if (!isNodeLocked[i])
			{
				nodeX[i] = nodes[i].x;
				nodeY[i] = nodes[i].y;
			}
		}
	}

    /**
     * This method ensures grid constraints in vertical direction
     * @param constr the grid constraints
     */    
    private void ensureConstraintsY(GridLayoutConstraints constr) {
        int rowCount = constr.getRowCount();
        
        if(rowCount>1)
        {
            QuadraticOptimizer funcmin = new QuadraticOptimizer(nodeCount+rowCount-1);

            // keep positions
            for(int i=0;i<nodeCount;i++)
            {
                funcmin.addQuadraticConstantDifference(i, nodes[i].y, 1);
                funcmin.setVariable(i, nodes[i].y);
            }
            
            // constraints to be in the right grid cell
            for(int i=0;i<this.realNodesCount;i++)
            {
                Integer row = constr.getRow(boxes[i]);
                
                if(row != null)
                {
                    if(row>1) funcmin.addInequality(row-2+nodeCount, i, nodes[i].r+constr.getRowBorderSpacing(row-1)); // do not add for first row
                    if(row<rowCount) funcmin.addInequality(i, row-1+nodeCount, nodes[i].r+constr.getRowBorderSpacing(row)); // do not add for last row                            
                }
            }

            // initialize row positions and minimum distance
            for(int i=0;i<rowCount-1;i++)
            {
                funcmin.setVariable(i+nodeCount, constr.getRowBottom(i+1));                        
            }

            for(int i=0;i<rowCount-2;i++)
            {
                funcmin.addInequality(i+nodeCount, i+nodeCount+1, constr.getRowMinHeight(i+2));
            }
            
            // perform optimization
            funcmin.setEpsilon(this.epsilon);
            double[] y = funcmin.performOptimization();

            // transfer result
            for(int i=0;i<nodeCount;i++)
            {
                SpringNode n = nodes[i];                        
                double dy = y[i] - n.y;

                // reduce the movement vector to ensure convergence
                if (Math.abs(dy) > n.r) n.dy = 0; 
                else n.dy += dy / 2;

                n.y = y[i];
            }
            
            // set row positions                    
            for(int i=0;i<rowCount-1;i++)
            {
                constr._setRowBottom(i+1, y[i+nodeCount]);
            }
        }
    }
    
    /**
     * This method ensures grid constraints in horizontal direction
     * @param constr the grid constraints
     */    
    private void ensureConstraintsX(GridLayoutConstraints constr) {
        int columnCount = constr.getColumnCount();
        
        if(columnCount>1)
        {
            QuadraticOptimizer funcmin = new QuadraticOptimizer(nodeCount+columnCount-1);

            // keep positions
            for(int i=0;i<nodeCount;i++)
            {
                funcmin.addQuadraticConstantDifference(i, nodes[i].x, 1);
                funcmin.setVariable(i, nodes[i].x);
            }
                       
            // constraints to be in the right grid cell
            for(int i=0;i<this.realNodesCount;i++)
            {
                Integer column = constr.getColumn(boxes[i]);                
                
                if(column != null)
                {
                    if(column>1) funcmin.addInequality(column-2+nodeCount, i, nodes[i].r+constr.getColumnBorderSpacing(column-1)); // do not add for first row
                    if(column<columnCount) funcmin.addInequality(i, column-1+nodeCount, nodes[i].r+constr.getColumnBorderSpacing(column)); // do not add for last row                            
                }
            }

            // initialize row positions and minimum distance
            for(int i=0;i<columnCount-1;i++)
            {
                funcmin.setVariable(i+nodeCount, constr.getColumnRight(i+1));                        
            }

            for(int i=0;i<columnCount-2;i++)
            {
                funcmin.addInequality(i+nodeCount, i+nodeCount+1, constr.getColumnMinWidth(i+2));
            }
            
            // perform optimization
            funcmin.setEpsilon(this.epsilon);
            double[] x = funcmin.performOptimization();

            // transfer result
            for(int i=0;i<nodeCount;i++)
            {
                SpringNode n = nodes[i];                        
                double dx = x[i] - n.x;

                // reduce the movement vector to ensure convergence
                if (Math.abs(dx) > n.r) n.dx = 0; 
                else n.dx += dx / 2;

                n.x = x[i];
            }
            
            // set row positions                    
            for(int i=0;i<columnCount-1;i++)
            {
                constr._setColumnRight(i+1, x[i+nodeCount]);
            }
        }
    }


	/**
	 * This method does some preparation work including the initial layout.
	 */
	private void prepareGraph()
	{
		for (int i = 0 ; i < edgeCount ; i++)
		{
			g.addUndirectEdge(edges[i].from, edges[i].to);
		}

		double R = this.adjustNodeRadius();

		// make starting layout by randomly distributing nodes on a circle
		randPerm(nodeCount);
                
                double sumR = 0;

		for (int i = 0 ; i < nodeCount ; i++)
		{
			nodes[i].z = 0;
                        sumR+=nodes[i].r;

			if ((!isNodeLocked[i]) && (!incremental))
			{
				nodes[i].x = R * Math.cos(nodePermutation[i] *
					2 * Math.PI / nodeCount);
				nodes[i].y = R * Math.sin(nodePermutation[i] *
					2 * Math.PI / nodeCount);
				nodes[i].z = randomGenerator.nextDouble() - 0.5;
			}

			nodes[i].dx = nodes[i].dy = nodes[i].dz = 0;
		}
                
                graphSizeWeight = (1.0/50.0)/Math.sqrt(R+100);
                epsilon = Math.max(0.000001, sumR/nodeCount*0.001);
	}

	private double adjustNodeRadius()
	{
		// adjust node sized depending on the degree
		// at first calculate the node degree

		int[] deg = new int[nodeCount];

		for (int i = 0 ; i < edgeCount ; i++)
		{
			deg[edges[i].from]++;
			deg[edges[i].to]++;
		}

		//adjust node radiuses
		for (int i = 0 ; i < realNodesCount ; i++)
		{
			nodes[i].fsum = 0;
			int sum = 0;
			int e = g.nodestart[i];

			while (e >= 0)
			{
				int enode = g.enodes[e];
				sum += deg[enode];
				e = g.next[e];
			}

			double w = Math.sqrt(sum);
			if (w > 4)
			{
				nodes[i].r += (w - 4) * nodes[i].r / 4 * this.nodeDegreeModifier;
			}
		}

		//calculate edge length and spring constants
		// as well as unit temperature
		unitTemperature = 0;

		for (int i = 0 ; i < edgeCount ; i++)
		{
			SpringEdge e = edges[i];
                        
                        if(e.from<realNodesCount && e.to<realNodesCount)
                            e.len = nodes[e.from].r + nodes[e.to].r;
                        else 
                            e.len = 10; // set unit length for edges adjacent to dummy nodes
                        
			double strength = Math.min(20, e.strength);
			e.K = strength * 30 / e.len;

			if (e.from == e.to)
			{
				e.K = 0;
			}

			nodes[e.from].fsum += e.K;
			nodes[e.to].fsum += e.K;
			unitTemperature += e.len;
		}

		double R;
                if(edgeCount>0)
                {
                    R= unitTemperature / 5;
                    unitTemperature /= edgeCount * 30;
                }else
                {
                    R= 20;
                    unitTemperature = 1;
                }

		return R;
	}


	/**
	 * This method calculates the new position of the specified node.
	 *
	 * @param nnum  node number to be processed
	 */
	private void moveNode(int nnum)
	{
		//get the node
		SpringNode n1 = nodes[nnum];

		// take the reduced old force vector
		double dx = n1.dx * 0.5;
		double dy = n1.dy * 0.5;
		double dz = n1.dz * 0.5;

		// calculate the attractive forces
		int e = g.nodestart[nnum];

		while (e >= 0)
		{
			SpringNode n2 = nodes[g.enodes[e]];
                        SpringEdge edge = edges[e >> 1];
                        int sign = ((e & 1)<<1)-1;

			double vx = n1.x - n2.x+edge.dx*sign;
			double vy = n1.y - n2.y+edge.dy*sign;
			double vz = n1.z - n2.z;
			double len = Math.sqrt(vx * vx + vy * vy + vz * vz);

			if (len < 0.01)
			{
				len = 0.01;
			}
			double f = -0.1* edge.K;//(edge.len - len) * edge.K / len;

			dx += f * vx;
			dy += f * vy;
			dz += f * vz;
			e = g.next[e];
		}

		dx += n1.repulsiveForceX * 15;
		dy += n1.repulsiveForceY * 15;
		dz += n1.repulsiveForceZ * 15;
                
                // add force to center                
                dx -= n1.x*graphSizeWeight;
                dy -= n1.y*graphSizeWeight;

		//add attraction force to the plane z = 0
		//double kz = temperature1 * 30 / n1.r;
		double kz = (this.temperature1) / 7;
		dz -= n1.z * kz;

		//save the force
		n1.dx = dx;
		n1.dy = dy;
		n1.dz = dz;

		//scale the force
		double fsum = 1.0 / (n1.fsum + kz);

		dx *= fsum;
		dy *= fsum;
		dz *= fsum;

		if(!incremental)
		{
			// if the force is too small then increase it
			double len = dx * dx + dy * dy + dz * dz;

			if (len < temperature * temperature && len > 0.001)
			{
				len = temperature / Math.sqrt(len);
				dx *= len;
				dy *= len;
				dz *= len;
			}
		}

		// add some randomness depending from the temperature
		if(!incremental)
		{
			n1.x += (randomGenerator.nextDouble() - 0.5) * temperature * 0.5;
			n1.y += (randomGenerator.nextDouble() - 0.5) * temperature * 0.5;
			n1.z += (randomGenerator.nextDouble() - 0.5) * temperature * 0.5;
		}

		// actually move the node
		n1.x += dx * forceReductionFactor;
		n1.y += dy * forceReductionFactor;
		n1.z += dz * forceReductionFactor;
	}


	/**
	 * This method calculates the new position of the specified node.
	 *
	 * @param nnum  node number to be processed
	 */
	private void calculateForce(int nnum)
	{
		//get the node
		SpringNode n1 = nodes[nnum];

		// take the reduced old force vector
		double dx = 0;
		double dy = 0;
		double dz = 0;

		// calculate the attractive forces
		int e = g.nodestart[nnum];

		while (e >= 0)
		{
			SpringNode n2 = nodes[g.enodes[e]];
                        SpringEdge edge = edges[e >> 1];
                        int sign = ((e & 1)<<1)-1;

			double vx = n1.x - n2.x+edge.dx*sign;
			double vy = n1.y - n2.y+edge.dy*sign;
			double vz = n1.z - n2.z;
			double len = Math.sqrt(vx * vx + vy * vy + vz * vz);

			if (len < 0.01)
			{
				len = 0.01;
			}
			double f = (edge.len - len) * edge.K / len;

			dx += f * vx;
			dy += f * vy;
			dz += f * vz;
			e = g.next[e];
		}

		dx += n1.repulsiveForceX * 15;
		dy += n1.repulsiveForceY * 15;
		dz += n1.repulsiveForceZ * 15;

		//add attraction force to the plane z = 0
		//double kz = temperature1 * 30 / n1.r;
		double kz = (this.temperature1) / 7;
		dz -= n1.z * kz;

		//scale the force
		double fsum = 1.0 / (n1.fsum + kz);

		//save the force
		force[nnum][0] = dx;
		force[nnum][1] = dy;
		force[nnum][2] = dz;
		force[nnum][3] = fsum;
	}

	void moveNodes()
	{
		for(int i=0;i<nodeCount;i++)
		{
			this.calculateForce(i);
		}

		double scal = 0;
		double len = 0;
		boolean freezedNodes [] = new boolean[nodeCount];

		//double oldLen = 0;

		for(int i=0;i<nodeCount;i++)
		{
			if (!this.isNodeLocked[i])
			{
				SpringNode n1 = nodes[i];
				double dx = force[i][0];
				double dy = force[i][1];
				double dz = force[i][2];
				double fsum = force[i][3];

				n1.dx = dx + n1.dx * 0.0;
				n1.dy = dy + n1.dy * 0.0;
				n1.dz = dz + n1.dz * 0.0;

				double curLen = force[i][0] * force[i][0] + force[i][1] * force[i][1] + force[i][2] * force[i][2];

				if (curLen*fsum * fsum > unitTemperature * unitTemperature * 0.05*0.05)
				{
					len += curLen;
					scal += oldForce[i][0] * force[i][0] + oldForce[i][1] * force[i][1] + oldForce[i][2] * force[i][2];
				}
				else
				{
					freezedNodes[i] = true;
				}
				if (curLen > fsum * fsum * 10 * 10)
				{
					//defreeze all
					//this.forceReductionFactor = Math.max(this.forceReductionFactor,0.5);
				}
				//System.out.println("curLen = " + curLen + " " + fsum);
				//scal += n1.dx *force[i][0]+n1.dy *force[i][1] + n1.dz*force[i][2];
				//oldLen += n1.dx *n1.dx + n1.dy *n1.dy + n1.dz*n1.dz;
			}
		}
		//System.out.println("freezedCount = " + freezedCount);
		//System.out.println("scal = " + scal);

		if(scal  > 1E-7)
		{
			forceReductionFactor *= 1 + 0.4 / 1.618033989;
		}
		else if(scal  < -1E-7)
		{
			forceReductionFactor /= 1.4;
		}
		//System.out.println("scal = " + scal);
		//System.out.println("len = " + len);

		forceReductionFactor = Math.min(1, forceReductionFactor);
		forceReductionFactor  = Math.max(0.002, forceReductionFactor);
		//System.out.println("forceReductionFactor = " + forceReductionFactor);

		for(int i=0;i<nodeCount;i++)
		{
			// actually move the node
			SpringNode n1 = nodes[i];

			double fsum = force[i][3];

            if(!isNodeLocked[i] && !freezedNodes[i])
			{
				n1.x += n1.dx*fsum * forceReductionFactor;
				n1.y += n1.dy*fsum * forceReductionFactor;
				n1.z += n1.dz*fsum * forceReductionFactor;
			}

			oldForce[i][0] = force[i][0];
			oldForce[i][1] = force[i][1];
			oldForce[i][2] = force[i][2];
		}


	}


	/* This method
	 * generates a random permutation of n integers
	 *
	 * @param n  Permutation length
	 */

	private void randPerm(int n)
	{
		for (int i = 0 ; i < n ; i++)
		{
			nodePermutation[i] = i;
		}

		for (int i = 0 ; i < n ; i++)
		{
			int i1 = randomGenerator.nextInt(n - i) + i;
			int tmp = nodePermutation[i];
			nodePermutation[i] = nodePermutation[i1];
			nodePermutation[i1] = tmp;
		}
	}


	/**
	 * This method makes one pass over the nodes moving the nodes to the
	 * new places
	 */

	private void relax()
	{            
            // do it in a random order
            this.randomNodePermutation();

            this.repulsiveForceTree.temperature = this.temperature1/4;
            this.repulsiveForceTree.buildTree(this.nodePermutationN, this.realNodesCount);

            if(!incremental)
            {
                for (int i = 0; i < nodeCount; i++)
                {
                    // do something only if the node is not locked
                    if (!isNodeLocked[i])
                    {
                        // move the node
                        moveNode(i);
                    }
                }
            }
            else
            {
                this.moveNodes();
            }
            
            ensureConstraints();
            draw();
	}


	/**
	 * This method makes one pass over the nodes moving the nodes to the
	 * new places.
	 */

	private void iterations()
	{
		//calculate the iteration count
		int step = (int) (2 * Math.sqrt(nodeCount) + 10);
		int iter = step * 2;

		//calculate the strarting temperature
		// in the incremental mode it is smaller
		if (incremental)
		{
			temperature = 10 * unitTemperature;
			temperature1 = 0.7;
		}
		else
		{
			temperature = (Math.sqrt(nodeCount) * 2 + 20) *
				unitTemperature;
			temperature1 = 0.1;
		}
                
                double finalTemperature = unitTemperature;
                
                if(constraints != null && constraints.getType() != LayoutConstraints.ConstraintType.NONE)
                {
                    step *= 1.5;
                    finalTemperature *= 0.5;
                }

		// do the iterations
		int i1 = 3;

		while (temperature > finalTemperature)
		{
			if ((iter--) <= 0)
			{
				//decrease the temperature
				iter = step * 3 / (i1++);
				temperature = temperature / 1.5 - 0.5;
				if (temperature < 20 * unitTemperature)
				{
					temperature1 = temperature1 * 1.5 + 0.1;
				}
			}

			// move the nodes
			relax();
		}
	}


	/**
	 * This method transfers the edges to the internal structures
         * removes self loops and multiedges.
	 *
	 * @param cB1           Edge first node index
	 * @param cB2           Edge second node index
	 * @param edgeStrength the strength of edge
	 */

	private void transferEdges(int cB1[], int cB2[], double [] edgeStrength)
	{
            int i=0;
            
            for (LayoutLine e:this.layoutLines)
            {
                edges[i].strength = edgeStrength == null ? 1 : edgeStrength[i];
                edges[i].from = cB1[i];
                edges[i].to = cB2[i];
                
                // calculate connection points
                double sx = e.endElement.getCenterX()-((AbstractContainer)e.originalLine.getEnd()).getCenterX();
                double sy = e.endElement.getCenterY()-((AbstractContainer)e.originalLine.getEnd()).getCenterY();
                double tx = e.startElement.getCenterX()-((AbstractContainer)e.originalLine.getStart()).getCenterX();
                double ty = e.startElement.getCenterY()-((AbstractContainer)e.originalLine.getStart()).getCenterY();
                
                edges[i].dx = tx-sx;
                edges[i].dy = ty-sy;                
                
                i++;
            }
	}


	/**
	 * This method generates a random permutation of nodes.
	 */
	private void randomNodePermutation()
	{
		for (int i = 0; i < realNodesCount; i++)
		{
			int i1 = this.randomGenerator.nextInt(realNodesCount - i) + i;
			SpringNode tmp = this.nodePermutationN[i];

			this.nodePermutationN[i] = this.nodePermutationN[i1];
			this.nodePermutationN[i1] = tmp;
		}
	}
        
        /**
         * Perform minimal node movement that makes constraints satisfied
         */
        private void ensureConstraints()
        {
            if(this.constraints==null || this.constraints.getType() == LayoutConstraints.ConstraintType.NONE) return;
            
            if(this.constraints instanceof GridLayoutConstraints)
            {
                GridLayoutConstraints constr = (GridLayoutConstraints) this.constraints;
                ensureConstraintsY(constr);
                ensureConstraintsX(constr);
            }
            else
            {
                throw new RuntimeException("not implemented constraint type");
            }
            
        }
// --------------------------------------------------------------------
// Section: debug
// --------------------------------------------------------------------
        
/*    private void paintGraph(Graphics g){
        g.clearRect(0, 0, 10000, 10000);

        double minx = Integer.MAX_VALUE;
        double maxx = Integer.MIN_VALUE;
        double miny = Integer.MAX_VALUE;
        double maxy = Integer.MIN_VALUE;

        for (SpringNode node:nodes)
        {
                double x = node.x;
                double y = node.y;

                minx = Math.min(minx, x);
                maxx = Math.max(maxx, x);
                miny = Math.min(miny, y);
                maxy = Math.max(maxy, y);
        }

        double w = Math.max(maxx - minx, maxy - miny);

        for (SpringNode node:nodes)
        {
                int x = (int) ((node.x - minx) * 800 / w);
                int y = (int) ((node.y - miny) * 800 / w);
                int r = (int) (node.r * 800 / w);

                g.drawOval(x - r, y - r, 2 * r, 2 * r);
        }

        for (SpringEdge edge:edges)
        {
                SpringNode source = nodes[edge.from];
                SpringNode target = nodes[edge.to];

                int x1 = (int) ((source.x - minx) * 800 / w);
                int y1 = (int) ((source.y - miny) * 800 / w);
                int x2 = (int) ((target.x - minx) * 800 / w);
                int y2 = (int) ((target.y - miny) * 800 / w);

                g.drawLine(x1, y1, x2, y2);
        }

        try {
            //Thread.sleep(120);
        } catch (Throwable ex) {            
        }
	}*/


	private void draw()
	{
/*            if(DEBUG_DISPLAY)
            {
                if(display == null)
                {
                    display = new DrawingFrame();
                    display.setSize(800,800);
                    display.setVisible(true);
                }
                paintGraph(display.getGraphics());
                //display.repaint();
            }*/

	}
        
/*        private class DrawingFrame extends JFrame{

            public void paint(Graphics g){
                paintGraph(g);
            }
        }*/


// --------------------------------------------------------------------
// Section: instance variables
// --------------------------------------------------------------------

	// the graph
	private GraphAdjacency g;

	//the number of nodes
	private int nodeCount;

	//the node array
	private SpringNode[] nodes;

	//the number of edges
	private int edgeCount;

	//the edge array
	private SpringEdge[] edges;

	// random permutation of the nodes
	private int[] nodePermutation;

	//temperature
	private double temperature;

	//related to temperature
	private double temperature1;

	//the unit temperature
	private double unitTemperature;

	//incremental mode
	private boolean incremental;

	// the data structure for repulsive force calculation
	private RepulsiveKFTree repulsiveForceTree = new RepulsiveKFTree();

	//random generator
	private Random randomGenerator;

	// locked nodes. Their position remains the same.
	private boolean[] isNodeLocked;

	private SpringNode[] nodePermutationN;
	private double nodeDegreeModifier = 1;
	private double forceReductionFactor = 1;
	private double[][] force;
	private double[][] oldForce;
        private int realNodesCount;
        private double graphSizeWeight;
        
//        private DrawingFrame display;
        private static final boolean DEBUG_DISPLAY = false;
        
        private AbstractContainer[] boxes;
        private LayoutConstraints constraints;
        private double epsilon = 0.01;
        private List<LayoutLine> layoutLines;
        
}
