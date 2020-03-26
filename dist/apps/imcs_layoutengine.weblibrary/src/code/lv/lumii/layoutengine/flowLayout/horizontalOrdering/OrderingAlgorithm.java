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

package lv.lumii.layoutengine.flowLayout.horizontalOrdering;

import java.util.*;
import java.io.PrintStream;
import java.io.File;

import lv.lumii.layoutengine.flowLayout.horizontalOrdering.pqtree.PQTree;
import lv.lumii.layoutengine.flowLayout.horizontalOrdering.pqtree.PQTreeList;


/**
 * This class implements the main control structures of the horizontal
 * ordering algorithm of a layered graph that is needed to provide
 * the horizontal ordering in the Flow Layout algorithm.
 * The algorithm runs in two stages:
 * (1) building node PQ-trees processing levels in top to bottom order,
 * (2) determining node ordering within levels in level bottom to top order
 * according PQ-tree data.
 *
 * @author Rudolfs
 */
public class OrderingAlgorithm
{
	public OrderingAlgorithm()
	{
		this.input = null;
	}


	/**
	 * This method sets the input object of this algorithm.
	 *
	 * @param input the input object.
	 */
	public void setInput(OrderingInput input)
	{
		this.input = input;
	}


	/**
	 * This method runs the ordering algorithm.
	 */
	public void run()
	{
		//this.input = (TSOrderingInput) this.getInput();
		this.layeredGraph = this.input.getLayeredGraph();

//		System.out.println("pirms");
//
//		for (int l = 0; l < this.layeredGraph.getNumberOfLevels(); l++)
//		{
//			ArrayList levelNodes = layeredGraph.getLevelNodeList(l);
//			for (int k = 0; k < levelNodes.size(); k++)
//			{
//				LayeredNode node = (LayeredNode) levelNodes.get(k);
////				System.out.println(k + " " + node.getLevelNumber() + " " + node);
//				System.out.print(node.getOriginalNode().getTag() + " ");
//
////				for (Iterator iter = node.inOutEdgeIterator(); iter.hasNext();)
////				{
////					LayeredEdge edge = (LayeredEdge) iter.next();
////					System.out.println("edge " + edge);
////
////					LayeredNode otherNode = edge.getOtherNode(node);
////					System.out.println("otherNode " + otherNode);
////				}
//			}
//			System.out.println("");
//		}

		int numberOfLevels = this.layeredGraph.getNumberOfLevels();
		ArrayList<PQTreeList<LayeredNode>> pqTreeArray = new ArrayList<>();

		for (int i = 0; i < numberOfLevels; i++)
		{
			pqTreeArray.add(new PQTreeList<LayeredNode>());

			ArrayList<LayeredNode> levelNodes = this.layeredGraph.getLevelNodeList(i);

			if (i == 0)
			{
				PQTree<LayeredNode> tree = new PQTree<>(levelNodes);
				tree.setLLValue(i);
				pqTreeArray.get(i).addTree(tree);
				myAssert(pqTreeArray.get(i).checkValidity());
			}

			if (i > 0)
			{
				pqTreeArray.get(i).copy(pqTreeArray.get(i - 1));
				myAssert(pqTreeArray.get(i).checkValidity());

				for (int singleReplace = 1; singleReplace < 3; singleReplace++)
				{
					for (int j = 0; j < levelNodes.size(); j++)
					{
						LayeredNode lNode = (LayeredNode) levelNodes.get(j);

						LinkedList<LayeredNode> replaceList = new LinkedList<>();

						for (Iterator it = lNode.inOutEdgeIterator(); it.hasNext();)
						{
							LayeredEdge edge = (LayeredEdge) it.next();
							LayeredNode otherNode =
								(LayeredNode) edge.getOtherNode(lNode);

							if (otherNode.getLevelNumber() == i - 1)
							{
								//pqTreeArray[i].replace(otherNode, lNode);
								replaceList.add(otherNode);
							}
						}

						if (singleReplace == 0)
						{
							pqTreeArray.get(i).merge(replaceList);
						}
						else if (singleReplace == 1 && replaceList.size() == 1)
						{
							myAssert(pqTreeArray.get(i).checkValidity());
							pqTreeArray.get(i).replace(replaceList.getFirst(), lNode);
							myAssert(pqTreeArray.get(i).checkValidity());
						}
						else if (singleReplace == 2 && replaceList.size() > 1)
						{
							pqTreeArray.get(i).replaceList(replaceList, lNode);
							myAssert(pqTreeArray.get(i).checkValidity());
						}
						else if (replaceList.size() == 0 && singleReplace == 2)
						{
							// now we process *hanging* vertex
							//pqTreeArray[i].addElement(lNode);

							pqTreeArray.get(i).addTree(lNode, i);
							myAssert(pqTreeArray.get(i).checkValidity());

//							pqTreeArray[i].add(
//									new PQTree(Collections.singletonList(lNode)));
						}
					}

//					System.out.println("To Level " + i);
//					pqTreeArray[i].print();

					pqTreeArray.get(i).removeUnnecessary();
					myAssert(pqTreeArray.get(i).checkValidity());

//					System.out.println("Afft rem. unnecessary " + i);
//					pqTreeArray[i].print();

					if (singleReplace == 0)
					{
						//System.out.println("Peec merge!");
						//pqTreeArray[i].print();
					}
				}

				pqTreeArray.get(i).removeNodes(
						this.layeredGraph.getLevelNodeList(i - 1));
				pqTreeArray.get(i).removeEmptyTrees();
				myAssert(pqTreeArray.get(i).checkValidity());

//				System.out.println("Afft level move " + i);
//				pqTreeArray[i].print();
			}

			//System.out.println("After replace and remove");
			//pqTreeArray[i].print();
			myAssert(pqTreeArray.get(i).checkValidity());

			if (i + 1 < numberOfLevels)
			{
				ArrayList nextLevelNodes =
					(ArrayList) this.layeredGraph.getLevelNodeList(i + 1);

//				System.out.println("</Merge>");

//				pqTreeArray[i].print();

				for (int j = 0; j < nextLevelNodes.size(); j++)
				{
					LayeredNode lNode = (LayeredNode) nextLevelNodes.get(j);

					LinkedList<LayeredNode> replaceList = new LinkedList<>();

					for (Iterator it = lNode.inOutEdgeIterator(); it.hasNext();)
					{
						LayeredEdge edge = (LayeredEdge) it.next();
						LayeredNode otherNode =
							(LayeredNode) edge.getOtherNode(lNode);

						if (otherNode.getLevelNumber() == i)
						{
							//pqTreeArray[i].replace(otherNode, lNode);
							replaceList.add(otherNode);
						}
					}
					pqTreeArray.get(i).merge(replaceList);

					pqTreeArray.get(i).removeUnnecessary();
					myAssert(pqTreeArray.get(i).checkValidity());
				}

//				System.out.println("</Merge>");
			}

			//System.out.println("Peec next merge.");
			//pqTreeArray[i].print();

			if (i + 1 < numberOfLevels)
			{
				// there is next level

				HashMap<LayeredNode, Integer> lNodeToIntMap = new HashMap<>();

				ArrayList nextLevelNodes =
					(ArrayList) this.layeredGraph.getLevelNodeList(i + 1);

				ArrayList<LinkedHashSet<LayeredNode>> rules = new ArrayList<>();

				for (int j = 0; j < nextLevelNodes.size(); j++)
				{
					LayeredNode lNode = (LayeredNode) nextLevelNodes.get(j);

					lNodeToIntMap.put(lNode, new Integer(j));

					rules.add(new LinkedHashSet<LayeredNode>());
				}

				HashSet<LayeredNode> invisibleNodeSet = new HashSet<>();

				for (int k = 0; k < levelNodes.size(); k++)
				{
					LayeredNode lNode = (LayeredNode) levelNodes.get(k);

					// invisible nodes are those whose outgoing degree is 0
					boolean isInvisibleNode = true;

					for (Iterator iter = lNode.inOutEdgeIterator(); iter.hasNext();)
					{
						LayeredEdge edge = (LayeredEdge) iter.next();

						LayeredNode neiNode =
							(LayeredNode) edge.getOtherNode((LayeredNode) lNode);

						if (neiNode.getLevelNumber() == i + 1)
						{
							isInvisibleNode = false;
							rules.get(lNodeToIntMap.get(neiNode)).add(lNode);
						}
					}

					if (isInvisibleNode)
					{
						invisibleNodeSet.add(lNode);
					}
				}

				pqTreeArray.get(i).setInvisibleNodeSet(invisibleNodeSet);

				for (int q = 0; q < nextLevelNodes.size(); q++)
				{
					if (rules.get(q).size() > 1)
					{
//						System.out.println("<add Rule>");
//						pqTreeArray[i].print();
						pqTreeArray.get(i).addRule(rules.get(q));
						//System.out.println("pqtree after new rule" + i);
						//pqTreeArray[i].print();
						myAssert(pqTreeArray.get(i).checkValidity());
//						pqTreeArray[i].print();
//						System.out.println("</add Rule>");
					}
				}

				// now all rules are updated and needs to get information from
				// pqtree;
			}

			myAssert(pqTreeArray.get(i).checkValidity());

			if (i > 0)
			{
				HashMap<LayeredNode,Integer> lNodeToIntMap = new HashMap<>();

				ArrayList prevLevelNodes =
					(ArrayList) this.layeredGraph.getLevelNodeList(i - 1);

				ArrayList<LinkedHashSet<LayeredNode>> rules = new ArrayList<>();

				for (int j = 0; j < prevLevelNodes.size(); j++)
				{
					LayeredNode lNode = (LayeredNode) prevLevelNodes.get(j);

					lNodeToIntMap.put(lNode, new Integer(j));

					rules.add(new LinkedHashSet<LayeredNode>());
				}

				HashSet<LayeredNode> invisibleNodeSet = new HashSet<>();

				for (int k = 0; k < levelNodes.size(); k++)
				{
					LayeredNode lNode = (LayeredNode) levelNodes.get(k);

					// invisible nodes are those whose outgoing degree is 0
					boolean isInvisibleNode = true;

					for (Iterator iter = lNode.inOutEdgeIterator();
						iter.hasNext();)
					{
						LayeredEdge edge = (LayeredEdge) iter.next();
						LayeredNode neiNode =
							(LayeredNode) edge.getOtherNode(lNode);

						if (neiNode.getLevelNumber() == i - 1)
						{
							isInvisibleNode = false;
							rules.get(lNodeToIntMap.get(neiNode)).add(lNode);
						}
					}

					if (isInvisibleNode)
					{
						invisibleNodeSet.add(lNode);
					}
				}

				pqTreeArray.get(i).setInvisibleNodeSet(invisibleNodeSet);
				pqTreeArray.get(i).setHangingNodeSet(invisibleNodeSet);
				myAssert(pqTreeArray.get(i).checkValidity());

				for (int q = 0; q < prevLevelNodes.size(); q++)
				{
					if (rules.get(q).size() > 1)
					{
//						System.out.println("<add Rule>");
//						pqTreeArray[i].print();
						pqTreeArray.get(i).addRule(rules.get(q));
						//System.out.println("pqtree after new rule" + i);
						//pqTreeArray[i].print();
						myAssert(pqTreeArray.get(i).checkValidity());
//						pqTreeArray[i].print();
//						System.out.println("</add Rule>");
					}
				}
			}

			myAssert(pqTreeArray.get(i).checkValidity());

//			pqTreeArray[i].print();
		}

//		 for (int q =0; q < pqTreeArray.length; q++)
//		  {
//		   pqTreeArray[q].print();
//		  }
		// now we have to merge all pqTrees

		ArrayList<LayeredNode> lastLevelNodeList = this.layeredGraph.getLevelNodeList(numberOfLevels - 1);

		// TODO: nesaistiitaas nodes vajag apstraadaat atseviski;

		LinkedList<LayeredNode> pqOrderList = pqTreeArray.get(numberOfLevels - 1).getList(null);
		
		lastLevelNodeList.clear();
		lastLevelNodeList.addAll(pqOrderList);
		
		for (int i2 = numberOfLevels - 2; i2 >= 0; i2--)
		{
			ArrayList nextLevelNodeList =
				(ArrayList) this.layeredGraph.getLevelNodeList(i2 + 1);

			HashSet<LayeredNode> usedNodesSet = new HashSet<>();

			LinkedList<LayeredNode> thisLevelLList;// = new LinkedList();
			ArrayList<LayeredNode> thisLevelAList = this.layeredGraph.getLevelNodeList(i2);
			//System.out.print("Before " + i + " - " + thisLevelAList.size());
			thisLevelAList.clear();

			LinkedList<HashSet<LayeredNode>> rulesList = new LinkedList<>();

			for (Iterator it = nextLevelNodeList.iterator(); it.hasNext();)
			{
				LayeredNode lNode = (LayeredNode) it.next();

				HashSet<LayeredNode> neigSet = new HashSet<>();
//				System.out.println(lNode);

				for (Iterator neiIt = lNode.inOutEdgeIterator(); neiIt.hasNext();)
				{
					LayeredEdge edge = (LayeredEdge) neiIt.next();
					LayeredNode olNode = (LayeredNode) edge.getOtherNode(lNode);

					if (olNode.getLevelNumber() == i2)
					{
						neigSet.add(olNode);
//						System.out.println("\t" + olNode);
					}

//					if (olNode.getLevelNumber() == i &&
//						!usedNodesSet.contains(olNode))
//					{
//						thisLevelLList.addLast(olNode);
//						usedNodesSet.add(olNode);
//					}
				}

				rulesList.addLast(neigSet);
			}

			//pqTreeArray[i].normalize();
			pqTreeArray.get(i2).removeUnnecessary();
			pqTreeArray.get(i2).updateLevelInfo();
			thisLevelLList = pqTreeArray.get(i2).getList(rulesList);
			thisLevelAList.addAll(thisLevelLList);
			//System.out.println(" after: " + thisLevelAList.size());
		}
		
		for (int j = 0; j < 0; j++)
		{
		for (int i3 = 2; i3 < numberOfLevels; i3++)
		{
			ArrayList nextLevelNodeList =
				(ArrayList) this.layeredGraph.getLevelNodeList(i3 - 1);

			HashSet<LayeredNode> usedNodesSet = new HashSet<>();

			LinkedList<LayeredNode> thisLevelLList;// = new LinkedList();
			ArrayList<LayeredNode> thisLevelAList = this.layeredGraph.getLevelNodeList(i3);
			//System.out.print("Before " + i + " - " + thisLevelAList.size());
			thisLevelAList.clear();

			LinkedList<HashSet<LayeredNode>> rulesList = new LinkedList<>();

			for (Iterator it = nextLevelNodeList.iterator(); it.hasNext();)
			{
				LayeredNode lNode = (LayeredNode) it.next();

				HashSet<LayeredNode> neigSet = new HashSet<>();

				for (Iterator neiIt = lNode.inOutEdgeIterator(); neiIt.hasNext();)
				{
					LayeredEdge edge = (LayeredEdge) neiIt.next();
					LayeredNode olNode = (LayeredNode) edge.getOtherNode(lNode);

					if (olNode.getLevelNumber() == i3)
					{
						neigSet.add(olNode);
					}

//					if (olNode.getLevelNumber() == i &&
//						!usedNodesSet.contains(olNode))
//					{
//						thisLevelLList.addLast(olNode);
//						usedNodesSet.add(olNode);
//					}
				}

				rulesList.addLast(neigSet);
			}

			//pqTreeArray[i].normalize();
			pqTreeArray.get(i3).removeUnnecessary();
			pqTreeArray.get(i3).updateLevelInfo();
			thisLevelLList = pqTreeArray.get(i3).getList(rulesList);
			thisLevelAList.addAll(thisLevelLList);
			//System.out.println(" after: " + thisLevelAList.size());
		}

		for (int i2 = numberOfLevels - 2; i2 >= 0; i2--)
		{
			ArrayList nextLevelNodeList =
				(ArrayList) this.layeredGraph.getLevelNodeList(i2 + 1);

			HashSet<LayeredNode> usedNodesSet = new HashSet<>();

			LinkedList<LayeredNode> thisLevelLList;// = new LinkedList();
			ArrayList<LayeredNode> thisLevelAList = this.layeredGraph.getLevelNodeList(i2);
			thisLevelAList.clear();

			LinkedList<HashSet<LayeredNode>> rulesList = new LinkedList<>();

			for (Iterator it = nextLevelNodeList.iterator(); it.hasNext();)
			{
				LayeredNode lNode = (LayeredNode) it.next();

				HashSet<LayeredNode> neigSet = new HashSet<>();

				for (Iterator neiIt = lNode.inOutEdgeIterator(); neiIt.hasNext();)
				{
					LayeredEdge edge = (LayeredEdge) neiIt.next();
					LayeredNode olNode = (LayeredNode) edge.getOtherNode(lNode);

					if (olNode.getLevelNumber() == i2)
					{
						neigSet.add(olNode);
					}

//					if (olNode.getLevelNumber() == i &&
//						!usedNodesSet.contains(olNode))
//					{
//						thisLevelLList.addLast(olNode);
//						usedNodesSet.add(olNode);
//					}
				}

				rulesList.addLast(neigSet);
			}

			//pqTreeArray[i].normalize();
			pqTreeArray.get(i2).removeUnnecessary();
			pqTreeArray.get(i2).updateLevelInfo();
			thisLevelLList = pqTreeArray.get(i2).getList(rulesList);
			thisLevelAList.addAll(thisLevelLList);
		}
		}
	}
	
	private void myAssert(boolean check)
	{
		if (!check)
		{
			throw new RuntimeException("Check failed!");
		}
	}
	
	private OrderingInput input;
	private LayeredGraph layeredGraph;
}
