package com.example.jobretriever.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jobretriever.R;
import com.example.jobretriever.models.Applicant;
import com.example.jobretriever.models.Offer;
import com.example.jobretriever.models.User;
import com.example.jobretriever.viewmodels.OfferViewModel;
import com.example.jobretriever.viewmodels.UserViewModel;

import java.util.List;
import java.util.stream.Collectors;


public class CandidateProfileFragment extends JRFragment {

    public CandidateProfileFragment() {
        super(R.string.profile, R.layout.fragment_candidate_profile, true);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        checkProfileArgs();
        if (isUserAllowed() && user != null && user instanceof Applicant) {
            Applicant applicant = (Applicant) user;
            TextView nameTextView = fragment.findViewById(R.id.profile_name);
            TextView informationsTextView = fragment.findViewById(R.id.informations);
            TextView experienceTextView = fragment.findViewById(R.id.exp_detailed);
            TextView educationTextView = fragment.findViewById(R.id.edu_detailed);
            ImageButton phoneButton = fragment.findViewById(R.id.contact_phone);
            ImageButton emailButton = fragment.findViewById(R.id.contact_email);

            nameTextView.setText(String.format(getString(R.string.profile_name_candidate), applicant.getFirstname(), applicant.getLastname()));

            informationsTextView.setText(String.format(getString(R.string.profile_infos_candidate), getString(user.getUserType().stringResId), applicant.getNationality(), applicant.getAge()));
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
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (!isUserAllowed()) {
            return;
        }

        createRecyclerView(R.id.saved_offers);

        if (user != null && user.getId().equals(userId) && user instanceof Applicant) {
            Applicant applicant = (Applicant) user;
            TextView savedOffersTitle = fragment.findViewById(R.id.saved_offers_title);
            RecyclerView savedOffersRV = fragment.findViewById(R.id.saved_offers);

            savedOffersTitle.setVisibility(View.VISIBLE);
            savedOffersRV.setVisibility(View.VISIBLE);

            List<Offer> offersLiveData = OfferViewModel.getInstance().getOffers().getValue();
            if (offersLiveData == null || offersLiveData.size() == 0) {
                OfferViewModel.getInstance().getAll();
            }

            OfferViewModel.getInstance().getOffers().observe(
                    getViewLifecycleOwner(),
                    offers -> {
                        List<Offer> favoriteOffers = offers.stream()
                                .filter(offer -> applicant.getFavoritesId().contains(offer.getId()))
                                .collect(Collectors.toList());

                        updateRecyclerView(R.id.saved_offers, favoriteOffers);
                    }
            );
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        OfferViewModel.getInstance().getOffers().removeObservers(getViewLifecycleOwner());
    }

    public void checkProfileArgs() {
        Bundle args = this.getArguments();
        List<User> users = UserViewModel.getInstance().getUsers().getValue();
        if (args != null && users != null && args.getString("userId") != null) {
            String userId = args.getString("userId");
            this.user = users.stream()
                    .filter(user -> user.getId().equals(userId))
                    .findFirst()
                    .orElse(null);
        }
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
}