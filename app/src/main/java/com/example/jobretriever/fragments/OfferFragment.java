package com.example.jobretriever.fragments;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.jobretriever.R;
import com.example.jobretriever.models.Offer;
import com.example.jobretriever.viewmodels.OfferViewModel;
import com.example.jobretriever.viewmodels.UserViewModel;

import java.util.List;


public class OfferFragment extends JRFragment {
    Offer offer;

    public OfferFragment() {
        super(R.string.details, R.layout.fragment_offer, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(offerExists()) {
            fillLayoutData();

            ImageButton favoriteButton = view.findViewById(R.id.offer_favorite);
            Button applyButton = view.findViewById(R.id.offer_apply);

            applyButton.setOnClickListener(v -> applyToOffer());
            favoriteButton.setOnClickListener(v -> toggleFavorite());
        } else {
            goToFragment(HomeFragment.class, null);
            showToast(R.string.error_offer_not_found);
        }
    }

    public boolean offerExists() {
        Bundle args = this.getArguments();
        List<Offer> offers = OfferViewModel.getInstance().getOffers().getValue();
        if(args != null && offers != null && args.getInt("offerIndex", -1) != -1) {
            int index = args.getInt("offerIndex");
            offer = offers.get(index);
            return true;
        }
        return false;
    }

    public void fillLayoutData() {
        TextView jobTitleTV = fragment.findViewById(R.id.offer_job_title);
        TextView businessNameTV = fragment.findViewById(R.id.offer_busines_name);
        TextView durationTV = fragment.findViewById(R.id.offer_duration);
        TextView descriptionTV = fragment.findViewById(R.id.offer_description);
        TextView cityCountry = fragment.findViewById(R.id.offer_location);
        TextView wage = fragment.findViewById(R.id.offer_wage);
        ImageButton favoriteButton = fragment.findViewById(R.id.offer_favorite);
        int favoriteImage = UserViewModel.getInstance().hasFavorite(offer.getId()) ?
                R.drawable.ic_baseline_favorite_24 :
                R.drawable.ic_baseline_favorite_border_24;

        jobTitleTV.setText(offer.getTitle());
        businessNameTV.setText(offer.getEmployer().getBusinessName());
        durationTV.setText(offer.getDuration());
        descriptionTV.setText(offer.getDescription());
        cityCountry.setText(String.format(getString(R.string.offer_location), offer.getLocation().getName(), offer.getLocation().getCountry()));
        wage.setText(String.format(getString(R.string.offer_wage), offer.getWage()));
        favoriteButton.setImageResource(favoriteImage);
    }

    public void applyToOffer() {
        if (isUserLoggedIn()) {
            goToFragment(ApplyFragment.class, null);
        }else {
            showToast(R.string.error_must_be_signed_in);
        }
    }

    public void toggleFavorite() {
        ImageButton favoriteButton = fragment.findViewById(R.id.offer_favorite);
        if (isUserLoggedIn()) {
            if(UserViewModel.getInstance().hasFavorite(offer.getId())) {
                UserViewModel.getInstance().removeFavorite(offer.getId());
                favoriteButton.setImageResource(R.drawable.ic_baseline_favorite_border_24);
            } else {
                UserViewModel.getInstance().addFavorite(offer.getId());
                favoriteButton.setImageResource(R.drawable.ic_baseline_favorite_24);
            }
        } else {
            showToast(R.string.error_must_be_signed_in);
        }
    }
}
