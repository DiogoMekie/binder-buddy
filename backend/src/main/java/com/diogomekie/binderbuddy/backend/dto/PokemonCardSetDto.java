package com.diogomekie.binderbuddy.backend.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PokemonCardSetDto {
    public String id;
    public String name;
    public String series;
    public String printedTotal;
    public String total;
    public LegalitiesDto legalities;
    public String ptcgoCode;
    public String releaseDate;
    public String symbol;
    public String logo;

    public String getName() {
        return name;
    }
}
