package com.hospital.structures.indexing;

import com.hospital.model.ServiceRequest;

/**
 * Max-heap dispatcher: highest urgency is extracted first; equal urgency is
 * broken by earlier {@code timeSubmitted}. This is the scheduling heap the
 * brief requires (the existing {@link MinHeap} remains the patient-ID demo).
 */
public class UrgencyHeap {

    private ServiceRequest[] elements;
    private int size;

    public UrgencyHeap() {
        this(16);
    }

    public UrgencyHeap(int initialCapacity) {
        if (initialCapacity <= 0) {
            throw new IllegalArgumentException("initialCapacity must be positive");
        }
        this.elements = new ServiceRequest[initialCapacity];
        this.size = 0;
    }

    public void insert(ServiceRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("request must not be null");
        }
        ensureCapacity(size + 1);
        elements[size] = request;
        siftUp(size);
        size++;
    }

    public ServiceRequest extractMax() {
        if (size == 0) {
            return null;
        }
        ServiceRequest max = elements[0];
        size--;
        elements[0] = elements[size];
        elements[size] = null;
        if (size > 0) {
            siftDown(0);
        }
        return max;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    public static int urgencyScore(String urgency) {
        if (urgency == null) {
            return 0;
        }
        return switch (urgency.trim().toUpperCase()) {
            case "CRITICAL" -> 4;
            case "HIGH" -> 3;
            case "MEDIUM" -> 2;
            case "LOW" -> 1;
            default -> 0;
        };
    }

    private void siftUp(int index) {
        int current = index;
        while (current > 0) {
            int parent = (current - 1) / 2;
            if (compare(elements[current], elements[parent]) <= 0) {
                break;
            }
            swap(current, parent);
            current = parent;
        }
    }

    private void siftDown(int index) {
        int current = index;
        while (true) {
            int left = current * 2 + 1;
            int right = left + 1;
            int best = current;
            if (left < size && compare(elements[left], elements[best]) > 0) {
                best = left;
            }
            if (right < size && compare(elements[right], elements[best]) > 0) {
                best = right;
            }
            if (best == current) {
                break;
            }
            swap(current, best);
            current = best;
        }
    }

    static int compare(ServiceRequest a, ServiceRequest b) {
        int urgency = Integer.compare(urgencyScore(a.getUrgency()), urgencyScore(b.getUrgency()));
        if (urgency != 0) {
            return urgency;
        }
        String ta = a.getTimeSubmitted() == null ? "" : a.getTimeSubmitted();
        String tb = b.getTimeSubmitted() == null ? "" : b.getTimeSubmitted();
        int time = ta.compareTo(tb);
        if (time != 0) {
            return -time;
        }
        return a.getRequestId().compareTo(b.getRequestId());
    }

    private void swap(int i, int j) {
        ServiceRequest tmp = elements[i];
        elements[i] = elements[j];
        elements[j] = tmp;
    }

    private void ensureCapacity(int min) {
        if (min <= elements.length) {
            return;
        }
        ServiceRequest[] grown = new ServiceRequest[elements.length * 2];
        for (int i = 0; i < size; i++) {
            grown[i] = elements[i];
        }
        elements = grown;
    }
}
