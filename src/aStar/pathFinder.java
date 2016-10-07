package aStar;

import java.util.ArrayList;
import java.util.List;

public class pathFinder {

	// constant multiples to add to G cost
	private final int CROSS_COST = 10;
	private final int DIAGANOL_COST = 14;

	private Node startNode, endNode, currentNode;
	private Node[] neighborNode = new Node[8];

	// order x: -1, 0, 1, -1, 1, -1, 0, 1
	// order y: 1, 1, 1, 0, 0, -1, -1, -1
	private final int[] NEIGHBOR_X_COORD_ORDER = { -1, 0, 1, -1, 1, -1, 0, 1 };
	private final int[] NEIGHBOR_Y_COORD_ORDER = { 1, 1, 1, 0, 0, -1, -1, -1 };
	private final int[] NEIGHBOR_G_COST_ORDER = { DIAGANOL_COST, CROSS_COST, DIAGANOL_COST, CROSS_COST, CROSS_COST,
			DIAGANOL_COST, CROSS_COST, DIAGANOL_COST };

	private List<Node> openNodes = new ArrayList<Node>();
	private List<Node> closedNodes = new ArrayList<Node>();

	private Node[] blockedNodes = null;
	
	private List<Node> path = new ArrayList<Node>();
	
	public pathFinder(int startX, int startY, int endX, int endY) {
		// create start and end nodes
		endNode = new Node(endX, endY, null, null);

		startNode = new Node(startX, startY, null, endNode);
		startNode.set_gCost(0);

		setCurrentNode(startNode);

		openNodes.add(startNode);

		// Fill blocked node array
	}

	public void setCurrentNode(Node node) {
		currentNode = node;
	}

	public void findNeighborNodes() {

		int parent_gCost = currentNode.get_gCost();
		int parentX = currentNode.getX();
		int parentY = currentNode.getY();

		for (int i = 0; i < neighborNode.length; i++)
			if (true /* ignore blocked nodes and start node */) {
				neighborNode[i] = new Node(parentX + NEIGHBOR_X_COORD_ORDER[i], parentY + NEIGHBOR_Y_COORD_ORDER[i],
						currentNode, endNode);
				neighborNode[i].set_gCost(parent_gCost + NEIGHBOR_G_COST_ORDER[i]);
			}

		// sort by f cost

		boolean swapped;
		int cutOff = neighborNode.length;
		Node swap;

		do {
			swapped = false;
			for (int i = 1; i < cutOff; i++) {
				if (neighborNode[i - 1].get_fCost() < neighborNode[i].get_fCost()) {
					swap = neighborNode[i - 1];
					neighborNode[i - 1] = neighborNode[i];
					neighborNode[i] = swap;
					swapped = true;
				}
			}
			cutOff--;
		} while (swapped);

		// add all to list of open Nodes unless they are equal to the starting
		// node or blocked nodes

		for (Node node : neighborNode)
			if (!node.isEqual(startNode) /* && !isBlocked(node) */)
				openNodes.add(0, node);
	}

	/*
	 * private boolean isBlocked(Node node) { for (Node blocked : blockedNodes)
	 * if (node.isEqual(blocked)) return true; return false; }
	 */

	public void closeCurrentNode() {
		openNodes.remove(currentNode);
		closedNodes.add(currentNode);
	}

	public Node bestCandidateNode() {
		return openNodes.get(0);
	}

	public Node getCurrentNode() {
		return currentNode;
	}

	public Node getEndNode() {
		return endNode;
	}
	
	public Node createPathList(Node node) {
		if (node.isEqual(startNode))
			return node;
		else
			createPathList(node.parent());
		path.add(0, node);
		return node;
	}
		
	
	public void printPath() {
		System.out.println("Path:");
		for (Node node : path) 
			System.out.print("(" + node.getX() + ", " + node.getY() + ") --> ");
		System.out.println("(" + startNode.getX() + ", " + startNode.getY() + ")");
	}

	public void print() {
		/*
		 * for (int y = 0; y < grid[0].length; y++) for (int x = 0; x <
		 * grid.length; x++) if (x < grid.length - 1) System.out.print("(" + x +
		 * ", " + y + ")\t"); //System.out.print(grid[x][y] + " "); else
		 * System.out.println("(" + x + ", " + y + ")\t");
		 * //System.out.println(grid[x][y] + " ");
		 */

		System.out.println("Neighboring Nodes to Current Node:");
		for (int i = 0; i < neighborNode.length; i++)
			System.out.println(i + ":\t" + neighborNode[i].getX() + ", " + neighborNode[i].getY() + "\tG Cost: "
					+ neighborNode[i].get_gCost() + "\tH Cost: " + neighborNode[i].get_hCost() + "\tF Cost: "
					+ neighborNode[i].get_fCost());

		System.out.println();
		System.out.println("List of Open Nodes:");
		for (Node node : openNodes)
			System.out.println("(" + node.getX() + ", " + node.getY() + ")\t\t" + "G Cost: " + node.get_gCost()
					+ "\tH Cost: " + node.get_hCost() + "\tF Cost: " + node.get_fCost());

		System.out.println();
		System.out.println("List of Closed Nodes:");
		for (Node node : closedNodes)
			System.out.println("(" + node.getX() + ", " + node.getY() + ")\t\t" + "G Cost: " + node.get_gCost()
					+ "\tH Cost: " + node.get_hCost() + "\tF Cost: " + node.get_fCost());
	}
}

/*
 * old neighborNode creator
 *
 * neighborNode[0] = new Node(currentNode.getX() - 1, currentNode.getY() + 1,
 * currentNode, endNode); neighborNode[0].set_gCost(currentNode.get_gCost() +
 * DIAGANOL_COST);
 * 
 * neighborNode[1] = new Node(currentNode.getX(), currentNode.getY() + 1,
 * currentNode, endNode); neighborNode[1].set_gCost(currentNode.get_gCost() +
 * CROSS_COST);
 * 
 * neighborNode[2] = new Node(currentNode.getX() + 1, currentNode.getY() + 1,
 * currentNode, endNode); neighborNode[2].set_gCost(currentNode.get_gCost() +
 * DIAGANOL_COST);
 * 
 * neighborNode[3] = new Node(currentNode.getX() - 1, currentNode.getY(),
 * currentNode, endNode); neighborNode[3].set_gCost(currentNode.get_gCost() +
 * CROSS_COST);
 * 
 * neighborNode[4] = new Node(currentNode.getX() + 1, currentNode.getY(),
 * currentNode, endNode); neighborNode[4].set_gCost(currentNode.get_gCost() +
 * CROSS_COST);
 * 
 * neighborNode[5] = new Node(currentNode.getX() - 1, currentNode.getY() - 1,
 * currentNode, endNode); neighborNode[5].set_gCost(currentNode.get_gCost() +
 * DIAGANOL_COST);
 * 
 * neighborNode[6] = new Node(currentNode.getX(), currentNode.getY() - 1,
 * currentNode, endNode); neighborNode[6].set_gCost(currentNode.get_gCost() +
 * CROSS_COST);
 * 
 * neighborNode[7] = new Node(currentNode.getX() + 1, currentNode.getY() - 1,
 * currentNode, endNode); neighborNode[7].set_gCost(currentNode.get_gCost() +
 * DIAGANOL_COST);
 */