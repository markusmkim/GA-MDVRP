package GA;

import GA.Components.Individual;
import GA.Operations.Initializer;
import MDVRP.Customer;
import MDVRP.Depot;
import MDVRP.Manager;
import MDVRP.Solution;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Algorithm {
    private Manager manager;

    public Algorithm(Manager manager) {
        this.manager = manager;
    }

    public Solution run() {
        List<Customer> customers = this.manager.getCustomers();
        List<Depot> depots = this.manager.assignCustomersToDepots();

        int numCustomers = customers.size();
        int numDepots = depots.size();
        System.out.println("Number of customer: " + numCustomers);
        System.out.println("Number of depots:   " + numDepots);
        Customer customer10 = customers.get(9);
        System.out.println("\nCustomer example: Customer 10");
        System.out.println("------------------------");
        System.out.println("Id:                 " + customer10.getId());
        System.out.println("X coordinate:       " + customer10.getX());
        System.out.println("Y coordinate:       " + customer10.getY());
        System.out.println("Duration:           " + customer10.getDuration());
        System.out.println("Demand:             " + customer10.getDemand());

        List<Integer> borderLineCustomerIds = new ArrayList<>();
        for (Customer customer: customers) {
            if (customer.getOnBorder()) {
                borderLineCustomerIds.add(customer.getId());
            }
        }
        String borderLineCustomerIdsString = Arrays.toString(borderLineCustomerIds.toArray());
        System.out.println("\nCustomers on borderline:");
        System.out.println(borderLineCustomerIdsString);

        System.out.println("------------------------");
        for (Depot depot: depots) {
            System.out.println(depot.getCustomerIds());
        }

        ////-- Test GA initializer
        System.out.println("\nInitial population:");
        List<Individual> population = Initializer.init(10, depots);
        for (Individual individual: population) {
            System.out.println(individual);
        }

        Metrics.evaluatePopulation(depots, population);

        // Sort / rank population here.

        Individual bestIndividual = population.get(0);
        List<Depot> solutionDepots = this.manager.assignCustomerToDepotsFromIndividual(bestIndividual);
        Solution solution = new Solution(solutionDepots, bestIndividual);
        return solution;
    }
}
