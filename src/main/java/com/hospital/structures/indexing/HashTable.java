package com.hospital.structures.indexing;

/**
 * Custom hash table — O(1) average lookup by NHIS number.
 * Do NOT use java.util.HashMap anywhere in this class; that defeats the assignment.
 *
 * Table size must derive from a team member's index number
 * (e.g. last 3 digits of the index number, mod a prime). Set that in the
 * constructor default and document which member's index number you used
 * and why, in your report.
 */
public class HashTable<K, V> {

    // Separate chaining: each bucket is a linked list of entries.
    // Swap to open addressing if your team prefers — just be consistent
    // and document collision handling in the report.
    private static class Entry<K, V> {
        K key;
        V value;
        Entry<K, V> next;

        Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    private Entry<K, V>[] buckets;
    private int size;
    private int collisionCount; // increment on every collision — needed for load-factor report

    /**
     * @param tableSize derive this from a member's index number, e.g.
     *                  (indexNumber % 97) — 97 is prime, adjust as needed.
     */
    @SuppressWarnings("unchecked")
    public HashTable(int tableSize) {
        this.buckets = new Entry[tableSize];
        this.size = 0;
        this.collisionCount = 0;
    }

    private int hash(K key) {
        // TODO: implement your own hash function, don't just call key.hashCode()
        // and mod — at minimum explain/justify whatever you use in the report.
        throw new UnsupportedOperationException("TODO");
    }

    public void put(K key, V value) {
        // TODO: insert or update. If the target bucket is non-empty before
        // insertion, increment collisionCount. Resize when load factor
        // crosses your chosen threshold (test at 0.5, 0.7, 0.9).
        throw new UnsupportedOperationException("TODO");
    }

    public V get(K key) {
        // TODO: return null (or throw, pick one and be consistent) if absent
        throw new UnsupportedOperationException("TODO");
    }

    public V remove(K key) {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

    public boolean containsKey(K key) {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

    public int size() {
        return size;
    }

    public int getCollisionCount() {
        return collisionCount;
    }

    public double loadFactor() {
        return (double) size / buckets.length;
    }

    private void resize() {
        // TODO: grow the bucket array and rehash all existing entries
        throw new UnsupportedOperationException("TODO");
    }
}
