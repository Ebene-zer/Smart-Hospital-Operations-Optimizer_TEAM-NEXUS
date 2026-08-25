package com.hospital.operations;

import com.hospital.structures.core.DynamicArray;

public class WardPatientRoster {
    public static class Patient {
        private final String name;
        private final String condition;
        private final int admittedTime;
        private int dischargedTime;
        private boolean isAdmitted;
        

        public Patient(String name, String condition, int admittedTime) {
            this.name = name;
            this.condition = condition;
            this.admittedTime = admittedTime;
            this.isAdmitted = true; 
            this.dischargedTime = -1;
        }

        public String getName() {
            return name;
        }
           
        public String getCondition() {
            return condition;
        }

        public int getAdmittedTime() {
            return admittedTime;
        }

        public int getDischargedTime() {
            return dischargedTime;
        }

        public boolean isAdmitted() {
            return isAdmitted;
        }

        public void discharge(int dischargedTime) {
            this.dischargedTime = dischargedTime;
            this.isAdmitted = false;
        }

        @Override 
        public String toString() {
            return "Patient{" +
                    "name='" + name + '\'' +
                    ", condition='" + condition + '\'' +
                    ", admittedTime=" + admittedTime +
                    ", dischargedTime=" + dischargedTime +
                    ", isAdmitted=" + isAdmitted +
                    '}';
        }
    }

        private final DynamicArray<Patient> patients;

        public WardPatientRoster() {
            this.patients = new DynamicArray<>();
        }

        // Admit a new patient to the ward
        public Patient admit(String name, String condition, int admittedTime) {
            Patient newPatient = new Patient(name, condition, admittedTime);
            patients.insert(patients.size(), newPatient);
            return newPatient; 
        }

        //Discharge a patient from the ward
        public void discharge(Patient patient, int dischargedTime) {
            patient.discharge(dischargedTime);
        }

        //Find a patient by name
        public Patient findPatientByName(String name) { 
            for (int i = 0; i < patients.size(); i++) {
                Patient patient = patients.get(i);
                if (patient.getName().equals(name)) {
                    return patient;
                }
            }
            return null; 
        }

        //Delete a patient from the roster
        public boolean deletePatient(Patient patient) {
            for (int i = 0; i < patients.size(); i++) {
                if (patients.get(i).equals(patient)) {
                    patients.remove(i);
                    return true; 
            }
            
        }
        return false; 
        }

        //All patients that are currently admitted to the ward
        public DynamicArray<Patient> getAdmittedPatients() {    
            DynamicArray<Patient> admittedPatients = new DynamicArray<>();
            for (int i = 0; i < patients.size(); i++) {
                Patient patient = patients.get(i);
                if (patient.isAdmitted()) {
                    admittedPatients.addLast(patient);
                }
            }
            return admittedPatients;
        }

        //All patients that that ever been in the roster, including those that have been discharged
        public DynamicArray<Patient> getAllPatients() {
            return patients;
        }

       public int getTotalPatients() {
            return patients.size();
        }

        private void printPatientRoster() {
            System.out.println("Patient Roster:");
            for (int i = 0; i < patients.size(); i++) {
                Patient patient = patients.get(i);
                System.out.println(patient);
            }
        }

        
    }

