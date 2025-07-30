package com.diogomekie.binderbuddy.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class AddCardToBinderRequest {
    @NotBlank(message = "Card API ID cannot be blank")
    private String apiCardId;

    @NotBlank(message = "Card name cannot be blank")
    private String name;

    private String imageUrl;

    private String cardSet;

    private String rarity;

    private Double price;

    @NotNull(message = "Binder ID cannot be null")
    private Long binderId;

    public AddCardToBinderRequest() {}

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

    public Long getBinderId() {
        return binderId;
    }

    public void setBinderId(Long binderId) {
        this.binderId = binderId;
    }
}
