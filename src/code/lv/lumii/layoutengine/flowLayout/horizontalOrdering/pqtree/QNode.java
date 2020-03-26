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


import java.util.Iterator;
import java.util.*;


/**
 * This class implements Q-type node of PQTree data structure.
 * @author Rudolfs
 * @param <T> object type
 */
public class QNode<T> extends PQTreeNode<T>
{
	public QNode(PQTreeNode<T> parent, PQTree<T> tree)
	{
		super(parent, tree);
		this.mlValues = new ArrayList<>();
	}


	/**
	 * This method returns int value to represent this node type.
	 * Node type is <code>PQTree.QNODE</code>
	 */
        @Override
	public int getType()
	{
		return PQTree.QNODE;
	}


	/**
	 * This method performs all transformations for this node.
	 * @param isRoot represents whether this is root of pertinent tree.
	 */
        @Override
	public PQTreeNode<T> process(boolean isRoot)
	{
		PQTreeNode<T> returnNode = null;

		this.update();
		
		if (!this.isVisited())
		{
			//return this;
		}

		isRoot &= ! this.isFull();

		//process;
		if (this.isFull())
		{
			// Q1

			//System.out.println("Q1");

			returnNode = this.getParent();
		}
		else
		{
			int numberOfPertQChilds = this.getPertQChildrenCount();
			
			if (numberOfPertQChilds < 3)
			{
				// Q2 vai Q3

				//System.out.println("Q23");

				boolean full = false;
				int place = 0;
				
				LinkedList<PQTreeNode<T>> cList = new LinkedList<>(this.getChildList());
				
				for (Iterator<PQTreeNode<T>> iter = cList.iterator(); iter.hasNext();place++)
				{
					PQTreeNode<T> node = iter.next();
					
					full |= node.isFull();
					
					if (node.isPertinent() && (node instanceof QNode))
					{
						this.removeChild(node);
						
						LinkedList<PQTreeNode<T>> childList =
							new LinkedList<>(node.getChildList());
						
						if ((childList.getFirst()).isFull() != full)
						{
							java.util.Collections.reverse(childList);
						}
						
						for (Iterator<PQTreeNode<T>> it = childList.iterator();
							it.hasNext();
							place++)
						{
							PQTreeNode<T> n = it.next();
							full |= n.isFull();
							n.setParent(this, place);
						}

						place--;
					}
				}

				if (!isRoot)
				{
					returnNode = this.getParent();
				}
				else
				{
					returnNode = null;
				}
			}
		}

		return returnNode;
	}


	/**
	 * This method returns char that represents this node type.
	 */
        @Override
	public char getTypeChar()
	{
		return 'Q';
	}


	/**
	 * This method is used for node ordering in algorithm second phase when
	 * moving backwards over layers.
	 * @return next node to process.
	 */
        @Override
	public PQTreeNode<T> getMaxMinSeq()
	{
		myAssert(this.getChildList().size() > 0);

		LinkedList childList = (LinkedList) this.getChildList();

		ListIterator endIter = childList.listIterator(childList.size());
		ListIterator frontIter = childList.listIterator(0);

		PQTreeNode firstNode = (PQTreeNode) frontIter.next();
		PQTreeNode lastNode = (PQTreeNode) endIter.previous();

		while ((firstNode.getMaxSeq() == -1 || lastNode.getMaxSeq() == -1) &&
			frontIter.hasNext() && endIter.hasPrevious())
		{
			if (firstNode.getMaxSeq() == -1)
			{
				firstNode = (PQTreeNode) frontIter.next();
			}
			else
			{
				lastNode = (PQTreeNode) endIter.previous();
			}
		}

		if (!frontIter.hasNext() || !endIter.hasPrevious())
		{
			firstNode = (PQTreeNode) childList.getFirst();
			lastNode = (PQTreeNode) childList.getLast();
		}

		PQTreeSeqComparator comparator = new PQTreeSeqComparator();

		if (comparator.compare(firstNode, lastNode) > 0)
		{
			Collections.reverse(this.getChildList());
		}

		this.setMinSeq(
			Math.min(firstNode.getMinSeq(), lastNode.getMinSeq()));
		this.setMaxSeq(
			Math.max(firstNode.getMaxSeq(), lastNode.getMaxSeq()));

		return this.getParent();
	}

	
	private void myAssert(boolean check)
	{
		if (!check)
		{
			throw new RuntimeException("Check failed!");
		}
	}

	/**
	 * This method returns required ML value.
	 * @param index returns ML value between index-th and (index + 1)-th child.
	 * @return ML value.
	 */
	public int getMLValue(int index)
	{
		int rc = 0;

		if (index < this.mlValues.size())
		{
			rc = ((Integer) this.mlValues.get(index)).intValue();
		}

		return rc;
	}


	/**
	 * This method returns ML value from given object to next in child list.
	 * @param o given object.
	 * @return ML value.
	 */
	public int getMLValue(Object o)
	{
		return this.getMLValue(this.getChildList().indexOf(o));
	}


	/**
	 * This method sets ML value between given objects.
	 * @param index place to put new ML value.
	 * @param MLValue new ML value.
	 * @param insert boolean value that determine whether new value just replace
	 * old value or inserts new one.
	 */
	public void setMLValue(int index, int MLValue, boolean insert)
	{
		if (this.mlValues.size() > index)
		{
			this.mlValues.add(index, new Integer(MLValue));
		}
		else
		{
			this.mlValues.add(new Integer(MLValue));
		}

		if (!insert && this.mlValues.size() > index + 1)
		{
			this.mlValues.remove(index + 1);
		}
	}


	/**
	 * This method clears all ML values between all children in this child
	 * list.
	 */
	public void clearMLValues()
	{
		this.mlValues.clear();
	}


	private final ArrayList<Integer> mlValues;
}
