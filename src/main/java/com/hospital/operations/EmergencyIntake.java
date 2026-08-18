package com.hospital.operations;

import com.hospital.structures.core.Deque;

public class EmergencyIntake {
    private final Deque<String> intake;

    public EmergencyIntake(){
        intake = new Deque<String>();
    }

    //Routine walk-ins join the rear
    public void admitRoutine(String patient) {
        intake.addRear(patient);
    }

    //Emergency/trauma cases join the front
    public void admitEmergency(String patient) {
        intake.addFront(patient);
    }

    //Attend the patient at the front
    public String attendNext(){
        return intake.removeFront();
    }

    //View the next patient to be attended
    public String getNextPatient(){
        if(intake.isEmpty()){
            return null;
        }
        return intake.peekFront();
    }

    public boolean hasPatients() {
        return !intake.isEmpty();
    }

    public int getPatientCount() {
        return intake.size();
    }

}
