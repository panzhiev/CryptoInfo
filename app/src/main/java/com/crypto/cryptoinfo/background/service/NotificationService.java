package com.crypto.cryptoinfo.background.service;

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

    public NotificationService(Context applicationContext) {
        super();
        Log.i("HERE", "here I am!");
    }

    public NotificationService() {
    }

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

    private Timer timer;
    private TimerTask timerTask;

    public void startTimer() {
        //set a new Timer
        timer = new Timer();

        //initialize the TimerTask's job
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
                //TODO: do your staff here
                new CoinsPresenter(NotificationService.this).getCurrenciesList();
                new Handler(Looper.getMainLooper())
                        .post(() -> Toast
                                .makeText(NotificationService.this, "Send Request!", Toast.LENGTH_SHORT)
                                .show());
            }
        };
    }

    /**
     * not needed
     */
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
        new NotificationAsyncTask().execute();

    }

    private void sendNotification(CoinPojo coinPojo, String symbol, String message) {
        Log.d(TAG, "sendNotification: ");
        Intent intent = new Intent(this, CoinInfoActivity.class);
        intent.putExtra(COIN, coinPojo);
        NotificationUtils.create(this, symbol.hashCode(), intent, this.getString(R.string.app_name), message);
        App.dbInstance.getAlertCoinDao().deleteAlert(symbol);
    }

    private class NotificationAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            String currentCurrency = SharedPreferencesHelper.getInstance().getCurrentCurrency();
            List<CoinPojo> coinPojos = App.dbInstance.getCoinDao().getAlerts();

            for (CoinPojo cp : coinPojos) {
                AlertCoinPojo alertCoinPojo = App.dbInstance.getAlertCoinDao().getAlertCoin(cp.getSymbol());
                if (alertCoinPojo != null) {
                    switch (currentCurrency) {
                        case USD:
                            Log.d(TAG, "notifyForChanges: case: USD");
                            double priceUsd = Double.parseDouble(cp.getPriceUsd());
                            if (priceUsd > alertCoinPojo.getHigh()) {
                                Log.d(TAG, "notifyForChanges: priceUsd > alertCoinPojo.getHigh()");
                                sendNotification(cp, alertCoinPojo.getSymbol(), alertCoinPojo.getSymbol() + " > " +
                                        Utils.formatPrice(String.valueOf(alertCoinPojo.getHigh())));
                            } else if (priceUsd < alertCoinPojo.getLow()) {
                                Log.d(TAG, "notifyForChanges: priceUsd < alertCoinPojo.getLow()");
                                sendNotification(cp, alertCoinPojo.getSymbol(), alertCoinPojo.getSymbol() + " < " +
                                        Utils.formatPrice(String.valueOf(alertCoinPojo.getLow())));
                            }
                    }
                }
            }
            return null;
        }
    }
}