package hashmap;

import java.lang.reflect.Array;
import java.util.*;

/**
 *  A hash table-backed Map implementation. Provides amortized constant time
 *  access to elements via get(), remove(), and put() in the best case.
 *
 *  Assumes null keys will never be inserted, and does not resize down upon remove().
 *  @author YOUR NAME HERE
 */
public class MyHashMap<K, V> implements Map61B<K, V> {

    @Override
    public Iterator<K> iterator() {
        return keySet().iterator();
    }


    /**
     * Protected helper class to store key/value pairs
     * The protected qualifier allows subclass access
     */
    protected class Node {
        K key;
        V value;

        Node(K k, V v) {
            key = k;
            value = v;
        }
    }

    /* Instance Variables */
    private Collection<Node>[] buckets;
    // You should probably define some more!


    private int size;
    private static final int initialSize = 16;
    private int capacity;
    private double maxLoad;
    private double loadFactor() {
        return (double)size / capacity;
    }
    /** Constructors */
    public MyHashMap() {
        this.size = 0;
        this.capacity = initialSize;
        maxLoad = 0.75;
        buckets = (Collection<Node>[]) new Collection[capacity];
        for (int i = 0; i < capacity; i++) {
            buckets[i] = createBucket();
        }
    }

    public MyHashMap(int initialSize) {
        this.size = 0;
        this.capacity = initialSize;
        maxLoad = 0.75;
        buckets = (Collection<Node>[]) new Collection[capacity];
        for (int i = 0; i < capacity; i++) {
            buckets[i] = createBucket();
        }
    }

    /**
     * MyHashMap constructor that creates a backing array of initialSize.
     * The load factor (# items / # buckets) should always be <= loadFactor
     *
     * @param initialSize initial size of backing array
     * @param maxLoad maximum load factor
     */
    public MyHashMap(int initialSize, double maxLoad) {
        this.size = 0;
        this.capacity = initialSize;
        this.maxLoad = maxLoad;
        buckets = (Collection<Node>[]) new Collection[capacity];
        for (int i = 0; i < capacity; i++) {
            buckets[i] = createBucket();
        }
    }

    /**
     * Returns a new node to be placed in a hash table bucket
     */
    private Node createNode(K key, V value) {
        return new Node(key, value);
    }

    /**
     * Returns a data structure to be a hash table bucket
     *
     * The only requirements of a hash table bucket are that we can:
     *  1. Insert items (`add` method)
     *  2. Remove items (`remove` method)
     *  3. Iterate through items (`iterator` method)
     *
     * Each of these methods is supported by java.util.Collection,
     * Most data structures in Java inherit from Collection, so we
     * can use almost any data structure as our buckets.
     *
     * Override this method to use different data structures as
     * the underlying bucket type
     *
     * BE SURE TO CALL THIS FACTORY METHOD INSTEAD OF CREATING YOUR
     * OWN BUCKET DATA STRUCTURES WITH THE NEW OPERATOR!
     */
    protected Collection<Node> createBucket() {
        return new LinkedList<>();
    }

    /**
     * Returns a table to back our hash table. As per the comment
     * above, this table can be an array of Collection objects
     *
     * BE SURE TO CALL THIS FACTORY METHOD WHEN CREATING A TABLE SO
     * THAT ALL BUCKET TYPES ARE OF JAVA.UTIL.COLLECTION
     *
     * @param tableSize the size of the table to create
     */
    private Collection<Node>[] createTable(int tableSize) {

        return (Collection<Node>[]) new Collection[tableSize];
    }

    // TODO: Implement the methods of the Map61B Interface below
    // Your code won't compile until you do so!

    private int index(K key) {
        int istmp;
        if (capacity % 2 == 1) {
            istmp = capacity - 2;
        } else {
            istmp = capacity - 1;
        }


        if (key == null) {
            return 0;
        }
        int num = Math.abs(key.hashCode());

        return num % istmp;
    }

    private void resizing() {
        if (loadFactor() > maxLoad) {
            extend();
        }

    }

    private void extend() {
        // 暂存原哈希表
        Collection<Node>[] oldbucket = buckets;
        // 初始化扩容后的新哈希表
        capacity *= 2;
        buckets = (Collection<Node>[]) new Collection[capacity];
        for (int i = 0; i < capacity; i++) {
            buckets[i] = createBucket();
        }
        // 将键值从原哈希表搬运至新哈希表
        for (int i = 0; i < (capacity/2); i++) {
            for (Node a : oldbucket[i]) {
                int newindex = index(a.key);
                buckets[newindex].add(a);
            }
        }
    }

    public void clear() {
        for (int i = 0; i < capacity; i++) {
            if (buckets[i] != null) {
                buckets[i].clear();
            }
        }
        size = 0;
    }

    /** Returns true if this map contains a mapping for the specified key. */
    public boolean containsKey(K key) {
        int key_num = index(key);

        for (Node node : buckets[key_num]) {
            if (node.key.equals(key)) {
                return true;
            }
        }
        return false;

    }

    /**
     * Returns the value to which the specified key is mapped, or null if this
     * map contains no mapping for the key.
     */
    public V get(K key) {
        int key_num = index(key);

        for (Node node : buckets[key_num]) {
            if (node.key.equals(key)) {
                return node.value;
            }
        }
        return null;
    }

    /** Returns the number of key-value mappings in this map. */
    public int size() {

        return size;
    }

    /**
     * Associates the specified value with the specified key in this map.
     * If the map previously contained a mapping for the key,
     * the old value is replaced.
     */
    public void put(K key, V value) {
        Node node = new Node(key, value);

        int key_num = index(key);
        for (Node a : buckets[key_num]) {
            if (a.key.equals(key)) {
                a.value = value;
                resizing();
                return;
            }
        }
        buckets[key_num].add(node);
        size += 1;
        resizing();
    }

    /** Returns a Set view of the keys contained in this map. */
    public Set<K> keySet() {
        Set<K> set = new HashSet<>();

        addkeys1(set);
        return set;
    }

    private void addkeys1(Set<K> set) {
        int i = 0;
        while (i < capacity) {
            if (!buckets[i].isEmpty()){
                addkeys2(buckets[i], set);
            }
            i++;
        }
    }
    private void addkeys2(Collection<Node> bucket, Set<K> set){
        for (Node i : bucket) {
            set.add(i.key);
        }
    }



    /**
     * Removes the mapping for the specified key from this map if present.
     * Not required for Lab 8. If you don't implement this, throw an
     * UnsupportedOperationException.
     */
    public V remove(K key) {
        throw new UnsupportedOperationException();
    }

    /**
     * Removes the entry for the specified key only if it is currently mapped to
     * the specified value. Not required for Lab 8. If you don't implement this,
     * throw an UnsupportedOperationException.
     */
    public V remove(K key, V value) {
        throw new UnsupportedOperationException();
    }

}
