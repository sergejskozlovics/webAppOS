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

import java.util.ArrayList;
import java.util.List;


/**
 * a node for the repulsive force tree.
 */
final class RepulsiveTreeNode extends BaseSpringNode
{
	/**
	 * force links for the repulsive force calculation.
	 */
	List<BaseSpringNode> forceLinkList = new ArrayList<>();

	// size
	double sizeEstimate;

	// children
	RepulsiveTreeNode left;
	RepulsiveTreeNode right;

	// leaves (springNodes)
	int leafCount;
	SpringNode leaves[];
}
