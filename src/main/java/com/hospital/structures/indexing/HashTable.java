package com.hospital.structures.indexing;

public class HashTable {

    private static final int DEFAULT_CAPACITY = 11;

    private final HashTableEntry[] buckets;

    private int size;
    private int collisionCount;

    public HashTable() {
        this(DEFAULT_CAPACITY);
    }

    public HashTable(int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("The capacity must be positive");
        }
        this.buckets = new HashTableEntry[capacity];
        this.size = 0;
        this.collisionCount = 0;
    }

    public void put(int patientId, String patientName, String wardName) {
        int index = hash(patientId);
        HashTableEntry current = buckets[index];
        boolean bucketOccupied = current != null;

        while (current != null) {
            if (current.getPatientId() == patientId) {
                current.setPatientName(patientName);
                current.setWardName(wardName);
                return;
            }
            current = current.getNext();
        }

        if (bucketOccupied) {
            collisionCount++;
        }
        HashTableEntry newEntry = new HashTableEntry(patientId, patientName, wardName);
        newEntry.setNext(buckets[index]);
        buckets[index] = newEntry;
        size++;
    }

    public HashTableEntry get(int patientId) {
        HashTableEntry current = buckets[hash(patientId)];

        while (current != null) {
            if (current.getPatientId() == patientId) {
                return current;
            }
            current = current.getNext();
        }

        return null;
    }

    public HashTableEntry remove(int patientId) {
        int index = hash(patientId);
        HashTableEntry current = buckets[index];
        HashTableEntry previous = null;

        while (current != null) {
            if (current.getPatientId() == patientId) {
                if (previous == null) {
                    buckets[index] = current.getNext();
                } else {
                    previous.setNext(current.getNext());
                }
                current.setNext(null);
                size--;
                return current;
            }
            previous = current;
            current = current.getNext();
        }

        return null;
    }

    private int hash(int patientId) {
        return (patientId & 0x7fffffff) % buckets.length;
    }

    public int size() {
        return size;
    }

    public int capacity() {
        return buckets.length;
    }

    public int collisionCount() {
        return collisionCount;
    }

    public double loadFactor() {
        return buckets.length == 0 ? 0.0 : (double) size / buckets.length;
    }

    public int longestChain() {
        int longest = 0;
        for (HashTableEntry bucket : buckets) {
            int length = 0;
            HashTableEntry current = bucket;
            while (current != null) {
                length++;
                current = current.getNext();
            }
            if (length > longest) {
                longest = length;
            }
        }
        return longest;
    }

    public int usedBuckets() {
        int used = 0;
        for (HashTableEntry bucket : buckets) {
            if (bucket != null) {
                used++;
            }
        }
        return used;
    }
}