package com.crypto.cryptoinfo.presenter;


import android.os.AsyncTask;
import android.util.Log;

import com.crypto.cryptoinfo.App;
import com.crypto.cryptoinfo.repository.db.room.entity.CoinPojo;
import com.crypto.cryptoinfo.repository.db.room.entity.PointTimePrice;
import com.crypto.cryptoinfo.ui.fragment.ILoadingView;
import com.google.gson.JsonElement;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Response;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public class CoinsPresenter extends BasePresenter implements IPresenter {

    private final String TAG = getClass().getSimpleName();
    private ILoadingView fragment;
    private Subscription mSubscriptionCurrencies, mSubscriptionGetCharts;
    private CompositeSubscription mCompositeSubscription = new CompositeSubscription();

    public CoinsPresenter(ILoadingView fragment) {
        super();
        this.fragment = fragment;
    }

    public void getCurrenciesList() {

        mSubscriptionCurrencies = mModel
                .getAllTickers()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(() -> fragment.showProgressIndicator())
                .doOnTerminate(() -> fragment.hideProgressIndicator())
                .subscribe(this::responseCurrenciesHandler, e -> fragment.showError());

        mCompositeSubscription.add(mSubscriptionCurrencies);
    }

    private void responseCurrenciesHandler(List<CoinPojo> coinPojos) {
        new SaveCoinsAsync().execute(coinPojos);
    }

    public void getChartsData(String coin, String pastTime, String presentTime) {
        mSubscriptionGetCharts = mModel
                .getGraphsPerPeriod(coin, pastTime, presentTime)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(() -> fragment.showProgressIndicator())
                .doOnTerminate(() -> fragment.hideProgressIndicator())
                .subscribe(this::responseChartsDataHandler);
    }

    private void responseChartsDataHandler(Response<JsonElement> response) {
        Log.d(TAG, "responseChartsDataHandler started");
        JSONObject jsonObject;
        if (response.isSuccessful()) {
            Log.d(TAG, "responseChartsDataHandler response.isSuccessful()");
            try {
                jsonObject = new JSONObject(response.body().toString());
                Log.d(TAG, jsonObject.toString());

                JSONArray jsonArray = jsonObject.getJSONArray("price_usd");
//                String priceUsd = String.valueOf();
//                Log.d(TAG, priceUsd);

                List<PointTimePrice> list = new ArrayList<>();
                for (int i = 0; i<jsonArray.length(); i++){
                    JSONArray jsonArray1 = jsonArray.getJSONArray(i);
                    list.add(new PointTimePrice(jsonArray1.getString(0), jsonArray1.getString(1)));
                }
                Log.d(TAG, list.toString());

            } catch (JSONException e) {
                e.printStackTrace();

            }
        } else {
            Log.d(TAG, "responseChartsDataHandler response.unSuccessful()");
            fragment.showError();
            try {
                Log.d("TAG", response.errorBody().string());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void sortListByRank(ArrayList<CoinPojo> list, boolean isSortUp) {
        if (isSortUp) {
            Collections.sort(list, CoinPojo.sRankCoinComparatorUp);
        } else {
            Collections.sort(list, CoinPojo.sRankCoinComparatorDown);
        }
        fragment.setList(list);
    }

    public void sortListByPrice(ArrayList<CoinPojo> list, boolean isSortUp) {
        if (isSortUp) {
            Collections.sort(list, CoinPojo.sPriceCoinComparatorUp);
        } else {
            Collections.sort(list, CoinPojo.sPriceCoinComparatorDown);
        }
        fragment.setList(list);
    }

    public void sortListByCap(ArrayList<CoinPojo> list, boolean isSortUp) {
        if (isSortUp) {
            Collections.sort(list, CoinPojo.sCapCoinComparatorUp);
        } else {
            Collections.sort(list, CoinPojo.sCapCoinComparatorDown);
        }
        fragment.setList(list);
    }

    public void sortListBy1h(ArrayList<CoinPojo> list, boolean isSortUp) {
        if (isSortUp) {
            Collections.sort(list, CoinPojo.s1hCoinComparatorUp);
        } else {
            Collections.sort(list, CoinPojo.s1hCoinComparatorDown);
        }
        fragment.setList(list);
    }

    @Override
    public void unsubscribe() {
        mCompositeSubscription.unsubscribe();
    }

    private static class SaveCoinsAsync extends AsyncTask<List<CoinPojo>, Void, Void> {
        @Override
        protected Void doInBackground(List<CoinPojo>[] lists) {
            App.dbInstance.getCoinDao().deleteAll();
            App.dbInstance.getCoinDao().insertListCoinPojo(lists[0]);
            return null;
        }
    }
}
