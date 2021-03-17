package GA.Operations;

import GA.Components.Individual;
import GA.Components.Route;
import GA.Metrics;
import MDVRP.Customer;
import MDVRP.Depot;
import MDVRP.Manager;

import java.util.*;

public class Mutation {
    private Manager manager;
    private Metrics metrics;
    private Inserter inserter;
    private double mutationRate;
    private int interDepotFreq;

    public Mutation(Manager manager, Metrics metrics, Inserter inserter, double mutationRate, int interDepotFreq) {
        this.manager = manager;
        this.metrics = metrics;
        this.inserter = inserter;
        this.mutationRate = mutationRate;
        this.interDepotFreq = interDepotFreq;
    }


    public void apply(Individual individual, int generation) {
        if (Math.random() < mutationRate) {
            if (generation % this.interDepotFreq == 0) {
                this.applyInterDepotSwapping(individual);
            }
            else {
                this.applyReversal(individual);
            }
        }
    }


    private void applyReversal(Individual individual) {
        Random random = new Random();
        List<Integer> depotIDs = new ArrayList<>(individual.getChromosome().keySet());
        int chosenDepotId = depotIDs.get(random.nextInt(depotIDs.size()));

        List<Route> routes = individual.getChromosome().get(chosenDepotId);
        List<Integer> routesFlattened = new ArrayList<>();
        for (Route route : routes) {
            routesFlattened.addAll(route.getRoute());
        }
        if (routesFlattened.size() < 2) {
            return;
        }

        int cutPoint1 = random.nextInt(routesFlattened.size());
        int cutPoint2 = cutPoint1;
        while (cutPoint1 == cutPoint2) {
            cutPoint2 = random.nextInt(routesFlattened.size());
        }
        int cutFrom;
        int cutTo;
        if (cutPoint1 < cutPoint2) {
            cutFrom = cutPoint1;
            cutTo = cutPoint2;
        }
        else {
            cutFrom = cutPoint2;
            cutTo = cutPoint1;
        }

        List<Integer> reversedSegment = new ArrayList<>();
        for (int i = cutTo; i >= cutFrom; i--) {
            reversedSegment.add(routesFlattened.get(i));
        }

        List<Integer> augmentedRoutesFlattened = new ArrayList<>(routesFlattened.subList(0, cutFrom));
        augmentedRoutesFlattened.addAll(reversedSegment);
        if (cutTo < routesFlattened.size() - 1) {
            augmentedRoutesFlattened.addAll(routesFlattened.subList(cutTo + 1, routesFlattened.size()));
        }

        List<Route> augmentedRoutes = new ArrayList<>();
        int flatIndex  = 0;
        for (Route route : routes) {
            List<Integer> augmentedRoute = new ArrayList<>();
            for (int ii = 0; ii < route.getRoute().size(); ii++) {
                augmentedRoute.add(augmentedRoutesFlattened.get(flatIndex));
                flatIndex++;
            }
            Route resultingRoute = new Route(augmentedRoute);
            this.metrics.evaluateRoute(chosenDepotId, resultingRoute);
            augmentedRoutes.add(resultingRoute);
        }

        // apply mutation to chromosome
        individual.getChromosome().put(chosenDepotId, augmentedRoutes);
    }


    private void applyInterDepotSwapping(Individual individual) {
        Random random = new Random();
        List<Customer> candidates = this.manager.getSwappableCustomers();
        Customer chosenCustomer = candidates.get(random.nextInt(candidates.size()));  // choose random customer to swap
        int chosenCustomerID = chosenCustomer.getId();
        int wasAtDepotID = 0;
        for (Map.Entry<Integer, List<Route>> entry : individual.getChromosome().entrySet()) {
            boolean breakLoop = false;
            int key = entry.getKey();
            for (Route route: entry.getValue()) {
                if (route.removeCustomer(chosenCustomerID)) {
                    wasAtDepotID = key;
                    this.metrics.evaluateRoute(key, route);
                    breakLoop = true;
                    break;
                }
            }
            entry.getValue().removeIf(Route::isEmpty);  // Remove empty routes
            if (breakLoop) {
                break;
            }
        }
        List<Integer> possibleDepotIDs = new ArrayList<>(chosenCustomer.getPossibleDepots());
        if (possibleDepotIDs.contains(wasAtDepotID)) {
            possibleDepotIDs.remove(Integer.valueOf(wasAtDepotID));
        }

        int chosenNextDepotID = possibleDepotIDs.get(random.nextInt(possibleDepotIDs.size()));
        Depot chosenDepot = this.manager.getDepot(chosenNextDepotID);
        this.inserter.insertCustomerID(chosenDepot, individual, chosenCustomerID, 1);
    }

}
