package com.hospital.model;

import java.util.Date;

public class AuditEvent {
    private int eventId;
    private String actionType;
    private String description;
    private Date timestamp;

    public AuditEvent(int eventId, String actionType, String description, Date timestamp) {
        this.eventId = eventId;
        this.actionType = actionType;
        this.description = description;
        this.timestamp = timestamp;
    }

    public int getEventId() {
        return eventId;
    }

    public String getActionType() {
        return actionType;
    }

    public String getDescription() {
        return description;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return "AuditEvent{" + "eventId=" + eventId +
                ", actionType='" + actionType + '\'' +
                ", description='" + description + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}