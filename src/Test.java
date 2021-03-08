import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Test {
    /*
    Testing
     */

    public static void main(String []args) {
        List<Integer> ints = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            ints.add(i + 1);
        }
        List<Integer> ints2 = new ArrayList<>(ints);
        Collections.shuffle(ints);
        System.out.println(Arrays.toString(ints2.toArray()));
        System.out.println(Arrays.toString(ints.toArray()));
    }
}
