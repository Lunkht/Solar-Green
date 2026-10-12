package com.solargreen.app.ui.dialogs;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.solargreen.app.R;
import com.solargreen.app.data.SystemStateRepository;

public class ConnectionBottomSheet extends BottomSheetDialogFragment {

    private TextView tvStatusBadge;
    private Button btnToggle;
    private ProgressBar pbScan;
    private boolean isConnected = true;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.sheet_connection, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        tvStatusBadge = view.findViewById(R.id.tv_connection_status_badge);
        btnToggle = view.findViewById(R.id.btn_toggle_connection);
        pbScan = view.findViewById(R.id.pb_conn_scan);

        view.findViewById(R.id.btn_scan_inverter).setOnClickListener(v -> {
            pbScan.setVisibility(View.VISIBLE);
            Toast.makeText(getContext(), "Recherche d'onduleurs SolarGreen sur le réseau...", Toast.LENGTH_SHORT).show();
            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                if (isAdded()) {
                    pbScan.setVisibility(View.GONE);
                    Toast.makeText(getContext(), "Onduleur SG-6000 détecté sur 192.168.1.150", Toast.LENGTH_SHORT).show();
                }
            }, 2000);
        });

        btnToggle.setOnClickListener(v -> {
            isConnected = !isConnected;
            if (isConnected) {
                tvStatusBadge.setText("CONNECTÉ");
                tvStatusBadge.setTextColor(getResources().getColor(R.color.accent, null));
                btnToggle.setText("Déconnecter");
                Toast.makeText(getContext(), "Connexion rétablie à l'onduleur SG-6000", Toast.LENGTH_SHORT).show();
            } else {
                tvStatusBadge.setText("DÉCONNECTÉ");
                tvStatusBadge.setTextColor(getResources().getColor(R.color.red_badge, null));
                btnToggle.setText("Connecter");
                Toast.makeText(getContext(), "Onduleur déconnecté", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getTheme() {
        return R.style.SolarGreenDialogTheme;
    }
}