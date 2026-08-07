package com.hospital.structures.indexing;

public class HashTable {

    private static final int DEFAULT_CAPACITY = 11;

    private final HashTableEntry[] buckets;

    public HashTable() {
        this(DEFAULT_CAPACITY);
    }

    public HashTable(int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("The capacity must be positive");
        }
        this.buckets = new HashTableEntry[capacity];
    }

    public void put(int patientId, String patientName, String wardName) {
        int index = hash(patientId);
        HashTableEntry current = buckets[index];

        while (current != null) {
            if (current.getPatientId() == patientId) {
                current.setPatientName(patientName);
                current.setWardName(wardName);
                return;
            }
            current = current.getNext();
        }

        HashTableEntry newEntry = new HashTableEntry(patientId, patientName, wardName);
        newEntry.setNext(buckets[index]);
        buckets[index] = newEntry;
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
}