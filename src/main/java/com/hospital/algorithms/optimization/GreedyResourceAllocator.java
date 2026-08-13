package com.hospital.algorithms.optimization;

import com.hospital.algorithms.PatientAdmission;
import com.hospital.algorithms.sort.AdmissionSort;

public final class GreedyResourceAllocator {

    private GreedyResourceAllocator() {
    }

    public static AllocationResult allocate(PatientAdmission[] admissions, int availableUnits) {
        if (admissions == null || availableUnits < 0) {
            throw new IllegalArgumentException("admissions must not be null and availableUnits must not be negative");
        }
        PatientAdmission[] ranked = copyAndValidate(admissions);
        AdmissionSort.insertionSort(ranked, AdmissionSort.Criterion.URGENCY);

        PatientAdmission[] allocated = new PatientAdmission[ranked.length];
        PatientAdmission[] unallocated = new PatientAdmission[ranked.length];
        int allocatedCount = 0;
        int unallocatedCount = 0;
        int remainingUnits = availableUnits;
        for (PatientAdmission admission : ranked) {
            if (admission.resourceUnits() <= remainingUnits) {
                allocated[allocatedCount++] = admission;
                remainingUnits -= admission.resourceUnits();
            } else {
                unallocated[unallocatedCount++] = admission;
            }
        }
        return new AllocationResult(trim(allocated, allocatedCount), trim(unallocated, unallocatedCount), remainingUnits);
    }

    private static PatientAdmission[] copyAndValidate(PatientAdmission[] admissions) {
        PatientAdmission[] copy = new PatientAdmission[admissions.length];
        for (int index = 0; index < admissions.length; index++) {
            if (admissions[index] == null) {
                throw new IllegalArgumentException("admissions must not contain null entries");
            }
            copy[index] = admissions[index];
        }
        return copy;
    }

    private static PatientAdmission[] trim(PatientAdmission[] values, int count) {
        PatientAdmission[] result = new PatientAdmission[count];
        for (int index = 0; index < count; index++) {
            result[index] = values[index];
        }
        return result;
    }

    public record AllocationResult(PatientAdmission[] allocated, PatientAdmission[] unallocated, int remainingUnits) {
        public AllocationResult {
            allocated = allocated.clone();
            unallocated = unallocated.clone();
        }

        @Override
        public PatientAdmission[] allocated() {
            return allocated.clone();
        }

        @Override
        public PatientAdmission[] unallocated() {
            return unallocated.clone();
        }
    }
}
