package com.diogomekie.binderbuddy.backend.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TcgplayerDto {
    public String url;
    public String updatedAt;
    public PricesDto prices;

    public PricesDto getPrices() {
        return prices;
    }
}
