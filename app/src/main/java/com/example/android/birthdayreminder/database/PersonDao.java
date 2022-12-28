package com.example.android.birthdayreminder.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface PersonDao {
    @Query("SELECT * FROM personTable ORDER BY relation")
    LiveData<List<PersonEntry>> loadAllPeople();

    @Insert
    void insertPerson(PersonEntry personEntry);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updatePerson(PersonEntry personEntry);

    @Delete
    void deletePerson(PersonEntry personEntry);

    @Query("SELECT * FROM personTable WHERE id = :id")
    LiveData<PersonEntry> loadPersonById(int id);
}
