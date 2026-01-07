/**
 * Node. Contains current puzzle board and previous parent node
 */
public class Node implements Comparable<Node> {
	int[][] board;
	Node previous;

	// both for A* only
	int cost;
	int priority; // used for priority queue ordering

	public Node(int[][] board, Node previous) {
		this.board = board;
		this.previous = previous;
	}

	// this one for A* only
	@Override
	public int compareTo(Node o) {
		return Integer.compare(priority, o.priority);
	}
}

