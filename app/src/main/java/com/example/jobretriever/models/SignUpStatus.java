package com.example.jobretriever.models;

import androidx.annotation.StringRes;

import com.example.jobretriever.R;

public enum SignUpStatus {
    // TODO Mettre des strings qui ont du sens
    PENDING(R.string.pending),
    ACCEPTED(R.string.pending),
    DENIED(R.string.pending);

    public final int stringResId;

    SignUpStatus(@StringRes int stringResId) {
        this.stringResId = stringResId;
    }
}
