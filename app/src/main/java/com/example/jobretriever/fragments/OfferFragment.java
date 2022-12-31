package com.example.jobretriever.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.jobretriever.R;
import com.example.jobretriever.models.Offer;
import com.example.jobretriever.viewmodels.OfferViewModel;

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
        view = inflater.inflate(R.layout.fragment_sign_in, container, false);
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
