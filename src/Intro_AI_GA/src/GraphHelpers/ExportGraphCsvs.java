package GraphHelpers;

import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashMap;
import java.util.Map;

/**
 * This is mainly hax to make my life easier to graph in excel
 */
public class ExportGraphCsvs {

	public static void main(String[] args) {
		// mutation for all crossover rates it shares
		getAllDataForMutationRate("0.1", "./results/dataset t2");
//		getAllDataForMutationRate("0.8", "./results/dataset t2");
//		getAllDataForMutationRate("0.0", "./results/dataset t2");
//
//		getAllDataForMutationRate("0.1", "./results/dataset t1");
//		getAllDataForMutationRate("0.8", "./results/dataset t1");
//		getAllDataForMutationRate("0.0", "./results/dataset t1");


		// mutation for all crossover rates it shares
//		getAllDataForCrossRate("0.92", "./results/dataset t2");
//		getAllDataForCrossRate("0.9", "./results/dataset t2");
//		getAllDataForCrossRate("1.0", "./results/dataset t2");
//
//		getAllDataForCrossRate("0.92", "./results/dataset t1");
//		getAllDataForCrossRate("0.9", "./results/dataset t1");
//		getAllDataForCrossRate("1.0", "./results/dataset t1");
	}


	/**
	 * Create new csv files with aggregated data. Pulls together all data with the defined mutation rate
	 *
	 * @param mutationRate
	 * @param path
	 */
	public static void getAllDataForMutationRate(String mutationRate, String path) {
		Map<String, Integer> fileCountByCrossRate = new HashMap<>(); // mutation paired with # of files
		try {
			// get all the csv files, including subdirs. create new csv file for each one
			Files.walkFileTree(Paths.get(path), new SimpleFileVisitor<Path>() {
				@Override
				public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
					if (file.toString().endsWith(".csv") && file.toString().contains("Mutation " + mutationRate) && file.toString().contains("(uniform)")) {

						String crossRate = extractCrossRate(file.toString());
						if (crossRate != null) {
							// here, want index the files. Can probably assume that the tests will be processed in order. So just do this dumb numbering
							// ex for mutation 0.0, cross, 1.0, test 1 will be processed first, then test 2... etc.
							int count = fileCountByCrossRate.getOrDefault(crossRate, 0);
							writeNewCSV(file.toFile(), crossRate, mutationRate, count + 1, false);
							fileCountByCrossRate.put(crossRate, count + 1);
						}
					}
					return FileVisitResult.CONTINUE;
				}
			});
			System.out.println("Done");
		} catch (IOException e) {
			System.err.println("Error......... ");
			e.printStackTrace();
		}
	}

	/**
	 * Extract crossover rate from string
	 *
	 * @param filePath
	 * @return
	 */
	private static String extractCrossRate(String filePath) {
		try {
			String[] raw = filePath.split("\\\\");
			String infoString = raw[raw.length - 3]; // get the part of the path that provides info about cross and mutation
			if (!infoString.contains("Cross") || !infoString.contains("Mutation")) {
				throw new Exception("invalid folder. format is ---> Cross (uniform) 0.9. Mutation 0.1");
			}

			// this mess splits by space, and extracts the crossover rate
			String[] parts = infoString.split(" ");
			return parts[2].substring(0, parts[2].length() - 1);
		} catch (Exception e) {
			System.err.println("Error extracting cross rate from file path");
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Write new csv file with data. Added cross rate and mutation rate to the csv data
	 *
	 * @param file
	 * @param crossRate
	 * @param mutationRate
	 * @param fileIndex
	 */
	private static void writeNewCSV(File file, String crossRate, String mutationRate, int fileIndex, boolean trueForCrossover) {
		String outFile;
		if (trueForCrossover) {
			outFile = "AGGREGATED cross MUTATION " + mutationRate + " cross " + crossRate + " file " + fileIndex + ".csv";
		}
		else {
			outFile = "AGGREGATED mutate MUTATION " + mutationRate + " cross " + crossRate + " file " + fileIndex + ".csv";
		}
		try (BufferedReader reader = new BufferedReader(new FileReader(file));
			 BufferedWriter writer = new BufferedWriter(new FileWriter(outFile))) {
			String line;

			// write and read out the header
			writer.write("Cross Rate,Mutation Rate,Generation,Best Fitness,Average Fitness");
			writer.newLine();
			reader.readLine();


			// now aggregate the data and write it in the new format
			while ((line = reader.readLine()) != null) {
				String[] parts = line.split(",");
				if (parts.length >= 3) {
					String generation = parts[0];
					String bestFitness = parts[1];
					String averageFitness = parts[2];

					writer.write(String.format("%s,%s,%s,%s,%s", crossRate, mutationRate, generation, bestFitness, averageFitness));
					writer.newLine();
				}
			}
			System.out.println("Finished: " + file.getAbsolutePath() + " -----------> " + outFile);
		} catch (IOException e) {
			System.err.println("Error reading file " + file.getAbsolutePath());
			e.printStackTrace();
		}
	}


	/**
	 * Create new csv files with aggregated data. Pulls together all data with the defined cross rate
	 * <p>
	 * This is the same code as the above function with minor changes to make it the other way around
	 *
	 * @param cRossRate
	 * @param path
	 */
	public static void getAllDataForCrossRate(String cRossRate, String path) {
		Map<String, Integer> fileCountByCrossRate = new HashMap<>(); // mutation paired with # of files
		try {
			// get all the csv files, including subdirs. create new csv file for each one
			Files.walkFileTree(Paths.get(path), new SimpleFileVisitor<Path>() {
				@Override
				public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
					// yeah... this doesnt work for the other crossover rate but for this part, I am only comparing with uniform cross anyways
					if (file.toString().endsWith(".csv") && file.toString().contains("Cross (uniform) " + cRossRate)) {

						// there is an issue where a value of 0.92 is wrongfully read as 0.9
						// I apologize for how awful this code look... but it works somehow.
						int beginIndex = file.toString().indexOf("Cross (uniform) " + cRossRate);
						String str = "Cross (uniform) ";
						String actualCrossRate = file.toString().substring(
							beginIndex,
							beginIndex + str.length() + 5
						).strip();
						if (actualCrossRate.endsWith(".")) {
							actualCrossRate = actualCrossRate.substring(0, actualCrossRate.length() - 1);
						}

						// do not process the file if this is the wrong rate
						if (actualCrossRate.compareTo("Cross (uniform) " + cRossRate) == 0) {
							String mutateRate = extractMutationRate(file.toString());
							if (mutateRate != null) {
								// here, want index the files. Can probably assume that the tests will be processed in order. So just do this dumb numbering
								// ex for mutation 0.0, cross, 1.0, test 1 will be processed first, then test 2... etc.
								int count = fileCountByCrossRate.getOrDefault(mutateRate, 0);
								writeNewCSV(file.toFile(), cRossRate, mutateRate, count + 1, true);
								fileCountByCrossRate.put(mutateRate, count + 1);
							}
						}
					}
					return FileVisitResult.CONTINUE;
				}
			});
			System.out.println("Done");
		} catch (IOException e) {
			System.err.println("Error......... ");
			e.printStackTrace();
		}
	}

	/**
	 * Extract crossover rate from string
	 *
	 * @param filePath
	 * @return
	 */
	private static String extractMutationRate(String filePath) {
		try {
			String[] raw = filePath.split("\\\\");
			String infoString = raw[raw.length - 3]; // get the part of the path that provides info about cross and mutation
			if (!infoString.contains("Cross") || !infoString.contains("Mutation")) {
				throw new Exception("invalid folder. format is ---> Cross (uniform) 0.9. Mutation 0.1");
			}

			// this mess splits by space, and extracts the crossover rate
			String[] parts = infoString.split(" ");
			return parts[4];
		} catch (Exception e) {
			System.err.println("Error extracting cross rate from file path     " + filePath);
			e.printStackTrace();
			return null;
		}
	}


}

