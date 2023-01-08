package com.example.jobretriever.models;


import com.example.jobretriever.R;
import com.google.firebase.firestore.Exclude;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unused")
public class Offer extends Entity {
    String title;
    String duration;
    Date date;
    String field;
    String description;
    Double wage;
    Map<String, ApplicationState> applications;
    String employerId;
    String location;
    @Exclude
    User employer;

    public Offer() {
        this.applications = new HashMap<>();
    }

    public Offer(String title, String duration, Date date, String field, String description, Double wage, String employerId, String location) {
        this.title = title;
        this.duration = duration;
        this.date = date;
        this.field = field;
        this.description = description;
        this.wage = wage;
        this.employerId = employerId;
        this.location = location;
        this.applications = new HashMap<>();
    }

    public int getApplicationStatus(String userId) {
        ApplicationState status = this.applications.get(userId);
        if(status != null) {
            return status.stringResId;
        }
        return R.string.empty_string;
    }

    public boolean isAppliedByUser(String userId) {
        return this.applications.containsKey(userId);
    }

    public void setWage(Double wage) {
        this.wage = wage;
    }
    public Double getWage() {
        return this.wage;
    }

    public Map<String, ApplicationState> getApplications() {
        return applications;
    }
    public void setApplications(Map<String, ApplicationState> applications) {
        this.applications = applications;
    }

    public String getLocation() {
        return location;
    }
    public void setLocation(String location) {
        this.location = location;
    }

    public String getField() {
        return field;
    }
    public void setField(String field) {
        this.field = field;
    }

    public User getEmployer() {
        return employer;
    }
    public void setEmployer(User employer) {
        this.employer = employer;
    }

    public String getEmployerId() {
        return employerId;
    }
    public void setEmployerId(String employerId) {
        this.employerId = employerId;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public String getDuration() {
        return duration;
    }
    public void setDuration(String duration) {
        this.duration = duration;
    }

    public Date getDate() {
        return date;
    }
    public void setDate(Date date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
}
