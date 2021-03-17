package GA.Operations;

import GA.Components.Individual;
import GA.Components.Route;
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

        List<Route> routes  = individual.getChromosome().get(depot.getId());
        int numberOfRoutes = 0;
        for (int routeLoc = 0; routeLoc < routes.size(); routeLoc++) {
            for (int index = 0; index < routes.get(routeLoc).getRoute().size() + 1; index ++) {
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
        // System.out.println(numberOfRoutes + " : " + depot.getMaxVehicles());
        if (numberOfRoutes < depot.getMaxVehicles()) {
            List<List<Integer>> routesCopy = Inserter.copyDepotRoutes(routes);
            List<Integer> newRoute = new ArrayList<>(Arrays.asList(customerID));
            routesCopy.add(newRoute);
            // System.out.println(Arrays.toString(routesCopy.toArray()));
            Insertion insertion = new Insertion(this.manager, this.metrics, depot, routesCopy, customerID, routesCopy.size() - 1, 0);
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

        List<Route> result = this.getResult(chosenInsertion);

        // insert
        individual.getChromosome().put(chosenInsertion.getDepot().getId(), result);
    }

    private List<Route> getResult(Insertion insertion) {
        List<Route> result = new ArrayList<>();
        for (List<Integer> route : insertion.getResult()) {
            Route resultingRoute = new Route(route);
            this.metrics.evaluateRoute(insertion.getDepot().getId(), resultingRoute);
            result.add(resultingRoute);
        }
        return result;
    }


    public static List<List<Integer>> copyDepotRoutes(List<Route> routes) {
        List<List<Integer>> copy = new ArrayList<>();
        for (Route route : routes) {
            List<Integer> routeCopy = new ArrayList<>(route.getRoute());
            copy.add(routeCopy);
        }
        return copy;
    }

}
