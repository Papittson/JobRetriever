package com.example.jobretriever.fragments;

import static android.content.Context.MODE_PRIVATE;
import static com.example.jobretriever.models.UserType.AGENCY;
import static com.example.jobretriever.models.UserType.APPLICANT;
import static com.example.jobretriever.models.UserType.EMPLOYER;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.annotation.NonNull;

import com.example.jobretriever.R;
import com.example.jobretriever.viewmodels.UserViewModel;


public class SignInFragment extends JRFragment {

    public SignInFragment() {
        super(R.string.action_sign_in, R.layout.fragment_sign_in, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button confirmButton = fragment.findViewById(R.id.confirmSignIn);
        EditText emailEditText = fragment.findViewById(R.id.signInMail);
        EditText passwordEditText = fragment.findViewById(R.id.signInPwd);
        Button signUpButton = fragment.findViewById(R.id.signUpButton);
        CheckBox rememberCheckBox = fragment.findViewById(R.id.rememberMeCheckBox);

        if(getContext() != null) {
            SharedPreferences sh = getContext().getSharedPreferences("JobRetriever", MODE_PRIVATE);
            String email = sh.getString("email", "");
            String password = sh.getString("password", "");
            emailEditText.setText(email);
            passwordEditText.setText(password);
        }

        confirmButton.setOnClickListener(_view -> {
            boolean rememberCheckBoxChecked = rememberCheckBox.isChecked();
            String email = emailEditText.getText().toString();
            String password = passwordEditText.getText().toString();
            String encryptedPassword = encrypt(password);
            UserViewModel.getInstance().signIn(email, encryptedPassword);
            if (rememberCheckBoxChecked && getContext() != null) {
                SharedPreferences sh = getContext().getSharedPreferences("JobRetriever", MODE_PRIVATE);
                SharedPreferences.Editor shEditor = sh.edit();
                shEditor.putString("email", email);
                shEditor.putString("password", password);
                shEditor.apply();
            }
        });

        signUpButton.setOnClickListener(_view -> goToFragment(SignUpFragment.class, null));
    }

    @Override
    public void onStart() {
        super.onStart();
        UserViewModel.getInstance().getUser().observe(
                getViewLifecycleOwner(),
                user -> {
                    if (user != null) {
                        if(user.getType() == APPLICANT) {
                            goToFragment(CandidateProfileFragment.class, null);
                        } else if(user.getType() == EMPLOYER || user.getType() == AGENCY) {
                            goToFragment(EmployerProfileFragment.class, null);
                        } else {
                            goToFragment(HomeFragment.class, null);
                        }
                    }
                }
        );
    }

    @Override
    public void onStop() {
        super.onStop();
        UserViewModel.getInstance().getUser().removeObservers(getViewLifecycleOwner());
    }
}