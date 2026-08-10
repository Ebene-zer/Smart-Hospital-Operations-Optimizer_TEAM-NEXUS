package com.hospital.structures.indexing;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RedBlackTreeTest {

    @Test
    void insertSinglePatient_makesPatientSearchableAndRootBlack() {
        // Arrange
        RedBlackTree tree = new RedBlackTree();

        // Act
        tree.insert(101, "Ama Owusu", "Emergency");

        // Assert
        assertTrue(tree.contains(101));
        assertTrue(tree.rootIsBlack());
    }

    @Test
    void searchExistingPatient_returnsTrue() {
        // Arrange
        RedBlackTree tree = new RedBlackTree();
        tree.insert(20, "Patient 20", "Ward A");
        tree.insert(10, "Patient 10", "Ward B");
        tree.insert(30, "Patient 30", "Ward C");

        // Act
        boolean found = tree.contains(10);

        // Assert
        assertTrue(found);
        assertTrue(tree.rootIsBlack());
    }

    @Test
    void searchMissingPatient_returnsFalse() {
        // Arrange
        RedBlackTree tree = new RedBlackTree();
        tree.insert(20, "Patient 20", "Ward A");
        tree.insert(10, "Patient 10", "Ward B");

        // Act
        boolean found = tree.contains(99);

        // Assert
        assertFalse(found);
        assertTrue(tree.rootIsBlack());
    }

    @Test
    void insertMultiplePatients_supportsLookupsAcrossLeftAndRightSubtrees() {
        // Arrange
        RedBlackTree tree = new RedBlackTree();

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
        assertTrue(tree.rootIsBlack());
    }

    @Test
    void duplicateInsertion_doesNotCreateDuplicateTraversalEntries() {
        // Arrange
        RedBlackTree tree = new RedBlackTree();
        tree.insert(42, "Original Name", "Ward A");

        // Act
        tree.insert(42, "Updated Name", "Ward B");
        String traversal = tree.inOrderTraversal();

        // Assert
        assertEquals(1, countOccurrences(traversal, "KB-42"));
        assertTrue(tree.contains(42));
        assertTrue(tree.rootIsBlack());
    }

    @Test
    void searchEmptyTree_returnsFalseAndEmptyTraversal() {
        // Arrange
        RedBlackTree tree = new RedBlackTree();

        // Act
        boolean found = tree.contains(1);
        String traversal = tree.inOrderTraversal();

        // Assert
        assertFalse(found);
        assertEquals("", traversal);
        assertTrue(tree.rootIsBlack());
    }

    @Test
    void inorderTraversalProducesSortedOrder_afterUnsortedInsertions() {
        // Arrange
        RedBlackTree tree = new RedBlackTree();
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
        assertTrue(tree.rootIsBlack());
    }

    @Test
    void insertBoundaryPatientIds_supportsMinimumAndMaximumIntegerValues() {
        // Arrange
        RedBlackTree tree = new RedBlackTree();

        // Act
        tree.insert(Integer.MIN_VALUE, "Min Patient", "Ward Min");
        tree.insert(Integer.MAX_VALUE, "Max Patient", "Ward Max");

        // Assert
        assertTrue(tree.contains(Integer.MIN_VALUE));
        assertTrue(tree.contains(Integer.MAX_VALUE));
        assertTrue(tree.rootIsBlack());
    }

    @Test
    void insertWithNullTextData_preservesSearchabilityOfPatientId() {
        // Arrange
        RedBlackTree tree = new RedBlackTree();

        // Act
        tree.insert(77, null, null);

        // Assert
        assertTrue(tree.contains(77));
        assertTrue(tree.rootIsBlack());
    }

    @Test
    void rootRemainsBlack_afterManySequentialInsertions() {
        // Arrange
        RedBlackTree tree = new RedBlackTree();

        // Act
        for (int patientId = 1; patientId <= 100; patientId++) {
            tree.insert(patientId, "Patient " + patientId, "Ward " + patientId);
        }

        // Assert
        assertTrue(tree.rootIsBlack());
        assertTrue(tree.contains(1));
        assertTrue(tree.contains(50));
        assertTrue(tree.contains(100));
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
