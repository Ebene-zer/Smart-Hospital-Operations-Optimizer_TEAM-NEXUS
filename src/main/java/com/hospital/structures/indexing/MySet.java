package com.hospital.structures.indexing;

/**
 * Custom set, built on top of HashTable (not java.util.HashSet).
 * Use case: allergy set per patient — e.g. {"penicillin", "sulfa"}.
 */
public class MySet<T> {

    // Store elements as keys with a dummy value — simplest way to reuse
    // your own HashTable without duplicating hashing/collision logic.
    private final HashTable<T, Boolean> backing;

    public MySet(int tableSize) {
        this.backing = new HashTable<>(tableSize);
    }

    public void add(T item) {
        backing.put(item, Boolean.TRUE);
    }

    public boolean contains(T item) {
        return backing.containsKey(item);
    }

    public void remove(T item) {
        backing.remove(item);
    }

    public int size() {
        return backing.size();
    }
}
