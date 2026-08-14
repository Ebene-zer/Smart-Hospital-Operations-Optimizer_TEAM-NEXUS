package com.hospital.model;

import java.util.Objects;

public class ServiceRequest {
    private final String requestId;
    private final String sourceId;
    private final String destinationId;
    private final String category;
    private final String urgency;
    private final String timeSubmitted;
    private final String deadline;
    private final String status;

    public ServiceRequest(String requestId, String sourceId, String destinationId, String category, String urgency,
            String timeSubmitted, String deadline, String status) {
        this.requestId = requestId;
        this.sourceId = sourceId;
        this.destinationId = destinationId;
        this.category = category;
        this.urgency = urgency;
        this.timeSubmitted = timeSubmitted;
        this.deadline = deadline;
        this.status = status;
    }

    public String getRequestId() {
        return requestId;
    }

    public String getSourceId() {
        return sourceId;
    }

    public String getDestinationId() {
        return destinationId;
    }

    public String getCategory() {
        return category;
    }

    public String getUrgency() {
        return urgency;
    }

    public String getTimeSubmitted() {
        return timeSubmitted;
    }

    public String getDeadline() {
        return deadline;
    }

    public String getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return "ServiceRequest{" +
                "requestId=" + requestId +
                ", sourceId=" + sourceId +
                ", destinationId=" + destinationId +
                ", category='" + category + '\'' +
                ", urgency=" + urgency +
                ", timeSubmitted='" + timeSubmitted + '\'' +
                ", deadline='" + deadline + '\'' +
                ", status='" + status + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof ServiceRequest that))
            return false;
        return Objects.equals(requestId, that.requestId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(requestId);
    }
}
