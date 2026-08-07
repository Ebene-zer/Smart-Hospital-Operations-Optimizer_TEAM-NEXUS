package com.hospital.structures.indexing;

class DisjointSetNode {

    private final int element;
    private DisjointSetNode parent;
    private int rank;
    private DisjointSetNode next;

    public DisjointSetNode(int element) {
        this.element = element;
        this.parent = this;
        this.rank = 0;
        this.next = null;
    }

    public int getElement() {
        return element;
    }

    public DisjointSetNode getParent() {
        return parent;
    }

    public void setParent(DisjointSetNode parent) {
        this.parent = parent;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public DisjointSetNode getNext() {
        return next;
    }

    public void setNext(DisjointSetNode next) {
        this.next = next;
    }
}