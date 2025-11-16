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


    /** 测试 equals() 功能
    public void testEquals() {
        ArrayDeque<String> ad1 = new ArrayDeque<>();
        ArrayDeque<String> ad2 = new ArrayDeque<>();
        ArrayDeque<String> ad3 = new ArrayDeque<>();
        LinkedListDeque<String> lld1 = new LinkedListDeque<>(); // 用于跨实现测试

        ad1.addLast("a");
        ad1.addLast("b"); // ad1: [a, b]
        ad2.addLast("a");
        ad2.addLast("b"); // ad2: [a, b]
        ad3.addLast("b");
        ad3.addLast("a"); // ad3: [b, a]
        lld1.addLast("a");
        lld1.addLast("b"); // lld1: [a, b]

        // 1. 与自身对比
        assertTrue("Deque 必须等于它自己 (Reflexive)", ad1.equals(ad1));

        // 2. 相同实现，相同内容
        assertTrue("内容相同的 ArrayDeque 必须相等", ad1.equals(ad2));

        // 3. 相同实现，不同内容 (顺序)
        assertFalse("内容或顺序不同的 ArrayDeque 不能相等", ad1.equals(ad3));

        // 4. 不同实现，相同内容 (CS61B 核心要求)
        assertTrue("ArrayDeque 必须能等于内容相同的 LinkedListDeque", ad1.equals(lld1));

        // 5. 与 null 对比
        assertFalse("Deque 不能等于 null", ad1.equals(null));

        // 6. 与不同类型的对象对比
        assertFalse("Deque 不能等于一个字符串", ad1.equals("a string"));

        // 7. 不同大小对比
        ad2.addLast("c");
        assertFalse("不同大小的 Deque 不能相等", ad1.equals(ad2));
    }*/


    /** 测试 iterator() 功能 (即 for-each 循环)
    public void testIterator() {
        ArrayDeque<String> ad = new ArrayDeque<>();
        ad.addLast("a");
        ad.addLast("b");
        ad.addLast("c");

        // 1. 测试非空队列
        String result = "";
        for (String s : ad) { // 这会隐式调用 iterator()
            result += s;
        }
        assertEquals("Iterator 应该按正确顺序 (a, b, c) 遍历", "abc", result);

        // 2. 测试空队列
        ArrayDeque<String> adEmpty = new ArrayDeque<>();
        String emptyResult = "";
        for (String s : adEmpty) {
            emptyResult += s;
        }
        assertEquals("空队列的 Iterator 不应执行任何操作", "", emptyResult);
    }*/

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


    /** 测试 缩容 (Shrinkage) 功能
    public void testResizeShrinkage() {
        ArrayDeque<Integer> ad = new ArrayDeque<>();
        // 1. 添加超过 16 个元素, 强制扩容到 32 (8->16->32)
        for (int i = 0; i < 20; i++) {
            ad.addLast(i);
        }
        assertEquals("Size 应该是 20", 20, ad.size());

        // 2. 移除元素, 直到触发缩容
        // 规范：当 size / capacity < 0.25 且 capacity >= 16 时
        // 20 -> ... -> 5。 当 size = 5, capacity = 32, (5/32 < 0.25)
        // 移除 15 个元素
        for (int i = 0; i < 15; i++) {
            ad.removeLast();
        }
        assertEquals("Size 应该是 5", 5, ad.size());

        // 此时, capacity 应该已经从 32 缩容到 16。
        // 我们通过检查它是否还能正常工作来验证。

        // 3. 检查剩余元素
        for (int i = 0; i < 5; i++) {
            assertEquals("缩容后, get(" + i + ") 应该返回 " + i, Integer.valueOf(i), ad.get(i));
        }

        // 4. 检查缩容后是否还能添加
        ad.addFirst(-1); // 队列: [-1, 0, 1, 2, 3, 4]
        ad.addLast(5);   // 队列: [-1, 0, 1, 2, 3, 4, 5]
        assertEquals("缩容后应该还能 addFirst", Integer.valueOf(-1), ad.get(0));
        assertEquals("缩容后应该还能 addLast", Integer.valueOf(5), ad.get(6));
        assertEquals("Size 应该被正确更新", 7, ad.size());
    }*/
}