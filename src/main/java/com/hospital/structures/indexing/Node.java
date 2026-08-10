package com.hospital.structures.indexing;

class Node {
    private final int patientId;
    private final String patientName;
    private final String wardName;
    private Node left;
    private Node right;

    public Node(int patientId, String patientName, String wardName) {
        this.patientId = patientId;
        this.patientName = patientName;
        this.wardName = wardName;
        this.left = null;
        this.right = null;
    }

    public int getPatientId() {
        return patientId;
    }

    public String getPatientName() {
        return patientName;
    }

    public String getWardName() {
        return wardName;
    }

    public Node getLeft() {
        return left;
    }

    public void setLeft(Node left) {
        this.left = left;
    }

    public Node getRight() {
        return right;
    }

    public void setRight(Node right) {
        this.right = right;
    }
    }


