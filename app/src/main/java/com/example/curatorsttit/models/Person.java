package com.example.curatorsttit.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Objects;

public class Person implements com.example.curatorsttit.models.interfaces.Person {
    //group number
    @Expose(serialize = false)
    @SerializedName("Number")
    private String group;

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    @SerializedName("Email")
    private String email;

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

    public Person(String lastName, String firstName, String middleName, String group, String email, String phone) {
        this.email = email;
        this.firstName = firstName;
        this.phone = phone;
        this.lastName = lastName;
        this.middleName = middleName;
        this.group = group;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public int getId() {
        return id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setName(String firstName) {
        this.firstName = firstName;
    }

    @Override
    public String getName() {
        return firstName;
    }

    @Override
    public String getSNP() {
        return lastName + " " + firstName + " " + middleName;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhone() {
        return phone;
    }

    public void setSurname(String lastName) {
        this.lastName = lastName;
    }

    public String getSurname() {
        return lastName;
    }

    public void setPatronymic(String middleName) {
        this.middleName = middleName;
    }

    @Override
    public String getPatronymic() {
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