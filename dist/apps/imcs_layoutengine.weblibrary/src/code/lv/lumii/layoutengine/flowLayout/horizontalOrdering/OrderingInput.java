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


/**
 * This class implements the input object of the horizontal ordering algorithm
 * of a layered graph that is needed to provide the horizontal ordering
 * in the Flow Layout algorithm.
 *
 * @author paulis
 */
public class OrderingInput
{
	public void setLayeredGraph(LayeredGraph layeredGraph)
	{
		this.layeredGraph = layeredGraph;
	}

	public LayeredGraph getLayeredGraph()
	{
		return this.layeredGraph;
	}
	
	private LayeredGraph layeredGraph;
}
