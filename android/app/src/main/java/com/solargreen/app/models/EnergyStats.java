package com.solargreen.app.models;

public class EnergyStats {

    public float lifetimeProductionKwh;
    public float lifetimeSelfConsumedKwh;
    public float lifetimeSavingsGnf;
    public float co2AvoidedKg;
    public int autonomyPercent;
    public int uptimeHours;
    public int lastMaintenanceMonth;

    public EnergyStats(float lifetimeProductionKwh, float lifetimeSelfConsumedKwh, float lifetimeSavingsGnf,
                       float co2AvoidedKg, int autonomyPercent, int uptimeHours, int lastMaintenanceMonth) {
        this.lifetimeProductionKwh = lifetimeProductionKwh;
        this.lifetimeSelfConsumedKwh = lifetimeSelfConsumedKwh;
        this.lifetimeSavingsGnf = lifetimeSavingsGnf;
        this.co2AvoidedKg = co2AvoidedKg;
        this.autonomyPercent = autonomyPercent;
        this.uptimeHours = uptimeHours;
        this.lastMaintenanceMonth = lastMaintenanceMonth;
    }
}
