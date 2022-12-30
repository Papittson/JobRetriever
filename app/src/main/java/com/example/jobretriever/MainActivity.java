package com.example.jobretriever;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        getSupportActionBar().setHomeAsUpIndicator(R.drawable.logo_24);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


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
        switch (item.getItemId()) {
            case R.id.action_sign_in:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, SignInFragment.class, null).commit();
                return (true);
            default:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, WelcomeFragment.class, null).commit();
                return (true);

        }
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.navbarHome:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, WelcomeFragment.class, null).commit();
                return true;
            case R.id.navbarAlerts:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, SignInFragment.class, null).commit();
                return true;
            case R.id.navbarSearch:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, WelcomeFragment.class, null).commit();
                return true;
            case R.id.navbarProfile:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, SignInFragment.class, null).commit();
                return true;
            default:
                return false;
        }
    }


}