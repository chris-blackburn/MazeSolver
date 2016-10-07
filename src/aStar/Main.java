package aStar;

public class Main {
	public static void main (String [] args){
		pathFinder maze = new pathFinder(2, 2, 6, 2 /* add node array for positions of blocked nodes*/);
		
		/*do {
			maze.findNeighborNodes();
			maze.closeCurrentNode();
			maze.setCurrentNode(maze.bestCandidateNode());
		} while (!maze.getCurrentNode().isEqual(maze.getEndNode()));*/
		
		while (!maze.getCurrentNode().isEqual(maze.getEndNode())) {
			maze.findNeighborNodes();
			maze.closeCurrentNode();
			maze.setCurrentNode(maze.bestCandidateNode());
			System.out.println("(" + maze.getCurrentNode().getX() + ", " + maze.getCurrentNode().getY() + ")" + "\t\tH Cost: " + maze.getCurrentNode().get_hCost());
		}
		
		/*maze.print();*/
	}
}
