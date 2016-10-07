package aStar;

import java.util.List;

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
		if (this.x == node.x && this.y == node.y && this.parentNode == node.parentNode
				&& this.gCost == node.gCost && this.hCost == node.hCost)
			return true;
		return false;
	}
	
	public boolean isClosed(List<Node> closedNodes) {
		for (Node node : closedNodes)
			if (this.isSameNode(node))
				return true;
		return false;
	}

	public void set_gCost(int cost) {
		gCost = cost;
	}

	private void set_hCost(Node endNode) {
		if (endNode == null) // for creating the endNode itself on the grid, the
								// hCost would be 0
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
