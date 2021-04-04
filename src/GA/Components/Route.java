package GA.Components;

import java.util.ArrayList;
import java.util.List;

/*
Represents a route from and to a particular depot.
 */
public class Route {
    private List<Integer> route;    // List of customer IDs
    private int demand;             // total demand of customers
    private double distance;        // total route distance

    public Route(List<Integer> route, int demand, double distance) {
        this.route = route;
        this.demand = demand;
        this.distance = distance;
    }

    public Route(List<Integer> route) {
        this.route = route;
    }

    // Getters //
    public List<Integer> getRoute() { return route; }
    public int getDemand()          { return demand; }
    public double getDistance()     { return distance; }

    // Setters //
    public void setDemand(int demand)           { this.demand = demand; }
    public void setDistance(double distance)    { this.distance = distance; }


    public boolean removeCustomer(int ID) {
        if (this.route.contains(ID)) {
            this.route.remove(Integer.valueOf(ID));
            return true;
        }
        return false;
    }

    public boolean isEmpty() {
        return this.route.isEmpty();
    }

    public Route getClone() {
        return new Route(new ArrayList<>(this.route), this.demand, this.distance);
    }
}
