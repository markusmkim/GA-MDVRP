package GA.Components.Population;

import java.util.Arrays;
import java.util.List;


public class Individual {
    // length = number of depots
    private List<List<Integer>> chromosome;


    public Individual(List<List<Integer>> initialChromosome) {
        this.chromosome = initialChromosome;
    }


    public List<List<Integer>> getChromosome() { return chromosome; }

    @Override
    public String toString() {
        String s = "";
        for (List<Integer> chromosomeDepot: this.chromosome) {
            s += Arrays.toString(chromosomeDepot.toArray()) + " - ";
        }
        return s.substring(0, s.length() - 3);
    }
}
