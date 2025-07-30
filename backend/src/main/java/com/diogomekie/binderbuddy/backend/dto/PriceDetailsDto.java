package com.diogomekie.binderbuddy.backend.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PriceDetailsDto {
    public Double low;
    public Double mid;
    public Double high;
    public Double market;
    public Double directLow;


    public Double getMarket() {
        return market;
    }
}
