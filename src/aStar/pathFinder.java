package aStar;

import java.util.ArrayList;
import java.util.List;

public class PathFinder {

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

	private List<Node> path = new ArrayList<Node>();

	public PathFinder(int startX, int startY, int endX, int endY, Node[] blockedNodes) {
		// create start and end nodes
		endNode = new Node(endX, endY, null, null);

		startNode = new Node(startX, startY, null, endNode);
		startNode.set_gCost(0);

		currentNode = startNode;
		openNodes.add(currentNode);

		for (Node blocked : blockedNodes)
			closedNodes.add(blocked);
	}

	public void findNeighborNodes() {

		int parent_gCost = currentNode.get_gCost();
		int parentX = currentNode.getX();
		int parentY = currentNode.getY();

		for (int i = 0; i < neighborNode.length; i++) {
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

		// add nodes not already part of any list
		for (Node node : neighborNode)
			if (!closedNodes.contains(node) && !openNodes.contains(node))
				openNodes.add(node);
	}

	public void nextNode() {
		openNodes.remove(currentNode);
		closedNodes.add(currentNode);

		currentNode = bestCandidateNode();
	}

	public Node bestCandidateNode() {
		// make one pass and grab index of node with smallest f cost that isn't
		// closed
		int smallest_fCost = openNodes.get(0).get_fCost(), smallest_hCost = openNodes.get(0).get_hCost(),
				indexOfSmallest = 0;

		for (Node node : openNodes)
			if (node.get_fCost() != 0 && (node.get_fCost() < smallest_fCost
					|| (node.get_fCost() == smallest_fCost && node.get_hCost() <= smallest_hCost))) {
				smallest_fCost = node.get_fCost();
				smallest_hCost = node.get_hCost();
				indexOfSmallest = openNodes.indexOf(node);
			}

		return openNodes.get(indexOfSmallest);
	}

	public Node getCurrentNode() {
		return currentNode;
	}

	public Node getEndNode() {
		return endNode;
	}

	public Node createPath(Node node) {
		if (node.hasSameCoords(startNode))
			return node;
		else
			createPath(node.parent());
		path.add(0, node);
		return null;
	}

	public void printPath() {
		System.out.println("Path:");
		for (Node node : path)
			System.out.print("(" + node.getX() + ", " + node.getY() + ") --> ");
		System.out.println("(" + startNode.getX() + ", " + startNode.getY() + ")");
	}

	
	public void printCurrentNeighborNodes() {
		System.out.println();
		System.out.println("Neighboring Nodes to Current Node:");
		for (int i = 0; i < neighborNode.length; i++)
			System.out.println(i + ":\t" + neighborNode[i].getX() + ", " + neighborNode[i].getY() + "\tG Cost: "
					+ neighborNode[i].get_gCost() + "\tH Cost: " + neighborNode[i].get_hCost() + "\tF Cost: "
					+ neighborNode[i].get_fCost());
	}
	public void printLists() {
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
