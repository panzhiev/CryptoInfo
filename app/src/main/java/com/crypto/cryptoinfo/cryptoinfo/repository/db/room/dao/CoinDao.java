package com.crypto.cryptoinfo.cryptoinfo.repository.db.room.dao;


import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.crypto.cryptoinfo.cryptoinfo.repository.db.room.entity.CoinPojo;

import java.util.List;

@Dao
public interface CoinDao {

    @Query("SELECT * FROM coin")
    LiveData<List<CoinPojo>> getAll();

    @Insert
    void insertAll(CoinPojo... coinPojos);

    @Insert
    void insertListCoinPojo(List<CoinPojo> coinPojoList);

    @Delete
    void deleteCoinPojo(CoinPojo coinPojo);

    @Query("DELETE FROM coin")
    void deleteAll();
}
