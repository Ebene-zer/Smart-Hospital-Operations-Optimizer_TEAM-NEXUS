package com.hospital.structures.indexing;

public class CustomMap<K, V> {

    private static final int DEFAULT_CAPACITY = 11;

    private final Entry<K, V>[] buckets;
    private int size;

    public CustomMap() {
        this(DEFAULT_CAPACITY);
    }

    @SuppressWarnings("unchecked")
    public CustomMap(int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("The capacity must be positive");
        }

        this.buckets = (Entry<K, V>[]) new Entry[capacity];
    }

    public V put(K key, V value) {
        int index = hash(key);
        Entry<K, V> current = buckets[index];

        while (current != null) {
            if (current.key.equals(key)) {
                V previousValue = current.value;
                current.value = value;
                return previousValue;
            }
            current = current.next;
        }

        buckets[index] = new Entry<>(key, value, buckets[index]);
        size++;
        return null;
    }

    public V get(K key) {
        Entry<K, V> entry = findEntry(key);
        return entry == null ? null : entry.value;
    }

    public boolean containsKey(K key) {
        return findEntry(key) != null;
    }

    public V remove(K key) {
        int index = hash(key);
        Entry<K, V> current = buckets[index];
        Entry<K, V> previous = null;

        while (current != null) {
            if (current.key.equals(key)) {
                if (previous == null) {
                    buckets[index] = current.next;
                } else {
                    previous.next = current.next;
                }
                size--;
                return current.value;
            }
            previous = current;
            current = current.next;
        }

        return null;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public void clear() {
        for (int index = 0; index < buckets.length; index++) {
            buckets[index] = null;
        }
        size = 0;
    }

    private Entry<K, V> findEntry(K key) {
        Entry<K, V> current = buckets[hash(key)];

        while (current != null) {
            if (current.key.equals(key)) {
                return current;
            }
            current = current.next;
        }

        return null;
    }

    private int hash(K key) {
        if (key == null) {
            throw new IllegalArgumentException("Null keys are not supported");
        }
        return (key.hashCode() & 0x7fffffff) % buckets.length;
    }

    private static class Entry<K, V> {
        private final K key;
        private V value;
        private Entry<K, V> next;

        Entry(K key, V value, Entry<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
