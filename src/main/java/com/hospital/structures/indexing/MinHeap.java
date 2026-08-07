package com.hospital.structures.indexing;

public class MinHeap {

    private static final int DEFAULT_CAPACITY = 16;

    private MinHeapNode[] elements;
    private int size;

    public MinHeap() {
        this(DEFAULT_CAPACITY);
    }

    public MinHeap(int initialCapacity) {
        if (initialCapacity <= 0) {
            throw new IllegalArgumentException("initialCapacity must be positive");
        }

        this.elements = new MinHeapNode[initialCapacity];
        this.size = 0;
    }

    public void insert(int patientId, String patientName, String wardName) {
        ensureCapacity(size + 1);
        elements[size] = new MinHeapNode(patientId, patientName, wardName);
        siftUp(size);
        size++;
    }

    public MinHeapNode extractMin() {
        if (size == 0) {
            return null;
        }

        MinHeapNode minimum = elements[0];
        size--;
        elements[0] = elements[size];
        elements[size] = null;

        if (size > 0) {
            siftDown(0);
        }

        return minimum;
    }

    public void heapify() {
        for (int index = (size / 2) - 1; index >= 0; index--) {
            siftDown(index);
        }
    }

    private void siftUp(int index) {
        int currentIndex = index;
        while (currentIndex > 0) {
            int parentIndex = (currentIndex - 1) / 2;
            if (elements[currentIndex].getPatientId() >= elements[parentIndex].getPatientId()) {
                break;
            }

            swap(currentIndex, parentIndex);
            currentIndex = parentIndex;
        }
    }

    private void siftDown(int index) {
        int currentIndex = index;

        while (true) {
            int leftChildIndex = (currentIndex * 2) + 1;
            int rightChildIndex = (currentIndex * 2) + 2;
            int smallestIndex = currentIndex;

            if (leftChildIndex < size && elements[leftChildIndex].getPatientId() < elements[smallestIndex].getPatientId()) {
                smallestIndex = leftChildIndex;
            }

            if (rightChildIndex < size && elements[rightChildIndex].getPatientId() < elements[smallestIndex].getPatientId()) {
                smallestIndex = rightChildIndex;
            }

            if (smallestIndex == currentIndex) {
                break;
            }

            swap(currentIndex, smallestIndex);
            currentIndex = smallestIndex;
        }
    }

    private void swap(int firstIndex, int secondIndex) {
        MinHeapNode temporary = elements[firstIndex];
        elements[firstIndex] = elements[secondIndex];
        elements[secondIndex] = temporary;
    }

    private void ensureCapacity(int minimumCapacity) {
        if (minimumCapacity <= elements.length) {
            return;
        }

        int newCapacity = elements.length * 2;
        if (newCapacity < minimumCapacity) {
            newCapacity = minimumCapacity;
        }

        MinHeapNode[] expanded = new MinHeapNode[newCapacity];
        for (int index = 0; index < size; index++) {
            expanded[index] = elements[index];
        }

        elements = expanded;
    }
}