package cs601.graph;

import java.util.Iterator;

public interface Node<ID extends Comparable> extends Comparable<Node<ID>> {
	/** Get the ith edge counting from zero. Throw index out of bound exception
	 *  if i is not within 0..n-1 for n edges
	 */
	ID getEdge(int i) throws IndexOutOfBoundsException;

	/** Return the number of edges for this node. */
	int getEdgeCount();

	/** Added an edge to target node by adding target.getName() to this
	 *  node's edge list. Do not add an edge with the same name more than once
	 *  for each node. Do nothing if target is null.
	 */
	void addEdge(Node<ID> target);

	/** Get the unique identifier for this node. You do not have to enforce
	 *  that this name is unique, but we can assume that the user uses
	 *  unique names.
	 */
	ID getName();

	/** Return an iterator object to that iterates over the edges, if any.
	 *  If there are no edges, return an iterator that immediately says there
	 *  are no nodes. This makes it easy to implement many of the algorithms.
	 */
	public Iterable<ID> edges();

	/** Return the node's name as a String */
	public String toString();
}
