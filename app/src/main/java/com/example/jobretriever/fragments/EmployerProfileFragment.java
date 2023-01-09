package com.example.jobretriever.fragments;

import static com.example.jobretriever.models.SignUpStatus.ACCEPTED;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.jobretriever.R;
import com.example.jobretriever.models.Employer;
import com.example.jobretriever.models.Offer;
import com.example.jobretriever.models.User;
import com.example.jobretriever.viewmodels.OfferViewModel;
import com.example.jobretriever.viewmodels.UserViewModel;

import java.util.List;
import java.util.stream.Collectors;


public class EmployerProfileFragment extends JRFragment {

    public EmployerProfileFragment() {
        super(R.string.profile, R.layout.fragment_employer_profile, true);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        checkProfileArgs();
        if(isUserAllowed() && user != null && user instanceof Employer) {
            Employer employer = (Employer) user;
            TextView nameTextView = fragment.findViewById(R.id.profile_name);
            TextView informationsTextView = fragment.findViewById(R.id.informations);
            TextView addressTextView = fragment.findViewById(R.id.address);
            TextView siretTextView = fragment.findViewById(R.id.siret);
            TextView managerTextView = fragment.findViewById(R.id.manager);
            ImageButton phoneButton = fragment.findViewById(R.id.contact_phone);
            ImageButton emailButton = fragment.findViewById(R.id.contact_email);
            Button createOfferButton = fragment.findViewById(R.id.create_offer);

            nameTextView.setText(employer.getBusinessName());

            informationsTextView.setText(getString(R.string.profile_infos_employer,getString(user.getUserType().stringResId),getString(employer.getSignUpStatus().stringResId)));
            addressTextView.setText(employer.getAddress());
            siretTextView.setText(employer.getSiret());
            managerTextView.setText(employer.getManager());

            if(user.getId().equals(userId) && ((Employer) user).getSignUpStatus() == ACCEPTED) {
                createOfferButton.setVisibility(View.VISIBLE);
            }

            phoneButton.setOnClickListener(v -> contactUserByPhone());
            emailButton.setOnClickListener(v -> contactUserByEmail());
            createOfferButton.setOnClickListener(v -> goToFragment(CreateOfferFragment.class, null));
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if(!isUserAllowed()) {
            return;
        }

        createRecyclerView(R.id.available_offers);

        if(user != null) {
            List<Offer> offersLiveData = OfferViewModel.getInstance().getOffers().getValue();
            if(offersLiveData == null || offersLiveData.size() == 0) {
                OfferViewModel.getInstance().getAll();
            }

            OfferViewModel.getInstance().getOffers().observe(
                    getViewLifecycleOwner(),
                    offers -> {
                        List<Offer> availableOffers = offers.stream()
                                .filter(offer -> offer.isCreatedByUser(user.getId()))
                                .collect(Collectors.toList());

                        updateRecyclerView(R.id.available_offers, availableOffers);
                    }
            );
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        OfferViewModel.getInstance().getOffers().removeObservers(getViewLifecycleOwner());
    }

    public void checkProfileArgs() {
        Bundle args = this.getArguments();
        List<User> users = UserViewModel.getInstance().getUsers().getValue();
        if(args != null && users != null && args.getString("userId") != null) {
            String userId = args.getString("userId");
            this.user = users.stream()
                    .filter(user -> user.getId().equals(userId))
                    .findFirst()
                    .orElse(null);
        }
    }

    public void contactUserByPhone() {
        String phoneNumber = user.getPhone();
        if(phoneNumber != null && !phoneNumber.isBlank()) {
            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phoneNumber, null));
            startActivity(intent);
        } else {
            showToast(R.string.unknown_phone_number);
        }
    }

    public void contactUserByEmail() {
        String emailAddress = user.getMail();
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:"));
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{emailAddress});
        intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));
        startActivity(intent);
    }
}