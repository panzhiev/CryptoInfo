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
import static com.crypto.cryptoinfo.repository.network.ApiConstants.PATH_QUICK_SEARCH;
import static com.crypto.cryptoinfo.repository.network.ApiConstants.TICKERS;
import static com.crypto.cryptoinfo.repository.network.ApiConstants.TICKER_SPECIFIC;

public interface ApiInterface {

    @GET(TICKERS)
    Observable<List<CoinPojo>> getTickersWithConvert(@Query("convert") String currency);

    @GET(TICKERS)
    Observable<List<CoinPojo>> getAllTickers();

    @GET(TICKER_SPECIFIC)
    Observable<CoinPojo> getTicker(@Path("id") String id);

    @GET("{coin}/{pastTime}/{presentTime}/")
    Observable<Response<JsonElement>> getGraphsPerPeriod(
            @Path("coin") String coinFullName,
            @Path("pastTime") String pastTime,
            @Path("presentTime") String presentTime);

    @GET(COIN_SNAPSHOT)
    Observable<Response<JsonElement>> getCoinSnapshot(@Query("fsym") String fromSymbol, @Query("tsym") String toSymbol);

    @GET(PATH_QUICK_SEARCH)
    Observable<List<JsonElement>> getCoinIds();
}