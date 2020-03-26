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
 * This class compares two PQTrees by their LL values and considers smaller
 * the one with smaller LL value.
 * @see PQTree
 * @author Rudolfs
 */
public class PQTreeLLComparator implements Comparator<PQTree>
{
	/**
	 * This method returns the result of comparison the given PQTrees.
	 *
	 * @param t1 the first PQTree.
	 * @param t2 the second PQTree.
	 * @return the comparison code.
	 */
        @Override
	public int compare(PQTree t1, PQTree t2)
	{
		int rc = 0;

		if (t1.getLLValue() < t2.getLLValue())
		{
			rc = -1;
		}
		else if (t1.getLLValue() > t2.getLLValue())
		{
			rc = 1;
		}

		return rc;
	}
}
