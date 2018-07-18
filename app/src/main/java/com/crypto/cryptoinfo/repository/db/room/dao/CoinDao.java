package com.crypto.cryptoinfo.repository.db.room.dao;


import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.crypto.cryptoinfo.repository.db.room.entity.CoinPojo;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface CoinDao {

    @Query("SELECT * FROM coin")
    List<CoinPojo> getAll();

    @Query("SELECT * FROM coin")
    LiveData<List<CoinPojo>> getAllAsLiveData();

//    @Query("SELECT * FROM coin WHERE isFavourite LIKE 1")
//    LiveData<List<CoinPojo>> getFavourites();

    @Query("SELECT * FROM coin "
            + "INNER JOIN fav ON coin.id = fav.id")
    LiveData<List<CoinPojo>> getFavourites();

    @Query("SELECT * FROM coin "
            + "INNER JOIN alert_coin ON coin.symbol = alert_coin.symbol")
    List<CoinPojo> getAlerts();

    @Insert(onConflict = REPLACE)
    void insertAll(CoinPojo... coinPojos);

    @Insert(onConflict = REPLACE)
    void insertListCoinPojo(List<CoinPojo> coinPojoList);

    @Delete
    void deleteCoinPojo(CoinPojo coinPojo);

    @Query("DELETE FROM coin")
    void deleteAll();
}
