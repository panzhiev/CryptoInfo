package com.crypto.cryptoinfo.repository.network;


import com.crypto.cryptoinfo.repository.db.room.entity.CoinPojo;
import com.google.gson.JsonElement;

import java.util.List;

import retrofit2.Response;
import rx.Observable;

import static com.crypto.cryptoinfo.repository.network.ApiConstants.BASE_URL_COINMARKETCAP;
import static com.crypto.cryptoinfo.repository.network.ApiConstants.BASE_URL_CRYPTOCOMPARE_NEW;
import static com.crypto.cryptoinfo.repository.network.ApiConstants.BASE_URL_GRAPHS;
import static com.crypto.cryptoinfo.repository.network.ApiConstants.BASE_URL_S2;

public class ModelImpl {

    public Observable<List<CoinPojo>> getAllTickers() {
        return RestClient.getApiInterface(BASE_URL_COINMARKETCAP).getAllTickers();
    }

    public Observable<List<CoinPojo>> getTickersWithConvert(String currencyToConvert) {
        return RestClient.getApiInterface(BASE_URL_COINMARKETCAP).getTickersWithConvert(currencyToConvert);
    }

    public Observable<Response<JsonElement>> getGraphsPerPeriod(String coinFullName, String pastTime, String presentTime) {
        return RestClient.getApiInterface(BASE_URL_GRAPHS).getGraphsPerPeriod(coinFullName, pastTime, presentTime);
    }

    public Observable<Response<JsonElement>> getCoinSnapshot(String fromSymbol, String toSymbol) {
        return RestClient.getApiInterface(BASE_URL_CRYPTOCOMPARE_NEW).getCoinSnapshot(fromSymbol, toSymbol);
    }

    public Observable<List<JsonElement>> getCoinIds() {
        return RestClient.getApiInterface(BASE_URL_S2).getCoinIds();
    }
}
