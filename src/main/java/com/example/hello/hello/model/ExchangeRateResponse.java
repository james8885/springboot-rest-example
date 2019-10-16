package com.example.hello.hello.model;

import java.io.Serializable;

import com.example.hello.hello.model.ExchangeCurrency;
import com.example.hello.hello.model.base.BaseResponse;

public class ExchangeRateResponse extends BaseResponse implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private ExchangeCurrency currency;


    public ExchangeRateResponse(ExchangeCurrency currency) {
        setErrorCode(0);
        setErrorMessage("Success");
        this.currency = currency;
    }
}