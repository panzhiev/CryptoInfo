package com.crypto.cryptoinfo.repository.db.room.dao;


import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.crypto.cryptoinfo.repository.db.room.entity.CoinFavPojo;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface CoinFavDao {

    @Query("SELECT * FROM fav")
    List<CoinFavPojo> getAll();

    @Query("SELECT * FROM fav")
    LiveData<List<CoinFavPojo>> getAllAsLiveData();

    @Insert(onConflict = REPLACE)
    void insertAll(CoinFavPojo... coinFavPojos);

    @Insert
    void insertListCoinFavPojo(List<CoinFavPojo> coinFavPojos);

    @Delete
    void deleteCoinFavPojo(CoinFavPojo coinFavPojo);

    @Query("DELETE FROM fav WHERE id = :id")
    void deleteFav(String id);

    @Query("DELETE FROM fav")
    void deleteAll();
}
