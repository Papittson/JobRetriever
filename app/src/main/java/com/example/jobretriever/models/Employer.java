package com.example.jobretriever.models;

import com.example.jobretriever.enums.SignUpStatus;
import com.example.jobretriever.enums.UserType;
import com.google.firebase.firestore.IgnoreExtraProperties;

@SuppressWarnings("unused")
@IgnoreExtraProperties
public class Employer extends User {
    String businessName;
    String address;
    String siret;
    String manager;
    SignUpStatus signUpStatus;

    public Employer() {}

    public Employer(String mail, String password, String phone, String websiteUrl, UserType type, String businessName, String address, String siret, String manager) {
        super(mail, password, phone, websiteUrl);
        this.userType = type;
        this.businessName = businessName;
        this.address = address;
        this.siret = siret;
        this.manager = manager;
        this.signUpStatus = SignUpStatus.PENDING;
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
    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

}
