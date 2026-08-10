package com.hospital.structures.indexing;

class MinHeapNode {

    private final int patientId;
    private final String patientName;
    private final String wardName;

    public MinHeapNode(int patientId, String patientName, String wardName) {
        this.patientId = patientId;
        this.patientName = patientName;
        this.wardName = wardName;
    }

    public int getPatientId() {
        return patientId;
    }

    public String getPatientName() {
        return patientName;
    }

    public String getWardName() {
        return wardName;
    }
}