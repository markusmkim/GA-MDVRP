package MDVRP;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/*
A depot with assigned customers
 */
public class CrowdedDepot extends Depot {
    private List<Customer> customers = new ArrayList<>();


    public CrowdedDepot(Integer id, Integer x, Integer y, Integer maxVehicles, Integer maxDuration, Integer maxVehicleLoad) {
        super(id, x, y, maxVehicles, maxDuration, maxVehicleLoad);
    }

    public CrowdedDepot(Depot depot) {
        super(depot.getId(), depot.getX(), depot.getY(), depot.getMaxVehicles(), depot.getMaxDuration(), depot.getMaxVehicleLoad());
    }


    // Getters //
    public List<Customer> getCustomers()    { return this.customers; }

    public Customer getCustomer(int id) {
        return this.customers.stream().filter(c -> id == c.getId()).findAny().orElse(null);
    }

    public String getCustomerIds() {
        String customerIds = Arrays.toString(this.customers.toArray());
        return "Depot " + this.getId() + " has customers: " + customerIds;
    }


    // Setters //
    public void setX(int x) { super.setX(x); }  // Used to shift coordinates before plotting
    public void setY(int y) { super.setY(y); }


    public void addCustomer(Customer customer) {
        this.customers.add(customer);
    }

    public void removeCustomer(Customer customer) {
        if (this.customers.contains(customer)) {
            this.customers.remove(customer);
        }
    }

}
