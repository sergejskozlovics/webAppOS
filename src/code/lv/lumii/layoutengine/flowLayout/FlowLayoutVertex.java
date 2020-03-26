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


/**
 * This class implements the vertex in internal representation of the diagram graph
 * for the Flow Layout algorithm.
 *
 * @author paulis
 */
public class FlowLayoutVertex
{
	/**
	 * This method sets the index of this vertex.
	 *
	 * @param index the vertex index.
	 */
	public void setIndex(int index)
	{
		this.index = index;
	}
	

	/**
	 * This method returns the index of this vertex.
	 *
	 * @return the vertex index.
	 */
	public int getIndex()
	{
		return this.index;
	}
	

	/**
	 * This method sets the tag of this vertex.
	 *
	 * @param tag the vertex tag.
	 */
	public void setTag(String tag)
	{
		this.tag = tag;
	}
	

	/**
	 * This method returns the tag of this vertex.
	 *
	 * @return the vertex tag.
	 */
	public String getTag()
	{
		return this.tag;
	}
	

	/**
	 * This method adds the given edge to the vertex incidence list.
	 *
	 * @param edge the edge to be add.
	 */
	public void addIncEdge(FlowLayoutEdge edge)
	{
		this.incEdgeList.add(edge);
	}
	
	private int index;
	private String tag;
	private final ArrayList<Integer> adjVertices = new ArrayList<>();
	private final ArrayList<FlowLayoutEdge> incEdgeList = new ArrayList<>();
}
