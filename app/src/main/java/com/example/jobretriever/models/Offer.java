package com.example.jobretriever.models;

import com.google.type.Date;

public class Offer extends Entity {
    String jobTitle;
    String duration;
    Date date;
    String sield;
    String description;
    double wage;

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
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

    public String getSield() {
        return sield;
    }

    public void setSield(String sield) {
        this.sield = sield;
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

    public void setWage(double wage) {
        this.wage = wage;
    }
}
