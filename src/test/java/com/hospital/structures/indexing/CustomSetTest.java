package com.hospital.structures.indexing;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CustomSetTest {

    @Test
    void newSet_isEmpty() {
        CustomSet<String> set = new CustomSet<>();

        assertTrue(set.isEmpty());
        assertEquals(0, set.size());
        assertFalse(set.contains("KB-101"));
        assertFalse(set.remove("KB-101"));
    }

    @Test
    void addMultipleElements_makesEachElementAvailable() {
        CustomSet<String> set = new CustomSet<>(5);

        assertTrue(set.add("Aa"));
        assertTrue(set.add("BB"));
        assertTrue(set.add("ICU"));

        assertTrue(set.contains("Aa"));
        assertTrue(set.contains("BB"));
        assertTrue(set.contains("ICU"));
        assertEquals(3, set.size());
    }

    @Test
    void duplicateAdd_doesNotChangeSet() {
        CustomSet<Integer> set = new CustomSet<>();

        assertTrue(set.add(101));
        assertFalse(set.add(101));

        assertEquals(1, set.size());
        assertTrue(set.contains(101));
    }

    @Test
    void removeExistingAndMissingElements_updatesSet() {
        CustomSet<Integer> set = new CustomSet<>();
        set.add(101);
        set.add(102);

        assertTrue(set.remove(101));
        assertFalse(set.contains(101));
        assertEquals(1, set.size());
        assertFalse(set.remove(999));
        assertEquals(1, set.size());
    }

    @Test
    void removeThenAdd_restoresElement() {
        CustomSet<Integer> set = new CustomSet<>();
        set.add(101);

        assertTrue(set.remove(101));
        assertTrue(set.add(101));
        assertTrue(set.contains(101));
        assertEquals(1, set.size());
    }

    @Test
    void clear_removesAllElements() {
        CustomSet<Integer> set = new CustomSet<>();
        set.add(101);
        set.add(102);

        set.clear();

        assertTrue(set.isEmpty());
        assertEquals(0, set.size());
        assertFalse(set.contains(101));
    }

    @Test
    void nullElementsAndInvalidCapacity_areRejected() {
        CustomSet<String> set = new CustomSet<>();

        assertThrows(IllegalArgumentException.class, () -> set.add(null));
        assertThrows(IllegalArgumentException.class, () -> set.contains(null));
        assertThrows(IllegalArgumentException.class, () -> set.remove(null));
        assertThrows(IllegalArgumentException.class, () -> new CustomSet<String>(-1));
    }
}
