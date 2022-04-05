package com.example.curatorsttit.data.local.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.curatorsttit.models.interfaces.Role;

@Entity(tableName = "roles")
public class RoleEntity implements Role {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String name;

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }
}
