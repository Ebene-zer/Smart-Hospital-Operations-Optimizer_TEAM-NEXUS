package com.hospital.structures.indexing;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BTreeTest {

    @Test
    void insertSinglePatient_makesPatientSearchable() {
        // Arrange
        BTree tree = new BTree();

        // Act
        tree.insert(101, "Ama Owusu", "Emergency");
        BTreeNode searchResult = tree.search(101);

        // Assert
        assertNotNull(searchResult);
        assertTrue(containsPatientId(searchResult, 101));
        assertEquals(1, searchResult.getKeyCount());
    }

    @Test
    void searchExistingPatient_returnsNodeContainingPatient() {
        // Arrange
        BTree tree = new BTree();
        tree.insert(20, "Patient 20", "Ward A");
        tree.insert(10, "Patient 10", "Ward B");
        tree.insert(30, "Patient 30", "Ward C");

        // Act
        BTreeNode searchResult = tree.search(10);

        // Assert
        assertNotNull(searchResult);
        assertTrue(containsPatientId(searchResult, 10));
    }

    @Test
    void searchMissingPatient_returnsNull() {
        // Arrange
        BTree tree = new BTree();
        tree.insert(20, "Patient 20", "Ward A");
        tree.insert(10, "Patient 10", "Ward B");

        // Act
        BTreeNode searchResult = tree.search(99);

        // Assert
        assertNull(searchResult);
    }

    @Test
    void multipleInsertions_supportSearchAcrossSplitChildren() {
        // Arrange
        BTree tree = new BTree();

        // Act
        tree.insert(50, "Patient 50", "Ward A");
        tree.insert(25, "Patient 25", "Ward B");
        tree.insert(75, "Patient 75", "Ward C");
        tree.insert(12, "Patient 12", "Ward D");
        tree.insert(37, "Patient 37", "Ward E");
        tree.insert(62, "Patient 62", "Ward F");
        tree.insert(87, "Patient 87", "Ward G");

        // Assert
        assertNotNull(tree.search(12));
        assertNotNull(tree.search(37));
        assertNotNull(tree.search(62));
        assertNotNull(tree.search(87));
        assertNull(tree.search(13));
    }

    @Test
    void duplicateInsertion_updatesExistingKeyWithoutCreatingMissingSearchResult() {
        // Arrange
        BTree tree = new BTree();
        tree.insert(42, "Original Name", "Ward A");

        // Act
        tree.insert(42, "Updated Name", "Ward B");
        BTreeNode searchResult = tree.search(42);

        // Assert
        assertNotNull(searchResult);
        int index = indexOfPatientId(searchResult, 42);
        assertTrue(index >= 0);
        assertEquals("Updated Name", searchResult.getPatientNameAt(index));
        assertEquals("Ward B", searchResult.getWardNameAt(index));
    }

    @Test
    void nodeSplittingBehavior_isVisibleThroughPublicSearchResults() {
        // Arrange
        BTree tree = new BTree();

        // Act
        tree.insert(1, "Patient 1", "Ward A");
        tree.insert(2, "Patient 2", "Ward B");
        tree.insert(3, "Patient 3", "Ward C");
        tree.insert(4, "Patient 4", "Ward D");

        BTreeNode rootSearchResult = tree.search(2);
        BTreeNode leftSearchResult = tree.search(1);
        BTreeNode rightSearchResult = tree.search(4);

        // Assert
        assertNotNull(rootSearchResult);
        assertFalse(rootSearchResult.isLeaf());
        assertEquals(1, rootSearchResult.getKeyCount());
        assertTrue(containsPatientId(rootSearchResult, 2));
        assertNotNull(leftSearchResult);
        assertNotNull(rightSearchResult);
        assertTrue(containsPatientId(leftSearchResult, 1));
        assertTrue(containsPatientId(rightSearchResult, 4));
    }

    @Test
    void boundaryKeys_areSearchableAfterInsertion() {
        // Arrange
        BTree tree = new BTree();

        // Act
        tree.insert(Integer.MIN_VALUE, "Min Patient", "Ward Min");
        tree.insert(Integer.MAX_VALUE, "Max Patient", "Ward Max");

        // Assert
        assertNotNull(tree.search(Integer.MIN_VALUE));
        assertNotNull(tree.search(Integer.MAX_VALUE));
    }

    @Test
    void insertExistingKey_updatesStoredValues() {
        // Arrange
        BTree tree = new BTree();
        tree.insert(77, "Original Patient", "Ward A");

        // Act
        tree.insert(77, "Updated Patient", "Ward B");
        BTreeNode searchResult = tree.search(77);

        // Assert
        assertNotNull(searchResult);
        int index = indexOfPatientId(searchResult, 77);
        assertTrue(index >= 0);
        assertEquals("Updated Patient", searchResult.getPatientNameAt(index));
        assertEquals("Ward B", searchResult.getWardNameAt(index));
    }

    private boolean containsPatientId(BTreeNode node, int patientId) {
        return indexOfPatientId(node, patientId) >= 0;
    }

    private int indexOfPatientId(BTreeNode node, int patientId) {
        for (int index = 0; index < node.getKeyCount(); index++) {
            if (node.getPatientIdAt(index) == patientId) {
                return index;
            }
        }

        return -1;
    }
}