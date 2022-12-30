package com.example.jobretriever;

import android.app.Application;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.jobretriever.models.Offer;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link candidate_search_results#newInstance} factory method to
 * create an instance of this fragment.
 */
public class candidate_search_results extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public candidate_search_results() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment candidate_search_results.
     */
    // TODO: Rename and change types and number of parameters
    public static candidate_search_results newInstance(String param1, String param2) {
        candidate_search_results fragment = new candidate_search_results();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    View view;
    FirebaseFirestore db;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_search_results, container, false);
        db = FirebaseFirestore.getInstance();
        RecyclerView rV = view.findViewById(R.id.list_search_results);
        List<Offer> offers = new ArrayList<>();

        candidate_search_results_adapter adapter = new candidate_search_results_adapter(offers, this.getActivity().getApplication());

        rV.setAdapter(adapter);
        rV.setLayoutManager(new LinearLayoutManager(view.getContext()));
        return view;
    }
}