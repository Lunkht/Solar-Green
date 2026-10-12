package com.solargreen.app.data;

import android.content.Context;
import android.content.SharedPreferences;

import com.solargreen.app.models.Appliance;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ApplianceRepository {
    private static final String PREF_NAME = "solargreen_consumption";
    private static final String KEY_APPLIANCES = "appliances";
    private static final Gson gson = new Gson();

    public static List<Appliance> getAppliances(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        String json = prefs.getString(KEY_APPLIANCES, null);
        if (json == null) {
            List<Appliance> defaults = new ArrayList<>();
            defaults.add(new Appliance("Climatiseur Inverter", 1, 1200, "LG / Samsung"));
            defaults.add(new Appliance("Réfrigérateur / Congélateur", 1, 250, "Haier"));
            defaults.add(new Appliance("Éclairage LED Maison", 12, 15, "Philips"));
            defaults.add(new Appliance("Téléviseur 55\" 4K", 1, 150, "Sony"));
            defaults.add(new Appliance("Pompe Solaire Forage", 1, 750, "SolarGreen"));
            saveAppliances(context, defaults);
            return defaults;
        }

        Type type = new TypeToken<ArrayList<Appliance>>() {}.getType();
        return gson.fromJson(json, type);
    }

    public static void saveAppliances(Context context, List<Appliance> appliances) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        String json = gson.toJson(appliances);
        prefs.edit().putString(KEY_APPLIANCES, json).apply();
    }

    public static void addAppliance(Context context, Appliance appliance) {
        List<Appliance> list = getAppliances(context);
        list.add(appliance);
        saveAppliances(context, list);
    }

    public static void removeAppliance(Context context, String id) {
        List<Appliance> list = getAppliances(context);
        list.removeIf(a -> a.id.equals(id));
        saveAppliances(context, list);
    }

    public static int getTotalConsumption(Context context) {
        List<Appliance> list = getAppliances(context);
        int total = 0;
        for (Appliance a : list) {
            total += a.getTotalConsumption();
        }
        return total;
    }
}