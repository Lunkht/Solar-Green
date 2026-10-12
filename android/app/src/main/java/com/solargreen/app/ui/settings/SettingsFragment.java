package com.solargreen.app.ui.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;

import com.solargreen.app.MainActivity;
import com.solargreen.app.R;
import com.solargreen.app.ui.dialogs.ConnectionBottomSheet;
import com.solargreen.app.ui.dialogs.InverterSettingsBottomSheet;

public class SettingsFragment extends Fragment {

    private static final String PREFS_NAME = "solargreen_prefs";
    private static final String PREF_DARK_MODE = "dark_mode";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        SwitchCompat switchDarkMode = view.findViewById(R.id.switch_dark_mode);
        SharedPreferences prefs = requireContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        boolean isDarkMode = prefs.getBoolean(PREF_DARK_MODE, true);
        switchDarkMode.setChecked(isDarkMode);

        switchDarkMode.setOnCheckedChangeListener((buttonView, isChecked) -> {
            prefs.edit().putBoolean(PREF_DARK_MODE, isChecked).apply();

            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }
        });

        view.findViewById(R.id.btn_test_connection).setOnClickListener(v -> {
            new ConnectionBottomSheet().show(getParentFragmentManager(), "conn_sheet");
        });

        view.findViewById(R.id.btn_inverter_settings).setOnClickListener(v -> {
            new InverterSettingsBottomSheet().show(getParentFragmentManager(), "inverter_sheet");
        });

        view.findViewById(R.id.btn_open_catalog).setOnClickListener(v -> {
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).openCatalog();
            }
        });

        view.findViewById(R.id.btn_open_contact).setOnClickListener(v -> {
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).openContact();
            }
        });

        return view;
    }
}