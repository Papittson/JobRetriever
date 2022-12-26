package com.example.jobretriever.repositories;

public class OfferRepository extends JRRepository {

    public static OfferRepository getInstance() {
        return (OfferRepository) getInstance("offers");
    }

}
