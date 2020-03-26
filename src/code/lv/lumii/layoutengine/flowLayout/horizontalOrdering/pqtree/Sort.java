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

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


/**
 * This class implements a stable sort algorithm needed for
 * the horizontal ordering of a layered graph.
 * @author Rudolfs
 * @param <T> object type
 */
public class Sort<T>
{
	/**
	 * This method performs sorting of the given list according the given comparator.
	 * @param list the list to be sorted.
	 * @param c the user comparator.
	 */
	public void sort(List<T> list, Comparator<? super T> c)
	{
                ArrayList<T> arr = new ArrayList<>(list);
		
		for (int i= 1; i < list.size(); i++)
		{
			for (int j = 0; j < i; j++)
			{
				if (c.compare(arr.get(j), arr.get(j+1)) > 0)
				{
					T o = arr.get(j);
					arr.set(j, arr.get(j+1));
					arr.set(j+1, o);
				}
			}
		}
		
		list.clear();
            for (T arr1 : arr) {
                list.add(arr1);
            }
	}
}
