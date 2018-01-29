package com.crypto.cryptoinfo.cryptoinfo.presenter;


import com.crypto.cryptoinfo.cryptoinfo.App;
import com.crypto.cryptoinfo.cryptoinfo.repository.db.room.entity.CoinPojo;
import com.crypto.cryptoinfo.cryptoinfo.repository.network.RestClient;
import com.crypto.cryptoinfo.cryptoinfo.ui.fragment.ILoadingView;

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
//        fragment.setList((ArrayList<CoinPojo>) coinPojos);
        App.dbInstance.getCoinDao().insertListCoinPojo(coinPojos);
    }

    @Override
    public void unsubscribe() {
        mCompositeSubscription.unsubscribe();
    }
}
