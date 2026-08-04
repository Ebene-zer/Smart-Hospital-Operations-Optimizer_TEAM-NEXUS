package com.hospital.structures.indexing;

/**
 * Custom map, built on top of HashTable (not java.util.HashMap).
 * Use case: doctor-to-patient assignment map for ward rounds.
 */
public class MyMap<K, V> {

    private final HashTable<K, V> backing;

    public MyMap(int tableSize) {
        this.backing = new HashTable<>(tableSize);
    }

    public void put(K key, V value) {
        backing.put(key, value);
    }

    public V get(K key) {
        return backing.get(key);
    }

    public boolean containsKey(K key) {
        return backing.containsKey(key);
    }

    public void remove(K key) {
        backing.remove(key);
    }

    public int size() {
        return backing.size();
    }
}
