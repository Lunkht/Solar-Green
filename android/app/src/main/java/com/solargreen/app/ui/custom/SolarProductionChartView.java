package com.solargreen.app.ui.custom;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.solargreen.app.R;

public class SolarProductionChartView extends View {

    private Paint gridPaint;
    private Paint axisTextPaint;
    private Paint barPaint;
    private Paint highlightBarPaint;

    private float[] hourlyData = {
        0.2f, 0.6f, 1.2f, 2.1f, 2.9f, 3.8f, 4.8f, 5.0f,
        4.5f, 3.8f, 2.6f, 1.8f, 1.1f, 0.4f, 0.1f
    };

    private final String[] hourLabels = {
        "06h", "08h", "10h", "12h", "14h", "16h", "18h", "20h"
    };

    private final RectF barRect = new RectF();
    private int selectedIndex = -1;

    private float animationProgress = 0.0f;
    private ValueAnimator animator;
    private boolean hasAnimatedOnAttached = false;

    public SolarProductionChartView(Context context) {
        super(context);
        init();
    }

    public SolarProductionChartView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SolarProductionChartView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        gridPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        gridPaint.setStyle(Paint.Style.STROKE);
        gridPaint.setStrokeWidth(dpToPx(1));
        gridPaint.setPathEffect(new DashPathEffect(new float[]{dpToPx(4), dpToPx(4)}, 0));

        axisTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        axisTextPaint.setTextSize(spToPx(10));

        barPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        barPaint.setStyle(Paint.Style.FILL);

        highlightBarPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        highlightBarPaint.setStyle(Paint.Style.FILL);
    }

    private void updateDynamicColors() {
        gridPaint.setColor(ContextCompat.getColor(getContext(), R.color.card_stroke));
        axisTextPaint.setColor(ContextCompat.getColor(getContext(), R.color.text_dim));
        barPaint.setColor(ContextCompat.getColor(getContext(), R.color.accent));
        highlightBarPaint.setColor(ContextCompat.getColor(getContext(), R.color.secondary_accent));
    }

    public void setHourlyData(float[] data) {
        setHourlyData(data, true);
    }

    public void setHourlyData(float[] data, boolean animate) {
        if (data != null && data.length > 0) {
            this.hourlyData = data;
            if (animate) {
                startChartAnimation();
            } else {
                this.animationProgress = 1.0f;
                invalidate();
            }
        }
    }

    public void startChartAnimation() {
        if (animator != null && animator.isRunning()) {
            animator.cancel();
        }
        animator = ValueAnimator.ofFloat(0.0f, 1.0f);
        animator.setDuration(900);
        animator.setInterpolator(new DecelerateInterpolator());
        animator.addUpdateListener(animation -> {
            animationProgress = (float) animation.getAnimatedValue();
            invalidate();
        });
        animator.start();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (!hasAnimatedOnAttached) {
            hasAnimatedOnAttached = true;
            startChartAnimation();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (animator != null && animator.isRunning()) {
            animator.cancel();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        updateDynamicColors();

        int width = getWidth();
        int height = getHeight();
        if (width == 0 || height == 0) return;

        float leftMargin = dpToPx(24);
        float bottomMargin = dpToPx(24);
        float topMargin = dpToPx(12);
        float rightMargin = dpToPx(12);

        float chartWidth = width - leftMargin - rightMargin;
        float chartHeight = height - topMargin - bottomMargin;

        float maxVal = 5.0f;

        // Draw Y-Axis grid lines & labels (0 to 5)
        for (int i = 0; i <= 5; i++) {
            float y = topMargin + chartHeight - (chartHeight * (i / maxVal));

            canvas.drawLine(leftMargin, y, leftMargin + chartWidth, y, gridPaint);
            canvas.drawText(String.valueOf(i), dpToPx(6), y + dpToPx(3), axisTextPaint);
        }

        // Draw Bars
        int barCount = hourlyData.length;
        float totalSpacing = chartWidth;
        float slotWidth = totalSpacing / barCount;
        float barWidth = Math.max(dpToPx(6), slotWidth * 0.6f);
        float barRadius = barWidth / 2f;

        for (int i = 0; i < barCount; i++) {
            float val = hourlyData[i];
            float barHeightRatio = Math.min(1.0f, val / maxVal);
            float h = chartHeight * barHeightRatio * animationProgress;

            float cx = leftMargin + (i * slotWidth) + (slotWidth / 2f);
            float barLeft = cx - (barWidth / 2f);
            float barRight = cx + (barWidth / 2f);
            float barBottom = topMargin + chartHeight;
            float barTop = barBottom - h;

            if (h < barRadius && h > 0) {
                barTop = barBottom - barRadius;
            }

            barRect.set(barLeft, barTop, barRight, barBottom);

            Paint currentBarPaint = (i == selectedIndex) ? highlightBarPaint : barPaint;
            canvas.drawRoundRect(barRect, barRadius, barRadius, currentBarPaint);
        }

        // Draw X-Axis Hour Labels
        for (int i = 0; i < hourLabels.length; i++) {
            int hourIndex = i * 2;
            if (hourIndex < barCount) {
                float cx = leftMargin + (hourIndex * slotWidth) + (slotWidth / 2f);
                float textWidth = axisTextPaint.measureText(hourLabels[i]);
                float tx = cx - (textWidth / 2f);
                canvas.drawText(hourLabels[i], tx, height - dpToPx(4), axisTextPaint);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            float x = event.getX();
            float leftMargin = dpToPx(24);
            float rightMargin = dpToPx(12);
            float chartWidth = getWidth() - leftMargin - rightMargin;
            int barCount = hourlyData.length;
            float slotWidth = chartWidth / barCount;

            int index = (int) ((x - leftMargin) / slotWidth);
            if (index >= 0 && index < barCount) {
                selectedIndex = index;
                invalidate();

                int hour = 6 + index;
                float kw = hourlyData[index];
                Toast.makeText(getContext(),
                    String.format("Production à %02dh00 : %.1f kW", hour, kw),
                    Toast.LENGTH_SHORT).show();
                return true;
            }
        }
        return super.onTouchEvent(event);
    }

    private float dpToPx(float dp) {
        return dp * getContext().getResources().getDisplayMetrics().density;
    }

    private float spToPx(float sp) {
        return sp * getContext().getResources().getDisplayMetrics().scaledDensity;
    }
}