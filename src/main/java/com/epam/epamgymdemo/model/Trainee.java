package com.epam.epamgymdemo.model;

import java.time.LocalDate;

public class Trainee {
    private LocalDate DoB;
    private String address;
    private String userId;

    public Trainee(LocalDate doB, String address, String userId) {
        DoB = doB;
        this.address = address;
        this.userId = userId;
    }

    public LocalDate getDoB() {
        return DoB;
    }

    public String getAddress() {
        return address;
    }

    public String getUserId() {
        return userId;
    }
}
