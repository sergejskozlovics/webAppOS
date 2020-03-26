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

package lv.lumii.layoutengine.funcmin;

import java.util.*;

import lv.lumii.dialoglayout.IMCSDialogLayout;
import lv.lumii.layoutengine.funcmin.OneWayLinkedList.TemporaryIterator;

/*
DifferenceConstraints.h
(see DifferenceConstraints.cpp for implementation)
A class for solving the system of difference constraints.
There are n variables x[0], x[1], ... , x[n] and
m constraints in the form of
x[i] - x[j] >= minValue.
For each constraint the desiredValue>=minValue is also given.
First, we will try to find the solution that satisfies the
constraints
x[i] - x[j] >= desiredValue
as far as it is possible. If the system of such
constraints is unsatisfiable, we will try to solve the
system of constraints
x[i] - x[j] >= minValue
taking into consideration desiredValues.

Author: Sergejs Kozlovics (in LaTeX: Sergejs Kozlovi\v{c}s)

Last modified: 31.08.2006. 16:15.
 */
public final class CycleReducer {

	boolean FINE_SOLUTION=false;
	boolean WHOLE_NUMBERS=false;
    double EPSILON;
//  double INFINITY; 
    int n;
    // the number of variables
    //DifferenceConstraints(int n);
    // n - the number of variables; we denote them as
    // x[0], x[1], ... , x[n-1]
    // ~DifferenceConstraints();
    // void addConstraint(
    //  int i, int j, double minValue, double desiredValue);
    // add constraint
    //   x[i] - x[j] >= minValue
    // with indication to find a solution that satisfies
    //   x[i] - x[j] >= desiredValue,
    // where desiredValue>=minValue
    // const double* getSolution();
    // Returns a pointer to an array with n elements
    // containing the values of x[0], x[1], ... , x[n-1]
    // or NULL, if the system of difference constraints
    // is unsatisfiable.
    // It is possible to add other contraints and call
    // getSolution() again. Then we use a previous solution
    // as a start point to solve a system. That may speed
    // the process of solving the system.
    // If new system is unsatisfiable, then the system
    // is returned (rolled back) to the previous state
    // when all the conditions were satisfied
    // (i.e. after previous getSolution() call, if getSolution()
    // already had been called; otherwise a system becomes empty).
    // void clearSolution();
    // Clears a solution, so the next call to getSolution()
    // will lead to computation of the solution from zero
    // (i.e., not using previous solution as a start point).
    double x[];
    // current values of the variables

    // Each variable corresponds to a vertex and
    // each constraint corresponds to an edge in the
    // directed weighted graph G=(V,E), where V={s,x[0],x[1],...,x[n-1]}
    // (s is an additional vertex)
    // and E={ (s,x[i]) | i in {0,...,n-1} } union
    //       { (x[i],x[j]) | there exists a constraint x[i]-x[j]>=b[i][j] }.
    // Weight function is as follows:
    //   w(s,x[i]) = 0 for all i in {0,...,n-1}
    //   w(x[i],x[j]) = -b[i][j] for each constraint x[i]-x[j]>=b[i][j]
    //                           that is equivalent to x[j]-x[i]<=-b[i][j].
    // It can be proven that if G has no negative cycle then
    // the shortest distances from s to all of x[i] form
    // the feasible solution that
    // satisfies all the difference constraints.
    // If there exists a negative cycle, the system is unsatisfiable.
    // Type to store the adjacent vertex. Instances of
    // this type correspond to edges in the graph.
    static class Adj {

        int to;    // to what vertex the edge is directed
        double w;  // current weight
        double maxWeight;     // maxWeight
        double desiredWeight; // desired weight <= maxWeight

        Adj(int to, double maxWeight, double desiredWeight) {
            this.to = to;
            this.maxWeight = maxWeight;
            this.desiredWeight = desiredWeight;
            this.w = desiredWeight;
        }
    }
    // The next field stores all the edges
    // { (x[i],x[j]) | there exists a constraint x[i]-x[j]>=b[i][j] }
    // with appropriate weights.
    OneWayLinkedList[] adjacent;
    // adjacent[i] represents the set of out-edges of the vertex i

    // Type to store in-edge (from, adjPtr->to).
    // from is a source vertex, adjPtr is a pointer to Adj structure
    // of an adjacent target vertex.
    static class InAdj {

        int from;
        Adj adjPtr;

        InAdj(int from, Adj adjPtr) {
            this.from = from;
            this.adjPtr = adjPtr;
        }
    }
    // The next field stores in-edges.
    ArrayList<ArrayList<InAdj>> in_edges;
    // Active vertices
//  static class ActiveVertices {
//    LinkedList<Integer> activeSet;
//      // the set of active vertices; in the beginning
//      // it contains all the vertices;
//      // later it will contain vertices involved in
//      // a constraint violation
//    ArrayList<ListIterator<Integer>> active;
//      // if active[i]!=end() then the vertex i is in the set of
//      // active vertices; we use this array to quickly test
//      // whether the given vertex is active;
//      // active counts only if x!=NULL
//    
////    void resize(int n);
////    void setActive(int v); // activate one vertex
////    void setActive(LinkedList<int> &list); // activate several vertices
////    void setInactive(int v);
////    bool isActive(int v);
////    void clear();
////    bool empty();
////    int takeOut();
//  void resize(int n)
//  {
//      active = new ArrayList<>(n);
//     for(int i = 0; i < n; i++)
//         active.add(null);
//  }
//
//  void setActive(int v)
//  {
//    if (!active.get(v).hasNext()) {
//      activeSet.add(v);
//      active.set(v, activeSet.listIterator(activeSet.size()-1));
//    }
//  }
//
//  void setActive(LinkedList<Integer> list)
//    // activate several vertices
//  {
//        for (Integer i : list)
//               setActive(i);
//  }
//
//  void setInactive(int v)
//  {
//    if (active.get(v).hasNext()) {
//      activeSet.erase(active[v]);
//      active[v]=activeSet.end();
//    }
//    #ifdef DEBUG
//    for (LinkedList<int>::iterator it=activeSet.begin();
//         it!=activeSet.end();
//         it++)
//         assert(it == (active[*it]));
//    #endif
//  }
//
//  bool DifferenceConstraints::ActiveVertices::isActive(int v)
//  {
//    return active[v]!=activeSet.end();
//  }
//
//  void DifferenceConstraints::ActiveVertices::clear()
//  {
//    activeSet.empty();
//    for (unsigned int i=0; i<active.size(); i++)
//      active[i] = activeSet.end();
//  }
//
//  bool DifferenceConstraints::ActiveVertices::empty()
//  {
//    return activeSet.empty();
//  }
//
//  int DifferenceConstraints::ActiveVertices::takeOut()
//  {
//    int u = activeSet.front();
//    activeSet.pop_front();
//    active[u] = activeSet.end();
//    return u;
//  }
//  }
    LinkedHashSet<Integer> activeVertices;
    // A shortest-path tree
    int[] parent;
    Adj[] parentEdge;
    // If (u,v) is a shortest-path tree edge, where
    // u=parent[v]!=-1, then parentEdge[v] is a pointer
    // to element of adjacent[u], where adjacent[u].to = v.
    // parentEdge is used when eliminating negative cycles
    int[] depth;
    // depth[v] is a depth of v in a shortest-path tree;
    // this is used to fasten the process of deremining negative
    // cycle when a back edge is found,
    // and also to find out the length of that cycle
    ArrayList<LinkedHashSet<Integer>> children;
    //ArrayList<LinkedList<Integer>> children;
    // children[v] contains a list of v children in
    // a shortest-path tree
    //ArrayList<ListIterator<Integer>> child;
    // if u is a parent of v then child[v] contains
    // a pointer to element of children[u] containing v

    // Types and methods for negative cycle elimination
//  bool formingCycle(int u, int v);
    // returns true, if path from v to u in the shortest-path tree
    // together with edge (u,v) form a cycle
    static class CycleEdge {

        Adj adjPtr;
        double dw;
    }

    static class CycleEdgeComparator implements Comparator<CycleEdge> {

        @Override
        public int compare(CycleEdge a, CycleEdge b) {
            return Double.compare(b.dw, a.dw);
        }
//    bool operator()(const CycleEdge &a, const CycleEdge &b)
//    {
//      return a.dw > b.dw;
//    }
    }
    
    static class CycleEdgeRoughComparator implements Comparator<CycleEdge> {

        @Override
        public int compare(CycleEdge a, CycleEdge b) {
            return Double.compare(a.dw, b.dw);
        }
    }

//  bool eliminateNegativeCycle(int u, int v, TemporaryIterator it);
//  void reviseSubtree(int v);
    // The algorithm:
    // A standart Bellman-Ford algorithm is based on vertices relaxation.
    // We use Bellman-Ford-Tarjan algorithm that speed up the
    // search using subtree disassembly method and active vertices list.
    // We find also stronly-connected components that leads
    // to linear-time algorithm for DAGs (directed acyclic graphs).
    // Bellman-Ford-Tarjan algorithm is used only inside the
    // strongly-connected components.
//  void subtreeDisassembly(int v);
//  void makeChild(int u, int v, Adj* parentEdge); // making v a child of u...
//  void relax(int u, TemporaryIterator it);
//  bool bellmanFordTarjan(
//    ArrayList<TemporaryIterator> &adjacent_end);
//
//  void stronglyConnectedComponents(
//    LinkedList<LinkedList<int> > &list,
//    ArrayList<TemporaryIterator> &adjacent_scc_end);
    // Finds stronly-connected components.
    // Each component is represented by a list of vertices.
    // This information is placed to the first parameter.
    // The order of elements in Adjacency lists may be changed, so
    // for each vertex u adjacent[u]
    // first contains vertices of the same component
    // followed by remaining adjacent vertices.
    // The ends of the first parts of the lists are returned
    // in adjacent_scc_end parameter.
    // So, from adjacent[u].begin() to adjacent_scc_end[u]-1
    // there are adjacent vertices from the same component and
    // from adjacent_scc_end[u] to adjacent[u].end()-1 there
    // are other vertices.
    // Rollback information
    TemporaryIterator[] rollback_adjacent_end;
    Integer[]/*ArrayList<InAdj>::iterator*/ rollback_in_edges_size;

//  void collectRollbackInfo();
    // this function is called when we've found a solution;
    // so this is a new rollback point; if, after adding
    // some more constraints, system
    // becomes unsatisfiable, it will be rolled back
    // to this point
//#endif // DIFFERENCE_CONSTRAINTS_H

    /*
    DifferenceConstraints.cpp
    (see DifferenceConstraints.h for more information about how to use)
    
    A class for solving the system of difference constraints.
    
    Author: Sergejs Kozlovics (in LaTeX: Sergejs Kozlovi\v{c}s)
    
    Last modified: 07.06.2007. 11:53.
     */
//#define DEBUG
//#define DEBUG_ALL // define, if you want detailed debug
//#ifdef DEBUG
//#include <iostream>
//using namespace std;
//#endif
//
//#ifdef DEBUG
//vector<int> negativeCycle;
//#endif
    public CycleReducer(int n, double epsilon) // n - the number of variables; we denote them as
    // x[0], x[1], ... , x[n-1]
    {
        EPSILON = epsilon;
//    INFINITY = 1.79E+308;
        this.n = n;
        adjacent = new OneWayLinkedList[n];
        in_edges = new ArrayList<>(n);
        activeVertices = new LinkedHashSet<>(n);
        children = new ArrayList<>();

        parent = new int[n];
        Arrays.fill(parent, -1);
        parentEdge = new Adj[n];
        for (int i = 0; i < n; i++) {
            adjacent[i] = new OneWayLinkedList();
            in_edges.add(new ArrayList<InAdj>());
            children.add(new LinkedHashSet<Integer>());
        }
        x = null;
        depth = new int[n];

        rollback_adjacent_end = new TemporaryIterator[n];
        rollback_in_edges_size = new Integer[n];

        collectRollbackInfo();
    }

    public void addConstraint(
            int i, int j, double minValue, double desiredValue) // add constraint
    //   x[i] - x[j] >= minValue
    // with indication to find a solution that satisfies
    //   x[i] - x[j] >= desiredValue,
    // where desiredValue>=minValue
    {
    	
        if (this.WHOLE_NUMBERS) {
       		minValue = Math.floor(minValue);
        	desiredValue = Math.ceil(desiredValue);
        	this.EPSILON = 0.4;
        }
        
        if (Math.abs(minValue) < EPSILON) {
            minValue = 0.0;
        }
        if (Math.abs(desiredValue) < EPSILON) {
            desiredValue = 0.0;
        }
        

        if (desiredValue < minValue) {
            desiredValue = minValue;
        }

        TemporaryIterator it = adjacent[i].push_back(new Adj(j, -minValue, -desiredValue));
        in_edges.get(j).add(new InAdj(i, it.value()));
        if ((x != null) && (x[i] + desiredValue < x[j])) {
            activeVertices.add(i);
        }

//    #ifdef DEBUG_ALL
//    cout << "Inserting edge ("<<i<<","<<j<<")..." << endl;
//    cout << "  minValue="<<minValue<<"; desiredValue="<<desiredValue<<endl;
//    cout << "  "<<i<<" -> ";
//    for (it=adjacent[i].begin(); it!=adjacent[i].end(); it++)
//      cout << it->to << " ";
//    cout << endl;
//    #endif
    }

    public double[] getSolution() // returns a pointer to an array with n elements
    // containing the values of x[0], x[1], ... , x[n-1]
    // or NULL, if the system of difference constraints
    // is unsatisfiable; in the last case, the system
    // is returned to the previous state when all the
    // conditions were satisfied
    {

        if (x == null) {
            LinkedList<LinkedList<Integer>> scc = new LinkedList<>();
            TemporaryIterator[] adjacent_scc_end = new TemporaryIterator[n];
            stronglyConnectedComponents(scc, adjacent_scc_end);

            x = new double[n];

            activeVertices.clear();

            while (!scc.isEmpty()) {
                assert (activeVertices.isEmpty());

                // implementing: activeSet = scc.front();
                activeVertices.addAll(scc.peek());//setActive(scc.peek());

                if (!bellmanFordTarjan(adjacent_scc_end)) {
                    clearSolution();
                    return null;
                }

                int u;
                TemporaryIterator it;

                while (!scc.peek().isEmpty()) {
                    u = scc.peek().poll();
                    for (it = adjacent_scc_end[u]; !it.equals(adjacent[u].end()); it.inc()) {
                        relax(u, it);
                    }
                }
                scc.poll();
            }
            collectRollbackInfo();
            return x;
        } else {
            double[] saved_x = x;
            x = Arrays.copyOf(x, n);

            int[] saved_parent = new int[parent.length];
            for (int i=0; i<parent.length; i++)
              saved_parent[i] = parent[i];


            Adj[] saved_parentEdge = new Adj[parentEdge.length];
            for (int i=0; i<parentEdge.length; i++)
              saved_parentEdge[i] = parentEdge[i];
            int[] saved_depth = new int[depth.length];
            for (int i=0; i<depth.length; i++)
              saved_depth[i] = depth[i];

            TemporaryIterator[] adjacent_end = new TemporaryIterator[n];
            for (int i = 0; i < n; i++) {
                adjacent_end[i] = adjacent[i].end();
            }


            if (bellmanFordTarjan(adjacent_end)) {
                saved_x = null;
                collectRollbackInfo();
                return x;
            } else { // rolling back...
                x = saved_x;
                parent = saved_parent;
                parentEdge = saved_parentEdge;
                // restoring the tree...
                for (int i = 0; i < n; i++) {
                    children.get(i).clear();
                }
                for (int i = 0; i < n; i++) {
                    if (parent[i] != -1) {
//            child[i] = children[parent[i]].insert(children[parent[i]].end(),i);
                        children.get(parent[i]).add(i);
                    }
                }

                depth = saved_depth;

                for (int i = 0; i < n; i++) {
                    adjacent[i].truncate(rollback_adjacent_end[i]);
                    ArrayList<InAdj> t = in_edges.get(i);
                    if (in_edges.size() > rollback_in_edges_size[i]) {
                        in_edges.set(i, new ArrayList<>(t.subList(0, rollback_in_edges_size[i])));
                    } else {
                        while (in_edges.get(i).size() < rollback_in_edges_size[i]) {
                            t.add(null);
                        }
                    }
                    //in_edges.get(i).resize (rollback_in_edges_size[i]);

                }
//        #ifdef DEBUG_ALL
//        cout << "Restored Adjacency lists: "<<endl;
//        for (int i=0; i<n; i++) {
//          cout << "  "<<i<<": ";
//          for (TemporaryIterator it=adjacent[i].begin();
//               it!=adjacent[i].end();
//               it++) {
//               cout << " "<<it->to;
//          }
//          cout << endl;
//        }
//        #endif
                return null;
            }
        }
    }

    void clearSolution() {
        if (x != null) {
            x = null;

            parent = new int[n];
            Arrays.fill(parent, -1);
            parentEdge = new Adj[n];
            depth = new int[n];

            activeVertices.clear();
        }
    }

    boolean formingCycle(int u, int v) {
        while ((u != -1) && (depth[u] > depth[v])) {
            u = parent[u];
        }
        return u == v;
    }

    // we need the following array to increment all
    // the weights in O(cycleLength) time
    static class EdgeIncrement {

        int count; // how many weights of edges we need to increment
        double increment;
    }

    boolean eliminateNegativeCycle(
            int u, int v, TemporaryIterator it) // returns: whether the negative cycle
    // (u,v) + (path from (v) to u in the shortest-path tree)
    // was eliminated (i.e. weights of some edges were increased);
    // if false is returned then there exists a negative cycle
    // that can't be eliminated;
    // note: if after calling this function
    // u is no more active, it should not be continued
    // to proceed
    {
        int cycleLength = depth[u] - depth[v] + 1;
        // the length of the cycle (in edges)

/*      System.out.println("eliminating cycle...");
      System.out.print(v+" "+u);
      int ww = u;
      while (ww!=v) {
        ww = parent[ww];
        System.out.print(" "+ww);
      };
      System.out.println();*/


//        double cw = 0.0;
        CycleEdge[] a = new CycleEdge[cycleLength + 1];
        for (int i = 0; i < a.length; i++) {
            a[i] = new CycleEdge();
        }
        int aIndex = 0;
        int w = u;
        while (w != v) {
            a[aIndex].adjPtr = parentEdge[w];
            a[aIndex].dw = parentEdge[w].maxWeight - parentEdge[w].w;
            /*fp*/ if (Math.abs(a[aIndex].dw) < EPSILON) /*fp*/ {
                a[aIndex].dw = 0.0;
            }
            aIndex++;
//      #ifdef DEBUG
//      cycleWeight += parentEdge[w]->w;
//            cw += parentEdge[w].w;
//      #endif
            w = parent[w];
        }
        Adj b = it.value();
        a[aIndex].adjPtr = it.value();
        a[aIndex].dw = it.value().maxWeight - it.value().w;
        /*fp*/ if (Math.abs(a[aIndex].dw) < EPSILON) /*fp*/ {
            a[aIndex].dw = 0.0;
        }
        aIndex++;
//    #ifdef DEBUG
//        cw+=it.value().w;
//    cycleWeight += it->w;
//
//    assert(fabs(cycleWeight - (x[u]+it->w - x[v]))<EPSILON);
//    /*fp*/ cycleWeight = x[u]+it->w - x[v];
        
//    #else
        double cycleWeight = x[u] + it.value().w - x[v];
//    #endif

        assert (cycleWeight < 0.0);

        a[aIndex].adjPtr = null;
        a[aIndex].dw = 0.0;
        assert (aIndex == cycleLength);
        
        if (this.FINE_SOLUTION) {
	
	        Arrays.sort(a, new CycleEdgeComparator());
	
	
	        EdgeIncrement[] inc = new EdgeIncrement[cycleLength];
	        for (int i = 0; i < inc.length; i++) {
	            inc[i] = new EdgeIncrement();
	        }
	        int incIndex = 0;
	
	    	
	        // A cycle that helps to calculate
	        // increments for each edge of the negative cycle.
	        // We want to maximize the value (maxWeight-w)
	        // between all the cycle edges.
	        aIndex = 0;
	        // by SK>>
	/*        while ((aIndex<=cycleLength) && (a[aIndex].dw < EPSILON))  
	        	aIndex++;
	        // by SK<<
	        if (aIndex>cycleLength)
	        	return false;*/
	        
	        int k = 1;
	        while ((cycleWeight < 0.0) && (a[aIndex].dw > 0.0)) { // fp
	            //int k = 1;
	            // implementing:
	            //   while (a[aIndex+k].dw == a[aIndex].dw)
	            //     k++;
	            while (Math.abs(a[aIndex + k].dw - a[aIndex].dw) < EPSILON) // fp
	            {
	                k++;
	            }
	            assert (a[aIndex].dw > a[aIndex + k].dw);
	
	            double increment = a[aIndex].dw - a[aIndex + k].dw;
	            aIndex += k;
	            increment *= aIndex;
	
	            // implementing:
	            //   if (cycleWeight + increment >= 0) {...
	            if (cycleWeight + increment > -EPSILON) { // fp
	                increment = -cycleWeight;
	                cycleWeight = 0.0;
	            } else {
	                cycleWeight += increment;
	            }
	
	            increment /= aIndex;
	            
	            if (this.WHOLE_NUMBERS)
	            	increment = Math.ceil(increment);
	
	            /* We could not use array inc, but write the
	            following lines:
	            for (int i=0; i<aIndex; i++)
	            a[i].adjPtr->w += increment;
	            But this for cycle could be executed for each
	            cycle vertex giving O(cycleLength^2) time.
	            
	            We use array inc to reaach O(cycleLength) time.
	             */
	            
	            //if (k!=0) {
	            	inc[incIndex].count = k;
	            	inc[incIndex].increment = increment;
	            	incIndex++;
	            //}
	        }
	
	
	        if (cycleWeight < 0.0) { // fp
	            inc = null; // weights were not changed
	            return false;
	        }
	
	        // Incrementing weights...
	
	        // Calculating total increments for all cycle edges...
	        int i;
	        for (i = incIndex - 2; i >= 0; i--) {
	            inc[i].increment += inc[i + 1].increment;
	        }
	/*        for (i = 0; i<inc.length; i++) {
	            System.out.print("inc"+i+"="+inc[i].increment+";cnt="+inc[i].count+" ");
	        }
	        System.out.println();*/ 
	
	        // Incrementing the edges...
	        aIndex = 0;
	        for (i = 0; i < incIndex; i++) {
	        	if (inc[i].count==0)
	        		break;
	            while (inc[i].count > 0) {
	                a[aIndex].adjPtr.w += inc[i].increment;
	                /*fp*/ if (Math.abs(a[aIndex].adjPtr.maxWeight - a[aIndex].adjPtr.w)
	                        /*fp*/ < EPSILON) {
	                    /*fp*/ a[aIndex].adjPtr.w = a[aIndex].adjPtr.maxWeight;
	                    /*fp*/ }
	                aIndex++;
	                inc[i].count--;
	            }
	        }
	
	        inc = null;

        }
        else {
	        Arrays.sort(a, new CycleEdgeRoughComparator());
	        
	        double sum=0.0;
	        for (int i=0; i<a.length; i++) {
	        	sum+=a[i].dw;
	        }
	        
	        
	        if (cycleWeight+sum<-EPSILON) 
	        	return false;
	        
        	for (int i=0; i<a.length; i++) {
        		double delta = Math.min(a[i].dw, -cycleWeight/(a.length-i)); // split delta among all remaining...        		
        		
        		
        		if (delta>0.0 && a[i].adjPtr!=null) {
	        	    a[i].adjPtr.w += delta;
	                if (Math.abs(a[aIndex].adjPtr.maxWeight - a[aIndex].adjPtr.w) < EPSILON) {
	                   a[aIndex].adjPtr.w = a[aIndex].adjPtr.maxWeight;
	                }
	                cycleWeight+=delta;
	                if (cycleWeight >= 0.0)
	                	break;
        		}
        	}
        	
	        if (cycleWeight < -EPSILON) {
	            return false;
	        }
        }
        
        
	    // weights of elements [0..aIndex-1] were changed

        reviseSubtree(v);
//    #ifdef DEBUG
//    cout << "Done eliminating cycle."<<endl;
//    #endif
        return true;
    }

    void reviseSubtree(int root) {
//    #ifdef DEBUG
//    cout << "Revising subree with root "<<root<<"..."<<endl;
//    #endif
        LinkedList<Integer> q = new LinkedList<>();
        //LinkedList<int>::iterator it;
        int u;
//    #ifdef DEBUG
//    cout << "  Fixing values..."<< endl;
//    #endif
        // fixing values...
        q.addLast(root);
        while (!q.isEmpty()) {
            u = q.poll();
            for (Integer v : children.get(u)) {
                //v = *it;
                x[v] = x[u] + parentEdge[v].w;
//             #ifdef DEBUG_ALL
//             cout << "  x["<<v<<"]="<<x[v];
//             #endif
                q.addLast(v);
            }
        }
//    #ifdef DEBUG
//    cout<<endl<<"  Done."<<endl;
//    cout << "  Checking for inconsistencies..."<< endl;
//    #endif
        // checking for inconsistencies...
        q.addLast(root);
        while (!q.isEmpty()) {
            u = q.poll();
            for (Integer v : children.get(u)) {
                depth[v] = depth[u] + 1; // x[u] may have been decreased
                // and become a child of another
                // parent, so its depth also
                // may have been changed; so, we
                // have to update the depth of v

                // implementing:
                //    min_w = min{x[t]+w(t,v) | t->v };
                //    t = such t that t->v and min_w = x[t]+w(t,v);
                //ArrayList<InAdj>::iterator t_it;
                int t = u;
                double min_w = x[v];

                //for (t_it=in_edges[v].begin(); t_it!=in_edges[v].end(); t_it++)
                for (InAdj t_it : in_edges.get(v)) {
                    if (/*(x[t_it->from]!=INFINITY)&&*/(x[t_it.from] + t_it.adjPtr.w < min_w)) {
                        min_w = x[t_it.from] + t_it.adjPtr.w;
                        t = t_it.from;
                    }
                }
                if (min_w < x[v]) {
                    activeVertices.add(t);
                }
                q.addLast(v);

            }
//    #ifdef DEBUG
//    cout <<"  Done."<<endl;
//    cout << "Done." << endl;
//    #endif
        }
    }

    void subtreeDisassembly(int v) {
//    #ifdef DEBUG_ALL
//    cout << "Subtree disassembly from vertex "<< v <<"..." << endl;
//    #endif
        LinkedList<Integer> q = new LinkedList<>();
        //LinkedList<int>::iterator it;
        q.addLast(v);
        int u;
        while (!q.isEmpty()) {
            u = q.poll();
            for (Integer v2 : children.get(u)) {
                activeVertices.remove(v2);
                parent[v2] = -1;
                x[v2] = 0.0;//INFINITY;
                q.addLast(v2);
            }
            children.get(u).clear();
        }
//    #ifdef DEBUG_ALL
//    cout << "Done." << endl;
//    #endif
    }

    void makeChild(int u, int v, Adj parentAdjPtr) {
        // making v a child of u...
        if (parent[v] != -1) {
            children.get(parent[v]).remove(v);
        }
        // remove v from children list of previous parent
        //child[v] = children[u].insert(children[u].end(), v);
        children.get(u).add(v);
        // add v to children list of new parent u
        parent[v] = u;
        parentEdge[v] = parentAdjPtr; //&(*it);
        depth[v] = depth[u] + 1;
    }

    void relax(int u, TemporaryIterator it) {
        int v = it.value().to;
//    #ifdef DEBUG_ALL
//    cout << "relax ("<<u<<","<<v<<"): x["<<u<<"]="<<x[u]<<", w="<<it->w
//         << ", x["<<v<<"]="<<x[v];
//    #endif
//    assert(x[u] != INFINITY);
        if (x[v] > x[u] + it.value().w) {
            // relaxing...
            x[v] = x[u] + it.value().w;
//      #ifdef DEBUG_ALL
//      cout << " OK: x["<<v<<"]="<<x[v] << endl;
//      #endif
            // making v a child of u...
            makeChild(u, v, it.value());
        }
//    #ifdef DEBUG_ALL
//    else
//      cout << " SKIPPED" << endl;
//    #endif
//
//    #ifdef DEBUG_ALL
//    {
//    //checking the tree
//    std::queue<int> q;
//    LinkedList<int>::iterator it;
//    q.push(v);
//    int u;
//    while (!q.empty()) {
//      u = q.front(); q.pop();
//      for (it=children[u].begin();
//           it!=children[u].end();
//           it=children[u].erase(it)) {
//             v = *it;
//             if (u!=parent[v]) {
//               cerr << u << "!=parent["<<v<<"]"<<endl;
//               exit(0);
//             }
//             if (fabs(x[u]+parentEdge[v]->w - x[v]) >= EPSILON) { // fp
//               cerr << "x["<<u <<"]+"<<parentEdge[v]->w<< "!=x["<<v<<"]"<<endl;
//               cerr << x[u]<<"+"<<parentEdge[v]->w<< "!="<<x[v]<<endl;
//               exit(0);
//             }
//           }
//    }
//    }
//    #endif
    }

    boolean bellmanFordTarjan(TemporaryIterator[] adjacent_end) {
        int u, v;
        TemporaryIterator it;

        while (!activeVertices.isEmpty()) {
        	
            u = activeVertices.iterator().next();
            activeVertices.remove(u);
//      assert(x[u] != INFINITY);

            for (it = adjacent[u].begin(); !it.equals(adjacent_end[u]); it.inc()) {
                v = it.value().to;
                // implementing:
                // if (x[u] + it->w < x[v]) {...
                if (x[u] + it.value().w <= x[v] - EPSILON) { // fp
                    // checking for negative cycle...
                    if (formingCycle(u, v)) {
                        if (!eliminateNegativeCycle(u, v, it)) {
//              #ifdef DEBUG
//              negativeCycle.clear();
//              int w = u;
//              negativeCycle.push_back(u);
//              while (w!=v) {
//                w = parent[w];
//                negativeCycle.push_back(w);
//              };
//              reverse(negativeCycle.begin(),negativeCycle.end());
////              cerr << endl;
//              #endif
                            return false;
                        }
                    } else {
                        subtreeDisassembly(v);
                        activeVertices.add(v);
                        relax(u, it);
                    }
                } else {
                    it.value().w = x[v] - x[u];
                    if (it.value().w < it.value().desiredWeight) {
                        it.value().w = it.value().desiredWeight;
                    }
                    // w could be incremented when eliminating negative
                    // cycle; if the system was rolled back, the cycle
                    // could disappear, but w did not decremented;
                    // so, we do it now. There will be no error, if
                    // this is not done.
                }
            } // for
        }
        return true;
    }

    void stronglyConnectedComponents(LinkedList<LinkedList<Integer>> list,
            TemporaryIterator[] adjacent_scc_end) {
//    #ifdef DEBUG
//    cout << "Searching for strongly connected components..." << endl;
//    #endif

        assert (list.isEmpty());

        int u;

        int[] parent = new int[n];
        int[] low = new int[n];
        LinkedList<Integer> scc_st = new LinkedList<>(); //stack
        int[] startTime = new int[n];
        Arrays.fill(startTime, -1);
        int time = 0;

        for (u = 0; u < n; u++) // external DFS-cycle
        {
            if (startTime[u] == -1) {
                // Non-recursive DFS:
                LinkedList<TemporaryIterator> st = new LinkedList<>(); // stack
                TemporaryIterator it = null;
                int v;
                for (;;) {
                    if (startTime[u] == -1) { // u is being visited for the first time
            /*=> before visiting <=*/
                        low[u] = startTime[u] = time;
                        time++;
                        scc_st.push(u);
                        adjacent_scc_end[u] = adjacent[u].begin();
                        it = adjacent[u].begin();
//            #ifdef DEBUG_ALL
//            cout << "  Before visiting "<<u<<"."<<endl;
//            #endif
                    }

                    if (!it.equals(adjacent[u].end())) {
                        v = it.value().to;
                        /*=> before reaching v from u <=*/
//            #ifdef DEBUG_ALL
//            cout << "  Before reaching "<<v<<" from "<<u<<"."<<endl;
//            #endif
                        if (startTime[v] == -1) { // v was not reached yet?
                            parent[v] = u;
//              #ifdef DEBUG_ALL
//              cout << "  Before recursive call "<<u<<"->"<<v<<"."<<endl;
//              #endif
              /*=> before recursive call emulation <=*/
                            st.push(it);
                            u = v;
                        }
                    } else {
                        /*=> after visiting u <=*/
//            #ifdef DEBUG_ALL
//            cout << "  After visiting "<<u<<"."<<endl;
//            #endif
                        if (low[u] == startTime[u]) {
                            list.push(new LinkedList<Integer>());
                            do {
                                v = scc_st.poll();
                                low[v] = n;
                                list.peek().add(v);
                            } while (v != u);
                        }

                        if (st.isEmpty()) {
                            break;
                        }

                        it = st.poll();
                        v = u;
                        u = parent[v];
                        /*=> after recursive call emulation from u to v <=*/
//            #ifdef DEBUG_ALL
//            cout << "  After recursive call "<<u<<"->"<<v<<"."<<endl;
//            #endif
                    }                    
                    if (startTime[v] != -1) {
                        /*=> after reaching v from u <=*/
//            #ifdef DEBUG_ALL
//            cout << "  After reaching "<<v<<" from "<<u<<"."<<endl;
//            #endif
                        if (low[v] < low[u]) {
                            low[u] = low[v];
                        }

                        if (low[v] != n) {
                            if (it.equals(adjacent_scc_end[u])) {
                                adjacent_scc_end[u].inc();
                                it.inc();
                            } else {
                                adjacent[u].move_to_front(it);
                                // it will point to the item followed the moved one
                                // before moving
                                if (adjacent_scc_end[u].equals(adjacent[u].begin())) {
                                    adjacent_scc_end[u].inc();
                                }
                            }
                        } else {
                            it.inc();
                        }
                    }
                } // end for(;;)
                // END OF DFS
            }
        }

//    #ifdef DEBUG_ALL
//    LinkedList<LinkedList<int> >::iterator it1;
//    LinkedList<int>::iterator it2;
//    for (it1=list.begin(); it1!=list.end(); it1++) {
//      cout << "[ ";
//      for (it2=it1->begin(); it2!=it1->end(); it2++)
//        cout << (*it2) << " ";
//      cout << "]"<<endl;
//    }
//    cout << "Done." << endl;
//    cout << "New Adjacency lists: "<<endl;
//    for (int i=0; i<n; i++) {
//      cout << "  "<<i<<": ";
//      for (TemporaryIterator it=Adjacent[i].begin();
//           it!=Adjacent[i].end();
//           it++) {
//           if (it == adjacent_scc_end[i])
//             cout << " | ";
//           cout << " "<<it->to;
//      }
//      cout << endl;
//    }
//    #endif
    }

    void collectRollbackInfo() {
        for (int i = 0; i < n; i++) {
            rollback_adjacent_end[i] = adjacent[i].end();
            rollback_in_edges_size[i] = in_edges.get(i).size();
        }
    }
}
