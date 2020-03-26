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


/**
 * This class implements the edge in internal representation of the diagram graph
 * for the Flow Layout algorithm.
 *
 * @author paulis
 */
public class FlowLayoutEdge
{
	public int v1;
	public int v2;
	

	/**
	 * This method is the class constructor. 
	 *
	 * @param v1 the edge from vertex index.
	 * @param v2 the edge to vertex index.
	 */
	public FlowLayoutEdge(int v1, int v2)
	{
		this.v1 = v1;
		this.v2 = v2;
	}
	

	/**
	 * This method returns the source vertex of the edge.
	 *
	 * @return the source vertex index.
	 */
	public FlowLayoutVertex getSourceNode()
	{
		return null;
	}
	

	/**
	 * This method returns the target vertex of the edge.
	 *
	 * @return the source vertex index.
	 */
	public FlowLayoutVertex getTargetNode()
	{
		return null;
	}
}
