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

import java.util.*;

/**
 * This class compares two objects that have min and max int value associated with them.
 * First they are compared by min values, but in case of tie max values are considered.
 * @author Rudolfs
 * @param <T> object type
 */
public class ObjectSeqComparator<T> implements Comparator<T>
{
	public ObjectSeqComparator(List<? extends Set<T>> sequenceRulesList)
	{
		this.minMap = new HashMap<>();
		this.maxMap = new HashMap<>();

		int index = 0;

		for (Iterator<? extends Set<T>> i1 = sequenceRulesList.iterator();
			 i1.hasNext();
			 index++)
		{
			Collection<T> cList = i1.next();
                    for (T o : cList) {
                        if (!minMap.containsKey(o))
                        {
                            this.minMap.put(o, new Integer(index));
                        }
                        
                        this.maxMap.put(o, new Integer(index));
                    }
		}
	}


	/**
	 * This method returns the result of comparison the given objects
	 * having min and max int values.
	 *
	 * @param o1 the first object to be compared.
	 * @param o2 the second object to be compared.
	 * @return the comparison code.
	 */
        @Override
	public int compare(T o1, T o2)
	{
		int rc = 0;

		int min[] = new int[2];
		int max[] = new int[2];

		if (this.minMap.containsKey(o1))
		{
			min[0] = ((Integer) this.minMap.get(o1)).intValue();
		}
		else
		{
			min[0] = -1;
		}

		if (this.minMap.containsKey(o2))
		{
			min[1] = ((Integer) this.minMap.get(o2)).intValue();
		}
		else
		{
			//TODO: think what to do with hanging vertices
			min[1] = -1;
		}

		if (this.maxMap.containsKey(o1))
		{
			max[0] = ((Integer) this.maxMap.get(o1)).intValue();
		}
		else
		{
			max[0] = -1;
		}

		if (this.maxMap.containsKey(o2))
		{
			max[1] = ((Integer) this.maxMap.get(o2)).intValue();
		}
		else
		{
			max[1] = -1;
		}

		if (min[0] < min[1])
		{
			rc = -1;
		}
		else if (min[0] > min[1])
		{
			rc = 1;
		}
		else if (min[0] == min[1])
		{
			if (max[0] < max[1])
			{
				rc = -1;
			}
			else if (max[0] > max[1])
			{
				rc = 1;
			}
		}

		return rc;
	}


	private final Map<T, Integer> minMap;
	private final Map<T, Integer> maxMap;
}
