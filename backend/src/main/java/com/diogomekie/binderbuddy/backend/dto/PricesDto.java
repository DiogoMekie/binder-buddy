package com.diogomekie.binderbuddy.backend.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PricesDto {
    @JsonProperty("holofoil")
    public PriceDetailsDto holofoil;
    @JsonProperty("reverseHolofoil")
    public PriceDetailsDto reverseHolofoil;
    @JsonProperty("normal")
    public PriceDetailsDto normal;
    @JsonProperty("1stEditionHolofoil")
    public PriceDetailsDto firstEditionHolofoil;

    public PriceDetailsDto getHolofoil() {
        return holofoil;
    }

    public PriceDetailsDto getReverseHolofoil() {
        return reverseHolofoil;
    }

    public PriceDetailsDto getNormal() {
        return normal;
    }

    public PriceDetailsDto getFirstEditionHolofoil() {
        return firstEditionHolofoil;
    }
}
