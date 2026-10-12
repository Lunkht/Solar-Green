package com.solargreen.app.ui.dialogs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.solargreen.app.R;
import com.solargreen.app.data.SystemStateRepository;

public class InverterSettingsBottomSheet extends BottomSheetDialogFragment {

    private Spinner spinnerPriority;
    private RadioGroup rgInputRange;
    private RadioButton rbRangeApl, rbRangeUps;
    private SeekBar seekChargeCurrent;
    private TextView tvChargeCurrentVal;
    private SwitchCompat switchBuzzer, switchEnergySaver;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.sheet_inverter_settings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        spinnerPriority = view.findViewById(R.id.spinner_priority);
        rgInputRange = view.findViewById(R.id.rg_input_range);
        rbRangeApl = view.findViewById(R.id.rb_range_apl);
        rbRangeUps = view.findViewById(R.id.rb_range_ups);
        seekChargeCurrent = view.findViewById(R.id.seek_charge_current);
        tvChargeCurrentVal = view.findViewById(R.id.tv_charge_current_val);
        switchBuzzer = view.findViewById(R.id.switch_buzzer);
        switchEnergySaver = view.findViewById(R.id.switch_energy_saver);

        String[] priorities = new String[]{
            "Solaire / Batterie / Réseau (SBU)",
            "Solaire / Réseau / Batterie (SUB)",
            "Réseau en priorité (UTI)"
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_spinner_item, priorities);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPriority.setAdapter(adapter);

        seekChargeCurrent.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int val = Math.max(10, (progress / 10) * 10);
                tvChargeCurrentVal.setText(val + " A");
            }

            @Override public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        view.findViewById(R.id.btn_save_inverter_settings).setOnClickListener(v -> {
            String selectedPriority = spinnerPriority.getSelectedItem().toString();
            int selectedRangeId = rgInputRange.getCheckedRadioButtonId();
            String range = (selectedRangeId == R.id.rb_range_ups) ? "UPS (170V-280V)" : "APL (90V-280V)";
            int chargeA = Math.max(10, (seekChargeCurrent.getProgress() / 10) * 10);
            boolean buzzer = switchBuzzer.isChecked();
            boolean energySaver = switchEnergySaver.isChecked();

            SystemStateRepository.getInstance().updateInverterSettings(
                    selectedPriority, range, chargeA, buzzer, energySaver
            );

            Toast.makeText(getContext(), "Réglages de l'onduleur appliqués avec succès !", Toast.LENGTH_SHORT).show();
            dismiss();
        });
    }

    @Override
    public int getTheme() {
        return R.style.SolarGreenDialogTheme;
    }
}