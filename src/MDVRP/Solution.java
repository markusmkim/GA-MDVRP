package MDVRP;

import GA.Components.Individual;

import java.util.List;

public class Solution {
    private List<CrowdedDepot> depots;
    private Individual individual;

    public Solution(List<CrowdedDepot> depots, Individual individual) {
        this.depots = depots;
        this.individual = individual;
    }

    public Individual getIndividual() { return individual; }

    public List<CrowdedDepot> getDepots()    { return depots; }
}
