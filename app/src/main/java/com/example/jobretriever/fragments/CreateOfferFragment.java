package com.example.jobretriever.fragments;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;

import com.example.jobretriever.R;
import com.example.jobretriever.models.Offer;
import com.example.jobretriever.viewmodels.OfferViewModel;
import com.google.android.material.datepicker.MaterialDatePicker;
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
        EditText datePicker = ((TextInputLayout) fragment.findViewById(R.id.signUpBirthdate_picker)).getEditText();

        if(datePicker != null) {
            datePicker.setOnClickListener(v -> pickDate(datePicker));
        }

        submitButton.setOnClickListener(_view -> {
            EditText titleEditText = ((TextInputLayout) fragment.findViewById(R.id.offer_title)).getEditText();
            EditText fieldEditText = ((TextInputLayout) fragment.findViewById(R.id.offer_field)).getEditText();
            EditText durationEditText = ((TextInputLayout) fragment.findViewById(R.id.offer_duration)).getEditText();
            EditText descriptionEditText = ((TextInputLayout) fragment.findViewById(R.id.offer_description)).getEditText();
            EditText wageEditText = ((TextInputLayout) fragment.findViewById(R.id.offer_wage)).getEditText();

            if(titleEditText != null && fieldEditText != null && durationEditText != null && descriptionEditText != null && wageEditText != null) {
                String title = titleEditText.getText().toString();
                String field = fieldEditText.getText().toString();
                String duration = durationEditText.getText().toString();
                String description = descriptionEditText.getText().toString();
                double wage = Double.parseDouble(wageEditText.getText().toString());
                Date date = Calendar.getInstance().getTime(); // TODO Changer ça
                String location = ""; // TODO Faire ça
                Offer offer = new Offer(title, duration, date, field, description, wage, user.getId(), location);
                OfferViewModel.getInstance().addOffer(offer);
            } else {
                showToast(R.string.required_fields);
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

    @SuppressWarnings("deprecation")
    public void pickDate(EditText showDatePicker) {
        MaterialDatePicker<Long> signUpBirthdate = MaterialDatePicker.Builder
                .datePicker()
                .setTitleText(getText(R.string.birthdate))
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .build();
        signUpBirthdate.show(getChildFragmentManager(), "MATERIAL_DATE_PICKER");
        signUpBirthdate.addOnPositiveButtonClickListener(selection -> {
            Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
            calendar.setTimeInMillis(selection);
            Date birthdate = new Date(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE));
            int month = birthdate.getMonth() + 1;
            showDatePicker.setText(String.format(getString(R.string.birthdate_format),birthdate.getDay(),month,birthdate.getYear()));
        });
    }
}