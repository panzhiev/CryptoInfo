package com.crypto.cryptoinfo.repository.network;

import com.crypto.cryptoinfo.repository.db.room.entity.CoinPojo;
import com.google.gson.JsonElement;

import java.util.List;

import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

import static com.crypto.cryptoinfo.repository.network.ApiConstants.COIN_SNAPSHOT;
import static com.crypto.cryptoinfo.repository.network.ApiConstants.TICKERS;

public interface ApiInterface {

    @GET(TICKERS)
    Observable<List<CoinPojo>> getTickersWithConvert(@Query("convert") String usd);

    @GET(TICKERS)
    Observable<List<CoinPojo>> getAllTickers();

    @GET("{coin}/{pastTime}/{presentTime}/")
    Observable<Response<JsonElement>> getGraphsPerPeriod(
            @Path("coin") String coinFullName,
            @Path("pastTime") String pastTime,
            @Path("presentTime") String presentTime);

    @GET(COIN_SNAPSHOT)
    Observable<Response<JsonElement>> getCoinSnapshot(@Query("fsym") String fromSymbol, @Query("tsym") String toSymbol);
}