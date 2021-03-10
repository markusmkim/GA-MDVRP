package MDVRP;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RouteScheduler {

    public static List<List<Integer>> getInitialRoutes(Depot depot) {
        List<Customer> customers = new ArrayList<>(depot.getCustomers());   // make COPY of customers
        Collections.shuffle(customers);                                     // then shuffle copied list

        int maxDuration = depot.getMaxDuration();
        if (maxDuration == 0) {
            maxDuration = 1000000000;
        }
        int maxVehicleLoad = depot.getMaxVehicleLoad();
        //////System.out.println("Max vehicle load: " + maxVehicleLoad);
        int maxVehicles = depot.getMaxVehicles();

        List<List<Integer>> routes = new ArrayList<>();
        int numberOfVehiclesInUse = 0;

        int currentRouteDuration = 0;
        int currentAggregatedVehicleLoad = 0;

        List<Integer> route = new ArrayList<>();
        for (Customer customer : customers) {
            boolean demandConstraintHolds = currentAggregatedVehicleLoad + customer.getDemand() <= maxVehicleLoad;
            boolean durationConstraintHolds = currentRouteDuration + customer.getDuration() <= maxDuration;
            if (demandConstraintHolds && durationConstraintHolds) {
                route.add(customer.getId());
                currentAggregatedVehicleLoad += customer.getDemand();
                currentRouteDuration += customer.getDuration();
            }
            else {
                routes.add(route);          // Add current route to router
                route = new ArrayList<>();  // Reset current route values
                route.add(customer.getId());
                currentRouteDuration = customer.getDuration();
                currentAggregatedVehicleLoad = customer.getDemand();
                numberOfVehiclesInUse += 1;
            }
        }
        if (route.size() > 0) {  // Add last route
            routes.add(route);
            numberOfVehiclesInUse += 1;
        }
        if (numberOfVehiclesInUse > maxVehicles) {
            System.out.println("\n -------------------------\n >> Too many vehicles! <<\n -------------------------\n" );
        }
        return routes;
    }
}
