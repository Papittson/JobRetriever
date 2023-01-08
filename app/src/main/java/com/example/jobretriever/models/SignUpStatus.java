package com.example.jobretriever.models;

import androidx.annotation.StringRes;

import com.example.jobretriever.R;

public enum SignUpStatus {

    PENDING(R.string.pending),
    ACCEPTED(R.string.accepted),
    DENIED(R.string.denied);

    public final int stringResId;

    SignUpStatus(@StringRes int stringResId) {
        this.stringResId = stringResId;
    }
}
