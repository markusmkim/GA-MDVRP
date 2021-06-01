import GA.Algorithm;
import MDVRP.Manager;
import MDVRP.Solution;

import java.util.ArrayList;
import java.util.List;

public class Average {
    /*
    Runs through all problems multiple times and saves average result
     */
    public static void main(String[] args) {
        double[] distanceVector = new double[23];
        int rounds = 2;

        for (int round = 0; round < rounds; round++) {
            for (int i = 1; i <= 23; i++) {
                String problem = i < 10 ? "p0" + i : "p" + i;
                Manager manager = new Manager("data/problems/" + problem, 0.5);
                Algorithm ga = new Algorithm(manager);
                Solution solution = ga.run();                                                    // Run algorithm
                double solutionCost = solution.getIndividual().getFitness();
                distanceVector[i - 1] += solutionCost;
            }
        }

        for (int j = 0; j < 23; j++) {
            distanceVector[j] = distanceVector[j] / rounds;                                         // Average results
        }

        Manager.saveAverageResults(distanceVector, "data/solutions/average" + ".csv");
    }
}
