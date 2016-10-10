package aStar;

import java.util.ArrayList;
import java.util.List;

public class Tester {
	public static void main(String[] args) {

		// Node[] blockedNodes = { new Node(3, 1, null, null), new Node(3, 2, null, null), new Node(4, 2, null, null), new Node(5, 2, null, null), new Node(6, 2, null, null), new Node(7, 2, null, null) };
		// Node[] blockedNodes = { new Node(4, 1, null, null), new Node(4, 2, null, null), new Node(4, 3, null, null)};
		// Node[] blockedNodes = new Node[0];
		
		
		// Transitioned to array lists
		List<Node> blockedNodes = new ArrayList<Node>();
		
		blockedNodes.add(new Node(4, 1, null, null));
		blockedNodes.add(new Node(4, 2, null, null));
		blockedNodes.add(new Node(4, 3, null, null));		

		PathFinder maze = new PathFinder(2, 2, 6, 2, blockedNodes);
		
		while (!maze.getCurrentNode().hasSameCoords(maze.getEndNode())) {
			maze.findNeighborNodes();
			maze.nextNode();
			maze.printCurrentNeighborNodes();
		}

		maze.createPath(maze.getCurrentNode());

		System.out.println();
		maze.printLists();

		System.out.println();
		maze.printPath();
	}
}
