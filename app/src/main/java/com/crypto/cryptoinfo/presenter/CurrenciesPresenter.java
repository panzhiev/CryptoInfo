package com.crypto.cryptoinfo.presenter;


import com.crypto.cryptoinfo.App;
import com.crypto.cryptoinfo.repository.db.room.entity.CoinPojo;
import com.crypto.cryptoinfo.repository.network.RestClient;
import com.crypto.cryptoinfo.ui.fragment.ILoadingView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public class CurrenciesPresenter implements IPresenter {

    private final String TAG = getClass().getSimpleName();
    private ILoadingView fragment;
    private Subscription mSubscriptionCurrencies;
    private CompositeSubscription mCompositeSubscription = new CompositeSubscription();

    public CurrenciesPresenter(ILoadingView fragment) {
        this.fragment = fragment;
    }

    public void getCurrenciesList() {

        mSubscriptionCurrencies = RestClient.getApiInterface()
                .getAllTickers()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(() -> fragment.showProgressIndicator())
                .doOnTerminate(() -> fragment.hideProgressIndicator())
                .subscribe(this::responseCurrenciesHandler, e -> fragment.showError());

        mCompositeSubscription.add(mSubscriptionCurrencies);
    }

    private void responseCurrenciesHandler(List<CoinPojo> coinPojos) {
        App.dbInstance.getCoinDao().deleteAll();
        App.dbInstance.getCoinDao().insertListCoinPojo(coinPojos);
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

    @Override
    public void unsubscribe() {
        mCompositeSubscription.unsubscribe();
    }
}
