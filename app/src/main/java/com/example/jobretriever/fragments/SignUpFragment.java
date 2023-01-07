package com.example.jobretriever.fragments;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.jobretriever.R;
import com.example.jobretriever.models.User;
import com.example.jobretriever.models.UserType;
import com.example.jobretriever.viewmodels.UserViewModel;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
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

public class SignUpFragment extends Fragment {


    View view;
    UserType userType;
    String mail;
    String password;
    String phoneNumber;
    String firstname;
    String lastname;
    String birthdate;
    String nationality;
    UserViewModel userViewModel;


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_sign_up, container, false);
        Button confirm = (Button) view.findViewById(R.id.confirmSignUp);
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        // Inflate the layout for this fragment

        List<String> userTypes = Arrays.stream(UserType.values()).map(obj -> obj.toString()).collect(Collectors.toList());
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), R.layout.user_type_item, userTypes);
        TextInputLayout dropdown = view.findViewById(R.id.dropdown_user_type_menu);
        AutoCompleteTextView dropdownItem = (AutoCompleteTextView) dropdown.getEditText();
        dropdownItem.setAdapter(adapter);

        dropdownItem.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        TextInputLayout signUpUserType = getView().findViewById(R.id.dropdown_user_type_menu);
                        TextInputLayout signUpFirstname = getView().findViewById(R.id.signUpFirstname);
                        signUpFirstname.setVisibility(View.VISIBLE);
                        TextInputLayout signUpLastname = getView().findViewById(R.id.signUpLastname);
                        signUpLastname.setVisibility(View.VISIBLE);
                        MaterialDatePicker<Long> signUpBirthdate = MaterialDatePicker.Builder.datePicker().setTitleText("Birthdate").setSelection(MaterialDatePicker.todayInUtcMilliseconds()).build();
                        TextInputLayout showDatePickerTIL = getView().findViewById(R.id.signUpBirthdate_picker);
                        EditText showDatePicker = showDatePickerTIL.getEditText();
                        showDatePickerTIL.setVisibility(View.VISIBLE);
                        showDatePicker.setOnClickListener(new View.OnClickListener() {
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
                                        showDatePicker.setText(birthdate.getDay()+"/"+month+"/"+birthdate.getYear());
                                    }
                                });
                            }
                        });

                        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), R.layout.user_type_item, getCountries());
                        TextInputLayout dropdownCountry = getView().findViewById(R.id.signUpDropdown_nationality);
                        AutoCompleteTextView dropdownItem = (AutoCompleteTextView) dropdownCountry.getEditText();
                        dropdownItem.setAdapter(adapter);
                        dropdownCountry.setVisibility(View.VISIBLE);
                        break;
                    case 1:
                        break;
                    case 2:
                        break;
                    case 3:
                        break;
                }
            }
        });
        final Observer<Boolean> loggedInObserver = loggedIn -> {
            if(loggedIn){
                userViewModel.getLoggedIn().removeObservers(getViewLifecycleOwner());
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,CandidateProfileFragment.class,null).commit();
            }else{
                Context context = getContext();
                CharSequence text = UserViewModel.getErrorMessage().getValue();
                int duration = Toast.LENGTH_LONG;
                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }
        };
        userViewModel.getLoggedIn().observe(getViewLifecycleOwner(),loggedInObserver);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextInputLayout signUpMail = view.findViewById(R.id.signUpMail);
                mail=signUpMail.getEditText().getText().toString();
                TextInputLayout signUpPassword = view.findViewById(R.id.signUpPassword);
                password=signUpPassword.getEditText().getText().toString();
                TextInputLayout signUpPhoneNumber = view.findViewById(R.id.signUpPhoneNumber);
                phoneNumber=signUpPhoneNumber.getEditText().getText().toString();
                TextInputLayout signUpFirstname = view.findViewById(R.id.signUpFirstname);
                firstname =signUpFirstname.getEditText().getText().toString();
                TextInputLayout signUpLastname = view.findViewById(R.id.signUpLastname);
                lastname =signUpLastname.getEditText().getText().toString();
                TextInputLayout signUpNationality = view.findViewById(R.id.signUpDropdown_nationality);
                nationality =signUpNationality.getEditText().getText().toString();
                TextInputLayout signUpBirthdate= view.findViewById(R.id.signUpBirthdate_picker);
                birthdate=signUpBirthdate.getEditText().getText().toString();
                TextInputLayout signUpType = view.findViewById(R.id.dropdown_user_type_menu);
                userType = UserType.valueOf(signUpType.getEditText().getText().toString());
                User newUser = new User(null,mail,password,firstname,lastname,nationality,phoneNumber,userType,birthdate);
                userViewModel.signUp(newUser);
            }
        });

        return view;
    }

    //utils


    public List<String> getCountries() {
        Locale[] locales = Locale.getAvailableLocales();
        ArrayList<String> countries = new ArrayList<String>();
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
}