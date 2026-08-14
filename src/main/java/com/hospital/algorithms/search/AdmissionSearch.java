package com.hospital.algorithms.search;

import com.hospital.algorithms.PatientAdmission;

public final class AdmissionSearch {

    private AdmissionSearch() {
    }

    public static int linearSearchByPatientId(PatientAdmission[] admissions, int patientId) {
        requireAdmissions(admissions);
        for (int index = 0; index < admissions.length; index++) {
            if (admissions[index].patientId() == patientId) {
                return index;
            }
        }
        return -1;
    }

    public static int binarySearchByPatientId(PatientAdmission[] admissions, int patientId) {
        requireAdmissions(admissions);
        requireSortedByPatientId(admissions);

        int low = 0;
        int high = admissions.length - 1;
        while (low <= high) {
            int middle = low + (high - low) / 2;
            int middleId = admissions[middle].patientId();
            if (middleId == patientId) {
                return middle;
            }
            if (middleId < patientId) {
                low = middle + 1;
            } else {
                high = middle - 1;
            }
        }
        return -1;
    }

    private static void requireAdmissions(PatientAdmission[] admissions) {
        if (admissions == null) {
            throw new IllegalArgumentException("admissions must not be null");
        }
        for (PatientAdmission admission : admissions) {
            if (admission == null) {
                throw new IllegalArgumentException("admissions must not contain null entries");
            }
        }
    }

    private static void requireSortedByPatientId(PatientAdmission[] admissions) {
        for (int index = 1; index < admissions.length; index++) {
            if (admissions[index - 1].patientId() > admissions[index].patientId()) {
                throw new IllegalArgumentException("binary search requires admissions sorted by patientId");
            }
        }
    }
}
