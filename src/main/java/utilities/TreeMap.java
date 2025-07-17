package utilities;

import java.util.Comparator;

/**
 * Custom implementation of TreeMap data structure.
 * This is a map backed by a binary search tree.
 *
 * @param <K> the type of keys maintained by this map
 * @param <V> the type of mapped values
 */
public class TreeMap<K, V> {
    private Node<K, V> root;
    private Comparator<? super K> comparator;

    private static class Node<K, V> {
        K key;
        V value;
        Node<K, V> left;
        Node<K, V> right;

        Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    public TreeMap(Comparator<? super K> comparator) {
        this.comparator = comparator;
    }

    public void put(K key, V value) {
        root = put(root, key, value);
    }

    private Node<K, V> put(Node<K, V> node, K key, V value) {
        if (node == null) return new Node<>(key, value);
        int cmp = comparator.compare(key, node.key);
        if (cmp < 0) node.left = put(node.left, key, value);
        else if (cmp > 0) node.right = put(node.right, key, value);
        else node.value = value;
        return node;
    }

    public V get(K key) {
        Node<K, V> node = get(root, key);
        return node == null ? null : node.value;
    }

    private Node<K, V> get(Node<K, V> node, K key) {
        if (node == null) return null;
        int cmp = comparator.compare(key, node.key);
        if (cmp < 0) return get(node.left, key);
        else if (cmp > 0) return get(node.right, key);
        else return node;
    }

    public boolean containsKey(K key) {
        return get(key) != null;
    }
}
