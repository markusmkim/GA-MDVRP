package GA.Operations;

import GA.Components.Individual;
import MDVRP.CrowdedDepot;
import MDVRP.Depot;
import MDVRP.RouteScheduler;

import java.util.*;


public class Initializer {

    public static List<Individual> init(Integer populationSize, List<CrowdedDepot> depots) {
        List<Individual> population = new ArrayList<>();

        for (int i = 0; i < populationSize; i++) {
            Map<Integer, List<List<Integer>>> chromosome = new HashMap<>();

            for (CrowdedDepot depot: depots) {
                List<List<Integer>> chromosomeDepot = RouteScheduler.getInitialRoutes(depot);
                chromosome.put(depot.getId(), chromosomeDepot);
            }

            Individual individual = new Individual(chromosome);
            population.add(individual);
        }
        return population;
    }
}