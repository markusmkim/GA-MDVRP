package MDVRP;

import GA.Components.Route;
import GA.Metrics;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class RouteScheduler {

    public static List<Route> getInitialRoutes(CrowdedDepot depot, Metrics metrics) {
        /*
        Creates routes from a crowded depot, with a bias towards feasibility.
         */
        List<Customer> customers = new ArrayList<>(depot.getCustomers());   // make copy of customers
        Collections.shuffle(customers);                                     // then shuffle copied list

        int maxVehicleLoad = depot.getMaxVehicleLoad();

        List<Route> routes = new ArrayList<>();

        int currentAggregatedVehicleLoad = 0;

        List<Integer> route = new ArrayList<>();
        for (Customer customer : customers) {
            boolean demandConstraintHolds = currentAggregatedVehicleLoad + customer.getDemand() <= maxVehicleLoad;
            if (demandConstraintHolds) {
                route.add(customer.getId());
                currentAggregatedVehicleLoad += customer.getDemand();
            }
            else {
                routes.add(new Route(route, currentAggregatedVehicleLoad, metrics.getRouteDistance(depot.getId(), route)));     // Add current route to router
                route = new ArrayList<>();                                                                                      // Reset current route values
                route.add(customer.getId());
                currentAggregatedVehicleLoad = customer.getDemand();
            }
        }
        if (route.size() > 0) {                                                                                                 // Add last route
            routes.add(new Route(route, currentAggregatedVehicleLoad, metrics.getRouteDistance(depot.getId(), route)));
        }

        return routes;
    }

}
