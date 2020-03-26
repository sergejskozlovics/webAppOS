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

/**
 * This class implements C-type node for PQ-tree data structure.
 * @author Rudolfs
 * @param <T> object type
 */

public class CNode<T> extends PQTreeNode<T>{
	public CNode(PQTreeNode<T> parent, T o, PQTree<T> tree)
	{
		super(parent, tree);
		this.value = o;
	}


	/**
	 * This method gets object associated with this c-node.
	 * @return associated object.
	 */
	public T getObject()
	{
		return this.value;
	}


	/**
	 * This method sets object associated with this c-node.
	 * @param o new object.
	 */
	public void setObject(T o)
	{
		this.value = o;
	}


	/**
	 * This method returns int value to represent this node type.
	 * Node type is <code>PQTree.CNODE</code>
	 */
        @Override
	public int getType()
	{
		return PQTree.CNODE;
	}


	/**
	 * This method performs all transformations with this node.
	 */
        @Override
	public PQTreeNode<T> process(boolean isRoot)
	{
		this.setFull();
		this.setVisited(true);
		return this.getParent();
	}


	/**
	 * This method sets this node as empty.
	 */
        @Override
	public void setEmpty()
	{
		super.setEmpty();
	}


	/**
	 * This method returns string value that will represent this node for
	 * more comfortable testing.
	 */
        @Override
	public String getTestValue()
	{
		return this.getObject().toString();
//		char fullFlag;
//
//		if (this.isFull())
//		{
//			fullFlag = 'x';
//		}
//		else
//		{
//			fullFlag = ' ';
//		}
//
//		char hangingFlag = ' ';
//
//		if (this.getTree().isHangingNode(this))
//		{
//			hangingFlag = 'h';
//		}
//
//		// set different values for debug and application use
//
//		if (this.value instanceof LayeredNode &&
//			!((LayeredNode) this.value).isDummy())
//		{
//			return ((String)((LayeredNode) this.value).getOriginalNode().getTag()) + fullFlag + hangingFlag;
//		}
//		else if(this.value instanceof LayeredNode)
//		{
//			String beg = "D L: " +
//				((LayeredNode) this.value).getLevelNumber();
//
//			String en = " ["+((LayeredEdge)((LayeredNode)this.value).inEdge()).getOriginalEdge().getSourceNode().getTag() +", " +
//				((LayeredEdge)((LayeredNode)this.value).inEdge()).getOriginalEdge().getTargetNode().getTag() + "]";
//
//			return beg + en + fullFlag + hangingFlag;
//		}
//		else
//		{
//			return this.value.toString() + fullFlag + hangingFlag;
//		}
	}


	/**
	 * This method returns char that represents this node type.
	 */
        @Override
	public char getTypeChar()
	{
		return 'C';
	}


	/**
	 * This method is used for node ordering in algorithm second phase when moving backwards over layers.
	 * @return next node to process.
	 */
        @Override
	public PQTreeNode<T> getMaxMinSeq()
	{
		return this.getParent();
	}
	
	private T value;
}
