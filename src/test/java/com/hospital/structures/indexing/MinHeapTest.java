package com.hospital.structures.indexing;

import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MinHeapTest {

    @Test
    void insertSinglePatient_makesPatientExtractable() {
        // Arrange
        MinHeap heap = new MinHeap();

        // Act
        heap.insert(101, "Ama Owusu", "Emergency");
        MinHeapNode extracted = heap.extractMin();

        // Assert
        assertNotNull(extracted);
        assertEquals(101, extracted.getPatientId());
        assertEquals("Ama Owusu", extracted.getPatientName());
        assertEquals("Emergency", extracted.getWardName());
    }

    @Test
    void extractMinFromEmptyHeap_returnsNull() {
        // Arrange
        MinHeap heap = new MinHeap();

        // Act
        MinHeapNode extracted = heap.extractMin();

        // Assert
        assertNull(extracted);
    }

    @Test
    void singleElementHeap_returnsElementThenBecomesEmpty() {
        // Arrange
        MinHeap heap = new MinHeap();
        heap.insert(55, "Patient 55", "Ward X");

        // Act
        MinHeapNode firstExtraction = heap.extractMin();
        MinHeapNode secondExtraction = heap.extractMin();

        // Assert
        assertNotNull(firstExtraction);
        assertEquals(55, firstExtraction.getPatientId());
        assertNull(secondExtraction);
    }

    @Test
    void multipleInsertions_extractInAscendingHeapOrder() {
        // Arrange
        MinHeap heap = new MinHeap();

        // Act
        heap.insert(40, "Patient 40", "Ward A");
        heap.insert(10, "Patient 10", "Ward B");
        heap.insert(30, "Patient 30", "Ward C");
        heap.insert(20, "Patient 20", "Ward D");
        heap.insert(50, "Patient 50", "Ward E");

        MinHeapNode first = heap.extractMin();
        MinHeapNode second = heap.extractMin();
        MinHeapNode third = heap.extractMin();
        MinHeapNode fourth = heap.extractMin();
        MinHeapNode fifth = heap.extractMin();

        // Assert
        assertEquals(10, first.getPatientId());
        assertEquals(20, second.getPatientId());
        assertEquals(30, third.getPatientId());
        assertEquals(40, fourth.getPatientId());
        assertEquals(50, fifth.getPatientId());
        assertNull(heap.extractMin());
    }

    @Test
    void heapOrderingIsPreservedAfterUnsortedInsertions() {
        // Arrange
        MinHeap heap = new MinHeap();

        // Act
        heap.insert(75, "Patient 75", "Ward G");
        heap.insert(12, "Patient 12", "Ward D");
        heap.insert(87, "Patient 87", "Ward H");
        heap.insert(25, "Patient 25", "Ward B");
        heap.insert(62, "Patient 62", "Ward F");

        int first = heap.extractMin().getPatientId();
        int second = heap.extractMin().getPatientId();
        int third = heap.extractMin().getPatientId();
        int fourth = heap.extractMin().getPatientId();
        int fifth = heap.extractMin().getPatientId();

        // Assert
        assertTrue(first < second);
        assertTrue(second < third);
        assertTrue(third < fourth);
        assertTrue(fourth < fifth);
        assertEquals(12, first);
        assertEquals(25, second);
        assertEquals(62, third);
        assertEquals(75, fourth);
        assertEquals(87, fifth);
    }

    @Test
    void duplicateValues_areStoredAndExtractedIndividually() {
        // Arrange
        MinHeap heap = new MinHeap();

        // Act
        heap.insert(42, "Original Name", "Ward A");
        heap.insert(42, "Updated Name", "Ward B");

        MinHeapNode firstExtraction = heap.extractMin();
        MinHeapNode secondExtraction = heap.extractMin();

        // Assert
        assertNotNull(firstExtraction);
        assertNotNull(secondExtraction);
        assertEquals(42, firstExtraction.getPatientId());
        assertEquals(42, secondExtraction.getPatientId());

        Set<String> names = new HashSet<>();
        names.add(firstExtraction.getPatientName());
        names.add(secondExtraction.getPatientName());

        assertTrue(names.contains("Original Name"));
        assertTrue(names.contains("Updated Name"));
        assertNull(heap.extractMin());
    }

    @Test
    void boundaryValues_areExtractedInCorrectOrder() {
        // Arrange
        MinHeap heap = new MinHeap();

        // Act
        heap.insert(Integer.MAX_VALUE, "Max Patient", "Ward Max");
        heap.insert(Integer.MIN_VALUE, "Min Patient", "Ward Min");
        heap.insert(0, "Zero Patient", "Ward Zero");

        MinHeapNode firstExtraction = heap.extractMin();
        MinHeapNode secondExtraction = heap.extractMin();
        MinHeapNode thirdExtraction = heap.extractMin();

        // Assert
        assertEquals(Integer.MIN_VALUE, firstExtraction.getPatientId());
        assertEquals(0, secondExtraction.getPatientId());
        assertEquals(Integer.MAX_VALUE, thirdExtraction.getPatientId());
    }

    @Test
    void insertBoundaryPatientIds_supportsExtremeValues() {
        // Arrange
        MinHeap heap = new MinHeap();

        // Act
        heap.insert(Integer.MIN_VALUE, "Min Patient", "Ward Min");
        heap.insert(Integer.MAX_VALUE, "Max Patient", "Ward Max");

        MinHeapNode firstExtraction = heap.extractMin();
        MinHeapNode secondExtraction = heap.extractMin();

        // Assert
        assertEquals(Integer.MIN_VALUE, firstExtraction.getPatientId());
        assertEquals(Integer.MAX_VALUE, secondExtraction.getPatientId());
    }
}