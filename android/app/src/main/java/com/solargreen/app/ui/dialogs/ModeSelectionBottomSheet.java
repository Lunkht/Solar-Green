package com.solargreen.app.ui.dialogs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.solargreen.app.R;
import com.solargreen.app.data.SystemStateRepository;

public class ModeSelectionBottomSheet extends BottomSheetDialogFragment {

    private RadioButton radioAutonome, radioHybride, radioEco;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.sheet_mode_selection, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        radioAutonome = view.findViewById(R.id.radio_autonome);
        radioHybride = view.findViewById(R.id.radio_hybride);
        radioEco = view.findViewById(R.id.radio_eco);

        String currentMode = SystemStateRepository.getInstance().getOperatingMode();
        updateRadioSelection(currentMode);

        view.findViewById(R.id.mode_autonome).setOnClickListener(v -> selectMode("Autonome", "Normal"));
        view.findViewById(R.id.mode_hybride).setOnClickListener(v -> selectMode("Hybride", "Actif"));
        view.findViewById(R.id.mode_eco).setOnClickListener(v -> selectMode("Éco", "Optimisé"));
    }

    private void updateRadioSelection(String mode) {
        radioAutonome.setChecked("Autonome".equals(mode));
        radioHybride.setChecked("Hybride".equals(mode));
        radioEco.setChecked("Éco".equals(mode));
    }

    private void selectMode(String mode, String status) {
        SystemStateRepository.getInstance().setOperatingMode(mode, status);
        updateRadioSelection(mode);
        dismiss();
    }

    @Override
    public int getTheme() {
        return R.style.SolarGreenDialogTheme;
    }
}