package com.example.landmarkbookapp;

import java.io.Serializable;

public class Stadium implements Serializable {
    String name;
    String Country;
    int id;

    public Stadium(String name, String country, int id) {
        this.name = name;
        this.Country = country;
        this.id = id;
    }
}
