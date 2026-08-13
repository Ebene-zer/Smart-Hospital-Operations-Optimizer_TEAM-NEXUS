package com.hospital.algorithms.sort;

import com.hospital.algorithms.PatientAdmission;

public final class AdmissionSort {

    public enum Criterion { ADMISSION_TIME, URGENCY, AGE }

    private AdmissionSort() {
    }

    public static void selectionSort(PatientAdmission[] admissions, Criterion criterion) {
        requireInputs(admissions, criterion);
        for (int start = 0; start < admissions.length - 1; start++) {
            int minimum = start;
            for (int index = start + 1; index < admissions.length; index++) {
                if (compare(admissions[index], admissions[minimum], criterion) < 0) {
                    minimum = index;
                }
            }
            swap(admissions, start, minimum);
        }
    }

    public static void insertionSort(PatientAdmission[] admissions, Criterion criterion) {
        requireInputs(admissions, criterion);
        for (int index = 1; index < admissions.length; index++) {
            PatientAdmission current = admissions[index];
            int position = index - 1;
            while (position >= 0 && compare(admissions[position], current, criterion) > 0) {
                admissions[position + 1] = admissions[position];
                position--;
            }
            admissions[position + 1] = current;
        }
    }

    public static void mergeSort(PatientAdmission[] admissions, Criterion criterion) {
        requireInputs(admissions, criterion);
        PatientAdmission[] auxiliary = new PatientAdmission[admissions.length];
        mergeSort(admissions, auxiliary, 0, admissions.length, criterion);
    }

    public static void quickSort(PatientAdmission[] admissions, Criterion criterion) {
        requireInputs(admissions, criterion);
        quickSort(admissions, 0, admissions.length - 1, criterion);
    }

    private static void mergeSort(PatientAdmission[] values, PatientAdmission[] auxiliary, int low, int high, Criterion criterion) {
        if (high - low <= 1) {
            return;
        }
        int middle = low + (high - low) / 2;
        mergeSort(values, auxiliary, low, middle, criterion);
        mergeSort(values, auxiliary, middle, high, criterion);
        int left = low;
        int right = middle;
        for (int target = low; target < high; target++) {
            if (left == middle) {
                auxiliary[target] = values[right++];
            } else if (right == high || compare(values[left], values[right], criterion) <= 0) {
                auxiliary[target] = values[left++];
            } else {
                auxiliary[target] = values[right++];
            }
        }
        for (int index = low; index < high; index++) {
            values[index] = auxiliary[index];
        }
    }

    private static void quickSort(PatientAdmission[] values, int low, int high, Criterion criterion) {
        if (low >= high) {
            return;
        }
        PatientAdmission pivot = values[high];
        int boundary = low;
        for (int index = low; index < high; index++) {
            if (compare(values[index], pivot, criterion) <= 0) {
                swap(values, boundary++, index);
            }
        }
        swap(values, boundary, high);
        quickSort(values, low, boundary - 1, criterion);
        quickSort(values, boundary + 1, high, criterion);
    }

    private static int compare(PatientAdmission first, PatientAdmission second, Criterion criterion) {
        int firstValue = valueFor(first, criterion);
        int secondValue = valueFor(second, criterion);
        int result = Integer.compare(firstValue, secondValue);
        return result != 0 ? result : Integer.compare(first.patientId(), second.patientId());
    }

    private static int valueFor(PatientAdmission admission, Criterion criterion) {
        return switch (criterion) {
            case ADMISSION_TIME -> admission.admissionTime();
            case URGENCY -> -admission.urgency();
            case AGE -> admission.age();
        };
    }

    private static void swap(PatientAdmission[] values, int first, int second) {
        PatientAdmission temporary = values[first];
        values[first] = values[second];
        values[second] = temporary;
    }

    private static void requireInputs(PatientAdmission[] admissions, Criterion criterion) {
        if (admissions == null || criterion == null) {
            throw new IllegalArgumentException("admissions and criterion must not be null");
        }
        for (PatientAdmission admission : admissions) {
            if (admission == null) {
                throw new IllegalArgumentException("admissions must not contain null entries");
            }
        }
    }
}
