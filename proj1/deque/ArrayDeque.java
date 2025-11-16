package deque;


import java.util.Iterator;

public class ArrayDeque<T> {
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
        items[nextLast-1] = null;
        nextLast = (nextLast - 1 + items.length) % items.length;
    }
    public T removeFirst() {
        if (isEmpty()) {
            return null;
        }
        items[nextFirst + 1] = null;
        nextFirst = (nextFirst + 1) % items.length;
    }
    /** 检测数组是否为空 */
    public boolean isEmpty() {
        if (items[nextFirst+1] == null) {
            return true;
        }
        return  false;
    }
    /** 输出数组元素 */
    public void printDeque() {
        int tmp = (nextFirst + 1) % items.length;
        for (int i = 0; i < size; i++) {
            System.out.print(items[tmp+1] + " ");
            tmp = (tmp + 1) % items.length;
        }
        System.out.println();
    }
}
