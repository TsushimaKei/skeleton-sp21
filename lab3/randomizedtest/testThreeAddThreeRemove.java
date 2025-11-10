package randomizedtest;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class testThreeAddThreeRemove {

    @Test
    public void testAList() {
        AListNoResizing<Integer> L = new AListNoResizing<>();
        BuggyAList<Integer> L1 = new BuggyAList<>();

        L.addLast(4);
        L.addLast(5);
        L.addLast(6);
        L1.addLast(4);
        L1.addLast(5);
        L1.addLast(6);

        L.removeLast();
        L.removeLast();

        L1.removeLast();
        L1.removeLast();

        assertEquals(L1.getLast(), L.getLast());
    }
    @Test
    public void testThreeAddThreeRemove() {
        AListNoResizing<Integer> correct = new AListNoResizing<>();
        BuggyAList<Integer> broken = new BuggyAList<>();

        correct.addLast(5);
        correct.addLast(10);
        correct.addLast(15);

        broken.addLast(5);
        broken.addLast(10);
        broken.addLast(15);

        assertEquals(correct.size(), broken.size());

        assertEquals(correct.removeLast(), broken.removeLast());
        assertEquals(correct.removeLast(), broken.removeLast());
        assertEquals(correct.removeLast(), broken.removeLast());
    }

}
