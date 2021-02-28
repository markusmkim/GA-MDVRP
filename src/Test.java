import MDVRP.Customer;
import Utils.Euclidian;

public class Test {
    /*
    Testing
     */

    public static void main(String []args) {
        System.out.println("Customer test:"); // prints Hello World

        Customer c = new Customer(0, 1, 2, 3,4);
        System.out.println(c.getId());
        System.out.println(c.getX());
        System.out.println(c.getY());
        System.out.println(c.getDuration());
        System.out.println(c.getDemand());

        int[] co1 = new int[]{1, 1};
        int[] co2 = new int[]{4, -4};

        double dist = Euclidian.distance(co1, co2);
        System.out.println("\n" + dist);
    }
}
