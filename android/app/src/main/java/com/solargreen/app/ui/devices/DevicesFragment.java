package com.solargreen.app.ui.devices;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.switchmaterial.SwitchMaterial;
import com.solargreen.app.R;
import com.solargreen.app.data.SystemStateRepository;
import com.solargreen.app.models.ApplianceItem;

import java.util.List;

public class DevicesFragment extends Fragment implements SystemStateRepository.StateChangeListener {

    private RecyclerView rv;
    private ApplianceAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_devices, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        rv = view.findViewById(R.id.rv_appliances);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));

        SystemStateRepository repo = SystemStateRepository.getInstance();
        adapter = new ApplianceAdapter(repo.getAppliances(), repo);
        rv.setAdapter(adapter);

        repo.addListener(this);
    }

    @Override
    public void onStateChanged() {
        if (getActivity() != null) {
            getActivity().runOnUiThread(() -> {
                if (adapter != null) adapter.notifyDataSetChanged();
            });
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        SystemStateRepository.getInstance().removeListener(this);
    }

    private static class ApplianceAdapter extends RecyclerView.Adapter<ApplianceAdapter.ViewHolder> {
        private final List<ApplianceItem> list;
        private final SystemStateRepository repo;

        public ApplianceAdapter(List<ApplianceItem> list, SystemStateRepository repo) {
            this.list = list;
            this.repo = repo;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_appliance, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            ApplianceItem item = list.get(position);
            holder.tvName.setText(item.name);
            holder.tvPower.setText(String.format("%.1f kW - %s", item.powerKw, item.category));

            holder.switchToggle.setOnCheckedChangeListener(null);
            holder.switchToggle.setChecked(item.isOn);
            holder.switchToggle.setOnCheckedChangeListener((buttonView, isChecked) -> {
                repo.toggleAppliance(item.id);
            });
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        static class ViewHolder extends RecyclerView.ViewHolder {
            TextView tvName, tvPower;
            SwitchMaterial switchToggle;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                tvName = itemView.findViewById(R.id.tv_appliance_name);
                tvPower = itemView.findViewById(R.id.tv_appliance_power);
                switchToggle = itemView.findViewById(R.id.switch_appliance);
            }
        }
    }
}