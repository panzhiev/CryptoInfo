package com.crypto.cryptoinfo.repository.db.room.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "alert_coin")
public class AlertCoinPojo {

    @PrimaryKey()
    @NonNull
    @ColumnInfo(name = "symbol")
    private String symbol = "BTC";
    private double high = 0.0;
    private double low = 0.0;
    private boolean oneTime = true;

    public AlertCoinPojo() {
    }

    @Ignore
    public AlertCoinPojo(String symbol, double high, double low, boolean oneTime) {
        this.symbol = symbol;
        this.high = high;
        this.low = low;
        this.oneTime = oneTime;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
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

        if (Double.compare(alertCoinPojo.high, high) != 0) return false;
        if (Double.compare(alertCoinPojo.low, low) != 0) return false;
        if (oneTime != alertCoinPojo.oneTime) return false;
        return symbol.equals(alertCoinPojo.symbol);
    }

    @Override
    public int hashCode() {
        return symbol.hashCode();
    }
}
