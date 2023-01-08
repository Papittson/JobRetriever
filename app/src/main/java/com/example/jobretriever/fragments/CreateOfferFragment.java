package com.example.jobretriever.fragments;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.example.jobretriever.R;
import com.example.jobretriever.models.Offer;
import com.example.jobretriever.viewmodels.OfferViewModel;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class CreateOfferFragment extends JRFragment {
    String offerId;

    public CreateOfferFragment() {
        super(R.string.offer_creation, R.layout.fragment_create_offer, true);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(!isUserAllowed()) {
            return;
        }
        Button submitButton = fragment.findViewById(R.id.submit_offer);
        submitButton.setOnClickListener(_view -> {
            EditText titleEditText = ((TextInputLayout) fragment.findViewById(R.id.offer_title)).getEditText();
            EditText fieldEditText = ((TextInputLayout) fragment.findViewById(R.id.offer_field)).getEditText();
            EditText durationEditText = ((TextInputLayout) fragment.findViewById(R.id.offer_duration)).getEditText();
            EditText descriptionEditText = ((TextInputLayout) fragment.findViewById(R.id.offer_description)).getEditText();
            EditText wageEditText = ((TextInputLayout) fragment.findViewById(R.id.offer_wage)).getEditText();
            EditText dateEditText = ((TextInputLayout) fragment.findViewById(R.id.offer_date)).getEditText();
            MaterialDatePicker<Long> signUpBirthdate = MaterialDatePicker.Builder.datePicker().setTitleText("Birthdate").setSelection(MaterialDatePicker.todayInUtcMilliseconds()).build();
            dateEditText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    signUpBirthdate.show(getChildFragmentManager(), "MATERIAL_DATE_PICKER");
                    signUpBirthdate.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Long>() {
                        @RequiresApi(api = Build.VERSION_CODES.O)
                        @Override
                        public void onPositiveButtonClick(Long selection) {
                            Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
                            calendar.setTimeInMillis(selection);
                            System.out.println(calendar);
                            Date birthdate = new Date(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DATE));
                            int month = birthdate.getMonth()+1;
                            dateEditText.setText(birthdate.getDay()+"/"+month+"/"+birthdate.getYear());
                        }
                    });
                }
            });

            if(titleEditText != null && fieldEditText != null && durationEditText != null && descriptionEditText != null && wageEditText != null) {
                String title = titleEditText.getText().toString();
                String field = fieldEditText.getText().toString();
                String duration = durationEditText.getText().toString();
                String description = descriptionEditText.getText().toString();
                double wage = Double.parseDouble(wageEditText.getText().toString());
                Offer offer = new Offer();
                offer.setTitle(title);
                offer.setField(field);
                offer.setDuration(duration);
                offer.setDescription(description);
                offer.setWage(wage);
                offer.setEmployerId(user.getId());
                offer.setDate(Calendar.getInstance().getTime()); // TODO Ne s'agit-il pas de la date de début ?
                offer.setLocationID(""); // TODO Il manque ce champ à faire
                OfferViewModel.getInstance().addOffer(offer);
            } else {
                showToast(0); // TODO Mettre un message d'erreur "Remplissez tous les champs"
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        if(!isUserAllowed()) {
            return;
        }

        OfferViewModel.getInstance().getOffer().observe(
                getViewLifecycleOwner(),
                offer -> {
                    this.offerId = offer.getId();
                    OfferViewModel.getInstance().getAll(null);
                }
        );

        OfferViewModel.getInstance().getOffers().observe(
                getViewLifecycleOwner(),
                offers -> {
                    System.out.println("TEST 4");
                    Bundle args = new Bundle();
                    args.putString("offerId", offerId);
                    goToFragment(OfferFragment.class, args);
                }
        );
    }

    @Override
    public void onStop() {
        super.onStop();
        OfferViewModel.getInstance().getOffers().removeObservers(getViewLifecycleOwner());
        OfferViewModel.getInstance().getOffer().removeObservers(getViewLifecycleOwner());
    }
}