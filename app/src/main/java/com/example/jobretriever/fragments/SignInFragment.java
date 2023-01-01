package com.example.jobretriever.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.jobretriever.R;
import com.example.jobretriever.viewmodels.UserViewModel;


public class SignInFragment extends Fragment {
    View view;

    public SignInFragment() {}

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getActivity() != null && getActivity() instanceof AppCompatActivity) {
            ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
            if (actionBar != null) {
                actionBar.setTitle(R.string.action_sign_in);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_sign_in, container, false);
        Button confirm = view.findViewById(R.id.confirmSignIn);
        EditText mail = view.findViewById(R.id.signInMail);
        EditText pwd = view.findViewById(R.id.signInPwd);
        Button signUp = view.findViewById(R.id.signUpButton);

        confirm.setOnClickListener(view -> {
            String email = mail.getText().toString();
            String password = pwd.getText().toString();
            UserViewModel.getInstance().signIn(email, password);
        });

        signUp.setOnClickListener(view -> {
            if (getActivity() != null) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, SignUpFragment.class, null).commit();
            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        UserViewModel.getInstance().getUser().observe(
                getViewLifecycleOwner(),
                user -> {
                    if (user != null && getActivity() != null) {
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, CandidateProfileFragment.class, null).commit();
                    }
                }
        );

        UserViewModel.getInstance().getError().observe(
                getViewLifecycleOwner(),
                errorMessage -> Toast.makeText(getContext(), errorMessage, Toast.LENGTH_LONG).show()
        );
    }

    @Override
    public void onStop() {
        super.onStop();
        UserViewModel.getInstance().getUser().removeObservers(getViewLifecycleOwner());
        UserViewModel.getInstance().getError().removeObservers(getViewLifecycleOwner());
    }
}