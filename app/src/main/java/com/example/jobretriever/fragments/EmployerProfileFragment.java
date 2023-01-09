package com.example.jobretriever.fragments;

import static com.example.jobretriever.enums.SignUpStatus.ACCEPTED;

import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.jobretriever.R;
import com.example.jobretriever.models.Employer;
import com.example.jobretriever.viewmodels.OfferViewModel;
import com.example.jobretriever.viewmodels.UserViewModel;


public class EmployerProfileFragment extends ProfileFragment {

    public EmployerProfileFragment() {
        super(R.layout.fragment_employer_profile);
    }

    @Override
    public void onStart() {
        super.onStart();
        if(!isUserAllowed()) {
            return;
        }

        createRecyclerView(R.id.available_offers);

        UserViewModel.getInstance().getSelectedUser().observe(
                this,
                _user -> {
                    if(_user == null) {
                        return;
                    }
                    this.user = _user;
                    UserViewModel.getInstance().getSelectedUser().removeObservers(this);
                    UserViewModel.getInstance().getSelectedUser().postValue(null);
                    if(this.user instanceof Employer) {
                        Employer employer = (Employer) this.user;
                        TextView nameTextView = fragment.findViewById(R.id.profile_name);
                        TextView informationsTextView = fragment.findViewById(R.id.informations);
                        TextView addressTextView = fragment.findViewById(R.id.address);
                        TextView siretTextView = fragment.findViewById(R.id.siret);
                        TextView managerTextView = fragment.findViewById(R.id.manager);
                        ImageButton phoneButton = fragment.findViewById(R.id.contact_phone);
                        ImageButton emailButton = fragment.findViewById(R.id.contact_email);
                        ImageButton websiteButton = fragment.findViewById(R.id.visit_website);
                        Button createOfferButton = fragment.findViewById(R.id.create_offer);

                        nameTextView.setText(employer.getBusinessName());
                        informationsTextView.setText(getString(R.string.profile_infos_employer,getString(user.getUserType().stringResId),getString(employer.getSignUpStatus().stringResId)));
                        addressTextView.setText(employer.getAddress());
                        siretTextView.setText(employer.getSiret());
                        managerTextView.setText(employer.getManager());

                        if(this.authUser.getId().equals(this.user.getId()) && employer.getSignUpStatus() == ACCEPTED) {
                            createOfferButton.setVisibility(View.VISIBLE);
                        }

                        phoneButton.setOnClickListener(v -> contactUserByPhone());
                        emailButton.setOnClickListener(v -> contactUserByEmail());
                        websiteButton.setOnClickListener(v -> visitWebsite());
                        createOfferButton.setOnClickListener(v -> goToFragment(CreateOfferFragment.class));

                        OfferViewModel.getInstance().getAvailables(user.getId());
                        OfferViewModel.getInstance().getAvailableOffers().observe(
                                this,
                                offers -> {
                                    if(offers != null) {
                                        updateRecyclerView(R.id.available_offers, offers);
                                        OfferViewModel.getInstance().getAvailableOffers().removeObservers(this);
                                        OfferViewModel.getInstance().getAvailableOffers().postValue(null);
                                    }
                                }
                        );
                    } else {
                        goToFragment(HomeFragment.class);
                        showToast(R.string.error_user_not_found);
                    }
                }
        );
    }

    @Override
    public void onStop() {
        super.onStop();
        OfferViewModel.getInstance().getAvailableOffers().removeObservers(this);
        OfferViewModel.getInstance().getAvailableOffers().postValue(null);
    }
}