package com.example.jobretriever.fragments;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;

import com.example.jobretriever.R;
import com.example.jobretriever.models.DurationType;
import com.example.jobretriever.models.Employer;
import com.example.jobretriever.models.Offer;
import com.example.jobretriever.viewmodels.OfferViewModel;
import com.google.android.material.datepicker.MaterialDatePicker;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class CreateOfferFragment extends JRFragment {
    String offerId;
    DurationType durationType;

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

        ArrayAdapter<String> citiesAdapter = new ArrayAdapter<>(getContext(), R.layout.user_type_item, getCities());
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
            if(user != null && user instanceof Employer) {
                Employer employer = (Employer) user;
                EditText titleEditText = fragment.findViewById(R.id.offer_title);
                EditText fieldEditText = fragment.findViewById(R.id.offer_field);
                EditText descriptionEditText = fragment.findViewById(R.id.offer_description);
                EditText wageEditText = fragment.findViewById(R.id.offer_wage);

                if(titleEditText != null && fieldEditText != null && descriptionEditText != null && wageEditText != null && durationType != null) {
                    String title = titleEditText.getText().toString();
                    String field = fieldEditText.getText().toString();
                    DurationType duration = durationType;
                    String description = descriptionEditText.getText().toString();
                    double wage = Double.parseDouble(wageEditText.getText().toString());
                    Date date = Calendar.getInstance().getTime(); // TODO Changer ça
                    String location = cityEditText.getText().toString();
                    Offer offer = new Offer(title, duration, date, field, description, wage, employer.getId(), location);
                    offer.setEmployer(employer);
                    OfferViewModel.getInstance().addOffer(offer);
                } else {
                    showToast(R.string.required_fields);
                }
            } else {
                goToFragment(HomeFragment.class, null);
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

        OfferViewModel.getInstance().getOffer().observe(
                getViewLifecycleOwner(),
                offer -> {
                    this.offerId = offer.getId();
                    OfferViewModel.getInstance().getAll();
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