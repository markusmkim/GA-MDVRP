package GA;

import GA.Components.Individual;
import MDVRP.Customer;
import MDVRP.Depot;
import Utils.Euclidian;

import java.util.List;
import java.util.Map;

public class Metrics {


    public static void evaluatePopulation(List<Depot> depots, List<Individual> population) {
        for (Individual individual : population) {
            if (individual.getFitness() == 0) {
                double fitness = getTotalDistance(depots, individual);
                individual.setFitness(fitness);
            }
        }
    }


    // FITNESS FUNCTION
    public static double getTotalDistance(List<Depot> depots, Individual individual) {
        int totalDistance = 0;
        for (Map.Entry<Integer, List<List<Integer>>> entry : individual.getChromosome().entrySet()) {
            int key = entry.getKey();
            List<List<Integer>> chromosomeDepot = entry.getValue();
            for (List<Integer> route : chromosomeDepot) {
                Depot depot = depots.stream().filter(d -> key == d.getId()).findAny().orElse(null);// find depot
                if (depot == null) {
                    System.out.println("Something wring, depot = null in Fitness.getTotalDistance");
                    depot = depots.get(0);
                }
                totalDistance += Metrics.getRouteDistance(depot, route);                                 // get distance

                // Print route demand
                double routeDemand = Metrics.getRouteDemand(depot, route);
                System.out.println("Route demand:  " + routeDemand);
            }
        }
        return totalDistance;
    }


    public static double getRouteDistance(Depot depot, List<Integer> route) {
        if (route.size() == 0) {
            System.out.println("Empty route");
            return 0;
        }

        System.out.println("\nCalculation new route \n----------------------------------");
        int totalDistance = 0;

        int[] depotCoordinates = new int[]{depot.getX(), depot.getY()};

        // Add distance from depot to first customer
        Customer toCustomer = depot.getCustomer(route.get(0));
        int[] toCustomerCoordinates = new int[]{toCustomer.getX(), toCustomer.getY()};
        totalDistance += Euclidian.distance(depotCoordinates, toCustomerCoordinates);
        //// System.out.println("Adding distance from " + Arrays.toString(depotCoordinates) + " to " + Arrays.toString(toCustomerCoordinates));


        // Add distances between customers
        Customer fromCustomer = toCustomer;
        for (int i = 1; i < route.size(); i++) {
            int[] fromCustomerCoordinates = new int[]{fromCustomer.getX(), fromCustomer.getY()};

            toCustomer = depot.getCustomer(route.get(i));
            toCustomerCoordinates = new int[]{toCustomer.getX(), toCustomer.getY()};

            totalDistance += Euclidian.distance(fromCustomerCoordinates, toCustomerCoordinates);
            //// System.out.println("Adding distance from " + Arrays.toString(fromCustomerCoordinates) + " to " + Arrays.toString(toCustomerCoordinates));

            fromCustomer = toCustomer;
        }

        // Add distance from last customer and back to depot
        totalDistance += Euclidian.distance(toCustomerCoordinates, depotCoordinates);
        //// System.out.println("Adding distance from " + Arrays.toString(toCustomerCoordinates) + " to " + Arrays.toString(depotCoordinates));
        System.out.println("=");
        System.out.println("Route distance: " + totalDistance);
        return totalDistance;
    }


    public static double getRouteDemand(Depot depot, List<Integer> route) {
        double totalDemand = 0;
        for (Integer customerId : route) {
            totalDemand += depot.getCustomer(customerId).getDemand();
        }
        return totalDemand;
    }
}
