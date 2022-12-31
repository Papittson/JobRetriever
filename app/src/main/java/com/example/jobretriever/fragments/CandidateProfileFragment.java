package com.example.jobretriever.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.jobretriever.R;
import com.example.jobretriever.viewmodels.UserViewModel;


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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        switch (UserViewModel.getUser().getValue().getType()){
            case APPLICANT:
                view = inflater.inflate(R.layout.fragment_candidate_home, container, false);
                TextView hello = view.findViewById(R.id.helloCandidate);
                String firstname = UserViewModel.getUser().getValue().getFirstname();
                String name = UserViewModel.getUser().getValue().getName();
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
}