package com.diogomekie.binderbuddy.backend.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class LegalitiesDto {
    public String standard;
    public String expanded;
    public String unlimited;
}
