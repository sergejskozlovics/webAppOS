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
import java.util.Iterator;

import lv.lumii.layoutengine.flowLayout.FlowLayoutVertex;


/**
 * This class implements a node of the auxiliary layered graph needed for
 * the horizontal ordering in the Flow Layout algorithm.
 *
 * @author paulis
 */
public class LayeredNode
{
	/**
	 * This method sets the level number of this layered node.
	 *
	 * @param levelNumber the node level number.
	 */
	public void setLevelNumber(int levelNumber)
	{
		this.levelNumber = levelNumber;
	}

	
	/**
	 * This method returns the level number of this layered node.
	 *
	 * @return the node level number.
	 */
	public int getLevelNumber()
	{
		return this.levelNumber;
	}

	
	/**
	 * This method sets the original vertex of this layered node.
	 *
	 * @param vertex the original vertex.
	 */
	public void setOriginalVertex(FlowLayoutVertex vertex)
	{
		this.originalVertex = vertex;
	}

	
	/**
	 * This method returns the original vertex of this layered node.
	 *
	 * @return the original vertex.
	 */
	public FlowLayoutVertex getOriginalVertex()
	{
		return this.originalVertex;
	}

	
	/**
	 * This method sets the flag indicating if this layered node is dummy.
	 *
	 * @param isDummy the flag value.
	 */
	public void setDummy(boolean isDummy)
	{
		this.isDummy = isDummy;
	}

	
	/**
	 * This method returns the flag indicating if this layered node is dummy.
	 *
	 * @return the flag value.
	 */
	public boolean isDummy()
	{
		return this.isDummy;
	}

	
	/**
	 * This method adds the given edge to this node incidence list.
	 *
	 * @param edge the edge to be add.
	 */
	public void addIncEdge(LayeredEdge edge)
	{
		this.incEdgeList.add(edge);
	}

	
	/**
	 * This method returns the iterator of this node incidence list.
	 *
	 * @return the iterator of the incidence list.
	 */
	public Iterator inOutEdgeIterator()
	{
		return incEdgeList.iterator();
	}

	
	/**
	 * This method adds the given edge to this node inedge list.
	 *
	 * @param edge the edge to be add.
	 */
	public void addInEdge(LayeredEdge edge)
	{
		this.inEdgeList.add(edge);
	}

	
	/**
	 * This method returns the first inedge of this node inedge list.
	 *
	 * @return the first inedge.
	 */
	public LayeredEdge inEdge()
	{
		return inEdgeList.get(0);
	}
	
	
	/**
	 * This auxiliary method returns the string containing the tag
	 * of the original vertex of this node.
	 *
	 * @return the string containing the tag.
	 */
	@Override
	public String toString() {
		return "LN: " + this.getOriginalVertex().getTag();
	}


	private int levelNumber;
	private FlowLayoutVertex originalVertex;
	private boolean isDummy;
	private final ArrayList<LayeredEdge> incEdgeList = new ArrayList<>();
	private final ArrayList<LayeredEdge> inEdgeList = new ArrayList<>();
}
