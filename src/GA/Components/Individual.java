package GA.Components;

import java.util.*;


public class Individual implements Comparable<Individual> {
    private double fitness;
    private Map<Integer, List<List<Integer>>> chromosome;  // length = number of depots
    private boolean isFeasible;


    public Individual(Map<Integer, List<List<Integer>>> initialChromosome) {
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


    public Individual getCopy() {
        /**
         * Clone individual without fitness and isFeasible
         */
        Map<Integer, List<List<Integer>>> chromosomeCopy = new HashMap<>();

        for (Map.Entry<Integer, List<List<Integer>>> entry : this.chromosome.entrySet()) {
            int key = entry.getKey();
            List<List<Integer>> chromosomeDepot = entry.getValue();
            List<List<Integer>> chromosomeDepotCopy = new ArrayList<>();
            for (List<Integer> route: chromosomeDepot) {
                List<Integer> routeCopy = new ArrayList<>(route);
                chromosomeDepotCopy.add(routeCopy);
            }
            chromosomeCopy.put(key, chromosomeDepotCopy);
        }
        return new Individual(chromosomeCopy);
    }


    public static void main(String[] args) {
        Map<Integer, List<List<Integer>>> cc = new HashMap<>();
        for (int key = 1; key <= 2; key++) {
            List<List<Integer>> ccc = new ArrayList<>();
            for (int routesIndex = 2; routesIndex < 5; routesIndex++) {
                List<Integer> cccc = new ArrayList<>();
                for (int routeIndex = 4; routeIndex < 6; routeIndex++) {
                    cccc.add(1);
                }
                ccc.add(cccc);
            }
            cc.put(key, ccc);
        }
        Individual ind = new Individual(cc);
        Individual copy = ind.getCopy();
        ind.getChromosome().get(1).get(0).remove(0);
        ind.getChromosome().get(1).get(0).add(10);
        List<List<Integer>> r = new ArrayList<>();
        List<Integer> rr = new ArrayList<>();
        rr.add(3);
        rr.add(8);
        r.add(rr);
        rr.add(10);
        copy.getChromosome().put(0, r);
        System.out.println(ind);
        System.out.println(copy);
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








