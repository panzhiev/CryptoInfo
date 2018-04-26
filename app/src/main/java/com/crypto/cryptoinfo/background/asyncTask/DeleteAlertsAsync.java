package com.crypto.cryptoinfo.background.asyncTask;

import android.os.AsyncTask;

import com.crypto.cryptoinfo.App;

public class DeleteAlertsAsync extends AsyncTask<Void, Void, Void> {

    @Override
    protected Void doInBackground(Void... voids) {
        App.dbInstance.getAlertCoinDao().deleteAll();
        return null;
    }
}
