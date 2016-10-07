package aStar;

public class Main {
	public static void main (String [] args){
		pathFinder maze = new pathFinder(0, 0, 9, 9 /* add node array for positions of blocked nodes*/);
		
		while (!maze.getCurrentNode().isEqual(maze.getEndNode())) {
			maze.findNeighborNodes();
			maze.closeCurrentNode();
			maze.setCurrentNode(maze.bestCandidateNode());
			System.out.println("(" + maze.getCurrentNode().getX() + ", " + maze.getCurrentNode().getY() + ")" + "\t\tH Cost: " + maze.getCurrentNode().get_hCost());
		}
		
		maze.createPathList(maze.getCurrentNode());
		
		System.out.println();
		maze.print();
		
		System.out.println();
		maze.printPath();
	}
}
