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
 * This class implements comparator that compares PQTreeNodes and considers
 * first node smaller than second node if first node is in level with larger
 * number than second node's level number.
 * @author Rudolfs
 */
public class PQTreeNodeLevelComparator implements Comparator<PQTreeNode>
{
	/**
	 * This method returns the result of comparison the given PQTreeNodes.
	 *
	 * @param n1 the first PQTreeNode to be compared.
	 * @param n2 the second PQTreeNode to be compared.
	 * @return the comparison code.
	 */
        @Override
	public int compare(PQTreeNode n1, PQTreeNode n2)
	{
		int rc = 0;

		if (n1.getLevel() < n2.getLevel())
		{
			rc = 1;
		}
		else if (n1.getLevel() > n2.getLevel())
		{
			rc = -1;
		}

		return rc;
	}
}
