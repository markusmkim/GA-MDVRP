package GA.Components.Population;

import MDVRP.Customer;
import MDVRP.Depot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class Initializer {

    public static List<Individual> init(Integer populationSize, List<Depot> depots) {
        List<Individual> population = new ArrayList<>();

        for (int i = 0; i < populationSize; i++) {
            List<List<Integer>> chromosome = new ArrayList<>();
            for (Depot depot: depots) {
                List<Integer> chromosomeDepot = new ArrayList<>();
                List<Customer> customers = new ArrayList<>(depot.getCustomers());   // make COPY of customers
                Collections.shuffle(customers);                                     // then shuffle copied list
                for (Customer customer: customers) {
                    chromosomeDepot.add(customer.getId());
                }
                chromosome.add(chromosomeDepot);
            }
            Individual individual = new Individual(chromosome);
            population.add(individual);
        }
        return population;
    }
}
