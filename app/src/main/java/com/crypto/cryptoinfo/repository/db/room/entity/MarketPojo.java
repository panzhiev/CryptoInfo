package com.crypto.cryptoinfo.repository.db.room.entity;


public class MarketPojo {

    private String name;
    private String price;
    private String lastUpdate;

    public MarketPojo(String name, String price, String lastUpdate) {
        this.name = name;
        this.price = price;
        this.lastUpdate = lastUpdate;
    }

    @Override
    public String toString() {
        return "MarketPojo{" +
                "name='" + name + '\'' +
                ", price='" + price + '\'' +
                ", lastUpdate='" + lastUpdate + '\'' +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(String lastUpdate) {
        this.lastUpdate = lastUpdate;
    }
}
