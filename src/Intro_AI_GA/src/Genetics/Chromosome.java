package Genetics;

import java.util.ArrayList;
import java.util.List;

/**
 * A solution to the problem, aka schedule(s) with all courses assigned a time slot
 */
public class Chromosome {

	public double fitness = Double.MIN_VALUE; // this value is to be modified externally
	public List<Genome> genesList;


	/**
	 * Create a chromosome with no genes (blank)
	 */
	public Chromosome() {
		this.genesList = new ArrayList<>();
	}

	/**
	 * Create a chromosome with pre-defined genes
	 *
	 * @param listOfGenes
	 */
	public Chromosome(List<Genome> listOfGenes) {
		this.genesList = listOfGenes;
	}

	/**
	 * Create a chromosome with pre-defined genes and fitness
	 *
	 * @param listOfGenes
	 */
	public Chromosome(List<Genome> listOfGenes, double fitness) {
		this.genesList = listOfGenes;
		this.fitness = fitness;
	}


	@Override
	public Chromosome clone() {
		return new Chromosome(cloneGenes(genesList), fitness);
	}

	/**
	 * Create a deep copy of the genes
	 *
	 * @param genesList
	 * @return
	 */
	private List<Genome> cloneGenes(List<Genome> genesList) {
		List<Genome> newGeneList = new ArrayList<>();
		genesList.forEach(thing -> newGeneList.add(thing.clone()));
		return newGeneList;
	}
}
