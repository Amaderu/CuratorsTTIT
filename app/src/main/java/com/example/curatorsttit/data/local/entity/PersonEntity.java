package com.example.curatorsttit.data.local.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.curatorsttit.models.interfaces.Person;

@Entity(tableName = "persons")
public class PersonEntity implements Person {
    @PrimaryKey(autoGenerate = true)
    int id;
    private String name, surname, patronymic;

    @Override
    public String getSNP() {
        final StringBuilder builder = new StringBuilder();
        builder.append(surname+" "+name.charAt(0)+"."+patronymic.charAt(0)+".");
        return builder.toString();
    }

    @Override
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    @Override
    public String getPatronymic() {
        return patronymic;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }
}
