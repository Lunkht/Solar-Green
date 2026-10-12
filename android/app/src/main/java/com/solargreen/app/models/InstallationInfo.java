package com.solargreen.app.models;

public class InstallationInfo {

    public String siteName;
    public String siteAddress;
    public String installerName;
    public String installDate;
    public String warrantyEnd;

    public String inverterModel;
    public String inverterSerial;
    public String inverterFirmware;
    public float inverterCapacityKw;

    public int panelCount;
    public int panelWattageW;
    public float totalCapacityKwp;
    public String panelOrientation;
    public String panelTilt;

    public String batteryModel;
    public float batteryCapacityKwh;

    public String networkName;
    public String ipAddress;
    public int signalPercent;
    public String connectionType;

    public InstallationInfo(String siteName, String siteAddress, String installerName, String installDate,
                            String warrantyEnd, String inverterModel, String inverterSerial, String inverterFirmware,
                            float inverterCapacityKw, int panelCount, int panelWattageW, float totalCapacityKwp,
                            String panelOrientation, String panelTilt, String batteryModel, float batteryCapacityKwh,
                            String networkName, String ipAddress, int signalPercent, String connectionType) {
        this.siteName = siteName;
        this.siteAddress = siteAddress;
        this.installerName = installerName;
        this.installDate = installDate;
        this.warrantyEnd = warrantyEnd;
        this.inverterModel = inverterModel;
        this.inverterSerial = inverterSerial;
        this.inverterFirmware = inverterFirmware;
        this.inverterCapacityKw = inverterCapacityKw;
        this.panelCount = panelCount;
        this.panelWattageW = panelWattageW;
        this.totalCapacityKwp = totalCapacityKwp;
        this.panelOrientation = panelOrientation;
        this.panelTilt = panelTilt;
        this.batteryModel = batteryModel;
        this.batteryCapacityKwh = batteryCapacityKwh;
        this.networkName = networkName;
        this.ipAddress = ipAddress;
        this.signalPercent = signalPercent;
        this.connectionType = connectionType;
    }

    public String getPanelSummary() {
        return panelCount + " x " + panelWattageW + " W (" + String.format("%.1f", totalCapacityKwp) + " kWp)";
    }
}
