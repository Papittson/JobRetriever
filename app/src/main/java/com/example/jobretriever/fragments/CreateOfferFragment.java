package com.example.jobretriever.fragments;

import static com.example.jobretriever.activities.MainActivity.DATE_FORMATTER;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;

import com.example.jobretriever.R;
import com.example.jobretriever.activities.MainActivity;
import com.example.jobretriever.enums.DurationType;
import com.example.jobretriever.models.Employer;
import com.example.jobretriever.models.Offer;
import com.example.jobretriever.viewmodels.OfferViewModel;
import com.google.android.material.datepicker.MaterialDatePicker;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

public class CreateOfferFragment extends JRFragment {
    DurationType durationType;
    LocalDate date;

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
        EditText datePicker = fragment.findViewById(R.id.offer_date);
        AutoCompleteTextView cityEditText = fragment.findViewById(R.id.offer_city);
        AutoCompleteTextView durationEditText = fragment.findViewById(R.id.offer_duration);

        ArrayAdapter<String> citiesAdapter = new ArrayAdapter<>(getContext(), R.layout.user_type_item, MainActivity.getCities());
        cityEditText.setAdapter(citiesAdapter);
        cityEditText.setThreshold(1);

        ArrayAdapter<DurationType> durationsAdapter = new ArrayAdapter<>(getContext(), R.layout.user_type_item, DurationType.values());
        durationEditText.setAdapter(durationsAdapter);

        durationEditText.setOnItemClickListener((parent, arg1, position, arg3) -> {
            Object item = parent.getItemAtPosition(position);
            if (item instanceof DurationType) {
                durationType = (DurationType) item;
            }
        });

        if(datePicker != null) {
            datePicker.setOnClickListener(v -> pickDate(datePicker));
        }

        submitButton.setOnClickListener(_view -> {
            if(this.authUser instanceof Employer) {
                Employer employer = (Employer) this.authUser;
                EditText titleEditText = fragment.findViewById(R.id.offer_title);
                EditText fieldEditText = fragment.findViewById(R.id.offer_field);
                EditText descriptionEditText = fragment.findViewById(R.id.offer_description);
                EditText wageEditText = fragment.findViewById(R.id.offer_wage);

                if(titleEditText != null && fieldEditText != null && descriptionEditText != null && wageEditText != null && durationType != null) {
                    String title = titleEditText.getText().toString();
                    String field = fieldEditText.getText().toString();
                    DurationType duration = durationType;
                    String description = descriptionEditText.getText().toString();
                    String wageString = wageEditText.getText().toString();
                    String location = cityEditText.getText().toString();
                    if(title.isBlank() || field.isBlank() || description.isBlank() || wageString.isBlank() || location.isBlank()) {
                        showToast(R.string.required_fields);
                        return;
                    }
                    double wage = Double.parseDouble(wageString);
                    Offer offer = new Offer(title, duration, date, field, description, wage, employer.getId(), location);
                    offer.setEmployer(employer);
                    OfferViewModel.getInstance().addOffer(offer);
                } else {
                    showToast(R.string.required_fields);
                }
            } else {
                goToFragment(HomeFragment.class);
                showToast(R.string.error_has_occured);
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        if(!isUserAllowed()) {
            return;
        }

        OfferViewModel.getInstance().getSelectedOffer().observe(
                this,
                offer -> {
                    if(offer != null) {
                        goToFragment(OfferFragment.class);
                        OfferViewModel.getInstance().getSelectedOffer().removeObservers(this);
                    }
                }
        );
    }

    @Override
    public void onStop() {
        super.onStop();
        OfferViewModel.getInstance().getSelectedOffer().removeObservers(this);
    }

    public void pickDate(EditText showDatePicker) {
        MaterialDatePicker<Long> signUpBirthdate = MaterialDatePicker.Builder
                .datePicker()
                .setTitleText(R.string.birthdate)
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .build();
        signUpBirthdate.show(getChildFragmentManager(), "MATERIAL_DATE_PICKER");
        signUpBirthdate.addOnPositiveButtonClickListener(selection -> {
            this.date = Instant.ofEpochMilli(selection).atZone(ZoneId.systemDefault()).toLocalDate();
            showDatePicker.setText(this.date.format(DATE_FORMATTER));
        });
    }
}