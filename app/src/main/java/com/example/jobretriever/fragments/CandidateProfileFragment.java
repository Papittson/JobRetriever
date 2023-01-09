package com.example.jobretriever.fragments;

import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.jobretriever.R;
import com.example.jobretriever.models.Applicant;
import com.example.jobretriever.viewmodels.OfferViewModel;
import com.example.jobretriever.viewmodels.UserViewModel;


public class CandidateProfileFragment extends ProfileFragment {

    public CandidateProfileFragment() {
        super(R.layout.fragment_candidate_profile);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (!isUserAllowed()) {
            return;
        }

        createRecyclerView(R.id.saved_offers);

        UserViewModel.getInstance().getSelectedUser().observe(
                this,
                _user -> {
                    if(_user == null) {
                        return;
                    }
                    this.user = _user;
                    System.out.println("TEST 1 " + this.user.getMail());
                    UserViewModel.getInstance().getSelectedUser().removeObservers(this);
                    UserViewModel.getInstance().getSelectedUser().postValue(null);
                    if(this.user instanceof Applicant) {
                        Applicant applicant = (Applicant) this.user;
                        TextView nameTextView = fragment.findViewById(R.id.profile_name);
                        TextView informationsTextView = fragment.findViewById(R.id.informations);
                        TextView experienceTextView = fragment.findViewById(R.id.exp_detailed);
                        TextView educationTextView = fragment.findViewById(R.id.edu_detailed);
                        ImageButton phoneButton = fragment.findViewById(R.id.contact_phone);
                        ImageButton emailButton = fragment.findViewById(R.id.contact_email);
                        ImageButton websiteButton = fragment.findViewById(R.id.visit_website);

                        nameTextView.setText(getString(R.string.profile_name_candidate, applicant.getFirstname(), applicant.getLastname()));
                        informationsTextView.setText(getString(R.string.profile_infos_candidate, getString(user.getUserType().stringResId), applicant.getNationality(), applicant.getAge()));

                        if(applicant.getExperiences() == null || applicant.getExperiences().isBlank()){
                            experienceTextView.setText(getText(R.string.no_experiences));
                        }else{
                            experienceTextView.setText(applicant.getExperiences());
                        }
                        if(applicant.getEducations() == null || applicant.getEducations().isBlank()){
                            educationTextView.setText(getText(R.string.no_educations));
                        }else{
                            educationTextView.setText(applicant.getEducations());
                        }

                        phoneButton.setOnClickListener(v -> contactUserByPhone());
                        emailButton.setOnClickListener(v -> contactUserByEmail());
                        websiteButton.setOnClickListener(v -> visitWebsite());

                        if(this.authUser.getId().equals(this.user.getId())) {
                            TextView savedOffersTitle = fragment.findViewById(R.id.saved_offers_title);
                            RecyclerView savedOffersRV = fragment.findViewById(R.id.saved_offers);

                            savedOffersTitle.setVisibility(View.VISIBLE);
                            savedOffersRV.setVisibility(View.VISIBLE);

                            OfferViewModel.getInstance().getFavorites(applicant.getFavoritesId());
                            OfferViewModel.getInstance().getSavedOffers().observe(
                                    this,
                                    offers -> {
                                        if(offers != null) {
                                            updateRecyclerView(R.id.saved_offers, offers);
                                            OfferViewModel.getInstance().getSavedOffers().removeObservers(this);
                                            OfferViewModel.getInstance().getSavedOffers().postValue(null);
                                        }
                                    }
                            );
                        }
                    } else {
                        goToFragment(HomeFragment.class);
                        showToast(R.string.error_user_not_found);
                    }
                }
        );
    }

    @Override
    public void onStop() {
        super.onStop();
        OfferViewModel.getInstance().getSavedOffers().removeObservers(this);
        OfferViewModel.getInstance().getSavedOffers().postValue(null);
    }
}