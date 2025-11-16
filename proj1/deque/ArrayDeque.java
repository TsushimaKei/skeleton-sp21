package deque;


import java.util.Deque;
import java.util.Iterator;

public class ArrayDeque<T> implements Iterable<T> {
    private T[] items;
    private int size;
    private int nextFirst;
    private int nextLast;
    public  ArrayDeque() {
        items = (T[]) new Object[8];
        size = 0;
        nextFirst = 4;
        nextLast = 5;
    }
    /** 将基础数组的大小调整为目标容量。. */
    private void resize(int capacity) {
        T[] a = (T[]) new Object[capacity];
        int tmp = (nextFirst + 1) % items.length;
        for (int i = 0; i < size; i++) {
            a[i] = items[tmp];
            tmp = (tmp + 1 + items.length) % items.length;
        }

        nextLast = size;
        nextFirst = capacity-1;
        items = a;
    }

    /** 在末尾添加元素 */
    public void addLast(T x) {
        if (size == items.length) {
            resize(size * 2);
        }
        items[nextLast] = x;
        size = size + 1;
        nextLast = (nextLast + 1) % items.length;
    }
    /** 在首位添加元素*/
    public void addFirst(T x) {
        if (size == items.length) {
            resize(size * 2);
        }
        items[nextFirst] = x;
        size = size + 1;
        // 在 addFirst 方法内部更新 nextFirst 时：
        nextFirst = (nextFirst - 1 + items.length) % items.length;
    }

    /** 获取最后一位元素. */
    public T getLast() {

        return items[(nextLast - 1 + items.length) % items.length];
    }
    public T getFirst() {
        int first = (nextFirst + 1 ) % items.length;
        return items[first];
    }
    /** 获取第i位元素. */
    public T get(int i) {
        int first = (nextFirst + 1 ) % items.length;
        int targetIndex = (first + i + items.length) % items.length;
        return items[targetIndex];
    }

    /** 返回数组元素个数 */
    public int size() {

        return size;
    }

    /** 删除最后一位元素
     * 返回删除的元素. */
    public T removeLast() {
        if (isEmpty()) {
            return null;
        }
        if (items.length >= 16 && (double) size / items.length < 0.25 ) {
            resize(items.length / 2);
        }
        nextLast = (nextLast - 1 + items.length) % items.length;
        T del = items[nextLast];
        items[nextLast] = null;
        size = size - 1;
        return del;
    }
    public T removeFirst() {
        if (isEmpty()) {
            return null;
        }
        if (items.length >= 16 && (double) size / items.length < 0.25 ) {
            resize(items.length / 2);
        }
        nextFirst = (nextFirst + 1) % items.length;
        T del = items[nextFirst];
        items[nextFirst] = null;

        size = size-1;
        return del;
    }
    /** 检测数组是否为空 */
    public boolean isEmpty() {
        return size == 0;
    }
    /** 输出数组元素 */
    public void printDeque() {
        int tmp = (nextFirst + 1) % items.length;
        for (int i = 0; i < size; i++) {
            System.out.print(items[tmp] + " ");
            tmp = (tmp + 1) % items.length;
        }
        System.out.println();
    }
    private class ArrayDequeIterator implements Iterator<T> {
        private int tmp;
        ArrayDequeIterator() {
            tmp = 0;
        }

        @Override
        public boolean hasNext() {
            if (tmp <size) {
                return true;
            }
            return false;
        }
        public T next() {
            T itemToReturn = get(tmp);
            // 2. 将我们的逻辑索引向前移动一位
            tmp += 1;
            // 3. 返回元素
            return itemToReturn;
        }
    }

    public Iterator<T> iterator() {
        return new ArrayDequeIterator();
    }

    @Override
    public boolean equals(Object o) {
        // 1. 身份检查 (检查是否是同一个内存地址)
        if (this == o) {
            return true;
        }

        // 2. null 检查
        if (o == null) {
            return false;
        }

        // 3. 类型检查 (既然我们只关心 ArrayDeque, 这样检查是安全的)
        if (!(o instanceof ArrayDeque)) {
            return false;
        }

        // 4. 类型转换 (现在这是安全的)
        ArrayDeque<?> other = (ArrayDeque<?>) o;

        // 5. 尺寸检查
        if (this.size() != other.size()) {
            return false;
        }

        // 6. (【关键修复】) 必须用 get(i) 按顺序比较！
        for (int i = 0; i < this.size(); i++) {
            T thisElement = this.get(i);
            Object otherElement = other.get(i);

            // "null 安全" 的比较
            if (thisElement == null) {
                if (otherElement != null) {
                    return false; // this 是 null, other 不是
                }
            } else if (!thisElement.equals(otherElement)) {
                return false; // this 不是 null, 但与 other 不相等
            }
        }

        // 7. 所有检查都通过
        return true;
    }
}


