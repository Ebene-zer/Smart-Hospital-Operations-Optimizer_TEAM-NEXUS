package com.hospital.structures.indexing;

public class DisjointSet {

    private DisjointSetNode head;

    public DisjointSet() {
        this.head = null;
    }

    public void makeSet(int element) {
        if (findNodeByElement(element) != null) {
            return;
        }

        DisjointSetNode newNode = new DisjointSetNode(element);
        newNode.setNext(head);
        head = newNode;
    }

    public int find(int element) {
        DisjointSetNode node = findNodeByElement(element);
        if (node == null) {
            return -1;
        }

        return findRepresentative(node).getElement();
    }

    public void union(int firstElement, int secondElement) {
        DisjointSetNode firstNode = findNodeByElement(firstElement);
        DisjointSetNode secondNode = findNodeByElement(secondElement);

        if (firstNode == null || secondNode == null) {
            return;
        }

        DisjointSetNode firstRoot = findRepresentative(firstNode);
        DisjointSetNode secondRoot = findRepresentative(secondNode);

        if (firstRoot == secondRoot) {
            return;
        }

        if (firstRoot.getRank() < secondRoot.getRank()) {
            firstRoot.setParent(secondRoot);
        } else if (firstRoot.getRank() > secondRoot.getRank()) {
            secondRoot.setParent(firstRoot);
        } else {
            secondRoot.setParent(firstRoot);
            firstRoot.setRank(firstRoot.getRank() + 1);
        }
    }

    private DisjointSetNode findRepresentative(DisjointSetNode node) {
        if (node.getParent() != node) {
            node.setParent(findRepresentative(node.getParent()));
        }

        return node.getParent();
    }

    private DisjointSetNode findNodeByElement(int element) {
        DisjointSetNode current = head;
        while (current != null) {
            if (current.getElement() == element) {
                return current;
            }
            current = current.getNext();
        }

        return null;
    }
}