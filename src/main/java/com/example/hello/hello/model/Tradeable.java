package com.example.hello.hello.model;

import java.math.BigDecimal;

public interface Tradeable {
    public String exhange(CurrencyModel toCurrency, BigDecimal amount);
}