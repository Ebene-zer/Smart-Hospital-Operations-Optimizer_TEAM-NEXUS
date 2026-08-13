package com.hospital.algorithms.sort;

import com.hospital.algorithms.PatientAdmission;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AdmissionSortTest {

    @Test
    void allAlgorithms_sortByRequestedHospitalField() {
        PatientAdmission[] admissions = admissions();
        PatientAdmission[] selection = admissions.clone();
        PatientAdmission[] insertion = admissions.clone();
        PatientAdmission[] merge = admissions.clone();
        PatientAdmission[] quick = admissions.clone();

        AdmissionSort.selectionSort(selection, AdmissionSort.Criterion.ADMISSION_TIME);
        AdmissionSort.insertionSort(insertion, AdmissionSort.Criterion.URGENCY);
        AdmissionSort.mergeSort(merge, AdmissionSort.Criterion.AGE);
        AdmissionSort.quickSort(quick, AdmissionSort.Criterion.ADMISSION_TIME);

        assertArrayEquals(new int[] {2, 1, 3}, ids(selection));
        assertArrayEquals(new int[] {2, 3, 1}, ids(insertion));
        assertArrayEquals(new int[] {2, 1, 3}, ids(merge));
        assertArrayEquals(new int[] {2, 1, 3}, ids(quick));
    }

    @Test
    void sorts_usePatientIdAsTheDeterministicTieBreakerForEqualKeys() {
        PatientAdmission first = new PatientAdmission(20, "First", 8, 5, 20, 1);
        PatientAdmission second = new PatientAdmission(10, "Second", 8, 5, 20, 1);
        PatientAdmission[] values = {first, second};

        AdmissionSort.mergeSort(values, AdmissionSort.Criterion.ADMISSION_TIME);

        assertArrayEquals(new int[] {10, 20}, ids(values));
    }

    @Test
    void sortAlgorithms_acceptEmptyAndSingleElementArraysAndRejectInvalidInput() {
        AdmissionSort.quickSort(new PatientAdmission[0], AdmissionSort.Criterion.AGE);
        PatientAdmission[] single = {admissions()[0]};
        AdmissionSort.selectionSort(single, AdmissionSort.Criterion.AGE);

        assertThrows(IllegalArgumentException.class, () -> AdmissionSort.mergeSort(null, AdmissionSort.Criterion.AGE));
        assertThrows(IllegalArgumentException.class, () -> AdmissionSort.insertionSort(new PatientAdmission[] {null}, AdmissionSort.Criterion.AGE));
    }

    @Test
    void allAlgorithms_sortAlreadySortedAndReverseSortedAdmissions() {
        PatientAdmission[] alreadySorted = {
                new PatientAdmission(1, "Ama", 7, 5, 20, 1),
                new PatientAdmission(2, "Kofi", 9, 4, 30, 1),
                new PatientAdmission(3, "Esi", 11, 2, 40, 1)
        };
        PatientAdmission[] reverseSorted = {
                alreadySorted[2], alreadySorted[1], alreadySorted[0]
        };

        AdmissionSort.selectionSort(alreadySorted, AdmissionSort.Criterion.ADMISSION_TIME);
        AdmissionSort.insertionSort(reverseSorted, AdmissionSort.Criterion.ADMISSION_TIME);
        assertArrayEquals(new int[] {1, 2, 3}, ids(alreadySorted));
        assertArrayEquals(new int[] {1, 2, 3}, ids(reverseSorted));

        reverseSorted = new PatientAdmission[] {alreadySorted[2], alreadySorted[1], alreadySorted[0]};
        AdmissionSort.mergeSort(reverseSorted, AdmissionSort.Criterion.ADMISSION_TIME);
        assertArrayEquals(new int[] {1, 2, 3}, ids(reverseSorted));

        reverseSorted = new PatientAdmission[] {alreadySorted[2], alreadySorted[1], alreadySorted[0]};
        AdmissionSort.quickSort(reverseSorted, AdmissionSort.Criterion.ADMISSION_TIME);
        assertArrayEquals(new int[] {1, 2, 3}, ids(reverseSorted));
    }

    private PatientAdmission[] admissions() {
        return new PatientAdmission[] {
                new PatientAdmission(1, "Ama", 9, 2, 30, 1),
                new PatientAdmission(2, "Kofi", 7, 5, 20, 1),
                new PatientAdmission(3, "Esi", 11, 4, 40, 1)
        };
    }

    private int[] ids(PatientAdmission[] admissions) {
        int[] ids = new int[admissions.length];
        for (int index = 0; index < admissions.length; index++) {
            ids[index] = admissions[index].patientId();
        }
        return ids;
    }
}
