package com.example.jobretriever.activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.jobretriever.R;
import com.example.jobretriever.fragments.CandidateProfileFragment;
import com.example.jobretriever.fragments.HomeFragment;
import com.example.jobretriever.fragments.SignInFragment;
import com.example.jobretriever.viewmodels.UserViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class AppActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app);

        ActionBar actionBar = getSupportActionBar();

        if(actionBar != null) {
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.logo_icon);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(true);
        }

        BottomNavigationView navBar = findViewById(R.id.navBar);
        navBar.setOnNavigationItemSelectedListener(this);
        navBar.setSelectedItemId(R.id.navbarHome);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_bar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_sign_in) {
            goToFragment(SignInFragment.class);
        } else {
            goToFragment(HomeFragment.class);
        }
        return true;
    }

    @Override
    @SuppressLint("NonConstantResourceId")
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.navbarHome:
                goToFragment(HomeFragment.class);
                return true;
            case R.id.navbarAlerts:
                goToFragment(SignInFragment.class);
                return true;
            case R.id.navbarSearch:
            case R.id.navbarProfile:
                if(UserViewModel.getInstance().isLoggedIn()){
                    goToFragment(CandidateProfileFragment.class);
                }else{
                    goToFragment(SignInFragment.class);
                }
                return true;
            default:
                return false;
        }
    }

    public void goToFragment(Class<? extends Fragment> fragmentClass) {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragmentClass, null).commit();
    }
}