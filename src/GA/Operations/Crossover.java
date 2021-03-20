package GA.Operations;

import GA.Components.Individual;
import GA.Components.Route;
import MDVRP.Depot;
import MDVRP.Manager;

import java.util.*;


/*
Recombination operator
 */
public class Crossover {
    private double crossoverRate;
    private double balanceParameter;
    private Manager manager;
    private Inserter inserter;


    public Crossover(Manager manager, Inserter inserter, double crossoverRate, double balanceParameter) {
        this.crossoverRate = crossoverRate;
        this.balanceParameter = balanceParameter;
        this.manager = manager;
        this.inserter = inserter;
    }


    public void setCrossoverRate(double crossoverRate) {
        System.out.println("Lowering crossover rate to " + crossoverRate);
        this.crossoverRate = crossoverRate;
    }


    public Individual[] apply(Individual p1, Individual p2) {
        /*
        Recombines two individuals (parents) to produce two new individuals (offspring),
        by applying Best Cost Route Crossover:
        1. Randomly select depot to undergo reproduction
        2. Randomly select a route from each parent
        3. For each selected route, remove all customers in that route from the other parent
        4. For each customer c that was removed from parent p, insert customer c back in parent p at best location.
         */
        if (Math.random() > this.crossoverRate) {
            // By some probability according to the crossover rate parameter, do not apply crossover
            return new Individual[]{p1, p2};
        }

        // Step 1: Randomly select depot to undergo reproduction //
        List<Depot> depots = this.manager.getDepots();
        Random random = new Random();
        List<Integer> depotIDs = new ArrayList<>(p1.getChromosome().keySet());
        int chosenDepotId = depotIDs.get(random.nextInt(depotIDs.size()));
        Depot chosenDepot = depots.stream().filter(d -> chosenDepotId == d.getId()).findAny().orElse(null);

        Individual parent1 = p1.getClone();  // Clone parents to avoid cross reference bugs
        Individual parent2 = p2.getClone();

        // Step 2. Randomly select a route from each parent //
        List<Integer> parent1RandomRoute = new ArrayList<>(parent1.
                getChromosome().
                get(chosenDepotId).
                get(random.nextInt(parent1.getChromosome().get(chosenDepotId).size())).getRoute());

        List<Integer> parent2RandomRoute = new ArrayList<>(parent2.
                getChromosome().
                get(chosenDepotId).
                get(random.nextInt(parent2.getChromosome().get(chosenDepotId).size())).getRoute());



        // Step 3: For each selected route, remove all customers in that route from the other parent //
        this.removeCustomerIDsFromRoutes(new ArrayList<>(parent2.getChromosome().values()), parent1RandomRoute);
        this.removeCustomerIDsFromRoutes(new ArrayList<>(parent1.getChromosome().values()), parent2RandomRoute);

        // Step 4: For each customer c that was removed from parent p, insert customer c in parent p at best location.
        for (Integer customerID : parent1RandomRoute) {
            // add all ids somewhere in parentCopy2
            this.inserter.insertCustomerID(chosenDepot, parent2, customerID, this.balanceParameter);
        }

        for (Integer customerID : parent2RandomRoute) {
            // add all ids somewhere in parentCopy1
            this.inserter.insertCustomerID(chosenDepot, parent1, customerID, this.balanceParameter);
        }

        return new Individual[]{parent1, parent2};
    }


    private void removeCustomerIDsFromRoutes(List<List<Route>> routesAcrossAllDepots, List<Integer> IDs) {
        for (List<Route> routes : routesAcrossAllDepots) {
            for (Route route : routes) {
                for (int ID : IDs) {
                    route.removeCustomer(ID);
                }
            }
            routes.removeIf(Route::isEmpty);  // Remove empty routes
        }
    }


    private static void printRoutes(List<List<Integer>> routes) {
        String s = "";
        for (List<Integer> route : routes) {
            s += Arrays.toString(route.toArray()) + " - ";
        }
        System.out.println(s);
    }
}
