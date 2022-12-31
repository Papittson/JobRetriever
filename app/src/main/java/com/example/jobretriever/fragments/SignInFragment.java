package com.example.jobretriever.fragments;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.jobretriever.R;
import com.example.jobretriever.models.User;
import com.example.jobretriever.viewmodels.UserViewModel;


public class SignInFragment extends Fragment {

    private static String ARG_EMAIL = "email";
    private static String ARG_PWD = "password";
    View view;
    private UserViewModel userViewModel;

    public SignInFragment() {
        // Required empty public constructor
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_sign_in, container, false);
        Button confirm = view.findViewById(R.id.confirmSignIn);
        EditText mail = view.findViewById(R.id.signInMail);
        EditText pwd = view.findViewById(R.id.signInPwd);
        Button signUp = view.findViewById(R.id.signUpButton);

        final Observer<User> getUserObserver = user -> {
            if (user != null) {
                UserViewModel.getUser().removeObservers(getViewLifecycleOwner());
                if (getActivity() != null) {
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, CandidateProfileFragment.class, null).commit();
                }
            } else {
                Context context = getContext();
                CharSequence text = UserViewModel.getErrorMessage().getValue();
                int duration = Toast.LENGTH_LONG;
                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }
        };
        UserViewModel.getUser().observe(getViewLifecycleOwner(), getUserObserver);
        confirm.setOnClickListener(view -> {
            ARG_EMAIL = mail.getText().toString();
            ARG_PWD = pwd.getText().toString();
            userViewModel.signIn(ARG_EMAIL, ARG_PWD);
        });
        signUp.setOnClickListener(view -> {
            if (getActivity() != null) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, SignUpFragment.class, null).commit();
            }
        });
        return view;
    }
}