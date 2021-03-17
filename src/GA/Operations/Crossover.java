package GA.Operations;

import GA.Components.Individual;
import GA.Components.Route;
import MDVRP.Depot;
import MDVRP.Manager;

import java.util.*;

public class Crossover {
    private double crossoverRate;
    private double balanceParameter;
    private Manager manager;
    private Inserter inserter;


    public Crossover(Manager manager, Inserter inserter, double crossoverRate) {
        this.crossoverRate = crossoverRate;
        this.balanceParameter = 1;
        this.manager = manager;
        this.inserter = inserter;
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
        Individual parent1 = p1.getClone();
        Individual parent2 = p2.getClone();

        // Choose random routes
        List<Integer> parent1RandomRoute = new ArrayList<>(parent1.
                getChromosome().
                get(chosenDepotId).
                get(random.nextInt(parent1.getChromosome().get(chosenDepotId).size())).getRoute());

        List<Integer> parent2RandomRoute = new ArrayList<>(parent2.
                getChromosome().
                get(chosenDepotId).
                get(random.nextInt(parent2.getChromosome().get(chosenDepotId).size())).getRoute());



        // Remove customers in parent 1 random route from parent 2, and vice versa
        this.removeCustomerIDsFromRoutes(new ArrayList<>(parent2.getChromosome().values()), parent1RandomRoute);
        this.removeCustomerIDsFromRoutes(new ArrayList<>(parent1.getChromosome().values()), parent2RandomRoute);


        // for (List<List<Integer>> routes : parentCopy1.getChromosome().get(chosenDepotId))
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
