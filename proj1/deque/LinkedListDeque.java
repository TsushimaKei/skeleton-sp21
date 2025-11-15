package deque;

import org.w3c.dom.Node;

import java.security.DrbgParameters;
import java.util.Iterator;

public class LinkedListDeque<T> {

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
        if (index > size) {
            return null;
        }
        while (index > 0) {
            index -= 1;
            L = L.next;
        }
        return L.item;
    }


}
