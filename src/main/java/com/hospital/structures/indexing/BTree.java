package com.hospital.structures.indexing;

/**
 * B-Tree simulating how a real records system pages patient files to disk.
 * Each node = one "page"; when a page exceeds capacity (order - 1 keys),
 * it splits and pushes the middle key up.
 */
public class BTree<K extends Comparable<K>, V> {

    private final int order; // max children per node (page capacity = order - 1 keys)

    static class Node<K, V> {
        Object[] keys;
        Object[] values;
        Node<K, V>[] children;
        int keyCount;
        boolean isLeaf;

        @SuppressWarnings("unchecked")
        Node(int order, boolean isLeaf) {
            this.keys = new Object[order - 1];
            this.values = new Object[order - 1];
            this.children = new Node[order];
            this.keyCount = 0;
            this.isLeaf = isLeaf;
        }
    }

    private Node<K, V> root;

    public BTree(int order) {
        if (order < 3) throw new IllegalArgumentException("order must be >= 3");
        this.order = order;
        this.root = new Node<>(order, true);
    }

    public void insert(K key, V value) {
        // TODO: descend to the correct leaf; if that leaf's page is full,
        // split it (this is your "node split trace" evidence — log the
        // before/after key layout when a split happens).
        throw new UnsupportedOperationException("TODO");
    }

    public V search(K key) {
        return searchHelper(root, key);
    }

    @SuppressWarnings("unchecked")
    private V searchHelper(Node<K, V> node, K key) {
        // TODO: linear/binary scan within the node's keys, then recurse into
        // the matching child if not found and node is not a leaf
        throw new UnsupportedOperationException("TODO");
    }

    private void splitChild(Node<K, V> parent, int childIndex) {
        // TODO: standard B-Tree split — middle key moves up to parent,
        // left/right halves become two nodes
        throw new UnsupportedOperationException("TODO");
    }
}
