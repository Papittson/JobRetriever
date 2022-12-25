package com.example.jobretriever.models;

import com.google.type.Date;

import java.util.Collection;

public class User {
    String id;
    String firstname;
    String mail;
    String name;
    String nationality;
    String password;
    String phone;
    String type;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

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

    // METHODS
    Collection<Offer> searchOfferById(String id){
        return null;
    }
    Collection<Offer> searchOfferByTitle(String title){
        return null;
    }
    Collection<Offer> searchOfferByDate(Date date){
        return null;
    }
    Collection<Offer> searchOfferByField(String field){
        return null;
    }
}
