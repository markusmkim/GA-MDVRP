package Utils;

import java.util.ArrayList;
import java.util.List;


public class Stats {
    public static List<Double> averageHistories(List<List<Double>> histories) {
        double[] summedHistories = new double[histories.get(0).size()];
        for (List<Double> history : histories) {
            for (int i = 0; i < history.size(); i++) {
                summedHistories[i] += history.get(i);
            }
        }
        List<Double> averagedHistories = new ArrayList<>();
        for (double value : summedHistories) {
            averagedHistories.add(value / histories.size());
        }

        return averagedHistories;
    }
}
