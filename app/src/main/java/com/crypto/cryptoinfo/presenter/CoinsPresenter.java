package com.crypto.cryptoinfo.presenter;


import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;

import com.crypto.cryptoinfo.App;
import com.crypto.cryptoinfo.repository.db.room.entity.CoinPojo;
import com.crypto.cryptoinfo.repository.db.room.entity.ExchangePojo;
import com.crypto.cryptoinfo.repository.db.room.entity.PointTimePrice;
import com.crypto.cryptoinfo.repository.db.sp.SharedPreferencesHelper;
import com.crypto.cryptoinfo.repository.db.room.entity.marketsPrices.MarketPrice;
import com.crypto.cryptoinfo.repository.db.room.entity.marketsPrices.Price;
import com.crypto.cryptoinfo.ui.fragment.ILoadingView;
import com.crypto.cryptoinfo.utils.JsonUtils;
import com.crypto.cryptoinfo.utils.Utils;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import retrofit2.Response;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

import static com.crypto.cryptoinfo.utils.Constants.EUR;
import static com.crypto.cryptoinfo.utils.Constants.USD;

public class CoinsPresenter extends BasePresenter implements IPresenter {

    private static final String TAG = CoinsPresenter.class.getSimpleName();
    private ILoadingView fragment;
    private Subscription mSubscriptionCurrencies,
            mSubscriptionGetCharts,
            mSubscriptionGetCoinSnapshot,
            mSubscriptionZip,
            mSubscriptionGetCoinTicker,
            mSubscriptionGetMarketsPrices;
    private CompositeSubscription mCompositeSubscription = new CompositeSubscription();

    public CoinsPresenter(ILoadingView fragment) {
        super();
        if (fragment == null) {
            this.fragment = new ILoadingView() {
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
            };
        } else {
            this.fragment = fragment;
        }
    }

    public void getCurrenciesList() {

        mSubscriptionZip = Observable
                .zip(mModel.getTickersWithConvert(EUR).subscribeOn(Schedulers.newThread()),
                        mModel.getCoinIds().subscribeOn(Schedulers.newThread()),
                        (responseTickers, responseIds) -> {
                            CoinPojo coinPojo = responseTickers.get(0);

                            String priceUsd = coinPojo.getPriceUsd();
                            String priceEur = coinPojo.getPriceEur();

                            if (priceUsd != null && priceEur != null) {
                                float coefficientUsdEur = Float.parseFloat(coinPojo.getPriceUsd())
                                        / Float.parseFloat(coinPojo.getPriceEur());
                                SharedPreferencesHelper.getInstance().putCoefficent(coefficientUsdEur);
                            }

                            for (int i = 0; i < responseTickers.size(); i++) {
                                responseTickers.get(i).setNumId(responseIds.get(i).getAsJsonObject().get("id").toString());
                            }
                            return responseTickers;
                        })
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(() -> fragment.showProgressIndicator())
                .doOnTerminate(() -> fragment.hideProgressIndicator())
                .subscribe(this::responseCurrenciesHandler, e -> fragment.showError());

        mCompositeSubscription.add(mSubscriptionZip);
    }

    private void responseCurrenciesHandler(List<CoinPojo> coinPojos) {
        new SaveCoinsAsync(fragment).execute(coinPojos);
    }

    public void getMarketsPrices() {
        mSubscriptionGetMarketsPrices = mModel
                .getMarketsPrices()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(() -> fragment.showProgressIndicator())
                .doOnTerminate(() -> fragment.hideProgressIndicator())
                .subscribe(this::responseMarketsPricesHandler, e -> {
                    fragment.showError();
                    e.printStackTrace();
                });

        mCompositeSubscription.add(mSubscriptionGetMarketsPrices);

    }

    @SuppressLint("StaticFieldLeak")
    private void responseMarketsPricesHandler(JsonElement jsonElement) {

        Log.d(TAG, "responseMarketsPricesHandler: " + jsonElement.toString());

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {

                JsonObject jsonObject = jsonElement.getAsJsonObject();
                ArrayList<MarketPrice> marketPrices = new ArrayList<>();
                Gson gson = new Gson();
                if (jsonObject.has("result")) {
                    String jsonResult = jsonObject.get("result").toString();
                    Map<String, Object> map = JsonUtils.jsonToMap(jsonResult);

                    for (Map.Entry<String, Object> entry : map.entrySet()) {
                        String[] namePair = entry.getKey().split(":");
                        String name = Utils.capitalizeFirstLetter(namePair[0]);
                        String pair = namePair[1];
                        try {
                            JSONObject jsonObjectPrice = new JSONObject(gson.toJson(entry.getValue()));
                            Price price = gson.fromJson(jsonObjectPrice.getString("price"), Price.class);
                            marketPrices.add(new MarketPrice(price, entry.getKey(), name, pair));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    App.dbInstance.getMarketPriceDao().insertList(marketPrices);
                    SharedPreferencesHelper.getInstance().putLastUpdMarkets(String.valueOf(System.currentTimeMillis()));
                }
                return null;
            }
        }.execute();
    }

    public void getChartsData(String coin, String pastTime) {
        mSubscriptionGetCharts = mModel
                .getGraphsPerPeriod(coin, pastTime, String.valueOf(System.currentTimeMillis()))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(() -> fragment.showProgressIndicator())
                .doOnTerminate(() -> fragment.hideProgressIndicator())
                .subscribe(this::responseChartsDataHandler, e -> fragment.showError());

        mCompositeSubscription.add(mSubscriptionGetCharts);
    }

    public void getCoinSnapshot(@NonNull String fromSymbol, @NonNull String toSymbol) {

        mSubscriptionGetCoinSnapshot = mModel.getCoinSnapshot(fromSymbol, toSymbol)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(() -> fragment.showProgressIndicator())
                .doOnTerminate(() -> fragment.hideProgressIndicator())
                .subscribe(this::responseCoinSnapshotHandler, e -> fragment.showError());

        mCompositeSubscription.add(mSubscriptionGetCoinSnapshot);
    }

    public void getCoinTicker(String coin) {

        mSubscriptionGetCoinTicker = mModel.getTicker(coin)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(() -> fragment.showProgressIndicator())
                .doOnTerminate(() -> fragment.hideProgressIndicator())
                .subscribe(this::responseCoinTicker, e -> fragment.showError());

        mCompositeSubscription.add(mSubscriptionGetCoinTicker);

    }

    private void responseCoinTicker(CoinPojo coinPojo) {
        //TODO:
    }

    private void responseCoinSnapshotHandler(Response<JsonElement> response) {
        Log.d(TAG, "responseCoinSnapshotHandler started");
        JSONObject jsonObject;
        ArrayList<ExchangePojo> exchangePojoArrayList = new ArrayList<>();

        if (response.isSuccessful()) {

            Log.d(TAG, "responseCoinSnapshotHandler response.isSuccessful()");
            try {

                jsonObject = new JSONObject(response.body().toString());
                Log.d(TAG, jsonObject.toString());

                JSONArray jsonArrayExchanges = jsonObject
                        .getJSONObject("Data")
                        .getJSONArray("Exchanges");

                for (int i = 0; i < jsonArrayExchanges.length(); i++) {
                    JSONObject jsonObjectMarket = (JSONObject) jsonArrayExchanges.get(i);
//                    Log.d(TAG, jsonObjectMarket.getString("MARKET") + "\n" + jsonObjectMarket.getString("PRICE"));

                    exchangePojoArrayList.add(new ExchangePojo(
                            jsonObjectMarket.getString("MARKET"),
                            jsonObjectMarket.getString("PRICE"),
                            jsonObjectMarket.getString("LASTUPDATE")
                    ));
                }

                fragment.setList(exchangePojoArrayList);

            } catch (NullPointerException | JSONException e) {
                e.printStackTrace();
                fragment.setList(exchangePojoArrayList);
            }
        }
    }

    private void responseChartsDataHandler(Response<JsonElement> response) {
        Log.d(TAG, "responseChartsDataHandler started");
        JSONObject jsonObject;
        if (response.isSuccessful()) {
            Log.d(TAG, "responseChartsDataHandler response.isSuccessful()");
            try {
                jsonObject = new JSONObject(response.body().toString());
                Log.d(TAG, jsonObject.toString());

                ArrayList<PointTimePrice> list = new ArrayList<>();

                switch (SharedPreferencesHelper.getInstance().getCurrentCurrency()) {
                    case USD:
                        JSONArray jsonArrayUsd = jsonObject.getJSONArray("price_usd");
                        //adding the first fake item to draw highlight vertical line correctly
                        for (int i = 0; i < jsonArrayUsd.length(); i++) {
                            JSONArray jsonArray1 = jsonArrayUsd.getJSONArray(i);
                            list.add(new PointTimePrice(jsonArray1.getString(0), jsonArray1.getString(1)));
                        }
                        break;
                    case EUR:
                        JSONArray jsonArray = jsonObject.getJSONArray("price_usd");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONArray jsonArray1 = jsonArray.getJSONArray(i);
                            String unixTime = jsonArray1.getString(0);
                            String price = jsonArray1.getString(1);
                            float coef = SharedPreferencesHelper.getInstance().getCoefficent();
                            String priceEur = String.valueOf(Float.parseFloat(price) / coef);
                            list.add(new PointTimePrice(unixTime, priceEur));
                        }
                        break;
                    default:
                        JSONArray jsonArrayBtc = jsonObject.getJSONArray("price_btc");
                        Log.d(TAG, "responseChartsDataHandler: jsonArrayBtc " + jsonArrayBtc.toString());
                        for (int i = 0; i < jsonArrayBtc.length(); i++) {
                            JSONArray jsonArray1 = jsonArrayBtc.getJSONArray(i);
                            list.add(new PointTimePrice(jsonArray1.getString(0), jsonArray1.getString(1)));
                        }
                        break;
                }

                fragment.setList(list);

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

        ILoadingView f;

        SaveCoinsAsync(ILoadingView f) {
            this.f = f;
        }

        @Override
        protected Void doInBackground(List<CoinPojo>[] lists) {
            App.dbInstance.getCoinDao().deleteAll();
            App.dbInstance.getCoinDao().insertListCoinPojo(lists[0]);
            SharedPreferencesHelper.getInstance().putLastUpdAllCoins(String.valueOf(System.currentTimeMillis()));
            f.notifyForChanges();
            return null;
        }
    }
}
