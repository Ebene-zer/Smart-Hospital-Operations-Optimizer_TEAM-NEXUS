package com.hospital.algorithms.optimization;

public final class SurgeryScheduler {

    private SurgeryScheduler() {
    }

    public static ScheduleResult schedule(SurgeryRequest[] requests, int regularHours, int overtimeHours) {
        if (requests == null || regularHours < 0 || overtimeHours < 0) {
            throw new IllegalArgumentException("requests must not be null and hour budgets must not be negative");
        }
        int capacity = regularHours + overtimeHours;
        int[][] table = new int[requests.length + 1][capacity + 1];
        for (int requestIndex = 1; requestIndex <= requests.length; requestIndex++) {
            SurgeryRequest request = requireRequest(requests[requestIndex - 1]);
            for (int hours = 0; hours <= capacity; hours++) {
                table[requestIndex][hours] = table[requestIndex - 1][hours];
                if (request.durationHours() <= hours) {
                    int withRequest = request.clinicalBenefit() + table[requestIndex - 1][hours - request.durationHours()];
                    if (withRequest > table[requestIndex][hours]) {
                        table[requestIndex][hours] = withRequest;
                    }
                }
            }
        }

        SurgeryRequest[] reversed = new SurgeryRequest[requests.length];
        int selectedCount = 0;
        int remainingHours = capacity;
        for (int requestIndex = requests.length; requestIndex > 0; requestIndex--) {
            if (table[requestIndex][remainingHours] != table[requestIndex - 1][remainingHours]) {
                SurgeryRequest selected = requests[requestIndex - 1];
                reversed[selectedCount++] = selected;
                remainingHours -= selected.durationHours();
            }
        }
        SurgeryRequest[] selected = new SurgeryRequest[selectedCount];
        for (int index = 0; index < selectedCount; index++) {
            selected[index] = reversed[selectedCount - 1 - index];
        }
        return new ScheduleResult(selected, table[requests.length][capacity], capacity - remainingHours, copyTable(table));
    }

    private static SurgeryRequest requireRequest(SurgeryRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("requests must not contain null entries");
        }
        return request;
    }

    private static int[][] copyTable(int[][] table) {
        int[][] copy = new int[table.length][];
        for (int row = 0; row < table.length; row++) {
            copy[row] = table[row].clone();
        }
        return copy;
    }

    public record SurgeryRequest(String surgeryId, int durationHours, int clinicalBenefit) {
        public SurgeryRequest {
            if (surgeryId == null || surgeryId.isBlank() || durationHours <= 0 || clinicalBenefit < 0) {
                throw new IllegalArgumentException("surgeryId must be present, duration positive and benefit non-negative");
            }
        }
    }

    public record ScheduleResult(SurgeryRequest[] selected, int totalClinicalBenefit, int hoursUsed, int[][] benefitTable) {
        public ScheduleResult {
            selected = selected.clone();
            benefitTable = copyTable(benefitTable);
        }

        @Override
        public SurgeryRequest[] selected() {
            return selected.clone();
        }

        @Override
        public int[][] benefitTable() {
            return copyTable(benefitTable);
        }
    }
}
