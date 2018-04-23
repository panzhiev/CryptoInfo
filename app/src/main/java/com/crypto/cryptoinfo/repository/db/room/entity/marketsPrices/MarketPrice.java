package com.crypto.cryptoinfo.repository.db.room.entity.marketsPrices;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverter;
import android.arch.persistence.room.TypeConverters;
import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Objects;

@Entity(tableName = "market_price")
public class MarketPrice {

    @TypeConverters(MarketPriceTypeConverters.class)
    private Price price;
    private String name;
    private String pair;

    @PrimaryKey()
    @NonNull
    private String namePair = "";

    public MarketPrice() {
    }

    public MarketPrice(Price price, @NonNull String namePair, String name, String pair) {
        this.price = price;
        this.namePair = namePair;
        this.name = name;
        this.pair = pair;
    }

    public Price getPrice() {
        return price;
    }

    public void setPrice(Price price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPair() {
        return pair;
    }

    public void setPair(String pair) {
        this.pair = pair;
    }

    @NonNull
    public String getNamePair() {
        return namePair;
    }

    public void setNamePair(@NonNull String namePair) {
        this.namePair = namePair;
    }

    @Override
    public String toString() {
        return "MarketPrice{" +
                "price=" + price +
                ", name='" + name + '\'' +
                ", pair='" + pair + '\'' +
                ", namePair='" + namePair + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MarketPrice that = (MarketPrice) o;
        return Objects.equals(namePair, that.namePair);
    }

    @Override
    public int hashCode() {
        return Objects.hash(namePair);
    }

    public static class MarketPriceTypeConverters {
        Gson gson = new Gson();

        @TypeConverter
        public Price jsonToPrice(String json) {
            if (json == null) {
                return null;
            }

            Type type = new TypeToken<Price>() {
            }.getType();

            return gson.fromJson(json, type);
        }

        @TypeConverter
        public String priceToJson(Price price) {
            return gson.toJson(price);
        }
    }
}
