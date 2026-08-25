package com.hospital.structures.core;


import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class QueueTest {

    @Test
    void testNewQueue() {
        Queue<Integer> queue = new Queue<>(5);
        assertEquals(0, queue.size());
        assertTrue(queue.isEmpty());
    }

    @Test
    void testEnqueue() {
        Queue<Integer> queue = new Queue<>(3);
        queue.enqueue(1);
        queue.enqueue(2);
        assertEquals(2, queue.size());
        assertFalse(queue.isEmpty());
    }

    @Test
    void testDequeue() {
        Queue<Integer> queue = new Queue<>(3);
        queue.enqueue(1);
        queue.enqueue(2);
        assertEquals(1, queue.dequeue());
        assertEquals(1, queue.size());
    }

    @Test
    void testPeek() {
        Queue<Integer> queue = new Queue<>(3);
        queue.enqueue(1);
        assertEquals(1, queue.peek());
        assertEquals(1, queue.size()); // Size should remain the same after peek
    }

    @Test
    void testIsFull() {
        Queue<Integer> queue = new Queue<>(2);
        queue.enqueue(1);
        queue.enqueue(2);
        assertTrue(queue.isFull());
    }

    @Test
    void testClear() {
        Queue<Integer> queue = new Queue<>(3);
        queue.enqueue(1);
        queue.enqueue(2);
        queue.clear();
        assertEquals(0, queue.size());
        assertTrue(queue.isEmpty());
        queue.enqueue(3);
        assertEquals(3, queue.peek());
    }

    @Test
    void testCapacity() {
        Queue<Integer> queue = new Queue<>(5);
        assertEquals(5, queue.capacity());
    }

    @Test
    void testToString() {
        Queue<Integer> queue = new Queue<>(3);
        queue.enqueue(1);
        queue.enqueue(2);
        assertEquals("Queue: [1, 2]", queue.toString());
    }

    @Test
    void testDequeueEmptyQueue() {
        Queue<Integer> queue = new Queue<>(3);
        assertThrows(IllegalStateException.class, queue::dequeue);
    }

    @Test
    void testEnqueueFullQueue() {
        Queue<Integer> queue = new Queue<>(2);
        queue.enqueue(1);
        queue.enqueue(2);
        assertThrows(IllegalStateException.class, () -> queue.enqueue(3));
    }

    @Test
    void testPeekEmptyQueue() {
        Queue<Integer> queue = new Queue<>(3);
        assertThrows(IllegalStateException.class, queue::peek);
    }


    @Test
    void testFIFOOrder() {
        Queue<Integer> queue = new Queue<>(5);
        queue.enqueue(1);
        queue.enqueue(2);
        queue.enqueue(3);
        assertEquals(1, queue.dequeue());
        assertEquals(2, queue.dequeue());
        assertEquals(3, queue.dequeue());
    }

    @Test
    void fillDrainEnqueue_reusesCapacityAfterLinearQueueEmpties() {
        Queue<String> queue = new Queue<>(2);
        queue.enqueue("A");
        queue.enqueue("B");
        assertTrue(queue.isFull());
        assertEquals("A", queue.dequeue());
        assertEquals("B", queue.dequeue());
        assertTrue(queue.isEmpty());
        assertFalse(queue.isFull());

        queue.enqueue("C");
        queue.enqueue("D");
        assertEquals("C", queue.dequeue());
        assertEquals("D", queue.dequeue());
    }

    @Test
    void enqueueAfterPartialDequeue_compactsInsteadOfReportingFull() {
        Queue<Integer> queue = new Queue<>(3);
        queue.enqueue(1);
        queue.enqueue(2);
        queue.enqueue(3);
        assertEquals(1, queue.dequeue());
        queue.enqueue(4);
        assertEquals(2, queue.dequeue());
        assertEquals(3, queue.dequeue());
        assertEquals(4, queue.dequeue());
    }
}
