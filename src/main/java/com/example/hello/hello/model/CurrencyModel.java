package com.example.hello.hello.model;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class CurrencyModel implements Tradeable {
    private String code;
    private String name;
    private BigDecimal value;

    public CurrencyModel(String code, String name, BigDecimal value) {
        this.code = code;
        this.name = name;
        this.value = value;
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getValue() {
        return this.value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    @Override
    public String exhange(CurrencyModel toCurrency, BigDecimal amount) {
        BigDecimal baseRate = new BigDecimal(1);
        BigDecimal temp1 = baseRate.multiply(this.getValue());
        BigDecimal temp2 = temp1.divide(toCurrency.getValue(), 20, RoundingMode.HALF_UP);
        return temp2.multiply(amount).setScale(5, RoundingMode.HALF_UP).toString();
    }

}