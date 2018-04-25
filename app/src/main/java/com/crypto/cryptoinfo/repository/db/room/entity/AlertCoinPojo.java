package com.crypto.cryptoinfo.repository.db.room.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.crypto.cryptoinfo.repository.db.sp.SharedPreferencesHelper;

import java.util.Objects;

@Entity(tableName = "alert_coin")
public class AlertCoinPojo {

    @PrimaryKey()
    @NonNull
    @ColumnInfo(name = "symbol")
    private String symbol = "BTC";
    private double high = 0.0;
    private double low = 0.0;
    private boolean oneTime = true;
    private String toCurrency = SharedPreferencesHelper.getInstance().getCurrentCurrency();

    public AlertCoinPojo() {
    }

    @Ignore
    public AlertCoinPojo(@NonNull String symbol, double high, double low, boolean oneTime, String toCurrency) {
        this.symbol = symbol;
        this.high = high;
        this.low = low;
        this.oneTime = oneTime;
        this.toCurrency = toCurrency;
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

    public String getToCurrency() {
        return toCurrency;
    }

    public void setToCurrency(String toCurrency) {
        this.toCurrency = toCurrency;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AlertCoinPojo that = (AlertCoinPojo) o;
        return Objects.equals(symbol, that.symbol);
    }

    @Override
    public int hashCode() {
        return Objects.hash(symbol);
    }

    @Override
    public String toString() {
        return "AlertCoinPojo{" +
                "symbol='" + symbol + '\'' +
                ", high=" + high +
                ", low=" + low +
                ", oneTime=" + oneTime +
                ", toCurrency='" + toCurrency + '\'' +
                '}';
    }
}
