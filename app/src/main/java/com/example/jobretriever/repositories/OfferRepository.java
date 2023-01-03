package com.example.jobretriever.repositories;

public class OfferRepository extends JRRepository {
    private static OfferRepository instance;

    OfferRepository() {
        super("offers");
    }

    public static OfferRepository getInstance() {
        if(instance == null)
            instance = new OfferRepository();
        return instance;
    }
}
