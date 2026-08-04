package com.hospital.structures.indexing;

import java.util.ArrayList;
import java.util.List;

/**
 * Unbalanced binary search tree, keyed by patientId (Korle-Bu numbering scheme).
 * Kept deliberately simple — this is the "before" baseline you'll compare
 * against RedBlackTree for the height/rotation evidence.
 */
public class BST<K extends Comparable<K>, V> {

    static class Node<K, V> {
        K key;
        V value;
        Node<K, V> left, right;

        Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    protected Node<K, V> root;
    private int size;

    public void insert(K key, V value) {
        // TODO: standard BST insert, update root
        throw new UnsupportedOperationException("TODO");
    }

    /**
     * Returns the value if found. Track (or log) the comparisons made so you
     * can produce a "search path" trace for the report.
     */
    public V search(K key) {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

    /** Sorted patient roster for a ward round. */
    public List<K> inorderKeys() {
        List<K> result = new ArrayList<>();
        inorderHelper(root, result);
        return result;
    }

    private void inorderHelper(Node<K, V> node, List<K> acc) {
        if (node == null) return;
        inorderHelper(node.left, acc);
        acc.add(node.key);
        inorderHelper(node.right, acc);
    }

    /** Height of the tree — used for the BST-vs-RedBlackTree comparison. */
    public int height() {
        return heightHelper(root);
    }

    private int heightHelper(Node<K, V> node) {
        if (node == null) return -1;
        return 1 + Math.max(heightHelper(node.left), heightHelper(node.right));
    }

    public int size() {
        return size;
    }
}
