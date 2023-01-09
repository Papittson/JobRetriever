package com.example.jobretriever.fragments;

import static com.example.jobretriever.activities.MainActivity.DATE_FORMATTER;
import static com.example.jobretriever.enums.UserType.AGENCY;
import static com.example.jobretriever.enums.UserType.EMPLOYER;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;

import com.example.jobretriever.R;
import com.example.jobretriever.activities.MainActivity;
import com.example.jobretriever.enums.UserType;
import com.example.jobretriever.models.Applicant;
import com.example.jobretriever.models.Employer;
import com.example.jobretriever.models.User;
import com.example.jobretriever.viewmodels.UserViewModel;
import com.google.android.material.datepicker.MaterialDatePicker;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

public class SignUpFragment extends JRFragment {
    UserType userType;
    LocalDate birthdate;

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
        ArrayAdapter<String> countriesAdapter = new ArrayAdapter<>(getContext(), R.layout.user_type_item, MainActivity.getCountries());

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
        UserViewModel.getInstance().getAuthUser().observe(
                this,
                user -> {
                    if (user != null) {
                        UserViewModel.getInstance().getAuthUser().removeObservers(this);
                        UserViewModel.getInstance().getSelectedUser().postValue(user);
                        if(user instanceof Applicant) {
                            goToFragment(CandidateProfileFragment.class);
                        } else {
                            goToFragment(EmployerProfileFragment.class);
                        }
                    }
                }
        );
    }

    @Override
    public void onStop() {
        super.onStop();
        UserViewModel.getInstance().getAuthUser().removeObservers(this);
    }

    public void pickDate(EditText showDatePicker) {
        MaterialDatePicker<Long> signUpBirthdate = MaterialDatePicker.Builder
                .datePicker()
                .setTitleText(R.string.birthdate)
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .build();
        signUpBirthdate.show(getChildFragmentManager(), "MATERIAL_DATE_PICKER");
        signUpBirthdate.addOnPositiveButtonClickListener(selection -> {
            this.birthdate = Instant.ofEpochMilli(selection).atZone(ZoneId.systemDefault()).toLocalDate();
            showDatePicker.setText(this.birthdate.format(DATE_FORMATTER));
        });
    }

    public void showCandidateInputs() {
        View signUpFirstname = fragment.findViewById(R.id.signUpFirstnameInput);
        View dropdownCountriesMenu = fragment.findViewById(R.id.signUpDropdown_nationalityInput);
        View signUpLastname = fragment.findViewById(R.id.signUpLastnameInput);
        View showDatePickerTIL = fragment.findViewById(R.id.signUpBirthdate_pickerInput);
        View signUpEducations = fragment.findViewById(R.id.signUpEducationsInput);
        View signUpExperiences = fragment.findViewById(R.id.signUpExperiencesInput);

        signUpFirstname.setVisibility(View.VISIBLE);
        signUpLastname.setVisibility(View.VISIBLE);
        showDatePickerTIL.setVisibility(View.VISIBLE);
        dropdownCountriesMenu.setVisibility(View.VISIBLE);
        signUpEducations.setVisibility(View.VISIBLE);
        signUpExperiences.setVisibility(View.VISIBLE);

        View signUpBusinessName = fragment.findViewById(R.id.signUpBusinessNameInput);
        View signUpAddress = fragment.findViewById(R.id.signUpAddressInput);
        View signUpSiret = fragment.findViewById(R.id.signUpSiretInput);
        View signUpManager = fragment.findViewById(R.id.signUpManagerInput);

        signUpBusinessName.setVisibility(View.GONE);
        signUpAddress.setVisibility(View.GONE);
        signUpSiret.setVisibility(View.GONE);
        signUpManager.setVisibility(View.GONE);
    }

    public void showEmployerInputs() {
        View signUpBusinessName = fragment.findViewById(R.id.signUpBusinessNameInput);
        View signUpAddress = fragment.findViewById(R.id.signUpAddressInput);
        View signUpSiret = fragment.findViewById(R.id.signUpSiretInput);
        View signUpManager = fragment.findViewById(R.id.signUpManagerInput);

        signUpBusinessName.setVisibility(View.VISIBLE);
        signUpAddress.setVisibility(View.VISIBLE);
        signUpSiret.setVisibility(View.VISIBLE);
        signUpManager.setVisibility(View.VISIBLE);

        View signUpFirstname = fragment.findViewById(R.id.signUpFirstnameInput);
        View dropdownCountriesMenu = fragment.findViewById(R.id.signUpDropdown_nationalityInput);
        View signUpLastname = fragment.findViewById(R.id.signUpLastnameInput);
        View showDatePickerTIL = fragment.findViewById(R.id.signUpBirthdate_pickerInput);
        View signUpEducations = fragment.findViewById(R.id.signUpEducationsInput);
        View signUpExperiences = fragment.findViewById(R.id.signUpExperiencesInput);

        signUpFirstname.setVisibility(View.GONE);
        signUpLastname.setVisibility(View.GONE);
        showDatePickerTIL.setVisibility(View.GONE);
        dropdownCountriesMenu.setVisibility(View.GONE);
        signUpEducations.setVisibility(View.GONE);
        signUpExperiences.setVisibility(View.GONE);
    }

    public void signup() {
        EditText emailEditText = fragment.findViewById(R.id.signUpMail);
        EditText passwordEditText = fragment.findViewById(R.id.signUpPassword);
        EditText phoneNumberEditText = fragment.findViewById(R.id.signUpPhoneNumber);
        EditText websiteUrlEditText = fragment.findViewById(R.id.signUpWebsiteUrl);
        EditText firstNameEditText = fragment.findViewById(R.id.signUpFirstname);
        EditText lastNameEditText = fragment.findViewById(R.id.signUpLastname);
        EditText nationalityEditText = fragment.findViewById(R.id.signUpDropdown_nationality);
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
                        websiteUrlEditText == null ||
                        firstNameEditText == null ||
                        lastNameEditText == null ||
                        nationalityEditText == null ||
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
        String websiteUrl = websiteUrlEditText.getText().toString();
        String firstname = firstNameEditText.getText().toString();
        String lastname = lastNameEditText.getText().toString();
        String nationality = nationalityEditText.getText().toString();
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
            User newUser = new Employer(email, encrypt(password), phoneNumber, websiteUrl, userType, businessName, address, siret, manager);
            UserViewModel.getInstance().signUp(newUser);
        } else {
            if (
                    email.isBlank() ||
                            password.isBlank() ||
                            firstname.isBlank() ||
                            lastname.isBlank() ||
                            nationality.isBlank() ||
                            birthdate == null
            ) {
                showToast(R.string.required_fields);
                return;
            }
            User newUser = new Applicant(email, encrypt(password), phoneNumber, websiteUrl, firstname, lastname, nationality, educations, experiences, birthdate);
            UserViewModel.getInstance().signUp(newUser);
        }
    }
}