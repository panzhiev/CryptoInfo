package com.crypto.cryptoinfo.repository.db.room.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "alert_coin")
public class AlertCoinPojo {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "uid")
    private int uid;

    private String id = "BTC";
    private double high = 0.0;
    private double low = 0.0;
    private boolean oneTime = true;

    public AlertCoinPojo() {
    }

    public AlertCoinPojo(String id, double high, double low, boolean oneTime) {
        this.id = id;
        this.high = high;
        this.low = low;
        this.oneTime = oneTime;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getHigh() {
        return high;
    }

    public void setHigh(double high) {
        this.high = high;
    }

    public double getLow() {
        return low;
    }

    public void setLow(double low) {
        this.low = low;
    }

    public boolean isOneTime() {
        return oneTime;
    }

    public void setOneTime(boolean oneTime) {
        this.oneTime = oneTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AlertCoinPojo alertCoinPojo = (AlertCoinPojo) o;

        if (uid != alertCoinPojo.uid) return false;
        if (Double.compare(alertCoinPojo.high, high) != 0) return false;
        if (Double.compare(alertCoinPojo.low, low) != 0) return false;
        if (oneTime != alertCoinPojo.oneTime) return false;
        return id.equals(alertCoinPojo.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
