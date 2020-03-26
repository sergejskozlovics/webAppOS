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

//import com.tomsawyer.util.TSSort;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * This class implements P-type node for PQ-Tree data structure.
 * @author Rudolfs
 * @param <T> object type
 */

public class PNode<T> extends PQTreeNode<T>
{
	public PNode(PQTreeNode<T> parent, PQTree<T> tree)
	{
		super(parent, tree);
	}


	/**
	 * This method returns int value to represent this node type.
	 * Node type is <code>PQTree.PNODE</code>
	 */
        @Override
	public int getType()
	{
		return PQTree.PNODE;
	}


	/**
	 * This method adds child to this node.
	 * @param child node that will be added as child. 
	 */
        @Override
	public void addChild(PQTreeNode<T> child)
	{
		super.addChild(child);
	}


	/**
	 * This method performs all transformations for this node.
	 * @param isRoot represents whether this is root of pertinent tree.
	 */
        @Override
	public PQTreeNode<T> process(boolean isRoot)
	{
		PQTreeNode<T> returnNode = null;

		// process
		this.update();
		
		if (!this.isFull())
		{
			//return this;
		}
		
		isRoot &= ! this.isFull();
		
		if (this.isFull())
		{
			// P1

			//System.out.println("P1");

			returnNode = this.getParent();
		}
		else if (this.isPertinent())
		{
			int pertQChildrenCount = this.getPertQChildrenCount();
			
			if (pertQChildrenCount == 0)
			{
				if (isRoot)
				{
					// P2

					//System.out.println("P2");

					LinkedList<PQTreeNode<T>> fullChildList = new LinkedList<>();
					
					for (Iterator<PQTreeNode<T>> iter = this.getChildIterator(); iter.hasNext();)
					{
						PQTreeNode<T> child = iter.next();
						
						if (child.isFull())
						{
							fullChildList.add(child);
						}
					}

					if (fullChildList.size() > 1)
					{
						PNode<T> pNode = new PNode<>(this, this.getTree());
                                            for (PQTreeNode<T> node : fullChildList) {
                                                node.setParent(pNode);
                                            }
					}

					returnNode = null;
				}
				else
				{
					// P3

					//System.out.println("P3");

					LinkedList<PQTreeNode<T>> childList = new LinkedList<>(this.getChildList());
					this.clearChildList();
					
					QNode<T> qNode = new QNode<>(null, this.getTree());
					qNode.setParent(this.getParent(), this);
					
					PNode<T> fullPNode = new PNode<>(qNode, this.getTree());
					
					fullPNode.setFull();
					
					PNode<T> emptyPNode = new PNode<>(qNode, this.getTree());
                                    for (PQTreeNode<T> node : childList) {
                                        if (node.isFull())
                                        {
                                            node.setParent(fullPNode);
                                        }
                                        else
                                        {
                                            node.setParent(emptyPNode);
                                        }
                                    }

					if (fullPNode.getChildList().size() == 1)
					{
						fullPNode.getChildList().get(0).
							setParent(qNode, fullPNode);
					}
					else if (fullPNode.getChildList().isEmpty())
					{
						fullPNode.getParent().removeChild(fullPNode);
					}

					if (emptyPNode.getChildList().size() == 1)
					{
						emptyPNode.getChildList().get(0).
							setParent(qNode, emptyPNode);
					}
					else if (emptyPNode.getChildList().isEmpty())
					{
						emptyPNode.getParent().removeChild(emptyPNode);
					}

					qNode.update();
					
					returnNode = this.getParent();
				}
			}
			else if (pertQChildrenCount == 1)
			{
				if (isRoot)
				{
					// P4

					//System.out.println("P4");

					QNode<T> qNode = null;
					
					LinkedList<PQTreeNode<T>> childList = new LinkedList<>();
					
					for (Iterator<PQTreeNode<T>> iter = this.getChildIterator(); iter.hasNext();)
					{
						PQTreeNode<T> node = iter.next();

						if ((node.getType() == PQTree.QNODE) && (node.isPertinent()))
						{
							qNode = (QNode<T>) node; 
						}
						else if (node.isFull())
						{
							childList.add(node);
						}
					}
					
                                        assert qNode != null;
					boolean end = qNode.getChildList().get(0).isFull();
					
					PQTreeNode<T> pNode;
					
					if (childList.size() > 1)
					{
						pNode = new PNode<>(null, this.getTree());
						pNode.setParent(qNode, end);
					}
					else
					{
						pNode = qNode;
					}
                                    for (PQTreeNode<T> node : childList) {
                                        node.setParent(pNode, end);
                                    }

					returnNode = null;
				}
				else
				{
					// P5

					//System.out.println("P5");

					QNode<T> qNode = null;
					
					LinkedList<PQTreeNode<T>> fullChildList = new LinkedList<>();
					LinkedList<PQTreeNode<T>> empChildList = new LinkedList<>();
					
					
					for (Iterator<PQTreeNode<T>> iter = this.getChildIterator(); iter.hasNext();)
					{
						PQTreeNode<T> node = iter.next();
						
						if ((node instanceof QNode) && (node.isPertinent()))
						{
							qNode = (QNode<T>) node; 
						}
						else if (node.isFull())
						{
							fullChildList.add(node);
						}
						else
						{
							empChildList.add(node);
						}
					}
					
                                        assert qNode != null;
					boolean end = qNode.getChildList().get(0).isFull();
					
					PNode<T> empPNode = new PNode<>(null, this.getTree());
					PNode<T> fullPNode = new PNode<>(null, this.getTree());
					
					empPNode.setParent(qNode, !end);
					fullPNode.setParent(qNode, end);
                                    for (PQTreeNode<T> node : fullChildList) {
                                        node.setParent(fullPNode);
                                    }
                                    for (PQTreeNode<T> node : empChildList) {
                                        node.setParent(empPNode);
                                    }
					
					if (empPNode.getChildList().size() == 1)
					{
						empPNode.getChildList().get(0).
							setParent(empPNode.getParent(), empPNode);
					}
					else if (empPNode.getChildList().isEmpty())
					{
						qNode.removeChild(empPNode);
					}

					if (fullPNode.getChildList().size() == 1)
					{
						fullPNode.getChildList().get(0).
							setParent(fullPNode.getParent(), fullPNode);
					}
					else if (fullPNode.getChildList().isEmpty())
					{
						qNode.removeChild(fullPNode);
					}

					qNode.setParent(this.getParent(), this);

					returnNode = this.getParent();
				}
			}
			else if (pertQChildrenCount == 2)
			{
				// P6

				//System.out.println("P6");

				ArrayList<QNode<T>> arr = new ArrayList<>();
				
				LinkedList<PQTreeNode<T>> fullChildList = new LinkedList<>();
				
				for (Iterator<PQTreeNode<T>> iter = this.getChildIterator(); iter.hasNext();)
				{
					PQTreeNode<T> node = iter.next();
					
					if ((node instanceof QNode) && (node.isPertinent()))
					{
                                                arr.add((QNode<T>) node);
					}
					else if (node.isFull())
					{
						fullChildList.add(node);
					}
				}
                                
				boolean end = arr.get(0).getChildList().get(0).isFull();
				
				PNode<T> pNode = new PNode<>(null, this.getTree());
				pNode.setParent(arr.get(0), end);
                            for (PQTreeNode<T> node : fullChildList) {
                                node.setParent(pNode);
                            }
				
				if (pNode.getChildList().size() == 1)
				{
					pNode.getChildList().get(0).
						setParent(pNode.getParent(), pNode);
				}
				else if (pNode.getChildList().isEmpty())
				{
					pNode.getParent().removeChild(pNode);
				}
				
				LinkedList<PQTreeNode<T>> otherChildList =
					new LinkedList<>(arr.get(1).getChildList());
				boolean otherEnd = otherChildList.getFirst().isFull();
				
				if (otherEnd)
				{
                                    for (PQTreeNode<T> node : otherChildList) {
                                        node.setParent(arr.get(0), end);
                                    }
				}
				else
				{
					java.util.Collections.reverse(otherChildList);
                                    for (PQTreeNode<T> node : otherChildList) {
                                        node.setParent(arr.get(0), end);
                                    }
				}
				
				arr.get(1).setParent(null);

				if (!isRoot)
				{
					returnNode =  this.getParent();
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
		return 'P';
	}


	/**
	 * This method is used for node ordering in algorithm second phase when moving backwards over layers.
	 * @return next node to process.
	 */
        @Override
	public PQTreeNode<T> getMaxMinSeq()
	{
//System.out.println("PNode: Collections.sort(this.getChildList() 0");
		Collections.sort(this.getChildList(), new PQTreeSeqComparator());
//System.out.println("PNode: Collections.sort(this.getChildList() 1");
		//TSSort.sort(this.getChildList(), new PQTreeSeqComparator());

		Iterator<PQTreeNode<T>> it = this.getChildList().iterator();

		int min = (int) 1E9;
		int max = -1;

		while (it.hasNext())
		{
			PQTreeNode<T> n = it.next();

			if (min > n.getMinSeq())
			{
				min = n.getMinSeq();
			}

			if (max < n.getMaxSeq())
			{
				max = n.getMaxSeq();
			}

		}

		this.setMinSeq(min);
		this.setMaxSeq(max);

		return this.getParent();
	}


	/**
	 * This method gets ML value of this node.
	 * @return ML integer value
	 */
	public int getMLValue()
	{
		return this.mlValue;
	}


	/**
	 * This method sets ML value of this node.
	 * @param MLValue new ML value.
	 */
	public void setMLValue(int MLValue)
	{
		this.mlValue = MLValue;
	}


	/**
	 * This method returns string value of this P-node.
	 * @return string value that is mainly used for debugging.
	 */
        @Override
	public String toString()
	{
		String s = super.toString();
		return s + " ML: " + this.mlValue; 
	}

	int mlValue;
}
