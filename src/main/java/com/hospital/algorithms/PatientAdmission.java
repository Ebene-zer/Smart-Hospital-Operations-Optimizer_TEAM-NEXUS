package com.hospital.algorithms;

public record PatientAdmission(int patientId, String patientName, int admissionTime,
                               int urgency, int age, int resourceUnits) {

    public PatientAdmission {
        if (patientName == null || patientName.isBlank()) {
            throw new IllegalArgumentException("patientName must not be blank");
        }
        if (urgency < 0 || age < 0 || resourceUnits <= 0) {
            throw new IllegalArgumentException("urgency and age must be non-negative and resourceUnits must be positive");
        }
    }
}
