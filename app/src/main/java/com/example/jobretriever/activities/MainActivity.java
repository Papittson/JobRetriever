package com.example.jobretriever.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.example.jobretriever.R;
import com.example.jobretriever.viewmodels.OfferViewModel;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    @SuppressLint("StaticFieldLeak")
    private static Context context;
    private static List<String> cities;
    private static List<String> countries;
    public static DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;

        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
        );

        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(false)
                .build();
        FirebaseFirestore.getInstance().setFirestoreSettings(settings);
        FirebaseFirestore.setLoggingEnabled(false);

        setContentView(R.layout.activity_main);
        OfferViewModel.getInstance().getAll(25);

        cities = retrieveCities();
        countries = retrieveCountries();

        new Handler().postDelayed(() -> {
            Intent i = new Intent(MainActivity.this, AppActivity.class);
            startActivity(i);
            finish();
        }, 1500);
    }

    public static Context getContext() {
        return context;
    }

    private List<String> retrieveCities() {
        List<String> cities = new ArrayList<>();
        String json = loadJSONFromAssets();
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

    public List<String> retrieveCountries() {
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

    public String loadJSONFromAssets() {
        String json = null;
        try {
            InputStream is = getAssets().open("cities.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            @SuppressWarnings("unused")
            int i = is.read(buffer);
            is.close();
            json = new String(buffer, StandardCharsets.UTF_8);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return json;
    }

    public static List<String> getCities() {
        return cities;
    }

    public static List<String> getCountries() {
        return countries;
    }
}