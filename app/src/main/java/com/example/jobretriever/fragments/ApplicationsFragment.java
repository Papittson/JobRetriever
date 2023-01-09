package com.example.jobretriever.fragments;

import com.example.jobretriever.R;
import com.example.jobretriever.models.Applicant;
import com.example.jobretriever.models.Employer;
import com.example.jobretriever.viewmodels.OfferViewModel;


public class ApplicationsFragment extends JRFragment {

    public ApplicationsFragment() {
        super(R.string.applications, R.layout.fragment_applications, true);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (!isUserAllowed()) {
            return;
        }

        if(this.authUser instanceof Applicant) {
            createRecyclerView(R.id.applied_offers);
            Applicant applicant = (Applicant) this.authUser;
            OfferViewModel.getInstance().getApplieds(applicant.getApplicationsId());
            OfferViewModel.getInstance().getAppliedOffers().observe(
                    this,
                    offers -> {
                        if(offers != null) {
                            updateRecyclerView(R.id.applied_offers, offers);
                            OfferViewModel.getInstance().getAppliedOffers().removeObservers(this);
                            OfferViewModel.getInstance().getAppliedOffers().postValue(null);
                        }
                    }
            );
        } else if(this.authUser instanceof Employer) {
            Employer employer = (Employer) this.authUser;
            // TODO Continue here
        } else {
            goToFragment(HomeFragment.class);
            showToast(R.string.error_has_occured);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        OfferViewModel.getInstance().getAppliedOffers().removeObservers(this);
        OfferViewModel.getInstance().getAppliedOffers().postValue(null);
    }
}