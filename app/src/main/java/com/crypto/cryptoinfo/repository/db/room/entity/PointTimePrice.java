package com.crypto.cryptoinfo.repository.db.room.entity;

public class PointTimePrice {

    private String unixTime;
    private String priceUsd;

    public PointTimePrice(String unixTime, String priceUsd) {
        this.unixTime = unixTime;
        this.priceUsd = priceUsd;
    }

    public String getUnixTime() {
        return unixTime;
    }

    public void setUnixTime(String unixTime) {
        this.unixTime = unixTime;
    }

    public String getPriceUsd() {
        return priceUsd;
    }

    public void setPriceUsd(String priceUsd) {
        this.priceUsd = priceUsd;
    }

    @Override
    public String toString() {
        return "PointTimePrice{" +
                "unixTime='" + unixTime + '\'' +
                ", priceUsd='" + priceUsd + '\'' +
                '}' + "\n";
    }
}
