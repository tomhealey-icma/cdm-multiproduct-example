package com.finxis.models;

public class BondModel {


    public String getIsin() {
        return isin;
    }

    public void setIsin(String isin) {
        this.isin = isin;
    }

    public String getInstrumentName() {
        return instrumentName;
    }

    public void setInstrumentName(String instrumentName) {
        this.instrumentName = instrumentName;
    }


    public String getSettlementDate() {
        return settlementDate;
    }

    public void setSettlementDate(String settlementDate) {
        this.settlementDate = settlementDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getMaturityDate() {
        return maturityDate;
    }

    public void setMaturityDate(String maturityDate) {
        this.maturityDate = maturityDate;
    }

    public String getIssuer() {
        return issuer;
    }

    public void setIssuer(String issuer) {
        this.issuer = issuer;
    }

    public String getCouponRate() {
        return couponRate;
    }

    public void setCouponRate(String couponRate) {
        this.couponRate = couponRate;
    }

    public String getYield() {
        return yield;
    }

    public void setYield(String yield) {
        this.yield = yield;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }


    public String getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(String effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public String getPaymentFrequency() {
        return paymentFrequency;
    }

    public void setPaymentFrequency(String paymentFrequency) {
        this.paymentFrequency = paymentFrequency;
    }

    public String getDayCountMethod() {
        return dayCountMethod;
    }

    public void setDayCountMethod(String dayCountMethod) {
        this.dayCountMethod = dayCountMethod;
    }

    public String getDayCountFraction() {
        return dayCountFraction;
    }

    public void setDayCountFraction(String dayCountFraction) {
        this.dayCountFraction = dayCountFraction;
    }

    public String isin;
    public String cusip;
    public String series;
    public String instrumentName;
    public String effectiveDate;
    public String settlementDate;
    public String maturityDate;
    public String status;
    public String currency;
    public String issuer;
    public String couponRate;
    public String yield;
    public String country;
    public String paymentFrequency;
    public String dayCountMethod;
    public String dayCountFraction;

    public String[][] indexSchedule;
    public String[][] rateSchedule;


}
