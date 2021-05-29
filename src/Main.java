import GA.Algorithm;
import MDVRP.*;
import Utils.Visualizer;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.util.List;


public class Main extends Application {

    @Override
    public void start(Stage stage) {
        String problem = "p01";

        Manager manager = new Manager("data/problems/" + problem, 0.5); // 0.46

        Algorithm ga = new Algorithm(manager);

        // Run algorithm
        Solution solution = ga.run();

        List<CrowdedDepot> depots = solution.getDepots();

        double solutionCost = solution.getIndividual().getFitness();
        System.out.println("\nTotal distance best solution: " + solutionCost);

        // Save solution to file
        Manager.saveSolution(solution, "data/solutions/" + problem + ".res");

        int height = 500;
        int width = 500;
        Canvas canvas = new Canvas(width, height);                          // Create the Canvas
        GraphicsContext gc = canvas.getGraphicsContext2D();                 // Get the graphics context of the canvas

        Visualizer visualizer = new Visualizer(gc);
        visualizer.plotSolution(depots, solution.getIndividual());          // Plot solution

        // Show result
        Pane root = new Pane();                                             // Create the Pane
        root.getChildren().add(canvas);                                     // Add the Canvas to the Pane
        Scene scene = new Scene(root);                                      // Create the Scene
        stage.setScene(scene);                                              // Add the Scene to the Stage
        stage.setTitle("Solution");                                         // Set the Title of the Stage
        stage.show();                                                       // Display the Stage

        // Save graphical solution to file
        Manager.saveSolutionImage(canvas, height, width, "data/solutionImages/" + problem + ".png");
    }



    public static void main(String[] args) {
        Application.launch(args);
    }
}
