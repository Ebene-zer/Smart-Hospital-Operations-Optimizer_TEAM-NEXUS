package com.hospital.structures.indexing;

/**
 * Red-Black tree — same key/value shape as BST, but self-balancing.
 * Build BST.java first; this reuses that mental model plus color +
 * rotation/recolor logic.
 *
 * Required evidence: insert the same growing dataset (300 -> 20,000
 * simulated admissions) into both BST and this class, log height after
 * each batch, and capture at least one rotation/recolor before-and-after
 * snapshot for the report.
 */
public class RedBlackTree<K extends Comparable<K>, V> {

    private static final boolean RED = true;
    private static final boolean BLACK = false;

    private static class Node<K, V> {
        K key;
        V value;
        Node<K, V> left, right;
        boolean color; // color of the link from the parent to this node

        Node(K key, V value, boolean color) {
            this.key = key;
            this.value = value;
            this.color = color;
        }
    }

    private Node<K, V> root;
    private int size;

    public void insert(K key, V value) {
        // TODO: standard BST insert (color new node RED), then fix-up:
        //   - rotateLeft if right child is RED and left child is BLACK
        //   - rotateRight if left child and left.left are both RED
        //   - flipColors if both children are RED
        // root must always end up BLACK.
        throw new UnsupportedOperationException("TODO");
    }

    public V search(K key) {
        // TODO: identical traversal logic to BST.search
        throw new UnsupportedOperationException("TODO");
    }

    public int height() {
        return heightHelper(root);
    }

    private int heightHelper(Node<K, V> node) {
        if (node == null) return -1;
        return 1 + Math.max(heightHelper(node.left), heightHelper(node.right));
    }

    private Node<K, V> rotateLeft(Node<K, V> h) {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

    private Node<K, V> rotateRight(Node<K, V> h) {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

    private void flipColors(Node<K, V> h) {
        // TODO: h flips to RED, both children flip to BLACK (or vice versa
        // depending on your insert direction)
        throw new UnsupportedOperationException("TODO");
    }

    private boolean isRed(Node<K, V> node) {
        return node != null && node.color == RED;
    }

    public int size() {
        return size;
    }
}
