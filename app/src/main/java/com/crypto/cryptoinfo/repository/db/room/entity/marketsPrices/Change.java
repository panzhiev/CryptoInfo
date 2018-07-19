package com.crypto.cryptoinfo.repository.db.room.entity.marketsPrices;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Change implements Serializable {

    @SerializedName("percentage")
    @Expose
    private Double percentage;
    @SerializedName("absolute")
    @Expose
    private Double absolute;

    @Override
    public String toString() {
        return "Change{" +
                "percentage=" + percentage +
                ", absolute=" + absolute +
                '}' + "\n";
    }

    public Double getPercentage() {
        return percentage;
    }

    public void setPercentage(Double percentage) {
        this.percentage = percentage;
    }

    public Double getAbsolute() {
        return absolute;
    }

    public void setAbsolute(Double absolute) {
        this.absolute = absolute;
    }
}