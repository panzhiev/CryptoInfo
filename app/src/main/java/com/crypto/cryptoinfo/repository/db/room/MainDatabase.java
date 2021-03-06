package com.crypto.cryptoinfo.repository.db.room;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.crypto.cryptoinfo.repository.db.room.dao.AlertCoinDao;
import com.crypto.cryptoinfo.repository.db.room.dao.CoinDao;
import com.crypto.cryptoinfo.repository.db.room.dao.CoinFavDao;
import com.crypto.cryptoinfo.repository.db.room.dao.MarketPriceDao;
import com.crypto.cryptoinfo.repository.db.room.entity.AlertCoinPojo;
import com.crypto.cryptoinfo.repository.db.room.entity.CoinFavPojo;
import com.crypto.cryptoinfo.repository.db.room.entity.CoinPojo;
import com.crypto.cryptoinfo.repository.db.room.entity.marketsPrices.MarketPrice;

@Database(entities = {CoinPojo.class, CoinFavPojo.class, AlertCoinPojo.class, MarketPrice.class}, version = 1)
public abstract class MainDatabase extends RoomDatabase {

    public abstract CoinDao getCoinDao();

    public abstract CoinFavDao getCoinFavDao();

    public abstract AlertCoinDao getAlertCoinDao();

    public abstract MarketPriceDao getMarketPriceDao();

//    public static final Migration MIGRATION_1_2 = new Migration(1, 2) {
//        @Override
//        public void migrate(@NonNull SupportSQLiteDatabase database) {
//            // Create the new table
//            database.execSQL(
//                    "CREATE TABLE nvs (uid INTEGER, name TEXT, value TEXT, address TEXT, expires_in TEXT, PRIMARY KEY(uid))");
//        }
//    };
}
