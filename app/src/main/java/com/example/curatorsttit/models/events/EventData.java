package com.example.curatorsttit.models.events;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

public class EventData {
    @SerializedName("Id")
    @Expose
    private int id;

    @SerializedName("PlanId")
    private int planId;

    @SerializedName("Name")
    private String name;

    @SerializedName("Date")
    private Date date;

    @SerializedName("Status")
    private String status;

    @SerializedName("Organizer")
    private String organizer;

    @SerializedName("Rezults")
    private String results;

    @SerializedName("EventCodes")
    private List<String> codes;

    public List<String> getCodes() {
        return codes;
    }

    public void setCodes(List<String> codes) {
        this.codes = codes;
    }

    @SerializedName("EventsModules")
    private List<String> modules;

    public List<String> getModules() {
        return modules;
    }

    public void setModules(List<String> modules) {
        this.modules = modules;
    }

    public EventData() {
    }

    public EventData(int id, int planId, String name, Date date, String status, String organizer, String results) {
        this.id = id;
        this.planId = planId;
        this.name = name;
        this.date = date;
        this.status = status;
        this.organizer = organizer;
        this.results = results;
    }

    public EventData(int id, String name, Date date, String status, String organizer, String results, List<String> codes, List<String> modules) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.status = status;
        this.organizer = organizer;
        this.results = results;
        this.codes = codes;
        this.modules = modules;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOrganizer() {
        return organizer;
    }

    public void setOrganizer(String organizer) {
        this.organizer = organizer;
    }

    public String getResults() {
        return results;
    }

    public void setResults(String results) {
        this.results = results;
    }
}
