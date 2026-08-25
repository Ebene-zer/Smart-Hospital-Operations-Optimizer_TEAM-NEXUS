package com.hospital.structures.indexing;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

class HashTableTest {

    @Test
    void putSinglePatient_makesPatientRetrievable() {
        // Arrange
        HashTable table = new HashTable();

        // Act
        table.put(101, "Ama Owusu", "Emergency");

        // Assert
        assertNotNull(table.get(101));
    }

    @Test
    void getExistingPatient_returnsEntry() {
        // Arrange
        HashTable table = new HashTable();
        table.put(20, "Patient 20", "Ward A");
        table.put(10, "Patient 10", "Ward B");
        table.put(30, "Patient 30", "Ward C");

        // Act
        Object retrievedEntry = table.get(10);

        // Assert
        assertNotNull(retrievedEntry);
    }

    @Test
    void getMissingPatient_returnsNull() {
        // Arrange
        HashTable table = new HashTable();
        table.put(20, "Patient 20", "Ward A");
        table.put(10, "Patient 10", "Ward B");

        // Act
        Object retrievedEntry = table.get(99);

        // Assert
        assertNull(retrievedEntry);
    }

    @Test
    void putMultiplePatients_supportsRetrievalAcrossCollisions() {
        // Arrange
        HashTable table = new HashTable(5);

        // Act
        table.put(1, "Patient 1", "Ward A");
        table.put(6, "Patient 6", "Ward B");
        table.put(11, "Patient 11", "Ward C");

        // Assert
        assertNotNull(table.get(1));
        assertNotNull(table.get(6));
        assertNotNull(table.get(11));
        assertNull(table.get(16));
    }

    @Test
    void duplicateKeyUpdatesExistingEntryWithoutLeavingDuplicateBehind() {
        // Arrange
        HashTable table = new HashTable();
        table.put(42, "Original Name", "Ward A");

        // Act
        Object originalEntry = table.get(42);
        table.put(42, "Updated Name", "Ward B");
        Object updatedEntry = table.get(42);
        Object removedEntry = table.remove(42);

        // Assert
        assertNotNull(originalEntry);
        assertSame(originalEntry, updatedEntry);
        assertNotNull(removedEntry);
        assertNull(table.get(42));
    }

    @Test
    void removeExistingPatient_returnsEntryAndClearsLookup() {
        // Arrange
        HashTable table = new HashTable();
        table.put(55, "Patient 55", "Ward X");
        table.put(77, "Patient 77", "Ward Y");

        // Act
        Object removedEntry = table.remove(55);

        // Assert
        assertNotNull(removedEntry);
        assertNull(table.get(55));
        assertNotNull(table.get(77));
    }

    @Test
    void removeMissingPatient_returnsNull() {
        // Arrange
        HashTable table = new HashTable();
        table.put(88, "Patient 88", "Ward Z");

        // Act
        Object removedEntry = table.remove(99);

        // Assert
        assertNull(removedEntry);
        assertNotNull(table.get(88));
    }

    @Test
    void emptyTable_returnsNullForGetAndRemove() {
        // Arrange
        HashTable table = new HashTable();

        // Act
        Object retrievedEntry = table.get(1);
        Object removedEntry = table.remove(1);

        // Assert
        assertNull(retrievedEntry);
        assertNull(removedEntry);
    }

    @Test
    void duplicateKeyInCollisionChain_preservesOtherEntries() {
        // Arrange
        HashTable table = new HashTable(5);
        table.put(1, "Patient 1", "Ward A");
        table.put(6, "Patient 6", "Ward B");

        // Act
        table.put(6, "Updated Patient 6", "Ward C");
        Object removedEntry = table.remove(6);

        // Assert
        assertNotNull(removedEntry);
        assertNull(table.get(6));
        assertNotNull(table.get(1));
    }

    @Test
    void invalidCapacity_isRejected() {
        assertThrows(IllegalArgumentException.class, () -> new HashTable(0));
    }
}
