package GA.Components.Population;

import java.util.Arrays;
import java.util.List;
import java.util.Map;


public class Individual {
    private double fitness;

    // length = number of depots
    private Map<Integer, List<List<Integer>>> chromosome;


    public Individual(Map<Integer, List<List<Integer>>> initialChromosome) {
        this.chromosome = initialChromosome;
    }


    public double getFitness() { return fitness; }
    public void setFitness(double fitness) {
        if (fitness < 0) {
            System.out.println("Something wrong - negative fitness in Individual.setFitness");
            return;
        }
        this.fitness = fitness;
    }


    public Map<Integer, List<List<Integer>>> getChromosome() { return chromosome; }

    @Override
    public String toString() {
        String s = "";
        for (Map.Entry<Integer, List<List<Integer>>> entry : this.chromosome.entrySet()) {
            int key = entry.getKey();
            List<List<Integer>> chromosomeDepot = entry.getValue();
            s += "Depot" + key + ": ";
            for (List<Integer> route: chromosomeDepot) {
                s += Arrays.toString(route.toArray()) + " - ";
            }
            s = s.substring(0, s.length() - 3);
            s += "|  ";
        }

        return s.substring(0, s.length() - 3);
    }
}
