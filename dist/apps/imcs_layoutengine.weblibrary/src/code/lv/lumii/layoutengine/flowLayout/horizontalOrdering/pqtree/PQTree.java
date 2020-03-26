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
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import lv.lumii.layoutengine.flowLayout.horizontalOrdering.priorityqueue.PriorityQueue;
//import lv.lu.mii.graphlab.algorithms.layout.util.Sort;
//import algorithm.pqtree.*;

//import algorithm.priorityqueue.PriorityQueue;
//import com.tomsawyer.util.TSSort;
//import com.tomsawyer.util.TSSystem;
//import com.tomsawyer.algorithm.layout.hierarchical.layeredgraph.TSLayeredNode;

/**
 * This class implements PQTree data structure.
 * @author Rudolfs
 * @param <T> object type
 */

public class PQTree<T> {

// ---------------------------------------------------------------------
// Section: constructors and initializers
// ---------------------------------------------------------------------

	public PQTreeNode testNode;
	
	public PQTree()
	{
		this.CNodeList = new LinkedList<>();
		this.invisibleNodeSet = new HashSet<>();
		this.hangingNodeSet = new HashSet<>();

		this.ObjectToCNodeMap = new HashMap<>();
		this.llValue = -1;
		this.userObject = null;
	}
	
	/**
	 * This is constructor of pq-tree that creates initial pq-tree.
	 * @param U collection of objects that need to be in pq-tree.
	 */
	public PQTree(Collection<? extends T> U)
	{
		this.CNodeList = new LinkedList<>();
		this.invisibleNodeSet = new HashSet<>();
		this.hangingNodeSet = new HashSet<>();

		this.ObjectToCNodeMap = new HashMap<>();

		this.llValue = -1;
		this.userObject = null;

		// make universal PQTree
		
		this.root = new QNode<>(null, this);

		PNode<T> p = new PNode<>(this.root, this);

		Iterator<? extends T> iter = U.iterator();
		
		while (iter.hasNext())
		{
			T i = iter.next();
			CNode<T> c = new CNode<>(p, i, this);
			this.CNodeList.add(c);
			this.ObjectToCNodeMap.put(i, c);
		}
	}


	/**
	 * This method clears all pq-tree - structure and objects.
	 */
	private void clear()
	{
		this.root = null;
		this.CNodeList = new LinkedList<>();
		this.ObjectToCNodeMap.clear();
		this.hangingNodeSet.clear();
	}


	/**
	 * Method that copies pq-tree structure from another pq-tree.
	 * @param sourceObject source pq-tree from which structure is copied.
	 */
	public void copy(PQTree<T> sourceObject)
	{
		this.clear();
		this.llValue = sourceObject.llValue;

		LinkedList<PQTreeNode<T>> queue = new LinkedList<>();
		queue.add(sourceObject.root);

		boolean toRoot = true;
		HashMap<PQTreeNode<T>, PQTreeNode<T>> copyMap = new HashMap<>();

		while (!queue.isEmpty())
		{
			PQTreeNode<T> node = queue.removeFirst();
			PQTreeNode<T> newNode = null;

			if (node.getType() == PQTree.QNODE)
			{
				newNode = new QNode<>(copyMap.get(node.getParent()),
					this);
			}
			else if (node.getType() == PQTree.PNODE)
			{
				newNode = new PNode<>(copyMap.get(node.getParent()),
					this);
			}
			else if (node.getType() == PQTree.CNODE)
			{
				newNode = new CNode<>(copyMap.get(node.getParent()),
					((CNode<T>)node).getObject(),
					this);
				this.ObjectToCNodeMap.put(((CNode<T>) node).getObject(), (CNode<T>) newNode);
				this.CNodeList.add((CNode<T>)newNode);
			}

			copyMap.put(node, newNode);
			if (toRoot)
			{
				this.root = newNode;
			}
			toRoot = false;

			queue.addAll(node.getChildList());
		}
            for (PQTreeNode o : sourceObject.hangingNodeSet) {
                this.hangingNodeSet.add(copyMap.get(o));
            }
	}


// ---------------------------------------------------------------------
// Section: getters and setters
// ---------------------------------------------------------------------


	/**
	 * This method sets invisible node set.
	 * @param s set of objects that are invisible.
	 */
	public void setInvisibleNodeSet(Set<T> s)
	{
		this.invisibleNodeSet.clear();

		if (this.CNodeList.size() < s.size())
		{
                        Iterator<CNode<T>> iter = this.CNodeList.iterator();
			for (; iter.hasNext();)
			{
				CNode<T> cNode = iter.next();

				if (s.contains(cNode.getObject()))
				{
					this.invisibleNodeSet.add(cNode);
				}
			}
		}
		else
		{
			Iterator<T> iter = s.iterator();
			for (; iter.hasNext();)
			{
				T o = iter.next();

				if (this.ObjectToCNodeMap.containsKey(o))
				{
					CNode<T> cNode = this.ObjectToCNodeMap.get(o);
					this.invisibleNodeSet.add(cNode);
				}
			}
		}
	}

	/**
	 * This method returns whether this node is invisible.
	 * @param node node to be tested for being invisible.
	 * @return <code>true</code> if this is invisible node, <code>false</code> otherwise.
	 */
	public boolean isInvisibleNode(PQTreeNode<T> node)
	{
            if(node.getType() != CNODE) {
                return false;
            } else {
		return this.invisibleNodeSet.contains((CNode<T>)node);
            }
	}


	public void setHangingNodeSet(Set<T> hangingNodeSet)
	{
            for (T o : hangingNodeSet) {
                CNode<T> cNode = this.ObjectToCNodeMap.get(o);
                
                this.hangingNodeSet.add(cNode);
            }
	}

	/**
	 * This method returns whether this node is hanging.
	 * @param node node to be tested for being hanging.
	 * @return <code>true</code> if this is hanging node, <code>false</code> otherwise.
	 */
	public boolean isHangingNode(CNode node)
	{
		return this.hangingNodeSet.contains(node);
	}


	public List<T> getObjects()
	{
		ArrayList<T> retList = new ArrayList<>(this.CNodeList.size());

                for(CNode<T> node : CNodeList) {
                    retList.add(node.getObject());
                }

		return retList;
	}

	public void setLLValue(int llValue)
	{
		this.llValue = llValue;
	}

	public int getLLValue()
	{
		return this.llValue;
	}

	public Collection<T> getUserObject()
	{
		return this.userObject;
	}

	public void setUserObject(Collection<T> userObject)
	{
		if (!this.isUserObjectInUse())
		{
			this.userObject = userObject;
		}
		else
		{
			throw new RuntimeException("User object allready in use.");
		}
	}

	public boolean isUserObjectInUse()
	{
		return this.userObject != null;
	}

	public void clearUserObject()
	{
		this.userObject = null;
	}


		/**
	 * This method returns possible permutation of objects in pq-tree.
	 * @param sequenceRulesList list of node sets that restrict that nodes from
	 * set in i-th position should be no further than nodes from i+1-th set.
	 * @return list of objects that are in valid order.
	 */
	public LinkedList<T> getList(List<? extends Set<T>> sequenceRulesList)
	{

		if (sequenceRulesList != null && !sequenceRulesList.isEmpty())
		{

			PriorityQueue<PQTreeNode> orderQueue = new PriorityQueue<>(
				this.CNodeList.size(),
				new PQTreeNodeLevelComparator());

			for(CNode<T> cNode : CNodeList) {
				cNode.setMinSeq((int) 1E9);
				cNode.setMaxSeq(-1);
				orderQueue.add(cNode);
			}

			int cnt = 0;
                    for (Set<T> s : sequenceRulesList) {
                        for (Iterator<T> iter = s.iterator(); iter.hasNext();)
                        {
                            CNode<T> cNode = this.ObjectToCNodeMap.get(iter.next());
                            
                            if (cNode != null)
                            {
                                cNode.setMinSeq(Math.min(cnt, cNode.getMinSeq()));
                                cNode.setMaxSeq(Math.max(cnt, cNode.getMaxSeq()));
                            }
                        }
                        cnt++;
                    }

			Set<PQTreeNode> visitedNodeSet = new HashSet<>();

			while (!orderQueue.isEmpty())
			{
				PQTreeNode node = (PQTreeNode) orderQueue.removeMin();

				PQTreeNode ret = node.getMaxMinSeq();

				if (ret != null && !visitedNodeSet.contains(ret))
				{
					orderQueue.add(ret);
					visitedNodeSet.add(ret);
				}
			}
		}

		LinkedList<CNode<T>> resultList = new LinkedList<>();

		LinkedList<PQTreeNode<T>> stack = new LinkedList<>();
		stack.add(this.root);

		while (!stack.isEmpty())
		{
			PQTreeNode<T> node = stack.removeFirst();

			if (node.getType() == PQTree.CNODE)
			{
				resultList.addLast((CNode<T>)node);
			}
			else
			{
				List<PQTreeNode<T>> childList = new LinkedList<>(node.getChildList());

				Collections.reverse(childList);
                            for (PQTreeNode<T> child : childList) {
                                stack.addFirst(child);
                            }
			}
		}

		List<CNode<T>> copiedList = new ArrayList<>(resultList);
		
		Sort<CNode<T>> mySort = new Sort<>();
		
		mySort.sort(resultList,
			new PQTreeNodeStableComparator(this.hangingNodeSet, copiedList));
		
		LinkedList<T> returnList = new LinkedList<>();

		for (int i = 0; i < resultList.size(); i++)
		{
			CNode<T> node = resultList.get(i);
			returnList.add(node.getObject());
		}

		return returnList;
	}

	protected List<CNode<T>> getCNodeList()
	{
		return this.CNodeList;
	}

// ---------------------------------------------------------------------
// Section: methods related to transformations
// ---------------------------------------------------------------------


	/**
	 * This method adds rule to pq-tree that some set of objects needs to be
	 * together.
	 * @param ruleObjectSet objects that needs to be together.
	 * @return boolean value whether this rule was feasible.
	 */
	public boolean addRule(Collection<T> ruleObjectSet)
	{
		boolean rc = true;

		Set<PQTreeNode> processedNodes = new HashSet<>();

		PriorityQueue<PQTreeNode> priorityQueue =
			new PriorityQueue<>(this.CNodeList.size(),
				new PQTreeNodeLevelComparator());

		if (this.ObjectToCNodeMap.keySet().containsAll(ruleObjectSet))
		{
			this.updateLevelInfo();

			int priorityQSize = 0;

			for (Iterator<T> iter = ruleObjectSet.iterator();iter.hasNext();)
			{
				PQTreeNode node = this.ObjectToCNodeMap.get(iter.next());

				priorityQueue.add(node);

				priorityQSize++;
			}

			while (!priorityQueue.isEmpty())
			{
				PQTreeNode n = (PQTreeNode) priorityQueue.removeMin();
				priorityQSize--;

				PQTreeNode ret = n.process(priorityQSize == 0);

				if (ret != null && !processedNodes.contains(ret))
				{
					priorityQueue.add(ret);
					processedNodes.add(ret);
					priorityQSize++;
				}
			}
		}
		else
		{
			rc = false;
		}

		this.cleanUp();

		return rc;
	}

	/**
	 * This method updates level information in each PQ-tree node. This is
	 * used with priority queue to traverse tree correctly.
	 */
	public void updateLevelInfo()
	{
		LinkedList<PQTreeNode<T>> queue = new LinkedList<>();

		queue.add(this.root);
		int levelIndex = 0;
		int levelCapacity = 1;

		while (!queue.isEmpty())
		{
			PQTreeNode<T> node = queue.removeFirst();

			node.setLevel(levelIndex);
			levelCapacity--;
			queue.addAll(node.getChildList());

			if (levelCapacity == 0)
			{
				levelIndex++;
				levelCapacity = queue.size();
			}
		}
	}

	/**
	 * This node removes all full signs all nodes. This is necessary after every
	 * new addRule operation.
	 */
	public void cleanUp()
	{
		LinkedList<PQTreeNode<T>> queue = new LinkedList<>();

		queue.add(this.root);

		while (!queue.isEmpty())
		{
			PQTreeNode<T> node = queue.removeFirst();
			node.setEmpty();
			node.setVisited(false);

			queue.addAll(node.getChildList());
		}
	}


	/**
	 * This method finishes pq-tree - removes unnecessary nodes from pq-tree -
	 * those with one child.
	 */
	public void seal()
	{
		while (this.root.getChildList().size() == 1 &&
			this.root.getType() != PQTree.CNODE)
		{
			this.root = this.root.getChildList().get(0);
			this.root.setParent(null);
		}
	}


	/**
	 * This method normalizes PQ-tree:
	 * <ul>
	 * 	<li> replaces all p-nodes with two children with q-node
	 * 	<li> removes all nodes with just one child.
	 * </ul>
	 */
	public void normalize()
	{
		LinkedList<PQTreeNode<T>> queue = new LinkedList<>();

		queue.add(this.root);

		while (!queue.isEmpty())
		{
			PQTreeNode<T> node = queue.removeFirst();

			if (node.getChildList().size() == 2 &&
				node.getType() == PQTree.PNODE)
			{
				PQTreeNode<T> parent = node.getParent();

				QNode<T> newQNode = new QNode<>(null, this);
				newQNode.setParent(parent, node);

				while (!node.getChildList().isEmpty())
				{
					PQTreeNode<T> childNode = node.getChildList().getFirst();
					childNode.setParent(newQNode);
				}
			}

			queue.addAll(node.getChildList());
		}

		this.removeUnnecessary();
	}


	/**
	 * This method removes all nodes from tree that has only one child node.
	 */
	public void removeUnnecessary()
	{
		LinkedList<PQTreeNode<T>> queue = new LinkedList<>();

		queue.add(this.root);

		while (!queue.isEmpty())
		{
			PQTreeNode<T> node = queue.removeFirst();

			if (node.getParent() != null &&
				node.getChildList().size() == 1)
			{
				PQTreeNode<T> parent = node.getParent();

				PQTreeNode<T> child = node.getChildList().get(0);

				queue.add(child);

				child.setParent(parent, node);

				node = child;
			}

			queue.addAll(node.getChildList());
		}
	}


// ---------------------------------------------------------------------
// Section: debug related methods
// ---------------------------------------------------------------------


	/**
	 * This method prints all pq-tree structure to console.
	 */
	public void print()
	{
		LinkedList<PQTreeNode<T>> nodeStack = new LinkedList<>();
		LinkedList<Integer> stack = new LinkedList<>();
		
		int intention = 0;
		String intent;
		
		nodeStack.add(this.root);
		stack.add(new Integer(1));

		System.out.println("LL Value :" + this.llValue);
		
		while (!nodeStack.isEmpty())
		{
			//PQTreeNode<T> node = queue.getFirst();
			PQTreeNode<T> node = nodeStack.removeLast();
			
			intent = "";
			
			for (int i=0;i<intention;i++)
			{
				intent +=" ";
			}
//			System.out.println(intent + '|');
			System.out.println(intent + node.toString());
			
//			int top = ((Integer) stack.getLast()).intValue();
			int top = ((Integer) stack.removeLast()).intValue();
			top--;
			stack.addLast(new Integer(top));
			
			if (node.getType() != PQTree.CNODE)
			{
				stack.addLast(new Integer(node.getChildList().size()));
				nodeStack.addAll(node.getChildList());
				intention++;
			}
			
			while (!stack.isEmpty() && ((Integer) stack.getLast()).intValue() == 0)
			{
				intention--;
				stack.removeLast();
			}
			
		}
	}


//	public Graph exportToGraph()
//	{
//		Graph graph = new Graph();
//		LinkedList queue = new LinkedList();
//
//		int level = 0;
//		int levelSize = 1;
//		queue.add(this.root);
//
//		while (levelSize != 0)
//		{
//			while (levelSize > 0)
//			{
//				PQTreeNode pqNode = (PQTreeNode) queue.removeFirst();
//
//				Node node = graph.addNode(level);
//
//				if (pqNode.getType() == PQTree.QNODE)
//				{
//					node.setShape(new Rectangle2D.Double(0, 0, 10, 10));
//				}
//				else if (pqNode.getType() == PQTree.PNODE)
//				{
//					node.setShape(new Ellipse2D.Double(0, 0, 10, 10));
//				}
//				else if (pqNode.getType() == PQTree.CNODE)
//				{
//					node.setShape(new Ellipse2D.Double(0, 0, 5, 10));
//				}
//
//				queue.addAll(pqNode.getChildList());
//				node.setCenter(levelSize * 40, (level + 2) * 30);
//				levelSize--;
//			}
//
//			level++;
//			levelSize = queue.size();
//		}
//
//		return graph;
//	}


// ---------------------------------------------------------------------
// Section: replace related methods
// ---------------------------------------------------------------------


	/**
	 * This method replaces one object in c-node connection with another.
	 * @param existingObject object that currently is in pq-tree.
	 * @param newObject object that will be inserted in pq-tree in place of
	 * existing object.
	 */
	public void replace(T existingObject, T newObject)
	{
		CNode<T> cNode = this.ObjectToCNodeMap.get(existingObject);

		boolean continueHang = this.hangingNodeSet.contains(cNode);

		if (cNode != null && cNode.getObject() == existingObject)
		{
			PNode<T> newParent = new PNode<>(null, this);
			newParent.setParent(cNode.getParent(), cNode);
			cNode.setParent(newParent);

			this.ObjectToCNodeMap.put(newObject, cNode);
			cNode.setObject(newObject);
		}
		else if (cNode != null && cNode.getObject() != existingObject)
		{
			PQTreeNode<T> parent = cNode.getParent();

			if (parent.getType() == PQTree.PNODE)
			{
				CNode<T> newCNode = new CNode<>(parent, newObject, this);
				this.CNodeList.add(newCNode);
				this.ObjectToCNodeMap.put(newObject, newCNode);
				cNode = newCNode;
			}
			else if (parent.getType() == PQTree.QNODE)
			{
				PNode<T> newParent = new PNode<>(null, this);
				newParent.setParent(parent, cNode);
				cNode.setParent(newParent);
				CNode<T> newCNode = new CNode<>(newParent, newObject, this);
				this.CNodeList.add(newCNode);
				this.ObjectToCNodeMap.put(newObject, newCNode);
				cNode = newCNode;
			}
		}

		if (continueHang)
		{
			this.hangingNodeSet.add(cNode);
		}
	}


	/**
	 * This method inserts new node in PQ-tree so that it would fit into
	 * place of replaceable node list.
	 * @param replaceNodeList list of objects that represents list of nodes
	 * from previous level which to replace with new object.
	 * @param newObject new object that will be placed in place of
	 * replaceable objects.
	 */
	public void replaceList(LinkedList<T> replaceNodeList, T newObject)
	{
		//this.normalize();
		this.removeUnnecessary();
		this.updateLevelInfo();

		if (replaceNodeList.size() > 2)
		{
			boolean canStop = false;

			int cnt = 0;
			int bestLevel = (int)1E8;
			int bestO = 0;
			int another = 1;
			boolean stright = false;

			for (Iterator<T> it = replaceNodeList.iterator();
				!canStop && it.hasNext();cnt++)
			{
				T origObject = it.next();
				CNode<T> cNode = this.ObjectToCNodeMap.get(origObject);

				if (bestLevel > cNode.getLevel() ||
					(bestLevel == cNode.getLevel() &&
						!stright))
				{
					bestO = cnt;

					bestLevel = Math.min(bestLevel, cNode.getLevel());

					stright = cNode.getObject() == origObject;
				}
			}

			if (stright)
			{
				CNode<T> cNode = this.ObjectToCNodeMap.
                                        get(replaceNodeList.get(bestO));
				this.replace(cNode.getObject(), newObject);
			}
			else
			{
				if (bestO == another)
				{
					another = 0;
				}

				CNode<T> cn = new CNode<>(null, newObject, this);
				//System.out.println(cn);

				PQTreeNode<T> fParent = this.ObjectToCNodeMap.
					get(replaceNodeList.get(another));
				PQTreeNode<T> sParent = this.ObjectToCNodeMap.
					get(replaceNodeList.get(bestO));

				CNode<T> bestNode = (CNode<T>) sParent;

				while (fParent.getLevel() != sParent.getLevel())
				{
					fParent = fParent.getParent();
				}

				while (fParent.getParent() != sParent.getParent())
				{
					fParent = fParent.getParent();
					sParent = sParent.getParent();
				}

				int ind1 = fParent.getParent().getChildList().indexOf(fParent);
				int ind2 = fParent.getParent().getChildList().indexOf(sParent);

				int i = bestNode.getParent().getChildList().indexOf(bestNode);

				CNode<T> newCNode = new CNode<>(null, newObject, this);
				this.CNodeList.add(newCNode);
				this.ObjectToCNodeMap.put(newObject, newCNode);

				if (ind1 < ind2)
				{
					newCNode.setParent(bestNode.getParent(), i);
				}
				else
				{
					if (bestNode.getParent().getChildList().size() == i+1)
					{
						newCNode.setParent(bestNode.getParent());
					}
					else
					{
						newCNode.setParent(bestNode.getParent(), i + 1);
					}
				}
			}
		}
		else
		{
			CNode<T> cNode1 = this.ObjectToCNodeMap.
				get(replaceNodeList.getFirst());
			CNode<T> cNode2 = this.ObjectToCNodeMap.
				get(replaceNodeList.getLast());

			if (cNode1.getParent() == cNode2.getParent())
			{
				LinkedList chList =
					(LinkedList) cNode1.getParent().getChildList();

				int place1 = chList.indexOf(cNode1);
				int place2 = chList.indexOf(cNode2);

				CNode<T> newCNode = new CNode<>(null, newObject, this);
				newCNode.setParent(cNode1.getParent(),
					(place1 + place2 + 1) / 2);
				this.ObjectToCNodeMap.put(newObject, newCNode);
				this.CNodeList.add(newCNode);
			}
			else
			{
				this.updateLevelInfo();

				if (cNode1.getLevel() < cNode2.getLevel())
				{
					CNode<T> cNodeTmp = cNode1;
					cNode1 = cNode2;
					cNode2 = cNodeTmp;
				}

				// cNode1.Level > cNode2.Level

				PQTreeNode<T> cNode1Parent = cNode1;

				while (cNode1Parent.getLevel() != cNode2.getLevel())
				{
					cNode1Parent = cNode1Parent.getParent();
				}

				PQTreeNode<T> cNode2Parent = cNode2;

				while (cNode2Parent.getParent() != cNode1Parent.getParent())
				{
					cNode1Parent = cNode1Parent.getParent();
					cNode2Parent = cNode2Parent.getParent();
				}

				if (cNode2Parent.getParent().getType() != PQTree.QNODE)
				{
//					System.out.println("Common ancestor not Q - type node");
//					TSSystem.tsAssert(false,
//						"Common ancestor not Q type node");
					CNode<T> newCNode = new CNode<>(cNode2Parent.getParent(),
						newObject,
						this);
                    this.CNodeList.add(newCNode);
                    this.ObjectToCNodeMap.put(newObject, newCNode);
				}
				else
				{
					int place1 = cNode1Parent.getParent().
						getChildList().indexOf(cNode1Parent);
					int place2 = cNode2Parent.getParent().getChildList().
						indexOf(cNode2Parent);

					CNode<T> newCNode = new CNode<>(null, newObject, this);
					newCNode.setParent(cNode1Parent.getParent(),
						(place1 + place2 + 1) / 2);
					this.ObjectToCNodeMap.put(newObject, newCNode);
					this.CNodeList.add(newCNode);
				}
			}
		}
	}


	/**
	 * This method removes all c-nodes that are connected with objects from U.
	 * @param U collection of removable objects.
	 */
	public void remove(Collection<T> U)
	{
            for (T ob : U) {
                CNode<T> cNode = this.ObjectToCNodeMap.get(ob);
                
                if (cNode != null && cNode.getObject() == ob)
                {
                    PQTreeNode<T> curNode = cNode;
                    PQTreeNode<T> parent;
                    this.CNodeList.remove(cNode);
                    
                    do
                    {
                        parent = curNode.getParent();
                        curNode.setParent(null);
                        curNode = parent;
                    } while(parent != null &&
                            parent.getChildList().isEmpty());
                }
            }
	}


	/**
	 * This method adds new element to PQ-tree. Element is added as child of P
	 * node. If none of two top vertices are P type then new top two vertices
	 * are added to PQ-tree.
	 * @param o new object that will be represented by new c-node.
	 */
	public void addElement(T o)
	{
		PQTreeNode<T> node = this.root;

		if (node.getType() == PQTree.PNODE)
		{
		}
		else if (node.getChildList().size() == 1 &&
                        node.getChildList().get(0).getType() == PQTree.PNODE)
		{
			node = node.getChildList().get(0);
		}
		else
		{
			QNode<T> newRoot = new QNode<>(null, this);
			node = new PNode<>(newRoot, this);
			this.root.setParent(node);
			this.root = newRoot;
		}

		CNode<T> cNode = new CNode<>(node, o, this);
		this.CNodeList.add(cNode);
		this.ObjectToCNodeMap.put(o, cNode);
	}


// ---------------------------------------------------------------------
// Section: PQTree merging related methods
// ---------------------------------------------------------------------

	/**
	 * This method implements two PQ-Tree merge operation. This and specified
	 * trees are merged together so that they share common object.
	 * @param t1 specified PQ-Tree that will be merged into this tree.
	 * @param o object that is common for both trees.
	 * @param allObjects List of all objects to whom t1 is merged.
	 */
	public void insert(PQTree<T> t1, T o, List<T> allObjects)
	{
		PQTreeNode<T> n = this.ObjectToCNodeMap.get(o);
		PQTreeNode<T> nChild;

		if (n == null)
		{
			throw new RuntimeException("Can not merge trees");
		}

		int t1LLValue = t1.getLLValue();
		boolean merged;

		do
		{
			nChild = n;
			n = n.getParent();
			merged = this.mergeCondition(n, nChild, t1LLValue, t1, allObjects);
		} while (!merged);


	}

	/**
	 * This method tries to run each merge condition and if any fit then merges.
	 * two trees together.
	 * @param n parent vertex.
	 * @param nChild child node that contains common vertex as child.
	 * @param t1LLValue LL value of second PQ-Tree.
	 * @param t1 second tree to be merged into this tree.
	 * @param allObjects list of objects to whom t1 is merged to.
	 * @return <code>True</code> iff one condition fits to this situation.
	 */
	private boolean mergeCondition(
		PQTreeNode<T> n,
		PQTreeNode<T> nChild,
		int t1LLValue,
		PQTree<T> t1,
		List<T> allObjects)
	{
		return aMergeCondition(n, nChild, t1LLValue, t1, allObjects) ||
			bMergeCondition(n, nChild, t1LLValue, t1) ||
			cMergeCondition(n, nChild, t1LLValue, t1) ||
			dMergeCondition(n, nChild, t1LLValue, t1) ||
			eMergeCondition(n, t1);
	}


	/**
	 * This method implements A merge condition - when parent is of P type.
	 * @param n P type parent node.
	 * @param nChild child from which we came.
	 * @param t1LLValue LL value of second tree.
	 * @param t1 second PQ-Tree
	 * @param allObjects list of all objects to whom this is merged to.
	 * @return <code>True</code> iff A merge condition was true and two PQ-Trees
	 * were merged together, <code>False</code> otherwise.
	 */
	private boolean aMergeCondition(
		PQTreeNode<T> n,
		PQTreeNode<T> nChild,
		int t1LLValue,
		PQTree<T> t1,
		List<T> allObjects)
	{
		boolean rc = false;

		if (n.getType() == PQTree.PNODE)
		{
			PNode<T> p = (PNode<T>) n;

			if (p.getMLValue() < t1LLValue)
			{
				Set<T> childObjectSet = n.getTree().getChildObjects(n);
				Set<T> mergableSet = new HashSet<>(allObjects);

				if (mergableSet.containsAll(childObjectSet) || allObjects.size() == 1)
				{
					t1.root.setParent(n);
					t1.replaceTree(this);
				}
				else
				{
///					TSSystem.tsAssert(n.getTree() == this);
					
					PNode<T> pNew = new PNode<>(n, n.getTree());
					nChild.setParent(pNew);
					
					t1.root.setParent(pNew);
					t1.replaceTree(this);
				}

				rc = true;
			}
		}

		return rc;
	}


	/**
	 * This method implements B merge condition - when parent is of Q type and
	 * child is at the end of child list.
	 * @param n Q type parent node.
	 * @param nChild child node of Q that is at the end of child list.
	 * @param t1LLValue LL value of second tree.
	 * @param t1 second PQ-Tree
	 * @return <code>True</code> iff B merge condition was true and two PQ-Trees
	 * were merged together, <code>False</code> otherwise.
	 */
	private boolean bMergeCondition(
		PQTreeNode<T> n,
		PQTreeNode<T> nChild,
		int t1LLValue,
		PQTree<T> t1)
	{
		boolean rc = false;

		if (n.getType() == PQTree.QNODE)
		{
			QNode q = (QNode) n;
			List l = q.getChildList();
			int index = l.indexOf(nChild);


			if (index == 0 && q.getMLValue(index) < t1LLValue)
			{
				// nChild is first in n child list

				int oldMLValue = q.getMLValue(index);

				QNode<T> newQ = new QNode<>(null, this);

				newQ.setParent(n, nChild);
				newQ.setMLValue(0, oldMLValue, true);
				nChild.setParent(newQ);
				t1.root.setParent(newQ);
				t1.replaceTree(this);

				rc = true;
			}
			else if (index == l.size() - 1 &&
				q.getMLValue(index - 1) < t1LLValue)
			{
				// nChild is last in n child list

				int oldMLValue = q.getMLValue(index - 1);

				QNode<T> q1 = new QNode<>(null, this);

				q1.setParent(n, nChild);
				q1.setMLValue(0, oldMLValue, true);
				nChild.setParent(q1);
				t1.root.setParent(q1);
				t1.replaceTree(this);

				rc = true;
			}
		}
		return rc;
	}


	/**
	 * This method implements C merge condition - when parent is of Q type and
	 * child is not at the end of child list.
	 * @param n Q type parent node.
	 * @param nChild child node of Q that is not at the end of child list.
	 * @param t1LLValue LL value of second tree.
	 * @param t1 second PQ-Tree
	 * @return <code>True</code> iff C merge condition was true and two PQ-Trees
	 * were merged together, <code>False</code> otherwise.
	 */
	private boolean cMergeCondition(
		PQTreeNode<T> n,
		PQTreeNode<T> nChild,
		int t1LLValue,
		PQTree<T> t1)
	{
		boolean rc = false;

		if (n.getType() == PQTree.QNODE)
		{
			QNode<T> q = (QNode<T>) n;
			List l = q.getChildList();
			int index = l.indexOf(nChild);

			if (index != 0 &&
				index != l.size() &&
				q.getMLValue(index - 1) < t1LLValue &&
				q.getMLValue(index) < t1LLValue)
			{
				QNode<T> newQ = new QNode<>(null, this);
				int mlfirstValue = q.getMLValue(index - 1);
				int mlsecondValue = q.getMLValue(index);

				newQ.setParent(n, nChild);
				newQ.setMLValue(0,
					Math.max(mlfirstValue, mlsecondValue),
					true);
				nChild.setParent(newQ);
				t1.root.setParent(newQ);

				t1.replaceTree(this);

				rc = true;
			}
		}

		return rc;
	}

	/**
	 * This method implements D merge condition - when there is unambiguous
	 * place to insert if parent node is of Q type.
	 * @param n Q type parent node.
	 * @param nChild child node of Q that is not at the end of child list.
	 * @param t1LLValue LL value of second tree.
	 * @param t1 second PQ-Tree
	 * @return <code>True</code> iff D merge condition was true and two PQ-Trees
	 * were merged together, <code>False</code> otherwise.
	 */
	private boolean dMergeCondition(
		PQTreeNode<T> n,
		PQTreeNode<T> nChild,
		int t1LLValue,
		PQTree<T> t1)
	{
		boolean rc = false;

		if (n.getType() == PQTree.QNODE)
		{
			QNode<T> q = (QNode<T>) n;
			List l = q.getChildList();
			int index = l.indexOf(nChild);

			if (index != 0 &&
				index != l.size() &&
				q.getMLValue(index - 1) < t1LLValue &&
				q.getMLValue(index) <= t1LLValue)
			{
				int mlfirstValue = q.getMLValue(index - 1);

				t1.root.setParent(n, index);
				t1.replaceTree(this);

				q.setMLValue(index, mlfirstValue, true);
				q.setMLValue(index + 1, mlfirstValue, false);

				rc = true;
			}
			else if (index != 0 &&
				index != l.size() &&
				q.getMLValue(index - 1) <= t1LLValue &&
				q.getMLValue(index) < t1LLValue)
			{
				int mlsecondValue = q.getMLValue(index);

				t1.root.setParent(n, index + 1);
				t1.replaceTree(this);

				q.setMLValue(index, mlsecondValue, true);
				q.setMLValue(index + 1, mlsecondValue, false);

				rc = true;
			}
		}

		return rc;
	}


	/**
	 * This method implements E merge condition - when two PQTrees have equal
	 * LL values.
	 * @param n - parent node.
	 * @param t1 - other tree.
	 * @return <code>True</code> iff E merge condition was true and two PQ-Trees
	 * were merged together, <code>False</code> otherwise.
	 */
	private boolean eMergeCondition(
			PQTreeNode<T> n,
			PQTree<T> t1)
	{
		boolean rc = false;

		if (n.getParent() == null)
		{
			// n == root of tree

			QNode<T> q = new QNode<>(null, this);

			n.setParent(q);
			this.root = q;
			t1.root.setParent(q);
			q.setMLValue(0, 0, true);

			t1.replaceTree(this);
			rc = true;
		}

		return rc;
	}


	/**
	 * This method replaces all occurrences of this PQ-Tree in its nodes with
	 * given tree. This is used when two PQ-Trees are merged together.
	 * @param tree PQ-Tree that will be inserted in place of current tree.
	 */
	public void replaceTree(PQTree<T> tree)
	{
		LinkedList<PQTreeNode<T>> queue = new LinkedList<>();

		tree.addToCNodeList(this.CNodeList);

		queue.add(this.root);

		while (!queue.isEmpty())
		{
			PQTreeNode<T> node = queue.removeFirst();
			node.setTree(tree);
			queue.addAll(node.getChildList());
		}
	}


	/**
	 * This method returns all objects that are in subtree of this node.
	 * @param node PQTreeNode which subtree is analyzed.
	 * @return Set of objects that belongs to the C nodes below this node.
	 */
	public Set<T> getChildObjects(PQTreeNode<T> node)
	{
		Set<T> returnSet = new HashSet<>();

		LinkedList<PQTreeNode<T>> queue = new LinkedList<>();
		queue.add(node);

		while (!queue.isEmpty())
		{
			PQTreeNode<T> n = queue.removeFirst();

			if (n.getType() == PQTree.CNODE)
			{
				returnSet.add(((CNode<T>) n).getObject());
			}

			queue.addAll(n.getChildList());
		}

		return returnSet;
	}


	/**
	 * This method adds C-type nodes to this PQTree's c-node list.
	 * @param c collection of C-type nodes.
	 */
	protected void addToCNodeList(Collection<CNode<T>> c)
	{
                for(CNode<T> cNode : c) {
			this.CNodeList.add(cNode);
			this.ObjectToCNodeMap.put(cNode.getObject(), cNode);
                }
	}


// ---------------------------------------------------------------------
// Section: instance variables
// ---------------------------------------------------------------------

	public PQTreeNode<T> root;
	private List<CNode<T>> CNodeList;
	private final Map<T, CNode<T>> ObjectToCNodeMap;
	private final Set<CNode<T>> invisibleNodeSet;
	private final Set<PQTreeNode> hangingNodeSet;
	private Collection<T> userObject;
	private int llValue;

// ---------------------------------------------------------------------
// Section: constants
// ---------------------------------------------------------------------

	public static final int PNODE = 1;
	public static final int QNODE = 2;
	public static final int CNODE = 3;
}
