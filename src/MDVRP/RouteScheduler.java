package MDVRP;

import GA.Components.Route;
import GA.Metrics;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RouteScheduler {

    public static List<Route> getInitialRoutes(CrowdedDepot depot, Metrics metrics) {
        List<Customer> customers = new ArrayList<>(depot.getCustomers());   // make COPY of customers
        Collections.shuffle(customers);                                     // then shuffle copied list

        int maxDuration = depot.getMaxDuration();
        int maxVehicleLoad = depot.getMaxVehicleLoad();
        int maxVehicles = depot.getMaxVehicles();

        List<Route> routes = new ArrayList<>();
        int numberOfVehiclesInUse = 0;

        double currentRouteDistance = 0;
        int currentAggregatedVehicleLoad = 0;

        List<Integer> route = new ArrayList<>();
        for (Customer customer : customers) {
            boolean demandConstraintHolds = currentAggregatedVehicleLoad + customer.getDemand() <= maxVehicleLoad;
            boolean distanceConstraintHolds = true;
            /*
            if (maxDuration > 0) {
                List<Integer> routeCopy = new ArrayList<>(route);
                routeCopy.add(customer.getId());
                distanceConstraintHolds = metrics.getRouteDistance(depot.getId(), routeCopy) <= maxDuration;
            }
             */

            if (demandConstraintHolds && distanceConstraintHolds) {
                route.add(customer.getId());
                currentAggregatedVehicleLoad += customer.getDemand();
            }
            else {
                routes.add(new Route(route, currentAggregatedVehicleLoad, metrics.getRouteDistance(depot.getId(), route)));          // Add current route to router
                route = new ArrayList<>();  // Reset current route values
                route.add(customer.getId());
                currentAggregatedVehicleLoad = customer.getDemand();
                numberOfVehiclesInUse += 1;
            }
        }
        if (route.size() > 0) {  // Add last route
            routes.add(new Route(route, currentAggregatedVehicleLoad, metrics.getRouteDistance(depot.getId(), route)));
            numberOfVehiclesInUse += 1;
        }
        if (numberOfVehiclesInUse > maxVehicles) {
            System.out.println("\n -------------------------\n >> Too many vehicles! <<\n -------------------------\n" );
        }
        return routes;
    }

}
