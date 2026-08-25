package com.hospital.operations;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UndoManagerTest {

    @Test
    void undoIsLifoAndRejectsEmpty() {
        UndoManager undo = new UndoManager(null, "test");
        undo.record("EDIT", "service_requests", "SR001", "wrong ward");
        undo.record("DISCHARGE", "service_requests", "SR002", "accidental");
        assertEquals("SR002", undo.undo().entityId());
        assertEquals("SR001", undo.undo().entityId());
        assertTrue(undo.isEmpty());
        assertThrows(IllegalStateException.class, undo::undo);
    }
}
