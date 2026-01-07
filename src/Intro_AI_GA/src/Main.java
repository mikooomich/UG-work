import Genetics.Chromosome;
import Genetics.Genome;
import Model.*;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * <p>
 * To run this, go to RunAll, or RunSingle
 */
public class Main {

	private static final float ELITE_TOP_PERCENTAGE = 0.1f; // I was told that we would only be doing percentage of 10%
	public static final int PRINTOUT_INCREMENT = 100; // display progress in console, so know it's still running

	// random number generator. each instance of the algorithm will have its own rng function
//	public final Random rng = new Random(7201105701102L);
	public Random rng;


	// THESE ARE SUPPOSED TO BE READ-ONLY
	public final List<Course> allCourses;
	public final List<Timeslot> allTimeslots;
	public final List<Room> allRooms;
	private final String label;


	// parameters
	private final boolean useUniformCross; // true to use uniform cross, false will use 2-point
	private final float crossRate;
	private final float mutationRate;
	private final float maxGenerations;
	private final float maxPopSize;

	public List<Chromosome> population = new ArrayList<>();

	final StringBuilder output;
	boolean foundSolution = false;

	/**
	 * Create a new instance of the algorithm, and do set up. use startAlgorithm() to actually run it
	 *
	 * @param useUniformCross true will use uniform crossover, else it will use the other crossover method, two-point crossover
	 */
	public Main(String label, int popSize, int maxGenerations, float mutationRate, float crossRate, boolean useUniformCross,
				List<Room> ro_rooms, List<Course> ro_course, List<Timeslot> ro_timeslot, Random rng) {
		this.mutationRate = mutationRate;
		this.crossRate = crossRate;
		this.maxGenerations = maxGenerations;
		this.maxPopSize = popSize;
		this.useUniformCross = useUniformCross;

		this.allCourses = ro_course;
		this.allRooms = ro_rooms;
		this.allTimeslots = ro_timeslot;
		this.label = label;
		this.rng = rng;
		output = new StringBuilder(label).append("\n");
		output.append("&generation, best fitness, average fitness\n");
	}

	/**
	 * Initiate the initial populator for genetic algorithm, run the generation loop, then print and save the results
	 */
	public void startAlgorithm() {
//		System.out.println("\n-----------------\nInitiating random population...\n-----------------\n");
		genRandomPop();

		System.out.println("\n-----------------\nRUNNING GENERATION LOOP\n" + label + " \n-----------------\n");
		bornTheNextGeneration();
		System.out.println("\n-----------------\nRUNNING DONE\n" + label + "\n-----------------\n");

		evalAllFitness(population);


		// print best solution
		Chromosome best = findElite(population);
		output.append("Fitness of this solution: " + best.fitness);
		output.append("\n\n");
		best.genesList.forEach(gene -> {
			output.append(gene.course.name).append("\n")
				.append(gene.course.professor).append("\n")
				.append(gene.rm.name).append("\n")
				.append(gene.ts.day).append(": ").append(gene.ts.hour).append("\n")
				.append("\n\n");
		});

//		printChromosome(best); // uncomment to print chromosomes to console
//		System.out.println("fitness for best: " + best.fitness); // uncomment to print the best fitness to console
	}

	/**
	 * The main genetic algorithm loop
	 */
	private void bornTheNextGeneration() {
		System.out.println("Each + or - printout is " + PRINTOUT_INCREMENT + " iterations/generations completed");

		for (int i = 0; i < maxGenerations; i++) {

			// progress printer, not part of algorithm
//			System.out.println(i);
			if (i % PRINTOUT_INCREMENT == 0) {
//				System.out.println("Iteration: " + i);
				//				System.out.println("Pop size " + population.size());
				if (i % (PRINTOUT_INCREMENT * 2) == 0) {
					System.out.print("+");
				}
				else {
					System.out.print("-");
				}
			}

			/**
			 * Steps are in order as defined in instructions of assignment
			 *
			 * For 0..MaxGenerations; do:
			 * 1. Evaluate fitness of individuals in the population
			 * 2. Select a new population using selection strategy
			 * 3. Apply Crossover
			 * 4. Apply mutation
			 * End For
			 *
			 *
			 * In this implementation, it is:
			 * 1. Evaluate fitness of individuals in the population
			 * 2. For remaining population space, do TOURNAMENT SELECTION (crossover is done within here)
			 * 3. Do mutation
			 */
			evalAllFitness(population);

			// write to output. Each generation's values is taken here, before it goes through the breeding phase
			output.append("&").append(i).append(",").append(findElite(population).fitness).append(",").append(getAvgFitness(population)).append("\n");
			if (foundSolution) {
				break; // do not breed because we need to write the solution now
			}

			// do elitism
			List<Chromosome> oldPopulation = population;
			population = new ArrayList<>();

			// copy the top 10% un-altered, then spam tournament selection
			// it was later clarified to only do top 10% for elitism
			population.addAll(elitismTop10Percent(oldPopulation));
			population.addAll(tournamentSelection(population));

//			 mutation
			// do not mutate the first few genes. those are the elite 10%
			int protectElites = (int) Math.floor(population.size() * ELITE_TOP_PERCENTAGE);
			for (int j = protectElites; j < population.size(); j++) {
				if (rng.nextFloat(0f, 1f) < mutationRate) {
					mutate(population.get(j)); // mutate individual
				}
			}
		}
		System.out.println();
	}


	/**
	 * ----------------------------
	 * Crossover and mutation functions
	 * ----------------------------
	 */


	/**
	 * Uniform crossover
	 * <p>
	 * This is where we make children
	 *
	 * Children are added directly to the list provided
	 */
	private void uniformCross(Chromosome mom, Chromosome dad, List<Chromosome> newPop) {
		// empty children
		Chromosome sister = new Chromosome();
		Chromosome brother = new Chromosome();

		// assume mom and dad same chromosome count
		boolean[] bitmask = new boolean[mom.genesList.size()]; // 1 = true, 0 = false
		for (int i = 0; i < bitmask.length; i++) {
			bitmask[i] = rng.nextBoolean();
		}

		for (int i = 0; i < mom.genesList.size(); i++) {
			if (bitmask[i]) {
				// copy directly where 1's
				sister.genesList.add(mom.genesList.get(i));
				brother.genesList.add(dad.genesList.get(i));
			}
			else {
				// copy crossed
				sister.genesList.add(dad.genesList.get(i));
				brother.genesList.add(mom.genesList.get(i));
			}
		}

		sister.fitness = evalFitness(sister);
		brother.fitness = evalFitness(brother);
		newPop.add(sister);
		newPop.add(brother);
	}

	/**
	 * Two point crossover. Generate 2 random indexes. The genes in between those two will get crossover, else it just
	 * directly copies the genes.
	 * <p>
	 * This is where we make children
	 *
	 * Children are added directly to the list provided
	 *
	 * @param mom    parent
	 * @param dad    parent
	 * @param newPop place to add children to
	 */
	private void twoPointCrossover(Chromosome mom, Chromosome dad, List<Chromosome> newPop) {
		// empty children
		Chromosome sister = new Chromosome();
		Chromosome brother = new Chromosome();

		// assume mom and dad same chromosome count
		int[] lines = {
			rng.nextInt(0, mom.genesList.size()),
			rng.nextInt(0, mom.genesList.size())
		};

		// get the index of 2 lines, where line1 >= line2
		int line1;
		int line2;
		if (lines[0] > lines[1]) {
			line1 = lines[1];
			line2 = lines[0];
		}
		else {
			line1 = lines[0];
			line2 = lines[1];
		}

		// crossover
		for (int i = 0; i < mom.genesList.size(); i++) {
			if (i < line1 || i >= line2) {
				// copy directly where out of cross zone
				sister.genesList.add(mom.genesList.get(i));
				brother.genesList.add(dad.genesList.get(i));
			}
			else {
				// copy crossed
				sister.genesList.add(dad.genesList.get(i));
				brother.genesList.add(mom.genesList.get(i));
			}
		}

		sister.fitness = evalFitness(sister);
		brother.fitness = evalFitness(brother);
		newPop.add(sister);
		newPop.add(brother);
	}

	/**
	 * Mutate. Mutate the individual (chromosome)
	 * <p>
	 * DIRECTLY MODIFIES THE THING!
	 *
	 * @param chromosome
	 */
	private void mutate(Chromosome chromosome) {
		// select a random course and throw it into a random room at a random time
		Genome gene = chromosome.genesList.get(rng.nextInt(0, chromosome.genesList.size()));
		// assign a random room at a random time
		gene.rm = allRooms.get(rng.nextInt(0, allRooms.size()));
		gene.ts = allTimeslots.get(rng.nextInt(0, allTimeslots.size()));
	}

	/**
	 * ----------------------------
	 * Calculating fitness
	 * ----------------------------
	 */
	private double getAvgFitness(List<Chromosome> population) {
		double avg = 0d;
		for (Chromosome chromosome : population) {
			avg += chromosome.fitness;
		}

		return avg / population.size();
	}

	/**
	 * Re-evaluate the fitness values for all population members
	 *
	 * @param cohort
	 */
	private void evalAllFitness(List<Chromosome> cohort) {
		cohort.forEach(chromosome -> chromosome.fitness = evalFitness(chromosome));
	}

	/**
	 * Evaluate fitness of a chromosome
	 * <p>
	 * This is somewhat based on the pseudocode in class.
	 * Instead of dynamically adding allRooms and professors to dictionaries, my code just creates schedules for each room
	 * and prof and throws the courses onto the schedules (for each room and prof), then finally count the conflicts
	 *
	 * @return
	 */
	private double evalFitness(Chromosome chromosome) {
		AtomicInteger conflicts = new AtomicInteger();


		/**
		 *
		 * Each room and professor cannot have more than one thing it participates in at a time, thus each has its own
		 * schedule. Now we are going to do a bit of set up
		 *
		 * Each room has its own schedule represented as int[][]. Each row is a day of the week and each column is the hour
		 * 0 means no class is scheduled and a number > 0 means there is a class
		 *
		 * For example:
		 * M: 0 0 0 1 1 0 0 0
		 * T: 0 0 1 1 2 2 1 0
		 * W: ...
		 *
		 * This means there is one 2 hour class on Monday from hour 4 to 5, and two classes on Tuesday. One is 4
		 * hour class first, and there is a second 3 hour class that overlaps with it. This is a 2 hour overlap as seen
		 * there, hence the "2 2"
		 *
		 *
		 * The same thing happens with the professor's schedule
		 */
		Hashtable<Room, int[][]> roomSchedules = new Hashtable<>();
		Hashtable<String, int[][]> profSchedules = new Hashtable<>();

		// add all empty schedules for profs and allRooms
		allRooms.forEach(rm -> roomSchedules.put(rm, Utils.genBlankSchedule()));
		allCourses.forEach(course -> profSchedules.put(course.professor, Utils.genBlankSchedule()));

		// now just fill in the matrix based on what course is schedule where.
		chromosome.genesList.forEach(genome -> {
			// get the schedule matrices for the room/prof this course is scheduled with
			int[][] roomMatrix = roomSchedules.get(genome.rm);
			int[][] profMatrix = profSchedules.get(genome.course.professor);
			int dayOfWeek = genome.ts.day.getValue();
			int time = genome.ts.hour;

			// for each hour that the class runs for...
			for (int i = 0; i < genome.course.duration; i++) {
				// get the timeslot and then throw the course onto the matrix
				roomMatrix[dayOfWeek][time] += 1;
				profMatrix[dayOfWeek][time] += 1;
				time++;
			}

			// check for conflict when if the room can't fit the course
			// since each gene only has one course and based on how genes are generated, it can never have duplicate courses
			if (genome.course.studentCount > genome.rm.studentLimit) {
//				System.out.println( genome.course.studentCount +"  --" + genome.course.name + genome.course.professor + "  ---  " +genome.rm.studentLimit);
				conflicts.addAndGet(2); // pseudocode states conflict is a +2
			}
		});


		// we go sum up all the room/professor conflicts
		// the assignment does not state this code has to be the most optimal and way of doing this... hence this primitive way instead of doing it as courses are filled in
		roomSchedules.forEach((rm, sched) -> {
			for (int i = 0; i < sched.length; i++) {
				for (int j = 0; j < sched[i].length; j++) {
					int score = sched[i][j];
					if (score > 1) {
						conflicts.addAndGet(3); // pseudocode states each conflict is a +3
					}
				}
			}
//				System.out.println("ROOM: " + rm.name);
//				Utils.print2DArray(sched);
		});

		profSchedules.forEach((rm, sched) -> {
			for (int i = 0; i < sched.length; i++) {
				for (int j = 0; j < sched[i].length; j++) {
					int score = sched[i][j];
					if (score > 1) {
						conflicts.addAndGet(3);
					}
				}
			}
		});

//		System.out.println(conflicts.get());
		double fitness = 1d / (1 + conflicts.get()); // provided by pseudocode
		if (fitness >= 1.0) {
			// signal stop if solution is found
			foundSolution = true;
		}
		return fitness;
	}


	/**
	 * ----------------------------
	 * Selection functions
	 * ----------------------------
	 */


	/**
	 * Select the fittest (single) individual in the provided list
	 *
	 * @param roster
	 * @return
	 */
	private Chromosome findElite(List<Chromosome> roster) {
		// find most fit by iterating through whole list
		Chromosome fittest = roster.getFirst();
		for (int i = 1; i < roster.size(); i++) {
			Chromosome temp = roster.get(i);
			if (fittest.fitness < temp.fitness) {
				fittest = temp;
			}
		}

		return fittest;
	}

	/**
	 * Select the top 10% percent of the population.
	 *
	 * Sort the list, then return the last 10%
	 * @param roster
	 * @return
	 */
	private List<Chromosome> elitismTop10Percent(List<Chromosome> roster) {
		int numAllowedToReprod = (int) Math.floor(roster.size() * Main.ELITE_TOP_PERCENTAGE);
		if (numAllowedToReprod <= 0) {
			throw new RuntimeException("The percentage of elites is too low. This will result in zero children being born.");
		}

		roster.sort(Comparator.comparingDouble(one -> one.fitness));
		return roster.subList(roster.size() - numAllowedToReprod, roster.size());
	}

	/**
	 * Tournament selection
	 *
	 * @param chromosomesList
	 * @return
	 */
	private List<Chromosome> tournamentSelection(List<Chromosome> chromosomesList) {
		ArrayList<Chromosome> newPop = new ArrayList<>();

		// fill until population is full
		// do not ever try population less than 41 please.
		while (newPop.size() < maxPopSize) {
			tournamentBracket(chromosomesList, newPop);
		}

		return newPop;
	}

	/**
	 * Get random 4 from the chromosome list, then return the fittest out of the 4 (highest fitness score)
	 * Assignment instructions told me to select 4
	 *
	 * @param chromosomesList
	 * @return
	 */
	private void tournamentBracket(List<Chromosome> chromosomesList, List<Chromosome> newPop) {
		List<Chromosome> roster = new ArrayList<>();
		// random select
		int[] previousIndexes = new int[]{-1, -1, -1, -1}; // track previous index we have selected
		int index = 0;

		// select random DISTINCT dudes for the tournament
		while (roster.size() < 4) {
			int number = rng.nextInt(0, chromosomesList.size());
			if (Arrays.stream(previousIndexes).noneMatch(n -> n == number)) {
				roster.add(chromosomesList.get(number));
				previousIndexes[index] = number;
				index++;
			}
		}

		// get top 2 fittest
		Chromosome mom = findElite(roster).clone();
		roster.remove(mom);
		Chromosome dad = findElite(roster).clone();


		// crossover
		List<Chromosome> crossoverProducts = new ArrayList<>();
		crossoverProducts.add(mom);
		crossoverProducts.add(dad);


		if (rng.nextFloat(0f, 1f) < crossRate) { // pretend this is <= crossRate  :)
//					System.out.println("DOING CROSS");
			if (useUniformCross) {
				uniformCross(mom, dad, newPop);
			}
			else {
				twoPointCrossover(mom, dad, newPop);
			}
		} // else parents don't have children.

		/**
		 * I asked our lord and savour Mr. Reggie, and he said the process was roughly:
		 *
		 * 1. Do the initial tournament selection for the roster
		 * 2. Pick best 2 for crossover, and perform crossover if crossover rate is met
		 * 3. Finally select best 2 out of parents + offspring to be added to the population
		 */
		crossoverProducts.sort(Comparator.comparingDouble(one -> one.fitness));
		newPop.addAll(crossoverProducts.subList(crossoverProducts.size() - 2, crossoverProducts.size() - 1));
	}


	/**
	 * ----------------------------
	 * Misc
	 * ----------------------------
	 */


	/**
	 * I think this is just "close your eyes" and
	 * assign random courses to random rooms at random times
	 */
	private void genRandomPop() {
		population.clear();
		for (int i = 0; i < maxPopSize; i++) {
			ArrayList<Genome> geneList = new ArrayList<>();

			// iterate through all courses. give then a random timeslot and room and disregard any constraints
			allCourses.forEach(course -> {
				Room rm = allRooms.get(rng.nextInt(0, allRooms.size()));
				Timeslot ts = allTimeslots.get(rng.nextInt(0, allTimeslots.size()));

				geneList.add(new Genome(rm, course, ts));
			});

			population.add(new Chromosome(geneList));
		}
	}


	/**
	 * Print schedules, plus a graphical view to console.
	 *
	 * For debugging, unused in this code
	 *
	 * @param chromosome
	 */
	private void printChromosome(Chromosome chromosome) {
		// this code is basically a stripped down of evaluate fitness
		Hashtable<Room, int[][]> roomSchedules = new Hashtable<>();
		Hashtable<String, int[][]> profSchedules = new Hashtable<>();

		// add all empty schedules for profs and allRooms
		allRooms.forEach(rm -> roomSchedules.put(rm, Utils.genBlankSchedule()));
		allCourses.forEach(course -> profSchedules.put(course.professor, Utils.genBlankSchedule()));

		// now just fill in the matrix based on what course is schedule where.
		chromosome.genesList.forEach(genome -> {
			// get the schedule matrices for the room/prof this course is scheduled with
			int[][] roomMatrix = roomSchedules.get(genome.rm);
			int[][] profMatrix = profSchedules.get(genome.course.professor);
			int dayOfWeek = genome.ts.day.getValue();
			int time = genome.ts.hour;

			// for each hour that the class runs for...
			for (int i = 0; i < genome.course.duration; i++) {
				// get the timeslot and then throw the course onto the matrix
				roomMatrix[dayOfWeek][time] += 1;
				profMatrix[dayOfWeek][time] += 1;
				time++;
			}
		});

		// ******** print to console *********
		roomSchedules.forEach((rm, sched) -> {
			System.out.println("\n\n\n\nROOM: " + rm.name);
//			output.append("\n\n\n\nROOM: " + rm.name).append("\n");
			try {
				Utils.print2DArray(sched);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		});

		profSchedules.forEach((rm, sched) -> {
			System.out.println("\n\n\n\nPROFS: " + rm);
//			output.append("\n\n\n\nPROFS: " + rm).append("\n");
			try {
				Utils.print2DArray(sched);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		});

		chromosome.genesList.forEach(gene -> {
			output.append(gene.course.name).append("\n")
				.append(gene.course.professor).append("\n")
				.append(gene.rm.name).append("\n")
				.append(gene.ts.day).append(":").append(gene.ts.hour).append("\n");
		});
	}

}