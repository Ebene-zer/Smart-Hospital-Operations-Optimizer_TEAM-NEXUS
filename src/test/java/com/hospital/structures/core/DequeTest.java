package com.hospital.structures.core;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class DequeTest {
    @Test
    void testNewDeque() {
        Deque<Integer> deque = new Deque<>();
        assertEquals(0, deque.size());
        assertTrue(deque.isEmpty());
    }

    @Test
    void testAddFront() {
        Deque<Integer> deque = new Deque<>();
        deque.addFront(1);
        deque.addFront(2);
        assertEquals(2, deque.size());
        assertFalse(deque.isEmpty());
        assertEquals(2, deque.peekFront());
    }

    @Test
    void testAddRear() {
        Deque<Integer> deque = new Deque<>();
        deque.addRear(1);
        deque.addRear(2);
        assertEquals(2, deque.size());
        assertFalse(deque.isEmpty());
        assertEquals(2, deque.peekRear());
    }

    @Test
    void testRemoveFront() {
        Deque<Integer> deque = new Deque<>();
        deque.addFront(1);
        deque.addFront(2);
        assertEquals(2, deque.removeFront());
        assertEquals(1, deque.size());
    }

    @Test
    void testRemoveRear() {
        Deque<Integer> deque = new Deque<>();
        deque.addRear(1);
        deque.addRear(2);
        assertEquals(2, deque.removeRear());
        assertEquals(1, deque.size());
    }

    @Test
    void testPeekFront() {
        Deque<Integer> deque = new Deque<>();
        deque.addFront(1);
        assertEquals(1, deque.peekFront());
        assertEquals(1, deque.size()); // Size should remain the same after peek
    }

    @Test
    void testPeekRear() {
        Deque<Integer> deque = new Deque<>();
        deque.addRear(1);
        assertEquals(1, deque.peekRear());
        assertEquals(1, deque.size()); // Size should remain the same after peek
    }

    @Test
    void testClear() {
        Deque<Integer> deque = new Deque<>();
        deque.addFront(1);
        deque.addRear(2);
        deque.clear();
        assertEquals(0, deque.size());
        assertTrue(deque.isEmpty());
    }

    @Test
    void testIsEmpty() {
        Deque<Integer> deque = new Deque<>();
        assertTrue(deque.isEmpty());
        deque.addFront(1);
        assertFalse(deque.isEmpty());
    }

    @Test
    void testSize() {
        Deque<Integer> deque = new Deque<>();
        assertEquals(0, deque.size());
        deque.addFront(1);
        assertEquals(1, deque.size());
    }

    @Test
    void testToString() {
        Deque<Integer> deque = new Deque<>();
        deque.addFront(1);
        deque.addRear(2);
        assertEquals("Deque: [1, 2]", deque.toString());
    }

}
