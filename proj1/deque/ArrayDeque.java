package deque;


import java.util.Iterator;

public class ArrayDeque<T> implements Deque<T>, Iterable<T> {
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
    @Override
    public void addLast(T x) {
        if (size == items.length) {
            resize(size * 2);
        }
        items[nextLast] = x;
        size = size + 1;
        nextLast = (nextLast + 1) % items.length;
    }
    /** 在首位添加元素*/
    @Override
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
    @Override
    public T get(int i) {
        int first = (nextFirst + 1 ) % items.length;
        int targetIndex = (first + i + items.length) % items.length;
        return items[targetIndex];
    }

    /** 返回数组元素个数 */
    @Override
    public int size() {

        return size;
    }

    /** 删除最后一位元素
     * 返回删除的元素. */
    @Override
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
    @Override
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

    @Override
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

            tmp += 1;

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


        if (!(o instanceof ArrayDeque)) {
            return false;
        }


        ArrayDeque<?> other = (ArrayDeque<?>) o;

        // 5. 尺寸检查
        if (this.size() != other.size()) {
            return false;
        }


        for (int i = 0; i < this.size(); i++) {
            T thisElement = this.get(i);
            Object otherElement = other.get(i);

            // "null 安全" 的比较
            if (thisElement == null) {
                if (otherElement != null) {
                    return false;
                }
            } else if (!thisElement.equals(otherElement)) {
                return false;
            }
        }


        return true;
    }
}


