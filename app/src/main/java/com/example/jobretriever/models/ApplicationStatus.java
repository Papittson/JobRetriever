package com.example.jobretriever.models;

import androidx.annotation.StringRes;

import com.example.jobretriever.R;

public enum ApplicationStatus {
    // TODO Mettre des strings qui ont du sens
    PENDING(R.string.pending),
    ACCEPTED(R.string.pending),
    DENIED(R.string.pending),
    AWAITING_ATTACHEMENT(R.string.pending),
    AWAITING_MEETING(R.string.pending);

    public final int stringResId;

    ApplicationStatus(@StringRes int stringResId) {
        this.stringResId = stringResId;
    }
}
