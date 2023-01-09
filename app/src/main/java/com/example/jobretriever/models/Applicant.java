package com.example.jobretriever.models;

import static com.example.jobretriever.activities.MainActivity.DATE_FORMATTER;
import static com.example.jobretriever.enums.UserType.APPLICANT;

import com.google.firebase.firestore.Exclude;
import com.google.firebase.firestore.IgnoreExtraProperties;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
@IgnoreExtraProperties
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

    public Applicant(String mail, String password, String phone, String websiteUrl, String firstname, String lastname, String nationality, String educations, String experiences, LocalDate birthdate) {
        super(mail, password, phone, websiteUrl);
        this.firstname = firstname;
        this.lastname = lastname;
        this.nationality = nationality;
        this.educations = educations;
        this.experiences = experiences;
        this.favoritesId = new ArrayList<>();
        this.applicationsId = new ArrayList<>();
        this.birthdate = birthdate;
        this.userType = APPLICANT;
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

    public String getBirthdate() {
        return this.birthdate.format(DATE_FORMATTER);
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = LocalDate.parse(birthdate, DATE_FORMATTER);
    }

    @Exclude
    public boolean hasFavorite(String offerId) {
        return favoritesId.contains(offerId);
    }
}
