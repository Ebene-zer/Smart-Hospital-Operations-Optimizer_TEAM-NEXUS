package com.hospital.algorithms.optimization;

import com.hospital.algorithms.PatientAdmission;

/**
 * Exhaustive 2^n subset search for bed allocation. Intended only for small n
 * (the menu refuses n &gt; 12) to show why brute force does not scale.
 */
public final class BruteForceAllocator {

    private BruteForceAllocator() {
    }

    public static Result allocate(PatientAdmission[] admissions, int availableUnits) {
        if (admissions == null || availableUnits < 0) {
            throw new IllegalArgumentException("admissions must not be null and availableUnits must not be negative");
        }
        if (admissions.length > 12) {
            throw new IllegalArgumentException("brute force is limited to 12 admissions; use greedy or DP for larger n");
        }
        int n = admissions.length;
        int bestMask = 0;
        int bestPatients = -1;
        int bestUrgency = -1;
        int subsets = 1 << n;
        for (int mask = 0; mask < subsets; mask++) {
            int units = 0;
            int patients = 0;
            int urgency = 0;
            boolean feasible = true;
            for (int i = 0; i < n; i++) {
                if ((mask & (1 << i)) == 0) {
                    continue;
                }
                PatientAdmission admission = admissions[i];
                if (admission == null) {
                    throw new IllegalArgumentException("admissions must not contain null entries");
                }
                units += admission.resourceUnits();
                if (units > availableUnits) {
                    feasible = false;
                    break;
                }
                patients++;
                urgency += admission.urgency();
            }
            if (!feasible) {
                continue;
            }
            if (patients > bestPatients || (patients == bestPatients && urgency > bestUrgency)) {
                bestPatients = patients;
                bestUrgency = urgency;
                bestMask = mask;
            }
        }
        PatientAdmission[] chosen = new PatientAdmission[Math.max(0, bestPatients)];
        int k = 0;
        for (int i = 0; i < n; i++) {
            if ((bestMask & (1 << i)) != 0) {
                chosen[k++] = admissions[i];
            }
        }
        return new Result(chosen, subsets, bestPatients, bestUrgency);
    }

    public record Result(PatientAdmission[] selected, int subsetsExamined, int patientsTreated, int totalUrgency) {
        public Result {
            selected = selected.clone();
        }

        @Override
        public PatientAdmission[] selected() {
            return selected.clone();
        }
    }
}
