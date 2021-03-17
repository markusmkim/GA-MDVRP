package GA;

import GA.Components.Individual;
import GA.Components.Route;
import MDVRP.Customer;
import MDVRP.Depot;
import MDVRP.Manager;
import Utils.Euclidian;

import java.util.List;
import java.util.Map;

public class Metrics {
    private Manager manager;

    public Metrics(Manager manager) {
        this.manager = manager;
    }

    // FITNESS FUNCTION
    public double getTotalDistanceOLD(Individual individual) {
        List<Depot> depots = this.manager.getDepots();
        double totalDistance = 0;
        for (Map.Entry<Integer, List<Route>> entry : individual.getChromosome().entrySet()) {
            int key = entry.getKey();
            List<Route> chromosomeDepot = entry.getValue();
            for (Route route : chromosomeDepot) {
                Depot depot = depots.stream().filter(d -> key == d.getId()).findAny().orElse(null);// find depot
                if (depot == null) {
                    System.out.println("Something wring, depot = null in Metrics.getTotalDistance");
                    depot = depots.get(0);
                }
                totalDistance += this.getRouteDistance(depot.getId(), route.getRoute());                                 // get distance

                // Print route demand
                // double routeDemand = this.getRouteDemand(route);
                //System.out.println("Route demand:  " + routeDemand);
            }
        }
        return totalDistance;
    }


    public double getTotalDistance(Individual individual) {
        double totalDistance = 0;
        for (Map.Entry<Integer, List<Route>> entry : individual.getChromosome().entrySet()) {
            List<Route> chromosomeDepot = entry.getValue();
            for (Route route : chromosomeDepot) {
                totalDistance += route.getDistance();     // get distance
            }
        }
        return totalDistance;
    }


    public void evaluateRoute(int depotID, Route route) {
        if (route.getRoute().size() == 0) {
            return;
        }

        double totalDistance = 0;
        int totalDemand = 0;

        Depot depot = this.manager.getDepot(depotID);
        int[] depotCoordinates = new int[]{depot.getX(), depot.getY()};

        // Add distance from depot to first customer
        Customer toCustomer = this.manager.getCustomer(route.getRoute().get(0));
        int[] toCustomerCoordinates = new int[]{toCustomer.getX(), toCustomer.getY()};
        totalDistance += Euclidian.distance(depotCoordinates, toCustomerCoordinates);

        // Add distances between customers
        Customer fromCustomer = toCustomer;
        for (int i = 1; i < route.getRoute().size(); i++) {
            totalDemand += fromCustomer.getDemand();

            int[] fromCustomerCoordinates = new int[]{fromCustomer.getX(), fromCustomer.getY()};

            toCustomer = this.manager.getCustomer(route.getRoute().get(i));
            toCustomerCoordinates = new int[]{toCustomer.getX(), toCustomer.getY()};

            totalDistance += Euclidian.distance(fromCustomerCoordinates, toCustomerCoordinates);
            fromCustomer = toCustomer;
        }
        totalDemand += fromCustomer.getDemand();

        // Add distance from last customer and back to depot
        totalDistance += Euclidian.distance(toCustomerCoordinates, depotCoordinates);

        route.setDistance(totalDistance);
        route.setDemand(totalDemand);
    }


    public double getRouteDistance(int depotID, List<Integer> route) {
        if (route.size() == 0) {
            // System.out.println("Empty route");
            return 0;
        }

        // System.out.println("\nCalculation new route \n----------------------------------");
        double totalDistance = 0;

        Depot depot = this.manager.getDepot(depotID);
        int[] depotCoordinates = new int[]{depot.getX(), depot.getY()};

        // Add distance from depot to first customer
        Customer toCustomer = this.manager.getCustomer(route.get(0));
        int[] toCustomerCoordinates = new int[]{toCustomer.getX(), toCustomer.getY()};
        totalDistance += Euclidian.distance(depotCoordinates, toCustomerCoordinates);
        //// System.out.println("Adding distance from " + Arrays.toString(depotCoordinates) + " to " + Arrays.toString(toCustomerCoordinates));


        // Add distances between customers
        Customer fromCustomer = toCustomer;
        for (int i = 1; i < route.size(); i++) {
            int[] fromCustomerCoordinates = new int[]{fromCustomer.getX(), fromCustomer.getY()};

            toCustomer = this.manager.getCustomer(route.get(i));
            toCustomerCoordinates = new int[]{toCustomer.getX(), toCustomer.getY()};

            totalDistance += Euclidian.distance(fromCustomerCoordinates, toCustomerCoordinates);
            //// System.out.println("Adding distance from " + Arrays.toString(fromCustomerCoordinates) + " to " + Arrays.toString(toCustomerCoordinates));

            fromCustomer = toCustomer;
        }

        // Add distance from last customer and back to depot
        totalDistance += Euclidian.distance(toCustomerCoordinates, depotCoordinates);
        //// System.out.println("Adding distance from " + Arrays.toString(toCustomerCoordinates) + " to " + Arrays.toString(depotCoordinates));
        //System.out.println("=");
        //System.out.println("Route distance: " + totalDistance);
        return totalDistance;
    }


    public boolean isIndividualFeasible(Individual individual) {
        List<Depot> depots = this.manager.getDepots();
        for (Map.Entry<Integer, List<Route>> entry : individual.getChromosome().entrySet()) {
            int key = entry.getKey();
            Depot depot = depots.stream().filter(d -> key == d.getId()).findAny().orElse(null);// find depot
            List<Route> routes = entry.getValue();
            if (! this.areRoutesFeasible(depot, routes)) {
                return false;
            }
        }
        return true;
    }


    public boolean areRoutesFeasible(Depot depot, List<Route> routes) {
        int numberOfVehiclesInUse = 0;
        for (Route route : routes) {
            int demand = route.getDemand();
            int maxDuration = depot.getMaxDuration();

            double duration = maxDuration == 0 ? 0 : route.getDistance();
            if (duration > maxDuration || demand > depot.getMaxVehicleLoad()) {
                return false;
            }
            numberOfVehiclesInUse++;
        }

        return numberOfVehiclesInUse <= depot.getMaxVehicles();
    }


    public boolean checkRoutes(Depot depot, List<Integer> route) {
        int demand = 0;
        for (int customerID : route) {
            Customer customer = this.manager.getCustomer(customerID);
            demand += customer.getDemand();
        }
        int maxDuration = depot.getMaxDuration();
        double duration = maxDuration == 0 ? 0 : this.getRouteDistance(depot.getId(), route);
        return duration <= maxDuration && demand <= depot.getMaxVehicleLoad();
    }


    public double getRouteDemand(List<Integer> route) {
        double totalDemand = 0;
        for (Integer customerId : route) {
            totalDemand += this.manager.getCustomer(customerId).getDemand();
        }
        return totalDemand;
    }
}
