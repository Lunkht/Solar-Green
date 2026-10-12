package com.solargreen.app.ui.custom;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.solargreen.app.R;

public class CircularGaugeView extends View {

    private Paint trackPaint;
    private Paint progressPaint;
    private Paint textTitlePaint;
    private Paint textValuePaint;
    private Paint sunPaint;

    private final RectF arcBounds = new RectF();

    private float animatedKw = 0.0f;
    private float maxKw = 6.0f;
    private ValueAnimator animator;

    public CircularGaugeView(Context context) {
        super(context);
        init();
    }

    public CircularGaugeView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CircularGaugeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        trackPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        trackPaint.setStyle(Paint.Style.STROKE);
        trackPaint.setStrokeWidth(dpToPx(12));
        trackPaint.setStrokeCap(Paint.Cap.ROUND);

        progressPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        progressPaint.setStyle(Paint.Style.STROKE);
        progressPaint.setStrokeWidth(dpToPx(12));
        progressPaint.setStrokeCap(Paint.Cap.ROUND);

        textTitlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textTitlePaint.setTextSize(spToPx(12));
        textTitlePaint.setTextAlign(Paint.Align.CENTER);

        textValuePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textValuePaint.setTextSize(spToPx(24));
        textValuePaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        textValuePaint.setTextAlign(Paint.Align.CENTER);

        sunPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    }

    private void updateDynamicColors() {
        trackPaint.setColor(ContextCompat.getColor(getContext(), R.color.card_stroke));
        progressPaint.setColor(ContextCompat.getColor(getContext(), R.color.accent));
        textTitlePaint.setColor(ContextCompat.getColor(getContext(), R.color.text_primary));
        textValuePaint.setColor(ContextCompat.getColor(getContext(), R.color.text_primary));
        sunPaint.setColor(ContextCompat.getColor(getContext(), R.color.accent));
    }

    public void setPower(float kw, float max) {
        setPower(kw, max, true);
    }

    public void setPower(float kw, float max, boolean animate) {
        float targetKw = Math.max(0, kw);
        this.maxKw = Math.max(1.0f, max);

        if (!animate) {
            this.animatedKw = targetKw;
            invalidate();
            return;
        }

        if (animator != null && animator.isRunning()) {
            animator.cancel();
        }

        float startVal = animatedKw;

        animator = ValueAnimator.ofFloat(startVal, targetKw);
        animator.setDuration(900);
        animator.setInterpolator(new DecelerateInterpolator());
        animator.addUpdateListener(animation -> {
            animatedKw = (float) animation.getAnimatedValue();
            invalidate();
        });
        animator.start();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        updateDynamicColors();

        int width = getWidth();
        int height = getHeight();
        if (width == 0 || height == 0) return;

        float strokeWidth = trackPaint.getStrokeWidth();
        float padding = strokeWidth / 2f + dpToPx(8);
        float size = Math.min(width, height) - padding * 2;

        float left = (width - size) / 2f;
        float top = dpToPx(8);
        arcBounds.set(left, top, left + size, top + size);

        float startAngle = 150f;
        float sweepAngle = 240f;

        // Draw track arc
        canvas.drawArc(arcBounds, startAngle, sweepAngle, false, trackPaint);

        // Draw active progress arc
        float ratio = Math.min(1.0f, animatedKw / maxKw);
        float activeSweep = sweepAngle * ratio;
        if (activeSweep > 0) {
            canvas.drawArc(arcBounds, startAngle, activeSweep, false, progressPaint);
        }

        // Draw "Énergie actuelle"
        float centerX = width / 2f;
        float centerY = arcBounds.centerY();

        canvas.drawText("Énergie actuelle", centerX, centerY - dpToPx(16), textTitlePaint);

        // Draw animated kW value
        String valueStr = String.format("%.1f kW", animatedKw);
        canvas.drawText(valueStr, centerX, centerY + dpToPx(16), textValuePaint);

        // Draw Sun icon
        float sunY = centerY + dpToPx(38);
        drawSunIcon(canvas, centerX, sunY, dpToPx(10));
    }

    private void drawSunIcon(Canvas canvas, float cx, float cy, float radius) {
        canvas.drawCircle(cx, cy, radius * 0.5f, sunPaint);

        Paint rayPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        rayPaint.setColor(ContextCompat.getColor(getContext(), R.color.accent));
        rayPaint.setStrokeWidth(dpToPx(2));

        for (int i = 0; i < 8; i++) {
            double angle = Math.toRadians(i * 45);
            float startX = (float) (cx + Math.cos(angle) * (radius * 0.7f));
            float startY = (float) (cy + Math.sin(angle) * (radius * 0.7f));
            float endX = (float) (cx + Math.cos(angle) * radius);
            float endY = (float) (cy + Math.sin(angle) * radius);
            canvas.drawLine(startX, startY, endX, endY, rayPaint);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (animator != null && animator.isRunning()) {
            animator.cancel();
        }
    }

    private float dpToPx(float dp) {
        return dp * getContext().getResources().getDisplayMetrics().density;
    }

    private float spToPx(float sp) {
        return sp * getContext().getResources().getDisplayMetrics().scaledDensity;
    }
}