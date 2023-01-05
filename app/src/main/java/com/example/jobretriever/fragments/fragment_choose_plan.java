package com.example.jobretriever.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.jobretriever.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link fragment_choose_plan#newInstance} factory method to
 * create an instance of this fragment.
 */
public class fragment_choose_plan extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public fragment_choose_plan() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment fragment_choose_plan.
     */
    // TODO: Rename and change types and number of parameters
    public static fragment_choose_plan newInstance(String param1, String param2) {
        fragment_choose_plan fragment = new fragment_choose_plan();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_choose_plan, container, false);
        /*Button confirm = view.findViewById(R.id.confirmPlan);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goToCandidateHome = new Intent(getActivity(), MainActivityCandidate.class);
                goToCandidateHome.putExtra("loadFragment", 0);
                startActivity(goToCandidateHome);
            }
        });*/
        return view;
    }
}