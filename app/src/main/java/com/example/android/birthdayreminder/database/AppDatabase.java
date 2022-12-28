package com.example.android.birthdayreminder.database;

import android.content.Context;
import android.util.Log;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {PersonEntry.class}, version = 1, exportSchema = false)

public abstract class AppDatabase extends RoomDatabase {
    private static final String LOG_TAG = AppDatabase.class.getSimpleName();
    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "birthdayPeopleList";
    private static AppDatabase sInstance;

    public static synchronized AppDatabase getInstance(Context context) {
        //Check if there is an existing app database, if there isn't - we create one
        if (sInstance == null) {

                Log.d(LOG_TAG, "Creating new database instance");
                sInstance = Room.databaseBuilder(context.getApplicationContext(),
                        AppDatabase.class, AppDatabase.DATABASE_NAME)
                        .fallbackToDestructiveMigration()
                        .build();

        }
        Log.d(LOG_TAG, "Getting the database instance");
        return sInstance;
    }
    //This method will communicate with the PersonDao interface and add the personDao
    public abstract PersonDao personDao();
}
