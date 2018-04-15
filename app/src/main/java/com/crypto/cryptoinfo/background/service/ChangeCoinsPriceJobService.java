package com.crypto.cryptoinfo.background.service;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.util.Log;

import com.crypto.cryptoinfo.ui.fragment.ILoadingView;

import java.util.ArrayList;


public class ChangeCoinsPriceJobService extends JobService implements ILoadingView{

    private static final String TAG = ChangeCoinsPriceJobService.class.getSimpleName();

    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        Log.d(TAG, "onStartJob: ...");
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        Log.d(TAG, "onStopJob: ...");
        return false;
    }

    @Override
    public void showProgressIndicator() {

    }

    @Override
    public void hideProgressIndicator() {

    }

    @Override
    public void setList(ArrayList list) {

    }

    @Override
    public void reloadList(ArrayList list) {

    }

    @Override
    public void showError() {

    }

    @Override
    public void notifyForChanges() {

    }
}
