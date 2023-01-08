package com.example.jobretriever.models;

import static com.example.jobretriever.models.SignUpStatus.ACCEPTED;
import static com.example.jobretriever.models.SignUpStatus.PENDING;
import static com.example.jobretriever.models.UserType.APPLICANT;

import androidx.annotation.NonNull;

import com.google.firebase.Timestamp;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@SuppressWarnings("unused")
public class User extends Entity {
    String firstname;
    String mail;
    String name;
    String nationality;
    String password;
    String phone;
    UserType type;
    String businessName;
    List<String> educations;
    List<String> experiences;
    List<String> favoritesId;
    List<String> applicationsId;
    String birthdate;
    String address;
    String siret;
    String manager;
    SignUpStatus signUpStatus;

    public User() {
        this.educations = new ArrayList<>();
        this.experiences = new ArrayList<>();
        this.favoritesId = new ArrayList<>();
        this.applicationsId = new ArrayList<>();
    }

    public User(String mail, String password, String firstname, String name, String nationality, String phone, String birthdate) {
        super();
        this.firstname = firstname;
        this.mail = mail;
        this.name = name;
        this.nationality = nationality;
        this.password = password;
        this.phone = phone;
        this.type = APPLICANT;
        this.birthdate = birthdate;
        this.signUpStatus = ACCEPTED;
    }

    public User(String mail, String password, String businessName, String phone, String address, String siret, String manager, UserType type) {
        super();
        this.mail = mail;
        this.password = password;
        this.businessName = businessName;
        this.phone = phone;
        this.address = address;
        this.siret = siret;
        this.manager = manager;
        this.type = type;
        this.signUpStatus = PENDING;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(Timestamp birthdate) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE);
        this.birthdate = formatter.format(birthdate.toDate());
    }

    public List<String> getExperiences() {
        return this.experiences;
    }

    public String getExperience() {
        if(this.experiences == null || this.experiences.isEmpty()) {
            return "No experiences"; // TODO Mettre ça dans un string resource
        }
        return String.join("\n", this.experiences);
    }

    public void setExperiences(List<String> experiences) {
        this.experiences = experiences;
    }

    public List<String> getEducations() {
        return this.educations;
    }

    public String getEducation() {
        if(this.educations == null || this.educations.isEmpty()) {
            return "No educations"; // TODO Mettre ça dans un string resource
        }
        return String.join("\n", this.educations);
    }

    public void setEducations(List<String> educations) {
        this.educations = educations;
    }

    public List<String> getApplicationsId() {
        return this.applicationsId;
    }

    public void setApplicationsId(List<String> applicationsId) {
        this.applicationsId = applicationsId;
    }

    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }

    public String getSiret() {
        return siret;
    }
    public void setSiret(String siret) {
        this.siret = siret;
    }

    public String getManager() {
        return manager;
    }
    public void setManager(String manager) {
        this.manager = manager;
    }

    public SignUpStatus getSignUpStatus() {
        return signUpStatus;
    }
    public void setSignUpStatus(SignUpStatus signUpStatus) {
        this.signUpStatus = signUpStatus;
    }

    public List<String> getFavoritesId() {
        return favoritesId;
    }

    public void setFavoritesId(List<String> favoritesId) {
        this.favoritesId = favoritesId;
    }

    public boolean hasFavorite(String offerId) {
        return favoritesId.contains(offerId);
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = "";
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public UserType getType() {
        return type;
    }

    public void setType(UserType type) {
        this.type = type;
    }

    @NonNull
    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", firstname='" + firstname + '\'' +
                ", mail='" + mail + '\'' +
                ", name='" + name + '\'' +
                ", nationality='" + nationality + '\'' +
                ", password='" + password + '\'' +
                ", phone='" + phone + '\'' +
                ", type='" + type + '\'' +
                '}';
    }

    public int getAge() {
        return Period.between(LocalDate.now(), LocalDate.now()).getYears(); // TODO Refaire ça avec birthdate
    }
}
