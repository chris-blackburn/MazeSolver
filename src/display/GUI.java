package display;

import java.awt.Color;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;

import aStar.Node;
import aStar.PathFinder;

import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;

public class GUI {

	private static JFrame frame;

	// add two to account for border
	private static int xRange = 25 + 2;
	private static int yRange = 25 + 2;

	private static int startX = 1;
	private static int startY = 1;
	private static int endX = xRange - 2;
	private static int endY = yRange - 2;

	// grid[y][x] because 2D arrays are oriented by rows of separate arrays
	private static NodeJLabel[][] grid;

	private static PathFinder maze;

	private static boolean picker = false;

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
		frame.setResizable(false);
		frame.setTitle("Maze Solver");
		frame.setBounds(100, 100, 750, 750);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		frame.getContentPane().setLayout(new GridLayout(yRange, xRange, 1, 1));

		JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);

		JMenu mnNewMenu = new JMenu("Maze");
		menuBar.add(mnNewMenu);

		JMenuItem mntmSolve = new JMenuItem("Solve");
		mntmSolve.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				resetPath();
				maze = new PathFinder(startX, startY, endX, endY, grabBlockedNodes());
				calculateMaze(maze);
				drawOpen(maze);
				drawClosed(maze);
				drawPath(maze);

				// reset start and end nodes
				updateLabel(startX, startY, Color.MAGENTA);
				updateLabel(endX, endY, Color.MAGENTA);
			}
		});
		mnNewMenu.add(mntmSolve);

		JMenuItem mntmReset = new JMenuItem("Reset");
		mntmReset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				resetGrid();
			}
		});
		mnNewMenu.add(mntmReset);

		JMenu mnOptions = new JMenu("Options");
		menuBar.add(mnOptions);

		JMenu mnDraw = new JMenu("Draw");
		mnOptions.add(mnDraw);

		JMenuItem mntmSetBounds = new JMenuItem("Set Bounds");
		mntmSetBounds.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.dispose();
				popupSetBounds();
				startX = 1;
				startY = 1;
				endX = xRange - 2;
				endY = yRange - 2;
				main(null);

			}
		});
		mnDraw.add(mntmSetBounds);

		JMenuItem mntmSetStart = new JMenuItem("Set Start");
		mntmSetStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				updateLabel(startX, startY, Color.WHITE);
				picker = true;
				// startX = ;
				// startY = ;
				picker = false;
			}
		});
		mnDraw.add(mntmSetStart);

		JMenuItem mntmSetEnd = new JMenuItem("Set End");
		mntmSetEnd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				updateLabel(endX, endY, Color.WHITE);
				picker = true;
				// endX = ;
				// endY = ;
				picker = false;
			}
		});
		mnDraw.add(mntmSetEnd);

		JMenuItem mntmSolvePicture = new JMenuItem("Map Picture");
		mnOptions.add(mntmSolvePicture);

		// initialize grid size
		grid = new NodeJLabel[yRange][xRange];

		// draw the grid for the first time
		initGrid();
	}

	public static boolean canChooseNewEnds() {
		return picker;
	}

	private static void popupSetBounds() {
		JPanel panel = new JPanel();

		SpinnerModel modelW = new SpinnerNumberModel(25, 2, 100, 1);
		JSpinner width = new JSpinner(modelW);
		SpinnerModel modelH = new SpinnerNumberModel(25, 2, 100, 1);
		JSpinner height = new JSpinner(modelH);

		panel.add(new JLabel("Width: "));
		panel.add(width);
		panel.add(new JLabel("Height: "));
		panel.add(height);

		int result = JOptionPane.showConfirmDialog(null, panel, "Input Width and Height", JOptionPane.OK_CANCEL_OPTION);

		if (result == JOptionPane.OK_OPTION) {
			xRange = (int) width.getValue() + 2;
			yRange = (int) height.getValue() + 2;
		}
	}

	private static void initGrid() {
		for (int y = 0; y < yRange; y++)
			for (int x = 0; x < xRange; x++)
				if (x == 0 || x == xRange - 1 || y == 0 || y == yRange - 1)
					createLabel(x, y, Color.DARK_GRAY);
				else if (x == startX && y == startY)
					createLabel(x, y, Color.MAGENTA);
				else if (x == endX && y == endY)
					createLabel(x, y, Color.MAGENTA);
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

	private static void drawPath(PathFinder maze) {
		int x, y;

		// Draw the path
		for (Node node : maze.getPathList()) {
			x = node.getX();
			y = node.getY();
			updateLabel(x, y, Color.CYAN);
		}
	}

	private static void drawOpen(PathFinder maze) {
		int x, y;

		for (Node node : maze.getOpen()) {
			x = node.getX();
			y = node.getY();
			updateLabel(x, y, Color.GREEN);
		}
	}

	private static void drawClosed(PathFinder maze) {
		int x, y;

		for (Node node : maze.getClosed()) {
			if (node.get_fCost() != 0) {
				x = node.getX();
				y = node.getY();
				updateLabel(x, y, Color.RED);
			}
		}
	}

	private static void updateLabel(int x, int y, Color color) {
		grid[y][x].setBackground(color);
	}

	private static void calculateMaze(PathFinder maze) {
		while (!maze.getCurrentNode().hasSameCoords(maze.getEndNode())) {
			maze.findNeighborNodes();
			maze.nextNode();
			// updateLabel(maze.getCurrentNode().getX(),
			// maze.getCurrentNode().getY(), Color.RED);
		}

		maze.createPath(maze.getCurrentNode());
	}

	private static void resetGrid() {
		for (int y = 0; y < yRange; y++)
			for (int x = 0; x < xRange; x++)
				if (x == 0 || x == xRange - 1 || y == 0 || y == yRange - 1)
					updateLabel(x, y, Color.DARK_GRAY);
				else if (x == startX && y == startY)
					updateLabel(x, y, Color.MAGENTA);
				else if (x == endX && y == endY)
					updateLabel(x, y, Color.MAGENTA);
				else
					updateLabel(x, y, Color.WHITE);
	}

	private static void resetPath() {
		for (int y = 0; y < yRange; y++)
			for (int x = 0; x < xRange; x++)
				if (grid[y][x].getBackground() != Color.DARK_GRAY && grid[y][x].getBackground() != Color.BLACK)
					if ((x == startX && y == startY) || (x == endX && y == endY))
						updateLabel(x, y, Color.CYAN);
					else
						updateLabel(x, y, Color.WHITE);
	}

	private static void printMaze(PathFinder maze) {
		System.out.println();
		maze.printLists();

		System.out.println();
		maze.printPath();
	}
}
