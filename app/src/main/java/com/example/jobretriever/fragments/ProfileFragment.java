package com.example.jobretriever.fragments;

import android.content.Intent;
import android.net.Uri;

import androidx.annotation.LayoutRes;

import com.example.jobretriever.R;
import com.example.jobretriever.models.User;
import com.example.jobretriever.viewmodels.UserViewModel;


public abstract class ProfileFragment extends JRFragment {
    User user;

    public ProfileFragment(@LayoutRes Integer fragmentLayout) {
        super(R.string.profile, fragmentLayout, true);
    }

    @Override
    public void onStop() {
        super.onStop();
        UserViewModel.getInstance().getSelectedUser().removeObservers(this);
        UserViewModel.getInstance().getSelectedUser().postValue(null);
    }

    public void contactUserByPhone() {
        String phoneNumber = user.getPhone();
        if (phoneNumber != null && !phoneNumber.isBlank()) {
            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phoneNumber, null));
            startActivity(intent);
        } else {
            showToast(R.string.unknown_phone_number);
        }
    }

    public void contactUserByEmail() {
        String emailAddress = user.getMail();
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:"));
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{emailAddress});
        intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));
        startActivity(intent);
    }

    public void visitWebsite() {
        String websiteUrl = user.getWebsiteUrl();
        if(websiteUrl != null && !websiteUrl.isBlank()) {
            if(!websiteUrl.startsWith("http://") && !websiteUrl.startsWith("https://")) {
                websiteUrl = "http://" + websiteUrl;
            }
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(websiteUrl));
            startActivity(browserIntent);
        } else {
            showToast(R.string.unknown_website);
        }
    }
}