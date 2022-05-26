package com.example.curatorsttit.models.events;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EventsCodes {
    @SerializedName("Id")
    int id;
    @SerializedName("EventId")
    int eventId;
    @SerializedName("Code")
    String code;

    public EventsCodes(int id, int eventId, String code) {
        this.id = id;
        this.eventId = eventId;
        this.code = code;
    }

    public EventsCodes() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
