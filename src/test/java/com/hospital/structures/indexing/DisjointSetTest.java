package com.hospital.structures.indexing;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DisjointSetTest {

    @Test
    void makeSetCreatesRepresentativeForSingleElement() {
        // Arrange
        DisjointSet disjointSet = new DisjointSet();

        // Act
        disjointSet.makeSet(10);

        // Assert
        assertEquals(10, disjointSet.find(10));
    }

    @Test
    void findMissingElement_returnsMinusOne() {
        // Arrange
        DisjointSet disjointSet = new DisjointSet();
        disjointSet.makeSet(10);

        // Act
        int representative = disjointSet.find(99);

        // Assert
        assertEquals(-1, representative);
    }

    @Test
    void unionExistingSets_mergesRepresentatives() {
        // Arrange
        DisjointSet disjointSet = new DisjointSet();
        disjointSet.makeSet(1);
        disjointSet.makeSet(2);

        // Act
        disjointSet.union(1, 2);

        // Assert
        assertEquals(disjointSet.find(1), disjointSet.find(2));
    }

    @Test
    void repeatedUnionOnSameSets_keepsRepresentativeStable() {
        // Arrange
        DisjointSet disjointSet = new DisjointSet();
        disjointSet.makeSet(3);
        disjointSet.makeSet(4);

        // Act
        disjointSet.union(3, 4);
        int firstRepresentative = disjointSet.find(3);
        disjointSet.union(3, 4);
        int secondRepresentative = disjointSet.find(4);

        // Assert
        assertEquals(firstRepresentative, secondRepresentative);
        assertEquals(firstRepresentative, disjointSet.find(3));
    }

    @Test
    void unionOfExistingSets_connectsSeparateComponents() {
        // Arrange
        DisjointSet disjointSet = new DisjointSet();
        disjointSet.makeSet(10);
        disjointSet.makeSet(20);
        disjointSet.makeSet(30);
        disjointSet.makeSet(40);

        // Act
        disjointSet.union(10, 20);
        disjointSet.union(30, 40);
        disjointSet.union(20, 30);

        // Assert
        int representative = disjointSet.find(10);
        assertEquals(representative, disjointSet.find(20));
        assertEquals(representative, disjointSet.find(30));
        assertEquals(representative, disjointSet.find(40));
    }

    @Test
    void representativeLookup_returnsSameValueForConnectedElements() {
        // Arrange
        DisjointSet disjointSet = new DisjointSet();
        disjointSet.makeSet(50);
        disjointSet.makeSet(60);
        disjointSet.makeSet(70);

        // Act
        disjointSet.union(50, 60);
        disjointSet.union(60, 70);
        int firstRepresentative = disjointSet.find(50);
        int secondRepresentative = disjointSet.find(60);
        int thirdRepresentative = disjointSet.find(70);

        // Assert
        assertEquals(firstRepresentative, secondRepresentative);
        assertEquals(secondRepresentative, thirdRepresentative);
    }

    @Test
    void disconnectedSets_keepDifferentRepresentatives() {
        // Arrange
        DisjointSet disjointSet = new DisjointSet();
        disjointSet.makeSet(101);
        disjointSet.makeSet(202);
        disjointSet.makeSet(303);
        disjointSet.makeSet(404);

        // Act
        disjointSet.union(101, 202);
        disjointSet.union(303, 404);

        // Assert
        int firstComponentRepresentative = disjointSet.find(101);
        int secondComponentRepresentative = disjointSet.find(303);

        assertEquals(firstComponentRepresentative, disjointSet.find(202));
        assertEquals(secondComponentRepresentative, disjointSet.find(404));
    }

    @Test
    void pathCompressionThroughRepeatedFindCalls_keepsRepresentativeStable() {
        // Arrange
        DisjointSet disjointSet = new DisjointSet();
        disjointSet.makeSet(1);
        disjointSet.makeSet(2);
        disjointSet.makeSet(3);
        disjointSet.makeSet(4);
        disjointSet.makeSet(5);

        // Act
        disjointSet.union(1, 2);
        disjointSet.union(2, 3);
        disjointSet.union(3, 4);
        disjointSet.union(4, 5);

        int firstRepresentative = disjointSet.find(5);
        int secondRepresentative = disjointSet.find(5);
        int thirdRepresentative = disjointSet.find(1);

        // Assert
        assertEquals(firstRepresentative, secondRepresentative);
        assertEquals(firstRepresentative, thirdRepresentative);
    }
}