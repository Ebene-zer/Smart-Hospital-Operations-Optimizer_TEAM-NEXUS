package com.hospital.structures.core;

import org.junit.jupiter.api.Test;

import java.util.Iterator;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

class SinglyLinkedListTest {



    @Test
    void addLast_appendsInArrivalOrder() {
        SinglyLinkedList<String> opd = new SinglyLinkedList<>();
        opd.addLast("Patient A");
        opd.addLast("Patient B");
        opd.addLast("Patient C");

        assertEquals("Patient A", opd.peekFirst());
        assertEquals("Patient C", opd.peekLast());
        assertEquals(3, opd.size());
    }

    @Test
    void insertAfter_reprioritisesDeterioratingPatient() {
        SinglyLinkedList<String> opd = new SinglyLinkedList<>();
        opd.addLast("Patient A"); // walk-in
        opd.addLast("Patient B"); // walk-in
        opd.addLast("Patient C"); // walk-in


        opd.insertAfter("Patient A", "Patient X (urgent)");

        Iterator<String> it = opd.iterator();
        assertEquals("Patient A", it.next());
        assertEquals("Patient X (urgent)", it.next());
        assertEquals("Patient B", it.next());
        assertEquals("Patient C", it.next());
        assertFalse(it.hasNext());
    }

    @Test
    void remove_middleNode_reconnectsList() {
        SinglyLinkedList<String> opd = new SinglyLinkedList<>();
        opd.addLast("A");
        opd.addLast("B");
        opd.addLast("C");

        assertTrue(opd.remove("B"));

        Iterator<String> it = opd.iterator();
        assertEquals("A", it.next());
        assertEquals("C", it.next());
        assertFalse(it.hasNext());
    }



    @Test
    void addFirst_onEmptyList_setsHeadAndTail() {
        SinglyLinkedList<String> opd = new SinglyLinkedList<>();
        opd.addFirst("Only Patient");

        assertEquals("Only Patient", opd.peekFirst());
        assertEquals("Only Patient", opd.peekLast());
    }

    @Test
    void insertAfter_onTail_updatesTailPointer() {
        SinglyLinkedList<String> opd = new SinglyLinkedList<>();
        opd.addLast("A");
        opd.insertAfter("A", "B"); // B becomes new tail

        opd.addLast("C"); // must correctly attach after B, not the stale tail
        Iterator<String> it = opd.iterator();
        assertEquals("A", it.next());
        assertEquals("B", it.next());
        assertEquals("C", it.next());
    }

    @Test
    void remove_lastRemainingNode_leavesListEmpty() {
        SinglyLinkedList<String> opd = new SinglyLinkedList<>();
        opd.addLast("Only Patient");
        opd.remove("Only Patient");

        assertTrue(opd.isEmpty());
        assertThrows(NoSuchElementException.class, opd::peekFirst);
    }



    @Test
    void insertAfter_targetNotPresent_throws() {
        SinglyLinkedList<String> opd = new SinglyLinkedList<>();
        opd.addLast("A");
        assertThrows(NoSuchElementException.class,
                () -> opd.insertAfter("Nonexistent Patient", "X"));
    }

    @Test
    void peekFirst_onEmptyList_throws() {
        SinglyLinkedList<String> opd = new SinglyLinkedList<>();
        assertThrows(NoSuchElementException.class, opd::peekFirst);
    }


    @Test
    void iteratorDemo_walksFullWaitingListInOrder() {
        SinglyLinkedList<String> opd = new SinglyLinkedList<>();
        opd.addLast("Patient A (08:01)");
        opd.addLast("Patient B (08:05)");
        opd.addLast("Patient C (08:10)");
        opd.insertAfter("Patient A (08:01)", "Patient X - deteriorating, bumped up");

        System.out.println("=== OPD waiting list — iterator walk ===");
        int position = 1;
        for (String patient : opd) { // uses the custom Iterator under the hood
            System.out.println(position++ + ". " + patient);
        }

        assertEquals(4, opd.size());
    }
}
