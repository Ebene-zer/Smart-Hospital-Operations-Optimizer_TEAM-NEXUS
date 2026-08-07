package com.hospital.structures.indexing;

class BTreeNode {

    private static final int MINIMUM_DEGREE = 2;

    private final int[] patientIds;
    private final String[] patientNames;
    private final String[] wardNames;
    private final BTreeNode[] children;
    private int keyCount;
    private boolean leaf;

    public BTreeNode(boolean leaf) {
        this.patientIds = new int[(2 * MINIMUM_DEGREE) - 1];
        this.patientNames = new String[(2 * MINIMUM_DEGREE) - 1];
        this.wardNames = new String[(2 * MINIMUM_DEGREE) - 1];
        this.children = new BTreeNode[2 * MINIMUM_DEGREE];
        this.keyCount = 0;
        this.leaf = leaf;
    }

    public int getKeyCount() {
        return keyCount;
    }

    public void setKeyCount(int keyCount) {
        this.keyCount = keyCount;
    }

    public boolean isLeaf() {
        return leaf;
    }

    public void setLeaf(boolean leaf) {
        this.leaf = leaf;
    }

    public boolean isFull() {
        return keyCount == patientIds.length;
    }

    public int getPatientIdAt(int index) {
        return patientIds[index];
    }

    public void setPatientIdAt(int index, int patientId) {
        patientIds[index] = patientId;
    }

    public String getPatientNameAt(int index) {
        return patientNames[index];
    }

    public void setPatientNameAt(int index, String patientName) {
        patientNames[index] = patientName;
    }

    public String getWardNameAt(int index) {
        return wardNames[index];
    }

    public void setWardNameAt(int index, String wardName) {
        wardNames[index] = wardName;
    }

    public BTreeNode getChildAt(int index) {
        return children[index];
    }

    public void setChildAt(int index, BTreeNode child) {
        children[index] = child;
    }
}