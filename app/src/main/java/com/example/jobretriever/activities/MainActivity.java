package com.example.jobretriever.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.example.jobretriever.R;
import com.example.jobretriever.viewmodels.OfferViewModel;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
        );

        setContentView(R.layout.activity_main);
        OfferViewModel.getInstance().getAll(null);

        new Handler().postDelayed((Runnable) () -> {
            Intent i = new Intent(MainActivity.this, AppActivity.class);
            startActivity(i);
            finish();
        }, 2000);
    }
}