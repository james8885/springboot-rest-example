package com.example.hello.hello.model;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class ExchangeCurrency extends Currency{
    private String code;
    private BigDecimal value;

    public ExchangeCurrency(String code, String name, BigDecimal value) {
        this.code = code;
        super.setName(name);
        this.value = value;
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public BigDecimal getValue() {
        return this.value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    @Override
    public String exhange(ExchangeCurrency toCurrency, BigDecimal amount) {
        BigDecimal baseRate = new BigDecimal(1);
        BigDecimal temp1 = baseRate.multiply(this.getValue());
        BigDecimal temp2 = temp1.divide(toCurrency.getValue(), 20, RoundingMode.HALF_UP);
        return temp2.multiply(amount).setScale(5, RoundingMode.HALF_UP).toString();
    }

    @Override
    public BigDecimal exchangeRate() {
        return getValue();
    }

}