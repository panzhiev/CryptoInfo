package com.crypto.cryptoinfo.repository.db.room.entity.marketsPrices;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Price implements Serializable
{

    @SerializedName("last")
    @Expose
    private Double last;
    @SerializedName("high")
    @Expose
    private Double high;
    @SerializedName("low")
    @Expose
    private Double low;
    @SerializedName("change")
    @Expose
    private Change change;

    @Override
    public String toString() {
        return "Price{" +
                "last=" + last +
                ", high=" + high +
                ", low=" + low +
                ", change=" + change +
                '}' + "\n";
    }

    public Double getLast() {
        return last;
    }

    public void setLast(Double last) {
        this.last = last;
    }

    public Double getHigh() {
        return high;
    }

    public void setHigh(Double high) {
        this.high = high;
    }

    public Double getLow() {
        return low;
    }

    public void setLow(Double low) {
        this.low = low;
    }

    public Change getChange() {
        return change;
    }

    public void setChange(Change change) {
        this.change = change;
    }
}