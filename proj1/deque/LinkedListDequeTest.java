package deque;

import org.junit.Test;
import static org.junit.Assert.*;


/** Performs some basic linked list tests. */
public class LinkedListDequeTest {

    @Test
    /** Adds a few things to the list, checking isEmpty() and size() are correct,
     * finally printing the results.
     *
     * && is the "and" operation. */
    public void addIsEmptySizeTest() {


        LinkedListDeque<String> lld1 = new LinkedListDeque<String>();

		assertTrue("A newly initialized LLDeque should be empty", lld1.isEmpty());
		lld1.addFirst("front");

		// The && operator is the same as "and" in Python.
		// It's a binary operator that returns true if both arguments true, and false otherwise.
        assertEquals(1, lld1.size());
        assertFalse("lld1 should now contain 1 item", lld1.isEmpty());

		lld1.addLast("middle");
		assertEquals(2, lld1.size());

		lld1.addLast("back");
		assertEquals(3, lld1.size());

		System.out.println("Printing out deque: ");
		lld1.printDeque();


    }

    @Test
    /** Adds an item, then removes an item, and ensures that dll is empty afterwards. */
    public void addRemoveTest() {


        LinkedListDeque<Integer> lld1 = new LinkedListDeque<Integer>();
		// should be empty
		assertTrue("lld1 should be empty upon initialization", lld1.isEmpty());

		lld1.addFirst(10);
		// should not be empty
		assertFalse("lld1 should contain 1 item", lld1.isEmpty());

		lld1.removeFirst();
		// should be empty
		assertTrue("lld1 should be empty after removal", lld1.isEmpty());


    }

    @Test
    /* Tests removing from an empty deque */
    public void removeEmptyTest() {


        LinkedListDeque<Integer> lld1 = new LinkedListDeque<>();
        lld1.addFirst(3);

        lld1.removeLast();
        lld1.removeFirst();
        lld1.removeLast();
        lld1.removeFirst();

        int size = lld1.size();
        String errorMsg = "  Bad size returned when removing from empty deque.\n";
        errorMsg += "  student size() returned " + size + "\n";
        errorMsg += "  actual size() returned 0\n";

        assertEquals(errorMsg, 0, size);


    }

    @Test
    /* Check if you can create LinkedListDeques with different parameterized types*/
    public void multipleParamTest() {


        LinkedListDeque<String>  lld1 = new LinkedListDeque<String>();
        LinkedListDeque<Double>  lld2 = new LinkedListDeque<Double>();
        LinkedListDeque<Boolean> lld3 = new LinkedListDeque<Boolean>();

        lld1.addFirst("string");
        lld2.addFirst(3.14159);
        lld3.addFirst(true);

        String s = lld1.removeFirst();
        double d = lld2.removeFirst();
        boolean b = lld3.removeFirst();


    }

    @Test
    /* check if null is return when removing from an empty LinkedListDeque. */
    public void emptyNullReturnTest() {


        LinkedListDeque<Integer> lld1 = new LinkedListDeque<Integer>();

        boolean passed1 = false;
        boolean passed2 = false;
        assertEquals("Should return null when removeFirst is called on an empty Deque,", null, lld1.removeFirst());
        assertEquals("Should return null when removeLast is called on an empty Deque,", null, lld1.removeLast());


    }

    @Test
    /* Add large number of elements to deque; check if order is correct. */
    public void bigLLDequeTest() {


        LinkedListDeque<Integer> lld1 = new LinkedListDeque<Integer>();
        for (int i = 0; i < 1000000; i++) {
            lld1.addLast(i);
        }

        for (double i = 0; i < 500000; i++) {
            assertEquals("Should have the same value", i, (double) lld1.removeFirst(), 0.0);
        }

        for (double i = 999999; i > 500000; i--) {
            assertEquals("Should have the same value", i, (double) lld1.removeLast(), 0.0);
        }

        }
    public void removeFirstTest() {
        LinkedListDeque<Integer> lld1 = new LinkedListDeque<Integer>();


    }

    /** 新加的测试*/
    @Test
    public void testAddRemoveAndGet() {
        LinkedListDeque<Integer> lld1 = new LinkedListDeque<Integer>();
        assertTrue(lld1.isEmpty()); // 初始应为空

        lld1.addFirst(10); // 队列: [10]
        lld1.addLast(20);  // 队列: [10, 20]
        lld1.addFirst(5);  // 队列: [5, 10, 20]
        lld1.addLast(30);  // 队列: [5, 10, 20, 30]

        assertEquals(4, lld1.size());

        // --- 测试 Get ---
        assertEquals("get(0) 应该返回第一个元素 5", Integer.valueOf(5), lld1.get(0));
        assertEquals("get(2) 应该返回第三个元素 20", Integer.valueOf(20), lld1.get(2));
        assertEquals("get(3) 应该返回最后一个元素 30", Integer.valueOf(30), lld1.get(3));
        assertNull("get(100) 应该返回 null (越界)", lld1.get(100));

        // --- 测试 Remove ---
        Integer removedLast = lld1.removeLast(); // 移除 30
        assertEquals("removeLast() 应该返回 30", Integer.valueOf(30), removedLast);
        assertEquals("移除后 size 应该为 3", 3, lld1.size());

        Integer removedFirst = lld1.removeFirst(); // 移除 5
        assertEquals("removeFirst() 应该返回 5", Integer.valueOf(5), removedFirst);
        assertEquals("移除后 size 应该为 2", 2, lld1.size());

        // --- 检查移除后的 Get ---
        // 队列现在应该是 [10, 20]
        assertEquals("移除后 get(0) 应该返回 10", Integer.valueOf(10), lld1.get(0));
        assertEquals("移除后 get(1) 应该返回 20", Integer.valueOf(20), lld1.get(1));

        // --- 移除至空 ---
        lld1.removeLast(); // 移除 20
        lld1.removeFirst(); // 移除 10
        assertTrue("全部移除后队列应为空", lld1.isEmpty());
    }


    @Test
    /*
     * 这是最关键的测试之一：全面测试 equals 方法
     * (注意: 这个测试假设你已经实现了 ArrayDeque 才能 100% 通过)
     */
    public void testEquals() {
        LinkedListDeque<String> lld1 = new LinkedListDeque<>();
        LinkedListDeque<String> lld2 = new LinkedListDeque<>();
        // ** 下面这一行需要你已经实现了 ArrayDeque **
        ArrayDeque<String> ad1 = new ArrayDeque<>();

        lld1.addLast("a");
        lld1.addLast("b");
        lld1.addLast("c");

        lld2.addLast("a");
        lld2.addLast("b");
        lld2.addLast("c");

        ad1.addLast("a");
        ad1.addLast("b");
        ad1.addLast("c");

        // 1. 与自身对比
        assertTrue("Deque 必须等于它自己", lld1.equals(lld1));

        // 2. 相同实现，相同内容
        assertTrue("lld1 应该等于 lld2", lld1.equals(lld2));

        // 3. (最重要) 跨实现，相同内容
        // 确保你的 equals 检查的是 Deque 接口, 而不是具体的类
        assertTrue("lld1 应该等于 ad1", lld1.equals(ad1));
        assertTrue("ad1 应该等于 lld1", ad1.equals(lld1));

        // 4. 与 null 对比
        assertFalse("Deque 不能等于 null", lld1.equals(null));

        // 5. 与不同类型对象对比
        assertFalse("Deque 不能等于一个字符串", lld1.equals("abc"));

        // 6. 不同大小对比
        lld2.addLast("d");
        assertFalse("不同大小的 Deque 不能相等", lld1.equals(lld2));

        // 7. 相同大小，不同内容对比
        LinkedListDeque<String> lld3 = new LinkedListDeque<>();
        lld3.addLast("a");
        lld3.addLast("B"); // 注意大小写
        lld3.addLast("c");
        assertFalse("内容不同的 Deque 不能相等", lld1.equals(lld3));

        // 8. 空 Deque 对比
        LinkedListDeque<Integer> lld_empty1 = new LinkedListDeque<>();
        ArrayDeque<Integer> ad_empty1 = new ArrayDeque<>();
        assertTrue("两个空的 Deque 必须相等 (即使实现不同)", lld_empty1.equals(ad_empty1));
    }

}

