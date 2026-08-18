package com.hospital.operations;

import com.hospital.structures.core.Queue;

public class PharmacyQueue {
    private final Queue<String> queue;

    public PharmacyQueue(int capacity) {
        queue = new Queue<String>(capacity);
    }

    // Add a patient to the back of the pharmacy queue
    public void addPatient(String patient) {
        queue.enqueue(patient);
    }

    // Serve the next patient who has waited the longest
    public String serveNext() {
        return queue.dequeue();
    }

    //View the next patient to be served
    public String getNextPatient() {
        return queue.peek();
    }

    public boolean hasPatients() {
        return !queue.isEmpty();
    }

    public boolean isFull() {
        return queue.isFull();
    }

    public int getQueueSize() {
        return queue.size();
    }
}
