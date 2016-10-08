package aStar;

public class Node {
	// distance from start node, distance from end node
	private int gCost, hCost;
	private int x, y;
	private Node parentNode;

	public Node(int x, int y, Node parentNode, Node endNode) {
		this.x = x;
		this.y = y;
		this.parentNode = parentNode;
		set_hCost(endNode);
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public Node parent() {
		return parentNode;
	}

	public boolean hasSameCoords(Node node) {
		if (this.x == node.x && this.y == node.y)
			return true;
		return false;
	}

	public boolean isSameNode(Node node) {
		if (this.x == node.x && this.y == node.y && this.gCost == node.gCost && this.hCost == node.hCost)
			return true;
		return false;
	}

	@Override
	public boolean equals(Object obj) {
		Node node = (Node) obj;

		if (node.get_fCost() == 0) {
			if (this.hasSameCoords(node))
				return true;
		} else if (this.isSameNode(node))
			return true;

		return false;
	}

	@Override
	public int hashCode() {
		return this.hashCode();
	}

	public void set_gCost(int cost) {
		gCost = cost;
	}

	private void set_hCost(Node endNode) {
		if (endNode == null) // end nodes h cost is 0
			hCost = 0;
		else
			hCost = 10 * (Math.abs(this.x - endNode.getX()) + Math.abs(this.y - endNode.getY()));
	}

	public int get_fCost() {
		return gCost + hCost;
	}

	public int get_gCost() {
		return gCost;
	}

	public int get_hCost() {
		return hCost;
	}
}
