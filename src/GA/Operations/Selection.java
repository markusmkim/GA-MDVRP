package GA.Operations;

import GA.Components.Individual;

import java.util.List;
import java.util.Random;

/*
Class for selection operator: Selecting a parent to undergo reproduction.
 */
public class Selection {
    public static Individual[] selectCompetitorPair(List<Individual> population) {
        /*
        Selects two random individuals from population.
         */
        Random random = new Random();
        Individual p1 = population.get(random.nextInt(population.size()));
        Individual p2 = population.get(random.nextInt(population.size()));
        return new Individual[]{p1, p2};
    }


    public static Individual runTournamentSelection(Individual player1, Individual player2, double fitnessBias) {
        /*
        Runs tournament selection between two candidates.
        Fitness bias controls how often the tournament is played out. If it is, best individuals wins.
        If not, choose random individual.
         */
        Individual chosenIndividual = player1;
        if (Math.random() < fitnessBias) {
            if (player2.getFitness() > player1.getFitness()) {
                chosenIndividual = player2;
            }
        }
        else {
            if (Math.random() < 0.5) {
                chosenIndividual = player2;
            }
        }
        return chosenIndividual;
    }

}
