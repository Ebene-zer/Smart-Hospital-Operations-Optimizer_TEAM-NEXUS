package com.hospital.structures.core;

import org.junit.jupiter.api.Test;

import java.util.EmptyStackException;

import static org.junit.jupiter.api.Assertions.*;

class StackTest {



    @Test
    void pushThenPop_returnsInLifoOrder() {
        Stack<String> undoLog = new Stack<>();
        undoLog.push("edit 1");
        undoLog.push("edit 2");
        undoLog.push("edit 3");

        assertEquals("edit 3", undoLog.pop());
        assertEquals("edit 2", undoLog.pop());
        assertEquals("edit 1", undoLog.pop());
    }

    @Test
    void peek_doesNotRemoveTopItem() {
        Stack<String> undoLog = new Stack<>();
        undoLog.push("edit 1");

        assertEquals("edit 1", undoLog.peek());
        assertEquals(1, undoLog.size()); // still there
    }



    @Test
    void isEmpty_trueOnNewStack_falseAfterPush() {
        Stack<String> undoLog = new Stack<>();
        assertTrue(undoLog.isEmpty());

        undoLog.push("edit 1");
        assertFalse(undoLog.isEmpty());
    }

    @Test
    void popLastItem_leavesStackEmpty() {
        Stack<String> undoLog = new Stack<>();
        undoLog.push("only edit");
        undoLog.pop();

        assertTrue(undoLog.isEmpty());
    }



    @Test
    void pop_onEmptyStack_throws() {
        Stack<String> undoLog = new Stack<>();
        assertThrows(EmptyStackException.class, undoLog::pop);
    }

    @Test
    void peek_onEmptyStack_throws() {
        Stack<String> undoLog = new Stack<>();
        assertThrows(EmptyStackException.class, undoLog::peek);
    }


    @Test
    void undoLogTrace_popsMostRecentEditFirst() {
        Stack<String> undoStack = new Stack<>();

        String dischargeA = "DISCHARGE patient P-001 from Male Surgical";
        String transferB = "TRANSFER patient P-002: A&E -> ICU";
        String wrongTransferC = "TRANSFER patient P-003: Paediatric -> ICU (should be Isolation)";

        System.out.println("=== Undo log trace ===");

        undoStack.push(dischargeA);
        System.out.println("PUSH " + dischargeA + " -> stack size " + undoStack.size());

        undoStack.push(transferB);
        System.out.println("PUSH " + transferB + " -> stack size " + undoStack.size());

        undoStack.push(wrongTransferC);
        System.out.println("PUSH " + wrongTransferC + " -> stack size " + undoStack.size());


        String toUndo = undoStack.pop();
        System.out.println("POP  " + toUndo + " -> stack size " + undoStack.size());
        System.out.println("Reverse this edit and write the reversal to audit_events.");

        assertEquals(wrongTransferC, toUndo);
        assertEquals(2, undoStack.size()); // dischargeA and transferB remain
    }
}