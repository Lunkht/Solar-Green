package com.solargreen.app.ui.dialogs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.solargreen.app.R;
import com.solargreen.app.data.SystemStateRepository;

public class BatteryManagementBottomSheet extends BottomSheetDialogFragment {

    private TextView tvReserveLimit;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.sheet_battery_management, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        TextView tvPercent = view.findViewById(R.id.tv_battery_percent_sheet);
        tvReserveLimit = view.findViewById(R.id.tv_reserve_limit);
        SeekBar seekBar = view.findViewById(R.id.seekbar_reserve);

        SystemStateRepository repo = SystemStateRepository.getInstance();
        tvPercent.setText(String.format("%.0f%% (%.1f kWh)", repo.getBatteryPercent(), repo.getBatteryKwh()));

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tvReserveLimit.setText(String.format("Conserver au moins %d%% de batterie", Math.max(10, progress)));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
    }

    @Override
    public int getTheme() {
        return R.style.SolarGreenDialogTheme;
    }
}