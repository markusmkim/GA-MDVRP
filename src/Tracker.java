import GA.Algorithm;
import MDVRP.Manager;
import Utils.Stats;

import java.util.ArrayList;
import java.util.List;


public class Tracker {
    /*
    Runs through algorithm multiple times and tracks statistics
     */
    public static void main(String[] args) {
        String problem = "p10";

        List<List<Double>> histories = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Manager manager = new Manager("data/problems/" + problem, 0.5);
            Algorithm ga = new Algorithm(manager);
            List<Double> history = new ArrayList<>();
            histories.add(history);
            ga.run(history);                                                            // Run algorithm
        }

        List<Double> averagedHistories = Stats.averageHistories(histories);             // Find Mean Best Fitness

        Manager.saveProgression(averagedHistories, "data/progression/" + problem + ".csv");
    }
}
