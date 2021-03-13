import GA.Algorithm;
import GA.Components.Individual;
import MDVRP.*;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.List;

// Take a look at https://zetcode.com/gui/javafx/canvas/


public class Main extends Application {

    @Override
    public void start(Stage stage) {
        Manager manager = new Manager("data/problems/p03", 5);

        Algorithm ga = new Algorithm(manager);

        // Run genetic algorithm and get best solution found
        Solution solution = ga.run();

        List<CrowdedDepot> depots = solution.getDepots();
        double solutionCost = solution.getIndividual().getFitness();
        System.out.println("\nTotal distance best solution: " + solutionCost);


        Canvas canvas = new Canvas(420, 420);                 // Create the Canvas
        GraphicsContext gc = canvas.getGraphicsContext2D();                 // Get the graphics context of the canvas

        this.drawCustomers(gc, depots, solution.getIndividual());           // Draw solution

        Pane root = new Pane();                                             // Create the Pane
        root.getChildren().add(canvas);                                     // Add the Canvas to the Pane
        Scene scene = new Scene(root);                                      // Create the Scene
        stage.setScene(scene);                                              // Add the Scene to the Stage
        stage.setTitle("Solution");                                         // Set the Title of the Stage
        stage.show();                                                       // Display the Stage
    }


    private void drawCustomers(GraphicsContext gc, List<CrowdedDepot> depots, Individual individual) {
        System.out.println(depots);

        Color[] colors = new Color[]{Color.CADETBLUE, Color.CORAL, Color.CORNFLOWERBLUE,
                Color.DARKGOLDENROD, Color.DARKMAGENTA, Color.DARKSALMON};
        int colorIndex = 0;

        for (CrowdedDepot depot: depots) {
            // draw depot
            gc.setFill(Color.BLACK);
            int depotSize = 10;
            int x = (depot.getX() * 5) - (depotSize / 2);  // scale up everything by a factor = 5
            int y = (depot.getY() * 5) - (depotSize / 2);
            gc.fillRect(x, y, depotSize, depotSize);

            // draw all customers belonging to depots
            gc.setFill(colors[colorIndex]);
            for (Customer customer: depot.getCustomers()) {
                int size = 6;
                if (customer.getOnBorder()) {
                    size = 10;
                }
                int xx = (customer.getX() * 5) - (size / 2);  // scale up everything by a factor = 5
                int yy = (customer.getY() * 5) - (size / 2);
                gc.fillOval(xx, yy, size, size);
            }
            gc.setStroke(colors[colorIndex]);
            this.drawRoutes(gc, depot, individual);
            colorIndex++;
        }
    }


    private void drawRoutes(GraphicsContext gc, CrowdedDepot depot, Individual individual) {
        List<List<Integer>> routes = individual.getChromosome().get(depot.getId());

        for (List<Integer> route : routes) {
            double[] xPoints = new double[route.size() + 1];
            double[] yPoints = new double[route.size() + 1];

            xPoints[0] = depot.getX() * 5;
            yPoints[0] = depot.getY() * 5;

            for (int i = 0; i < route.size(); i++) {
                Customer customer = depot.getCustomer(route.get(i));
                xPoints[i + 1] = customer.getX() * 5;
                yPoints[i + 1] = customer.getY() * 5;
            }

            gc.strokePolygon(xPoints, yPoints, xPoints.length);
        }
    }


    public static void main(String[] args) {
        Application.launch(args);
    }
}
