package com.example.jobretriever.repositories;

public class ApplicationRepository extends JRRepository {
    private static ApplicationRepository instance;

    ApplicationRepository() {
        super("applications");
    }

    public static ApplicationRepository getInstance() {
        if(instance == null)
            instance = new ApplicationRepository();
        return instance;
    }
}
