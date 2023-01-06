package com.example.jobretriever.fragments;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.jobretriever.R;
import com.example.jobretriever.viewmodels.UserViewModel;
import com.google.common.hash.Hashing;

import java.nio.charset.StandardCharsets;


public class SignInFragment extends Fragment {
    View view;

    public SignInFragment() {
    }

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
        Button confirmButton = view.findViewById(R.id.confirmSignIn);
        EditText emailEditText = view.findViewById(R.id.signInMail);
        EditText passwordEditText = view.findViewById(R.id.signInPwd);
        Button signUpButton = view.findViewById(R.id.signUpButton);
        CheckBox rememberCheckBox = view.findViewById(R.id.rememberMeCheckBox);

        if(getContext() != null) {
            SharedPreferences sh = getContext().getSharedPreferences("JobRetriever", MODE_PRIVATE);
            String email = sh.getString("email", "");
            String password = sh.getString("password", "");
            emailEditText.setText(email);
            passwordEditText.setText(password);
        }

        confirmButton.setOnClickListener(view -> {
            boolean rememberCheckBoxChecked = rememberCheckBox.isChecked();
            String email = emailEditText.getText().toString();
            String password = passwordEditText.getText().toString();
            String encryptedPassword = Hashing.sha256()
                    .hashString(password, StandardCharsets.UTF_8)
                    .toString();
            UserViewModel.getInstance().signIn(email, encryptedPassword);
            if (rememberCheckBoxChecked && getContext() != null) {
                SharedPreferences sh = getContext().getSharedPreferences("JobRetriever", MODE_PRIVATE);
                SharedPreferences.Editor shEditor = sh.edit();
                shEditor.putString("email", email);
                shEditor.putString("password", password);
                shEditor.apply();
            }
        });

        signUpButton.setOnClickListener(view -> {
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
                errorMessage -> Toast.makeText(getContext(), getString(errorMessage), Toast.LENGTH_LONG).show()
        );
    }

    @Override
    public void onStop() {
        super.onStop();
        UserViewModel.getInstance().getUser().removeObservers(getViewLifecycleOwner());
        UserViewModel.getInstance().getError().removeObservers(getViewLifecycleOwner());
    }
}