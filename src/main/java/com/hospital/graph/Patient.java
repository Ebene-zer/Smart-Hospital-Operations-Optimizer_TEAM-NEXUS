package com.hospital.model;

import java.util.Date;

public class Patient {
    private String patientId;
    private String name;
    private String nhisNumber;
    private Date birthDate;
    private String wardLocationId;
    private String allergies; // Simple string for now, can be parsed

    public Patient(String patientId, String name, String nhisNumber, Date birthDate, String wardLocationId,
            String allergies) {
        this.patientId = patientId;
        this.name = name;
        this.nhisNumber = nhisNumber;
        this.birthDate = birthDate;
        this.wardLocationId = wardLocationId;
        this.allergies = allergies;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNhisNumber() {
        return nhisNumber;
    }

    public void setNhisNumber(String nhisNumber) {
        this.nhisNumber = nhisNumber;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public String getWardLocationId() {
        return wardLocationId;
    }

    public void setWardLocationId(String wardLocationId) {
        this.wardLocationId = wardLocationId;
    }

    public String getAllergies() {
        return allergies;
    }

    public void setAllergies(String allergies) {
        this.allergies = allergies;
    }

    @Override
    public String toString() {
        return "Patient{" +
                "patientId='" + patientId + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}