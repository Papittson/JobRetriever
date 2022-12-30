package com.example.jobretriever;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jobretriever.adapters.SearchOffersAdapter;
import com.example.jobretriever.models.Offer;
import com.example.jobretriever.viewmodels.OfferViewModel;

import java.util.ArrayList;
import java.util.List;


public class WelcomeFragment extends Fragment {

    View view;

    public WelcomeFragment() {
        // Required empty public constructor
    }
@Override
public void onActivityCreated(Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    if(getActivity() != null && getActivity() instanceof AppCompatActivity) {
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if(actionBar != null) {
            actionBar.setTitle(R.string.welcome_message);
        }
    }
}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_welcome, container, false);

        /* OFFERS */
        RecyclerView offersRV = view.findViewById(R.id.welcomeOfferRV);
        ArrayList<Offer> offers = new ArrayList<>();
        SearchOffersAdapter adapter = new SearchOffersAdapter(getContext(),offers) ;
        final Observer<List<Offer>> listOfferObserver = offerList -> {
            //offers.clear();
            offers.addAll(offerList);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
            offersRV.setLayoutManager(linearLayoutManager);
            offersRV.setAdapter(adapter);
            OfferViewModel.getInstance().getData().removeObservers(getViewLifecycleOwner());
        };
        OfferViewModel.getInstance().getAll().observe(getViewLifecycleOwner(),listOfferObserver);




        Button confirm = (Button) view.findViewById(R.id.confirmSearch);

        // Inflate the layout for this fragment
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        return view;
    }
}