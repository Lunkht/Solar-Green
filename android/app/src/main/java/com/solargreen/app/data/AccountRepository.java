package com.solargreen.app.data;

import com.solargreen.app.models.AccountProfile;
import com.solargreen.app.models.EnergyStats;
import com.solargreen.app.models.InstallationInfo;

import java.util.ArrayList;
import java.util.List;

public class AccountRepository {

    public interface AccountListener {
        void onAccountChanged();
    }

    private static AccountRepository instance;

    private final List<AccountListener> listeners = new ArrayList<>();

    private final AccountProfile profile;
    private final InstallationInfo installation;
    private EnergyStats stats;

    private AccountRepository() {
        this.profile = new AccountProfile(
                "Karim Benali",
                "karim.benali@solargreen.io",
                "+33 6 41 22 87 09",
                "Propriétaire",
                "Mars 2023",
                "SolarGreen Premium",
                "12 000 GNF/mois",
                "12 janvier 2027",
                true
        );

        this.installation = new InstallationInfo(
                "Résidence Benali",
                "14 allée des Tilleuls, 69003 Lyon",
                "SolarGreen Services SAS",
                "18 mars 2023",
                "18 mars 2043",
                "Onduleur Hybride SG-6000",
                "SG-8849204-EU",
                "v3.4.12-stable",
                6.0f,
                12,
                500,
                6.0f,
                "Sud",
                "30°",
                "Solar Battery Pro",
                10.0f,
                "SolarGreen_Net_5G",
                "192.168.1.150",
                87,
                "Wi-Fi 5 GHz"
        );

        this.stats = new EnergyStats(18432.0f, 13980.0f, 2875000.0f, 8240.0f, 76, 12480, 6);
    }

    public static synchronized AccountRepository getInstance() {
        if (instance == null) {
            instance = new AccountRepository();
        }
        return instance;
    }

    public AccountProfile getProfile() {
        return profile;
    }

    public InstallationInfo getInstallation() {
        return installation;
    }

    public EnergyStats getStats() {
        return stats;
    }

    public void setAutoRenew(boolean enabled) {
        profile.autoRenew = enabled;
        notifyListeners();
    }

    public void addListener(AccountListener listener) {
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    public void removeListener(AccountListener listener) {
        listeners.remove(listener);
    }

    private void notifyListeners() {
        for (AccountListener listener : new ArrayList<>(listeners)) {
            listener.onAccountChanged();
        }
    }
}
