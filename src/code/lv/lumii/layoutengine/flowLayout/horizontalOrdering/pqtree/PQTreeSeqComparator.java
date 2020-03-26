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

package lv.lumii.layoutengine.flowLayout.horizontalOrdering.pqtree;

import java.util.Comparator;

/**
 * This class implements comparator that compares two PQ tree nodes and
 * considers first node smaller or equal than second.
 * @author Rudolfs
 */
public class PQTreeSeqComparator implements Comparator<PQTreeNode>
{
	/**
	 * This method returns the result of comparison the given PQ tree nodes.
	 *
	 * @param node1 the first PQTreeNode to be compared.
	 * @param node2 the second PQTreeNode to be compared.
	 * @return the comparison code.
	 */
        @Override
	public int compare(PQTreeNode node1, PQTreeNode node2)
	{
		int rc = 0;

		if (node1.getMinSeq() < node2.getMinSeq())
		{
			rc = -1;
		}
		else if (node1.getMinSeq() > node2.getMinSeq())
		{
			rc = 1;
		}
		else if (node1.getMinSeq() == node2.getMinSeq() &&
			node1.getMaxSeq() < node2.getMaxSeq())
		{
			rc = -1;
		}
		else if (node1.getMinSeq() == node2.getMinSeq() &&
			node1.getMaxSeq() > node2.getMaxSeq())
		{
			rc = 1;
		}
		return rc;
	}
}
