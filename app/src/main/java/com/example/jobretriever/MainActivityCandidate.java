package com.example.jobretriever;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.widget.Button;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivityCandidate extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_JobRetriever);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_candidate);

        replaceFragment(new candidate_home());

        BottomNavigationView navbar = findViewById(R.id.navbar);



    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fM = getSupportFragmentManager();
        FragmentTransaction fT = fM.beginTransaction();
        fT.replace(R.id.mainScreen, fragment);
        fT.commit();
    }
}