package com.example.curatorsttit.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Objects;

public class Person {

    @SerializedName("Email")
    private String email;

    //group number
    @SerializedName("number")
    private String group;

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    @SerializedName("FirstName")
    private String firstName;

    @SerializedName("Phone")
    private String phone;

    @SerializedName("Id")
    @Expose
    private int id;

    @SerializedName("LastName")
    private String lastName;

    @SerializedName("MiddleName")
    private String middleName;

    public Person(String lastName, String firstName, String middleName, String email, String phone) {
        this.email = email;
        this.firstName = firstName;
        this.phone = phone;
        this.lastName = lastName;
        this.middleName = middleName;
    }

    public Person(String lastName, String firstName, String middleName, String group) {
        this.email = email;
        this.firstName = firstName;
        this.phone = phone;
        this.lastName = lastName;
        this.middleName = middleName;
        this.group = group;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getFIO() {
        return lastName + " " + firstName + " " + middleName;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhone() {
        return phone;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getMiddleName() {
        return middleName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return id == person.id && Objects.equals(email, person.email) && Objects.equals(group, person.group) && Objects.equals(firstName, person.firstName) && Objects.equals(phone, person.phone) && Objects.equals(lastName, person.lastName) && Objects.equals(middleName, person.middleName);
    }

}