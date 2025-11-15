package deque;




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
        int indexToNull = (nextLast - 1 + items.length) % items.length;
        T del = items[indexToNull]; // 你用 getLast() 也可以，但这样更直接
        items[indexToNull] = null; // 在正确的位置设为 null
        nextLast = indexToNull; // 更新指针
        size -= 1;
        if (items.length >= 16 && (double) size / items.length < 0.25) {
            resize(items.length / 2); // 缩小一半
        }
        return del;
    }
    public T removeFirst() {
        if (isEmpty()) {
            return null;
        }
        int indexToNull = (nextFirst + 1) % items.length;
        T del = items[indexToNull]; // 你用 getFirst() 也可以
        items[indexToNull] = null; // 在正确的位置设为 null
        nextFirst = indexToNull; // 更新指针
        size -= 1;
        //... (resize 逻辑) ...
        if (items.length >= 16 && (double) size / items.length < 0.25) {
            resize(items.length / 2); // 缩小一半
        }
        return del;
    }
    /** 检测数组是否为空 */
    public boolean isEmpty() {
        if (size == 0) {
            return true;
        }
        return false;
    }
    /** 输出数组元素 */
    public void printDeque() {
        for (int i = 0; i < size; i++) {
            System.out.print(get(i) + " ");
        }
        System.out.println();
    }


    /**
     * 这是一个私有内部类，它实现了 Iterator 接口。
     * 它让 ArrayDeque 能够支持 for-each 循环。
     */
    private class ArrayDequeIterator implements Iterator<T> {
        private int currentPosition; // 这是一个“逻辑”索引，从 0 开始

        /** 构造函数，初始化迭代器 */
        public ArrayDequeIterator() {
            currentPosition = 0;
        }

        /**
         * 检查是否还有下一个元素
         * @return 如果逻辑索引 < 总大小，则为 true
         */
        @Override
        public boolean hasNext() {
            // 我们只需要检查“逻辑”索引是否小于队列的“逻辑”大小
            return currentPosition < size;
        }

        /**
         * 返回下一个元素，并将迭代器向前移动
         * @return 队列中的下一个元素
         */
        @Override
        public T next() {
            // 1. 获取当前逻辑位置的元素
            //    (我们直接复用已经写好的 get() 方法，这是最简单的！)
            T itemToReturn = get(currentPosition);

            // 2. 将迭代器的逻辑位置向前移动
            currentPosition += 1;

            // 3. 返回元素
            return itemToReturn;
        }


    }
    /**
     * 返回一个迭代器，用于 for-each 循环。
     * 这是 Deque 接口 (继承自 Iterable) 的要求。
     * @return 一个实现了 Iterator 接口的对象
     */
    @Override
    public Iterator<T> iterator() {
        return new ArrayDequeIterator();
    }
    /**
     * 检查此 ArrayDeque 是否与另一个对象相等。
     * @param o 要比较的对象
     * @return 如果 o 是一个 Deque，并且内容与此 Deque 相同且顺序一致，则为 true
     */
    @Override
    public boolean equals(Object o) {
        // --- 步骤 1: 身份检查 ---
        // 检查 o是不是 this (同一个内存地址)
        if (o == this) {
            return true;
        }

        // --- 步骤 2: null 检查 ---
        // 检查 o 是不是 null
        if (o == null) {
            return false;
        }

        // --- 步骤 3: 类型检查 (最关键!) ---
        // 检查 o 是不是一个 Deque。
        // **不能** 检查 (o instanceof ArrayDeque),
        // 因为我们必须能和 LinkedListDeque 比较！
        if (!(o instanceof ArrayDeque<?>)) {
            return false;
        }

        // --- 步骤 4: 类型转换 ---
        // 我们现在知道 o 是一个 Deque，把它转换成 Deque 接口
        ArrayDeque<?> other = (ArrayDeque<?>) o;

        // --- 步骤 5: 尺寸检查 ---
        // 如果尺寸不同，它们肯定不相等
        if (this.size() != other.size()) {
            return false;
        }

        // --- 步骤 6: 逐个元素对比 ---
        // 我们必须使用 get(i) 循环比较，因为我们不能访问
        // other 的私有变量 (比如 items 或 sentinel)
        for (int i = 0; i < this.size(); i++) {
            Object thisElement = this.get(i);
            Object otherElement = other.get(i);

            // 我们需要一个“null 安全”的比较
            if (thisElement == null) {
                // 如果 this 的元素是 null, other 的也必须是 null
                if (otherElement != null) {
                    return false;
                }
                // 如果两者都是 null, 它们相等, 继续循环
            } else {
                // 如果 this 的元素不是 null, 我们可以安全地调用 .equals()
                // (这也处理了 otherElement 是 null 的情况)
                if (!thisElement.equals(otherElement)) {
                    return false;
                }
            }
        }

        // --- 步骤 7: 所有检查都通过 ---
        // 如果循环跑完了，说明所有元素都匹配
        return true;
    }
}
