package com.solargreen.app.data;

import android.content.Context;

import com.solargreen.app.models.Product;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class ProductRepository {

    private static List<Product> cache;

    public static List<Product> load(Context context) {
        if (cache != null) return cache;
        cache = new ArrayList<>();
        try {
            InputStream is = context.getAssets().open("products.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            String json = new String(buffer, StandardCharsets.UTF_8);
            JSONArray arr = new JSONArray(json);
            for (int i = 0; i < arr.length(); i++) {
                JSONObject o = arr.getJSONObject(i);
                JSONArray specs = o.getJSONArray("specs");
                int n = specs.length();
                String[] labels = new String[n];
                String[] values = new String[n];
                for (int j = 0; j < n; j++) {
                    JSONObject s = specs.getJSONObject(j);
                    labels[j] = s.getString("label");
                    values[j] = s.getString("value");
                }
                cache.add(new Product(
                        o.getString("id"),
                        o.getString("name"),
                        o.getString("type"),
                        o.getString("category"),
                        o.optString("price"),
                        o.optString("priceCfa"),
                        o.getString("tagline"),
                        o.getString("description"),
                        o.optString("imageUrl", "https://images.unsplash.com/photo-1509391366360-fe5bb584850a?q=80&w=500"),
                        labels,
                        values
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cache;
    }

    public static Product findById(List<Product> list, String id) {
        for (Product p : list) {
            if (p.id.equals(id)) return p;
        }
        return list.isEmpty() ? null : list.get(0);
    }
}