package com.crypto.cryptoinfo.background.asyncTask;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.crypto.cryptoinfo.App;
import com.crypto.cryptoinfo.background.service.NotificationService;
import com.crypto.cryptoinfo.repository.db.room.entity.AlertCoinPojo;
import com.crypto.cryptoinfo.repository.db.room.entity.CoinPojo;
import com.crypto.cryptoinfo.repository.db.sp.SharedPreferencesHelper;
import com.crypto.cryptoinfo.utils.Constants;
import com.crypto.cryptoinfo.utils.NotificationUtils;
import com.crypto.cryptoinfo.utils.Utils;

import java.util.List;

import static com.crypto.cryptoinfo.utils.Constants.BTC;
import static com.crypto.cryptoinfo.utils.Constants.EUR;
import static com.crypto.cryptoinfo.utils.Constants.USD;

public class NotificationAsyncTask extends AsyncTask<Void, Void, Void> {

    private static final String TAG = NotificationAsyncTask.class.getSimpleName();

    @SuppressLint("StaticFieldLeak")
    private Context mContext;

    public NotificationAsyncTask(Context context) {
        mContext = context;
    }

    @Override
    protected Void doInBackground(Void... params) {

//        String currentCurrency = SharedPreferencesHelper.getInstance().getCurrentCurrency();
        List<CoinPojo> coinPojos = App.dbInstance.getCoinDao().getAlerts();

        for (CoinPojo cp : coinPojos) {
            AlertCoinPojo alertCoinPojo = App.dbInstance.getAlertCoinDao().getAlertCoin(cp.getSymbol());
            if (alertCoinPojo != null) {

                if (cp.getPriceUsd() == null) return null;

                String toCurrency = alertCoinPojo.getToCurrency();
                double price;
                switch (toCurrency) {
                    case USD:
                        price = Double.parseDouble(cp.getPriceUsd());
                        break;
                    case EUR:
                        price = Double.parseDouble(cp.getPriceEur());
                        break;
                    case BTC:
                        price = Double.parseDouble(cp.getPriceBtc());
                        break;
                    default:
                        price = Double.parseDouble(cp.getPriceUsd());
                        break;
                }

                double high = alertCoinPojo.getHigh();
                double low = alertCoinPojo.getLow();

                Log.d(TAG, "doInBackground: high " + high);
                Log.d(TAG, "doInBackground: low " + low);
                Log.d(TAG, "doInBackground: price " + price);

                if (price > high && high != 0.0) {
                    Log.d(TAG, "notifyForChanges: priceUsd > alertCoinPojo.getHigh()");
                    NotificationUtils.sendNotificationForPriceChange(
                            mContext,
                            cp,
                            alertCoinPojo.getSymbol(),
                            alertCoinPojo.getSymbol() + " > " +
                                    Utils.formatPrice(String.valueOf(alertCoinPojo.getHigh())));
                    alertCoinPojo.setHigh(0.0);
                }

                if (price < low && low != 0.0) {
                    Log.d(TAG, "notifyForChanges: priceUsd < alertCoinPojo.getLow()");
                    NotificationUtils.sendNotificationForPriceChange(
                            mContext,
                            cp,
                            alertCoinPojo.getSymbol(),
                            alertCoinPojo.getSymbol() + " < " +
                                    Utils.formatPrice(String.valueOf(alertCoinPojo.getLow())));
                    alertCoinPojo.setLow(0.0);
                }

                if (alertCoinPojo.getHigh() == 0.0 && alertCoinPojo.getLow() == 0.0) {
                    App.dbInstance.getAlertCoinDao().deleteAlertCoinPojo(alertCoinPojo);
                } else {
                    App.dbInstance.getAlertCoinDao().insertAll(alertCoinPojo);
                }
            }
        }
        return null;
    }
}
