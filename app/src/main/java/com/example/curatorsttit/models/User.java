package com.example.curatorsttit.models;

import com.google.gson.annotations.SerializedName;

public class User implements com.example.curatorsttit.models.interfaces.User {
    @SerializedName("Id")
    int id;
    @SerializedName("PersonId")
    int personId;
    @SerializedName("RoleId")
    int roleId;
    @SerializedName("Login")
    String login;
    @SerializedName("Password")
    String password;

    public User(int id, int personId, int roleId, String login, String password) {
        this.id = id;
        this.personId = personId;
        this.roleId = roleId;
        this.login = login;
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPersonId() {
        return personId;
    }

    public void setPersonId(int personId) {
        this.personId = personId;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
