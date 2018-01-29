package com.crypto.cryptoinfo.cryptoinfo.repository.network;

import com.crypto.cryptoinfo.cryptoinfo.repository.db.room.entity.CoinPojo;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

import static com.crypto.cryptoinfo.cryptoinfo.repository.network.ApiConstants.TICKERS;

public interface ApiInterface {

    @GET(TICKERS)
    Observable<List<CoinPojo>> getTickersWithConvert(@Query("convert") String usd);

    @GET(TICKERS)
    Observable<List<CoinPojo>> getAllTickers();

}