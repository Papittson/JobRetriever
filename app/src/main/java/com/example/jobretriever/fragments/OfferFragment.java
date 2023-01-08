package com.example.jobretriever.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.jobretriever.R;
import com.example.jobretriever.models.Offer;
import com.example.jobretriever.models.User;
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

            ImageButton favoriteButton = fragment.findViewById(R.id.offer_favorite);
            ImageButton phoneButton = fragment.findViewById(R.id.contact_phone);
            ImageButton emailButton = fragment.findViewById(R.id.contact_email);
            Button applyButton = fragment.findViewById(R.id.offer_apply);

            favoriteButton.setOnClickListener(v -> toggleFavorite());
            phoneButton.setOnClickListener(v -> contactEmployerByPhone());
            emailButton.setOnClickListener(v -> contactEmployerByEmail());
            applyButton.setOnClickListener(v -> applyToOffer());
        } else {
            goToFragment(HomeFragment.class, null);
            showToast(R.string.error_offer_not_found);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        OfferViewModel.getInstance().getOffer().observe(
                getViewLifecycleOwner(),
                _offer -> showToast(R.string.application_added)
        );
    }

    @Override
    public void onStop() {
        super.onStop();
        OfferViewModel.getInstance().getOffer().removeObservers(getViewLifecycleOwner());
    }

    public boolean offerExists() {
        Bundle args = this.getArguments();
        List<Offer> offers = OfferViewModel.getInstance().getOffers().getValue();
        if(args != null && offers != null && args.getString("offerId") != null) {
            String offerId = args.getString("offerId");
            this.offer = offers.stream()
                    .filter(offer -> offer.getId().equals(offerId))
                    .findFirst()
                    .orElse(null);
            return this.offer != null;
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
        cityCountry.setText(offer.getLocation());
        wage.setText(String.format(getString(R.string.offer_wage), offer.getWage()));
        favoriteButton.setImageResource(favoriteImage);
    }

    public void applyToOffer() {
        if (isUserLoggedIn()) {
            OfferViewModel.getInstance().addApplication(user.getId(), offer);
        } else {
            showToast(R.string.error_must_be_signed_in);
        }
    }

    public void contactEmployerByPhone() {
        User employer = this.offer.getEmployer();
        String phoneNumber = employer.getPhone();
        if(phoneNumber != null) {
            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phoneNumber, null));
            startActivity(intent);
        } else {
            showToast(0); // TODO Mettre message "Numéro de téléphone non renseigné"
        }
    }

    public void contactEmployerByEmail() {
        User employer = this.offer.getEmployer();
        String emailAddress = employer.getMail();
        if(emailAddress != null) {
            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse("mailto:"));
            intent.putExtra(Intent.EXTRA_EMAIL, new String[]{emailAddress});
            intent.putExtra(Intent.EXTRA_SUBJECT, this.offer.getTitle());
            startActivity(intent);
        } else {
            showToast(0); // TODO Mettre message "Adresse e-mail non renseignée"
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
