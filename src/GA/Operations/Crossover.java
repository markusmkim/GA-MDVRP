package GA.Operations;

import GA.Components.Individual;
import GA.Metrics;
import MDVRP.Depot;
import MDVRP.Manager;

import java.util.*;

public class Crossover {
    private double crossoverRate;
    private double balanceParameter;
    private Manager manager;
    private Metrics metrics;


    public Crossover(Manager manager, Metrics metrics, double crossoverRate) {
        this.crossoverRate = crossoverRate;
        this.balanceParameter = 0.8;
        this.manager = manager;
        this.metrics = metrics;
    }


    public Individual[] apply(Individual p1, Individual p2) {
        if (Math.random() > this.crossoverRate) {
            // By some probability according to the crossover rate parameter, do not d apply crossover
            return new Individual[]{p1, p2};
        }
        List<Depot> depots = this.manager.getDepots();

        // Chose random depot to apply crossover to
        Random random = new Random();
        List<Integer> depotIDs = new ArrayList<>(p1.getChromosome().keySet());
        int chosenDepotId = depotIDs.get(random.nextInt(depotIDs.size()));
        Depot chosenDepot = depots.stream().filter(d -> chosenDepotId == d.getId()).findAny().orElse(null);

        // Copy parents to avoid cross reference bugs
        Individual parent1 = p1.getCopy();
        Individual parent2 = p2.getCopy();

        // Choose random routes
        List<Integer> parent1RandomRoute = new ArrayList<>(parent1.
                getChromosome().
                get(chosenDepotId).
                get(random.nextInt(parent1.getChromosome().get(chosenDepotId).size())));

        List<Integer> parent2RandomRoute = new ArrayList<>(parent2.
                getChromosome().
                get(chosenDepotId).
                get(random.nextInt(parent2.getChromosome().get(chosenDepotId).size())));

        /*
        System.out.println("\n----------------------------------------------------------------------------------------\n");
        System.out.println("Before");
        System.out.println(parent1);
        System.out.println(parent2);

        System.out.println("Choosing route " + Arrays.toString(parent1RandomRoute.toArray()) + "from parent 1");
        System.out.println("Choosing route " + Arrays.toString(parent2RandomRoute.toArray()) + "from parent 2");
         */


        // Remove customers in parent 1 random route from parent 2, and vice versa
        this.removeCustomerIDsFromRoutes(new ArrayList<>(parent2.getChromosome().values()), parent1RandomRoute);
        this.removeCustomerIDsFromRoutes(new ArrayList<>(parent1.getChromosome().values()), parent2RandomRoute);

        /*
        System.out.println("After removing");
        System.out.println(parent1);
        System.out.println(parent2);
         */

        // for (List<List<Integer>> routes : parentCopy1.getChromosome().get(chosenDepotId))
        for (Integer customerID : parent1RandomRoute) {
            // add all ids somewhere in parentCopy2
            this.insertCustomerID(chosenDepot, parent2, customerID);
        }

        for (Integer customerID : parent2RandomRoute) {
            // add all ids somewhere in parentCopy1
            this.insertCustomerID(chosenDepot, parent1, customerID);
        }
        /*
        System.out.println("After insertion");
        System.out.println(parent1);
        System.out.println(parent2);
        System.out.println("\n----------------------------------------------------------------------------------------\n");

         */

        return new Individual[]{parent1, parent2};
    }


    private void insertCustomerID(Depot depot, Individual individual, int customerID) {
        List<Insertion> feasibleInsertions = new ArrayList<>();
        List<Insertion> unFeasibleInsertions = new ArrayList<>();

        List<List<Integer>> routes  = individual.getChromosome().get(depot.getId());
        int numberOfRoutes = 0;
        for (int routeLoc = 0; routeLoc < routes.size(); routeLoc++) {
            for (int index = 0; index < routes.get(routeLoc).size() + 1; index ++) {
                List<List<Integer>> routesCopy = Crossover.copyDepotRoutes(routes);
                routesCopy.get(routeLoc).add(index, customerID);
                Insertion insertion = new Insertion(this.manager, this.metrics, depot, routesCopy, customerID, routeLoc, index);
                if (insertion.isFeasible()) {
                    feasibleInsertions.add(insertion);
                } else {
                    unFeasibleInsertions.add(insertion);
                }
            }
            numberOfRoutes++;
        }
        if (numberOfRoutes < depot.getMaxVehicles()) {
            List<List<Integer>> routesCopy = Crossover.copyDepotRoutes(routes);
            List<Integer> newRoute = new ArrayList<>(Arrays.asList(customerID));
            routesCopy.add(newRoute);
            Insertion insertion = new Insertion(this.manager, this.metrics, depot, routesCopy, customerID, 0, 0);
            if (insertion.isFeasible()) {
                feasibleInsertions.add(insertion);
            } else {
                unFeasibleInsertions.add(insertion);
            }
        }


        Insertion chosenInsertion;
        if (Math.random() < this.balanceParameter && feasibleInsertions.size() > 0) {   // Find first feasible insertion
            chosenInsertion = Insertion.findBest(feasibleInsertions);
        }
        else {
            List<Insertion> allInsertions = new ArrayList<>();
            allInsertions.addAll(feasibleInsertions);
            allInsertions.addAll(unFeasibleInsertions);
            chosenInsertion = Insertion.findBest(allInsertions);                       // Else, take best infeasible
        }

        // insert
        individual.getChromosome().put(chosenInsertion.getDepot().getId(), chosenInsertion.getResult());
    }

    private void removeCustomerIDsFromRoutes(List<List<List<Integer>>> routesAcrossAllDepots, List<Integer> IDs) {
        for (List<List<Integer>> routes : routesAcrossAllDepots) {
            for (List<Integer> route : routes) {
                for (int ID : IDs) {
                    if (route.contains(ID)) {
                        route.remove(Integer.valueOf(ID));
                    }
                }
            }
            routes.removeIf(List::isEmpty);  // Remove empty routes
        }
    }



    public static List<List<Integer>> copyDepotRoutes(List<List<Integer>> routes) {
        List<List<Integer>> copy = new ArrayList<>();
        for (List<Integer> route : routes) {
            List<Integer> routeCopy = new ArrayList<>(route);
            copy.add(routeCopy);
        }
        return copy;
    }

    private static void printRoutes(List<List<Integer>> routes) {
        String s = "";
        for (List<Integer> route : routes) {
            s += Arrays.toString(route.toArray()) + " - ";
        }
        System.out.println(s);
    }


    public static void main(String[] args) {
        List<Integer> route1 = new ArrayList<>(Arrays.asList(5, 3, 2, 4, 1));
        List<Integer> route2 = new ArrayList<>(Arrays.asList(7, 11, 9, 10, 8, 12));
        List<List<Integer>> routes = new ArrayList<>();
        List<List<List<Integer>>> rr = new ArrayList<>();

        routes.add(route1);
        routes.add(route2);
        rr.add(routes);

        System.out.println("Before");
        Crossover.printRoutes(routes);

        List<Integer> IDsToRemove = new ArrayList<>(Arrays.asList(3, 5, 11));
        //Crossover crossover = new Crossover();
        //crossover.removeCustomerIDsFromRoutes(rr, IDsToRemove);
        //System.out.println("Removing keys 3, 5, and 11...");
        //System.out.println("After");
        //Crossover.printRoutes(routes);
    }

    /*
    private void insertCustomerID(List<Depot> depots, Individual individual, int customerID) {
        List<Insertion> insertions = new ArrayList<>();
        for (Depot depot : depots) {
            List<List<Integer>> routes  = individual.getChromosome().get(depot.getId());
            int numberOfRoutes = 0;
            for (int routeLoc = 0; routeLoc < routes.size(); routeLoc++) {
                for (int index = 0; index < routes.get(routeLoc).size(); index ++) {
                    List<List<Integer>> routesCopy = Crossover.copyDepotRoutes(routes);
                    routesCopy.get(routeLoc).add(index, customerID);
                    Insertion insertion = new Insertion(this.manager, this.metrics, depot, routes, routesCopy);
                    insertions.add(insertion);
                }
                numberOfRoutes++;
            }
            if (numberOfRoutes < depot.getMaxVehicles()) {
                List<List<Integer>> routesCopy = Crossover.copyDepotRoutes(routes);
                List<Integer> newRoute = new ArrayList<>(Arrays.asList(customerID));
                routesCopy.add(newRoute);
                Insertion insertion = new Insertion(this.manager, this.metrics, depot, routes, routesCopy);
                insertions.add(insertion);
            }
        }
     */
}
