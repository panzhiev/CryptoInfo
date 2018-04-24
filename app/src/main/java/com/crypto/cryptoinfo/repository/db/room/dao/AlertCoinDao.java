package com.crypto.cryptoinfo.repository.db.room.dao;


import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.crypto.cryptoinfo.repository.db.room.entity.AlertCoinPojo;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface AlertCoinDao {

    @Query("SELECT * FROM alert_coin")
    List<AlertCoinPojo> getAll();

    @Query("SELECT * FROM alert_coin WHERE symbol LIKE :symbol")
    AlertCoinPojo getAlertCoin(String symbol);

    @Query("SELECT * FROM alert_coin")
    LiveData<List<AlertCoinPojo>> getAllAsLiveData();

//    @Query("SELECT * FROM coin WHERE isFavourite LIKE 1")
//    LiveData<List<CoinPojo>> getFavourites();

//    @Query("SELECT * FROM alert_coin "
//            + "INNER JOIN fav ON coin.id = fav.id")
//    LiveData<List<CoinPojo>> getFavourites();

    @Insert(onConflict = REPLACE)
    void insertAll(AlertCoinPojo... alertCoinPojos);

    @Insert
    void insertListAlertCoinPojos(List<AlertCoinPojo> alertCoinPojos);

    @Delete
    void deleteAlertCoinPojo(AlertCoinPojo alertCoinPojo);

    @Query("DELETE FROM alert_coin WHERE symbol = :symbol")
    void deleteAlert(String symbol);

    @Query("DELETE FROM alert_coin")
    void deleteAll();
}
