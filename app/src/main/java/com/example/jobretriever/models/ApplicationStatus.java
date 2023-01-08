package com.example.jobretriever.models;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;

import com.example.jobretriever.R;
import com.example.jobretriever.activities.MainActivity;

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

    @NonNull
    @Override
    public String toString() {
        return MainActivity.getContext().getString(stringResId);
    }
}
