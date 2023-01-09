package com.example.jobretriever.fragments;

import static com.example.jobretriever.enums.UserType.APPLICANT;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.jobretriever.R;
import com.example.jobretriever.models.Applicant;
import com.example.jobretriever.models.Employer;
import com.example.jobretriever.models.Offer;
import com.example.jobretriever.models.User;
import com.example.jobretriever.viewmodels.OfferViewModel;
import com.example.jobretriever.viewmodels.UserViewModel;


public class OfferFragment extends JRFragment {
    Offer offer;

    public OfferFragment() {
        super(R.string.details, R.layout.fragment_offer, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        OfferViewModel.getInstance().getSelectedOffer().observe(
                this,
                _offer -> {
                    if(_offer == null) {
                        return;
                    }
                    this.offer = _offer;
                    OfferViewModel.getInstance().getSelectedOffer().removeObservers(this);
                    OfferViewModel.getInstance().getSelectedOffer().postValue(null);
                    fillLayoutData();

                    ImageButton favoriteButton = fragment.findViewById(R.id.offer_favorite);
                    ImageButton phoneButton = fragment.findViewById(R.id.contact_phone);
                    ImageButton emailButton = fragment.findViewById(R.id.contact_email);
                    ImageButton profileButton = fragment.findViewById(R.id.view_profile);
                    Button applyButton = fragment.findViewById(R.id.offer_apply);

                    if(!isUserLoggedIn() || authUser.getUserType() == APPLICANT) {
                        applyButton.setVisibility(View.VISIBLE);
                    }

                    favoriteButton.setOnClickListener(v -> toggleFavorite());
                    phoneButton.setOnClickListener(v -> contactEmployerByPhone());
                    emailButton.setOnClickListener(v -> contactEmployerByEmail());
                    profileButton.setOnClickListener(v -> viewEmployerProfile());
                    applyButton.setOnClickListener(v -> applyToOffer());
                }
        );

        OfferViewModel.getInstance().getAppliedOffer().observe(
                this,
                _offer -> {
                    if(_offer != null) {
                        Button applyButton = fragment.findViewById(R.id.offer_apply);
                        applyButton.setVisibility(View.GONE);
                        showToast(R.string.application_added);
                        OfferViewModel.getInstance().getAppliedOffer().removeObservers(this);
                        OfferViewModel.getInstance().getAppliedOffer().postValue(null);
                    }
                }
        );
    }

    @Override
    public void onStop() {
        super.onStop();
        OfferViewModel.getInstance().getSelectedOffer().removeObservers(this);
        OfferViewModel.getInstance().getAppliedOffer().removeObservers(this);
    }

    public void fillLayoutData() {
        TextView jobTitleTV = fragment.findViewById(R.id.offer_job_title);
        TextView businessNameTV = fragment.findViewById(R.id.offer_busines_name);
        TextView durationTV = fragment.findViewById(R.id.offer_duration);
        TextView descriptionTV = fragment.findViewById(R.id.offer_description);
        TextView cityCountry = fragment.findViewById(R.id.offer_location);
        TextView wage = fragment.findViewById(R.id.offer_wage);
        ImageButton favoriteButton = fragment.findViewById(R.id.offer_favorite);

        if(this.authUser instanceof Employer) {
            favoriteButton.setVisibility(View.GONE);
        } else if(this.authUser instanceof Applicant && ((Applicant) this.authUser).hasFavorite(offer.getId())) {
            favoriteButton.setImageResource(R.drawable.ic_baseline_favorite_24);
        } else {
            favoriteButton.setImageResource(R.drawable.ic_baseline_favorite_border_24);
        }

        jobTitleTV.setText(offer.getTitle());
        businessNameTV.setText(offer.getEmployer().getBusinessName());
        durationTV.setText(getString(R.string.profile_infos_employer, getString(offer.getDuration().stringResId), offer.getDate()));
        descriptionTV.setText(offer.getDescription());
        cityCountry.setText(offer.getLocation());
        wage.setText(getString(R.string.offer_wage, offer.getWage()));
    }

    public void applyToOffer() {
        if (isUserLoggedIn()) {
            if(authUser instanceof Applicant) {
                OfferViewModel.getInstance().addApplication((Applicant) authUser, offer);
            }
        } else {
            showToast(R.string.error_must_be_signed_in);
        }
    }

    public void viewEmployerProfile() {
        User employer = this.offer.getEmployer();
        UserViewModel.getInstance().getSelectedUser().postValue(employer);
        goToFragment(EmployerProfileFragment.class);
    }

    public void contactEmployerByPhone() {
        User employer = this.offer.getEmployer();
        String phoneNumber = employer.getPhone();
        if(phoneNumber != null && !phoneNumber.isBlank()) {
            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phoneNumber, null));
            startActivity(intent);
        } else {
            showToast(R.string.unknown_phone_number);
        }
    }

    public void contactEmployerByEmail() {
        User employer = this.offer.getEmployer();
        String emailAddress = employer.getMail();
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:"));
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{emailAddress});
        intent.putExtra(Intent.EXTRA_SUBJECT, this.offer.getTitle());
        startActivity(intent);
    }

    public void toggleFavorite() {
        ImageButton favoriteButton = fragment.findViewById(R.id.offer_favorite);
        if (isUserLoggedIn()) {
            if(this.authUser instanceof Applicant) {
                Applicant applicant = (Applicant) this.authUser;
                if(applicant.hasFavorite(offer.getId())) {
                    UserViewModel.getInstance().removeFavorite(offer.getId());
                    favoriteButton.setImageResource(R.drawable.ic_baseline_favorite_border_24);
                } else {
                    UserViewModel.getInstance().addFavorite(offer.getId());
                    favoriteButton.setImageResource(R.drawable.ic_baseline_favorite_24);
                }
            }
        } else {
            showToast(R.string.error_must_be_signed_in);
        }
    }
}
