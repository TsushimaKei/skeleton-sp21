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
    @Test
    /** 测试 isEmpty() 功能 和 构造函数() */
    public void testIsEmpty() {
        // 1. 测试新创建的 Deque
        LinkedListDeque<Integer> lld = new LinkedListDeque<>();
        assertTrue("一个新创建的 Deque 应该为空 (isEmpty() == true)", lld.isEmpty());

        // 2. 测试添加元素后
        lld.addFirst(5);
        assertFalse("添加一个元素后, Deque 不应为空 (isEmpty() == false)", lld.isEmpty());

        // 3. 测试移除元素后
        lld.removeFirst();
        assertTrue("移除所有元素后, Deque 应该再次为空", lld.isEmpty());
    }

    @Test
    /** 测试 size() 功能 */
    public void testSize() {
        LinkedListDeque<Integer> lld = new LinkedListDeque<>();
        assertEquals("一个新创建的 Deque 的 size 应该是 0", 0, lld.size());

        lld.addFirst(5);
        assertEquals("添加一个元素后, size 应该是 1", 1, lld.size());

        lld.addLast(10);
        assertEquals("添加两个元素后, size 应该是 2", 2, lld.size());

        lld.removeLast();
        assertEquals("移除一个元素后, size 应该变回 1", 1, lld.size());

        lld.removeFirst();
        assertEquals("移除所有元素后, size 应该变回 0", 0, lld.size());
    }

    @Test
    /** 测试 addFirst() 功能 */
    public void testAddFirst() {
        LinkedListDeque<Integer> lld = new LinkedListDeque<>();
        lld.addFirst(10); // 队列: [10]
        assertEquals("addFirst 应将元素添加到队列头部", Integer.valueOf(10), lld.get(0));

        lld.addFirst(5);  // 队列: [5, 10]
        assertEquals("addFirst 应更新队列头部", Integer.valueOf(5), lld.get(0));
        assertEquals("addFirst 应将旧的头部元素向后推", Integer.valueOf(10), lld.get(1));
        assertEquals("Size 应被正确更新", 2, lld.size());
    }

    @Test
    /** 测试 addLast() 功能 */
    public void testAddLast() {
        LinkedListDeque<Integer> lld = new LinkedListDeque<>();
        lld.addLast(10); // 队列: [10]
        assertEquals("addLast 应将元素添加到队列尾部", Integer.valueOf(10), lld.get(0));

        lld.addLast(20); // 队列: [10, 20]
        assertEquals("addLast 不应影响头部元素", Integer.valueOf(10), lld.get(0));
        assertEquals("addLast 应更新队列尾部", Integer.valueOf(20), lld.get(1));
        assertEquals("Size 应被正确更新", 2, lld.size());
    }

    @Test
    /** 测试 get() 功能 */
    public void testGet() {
        LinkedListDeque<Integer> lld = new LinkedListDeque<>();

        // 1. 测试空队列
        assertNull("在空队列上 Get 应该返回 null", lld.get(0));

        // 2. 测试非空队列
        lld.addLast(10); // 队列: [10]
        lld.addLast(20); // 队列: [10, 20]
        lld.addFirst(5); // 队列: [5, 10, 20]

        assertEquals("get(0) 应该返回第一个元素", Integer.valueOf(5), lld.get(0));
        assertEquals("get(1) 应该返回第二个元素", Integer.valueOf(10), lld.get(1));
        assertEquals("get(2) 应该返回第三个元素", Integer.valueOf(20), lld.get(2));

        // 3. 测试越界
        assertNull("Get 一个负数索引应该返回 null", lld.get(-1));
        assertNull("Get 一个大于 size 的索引应该返回 null", lld.get(3));
        assertNull("Get 一个远大于 size 的索引应该返回 null", lld.get(100));
    }

    @Test
    /** 测试 getRecursive() 功能 (如果你的 LinkedListDeque 有这个方法) */
    public void testGetRecursive() {
        LinkedListDeque<Integer> lld = new LinkedListDeque<>();

        // 1. 测试空队列
        // 假设 getRecursive 的行为和 get 一样
        assertNull("在空队列上 getRecursive 应该返回 null", lld.getRecursive(0));

        // 2. 测试非空队列
        lld.addLast(10); // 队列: [10]
        lld.addLast(20); // 队列: [10, 20]
        lld.addFirst(5); // 队列: [5, 10, 20]

        assertEquals("getRecursive(0) 应该返回第一个元素", Integer.valueOf(5), lld.getRecursive(0));
        assertEquals("getRecursive(1) 应该返回第二个元素", Integer.valueOf(10), lld.getRecursive(1));
        assertEquals("getRecursive(2) 应该返回第三个元素", Integer.valueOf(20), lld.getRecursive(2));

        // 3. 测试越界
        assertNull("getRecursive 一个负数索引应该返回 null", lld.getRecursive(-1));
        assertNull("getRecursive 一个大于 size 的索引应该返回 null", lld.getRecursive(3));
    }

    @Test
    /** 测试 removeFirst() 功能 */
    public void testRemoveFirst() {
        LinkedListDeque<Integer> lld = new LinkedListDeque<>();

        // 1. 测试从空队列移除
        assertNull("从空队列 removeFirst 应该返回 null", lld.removeFirst());

        // 2. 测试从非空队列移除
        lld.addLast(10);
        lld.addLast(20); // 队列: [10, 20]
        Integer removed = lld.removeFirst(); // 移除 10

        assertEquals("removeFirst 应该返回被移除的元素", Integer.valueOf(10), removed);
        assertEquals("removeFirst 后 size 应该减少", 1, lld.size());
        assertEquals("removeFirst 后, 新的头部元素应该是 20", Integer.valueOf(20), lld.get(0));

        // 3. 测试移除到空
        removed = lld.removeFirst(); // 移除 20
        assertEquals("removeFirst 应该返回被移除的元素", Integer.valueOf(20), removed);
        assertTrue("移除所有元素后, 队列应该为空", lld.isEmpty());
        assertNull("在空队列上 Get 应该返回 null", lld.get(0));
    }

    @Test
    /** 测试 removeLast() 功能 */
    public void testRemoveLast() {
        LinkedListDeque<Integer> lld = new LinkedListDeque<>();

        // 1. 测试从空队列移除
        assertNull("从空队列 removeLast 应该返回 null", lld.removeLast());

        // 2. 测试从非空队列移除
        lld.addLast(10);
        lld.addLast(20); // 队列: [10, 20]
        Integer removed = lld.removeLast(); // 移除 20

        assertEquals("removeLast 应该返回被移除的元素", Integer.valueOf(20), removed);
        assertEquals("removeLast 后 size 应该减少", 1, lld.size());
        assertEquals("removeLast 后, 头部元素 10 应该不受影响", Integer.valueOf(10), lld.get(0));

        // 3. 测试移除到空
        removed = lld.removeLast(); // 移除 10
        assertEquals("removeLast 应该返回被移除的元素", Integer.valueOf(10), removed);
        assertTrue("移除所有元素后, 队列应该为空", lld.isEmpty());
    }

    @Test
    /**
     * 测试 iterator() 功能 (即 for-each 循环)
     * * 1. 检查非空队列是否按正确顺序遍历。
     * 2. 检查空队列是否根本不遍历。
     */
    public void testIterator() {

        // --- 1. 测试非空队列 ---

        LinkedListDeque<String> lld = new LinkedListDeque<>();
        lld.addLast("a");
        lld.addLast("b");
        lld.addLast("c");
        // 队列现在逻辑上是: [a, b, c]

        // 我们将使用一个“累加器” (accumulator) 字符串来收集结果
        String result = "";

        // 这个 "for-each" 循环会隐式地：
        // 1. 调用 lld.iterator() 来获取 LinkedListDequeIterator
        // 2. 循环调用 hasNext()
        // 3. 循环调用 next()
        for (String s : lld) {
            result += s;
        }

        // 我们断言结果必须是 "abc"，这证明了
        // a) hasNext() 在正确的时候停止 (p != sentinel)
        // b) next() 每次都返回了正确的 item 并移动了 p 指针
        assertEquals("Iterator 应该按正确顺序 (a, b, c) 遍历", "abc", result);


        // --- 2. 测试空队列 ---

        LinkedListDeque<String> lldEmpty = new LinkedListDeque<>();
        StringBuilder emptyResult = new StringBuilder();

        // 在一个空队列上, hasNext() (即 p != sentinel)
        // 第一次检查时就应该是 false (因为 p 一开始就是 sentinel.next,
        // 在空队列里, sentinel.next 就是 sentinel 自己)
        for (String s : lldEmpty) {
            emptyResult.append(s); // 这一行永远不应该被执行
        }

        assertEquals("空队列的 Iterator 不应执行任何操作", "", emptyResult.toString());
    }


}

