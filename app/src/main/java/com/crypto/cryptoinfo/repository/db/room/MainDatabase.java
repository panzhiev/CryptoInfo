package com.crypto.cryptoinfo.repository.db.room;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.crypto.cryptoinfo.repository.db.room.dao.CoinDao;
import com.crypto.cryptoinfo.repository.db.room.entity.CoinPojo;

@Database(entities = {CoinPojo.class}, version = 1)
public abstract class MainDatabase extends RoomDatabase {

    public abstract CoinDao getCoinDao();

//    public static final Migration MIGRATION_1_2 = new Migration(1, 2) {
//        @Override
//        public void migrate(@NonNull SupportSQLiteDatabase database) {
//            // Create the new table
//            database.execSQL(
//                    "CREATE TABLE nvs (uid INTEGER, name TEXT, value TEXT, address TEXT, expires_in TEXT, PRIMARY KEY(uid))");
//        }
//    };
}
