package deque;

public class ArrayDeque<T> {
    private T[] items;
    private int size;

    public  ArrayDeque() {
        items = (T[]) new Object[100];
        size = 0;
    }

    /** Resizes the underlying array to the target capacity. */
    private void resize(int capacity) {
        T[] a = (T[]) new Object[capacity];
        System.arraycopy(items, 0, a, 0, size);
        items = a;
    }

    /** Inserts X into the back of the list. */
    public void addLast(T x) {
        if (size == items.length) {
            resize(size * 2);
        }
        items[size] = x;
        size = size + 1;
    }

    /** Returns the item from the back of the list. */
    public T getLast() {
        return items[size-1];
    }
    /** Gets the ith item in the list (0 is the front). */
    public T get(int i) {
        return items[i];
    }

    /** Returns the number of items in the list. */
    public int size() {
        return size;
    }

    /** Deletes item from back of the list and
     * returns deleted item. */
    public T removeLast() {
        if (isEmpty()) {
            return null;
        }
        T del = getLast();
        size -= 1;
        items[size-1] = null;
        return del;
    }

    public boolean isEmpty() {

    }

}
