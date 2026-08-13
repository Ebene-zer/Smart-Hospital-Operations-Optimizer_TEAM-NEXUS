package com.hospital.model;

import java.util.Objects;

public class ServiceRequest {
    private final int requestId;
    private final Integer sourceId;
    private final Integer destinationId;
    private final String category;
    private final Integer urgency;
    private final String timeSubmitted;
    private final String deadline;
    private final String status;

    public ServiceRequest(int requestId, Integer sourceId, Integer destinationId, String category, Integer urgency,
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

    public int getRequestId() {
        return requestId;
    }

    public Integer getSourceId() {
        return sourceId;
    }

    public Integer getDestinationId() {
        return destinationId;
    }

    public String getCategory() {
        return category;
    }

    public Integer getUrgency() {
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
        return requestId == that.requestId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(requestId);
    }
}
