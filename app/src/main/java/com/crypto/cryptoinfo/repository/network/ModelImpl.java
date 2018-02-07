package com.crypto.cryptoinfo.repository.network;


import com.crypto.cryptoinfo.repository.db.room.entity.CoinPojo;
import com.google.gson.JsonElement;

import java.util.List;

import retrofit2.Response;
import rx.Observable;

import static com.crypto.cryptoinfo.repository.network.ApiConstants.BASE_URL;
import static com.crypto.cryptoinfo.repository.network.ApiConstants.GRAFHS_BASE_URL;

public class ModelImpl {

    public Observable<List<CoinPojo>> getAllTickers() {
        return RestClient.getApiInterface(BASE_URL).getAllTickers();
    }

    public Observable<Response<JsonElement>> getGraphsPerPeriod(String coinFullName, String pastTime, String presentTime) {
        return RestClient.getApiInterface(GRAFHS_BASE_URL).getGraphsPerPeriod(coinFullName, pastTime, presentTime);
    }
}
