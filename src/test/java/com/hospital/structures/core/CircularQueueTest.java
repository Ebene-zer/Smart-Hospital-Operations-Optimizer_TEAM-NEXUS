package com.hospital.structures.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class CircularQueueTest {
    @Test
    @DisplayName("Test all methods of CircularQueue class")

    void testNewCircularQueue() {
        CircularQueue<Integer> queue = new CircularQueue<>(5);
        assertTrue(queue.isEmpty());
        assertFalse(queue.isFull());
        assertEquals(0, queue.size());
    }

    @Test
    void testEnqueue() {
        CircularQueue<Integer> queue = new CircularQueue<>(5);
        queue.enqueue(1);
        queue.enqueue(2);
        queue.enqueue(3);
        assertEquals(3, queue.size());
        assertFalse(queue.isEmpty());
        assertFalse(queue.isFull());
    }

    @Test
    void testDequeue() {
        CircularQueue<Integer> queue = new CircularQueue<>(5);
        queue.enqueue(1);
        queue.enqueue(2);
        queue.enqueue(3);
        assertEquals(1, queue.dequeue());
        assertEquals(2, queue.size());
    }

    @Test
    void testPeek() {
        CircularQueue<Integer> queue = new CircularQueue<>(5);
        queue.enqueue(1);
        queue.enqueue(2);
        assertEquals(1, queue.peek());
    }

    @Test
    void testIsEmpty() {
        CircularQueue<Integer> queue = new CircularQueue<>(5);
        assertTrue(queue.isEmpty());
        queue.enqueue(1);
        assertFalse(queue.isEmpty());
    }


    @Test
    void testIsFull() {
        CircularQueue<Integer> queue = new CircularQueue<>(2);
        assertFalse(queue.isFull());
        queue.enqueue(1);
        queue.enqueue(2);
        assertTrue(queue.isFull());
    }

    @Test
    void testSize() {
        CircularQueue<Integer> queue = new CircularQueue<>(5);
        assertEquals(0, queue.size());
        queue.enqueue(1);
        queue.enqueue(2);
        assertEquals(2, queue.size());
    }

    @Test
    void testClear() {
        CircularQueue<Integer> queue = new CircularQueue<>(5);
        queue.enqueue(1);
        queue.enqueue(2);
        queue.clear();
        assertTrue(queue.isEmpty());
        assertEquals(0, queue.size());
    }

    @Test
    void testOverflow() {
        CircularQueue<Integer> queue = new CircularQueue<>(2);
        queue.enqueue(1);
        queue.enqueue(2);
        try {
            queue.enqueue(3);
        } catch (IllegalStateException e) {
            assertEquals("Overflow: Queue is full", e.getMessage());
        }
    }

    @Test
    void testUnderflow() {
        CircularQueue<Integer> queue = new CircularQueue<>(2);
        try {
            queue.dequeue();
        } catch (IllegalStateException e) {
            assertEquals("Underflow: Queue is empty", e.getMessage());
        }
    }

    @Test
    void testToString() {
        CircularQueue<Integer> queue = new CircularQueue<>(5);
        queue.enqueue(1);
        queue.enqueue(2);
        queue.enqueue(3);
        assertEquals("Queue: [1, 2, 3]", queue.toString());
    }
}
