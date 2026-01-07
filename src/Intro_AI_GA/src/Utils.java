import Model.Course;
import Model.Room;
import Model.Timeslot;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Utils {

	/**
	 * Read the text file data
	 * <p>
	 * Assumes that the data is not malformed and all files exist.
	 */
	public static List[] readTextFile(String dataSetPath) throws Exception {

		ArrayList<Timeslot> allTimeslots = new ArrayList<>();
		ArrayList<Course> allCourses = new ArrayList<>();
		ArrayList<Room> allRooms = new ArrayList<>();

		// read all Timeslots
		FileReader file = new FileReader(dataSetPath + "/timeslots.txt");
		BufferedReader reader = new BufferedReader(file);

		String line = "";
		reader.readLine(); // yeet header
		while ((line = reader.readLine()) != null) {
//			System.out.println(line);
			String[] parts = line.split(",");
			allTimeslots.add(new Timeslot(parts[0], Integer.parseInt(parts[1])));
		}
		file.close();
		reader.close();


		// read course
		file = new FileReader(dataSetPath + "/courses.txt");
		reader = new BufferedReader(file);

		reader.readLine(); // yeet header
		while ((line = reader.readLine()) != null) {
//			System.out.println(line);
			String[] parts = line.split(",");
			if (parts.length < 4) { // invalid line
				continue;
			}
			allCourses.add(new Course(parts[0], parts[1], Integer.parseInt(parts[2]), Integer.parseInt(parts[3])));
		}
		file.close();
		reader.close();


		// read rooms
		file = new FileReader(dataSetPath + "/rooms.txt");
		reader = new BufferedReader(file);
		reader.readLine(); // yeet header
		while ((line = reader.readLine()) != null) {
//			System.out.println(line);
			String[] parts = line.split(",");
			allRooms.add(new Room(parts[0], Integer.parseInt(parts[1])));
		}
		file.close();
		reader.close();


		return new ArrayList[]{allRooms, allCourses, allTimeslots};
	}


	/**
	 * -------------------
	 * Array stuff
	 * -------------------
	 */

	/**
	 * Create a blank schedule matrix initialized with all 0
	 *
	 * @return
	 */
	public static int[][] genBlankSchedule() {
		int[][] roomUsg = new int[7][24]; // 7 days 24 hours
		for (int i = 0; i < roomUsg.length; i++) {
			Arrays.fill(roomUsg[i], 0);
		}

		return roomUsg;
	}


	public static void writeOutputToFile(String datasetLabel, int run, StringBuilder output) throws IOException {
		String result = output.toString();

		// write results
//		long currentTime = new Date().getTime();

		String dataset = output.substring(0, output.indexOf("\n"));

		String path = "./results/" + datasetLabel + "/" + dataset + "/" + run + "/";
		Files.createDirectories(Paths.get(path));
		BufferedWriter solution = new BufferedWriter(new FileWriter(path + "/solution.txt"));
		BufferedWriter graphData = new BufferedWriter(new FileWriter(path + "/graph.csv"));


//		System.out.println(output);
		result = result.substring(result.indexOf("\n"));


		Arrays.stream(result.split("\n")).forEach(line -> {
			if (line.startsWith("&")) {
				// data point, print to csv
				try {
					graphData.write(line.substring(1) + "\n");
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}
			else {
				// stuff to print to solution file
				try {
					solution.write(line + "\n");
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}
		});


		solution.close();
		graphData.close();
	}


	/**
	 * Print in monospace
	 *
	 * This was used for debugging, not used now
	 *
	 * @param board
	 */
	public static void print2DArray(int[][] board) throws Exception {
		System.out.println("----------");

		// print time header
		System.out.printf("%-3s", "");
		System.out.printf("%-3s", "\t");
		for (int i = 0; i < board[0].length; i++) {
			if (i < 8) {
				System.out.printf("%-3c", '-');
			}
			else {
				System.out.printf("%-3d", i);
			}
		}
		System.out.println();

		for (int i = 0; i < board.length; i++) {
			if (i % 2 == 0) {
				System.out.println();
			}
			// print day of week
			System.out.printf("%-3s", Timeslot.intDayToChar(i + 1));
			System.out.printf("%-3s", "\t");

			// print schedule for that day
			for (int j = 0; j < board[i].length; j++) {
				System.out.printf("%-3d", board[i][j]);
			}

			// print day of week
			System.out.printf("%-3s", "\t");
			System.out.printf("%-3s", Timeslot.intDayToChar(i + 1));

			System.out.println();
		}
		System.out.println("----------");
	}


}
