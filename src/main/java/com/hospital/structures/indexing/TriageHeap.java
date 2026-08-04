package com.hospital.structures.indexing;

import java.util.NoSuchElementException;

/**
 * Array-backed max-heap for the A&E triage queue.
 * Priority = urgency score; ties broken by earlier arrival time.
 * Do NOT use java.util.PriorityQueue — this must be your own array + sift logic.
 */
public class TriageHeap<T> {

    public interface Comparator2<T> {
        // priority: higher urgency = higher priority.
        // On a tie, earlier arrivalTime wins (return negative if a should
        // come before b).
        int compare(T a, T b);
    }

    private Object[] data;
    private int count;
    private final Comparator2<T> comparator;

    public TriageHeap(int initialCapacity, Comparator2<T> comparator) {
        this.data = new Object[initialCapacity];
        this.count = 0;
        this.comparator = comparator;
    }

    public void insert(T item) {
        // TODO: place at end, sift up
        throw new UnsupportedOperationException("TODO");
    }

    @SuppressWarnings("unchecked")
    public T extractMax() {
        if (count == 0) throw new NoSuchElementException("heap is empty");
        // TODO: swap root with last, remove last, sift down from root
        throw new UnsupportedOperationException("TODO");
    }

    @SuppressWarnings("unchecked")
    public T peek() {
        if (count == 0) throw new NoSuchElementException("heap is empty");
        return (T) data[0];
    }

    public boolean isEmpty() {
        return count == 0;
    }

    public int size() {
        return count;
    }

    private void siftUp(int index) {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

    private void siftDown(int index) {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

    private void resize() {
        // TODO: grow the backing array when full
        throw new UnsupportedOperationException("TODO");
    }
}
