import GA.Components.Population.Individual;
import GA.Components.Population.Initializer;
import MDVRP.Customer;
import MDVRP.Manager;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import MDVRP.Depot;

// Take a look at https://zetcode.com/gui/javafx/canvas/


public class Main extends Application {

    @Override
    public void start(Stage stage) {
        Manager m = new Manager("data/problems/p01", 5);
        m.assignCustomersToDepots();

        List<Customer> customers = m.getCustomers();
        List<Depot> depots = m.getDepots();
        int numCustomers = customers.size();
        int numDepots = depots.size();
        System.out.println("Number of customer: " + numCustomers);
        System.out.println("Number of depots:   " + numDepots);
        Customer customer10 = customers.get(9);
        System.out.println("\nCustomer example: Customer 10");
        System.out.println("------------------------");
        System.out.println("Id:                 " + customer10.getId());
        System.out.println("X coordinate:       " + customer10.getX());
        System.out.println("Y coordinate:       " + customer10.getY());
        System.out.println("Duration:           " + customer10.getDuration());
        System.out.println("Demand:             " + customer10.getDemand());
        System.out.println("Depot:              " + customer10.getDepot());

        List<Integer> borderLineCustomerIds = new ArrayList<>();
        for (Customer customer: customers) {
            if (customer.getOnBorder()) {
                borderLineCustomerIds.add(customer.getId());
            }
        }
        String borderLineCustomerIdsString = Arrays.toString(borderLineCustomerIds.toArray());
        System.out.println("\nCustomers on borderline:");
        System.out.println(borderLineCustomerIdsString);

        System.out.println("------------------------");
        for (Depot depot: depots) {
            System.out.println(depot.getCustomerIds());
        }

        ////-- Test GA initializer
        System.out.println("\nInitial population:");
        List<Individual> population = Initializer.init(10, depots);
        for (Individual individual: population) {
            System.out.println(individual);
        }
        ////--

        ////*** Next: Write RouteScheduler to create route from one individual. And display in graphics window
        ////***

        // Create the Canvas
        Canvas canvas = new Canvas(420, 420);
        //// Set the width/height of the Canvas canvas: canvas.setWidth(400) - canvas.setHeight(400);

        // Get the graphics context of the canvas
        GraphicsContext gc = canvas.getGraphicsContext2D();

        this.drawCustomers(gc, depots);


        // Create the Pane
        Pane root = new Pane();
        // Add the Canvas to the Pane
        root.getChildren().add(canvas);
        // Create the Scene
        Scene scene = new Scene(root);
        // Add the Scene to the Stage
        stage.setScene(scene);
        // Set the Title of the Stage
        stage.setTitle("Solution");
        // Display the Stage
        stage.show();
    }

    private void drawCustomers(GraphicsContext gc, List<Depot> depots) {
        System.out.println(depots);

        Color[] colors = new Color[]{Color.CADETBLUE, Color.CORAL, Color.CORNFLOWERBLUE,
                Color.DARKGOLDENROD, Color.DARKMAGENTA, Color.DARKSALMON};
        int colorIndex = 0;

        for (Depot depot: depots) {
            // draw depot
            gc.setFill(Color.BLACK);
            int x = depot.getX() * 5;  // scale up everything by a factor = 4
            int y = depot.getY() * 5;
            gc.fillRect(x, y, 6, 6);

            // draw all customers belonging to depots
            for (Customer customer: depot.getCustomers()) {
                int size = 6;
                if (customer.getOnBorder()) {
                    size = 9;
                }
                gc.setFill(colors[colorIndex]);
                int xx = customer.getX() * 5;  // scale up everything by a factor = 4
                int yy = customer.getY() * 5;
                gc.fillOval(xx, yy, size, size);
            }
            colorIndex++;
        }

    }


    public static void main(String[] args) {
        Application.launch(args);
    }
}
