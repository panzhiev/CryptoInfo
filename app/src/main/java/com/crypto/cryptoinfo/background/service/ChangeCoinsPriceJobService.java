package com.crypto.cryptoinfo.background.service;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.util.Log;

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

import static com.crypto.cryptoinfo.utils.Constants.USD;


public class ChangeCoinsPriceJobService extends JobService implements ILoadingView {

    private static final String TAG = ChangeCoinsPriceJobService.class.getSimpleName();
    CoinsPresenter mCoinsPresenter = new CoinsPresenter(this);

    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        Log.d(TAG, "onStartJob: ...");
        mCoinsPresenter.getCurrenciesList();
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
        String currentCurrency = SharedPreferencesHelper.getInstance().getCurrentCurrency();
        List<CoinPojo> coinPojos = App.dbInstance.getCoinDao().getAlerts();

        for (CoinPojo cp : coinPojos) {
            AlertCoinPojo alertCoinPojo = App.dbInstance.getAlertCoinDao().getAlertCoin(cp.getId());
            if (alertCoinPojo != null) {
                switch (currentCurrency) {
                    case USD:
                        double priceUsd = Double.parseDouble(cp.getPriceUsd());
                        if (priceUsd > alertCoinPojo.getHigh()) {
                            sendNotification(alertCoinPojo.getId(), alertCoinPojo.getId() + " > " +
                                    Utils.formatPrice(String.valueOf(alertCoinPojo.getHigh())));
                        } else if (priceUsd < alertCoinPojo.getLow()) {
                            sendNotification(alertCoinPojo.getId(), alertCoinPojo.getId() + " < " +
                                    Utils.formatPrice(String.valueOf(alertCoinPojo.getLow())));
                        }
                }
            }
        }
    }

    private void sendNotification(String id, String message) {
        Intent intent = new Intent(this, CoinInfoActivity.class);
        NotificationUtils.create(this, id.hashCode(), intent, this.getString(R.string.app_name), message);
    }
}
