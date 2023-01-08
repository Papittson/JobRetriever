package com.example.jobretriever.models;

import com.google.firebase.Timestamp;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public class Applicant extends User {
    String firstname;
    String lastname;
    String nationality;
    String educations;
    String experiences;
    List<String> favoritesId;
    List<String> applicationsId;
    LocalDate birthdate;

    public Applicant() {}

    public Applicant(String mail, String password, String phone, String firstname, String lastname, String nationality, String educations, String experiences, LocalDate birthdate) {
        super(mail, password, phone);
        this.firstname = firstname;
        this.lastname = lastname;
        this.nationality = nationality;
        this.educations = educations;
        this.experiences = experiences;
        this.favoritesId = new ArrayList<>();
        this.applicationsId = new ArrayList<>();
        this.birthdate = birthdate;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getEducations() {
        return educations;
    }

    public void setEducations(String educations) {
        this.educations = educations;
    }

    public String getExperiences() {
        return experiences;
    }

    public void setExperiences(String experiences) {
        this.experiences = experiences;
    }

    public List<String> getFavoritesId() {
        return favoritesId;
    }

    public void setFavoritesId(List<String> favoritesId) {
        this.favoritesId = favoritesId;
    }

    public List<String> getApplicationsId() {
        return applicationsId;
    }

    public void setApplicationsId(List<String> applicationsId) {
        this.applicationsId = applicationsId;
    }

    public LocalDate getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(Timestamp birthdate) {
        this.birthdate = birthdate.toDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    public boolean hasFavorite(String offerId) {
        return favoritesId.contains(offerId);
    }

    public int getAge() {
        return (int) ChronoUnit.YEARS.between(birthdate, LocalDate.now());
    }
}
