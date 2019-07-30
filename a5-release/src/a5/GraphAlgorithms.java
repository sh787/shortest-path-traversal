package a5;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import a5.Heap;
import common.NotImplementedError;
import graph.Edge;
import graph.Node;
import graph.LabeledEdge;

/** We've provided depth-first search as an example; you need to implement Dijkstra's algorithm.
 */
public class GraphAlgorithms  {
	/** Return the Nodes reachable from start in depth-first-search order */
	public static <N extends Node<N,E>, E extends Edge<N,E>>
	List<N> dfs(N start) {
		
		Stack<N> worklist = new Stack<N>();
		worklist.add(start);
		
		Set<N>   visited  = new HashSet<N>();
		List<N>  result   = new ArrayList<N>();
		while (!worklist.isEmpty()) {
			// invariants:
			//    - everything in visited has a path from start to it
			//    - everything in worklist has a path from start to it
			//      that only traverses visited nodes
			//    - nothing in the worklist is visited
			N next = worklist.pop();
			visited.add(next);
			result.add(next);
			for (N neighbor : next.outgoing().keySet())
				if (!visited.contains(neighbor))
					worklist.add(neighbor);
		}
		return result;
	}
	
	/**
	 * Return a minimal path from start to end.  This method should return as
	 * soon as the shortest path to end is known; it should not continue to search
	 * the graph after that. 
	 * 
	 * @param <N> The type of nodes in the graph
	 * @param <E> The type of edges in the graph; the weights are given by e.label()
	 * @param start The node to search from
	 * @param end   The node to find
	 */
	public static <N extends Node<N,E>, E extends LabeledEdge<N,E,Integer>>
	List<N> shortestPath(N start, N end) {
		
		Heap<N,Integer> worklist = new Heap<N,Integer>(Collections.reverseOrder());
		worklist.add(start, 0);
		Set<N>   visited  = new HashSet<N>();
		//List<N>  result   = new ArrayList<N>();
		//Map <N, N> previous = new HashMap <N, N>();
		//Map <N, NodeDP> info = new HashMap<N, NodeDP>();
		Map <N, Integer> visitedEdges = new HashMap<N, Integer>();
		Map <N, N> backPointers = new HashMap<N, N>();
		
		while (worklist.size() > 0) {
			//NodeDP<N> ndp = new NodeDP<N>(worklist.getPriority(worklist.peek()), worklist.peek(), true);
			visitedEdges.put(worklist.peek(), worklist.getPriority(worklist.peek()));
			N next = worklist.poll();
			visited.add(next);
			//result.add(next);
			for (N neighbor : next.outgoing().keySet()) {
				int p = visitedEdges.get(next) + next.outgoing().get(neighbor).label();
				if (!visited.contains(neighbor)) {
					if (worklist.contains(neighbor)) {
						if (p < worklist.getPriority(neighbor)) {
							worklist.changePriority(neighbor, p);
							backPointers.put(neighbor, next);
						}
					} else {
						worklist.add(neighbor, p);
						backPointers.put(neighbor, next);
					}
				}
			}
		}
		
		List<N>  result2   = new ArrayList<N>();
		N trace = end;
		result2.add(trace);
		while (backPointers.get(trace) != null) {
			result2.add(0, backPointers.get(trace));
			trace = backPointers.get(trace);
		}
		
		if ((!start.equals(end)) && (!visitedEdges.containsKey(end))) {
			return new ArrayList<N>();
		}
		
		return result2;
		
	}
	
	//Make class with node, distance, and previous node
	/**
	 * NDP is a representation of a node in relation to the shortest path
	 * algorithm that includes its distance from the start and the NDP prior
	 * along its path.
	 * 
	 * @param <N>
	 */
	public class NodeDP<N> {
		int distance;
		N previous;
		boolean visited;
		
		public NodeDP(int d, N p, boolean v) {
			this.distance = d;
			this.previous = p;
			this.visited = v;
		}
		
	}
	
}
