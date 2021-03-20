package GA;

import GA.Components.Individual;
import GA.Components.Route;
import GA.Operations.*;
import MDVRP.*;

import java.util.*;
import java.util.stream.Collectors;


public class Algorithm {
    private Manager manager;
    private Metrics metrics;
    private Crossover crossover;
    private Mutation mutation;
    private int populationSize;
    private int numberOfGenerations;
    private double fitnessGoal;
    private int refinementAfter;
    private double fitnessBias;
    private int eliteReplacement;


    public Algorithm(Manager manager) {
        // ------------------- PARAMS -------------------- //
        int populationSize = 60; // 100
        int numberOfGenerations = 1400;
        double fitnessGoal = 4233;
        int refinementAfter = 600; // 800
        double fitnessBias = 0.8;
        double crossoverRate = 0.8;
        double crossoverFeasibleBalance = 1;
        double mutationRate = 0.05;
        int eliteReplacement = 10; // 20
        int interDepotMutationFreq = 10;
        // ------------------------------------------------//

        this.populationSize = populationSize;
        this.numberOfGenerations = numberOfGenerations;
        this.fitnessGoal = fitnessGoal;
        this.refinementAfter = refinementAfter;
        this.fitnessBias = fitnessBias;
        this.eliteReplacement = eliteReplacement;
        this.manager = manager;
        this.metrics = new Metrics(manager);
        Inserter inserter = new Inserter(manager, this.metrics);
        this.crossover = new Crossover(manager, inserter, crossoverRate, crossoverFeasibleBalance);
        this.mutation = new Mutation(manager, this.metrics, inserter, mutationRate, interDepotMutationFreq);
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

        System.out.println("borderline customers");
        this.printBorderCustomers();

        // Initialize population
        List<Individual> population = Initializer.init(this.populationSize, crowdedDepots, this.metrics);
        System.out.println("Initial population size: " + population.size());


        // Evaluate fitness
        double averageDistance = this.evaluatePopulation(population);
        this.evaluateFeasibility(population);
        System.out.println("Generation: 0  |  Population size: " + population.size() + "  | Average total distance: " + averageDistance);

        // ---------------------------------------------------------------------------- //
        Collections.sort(population);
        List<Individual> bestParents = new ArrayList<>();
        for (int i = 0; i < this.eliteReplacement; i++) {
            bestParents.add(population.get(i).getClone());
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
                    this.mutation.apply(offspringIndividual, generation);
                }

                // add offspring pair to offspring
                Collections.addAll(offspring, offspringPair);

                // Elite replacement ??

            }
            averageDistance = this.evaluatePopulation(offspring);
            population = offspring;
            this.evaluateFeasibility(population);
            Collections.sort(population);

            // Elitism
            population = population.subList(0, population.size() - this.eliteReplacement);
            population.addAll(bestParents);
            Collections.sort(population);

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
                bestParents.add(population.get(i).getClone());
            }
            this.evaluatePopulation(bestParents);
            this.evaluateFeasibility(bestParents);

            if (population.get(0).isFeasible() && population.get(0).getFitness() < this.fitnessGoal) {
                System.out.println("Found sufficient solution --> stopping algorithm");
                break;
            }

            if (generation == this.refinementAfter) {
                this.mutation.setRefinementPhase();
                this.crossover.setCrossoverRate(0.5);
            }
        }


        // this.evaluateFeasibility(population);

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

        List<CrowdedDepot> solutionDepots = this.manager.assignCustomerToDepotsFromIndividual(bestIndividual);
        int aa = 0;
        for (CrowdedDepot depot: solutionDepots) {
            System.out.println(depot.getCustomerIds());
            aa += depot.getCustomers().size();
        }

        System.out.println("Total distance old metric");
        System.out.println(this.metrics.getTotalDistanceOLD(bestIndividual));

        System.out.println("Number of customers in solution: " + aa);
        return new Solution(solutionDepots, bestIndividual, this.metrics);
    }

    private void printBorderCustomers() {
        List<Integer> borderLineCustomerIds = new ArrayList<>();
        for (Customer customer: this.manager.getSwappableCustomers()) {
            borderLineCustomerIds.add(customer.getId());
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
}
