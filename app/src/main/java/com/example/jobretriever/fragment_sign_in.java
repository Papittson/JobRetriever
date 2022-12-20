package com.example.jobretriever;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link fragment_sign_in#newInstance} factory method to
 * create an instance of this fragment.
 */
public class fragment_sign_in extends Fragment {

    View view;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_EMAIL = "email";
    private static final String ARG_PWD = "password";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public fragment_sign_in() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment fragment_sign_in.
     */
    // TODO: Rename and change types and number of parameters
    public static fragment_sign_in newInstance(String param1, String param2) {
        fragment_sign_in fragment = new fragment_sign_in();
        Bundle args = new Bundle(2);
        args.putString(ARG_EMAIL, param1);
        args.putString(ARG_PWD, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_EMAIL);
            mParam2 = getArguments().getString(ARG_PWD);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_sign_in, container, false);
        Button confirm = (Button) view.findViewById(R.id.confirmSignIn);
        // Inflate the layout for this fragment
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goToCandidateHome = new Intent(getActivity(), MainActivityCandidate.class);
                startActivity(goToCandidateHome);
            }
        });
        return view;

    }
}