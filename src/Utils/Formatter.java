package Utils;

import java.util.List;
import java.util.Locale;

public class Formatter {

    public static String formatOutputLine(int depotID, int vehicleID, double distance, int demand, List<Integer> route) {
        String output = "" + depotID + "  " + vehicleID + "  " + String.format(Locale.ROOT, "%.2f", distance) + "  " + demand + "  0 ";
        for (int customerID : route) {
            output += customerID + " ";
        }
        return output + "0";
    }
}
