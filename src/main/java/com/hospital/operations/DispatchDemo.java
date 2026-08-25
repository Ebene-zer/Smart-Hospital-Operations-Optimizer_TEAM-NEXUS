package com.hospital.operations;

import com.hospital.model.ServiceRequest;
import com.hospital.structures.core.CircularQueue;
import com.hospital.structures.core.Deque;
import com.hospital.structures.core.Queue;
import com.hospital.structures.indexing.UrgencyHeap;

/**
 * Side-by-side dispatch of the same request batch under FIFO, circular-queue
 * rotation, deque (trauma-to-front) and urgency-heap rules.
 */
public final class DispatchDemo {

    private DispatchDemo() {
    }

    public static String compare(ServiceRequest[] batch) {
        if (batch == null) {
            throw new IllegalArgumentException("batch must not be null");
        }
        StringBuilder out = new StringBuilder();
        out.append("Dispatch comparison on ").append(batch.length).append(" requests\n\n");
        out.append("FIFO queue:\n").append(fifo(batch)).append('\n');
        out.append("Circular queue (one rotation of the line):\n").append(circular(batch)).append('\n');
        out.append("Deque (CRITICAL/HIGH jump the front):\n").append(deque(batch)).append('\n');
        out.append("Urgency heap (extractMax):\n").append(heap(batch)).append('\n');
        return out.toString();
    }

    public static String fifo(ServiceRequest[] batch) {
        Queue<ServiceRequest> queue = new Queue<>(Math.max(1, batch.length));
        for (ServiceRequest request : batch) {
            queue.enqueue(request);
        }
        return drainQueue(queue);
    }

    public static String circular(ServiceRequest[] batch) {
        CircularQueue<ServiceRequest> queue = new CircularQueue<>(Math.max(1, batch.length));
        for (ServiceRequest request : batch) {
            queue.enqueue(request);
        }
        StringBuilder order = new StringBuilder();
        int n = queue.size();
        for (int i = 0; i < n; i++) {
            ServiceRequest request = queue.dequeue();
            order.append(i + 1).append(". ").append(label(request)).append('\n');
            queue.enqueue(request);
        }
        return order.toString();
    }

    public static String deque(ServiceRequest[] batch) {
        Deque<ServiceRequest> intake = new Deque<>();
        for (ServiceRequest request : batch) {
            int score = UrgencyHeap.urgencyScore(request.getUrgency());
            if (score >= 3) {
                intake.addFront(request);
            } else {
                intake.addRear(request);
            }
        }
        StringBuilder order = new StringBuilder();
        int step = 1;
        while (!intake.isEmpty()) {
            order.append(step++).append(". ").append(label(intake.removeFront())).append('\n');
        }
        return order.toString();
    }

    public static String heap(ServiceRequest[] batch) {
        UrgencyHeap heap = new UrgencyHeap();
        for (ServiceRequest request : batch) {
            heap.insert(request);
        }
        StringBuilder order = new StringBuilder();
        int step = 1;
        while (!heap.isEmpty()) {
            order.append(step++).append(". ").append(label(heap.extractMax())).append('\n');
        }
        return order.toString();
    }

    public static String traumaWalkInTrace() {
        ServiceRequest[] evening = new ServiceRequest[] {
                req("SR-W01", "LOW", "2024-01-02 18:01"),
                req("SR-W02", "MEDIUM", "2024-01-02 18:03"),
                req("SR-W03", "LOW", "2024-01-02 18:04"),
                req("SR-W04", "MEDIUM", "2024-01-02 18:06"),
                req("SR-W05", "LOW", "2024-01-02 18:08"),
                req("SR-W06", "HIGH", "2024-01-02 18:09"),
                req("SR-W07", "LOW", "2024-01-02 18:11"),
                req("SR-W08", "MEDIUM", "2024-01-02 18:12"),
                req("SR-W09", "LOW", "2024-01-02 18:14"),
                req("SR-W10", "MEDIUM", "2024-01-02 18:15"),
                req("SR-T01", "CRITICAL", "2024-01-02 18:16"),
                req("SR-T02", "CRITICAL", "2024-01-02 18:18")
        };
        StringBuilder out = new StringBuilder();
        out.append("Busy A&E evening: 10 walk-ins then 2 trauma cases\n\n");
        out.append("Heap extractMax order (critical first, then earlier time):\n");
        out.append(heap(evening));
        out.append("\nDeque order (trauma/high jump the front as they arrive):\n");
        out.append(deque(evening));
        return out.toString();
    }

    private static String drainQueue(Queue<ServiceRequest> queue) {
        StringBuilder order = new StringBuilder();
        int step = 1;
        while (!queue.isEmpty()) {
            order.append(step++).append(". ").append(label(queue.dequeue())).append('\n');
        }
        return order.toString();
    }

    private static String label(ServiceRequest request) {
        return request.getRequestId() + " [" + request.getUrgency() + " @ " + request.getTimeSubmitted() + "]";
    }

    private static ServiceRequest req(String id, String urgency, String time) {
        return new ServiceRequest(id, "L001", "L001", "EMERGENCY", urgency, time, time, "PENDING");
    }
}
