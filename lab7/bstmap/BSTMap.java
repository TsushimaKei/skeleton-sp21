package bstmap;

import edu.princeton.cs.algs4.BST;
import edu.princeton.cs.algs4.SET;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class BSTMap<K extends Comparable<K>, V>  implements Map61B<K, V>{

    private class BSTNode {
        public BSTNode leftNode;
        public BSTNode rightNode;
        public V val;
        public K key;


        public BSTNode (V val, K key) {
            this.val = val;
            this.key = key;
            this.leftNode = null;
            this.rightNode = null;
        }
    }
    private BSTNode root;

    private int size = 0;

    @Override
    public Iterator<K> iterator() {
        return keySet().iterator();

    }

    /** Removes all of the mappings from this map.
     * 从此映射中移除所有映射。*/
    @Override
    public void clear() {
        root = null;
        size = 0;
    }

    /* Returns true if this map contains a mapping for the specified key.
    * 如果此映射包含指定键的映射，则返回true。*/
    @Override
    public boolean containsKey(K key) {
        return containsKey(root, key);
    }
    private boolean containsKey(BSTNode root, K key) {
        if (root == null) {
            return false;
        }

        int cmp = key.compareTo(root.key);

        if (cmp > 0) {
            return containsKey(root.rightNode, key);
        } else if (cmp < 0) {
            return containsKey(root.leftNode, key);
        } else {
            return true;
        }
    }

    /* Returns the value to which the specified key is mapped, or null if this
     * map contains no mapping for the key.
     * 返回指定键所映射的值，如果此映射不包含该键的映射，则返回null。
     */
    @Override
    public V get(K key) {
        return get(root, key);
    }
    private V get(BSTNode root, K key) {
        if (root == null) {
            return null;
        }
        int cmp = key.compareTo(root.key);

        if (cmp > 0) {
           return get(root.rightNode, key);
        } else if (cmp < 0) {
           return get(root.leftNode, key);
        } else {
            return root.val;
        }
    }
    /* Returns the number of key-value mappings in this map.
    * 返回此映射中键值映射的数量*/
    @Override
    public int size() {
        return size;
    }

    /* Associates the specified value with the specified key in this map.
    *  将指定值与该映射中的指定键相关联 */
    @Override
    public void put(K key, V value) {
        root = put(root, key, value);
    }

    private BSTNode put(BSTNode root,K key,V val) {
        if (root == null) {
            size += 1;
            return new BSTNode(val, key);
        }

        int cmp = key.compareTo(root.key);

        if (cmp == 0) {
            root.val = val;
            size += 1;
        } else if (cmp > 0) {
            root.rightNode = put(root.rightNode, key, val);
        } else {
            root.leftNode =  put(root.leftNode, key, val);
        }
        return root;
    }


    public void printInOrder() {
        printInOrder(root);
    }

    private void printInOrder(BSTNode root) {
        if (root == null) {
            return ;
        }

        printInOrder(root.leftNode);

        System.out.println(root.key.toString() + " -> " + root.val.toString());

        printInOrder(root.rightNode);
    }

    @Override
    public V remove(K key){

        size -= 1;
        V val = get(key);
        root = remove(root, key);
        return val;
    }

    public BSTNode remove(BSTNode root, K key) {
        if (root == null) {
            return null;
        }
        int cmp = key.compareTo(root.key);
        if (cmp > 0) {
            root.rightNode =  remove(root.rightNode, key);
        } else if (cmp < 0) {
            root.leftNode = remove(root.leftNode, key);
        } else {
            if (root.leftNode == null) {
                return root.rightNode;
            }
            if (root.rightNode == null) {
                return root.leftNode;
            }
            BSTNode succesor = min(root.rightNode);
            root.key = succesor.key;
            root.val = succesor.val;
            root.rightNode = remove(root.rightNode, succesor.key);
        }
        return root;
    }

    public BSTNode min(BSTNode root) {
        if (root.leftNode == null) {
            return root;
        } else {
            return min(root.leftNode);
        }
    }

    @Override
    public V remove(K key, V value){
        V val = get(key);
        if (val == null) {
            return null;
        }
        if (val.equals(value)) {
            root = remove(root, key);
            size -= 1;
            return val;
        }
        return null;
    }
    @Override
    public Set<K> keySet() {
        Set<K> set = new HashSet<>();

        addKeys(root, set);
        return set;
    }
    private void addKeys(BSTNode node, Set<K> set) {
        if (node == null) {
            return;
        }
        addKeys(node.leftNode, set);

        set.add(node.key);

        addKeys(node.rightNode, set);
    }
}
