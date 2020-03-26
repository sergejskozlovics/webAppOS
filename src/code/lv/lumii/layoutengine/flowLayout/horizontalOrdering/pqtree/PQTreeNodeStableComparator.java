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
import java.util.List;
import java.util.Set;

/**
 * This class implements comparator that compares two PQ tree nodes and
 * considers first node smaller than second if none of both nodes are
 * in new node set or if at least one of nodes are in new node set and 
 * PQTreeSeqComparator considers first node smaller or equal than second.
 * @see PQTreeSeqComparator
 * @author Rudolfs
 */
public class PQTreeNodeStableComparator implements Comparator<PQTreeNode>
{
	public PQTreeNodeStableComparator(Set<PQTreeNode> newNodeSet, List<? extends PQTreeNode> oldOrder)
	{
		this.newNodeSet = newNodeSet;
		this.oldOrder = oldOrder;
	}


	/**
	 * This method returns the result of comparison the given PQ tree nodes.
	 *
	 * @param o1 the first PQTreeNode to be compared.
	 * @param o2 the second PQTreeNode to be compared.
	 * @return the comparison code.
	 */
        @Override
	public int compare(PQTreeNode o1, PQTreeNode o2)
	{
		int rc;

		if (this.newNodeSet.contains(o1) || this.newNodeSet.contains(o2))
		{
			rc = this.internalComp.compare(o1, o2);
		}
		else
		{
			int ind1 = this.oldOrder.indexOf(o1);
			int ind2 = this.oldOrder.indexOf(o2);
			
			rc = (ind1 < ind2 ? -1 : (ind1 > ind2 ? 1 : 0));
		}
			
		return rc;
	}

/*	public int compare(Object o1, Object o2)
	{
		int rc = -1;

		if (this.newNodeSet.contains(o1) || this.newNodeSet.contains(o2))
		{
			rc = this.internalComp.compare(o1, o2);

			if (rc == 0)
			{
				rc = -1;
			}
		}

		return rc;
	}
*/
	
	private final Comparator<PQTreeNode> internalComp = new PQTreeSeqComparator();
	private final Set<PQTreeNode> newNodeSet;
	private final List<? extends PQTreeNode> oldOrder;
}
