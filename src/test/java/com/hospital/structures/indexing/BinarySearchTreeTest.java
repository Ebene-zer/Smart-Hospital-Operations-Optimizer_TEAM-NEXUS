package com.hospital.structures.indexing;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BinarySearchTreeTest {

    @Test
    void insertSinglePatient_makesPatientSearchable() {
        // Arrange
        BinarySearchTree tree = new BinarySearchTree();

        // Act
        tree.insert(101, "Ama Owusu", "Emergency");

        // Assert
        assertTrue(tree.contains(101));
        assertNotNull(tree.searchWithTrace(101));
    }

    @Test
    void searchExistingPatient_returnsTrueAndNode() {
        // Arrange
        BinarySearchTree tree = new BinarySearchTree();
        tree.insert(20, "Patient 20", "Ward A");
        tree.insert(10, "Patient 10", "Ward B");
        tree.insert(30, "Patient 30", "Ward C");

        // Act
        boolean found = tree.contains(10);
        Node searchResult = tree.searchWithTrace(10);

        // Assert
        assertTrue(found);
        assertNotNull(searchResult);
        assertEquals(10, searchResult.getPatientId());
    }

    @Test
    void searchMissingPatient_returnsFalseAndNull() {
        // Arrange
        BinarySearchTree tree = new BinarySearchTree();
        tree.insert(20, "Patient 20", "Ward A");
        tree.insert(10, "Patient 10", "Ward B");

        // Act
        boolean found = tree.contains(99);
        Node searchResult = tree.searchWithTrace(99);

        // Assert
        assertFalse(found);
        assertNull(searchResult);
    }

    @Test
    void insertMultiplePatients_supportsLookupsAcrossLeftAndRightSubtrees() {
        // Arrange
        BinarySearchTree tree = new BinarySearchTree();

        // Act
        tree.insert(50, "Patient 50", "Ward A");
        tree.insert(25, "Patient 25", "Ward B");
        tree.insert(75, "Patient 75", "Ward C");
        tree.insert(12, "Patient 12", "Ward D");
        tree.insert(37, "Patient 37", "Ward E");
        tree.insert(62, "Patient 62", "Ward F");
        tree.insert(87, "Patient 87", "Ward G");

        // Assert
        assertTrue(tree.contains(12));
        assertTrue(tree.contains(37));
        assertTrue(tree.contains(62));
        assertTrue(tree.contains(87));
        assertFalse(tree.contains(13));
    }

    @Test
    void duplicateInsertion_doesNotCreateDuplicateTraversalEntries() {
        // Arrange
        BinarySearchTree tree = new BinarySearchTree();
        tree.insert(42, "Original Name", "Ward A");

        // Act
        tree.insert(42, "Updated Name", "Ward B");
        String traversal = tree.inOrderTraversal();

        // Assert
        assertEquals(1, countOccurrences(traversal, "KB-42"));
    }

    @Test
    void searchEmptyTree_returnsFalseAndNullAndEmptyTraversal() {
        // Arrange
        BinarySearchTree tree = new BinarySearchTree();

        // Act
        boolean found = tree.contains(1);
        Node searchResult = tree.searchWithTrace(1);
        String traversal = tree.inOrderTraversal();

        // Assert
        assertFalse(found);
        assertNull(searchResult);
        assertEquals("", traversal);
    }

    @Test
    void inorderTraversalProducesSortedOrder_afterUnsortedInsertions() {
        // Arrange
        BinarySearchTree tree = new BinarySearchTree();
        tree.insert(40, "Patient 40", "Ward A");
        tree.insert(10, "Patient 10", "Ward B");
        tree.insert(30, "Patient 30", "Ward C");
        tree.insert(20, "Patient 20", "Ward D");
        tree.insert(50, "Patient 50", "Ward E");

        // Act
        String traversal = tree.inOrderTraversal();

        // Assert
        int index10 = traversal.indexOf("KB-10");
        int index20 = traversal.indexOf("KB-20");
        int index30 = traversal.indexOf("KB-30");
        int index40 = traversal.indexOf("KB-40");
        int index50 = traversal.indexOf("KB-50");

        assertTrue(index10 >= 0);
        assertTrue(index20 > index10);
        assertTrue(index30 > index20);
        assertTrue(index40 > index30);
        assertTrue(index50 > index40);
    }

    @Test
    void insertBoundaryPatientIds_supportsMinimumAndMaximumIntegerValues() {
        // Arrange
        BinarySearchTree tree = new BinarySearchTree();

        // Act
        tree.insert(Integer.MIN_VALUE, "Min Patient", "Ward Min");
        tree.insert(Integer.MAX_VALUE, "Max Patient", "Ward Max");

        // Assert
        assertTrue(tree.contains(Integer.MIN_VALUE));
        assertTrue(tree.contains(Integer.MAX_VALUE));
    }

    @Test
    void insertWithNullTextData_preservesSearchabilityOfPatientId() {
        // Arrange
        BinarySearchTree tree = new BinarySearchTree();

        // Act
        tree.insert(77, null, null);

        // Assert
        assertTrue(tree.contains(77));
        assertNotNull(tree.searchWithTrace(77));
    }

    private int countOccurrences(String text, String token) {
        int count = 0;
        int fromIndex = 0;

        while (true) {
            int foundIndex = text.indexOf(token, fromIndex);
            if (foundIndex < 0) {
                return count;
            }
            count++;
            fromIndex = foundIndex + token.length();
        }
    }
}