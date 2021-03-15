package GA.Operations;

import GA.Components.Individual;
import GA.Metrics;
import MDVRP.Depot;
import MDVRP.Manager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class Inserter {
    private Manager manager;
    private Metrics metrics;

    public Inserter(Manager manager, Metrics metrics) {
        this.manager = manager;
        this.metrics = metrics;
    }


    public void insertCustomerID(Depot depot, Individual individual, int customerID, double balanceParameter) {
        List<Insertion> feasibleInsertions = new ArrayList<>();
        List<Insertion> unFeasibleInsertions = new ArrayList<>();

        List<List<Integer>> routes  = individual.getChromosome().get(depot.getId());
        int numberOfRoutes = 0;
        for (int routeLoc = 0; routeLoc < routes.size(); routeLoc++) {
            for (int index = 0; index < routes.get(routeLoc).size() + 1; index ++) {
                List<List<Integer>> routesCopy = Inserter.copyDepotRoutes(routes);
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
            List<List<Integer>> routesCopy = Inserter.copyDepotRoutes(routes);
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
        if (Math.random() < balanceParameter && feasibleInsertions.size() > 0) {   // Find first feasible insertion
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


    public static List<List<Integer>> copyDepotRoutes(List<List<Integer>> routes) {
        List<List<Integer>> copy = new ArrayList<>();
        for (List<Integer> route : routes) {
            List<Integer> routeCopy = new ArrayList<>(route);
            copy.add(routeCopy);
        }
        return copy;
    }

}
