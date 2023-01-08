package com.example.jobretriever.fragments;

import static com.example.jobretriever.models.UserType.AGENCY;
import static com.example.jobretriever.models.UserType.EMPLOYER;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;

import com.example.jobretriever.R;
import com.example.jobretriever.models.User;
import com.example.jobretriever.models.UserType;
import com.example.jobretriever.viewmodels.UserViewModel;
import com.google.android.material.datepicker.MaterialDatePicker;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class SignUpFragment extends JRFragment {
    UserType userType;

    public SignUpFragment() {
        super(R.string.action_sign_up, R.layout.fragment_sign_up, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        Button confirmButton = fragment.findViewById(R.id.confirmSignUp);
        EditText datePicker = fragment.findViewById(R.id.signUpBirthdate_picker);
        AutoCompleteTextView dropdownUserTypesEditText = fragment.findViewById(R.id.dropdown_user_type_menu);
        AutoCompleteTextView dropdownCountriesEditText = fragment.findViewById(R.id.signUpDropdown_nationality);

        ArrayAdapter<UserType> userTypesAdapter = new ArrayAdapter<>(getContext(), R.layout.user_type_item, UserType.values());
        ArrayAdapter<String> countriesAdapter = new ArrayAdapter<>(getContext(), R.layout.user_type_item, getCountries());

        datePicker.setOnClickListener(v -> pickDate(datePicker));
        dropdownCountriesEditText.setAdapter(countriesAdapter);

        dropdownUserTypesEditText.setAdapter(userTypesAdapter);
        dropdownUserTypesEditText.setOnItemClickListener((parent, _view, position, id) -> {
            Object item = parent.getItemAtPosition(position);
            if (item instanceof UserType) {
                userType = (UserType) item;
                if (userType == EMPLOYER || userType == AGENCY) {
                    showEmployerInputs();
                } else {
                    showCandidateInputs();
                }
            }
        });

        confirmButton.setOnClickListener(_view -> signup());
    }

    @Override
    public void onStart() {
        super.onStart();
        UserViewModel.getInstance().getUser().observe(
                getViewLifecycleOwner(),
                user1 -> {
                    if(user1 == null) {
                        return;
                    }
                    if(user1.getType() == EMPLOYER || user1.getType() == AGENCY) {
                        goToFragment(EmployerProfileFragment.class, null);
                    } else {
                        goToFragment(CandidateProfileFragment.class, null);
                    }
                }
        );
    }

    @Override
    public void onStop() {
        super.onStop();
        UserViewModel.getInstance().getUser().removeObservers(getViewLifecycleOwner());
    }

    @SuppressWarnings("deprecation")
    public void pickDate(EditText showDatePicker) {
        MaterialDatePicker<Long> signUpBirthdate = MaterialDatePicker.Builder
                .datePicker()
                .setTitleText(R.string.birthdate)
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .build();
        signUpBirthdate.show(getChildFragmentManager(), "MATERIAL_DATE_PICKER");
        signUpBirthdate.addOnPositiveButtonClickListener(selection -> {
            Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
            calendar.setTimeInMillis(selection);
            Date birthdate = new Date(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE));
            int month = birthdate.getMonth() + 1;
            showDatePicker.setText(String.format(getString(R.string.birthdate_format), birthdate.getDay(), month, birthdate.getYear()));
        });
    }

    public void showCandidateInputs() {
        EditText signUpFirstname = fragment.findViewById(R.id.signUpFirstname);
        EditText dropdownCountriesMenu = fragment.findViewById(R.id.signUpDropdown_nationality);
        EditText signUpLastname = fragment.findViewById(R.id.signUpLastname);
        EditText showDatePickerTIL = fragment.findViewById(R.id.signUpBirthdate_picker);

        signUpFirstname.setVisibility(View.VISIBLE);
        signUpLastname.setVisibility(View.VISIBLE);
        showDatePickerTIL.setVisibility(View.VISIBLE);
        dropdownCountriesMenu.setVisibility(View.VISIBLE);
    }

    public void showEmployerInputs() {
        EditText signUpBusinessName = fragment.findViewById(R.id.signUpBusinessName);
        EditText signUpAddress = fragment.findViewById(R.id.signUpAddress);
        EditText signUpSiret = fragment.findViewById(R.id.signUpSiret);
        EditText signUpManager = fragment.findViewById(R.id.signUpManager);

        signUpBusinessName.setVisibility(View.VISIBLE);
        signUpAddress.setVisibility(View.VISIBLE);
        signUpSiret.setVisibility(View.VISIBLE);
        signUpManager.setVisibility(View.VISIBLE);
    }

    public void signup() {
        EditText emailEditText = fragment.findViewById(R.id.signUpMail);
        EditText passwordEditText = fragment.findViewById(R.id.signUpPassword);
        EditText phoneNumberEditText = fragment.findViewById(R.id.signUpPhoneNumber);
        EditText firstNameEditText = fragment.findViewById(R.id.signUpFirstname);
        EditText lastNameEditText = fragment.findViewById(R.id.signUpLastname);
        EditText nationalityEditText = fragment.findViewById(R.id.signUpDropdown_nationality);
        EditText birthdateEditText = fragment.findViewById(R.id.signUpBirthdate_picker);
        EditText userTypeEditText = fragment.findViewById(R.id.dropdown_user_type_menu);
        EditText businessNameEditText = fragment.findViewById(R.id.signUpBusinessName);
        EditText addressEditText = fragment.findViewById(R.id.signUpAddress);
        EditText siretEditText = fragment.findViewById(R.id.signUpSiret);
        EditText managerEditText = fragment.findViewById(R.id.signUpManager);
        EditText experiencesEditText = fragment.findViewById(R.id.signUpExperiences);
        EditText educationsEditText = fragment.findViewById(R.id.signUpEducations);

        if (
                emailEditText == null ||
                        passwordEditText == null ||
                        phoneNumberEditText == null ||
                        firstNameEditText == null ||
                        lastNameEditText == null ||
                        nationalityEditText == null ||
                        birthdateEditText == null ||
                        userTypeEditText == null ||
                        businessNameEditText == null ||
                        addressEditText == null ||
                        siretEditText == null ||
                        managerEditText == null ||
                        educationsEditText == null ||
                        experiencesEditText == null ||
                        userType == null
        ) {
            showToast(R.string.error_has_occured);
            return;
        }

        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String phoneNumber = phoneNumberEditText.getText().toString();
        String firstname = firstNameEditText.getText().toString();
        String lastname = lastNameEditText.getText().toString();
        String nationality = nationalityEditText.getText().toString();
        String birthdate = birthdateEditText.getText().toString();
        String experiences = experiencesEditText.getText().toString();
        String educations = educationsEditText.getText().toString();
        String businessName = businessNameEditText.getText().toString();
        String address = addressEditText.getText().toString();
        String siret = siretEditText.getText().toString();
        String manager = managerEditText.getText().toString();

        if (userType == EMPLOYER || userType == AGENCY) {
            if (
                    email.isBlank() ||
                            password.isBlank() ||
                            businessName.isBlank() ||
                            address.isBlank() ||
                            siret.isBlank() ||
                            manager.isBlank()
            ) {
                showToast(R.string.required_fields);
                return;
            }
            User newUser = new User(email, password, businessName, phoneNumber, address, siret, manager, userType);
            UserViewModel.getInstance().signUp(newUser);
        } else {
            if (
                    email.isBlank() ||
                            password.isBlank() ||
                            firstname.isBlank() ||
                            lastname.isBlank() ||
                            nationality.isBlank() ||
                            birthdate.isBlank()
            ) {
                showToast(R.string.required_fields);
                return;
            }
            User newUser = new User(email, password, firstname, lastname, nationality, phoneNumber, birthdate,experiences,educations);
            UserViewModel.getInstance().signUp(newUser);
        }
    }
}