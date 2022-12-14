package com.example.jobretriever.activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.jobretriever.R;
import com.example.jobretriever.fragments.ApplicationsFragment;
import com.example.jobretriever.fragments.CandidateProfileFragment;
import com.example.jobretriever.fragments.EmployerProfileFragment;
import com.example.jobretriever.fragments.HomeFragment;
import com.example.jobretriever.fragments.SignInFragment;
import com.example.jobretriever.models.Applicant;
import com.example.jobretriever.models.User;
import com.example.jobretriever.viewmodels.UserViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

@SuppressWarnings("deprecation")
public class AppActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    BottomNavigationView navBar;

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

        navBar = findViewById(R.id.nav_bar);
        navBar.setOnNavigationItemSelectedListener(this);
        navBar.setSelectedItemId(R.id.nav_item_home);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_bar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_sign_in) {
            if(UserViewModel.getInstance().isLoggedIn()) {
                navBar.setSelectedItemId(R.id.nav_item_home);
                UserViewModel.getInstance().disconnectUser();
                Toast.makeText(this, getString(R.string.disconnected), Toast.LENGTH_SHORT).show();
            } else {
                navBar.setSelectedItemId(R.id.nav_item_profile);
            }
        } else {
            navBar.setSelectedItemId(R.id.nav_item_home);
        }
        return true;
    }

    @Override
    @SuppressLint("NonConstantResourceId")
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_item_home:
                goToFragment(HomeFragment.class);
                return true;
            case R.id.nav_item_applications:
                if(UserViewModel.getInstance().isLoggedIn()) {
                    goToFragment(ApplicationsFragment.class);
                    return true;
                } else {
                    navBar.setSelectedItemId(R.id.nav_item_profile);
                    return false;
                }
            case R.id.nav_item_profile:
                User authUser = UserViewModel.getInstance().getAuthUser().getValue();
                if(authUser != null) {
                    UserViewModel.getInstance().getSelectedUser().postValue(authUser);
                    if(authUser instanceof Applicant) {
                        goToFragment(CandidateProfileFragment.class);
                    } else {
                        goToFragment(EmployerProfileFragment.class);
                    }
                } else {
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