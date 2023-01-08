package com.example.jobretriever.models;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;

import com.example.jobretriever.R;
import com.example.jobretriever.activities.MainActivity;

public enum UserType {
    APPLICANT(R.string.applicant),
    EMPLOYER(R.string.employer),
    AGENCY(R.string.agency);

    public final int stringResId;

    UserType(@StringRes int stringResId) {
        this.stringResId = stringResId;
    }

    @NonNull
    @Override
    public String toString() {
        return MainActivity.getContext().getString(stringResId);
    }
}
