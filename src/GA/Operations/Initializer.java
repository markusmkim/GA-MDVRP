package GA.Operations;

import GA.Components.Individual;
import GA.Components.Route;
import GA.Metrics;
import MDVRP.CrowdedDepot;
import MDVRP.RouteScheduler;

import java.util.*;

/*
Population initializer
 */
public class Initializer {
    public static List<Individual> init(Integer populationSize, List<CrowdedDepot> depots, Metrics metrics) {
        /*
        Returns a list of new individuals. Each individual chromosome is initialized with depots
        containing the appropriate customers according to depots (initial assignments), but the routes
        within each depot is generated randomly with a bias towards feasibility.
         */

        List<Individual> population = new ArrayList<>();

        for (int i = 0; i < populationSize; i++) {
            Map<Integer, List<Route>> chromosome = new HashMap<>();

            for (CrowdedDepot depot: depots) {
                List<Route> chromosomeDepot = RouteScheduler.getInitialRoutes(depot, metrics);
                chromosome.put(depot.getId(), chromosomeDepot);
            }

            Individual individual = new Individual(chromosome);
            population.add(individual);
        }
        return population;
    }
}
