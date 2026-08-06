package com.hospital.structures.core;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DynamicArrayTest {



    @Test
    void insertAndGet_returnsCorrectValues() {
        DynamicArray<String> roster = new DynamicArray<>();
        roster.addLast("Patient A");
        roster.addLast("Patient B");
        roster.insert(1, "Patient C"); // inserted between A and B

        assertEquals("Patient A", roster.get(0));
        assertEquals("Patient C", roster.get(1));
        assertEquals("Patient B", roster.get(2));
        assertEquals(3, roster.size());
    }

    @Test
    void set_overwritesExistingValue() {
        DynamicArray<String> roster = new DynamicArray<>();
        roster.addLast("Patient A");
        roster.set(0, "Patient A (corrected)");

        assertEquals("Patient A (corrected)", roster.get(0));
    }

    @Test
    void remove_shiftsLaterElementsLeft() {
        DynamicArray<String> roster = new DynamicArray<>();
        roster.addLast("A");
        roster.addLast("B");
        roster.addLast("C");

        String removed = roster.remove(0);

        assertEquals("A", removed);
        assertEquals("B", roster.get(0));
        assertEquals("C", roster.get(1));
        assertEquals(2, roster.size());
    }

    // ---- boundary case ----

    @Test
    void removeLastElement_leavesEmptyList() {
        DynamicArray<String> roster = new DynamicArray<>();
        roster.addLast("Only Patient");
        roster.remove(0);

        assertTrue(roster.isEmpty());
        assertEquals(0, roster.size());
    }

    @Test
    void insertAtEnd_isEquivalentToAddLast() {
        DynamicArray<String> roster = new DynamicArray<>();
        roster.addLast("A");
        roster.insert(roster.size(), "B"); // insert at index == size

        assertEquals("B", roster.get(1));
    }

    // ---- invalid input case ----

    @Test
    void get_negativeIndex_throws() {
        DynamicArray<String> roster = new DynamicArray<>();
        roster.addLast("A");
        assertThrows(IndexOutOfBoundsException.class, () -> roster.get(-1));
    }

    @Test
    void get_indexEqualToSize_throws() {
        DynamicArray<String> roster = new DynamicArray<>();
        roster.addLast("A");
        assertThrows(IndexOutOfBoundsException.class, () -> roster.get(1));
    }

    @Test
    void insert_indexGreaterThanSize_throws() {
        DynamicArray<String> roster = new DynamicArray<>();
        roster.addLast("A");
        assertThrows(IndexOutOfBoundsException.class, () -> roster.insert(5, "B"));
    }

    // ---- resize trace evidence ----
    // Start with a deliberately tiny capacity so the resize triggers within
    // a handful of inserts, mirroring "Male Surgical Ward crosses its
    // initial capacity" from the team directions. Run this test with
    // -Dtest.showOutput=true (or just `mvn test`) and copy the printed log
    // straight into the report's resize trace table.
    @Test
    void resizeTrace_capturesGrowthEvent() {
        DynamicArray<String> ward = new DynamicArray<>(2); // capacity 2

        ward.addLast("Patient 1");
        ward.addLast("Patient 2"); // still fits, size == capacity, no resize yet
        assertEquals(2, ward.capacity());

        ward.addLast("Patient 3"); // triggers resize: capacity 2 -> 4

        assertEquals(4, ward.capacity());
        assertEquals(3, ward.size());

        String trace = ward.getResizeLog();
        System.out.println("=== Dynamic Array resize trace ===");
        System.out.print(trace);

        assertTrue(trace.contains("oldCapacity=2"));
        assertTrue(trace.contains("newCapacity=4"));
    }
}