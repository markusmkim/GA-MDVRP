package Utils;

import GA.Components.Individual;
import GA.Components.Route;
import MDVRP.CrowdedDepot;
import MDVRP.Customer;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.*;


public class Visualizer {
    private double scalingFactor;
    private GraphicsContext gc;


    public Visualizer(GraphicsContext gc) {
        this.gc = gc;
    }


    public void plotSolution(List<CrowdedDepot> depots, Individual individual) {
        // prepare data for plotting
        this.scaleTransform(depots);

        // plot data
        for (CrowdedDepot depot: depots) {
            this.drawCustomers(depot);
            this.gc.setLineWidth(1.2);
            this.drawRoutes(depot, individual);
        }
    }


    private void drawCustomers(CrowdedDepot depot) {
        // draw depot
        this.gc.setFill(Color.BLACK);
        double depotSize = 10;
        double x = (depot.getX() * this.scalingFactor) - (depotSize / 2);
        double y = (depot.getY() * this.scalingFactor) - (depotSize / 2);
        this.gc.fillRect(x, y, depotSize, depotSize);

        // draw all customers belonging to depots
        for (Customer customer: depot.getCustomers()) {
            double size = 6;
            double xx = (customer.getX() * this.scalingFactor) - (size / 2);
            double yy = (customer.getY() * this.scalingFactor) - (size / 2);
            gc.fillOval(xx, yy, size, size);
        }
    }


    private void drawRoutes(CrowdedDepot depot, Individual individual) {
        // Possible colors for a route
        Color[] colors = new Color[]{
                Color.CADETBLUE,
                Color.CORAL,
                Color.DARKSLATEBLUE,
                Color.CYAN,
                Color.DARKGOLDENROD,
                Color.DARKMAGENTA,
                Color.BLUEVIOLET,
                Color.CRIMSON,
                Color.DEEPPINK,
                Color.DARKORANGE,
        };

        Random random = new Random();

        int colorIndex = random.nextInt(10);

        List<Route> routes = individual.getChromosome().get(depot.getId());

        for (Route route : routes) {
            double[] xPoints = new double[route.getRoute().size() + 1];
            double[] yPoints = new double[route.getRoute().size() + 1];

            xPoints[0] = depot.getX() * this.scalingFactor;
            yPoints[0] = depot.getY() * this.scalingFactor;

            for (int i = 0; i < route.getRoute().size(); i++) {
                Customer customer = depot.getCustomer(route.getRoute().get(i));
                xPoints[i + 1] = customer.getX() * this.scalingFactor;
                yPoints[i + 1] = customer.getY() * this.scalingFactor;
            }

            this.gc.setStroke(colors[colorIndex]);
            this.gc.strokePolygon(xPoints, yPoints, xPoints.length);
            colorIndex = (colorIndex + 1) % 10;
        }
    }


    private void scaleTransform(List<CrowdedDepot> depots) {
        int maxX = 0;
        int maxY = 0;
        int minX = 0;
        int minY = 0;
        for (CrowdedDepot depot : depots) {
            for (Customer customer : depot.getCustomers()) {
                if (customer.getX() > maxX) { maxX = customer.getX(); }
                if (customer.getY() > maxY) { maxY = customer.getY(); }
                if (customer.getX() < minX) { minX = customer.getX(); }
                if (customer.getY() < minY) { minY = customer.getY(); }
            }
        }

        // Compute appropriate scaling factor
        int diffX = maxX - minX;
        int diffY = maxY - minY;
        double size = Integer.max(diffX, diffY);

        this.scalingFactor = 470 / size;

        // Transform all coordinates to positive axes
        if (minX < 0 || minY < 0) {
            for (CrowdedDepot depot : depots) {
                if (minX < 0) {
                    depot.setX(depot.getX() - minX + 10);
                }
                if (minX < 0) {
                    depot.setY(depot.getY() - minY + 10);
                }
                for (Customer customer : depot.getCustomers()) {
                    if (minX < 0) {
                        customer.setX(customer.getX() - minX + 10);
                    }
                    if (minX < 0) {
                        customer.setY(customer.getY() - minY + 10);
                    }
                }
            }
        }
    }
}
