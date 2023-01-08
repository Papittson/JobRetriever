package com.example.jobretriever.models;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;

import com.example.jobretriever.R;
import com.example.jobretriever.activities.MainActivity;

public enum DurationType {
    CDD(R.string.CDD),
    CDI(R.string.CDI),
    STAGE(R.string.internship),
    ALTERNANCE(R.string.sandwish_course);

    public final int stringResId;

    DurationType(@StringRes int stringResId) {
        this.stringResId = stringResId;
    }

    @NonNull
    @Override
    public String toString() {
        return MainActivity.getContext().getString(stringResId);
    }
}
