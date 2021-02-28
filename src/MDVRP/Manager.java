package MDVRP;

import java.util.List;
import java.lang.Math;

import Utils.Euclidian;


public class Manager {
    private List<Customer> customers;
    private List<Depot> depots;
    private float borderlineThreshold;


    public Manager(float borderlineThreshold) {
        this.borderlineThreshold = borderlineThreshold;
        // read data and initalize customers and depots
    }


    public void assignCustomersToDepots() {
        for (Customer customer: this.customers) {
            boolean isBorderlineCustomer = false;
            int[] customerCoordinates = new int[]{customer.getX(), customer.getY()};

            Depot firstDepot = this.depots.get(0);
            int[] firstDepotCoordinates = new int[]{firstDepot.getX(), firstDepot.getY()};

            double shortestDistance = Euclidian.distance(customerCoordinates, firstDepotCoordinates);
            Depot currentShortestDepot = firstDepot;

            double shortestOtherDistance = shortestDistance;

            for (Depot depot: this.depots) {
                int[] depotCoordinates = new int[]{depot.getX(), depot.getY()};
                double distance = Euclidian.distance(customerCoordinates, depotCoordinates);
                if (distance < shortestDistance) {
                    shortestOtherDistance = shortestDistance;
                    shortestDistance = distance;
                    currentShortestDepot = depot;
                }
            }

            if (Math.abs(shortestDistance - shortestOtherDistance) < this.borderlineThreshold) {
                isBorderlineCustomer = true;
            }

            currentShortestDepot.addCustomer(customer, isBorderlineCustomer);
        }
    }
}
