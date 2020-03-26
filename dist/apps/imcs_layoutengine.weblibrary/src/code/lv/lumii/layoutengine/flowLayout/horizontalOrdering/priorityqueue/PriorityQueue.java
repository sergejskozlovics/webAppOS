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

package lv.lumii.layoutengine.flowLayout.horizontalOrdering.priorityqueue;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Collections;

/**
 * This class implements the priority queue needed for
 * the horizontal ordering algorithm of a layered graph.
 *
 * @author Rudolfs
 * @param <T> the type of the elements of this queue
 */
public class PriorityQueue<T>
{
	public PriorityQueue(int initialCapacity, Comparator<T> comparator)
	{
		this.comparator = comparator;
		this.heap = new ArrayList<>(initialCapacity);
	}

	/**
	 * This method returns element count in priority queue.
	 * @return size.
	 */
	public int size()
	{
		return this.heap.size();
	}

	/**
	 * This method returns whether priority queue is empty.
	 * @return boolean value that is <code>true</code> if priority queue is empty.
	 */
	public boolean isEmpty()
	{
		return this.heap.isEmpty();
	}


	/**
	 * This method removes smallest element in queue if there are several equal elements than returns first inserted
	 * first.
	 * @return removed element.
	 */
	public T removeMin()
	{
		T ret = this.heap.get(0);

		Collections.swap(this.heap, 0, this.heap.size() - 1);

		this.heap.remove(this.heap.size() - 1);

		int index = 0;

		while ((index * 2 + 1 < this.heap.size() &&
			this.comparator.compare(this.heap.get(index),
				this.heap.get(index * 2 + 1)) > 0) ||
			(index * 2 + 2 < this.heap.size() &&
				this.comparator.compare(this.heap.get(index),
					this.heap.get(index * 2 + 2)) > 0))
		{
			if (index * 2 + 2 < this.heap.size() &&
				this.comparator.compare(
					this.heap.get(index * 2 + 1),
					this.heap.get(index * 2 + 2)) > 0)
			{
				Collections.swap(this.heap, index, index * 2 + 2);
				index = index * 2 + 2;
			}
			else
			{
				Collections.swap(this.heap, index, index * 2 + 1);
				index = index * 2 + 1;
			}
		}
		
		return ret;
	}


	/**
	 * Adds element to the priority queue.
	 * @param o element to insert.
	 */
	public void add(T o)
	{
		this.heap.add(o);

		int index = this.heap.size() - 1;

		while (index > 0 &&
			this.comparator.compare(this.heap.get((index - 1) / 2), o) > 0)
		{
			Collections.swap(this.heap, index, (index - 1) / 2);
			index = (index - 1) / 2;
		}
	}

	private final ArrayList<T> heap;
	private final Comparator<T> comparator;
}
