package com.solargreen.app.ui.stats;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.solargreen.app.R;
import com.solargreen.app.data.ApplianceRepository;
import com.solargreen.app.models.Appliance;
import com.solargreen.app.ui.consumption.ApplianceAdapter;

import java.util.List;

public class StatsFragment extends Fragment implements ApplianceAdapter.OnApplianceListener {

    private ApplianceAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_stats, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView recyclerView = view.findViewById(R.id.recycler_stats_appliances);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        List<Appliance> appliances = ApplianceRepository.getAppliances(requireContext());
        adapter = new ApplianceAdapter(appliances, this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onDelete(Appliance appliance) {
        ApplianceRepository.removeAppliance(requireContext(), appliance.id);
        refreshData();
    }

    private void refreshData() {
        List<Appliance> list = ApplianceRepository.getAppliances(requireContext());
        if (adapter != null) {
            adapter.updateList(list);
        }
    }
}