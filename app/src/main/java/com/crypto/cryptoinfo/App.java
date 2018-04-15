package com.crypto.cryptoinfo;

import android.app.Application;
import android.app.job.JobScheduler;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.support.v7.app.AppCompatDelegate;

import com.crypto.cryptoinfo.di.component.ApplicationComponent;
import com.crypto.cryptoinfo.di.component.DaggerApplicationComponent;
import com.crypto.cryptoinfo.di.module.ApplicationModule;
import com.crypto.cryptoinfo.repository.db.room.MainDatabase;
import com.crypto.cryptoinfo.repository.db.sp.SharedPreferencesHelper;

public class App extends Application {

    public static MainDatabase dbInstance;
    private static ApplicationComponent sApplicationComponent;

    private static JobScheduler mJobScheduler;

    static {
        AppCompatDelegate.setDefaultNightMode(
                AppCompatDelegate.MODE_NIGHT_AUTO);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        SharedPreferencesHelper.getInstance().initialize(this);
        initComponent();
        dbInstance = Room
                .databaseBuilder(getApplicationContext(), MainDatabase.class, "main_database")
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build();

        mJobScheduler = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
    }

    private void initComponent() {
        sApplicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .build();
    }

    public static JobScheduler getJobScheduler (){
        return mJobScheduler;
    }

    public static ApplicationComponent getApplicationComponent() {
        return sApplicationComponent;
    }
}
