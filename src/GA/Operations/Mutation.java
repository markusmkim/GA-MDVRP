package GA.Operations;

import GA.Components.Individual;
import MDVRP.Customer;
import MDVRP.Depot;
import MDVRP.Manager;

import java.util.*;

public class Mutation {
    private Manager manager;
    private Inserter inserter;
    private double mutationRate;
    private int interDepotFreq;

    public Mutation(Manager manager, Inserter inserter, double mutationRate, int interDepotFreq) {
        this.manager = manager;
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

        List<List<Integer>> routes = individual.getChromosome().get(chosenDepotId);
        List<Integer> routesFlattened = new ArrayList<>();
        for (List<Integer> route : routes) {
            routesFlattened.addAll(route);
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

        List<List<Integer>> augmentedRoutes = new ArrayList<>();
        int flatIndex  = 0;
        for (List<Integer> route : routes) {
            List<Integer> augmentedRoute = new ArrayList<>();
            for (int ii = 0; ii < route.size(); ii++) {
                augmentedRoute.add(augmentedRoutesFlattened.get(flatIndex));
                flatIndex++;
            }
            augmentedRoutes.add(augmentedRoute);
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
        for (Map.Entry<Integer, List<List<Integer>>> entry : individual.getChromosome().entrySet()) {
            boolean breakLoop = false;
            int key = entry.getKey();
            for (List<Integer> route: entry.getValue()) {
                if (route.contains(chosenCustomerID)) {
                    wasAtDepotID = key;
                    route.remove(Integer.valueOf(chosenCustomerID));
                    breakLoop = true;
                    break;
                }
            }
            entry.getValue().removeIf(List::isEmpty);  // Remove empty routes
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
