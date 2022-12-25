package com.example.jobretriever;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.jobretriever.viewmodels.UserViewModel;


public class SignInFragment extends Fragment {

    View view;
    private UserViewModel userViewModel;

    private static  String ARG_EMAIL = "email";
    private static  String ARG_PWD = "password";

    public SignInFragment() {
        // Required empty public constructor
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_sign_in, container, false);
        Button confirm = (Button) view.findViewById(R.id.confirmSignIn);
        EditText mail = view.findViewById(R.id.signInMail);
        EditText pwd = view.findViewById(R.id.signInPwd);
        ARG_EMAIL = mail.getText().toString();
        ARG_PWD = pwd.getText().toString();

        confirm.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                final Observer<Boolean> usersObserver = loggedIn -> {
                    if(loggedIn){
                        Navigation.findNavController(view).navigate(R.id.action_signInFragment_to_candidateHomeFragment);
                    }else{
                        System.out.println("USER NOT FOUND");
                    }
                };
                userViewModel.signIn(ARG_EMAIL,ARG_PWD).observe(getActivity(),usersObserver);
            }
        });
        return view;
    }
}