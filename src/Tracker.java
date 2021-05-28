import GA.Algorithm;
import MDVRP.Manager;
import Utils.Visualizer;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;


public class Tracker extends Application {

    @Override
    public void start(Stage stage) {
        String problem = "p06";

        Manager manager = new Manager("data/problems/" + problem, 0.5); // 0.46

        Algorithm ga = new Algorithm(manager);

        List<List<Double>> histories = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            List<Double> history = new ArrayList<>();
            histories.add(history);
            ga.run(history);                                                // Run algorithm
        }

        int height = 500;
        int width = 800;
        Canvas canvas = new Canvas(width, height);                          // Create the Canvas
        GraphicsContext gc = canvas.getGraphicsContext2D();                 // Get the graphics context of the canvas

        Visualizer visualizer = new Visualizer(gc);
        visualizer.plotMeanProgression(histories);                          // Plot progression

        // Show results
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
