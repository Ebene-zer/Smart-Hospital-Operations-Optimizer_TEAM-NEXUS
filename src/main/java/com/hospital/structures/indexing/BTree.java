package com.hospital.structures.indexing;

public class BTree {

    private static final int MINIMUM_DEGREE = 2;

    private BTreeNode root;

    public BTree() {
        this.root = null;
    }

    public void insert(int patientId, String patientName, String wardName) {
        if (root == null) {
            root = new BTreeNode(true);
            root.setPatientIdAt(0, patientId);
            root.setPatientNameAt(0, patientName);
            root.setWardNameAt(0, wardName);
            root.setKeyCount(1);
            return;
        }

        if (root.isFull()) {
            BTreeNode newRoot = new BTreeNode(false);
            newRoot.setChildAt(0, root);
            splitChild(newRoot, 0);
            insertNonFull(newRoot, patientId, patientName, wardName);
            root = newRoot;
            return;
        }

        insertNonFull(root, patientId, patientName, wardName);
    }

    public BTreeNode search(int patientId) {
        return searchRecursive(root, patientId);
    }

    public boolean contains(int patientId) {
        return search(patientId) != null;
    }

    private BTreeNode searchRecursive(BTreeNode current, int patientId) {
        if (current == null) {
            return null;
        }

        int index = 0;
        while (index < current.getKeyCount() && patientId > current.getPatientIdAt(index)) {
            index++;
        }

        if (index < current.getKeyCount() && patientId == current.getPatientIdAt(index)) {
            return current;
        }

        if (current.isLeaf()) {
            return null;
        }

        return searchRecursive(current.getChildAt(index), patientId);
    }

    private void insertNonFull(BTreeNode current, int patientId, String patientName, String wardName) {
        int index = current.getKeyCount() - 1;

        if (current.isLeaf()) {
            while (index >= 0 && patientId < current.getPatientIdAt(index)) {
                current.setPatientIdAt(index + 1, current.getPatientIdAt(index));
                current.setPatientNameAt(index + 1, current.getPatientNameAt(index));
                current.setWardNameAt(index + 1, current.getWardNameAt(index));
                index--;
            }

            if (index >= 0 && patientId == current.getPatientIdAt(index)) {
                current.setPatientNameAt(index, patientName);
                current.setWardNameAt(index, wardName);
                return;
            }

            current.setPatientIdAt(index + 1, patientId);
            current.setPatientNameAt(index + 1, patientName);
            current.setWardNameAt(index + 1, wardName);
            current.setKeyCount(current.getKeyCount() + 1);
            return;
        }

        while (index >= 0 && patientId < current.getPatientIdAt(index)) {
            index--;
        }

        if (index >= 0 && patientId == current.getPatientIdAt(index)) {
            current.setPatientNameAt(index, patientName);
            current.setWardNameAt(index, wardName);
            return;
        }

        index++;

        BTreeNode child = current.getChildAt(index);
        if (child.isFull()) {
            splitChild(current, index);

            if (patientId > current.getPatientIdAt(index)) {
                index++;
            } else if (patientId == current.getPatientIdAt(index)) {
                current.setPatientNameAt(index, patientName);
                current.setWardNameAt(index, wardName);
                return;
            }
        }

        insertNonFull(current.getChildAt(index), patientId, patientName, wardName);
    }

    private void splitChild(BTreeNode parent, int childIndex) {
        BTreeNode fullChild = parent.getChildAt(childIndex);
        BTreeNode newChild = new BTreeNode(fullChild.isLeaf());

        newChild.setKeyCount(MINIMUM_DEGREE - 1);

        for (int i = 0; i < MINIMUM_DEGREE - 1; i++) {
            newChild.setPatientIdAt(i, fullChild.getPatientIdAt(i + MINIMUM_DEGREE));
            newChild.setPatientNameAt(i, fullChild.getPatientNameAt(i + MINIMUM_DEGREE));
            newChild.setWardNameAt(i, fullChild.getWardNameAt(i + MINIMUM_DEGREE));

            fullChild.setPatientIdAt(i + MINIMUM_DEGREE, 0);
            fullChild.setPatientNameAt(i + MINIMUM_DEGREE, null);
            fullChild.setWardNameAt(i + MINIMUM_DEGREE, null);
        }

        if (!fullChild.isLeaf()) {
            for (int i = 0; i < MINIMUM_DEGREE; i++) {
                newChild.setChildAt(i, fullChild.getChildAt(i + MINIMUM_DEGREE));
                fullChild.setChildAt(i + MINIMUM_DEGREE, null);
            }
        }

        fullChild.setKeyCount(MINIMUM_DEGREE - 1);

        for (int i = parent.getKeyCount(); i >= childIndex + 1; i--) {
            parent.setChildAt(i + 1, parent.getChildAt(i));
        }
        parent.setChildAt(childIndex + 1, newChild);

        for (int i = parent.getKeyCount() - 1; i >= childIndex; i--) {
            parent.setPatientIdAt(i + 1, parent.getPatientIdAt(i));
            parent.setPatientNameAt(i + 1, parent.getPatientNameAt(i));
            parent.setWardNameAt(i + 1, parent.getWardNameAt(i));
        }

        parent.setPatientIdAt(childIndex, fullChild.getPatientIdAt(MINIMUM_DEGREE - 1));
        parent.setPatientNameAt(childIndex, fullChild.getPatientNameAt(MINIMUM_DEGREE - 1));
        parent.setWardNameAt(childIndex, fullChild.getWardNameAt(MINIMUM_DEGREE - 1));

        fullChild.setPatientIdAt(MINIMUM_DEGREE - 1, 0);
        fullChild.setPatientNameAt(MINIMUM_DEGREE - 1, null);
        fullChild.setWardNameAt(MINIMUM_DEGREE - 1, null);

        parent.setKeyCount(parent.getKeyCount() + 1);
    }
}