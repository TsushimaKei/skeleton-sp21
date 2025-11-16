package deque; // 确保这个包名和你的文件夹结构一致

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * 这是一个为 ArrayDeque 编写的独立单元测试文件。
 * 这里的每一个 @Test 方法都对应 ArrayDeque 的一个特定功能。
 */
public class ArrayDequeTest {

    @Test
    /** 测试 isEmpty() 功能 和 构造函数() */
    public void testIsEmpty() {
        // 1. 测试新创建的 Deque
        ArrayDeque<Integer> ad = new ArrayDeque<>();
        assertTrue("一个新创建的 Deque 应该为空 (isEmpty() == true)", ad.isEmpty());

        // 2. 测试添加元素后
        ad.addFirst(5);
        assertFalse("添加一个元素后, Deque 不应为空 (isEmpty() == false)", ad.isEmpty());

        // 3. 测试移除元素后
        ad.removeFirst();
        assertTrue("移除所有元素后, Deque 应该再次为空", ad.isEmpty());
    }

    @Test
    /** 测试 size() 功能 */
    public void testSize() {
        ArrayDeque<Integer> ad = new ArrayDeque<>();
        assertEquals("一个新创建的 Deque 的 size 应该是 0", 0, ad.size());

        ad.addFirst(5);
        assertEquals("添加一个元素后, size 应该是 1", 1, ad.size());

        ad.addLast(10);
        assertEquals("添加两个元素后, size 应该是 2", 2, ad.size());

        ad.removeLast();
        assertEquals("移除一个元素后, size 应该变回 1", 1, ad.size());

        ad.removeFirst();
        assertEquals("移除所有元素后, size 应该变回 0", 0, ad.size());
    }

    @Test
    /** 测试 addFirst() 功能 */
    public void testAddFirst() {
        ArrayDeque<Integer> ad = new ArrayDeque<>();
        ad.addFirst(10); // 队列: [10]
        assertEquals("addFirst 应将元素添加到队列头部", Integer.valueOf(10), ad.get(0));

        ad.addFirst(5);  // 队列: [5, 10]
        assertEquals("addFirst 应更新队列头部", Integer.valueOf(5), ad.get(0));
        assertEquals("addFirst 应将旧的头部元素向后推", Integer.valueOf(10), ad.get(1));
        assertEquals("Size 应被正确更新", 2, ad.size());
    }

    @Test
    /** 测试 addLast() 功能 */
    public void testAddLast() {
        ArrayDeque<Integer> ad = new ArrayDeque<>();
        ad.addLast(10); // 队列: [10]
        assertEquals("addLast 应将元素添加到队列尾部", Integer.valueOf(10), ad.get(0));

        ad.addLast(20); // 队列: [10, 20]
        assertEquals("addLast 不应影响头部元素", Integer.valueOf(10), ad.get(0));
        assertEquals("addLast 应更新队列尾部", Integer.valueOf(20), ad.get(1));
        assertEquals("Size 应被正确更新", 2, ad.size());
    }

    @Test
    /** 测试 get() 功能 */
    public void testGet() {
        ArrayDeque<Integer> ad = new ArrayDeque<>();

        // 1. 测试空队列
        assertNull("在空队列上 Get 应该返回 null", ad.get(0));

        // 2. 测试非空队列
        ad.addLast(10); // 队列: [10]
        ad.addLast(20); // 队列: [10, 20]
        ad.addFirst(5); // 队列: [5, 10, 20]

        assertEquals("get(0) 应该返回第一个元素", Integer.valueOf(5), ad.get(0));
        assertEquals("get(1) 应该返回第二个元素", Integer.valueOf(10), ad.get(1));
        assertEquals("get(2) 应该返回第三个元素", Integer.valueOf(20), ad.get(2));

        // 3. 测试越界
        assertNull("Get 一个负数索引应该返回 null", ad.get(-1));
        assertNull("Get 一个大于 size 的索引应该返回 null", ad.get(3));
        assertNull("Get 一个远大于 size 的索引应该返回 null", ad.get(100));
    }

    @Test
    /** 测试 removeFirst() 功能 */
    public void testRemoveFirst() {
        ArrayDeque<Integer> ad = new ArrayDeque<>();

        // 1. 测试从空队列移除
        assertNull("从空队列 removeFirst 应该返回 null", ad.removeFirst());

        // 2. 测试从非空队列移除
        ad.addLast(10);
        ad.addLast(20); // 队列: [10, 20]
        Integer removed = ad.removeFirst(); // 移除 10

        assertEquals("removeFirst 应该返回被移除的元素", Integer.valueOf(10), removed);
        assertEquals("removeFirst 后 size 应该减少", 1, ad.size());
        assertEquals("removeFirst 后, 新的头部元素应该是 20", Integer.valueOf(20), ad.get(0));

        // 3. 测试移除到空
        removed = ad.removeFirst(); // 移除 20
        assertEquals("removeFirst 应该返回被移除的元素", Integer.valueOf(20), removed);
        assertTrue("移除所有元素后, 队列应该为空", ad.isEmpty());
        assertNull("在空队列上 Get 应该返回 null", ad.get(0));
    }

    @Test
    /** 测试 removeLast() 功能 */
    public void testRemoveLast() {
        ArrayDeque<Integer> ad = new ArrayDeque<>();

        // 1. 测试从空队列移除
        assertNull("从空队列 removeLast 应该返回 null", ad.removeLast());

        // 2. 测试从非空队列移除
        ad.addLast(10);
        ad.addLast(20); // 队列: [10, 20]
        Integer removed = ad.removeLast(); // 移除 20

        assertEquals("removeLast 应该返回被移除的元素", Integer.valueOf(20), removed);
        assertEquals("removeLast 后 size 应该减少", 1, ad.size());
        assertEquals("removeLast 后, 头部元素 10 应该不受影响", Integer.valueOf(10), ad.get(0));

        // 3. 测试移除到空
        removed = ad.removeLast(); // 移除 10
        assertEquals("removeLast 应该返回被移除的元素", Integer.valueOf(10), removed);
        assertTrue("移除所有元素后, 队列应该为空", ad.isEmpty());
    }

    /**测试 iterator() 功能 (即 for-each 循环)*/
    @Test
    public void testIterator() {
        ArrayDeque<String> ad = new ArrayDeque<>();
        ad.addLast("a");
        ad.addLast("b");
        ad.addLast("c");

        // 1. 测试非空队列
        StringBuilder result = new StringBuilder();
        for (String s : ad) { // 这会隐式调用 iterator()
            result.append(s);
        }
        assertEquals("Iterator 应该按正确顺序 (a, b, c) 遍历", "abc", result.toString());

        // 2. 测试空队列
        ArrayDeque<String> adEmpty = new ArrayDeque<>();
        StringBuilder emptyResult = new StringBuilder();
        for (String s : adEmpty) {
            emptyResult.append(s);
        }
        assertEquals("空队列的 Iterator 不应执行任何操作", "", emptyResult.toString());
    }

    /*
     * 以下测试针对 ArrayDeque 的内部特性：resize
     */

    @Test
    /** 测试 扩容 (Expansion) 功能 */
    public void testResizeExpansion() {
        ArrayDeque<Integer> ad = new ArrayDeque<>();
        // 默认容量是 8。我们添加 10 个元素来强制触发 resize。
        for (int i = 0; i < 10; i++) {
            ad.addLast(i);
        }
        assertEquals("添加 10 个元素后, size 应该是 10", 10, ad.size());

        // 检查所有元素在 resize 后是否仍然正确
        for (int i = 0; i < 10; i++) {
            assertEquals("resize 后, get(" + i + ") 应该返回 " + i, Integer.valueOf(i), ad.get(i));
        }

        // 检查 resize 后是否还能继续添加
        ad.addLast(10);
        assertEquals("resize 后应该还能 addLast", Integer.valueOf(10), ad.get(10));
        ad.addFirst(-1);
        assertEquals("resize 后应该还能 addFirst", Integer.valueOf(-1), ad.get(0));
        assertEquals("resize 后的 addFirst 应该把旧元素推后", Integer.valueOf(0), ad.get(1));
        assertEquals("Size 应该被正确更新", 12, ad.size());
    }

}