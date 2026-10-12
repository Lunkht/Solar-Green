package com.solargreen.app.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.filament.gltfio.FilamentInstance;
import com.solargreen.app.MainActivity;
import com.solargreen.app.R;
import com.solargreen.app.data.SystemStateRepository;
import com.solargreen.app.ui.custom.CircularGaugeView;
import com.solargreen.app.ui.dialogs.BatteryManagementBottomSheet;
import com.solargreen.app.ui.dialogs.ModeSelectionBottomSheet;
import com.solargreen.app.ui.dialogs.NotificationsBottomSheet;

import io.github.sceneview.SceneView;
import io.github.sceneview.node.ModelNode;

public class HomeFragment extends Fragment implements SystemStateRepository.StateChangeListener {

    private CircularGaugeView circularGauge;
    private TextView tvStatusText;
    private View statusDot;
    private TextView tvValSolarProd;
    private TextView tvValBatteryPercent;
    private TextView tvValBatteryKwh;
    private TextView tvValHomeConso;
    private TextView tvValTodayTotal;
    private TextView tvOperatingModeTitle;
    private TextView tvOperatingModeBadge;

    private TextView tvPowerBtnLabel;
    private ImageView ivPowerIcon;
    private TextView tvNotifBadgeCount;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        circularGauge = view.findViewById(R.id.circular_gauge);
        tvStatusText = view.findViewById(R.id.tv_status_text);
        statusDot = view.findViewById(R.id.status_dot);
        tvValSolarProd = view.findViewById(R.id.tv_val_solar_prod);
        tvValBatteryPercent = view.findViewById(R.id.tv_val_battery_percent);
        tvValBatteryKwh = view.findViewById(R.id.tv_val_battery_kwh);
        tvValHomeConso = view.findViewById(R.id.tv_val_home_conso);
        tvValTodayTotal = view.findViewById(R.id.tv_val_today_total);
        tvOperatingModeTitle = view.findViewById(R.id.tv_operating_mode_title);
        tvOperatingModeBadge = view.findViewById(R.id.tv_operating_mode_badge);

        tvPowerBtnLabel = view.findViewById(R.id.tv_power_btn_label);
        ivPowerIcon = view.findViewById(R.id.iv_power_icon);
        tvNotifBadgeCount = view.findViewById(R.id.tv_notif_badge_count);

        SceneView sceneView = view.findViewById(R.id.sceneView);
        if (sceneView != null) {
            SceneViewLoader.loadHouseModel(requireContext(), sceneView);
        }

        setupClickListeners(view);
        SystemStateRepository.getInstance().addListener(this);
        updateUiFromState();
        animateEntrance(view);
    }

    private void animateEntrance(View rootView) {
        View cardGauge = rootView.findViewById(R.id.card_gauge);
        View cardOverview = rootView.findViewById(R.id.card_overview);
        View cardChart = rootView.findViewById(R.id.card_chart_production);
        View cardMode = rootView.findViewById(R.id.card_operating_mode);

        View[] views = new View[]{cardGauge, cardOverview, cardChart, cardMode};
        for (int i = 0; i < views.length; i++) {
            View v = views[i];
            if (v != null) {
                v.setAlpha(0f);
                v.setTranslationY(30f);
                v.animate()
                        .alpha(1f)
                        .translationY(0f)
                        .setDuration(450)
                        .setStartDelay(i * 70L)
                        .setInterpolator(new DecelerateInterpolator())
                        .start();
            }
        }
    }

    private void setupClickListeners(View view) {
        // Drawer Menu Button
        view.findViewById(R.id.btn_drawer_menu).setOnClickListener(v -> {
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).openDrawer();
            }
        });

        // Mode Card -> BottomSheet
        view.findViewById(R.id.card_operating_mode).setOnClickListener(v -> {
            new ModeSelectionBottomSheet().show(getParentFragmentManager(), "mode_sheet");
        });

        // Power Toggle Button
        view.findViewById(R.id.btn_action_power).setOnClickListener(v -> {
            SystemStateRepository.getInstance().toggleSystemPower();
            boolean isOn = SystemStateRepository.getInstance().isSystemOn();
            Toast.makeText(getContext(),
                    isOn ? "Démarrage du système de production..." : "Mise en veille du système...",
                    Toast.LENGTH_SHORT).show();
        });

        // Battery Button -> BottomSheet
        view.findViewById(R.id.btn_action_battery).setOnClickListener(v -> {
            new BatteryManagementBottomSheet().show(getParentFragmentManager(), "battery_sheet");
        });

        // Home Energy Button -> Switch to Devices Tab
        view.findViewById(R.id.btn_action_home_energy).setOnClickListener(v -> {
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).goToTab(R.id.nav_devices);
            }
        });

        // History Button -> Switch to Stats Tab
        view.findViewById(R.id.btn_action_history).setOnClickListener(v -> {
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).goToTab(R.id.nav_stats);
            }
        });

        // Notifications Button -> BottomSheet
        view.findViewById(R.id.btn_action_notifications).setOnClickListener(v -> {
            new NotificationsBottomSheet().show(getParentFragmentManager(), "notif_sheet");
        });

        // Settings Button -> Switch to Settings Fragment
        view.findViewById(R.id.btn_action_settings).setOnClickListener(v -> {
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).openSettings();
            }
        });

        view.findViewById(R.id.btn_header_account).setOnClickListener(v -> {
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).openAccount();
            }
        });
    }

    private void updateUiFromState() {
        if (!isAdded()) return;

        SystemStateRepository repo = SystemStateRepository.getInstance();

        tvStatusText.setText(repo.getStatusText());
        tvValSolarProd.setText(String.format("%.1f kW", repo.getSolarProductionKw()));
        tvValBatteryPercent.setText(String.format("%.0f%%", repo.getBatteryPercent()));
        tvValBatteryKwh.setText(String.format(" (%.1f kWh)", repo.getBatteryKwh()));
        tvValHomeConso.setText(String.format("%.1f kW", repo.getHomeConsumptionKw()));
        tvValTodayTotal.setText(String.format("%.1f kWh", repo.getTodayTotalKwh()));

        tvOperatingModeTitle.setText(repo.getOperatingMode());
        tvOperatingModeBadge.setText(repo.getOperatingModeSub());

        circularGauge.setPower(repo.getCurrentPowerKw(), 6.0f);

        if (repo.isSystemOn()) {
            tvPowerBtnLabel.setText("Éteindre\nle système");
            ivPowerIcon.setAlpha(1.0f);
        } else {
            tvPowerBtnLabel.setText("Allumer\nle système");
            ivPowerIcon.setAlpha(0.4f);
        }

        int unread = repo.getUnreadNotificationCount();
        if (unread > 0) {
            tvNotifBadgeCount.setVisibility(View.VISIBLE);
            tvNotifBadgeCount.setText(String.valueOf(unread));
        } else {
            tvNotifBadgeCount.setVisibility(View.GONE);
        }
    }

    @Override
    public void onStateChanged() {
        if (getActivity() != null) {
            getActivity().runOnUiThread(this::updateUiFromState);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        SystemStateRepository.getInstance().removeListener(this);
    }
}