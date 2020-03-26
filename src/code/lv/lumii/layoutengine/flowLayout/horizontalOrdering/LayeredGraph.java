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

package lv.lumii.layoutengine.flowLayout.horizontalOrdering;

import java.util.ArrayList;


/**
 * This class implements an auxiliary layered graph needed for
 * the horizontal ordering in the Flow Layout algorithm.
 *
 * @author paulis
 */
public class LayeredGraph
{
	/**
	 * This method is the class constructor. 
	 *
	 * @param numberOfLevels the number of levels nodes are grouped into.
	 */
	public LayeredGraph(int numberOfLevels)
	{
		this.numberOfLevels = numberOfLevels;
                this.levelNodeListArray = new ArrayList<>();
		this.edgeList = new ArrayList<>();
                
                for(int i = 0; i < numberOfLevels; i++) {
                    this.levelNodeListArray.add(new ArrayList<LayeredNode>());
                }
	}


	/**
	 * This method returns the number of levels.
	 *
	 * @return the number of levels.
	 */
	public int getNumberOfLevels()
	{
		return this.numberOfLevels;
	}


	/**
	 * This method adds the given node to the specified level.
	 *
	 * @param level the level index.
	 * @param node the node to be added.
	 */
	public void addLevelNode(int level, LayeredNode node)
	{
		this.levelNodeListArray.get(level).add(node);
	}


	/**
	 * This method returns the list of nodes of the specified level.
	 *
	 * @param level the level index.
	 * @return the list of nodes.
	 */
	public ArrayList<LayeredNode> getLevelNodeList(int level)
	{
		return this.levelNodeListArray.get(level);
	}


	/**
	 * This method adds an edge to this layered graph.
	 *
	 * @param sourceNode the from node of the edge to be added.
	 * @param targetNode the to node of the edge to be added.
	 */
	public void addEdge(LayeredNode sourceNode, LayeredNode targetNode)
	{
		LayeredEdge edge = new LayeredEdge(sourceNode, targetNode);
		
		this.edgeList.add(edge);
		sourceNode.addIncEdge(edge);
		targetNode.addIncEdge(edge);
		targetNode.addInEdge(edge);
	}
	
	private final int numberOfLevels;
	private final ArrayList<ArrayList<LayeredNode>> levelNodeListArray;
	private final ArrayList<LayeredEdge> edgeList;
}
