package com.example.jobretriever.models;

import androidx.annotation.StringRes;

import com.example.jobretriever.R;

public enum ApplicationStatus {

    PENDING(R.string.pending),
    ACCEPTED(R.string.accepted),
    DENIED(R.string.denied),
    AWAITING_ATTACHEMENT(R.string.awaiting_attachement),
    AWAITING_MEETING(R.string.awaiting_meeting);

    public final int stringResId;

    ApplicationStatus(@StringRes int stringResId) {
        this.stringResId = stringResId;
    }
}
