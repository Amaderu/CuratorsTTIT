package com.example.curatorsttit.models;

import com.google.gson.annotations.SerializedName;

public class CuratorsGroups {
    @SerializedName("Id")
    int id;
    @SerializedName("UserId")
    int userId;
    @SerializedName("GroupId")
    int groupId;

    public CuratorsGroups(int id, int userId, int groupId) {
        this.id = id;
        this.userId = userId;
        this.groupId = groupId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }
}
