package com.crypto.cryptoinfo.repository.db.supportPojo;

public class AlertCoin {

    private String coin = "BTC";
    private float high = 0.0f;
    private float low = 0.0f;
    private boolean oneTime = true;

    public String getCoin() {
        return coin;
    }

    public void setCoin(String coin) {
        this.coin = coin;
    }

    public float getHigh() {
        return high;
    }

    public void setHigh(float high) {
        this.high = high;
    }

    public float getLow() {
        return low;
    }

    public void setLow(float low) {
        this.low = low;
    }

    public boolean isOneTime() {
        return oneTime;
    }

    public void setOneTime(boolean oneTime) {
        this.oneTime = oneTime;
    }
}
