package com.example.jobretriever.viewmodels;

import com.example.jobretriever.models.Offer;
import com.example.jobretriever.repositories.OfferRepository;

public class OfferViewModel extends JRViewModel<Offer, OfferRepository> {

    public OfferViewModel(Class<Offer> offerClass) {
        super(offerClass);
    }

    public static OfferViewModel getInstance() {
        return new OfferViewModel(Offer.class);
    }
}
