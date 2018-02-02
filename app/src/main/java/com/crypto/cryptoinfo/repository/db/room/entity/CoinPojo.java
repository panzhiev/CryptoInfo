package com.crypto.cryptoinfo.repository.db.room.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Comparator;

@Entity(tableName = "coin")
public class CoinPojo {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "uid")
    private int uid;

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("symbol")
    @Expose
    private String symbol;
    @SerializedName("rank")
    @Expose
    private String rank;
    @SerializedName("price_usd")
    @Expose
    private String priceUsd;
    @SerializedName("price_btc")
    @Expose
    private String priceBtc;
    @SerializedName("24h_volume_usd")
    @Expose
    private String _24hVolumeUsd;
    @SerializedName("market_cap_usd")
    @Expose
    private String marketCapUsd;
    @SerializedName("available_supply")
    @Expose
    private String availableSupply;
    @SerializedName("total_supply")
    @Expose
    private String totalSupply;
    @SerializedName("percent_change_1h")
    @Expose
    private String percentChange1h;
    @SerializedName("percent_change_24h")
    @Expose
    private String percentChange24h;
    @SerializedName("percent_change_7d")
    @Expose
    private String percentChange7d;
    @SerializedName("last_updated")
    @Expose
    private String lastUpdated;

    private boolean isFavourite;

    /**
     * No args constructor for use in serialization
     */
    public CoinPojo() {
    }

    public CoinPojo(String id, String name, String symbol, String rank, String priceUsd, String priceBtc, String _24hVolumeUsd, String marketCapUsd, String availableSupply, String totalSupply, String percentChange1h, String percentChange24h, String percentChange7d, String lastUpdated) {
        super();
        this.id = id;
        this.name = name;
        this.symbol = symbol;
        this.rank = rank;
        this.priceUsd = priceUsd;
        this.priceBtc = priceBtc;
        this._24hVolumeUsd = _24hVolumeUsd;
        this.marketCapUsd = marketCapUsd;
        this.availableSupply = availableSupply;
        this.totalSupply = totalSupply;
        this.percentChange1h = percentChange1h;
        this.percentChange24h = percentChange24h;
        this.percentChange7d = percentChange7d;
        this.lastUpdated = lastUpdated;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public String getPriceUsd() {
        return priceUsd;
    }

    public void setPriceUsd(String priceUsd) {
        this.priceUsd = priceUsd;
    }

    public String getPriceBtc() {
        return priceBtc;
    }

    public void setPriceBtc(String priceBtc) {
        this.priceBtc = priceBtc;
    }

    public String get24hVolumeUsd() {
        return _24hVolumeUsd;
    }

    public void set24hVolumeUsd(String _24hVolumeUsd) {
        this._24hVolumeUsd = _24hVolumeUsd;
    }

    public String getMarketCapUsd() {
        return marketCapUsd;
    }

    public void setMarketCapUsd(String marketCapUsd) {
        this.marketCapUsd = marketCapUsd;
    }

    public String getAvailableSupply() {
        return availableSupply;
    }

    public void setAvailableSupply(String availableSupply) {
        this.availableSupply = availableSupply;
    }

    public String getTotalSupply() {
        return totalSupply;
    }

    public void setTotalSupply(String totalSupply) {
        this.totalSupply = totalSupply;
    }

    public String getPercentChange1h() {
        return percentChange1h;
    }

    public void setPercentChange1h(String percentChange1h) {
        this.percentChange1h = percentChange1h;
    }

    public String getPercentChange24h() {
        return percentChange24h;
    }

    public void setPercentChange24h(String percentChange24h) {
        this.percentChange24h = percentChange24h;
    }

    public String getPercentChange7d() {
        return percentChange7d;
    }

    public void setPercentChange7d(String percentChange7d) {
        this.percentChange7d = percentChange7d;
    }

    public String getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(String lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public boolean isFavourite() {
        return isFavourite;
    }

    public void setFavourite(boolean favourite) {
        isFavourite = favourite;
    }

    @Override
    public String toString() {
        return "CoinPojo{" +
                "uid=" + uid +
                ", id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", symbol='" + symbol + '\'' +
                ", rank='" + rank + '\'' +
                ", priceUsd='" + priceUsd + '\'' +
                ", priceBtc='" + priceBtc + '\'' +
                ", _24hVolumeUsd='" + _24hVolumeUsd + '\'' +
                ", marketCapUsd='" + marketCapUsd + '\'' +
                ", availableSupply='" + availableSupply + '\'' +
                ", totalSupply='" + totalSupply + '\'' +
                ", percentChange1h='" + percentChange1h + '\'' +
                ", percentChange24h='" + percentChange24h + '\'' +
                ", percentChange7d='" + percentChange7d + '\'' +
                ", lastUpdated='" + lastUpdated + '\'' +
                '}' + "\n";
    }

    public static Comparator<CoinPojo> sRankCoinComparatorUp = (c1, c2) -> {
        int rank1 = Integer.parseInt(c1.getRank().toUpperCase());
        int rank2 = Integer.parseInt(c2.getRank().toUpperCase());

        return rank1 - rank2;
    };

    public static Comparator<CoinPojo> sRankCoinComparatorDown = (c1, c2) -> {
        int rank1 = Integer.parseInt(c1.getRank().toUpperCase());
        int rank2 = Integer.parseInt(c2.getRank().toUpperCase());

        return rank2 - rank1;
    };

    public static Comparator<CoinPojo> sPriceCoinComparatorUp = (c1, c2) -> {

        String priceString1 = c1.getPriceUsd();
        String priceString2 = c2.getPriceUsd();
        if (priceString1 != null && priceString2 != null) {
            double price1 = Double.parseDouble(priceString1);
            double price2 = Double.parseDouble(priceString2);
            return Double.compare(price1, price2);
        }

        return 1;
    };

    public static Comparator<CoinPojo> sPriceCoinComparatorDown = (c1, c2) -> {

        String priceString1 = c1.getPriceUsd();
        String priceString2 = c2.getPriceUsd();
        if (priceString1 != null && priceString2 != null) {
            double price1 = Double.parseDouble(priceString1);
            double price2 = Double.parseDouble(priceString2);
            return Double.compare(price2, price1);
        }

        return 1;
    };

    public static Comparator<CoinPojo> sCapCoinComparatorUp = (c1, c2) -> {

        String capString1 = c1.getMarketCapUsd();
        if (capString1 == null) capString1 = "0";
        String capString2 = c2.getMarketCapUsd();
        if (capString2 == null) capString2 = "0";

        double cap1 = Double.parseDouble(capString1);
        double cap2 = Double.parseDouble(capString2);
        return Double.compare(cap1, cap2);
    };

    public static Comparator<CoinPojo> sCapCoinComparatorDown = (c1, c2) -> {

        String capString1 = c1.getMarketCapUsd();
        if (capString1 == null) capString1 = "0";
        String capString2 = c2.getMarketCapUsd();
        if (capString2 == null) capString2 = "0";

        double cap1 = Double.parseDouble(capString1);
        double cap2 = Double.parseDouble(capString2);
        return Double.compare(cap2, cap1);
    };

    public static Comparator<CoinPojo> s1hCoinComparatorUp = (c1, c2) -> {

        String persent1h1 = c1.getPercentChange1h();
        if (persent1h1 == null) persent1h1 = "0";
        String persent1h2 = c2.getPercentChange1h();
        if (persent1h2 == null) persent1h2 = "0";

        double percent1 = Double.parseDouble(persent1h1);
        double percent2 = Double.parseDouble(persent1h2);
        return Double.compare(percent1, percent2);
    };

    public static Comparator<CoinPojo> s1hCoinComparatorDown = (c1, c2) -> {

        String persent1h1 = c1.getPercentChange1h();
        if (persent1h1 == null) persent1h1 = "0";
        String persent1h2 = c2.getPercentChange1h();
        if (persent1h2 == null) persent1h2 = "0";

        double percent1 = Double.parseDouble(persent1h1);
        double percent2 = Double.parseDouble(persent1h2);
        return Double.compare(percent2, percent1);
    };
}
