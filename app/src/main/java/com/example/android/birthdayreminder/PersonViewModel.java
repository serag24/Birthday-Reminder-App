package com.example.android.birthdayreminder;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.android.birthdayreminder.database.PersonEntry;

import java.util.List;

public class PersonViewModel extends AndroidViewModel {
    private static final String TAG = PersonViewModel.class.getSimpleName();

    private PersonRepository repository;
    private final LiveData<List<PersonEntry>> people;

    public PersonViewModel(Application application) {
        super(application);
        //The database is now actively queried in the ViewModel
        // Previously it was done in the Main- and AddTaskActivity
        repository = new PersonRepository(application);
        Log.d(TAG, "Actively retrieving the tasks from the DataBase");
        people = repository.getPeople();
    }

    public void insert(PersonEntry person) {
        repository.insert(person);
    }

    public void update(PersonEntry person) {
        repository.update(person);
    }

    public void delete(PersonEntry person) {
        repository.delete(person);
    }

    public LiveData<List<PersonEntry>> getPeople() {
        return people;
    }

    public LiveData<PersonEntry> getPersonById(int id) {
        return repository.getPersonById(id);
    }
}
