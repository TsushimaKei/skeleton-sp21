package deque;

import org.w3c.dom.Node;

import java.security.DrbgParameters;
import java.util.Iterator;

public class LinkedListDeque<T> implements Iterable<T> {

    public class Node {
        public Node next;
        public Node prev;
        public T item;

        public Node(T i, Node p, Node n) {
            item = i;
            next = n;
            prev = p;
        }
    }
    private Node sentinel; // 哨兵节点
    private int size;

    public LinkedListDeque() {
        sentinel = new Node(null, null, null);
        sentinel.next = sentinel;
        sentinel.prev = sentinel;

        size = 0;

    }
    // 构造函数
    public LinkedListDeque(T item) {
        sentinel = new Node(null, null, null);
        sentinel.next = sentinel;
        sentinel.prev = sentinel;

        addFirst(item);
        size = 1;
    }
    // 将类型为 T 的元素添加到双端队列的前端。可以假设 item 永远不会是 null 。
    public void addFirst(T item) {
        Node oldFirst = sentinel.next;

        Node newNode = new Node(item, sentinel, oldFirst);


        oldFirst.prev = newNode;
        sentinel.next = newNode;

        size += 1;
    }
    // 将类型为 T 的元素添加到双端队列的后端。可以假设 item 永远不会是 null 。
    public void addLast(T item) {
        Node oldFirst = sentinel.prev;

        Node newNode = new Node(item, oldFirst, sentinel);

        oldFirst.next = newNode;
        sentinel.prev = newNode;
        size += 1;
    }

    public boolean isEmpty() {
        if (sentinel.next == sentinel) {
            return true;
        }
        return false;
    }

    public int size() {
        return size;
    }

    public void printDeque() {
        Node L = sentinel.next;
        while (L != sentinel) {
            System.out.print(L.item + " ");
            L = L.next;
        }
        System.out.println();
    }

    public T removeFirst() {
        if (isEmpty()) {
            return null;
        }
        Node L = sentinel.next;

        sentinel.next = L.next;
        L.next.prev = sentinel;
        size -= 1;
        return L.item;
    }

    public T removeLast() {
        if (isEmpty()) {
            return null;
        }
        Node L = sentinel.prev;

        Node newLast = L.prev;

        sentinel.prev = newLast;
        newLast.next = sentinel;


        size -= 1;
        return  L.item;
    }

    public T get(int index) {
        Node L = sentinel.next;
        if (index > size || index < 0) {
            return null;
        }
        while (index > 0) {
            index -= 1;
            L = L.next;
        }
        return L.item;
    }




    public T getRecursive(int index) {
        // 1. 边界检查 (正确的检查)
        if (index < 0 || index >= size) {
            return null;
        }

        // 2. 发起递归调用 (从第一个真实节点开始)
        return getRecursiveHelper(index, sentinel.next);
    }
    private T getRecursiveHelper(int count, Node currentNode) {
        // Base Case (基本情况): 倒数到 0 了
        if (count == 0) {
            return currentNode.item;
        }

        // Recursive Step (递归步骤):
        // 没到 0, 就让“下一个节点”去找“count - 1”
        return getRecursiveHelper(count - 1, currentNode.next);
    }

    private class LinkdeListDequeIterator implements Iterator<T> {
        private Node P;
        LinkdeListDequeIterator() {
            P = sentinel.next;
        }

        @Override
        public boolean hasNext() {
            return P != sentinel;
        }
        public T next() {
            T itemToReturn = P.item;
            // 2. 将我们的逻辑索引向前移动一位
            P = P.next;
            // 3. 返回元素
            return itemToReturn;
        }
    }

    public Iterator<T> iterator() {
        return new LinkdeListDequeIterator();
    }
}
