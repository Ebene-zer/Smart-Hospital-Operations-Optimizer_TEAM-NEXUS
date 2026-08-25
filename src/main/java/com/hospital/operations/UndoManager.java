package com.hospital.operations;

import com.hospital.db.AuditEventDAO;
import com.hospital.structures.core.Stack;

import java.sql.SQLException;
import java.time.Instant;

/**
 * Stack-based undo for record edits. Each push is also written to
 * {@code audit_events} so the database is part of the running undo log.
 */
public class UndoManager {

    public record UndoEvent(String action, String entityTable, String entityId, String details) {
        @Override
        public String toString() {
            return action + " " + entityTable + "/" + entityId + " :: " + details;
        }
    }

    private final Stack<UndoEvent> history = new Stack<>();
    private final AuditEventDAO auditEvents;
    private final String performedBy;

    public UndoManager() {
        this(new AuditEventDAO(), "console");
    }

    public UndoManager(AuditEventDAO auditEvents, String performedBy) {
        this.auditEvents = auditEvents;
        this.performedBy = performedBy;
    }

    public void record(String action, String entityTable, String entityId, String details) {
        UndoEvent event = new UndoEvent(action, entityTable, entityId, details);
        history.push(event);
        if (auditEvents != null) {
            try {
                auditEvents.insert(action, entityTable, entityId, performedBy, Instant.now().toString());
            } catch (SQLException ignored) {
                // Demo still works if the DB is unavailable; the stack retains the event.
            }
        }
    }

    public UndoEvent undo() {
        if (history.isEmpty()) {
            throw new IllegalStateException("Nothing to undo");
        }
        UndoEvent event = history.pop();
        if (auditEvents != null) {
            try {
                auditEvents.insert("UNDO", event.entityTable(), event.entityId(), performedBy, Instant.now().toString());
            } catch (SQLException ignored) {
            }
        }
        return event;
    }

    public UndoEvent peek() {
        return history.peek();
    }

    public boolean isEmpty() {
        return history.isEmpty();
    }

    public int size() {
        return history.size();
    }
}
