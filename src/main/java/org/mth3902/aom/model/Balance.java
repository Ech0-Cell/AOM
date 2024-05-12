package org.mth3902.aom.model;

import java.math.BigDecimal;

public class Balance {

    private final String remData;
    private final String remSms;
    private final String remMin;
    private final BigDecimal remMoney;
    private final Long endDate;
    private final Long startDate;
    private final BigDecimal price;

    public Balance(String remData, String remSms, String remMin, BigDecimal remMoney, Long endDate, Long startDate, BigDecimal price) {
        this.remData = remData;
        this.remSms = remSms;
        this.remMin = remMin;
        this.remMoney = remMoney;
        this.endDate = endDate;
        this.startDate = startDate;
        this.price = price;
    }

    public String getRemData() {
        return remData;
    }

    public String getRemSms() {
        return remSms;
    }

    public String getRemMin() {
        return remMin;
    }

    public BigDecimal getRemMoney() {
        return remMoney;
    }

    public Long getEndDate() {
        return endDate;
    }

    public Long getStartDate() {
        return startDate;
    }

    public BigDecimal getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return "Balance{" +
                "remData='" + remData + '\'' +
                ", remSms='" + remSms + '\'' +
                ", remMin='" + remMin + '\'' +
                ", remMoney='" + remMoney + '\'' +
                ", endDate='" + endDate + '\'' +
                ", startDate='" + startDate + '\'' +
                ", price='" + price + '\'' +
                '}';
    }
}
