package GA.Components;

import java.util.*;


public class Individual implements Comparable<Individual> {
    private double fitness;
    private Map<Integer, List<Route>> chromosome;  // length = number of depots
    private boolean isFeasible;


    public Individual(Map<Integer, List<Route>> initialChromosome) {
        this.chromosome = initialChromosome;
    }

    public void setIsFeasible(boolean feasible) { this.isFeasible = feasible; }
    public boolean isFeasible()                 { return isFeasible; }
    public double getFitness()                  { return fitness; }
    public void setFitness(double fitness) {
        if (fitness < 0) {
            System.out.println("Something wrong - negative fitness in Individual.setFitness");
            return;
        }
        this.fitness = fitness;
    }


    public Map<Integer, List<Route>> getChromosome() { return chromosome; }

    @Override
    public String toString() {
        String s = "";
        for (Map.Entry<Integer, List<Route>> entry : this.chromosome.entrySet()) {
            int key = entry.getKey();
            List<Route> chromosomeDepot = entry.getValue();
            s += "Depot" + key + ": ";
            for (Route route: chromosomeDepot) {
                s += Arrays.toString(route.getRoute().toArray()) + " - ";
            }
            s = s.substring(0, s.length() - 3);
            s += "|  ";
        }

        return s.substring(0, s.length() - 3);
    }


    public Individual getClone() {
        /**
         * Clone individual without fitness and isFeasible
         */
        Map<Integer, List<Route>> chromosomeCopy = new HashMap<>();

        for (Map.Entry<Integer, List<Route>> entry : this.chromosome.entrySet()) {
            int key = entry.getKey();
            List<Route> chromosomeDepot = entry.getValue();
            List<Route> chromosomeDepotCopy = new ArrayList<>();
            for (Route route: chromosomeDepot) {
                Route routeCopy = route.getClone();
                chromosomeDepotCopy.add(routeCopy);
            }
            chromosomeCopy.put(key, chromosomeDepotCopy);
        }
        return new Individual(chromosomeCopy);
    }


    @Override
    public int compareTo(Individual individual) {
        if (this.isFeasible && !individual.isFeasible()) {
            return -1;
        }
        if (!this.isFeasible && individual.isFeasible()) {
            return 1;
        }
        return Double.compare(this.fitness, individual.getFitness());
    }
}








