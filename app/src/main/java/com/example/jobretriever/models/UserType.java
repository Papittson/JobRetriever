package com.example.jobretriever.models;

import androidx.annotation.StringRes;

import com.example.jobretriever.R;

public enum UserType {
    APPLICANT(R.string.applicant),
    EMPLOYER(R.string.applicant),
    AGENCY(R.string.applicant);

    public final int stringResId;

    UserType(@StringRes int stringResId) {
        this.stringResId = stringResId;
    }
}
