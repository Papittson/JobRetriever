package com.example.jobretriever.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jobretriever.R;
import com.example.jobretriever.adapters.OffersAdapter;
import com.example.jobretriever.models.Offer;
import com.example.jobretriever.models.User;
import com.example.jobretriever.viewmodels.OfferViewModel;
import com.example.jobretriever.viewmodels.UserViewModel;
import com.google.common.hash.Hashing;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class JRFragment extends Fragment {
    View fragment;
    Integer actionBarTitle;
    Integer fragmentLayout;
    boolean isProtected;
    User user;

    public JRFragment(@StringRes Integer actionBarTitle, @LayoutRes Integer fragmentLayout, boolean isProtected) {
        this.actionBarTitle = actionBarTitle;
        this.fragmentLayout = fragmentLayout;
        this.isProtected = isProtected;
        this.user = UserViewModel.getInstance().getUser().getValue();
    }

    @Override
    @SuppressWarnings("deprecation")
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getActivity() != null && getActivity() instanceof AppCompatActivity) {
            ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
            if (actionBar != null) {
                actionBar.setTitle(actionBarTitle);
            }
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragment = inflater.inflate(fragmentLayout, container, false);
        return fragment;
    }

    @Override
    public void onStart() {
        super.onStart();
        if(!isUserAllowed()) {
            goToFragment(SignInFragment.class, null);
            showToast(0); // TODO Modifier et mettre un msg d'erreur
        }
        OfferViewModel.getInstance().getError().observe(
                getViewLifecycleOwner(),
                this::showToast
        );
    }

    @Override
    public void onStop() {
        super.onStop();
        OfferViewModel.getInstance().getError().removeObservers(getViewLifecycleOwner());
    }

    public void showToast(@StringRes int stringResId) {
        Toast.makeText(getContext(), stringResId, Toast.LENGTH_LONG).show();
    }

    public void createRecyclerView(@IdRes int recyclerViewId) {
        RecyclerView recyclerView = fragment.findViewById(recyclerViewId);
        OffersAdapter adapter = new OffersAdapter(getContext(), getActivity(), new ArrayList<>());
        recyclerView.setLayoutManager(
                new LinearLayoutManager(
                        getContext(),
                        LinearLayoutManager.VERTICAL,
                        false
                )
        );
        recyclerView.setAdapter(adapter);
    }

    public void updateRecyclerView(@IdRes int recyclerViewId, List<Offer> items) {
        RecyclerView recyclerView = fragment.findViewById(recyclerViewId);
        OffersAdapter adapter = new OffersAdapter(getContext(), getActivity(), items);
        recyclerView.setAdapter(adapter);
    }

    public boolean isUserAllowed() {
        return !isProtected || user != null;
    }

    public boolean isUserLoggedIn() {
        return user != null;
    }

    public String encrypt(String password) {
        return Hashing.sha256().hashString(password, StandardCharsets.UTF_8).toString();
    }

    public void goToFragment(Class<? extends Fragment> fragmentClass, Bundle args) {
        if(getActivity() != null) {
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragmentClass, args)
                    .commit();
        }
    }
}
