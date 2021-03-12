package GA.Operations;

import GA.Components.Individual;

import java.util.List;
import java.util.Random;


public class Selection {

    public static Individual[] selectCompetitorPair(List<Individual> population) {
        Random random = new Random();
        Individual p1 = population.get(random.nextInt(population.size()));
        Individual p2 = population.get(random.nextInt(population.size()));
        return new Individual[]{p1, p2};
    }


    // Fitness bias = 0.8 in the paper
    public static Individual runTournamentSelection(Individual player1, Individual player2, double fitnessBias) {
        Individual[] winners = new Individual[2];

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
