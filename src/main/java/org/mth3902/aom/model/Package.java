package org.mth3902.aom.model;

import java.math.BigDecimal;

public class Package {

    private long id;
    private String name;
    private int period;
    private double price;
    private int dataAmount;
    private int minAmount;
    private int smsAmount;

    public Package(long id, String name, int period, double price, int dataAmount, int minAmount, int smsAmount) {
        this.id = id;
        this.name = name;
        this.period = period;
        this.price = price;
        this.dataAmount = dataAmount;
        this.minAmount = minAmount;
        this.smsAmount = smsAmount;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getPeriod() {
        return period;
    }

    public double getPrice() {
        return price;
    }

    public int getDataAmount() {
        return dataAmount;
    }

    public int getMinAmount() {
        return minAmount;
    }

    public int getSmsAmount() {
        return smsAmount;
    }

    @Override
    public String toString() {
        return "Package{" +
                "name=" + name +
                ", period=" + period +
                ", price=" + price +
                ", dataAmount=" + dataAmount +
                ", minAmount=" + minAmount +
                ", smsAmount=" + smsAmount +
                '}';
    }
}
