package MDVRP;

import GA.Components.Individual;

import java.util.List;

public class Solution {
    private List<Depot> depots;
    private Individual individual;

    public Solution(List<Depot> depots, Individual individual) {
        this.depots = depots;
        this.individual = individual;
    }

    public Individual getIndividual() { return individual; }

    public List<Depot> getDepots()    { return depots; }
}
