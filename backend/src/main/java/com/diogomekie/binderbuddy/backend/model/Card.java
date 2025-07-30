package com.diogomekie.binderbuddy.backend.model;

import jakarta.persistence.*;

@Entity
@Table(name = "cards")
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String apiCardId;

    private String name;
    private String imageUrl;
    private String cardSet;
    private String rarity;
    private Double price;

    @ManyToOne
    @JoinColumn(name = "binder_id", nullable = false)
    private Binder binder;

    public Card() {
    }

    public Card(String apiCardId, String name, String imageUrl, String cardSet, String rarity, Double price) {
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

    public String getCardSet(){
        return cardSet;
    }

    public void setCardSet(String set) {
        this.cardSet = set;
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

    public Binder getBinder() {
        return binder;
    }

    public void setBinder(Binder binder) {
        this.binder = binder;
    }
}
