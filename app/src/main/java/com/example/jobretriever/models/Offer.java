package com.example.jobretriever.models;


import androidx.annotation.NonNull;

import com.example.jobretriever.R;

import java.util.Date;
import java.util.Map;

@SuppressWarnings("unused")
public class Offer extends Entity {
    String title;
    String duration;
    Date date;
    String field;
    String description;
    Double wage;
    String employerID;
    User employer;
    String locationID;
    Location location;
    Map<String, ApplicationState> applications;

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

    public Map<String, ApplicationState> getApplications() {
        return applications;
    }

    public void setApplications(Map<String, ApplicationState> applications) {
        this.applications = applications;
    }

    public String getLocationID() {
        return locationID;
    }

    public void setLocationID(String locationID) {
        this.locationID = locationID;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
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
    public String getEmployerID() {
        return employerID;
    }

    public void setEmployerID(String employerID) {
        this.employerID = employerID;
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

    public double getWage() {
        return wage;
    }

    public void setWage(double wage) { this.wage = wage; }

    @NonNull
    @Override
    public String toString() {
        return "Offer{" +
                "title='" + title + '\'' +
                ", duration='" + duration + '\'' +
                ", date=" + date +
                ", field='" + field + '\'' +
                ", description='" + description + '\'' +
                ", wage=" + wage +
                ", employerID='" + employerID + '\'' +
                ", employer=" + employer +
                ", locationID='" + locationID + '\'' +
                ", location=" + location +
                '}';
    }
}
