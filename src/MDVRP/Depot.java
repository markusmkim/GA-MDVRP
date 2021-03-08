package MDVRP;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;


public class Depot {
    private int id;
    private int x;
    private int y;
    private int maxVehicles;
    private int maxDuration;
    private int maxVehicleLoad;  // Capacity
    private List<Customer> customers = new ArrayList<>();


    public Depot(Integer id, Integer x, Integer y, Integer maxVehicles, Integer maxDuration, Integer maxVehicleLoad) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.maxVehicles = maxVehicles;
        this.maxDuration = maxDuration;
        this.maxVehicleLoad = maxVehicleLoad;
    }


    public void addCustomer(Customer customer, boolean onBorderline) {
        this.customers.add(customer);
        customer.setDepot(this, onBorderline);
    }

    public void removeCustomer(Customer customer) {
        if (this.customers.contains(customer)) {
            this.customers.remove(customer);
        }
    }


    public int getId()                      { return this.id; }
    public int getX()                       { return this.x; }
    public int getY()                       { return this.y; }
    public int getMaxDuration()             { return maxDuration; }
    public int getMaxVehicles()             { return this.maxVehicles; }
    public int getMaxVehicleLoad()          { return maxVehicleLoad; }
    public List<Customer> getCustomers()    { return this.customers; }

    public String getCustomerIds() {
        String customerIds = Arrays.toString(this.customers.toArray());
        return "Depot " + this.getId() + " has customers: " + customerIds;
    }

    @Override
    public String toString() {
        // return depot id
        return "" + this.id;
    }
}
