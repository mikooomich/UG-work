import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;


/**
 * <p>
 * To run this, refer to instructions in main method
 */
public class Main {

	public static List<int[][]> gameBoards = new ArrayList<>(); // all boards in file
	public static int BLANK = Integer.MAX_VALUE; // we want this at the end, and so the solution/goal is calculated properly via sort().

	// for both DFS and A*
	private int[][] currentGame;
	private int[][] goal; // aka target
	private Node answer;
	private Heuristic heuristic;


	// for A*
	private PriorityQueue<Node> pathQueue = new PriorityQueue<>();

	public Main(int selected, Heuristic heuristic) {
		currentGame = gameBoards.get(selected);
		this.heuristic = heuristic;

		int[] oneD = twoDtoOneD(currentGame);
		Arrays.sort(oneD);

		int[][] twoD = oneDtoTwoD(oneD);
		goal = twoD;
	}


	/**
	 * Hi. To use this:
	 * <p>
	 * java <program> <heuristic> <puzzle number>
	 * ex. java <program> 1 0
	 * <p>
	 * in the input.txt, there are the problems provided by the assignment, select with indexes 0-3
	 * <p>
	 * Possible heuristics:
	 * 0 -> ID DFS
	 * 1 -> A* Number of tiles out of place
	 * 2 -> A* Total distances of tiles from the goal
	 * 3 -> A* "CUSTOM_LINEAR_CONFLICT, //  for "A* with a heuristic of your design/research"
	 * <p>
	 * Should you wish to bypass args, specify Heuristic heuristic and int puzzleNumber manually below
	 */
	public static void main(String[] args) throws Exception {
		System.out.println("Hewwo world!");
		System.out.println("Specify args.>");
		System.out.println("java <program> <heuristic> <puzzle number>");
		System.out.println("ex. java <program> 1 0");
		System.out.println("This will run with heuristic 1, and puzzle 0");
		System.out.println("Possible heuristics:\n\n0 -> ID DFS\n1 -> A* Number of tiles out of place\n2 -> A* Total distances of tiles from the goal\n3 -> Linear Conflict");
		readTextFile();


//		Heuristic heuristic = Heuristic.NUM_OUT_OF_PLACE; // for if want manual specify
		Heuristic heuristic = switch (Integer.parseInt(args[0])) {
			case 0 -> Heuristic.NONE;
			case 1 -> Heuristic.NUM_OUT_OF_PLACE;
			case 2 -> Heuristic.DIST_FROM_GOAL_MANHATTAN;
			case 3 -> Heuristic.CUSTOM_LINEAR_CONFLICT;
			default -> {
				System.out.println("WARNING!!!!!!!!!!!!!!!!! Invalid heuristic, defaulting on DFS");
				yield Heuristic.NONE;
			}
		};

//		int puzzleNumber = 0; // for if want manual specify
		int puzzleNumber = Integer.parseInt(args[1]); // which board to load, change this number. Zero indexed.
		int max_depth = 999; // for dfs only





		if (heuristic == Heuristic.NONE) {
			// Iterative Deepening Depth First Search
			Main iddfsThingy = new Main(puzzleNumber, heuristic);
			System.out.println("\n\n\n\n\n-----------Iterative Deepening Depth First Search-----------");
//		System.out.println("The goal is: ");
//		print1DArray(iddfsThingy.goal);

			System.out.println("Running search on the following board");
			print2DArray(iddfsThingy.currentGame);
			System.out.println("running...");
			Date start = new Date();
			boolean found = iddfsThingy.iterativeDeepeningDfs(new Node(iddfsThingy.currentGame, null), max_depth);
			Date end = new Date();
			if (found) {
				iddfsThingy.printResult();
			}
			else {
				System.out.println("Not found");
			}

			long timeInMs = end.getTime() - start.getTime();
			System.out.println("Time elapsed in milliseconds: " + timeInMs);
			System.out.println("In seconds: " + ((double) timeInMs / 1000));
			System.out.println("In minutes: " + ((double) timeInMs / 1000 / 60));
		}
		else {
			// A*
			Main astar = new Main(puzzleNumber, heuristic);
			System.out.println("\n\n\n\n\n-----------A* Search-----------");
			System.out.println("Heuristic is: " + heuristic);
//		System.out.println("The goal is: ");
//		print1DArray(astar.goal);

			System.out.println("Running search on the following board");
			print2DArray(astar.currentGame);
			System.out.println("running...");

			// A*
			Date start = new Date();
			astar.aStar(new Node(astar.currentGame, null));
			Date end = new Date();

			if (astar.answer != null) {
				astar.printResult();
			}
			else {
				System.out.println("Not found");
			}

			long timeInMs = end.getTime() - start.getTime();
			System.out.println("Time elapsed in milliseconds: " + timeInMs);
			System.out.println("In seconds: " + ((double) timeInMs / 1000));
			System.out.println("In minutes: " + ((double) timeInMs / 1000 / 60));
		}
	}


	/**
	 * Searching implementations
	 */

	/**
	 * ID-DFS, based on pseudocode provided in class
	 *
	 * @param node
	 * @param depth
	 * @return
	 */
	private boolean iterativeDeepeningDfs(Node node, int depth) {
		for (int i = 0; i < depth; i++) {
			boolean result = dfs(node, i);
			if (result) {
				return true;
			}
		}

		return false;
	}

	/**
	 * DFS, based on pseudocode provided in class
	 *
	 * @param node
	 * @param depth
	 * @return
	 */
	private boolean dfs(Node node, int depth) {
//		print2DArray(node.board);
		if (depth <= 0) {
			if (compareArray(node.board, goal)) {
				return true;
			}
			else {
				return false;
			}
		}

		// find empty square
		int[] coords = findEmptyTile(node.board);
		int emptyX = coords[0];
		int emptyY = coords[1];

		// get possible moves aka children
		List<Integer> possibleMoves = getPossibleMoves(emptyX, emptyY, node.board); // x and y come in pairs, so index 0,1 is a pair, 2,3 is a pair, etc

		int i = 0;
		while (i < possibleMoves.size()) {
			int tilex = possibleMoves.get(i);
			int tiley = possibleMoves.get(i + 1);
			i += 2;

			// apply move, this is the child
			Node nextNode = new Node(copyBoard(node.board), node);
			swap(nextNode.board, emptyX, emptyY, tilex, tiley);

			// do dfs on child
			boolean result = dfs(nextNode, depth - 1);
			if (result) {
				if (answer == null) {
					answer = nextNode;
				}
				return true;
			}
		}

		return false;
	}


	/**
	 * A* search. Kinda based on pseudocode provided in class
	 *
	 * @param node
	 * @throws Exception
	 */
	private void aStar(Node node) throws Exception {
		node.cost = 0;
		pathQueue.add(node);

		while (!pathQueue.isEmpty()) {
			Node currNode = pathQueue.remove();

			if (compareArray(currNode.board, goal)) { // found, stop searching
				if (answer == null) {
					answer = currNode;
					pathQueue.clear();
				}
				return;
			}


			// find empty square
			int[] coords = findEmptyTile(currNode.board);
			int emptyX = coords[0];
			int emptyY = coords[1];


			// get possible moves aka children
			List<Integer> possibleMoves = getPossibleMoves(emptyX, emptyY, currNode.board); // x and y come in pairs, so index 0,1 is a pair, 2,3 is a pair, etc

			int i = 0;
			// for all children
			while (i < possibleMoves.size()) {
				int tilex = possibleMoves.get(i);
				int tiley = possibleMoves.get(i + 1);
				i += 2;

				// apply move, this is the child
				Node nextNode = new Node(copyBoard(currNode.board), currNode);
				swap(nextNode.board, emptyX, emptyY, tilex, tiley);
				nextNode.cost = currNode.cost + 1;
				nextNode.priority = currNode.cost + 1 + heuristic(nextNode);


				// if child not in path, add
				boolean dupe = pathQueue.stream().anyMatch(n -> compareArray(nextNode.board, n.board));
				if (!dupe || nextNode.cost < currNode.cost) {
					pathQueue.add(nextNode);
				}
			}
		}
	}


	/**
	 * Heuristic function h(x)
	 *
	 * @param node
	 * @return
	 */
	private int heuristic(Node node) throws Exception {
		int[][] nodeBoard = node.board;

		switch (heuristic) {
			case DIST_FROM_GOAL_MANHATTAN -> {
				/**
				 * total distance from goal state
				 */

				int distance = 0;
				for (int i = 0; i < goal.length; i++) {
					for (int j = 0; j < goal.length; j++) {
						if (goal[i][j] != nodeBoard[i][j]) {
							// coords are x/y, not i/j
							// only travel in vertical and horizontal, no diagonals. All this does is take the difference between x's and y's and add them
							int[] goalCoords = findTile(goal, nodeBoard[i][j]);
							distance += Math.abs(j - goalCoords[0]) + Math.abs(i - goalCoords[1]);
						}
					}
				}

				return distance;
			}
			case NUM_OUT_OF_PLACE -> {
				/**
				 * number of stuff out of place
				 */
				int anomolies = 0;
				// compare each tile to the goal
				for (int i = 0; i < goal.length; i++) {
					for (int j = 0; j < goal.length; j++) {
						if (goal[i][j] != nodeBoard[i][j]) {
							anomolies++;
						}
					}
				}
				return anomolies;
			}
			case CUSTOM_LINEAR_CONFLICT -> {
				/**
				 * linear conflict
				 *
				 * All the examples and places explaining it only really show examples of when only 2 tiles need to be
				 * swapped, and both are in each other’s goal position. Ex. 3,2,1 or 7,9,8. It’s not really clear for
				 * example a row with 4,3,2,1. 3/2 is one pair, and 4/1 is another, but what about 4 and 3? 4 and 2? Etc.
				 *
				 *
				 * I will assume the definition is the correct method. So thus 4,3,2,1 has the following conflicts:
				 * 4 and 3
				 * 4 and 2
				 * 4 and 1
				 * 3 and 2
				 * 3 and 1
				 * 2 and 1
				 */

				// get manhatten first
				int distance = 0;
				for (int i = 0; i < goal.length; i++) {
					for (int j = 0; j < goal.length; j++) {
						if (goal[i][j] != nodeBoard[i][j]) {
							// coords are x/y, not i/j
							// only travel in vertical and horizontal, no diagonals. All this does is take the difference between x's and y's and add them
							int[] goalCoords = findTile(goal, nodeBoard[i][j]);
							distance += Math.abs(j - goalCoords[0]) + Math.abs(i - goalCoords[1]);
						}
					}
				}

				int conflicts = 0;
				// linear conflict for rows
				for (int i = 0; i < goal.length; i++) {
					List<Integer> candidates = new ArrayList<>(); // stuff that is in the correct row but not correct column
					// get all tiles that are supposed to be in this row but not correct column
					for (int k = 0; k < goal.length; k++) {
						int finalI = i;
						int finalK = k;
						if (Arrays.stream(goal[i]).anyMatch(n -> n == nodeBoard[finalI][finalK] && n != goal[finalI][finalK])) {
							candidates.add(nodeBoard[i][k]);
						}
					}


					// we know that the solution always is least to greatest, so lower index's value is lower than all higher index's value, else they need to be swapped
					while (candidates.size() > 1) { // if one candidate, there is no possible way it is reversed with someone else
						int currentCandidate = candidates.removeFirst();

						// check currentCandidate with remaining candidates
						for (int k = 0; k < candidates.size(); k++) {
							if (currentCandidate > candidates.get(k)) {
								conflicts++;
//								System.out.println("WE FOUND ONE " + currentCandidate + "----" + candidates.get(k));
							}
						}
					}
				}


				// now do this linear conflict nightmare again for column
				for (int i = 0; i < goal.length; i++) {
					List<Integer> candidates = new ArrayList<>(); // stuff that is in the correct col but not correct row
					// get all tiles that are supposed to be in this col but not correct row

					for (int k = 0; k < goal.length; k++) {// for all elements in col
						int finalI = i;
						int finalK = k;

						// get columns to compare against
						int[] nodeBoardCol = new int[goal.length];
						int[] goalCol = new int[goal.length];
						for (int j = 0; j < goal.length; j++) {
							goalCol[j] = goal[j][i];
							nodeBoardCol[j] = nodeBoard[j][i];
						}

						// I know this entire segment is ugly, I'm sorry, my brain is fired, but I promise you it works
						if (Arrays.stream(goalCol).anyMatch(n -> n == nodeBoard[finalK][finalI] && n != goal[finalK][finalI])) {
							candidates.add(nodeBoard[k][i]);
						}
					}


					// we know that the solution always is least to greatest, so lower index's value is lower than all higher index's value, else they need to be swapped
					while (candidates.size() > 1) { // if one candidate, there is no possible way it is reversed with someone else
						int currentCandidate = candidates.removeFirst();

						// check currentCandidate with remaining candidates
						for (int k = 0; k < candidates.size(); k++) {
							if (currentCandidate > candidates.get(k)) {
								conflicts++;
//								System.out.println("WE FOUND ONE " + currentCandidate + "----" + candidates.get(k));
							}
						}
					}
				}
				return (distance + (conflicts));
			}
		}


		throw new Exception("Dont support that heuristic");
	}


	/**
	 * Sliding tile specific helpers
	 */

	/**
	 * Print steps to reach goal
	 */
	private void printResult() {
		System.out.println("THE ANSWER HAS BEEN FOUND");
		int steps = -1; // we have all nodes including the initial state (which is step 0). Account for that
		// print out all the nodes
		Node weh = answer;

		do {
			print2DArray(weh.board);
			steps++;
		} while ((weh = weh.previous) != null);
		System.out.println("The printout has the result at the top, and the initial state at the bottom. Scroll up to reach the goal state.");
		System.out.println("Done in " + steps + " steps");
	}


	/**
	 * Get coordinates of empty tile
	 *
	 * @param board
	 * @return
	 */
	private int[] findEmptyTile(int[][] board) {
		return findTile(board, BLANK);
	}

	/**
	 * Get coordinates of tile
	 *
	 * @param board
	 * @param thingy
	 * @return
	 */
	private int[] findTile(int[][] board, int thingy) {
		int[] coords = new int[2];

		outer:
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++) {
				if (board[i][j] == thingy) {
					coords[0] = j;
					coords[1] = i;
					break outer;
				}
			}
		}
		return coords;
	}

	/**
	 * Looks around the empty tile for non-out-of-bounds children tiles to explore
	 * <p>
	 * In order of left, up, right, down
	 */
	private List<Integer> getPossibleMoves(int emptyX, int emptyY, int[][] board) {
//		System.out.println("doing " + emptyX + "ddd" + emptyY);
		List<Integer> possibleMoves = new ArrayList<>(); // x and y come in pairs, so indexes 0,1 is a pair, 2,3 is a pair, etc

		if (emptyX - 1 >= 0) { // left
			possibleMoves.add(emptyX - 1);
			possibleMoves.add(emptyY);
		}
		if (emptyY - 1 >= 0) { // up
			possibleMoves.add(emptyX);
			possibleMoves.add(emptyY - 1);
		}
		if (emptyX + 1 < board[emptyY].length) { // right
			possibleMoves.add(emptyX + 1);
			possibleMoves.add(emptyY);
		}
		if (emptyY + 1 < board.length) { // down
			possibleMoves.add(emptyX);
			possibleMoves.add(emptyY + 1);
		}
		return possibleMoves;
	}

	/**
	 * Swap empty and child tiles.
	 * DOES NOT COPY!!! MAKES CHANGES DIRECTLY
	 */
	private void swap(int[][] board, int emptyX, int emptyY, int childX, int childY) {
//		System.out.println("swapping " + emptyX + ", " + emptyY + "and    " + childX + "," + childY);
		board[emptyY][emptyX] = board[childY][childX];
		board[childY][childX] = BLANK;
	}


	/**
	 * Read the text file data
	 * <p>
	 * Assumes that the data is not malformed. Assumes that this is a "square" dimension board
	 *
	 * @throws IOException
	 */
	private static void readTextFile() throws IOException {
		FileReader file = new FileReader("./input.txt");
		BufferedReader reader = new BufferedReader(file);


		int[][] tempBoard = new int[0][0];
		int row = 0;

		String line = "";

		// for each line, split by tab character, then write to the array board thing.
		while ((line = reader.readLine()) != null) {
//			System.out.println(line);
			if (!line.contains("\t") && !line.isBlank()) {
				row = 0;
				if (tempBoard.length > 0) {
					gameBoards.add(tempBoard);
				}
				tempBoard = new int[Integer.parseInt(line)][Integer.parseInt(line)];
			}
			else {
				if (!line.isBlank()) {
					String[] stuff = line.split("\t");
					int i = 0;
					for (String s : stuff) {
						if (s.compareTo("X") == 0) {
							tempBoard[row][i] = BLANK;
						}
						else {
							tempBoard[row][i] = Integer.parseInt(s);
						}
						i++;
					}
					row++;
				}
			}
		}

		// flush
		if (tempBoard.length > 0) {
			gameBoards.add(tempBoard);
		}
	}


	/**
	 * Print, copy, misc array stuff
	 */


	/**
	 * Create a new copy of the board
	 */
	private static int[][] copyBoard(int[][] board) {
		int[][] result = new int[board.length][board.length];
		for (int i = 0; i < board.length; i++) {
			System.arraycopy(board[i], 0, result[i], 0, board[i].length);
		}
		return result;
	}


	/**
	 * Returns true if board contents are exact same (aka goal test)
	 *
	 * @param one
	 * @param two
	 * @return
	 */
	private static boolean compareArray(int[][] one, int[][] two) {
		for (int i = 0; i < one.length; i++) {
			for (int j = 0; j < two[i].length; j++) {
				if (!(one[i][j] == two[i][j])) {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * Flatten array, mostly used to compare against the goal
	 *
	 * @param twoD
	 * @return
	 */
	private int[] twoDtoOneD(int[][] twoD) {
		int[] flatArray = new int[twoD.length * twoD.length];
		int index = 0;
		for (int i = 0; i < twoD.length; i++) {
			for (int j = 0; j < twoD[i].length; j++) {
				flatArray[index++] = twoD[i][j];
			}
		}
		return flatArray;
	}

	/**
	 * Convert 1D array to a 2D board. assume no edge cases and boards can convert perfectly...
	 *
	 * @param oneD
	 * @return
	 */
	private int[][] oneDtoTwoD(int[] oneD) {
		int[][] twoD = new int[currentGame.length][currentGame.length];
		int count = 0;
		for (int i = 0; i < twoD.length; i++) {
			for (int j = 0; j < twoD.length; j++) {
				twoD[i][j] = oneD[count];
				count++;
			}
		}
		return twoD;
	}

	/**
	 * Print in monospace
	 *
	 * @param board
	 */
	public static void print2DArray(int[][] board) {
		System.out.println("----------");
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++) {
				if (board[i][j] == BLANK) {
					System.out.printf("%-3c", 'X');
				}
				else {
					System.out.printf("%-3d", board[i][j]);
				}
			}
			System.out.println();
		}
		System.out.println("----------");
	}

	/**
	 * Print in monospace
	 *
	 * @param board
	 */
	public static void print1DArray(int[] board) {
		System.out.println("----------");
		for (int i = 0; i < board.length; i++) {
			if (board[i] == BLANK) {
				System.out.printf("%-3c", 'X');
			}
			else {
				System.out.printf("%-3d", board[i]);
			}
		}
		System.out.println("----------");
	}
}