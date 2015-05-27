package cs601.graph;

import java.util.List;
import java.util.Map;

public interface Graph<ID extends Comparable,N extends Node<ID>> {
	/** Add a node to the graph by mapping node.getName() to node.
	 *  If node already exists in the graph, return that one
	 *  and do not insert the node parameter into the graph.
	 *  If the node does not exist already, add it to the graph and return.
	 *
	 *  Do nothing upon null node.
	 */
	N addNode(N node);

	/** Return a sorted (by name) list of all nodes between start and stop, inclusively.
	 *  If either the start or stop nodes are null, return Collections.emptyList()
	 */
	List<N> getAllNodes(ID start, ID stop);

	/** Return the minimum number of edges traversed to go from start to stop.
	 *  Return -1 if there is no path from start to stop or one of the
	 *  parameters is null. If stop == start, return 0.
	 */
	int getMinPathLength(ID start, ID stop);

	/** Return a sorted list of all nodes reachable from start, inclusively.
	 *  If start node is null, return Collections.emptyList().
	 */
	List<N> getAllReachableNodes(ID start);

	/** Return a list of graph roots. A graph root is any node has no
	 *  incident (incoming) edges. This returns an empty list if there
	 *  are no roots.
	 */
	List<ID> getRootNames();

	/** Return an edge list for graph in the form of a string, one edge per line */
	String toString();
}
