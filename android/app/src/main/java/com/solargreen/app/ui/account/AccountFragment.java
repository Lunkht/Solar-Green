package com.solargreen.app.ui.account;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;

import com.solargreen.app.R;
import com.solargreen.app.data.AccountRepository;
import com.solargreen.app.models.AccountProfile;
import com.solargreen.app.models.EnergyStats;
import com.solargreen.app.models.InstallationInfo;

import java.util.Locale;

public class AccountFragment extends Fragment implements AccountRepository.AccountListener {

    private TextView tvAccountInitials, tvName, tvEmail, tvRole, tvMemberSince;
    private TextView tvInverterModel, tvInverterSerial, tvInverterFirmware, tvSite, tvPanels, tvBattery, tvInstallDate;
    private TextView tvConnectionType, tvNetworkName, tvIp;
    private TextView tvPlanName, tvPlanPrice, tvPlanRenewal;
    private TextView tvStatProduction, tvStatAutonomy, tvStatSavings, tvStatCo2;
    private TextView tvInstaller, tvWarranty;
    private SwitchCompat switchPlanAutoRenew;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_account, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvAccountInitials = view.findViewById(R.id.tv_account_initials);
        tvName = view.findViewById(R.id.tv_name);
        tvEmail = view.findViewById(R.id.tv_email);
        tvRole = view.findViewById(R.id.tv_role);
        tvMemberSince = view.findViewById(R.id.tv_member_since);

        tvInverterModel = view.findViewById(R.id.tv_inverter_model);
        tvInverterSerial = view.findViewById(R.id.tv_inverter_serial);
        tvInverterFirmware = view.findViewById(R.id.tv_inverter_firmware);
        tvSite = view.findViewById(R.id.tv_site);
        tvPanels = view.findViewById(R.id.tv_panels);
        tvBattery = view.findViewById(R.id.tv_battery);
        tvInstallDate = view.findViewById(R.id.tv_install_date);

        tvConnectionType = view.findViewById(R.id.tv_connection_type);
        tvNetworkName = view.findViewById(R.id.tv_network_name);
        tvIp = view.findViewById(R.id.tv_ip);

        tvPlanName = view.findViewById(R.id.tv_plan_name);
        tvPlanPrice = view.findViewById(R.id.tv_plan_price);
        tvPlanRenewal = view.findViewById(R.id.tv_plan_renewal);
        switchPlanAutoRenew = view.findViewById(R.id.switch_plan_auto_renew);

        tvStatProduction = view.findViewById(R.id.tv_stat_production);
        tvStatAutonomy = view.findViewById(R.id.tv_stat_autonomy);
        tvStatSavings = view.findViewById(R.id.tv_stat_savings);
        tvStatCo2 = view.findViewById(R.id.tv_stat_co2);

        tvInstaller = view.findViewById(R.id.tv_installer);
        tvWarranty = view.findViewById(R.id.tv_warranty);

        switchPlanAutoRenew.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (buttonView.isPressed()) {
                    AccountRepository.getInstance().setAutoRenew(isChecked);
                }
            }
        });

        render();
    }

    @Override
    public void onStart() {
        super.onStart();
        AccountRepository.getInstance().addListener(this);
    }

    @Override
    public void onStop() {
        AccountRepository.getInstance().removeListener(this);
        super.onStop();
    }

    @Override
    public void onAccountChanged() {
        if (isAdded()) {
            requireActivity().runOnUiThread(this::render);
        }
    }

    private void render() {
        AccountRepository repo = AccountRepository.getInstance();
        AccountProfile profile = repo.getProfile();
        InstallationInfo install = repo.getInstallation();
        EnergyStats stats = repo.getStats();

        tvAccountInitials.setText(profile.getInitials());
        tvName.setText(profile.fullName);
        tvEmail.setText(profile.email);
        tvRole.setText(profile.role);
        tvMemberSince.setText(profile.memberSince);

        tvInverterModel.setText(install.inverterModel);
        tvInverterSerial.setText(getString(R.string.account_serial, install.inverterSerial));
        tvInverterFirmware.setText(install.inverterFirmware);
        tvSite.setText(String.format("%s — %s", install.siteName, install.siteAddress));
        tvPanels.setText(String.format("%s — Orientation %s, %s",
                install.getPanelSummary(), install.panelOrientation, install.panelTilt));
        tvBattery.setText(String.format("%s — %s kWh", install.batteryModel, trimFloat(install.batteryCapacityKwh)));
        tvInstallDate.setText(getString(R.string.account_installed_on, install.installDate));

        tvConnectionType.setText(install.connectionType);
        tvNetworkName.setText(String.format("%s — Signal %d %%", install.networkName, install.signalPercent));
        tvIp.setText(install.ipAddress);

        tvPlanName.setText(profile.planName);
        tvPlanPrice.setText(profile.planPrice);
        tvPlanRenewal.setText(getString(R.string.account_renewal, profile.planRenewalDate));
        switchPlanAutoRenew.setChecked(profile.autoRenew);

        tvStatProduction.setText(String.format(Locale.FRANCE, "%,d kWh", Math.round(stats.lifetimeProductionKwh)));
        tvStatAutonomy.setText(stats.autonomyPercent + " %");
        tvStatSavings.setText(String.format(Locale.FRANCE, "%,d GNF", Math.round(stats.lifetimeSavingsGnf)));
        tvStatCo2.setText(String.format(Locale.FRANCE, "%.1f t", stats.co2AvoidedKg / 1000.0));

        tvInstaller.setText(getString(R.string.account_installer, install.installerName));
        tvWarranty.setText(getString(R.string.account_warranty, install.warrantyEnd));
    }

    private String trimFloat(float value) {
        return value == Math.round(value) ? String.valueOf((int) value) : String.valueOf(value);
    }
}
