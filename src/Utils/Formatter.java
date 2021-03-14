package Utils;

import java.util.List;

public class Formatter {

    public static String formatOutputLine(int depotID, int vehicleID, double distance, double demand, List<Integer> route) {
        String output = "" + depotID + "  " + vehicleID + "  " + distance + "  " + demand + "  0 ";
        for (int customerID : route) {
            output += customerID + " ";
        }
        return output + "0";
    }
}
