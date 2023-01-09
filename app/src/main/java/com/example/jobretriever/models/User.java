package com.example.jobretriever.models;

import com.example.jobretriever.enums.UserType;
import com.google.firebase.firestore.IgnoreExtraProperties;

@SuppressWarnings("unused")
@IgnoreExtraProperties
public class User extends Entity {
    String mail;
    String password;
    String phone;
    String websiteUrl;
    UserType userType;

    public User() {}

    public User(String mail, String password, String phone, String websiteUrl) {
        this.mail = mail;
        this.password = password;
        this.phone = phone;
        this.websiteUrl = websiteUrl;
    }

    public String getWebsiteUrl() {
        return websiteUrl;
    }

    public void setWebsiteUrl(String websiteUrl) {
        this.websiteUrl = websiteUrl;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }
}
