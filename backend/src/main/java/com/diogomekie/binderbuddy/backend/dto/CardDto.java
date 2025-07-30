package com.diogomekie.binderbuddy.backend.dto;

public class CardDto {
    private Long id;
    private String apiCardId;
    private String name;
    private String imageUrl;
    private String cardSet;
    private String rarity;
    private Double price;

    public CardDto() {
    }

    public CardDto(Long id, String apiCardId, String name, String imageUrl, String cardSet, String rarity, Double price) {
        this.id = id;
        this.apiCardId = apiCardId;
        this.name = name;
        this.imageUrl = imageUrl;
        this.cardSet = cardSet;
        this.rarity = rarity;
        this.price = price;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getApiCardId() {
        return apiCardId;
    }

    public void setApiCardId(String apiCardId) {
        this.apiCardId = apiCardId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getCardSet() {
        return cardSet;
    }

    public void setCardSet(String cardSet) {
        this.cardSet = cardSet;
    }

    public String getRarity() {
        return rarity;
    }

    public void setRarity(String rarity) {
        this.rarity = rarity;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
}
