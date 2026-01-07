import java.util.List;
import java.util.Random;


/**
 * Run one run of the algorithm
 *
 * This is not really used in any meaningful way in this, you can disregard this class entirely.
 * <p>
 * <p>
 * Just run this with intellij. This runs for all the param set defined below, and writes to ./results/
 * For interpreting the data it produces, refer to the report, it has a section for how to interpret the data
 * You will need to manually specify which dataset to use. including the bottom line of code
 * <p>
 * NOTE: this one creates a run with a run number of 999. You may run this, but it writes to results folder too, which may interfere with the GraphHelper.
 * You can delete the results and regenerate with RunAll.
 */
public class RunSingle {


	private static final int POP_SIZE = 50;
	private static final int MAX_GENERATIONS = 10000;

	/**
	 * Test all params for each data set
	 * <p>
	 * then do the next data set
	 *
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {

		float cross_Rate = 0.92f;
		float mutateRate = 0.8f;
		/**
		 *  for i in param list.... do:
		 *
		 *
		 */
		System.out.println("Hewwo world!\n Reading text file...");

		List[] t1Data = Utils.readTextFile("./3p71_ga/t2");
		List[] t2Data = Utils.readTextFile("./3p71_ga/t2");


		Main t1 = new Main("Cross (uniform) " + cross_Rate + ". Mutation " + mutateRate + "\n",
			POP_SIZE, // population size
			MAX_GENERATIONS, // max generations
			mutateRate, // mutation rate
			cross_Rate, // crossover rate
			false, // true to use uniform cross, false to use 2 point cross
			t1Data[0], t1Data[1], t1Data[2], new Random(7201105701102L));

		t1.startAlgorithm();
		Utils.writeOutputToFile("Dataset t1", 999, t1.output);
	}
}
