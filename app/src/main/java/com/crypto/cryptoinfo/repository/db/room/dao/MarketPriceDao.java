package com.crypto.cryptoinfo.repository.db.room.dao;


import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.crypto.cryptoinfo.repository.db.room.entity.AlertCoinPojo;
import com.crypto.cryptoinfo.repository.db.room.entity.marketsPrices.MarketPrice;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface MarketPriceDao {

    @Query("SELECT * FROM market_price")
    List<MarketPrice> getAll();

    @Query("SELECT * FROM market_price")
    LiveData<List<MarketPrice>> getAllAsLiveData();

    @Query("SELECT * FROM market_price WHERE pair LIKE :pair")
    List<MarketPrice> getMarketsWithPair(String pair);

    @Query("SELECT * FROM market_price WHERE pair LIKE :pair")
    LiveData<List<MarketPrice>> getMarketsWithPairLiveData(String pair);

//    @Query("SELECT * FROM coin WHERE isFavourite LIKE 1")
//    LiveData<List<CoinPojo>> getFavourites();

//    @Query("SELECT * FROM alert_coin "
//            + "INNER JOIN fav ON coin.id = fav.id")
//    LiveData<List<CoinPojo>> getFavourites();

    @Insert(onConflict = REPLACE)
    void insertAll(MarketPrice... marketPrices);

    @Insert(onConflict = REPLACE)
    void insertList(List<MarketPrice> marketPrices);

    @Delete
    void delete(MarketPrice marketPrice);

//    @Query("DELETE FROM market_price WHERE pair = :pair")
//    void deleteAlert(String pair);

    @Query("DELETE FROM market_price")
    void deleteAll();
}
