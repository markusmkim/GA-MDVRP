package MDVRP;

import java.io.File;
import java.io.FileWriter;
import java.io.FileNotFoundException;
import java.util.*;
import java.lang.Math;
import java.util.stream.Collectors;

import GA.Components.Individual;
import GA.Components.Route;
import GA.Operations.Selection;
import Utils.Euclidian;
import Utils.Formatter;


public class Manager {
    private List<Customer> customers = new ArrayList<>();
    private List<Customer> swappableCustomers = new ArrayList<>();
    private List<Depot> depots = new ArrayList<>();
    private double borderlineThreshold;


    public Manager(String problemFilepath, double borderlineThreshold) {
        this.borderlineThreshold = borderlineThreshold;
        // read data and initialize customers and depots
        this.readData(problemFilepath);
    }


    public List<Customer> getCustomers()            { return this.customers; }
    public List<Customer> getSwappableCustomers()   { return swappableCustomers; }
    public List<Depot> getDepots()                  { return this.depots; }

    public Customer getCustomer(int id) {
        return this.customers.stream().filter(c -> id == c.getId()).findAny().orElse(null);
    }

    public Depot getDepot(int id) {
        return this.depots.stream().filter(d -> id == d.getId()).findAny().orElse(null);
    }

    private List<Depot> copyDepots() {
        // Create copy of depots
        return this.depots.stream().map(Depot::getDepotCopy).collect(Collectors.toList());
    }

    private void readData(String problemFilepath) {
        try {
            File myObj = new File(problemFilepath);
            Scanner fileReader = new Scanner(myObj);

            // Read the first line to get number of vehicles, number of customers, and number of depots
            String firstLine = fileReader.nextLine().trim();
            String[] firstLineArrSplit = firstLine.split("\\s+");

            int nVehicles = Integer.parseInt(firstLineArrSplit[0]);
            int nCustomers = Integer.parseInt(firstLineArrSplit[1]);
            int nDepots = Integer.parseInt(firstLineArrSplit[2]);

            // read each depots maximum specifications
            List<Integer> maxRouteDurationsPerDepot = new ArrayList<>();
            List<Integer> maxLoadEachVehiclePerDepot = new ArrayList<>();
            int lineCounter = 0;
            while (fileReader.hasNextLine() && lineCounter < nDepots) {
                String line = fileReader.nextLine().trim();
                String[] lineArrSplit = line.split("\\s+");

                int maxDuration = Integer.parseInt(lineArrSplit[0]);
                int maxVehicleLoad = Integer.parseInt(lineArrSplit[1]);
                maxRouteDurationsPerDepot.add(maxDuration);
                maxLoadEachVehiclePerDepot.add(maxVehicleLoad);
                lineCounter++;
            }

            // then read customer data and create customers
            lineCounter = 0;
            while (fileReader.hasNextLine() && lineCounter < nCustomers) {
                String line = fileReader.nextLine().trim();
                String[] lineArrSplit = line.split("\\s+");

                int customerId = Integer.parseInt(lineArrSplit[0]);
                int x = Integer.parseInt(lineArrSplit[1]);
                int y = Integer.parseInt(lineArrSplit[2]);
                int duration = Integer.parseInt(lineArrSplit[3]);
                int demand = Integer.parseInt(lineArrSplit[4]);

                // create and add customer
                Customer customer = new Customer(customerId, x, y, duration, demand);
                this.customers.add(customer);
                lineCounter++;
            }

            // at last, read depots coordinates and create depots
            lineCounter = 0;
            while (fileReader.hasNextLine() && lineCounter < nDepots) {
                String line = fileReader.nextLine().trim();
                String[] lineArrSplit = line.split("\\s+");
                //////System.out.println(Arrays.toString(lineArrSplit));
                int x = Integer.parseInt(lineArrSplit[1]);
                int y = Integer.parseInt(lineArrSplit[2]);
                int maxDuration = maxRouteDurationsPerDepot.get(lineCounter);
                int maxVehicleLoad = maxLoadEachVehiclePerDepot.get(lineCounter);

                // create and add depot
                Depot depot = new Depot(lineCounter + 1, x, y, nVehicles, maxDuration, maxVehicleLoad);
                this.depots.add(depot);
                lineCounter++;
            }
            fileReader.close();

        } catch (FileNotFoundException e) {
            System.out.println("Cannot find file...");
            e.printStackTrace();
        }

    }


    public List<CrowdedDepot> assignCustomersToDepots() {
        List<CrowdedDepot> depots = this.copyDepots().stream().map(CrowdedDepot::new).collect(Collectors.toList());

        for (Customer customer: this.customers) {
            boolean isBorderlineCustomer = false;
            Map<Integer, Double> depotDistances = new HashMap<>();
            int[] customerCoordinates = new int[]{customer.getX(), customer.getY()};

            // initial values
            CrowdedDepot firstDepot = depots.get(0);
            double shortestDistance = 1000000000;
            CrowdedDepot currentShortestDepot = firstDepot;

            // loop through depots to find the nearest depot and the distance to that depot,
            // and also the distance to second nearest depot (to check if on borderline)
            for (CrowdedDepot depot: depots) {
                int[] depotCoordinates = new int[]{depot.getX(), depot.getY()};
                double distance = Euclidian.distance(customerCoordinates, depotCoordinates);
                depotDistances.put(depot.getId(), distance);
                if (distance < shortestDistance) {
                    shortestDistance = distance;
                    currentShortestDepot = depot;
                }
            }

            // add customer to the depot closest to the customer
            currentShortestDepot.addCustomer(customer);
            customer.addPossibleDepot(currentShortestDepot.getId());

            for (Map.Entry<Integer, Double> entry : depotDistances.entrySet()) {
                if ((entry.getValue() - shortestDistance) / shortestDistance < this.borderlineThreshold && entry.getKey() != currentShortestDepot.getId()) {
                    customer.addPossibleDepot(entry.getKey());
                    customer.setOnBorderline();
                    isBorderlineCustomer = true;
                }
            }

            if (isBorderlineCustomer) {
                this.swappableCustomers.add(customer);
            }

        }
        return depots;
    }


    public List<CrowdedDepot> assignCustomerToDepotsFromIndividual(Individual individual) {
        List<CrowdedDepot> depots = this.copyDepots().stream().map(CrowdedDepot::new).collect(Collectors.toList());
        for (CrowdedDepot depot : depots) {
            List<Route> routes = individual.getChromosome().get(depot.getId());
            for (Route route : routes) {
                for (Integer customerId : route.getRoute()) {
                    Customer customer = this.getCustomer(customerId);
                    depot.addCustomer(customer);
                }
            }
        }

        return depots;
    }


    public static void saveSolution(Solution solution, String outputPath) {
        File file;
        FileWriter filewriter = null;

        try {
            file = new File(outputPath);

            filewriter = new FileWriter(file);

            // Write total solution cost (distance)
            filewriter.write("" + solution.getIndividual().getFitness());

            for (Map.Entry<Integer, List<Route>> entry : solution.getIndividual().getChromosome().entrySet()) {
                int depotID = entry.getKey();
                List<Double> routesDemand = solution.getRoutesDemand().get(depotID);
                List<Double> routesDistance = solution.getRoutesDistance().get(depotID);

                List<Route> depotRoutes = entry.getValue();

                for (int i = 0; i < depotRoutes.size(); i++) {
                    Route route = depotRoutes.get(i);
                    double routeDemand = routesDemand.get(i);
                    double routeDistance = routesDistance.get(i);
                    int vehicleID = i + 1;

                    // Write output line to file
                    filewriter.write("\n" + Formatter.formatOutputLine(depotID, vehicleID, routeDistance, routeDemand, route.getRoute()));
                }
            }

            filewriter.close();  // Close stream
            System.out.println("Solution saved");
        }

        catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (filewriter != null) {
                    filewriter.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }



}
