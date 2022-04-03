package com.example.curatorsttit.models;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

public class Group {
    @SerializedName("Id")
    int id;
    @SerializedName("Number")
    String number;
    @SerializedName("Speciality")
    String speciality;

    public Group(int id, String number, String speciality) {
        this.id = id;
        this.number = number;
        this.speciality = speciality;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getSpeciality() {
        return speciality;
    }

    public void setSpeciality(String speciality) {
        this.speciality = speciality;
    }

    @NonNull
    @Override
    public String toString() {
        return this.number;
    }
}
