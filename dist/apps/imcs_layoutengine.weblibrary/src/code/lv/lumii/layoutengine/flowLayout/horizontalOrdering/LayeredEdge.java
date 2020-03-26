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

import lv.lumii.layoutengine.flowLayout.FlowLayoutEdge;

/**
 * This class implements an edge of the auxiliary layered graph needed for
 * the horizontal ordering in the Flow Layout algorithm.
 *
 * @author paulis
 */
public class LayeredEdge
{
	/**
	 * This method is the class constructor. 
	 *
	 * @param sourceNode the from node of this edge.
	 * @param targetNode the to node of this edge.
	 */
	public LayeredEdge(LayeredNode sourceNode, LayeredNode targetNode)
	{
		this.sourceNode = sourceNode;
		this.targetNode = targetNode;
	}


	/**
	 * This method returns the original edge of this layered edge.
	 *
	 * @return the original edge.
	 */
	public FlowLayoutEdge getOriginalEdge()
	{
		return null;
	}


	/**
	 * This method returns the other endnode of this layered edge.
	 *
	 * @param node the node of this edge.
	 * @return the other node of this edge.
	 */
	public LayeredNode getOtherNode(LayeredNode node)
	{
		LayeredNode otherNode = targetNode;
		
		if (node == targetNode)
		{
			otherNode = sourceNode;
		}
		
		return otherNode;
	}
	

	private final LayeredNode sourceNode;
	private final LayeredNode targetNode;
}
