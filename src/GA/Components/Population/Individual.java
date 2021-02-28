package GA.Components.Population;

import java.util.List;


public class Individual {
    private List<String> chromosome;


    public Individual(List<String> initialDepotCustomerConfig) {
        this.chromosome = initialDepotCustomerConfig;
    }


    public List<String> getChromosome() { return chromosome; }
}
