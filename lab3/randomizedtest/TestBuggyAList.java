package randomizedtest;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdRandom;
import org.junit.Test;
import static org.junit.Assert.*;

public class TestBuggyAList {
    /**
     * Created by hug.
     */
    @Test
    public void randomizedTest() {
        int N = 5000;
        AListNoResizing<Integer> L = new AListNoResizing<>();
        BuggyAList<Integer> L2 = new BuggyAList<>();

        for (int i = 0; i < N; i += 1) {
            int operationNumber = StdRandom.uniform(0, 4);
            if (operationNumber == 0) {
                // addLast

                int randVal = StdRandom.uniform(0, 100);
                L.addLast(randVal);
                L2.addLast(randVal);
            } else if (operationNumber == 1) {
                // size

                int size1 = L.size();
                int size2 = L2.size();

            } else if (operationNumber == 2) {
                if (L.size() > 0) {
                    L.getLast();

                    L2.getLast();

                }
            } else if (operationNumber == 3) {
                if (L.size() > 0) {
                    L.removeLast();
                    L2.removeLast();
                }
            }
        }
    }
    // YOUR TESTS HERE

}
