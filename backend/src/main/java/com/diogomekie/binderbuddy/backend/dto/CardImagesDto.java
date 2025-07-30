package com.diogomekie.binderbuddy.backend.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CardImagesDto {
    public String small;
    public String large;

    public String getSmall() {
        return small;
    }

    public String getLarge() {
        return large;
    }
}
