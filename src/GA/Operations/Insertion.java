package GA.Operations;

import GA.Metrics;
import MDVRP.Depot;
import MDVRP.Manager;
import MDVRP.RouteScheduler;

import java.util.Arrays;
import java.util.List;

public class Insertion implements Comparable<Insertion> {

    private Depot depot;
    private double cost;
    private boolean isFeasible;
    private List<List<Integer>> result;

    public Insertion(Metrics metrics, Depot depot, List<List<Integer>> routes, List<List<Integer>> augmentedRoutes) {
        double routesCost = 0;
        double augmentedRoutesCost = 0;
        for (List<Integer> route : routes) {
            routesCost += metrics.getRouteDistance(depot.getId(), route);
        }
        for (List<Integer> augmentedRoute : augmentedRoutes) {
            augmentedRoutesCost += metrics.getRouteDistance(depot.getId(), augmentedRoute);
        }
        double insertionCost = augmentedRoutesCost - routesCost;
        this.isFeasible = metrics.areRoutesFeasible(depot, augmentedRoutes);
        /*
        if (insertionCost < 0) {
            System.out.println("Insertion cost below zero!! in Insertion constructor");
            System.out.println("Is feasable" + this.isFeasible);
            System.out.println(Arrays.toString(routes.toArray()) + routesCost);
            System.out.println(Arrays.toString(augmentedRoutes.toArray()) + augmentedRoutesCost);
        }
         */

        this.depot = depot;
        this.cost = insertionCost;

        this.result = augmentedRoutes;
    }

    public Depot getDepot()                 { return depot; }
    public double getCost()                 { return cost; }
    public boolean isFeasible()             { return isFeasible; }
    public List<List<Integer>> getResult()  { return result; }

    @Override
    public String toString() {
        return "" + this.cost;
    }

    @Override
    public int compareTo(Insertion insertion) {
        return Double.compare(this.cost, insertion.getCost());
    }
}
