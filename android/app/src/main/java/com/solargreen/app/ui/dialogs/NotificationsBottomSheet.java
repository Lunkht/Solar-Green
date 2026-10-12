package com.solargreen.app.ui.dialogs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.solargreen.app.R;
import com.solargreen.app.data.SystemStateRepository;
import com.solargreen.app.models.NotificationItem;

import java.util.List;

public class NotificationsBottomSheet extends BottomSheetDialogFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.sheet_notifications, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        RecyclerView rv = view.findViewById(R.id.rv_sheet_notifications);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));

        SystemStateRepository repo = SystemStateRepository.getInstance();
        NotificationAdapter adapter = new NotificationAdapter(repo.getNotifications());
        rv.setAdapter(adapter);

        view.findViewById(R.id.btn_clear_notifications).setOnClickListener(v -> {
            repo.markAllNotificationsRead();
            adapter.notifyDataSetChanged();
            dismiss();
        });
    }

    @Override
    public int getTheme() {
        return R.style.SolarGreenDialogTheme;
    }

    private static class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {
        private final List<NotificationItem> list;

        public NotificationAdapter(List<NotificationItem> list) {
            this.list = list;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notification, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            NotificationItem item = list.get(position);
            holder.tvTitle.setText(item.title);
            holder.tvMessage.setText(item.message);
            holder.tvTime.setText(item.timestamp);
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        static class ViewHolder extends RecyclerView.ViewHolder {
            TextView tvTitle, tvMessage, tvTime;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                tvTitle = itemView.findViewById(R.id.tv_notif_title);
                tvMessage = itemView.findViewById(R.id.tv_notif_message);
                tvTime = itemView.findViewById(R.id.tv_notif_time);
            }
        }
    }
}