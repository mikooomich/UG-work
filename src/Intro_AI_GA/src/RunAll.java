import java.io.IOException;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Run all the various combination.
 * 2 crossovers
 * 5 parameters
 * 2 data sets
 * 5 times each
 * = 100 runs
 * <p>
 * <p>
 * Just run this with intellij. This runs for all the param set defined below, and writes to ./results/
 * For interpreting the data it produces, refer to the report, it has a section for how to interpret the data
 */
public class RunAll {

	/**
	 * To save your time and my time, I have implemented semi-asynchronous running of tests.
	 * <p>
	 * This value controls the max number of run jobs that can happen concurrently
	 */
	private static final int MAX_CONCURRENCY = 6;


	private static final int POP_SIZE = 100;
	private static final int MAX_GENERATIONS = 20000;

	/**
	 * Crossover and mutation values, each crossover value is paired with the corresponding mutation value
	 */
	static final float[] cross_Rate = {
		1.0f, 0.9f, 1.0f, 0.9f, // ones we are supposed to use ads defined in assignment
		0.92f // mine
	};
	static final float[] mutateRate = {
		0.0f, 0.0f, 0.1f, 0.1f,
		0.8f
	};

// 2 CROSSOVERS * 5 CROSS/MUTATE PARAMS PAIRS * 2 DATA SETS  * 5 RUNS EACH == 100 runs

	/**
	 * Test all params for each data set
	 * <p>
	 * then do the next data set
	 *
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(MAX_CONCURRENCY);
		System.out.println("Hewwo world!\n Reading text file...");

		List[] t1Data = Utils.readTextFile("./3p71_ga/t1");
		List[] t2Data = Utils.readTextFile("./3p71_ga/t2");

		System.out.println("Running jobs in 2 seconds...");
		Thread.sleep(2000);

		// test all mutation and crossover rate for t1
		for (int i = 0; i < cross_Rate.length; i++) {
			// test these values on both dataset 1 and 2
			executor.submit(new MultiThreadingHax("Dataset t1", t1Data, i));
			executor.submit(new MultiThreadingHax("Dataset t2", t2Data, i));
		}

		executor.shutdown();
	}


	/**
	 * Genetic algorithm run job. Runs each parameter set 5 times
	 */
	static class MultiThreadingHax implements Runnable {
		int crossAndMutateParamIndex;
		String datasetLabel;
		List[] dataset;

		public MultiThreadingHax(String datasetLabel, List[] dataSet, int iterationNumber) {
			this.crossAndMutateParamIndex = iterationNumber;
			this.datasetLabel = datasetLabel;
			this.dataset = dataSet;
		}

		@Override
		public void run() {
			// each test uses a different seed. But all test 1s will use the same seed, all test 2s, etc.
			Random[] rng = {
				new Random(7201105701102L),
				new Random(9282),
				new Random(1),
				new Random(323),
				new Random(999999999),
			};

			// run each one 5 times
			for (int j = 1; j < 6; j++) {
				Main algorithm = new Main(
					"Cross (uniform) " + cross_Rate[crossAndMutateParamIndex] + ". Mutation " + mutateRate[crossAndMutateParamIndex] + "\n",
					POP_SIZE, // population size
					MAX_GENERATIONS, // max generations
					mutateRate[crossAndMutateParamIndex], // mutation rate
					cross_Rate[crossAndMutateParamIndex], // crossover rate
					true, // true to use uniform cross, false to use 2 point cross
					dataset[0], dataset[1], dataset[2],
					rng[j - 1] // random number generator
				);

				algorithm.startAlgorithm();
				try {
					Utils.writeOutputToFile(datasetLabel, j, algorithm.output);
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}


			rng = new Random[]{
				new Random(7201105701102L),
				new Random(9282),
				new Random(1),
				new Random(323),
				new Random(999999999),
			};

			// 5 times again, but with the other crossover
			for (int j = 1; j < 6; j++) {
				Main algorithm = new Main(
					"Cross (2-point) " + cross_Rate[crossAndMutateParamIndex] + ". Mutation " + mutateRate[crossAndMutateParamIndex] + "\n",
					POP_SIZE, // population size
					MAX_GENERATIONS, // max generations
					mutateRate[crossAndMutateParamIndex], // mutation rate
					cross_Rate[crossAndMutateParamIndex], // crossover rate
					false, // true to use uniform cross, false to use 2 point cross
					dataset[0], dataset[1], dataset[2],
					rng[j - 1]
				);

				algorithm.startAlgorithm();
				try {
					Utils.writeOutputToFile(datasetLabel, j, algorithm.output);
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}
		}
	}
}
