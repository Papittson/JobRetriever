package com.example.jobretriever.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jobretriever.R;
import com.example.jobretriever.adapters.OffersAdapter;
import com.example.jobretriever.models.Offer;
import com.example.jobretriever.models.User;
import com.example.jobretriever.viewmodels.OfferViewModel;
import com.example.jobretriever.viewmodels.UserViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class CandidateProfileFragment extends Fragment {
    View view;
    
    public CandidateProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        switch (UserViewModel.getInstance().getUser().getValue().getType()){
            case APPLICANT:
                view = inflater.inflate(R.layout.fragment_candidate_home, container, false);
                TextView hello = view.findViewById(R.id.helloCandidate);
                String firstname = UserViewModel.getInstance().getUser().getValue().getFirstname();
                String name = UserViewModel.getInstance().getUser().getValue().getName();
                String text = String.format(getResources().getString(R.string.hello_candidate_fragment), firstname, name);
                hello.setText(text);
                break;
            case EMPLOYER:
                view = inflater.inflate(R.layout.fragment_candidate_home, container, false);
                break;
            case AGENCY:
                view = inflater.inflate(R.layout.fragment_candidate_home, container, false);
                break;
            case MODERATOR:
                view = inflater.inflate(R.layout.fragment_candidate_home, container, false);
                break;
        }
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        RecyclerView recyclerView = view.findViewById(R.id.saved_offers);
        ArrayList<Offer> offers = new ArrayList<>();
        OffersAdapter adapter = new OffersAdapter(getContext(), getActivity(), offers);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);

        User user = UserViewModel.getInstance().getUser().getValue();

        if(user == null) {
            goToFragment(SignInFragment.class);
            return;
        }

        List<Offer> offersLiveData = OfferViewModel.getInstance().getOffers().getValue();
        if(offersLiveData == null || offersLiveData.size() == 0) {
            OfferViewModel.getInstance().getAll(null);
        }

        OfferViewModel.getInstance().getOffers().observe(
                getViewLifecycleOwner(),
                offerList -> {
                    offers.clear();
                    List<Offer> favoriteOffers = offerList.stream()
                            .filter(offer -> user.getFavoritesId().contains(offer.getId()))
                            .collect(Collectors.toList());
                    offers.addAll(favoriteOffers);
                    recyclerView.setAdapter(adapter);
                }
        );

        OfferViewModel.getInstance().getError().observe(
                getViewLifecycleOwner(),
                errorMessage -> Toast.makeText(getContext(),getString(errorMessage) , Toast.LENGTH_LONG).show()
        );
    }

    public void goToFragment(Class<? extends Fragment> fragmentClass) {
        if(getActivity() != null) {
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragmentClass, null).commit();
        }
    }
}