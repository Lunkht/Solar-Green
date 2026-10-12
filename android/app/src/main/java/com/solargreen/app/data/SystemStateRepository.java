package com.solargreen.app.data;

import com.solargreen.app.models.ApplianceItem;
import com.solargreen.app.models.NotificationItem;

import java.util.ArrayList;
import java.util.List;

public class SystemStateRepository {

    public interface StateChangeListener {
        void onStateChanged();
    }

    private static SystemStateRepository instance;

    private final List<StateChangeListener> listeners = new ArrayList<>();

    // Live System State
    private boolean isSystemOn = true;
    private String statusText = "En ligne";
    private float currentPowerKw = 4.8f;
    private float solarProductionKw = 5.2f;
    private float batteryPercent = 72.0f;
    private float batteryKwh = 7.2f;
    private float homeConsumptionKw = 2.4f;
    private float todayTotalKwh = 12.6f;

    private String operatingMode = "Autonome";
    private String operatingModeSub = "Normal";

    // Inverter Settings State
    private String outputPriority = "Solaire / Batterie / Réseau (SBU)";
    private String acInputRange = "APL (90V - 280V)";
    private int maxChargingCurrentA = 60;
    private boolean inverterBuzzerOn = true;
    private boolean energySaverOn = false;

    private final List<ApplianceItem> appliances = new ArrayList<>();
    private final List<NotificationItem> notifications = new ArrayList<>();

    private SystemStateRepository() {
        initDefaultAppliances();
        initDefaultNotifications();
    }

    public static synchronized SystemStateRepository getInstance() {
        if (instance == null) {
            instance = new SystemStateRepository();
        }
        return instance;
    }

    private void initDefaultAppliances() {
        appliances.add(new ApplianceItem("1", "Climatisation salon", 1.2f, true, "HVAC"));
        appliances.add(new ApplianceItem("2", "Chauffe-eau solaire", 0.8f, true, "Eau"));
        appliances.add(new ApplianceItem("3", "Pompe de piscine", 0.6f, false, "Jardin"));
        appliances.add(new ApplianceItem("4", "Borne de recharge VE", 3.7f, false, "Véhicule"));
        appliances.add(new ApplianceItem("5", "Éclairage intelligent", 0.2f, true, "Maison"));
    }

    private void initDefaultNotifications() {
        notifications.add(new NotificationItem("1", "Production optimale", "Pic de production atteint à 12h30 (5.2 kW).", "12:30", false));
        notifications.add(new NotificationItem("2", "Mode Autonome actif", "Passage automatique sur batterie et solaire.", "10:15", false));
        notifications.add(new NotificationItem("3", "Batterie à 72%", "Niveau de charge suffisant pour la soirée.", "09:00", false));
    }

    public void addListener(StateChangeListener listener) {
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    public void removeListener(StateChangeListener listener) {
        listeners.remove(listener);
    }

    private void notifyListeners() {
        for (StateChangeListener listener : new ArrayList<>(listeners)) {
            listener.onStateChanged();
        }
    }

    // System Power Toggle
    public void toggleSystemPower() {
        isSystemOn = !isSystemOn;
        if (isSystemOn) {
            statusText = "En ligne";
            currentPowerKw = 4.8f;
            solarProductionKw = 5.2f;
            homeConsumptionKw = recalculateConsumption();
        } else {
            statusText = "Hors ligne / Veille";
            currentPowerKw = 0.0f;
            solarProductionKw = 0.0f;
            homeConsumptionKw = 0.0f;
        }
        notifications.add(0, new NotificationItem(
                String.valueOf(System.currentTimeMillis()),
                isSystemOn ? "Système Démarré" : "Système Éteint",
                isSystemOn ? "L'installation produit et alimente la maison." : "Système mis en mode veille.",
                "À l'instant", false
        ));
        notifyListeners();
    }

    public void setOperatingMode(String mode, String status) {
        this.operatingMode = mode;
        this.operatingModeSub = status;
        notifications.add(0, new NotificationItem(
                String.valueOf(System.currentTimeMillis()),
                "Changement de Mode",
                "Mode de fonctionnement modifié vers : " + mode,
                "À l'instant", false
        ));
        notifyListeners();
    }

    public void updateInverterSettings(String priority, String inputRange, int chargeCurrentA, boolean buzzer, boolean energySaver) {
        this.outputPriority = priority;
        this.acInputRange = inputRange;
        this.maxChargingCurrentA = chargeCurrentA;
        this.inverterBuzzerOn = buzzer;
        this.energySaverOn = energySaver;
        notifications.add(0, new NotificationItem(
                String.valueOf(System.currentTimeMillis()),
                "Réglages Onduleur",
                "Paramètres appliqués : " + priority + ", Charge: " + chargeCurrentA + "A",
                "À l'instant", false
        ));
        notifyListeners();
    }

    public void toggleAppliance(String id) {
        for (ApplianceItem app : appliances) {
            if (app.id.equals(id)) {
                app.isOn = !app.isOn;
                break;
            }
        }
        if (isSystemOn) {
            homeConsumptionKw = recalculateConsumption();
        }
        notifyListeners();
    }

    private float recalculateConsumption() {
        float total = 0.4f; // Base idle load
        for (ApplianceItem app : appliances) {
            if (app.isOn) {
                total += app.powerKw;
            }
        }
        return (float) (Math.round(total * 10.0) / 10.0);
    }

    public void markAllNotificationsRead() {
        for (NotificationItem n : notifications) {
            n.isRead = true;
        }
        notifyListeners();
    }

    // Getters
    public boolean isSystemOn() { return isSystemOn; }
    public String getStatusText() { return statusText; }
    public float getCurrentPowerKw() { return currentPowerKw; }
    public float getSolarProductionKw() { return solarProductionKw; }
    public float getBatteryPercent() { return batteryPercent; }
    public float getBatteryKwh() { return batteryKwh; }
    public float getHomeConsumptionKw() { return homeConsumptionKw; }
    public float getTodayTotalKwh() { return todayTotalKwh; }
    public String getOperatingMode() { return operatingMode; }
    public String getOperatingModeSub() { return operatingModeSub; }

    public String getOutputPriority() { return outputPriority; }
    public String getAcInputRange() { return acInputRange; }
    public int getMaxChargingCurrentA() { return maxChargingCurrentA; }
    public boolean isInverterBuzzerOn() { return inverterBuzzerOn; }
    public boolean isEnergySaverOn() { return energySaverOn; }

    public List<ApplianceItem> getAppliances() { return appliances; }
    public List<NotificationItem> getNotifications() { return notifications; }

    public int getUnreadNotificationCount() {
        int count = 0;
        for (NotificationItem n : notifications) {
            if (!n.isRead) count++;
        }
        return count;
    }
}