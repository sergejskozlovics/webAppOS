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

package lv.lumii.layoutengine.springembedder;



import java.util.LinkedList;


/**
 * A data structure for repulsive force calculation for
 * particle systems.
 */
final class RepulsiveKFTree
{

	/**
	 * Builds the tree from the given array of springNodes.
	 */
	public void buildTree(SpringNode nodeArray[], int count)
	{
		if (this.root != null)
		{
			this.freeNode(this.root);
		}

		this.root = this.newNode();

		if (count > 0)
		{
			this.buildTreeRecursive(nodeArray, 0, count, this.root);
		}

		// calculate the node forces

		for (int i = 0;i < count;i++)
		{
			nodeArray[i].repulsiveForceX = 0;
			nodeArray[i].repulsiveForceY = 0;
			nodeArray[i].repulsiveForceZ = 0;
		}

		this.calculateForces();
	}


	/**
	 * Stores the nodes for reuse.
	 */
	private void freeNode(RepulsiveTreeNode node)
	{
		if (node.left != null)
		{
			this.freeNode(node.left);
		}

		if (node.right != null)
		{
			this.freeNode(node.right);
		}

		node.left = this.freeNodeRoot;
		this.freeNodeRoot = node;
	}


	/**
	 * Allocates a new node. It reuses any free node if it exists.
	 */
	private RepulsiveTreeNode newNode()
	{
		RepulsiveTreeNode node;

		if (this.freeNodeRoot != null)
		{
			node = this.freeNodeRoot;
			this.freeNodeRoot = node.left;

			node.left = null;
			node.right = null;
			node.leafCount = 0;
			node.sizeEstimate = 0;
		}
		else
		{
			node = new RepulsiveTreeNode();
			node.leaves = new SpringNode[this.maxLeafCount];
		}

		node.repulsiveForceX = 0;
		node.repulsiveForceY = 0;
		node.repulsiveForceZ = 0;
		node.forceLinkList.clear();

		return node;
	}


	/**
	 * Swaps two elements in an array.
	 */
	private static void swap(final BaseSpringNode arr[],
		final int a,
		final int b)
	{
		BaseSpringNode p = arr[a];
		arr[a] = arr[b];
		arr[b] = p;
	}

	
	/**
	 * Adds and edge of possible force interaction between the
	 * specified nodes.
	 */
	private void addForceLink(RepulsiveTreeNode source, BaseSpringNode target)
	{
		source.forceLinkList.add(target);
	}


	/**
	 * Calculates the median index of the point array x
	 * coordinates in the specified range.
	 * In addition it sorts the array range such that all elemnts with smaller
	 * x than the median are below median element, and all elemnts with larger
	 * x are above the median.
	 */
	private int getMedianX(BaseSpringNode arr[], int low, int high)
	{
		high--;

		int median;
		int middle;
		int ll;
		int hh;

		median = (low + high) / 2;

		for (;;)
		{
			if (high <= low)
			{
				// One element only

				return median;
			}

			if (high == low + 1)
			{
				// Two elements only

				if (arr[low].x > arr[high].x)
				{
					swap(arr, low, high);
				}

				return median;
			}

			// Find median of low, middle and high items; swap into position
			// low

			middle = (low + high) / 2;

			if (arr[middle].x > arr[high].x)
			{
				swap(arr, middle, high);
			}

			if (arr[low].x > arr[high].x)
			{
				swap(arr, low, high);
			}

			if (arr[middle].x > arr[low].x)
			{
				swap(arr, middle, low);
			}

			// Swap low item (now in position middle) into position (low+1)
			swap(arr, middle, low + 1);

			// Nibble from each end towards middle, swapping items when stuck
			ll = low + 1;
			hh = high;

			for (;;)
			{
				do
				{
					ll++;
				}
				while (arr[low].x > arr[ll].x);

				do
				{
					hh--;
				}
				while (arr[hh].x > arr[low].x);

				if (hh < ll)
				{
					break;
				}

				//swap(arr, ll, hh);
				BaseSpringNode tmp = arr[ll];
				arr[ll] = arr[hh];
				arr[hh] = tmp;
			}

			// Swap middle item (in position low) back into correct position
			swap(arr, low, hh);

			// Re-set active partition

			if (hh <= median)
			{
				low = ll;
			}

			if (hh >= median)
			{
				high = hh - 1;
			}
		}
	}


	/**
	 * Calculates the median index of the point array y
	 * coordinates in the specified range.
	 * In addition it sorts the array range such that all elemnts with smaller
	 * y than the median are below median element, and all elemnts with larger
	 * y are above the median.
	 */
	private int getMedianY(BaseSpringNode arr[], int low, int high)
	{
		high--;

		int median;
		int middle;
		int ll;
		int hh;

		median = (low + high) / 2;

		for (;;)
		{
			if (high <= low)
			{
				// One element only

				return median;
			}

			if (high == low + 1)
			{
				// Two elements only

				if (arr[low].y > arr[high].y)
				{
					swap(arr, low, high);
				}

				return median;
			}

			// Find median of low, middle and high items; swap into position
			// low

			middle = (low + high) / 2;

			if (arr[middle].y > arr[high].y)
			{
				swap(arr, middle, high);
			}

			if (arr[low].y > arr[high].y)
			{
				swap(arr, low, high);
			}

			if (arr[middle].y > arr[low].y)
			{
				swap(arr, middle, low);
			}

			// Swap low item (now in position middle) into position (low+1)
			swap(arr, middle, low + 1);

			// Nibble from each end towards middle, swapping items when stuck
			ll = low + 1;
			hh = high;

			for (;;)
			{
				do
				{
					ll++;
				}
				while (arr[low].y > arr[ll].y);

				do
				{
					hh--;
				}
				while (arr[hh].y > arr[low].y);

				if (hh < ll)
				{
					break;
				}

				//swap(arr, ll, hh);

				BaseSpringNode tmp = arr[ll];
				arr[ll] = arr[hh];
				arr[hh] = tmp;
			}

			// Swap middle item (in position low) back into correct position
			swap(arr, low, hh);

			// Re-set active partition

			if (hh <= median)
			{
				low = ll;
			}

			if (hh >= median)
			{
				high = hh - 1;
			}
		}
	}


	/**
	 * Rrecursive tree building.
	 * Note that hi endpoint is not included.
	 * The order of points in the array is changed.
	 */
	private void buildTreeRecursive(SpringNode nodeArray[],
		int start,
		int end,
		RepulsiveTreeNode subtree)
	{
		// calculate current bounds
		double x1 = nodeArray[start].x;
		double x2 = x1;
		double y1 = nodeArray[start].y;
		double y2 = y1;

		for (int i = start + 1;i < end;i++)
		{
			BaseSpringNode p = nodeArray[i];
			double x = p.x;
			double y = p.y;

			if (x < x1)
			{
				x1 = x;
			}

			if (x > x2)
			{
				x2 = x;
			}

			if (y < y1)
			{
				y1 = y;
			}

			if (y > y2)
			{
				y2 = y;
			}
		}

		subtree.sizeEstimate = Math.max(x2 - x1 , y2 - y1);
		subtree.sizeEstimate =
			subtree.sizeEstimate * subtree.sizeEstimate + 0.1;
		subtree.sizeEstimate *= 2;

		int count = end - start;

		// form a leaf if too few points

		if (count <= this.maxLeafCount)
		{
			subtree.leafCount = count;
			double x = 0;
			double y = 0;
			double z = 0;
			double r = 0;

			for (int i = 0;i < count;i++)
			{
				SpringNode node = nodeArray[i + start];
				x += node.x;
				y += node.y;
				z += node.z;
				r += node.r;
				subtree.leaves[i] = node;
			}

			double invcnt = 1.0 / count;
			subtree.r = r;
			subtree.x = x * invcnt;
			subtree.y = y * invcnt;
			subtree.z = z * invcnt;

			return;
		}

		//split

		int median;

		if (x2 - x1 > y2 - y1)
		{
			median = this.getMedianX(nodeArray, start, end);
		}
		else
		{
			median = this.getMedianY(nodeArray, start, end);
		}

		median++;

		RepulsiveTreeNode child = this.newNode();
		subtree.left = child;
		this.buildTreeRecursive(nodeArray, start, median, child);

		child = this.newNode();
		subtree.right = child;
		this.buildTreeRecursive(nodeArray, median, end, child);

		double invcnt = 1.0 / count;
		subtree.r = subtree.right.r + subtree.left.r;
		subtree.x = (subtree.left.x * (median - start) +
			subtree.right.x * (end - median)) * invcnt;
		subtree.y = (subtree.left.y * (median - start) +
			subtree.right.y * (end - median)) * invcnt;
		subtree.z = (subtree.left.z * (median - start) +
			subtree.right.z * (end - median)) * invcnt;
	}


	/**
	 * Calculates the repulsive forces among all the particles.
	 */
	private void calculateForces()
	{
		LinkedList<RepulsiveTreeNode> queue = new LinkedList<>();

		if (this.root.left != null)
		{
			queue.add(this.root.left);
			queue.add(this.root.right);
			this.addForceLink(this.root.left, this.root.right);
		}
		else
		{
			queue.add(this.root);
		}

		while (!queue.isEmpty())
		{
			RepulsiveTreeNode node = queue.removeFirst();
			double invr = 1.0 / node.r;

			if (node.left != null)
			{
				this.processTreeNode(node, node.left);
				this.processTreeNode(node, node.right);
				queue.add(node.left);
				queue.add(node.right);

				double factor = node.left.r * invr;
				double forceX = node.repulsiveForceX * factor;
				double forceY = node.repulsiveForceY * factor;

				node.left.repulsiveForceX += forceX;
				node.left.repulsiveForceY += forceY;
				node.right.repulsiveForceX += node.repulsiveForceX - forceX;
				node.right.repulsiveForceY += node.repulsiveForceY - forceY;

				this.addForceLink(node.left, node.right);
			}
			else
			{
				for (int i = 0;i < node.leafCount;i++)
				{
					SpringNode node1 = (SpringNode) node.leaves[i];
					this.processLeafNode(node, node1);

					double factor = node1.r * invr;
					node1.repulsiveForceX += node.repulsiveForceX * factor;
					node1.repulsiveForceY += node.repulsiveForceY * factor;

					for (int k = i + 1;k < node.leafCount;k++)
					{
						SpringNode node2 = (SpringNode) node.leaves[k];
						this.forceBetweenParticles(node1, node2);
					}

				}
			}

			// cleanup
			node.forceLinkList.clear();
		}
	}


	/**
	 * Calculates the force on a given subdivision.
	 * @param node the root of the subtree
	 * @param subtree the branch for which force is to be calculated
	 */
	private void processTreeNode(RepulsiveTreeNode node,
		RepulsiveTreeNode subtree)
	{
		int length = node.forceLinkList.size();

		// iteration by index is faster

		for (int i = 0;i < length;i++)
		{
			BaseSpringNode otherNode = node.forceLinkList.get(i);

			double vx = subtree.x - otherNode.x;
			double vy = subtree.y - otherNode.y;
			double len = vx * vx + vy * vy;
			double threshold = subtree.sizeEstimate;

			if(otherNode instanceof RepulsiveTreeNode)
			{
				threshold+= ((RepulsiveTreeNode)otherNode).sizeEstimate;
			}

			if (threshold < len)
			{
				double w = subtree.r * otherNode.r /
					(len * Math.sqrt(len));
				double fx = vx * w;

				subtree.repulsiveForceX += fx;
				otherNode.repulsiveForceX -= fx;
				double fy = vy * w;
				subtree.repulsiveForceY += fy;
				otherNode.repulsiveForceY -= fy;
			}
			else
			{
				if (otherNode instanceof SpringNode)
					this.addForceLink(subtree, otherNode);
				else
					this.addForceLink((RepulsiveTreeNode) otherNode, subtree);
			}
		}
	}


	/**
	 * Calculates the force on a given leaf node.
	 * @param node the root of the subtree
	 * @param subtree the branch for which force is to be calculated
	 */
	private void processLeafNode(RepulsiveTreeNode node,
		SpringNode subtree)
	{
		int length = node.forceLinkList.size();

		// iteration by index is faster

		for (int i = 0;i < length;i++)
		{
			BaseSpringNode otherNode = node.forceLinkList.get(i);

			if (otherNode instanceof SpringNode)
			{
				this.forceBetweenParticles((SpringNode) subtree,
					(SpringNode) otherNode);
			}
			else
			{
				double vx = subtree.x - otherNode.x;
				double vy = subtree.y - otherNode.y;
				double len = vx * vx + vy * vy;
				double threshold = ((RepulsiveTreeNode) otherNode).
					sizeEstimate;

				if (threshold < len)
				{
					double w = subtree.r * otherNode.r /
						(len * Math.sqrt(len));
					double fx = vx * w;

					subtree.repulsiveForceX += fx;
					otherNode.repulsiveForceX -= fx;
					double fy = vy * w;
					subtree.repulsiveForceY += fy;
					otherNode.repulsiveForceY -= fy;
				}
				else
				{
					this.addForceLink((RepulsiveTreeNode) otherNode, subtree);
				}
			}
		}
	}


	/**
	 * Calculates the force between two spring nodes.
	 */
	private void forceBetweenParticles(SpringNode node1, SpringNode node2)
	{
		double vx1 = node1.x - node2.x;
		double vy1 = node1.y - node2.y;
		double vz1 = node1.z - node2.z;
		double len1 = vx1 * vx1 + vy1 * vy1 + vz1 * vz1;
		double r = node1.r + node2.r;

		double w;

		if (len1 * this.temperature < r)
		{
			w = 0.25 * this.temperature *
				Math.sqrt(this.temperature * r);
		}
		else
		{
			w = 0.25 * r * r / (len1 * Math.sqrt(len1));
		}

		node1.repulsiveForceX += vx1 * w;
		node2.repulsiveForceX -= vx1 * w;
		node1.repulsiveForceY += vy1 * w;
		node2.repulsiveForceY -= vy1 * w;
		node1.repulsiveForceZ += vz1 * w;
		node2.repulsiveForceZ -= vz1 * w;
	}


// --------------------------------------------------------------------
// Section: instance variables
// --------------------------------------------------------------------

	// root of the tree
	protected RepulsiveTreeNode root;

	// maximum number of points in a leaf
	protected final int maxLeafCount = 5;

	// Spring embedder temperature
	double temperature;

	// the first emty node for reuse
	private RepulsiveTreeNode freeNodeRoot;
}
