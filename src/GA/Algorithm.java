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
        this.printCustomerInfo(customer10, 10);
        this.printBorderCustomers(customers);

        System.out.println("------------------------");
        for (Depot depot: depots) {
            System.out.println(depot.getCustomerIds());
        }

        // Initialize population
        System.out.println("\nInitial population:");
        List<Individual> population = Initializer.init(10, depots);
        for (Individual individual: population) {
            System.out.println(individual);
        }

        // Evaluate fitness
        Metrics.evaluatePopulation(depots, population);

        // ---------------------------------------------------------------------------- //
        // For each generation
        //// While offspring size < parents size
        ////// Select 2 competitor pairs and get to winners to be parents
        ////// Apply crossover
        ////// Apply mutation, both intra and inter depot
        ////// Elite replacement
        //// Evaluate generation
        // ---------------------------------------------------------------------------- //


        // Sort / rank population here.

        Individual bestIndividual = population.get(0);
        List<Depot> solutionDepots = this.manager.assignCustomerToDepotsFromIndividual(bestIndividual);

        return new Solution(solutionDepots, bestIndividual);
    }


    private void printCustomerInfo(Customer customer, int customerId) {
        System.out.println("\nCustomer example: Customer " + customerId);
        System.out.println("------------------------");
        System.out.println("Id:                 " + customer.getId());
        System.out.println("X coordinate:       " + customer.getX());
        System.out.println("Y coordinate:       " + customer.getY());
        System.out.println("Duration:           " + customer.getDuration());
        System.out.println("Demand:             " + customer.getDemand());
    }
    private void printBorderCustomers(List<Customer> customers) {
        List<Integer> borderLineCustomerIds = new ArrayList<>();
        for (Customer customer: customers) {
            if (customer.getOnBorder()) {
                borderLineCustomerIds.add(customer.getId());
            }
        }
        String borderLineCustomerIdsString = Arrays.toString(borderLineCustomerIds.toArray());
        System.out.println("\nCustomers on borderline:");
        System.out.println(borderLineCustomerIdsString);
    }
}
