package com.ingemark.demo.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ingemark.demo.util.Constants;

public class HnbResponse {
    @JsonProperty(Constants.EXCHANGE_RATE)
    private String exchangeRate;

    public String getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(String exchangeRate) {
        this.exchangeRate = exchangeRate;
    }
}
