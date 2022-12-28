package com.example.android.birthdayreminder;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

//The executor runs the code we need on another thread
public class AppExecutor {
    private static AppExecutor sInstance;
    private final Executor diskIO;

    //The constructor
    public AppExecutor(Executor diskIO) {
        this.diskIO = diskIO;
    }

    public static synchronized AppExecutor getInstance() {
        if (sInstance == null) {
                sInstance = new AppExecutor(
                        Executors.newSingleThreadExecutor()
                );
        }
        return sInstance;
    }

    public Executor diskIO() {return diskIO;}
}
