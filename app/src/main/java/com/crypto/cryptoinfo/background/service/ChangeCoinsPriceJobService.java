package com.crypto.cryptoinfo.background.service;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.crypto.cryptoinfo.App;
import com.crypto.cryptoinfo.R;
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

import static com.crypto.cryptoinfo.utils.Constants.COIN;
import static com.crypto.cryptoinfo.utils.Constants.USD;


public class ChangeCoinsPriceJobService extends JobService implements ILoadingView {

    private static final String TAG = ChangeCoinsPriceJobService.class.getSimpleName();
    JobParameters jobParameters;

    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        Log.d(TAG, "onStartJob: ...");
        this.jobParameters = jobParameters;
        new CoinsPresenter(this).getCurrenciesList();
        Toast.makeText(this, "onStartJob", Toast.LENGTH_SHORT).show();
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
//        Log.d(TAG, "onStopJob: ...");
        Toast.makeText(this, "onStopJob", Toast.LENGTH_SHORT).show();
        return true;
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

        jobFinished(jobParameters, true);
    }

    private void sendNotification(CoinPojo coinPojo, String symbol, String message) {
        Log.d(TAG, "sendNotification: ");
        Intent intent = new Intent(this, CoinInfoActivity.class);
        intent.putExtra(COIN, coinPojo);
        NotificationUtils.create(this, symbol.hashCode(), intent, this.getString(R.string.app_name), message);
        App.dbInstance.getAlertCoinDao().deleteAlert(symbol);
    }
}
