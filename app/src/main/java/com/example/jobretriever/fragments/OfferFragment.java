package com.example.jobretriever.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.jobretriever.R;
import com.example.jobretriever.models.Offer;
import com.example.jobretriever.models.User;
import com.example.jobretriever.repositories.UserRepository;
import com.example.jobretriever.viewmodels.OfferViewModel;
import com.example.jobretriever.viewmodels.UserViewModel;

import java.util.List;


public class OfferFragment extends Fragment {
    View view;

    public OfferFragment() {
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getActivity() != null && getActivity() instanceof AppCompatActivity) {
            ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
            if (actionBar != null) {
                actionBar.setTitle(R.string.welcome_message);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_offer, container, false);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        Bundle args = this.getArguments();
        List<Offer> offers = OfferViewModel.getInstance().getOffers().getValue();

        if(args != null && offers != null && args.getInt("offerIndex", -1) != -1) {
            int index = args.getInt("offerIndex");
            Offer offer = offers.get(index);

            TextView jobTitleTV = view.findViewById(R.id.offer_job_title);
            TextView businessNameTV = view.findViewById(R.id.offer_busines_name);
            TextView durationTV = view.findViewById(R.id.offer_duration);
            TextView descriptionTV = view.findViewById(R.id.offer_description);
            TextView cityCountry = view.findViewById(R.id.offer_location);
            TextView wage = view.findViewById(R.id.offer_wage);
            ImageButton favoriteButton = view.findViewById(R.id.offer_favorite);

            jobTitleTV.setText(offer.getTitle());
            businessNameTV.setText(offer.getEmployer().getBusinessName());
            durationTV.setText(offer.getDuration());
            descriptionTV.setText(offer.getDescription());
            cityCountry.setText(String.format(getString(R.string.offer_location), offer.getLocation().getName(), offer.getLocation().getCountry()));
            wage.setText(String.format(getString(R.string.offer_wage), offer.getWage()));

            if(UserViewModel.getInstance().hasFavorite(offer.getId())) {
                favoriteButton.setImageResource(R.drawable.ic_baseline_favorite_24);
            } else {
                favoriteButton.setImageResource(R.drawable.ic_baseline_favorite_border_24);
            }

            favoriteButton.setOnClickListener(v -> {
                if (UserViewModel.getInstance().isLoggedIn()) {
                    if(UserViewModel.getInstance().hasFavorite(offer.getId())) {
                        UserViewModel.getInstance().removeFavorite(offer.getId());
                        favoriteButton.setImageResource(R.drawable.ic_baseline_favorite_border_24);
                    } else {
                        UserViewModel.getInstance().addFavorite(offer.getId());
                        favoriteButton.setImageResource(R.drawable.ic_baseline_favorite_24);
                    }
                } else {
                    if(getActivity() != null) {
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, SignInFragment.class, null).commit();
                    }
                    Toast.makeText(getContext(), "TODO Remplacer par msg d'erreur", Toast.LENGTH_LONG).show();
                }
            });
        } else {
            if(getActivity() != null) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, HomeFragment.class, null).commit();
            }
            Toast.makeText(getContext(), "TODO Remplacer par msg d'erreur", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
    }
}
