package com.example.jobretriever.models;

import androidx.annotation.StringRes;

import com.example.jobretriever.R;

public enum DurationType {
    // TODO Mettre des strings qui ont du sens
    CDD(R.string.pending),
    CDI(R.string.pending),
    STAGE(R.string.pending),
    ALTERNANCE(R.string.pending);

    public final int stringResId;

    DurationType(@StringRes int stringResId) {
        this.stringResId = stringResId;
    }
}
