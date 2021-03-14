package GA;

import GA.Components.Individual;
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
    public double getTotalDistance(Individual individual) {
        List<Depot> depots = this.manager.getDepots();
        double totalDistance = 0;
        for (Map.Entry<Integer, List<List<Integer>>> entry : individual.getChromosome().entrySet()) {
            int key = entry.getKey();
            List<List<Integer>> chromosomeDepot = entry.getValue();
            for (List<Integer> route : chromosomeDepot) {
                Depot depot = depots.stream().filter(d -> key == d.getId()).findAny().orElse(null);// find depot
                if (depot == null) {
                    System.out.println("Something wring, depot = null in Metrics.getTotalDistance");
                    depot = depots.get(0);
                }
                totalDistance += this.getRouteDistance(depot.getId(), route);                                 // get distance

                // Print route demand
                // double routeDemand = this.getRouteDemand(route);
                //System.out.println("Route demand:  " + routeDemand);
            }
        }
        return totalDistance;
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
        for (Map.Entry<Integer, List<List<Integer>>> entry : individual.getChromosome().entrySet()) {
            int key = entry.getKey();
            Depot depot = depots.stream().filter(d -> key == d.getId()).findAny().orElse(null);// find depot
            List<List<Integer>> routes = entry.getValue();
            if (! this.areRoutesFeasible(depot, routes)) {
                return false;
            }
        }
        return true;
    }

    public boolean areRoutesFeasible(Depot depot, List<List<Integer>> routes) {
        //System.out.println("Checking feasible");
        //System.out.println("Depot max vehicles           : " + depot.getMaxVehicles());
        //System.out.println("Depot max route vehicle load : " + depot.getMaxVehicleLoad());
        //System.out.println("Depot max duration per route : " + depot.getMaxDuration());
        int numberOfVehiclesInUse = 0;
        for (List<Integer> route : routes) {
            int duration = 0;
            int demand = 0;
            for (int customerID : route) {
                Customer customer = this.manager.getCustomer(customerID);
                duration += customer.getDuration();
                demand += customer.getDemand();
            }
            //System.out.println("Route demand     :" + demand);
            //System.out.println("Route duration   :" + duration);
            if (duration > depot.getMaxDuration() || demand > depot.getMaxVehicleLoad()) {
                // System.out.println("Returning false");
                return false;
            }
            numberOfVehiclesInUse++;
        }
        //System.out.println("Vehicles in use");
        //boolean a = numberOfVehiclesInUse <= depot.getMaxVehicles();
        //System.out.println("Returning" + a);
        //System.out.println("Done checking feasible\n");
        return numberOfVehiclesInUse <= depot.getMaxVehicles();
    }


    public double getRouteDemand(List<Integer> route) {
        double totalDemand = 0;
        for (Integer customerId : route) {
            totalDemand += this.manager.getCustomer(customerId).getDemand();
        }
        return totalDemand;
    }
}
