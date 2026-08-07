package com.hospital.structures.indexing;

public class BinarySearchTree {
   
    private Node root;

    public BinarySearchTree() {
        this.root = null;
    }


    public void insert(int patientId, String patientName, String wardName) {
        root = insertRecursive(root, patientId, patientName, wardName);
    }

    public boolean contains(int patientId) {
        return searchRecursive(root, patientId) != null;
    }

    public String inOrderTraversal() {
        StringBuilder traversal = new StringBuilder();
        buildInOrderTraversal(root, traversal);
        return traversal.toString();
    }

    private Node insertRecursive(Node current, int patientId, String patientName, String wardName) {

        if (current == null) {
            return new Node(patientId, patientName, wardName);
        }


        if (patientId < current.getPatientId()) {
            current.setLeft(insertRecursive(current.getLeft(), patientId, patientName, wardName));
        }

        else if (patientId > current.getPatientId()) {
            current.setRight(insertRecursive(current.getRight(), patientId, patientName, wardName));
        }

        return current;
    }


    public Node searchWithTrace(int targetId) {
        System.out.print(" Search Path for ID " + targetId + ": Root (" + (root != null ? root.getPatientId() : "Empty") + ")");
        traceSearch(root, targetId);
        Node found = searchRecursive(root, targetId);
        if (found == null) {
            System.out.println(" -> Not Found!");
        }
        return found;
    }

    private Node searchRecursive(Node current, int targetId) {

        if (current == null || current.getPatientId() == targetId) {
            return current;
        }


        if (targetId < current.getPatientId()) {
            return searchRecursive(current.getLeft(), targetId);
        }


        return searchRecursive(current.getRight(), targetId);
    }

    private void traceSearch(Node current, int targetId) {
        if (current == null) {
            return;
        }


        if (targetId < current.getPatientId()) {
            if (current.getLeft() != null) System.out.print(" -> Left (" + current.getLeft().getPatientId() + ")");
            traceSearch(current.getLeft(), targetId);
            return;
        }


        if (current.getPatientId() == targetId) {
            System.out.println(" -> Found!");
            return;
        }

        if (current.getRight() != null) System.out.print(" -> Right (" + current.getRight().getPatientId() + ")");
        traceSearch(current.getRight(), targetId);
    }


    public void printInOrderRoster() {
        inOrderRecursive(root);
    }

    private void inOrderRecursive(Node current) {
        if (current != null) {
            inOrderRecursive(current.getLeft());


            System.out.printf("ID: KB-%-5d | Name: %-18s | Location: %s%n",
                    current.getPatientId(), current.getPatientName(), current.getWardName());

            inOrderRecursive(current.getRight());
        }
    }

    private void buildInOrderTraversal(Node current, StringBuilder traversal) {
        if (current == null) {
            return;
        }

        buildInOrderTraversal(current.getLeft(), traversal);

        traversal.append(String.format("ID: KB-%-5d | Name: %-18s | Location: %s%n",
                current.getPatientId(), current.getPatientName(), current.getWardName()));

        buildInOrderTraversal(current.getRight(), traversal);
    }
}
