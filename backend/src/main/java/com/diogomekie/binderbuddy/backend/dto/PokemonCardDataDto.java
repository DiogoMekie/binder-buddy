package com.diogomekie.binderbuddy.backend.dto;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PokemonCardDataDto {
    public String id;
    public String name;
    public String supertype;
    public String[] subtypes;
    public String hp;
    public String[] types;
    public String evolvesFrom;
    public String[] abilities;
    public String[] attacks;
    public String[] weaknesses;
    public String[] resistances;
    public String[] retreatCost;
    public int convertedRetreatCost;
    public PokemonCardSetDto set;
    public String number;
    public String artist;
    public String rarity;
    public String flavorText;
    public String[] nationalPokedexNumbers;
    public LegalitiesDto legalities;
    public String regulationMark;
    public TcgplayerDto tcgplayer;
    public CardImagesDto images;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getImageUrl() {
        return images != null ? images.small : null;
    }

    public PokemonCardSetDto getSet() {
        return set;
    }

    public String getRarity() {
        return rarity;
    }

    public TcgplayerDto getTcgplayer() {
        return tcgplayer;
    }
}
