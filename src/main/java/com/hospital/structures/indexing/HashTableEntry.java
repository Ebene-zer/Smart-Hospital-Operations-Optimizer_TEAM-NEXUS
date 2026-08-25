package com.hospital.structures.indexing;

public class HashTableEntry {

    private final int patientId;
    private String patientName;
    private String wardName;
    private HashTableEntry next;

    public HashTableEntry(int patientId, String patientName, String wardName) {
        this.patientId = patientId;
        this.patientName = patientName;
        this.wardName = wardName;
        this.next = null;
    }

    public int getPatientId() {
        return patientId;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getWardName() {
        return wardName;
    }

    public void setWardName(String wardName) {
        this.wardName = wardName;
    }

    public HashTableEntry getNext() {
        return next;
    }

    public void setNext(HashTableEntry next) {
        this.next = next;
    }
}