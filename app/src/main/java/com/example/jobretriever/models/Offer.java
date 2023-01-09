package com.example.jobretriever.models;


import static com.example.jobretriever.activities.MainActivity.DATE_FORMATTER;

import com.example.jobretriever.R;
import com.example.jobretriever.enums.ApplicationStatus;
import com.example.jobretriever.enums.DurationType;
import com.google.firebase.firestore.Exclude;
import com.google.firebase.firestore.IgnoreExtraProperties;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unused")
@IgnoreExtraProperties
public class Offer extends Entity {
    String title;
    DurationType duration;
    LocalDate date;
    String field;
    String description;
    Double wage;
    Map<String, ApplicationStatus> applications;
    String employerId;
    String location;
    @Exclude
    Employer employer;

    public Offer() {
        super();
        this.applications = new HashMap<>();
    }

    public Offer(String title, DurationType duration, LocalDate date, String field, String description, Double wage, String employerId, String location) {
        this();
        this.title = title;
        this.duration = duration;
        this.date = date;
        this.field = field;
        this.description = description;
        this.wage = wage;
        this.employerId = employerId;
        this.location = location;
    }

    @Exclude
    public int getApplicationStatus(String userId) {
        ApplicationStatus status = this.applications.get(userId);
        if(status != null) {
            return status.stringResId;
        }
        return R.string.empty_string;
    }

    @Exclude
    public boolean isSimilarTo(String title) {
        return this.title.toLowerCase().contains(title.toLowerCase()) || title.toLowerCase().contains(this.title.toLowerCase());
    }

    public void setWage(Double wage) {
        this.wage = wage;
    }
    public Double getWage() {
        return this.wage;
    }

    public Map<String, ApplicationStatus> getApplications() {
        return applications;
    }
    public void setApplications(Map<String, ApplicationStatus> applications) {
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

    @Exclude
    public Employer getEmployer() {
        return employer;
    }
    @Exclude
    public void setEmployer(Employer employer) {
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

    public DurationType getDuration() {
        return duration;
    }
    public void setDuration(DurationType duration) {
        this.duration = duration;
    }

    public String getDate() {
        return this.date.format(DATE_FORMATTER);
    }
    public void setDate(String date) {
        this.date = LocalDate.parse(date, DATE_FORMATTER);;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
}
