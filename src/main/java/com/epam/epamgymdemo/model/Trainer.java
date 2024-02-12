package com.epam.epamgymdemo.model;

public class Trainer {
    private String specialization;
    private String userId;

    public Trainer(String specialization, String userId) {
        this.specialization = specialization;
        this.userId = userId;
    }

    public String getSpecialization() {
        return specialization;
    }

    public String getUserId() {
        return userId;
    }
}
