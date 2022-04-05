package com.example.curatorsttit.data.local.entity;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.example.curatorsttit.models.interfaces.User;

@Entity(tableName = "users"
        ,
        foreignKeys = {
                @ForeignKey(entity = RoleEntity.class,
                        parentColumns = "id",
                        childColumns = "roleId"
                ),
                @ForeignKey(entity = com.example.curatorsttit.data.local.entity.PersonEntity.class,
                        parentColumns = "id",
                        childColumns = "personId"
                )
        },
        indices = {@Index(value = "roleId"),
                @Index(value = "personId",unique=true)}
)
public class UserEntity implements User {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private int personId;
    private int roleId;
    private String login;
    private String password;

    @Override
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public int getPersonId() {
        return personId;
    }

    public void setPersonId(int personId) {
        this.personId = personId;
    }

    @Override
    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    @Override
    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserEntity() {
    }
    @Ignore
    public UserEntity(int id, int personId, int roleId, String login, String password) {
        this.id = id;
        this.personId = personId;
        this.roleId = roleId;
        this.login = login;
        this.password = password;
    }

    public UserEntity(User user) {
        this.id = user.getId();
        this.personId = user.getPersonId();
        this.roleId = user.getRoleId();
        this.login = user.getLogin();
        this.password = user.getPassword();
    }

}
