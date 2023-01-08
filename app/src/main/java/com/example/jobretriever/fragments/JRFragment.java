package com.example.jobretriever.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;
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

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class JRFragment extends Fragment {
    View fragment;
    Integer actionBarTitle;
    Integer fragmentLayout;
    boolean isProtected;
    User user;
    String userId;
    List<String> cities;

    public JRFragment(@StringRes Integer actionBarTitle, @LayoutRes Integer fragmentLayout, boolean isProtected) {
        this.actionBarTitle = actionBarTitle;
        this.fragmentLayout = fragmentLayout;
        this.isProtected = isProtected;
        this.user = UserViewModel.getInstance().getUser().getValue();
        if(user != null) {
            this.userId = user.getId();
        }
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.cities = retrieveCities();
        fragment = inflater.inflate(fragmentLayout, container, false);
        return fragment;
    }

    @Override
    public void onStart() {
        super.onStart();
        if(!isUserAllowed()) {
            goToFragment(SignInFragment.class, null);
            showToast(R.string.error_must_be_signed_in);
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

    public String loadJSONFromAssets() {
        String json = null;
        System.out.println("TA 1");
        if(getActivity() != null) {
            System.out.println("TA 2");
            try {
                InputStream is = getActivity().getAssets().open("cities.json");
                int size = is.available();
                byte[] buffer = new byte[size];
                @SuppressWarnings("unused")
                int i = is.read(buffer);
                is.close();
                System.out.println("TA 3");
                json = new String(buffer, StandardCharsets.UTF_8);
            } catch (IOException ex) {
                System.out.println("TA 4");
                ex.printStackTrace();
            }
        }
        System.out.println("TA 5");
        return json;
    }

    private List<String> retrieveCities() {
        List<String> cities = new ArrayList<>();
        String json = loadJSONFromAssets();
        System.out.println("TEST " + json);
        try {
            JSONArray array = new JSONArray(json);
            for (int i = 0; i < array.length(); i++) {
                cities.add(array.getString(i));
            }
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
        return cities;
    }

    public List<String> getCities() {
        return cities;
    }

    public List<String> getCountries() {
        Locale[] locales = Locale.getAvailableLocales();
        ArrayList<String> countries = new ArrayList<>();
        for (Locale locale : locales) {
            String country = locale.getDisplayCountry();
            if (country.trim().length() > 0 && !countries.contains(country)) {
                countries.add(country);
            }
        }
        Collections.sort(countries);
        return countries;
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
