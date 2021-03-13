package GA.Operations;

import GA.Components.Individual;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Mutation {
    private double mutationRate;

    public Mutation(double mutationRate) {
        this.mutationRate = mutationRate;
    }


    public void apply(Individual individual) {
        if (Math.random() < mutationRate) {
            this.applyReversal(individual);
        }
    }


    private void applyReversal(Individual individual) {
        // System.out.println("Before mutation");
        // System.out.println(individual);
        Random random = new Random();
        List<Integer> depotIDs = new ArrayList<>(individual.getChromosome().keySet());
        int chosenDepotId = depotIDs.get(random.nextInt(depotIDs.size()));
        // System.out.println("Chosen depot " + chosenDepotId);

        List<List<Integer>> routes = individual.getChromosome().get(chosenDepotId);
        List<Integer> routesFlattened = new ArrayList<>();
        for (List<Integer> route : routes) {
            routesFlattened.addAll(route);
        }
        if (routesFlattened.size() < 2) {
            return;
        }

        int cutPoint1 = random.nextInt(routesFlattened.size());
        int cutPoint2 = cutPoint1;
        while (cutPoint1 == cutPoint2) {
            cutPoint2 = random.nextInt(routesFlattened.size());
        }
        int cutFrom;
        int cutTo;
        if (cutPoint1 < cutPoint2) {
            cutFrom = cutPoint1;
            cutTo = cutPoint2;
        }
        else {
            cutFrom = cutPoint2;
            cutTo = cutPoint1;
        }
        // System.out.println("Cut from " + cutFrom + ", cut to " + cutTo);
        List<Integer> reversedSegment = new ArrayList<>();
        for (int i = cutTo; i >= cutFrom; i--) {
            reversedSegment.add(routesFlattened.get(i));
        }

        List<Integer> augmentedRoutesFlattened = new ArrayList<>(routesFlattened.subList(0, cutFrom));
        augmentedRoutesFlattened.addAll(reversedSegment);
        if (cutTo < routesFlattened.size() - 1) {
            augmentedRoutesFlattened.addAll(routesFlattened.subList(cutTo + 1, routesFlattened.size()));
        }

        List<List<Integer>> augmentedRoutes = new ArrayList<>();
        int flatIndex  = 0;
        for (List<Integer> route : routes) {
            List<Integer> augmentedRoute = new ArrayList<>();
            for (int ii = 0; ii < route.size(); ii++) {
                augmentedRoute.add(augmentedRoutesFlattened.get(flatIndex));
                flatIndex++;
            }
            augmentedRoutes.add(augmentedRoute);
        }

        // apply mutation to chromosome
        individual.getChromosome().put(chosenDepotId, augmentedRoutes);
        // System.out.println("After mutation");
        // System.out.println(individual);
    }
}
