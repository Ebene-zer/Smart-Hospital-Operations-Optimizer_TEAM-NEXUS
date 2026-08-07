package com.hospital.structures.indexing;

class RedBlackNode {
    private static final boolean RED = true;
    private static final boolean BLACK = false;

    private final int patientId;
    private final String patientName;
    private final String wardName;
    private RedBlackNode left;
    private RedBlackNode right;
    private boolean color;

    public RedBlackNode(int patientId, String patientName, String wardName) {
        this.patientId = patientId;
        this.patientName = patientName;
        this.wardName = wardName;
        this.color = RED;
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

    public RedBlackNode getLeft() {
        return left;
    }

    public void setLeft(RedBlackNode left) {
        this.left = left;
    }

    public RedBlackNode getRight() {
        return right;
    }

    public void setRight(RedBlackNode right) {
        this.right = right;
    }

    public boolean isRed() {
        return color == RED;
    }

    public void setRed(boolean red) {
        this.color = red ? RED : BLACK;
    }
}