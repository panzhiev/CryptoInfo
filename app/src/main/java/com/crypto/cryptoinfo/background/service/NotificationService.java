package com.crypto.cryptoinfo.background.service;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.crypto.cryptoinfo.App;
import com.crypto.cryptoinfo.R;
import com.crypto.cryptoinfo.background.asyncTask.NotificationAsyncTask;
import com.crypto.cryptoinfo.background.br.NotificationBroadcastReceiver;
import com.crypto.cryptoinfo.presenter.CoinsPresenter;
import com.crypto.cryptoinfo.repository.db.room.entity.AlertCoinPojo;
import com.crypto.cryptoinfo.repository.db.room.entity.CoinPojo;
import com.crypto.cryptoinfo.repository.db.sp.SharedPreferencesHelper;
import com.crypto.cryptoinfo.ui.activity.CoinInfoActivity;
import com.crypto.cryptoinfo.ui.fragment.ILoadingView;
import com.crypto.cryptoinfo.utils.NotificationUtils;
import com.crypto.cryptoinfo.utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static com.crypto.cryptoinfo.utils.Constants.COIN;
import static com.crypto.cryptoinfo.utils.Constants.TIME_TO_UPD;
import static com.crypto.cryptoinfo.utils.Constants.USD;

public class NotificationService extends Service implements ILoadingView {

    private static final String TAG = NotificationService.class.getSimpleName();
    private Timer timer;
    private TimerTask timerTask;

//    public NotificationService(Context applicationContext) {
//        super();
//    }
//
//    public NotificationService() {
//    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        startTimer();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy: ");
        Intent broadcastIntent = new Intent(NotificationBroadcastReceiver.ACTION_RESTART_SERVICE);
        sendBroadcast(broadcastIntent);
        stopTimerTask();
    }

    public void startTimer() {

        timer = new Timer();

        initializeTimerTask();

        //schedule the timer, to wake up every 5 minutes
        timer.schedule(timerTask, 1000, TIME_TO_UPD);
    }

    /**
     * it sets the timer to print the counter every x seconds
     */
    public void initializeTimerTask() {

        timerTask = new TimerTask() {
            public void run() {
                if (!App.dbInstance.getAlertCoinDao().getAll().isEmpty()) {
                    Log.d(TAG, "run: list !isEmpty");
                    new CoinsPresenter(NotificationService.this).getCurrenciesList();
                    new Handler(Looper.getMainLooper())
                            .post(() -> Toast
                                    .makeText(NotificationService.this, "Send Request!", Toast.LENGTH_SHORT)
                                    .show());
                } else {
                    new Handler(Looper.getMainLooper())
                            .post(() -> Toast
                                    .makeText(NotificationService.this, "List is empty!", Toast.LENGTH_SHORT)
                                    .show());
                }
            }
        };
    }

    public void stopTimerTask() {
        //stop the timer, if it's not already null
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
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
        Log.d(TAG, "notifyForChanges: ");
        new NotificationAsyncTask(this).execute();
    }
}