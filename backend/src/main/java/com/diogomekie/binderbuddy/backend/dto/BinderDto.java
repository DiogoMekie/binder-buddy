package com.diogomekie.binderbuddy.backend.dto;

import java.util.List;

// DTO para representar uma Binder na resposta da API
public class BinderDto {
    private Long id;
    private String name;
    private String description;
    private Long userId;
    private List<CardDto> cards;
    private Double totalPrice;

    public BinderDto() {
    }

    public BinderDto(Long id, String name, String description, Long userId, List<CardDto> cards, Double totalPrice) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.userId = userId;
        this.cards = cards;
        this.totalPrice = totalPrice;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public List<CardDto> getCards() {
        return cards;
    }

    public void setCards(List<CardDto> cards) {
        this.cards = cards;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }
}
