package com.hospital.structures.indexing;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CustomMapTest {

    @Test
    void newMap_isEmpty() {
        CustomMap<String, Integer> map = new CustomMap<>();

        assertTrue(map.isEmpty());
        assertEquals(0, map.size());
        assertNull(map.get("missing"));
        assertNull(map.remove("missing"));
    }

    @Test
    void putAndGetMultipleEntries_supportsCollisions() {
        CustomMap<String, Integer> map = new CustomMap<>(5);

        map.put("Aa", 10);
        map.put("BB", 20);
        map.put("ward", 30);

        assertEquals(10, map.get("Aa"));
        assertEquals(20, map.get("BB"));
        assertEquals(30, map.get("ward"));
        assertEquals(3, map.size());
    }

    @Test
    void putExistingKey_replacesValueWithoutIncreasingSize() {
        CustomMap<String, String> map = new CustomMap<>();
        map.put("KB-101", "Emergency");

        String previous = map.put("KB-101", "ICU");

        assertEquals("Emergency", previous);
        assertEquals("ICU", map.get("KB-101"));
        assertEquals(1, map.size());
    }

    @Test
    void removeExistingAndMissingKeys_updatesMembershipAndSize() {
        CustomMap<Integer, String> map = new CustomMap<>();
        map.put(101, "Ama");
        map.put(102, "Kofi");

        assertEquals("Ama", map.remove(101));
        assertFalse(map.containsKey(101));
        assertEquals(1, map.size());
        assertNull(map.remove(999));
        assertEquals(1, map.size());
    }

    @Test
    void removeFromCollisionChain_preservesOtherEntries() {
        CustomMap<String, Integer> map = new CustomMap<>(5);
        map.put("Aa", 10);
        map.put("BB", 20);

        assertEquals(10, map.remove("Aa"));
        assertEquals(20, map.get("BB"));
        assertEquals(1, map.size());
    }

    @Test
    void nullValue_isStoredAndDistinguishedByContainsKey() {
        CustomMap<String, String> map = new CustomMap<>();

        map.put("unassigned", null);

        assertTrue(map.containsKey("unassigned"));
        assertNull(map.get("unassigned"));
    }

    @Test
    void clear_removesAllEntries() {
        CustomMap<Integer, String> map = new CustomMap<>();
        map.put(101, "Ama");
        map.put(102, "Kofi");

        map.clear();

        assertTrue(map.isEmpty());
        assertEquals(0, map.size());
        assertNull(map.get(101));
    }

    @Test
    void nullKeys_areRejectedAndCapacityMustBePositive() {
        CustomMap<String, Integer> map = new CustomMap<>();

        assertThrows(IllegalArgumentException.class, () -> map.put(null, 1));
        assertThrows(IllegalArgumentException.class, () -> map.get(null));
        assertThrows(IllegalArgumentException.class, () -> map.containsKey(null));
        assertThrows(IllegalArgumentException.class, () -> map.remove(null));
        assertThrows(IllegalArgumentException.class, () -> new CustomMap<String, Integer>(0));
    }
}
