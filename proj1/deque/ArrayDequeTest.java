package deque; // 确保这个包名和你的文件夹结构一致

import org.junit.Test;
import static org.junit.Assert.*;

public class ArrayDequeTest {

    @Test
    /*
     * 这是一个基础测试，类似于 LinkedListDequeTest 中的那个。
     * 测试 add, isEmpty 和 size。
     */
    public void addIsEmptySizeTest() {
        ArrayDeque<String> ad1 = new ArrayDeque<String>();

        assertTrue("一个新初始化的 ArrayDeque 应该为空", ad1.isEmpty());
        ad1.addFirst("front");

        assertEquals(1, ad1.size());
        assertFalse("ad1 现在应该包含 1 个元素", ad1.isEmpty());

        ad1.addLast("middle");
        assertEquals(2, ad1.size());

        ad1.addLast("back");
        assertEquals(3, ad1.size());

        System.out.println("打印出 deque: ");
        ad1.printDeque();
    }

    @Test
    /*
     * (与 LinkedListDequeTest 相同)
     * 检查从空队列中移除是否返回 null。
     */
    public void emptyNullReturnTest() {
        ArrayDeque<Integer> ad1 = new ArrayDeque<Integer>();

        assertEquals("从空 Deque 调用 removeFirst 应该返回 null", null, ad1.removeFirst());
        assertEquals("从空 Deque 调用 removeLast 应该返回 null", null, ad1.removeLast());
    }

    @Test
    /*
     * (与 LinkedListDequeTest 类似)
     * 这是一个更全面的测试，混合了 Add, Remove 和 Get。
     */
    public void testAddRemoveAndGet() {
        ArrayDeque<Integer> ad1 = new ArrayDeque<Integer>();
        assertTrue(ad1.isEmpty()); // 初始应为空

        ad1.addFirst(10); // 队列: [10]
        ad1.addLast(20);  // 队列: [10, 20]
        ad1.addFirst(5);  // 队列: [5, 10, 20]
        ad1.addLast(30);  // 队列: [5, 10, 20, 30]

        assertEquals(4, ad1.size());

        // --- 测试 Get ---
        assertEquals("get(0) 应该返回第一个元素 5", Integer.valueOf(5), ad1.get(0));
        assertEquals("get(2) 应该返回第三个元素 20", Integer.valueOf(20), ad1.get(2));
        assertEquals("get(3) 应该返回最后一个元素 30", Integer.valueOf(30), ad1.get(3));
        assertNull("get(100) 应该返回 null (越界)", ad1.get(100));

        // --- 测试 Remove ---
        Integer removedLast = ad1.removeLast(); // 移除 30
        assertEquals("removeLast() 应该返回 30", Integer.valueOf(30), removedLast);
        assertEquals("移除后 size 应该为 3", 3, ad1.size());

        Integer removedFirst = ad1.removeFirst(); // 移除 5
        assertEquals("removeFirst() 应该返回 5", Integer.valueOf(5), removedFirst);
        assertEquals("移除后 size 应该为 2", 2, ad1.size());

        // --- 检查移除后的 Get ---
        // 队列现在应该是 [10, 20]
        assertEquals("移除后 get(0) 应该返回 10", Integer.valueOf(10), ad1.get(0));
        assertEquals("移除后 get(1) 应该返回 20", Integer.valueOf(20), ad1.get(1));

        // --- 移除至空 ---
        ad1.removeLast(); // 移除 20
        ad1.removeFirst(); // 移除 10
        assertTrue("全部移除后队列应为空", ad1.isEmpty());
    }


    @Test
    /*
     * (与 LinkedListDequeTest 相同)
     * 全面测试 equals 方法，特别是跨实现对比
     */
    public void testEquals() {
        ArrayDeque<String> ad1 = new ArrayDeque<>();
        ArrayDeque<String> ad2 = new ArrayDeque<>();
        LinkedListDeque<String> lld1 = new LinkedListDeque<>(); // ** 跨实现对比 **

        ad1.addLast("a");
        ad1.addLast("b");
        ad1.addLast("c");

        ad2.addLast("a");
        ad2.addLast("b");
        ad2.addLast("c");

        lld1.addLast("a");
        lld1.addLast("b");
        lld1.addLast("c");

        // 1. 与自身对比
        assertTrue("Deque 必须等于它自己", ad1.equals(ad1));

        // 2. 相同实现，相同内容
        assertTrue("ad1 应该等于 ad2", ad1.equals(ad2));

        // 3. (最重要) 跨实现，相同内容
        assertTrue("ad1 应该等于 lld1", ad1.equals(lld1));
        assertTrue("lld1 应该等于 ad1", lld1.equals(ad1));

        // 4. 与 null 对比
        assertFalse("Deque 不能等于 null", ad1.equals(null));

        // 5. 与不同类型对象对比
        assertFalse("Deque 不能等于一个字符串", ad1.equals("abc"));

        // 6. 不同大小对比
        ad2.addLast("d");
        assertFalse("不同大小的 Deque 不能相等", ad1.equals(ad2));

        // 7. 相同大小，不同内容对比
        ArrayDeque<String> ad3 = new ArrayDeque<>();
        ad3.addLast("a");
        ad3.addLast("B"); // 注意大小写
        ad3.addLast("c");
        assertFalse("内容不同的 Deque 不能相等", ad1.equals(ad3));
    }

    @Test
    /*
     * (与 LinkedListDequeTest 相同)
     * 测试 Iterator 是否被正确实现 (即 for-each 循环是否能用)
     */
    public void testIterator() {
        ArrayDeque<String> ad1 = new ArrayDeque<>();
        ad1.addLast("a");
        ad1.addLast("b");
        ad1.addLast("c");

        String result = "";
        // 这会调用你的 iterator() 方法
        for (String s : ad1) {
            result += s;
        }
        assertEquals("Iterator 应该按顺序遍历所有元素", "abc", result);
    }


    /*
     *
     * ========= 针对 ArrayDeque 的特定测试 =========
     *
     */


    @Test
    /*
     * 这是一个专门的测试，用来强迫 ArrayDeque **扩容 (Expansion)**
     * 初始容量通常是 8。我们将添加 10 个元素来触发 resize。
     */
    public void testResizeExpansion() {
        ArrayDeque<Integer> ad1 = new ArrayDeque<>();
        for (int i = 0; i < 10; i++) {
            ad1.addLast(i);
        } // 此时应该已经触发了 resize(16)

        assertEquals("添加 10 个元素后 size 应该是 10", 10, ad1.size());

        // 检查 resize 后所有元素是否仍然按正确顺序存在
        for (int i = 0; i < 10; i++) {
            assertEquals("resize 后 get(" + i + ") 应该返回 " + i,
                    Integer.valueOf(i), ad1.get(i));
        }

        // 继续添加，确保 resize 后的指针是正确的
        ad1.addLast(10);
        assertEquals(Integer.valueOf(10), ad1.get(10));

        ad1.addFirst(-1); // 队列: [-1, 0, 1, ..., 9, 10]
        assertEquals(Integer.valueOf(-1), ad1.get(0));
        assertEquals(Integer.valueOf(0), ad1.get(1));
    }


    @Test
    /*
     * 这是一个专门的测试，用来强迫 ArrayDeque **缩容 (Shrinkage)**
     * 1. 我们先添加很多元素 (比如 20 个)，触发扩容 (到 32)
     * 2. 然后我们移除很多元素 (比如 15 个)，直到 size = 5
     * 3. 此时 usage ratio = 5 / 32 < 0.25，应该触发缩容 (到 16)
     */
    public void testResizeShrinkage() {
        ArrayDeque<Integer> ad1 = new ArrayDeque<>();
        // 1. 填满，触发扩容
        for (int i = 0; i < 20; i++) {
            ad1.addLast(i);
        } // size = 20, capacity 应该 = 32

        // 2. 移除，直到触发缩容
        for (int i = 0; i < 15; i++) {
            ad1.removeLast();
        } // 移除了 15 个。size = 5。
        // 5 / 32 < 0.25，此时 removeLast() 应该触发了 resize(16)

        assertEquals("缩容后 size 应该为 5", 5, ad1.size());

        // 3. 检查缩容后，剩余的元素是否正确
        // 队列现在应该是 [0, 1, 2, 3, 4]
        for (int i = 0; i < 5; i++) {
            assertEquals("缩容后 get(" + i + ") 应该返回 " + i,
                    Integer.valueOf(i), ad1.get(i));
        }

        // 4. 检查缩容后的指针是否正确
        ad1.addLast(5); // 队列: [0, 1, 2, 3, 4, 5]
        assertEquals(Integer.valueOf(5), ad1.get(5));

        ad1.addFirst(-1); // 队列: [-1, 0, 1, 2, 3, 4, 5]
        assertEquals(Integer.valueOf(-1), ad1.get(0));
        assertEquals(Integer.valueOf(0), ad1.get(1));
        assertEquals("缩容并添加后 size 应该为 7", 7, ad1.size());
    }

    @Test
    /*
     * 这是一个压力测试，确保 resize 和 循环索引 能在
     * 大量混合操作 (addFirst/addLast/removeFirst/removeLast) 下正常工作
     */
    public void testRandomizedMix() {
        ArrayDeque<Integer> ad1 = new ArrayDeque<>();
        java.util.LinkedList<Integer> solution = new java.util.LinkedList<>(); // 用 Java 官方库作为“正确答案”

        int N = 50000;
        for (int i = 0; i < N; i++) {
            // 生成一个 0 到 3 之间的随机数
            int operation = (int) (Math.random() * 4);

            if (operation == 0) { // addFirst
                int randVal = (int) (Math.random() * 100);
                ad1.addFirst(randVal);
                solution.addFirst(randVal);

            } else if (operation == 1) { // addLast
                int randVal = (int) (Math.random() * 100);
                ad1.addLast(randVal);
                solution.addLast(randVal);

            } else if (operation == 2) { // removeFirst
                if (solution.isEmpty()) {
                    assertNull(ad1.removeFirst());
                } else {
                    assertEquals(solution.removeFirst(), ad1.removeFirst());
                }

            } else if (operation == 3) { // removeLast
                if (solution.isEmpty()) {
                    assertNull(ad1.removeLast());
                } else {
                    assertEquals(solution.removeLast(), ad1.removeLast());
                }
            }

            // 每次操作后都检查 size
            assertEquals(solution.size(), ad1.size());
        }

        // 50000 次操作后，检查 get
        for (int i = 0; i < solution.size(); i++) {
            assertEquals(solution.get(i), ad1.get(i));
        }
    }
}