package Utils;

import java.lang.Math;


public class Euclidian {
    /*
    Returns euclidean distance between a pair of (x,y)-coordinates
     */
    public static double distance(int[] coordinatesA, int[] coordinatesB) {
        float xDistance = Math.abs(coordinatesA[0] - coordinatesB[0]);
        float yDistance = Math.abs(coordinatesA[1] - coordinatesB[1]);

        return Math.sqrt((xDistance * xDistance) + (yDistance * yDistance));
    }
}
