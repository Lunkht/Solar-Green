package com.solargreen.app.ui.catalog;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.solargreen.app.MainActivity;
import com.solargreen.app.R;
import com.solargreen.app.data.ProductRepository;
import com.solargreen.app.models.Product;

import java.util.List;

public class ProductDetailActivity extends AppCompatActivity {

    public static final String EXTRA_PRODUCT_ID = "product_id";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        String id = getIntent().getStringExtra(EXTRA_PRODUCT_ID);
        List<Product> products = ProductRepository.load(this);
        Product p = ProductRepository.findById(products, id);
        if (p == null) {
            finish();
            return;
        }

        TextView title = findViewById(R.id.detail_title);
        ImageView image = findViewById(R.id.detail_image);
        TextView type = findViewById(R.id.detail_type);
        TextView tagline = findViewById(R.id.detail_tagline);
        TextView description = findViewById(R.id.detail_description);
        TextView price = findViewById(R.id.detail_price);
        TextView priceCfa = findViewById(R.id.detail_price_eur);
        LinearLayout specs = findViewById(R.id.detail_specs);

        setTitle(p.name);
        title.setText(p.name);
        type.setText(p.category);

        Glide.with(this).load(p.imageUrl).into(image);

        tagline.setText(p.tagline);
        description.setText(p.description);
        price.setText(p.price);
        if (p.priceCfa == null || p.priceCfa.isEmpty()) {
            priceCfa.setVisibility(View.GONE);
        } else {
            priceCfa.setVisibility(View.VISIBLE);
            priceCfa.setText(p.priceCfa);
        }

        View quoteButton = findViewById(R.id.detail_quote);
        quoteButton.setOnClickListener(v -> {
            Intent i = new Intent(this, MainActivity.class);
            i.putExtra(MainActivity.EXTRA_TAB, R.id.nav_devices);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(i);
        });

        specs.removeAllViews();
        for (int i = 0; i < p.specLabels.length; i++) {
            LinearLayout row = new LinearLayout(this);
            row.setOrientation(LinearLayout.HORIZONTAL);
            row.setPadding(dp(8), dp(12), dp(8), dp(12));

            TextView k = new TextView(this);
            k.setText(p.specLabels[i]);
            k.setTextColor(getColor(R.color.text_dim));
            k.setTextSize(14);

            TextView v = new TextView(this);
            v.setText(p.specValues[i]);
            v.setTextColor(getColor(R.color.text));
            v.setTextSize(14);

            LinearLayout.LayoutParams kp = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
            LinearLayout.LayoutParams vp = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            v.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);

            row.addView(k, kp);
            row.addView(v, vp);
            specs.addView(row, new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        }
    }

    private int dp(int value) {
        return Math.round(getResources().getDisplayMetrics().density * value);
    }
}