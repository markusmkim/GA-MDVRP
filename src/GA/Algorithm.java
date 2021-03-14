package GA;

import GA.Components.Individual;
import GA.Operations.Crossover;
import GA.Operations.Initializer;
import GA.Operations.Mutation;
import GA.Operations.Selection;
import MDVRP.*;

import java.util.*;
import java.util.stream.Collectors;


public class Algorithm {
    private Manager manager;
    private Metrics metrics;
    private Crossover crossover;
    private Mutation mutation;
    private int populationSize = 100;
    private int numberOfGenerations = 500;
    private double fitnessBias = 0.8;
    private double crossoverRate = 0.8;
    private double mutationRate = 0.003;
    private int eliteReplacement = 10;

    public Algorithm(Manager manager) {
        this.manager = manager;
        this.metrics = new Metrics(manager);
        this.crossover = new Crossover(manager, this.metrics, this.crossoverRate);
        this.mutation = new Mutation(this.mutationRate);
    }

    public Solution run() {
        List<Customer> customers = this.manager.getCustomers();
        List<CrowdedDepot> crowdedDepots = this.manager.assignCustomersToDepots();

        int numCustomers = customers.size();
        int numDepots = crowdedDepots.size();
        System.out.println("Number of customer: " + numCustomers);
        System.out.println("Number of depots:   " + numDepots);

        System.out.println("------------------------");
        for (CrowdedDepot depot: crowdedDepots) {
            System.out.println(depot.getCustomerIds());
        }

        // Initialize population
        List<Individual> population = Initializer.init(this.populationSize, crowdedDepots);
        System.out.println("Initial population size: " + population.size());


        // Evaluate fitness
        double averageDistance = this.evaluatePopulation(population);
        this.evaluateFeasibility(population);
        System.out.println("Generation: 0  |  Population size: " + population.size() + "  | Average total distance: " + averageDistance);

        // ---------------------------------------------------------------------------- //
        Collections.sort(population);
        List<Individual> bestParents = new ArrayList<>();
        for (int i = 0; i < this.eliteReplacement; i++) {
            bestParents.add(population.get(i).getCopy());
        }
        this.evaluatePopulation(bestParents);
        this.evaluateFeasibility(bestParents);
        // For each generation
        for (int generation = 1; generation <= this.numberOfGenerations; generation++) {
            List<Individual> offspring = new ArrayList<>();

            // While offspring size < parents size
            while (offspring.size() < population.size()) {
                // Select 2 competitor pairs
                Individual[] competitorPair1 = Selection.selectCompetitorPair(population);
                Individual[] competitorPair2 = Selection.selectCompetitorPair(population);

                // Select parents from tournament selection
                Individual parent1 = Selection.runTournamentSelection(competitorPair1[0], competitorPair1[1], this.fitnessBias);
                Individual parent2 = Selection.runTournamentSelection(competitorPair2[0], competitorPair2[1], this.fitnessBias);

                // Apply crossover
                Individual[] offspringPair = this.crossover.apply(parent1, parent2);

                // Apply mutation
                for (Individual offspringIndividual : offspringPair) {
                    this.mutation.apply(offspringIndividual);
                }

                // add offspring pair to offspring
                Collections.addAll(offspring, offspringPair);

                // Elite replacement ??

            }
            averageDistance = this.evaluatePopulation(offspring);
            population = offspring;
            Collections.sort(population);

            // Elitism
            population = population.subList(0, population.size() - this.eliteReplacement);
            population.addAll(bestParents);
            Collections.sort(population);
            this.evaluateFeasibility(population);
            if (generation % 100 == 0) {
                System.out.println("Generation: " + generation +
                        "  |  Population size: " + population.size() +
                        "  | Average total distance: " + averageDistance +
                        "  | Best individual distance " + population.get(0).getFitness() +
                        "  | Best individual is feasible: " + population.get(0).isFeasible() +
                        "  | Worst individual distance " + population.get(population.size() - 1).getFitness());
            }
            bestParents = new ArrayList<>();
            for (int i = 0; i < this.eliteReplacement; i++) {
                bestParents.add(population.get(i).getCopy());
            }
            this.evaluatePopulation(bestParents);
            this.evaluateFeasibility(bestParents);

            /*
            if (generation == this.numberOfGenerations / 2) {
                System.out.println("Cutting population");
                population = population.subList(0, population.size() - 21);
            }
             */
        }


        this.evaluateFeasibility(population);

        // Sort last population
        Collections.sort(population);
        int fCounter = 0;
        for (Individual individual : population) {
            if (individual.isFeasible()) { fCounter++; }
        }
        System.out.println("Number of feasible solutions: " + fCounter);

        // Get result
        Individual bestIndividual = population.get(0);
        System.out.println("Result is feasible " + bestIndividual.isFeasible());
        System.out.println(bestIndividual);
        this.printRouteDemands(bestIndividual);
        List<CrowdedDepot> solutionDepots = this.manager.assignCustomerToDepotsFromIndividual(bestIndividual);
        for (CrowdedDepot depot: solutionDepots) {
            System.out.println(depot.getCustomerIds());
        }


        return new Solution(solutionDepots, bestIndividual, this.metrics);
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

    private double evaluatePopulation(List<Individual> population) {
        double totalDistanceForPop = 0;
        for (Individual individual : population) {
            if (individual.getFitness() == 0) {
                double fitness = this.metrics.getTotalDistance(individual);
                individual.setFitness(fitness);
                totalDistanceForPop += fitness;
            }
            else {
                totalDistanceForPop += individual.getFitness();
            }
        }
        return totalDistanceForPop / population.size();
    }

    private void evaluateFeasibility(List<Individual> population) {
        for (Individual individual : population) {
            boolean isFeasible = this.metrics.isIndividualFeasible(individual);
            individual.setIsFeasible(isFeasible);
        }
    }

    private void printRouteDemands(Individual individual) {
        System.out.println("Route demands");
        for (Map.Entry<Integer, List<List<Integer>>> entry : individual.getChromosome().entrySet()) {
            int key = entry.getKey();
            System.out.println("Depot " + key);
            List<List<Integer>> chromosomeDepot = entry.getValue();
            int routeIndex = 1;
            for (List<Integer> route: chromosomeDepot) {
                int demand = 0;
                for (int customerID : route) {
                    Customer c = this.manager.getCustomer(customerID);
                    demand += c.getDemand();
                }
                System.out.println("Route " + routeIndex + ": " + demand);
                routeIndex++;
            }
        }
    }

}
