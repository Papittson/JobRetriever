package com.example.jobretriever;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.jobretriever.viewmodels.UserViewModel;


public class CandidateHomeFragment extends Fragment {

    View view;


    public CandidateHomeFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_candidate_home, container, false);
        TextView hello = view.findViewById(R.id.helloCandidate);
        String firstname = UserViewModel.getUser().getValue().getFirstname();
        String name = UserViewModel.getUser().getValue().getName();
        hello.setText("Hello"+ firstname +" "+name);
        return view;
    }
}