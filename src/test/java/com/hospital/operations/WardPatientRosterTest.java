package com.hospital.operations;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class WardPatientRosterTest {

    private WardPatientRoster roster;

    @BeforeEach
    void setUp() {
        roster = new WardPatientRoster();
    }

    // Admit a new patient to the ward
    @Test
    void admit_addsPatientAsAdmitted() {
        var p = roster.admit("Ama", "Malaria", 10);
        assertTrue(p.isAdmitted());
        assertEquals(1, roster.getTotalPatients());
    }

    @Test
    void admit_multiplePatients_incrementsCountCorrectly() {
        // edge case
        roster.admit("A", "x", 1);
        roster.admit("B", "x", 2);
        roster.admit("C", "x", 3);
        roster.admit("D", "x", 4);
        roster.admit("E", "x", 5); 
        assertEquals(5, roster.getTotalPatients());
    }


    //Discharge a patient from the ward
    @Test
    void discharge_marksPatientAsNotAdmitted() {
        var p = roster.admit("Kofi", "Fever", 5);
        roster.discharge(p, 15);
        assertFalse(p.isAdmitted());
        assertEquals(15, p.getDischargedTime());
    }

    @Test
    void discharge_calledTwice_overwritesDischargeTime() {
        // edge case
        var p = roster.admit("Yaw", "Cold", 1);
        roster.discharge(p, 10);
        roster.discharge(p, 20);
        assertEquals(20, p.getDischargedTime());
    }

  
    //Find a patient by name
    @Test
    void findPatientByName_returnsNullWhenNeverAdmitted() {
        roster.admit("Esi", "Flu", 1);
        assertNull(roster.findPatientByName("Yaw")); 
    }

    @Test
    void findPatientByName_stillFindsDischargedPatient() {
        // edge case: method searches ALL patients, admitted or not
        var p = roster.admit("Kwame", "Cold", 1);
        roster.discharge(p, 5);
        assertNotNull(roster.findPatientByName("Kwame"));
    }

    @Test
    void findPatientByName_emptyRoster_returnsNull() {
        assertNull(roster.findPatientByName("Anyone"));
    }


    // Delete a patient from the roster
    @Test
    void deletePatient_removesPatient_returnsTrue() {
        var p = roster.admit("Adjoa", "Cold", 2);
        assertTrue(roster.deletePatient(p));
        assertEquals(0, roster.getTotalPatients());
    }

    @Test
    void deletePatient_alreadyDeleted_returnsFalse() {
        var p = roster.admit("Adjoa", "Cold", 2);
        roster.deletePatient(p);
        assertFalse(roster.deletePatient(p)); //no longer in roster
    }

    @Test
    void deletePatient_neverAdmitted_returnsFalse() {
        // edge case: a Patient that was never added to this roster at all
        var stranger = roster.admit("Temp", "x", 0);
        roster.deletePatient(stranger);
        assertFalse(roster.deletePatient(stranger));
    }


    // All patients currently admitted to the ward
    @Test
    void getAdmittedPatients_excludesDischargedPatients() {
        var p1 = roster.admit("Ama", "Malaria", 1);
        var p2 = roster.admit("Kofi", "Fever", 2);
        roster.discharge(p2, 10);

        var admitted = roster.getAdmittedPatients();
        assertEquals(1, admitted.size());
        assertEquals(p1, admitted.get(0));
    }

    @Test
    void getAdmittedPatients_emptyRoster_returnsEmpty() {
        //edge case: no patients have been admitted yet
        assertEquals(0, roster.getAdmittedPatients().size());
    }



    
    // All patients ever in the roster (including discharged)
    @Test
    void getAllPatients_includesDischargedPatients() {
        var p1 = roster.admit("Ama", "Malaria", 1);
        var p2 = roster.admit("Kofi", "Fever", 2);
        roster.discharge(p2, 10);

        assertEquals(2, roster.getAllPatients().size());
    }

    @Test
    void getAllPatients_emptyRoster_returnsEmpty() {
        //edge case: no patients have been admitted yet
        assertEquals(0, roster.getAllPatients().size());
    }
}