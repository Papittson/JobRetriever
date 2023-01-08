package com.example.jobretriever.fragments;

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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.stream.Collectors;

public class SignUpFragment extends JRFragment {

    public SignUpFragment() {
        super(R.string.action_sign_up, R.layout.fragment_sign_up, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        Button confirmButton = fragment.findViewById(R.id.confirmSignUp);
        TextInputLayout showDatePickerTIL = fragment.findViewById(R.id.signUpBirthdate_picker);
        EditText showDatePicker = showDatePickerTIL.getEditText();
        TextInputLayout dropdownUserTypesMenu = fragment.findViewById(R.id.dropdown_user_type_menu);
        AutoCompleteTextView dropdownUserTypesEditText = (AutoCompleteTextView) dropdownUserTypesMenu.getEditText();
        TextInputLayout dropdownCountriesMenu = fragment.findViewById(R.id.signUpDropdown_nationality);
        AutoCompleteTextView dropdownCountriesEditText = (AutoCompleteTextView) dropdownCountriesMenu.getEditText();

        List<String> userTypes = Arrays.stream(UserType.values())
                .map(userType -> getString(userType.stringResId))
                .collect(Collectors.toList());

        ArrayAdapter<String> userTypesAdapter = new ArrayAdapter<>(getContext(), R.layout.user_type_item, userTypes);
        ArrayAdapter<String> countriesAdapter = new ArrayAdapter<>(getContext(), R.layout.user_type_item, getCountries());

        if(showDatePicker != null) {
            showDatePicker.setOnClickListener(v -> pickDate(showDatePicker));
        }

        if(dropdownCountriesEditText != null) {
            dropdownCountriesEditText.setAdapter(countriesAdapter);
        }

        if(dropdownUserTypesEditText != null) {
            dropdownUserTypesEditText.setAdapter(userTypesAdapter);
            dropdownUserTypesEditText.setOnItemClickListener((parent, _view, position, id) -> {
                if(position == 0) {
                    showCandidateInputs();
                } else if(position == 1) {
                    showCandidateInputs(); // TODO Faire affichage pour les autres
                } else if(position == 2) {
                    showCandidateInputs(); // TODO Faire affichage pour les autres
                } else if(position == 3) {
                    showCandidateInputs(); // TODO Faire affichage pour les autres
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
                    switch (user.getType()) {
                        case APPLICANT:
                            goToFragment(CandidateProfileFragment.class, null);
                            break;
                        case EMPLOYER:
                        case AGENCY:
                            // TODO Faire les goToFragment(EmployerProfileFragment)
                            break;
                        case MODERATOR:
                            // TODO Faire les goToFragment(ModeratorProfileFragment)
                            break;
                    }
                }
        );
    }

    @Override
    public void onStop() {
        super.onStop();
        UserViewModel.getInstance().getUser().removeObservers(getViewLifecycleOwner());
    }

    public void pickDate(EditText showDatePicker) {
        MaterialDatePicker<Long> signUpBirthdate = MaterialDatePicker.Builder
                .datePicker()
                .setTitleText("Birthdate") // TODO Mettre un string resource
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .build();
        signUpBirthdate.show(getChildFragmentManager(), "MATERIAL_DATE_PICKER");
        signUpBirthdate.addOnPositiveButtonClickListener(selection -> {
            Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
            calendar.setTimeInMillis(selection);
            Date birthdate = new Date(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE));
            int month = birthdate.getMonth() + 1;
            showDatePicker.setText(birthdate.getDay()+"/"+month+"/"+birthdate.getYear()); // TODO Mettre string res placeholder
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

    public void signup() {
        TextInputLayout emailInput = fragment.findViewById(R.id.signUpMail);
        TextInputLayout passwordInput = fragment.findViewById(R.id.signUpPassword);
        TextInputLayout phoneNumberInput = fragment.findViewById(R.id.signUpPhoneNumber);
        TextInputLayout firstNameInput = fragment.findViewById(R.id.signUpFirstname);
        TextInputLayout lastNameInput = fragment.findViewById(R.id.signUpLastname);
        TextInputLayout nationalityInput = fragment.findViewById(R.id.signUpDropdown_nationality);
        TextInputLayout birthdateInput= fragment.findViewById(R.id.signUpBirthdate_picker);
        TextInputLayout userTypeInput = fragment.findViewById(R.id.dropdown_user_type_menu);

        EditText emailEditText = emailInput.getEditText();
        EditText passwordEditText = passwordInput.getEditText();
        EditText phoneNumberEditText = phoneNumberInput.getEditText();
        EditText firstNameEditText = firstNameInput.getEditText();
        EditText lastNameEditText = lastNameInput.getEditText();
        EditText nationalityEditText = nationalityInput.getEditText();
        EditText birthdateEditText = birthdateInput.getEditText();
        EditText userTypeEditText = userTypeInput.getEditText();

        if(
           emailEditText == null ||
           passwordEditText == null ||
           phoneNumberEditText == null ||
           firstNameEditText == null ||
           lastNameEditText == null ||
           nationalityEditText == null ||
           birthdateEditText == null ||
           userTypeEditText == null
        ) {
            showToast(0); // TODO Message style "Une erreur est survenue, veuillez réessayer"
            return;
        }

        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String phoneNumber = phoneNumberEditText.getText().toString();
        String firstname = firstNameEditText.getText().toString();
        String lastname = lastNameEditText.getText().toString();
        String nationality = nationalityEditText.getText().toString();
        String birthdate = birthdateEditText.getText().toString();
        UserType userType = UserType.valueOf(userTypeEditText.getText().toString());

        if(
           email.isBlank() ||
           password.isBlank() ||
           phoneNumber.isBlank() ||
           firstname.isBlank() ||
           lastname.isBlank() ||
           nationality.isBlank() ||
           birthdate.isBlank()
        ) {
            showToast(0); // TODO Message d'erreur champs obligatoires
            return;
        }

        User newUser = new User(email, password, firstname, lastname, nationality, phoneNumber, userType, birthdate);
        UserViewModel.getInstance().signUp(newUser);
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

}