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

import java.util.Comparator;


/**
 * This class implements a level comparator of layered nodes of an auxiliary
 * graph needed for the horizontal ordering in the Flow Layout algorithm.
 *
 * @author paulis
 */
public class LayeredNodeLevelComparator implements Comparator<LayeredNode>
{
	/**
	 * This method returns the result of comparison of the level numbers
	 * of two layered nodes.
	 *
	 * @param n1 the first layered node.
	 * @param n2 the second layered node.
	 * @return the comparison code.
	 */
    @Override
	public int compare(LayeredNode n1, LayeredNode n2)
	{
		int rc = 0;

		if (n1.getLevelNumber() > n2.getLevelNumber())
		{
			rc = -1;
		}
		else if (n1.getLevelNumber() < n2.getLevelNumber())
		{
			rc = 1;
		}

		return rc;
	}
}
