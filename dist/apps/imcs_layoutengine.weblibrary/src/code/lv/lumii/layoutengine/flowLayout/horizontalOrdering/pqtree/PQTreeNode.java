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
 * This class implements all common methods that are common to P, Q and C-type nodes.
 * @author Rudolfs
 * @param <T> object type
 */
abstract public class PQTreeNode<T>
{
	public PQTreeNode(PQTreeNode<T> parent, PQTree<T> tree)
	{
		this.parent = parent;
		this.childList = new LinkedList<>();
		if (parent != null)
			parent.addChild(this);
		this.visited = false;
		this.tree = tree;
		this.minSeq = (int) 1E9;
		this.maxSeq = -1;
	}
	
	
	/**
	 * This method returns parent of this method.
	 * @return parent tree node. If this is root node then parent is <code>null</code>.
	 */
	public PQTreeNode<T> getParent()
	{
		return this.parent;
	}
	
	
	/**
	 * This method adds child to this node.
	 * @param child new child node.
	 */
	public void addChild(PQTreeNode<T> child)
	{
		this.addChild(child, true);
	}
	
	
	/**
	 * This method adds child to this node to one of ends of child list.
	 * @param child new child node.
	 * @param front if true then will add in front of list else at the end.
	 */
	public void addChild(PQTreeNode<T> child, boolean front)
	{
		if (front)
		{
			this.childList.addFirst(child);
		}
		else
		{
			this.childList.addLast(child);
		}
	}
	
	
	/**
	 * This method sets new parent of this node and removes this from old parent's child lists.
	 * @param newParent new parent node.
	 * @param index place in child list of new parent where to put this node.
	 */
	public void setParent(PQTreeNode<T> newParent, int index)
	{
		if (this.parent != null)
		{
			this.parent.removeChild(this);
			this.parent = null;
		}
		
		if (newParent.childList.size() >= index)
		{
			if (index != newParent.getChildList().size())
			{
				newParent.childList.add(index, this);
			}
			else
			{
				newParent.childList.add(this);
			}
			this.parent = newParent;
		}
		else
		{
			this.setParent(newParent);
		}
	}
	
	
	/**
	 * This method changes parent of this node and adds it in child list in place of another node. 
	 * @param newParent new parent node.
	 * @param substitute place in which to put this node in new parent child list.
	 */
	public void setParent(PQTreeNode<T> newParent, PQTreeNode<T> substitute)
	{
		if (parent != null)
		{
			this.parent.removeChild(this);
			this.parent = null;
		}
		
		if (newParent.childList.contains(substitute))
		{
			int index = newParent.childList.indexOf(substitute);
			newParent.childList.remove(index);
			newParent.getChildList().add(index, this);
			this.parent = newParent;
		}
		else
		{
			this.setParent(newParent);
		}
	}
	
	
	/**
	 * This method changes parent of this node and adds it in child list in one of ends.
	 * @param newParent new parent node.
	 * @param front if true then add this node in front of child list otherwise at the end. 
	 */
	public void setParent(PQTreeNode<T> newParent, boolean front)
	{
		if (parent != null)
		{
			this.parent.removeChild(this);
		}
		
		this.parent = newParent;

		if (newParent != null)
		{
			newParent.addChild(this, front);
		}
	}
	
	
	/**
	 * This method changes parent of this node.
	 * @param newParent new parent node.
	 */
	public void setParent(PQTreeNode<T> newParent)
	{
		this.setParent(newParent, true);
	}
	
	
	/**
	 * This method removes child from this nodes child list.
	 * @param child to remove from child list.
	 */
	public void removeChild(PQTreeNode<T>child)
	{
		if (this.childList.contains(child))
		{
			this.childList.remove(child);
		}
	}
	
	
	/**
	 * This method sets this node as full.
	 */
	protected void setFull()
	{
		this.full = true;
	}
	

	/**
	 * This method sets this node as empty.
	 */
	public void setEmpty()
	{
		this.full = false;
		this.pertinent = false;
	}
	
	
	/**
	 * This method computes status of this node - full / pertinent / empty.
	 */
	public void update()
	{
		boolean b1 = true;
		boolean b2 = false;
		this.visited = true;
            for (PQTreeNode<T> node : this.childList) {
                b1 &= node.isFull() || this.tree.isInvisibleNode(node);
                b2 |= node.isFull() || node.isPertinent();
                
                this.visited &= node.isVisited();
            }
		
		this.full = this.pertinent = false;
		
		if (b1)
		{
			this.full = true;
		}
		else if (b2)
		{
			this.pertinent = true;
		}
	}
	
	
	/**
	 * This node returns whether this node is marked as full.
	 * @return boolean value that is true if this node is marked as full false otherwise.
	 */
	public boolean isFull()
	{
		return this.full;
	}
	
	
	/**
	 * This node returns whether this node is marked as pertinent.
	 * @return boolean value that is true if this node is marked as pertinent false otherwise.
	 */
	public boolean isPertinent()
	{
		return this.pertinent;
	}
	
	
	/**
	 * This method returns number of pertinent q-node children.
	 * @return number of pertinent q-node children.
	 */
	public int getPertQChildrenCount()
	{
		int rc = 0;
            for (PQTreeNode node : this.childList) {
                if ((node instanceof QNode) && (node.isPertinent()))
                {
                    rc++;
                }
            }
		return rc;
	}
	
	
	/**
	 * This method return child list iterator.
	 * @return iterator over all children.
	 */
	public Iterator<PQTreeNode<T>> getChildIterator()
	{
		return this.childList.iterator();
	}
	
	
	/**
	 * This method returns child list of this node.
	 * @return list of children.
	 */
	public LinkedList<PQTreeNode<T>> getChildList()
	{
		return this.childList;
	}
	
	
	/**
	 * This method clears child list.
	 */
	public void clearChildList()
	{
		this.childList.clear();
	}
	
	
	/**
	 * This method processes this node and makes all necessary transformations.
	 * @param isRoot boolean value whether this is pertinent tree root node.
	 * @return boolean value that represents successful transformation. 
	 */
	abstract public PQTreeNode<T> process(boolean isRoot);
	
	
	/**
	 * This method returns type of this node.
	 * @return integer type value. Types are coded as: 
	 * <li><code>PQTree.CNODE</code>, 
	 * <li><code>PQTree.QNODE</code>,
	 * <li><code>PQTree.PNODE</code>. 
	 * @see PQTree
	 */
	abstract public int getType();
	
	
	/**
	 * This method returns different char for each type of node.
	 * @return char.
	 */
	abstract public char getTypeChar();
	
	
	/**
	 * This method returns value that can be used for testing to
	 * identify this node.
	 * @return string value that represents this node.
	 */
	public String getTestValue()
	{
		//return Integer.toString(this.hashCode());
		return "";
	}


	/**
	 * This method returns string that represents this node.
	 */
        @Override
	public String toString()
	{
		String rc = "";
		rc += this.getTypeChar();
		rc += " ";
		rc += this.getTestValue();

		return rc;
	}


	/**
	 * This method sets new value of visited flag.
	 * @param value new value of visited flag.
	 */
	public void setVisited(boolean value)
	{
		this.visited = value;
	}


	/**
	 * This method checks if this node is visited.
	 * @return <code>True</code> iff this node is visited,
	 * <code>False</code> otherwise.
	 */
	public boolean isVisited()
	{
		return this.visited;
	}


	/**
	 * This method sets minimal sequence value.
	 * @param newValue new value of minimal sequence.
	 */
	public void setMinSeq(int newValue)
	{
		this.minSeq = newValue;
	}


	/**
	 * This method sets maximal sequence value.
	 * @param newValue new value of maximal sequence.
	 */
	public void setMaxSeq(int newValue)
	{
		this.maxSeq = newValue;
	}


	/**
	 * This method returns minimal sequence value of this node.
	 * @return minimal sequence value.
	 */
	public int getMinSeq()
	{
		return this.minSeq;
	}


	/**
	 * This method returns maximal sequence value of this node.
	 * @return maximal sequence value.
	 */
	public int getMaxSeq()
	{
		return this.maxSeq;
	}


	/**
	 * This method sets level value of this node. Level is counted as PQ-tree
	 * level. PQ-tree root is in 0-th level.
	 * @param index new level value.
	 */
	public void setLevel(int index)
	{
		this.level = index;
	}


	/**
	 * This method gets level value of this mode. Level is counted as PQ-tree
	 * level. PQ-tree root is in O-th level.
	 * @return level value.
	 */
	public int getLevel()
	{
		return this.level;
	}


	/**
	 * This method returns reference of owner tree.
	 * @return owner PQTree.
	 */
	public PQTree<T> getTree()
	{
		return this.tree;
	}


	/**
	 * This method sets owner PQTree of this node.
	 * @param tree new owner tree.
	 */
	public void setTree(PQTree<T> tree)
	{
		this.tree = tree;
	}


	/**
	 * This method computes minimal and maximal sequence values from its
	 * children and rotates to get child in such order that ones with minimal
	 * sequence is in the beginning of child list.
	 */
	abstract public PQTreeNode<T> getMaxMinSeq();


	private PQTreeNode<T> parent;
	private final LinkedList<PQTreeNode<T>> childList;
	private boolean full;
	private boolean pertinent;
	private int minSeq;
	private int maxSeq;
	private boolean visited;
	private int level;
	private PQTree<T> tree;
}
