package display;

import java.awt.Color;
import java.awt.EventQueue;

import javax.swing.JFrame;
import aStar.Node;
import aStar.PathFinder;

import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class GUI {

	private static JFrame frame;

	private static int xRange = 10;
	private static int yRange = 10;

	private static int startX = 1;
	private static int startY = 1;
	private static int endX = xRange - 2;
	private static int endY = yRange - 2;

	private static NodeJLabel[][] grid = new NodeJLabel[yRange][xRange];

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@SuppressWarnings("static-access")
			public void run() {
				try {
					GUI window = new GUI();
					window.frame.setVisible(true);

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public GUI() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setTitle("Maze Solver");
		frame.setResizable(false);
		frame.setBounds(100, 100, 750, 750);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new GridLayout(xRange, yRange, 1, 1));

		JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);

		JMenu mnNewMenu = new JMenu("Maze");
		menuBar.add(mnNewMenu);

		JMenuItem mntmReset = new JMenuItem("Reset");
		mntmReset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				initGrid();
			}
		});
		mnNewMenu.add(mntmReset);

		JMenuItem mntmSolve = new JMenuItem("Solve");
		mntmSolve.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				PathFinder maze = new PathFinder(1, 1, xRange - 2, yRange - 2, grabBlockedNodes());
				calculateMaze(maze);
				printMaze(maze);
			}
		});
		mnNewMenu.add(mntmSolve);

		// draw the grid for the first time
		initGrid();
	}

	private static void initGrid() {
		for (int y = 0; y < yRange; y++)
			for (int x = 0; x < xRange; x++)
				if (x == 0 || x == xRange - 1 || y == 0 || y == yRange - 1)
					createLabel(x, y, Color.DARK_GRAY);
				else if (x == startX && y == startY)
					createLabel(x, y, Color.GREEN);
				else if (x == endX && y == endY)
					createLabel(x, y, Color.RED);
				else
					createLabel(x, y, Color.WHITE);
	}

	private static void createLabel(int x, int y, Color color) {
		grid[y][x] = new NodeJLabel();
		grid[y][x].setBackground(color);
		grid[y][x].setOpaque(true);
		frame.getContentPane().add(grid[y][x]);
	}

	private static List<Node> grabBlockedNodes() {
		List<Node> blockedNodes = new ArrayList<Node>();

		// for each JLabel, if the color is black, then add to list

		for (int y = 0; y < yRange; y++)
			for (int x = 0; x < xRange; x++)
				if (grid[y][x].getBackground() == Color.DARK_GRAY || grid[y][x].getBackground() == Color.BLACK)
					blockedNodes.add(new Node(x, y, null, null));

		return blockedNodes;
	}
	
	/*private static void drawPath(List<Node> list) {
		int x = 0;
		int y = 0;
		
			
		
		for (Node node : list) 
			
	}*/

	private static void calculateMaze(PathFinder maze) {
		while (!maze.getCurrentNode().hasSameCoords(maze.getEndNode())) {
			maze.findNeighborNodes();
			maze.nextNode();
		}

		maze.createPath(maze.getCurrentNode());
	}

	private static void printMaze(PathFinder maze) {
		System.out.println();
		maze.printLists();

		System.out.println();
		maze.printPath();
	}
}
