package com.hospital.algorithms.optimization;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SurgerySchedulerTest {

    @Test
    void schedule_buildsBenefitTableAndReconstructsBestScheduleWithinTheatreBudget() {
        SurgeryScheduler.SurgeryRequest[] requests = {
                request("S1", 2, 6), request("S2", 3, 10), request("S3", 2, 7)
        };

        SurgeryScheduler.ScheduleResult result = SurgeryScheduler.schedule(requests, 4, 1);

        assertEquals(17, result.totalClinicalBenefit());
        assertEquals(5, result.hoursUsed());
        assertArrayEquals(new String[] {"S2", "S3"}, ids(result.selected()));
        assertArrayEquals(new int[] {0, 0, 6, 6, 6, 6}, result.benefitTable()[1]);
        assertEquals(17, result.benefitTable()[3][5]);
    }

    @Test
    void schedule_handlesZeroBudgetAndEmptyRequestList() {
        SurgeryScheduler.ScheduleResult noTime = SurgeryScheduler.schedule(new SurgeryScheduler.SurgeryRequest[] {request("S1", 1, 5)}, 0, 0);
        SurgeryScheduler.ScheduleResult noRequests = SurgeryScheduler.schedule(new SurgeryScheduler.SurgeryRequest[0], 4, 0);

        assertEquals(0, noTime.totalClinicalBenefit());
        assertEquals(0, noRequests.totalClinicalBenefit());
        assertEquals(1, noRequests.benefitTable().length);
    }

    @Test
    void schedule_rejectsInvalidBudgetAndRequests() {
        assertThrows(IllegalArgumentException.class, () -> SurgeryScheduler.schedule(null, 1, 0));
        assertThrows(IllegalArgumentException.class, () -> SurgeryScheduler.schedule(new SurgeryScheduler.SurgeryRequest[0], -1, 0));
        assertThrows(IllegalArgumentException.class, () -> SurgeryScheduler.schedule(new SurgeryScheduler.SurgeryRequest[] {null}, 1, 0));
        assertThrows(IllegalArgumentException.class, () -> request("", 1, 1));
    }

    private SurgeryScheduler.SurgeryRequest request(String id, int duration, int benefit) {
        return new SurgeryScheduler.SurgeryRequest(id, duration, benefit);
    }

    private String[] ids(SurgeryScheduler.SurgeryRequest[] requests) {
        String[] ids = new String[requests.length];
        for (int index = 0; index < requests.length; index++) {
            ids[index] = requests[index].surgeryId();
        }
        return ids;
    }
}
