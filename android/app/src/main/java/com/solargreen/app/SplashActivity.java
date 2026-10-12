package com.solargreen.app;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        View logo = findViewById(R.id.iv_splash_logo);
        if (logo != null) {
            logo.setAlpha(0f);
            logo.setScaleX(0.85f);
            logo.setScaleY(0.85f);
            logo.animate()
                    .alpha(1f)
                    .scaleX(1.0f)
                    .scaleY(1.0f)
                    .setDuration(800)
                    .setInterpolator(new DecelerateInterpolator())
                    .start();
        }

        // Delay 1.6 seconds on splash screen before launching MainActivity
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            Bundle options = ActivityOptionsCompat.makeCustomAnimation(
                    SplashActivity.this, R.anim.fade_in, R.anim.fade_out).toBundle();
            startActivity(intent, options);
            finish();
        }, 1600);
    }
}