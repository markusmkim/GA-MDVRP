package GA.Operations;

import GA.Metrics;
import MDVRP.Customer;
import MDVRP.Depot;
import MDVRP.Manager;
import Utils.Euclidian;

import java.util.List;

/*
Data wrapper class to represent a possible insertion of a customer into a route
 */
public class Insertion implements Comparable<Insertion> {
    private Manager manager;
    private Depot depot;
    private double cost;
    private boolean isFeasible;
    private List<List<Integer>> result;  // The result (all routes in depot) of this insertion


    public Insertion(Manager manager, Metrics metrics, Depot depot, List<List<Integer>> augmentedRoutes, int customerID, int routeLoc, int index) {
        this.manager = manager;
        this.depot = depot;

        this.cost = this.insertionCost(augmentedRoutes, customerID, routeLoc, index);
        this.isFeasible = metrics.checkRoutes(depot, augmentedRoutes.get(routeLoc));
        this.result = augmentedRoutes;
    }


    private double insertionCost(List<List<Integer>> routes, int customerID, int routeLoc, int index) {
        /*
        Computes the insertion cost of the insertion, based on which route and location in said route insertion is applied
         */
        List<Integer> augmentedRoute = routes.get(routeLoc);
        Customer customer = this.manager.getCustomer(customerID);

        // If this insertion created a new route distance, the cost is simply twice the distance between depot and customer
        if (augmentedRoute.size() == 1) {
            return 2 * Euclidian.distance(new int[]{depot.getX(), depot.getY()}, new int[]{customer.getX(), customer.getY()});
        }

        // If this insertion was at start or end of route,
        // cost = distance from depot to customer and customer to old first/last customer minus distance from depot to old first/last customer
        if (index == 0 || index == augmentedRoute.size() - 1) {
            int offset = index == 0 ? 1 : -1;
            Customer otherCustomer = this.manager.getCustomer(augmentedRoute.get(index + offset));
            double distanceToDepot = Euclidian.distance(new int[]{depot.getX(), depot.getY()}, new int[]{customer.getX(), customer.getY()});
            double distanceToOther = Euclidian.distance(new int[]{customer.getX(), customer.getY()}, new int[]{otherCustomer.getX(), otherCustomer.getY()});
            double distanceDepotToOther = Euclidian.distance(new int[]{depot.getX(), depot.getY()}, new int[]{otherCustomer.getX(), otherCustomer.getY()});
            return distanceToDepot + distanceToOther - distanceDepotToOther;
        }

        // Else insertion is at location between two customers, and
        // cost = distance from customer to both other customers minus distance between both other customers
        Customer customerBefore = this.manager.getCustomer(augmentedRoute.get(index - 1));
        Customer customerAfter = this.manager.getCustomer(augmentedRoute.get(index + 1));
        double distanceToCustomerBefore = Euclidian.distance(new int[]{customer.getX(), customer.getY()}, new int[]{customerBefore.getX(), customerBefore.getY()});
        double distanceToCustomerAfter = Euclidian.distance(new int[]{customer.getX(), customer.getY()}, new int[]{customerAfter.getX(), customerAfter.getY()});
        double distanceBetweenBeforeAfter = Euclidian.distance(new int[]{customerBefore.getX(), customerBefore.getY()}, new int[]{customerAfter.getX(), customerAfter.getY()});
        return (distanceToCustomerBefore + distanceToCustomerAfter) - distanceBetweenBeforeAfter;
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


    public static Insertion findBest(List<Insertion> insertions) {
        Insertion leader = insertions.get(0);
        for (Insertion insertion : insertions) {
            if (insertion.getCost() < leader.getCost()) {
                leader = insertion;
            }
        }
        return leader;
    }
}
