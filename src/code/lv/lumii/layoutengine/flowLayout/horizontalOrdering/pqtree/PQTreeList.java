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

import lv.lumii.layoutengine.flowLayout.horizontalOrdering.LayeredGraph;
import lv.lumii.layoutengine.flowLayout.horizontalOrdering.LayeredNode;
import lv.lumii.layoutengine.flowLayout.horizontalOrdering.LayeredEdge;
import lv.lumii.layoutengine.flowLayout.horizontalOrdering.LayeredNodeLevelComparator;


/**
 * This class implements list of PQ-Trees that support all list operations and
 * several others. All these special operations lighten work with rules that
 * applies to several trees. Rules are applied separately for each tree and they
 * are merged together if needed.
 * @author Rudolfs
 * @param <T> object type
 */
public class PQTreeList<T> extends ArrayList<PQTree<T>>
{
	public PQTreeList()
	{
		super();
		this.object2TreeMap = new HashMap<>();
	}


	/**
	 * This method returns tree that is in given place in this list.
	 * @param index index of necessary tree.
	 * @return required PQTree.
	 */
	public PQTree<T> getTree(int index)
	{
		return super.get(index);
	}


	/**
	 * This method adds new PQTree to this list.
	 * @param o new object that will be the only one in new PQTree.
	 * @param LLValue LL value on new PQTree.
	 */
	public void addTree(T o, int LLValue)
	{
		PQTree<T> tree = new PQTree<>(Collections.singletonList(o));

		tree.setLLValue(LLValue);
		this.object2TreeMap.put(o, tree);

		super.add(tree);
	}

	/**
	 * This method adds new PQTree to this list.
	 * @param tree PQTree object that will be added.
	 */
	public void addTree(PQTree<T> tree)
	{
		this.add(tree);

		this.putTreeObjectsInMap(tree);
	}


	/**
	 * This method replaces one object with another in correct PQTree.
	 * @param existingObject existing object that will be replaced.
	 * @param newObject new object that will be added in place of existing one.
	 */
	public void replace(T existingObject, T newObject)
	{
		LayeredNode exNode = (LayeredNode) existingObject;
		LayeredNode nNode = (LayeredNode) newObject;
		if (!exNode.isDummy())
		{
			//System.out.print("exist:" + exNode.getOriginalNode().getTag());
		}
		else
		{
			String en = "[" + ((LayeredEdge) exNode.inEdge()).getOriginalEdge().getSourceNode().getTag() +", " +
				((LayeredEdge) exNode.inEdge()).getOriginalEdge().getTargetNode().getTag() + "]";
			//System.out.print(en);
		}
		//System.out.print(" ");
		if (!nNode.isDummy())
		{
			//System.out.println("newO:" + nNode.getOriginalNode().getTag());
		}
		else
		{
			String en = "[" + ((LayeredEdge) nNode.inEdge()).getOriginalEdge().getSourceNode().getTag() +", " +
				((LayeredEdge) nNode.inEdge()).getOriginalEdge().getTargetNode().getTag() + "]";
			//System.out.println(en);
		}
		PQTree<T> tree = this.object2TreeMap.get(existingObject);
		this.object2TreeMap.put(newObject, tree);
		tree.replace(existingObject, newObject);
	}


	/**
	 * This method replaces list of nodes with single node.
	 * @param list list of objects that will be replaced.
	 * @param o object to be inserted.
	 */
	public void replaceList(List<T> list, T o)
	{
		for (int k = 0; k < this.size(); k++)
		{
			PQTree<T> tree = this.getTree(k);

			tree.setUserObject(new LinkedList<T>());
		}

		Iterator<T> iter = list.iterator();

		Set<PQTree<T>> treeSet = new HashSet<>();

		while (iter.hasNext())
		{
			T obj = iter.next();
			PQTree<T> tree = this.object2TreeMap.get(obj);

			tree.getUserObject().add(obj);

			treeSet.add(tree);
		}

		LinkedList<PQTree<T>> involvedTreeList = new LinkedList<>(treeSet);

//System.out.println("PQTreeList: Collections.sort(involvedTreeList 00");
		Collections.sort(involvedTreeList, new PQTreeLLComparator());
//System.out.println("PQTreeList: Collections.sort(involvedTreeList 01");

		myAssert(involvedTreeList.size() == 1);

		if (involvedTreeList.size() > 0)
		{
			PQTree<T> t1 = involvedTreeList.removeFirst();
			this.object2TreeMap.put(o, t1);

			t1.replaceList((LinkedList<T>)t1.getUserObject(), o);
			//this.calculateMLValues(t1);

//			while (!involvedTreeList.isEmpty())
//			{
//				PQTree t2 = (PQTree) involvedTreeList.removeFirst();
//				this.calculateMLValues(t2);
//
//				List t2ObjList = t2.getObjects();
//
//				for (Iterator it = t2ObjList.iterator(); it.hasNext();)
//				{
//					this.object2TreeMap.put(it.next(), t1);
//				}
//
//				t1.insert(t2, o);
//
//				this.remove(t2);
//			}

//			List objList = t1.getObjects();
//
//			Iterator it = objList.iterator();
//
//			while (it.hasNext())
//			{
//				this.object2TreeMap.put(it.next(), t1);
//			}
		}

		for (int k = 0; k < this.size(); k++)
		{
			PQTree tree = this.getTree(k);

			tree.clearUserObject();
		}
	}


	/**
	 * This method merges all trees that contains objects in list.
	 * @param list list of objects that have to belong to the same tree after
	 * merge operation.
	 */
	public void merge(List<T> list)
	{
		for (int k = 0; k < this.size(); k++)
		{
			PQTree<T> tree = this.getTree(k);

			tree.setUserObject(new LinkedList<T>());
		}

		Iterator<T> iter = list.iterator();

		Set<PQTree<T>> treeSet = new LinkedHashSet<>();

		while (iter.hasNext())
		{
			T obj = iter.next();
			PQTree<T> tree = this.object2TreeMap.get(obj);

			tree.getUserObject().add(obj);

			treeSet.add(tree);
		}

		LinkedList<PQTree<T>> involvedTreeList = new LinkedList<>(treeSet);

//System.out.println("PQTreeList: Collections.sort(involvedTreeList 10");
		Collections.sort(involvedTreeList, new PQTreeLLComparator());
//System.out.println("PQTreeList: Collections.sort(involvedTreeList 11");

		if (involvedTreeList.size() > 1)
		{
			PQTree<T> t1 = involvedTreeList.removeFirst();
//			this.object2TreeMap.put(o, t1);
			T o = ((LinkedList<T>) t1.getUserObject()).get(0);
			t1.addRule(t1.getUserObject());

//			t1.replaceList((List) t1.getUserObject(), o);
			this.calculateMLValues(t1);

			while (!involvedTreeList.isEmpty())
			{
				PQTree<T> t2 = involvedTreeList.removeFirst();
				t2.addRule(t2.getUserObject());
				this.calculateMLValues(t2);

				List<T> t2ObjList = t2.getObjects();

				for (Iterator<T> it = t2ObjList.iterator(); it.hasNext();)
				{
					this.object2TreeMap.put(it.next(), t1);
				}

				t1.insert(t2, o, (List<T>) t1.getUserObject());

				this.remove(t2);
			}

//			List objList = t1.getObjects();
//
//			Iterator it = objList.iterator();
//
//			while (it.hasNext())
//			{
//				this.object2TreeMap.put(it.next(), t1);
//			}
		}

		this.clearUserObjects();
	}


	/**
	 * This method clears user objects from each tree in this tree list.
	 */
	public void clearUserObjects()
	{
		for (int k = 0; k < this.size(); k++)
		{
			PQTree tree = this.getTree(k);

			tree.clearUserObject();
		}
	}


	/**
	 * This method adds new rule to one of trees in this list.
	 * @param ruleObjectSet set of objects that should be sequentially.
	 */
	public void addRule(Collection<T> ruleObjectSet)
	{
		// print added rule:

//		System.out.print("Adding Rule: ");

//		for (Object o : ruleObjectSet)
//		{
//			System.out.print(o.toString() + " ");
//		}
//		System.out.println();

		Iterator<T> it = ruleObjectSet.iterator();

		if (it.hasNext())
		{
			T o = it.next();

			PQTree<T> tree = this.object2TreeMap.get(o);

			tree.addRule(ruleObjectSet);
		}
	}


	/**
	 * This method removes all unnecessary nodes from each tree in the list.
	 * @see PQTree
	 */
	public void removeUnnecessary()
	{
		for (int k = this.size() - 1; k >= 0; k--)
		{
			this.getTree(k).removeUnnecessary();

			if (this.getTree(k).getCNodeList().isEmpty())
			{
				this.remove(k);
			}
		}
	}


	/**
	 * This method that copies all trees from another PQTreeList.
	 * @param originallist tree list from which everything is copied.
	 */
	public void copy(PQTreeList<T> originallist)
	{
		Iterator<PQTree<T>> treeIter = originallist.iterator();

		while (treeIter.hasNext())
		{
			PQTree<T> tree = treeIter.next();
			PQTree<T> newTree = new PQTree<>();
			newTree.copy(tree);

			this.putTreeObjectsInMap(newTree);

			this.add(newTree);
		}
	}

	
	/**
	 * This method creates correct mapping from objects to trees, that
	 * will be used later to correctly add rules etc.
	 * @param tree PQTree which objects are inserted into mapping.
	 */
	private void putTreeObjectsInMap(PQTree<T> tree)
	{
		Iterator<T> objectsIter = tree.getObjects().iterator();

		while (objectsIter.hasNext())
		{
			this.object2TreeMap.put(objectsIter.next(), tree);
		}
	}


	/**
	 * This method updates levl information for each tree in the list.
	 */
	public void updateLevelInfo()
	{
		for (int k = 0; k < this.size(); k++)
		{
			PQTree tree = this.getTree(k);

			tree.updateLevelInfo();
		}
	}


	/**
	 * This method sets invisible node sets for each tree.
	 * @param s set of objects that are invisible nodes in original graph.
	 */
	public void setInvisibleNodeSet(Set<T> s)
	{
		for (int k = 0; k < this.size(); k++)
		{
			PQTree<T> tree = this.getTree(k);
			tree.setUserObject(new HashSet<T>());
		}

		Iterator<T> iter = s.iterator();

		while (iter.hasNext())
		{
			T o = iter.next();
			PQTree<T> tree = this.object2TreeMap.get(o);

			tree.getUserObject().add(o);
		}

		for (int k = 0; k < this.size(); k++)
		{
			PQTree<T> tree = this.getTree(k);
			tree.setInvisibleNodeSet((Set<T>) tree.getUserObject());
			tree.clearUserObject();
		}
	}


	/**
	 * This method sets hanging node sets for each tree.
	 * @param s set of objects that are hanging nodes in original graph.
	 */
	public void setHangingNodeSet(Set<T> s)
	{
		for (int k = 0; k < this.size(); k++)
		{
			PQTree<T> tree = this.getTree(k);
			tree.setUserObject(new HashSet<T>());
		}

		Iterator<T> iter = s.iterator();

		while (iter.hasNext())
		{
			T o = iter.next();
			PQTree<T> tree = this.object2TreeMap.get(o);

			tree.getUserObject().add(o);
		}

		for (int k = 0; k < this.size(); k++)
		{
			PQTree<T> tree = this.getTree(k);
			tree.setHangingNodeSet((Set<T>) tree.getUserObject());
			tree.clearUserObject();
		}
	}


	/**
	 * This method prints all PQ-trees in this list.
	 */
	public void print()
	{
		System.out.println("------------List-------------");

		for (int k = 0; k < this.size(); k++)
		{
			System.out.println("------------" + k + "-------------");

			this.getTree(k).print();
		}
	}


	/**
	 * This method gets correct node list of all objects. Correct node order is
	 * arbitrary if sequence rules is null, otherwise such ordering that
	 * corresponds sequence rules.
	 * @param sequenceRulesList - list of node sets that means that objects in
	 * i-th set have to be no further than objects from (i+1)-th set.
	 * @return List of nodes that represents node order.
	 */
	public LinkedList<T> getList(List<? extends Set<T>> sequenceRulesList)
	{
		LinkedList<T> returnList = new LinkedList<>();

		if (sequenceRulesList == null)
		{
			// simple case when aren't any rules
			for (int k = 0; k < this.size(); k++)
			{
				PQTree<T> tree = this.getTree(k);

				returnList.addAll(tree.getList(null));
			}
		}
		else
		{
			//TODO:analyse this case;

			ArrayList<LinkedList<T>> lists = new ArrayList<>();

			for (int k = 0; k < this.size(); k++)
			{
				PQTree<T> tree = this.getTree(k);

				tree.getList(sequenceRulesList);
			}

			Collections.sort(this, new Comparator<PQTree>()
			{
                                @Override
				public int compare(PQTree tree1, PQTree tree2)
				{
					int rc = 0;

					if (tree1.root.getMinSeq() < tree2.root.getMinSeq())
					{
						rc = -1;
					}
					else if (tree1.root.getMinSeq() > tree2.root.getMinSeq())
					{
						rc = 1;
					}
					else if (tree1.root.getMinSeq() == tree2.root.getMinSeq())
					{
						if (tree1.root.getMaxSeq() < tree2.root.getMaxSeq())
						{
							rc = -1;
						}
						else if (tree1.root.getMaxSeq() > tree2.root.getMaxSeq())
						{
							rc = 1;
						}
					}

					return rc;
				}
			});

			for (int k = 0; k < this.size(); k++)
			{
				PQTree<T> tree = this.getTree(k);

				lists.add(tree.getList(null));
//				returnList.addAll(lists[k]);
			}

			returnList.addAll(lists.get(0));

			Comparator<T> placeComparator =
					new ObjectSeqComparator<>(sequenceRulesList);

			for (int k = 1; k < this.size(); k++)
			{
				Iterator<T> it = returnList.iterator();
				boolean placeFound = false;
				int index1 = -1;

				while (it.hasNext() && !placeFound)
				{
					placeFound = (1 == placeComparator.compare(it.next(), lists.get(k).getFirst()));

					index1++;
				}

				if (!placeFound)
				{
					returnList.addAll(lists.get(k));
				}
				else
				{
					returnList.addAll(index1, lists.get(k));
				}
			}
		}
		return returnList;
	}


	/**
	 * This method calculates correct ML values of given PQ-tree.
	 * @param tree PQ-tree which ML values will be calculated.
	 */
	public void calculateMLValues(PQTree<T> tree)
	{
		LinkedList<PQTreeNode<T>> queue = new LinkedList<>();
		queue.add(tree.root);

		while (!queue.isEmpty())
		{
			PQTreeNode<T> n = queue.removeFirst();

			if (n.getType() == PQTree.PNODE)
			{
				PNode<T> p = (PNode<T>) n;

				Set<T> s = tree.getChildObjects(p);

				p.setMLValue(this.calculateMLValue(s));
			}
			else if (n.getType() == PQTree.QNODE)
			{
				QNode<T> q = (QNode<T>) n;

				q.clearMLValues();

				List<PQTreeNode<T>> chList = q.getChildList();

				Set<T> prevSet;
				Set<T> curSet = null;

				Iterator<PQTreeNode<T>> it = chList.iterator();

				//TODO:check if c shouldn't be -1
				int c = 0;

				while (it.hasNext())
				{
					prevSet = curSet;
					curSet = tree.getChildObjects(it.next());

					if (prevSet != null)
					{
						Set<T> s = new HashSet<>(curSet);
						s.addAll(prevSet);

						q.setMLValue(c, this.calculateMLValue(s), true);
					}

					c++;
				}
			}

			queue.addAll(n.getChildList());

		}
	}


	/**
	 * This method calculates ML value for given set of original graph nodes.
	 * @param s set of nodes for whom ML (meet level) is calculated.
	 * @return index of meet level.
	 */
	public int calculateMLValue(Set s)
	{
		int rc;

		lv.lumii.layoutengine.flowLayout.horizontalOrdering.priorityqueue.PriorityQueue<LayeredNode> queue =
			new lv.lumii.layoutengine.flowLayout.horizontalOrdering.priorityqueue.PriorityQueue<>(s.size(), new LayeredNodeLevelComparator());

		LayeredNode o =(LayeredNode) s.toArray()[0];

		rc = o.getLevelNumber();

		Set<LayeredNode>  visiteNodeSet= new HashSet<>();
		visiteNodeSet.add(o);

		int minLevel = rc;

		queue.add(o);
		s.remove(o);

		while (!s.isEmpty() && !queue.isEmpty())
		{
			LayeredNode node = (LayeredNode) queue.removeMin();

			rc = Math.min(rc, node.getLevelNumber());
			s.remove(node);

			Iterator it = node.inOutEdgeIterator();

			while (it.hasNext())
			{
				LayeredEdge edge = (LayeredEdge) it.next();

				LayeredNode n = (LayeredNode) edge.getOtherNode(node);

				if (n.getLevelNumber() <= minLevel &&
					!visiteNodeSet.contains(n))
				{
					visiteNodeSet.add(n);
					queue.add(n);
				}
			}
		}

		if (!s.isEmpty())
		{
			rc = 0;
		}

		return rc;
	}

	/**
	 * This method sets layered graph.
	 * @param graph reference to new layered graph.
	 */
	public void setLayeredGraph(LayeredGraph graph)
	{
		this.graph = graph;
	}


	/**
	 * This method gets layered graph.
	 * @return reference of layered graph.
	 */
	public LayeredGraph getLayeredGraph()
	{
		return this.graph;
	}


	/**
	 * This method removes unnecessary nodes from all graphs.
	 * @param c collection of unnecessary nodes.
	 */
	public void removeNodes(Collection<T> c)
	{
		for (int k = 0; k < this.size(); k++)
		{
			PQTree<T> tree = this.getTree(k);
			tree.setUserObject(new LinkedList<T>());
		}

		Iterator<T> iter = c.iterator();

		while (iter.hasNext())
		{
			T o = iter.next();
			PQTree<T> t = this.object2TreeMap.get(o);

			this.object2TreeMap.remove(o);
			t.getUserObject().add(o);
		}

		for (int k = 0; k < this.size(); k++)
		{
			PQTree<T> tree = this.getTree(k);
			tree.remove(tree.getUserObject());
			tree.clearUserObject();
		}
	}


	/**
	 * This method removes all trees that doesn't contain any CNode.
	 */
	public void removeEmptyTrees()
	{
		Set<PQTree> removableTreeSet = new HashSet<>();

		for (int k = 0; k < this.size(); k++)
		{
			if (this.getTree(k).getCNodeList().isEmpty())
			{
				removableTreeSet.add(this.getTree(k));
			}
		}

		this.removeAll(removableTreeSet);
	}

	/**
	 * This method checks consistency of information.
	 *
	 * @return false if there exists object x that is in tree, but it isn't in
	 * object to tree mapping.
	 */
	public boolean checkValidity()
	{
		boolean rc = true;

		for (int k = 0; k < this.size(); k++)
		{
			PQTree tree = this.getTree(k);

			List objList = tree.getObjects();

			rc &= (this.object2TreeMap.keySet().containsAll(objList));
		}
		
		return rc;
	}
	
	private void myAssert(boolean check)
	{
		if (!check)
		{
			throw new RuntimeException("Check failed!");
		}
	}

	/**
	 * This private attribute contains mapping between objects and PQ-trees in this list.
	 */
	private final Map<T, PQTree<T>> object2TreeMap;

	/**
	 * This private attribute contains reference to original layered graph. It is used to determine ML values.
	 */
	private LayeredGraph graph;
}
