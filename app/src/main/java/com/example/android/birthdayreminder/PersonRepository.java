package com.example.android.birthdayreminder;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.android.birthdayreminder.database.AppDatabase;
import com.example.android.birthdayreminder.database.PersonDao;
import com.example.android.birthdayreminder.database.PersonEntry;

import java.util.List;

public class PersonRepository {
    private final PersonDao personDao;
    private final LiveData<List<PersonEntry>> people;

    public PersonRepository(Application application) {
        AppDatabase appDatabase = AppDatabase.getInstance(application);
        personDao = appDatabase.personDao();
        people = personDao.loadAllPeople();
    }

    public void insert(PersonEntry person) {
AppExecutor.getInstance().diskIO().execute(new Runnable() {
    @Override
    public void run() {
        personDao.insertPerson(person);
    }
});
    }

    public void update(PersonEntry person) {
AppExecutor.getInstance().diskIO().execute(new Runnable() {
    @Override
    public void run() {
        personDao.updatePerson(person);
    }
});
    }

    public void delete(PersonEntry person) {
AppExecutor.getInstance().diskIO().execute(new Runnable() {
    @Override
    public void run() {
        personDao.deletePerson(person);
    }
});
    }

    public LiveData<List<PersonEntry>> getPeople() {
        return people;
    }

    public LiveData<PersonEntry> getPersonById(int id) {
        LiveData<PersonEntry> person = personDao.loadPersonById(id);
        return person;
    }
}
