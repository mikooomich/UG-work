package GraphHelpers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes; /**
 * This gets
 */
public class GetGenerationCount {
	public static final String directoryPath = "./results";

	public static void main(String[] args) throws IOException {
		System.out.println("This is interpreted as:");
		System.out.println("2-point for t1\n...\nuniform for t1\n...\n2-point for t2\n...\nuniform for t2\n...\n");
		System.out.println("cross method, cross, mutation, gen count");
		printGenerationLengths();
	}

	/**
	 * print the amount of generations in each run
	 */
	private static void printGenerationLengths() throws IOException {

		Files.walkFileTree(Paths.get(directoryPath), new SimpleFileVisitor<Path>() {
			@Override
			public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
				if (file.toString().endsWith("graph.csv")) {
					int rowCount = getCSVRowCount(file.toFile());
					System.out.println(formatOutput(file.toString(), rowCount));
				}
				return FileVisitResult.CONTINUE;
			}
		});

	}

	/**
	 * Get generations in this csv file. This is just an rough idea anyways, it wont matter if its off by 1 or 2...
	 * so just count number of lines in file
	 *
	 * @param file
	 * @return
	 */
	private static int getCSVRowCount(File file) {
		int rows = 0;
		try (BufferedReader read = new BufferedReader(new FileReader(file))) {
			while (read.readLine() != null) {
				rows++;
			}
		} catch (IOException e) {
			System.err.println(e.getMessage());
		}
		return rows;
	}

	/**
	 * Format into a readable output
	 *
	 * @param filePath
	 * @param rowCount
	 * @return
	 */
	private static String formatOutput(String filePath, int rowCount) {
		String[] parts = filePath.split("\\\\");
		String crossThing = parts[parts.length - 3];

		// extract values
		String[] crossParts = crossThing.split("\\s");
		String crossVariant = crossParts[1].replace("(", "").replace(")", "");
		String crossVal = crossParts[2].substring(0, crossParts[2].length() - 1);
		String mutationVal = crossParts[4];

		return String.format("%s,%s,%s,%d", crossVariant, crossVal, mutationVal, rowCount);

	}
}
