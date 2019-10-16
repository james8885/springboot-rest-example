package com.example.hello.hello.model;

import java.math.BigDecimal;

public interface Tradeable {
    public String exhange(ExchangeCurrency toCurrency, BigDecimal amount);
    public BigDecimal exchangeRate();
}