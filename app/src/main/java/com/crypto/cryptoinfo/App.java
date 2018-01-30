package com.crypto.cryptoinfo;

import android.app.Application;
import android.arch.persistence.room.Room;

import com.crypto.cryptoinfo.repository.db.room.MainDatabase;
import com.crypto.cryptoinfo.repository.db.sp.SharedPreferencesHelper;

public class App extends Application {

    public static MainDatabase dbInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        SharedPreferencesHelper.getInstance().initialize(this);
        dbInstance = Room
                .databaseBuilder(getApplicationContext(), MainDatabase.class, "main_database")
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build();
    }

}
