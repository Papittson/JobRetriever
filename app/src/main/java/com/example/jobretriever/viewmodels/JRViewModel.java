package com.example.jobretriever.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.jobretriever.models.Entity;
import com.example.jobretriever.repositories.JRRepository;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class JRViewModel<Model extends Entity, Repository extends JRRepository> extends ViewModel {
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private final MutableLiveData<Model> datum = new MutableLiveData<>();
    private final MutableLiveData<List<Model>> data = new MutableLiveData<>();
    private final Class<Model> modelClass;

    public JRViewModel(Class<Model> modelClass) {
        this.modelClass = modelClass;
    }

    public MutableLiveData<List<Model>> getAll() {
        Repository.getInstance().getAll().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                List<Model> list = new ArrayList<>();
                for(QueryDocumentSnapshot doc : task.getResult()) {
                    Model obj = doc.toObject(modelClass);
                    obj.setId(doc.getId());
                    list.add(obj);
                }
                data.postValue(list);
            } else {
                errorMessage.postValue("Error loading users");
                if(task.getException() != null) {
                    task.getException().printStackTrace();
                }
            }
        });
        return data;
    }
}
