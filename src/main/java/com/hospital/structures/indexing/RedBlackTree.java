package com.hospital.structures.indexing;

public class RedBlackTree {

    private RedBlackNode root;

    public RedBlackTree() {
        this.root = null;
    }

    public void insert(int patientId, String patientName, String wardName) {
        root = insertRecursive(root, patientId, patientName, wardName);
        root.setRed(false);
    }

    public boolean contains(int patientId) {
        return searchRecursive(root, patientId) != null;
    }

    public boolean rootIsBlack() {
        return root == null || !root.isRed();
    }

    public String inOrderTraversal() {
        StringBuilder traversal = new StringBuilder();
        buildInOrderTraversal(root, traversal);
        return traversal.toString();
    }

    private RedBlackNode insertRecursive(RedBlackNode current, int patientId, String patientName, String wardName) {
        if (current == null) {
            return new RedBlackNode(patientId, patientName, wardName);
        }

        if (patientId < current.getPatientId()) {
            current.setLeft(insertRecursive(current.getLeft(), patientId, patientName, wardName));
        } else if (patientId > current.getPatientId()) {
            current.setRight(insertRecursive(current.getRight(), patientId, patientName, wardName));
        }

        if (isRed(current.getRight()) && !isRed(current.getLeft())) {
            current = rotateLeft(current);
        }

        if (isRed(current.getLeft()) && isRed(current.getLeft().getLeft())) {
            current = rotateRight(current);
        }

        if (isRed(current.getLeft()) && isRed(current.getRight())) {
            flipColors(current);
        }

        return current;
    }

    private RedBlackNode rotateLeft(RedBlackNode current) {
        RedBlackNode newRoot = current.getRight();
        current.setRight(newRoot.getLeft());
        newRoot.setLeft(current);
        newRoot.setRed(current.isRed());
        current.setRed(true);
        return newRoot;
    }

    private RedBlackNode rotateRight(RedBlackNode current) {
        RedBlackNode newRoot = current.getLeft();
        current.setLeft(newRoot.getRight());
        newRoot.setRight(current);
        newRoot.setRed(current.isRed());
        current.setRed(true);
        return newRoot;
    }

    private void flipColors(RedBlackNode current) {
        current.setRed(true);
        current.getLeft().setRed(false);
        current.getRight().setRed(false);
    }

    private boolean isRed(RedBlackNode node) {
        return node != null && node.isRed();
    }

    private RedBlackNode searchRecursive(RedBlackNode current, int patientId) {
        if (current == null || current.getPatientId() == patientId) {
            return current;
        }

        if (patientId < current.getPatientId()) {
            return searchRecursive(current.getLeft(), patientId);
        }

        return searchRecursive(current.getRight(), patientId);
    }

    private void buildInOrderTraversal(RedBlackNode current, StringBuilder traversal) {
        if (current == null) {
            return;
        }

        buildInOrderTraversal(current.getLeft(), traversal);

        traversal.append(String.format("ID: KB-%-5d | Name: %-18s | Location: %s%n",
                current.getPatientId(), current.getPatientName(), current.getWardName()));

        buildInOrderTraversal(current.getRight(), traversal);
    }

    public int height() {
        return heightRecursive(root);
    }

    private int heightRecursive(RedBlackNode current) {
        if (current == null) {
            return 0;
        }
        int left = heightRecursive(current.getLeft());
        int right = heightRecursive(current.getRight());
        return 1 + Math.max(left, right);
    }
}