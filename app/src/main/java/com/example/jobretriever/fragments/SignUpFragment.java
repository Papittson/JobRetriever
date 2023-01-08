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
import com.google.android.material.textfield.TextInputLayout;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.stream.Collectors;

public class SignUpFragment extends JRFragment {

    public SignUpFragment() {
        super(R.string.action_sign_up, R.layout.fragment_sign_up, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        Button confirmButton = fragment.findViewById(R.id.confirmSignUp);
        EditText datePicker = ((TextInputLayout) fragment.findViewById(R.id.signUpBirthdate_picker)).getEditText();
        TextInputLayout dropdownUserTypesMenu = fragment.findViewById(R.id.dropdown_user_type_menu);
        AutoCompleteTextView dropdownUserTypesEditText = (AutoCompleteTextView) dropdownUserTypesMenu.getEditText();
        TextInputLayout dropdownCountriesMenu = fragment.findViewById(R.id.signUpDropdown_nationality);
        AutoCompleteTextView dropdownCountriesEditText = (AutoCompleteTextView) dropdownCountriesMenu.getEditText();

        List<String> userTypes = Arrays.stream(UserType.values())
                .map(userType -> getString(userType.stringResId))
                .collect(Collectors.toList());

        ArrayAdapter<String> userTypesAdapter = new ArrayAdapter<>(getContext(), R.layout.user_type_item, userTypes);
        ArrayAdapter<String> countriesAdapter = new ArrayAdapter<>(getContext(), R.layout.user_type_item, getCountries());

        if (datePicker != null) {
            datePicker.setOnClickListener(v -> pickDate(datePicker));
        }

        if (dropdownCountriesEditText != null) {
            dropdownCountriesEditText.setAdapter(countriesAdapter);
        }

        if (dropdownUserTypesEditText != null) {
            dropdownUserTypesEditText.setAdapter(userTypesAdapter);
            dropdownUserTypesEditText.setOnItemClickListener((parent, _view, position, id) -> {
                if (position == 1 || position == 2) {
                    showEmployerInputs();
                } else {
                    showCandidateInputs();
                }
            });
        }

        confirmButton.setOnClickListener(_view -> signup());
    }

    @Override
    public void onStart() {
        super.onStart();
        UserViewModel.getInstance().getUser().observe(
                getViewLifecycleOwner(),
                user1 -> {
                    if (user.getType() == EMPLOYER || user.getType() == AGENCY) {
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
        TextInputLayout signUpFirstname = fragment.findViewById(R.id.signUpFirstname);
        TextInputLayout dropdownCountriesMenu = fragment.findViewById(R.id.signUpDropdown_nationality);
        TextInputLayout signUpLastname = fragment.findViewById(R.id.signUpLastname);
        TextInputLayout showDatePickerTIL = fragment.findViewById(R.id.signUpBirthdate_picker);

        signUpFirstname.setVisibility(View.VISIBLE);
        signUpLastname.setVisibility(View.VISIBLE);
        showDatePickerTIL.setVisibility(View.VISIBLE);
        dropdownCountriesMenu.setVisibility(View.VISIBLE);
    }

    public void showEmployerInputs() {
        TextInputLayout signUpBusinessName = fragment.findViewById(R.id.signUpBusinessName);
        TextInputLayout signUpAddress = fragment.findViewById(R.id.signUpAddress);
        TextInputLayout signUpSiret = fragment.findViewById(R.id.signUpSiret);
        TextInputLayout signUpManager = fragment.findViewById(R.id.signUpManager);

        signUpBusinessName.setVisibility(View.VISIBLE);
        signUpAddress.setVisibility(View.VISIBLE);
        signUpSiret.setVisibility(View.VISIBLE);
        signUpManager.setVisibility(View.VISIBLE);
    }

    public void signup() {
        TextInputLayout emailInput = fragment.findViewById(R.id.signUpMail);
        TextInputLayout passwordInput = fragment.findViewById(R.id.signUpPassword);
        TextInputLayout phoneNumberInput = fragment.findViewById(R.id.signUpPhoneNumber);
        TextInputLayout firstNameInput = fragment.findViewById(R.id.signUpFirstname);
        TextInputLayout lastNameInput = fragment.findViewById(R.id.signUpLastname);
        TextInputLayout nationalityInput = fragment.findViewById(R.id.signUpDropdown_nationality);
        TextInputLayout birthdateInput = fragment.findViewById(R.id.signUpBirthdate_picker);
        TextInputLayout userTypeInput = fragment.findViewById(R.id.dropdown_user_type_menu);
        TextInputLayout businessNameInput = fragment.findViewById(R.id.signUpBusinessName);
        TextInputLayout addressInput = fragment.findViewById(R.id.signUpAddress);
        TextInputLayout siretInput = fragment.findViewById(R.id.signUpSiret);
        TextInputLayout managerInput = fragment.findViewById(R.id.signUpManager);
        TextInputLayout experiencesInput = fragment.findViewById(R.id.signUpExperiences);
        TextInputLayout educationsInput = fragment.findViewById(R.id.signUpEducations);

        EditText emailEditText = emailInput.getEditText();
        EditText passwordEditText = passwordInput.getEditText();
        EditText phoneNumberEditText = phoneNumberInput.getEditText();
        EditText firstNameEditText = firstNameInput.getEditText();
        EditText lastNameEditText = lastNameInput.getEditText();
        EditText nationalityEditText = nationalityInput.getEditText();
        EditText birthdateEditText = birthdateInput.getEditText();
        EditText userTypeEditText = userTypeInput.getEditText();
        EditText businessNameEditText = businessNameInput.getEditText();
        EditText addressEditText = addressInput.getEditText();
        EditText siretEditText = siretInput.getEditText();
        EditText managerEditText = managerInput.getEditText();
        EditText experiencesEditText = experiencesInput.getEditText();
        EditText educationsEditText = educationsInput.getEditText();

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
                        experiencesEditText == null
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

        UserType userType = UserType.valueOf(userTypeEditText.getText().toString());
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