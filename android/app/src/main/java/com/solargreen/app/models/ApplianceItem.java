package com.solargreen.app.models;

public class ApplianceItem {
    public String id;
    public String name;
    public float powerKw;
    public boolean isOn;
    public String category;

    public ApplianceItem(String id, String name, float powerKw, boolean isOn, String category) {
        this.id = id;
        this.name = name;
        this.powerKw = powerKw;
        this.isOn = isOn;
        this.category = category;
    }
}