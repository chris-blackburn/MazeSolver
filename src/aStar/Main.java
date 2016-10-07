package aStar;

public class Main {
	public static void main(String[] args) {

		Node[] blockedNodes = { new Node(4, 1, null, null), new Node(4, 2, null, null), new Node(4, 3, null, null) };
		// blockedNodes = new Node[0];

		pathFinder maze = new pathFinder(2, 2, 6, 2, blockedNodes);

		System.out.println("(" + maze.getCurrentNode().getX() + ", " + maze.getCurrentNode().getY() + ")"
				+ "\t-\tG Cost: " + maze.getCurrentNode().get_gCost() + "\tH Cost: " + maze.getCurrentNode().get_hCost()
				+ "\tF Cost: " + maze.getCurrentNode().get_fCost());
		
		do {
			maze.findNeighborNodes();
			maze.closeCurrentNode();
			maze.setCurrentNode(maze.bestCandidateNode());
			System.out.println("(" + maze.getCurrentNode().getX() + ", " + maze.getCurrentNode().getY() + ")"
					+ "\t-\tG Cost: " + maze.getCurrentNode().get_gCost() + "\tH Cost: "
					+ maze.getCurrentNode().get_hCost() + "\tF Cost: " + maze.getCurrentNode().get_fCost());
		} while (!maze.getCurrentNode().hasSameCoords(maze.getEndNode()));

		maze.createPathList(maze.getCurrentNode());

		System.out.println();
		maze.print();

		System.out.println();
		maze.printPath();
	}
}
