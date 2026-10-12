package com.solargreen.app.models;

public class Product {
    public final String id;
    public final String name;
    public final String type;
    public final String category;
    public final String price;
    public final String priceCfa;
    public final String tagline;
    public final String description;
    public final String imageUrl;
    public final String[] specLabels;
    public final String[] specValues;

    public Product(String id, String name, String type, String category,
                   String price, String priceCfa, String tagline, String description,
                   String imageUrl, String[] specLabels, String[] specValues) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.category = category;
        this.price = price;
        this.priceCfa = priceCfa;
        this.tagline = tagline;
        this.description = description;
        this.imageUrl = imageUrl;
        this.specLabels = specLabels;
        this.specValues = specValues;
    }
}