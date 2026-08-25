package com.hospital.operations;

import com.hospital.model.ServiceRequest;
import com.hospital.structures.core.SinglyLinkedList;

/**
 * OPD reception waiting list: new arrivals join the tail; a nurse can bump a
 * deteriorating patient immediately after a named predecessor.
 */
public class OPDWaitingList {

    private final SinglyLinkedList<ServiceRequest> waiting = new SinglyLinkedList<>();

    public void arrive(ServiceRequest request) {
        waiting.addLast(require(request));
    }

    public void bumpAfter(ServiceRequest predecessor, ServiceRequest urgent) {
        waiting.insertAfter(require(predecessor), require(urgent));
    }

    public ServiceRequest serveNext() {
        if (waiting.isEmpty()) {
            throw new IllegalStateException("OPD waiting list is empty");
        }
        ServiceRequest next = waiting.peekFirst();
        waiting.remove(next);
        return next;
    }

    public boolean isEmpty() {
        return waiting.isEmpty();
    }

    public int size() {
        return waiting.size();
    }

    public String iteratorWalk() {
        StringBuilder walk = new StringBuilder();
        int position = 1;
        for (ServiceRequest request : waiting) {
            walk.append(position++)
                    .append(". ")
                    .append(request.getRequestId())
                    .append(" ")
                    .append(request.getUrgency())
                    .append(" ")
                    .append(request.getCategory())
                    .append('\n');
        }
        return walk.toString();
    }

    private static ServiceRequest require(ServiceRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("request must not be null");
        }
        return request;
    }
}
